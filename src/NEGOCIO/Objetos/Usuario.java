
package NEGOCIO.Objetos;

import Anotaciones.Entity;
import Anotaciones.PrimaryKey;
import Anotaciones.EntityConstructor;
@Entity(Name = "Usuario",Squema = "System")
public class Usuario {
    @PrimaryKey
    private String cedula;
    private String nombre,apellido,password,telefono,direccion,mail;
    
    @EntityConstructor
    public Usuario(String cedula, String nombre, String apellido, String password, String telefono,String direccion,String mail) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.telefono = telefono;
        this.direccion=direccion;
        this.mail = mail;
    }
    public Usuario(){
    
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    
    
}
