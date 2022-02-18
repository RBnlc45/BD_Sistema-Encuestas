package PRESENTACION;

import NEGOCIO.CuestionariosDao;
import NEGOCIO.Objetos.Cuestionario;
import NEGOCIO.UsuariosDao;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import oracle.sql.TIMESTAMP;

public class VistaEncuestado extends javax.swing.JFrame implements WindowListener{
    private VistaPrincipal v_principal; //Instancia vista principal
    private String cedula; //Cedula usuario
    private CuestionariosDao CuestionariosDao; //CuestionariosDao
    private UsuariosDao usuarioActual; //Instancia UsuariosDao
    
    public VistaEncuestado(String cedula, String contrasenia, VistaPrincipal v_principal) { //Pasa cedula del usuario que va a responder las encuestas
        this.v_principal=v_principal; //Instancia vista principla para regresar a la vista anterior
        initComponents();
        this.addWindowListener(this);
        this.cedula=cedula; //Cedula encuestado
        //DAO
        this.CuestionariosDao=new CuestionariosDao(); //Manejador de cuestionarios
        this.usuarioActual=new UsuariosDao(cedula,contrasenia); //Instancia Usuario Actual
        this.setVisible(true);
        //CONFIGURACION DE VISTA INICIAL
        this.RefrescarTabla(this.jgdCDisponibles, CuestionariosDao.cargar_cuestionarios_disponibles()); //Cuestionarios disponibles
        this.RefrescarTabla(this.jgdCResueltos, CuestionariosDao.cargar_cuestionarios_resueltos(this.cedula)); //Cuestionarios resueltos
        this.lblCedulaResolver.setText(this.cedula); //Seteamos lbl de cedula en vista Resolver
        this.lblCedulaResueltos.setText(this.cedula); //Seteamos lbl de cedula en vista Resueltos
    }
    
