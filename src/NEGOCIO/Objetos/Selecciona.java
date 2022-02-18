
package NEGOCIO.Objetos;

import Anotaciones.Entity;
import Anotaciones.ForeignKey;
import Anotaciones.PrimaryKey;
import Anotaciones.EntityConstructor;

@Entity(Name = "Selecciona",Squema = "System")
public class Selecciona {
    @PrimaryKey @ForeignKey(ReferenceTo = Usuario.class)
    private String cedula;
    
    @PrimaryKey @ForeignKey(ReferenceTo = Opcion.class)
    private Integer idOpcion;

    @EntityConstructor
    public Selecciona(String cedula,Integer idOpcion) {
        this.idOpcion = idOpcion;
        this.cedula = cedula;
    }

    public Selecciona() {

    }

    public Integer getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Integer idOpcion) {
        this.idOpcion = idOpcion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
}
