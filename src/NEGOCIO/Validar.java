package NEGOCIO;

import javax.swing.JOptionPane;

public class Validar {
       
    public static boolean cedula(String cedula) {
        if(cedula.length()!=10) return false;
        int n = 0, i = 0;
        int multiplicador = 0, residuo = 0, aux = 0, sumador = 0;
        String multiplicadorCadena = "";
        for (i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                if (Character.getNumericValue(cedula.charAt(i)) > 4) {
                    aux = (Character.getNumericValue(cedula.charAt(i)) * 2) - 9;
                } else {
                    aux = Character.getNumericValue(cedula.charAt(i)) * 2;
                }
            } else {
                aux = (Character.getNumericValue(cedula.charAt(i)));
            }
            multiplicadorCadena += Character.forDigit(aux, 10);
        }
        for (i = 0; i < 9; i++) {
            sumador += Character.getNumericValue(multiplicadorCadena.charAt(i));
        }
        residuo = sumador % 10;
        if (10 - residuo == Character.getNumericValue(cedula.charAt(9))) {
            return true;
        } else {
            return false;
        }
    }
       
    public static String telefono(String cedula){;
        if(cedula.length()!=10) return "Error";
        else{  
            try{
                Double.parseDouble(cedula); //Para verificar que sean digitos
            }catch(NumberFormatException e) {
                return "Error";
            }
        }
        return "Correcto";
    } //Valida cedula
    
    public static String campos_vacios(String[] campos){
        for(String c: campos){
            if(c.equals("")){
                return "Error";
            }
        }
        return "Correcto";
    }
         
    public static boolean numero_caracteres(int rango, int n_caracteres){
        if(n_caracteres>rango) return false; //Si los caracteres ingresados en un recuadro de texto son mas que los permitidos
        return true;
    }
    
    public static boolean comprobar_contrasenia(String contrasenia){
        char f_char=contrasenia.charAt(0);//primer caracter de la contrasenia
        if(Character.isDigit(f_char)){//primer caracter es un numero
            if(contrasenia.chars().allMatch( Character::isDigit )){//si la contrasenia esta compuesta por caracteres
                return true;
            }
            else{
                return false;
            }
        }
        else return true;
    }
    
    public static String CamposDatosUsuario(String[] campos){
       if(Validar.campos_vacios(campos).equals("Error")){ //Si no hay campos vacios
           return "No se pueden dejar campos vacios";
       }
       else if(!Validar.cedula(campos[0])){ //Validar cedula
           return "Cedula incorrecta";
       }
       else if(!Validar.numero_caracteres(16, campos[3].length())){ //Validar 16 caracteres password
           return "La contraseña debe tener como máximo 16 digitos";
       }else if(!Validar.comprobar_contrasenia(campos[3])){ //Validar contrasenia
           return "La contraseña no puede contener un valor numérico al inicio";
           
       }
       else if(!Validar.numero_caracteres(20, campos[1].length())){ //Validar 20 caracteres nombre
           return "El nombre debe tener como máximo 20 digitos";
       }
       else if(!Validar.numero_caracteres(16, campos[2].length())){ //Validar 20 caracteres apellido
           return "El apellido debe tener como máximo 20 digitos";
       }
       else if(Validar.telefono(campos[4]).equals("Error")){ //Validar 10 caracteres telefono
           return "El teléfono debe tener 10 digitos numéricos";
       }
       else if(!Validar.numero_caracteres(50, campos[5].length())){ //Validar 50 caracteres direccion
           return "La dirección debe tener como máximo 50 digitos";
       }
       else if(!Validar.numero_caracteres(30, campos[6].length())){ //Validar 30 caracteres correo
           return "El correo debe tener como máximo 30 digitos";
       }
       return null;
   }
}
