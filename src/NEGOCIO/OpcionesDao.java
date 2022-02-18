
package NEGOCIO;

import DATOS.QueryBuilder;
import NEGOCIO.Objetos.Opcion;
import NEGOCIO.Objetos.Pregunta;
import NEGOCIO.Objetos.PreguntaMultiple;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JList;
import javax.swing.ListModel;

public class OpcionesDao {
    Map<Pregunta, LinkedList<Opcion>> mapa_opciones; //Cache de opciones
    int idOpcion;
    public OpcionesDao(){
        mapa_opciones=new HashMap<>(); //Mapa que contendra las opciones para todas las preguntas
    }
    public void agregar_opciones(JList opciones, Pregunta pregunta){ //Agrega opcione
        ListModel mdl=opciones.getModel(); //se obtiene el modelo de la lista de opciones
        LinkedList<Opcion> lista_opciones=new LinkedList<>(); //Instanciamos lista de opciones vacia
        for(int i=0;i<mdl.getSize();i++){  //Recorremos las opciones
            lista_opciones.add(new Opcion(null,null,(String)mdl.getElementAt(i),(PreguntaMultiple)pregunta)); //Agregamos las opciones a la lista de opciones
        }
        mapa_opciones.put(pregunta, lista_opciones); //Agregar la Pregunta y la lista de Opciones en hash map
    } //Guardar en cache opciones
    
    public LinkedList<Opcion> recuperar_opciones(Pregunta k){
        return mapa_opciones.get(k); //Recuperamos las opciones de una pregunta determinada
    }
    
    public void eliminar_opciones(Pregunta k){
        mapa_opciones.remove(k); //Eliminamos las opciones de una pregunta determinada
    }
        
    public Map<Integer, LinkedList<Opcion>> cargar_opciones(int idCuestionario, LinkedList<Object> lista_preguntas){ //Obtener las opciones de todas las preguntas de un cuestionario
        Map Cuestionario=new HashMap<Integer, LinkedList<Opcion>>(); //Instanciamos hash map que contendra todas las opciones de todas las preguntas
        LinkedList<Opcion> opciones;
        if(lista_preguntas!=null){ //S i la lista de preguntas no es nul
            for(Object o: lista_preguntas){ //Recorremos la lista de preguntas
                if(o.getClass().getSimpleName().equals("PreguntaAbierta")){ //Si es pregunta abierta 
                    continue; //Pasamos a la siguiente pregunta
                }
                PreguntaMultiple p=(PreguntaMultiple)o; //Si es multiple casteamos a clase pregunta multiple
                QueryBuilder op_query=new QueryBuilder();
                op_query.begin_select(new Opcion(), "op");
                try {
                    op_query.inner_join(new PreguntaMultiple(), new Opcion(), "idPregunta", "idPreguntaMultiple", "pm", "op");
                    op_query.where_condition(p.getIdPregunta(), "idPreguntaMultiple", "op", true);
                    op_query.order_by("op.idOpcion");
                } catch (Exception ex) {return Cuestionario;}
                    opciones=op_query.execute(new Opcion());
                    Cuestionario.put(p.getOrden(), opciones); //Agregamos las opciones a la pergunta
            }
            return Cuestionario;
        }
        return null;
    }
      
}
