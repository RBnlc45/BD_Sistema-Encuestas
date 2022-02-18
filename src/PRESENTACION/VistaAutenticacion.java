
package PRESENTACION;

import NEGOCIO.Validar;
import NEGOCIO.UsuariosDao;
import javax.swing.JOptionPane;

public class VistaAutenticacion extends javax.swing.JFrame{
    UsuariosDao UsuariosDao;

    public VistaAutenticacion() {
        initComponents();
        this.UsuariosDao=new UsuariosDao("System","1234");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grbLogin = new javax.swing.JPanel();
        grbPrincipal = new javax.swing.JPanel();
        lblContrasenia = new javax.swing.JLabel();
        lblCedula = new javax.swing.JLabel();
        btnIngresar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        txtCedula = new javax.swing.JTextField();
        txtContrasenia = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Iniciar Sesión");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grbLogin.setBackground(new java.awt.Color(204, 204, 204));
        grbLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        grbLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grbPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblContrasenia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblContrasenia.setText("Contraseña:");
        grbPrincipal.add(lblContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, -1));

        lblCedula.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCedula.setText("Cédula:");
        grbPrincipal.add(lblCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, 20));

        btnIngresar.setText("Ingresar");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        grbPrincipal.add(btnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 90, -1));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        grbPrincipal.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, 90, -1));

        txtCedula.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        grbPrincipal.add(txtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 150, -1));

        txtContrasenia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        grbPrincipal.add(txtContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 150, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PRESENTACION/iconos/login.png"))); // NOI18N
        grbPrincipal.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 70, 60));

        grbLogin.add(grbPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 350, 200));

        getContentPane().add(grbLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 390, 240));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        String pass=new String(this.txtContrasenia.getPassword()); //Contrasenia
        String[] campos=new String[]{this.txtCedula.getText(),pass}; //Cedula y contrasenia
        if(Validar.campos_vacios(campos).equals("Error")){
            JOptionPane.showMessageDialog(this, "No puede dejar campos vacios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } //Valida campos vacios
        else if(!(campos[0].equalsIgnoreCase("admin")) && !Validar.cedula(campos[0])){
            JOptionPane.showMessageDialog(this, "Cedula incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            return;   
        } //Valida la cedula ingresada
        if(!campos[0].equalsIgnoreCase("admin") && !UsuariosDao.comprobar_usuario(campos[0],campos[1])){
           JOptionPane.showMessageDialog(this, "Cedula o contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } //Verifica usuario y contrasenia
        if(campos[0].equalsIgnoreCase("admin") && !campos[1].equalsIgnoreCase("1234")){
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } //Verificaion admin
        new VistaPrincipal(campos[0],campos[1],this).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnIngresarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnIngresar;
    public javax.swing.JButton btnSalir;
    private javax.swing.JPanel grbLogin;
    private javax.swing.JPanel grbPrincipal;
    private javax.swing.JLabel jLabel1;
    public javax.swing.JLabel lblCedula;
    public javax.swing.JLabel lblContrasenia;
    public javax.swing.JTextField txtCedula;
    public javax.swing.JPasswordField txtContrasenia;
    // End of variables declaration//GEN-END:variables

}
