
package Anotaciones;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {//anotación para identificar un objeto que es entidad de la base de datos
    public String Name();//Nombre de la entidad en la base de datos
    public String Squema();//esquema donde se encuentra la entidad
}
