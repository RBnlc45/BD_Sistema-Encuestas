
package DATOS;
import DATOS.GENERICO.SQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;


public class Privilege_Role_Statement {
    private SQL sql;
    public Privilege_Role_Statement(){
        sql=SQL.getInstance();//conexión a la base de datos
    }
    public boolean asign_role(boolean bol, String usuario, String rol){
        String str;
        if(bol) str="GRANT "+rol+" TO "+"USUARIO_"+usuario;
        else str="REVOKE "+rol+" FROM "+"USUARIO_"+usuario;
        return sql.execute_statement(str);
    } 
    public LinkedList<String> get_roles(String usuario){
        LinkedList<String> lista_roles=new LinkedList<>();
        String str="select granted_role from dba_role_privs where grantee = 'USUARIO_"+usuario+"'";
        ResultSet rs=sql.execute_query_statement(str);
        if(rs==null) return new LinkedList<String>();
        try {while(rs.next()){lista_roles.add(rs.getString(1));}} 
        catch (SQLException ex){return new LinkedList<String>();}
        return lista_roles;     
    }
    
    public boolean asign_privs(boolean bol, String usuario, String tabla, String priv){
        String str;
        if(bol) str="GRANT "+priv+" ON SYSTEM."+tabla+" TO USUARIO_"+usuario;
        else str="REVOKE "+priv+" ON SYSTEM."+tabla+" FROM "+"USUARIO_"+usuario;
        return sql.execute_statement(str); 
    }
    public LinkedList<String> get_privs(String usuario, String tabla){ 
        try{
            LinkedList<String> lista_roles=new LinkedList<>();
            String str="SELECT PRIVILEGE FROM USER_TAB_PRIVS WHERE grantee='USUARIO_"+usuario+"' AND TABLE_NAME='"+tabla+"'";
            ResultSet rs=sql.execute_query_statement(str);
            if(rs==null) return new LinkedList<String>();
            while(rs.next()){lista_roles.add(rs.getString(1));}
            return lista_roles;     
        }catch(Exception e){
            return new LinkedList<String>();
        }
    }
    
}
