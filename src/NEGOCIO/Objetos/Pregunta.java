package NEGOCIO.Objetos;
import Anotaciones.ForeignKey;
import Anotaciones.ObjectReference;
import Anotaciones.PrimaryKey;
import Anotaciones.EntityConstructor;

public class Pregunta implements Comparable<Pregunta>{
    @PrimaryKey(AutoIncrement = true,Squema = "System",Sequence = "?_id_seq")
    private Integer idPregunta;
   
    @ForeignKey(ReferenceTo = Cuestionario.class)
    private Integer idCuestionario;
    private String enunciado;
    private int orden;
    private String obligatoria;
   
    @ObjectReference
    private Cuestionario cuestionario;//referencia del objeto cuestionario
   
    public Pregunta(Integer idPregunta, Integer idCuestionario,String enunciado, int orden,String obligatorio,Cuestionario cuestionario){
        this.idPregunta = idPregunta;
        this.enunciado = enunciado;
        this.orden = orden;
        this.idCuestionario = idCuestionario;
        this.cuestionario=cuestionario;
        this.obligatoria=obligatorio;
    }

    public Pregunta() {
       
    }

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public Integer getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(Integer idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(String obligatoria) {
        this.obligatoria = obligatoria;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }  
    
    @Override
    public int compareTo(Pregunta o) {
        if (this.orden > o.orden) {
                    return 1;
            }else {
                    return -1;
            }
    }

    @Override
    public String toString() {
        return "Pregunta{" + "idPregunta=" + idPregunta + ", idCuestionario=" + idCuestionario + ", enunciado=" + enunciado + ", orden=" + orden + ", cuestionario=" + cuestionario + '}';
    }
    
    
    
}
