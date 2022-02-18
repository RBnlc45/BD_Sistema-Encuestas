
package NEGOCIO.Objetos;

import Anotaciones.Entity;
import Anotaciones.EntityConstructor;


@Entity(Name = "PreguntaAbierta",Squema = "System")
public class PreguntaAbierta extends Pregunta{
    
    @EntityConstructor
    public PreguntaAbierta(Integer idPregunta,Integer idCuestionario,String enunciado,int orden,String obligatorio,Cuestionario cuestionario){
        super(idPregunta,idCuestionario,enunciado,orden,obligatorio,cuestionario);  
    }

    public PreguntaAbierta() {
    }
    
}

