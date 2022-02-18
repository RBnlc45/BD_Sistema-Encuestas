package DATOS.GENERICO;

import Anotaciones.Entity;
import Anotaciones.EntityConstructor;
import Anotaciones.ForeignKey;
import Anotaciones.Input;
import Anotaciones.ObjectReference;
import Anotaciones.PrimaryKey;
import Anotaciones.Procedure;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;

public final class SQL{
    private static SQL instance;
    /*Singleton*/
    public static SQL getInstance() {
        if (instance == null) {instance = new SQL();}
        return instance;
    }
    private Connection connection;
    private String user;
    private String pass;
       
    public SQL(){}

    public void setCredentials(String user,String pass) {//Setea las credenciales para iniciar sesión
        if(connection!=null){//si existe una conexión activa
            try {connection.close();} //se cierra la conexión
            catch (SQLException ex) {}
        }
        if(user.equalsIgnoreCase("SYSTEM")) this.user=user;//se ingresa como system
        else this.user="USUARIO_"+user;//se ingresa como usuario
        this.pass=pass;
        begin_connection();//ingresa en la base de datos
    }
    public void begin_connection(){//generar conexión con la base de datos
        try{
            String url="jdbc:oracle:thin:@localhost:1521:XE";//driver oracle
            connection=DriverManager.getConnection(url, this.user, this.pass);//instancia de la conexión
            connection.setAutoCommit(false);  //desactivación del autocommit
        }
        catch(SQLException e){}       
    }
    public void close_connection(){//Cierre de conexión
        if(connection!=null) {//si existe una conexión instanciada
         try {connection.close();}//cierre de conexión
         catch (SQLException ex) {}
        }
    }
    
