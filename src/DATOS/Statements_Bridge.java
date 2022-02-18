
package DATOS;

import DATOS.GENERICO.SQL;
import java.util.LinkedList;

public class Statements_Bridge {//Puente de ejecución de sentencias genericas del módulo SQL
    private final SQL sql_driver;
    public Statements_Bridge() {
        sql_driver=SQL.getInstance();
    }
    public boolean transaccion_insertar(LinkedList<Object> lista){
        return sql_driver.insert_statement(lista);
    }
    public boolean sentencia_eliminar(Object obj){
        return sql_driver.delete_statement(obj);
    }
    public boolean transaccion_actualizar(LinkedList<Object> objs){
        return sql_driver.update_statement(objs);
    }
    /*Selecciones*/
    public LinkedList seleccionar_todos(Object obj){
        return sql_driver.select_statement_all(obj);
    }
    public LinkedList seleccionar_especifico(Object obj){
        return sql_driver.select_statement(obj);
    }
    /*Ejecuciones */
    public void ejecutar_procedimiento(Object obj){
        sql_driver.execute_procedure(obj);
    }

    /*Usuarios*/
    public void cambio_usuario(String id,String password){
        sql_driver.setCredentials(id, password);
    }
    public void cerrar_conexion(){
        sql_driver.close_connection();
    }
    
    
}
