package PRESENTACION;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

public class VistaPrincipal extends javax.swing.JFrame implements WindowListener{
    String cedula;
    String pass;
    public VistaPrincipal(String cedula, String pass, JFrame v_autenticacion) {
        this.addWindowListener(this);
        initComponents();
        this.cedula=cedula; //Obtenemos la cedula de inicio
        this.pass=pass; //Obtenemos la contrasenia de pass
        if(!cedula.equals("admin")) this.btnAdministrar.setEnabled(false); //Si no es admin desabilitamos el boton
        else{
            this.cedula="system"; //Seteamos la cedula como system
            this.btnEncuestador.setEnabled(false); //Desactivamos botones innecesarios
            this.btnEncuestado.setEnabled(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAdministrar = new javax.swing.JToggleButton();
        btnEncuestador = new javax.swing.JToggleButton();
        btnEncuestado = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Menú");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAdministrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PRESENTACION/iconos/administrador.png"))); // NOI18N
        btnAdministrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdministrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdministrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 21, 150, 100));

        btnEncuestador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PRESENTACION/iconos/agregar-simbolo.png"))); // NOI18N
        btnEncuestador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncuestadorActionPerformed(evt);
            }
        });
        getContentPane().add(btnEncuestador, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 161, 150, 100));

        btnEncuestado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PRESENTACION/iconos/marca-de-verificacion.png"))); // NOI18N
        btnEncuestado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncuestadoActionPerformed(evt);
            }
        });
        getContentPane().add(btnEncuestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 301, 150, 100));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PRESENTACION/iconos/formulario-de-contacto.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 160, -1, -1));

        jLabel2.setText("Administrar");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 70, -1));

        jLabel3.setText("Crear");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 50, -1));

        jLabel4.setText("Responder");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 400, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 400, 10, 30));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 20));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdministrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdministrarActionPerformed
       this.setVisible(false); //Esconde la vista actual
       new VistaAdministrador(cedula, pass, this).setVisible(true); //Mostramos la vista de administrador
       this.btnAdministrar.setSelected(false); //Deseccionamos el boton que se presiono
    }//GEN-LAST:event_btnAdministrarActionPerformed

    private void btnEncuestadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncuestadorActionPerformed
        this.setVisible(false); //Escondemos la vista actual
        new VistaEncuestador(cedula, pass, this).setVisible(true); //Mostramos la vista encuestador
        this.btnEncuestador.setSelected(false); //Deseleccionamos el boton presionado
    }//GEN-LAST:event_btnEncuestadorActionPerformed

    private void btnEncuestadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncuestadoActionPerformed
        this.setVisible(false); //Escondemos la vista actual
        new VistaEncuestado(cedula, pass, this).setVisible(true); //Mostramos la vista de encuestado
        this.btnEncuestado.setSelected(false); //Deseleccionamos el boton presionado
    }//GEN-LAST:event_btnEncuestadoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnAdministrar;
    private javax.swing.JToggleButton btnEncuestado;
    private javax.swing.JToggleButton btnEncuestador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        new VistaAutenticacion().setVisible(true);
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
