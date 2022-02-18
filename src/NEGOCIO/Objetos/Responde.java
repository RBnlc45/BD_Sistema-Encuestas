
package NEGOCIO.Objetos;

import Anotaciones.Entity;
import Anotaciones.ForeignKey;
import Anotaciones.PrimaryKey;
import Anotaciones.EntityConstructor;

@Entity(Name = "Responde",Squema = "System")
public class Responde {
    @PrimaryKey @ForeignKey(ReferenceTo = Usuario.class)
    private String cedula;
    
    @PrimaryKey @ForeignKey(ReferenceTo = PreguntaAbierta.class)
    private Integer idPreguntaAbierta;

    private String respuesta;

    @EntityConstructor
    public Responde(String cedula,Integer idPreguntaAbierta, String respuesta) {
        this.idPreguntaAbierta = idPreguntaAbierta;
        this.cedula = cedula;
        this.respuesta = respuesta;
    }

    public Responde() {
        
    }

    public Integer getIdPreguntaAbierta() {
        return idPreguntaAbierta;
    }

    public void setIdPreguntaAbierta(Integer idPreguntaAbierta) {
        this.idPreguntaAbierta = idPreguntaAbierta;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
    
}
