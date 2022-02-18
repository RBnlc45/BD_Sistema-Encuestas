
package DATOS;

import DATOS.GENERICO.SQL;
import Anotaciones.Entity;
import Anotaciones.MaterializedView;
import Anotaciones.ObjectReference;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.sql.ResultSet;

public class QueryBuilder {
    private final String SELECT="SELECT ";
    private final String FROM=" FROM ";
    private final String ALL="*";
    private final String INNERJOIN=" INNER JOIN ";
    private final String LEFTJOIN=" LEFT JOIN ";
    private final String RIGHTJOIN=" RIGHT JOIN ";
    private final String FULLJOIN=" FULL JOIN ";
    private final String ORDERBY=" ORDER BY ";
    private final String WHERE=" WHERE ";
    private final String AND=" AND ";
    private final String OR=" OR ";
    private final String ON=" ON ";
    private StringBuffer query;
    public QueryBuilder() {
        query=new StringBuffer();  
    }
    public void begin_select_all(){
        query.append(SELECT).append(ALL);
    }
    public void begin_select(Object obj,String rename){//selecciona los atributos de un objeto especifico
        if(!(obj.getClass().isAnnotationPresent(Entity.class)||obj.getClass().isAnnotationPresent(MaterializedView.class))){
            return;
        } 
        query.append(SELECT);
        Field[] campos=obj.getClass().getDeclaredFields();
        int cont=0;
        for(Field f:campos){
            if(!f.isAnnotationPresent(ObjectReference.class)){
                query.append(rename).append(".").append(f.getName()).append(",");
            }
            cont=cont+1;
        }
        query.deleteCharAt(query.lastIndexOf(","));
    }
    public void from_table(Object table,String rename) throws Exception{
        String squema="",table_name="";
        if(table.getClass().isAnnotationPresent(Entity.class)){
            table_name=table.getClass().getAnnotation(Entity.class).Name();
            squema=table.getClass().getAnnotation(Entity.class).Squema();
        }
        else if(table.getClass().isAnnotationPresent(MaterializedView.class)){
            table_name=table.getClass().getAnnotation(MaterializedView.class).Name();
            squema=table.getClass().getAnnotation(MaterializedView.class).Squema();
        } 
        else{
            throw new Exception("El objeto ingresado debe ser una entidad o vista materializada de la base de datos");
        }
        if(query.indexOf(FROM)<0){query.append(FROM);}
        else{query.append(",");}
        query.append(squema).append(".").append(table_name);
        if(rename!=null) query.append(" ").append(rename);
    }
    public void where_condition(Object value,String attribute_name,String attribute_table_reference,boolean conector){//true: and, false: or
        if(query.indexOf(WHERE)<0){query.append(WHERE);}
        else if(conector){query.append(AND);}
        else if(!conector){query.append(OR);}
        if(attribute_table_reference!=null)query.append(attribute_table_reference).append(".");
        query.append(attribute_name);
        query.append("='").append(value).append("'");
    }
    public void inner_join(Object table1,Object table2,String attribute1,String attribute2,String rename1,String rename2) throws Exception{
        String squema1="",table_name1="",squema2="",table_name2="";
        if(table1.getClass().isAnnotationPresent(Entity.class)){
            table_name1=table1.getClass().getAnnotation(Entity.class).Name();
            squema1=table1.getClass().getAnnotation(Entity.class).Squema();
        }
        else if(table1.getClass().isAnnotationPresent(MaterializedView.class)){
            table_name1=table1.getClass().getAnnotation(MaterializedView.class).Name();
            squema1=table1.getClass().getAnnotation(MaterializedView.class).Squema();
        } 
        else{throw new Exception("El objeto ingresado debe ser una entidad o vista materializada de la base de datos");}
        if(table2.getClass().isAnnotationPresent(Entity.class)){
            table_name2=table2.getClass().getAnnotation(Entity.class).Name();
            squema2=table2.getClass().getAnnotation(Entity.class).Squema();
        }
        else if(table2.getClass().isAnnotationPresent(MaterializedView.class)){
            table_name2=table2.getClass().getAnnotation(MaterializedView.class).Name();
            squema2=table2.getClass().getAnnotation(MaterializedView.class).Squema();
        } 
        else{throw new Exception("El objeto ingresado debe ser una entidad o vista materializada de la base de datos");}
        query.append(FROM).append(squema1).append(".").append(table_name1).append(" ").append(rename1);
        query.append(INNERJOIN);
        query.append(squema2).append(".").append(table_name2).append(" ").append(rename2);
        query.append(ON);
        query.append(rename1).append(".").append(attribute1).append("=").append(rename2).append(".").append(attribute2);
    }
    public void order_by(String attribute_name){
        query.append(ORDERBY).append(attribute_name);
    }
    public LinkedList execute(Object to_get){
        SQL sql=SQL.getInstance();
        ResultSet result=sql.execute_query_statement(query.toString());
        LinkedList<Object> ls=sql.process_resultset_entity_objects(result, to_get);
        //if(ls==null) return new LinkedList();
        return ls;
    }
    
}
