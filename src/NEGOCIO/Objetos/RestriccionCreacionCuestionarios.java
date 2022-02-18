
package NEGOCIO.Objetos;

import Anotaciones.Input;
import Anotaciones.Output;
import Anotaciones.Procedure;

@Procedure(Name ="Procedure_RestriccionCreacionCuestionarios",Squema = "System")
public class RestriccionCreacionCuestionarios {
    @Input
    private String cedula;
    
    @Output
    private String cumple;

    public RestriccionCreacionCuestionarios(String cedula) {
        this.cedula = cedula;
    }

    public String getCumple() {
        return cumple;
    }   
}
