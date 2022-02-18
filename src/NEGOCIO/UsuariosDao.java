package NEGOCIO;

import DATOS.Privilege_Role_Statement;
import NEGOCIO.Objetos.Usuario;
import DATOS.Statements_Bridge;
import java.util.Arrays;
import java.util.LinkedList;

public class UsuariosDao{ //Data Access Object
    Statements_Bridge statement;
    Privilege_Role_Statement RPQuery;
    
    public UsuariosDao(String cedula, String contrasenia){
        statement=new Statements_Bridge();
        statement.cambio_usuario(cedula, contrasenia);
        RPQuery=new Privilege_Role_Statement();
    }
    public UsuariosDao(){
        statement=new Statements_Bridge();
        RPQuery=new Privilege_Role_Statement();
    }
    public String[] agregar_usuario(Usuario usuario){ //Manda a agregar usuario en la tabla
        if(this.usuario_repetido(usuario).equals("Error")) return new String[]{"Error","Ya existe un "+usuario.getClass().getSimpleName()+" con el numero de cedula ingresado"};
        boolean s=statement.transaccion_insertar(new LinkedList(Arrays.asList(usuario)));
        if(s==true){
            return new String[]{"Informacion","Usuario agregado correctamente"};
        }else{
            return new String[]{"Error","Usuario no agregado"};
        }
    }
     
    public String[] eliminar_usuario(Usuario usuario){
        boolean s=statement.sentencia_eliminar(usuario); //Manda a eliminar usuario de la tabla
        if(s==true){return new String[]{"Informacion","Usuario eliminado correctamente"};}
        else{return new String[]{"Error","Usuario no eliminado"};}
    }
    
    public LinkedList<Usuario> recuperar_usuarios(Usuario usuario){
        return statement.seleccionar_todos(usuario); //Recupera usuarios
    }
    
    public Usuario recuperar_usuario(String cedula){ //Recupera usuario determinado por la cedula
        Usuario usuario=new Usuario();
        usuario.setCedula(cedula);
        LinkedList<Usuario> usuarios=statement.seleccionar_especifico(usuario);
        if(usuarios!=null) return usuarios.get(0);
        return new Usuario();
    }
    
    public boolean comprobar_usuario(String cedula, String contrasenia){ //Recupera usuario con la cedula y verifica que la contrasenia sea la misma a la que ingreso
        Usuario usuario=new Usuario();
        usuario.setCedula(cedula); //Cedula
        LinkedList<Usuario> ListaUsuarios=statement.seleccionar_especifico(usuario);
        if(ListaUsuarios.size()>0 && ListaUsuarios.get(0).getPassword().equals(contrasenia)) return true; //Verificamos usuario y contrasenia 
        else return false;
    }    
    
    public String usuario_repetido(Usuario usuario){ //Al crear nuevo usuario comprueba que no este repetido
        LinkedList<Usuario> usuarios=statement.seleccionar_todos(usuario);
        for(Usuario e: usuarios){
            if(e.getCedula().equals(usuario.getCedula())){return "Error";}
        }
        return "Correcto";
    }
        
    public boolean asignar_rol(String cedula, String rol){ //Asigna rol a usuario
        return RPQuery.asign_role(true,cedula, rol); //true aigna
    }
    
    public boolean revocar_rol(String cedula, String rol){ //Revoca rol
        return RPQuery.asign_role(false,cedula, rol); //false quita
    }
    
    public LinkedList<String> obtener_roles(String cedula){ //Obtener roles
        return RPQuery.get_roles(cedula);
    }
    
    public boolean asignar_priv(String cedula, String tabla, String priv){ //Asigna privilegio sobre tabla a usuario
        return RPQuery.asign_privs(true, cedula, tabla, priv); //true aigna
    }
    
    public boolean revocar_priv(String cedula, String tabla, String priv){ //Revoca privilegio sobre tabla
        return RPQuery.asign_privs(false,cedula, tabla, priv); //true aigna
    }
    
    public LinkedList<String> obtener_privs_tabla(String cedula, String tabla){ //Obtener privilegios de usuario sobre una tabla
        return RPQuery.get_privs(cedula,tabla);
    }   
    public boolean editar_usuario(Usuario usuario){ //Manda el objeto usuario con los nuevos datos a que se actualicen
        return statement.transaccion_actualizar(new LinkedList(Arrays.asList(usuario)));
    }
    public void cerrar_sesion(){ //Para cerrar sesion del usuario
        statement.cerrar_conexion();
    }
}