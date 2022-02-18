package NEGOCIO;

import DATOS.QueryBuilder;
import DATOS.Statements_Bridge;
import NEGOCIO.Objetos.Opcion;
import NEGOCIO.Objetos.Responde;
import NEGOCIO.Objetos.Selecciona;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class RespuestasDao {
    Map<Integer, LinkedList<Object>> mapa_respuestas;
    Statements_Bridge statement;
    int idPregunta;
    public RespuestasDao(){ //Data Acces Object
        statement=new Statements_Bridge();
        mapa_respuestas=new HashMap<>(); //Lista con las respuestas de un usuario
    }
        
    public void actualizar_seleccion(Object responde, LinkedList<Opcion> lista_opciones, String tipo, int orden){ //Borramos seleccion anterior y aniadimos nueva
        LinkedList<Object> lista_respuestas=new LinkedList<>();
        String clase=responde.getClass().getSimpleName(); //Clase de la respuesta
        if(tipo.equals("Multiple")){ //Seleccion Multiple
            lista_respuestas=mapa_respuestas.get(orden);
            if(lista_respuestas!=null){ //Si la lista de respuestas no es null
                Selecciona seleccion=(Selecciona)responde;
                for(int i=0;i<lista_respuestas.size();i++){ //Recorremos la lista de opciones
                    Selecciona aux = (Selecciona)lista_respuestas.get(i); //Recorremos seleccion por seleccion
                    if(Objects.equals(seleccion.getIdOpcion(), aux.getIdOpcion())){ //Si la opcion ya habia sido seleccionada
                        lista_respuestas.remove(i); //Quitamos la seleccion
                        mapa_respuestas.remove(orden); //Eliminamos lista de respuestas
                        mapa_respuestas.put(orden, lista_respuestas); //Agregamos lista de respuestas quitada la opcion
                        return;
                    }
                }
            }else{
                lista_respuestas=new LinkedList<>();
            }
        }
        lista_respuestas.add(responde);
        mapa_respuestas.remove(orden); //Eliminamos lista de respuestas
        mapa_respuestas.put(orden, lista_respuestas); //Agregamos lista de respuestas quitada la opcion
    } //Borramos seleccion anterior y aniadimos nueva
       
    public LinkedList<Object> recuperar_selecciones(int orden){ //Del cache recuperar las respuestas de una pregunta       
        return mapa_respuestas.get(orden);
    }
    
    public void guardar_respuestas(){ //Guardar las respuestas del usuario
        LinkedList<Object> lista_respuestas=new LinkedList();//lista con las respuestas del cuestionario
        for(Object k: mapa_respuestas.keySet()){
            lista_respuestas.addAll(mapa_respuestas.get(k)); //Obtengo respuesta para pregunta dada
        }
        if(statement.transaccion_insertar(lista_respuestas)==false) throw new Error("No tiene los permisos suficientes para realizar esta acción");//inserción de las respuestas en la base de datos
        this.mapa_respuestas.clear();//se limpia el mapa
    } //Guardar las respuestas del usuario
    
    public LinkedList<Selecciona> cargar_respuestas_multiple(String cedula, int idOpcion){ //Devuelve lista de respuestas multiples
        QueryBuilder cargar_respuestas=new QueryBuilder();
        cargar_respuestas.begin_select_all();
        try {cargar_respuestas.from_table(new Selecciona(), "p");} catch (Exception ex) {return new LinkedList<>();}
        cargar_respuestas.where_condition(idOpcion,"idOpcion","p",true);
        cargar_respuestas.where_condition(cedula,"cedula","p",true);
        LinkedList p=cargar_respuestas.execute(new Selecciona());
        return p;
    } //Devuelve lista de respuestas multiples
    
    public LinkedList<Object> cargar_respuestas_abierta(String cedula, int idPregunta){ //Devuelve lista de respuestas abiertas
        QueryBuilder cargar_respuestas=new QueryBuilder();
        try {
            cargar_respuestas.begin_select_all();
            cargar_respuestas.from_table(new Responde(), "p");
            cargar_respuestas.where_condition(idPregunta,"idPreguntaAbierta","p",true);
            cargar_respuestas.where_condition(cedula,"cedula","p",true);
            LinkedList<Object> p=cargar_respuestas.execute(new Responde());
            return p;
        } catch (Exception ex) {
           return new LinkedList<>();
        } 
    }
}
