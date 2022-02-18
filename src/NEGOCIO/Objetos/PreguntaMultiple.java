
package NEGOCIO.Objetos;

import Anotaciones.Entity;
import Anotaciones.EntityConstructor;

@Entity(Name = "PreguntaMultiple",Squema = "System")
public class PreguntaMultiple extends Pregunta{

    private String tipo;
    
    @EntityConstructor
    public PreguntaMultiple(Integer idPregunta,Integer idCuestionario,String enunciado, int orden, String obligatorio,String tipo,Cuestionario cuestionario){
        super(idPregunta, idCuestionario,enunciado,orden,obligatorio,cuestionario);
        this.tipo=tipo;
    }

    public PreguntaMultiple() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
       
    
}
