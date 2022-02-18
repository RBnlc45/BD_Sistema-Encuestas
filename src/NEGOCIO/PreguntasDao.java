
package NEGOCIO;

import DATOS.QueryBuilder;
import NEGOCIO.Objetos.Opcion;
import NEGOCIO.Objetos.Pregunta;
import NEGOCIO.Objetos.PreguntaAbierta;
import NEGOCIO.Objetos.PreguntaMultiple;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.JList;

public class PreguntasDao {
    LinkedList<Pregunta> lista_preguntas; //Cache Preguntas
    public OpcionesDao OpcionesDao;
    
    public PreguntasDao(){ //Data Acces Object
        this.lista_preguntas=new LinkedList<>();
        this.OpcionesDao=new OpcionesDao();
    }
    
    public void agregar_pregunta(Pregunta pregunta,JList opciones){
        if(lista_preguntas.size()>=pregunta.getOrden()){ 
            Pregunta eliminada=lista_preguntas.remove(pregunta.getOrden()-1);
            if(pregunta.getClass().getSimpleName().equals("PreguntaMultiple")){
                this.OpcionesDao.eliminar_opciones(eliminada);
            }
        }
        this.lista_preguntas.add(pregunta.getOrden()-1,pregunta); //Agregar pregunta de cualquier tipo a lista de preguntas
        if(pregunta.getClass().getSimpleName().equals("PreguntaMultiple")){ //Se agregan opciones solo a multiple
            this.OpcionesDao.agregar_opciones(opciones,pregunta); //Agregamos opciones de pregunta multiples opciones
        }
    } //Agrega pregunta a cache
    
    public void eliminar_pregunta(int orden, String tipo){
        if(lista_preguntas.size()>=orden){ 
            Pregunta eliminada=lista_preguntas.remove(orden-1);
            if(tipo.equals("PreguntaMultiple")){
                this.OpcionesDao.eliminar_opciones(eliminada);
            }
        }
        this.reasignar_orden(); //Reasignar orden de las preguntas para no perder secuencia
    }
    
    public void reasignar_orden(){ //Para reordenar cuando se elimina una pregunta de el medio
        for(int i=0;i<lista_preguntas.size();i++){
            lista_preguntas.get(i).setOrden(i+1);
        }
    }

    public Pregunta recuperar_pregunta(int orden){
        return lista_preguntas.get(orden-1); //Recupera la pregunta con el orden dado
    }
    
    public LinkedList<Opcion> recuperar_opciones(Pregunta pregunta){ //Recupera las opciones de una pregunta determinada
        return this.OpcionesDao.recuperar_opciones(pregunta); //Recupera las opciones de una pregunta determinada
    }
        
    public int numero_preguntas(){
        return lista_preguntas.size();
    } //Encuestador para verificar que exista almenos una pregunt aantes de guardar el cuestionario
    
    public LinkedList<Object> cargar_preguntas(int idCuestionario){ //Carga las preguntas de un cuestionario (Resolver)
        PreguntaAbierta obj=new PreguntaAbierta();
        /*Consulta--Preguntas Abiertas*/
        QueryBuilder preguntas_abiertas=new QueryBuilder();
        preguntas_abiertas.begin_select_all();
        try {preguntas_abiertas.from_table(obj,null);} 
        catch (Exception ex) {}
        preguntas_abiertas.where_condition(idCuestionario, "idCuestionario", null, true);
        preguntas_abiertas.order_by("orden");
        LinkedList<Pregunta> abiertas=preguntas_abiertas.execute(obj); //lista de preguntasabiertas
        if(abiertas!=null){
            /*Consulta--Preguntas Multiples*/
            QueryBuilder preguntas_multiples=new QueryBuilder();
            preguntas_multiples.begin_select_all();
            try {preguntas_multiples.from_table(new PreguntaMultiple(),null);} 
            catch (Exception ex) {}
            preguntas_multiples.where_condition(idCuestionario, "idCuestionario", null, true);
            preguntas_multiples.order_by("orden");
            LinkedList<Object> multiples=preguntas_multiples.execute(new PreguntaMultiple()); //Lusta de preguntas multiples
            for(Object o: abiertas){
                multiples.add(o); //Agregamos ambos tipos de pregunta a una sola lista
            }
            multiples=ordenar_preguntas(multiples); //Enviamos a ordenar por numero de pregunta la lista
            return multiples; //Devuelve la lista con objetos pregunta
        
        }else{
            return null;
        }  
    }
    
    public LinkedList<Object> ordenar_preguntas(LinkedList<Object> lista_preguntas_obj){
        LinkedList<Pregunta> cast_preguntas=(LinkedList<Pregunta>)lista_preguntas_obj.clone();//Casteo de lista de objetos auxiliar
        Collections.sort(cast_preguntas); //Modelo a seguir
        LinkedList<Object> lista_preguntas_obj_sorted =new LinkedList<>(); //Instanciamos lista que va a estar ordenada
        for(Pregunta p:cast_preguntas){ //Recorremos el caste de preguntas ordenadas
            for(Object p1:lista_preguntas_obj){  //Recorremos la lista de preguntas objeto
                if((p1.getClass().equals(p.getClass()))&&((Pregunta)p1).getIdPregunta()==p.getIdPregunta()){ //Si son la misma pregunta
                    lista_preguntas_obj_sorted.add(p1); //Agregamos a la lista de preguntas ordenadas
                    break;
                } 
            }
        }
        return lista_preguntas_obj_sorted; //Retornamos la lista de objetos pregunta ordenada
    } //Ordena la lista de objetos
    
}
