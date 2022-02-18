
package Anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {//anotación para diferenciar los atributos que son clave foranea en una entidad
    public Class ReferenceTo() default Object.class;//referencia a la clase de objeto de la cual es la clave foranea
}