    public void execute_procedure(Object procedure){//Función para ejecutar procedimientos almacenados
        if (!procedure.getClass().isAnnotationPresent(Procedure.class)) return;//solo se permiten objetos etiquetados como procedure
        /*Preparación de la sentencia de llamada al procedimiento*/
        String squema,procedure_name;
        squema=procedure.getClass().getAnnotation(Procedure.class).Squema();//nombre del esquema del procedimiento
        procedure_name=procedure.getClass().getAnnotation(Procedure.class).Name();//nombre del procedimiento
        String ex_stmt="call "+squema+"."+procedure_name+"(";//construcción del llamado al procedimiento
        Field[] campos=procedure.getClass().getDeclaredFields();//se obtienen los atributos del objecto del procedimiento
        LinkedList<Field> campos_output=new LinkedList<>();//lista que almacenará los campos que son salidas del procedimiento
        int num_campos=campos.length;//número total de atributos del objeto de tipo procedimiento
        for (int i=0;i<num_campos;i++){//bucle de llenado de la sentencia de llamada del procedimiento
            ex_stmt=ex_stmt+" ?";//se agregan los ? para generar una llamada(Call) preparada
            if(i+1<num_campos) ex_stmt=ex_stmt+",";//se agregan comas mientras no se llegue al ultimo atributo
        }
        ex_stmt=ex_stmt+" )";//cierre de la sentencia de llamado al procedimiento
        /*Ejecución de la llamada al procedimiento*/
        try {
            CallableStatement stmt_prepared=connection.prepareCall(ex_stmt);//se genera una llamada preparada
            int i=1;//contador para recorrer los parámetros anónimos ? de la sentencia preparada
            for (Field f:campos){//se recorren todos los atributos del objeto de tipo procedimiento
                if(f.isAnnotationPresent(Input.class)){//si el atributo es input
                    f.setAccessible(true);//desprivatización
                    stmt_prepared.setObject(i, f.get(procedure));//se coloca el parámetro en la sentencia   
                }
                else{//si el atributo es output
                   campos_output.add(f);//se agrega el atributo a la lista de atributos output
                   stmt_prepared.registerOutParameter(i,get_sql_data_type(f.getType()));//se identifica el parámetro de la sentencia como parámetro de salida
                }
                i++;//aumento de contador
            }
            stmt_prepared.executeUpdate();//se ejecuta la sentencia generada
            //Colocar los parámetros resultantes
            int cont=campos.length-campos_output.size()+1;//Ubicación del inicio de los atributos output en la sentencia 
            for(Field f:campos_output){//recorrer la lista de atributos output
                f.setAccessible(true);//desprivatización
                f.set(procedure,stmt_prepared.getObject(cont));//se coloca el resultado correspondiente al parámetro obtenido de la ejecución del procedimiento
                cont=cont+1;//se aumenta el contador
            }
            stmt_prepared.close();//se cierra la llamada preparada
        } catch (IllegalAccessException | IllegalArgumentException | SQLException ex) {//errores
        }
    }
    private int get_sql_data_type(Class c){//devuelve el tipo de dato de sql para un objeto enviado
        if(c==String.class){
            return java.sql.Types.VARCHAR;
        }
        else if(c==Integer.class){
            return java.sql.Types.INTEGER;
        }
        else if(c==Float.class){
            return java.sql.Types.FLOAT;
        }
        else if (c==Timestamp.class){
            return java.sql.Types.TIMESTAMP;
        }
        else{
            /*Por implementar más tipos de datos dependiendo del dominio*/
            return -1;
        }
    }
    public ResultSet execute_query_statement(String st) {//Se permite la ejecución directa consultas a la base de datos
        if(connection==null) begin_connection();//se inicia sesión si la referencia de conexión es nula
        try {
            return connection.createStatement().executeQuery(st);//se crea y ejecuta la sentencia enviada como parámetro
        } 
        catch (SQLException ex) {return null;}//falla en la ejecución de la sentencia
    }
    public boolean execute_statement(String st){//se permite la ejecución de sentencias sql
        try {
            Boolean result=connection.createStatement().execute(st);//se crea y ejecuta la sentencia que devuelve un true si se ejecutó con éxito
            connection.commit();//se realiza un commit, la sentencia ejecutada puede ser de tipo grant, drop, etc
            return result;//se devuelve el resultado
        } catch (SQLException ex) {//la ejecución falla
            if(connection!=null){//la conexión no es nula
                try {connection.rollback();} //rollback en caso de errores
                catch (SQLException ex1) {}
            }
            return false;
        }
    }
    private Integer get_next_sequence_value(String squema,String name){//ejecuta una secuencia con sus respectivo nombre y esquema
        String stmt="select "+squema+"."+name+".nextval from DUAL";//se crea la sentencia para obtener el siguiente valor de la secuencia.
        int nextID=0;//inicialización del id
        try{
            PreparedStatement ps = connection.prepareStatement(stmt);//se crea una sentencia preparada
            ResultSet rs = ps.executeQuery();//se obtiene el resultado de la consulta del nuevo valor
            if(rs.next()){
                nextID= rs.getInt(1);
            }//se lee el resultado de la consulta
            rs.close();
        }
        catch(SQLException e){return null;}//en caso de errores
        return nextID;//devuelve el id leido
    }
    private LinkedList<Field> get_constraint_obj(Object obj,Class constraint){//Permite obtener los atributos que son constraint(PK o FK) de un objeto
        LinkedList<Field> atributos=get_atributtes(obj,true);//atributos del objeto sin los objetos referenciales de los fk
        LinkedList<Field> constraints=new LinkedList();//lista con los atributos que son constraint
        for(Field f: atributos){//recorrer los atributos
            if(f.isAnnotationPresent(constraint)) constraints.add(f);//se agrega el atributo que es constraint 
        }
        return constraints;//se devuelven los atributos constraint
    }
    private LinkedList<Field> get_atributtes(Object object,boolean with_references){//devuelve una lista con todos los atributos de un objecto
        /*with_references: true: agrega atributos si no son un objeto referencial de los fk*/
        /*with_references: false: agrega atributos si son un objeto referencial de los fk*/
        LinkedList<Field> attributes_ls=new LinkedList<>();//List of lists with the attributes and their types.
        for (Field p:object.getClass().getSuperclass().getDeclaredFields()){//recorrer array con los atributos de la clase padre
            if(!p.isAnnotationPresent(ObjectReference.class)==with_references) attributes_ls.add(p);//agrega el atributo a la lista de atributos 
        }
        for (Field p:object.getClass().getDeclaredFields()){//recorrer array con los attributos de la clase del objeto
            if(!p.isAnnotationPresent(ObjectReference.class)==with_references) attributes_ls.add(p);//agrega el campo a la lista de campos
        }
        return attributes_ls;//retorna la lista de atributos solicitado
    }
    private LinkedList<Object> get_referenced_objects(Object object,LinkedList<Field> fks) throws IllegalArgumentException, IllegalAccessException{
        LinkedList<Object> list=new LinkedList();//lista de objetos a utilizar
        LinkedList<Field> fields=get_atributtes(object, false);//atributos del objeto con los objetos referenciales de los fk
        for(Field f: fks){//se recorren los atributos que son ids de fks
            String nombre_obj_fk=f.getAnnotation(ForeignKey.class).ReferenceTo().getSimpleName();//se coloca el nombre de la clase a la que se referencia con el fk
            for(Field attribute:fields){//se recorren los atributos que son objetos referenciales de fk
                if(attribute.getType().getSimpleName().equals(nombre_obj_fk)){//Se ubica el objeto referencial a través del nombre asignado en el fk
                    attribute.setAccessible(true);//desprivatización
                    list.add(attribute.get(object));//se agrega el objeto a la lista de objetos referenciales
                    fields.remove(attribute);//se elimina el atributo encontrado
                    break;
                }
            }
        }
        return list;//devuelve la lista de objetos  
    }
    private PreparedStatement get_insert_statement(Object obj) throws SQLException{//devuelve un prepared statement de insert enviando un objeto
        if(!obj.getClass().isAnnotationPresent(Entity.class)) throw new SQLException("El objeto ingresado no pertenece a una entidad");//se verifica que el objeto enviado sea una entidad
        String table_name=obj.getClass().getAnnotation(Entity.class).Name();//nombre de la tabla
        String squema=obj.getClass().getAnnotation(Entity.class).Squema();//esquema de la tabla
        String str="INSERT INTO "+squema+"."+table_name;//consulta generada
        String field_names=" (";//atributos que se van a insertar
        String values_campos=" VALUES (";//valores de los atributos
        LinkedList<Field> attributes=get_atributtes(obj,true);//atributos del objeto sin los objetos referenciales
        Iterator iterador_ls=attributes.iterator();//iterador de atributos
        while(iterador_ls.hasNext()){//se recorren todos los atributos
            Field campo=(Field) iterador_ls.next();//variable con el atributo recuperado
            field_names=field_names+campo.getName();//concatena los nombres de los atributos a insertar
            values_campos=values_campos+" ? ";//se agregan las referencias anónimas de los atributos a ingresar en la sentencia
            if(iterador_ls.hasNext()){//si no se llega al último atributo
                values_campos=values_campos+", ";//se agrega coma
                field_names=field_names+", ";//se agrega coma
            }
        }
        values_campos=values_campos+")";//ciere 
        field_names=field_names+")";//cierre
        str=str+field_names+values_campos;//Sentencia creada
        PreparedStatement stmt=null;//instancia de una sentencia preparada
        try{
            stmt=connection.prepareStatement(str);
            int cont=1;//contador para recorrer las referencia anónimas ?
            for(Field f: attributes){//se recorre la lista de atributos del objeto
                f.setAccessible(true);//se desprivatiza el atributo
                stmt.setObject(cont, f.get(obj));//se agrega el objeto a la sentencia creada
                cont=cont+1;//aumenta el contador
            }
        }
        catch(IllegalAccessException e){
            if (stmt!= null) stmt.close();//cierre de la sentencia
        }
        return stmt;//devuelve la sentencia
    }
    private void primary_keys_resolution(Object obj) throws Exception{//resuelven los ids autogenerados en caso de que el objeto los tenga
        LinkedList<Field> pks=get_constraint_obj(obj,PrimaryKey.class);//se obtienen los atributos que son PKs
        for(Field pk:pks){//recorre la lista con los primary key del objeto
            if (pk.getAnnotation(PrimaryKey.class).AutoIncrement()){//si el PK es autoincrementable
                String sequence,squema;
                pk.setAccessible(true);//se desprivatiza el atributo
                squema=pk.getAnnotation(PrimaryKey.class).Squema();//se obtiene el esquema de la secuencia para el Pk
                sequence=pk.getAnnotation(PrimaryKey.class).Sequence();//se obtienen el nombre de la secuencia para el PK
                //Convención, para claves autogeneradas de entidades que tienen generalización se ocupa el nombre de cada entidad generalizada para la secuencia
                if(!obj.getClass().getSuperclass().equals(Object.class)) sequence=sequence.replace("?", obj.getClass().getSimpleName());//se usa el nombre de la entidad cuando se tiene generalización
                Integer value=get_next_sequence_value(squema,sequence);//se consulta el siguiente id para la tupla
                if (value==null){ throw new Exception();}//hubo un error al obtener el siguiente id
                pk.set(obj,value);//se coloca el pk generado en el objeto
            }
        }
    }
    private void foreign_keys_resolution(Object obj) throws IllegalAccessException{
        //Se resuelven las claves foraneas en caso de tenerlas
        LinkedList<Field> fks=get_constraint_obj(obj,ForeignKey.class);//se obtienen los atributos que son fk
        LinkedList<Object> referenced_objs=get_referenced_objects(obj,fks);//Lista de objetos referenciados por los fk
        if(referenced_objs.size()>0){//Si se tienen objetos referenciados a los fks
            for(int i=0;i<fks.size();i++){//Se recorren las listas de fks y objetos referenciados  
                Object referenced_object=referenced_objs.get(i);//objeto referenciado
                if (referenced_object==null) continue;//El objeto es nulo, se continua con la iteracion
                LinkedList<Field> pk_key=get_constraint_obj(referenced_object,PrimaryKey.class);//se obtienen las claves primarias del objeto referencial del fk
                if (pk_key.size()==1){/*Solo aplicamos el manejo cuando se tiene un solo atributo como pk*/
                    pk_key.get(0).setAccessible(true);//se desprivatiza el atributo primary key del objeto referencial
                    fks.get(i).setAccessible(true);//se desprivatiza el atributo que es foreign key
                    fks.get(i).set(obj, pk_key.get(0).get(referenced_object));//se actualiza el foreign key recuperando el primary key del objeto referencial
                }    
            }
        }
    }
    
    
    public boolean insert_statement(LinkedList<Object> objs){//realiza un transacción de inserts
        //begin_connection();//inicializa la conexión con la base de datos
        for(Object o:objs){//se ejecutarán una a una las inserciones de los objetos, si una falla se realiza un rollback
            try {
                //Se resuelven los ids autogenerados del objeto en caso que los tenga
                primary_keys_resolution(o);//resolución de claves primarias
                foreign_keys_resolution(o);//resolución de claves foráneas
                PreparedStatement stmt=get_insert_statement(o);//sentencia preparada
                stmt.executeUpdate();//se ejecuta la sentencia
                stmt.close();//se cierra la sentencia
            } catch (Exception ex) {//Problemas en el proceso
                if(connection==null) return false;
                try {connection.rollback();}//rollback
                catch (SQLException ex1) {}
                return false;//finalizo con error
            }
        }
        try {connection.commit();}//commit si se ejecutaron todas las inserciones 
        catch (SQLException ex) {return false;}//Se produjo un error
        return true;//Todas las sentencias ingresadas
    }
    
