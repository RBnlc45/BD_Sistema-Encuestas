
package PRESENTACION;
import NEGOCIO.Validar;
import NEGOCIO.Objetos.Cuestionario;
import NEGOCIO.Objetos.Usuario;
import NEGOCIO.UsuariosDao;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class VistaEditarUsuario extends javax.swing.JFrame implements WindowListener{
    private Usuario usuario; //Referencia de usuario
    private VistaPrincipal v_principal; //Referencia vista principal
    private VistaAdministrador v_admin; //Referencia vista administrador
    private VistaEncuestador v_encuestador; //Referencia vista encuestador
    private VistaEncuestado v_encuestado; //Referencia vista encuestado
    private String pass; //Campo para contrasenia

    //Las referencias de las ventanas se envian desde la ventana en la cual se instancia esta ventana
    //Las otras referencias se envian null excepto la vista principal que sirve para actualizar el campo de contrasenia en caso de que se actualice la contrasenia
    public VistaEditarUsuario(Usuario usuario, VistaPrincipal v_principal,VistaAdministrador v_admin, VistaEncuestador v_encuestador, VistaEncuestado v_encuestado) {
        this.v_principal=v_principal; //Referencia vista principal
        initComponents();
        this.addWindowListener(this); //Maneja el cierre de la ventana
        this.usuario=usuario; //Usuario a editar
        this.v_admin=v_admin; //Referencia vista administrador
        this.v_encuestador=v_encuestador; //Referencia vista encuestador
        this.v_encuestado=v_encuestado; //Referencia vista encuestado
        this.pass=usuario.getPassword(); //Obtenemos el pass del usuario
        this.ConfigurarTextos(); //Para mostrar datos de usuario en los textos
        
    }
    
    public void DesactivarTextos(){ //Desactiva todos los txt de la vista para no dejar hacer cambios util en el caso de que no pueda visivilizar os datos de usuario
        this.txtContrasenia.setEnabled(false);
        this.txtNombre.setEnabled(false);
        this.txtApellido.setEnabled(false);
        this.txtDireccion.setEnabled(false);
        this.txtTelefono.setEnabled(false);
        this.txtCorreo.setEnabled(false);
    } //Desactiva todos los txt
    
    public void ConfigurarTextos(){ //Configura los textos de la vista con los datos del usuario
        this.setVisible(true);
        if(!(usuario.getCedula()!=null)){ //En el caso de que no pueda visualizar datos de usuario
           this.DesactivarTextos();
           JOptionPane.showMessageDialog(this,"No tiene los permisos suficientes para mostrar los datos de usuario","Error",JOptionPane.ERROR_MESSAGE); 
           return;
        }    
        this.txtCedula.setText(usuario.getCedula()); //Seteamos todos los datos
        this.txtContrasenia.setText(usuario.getPassword());
        this.txtNombre.setText(usuario.getNombre());
        this.txtApellido.setText(usuario.getApellido());
        this.txtTelefono.setText(usuario.getTelefono());
        this.txtDireccion.setText(usuario.getDireccion());
        this.txtCorreo.setText(usuario.getMail());
    } //Configura los textos de la vista con los datos del usuario

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grbEdicionUsuarios = new javax.swing.JPanel();
        grbElementosCrear = new javax.swing.JPanel();
        btnEditar = new javax.swing.JButton();
        grbLogin = new javax.swing.JPanel();
        txtCedula = new javax.swing.JTextField();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Edición");

        grbEdicionUsuarios.setBackground(new java.awt.Color(204, 204, 204));

        btnEditar.setText("Aceptar");
        btnEditar.setEnabled(false);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        grbLogin.setBorder(javax.swing.BorderFactory.createTitledBorder("Login"));

        txtCedula.setEnabled(false);

        txtContrasenia.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt){
                txtActionPerformed(evt);
            }
        });

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
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(243, Short.MAX_VALUE))
        );
        grbLoginLayout.setVerticalGroup(
            grbLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbLoginLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(grbLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCedula)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(grbLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombre2))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        grbDatosAdicionales.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Adicionales"));

        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt){
                txtActionPerformed(evt);
            }
        });

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt){
                txtActionPerformed(evt);
            }
        });

        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt){
                txtActionPerformed(evt);
            }
        });

        txtCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt){
                txtActionPerformed(evt);
            }
        });

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt){
                txtActionPerformed(evt);
            }
        });

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
                .addGroup(grbElementosCrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(grbElementosCrearLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(grbElementosCrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(grbLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(grbDatosAdicionales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(grbElementosCrearLayout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        grbElementosCrearLayout.setVerticalGroup(
            grbElementosCrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbElementosCrearLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(grbLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(grbDatosAdicionales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout grbEdicionUsuariosLayout = new javax.swing.GroupLayout(grbEdicionUsuarios);
        grbEdicionUsuarios.setLayout(grbEdicionUsuariosLayout);
        grbEdicionUsuariosLayout.setHorizontalGroup(
            grbEdicionUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbEdicionUsuariosLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(grbElementosCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        grbEdicionUsuariosLayout.setVerticalGroup(
            grbEdicionUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbEdicionUsuariosLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(grbElementosCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(grbEdicionUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(grbEdicionUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //Accion para aceptar la edicion
    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if(!(this.usuario.getCedula()!=null)){ //Si no puede visualizar los datos del usuario
           JOptionPane.showMessageDialog(this,"No tiene los permisos suficientes para realizar esta acción","Error",JOptionPane.ERROR_MESSAGE); 
           return;
        }  
        String[] campos=new String[]{this.txtCedula.getText(),this.txtNombre.getText(),this.txtApellido.getText(),
            this.txtContrasenia.getText(), this.txtTelefono.getText(), this.txtDireccion.getText(),
            this.txtCorreo.getText()};//Se obtienen los campos actualizados
        String mensaje=Validar.CamposDatosUsuario(campos);//Se validan los datos
        if(mensaje!=null){//Si falla la validación de campos de usuario
            JOptionPane.showMessageDialog(this, mensaje,"Error",JOptionPane.ERROR_MESSAGE);
            this.btnEditar.setEnabled(false);
        } 
        Usuario usuario=new Usuario(campos[0],campos[1],campos[2],campos[3],campos[4],campos[5],campos[6]);//Se crea la instancia de usuario 
        if(!new UsuariosDao().editar_usuario(usuario)){ //Si no se realiza el cambio de datos
            JOptionPane.showMessageDialog(this,"No tiene los permisos suficientes para realizar esta acción","Error",JOptionPane.ERROR_MESSAGE);
            return;
        } //False si no se realizo la edicion
        this.btnEditar.setEnabled(false);//Se deshabilita el botón
        pass=usuario.getPassword();//Actualización de contraseña
        if(!this.v_principal.cedula.equalsIgnoreCase("system")) this.v_principal.pass=pass; //Si lse ha llamado a la ventana editar desde administrador no se actualiza el campo de contrasenia
        JOptionPane.showMessageDialog(this,"Datos actualizados correctamente");
    }//GEN-LAST:event_btnEditarActionPerformed

    //Accion al escribir
    private void txtActionPerformed(java.awt.event.KeyEvent evt){ //Capta eventos de cualquier campo de texto para que solo cuando se modifique un campo se active el boton para aceptar los cambios
        if(!this.btnEditar.isEnabled()) this.btnEditar.setEnabled(true);//Cambio de estado del botón para editar los datos
    } 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JPanel grbDatosAdicionales;
    private javax.swing.JPanel grbEdicionUsuarios;
    private javax.swing.JPanel grbElementosCrear;
    private javax.swing.JPanel grbLogin;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombre1;
    private javax.swing.JLabel lblNombre2;
    private javax.swing.JLabel lblNombre3;
    private javax.swing.JLabel lblNombre4;
    private javax.swing.JLabel lblNombre5;
    public javax.swing.JTextField txtApellido;
    public javax.swing.JTextField txtCedula;
    public javax.swing.JTextField txtContrasenia;
    public javax.swing.JTextField txtCorreo;
    public javax.swing.JTextField txtDireccion;
    public javax.swing.JTextField txtNombre;
    public javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        if(v_admin!=null){
            this.v_admin.MostrarTabla(v_admin.jgdUsuarios, v_admin.usuarioActual.recuperar_usuarios(new Usuario())); //Muestra la tabla de usuarios actualizada
            this.v_admin.setVisible(true); //Muestra la ventana de administrador
        }else if(v_encuestador!=null){ //Si no es null se llamo a esta ventana desde la ventana encuestador
            //Refrescamos la tabla de usuario para mostrar los cambios en los datos del usuario
            if(v_encuestador.cbxidCuestionario.getSelectedItem()!=null) this.v_encuestador.RefrescarTablaUsuarios(v_encuestador.CuestionariosDao.cargar_usuarios_cuestionario(((Cuestionario)v_encuestador.cbxidCuestionario.getSelectedItem()).getIdCuestionario()));
            else this.v_encuestador.RefrescarTablaUsuarios(new LinkedList()); //En el caso de que sea null la seleccion del combo no se actualiza
            this.v_encuestador.setVisible(true); //Muestra la vista del encuestador
        }else if(v_encuestado!=null){ //Si no es null se llamo a esta ventana desde la ventana encuestado
           this.v_encuestado.setVisible(true); //Muestra la vista del encuestado
        }
        this.dispose();
    }
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
