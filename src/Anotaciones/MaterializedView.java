
package Anotaciones;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface MaterializedView {//anotación para identificar objetos que son vistas materializadas en la base de datos
    public String Name();//nombre de la vista materializada
    public String Squema();//esquema donde se encuentra la vista materializada
}
