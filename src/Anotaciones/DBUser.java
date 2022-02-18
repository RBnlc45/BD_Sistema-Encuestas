
package Anotaciones;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface DBUser {
    //Anotación para diferenciar  al objeto que es una instancia de un usuario de la base de datos
}
