package PRESENTACION;

import NEGOCIO.Validar;
import NEGOCIO.UsuariosDao;
import NEGOCIO.Objetos.Usuario;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VistaAdministrador extends javax.swing.JFrame implements WindowListener{
    private VistaPrincipal v_princiapal; //Instancia ventana principal
    protected UsuariosDao usuarioActual; //Conexion como usuario
    
    public VistaAdministrador(String cedula, String contrasenia, VistaPrincipal v_principal) {
        this.v_princiapal=v_principal; //Referencia de la vista principal
        initComponents();
        this.addWindowListener(this); //Para regresar a la vista principal
        usuarioActual=new UsuariosDao(cedula, contrasenia); //Maneja Usuarios
        this.MostrarTabla(this.jgdUsuarios, usuarioActual.recuperar_usuarios(new Usuario())); //Mostrar tabla de administradores al iniciar
        this.ConfigCbxRolPrivs(); //Combo de usuarios creados
    }
    
    public void MostrarTabla(JTable tabla, LinkedList<Usuario> contenedor){
        DefaultTableModel model=(DefaultTableModel) tabla.getModel();
        int size=model.getRowCount();
        for(int i=size-1;i>=0;i--){ //borra toda la tabla
        	model.removeRow(i);
        }
        for(int i=0; i<contenedor.size();i++){ //Ingresa los elementos a la tabla
        	model.insertRow(model.getRowCount(), new Object[] {});
        	model.setValueAt(contenedor.get(i).getCedula(),  model.getRowCount()-1, 0);
                model.setValueAt(contenedor.get(i).getNombre(),  model.getRowCount()-1, 1);
                model.setValueAt(contenedor.get(i).getApellido(),  model.getRowCount()-1, 2);
                model.setValueAt(contenedor.get(i).getTelefono(),  model.getRowCount()-1, 3);
                model.setValueAt(contenedor.get(i).getDireccion(),  model.getRowCount()-1, 4);
                model.setValueAt(contenedor.get(i).getMail(),  model.getRowCount()-1, 5);    
        }
    } //Muestra tabla de usuarios
    
    public void ConfiguracionRolPrivs(int filas){
        boolean bool=false; //Maneja los componentes de la ventana de roles y privilegios
        if(filas>0) bool=true; //Si hay almenos un usuario se habilitan las ventanas de roles y privilegios
        this.cbxCedulaRol.setEnabled(bool);
        this.cbxEncuestador.setEnabled(bool);
        this.cbxEncuestado.setEnabled(bool);
        this.btnAsignarRol.setEnabled(bool);
        this.cbxCedulaPrivilegio.setEnabled(bool);
        this.cbxTabla.setEnabled(bool);
        this.cbxInsert.setEnabled(bool);
        this.cbxDelete.setEnabled(bool);
        this.cbxSelect.setEnabled(bool);
        this.btnAsignarPrivilegio.setEnabled(bool);
    } //Setea las tabs de roles y privilegios
    
    public void ConfigCbxRolPrivs(){ //Agrega las cedulas de los usuarios si existen
        this.ConfiguracionRolPrivs(this.jgdUsuarios.getRowCount()); //Configura la ventana de roles y privilegios
        LinkedList<Usuario> lista_usuarios=usuarioActual.recuperar_usuarios(new Usuario()); //Recupera la lista de usuarios registrados
        DefaultComboBoxModel cbxModelR=new DefaultComboBoxModel();
        DefaultComboBoxModel cbxModelP=new DefaultComboBoxModel();
        for(Usuario u: lista_usuarios){ //Agrega cedulas de usuarios creados a combo roles
            cbxModelR.insertElementAt(u.getCedula(), cbxModelR.getSize()); //Ingresa las cedulas en el combo de roles
            cbxModelP.insertElementAt(u.getCedula(), cbxModelP.getSize()); //Ingresa las cedulas en el combo de privs
        }
        this.cbxCedulaRol.setModel(cbxModelR); //Asgina modelo a combo de ventana de roles
        if(this.cbxCedulaRol.getModel().getSize()>0) this.cbxCedulaRol.setSelectedIndex(0); //Si hay minimo un elemento se selecciona el primero
        this.cbxCedulaPrivilegio.setModel(cbxModelP); //Asigna modelo a combo de ventana de privilegios
        if(this.cbxCedulaPrivilegio.getModel().getSize()>0) this.cbxCedulaPrivilegio.setSelectedIndex(0); //Si hay minimo un elemento se selecciona el primero
        this.MostrarRoles(); //Muestra los roles del usuario seleccionado en el combo box de la ventana de roles
        this.MostararPrivilegios(); //Muestra los privilegios de la cedula celeccionada sobre 
    } //Al iniciar
    
    public void MostrarRoles(){
        this.cbxEncuestador.setSelected(false);
        this.cbxEncuestado.setSelected(false);
        if(this.cbxCedulaRol.getSelectedItem()==null) return; //No hay cedulas
        LinkedList<String> roles_usuario=usuarioActual.obtener_roles(this.cbxCedulaRol.getSelectedItem().toString());
        if(roles_usuario!=null && roles_usuario.size()>0){ //Si el usuario seleccionado tiene roles
            for(String r: roles_usuario){ //Recorre el linked list con los roles
                if(r.equalsIgnoreCase("ENCUESTADOR")) this.cbxEncuestador.setSelected(true);
                if(r.equalsIgnoreCase("ENCUESTADO")) this.cbxEncuestado.setSelected(true);
            }
        }
    } //Muestra roles del usuario seleccionado
    
    public void MostararPrivilegios(){
        this.cbxInsert.setSelected(false);
        this.cbxSelect.setSelected(false);
        this.cbxDelete.setSelected(false);
        this.cbxUpdate.setSelected(false);
        if(this.cbxTabla.getSelectedItem().toString().equalsIgnoreCase("Usuario")){
            this.cbxDelete.setVisible(false);
            this.cbxUpdate.setVisible(true);
        }  
        else {
            this.cbxDelete.setVisible(true);
            this.cbxUpdate.setVisible(false);
        }
        if(this.cbxCedulaPrivilegio.getSelectedItem()==null) return; //No hay cedulas
        LinkedList<String> privilegios_usuario=usuarioActual.obtener_privs_tabla(this.cbxCedulaPrivilegio.getSelectedItem().toString(),this.cbxTabla.getSelectedItem().toString().toUpperCase());
        if(privilegios_usuario!=null && privilegios_usuario.size()>0){ //Si el usuario seleccionado tiene privilegios
            for(String p: privilegios_usuario){ //Recorre el linked list con los roles
                if(p.equalsIgnoreCase("SELECT")) this.cbxSelect.setSelected(true);
                if(p.equalsIgnoreCase("INSERT")) this.cbxInsert.setSelected(true);
                if(this.cbxDelete.isEnabled() && p.equalsIgnoreCase("DELETE")) this.cbxDelete.setSelected(true);
                if(this.cbxUpdate.isEnabled() && p.equalsIgnoreCase("UPDATE")) this.cbxUpdate.setSelected(true);
            }
        }
    } //Muestra privs del usuario seleccionado
    
    public void LimpiarTextosCreacion(){
        this.txtCedulaCrear.setText("");
        this.txtNombre.setText("");
        this.txtApellido.setText("");
        this.txtContrasenia.setText("");
        this.txtTelefono.setText("");
        this.txtDireccion.setText("");
        this.txtCorreo.setText("");
    } //Limpia textos de la tab para crear usuarios
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbtgRol = new javax.swing.ButtonGroup();
        grbRoles = new javax.swing.JTabbedPane();
        grbUsuarios = new javax.swing.JPanel();
        grbElementosUsuarios = new javax.swing.JPanel();
        jScrollPaneUsuariosRegistrados = new javax.swing.JScrollPane();
        jgdUsuarios = new javax.swing.JTable();
        lblUsuariosRegistrados = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        grbCrearUsuario = new javax.swing.JPanel();
        grbElementosCrear = new javax.swing.JPanel();
        btnCrear = new javax.swing.JButton();
        grbLogin = new javax.swing.JPanel();
        txtCedulaCrear = new javax.swing.JTextField();
        txtContrasenia = new javax.swing.JTextField();
        lblNombre2 = new javax.swing.JLabel();
        lblCedula = new javax.swing.JLabel();
        grbDatosAdicionales = new javax.swing.JPanel();
        txtTelefono = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        lblNombre5 = new javax.swing.JLabel();
        lblNombre4 = new javax.swing.JLabel();
        lblNombre3 = new javax.swing.JLabel();
        lblNombre1 = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        gbrPermisos = new javax.swing.JPanel();
        grbElementosPermisos = new javax.swing.JPanel();
        lblCedula1 = new javax.swing.JLabel();
        btnAsignarRol = new javax.swing.JButton();
        cbxEncuestador = new javax.swing.JCheckBox();
        cbxEncuestado = new javax.swing.JCheckBox();
        cbxCedulaRol = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        grbElementosPermisos1 = new javax.swing.JPanel();
        lblCedula2 = new javax.swing.JLabel();
        btnAsignarPrivilegio = new javax.swing.JButton();
        cbxSelect = new javax.swing.JCheckBox();
        cbxInsert = new javax.swing.JCheckBox();
        cbxDelete = new javax.swing.JCheckBox();
        cbxTabla = new javax.swing.JComboBox<>();
        cbxCedulaPrivilegio = new javax.swing.JComboBox<>();
        lblCedula3 = new javax.swing.JLabel();
        cbxUpdate = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Administrador");

        grbUsuarios.setBackground(new java.awt.Color(204, 204, 204));
        grbUsuarios.setForeground(new java.awt.Color(255, 255, 255));

        jgdUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Cédula", "Nombre", "Apelllido", "Teléfono", "Dirección", "Correo"
            }
        ));
        jScrollPaneUsuariosRegistrados.setViewportView(jgdUsuarios);

        lblUsuariosRegistrados.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblUsuariosRegistrados.setText("USUARIOS REGISTRADOS");

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout grbElementosUsuariosLayout = new javax.swing.GroupLayout(grbElementosUsuarios);
        grbElementosUsuarios.setLayout(grbElementosUsuariosLayout);
        grbElementosUsuariosLayout.setHorizontalGroup(
            grbElementosUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbElementosUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grbElementosUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(grbElementosUsuariosLayout.createSequentialGroup()
                        .addComponent(jScrollPaneUsuariosRegistrados, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grbElementosUsuariosLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(200, 200, 200))))
            .addGroup(grbElementosUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(grbElementosUsuariosLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblUsuariosRegistrados)
                    .addContainerGap(499, Short.MAX_VALUE)))
        );
        grbElementosUsuariosLayout.setVerticalGroup(
            grbElementosUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbElementosUsuariosLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jScrollPaneUsuariosRegistrados, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(grbElementosUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(grbElementosUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(grbElementosUsuariosLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblUsuariosRegistrados)
                    .addContainerGap(301, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout grbUsuariosLayout = new javax.swing.GroupLayout(grbUsuarios);
        grbUsuarios.setLayout(grbUsuariosLayout);
        grbUsuariosLayout.setHorizontalGroup(
            grbUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(grbElementosUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        grbUsuariosLayout.setVerticalGroup(
            grbUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grbUsuariosLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(grbElementosUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        grbRoles.addTab("USUARIOS REGISTRADOS", grbUsuarios);

        grbCrearUsuario.setBackground(new java.awt.Color(204, 204, 204));

        btnCrear.setText("Crear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });

        grbLogin.setBorder(javax.swing.BorderFactory.createTitledBorder("Login"));

        lblNombre2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombre2.setText("Contraseña:");

        lblCedula.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCedula.setText("Cédula:");

        javax.swing.GroupLayout grbLoginLayout = new javax.swing.GroupLayout(grbLogin);
        grbLogin.setLayout(grbLoginLayout);
        grbLoginLayout.setHorizontalGroup(
            grbLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbLoginLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(grbLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCedula)
                    .addComponent(lblNombre2))
                .addGap(39, 39, 39)
                .addGroup(grbLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCedulaCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(243, Short.MAX_VALUE))
        );
        grbLoginLayout.setVerticalGroup(
            grbLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbLoginLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(grbLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCedula)
                    .addComponent(txtCedulaCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(grbLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombre2))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        grbDatosAdicionales.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Adicionales"));

        lblNombre5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombre5.setText("Dirección:");

        lblNombre4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombre4.setText("Correo:");

        lblNombre3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombre3.setText("Teléfono:");

        lblNombre1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombre1.setText("Apellido:");

        lblNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombre.setText("Nombre:");

        javax.swing.GroupLayout grbDatosAdicionalesLayout = new javax.swing.GroupLayout(grbDatosAdicionales);
        grbDatosAdicionales.setLayout(grbDatosAdicionalesLayout);
        grbDatosAdicionalesLayout.setHorizontalGroup(
            grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbDatosAdicionalesLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblNombre, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblNombre1))
                    .addGroup(grbDatosAdicionalesLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNombre3)
                            .addComponent(lblNombre5)
                            .addComponent(lblNombre4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addGroup(grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtApellido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        grbDatosAdicionalesLayout.setVerticalGroup(
            grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grbDatosAdicionalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(grbDatosAdicionalesLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(lblNombre1)
                        .addGap(18, 18, 18)
                        .addComponent(lblNombre3)
                        .addGap(18, 18, 18)
                        .addComponent(lblNombre5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(grbDatosAdicionalesLayout.createSequentialGroup()
                        .addGroup(grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre)
                            .addComponent(lblNombre))
                        .addGap(18, 18, 18)
                        .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(grbDatosAdicionalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombre4))))
                .addContainerGap())
        );

        javax.swing.GroupLayout grbElementosCrearLayout = new javax.swing.GroupLayout(grbElementosCrear);
        grbElementosCrear.setLayout(grbElementosCrearLayout);
        grbElementosCrearLayout.setHorizontalGroup(
            grbElementosCrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbElementosCrearLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grbElementosCrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(grbLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(grbDatosAdicionales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grbElementosCrearLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(261, 261, 261))
        );
        grbElementosCrearLayout.setVerticalGroup(
            grbElementosCrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbElementosCrearLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(grbLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(grbDatosAdicionales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout grbCrearUsuarioLayout = new javax.swing.GroupLayout(grbCrearUsuario);
        grbCrearUsuario.setLayout(grbCrearUsuarioLayout);
        grbCrearUsuarioLayout.setHorizontalGroup(
            grbCrearUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grbCrearUsuarioLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(grbElementosCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );
        grbCrearUsuarioLayout.setVerticalGroup(
            grbCrearUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbCrearUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(grbElementosCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        grbRoles.addTab("CREAR USUARIO", grbCrearUsuario);

        gbrPermisos.setBackground(new java.awt.Color(204, 204, 204));

        lblCedula1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCedula1.setText("Cédula:");

        btnAsignarRol.setText("Asignar Roles");
        btnAsignarRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarRolActionPerformed(evt);
            }
        });

        cbxEncuestador.setText("Rol Encuestador");

        cbxEncuestado.setText("Rol Encuestado");

        cbxCedulaRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCedulaRolActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout grbElementosPermisosLayout = new javax.swing.GroupLayout(grbElementosPermisos);
        grbElementosPermisos.setLayout(grbElementosPermisosLayout);
        grbElementosPermisosLayout.setHorizontalGroup(
            grbElementosPermisosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grbElementosPermisosLayout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addGroup(grbElementosPermisosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxEncuestado)
                    .addComponent(cbxEncuestador)
                    .addGroup(grbElementosPermisosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grbElementosPermisosLayout.createSequentialGroup()
                            .addComponent(lblCedula1)
                            .addGap(38, 38, 38)
                            .addComponent(cbxCedulaRol, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(123, 123, 123))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grbElementosPermisosLayout.createSequentialGroup()
                            .addComponent(btnAsignarRol, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(183, 183, 183)))))
        );
        grbElementosPermisosLayout.setVerticalGroup(
            grbElementosPermisosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbElementosPermisosLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(grbElementosPermisosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxCedulaRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCedula1))
                .addGap(69, 69, 69)
                .addComponent(cbxEncuestador)
                .addGap(47, 47, 47)
                .addComponent(cbxEncuestado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(btnAsignarRol, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout gbrPermisosLayout = new javax.swing.GroupLayout(gbrPermisos);
        gbrPermisos.setLayout(gbrPermisosLayout);
        gbrPermisosLayout.setHorizontalGroup(
            gbrPermisosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gbrPermisosLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(grbElementosPermisos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
        );
        gbrPermisosLayout.setVerticalGroup(
            gbrPermisosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gbrPermisosLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(grbElementosPermisos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        grbRoles.addTab("ASIGNAR ROLES", gbrPermisos);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        grbElementosPermisos1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCedula2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCedula2.setText("Cédula:");
        grbElementosPermisos1.add(lblCedula2, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 21, -1, -1));

        btnAsignarPrivilegio.setText("Asignar Privilegios");
        btnAsignarPrivilegio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarPrivilegioActionPerformed(evt);
            }
        });
        grbElementosPermisos1.add(btnAsignarPrivilegio, new org.netbeans.lib.awtextra.AbsoluteConstraints(194, 274, -1, 31));

        cbxSelect.setText("SELECT");
        grbElementosPermisos1.add(cbxSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 169, 81, -1));

        cbxInsert.setText("INSERT");
        grbElementosPermisos1.add(cbxInsert, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 169, 81, -1));

        cbxDelete.setText("DELETE");
        grbElementosPermisos1.add(cbxDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(339, 169, 81, -1));

        cbxTabla.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Usuario", "Cuestionario", "Preguntaabierta", "Preguntamultiple", "Opcion", "Responde", "Selecciona" }));
        cbxTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTablaActionPerformed(evt);
            }
        });
        grbElementosPermisos1.add(cbxTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(194, 75, 180, -1));

        cbxCedulaPrivilegio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCedulaPrivilegioActionPerformed(evt);
            }
        });
        grbElementosPermisos1.add(cbxCedulaPrivilegio, new org.netbeans.lib.awtextra.AbsoluteConstraints(194, 20, 180, -1));

        lblCedula3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCedula3.setText("Tabla:");
        grbElementosPermisos1.add(lblCedula3, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 75, -1, -1));

        cbxUpdate.setText("UPDATE");
        grbElementosPermisos1.add(cbxUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(grbElementosPermisos1, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(grbElementosPermisos1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        grbRoles.addTab("ASIGNAR PRIVILEGIOS", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(grbRoles)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(grbRoles)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        String[] campos=new String[]{this.txtCedulaCrear.getText(),this.txtNombre.getText(),this.txtApellido.getText(),
                this.txtContrasenia.getText(), this.txtTelefono.getText(), this.txtDireccion.getText(),
                    this.txtCorreo.getText()};
        if(!CamposDatosUsuario(campos)) return; //Si no pasa validaciones de campos
        Usuario usuario=new Usuario(campos[0],campos[1],campos[2],campos[3],campos[4],campos[5],campos[6]); //Obtener usuario(rol) a crear
        String[] mensaje=usuarioActual.agregar_usuario(usuario); //Mandar a insertar en tablas el usuario
        if(mensaje[0].equals("Informacion")){
            JOptionPane.showMessageDialog(this, mensaje[1],mensaje[0],JOptionPane.INFORMATION_MESSAGE);
            this.LimpiarTextosCreacion();
            DefaultTableModel model=((DefaultTableModel)jgdUsuarios.getModel());
            model.insertRow(model.getRowCount(), new Object[] {campos[0],campos[1],campos[2],campos[4],campos[5],campos[6]});
            jgdUsuarios.setModel(model);        
            this.cbxCedulaRol.addItem(campos[0]);
            this.cbxCedulaPrivilegio.addItem(campos[0]);
            this.ConfiguracionRolPrivs(this.jgdUsuarios.getRowCount());
        }else{
            JOptionPane.showMessageDialog(this, mensaje[1],mensaje[0],JOptionPane.ERROR_MESSAGE);
        }  
    }//GEN-LAST:event_btnCrearActionPerformed
    
    private void btnAsignarRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarRolActionPerformed
        String cedula=(String)this.cbxCedulaRol.getModel().getSelectedItem();
        if(this.cbxEncuestado.isSelected()) usuarioActual.asignar_rol(cedula, "Encuestado");
        else if(!this.cbxEncuestado.isSelected()) usuarioActual.revocar_rol(cedula, "Encuestado");
        if(this.cbxEncuestador.isSelected()) usuarioActual.asignar_rol(cedula, "Encuestador");
        else if(!this.cbxEncuestador.isSelected()) usuarioActual.revocar_rol(cedula, "Encuestador");
        JOptionPane.showMessageDialog(this, "Roles asignados/revocados correctamente");
    }//GEN-LAST:event_btnAsignarRolActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        int indice=this.jgdUsuarios.getSelectedRow();
        if(!(indice!=-1)){
            JOptionPane.showMessageDialog(this, "Primero seleccione una fila de la tabla");
            return;
        }
        new VistaEditarUsuario(new UsuariosDao().recuperar_usuario((String)this.jgdUsuarios.getModel().getValueAt(indice, 0)), this.v_princiapal, this, null, null).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if(!(JOptionPane.showConfirmDialog(this, "Esta seguro de eliminar el usuario", "Advertencia", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION)){
            return;
        } //Adveretencia para eliminar el usuario
        int indice=this.jgdUsuarios.getSelectedRow();
        if(!(indice!=-1)){
            JOptionPane.showMessageDialog(this, "Primero seleccione una fila de la tabla");
            return;
        } //Verifica que haya seleccionado una fila
        String cedula=(String)this.jgdUsuarios.getModel().getValueAt(indice, 0); //Obtener la cedula de la fila
        this.usuarioActual.eliminar_usuario(new Usuario(cedula,"","","","","","")); //Elimina usuario
        ((DefaultTableModel)this.jgdUsuarios.getModel()).removeRow(indice); //Elimina fila del usuario
        this.cbxCedulaRol.removeItem(cedula); //Elimina la cedula del combo de roles
        this.cbxCedulaPrivilegio.removeItem(cedula); //Elima la cedula del combo de privilegios
        JOptionPane.showMessageDialog(this, "Usuario eliminado con exito","Error",JOptionPane.INFORMATION_MESSAGE);
        if(this.jgdUsuarios.getRowCount()==0) this.ConfiguracionRolPrivs(0); //Se manda a desactivar
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnAsignarPrivilegioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarPrivilegioActionPerformed
        String cedula=(String)this.cbxCedulaPrivilegio.getModel().getSelectedItem(); //Toma la cedula del combo de privilegios
        String tabla=(String)this.cbxTabla.getModel().getSelectedItem(); //Toma la tabla del combo de tablas
        if(this.cbxSelect.isSelected()) usuarioActual.asignar_priv(cedula, tabla,"SELECT"); //Si esta seleccionado el combo select
        else if(!this.cbxSelect.isSelected()) usuarioActual.revocar_priv(cedula, tabla, "SELECT");
        if(this.cbxInsert.isSelected()) usuarioActual.asignar_priv(cedula, tabla, "INSERT"); //Si esta seleccionado el combo insert
        else if(!this.cbxInsert.isSelected()) usuarioActual.revocar_priv(cedula, tabla, "INSERT");
        if(this.cbxDelete.isEnabled() && this.cbxDelete.isSelected()) usuarioActual.asignar_priv(cedula, tabla, "DELETE"); //Si esta seleccionado el combo delete
        else if(this.cbxDelete.isEnabled() && !this.cbxDelete.isSelected()) usuarioActual.revocar_priv(cedula, tabla, "DELETE");
        if(this.cbxUpdate.isEnabled() && this.cbxUpdate.isSelected()) usuarioActual.asignar_priv(cedula, tabla, "UPDATE"); //Si esta seleccionado el combo update
        else if(this.cbxUpdate.isEnabled() && !this.cbxUpdate.isSelected()) usuarioActual.revocar_priv(cedula, tabla, "UPDATE");
        JOptionPane.showMessageDialog(this, "Privilegios sobre tabla "+tabla+" asignados/revocados correctamente"); //Mensaje
    }//GEN-LAST:event_btnAsignarPrivilegioActionPerformed

    private void cbxCedulaRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCedulaRolActionPerformed
        this.MostrarRoles(); //Muestra los roles del usuario
    }//GEN-LAST:event_cbxCedulaRolActionPerformed

    private void cbxTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTablaActionPerformed
        this.MostararPrivilegios();
    }//GEN-LAST:event_cbxTablaActionPerformed

    private void cbxCedulaPrivilegioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCedulaPrivilegioActionPerformed
        this.MostararPrivilegios(); //Muestra privilegios del usuario en el combo box
    }//GEN-LAST:event_cbxCedulaPrivilegioActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAsignarPrivilegio;
    public javax.swing.JButton btnAsignarRol;
    private javax.swing.JButton btnCrear;
    public javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    public javax.swing.JComboBox<String> cbxCedulaPrivilegio;
    public javax.swing.JComboBox<String> cbxCedulaRol;
    public javax.swing.JCheckBox cbxDelete;
    public javax.swing.JCheckBox cbxEncuestado;
    public javax.swing.JCheckBox cbxEncuestador;
    public javax.swing.JCheckBox cbxInsert;
    public javax.swing.JCheckBox cbxSelect;
    public javax.swing.JComboBox<String> cbxTabla;
    private javax.swing.JCheckBox cbxUpdate;
    private javax.swing.JPanel gbrPermisos;
    private javax.swing.JPanel grbCrearUsuario;
    private javax.swing.JPanel grbDatosAdicionales;
    private javax.swing.JPanel grbElementosCrear;
    private javax.swing.JPanel grbElementosPermisos;
    private javax.swing.JPanel grbElementosPermisos1;
    private javax.swing.JPanel grbElementosUsuarios;
    private javax.swing.JPanel grbLogin;
    private javax.swing.JTabbedPane grbRoles;
    private javax.swing.JPanel grbUsuarios;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPaneUsuariosRegistrados;
    public javax.swing.JTable jgdUsuarios;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblCedula1;
    private javax.swing.JLabel lblCedula2;
    private javax.swing.JLabel lblCedula3;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombre1;
    private javax.swing.JLabel lblNombre2;
    private javax.swing.JLabel lblNombre3;
    private javax.swing.JLabel lblNombre4;
    private javax.swing.JLabel lblNombre5;
    public javax.swing.JLabel lblUsuariosRegistrados;
    public javax.swing.ButtonGroup rbtgRol;
    public javax.swing.JTextField txtApellido;
    public javax.swing.JTextField txtCedulaCrear;
    public javax.swing.JTextField txtContrasenia;
    public javax.swing.JTextField txtCorreo;
    public javax.swing.JTextField txtDireccion;
    public javax.swing.JTextField txtNombre;
    public javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

    private boolean CamposDatosUsuario(String[] campos){
       if(Validar.campos_vacios(campos).equals("Error")){ //Si no hay campos vacios
           JOptionPane.showMessageDialog(this, "No se pueden dejar campos vacios","Error",JOptionPane.ERROR_MESSAGE);
           return false;
       }
       else if(!Validar.cedula(campos[0])){ //Validar cedula
           JOptionPane.showMessageDialog(this, "Cedula incorrecta","Error",JOptionPane.ERROR_MESSAGE);
           return false;
       }
       else if(!Validar.numero_caracteres(16, campos[3].length())){ //Validar 16 caracteres password
           JOptionPane.showMessageDialog(this, "La contraseña debe tener como máximo 16 digitos","Error",JOptionPane.ERROR_MESSAGE);
           return false;
       }
       else if(!Validar.comprobar_contrasenia(campos[3])){ //Validar contrasenia
           JOptionPane.showMessageDialog(this, "La contraseña no puede contener un valor numérico al inicio","Error",JOptionPane.ERROR_MESSAGE);
           return false;
       }
       else if(!Validar.numero_caracteres(20, campos[1].length())){ //Validar 20 caracteres nombre
           JOptionPane.showMessageDialog(this, "El nombre debe tener como máximo 20 digitos","Error",JOptionPane.ERROR_MESSAGE);
           return false;
       }
       else if(!Validar.numero_caracteres(16, campos[2].length())){ //Validar 20 caracteres apellido
           JOptionPane.showMessageDialog(this, "El apellido debe tener como máximo 20 digitos","Error",JOptionPane.ERROR_MESSAGE);
           return false;
       }
       else if(Validar.telefono(campos[4]).equals("Error")){ //Validar 10 caracteres telefono
           JOptionPane.showMessageDialog(this, "El teléfono debe tener 10 digitos numéricos","Error",JOptionPane.ERROR_MESSAGE);
           return false;
       }
       else if(!Validar.numero_caracteres(50, campos[5].length())){ //Validar 50 caracteres direccion
           JOptionPane.showMessageDialog(this, "La dirección debe tener como máximo 50 digitos","Error",JOptionPane.ERROR_MESSAGE);
           return false;
       }
       else if(!Validar.numero_caracteres(30, campos[6].length())){ //Validar 30 caracteres correo
           JOptionPane.showMessageDialog(this, "El correo debe tener como máximo 30 digitos","Error",JOptionPane.ERROR_MESSAGE);
           return false;
       }
       return true;
   }
    
    @Override
    public void windowClosing(WindowEvent e) {
        usuarioActual.cerrar_sesion();
        this.v_princiapal.setVisible(true);
        this.dispose();
    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

}
