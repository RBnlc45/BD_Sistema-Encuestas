package Anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {//anotación para identificar atributos que son clave primaria
   public boolean AutoIncrement() default false;//bandera, la clave es autoincrementada
   public String Sequence() default "";//Nombre de la secuencia para la clave primaria
   public String Squema() default "";//nombre del esquema de la secuencia adjunta
}
