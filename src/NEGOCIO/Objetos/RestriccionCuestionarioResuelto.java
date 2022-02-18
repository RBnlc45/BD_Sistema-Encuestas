
package NEGOCIO.Objetos;

import Anotaciones.Input;
import Anotaciones.Output;
import Anotaciones.Procedure;

@Procedure(Name = "Procedure_RestriccionCuestionarioResuelto",Squema = "System")
public class RestriccionCuestionarioResuelto {
    @Input
    private String cedula;
    
    @Input
    private Integer idCuestionario;
    
    @Output
    private String resuelto_flag;

    public RestriccionCuestionarioResuelto(String cedula, Integer idCuestionario) {
        this.cedula = cedula;
        this.idCuestionario = idCuestionario;
    }
   
    public String getResuelto() {
        return resuelto_flag;
    }
    
    
}
