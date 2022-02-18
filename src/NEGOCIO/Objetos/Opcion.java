
package NEGOCIO.Objetos;
import Anotaciones.Entity;
import Anotaciones.ForeignKey;
import Anotaciones.PrimaryKey;
import Anotaciones.ObjectReference;
import Anotaciones.EntityConstructor;

@Entity(Name = "Opcion",Squema = "System")
public class Opcion {
   @PrimaryKey(AutoIncrement = true,Sequence = "Opcion_id_seq",Squema = "System")
   private Integer idOpcion;
   
   @ForeignKey(ReferenceTo = PreguntaMultiple.class)
   private Integer idPreguntaMultiple;
   
   private String contenido;
   
   @ObjectReference
   private PreguntaMultiple pregunta;//Referencia de la pregunta a la que pertenece la opción
   
   @EntityConstructor
   public Opcion(Integer idOpcion,Integer idPreguntaMultiple, String contenido,PreguntaMultiple pregunta) {
        this.idOpcion =idOpcion;
        this.contenido = contenido;
        this.idPreguntaMultiple=idPreguntaMultiple;
        this.pregunta=pregunta;
    }

    public Opcion() {
    }
    

    public int getIdPregunta() {
        return idPreguntaMultiple;
    }
    public void setIdPregunta(int idPregunta) {
        this.idPreguntaMultiple = idPregunta;
    }
    public int getIdOpcion() {
        return idOpcion;
    }
    public void setIdOpcion(int idOpcion) {
        this.idOpcion = idOpcion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
   
}