    private PreparedStatement get_update_statement(Object obj) throws SQLException{
        if(!obj.getClass().isAnnotationPresent(Entity.class)) throw new SQLException("El objeto que ingresado no pertenece a una entidad");//se verifica que el objeto sea una entidad
        String table_name=obj.getClass().getAnnotation(Entity.class).Name();//nombre de la tabla
        String squema=obj.getClass().getAnnotation(Entity.class).Squema();//nombre del esquema
        LinkedList<Field> attributes=get_atributtes(obj,true);//atributos del objeto sin los objetos referenciales de los fk
        LinkedList<Field> pk_Attribute=get_constraint_obj(obj, PrimaryKey.class);//Lista de atributos que son clave primaria del objeto
        LinkedList<Field> fk_Attribute=get_constraint_obj(obj, ForeignKey.class);//lista de atributos que son clave foránea
        String str="UPDATE "+squema+"."+table_name+" SET ";//sentencia de update
        String condition=" WHERE ";//condiciones para la actualización
        Iterator iterador_ls=attributes.iterator();//iterador de atributos del objeto
        int cont=0;//contador de apariciones de pk
        LinkedList<Field> atributos_actualizar=new LinkedList();//lista de atributos que se actualizaran
        while(iterador_ls.hasNext()){//se recorre la lista de atributos del objeto
            Field campo=(Field) iterador_ls.next();//atributo actual
            if(!pk_Attribute.contains(campo) && !fk_Attribute.contains(campo)){//No se actualizan los primary key y foreign key
                str=str+campo.getName()+"= ? ";//se coloca la referencia anónima del atributo que se agregará a la sentencia
                if(iterador_ls.hasNext()){ str=str+", "; }//se agregan comas mientras no se llegue al último atributo
                atributos_actualizar.add(campo);//se agrega el atributo a la lista de atributos actualizables
            }
            else if(pk_Attribute.contains(campo)){//se insertan la condición de actualización en base al primary key
                condition=condition+campo.getName()+"= ?";//Se agrega el nombre del primary key
                cont++;
                if (cont<pk_Attribute.size()) condition=condition+" and ";//si se tienen mas primary keys se concatenan con conectores and
            }
        }
        str=str+condition;//se completa al sentencia update
        PreparedStatement c=connection.prepareStatement(str);//se crea un prepared statement
        cont=1;//contador para recorrrer las referencias anónimas de los atributos que se ingresan en la sentencia
        try{
            for(Field f:atributos_actualizar){//se recorre la lista de atributos actualizables
                f.setAccessible(true);//se desprivatiza el atributo
                c.setObject(cont, f.get(obj));//se coloca el valor del atributo en la sentencia
                cont++;
            }
            for(Field f:pk_Attribute){//se recorre la lista de atributos pk
                f.setAccessible(true);//se desprivatiza el atributo
                c.setObject(cont, f.get(obj));//se coloca el valor del atributo en la sentencia
                cont++;
            } 
        }
        catch(IllegalAccessException | IllegalArgumentException | SQLException e){if (c!= null) c.close();}
        return c;
    }
    
    
    public boolean update_statement(LinkedList<Object> objs){//realiza un transacción de updates
        for(Object o:objs){//se ejecutarán una a una las actualizaciones de los objetos, si una falla se realiza un rollback
            try {
                PreparedStatement stmt=get_update_statement(o);//sentencia preparada para cada objeto
                stmt.execute();//se ejecuta la sentencia
                stmt.close();//se cierra la sentencia
            } catch (SQLException ex) {//Problemas
                try {connection.rollback();}//rollback
                catch (SQLException ex1) {}
                return false;//finalizó con error
            }
        }
        try {connection.commit();}//commit si se ejecutaron todas las sentencias correctamente
        catch (SQLException ex) {return false;}//Se produjo un error
        return true;//Todas las sentencias ingresadas
    }
    
