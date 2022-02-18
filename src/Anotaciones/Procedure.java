
package Anotaciones;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface Procedure {//anotación para identificar un objeto que es un procedimiento almacenado en la base de datos
    public String Name();//nombre del procedimiento
    public String Squema();//nombre del esquema donde se encuentra el procedimiento
}
