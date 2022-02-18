package Anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ObjectReference {
//Anotación para atributos de una clase que sirven como Objetos de referencia para
//las claves foraneas existentes en la relación.
    public boolean inConstructor() default true;//indica si el objeto es parte del constructor del objeto
}