    public boolean delete_statement(Object obj){
        if(!obj.getClass().isAnnotationPresent(Entity.class)) return false;//se verifica que el objeto sea una entidad
        String table_name=obj.getClass().getAnnotation(Entity.class).Name();//nombre de la tabla
        String squema=obj.getClass().getAnnotation(Entity.class).Squema();//nombre del esquema
        LinkedList<Field> pk_Attribute=get_constraint_obj(obj, PrimaryKey.class);//Lista de atributos que son clave primaria del objeto
        String str="DELETE FROM "+squema+"."+table_name+" WHERE ";//sentencia update
        Iterator iterador_ls=pk_Attribute.iterator();//iterador de atributos primary key
        /*Condiciones para ejecutar la sentencia de borrado*/
        while(iterador_ls.hasNext()){//Se recorre la lista de primary keys
            Field a=(Field)iterador_ls.next();//atributo actual
            str=str+a.getName()+" = ?";//se coloca la referencia anónima del id del atributo a ingresar en la sentencia
            if(iterador_ls.hasNext()) str=str+" and ";//si se tienen más de un primary key se concatenan las condiciones con un conector and
        }
        try{
            PreparedStatement c=connection.prepareStatement(str);//se genera la sentencia preparada
            int cont=1;
            for(Field f:pk_Attribute){//se recorre la lista de atributos primary key
                f.setAccessible(true);//Se desprivatiza el atributo
                c.setObject(cont, f.get(obj));//Se agrega el valor a la sentencia
                cont++;
            }
            c.execute();//se ejecuta la sentencia
            c.close();//se cierra la sentencia
            connection.commit();//se realiza commit
            return true;//ejecución éxitosa
        }
        catch(Exception e){
            try {connection.rollback();} //rollback en caso de errores
            catch (SQLException ex) {}
            return false;
        }
    }  
    