    public void RefrescarTabla(JTable tabla, LinkedList<Cuestionario> contenedor){
        if(tabla==this.jgdCDisponibles && contenedor==null){ //Si el contenedor de cuestionarios disponibles es null
            JOptionPane.showMessageDialog(this,"No tiene los permisos suficientes para visualizar los cuestionarios disponibles","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }else if(tabla==this.jgdCResueltos && contenedor==null){ //Si el contenedor de cuestionarios resueltos es null
            JOptionPane.showMessageDialog(this,"No tiene los permisos suficientes para visualizar los cuestionarios resueltos","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model=(DefaultTableModel) tabla.getModel(); 
        int size=model.getRowCount(); //Elimina las filas si existen en la tabla
        for(int i=size-1;i>=0;i--){ //borra toda la tabla
        	model.removeRow(i);
        }
        for(int i=0; i<contenedor.size();i++){ //Insertamos fila por fila
        	model.insertRow(model.getRowCount(), new Object[] {});
        	model.setValueAt(contenedor.get(i).getIdCuestionario(),  model.getRowCount()-1, 0);
                model.setValueAt(contenedor.get(i).getFechaCreacion(),  model.getRowCount()-1, 1);
                model.setValueAt(contenedor.get(i).getTitulo(),  model.getRowCount()-1, 2);
                model.setValueAt(contenedor.get(i).getDescripcion(),  model.getRowCount()-1, 3);
                model.setValueAt(contenedor.get(i).getComentario(),  model.getRowCount()-1, 4);   
        }
    }
    
    public boolean VerificarResuelto(int idCuestionario){ //Verifica si no ha resuelto el cuestionario previamente
        for(Cuestionario c: CuestionariosDao.cargar_cuestionarios_resueltos(this.cedula)){ //Recorremos la lista de cuestionarios resueltos y los comparamoscon el que se quiere resolver
            if(c.getIdCuestionario()==idCuestionario) return true; //Ya resolvio el cuestionario
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneEncuestado = new javax.swing.JTabbedPane();
        grbResolver = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblCedulaResolver = new javax.swing.JLabel();
        btnResolver = new javax.swing.JButton();
        jScrollPaneCD = new javax.swing.JScrollPane();
        jgdCDisponibles = new javax.swing.JTable();
        lblCDisponibles = new javax.swing.JLabel();
        btnPerfilB = new javax.swing.JButton();
        grbResueltos = new javax.swing.JPanel();
        grbCuestionariosResueltos = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPaneCR = new javax.swing.JScrollPane();
        jgdCResueltos = new javax.swing.JTable();
        lblCedulaResueltos = new javax.swing.JLabel();
        btnRevisar = new javax.swing.JButton();
        btnPerfil = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Encuestado");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grbResolver.setBackground(new java.awt.Color(204, 204, 204));
        grbResolver.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCedulaResolver.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCedulaResolver.setText("0123456789");

        btnResolver.setText("Resolver Cuestionario");
        btnResolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResolverActionPerformed(evt);
            }
        });

        jgdCDisponibles.getTableHeader().setReorderingAllowed(false);
        jgdCDisponibles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idCuestionario", "Fecha", "Título", "Descripción", "Comentario"
            }
        ));
        jScrollPaneCD.setViewportView(jgdCDisponibles);
        if (jgdCDisponibles.getColumnModel().getColumnCount() > 0) {
            jgdCDisponibles.getColumnModel().getColumn(0).setPreferredWidth(35);
            jgdCDisponibles.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        lblCDisponibles.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblCDisponibles.setText("CUESTIONARIOS DISPONIBLES");

        btnPerfilB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerfilActionPerformed(evt);
            }
        });
        btnPerfilB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PRESENTACION/iconos/perfil.png"))); // NOI18N
        btnPerfilB.setFocusable(false);
        btnPerfilB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPerfilB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCDisponibles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCedulaResolver)
                .addGap(18, 18, 18)
                .addComponent(btnPerfilB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPaneCD, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(256, 256, 256)
                .addComponent(btnResolver, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCDisponibles)
                            .addComponent(lblCedulaResolver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(17, 17, 17))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnPerfilB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPaneCD, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnResolver, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        grbResolver.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 20, 740, 380));

        jTabbedPaneEncuestado.addTab("RESOLVER CUESTIONARIO", grbResolver);

        grbResueltos.setBackground(new java.awt.Color(204, 204, 204));
        grbResueltos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grbCuestionariosResueltos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel7.setText("CUESTIONARIOS RESUELTOS");
        grbCuestionariosResueltos.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jgdCResueltos.getTableHeader().setReorderingAllowed(false);
        jgdCResueltos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idCuestionario", "Fecha", "Título", "Descripción", "Comentario"
            }
        ));
        jScrollPaneCR.setViewportView(jgdCResueltos);
        if (jgdCResueltos.getColumnModel().getColumnCount() > 0) {
            jgdCResueltos.getColumnModel().getColumn(0).setPreferredWidth(35);
            jgdCResueltos.getColumnModel().getColumn(1).setPreferredWidth(40);
            jgdCResueltos.getColumnModel().getColumn(2).setPreferredWidth(60);
        }

        grbCuestionariosResueltos.add(jScrollPaneCR, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 740, 270));

        lblCedulaResueltos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCedulaResueltos.setText("0123456789");
        grbCuestionariosResueltos.add(lblCedulaResueltos, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 10, -1, 20));

        btnRevisar.setText("Revisar Cuestionario");
        btnRevisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevisarActionPerformed(evt);
            }
        });
        grbCuestionariosResueltos.add(btnRevisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 340, 170, 30));

        btnPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerfilActionPerformed(evt);
            }
        });
        btnPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PRESENTACION/iconos/perfil.png"))); // NOI18N
        btnPerfil.setFocusable(false);
        btnPerfil.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPerfil.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        grbCuestionariosResueltos.add(btnPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, 40, 40));

        grbResueltos.add(grbCuestionariosResueltos, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 20, 740, 380));

        jTabbedPaneEncuestado.addTab("CUESTIONARIOS RESUELTOS", grbResueltos);

        getContentPane().add(jTabbedPaneEncuestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 452));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //Revisar cuestionario
    private void btnRevisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevisarActionPerformed
        int indice=this.jgdCResueltos.getSelectedRow();
        if(indice==-1){ //Si no selecciona una fila
            JOptionPane.showMessageDialog(this, "Primero seleccione un cuestionario de la lista", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        Cuestionario cuestionario = new Cuestionario(); //Instancia cuestionario a revisar y lo inicia con los datos de la fila
        cuestionario.setIdCuestionario((Integer)this.jgdCResueltos.getModel().getValueAt(indice, 0));
        cuestionario.setFechaCreacion((TIMESTAMP) this.jgdCDisponibles.getModel().getValueAt(indice, 1));
        cuestionario.setTitulo((String) this.jgdCDisponibles.getModel().getValueAt(indice, 2));
        cuestionario.setDescripcion((String) this.jgdCDisponibles.getModel().getValueAt(indice, 3));
        cuestionario.setComentario((String) this.jgdCDisponibles.getModel().getValueAt(indice, 4));
        new VistaRevisarCuestionario(this.cedula,cuestionario,this,true).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnRevisarActionPerformed

    //Resolver cuestionario
    private void btnResolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResolverActionPerformed
        int indice=this.jgdCDisponibles.getSelectedRow();
        if(indice!=-1){ //Si ha seleccionado un cuestionario
            int idCuestionario=(int) this.jgdCDisponibles.getModel().getValueAt(indice, 0);
            TIMESTAMP fecha= (TIMESTAMP) this.jgdCDisponibles.getModel().getValueAt(indice, 1);
            String  titulo=(String) this.jgdCDisponibles.getModel().getValueAt(indice, 2);
            String descripcion=(String) this.jgdCDisponibles.getModel().getValueAt(indice, 3);
            String comentario=(String) this.jgdCDisponibles.getModel().getValueAt(indice, 4);
            Cuestionario cuestionario=new Cuestionario(idCuestionario,fecha,titulo,descripcion,comentario,""); //Instancia cuestionario con los datos de la tabla de cuestionarios disponibles para resolver
            if(CuestionariosDao.restriccion_cuestionario_resuelto(this.cedula, idCuestionario)){ //Si no ha resuelto el cuestionario
                this.setVisible(false);
                new VistaResolverCuestionario(this.cedula,cuestionario,this).setVisible(true); //Muestra la vista para resolver el cuestionario
            }else{ //Si ha resuelto el cuestionario
                JOptionPane.showMessageDialog(this, "Solo se permite un intento por cuestionario","Error",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Primero seleccione un cuestionario de la tabla de cuestionarios disponibles","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnResolverActionPerformed

    //Muestra la vista pera revisar los datos del usuario
    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt){
        this.setVisible(false);
        new VistaEditarUsuario(new UsuariosDao().recuperar_usuario(this.cedula),this.v_principal,null,null,this).setVisible(true);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPerfil;
    private javax.swing.JButton btnPerfilB;
    public javax.swing.JButton btnResolver;
    public javax.swing.JButton btnRevisar;
    private javax.swing.JPanel grbCuestionariosResueltos;
    public javax.swing.JPanel grbResolver;
    private javax.swing.JPanel grbResueltos;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPaneCD;
    private javax.swing.JScrollPane jScrollPaneCR;
    public javax.swing.JTabbedPane jTabbedPaneEncuestado;
    public javax.swing.JTable jgdCDisponibles;
    public javax.swing.JTable jgdCResueltos;
    private javax.swing.JLabel lblCDisponibles;
    public javax.swing.JLabel lblCedulaResolver;
    public javax.swing.JLabel lblCedulaResueltos;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        usuarioActual.cerrar_sesion(); //Cierra la sesion
        v_principal.setVisible(true); //Vista principal
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
