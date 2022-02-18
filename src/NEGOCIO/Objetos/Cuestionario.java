
package NEGOCIO.Objetos;

import Anotaciones.Entity;
import Anotaciones.ForeignKey;
import Anotaciones.ObjectReference;
import Anotaciones.PrimaryKey;
import oracle.sql.TIMESTAMP;
import Anotaciones.EntityConstructor;
@Entity(Name = "Cuestionario",Squema = "System")
public class Cuestionario {
    @PrimaryKey(AutoIncrement = true,Sequence = "Cuestionario_id_seq",Squema = "System")
    private Integer idCuestionario;
    private TIMESTAMP fecha;
    private String titulo;
    private String descripcion;
    private String comentario;
    @ForeignKey(ReferenceTo = Usuario.class)
    private String cedula;
   
    @ObjectReference(inConstructor = false)
    private Usuario encuestador;
    
    @EntityConstructor
    public Cuestionario(Integer idCuestionario,TIMESTAMP fecha, String titulo, String descripcion, String comentario,String cedula) {
        this.idCuestionario = idCuestionario;
        this.fecha = fecha;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.comentario = comentario;
        this.cedula=cedula;
    }

    public Cuestionario() {
    
    }

    public int getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(int idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public TIMESTAMP getFechaCreacion() {
        return fecha;
    }

    public void setFechaCreacion(TIMESTAMP fechaCreacion) {
        this.fecha = fechaCreacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return titulo;
    }
    
    
}