    public LinkedList select_statement(Object obj){
        String squema,table_name;
        if(obj.getClass().isAnnotationPresent(Entity.class)){//cuando el objeto es una entidad
            table_name=obj.getClass().getAnnotation(Entity.class).Name();//nombre de la tabla
            squema=obj.getClass().getAnnotation(Entity.class).Squema();//nombre del esquema
        }
        else return null;
        LinkedList<Field> pks_obj=get_constraint_obj(obj,PrimaryKey.class);//lista de primary keys del objeto
        Iterator iterador_pks=pks_obj.iterator();
        String str="SELECT * FROM "+squema+"."+table_name;//sentencia select
        String condicion=" WHERE ";//condiciones del select
        //Se recorre la lista de pks y se colocan las condiciones dadas por los pks
        while(iterador_pks.hasNext()){
            Field value=(Field) iterador_pks.next();//atributo actual
            condicion=condicion+value.getName()+"= ? ";//se coloca la referencia anónima para el valor que se colocará en la sentencia
            if(iterador_pks.hasNext()){
                condicion=condicion+"and ";//si existen más de una primary key se concatenan las condiciones con un conector and
            }
        }
        str=str+condicion;//Sentencia completa
        try{
            PreparedStatement c=connection.prepareStatement(str);//sentencia preparada
            for(Field pk:pks_obj){//Se colocan los valores de la condición
                pk.setAccessible(true);//se desprivatiza el atributo pk
                c.setObject(1, pk.get(obj));//se agrega el atributo a la sentencia
            }
            ResultSet result=c.executeQuery();//se ejecuta la sentencia
            return this.process_resultset_entity_objects(result, obj);//se procesa el resultado de la sentencia y se devuelve
        }
        catch(IllegalAccessException | IllegalArgumentException | SQLException e){
            return null;//en caso de errores
        }
    }
    public LinkedList select_statement_all(Object obj){//seleciona todos los elementos de una tabla
       if(!(obj.getClass().isAnnotationPresent(Entity.class))) return null;//si el objeto es una entidad
       String table_name=obj.getClass().getAnnotation(Entity.class).Name();//nombre de la tabla
       String squema=obj.getClass().getAnnotation(Entity.class).Squema();//nombre del esquema
       LinkedList<Object> ls_objs=null;//lista de objetos 
       String stmt="Select * FROM "+squema+"."+table_name;//sentencia de selección
        try {
            //begin_connection();
            ls_objs=process_resultset_entity_objects(connection.createStatement().executeQuery(stmt),obj);//se ejecutan la sentencia y procesa el resultado
        } catch (SQLException ex) {}//en caso de errores
        return ls_objs;
    }
    public LinkedList<Object> process_resultset_entity_objects(ResultSet r,Object obj){//procesa un result set y devuelve una lista de objetos de tipo obj
        if(r==null) return null;//si el result set no es nulo
        try {
            Constructor constructor = null;//constructor del objeto que se va a recuperar
            Constructor[] constructores=obj.getClass().getDeclaredConstructors();//constructores del objeto
            for (Constructor c:constructores){
                if (c.isAnnotationPresent(EntityConstructor.class)) {//se selecciona el constructor anotado para el objeto
                    c.setAccessible(true);//se desprivatiza el constructor
                    constructor=c;//se copia la instancia
                    break;
                }
            }
            int nAtributes=constructor.getParameterCount();//número de atributos del constructor del objeto
            Object[] values=new Object[nAtributes];//objetos con los valores a insertar en un nuevo objeto
            LinkedList<Object> list=new LinkedList<>();//lista con los objetos recuperados
            try {
                while(r.next()){
                    for(int i=1;i<=nAtributes;i++){
                        try{
                           values[i-1]=r.getObject(i);//se obtiene el objeto recuperado de la base
                           //En caso de tener BigDecimal 
                           if (values[i-1].getClass().getSimpleName().equals("BigDecimal")){//transformacion de bigdecimal
                               values[i-1]=((BigDecimal)values[i-1]).intValue();
                            }
                        }
                        catch(SQLException e){//Si el objeto tiene un objeto referenciado
                            values[i-1]=null;//instancia nula en caso de tener un objeto referencial
                        }
                    }
                    list.add(constructor.newInstance(values));//se genera un nuevo objeto y se agrega a la lista
                }
                return list;
            }
            catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException | SQLException ex) {return null;}
        } 
        catch (SecurityException ex) { return null;}
    }
}
