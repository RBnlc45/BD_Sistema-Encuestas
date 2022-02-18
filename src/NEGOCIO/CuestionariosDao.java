
package NEGOCIO;

import DATOS.QueryBuilder;
import DATOS.Statements_Bridge;
import NEGOCIO.Objetos.Cuestionario;
import NEGOCIO.Objetos.Opcion;
import NEGOCIO.Objetos.RestriccionCreacionCuestionarios;
import NEGOCIO.Objetos.RestriccionCuestionarioResuelto;
import NEGOCIO.Objetos.Usuario;
import NEGOCIO.Objetos.Vista_CuestionarioResuelto;
import java.util.LinkedList;
import oracle.sql.TIMESTAMP;

public class CuestionariosDao {
    
    private final Statements_Bridge statements;

    public CuestionariosDao(){
        statements=new Statements_Bridge();
    }
    
    public String[] guardar_cuestionario(Cuestionario cuestionario,PreguntasDao preguntas){
         //Se crea la lista de objetos
        LinkedList<Object> objs=new LinkedList<>();
        objs.add(cuestionario);//Cuestionario agregado
        objs.addAll(1, preguntas.lista_preguntas);//Se agregan todas las preguntas del cuestionario
        for(LinkedList<Opcion> ls: preguntas.OpcionesDao.mapa_opciones.values()){//se recorren las opciones de las preguntas
            for(Opcion op:ls){//Se agregan las opciones a la lista de objetos
                objs.add(op);
            }
        }
        boolean s=statements.transaccion_insertar(objs);//Se manda a ejecutar la transacción
        if(s==true){
            preguntas.lista_preguntas.clear(); //Se limpia la lista de preguntas
            preguntas.OpcionesDao.mapa_opciones.clear();
            return new String[]{"Informacion","Cuestionario agregado correctamente"};
        }else{
            return new String[]{"Error","No tiene los permisos suficientes para realizar esta acción"};
        }
    }
       
    public String[] eliminar_cuestionario(int idCuestionario, TIMESTAMP fecha, String titulo, String Descripcion, String comentario, String cedula){
        boolean s = statements.sentencia_eliminar(new Cuestionario(idCuestionario,fecha,titulo,Descripcion,comentario,cedula)); //Envia la sentencia eliminar con el objeto cuestionario deseado
        if(s==true){
            return new String[]{"Informacion","Cuestionario eliminado correctamente"};
        }else{
            return new String[]{"Error","No tiene los permisos suficientes para realizar esta acción"};
        }
    }
   
    public LinkedList<Cuestionario> cargar_cuestionarios_disponibles(){ 
        return statements.seleccionar_todos(new Cuestionario()); //Recupera todos los cuestionarios disponibles
    } //Cuestionarios disponibles (Encuestado)
    
    public LinkedList<Cuestionario> cargar_cuestionarios_resueltos(String cedula){ //Cuestionarios resueltos (Encuestado)
        QueryBuilder cuestionarios_resueltos=new QueryBuilder();
        cuestionarios_resueltos.begin_select(new Cuestionario(),"c");
        try {cuestionarios_resueltos.from_table(new Vista_CuestionarioResuelto(), "c");} 
        catch (Exception ex) {return new LinkedList();}
        cuestionarios_resueltos.where_condition(cedula, "cedula", "c", true);
        return cuestionarios_resueltos.execute(new Cuestionario()); //Recupera los cuestionarios resueltos por un encuestado
    } //Cuestionarios resueltos de un encuestado (Encuestado)
        
    public LinkedList<Cuestionario> cargar_cuestionarios_encuestador(String cedula){ //Cuestionarios creados (Encuestador)
        /*Consulta--Preguntas Multiples*/
        QueryBuilder cuestionarios=new QueryBuilder();
        cuestionarios.begin_select_all();
        try {cuestionarios.from_table(new Cuestionario(),null);} 
        catch (Exception ex) {}
        cuestionarios.where_condition(cedula, "cedula", null, true);
        cuestionarios.order_by("idcuestionario");
        return cuestionarios.execute(new Cuestionario()); //Recupera todos los cuestionarios creados por un encuestador
    } //Cuestionarios creados (Encuestador)
    
    public LinkedList<Usuario> cargar_usuarios_cuestionario(int idCuestionario){ //Usuarios que han resuelto un cuestionario dado (Encuestador)
        QueryBuilder usuarios=new QueryBuilder();
        usuarios.begin_select(new Usuario(),"c");
        try {usuarios.from_table(new Vista_CuestionarioResuelto(), "c");} 
        catch (Exception ex) {return new LinkedList();}
        usuarios.where_condition(idCuestionario, "idCuestionario", "c", true);
        return usuarios.execute(new Usuario()); 
    } //Usuarios resuelto un cuestionario (Encuestador)
    
    public boolean restriccion_cuestionario_resuelto(String cedula, int idCuestionario){ //Comprueba si aun no resuelve un cuestionario determinado
        RestriccionCuestionarioResuelto rcr=new RestriccionCuestionarioResuelto(cedula,idCuestionario);
        statements.ejecutar_procedimiento(rcr);
        String cumple=rcr.getResuelto();
        if(cumple.equals("T")) return false; //Puede resolver
        else return true;   
    } // Resolver 1 cuestionario

    public boolean restriccion_cuestionarios_creados(String cedula){ //Comprueba la restriccion del limite de creacion de 10 cuestionarios
        RestriccionCreacionCuestionarios rcc=new RestriccionCreacionCuestionarios(cedula);
        statements.ejecutar_procedimiento(rcc);
        String cumple=rcc.getCumple();
        if(cumple.equals("T")) return true;
        else return false;
    }//Crear 10 cuestionarios
            
}
