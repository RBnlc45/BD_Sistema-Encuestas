package PRESENTACION;

import NEGOCIO.Objetos.Cuestionario;
import NEGOCIO.Objetos.Opcion;
import NEGOCIO.Objetos.Pregunta;
import NEGOCIO.Objetos.PreguntaAbierta;
import NEGOCIO.Objetos.PreguntaMultiple;
import NEGOCIO.Objetos.Responde;
import NEGOCIO.Objetos.Selecciona;
import NEGOCIO.OpcionesDao;
import NEGOCIO.PreguntasDao;
import NEGOCIO.RespuestasDao;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class VistaResolverCuestionario extends javax.swing.JFrame implements WindowListener{
    private VistaEncuestado v_anterior;
    private String cedula; //Cedula del encuestado
    private int orden=1;
    private LinkedList<Object> lista_preguntas;
    private Map<Integer, LinkedList<Opcion>> mapa_opciones;
    private RespuestasDao RespuestasDao;
    private Cuestionario cuestionario;
    private JTextArea txt;
    
    public VistaResolverCuestionario(String cedula,Cuestionario cuestionario, VistaEncuestado vista) {
        initComponents();
        this.addWindowListener(this);
        this.txt=new JTextArea(70,40);
        this.v_anterior=vista;
        //DATOS ENCUESTA/ENCUESTADO
        this.cedula=cedula; //Cedula del encuestado
        this.cuestionario=cuestionario;//cuestionario que se está contestando
        int idCuestionario=cuestionario.getIdCuestionario();//id del cuestionario a resolver
        //LBLS/BUTTONS/Inicialización de contenido del cuestionario
        this.lbliDCuestionario.setText(idCuestionario+""); //Seteamos el lbl del id del cuestionario
        this.lblTitulo.setText(cuestionario.getTitulo()); //Seteamos el lbl del titulo
        this.lblDescripcion.setText(cuestionario.getDescripcion()); //Seteamos el lbl de la descripcion del cuestionario
        this.lblFechaCreacion.setText(cuestionario.getFechaCreacion().toString()); //Seteamos el lbl de la fecha de creacion
        this.lblCometario.setText(cuestionario.getComentario()); //Serear el lbl de Comentario
        this.btnAnterior.setEnabled(false); //El boton para regresar inicia desactivado
        //PREGUNTAS/OPCIONES
        this.lista_preguntas=new PreguntasDao().cargar_preguntas(idCuestionario); //Obtengo lista de objetos preguntas para un idCuestionario dado       
        this.mapa_opciones=new OpcionesDao().cargar_opciones(idCuestionario, lista_preguntas);//Se obtienen las opciones de las preguntas de selección
        //DAO RESPUESTAS
        this.RespuestasDao=new RespuestasDao(); //Para obtener las respuestas
        //PROCESOS
        if(this.lista_preguntas!=null && this.mapa_opciones!=null){
            this.MostrarPregunta(); //Muestra la pregunta
            this.Botones(); //Configura botones
        }else{
            this.setVisible(true); //Mostramos esta ventana 
            this.grbEncabezado.setVisible(false); //Escondemos el encabezado del cuestionario
            this.grbPregunta.setVisible(false); //Escondemos el panel de pregunta
            this.btnSiguiente.setEnabled(false); //Desactivamos el boton de siguiente
            JOptionPane.showMessageDialog(this,"No tiene los permisos suficientes para visualizar el cuestionario","Error",JOptionPane.ERROR_MESSAGE);
        }        
    }
            
    public void Botones(){ //Maneja activacion y desactivacion boton anterior siguiente y finalizar
        if(orden==1 && orden!=lista_preguntas.size()){ //Si es la primera y no ultima pregunta
            this.btnAnterior.setEnabled(false);
            this.btnSiguiente.setEnabled(true);
            this.btnFinalizar.setEnabled(false);
        }else if(orden==lista_preguntas.size() &&lista_preguntas.size()!=1){ //Si el numero de pregunta es igual al numero de preguntas en la lista y el tamanio de la lista no es uno
            this.btnSiguiente.setEnabled(false);
            this.btnAnterior.setEnabled(true);
            this.btnFinalizar.setEnabled(true);
        }else if(this.lista_preguntas.size()==1){ //Si la lista de preguntas no tiene una sola pregunta
            this.btnAnterior.setEnabled(false);
            this.btnSiguiente.setEnabled(false);
            this.btnFinalizar.setEnabled(true);
        }else{ //Estamos en pregunta intermedia
            btnAnterior.setEnabled(true); //Se abilitan ambos botones de navegacion
            btnSiguiente.setEnabled(true);
            this.btnFinalizar.setEnabled(false); //Se desabilita el boton de finalizar
        }
    }
    
    public void MostrarPregunta(){
        this.lblObligatorio.setVisible(false); //Inicia con lbl de obligatorio en invisible despues se analiza si es necesario mostrarlo
        //DATOS PREGUNTA
        Object pregunta_obj=this.lista_preguntas.get(orden-1); //Se obtiene la pregunta en la posicion deseada
        String clase=pregunta_obj.getClass().getSimpleName(); //Se obtiene la clase de la pregunta
        this.lblEnunciado.setText(((Pregunta)pregunta_obj).getEnunciado()); //Se setea el texto del enunciado
        //OPCIONES PREGUNTA
        LinkedList<Opcion> lista_opciones=this.mapa_opciones.get(orden); //obtenemos la lista de opciones de la pregunta actual
        //RESPUESTAS PREGUNTA
        LinkedList<Object> lista_respuestas=this.RespuestasDao.recuperar_selecciones(orden); //Obtenemos la lista de respuestas de la pregunta actual       
        //PRESENTAR PREGUNTA
        if(clase.equals("PreguntaMultiple")){ //En caso de que sea de una opcion multiple
            PreguntaMultiple pregunta_multiple=(PreguntaMultiple)pregunta_obj; //Casteamos el objeto pregunta a pregunta de tipo multiple
            if(pregunta_multiple.getObligatoria().equals("V")) this.lblObligatorio.setVisible(true); //Si es obligatoria mostramos el lbl con mensaje obligatoria
            if(pregunta_multiple.getTipo().equals("Unica") || pregunta_multiple.getTipo().equals("V/F")){ //Si es vf o unica
                this.AgregarOpcionesRespuestaUnica(lista_opciones,lista_respuestas,pregunta_multiple.getTipo()); //Agregamos las opciones en tipo radio button
            }else{ //Si son opciones de multiple seleccion
                this.AgregarOpcionesRespuestaMultiple(lista_opciones, lista_respuestas,pregunta_multiple.getTipo()); //Agregamos las opciones en tipo chech box
            }
        }else if(clase.equals("PreguntaAbierta")){ //En caso de que sea pregunta abierta
            if(((PreguntaAbierta)pregunta_obj).getObligatoria().equals("V")) this.lblObligatorio.setVisible(true); //Si es obligatoria mostramos el lbl con mensaje obligatoria
            this.AgregarAreaRespuestaAbierta(lista_respuestas, "Abierta"); //Se agrega el txt de pregunta abierta 
        }
    }
       
    public void AgregarOpcionesRespuestaUnica(LinkedList<Opcion> lista_opciones, LinkedList<Object> lista_respuestas, String tipo){
        ButtonGroup rbtg=new ButtonGroup(); //Grupo de botones
        for(Opcion o: lista_opciones){ //Recorremos la lista de opciones
            String opcion=o.getContenido(); //Obtenemos el texto de la opcion
            JRadioButton rbt=new JRadioButton(opcion); //Creamos boton con el texto de la opcion
            if(lista_respuestas!=null){ //Si hay respuesta para esta pregunta
                for(Object r: lista_respuestas){ //Recorremos la lista de respuestas
                    if(r.getClass().getSimpleName().equals("Selecciona")){ //Si es un objeto instancia de selecciona
                        Selecciona seleccion=(Selecciona)r; //Casteamos a el objeto a la clase selecciona
                        if(o.getIdOpcion()==seleccion.getIdOpcion()){ //Si los ids de opcion y de selecciona coinciden
                            rbt.setSelected(true); //Marcamos la respuesta
                        }
                    }
                }
            }
            rbtg.add(rbt); //Agregamos el radio button al grupo de radio buttons
            rbt.addActionListener(new java.awt.event.ActionListener() { //Agregamos listener al radio button
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    RespuestasDao.actualizar_seleccion(new Selecciona(cedula,o.getIdOpcion()), lista_opciones, tipo, orden); //Si se presiona se manda a actualizar la seleccion
                }
            });        
            this.grbOpciones.add(rbt); //Agregamos el radio button al panel
        }
    }
    
    public void AgregarOpcionesRespuestaMultiple(LinkedList<Opcion> lista_opciones, LinkedList<Object> lista_respuestas, String tipo){
        for(Opcion o: lista_opciones){
            String opcion=o.getContenido();
            JCheckBox cbx=new JCheckBox(opcion); //Instanciamos un check box
            if(lista_respuestas!=null){ //Si hay respuesta para esta pregunta
                for(Object r: lista_respuestas){ //Recorremos las respuestas
                    if(r.getClass().getSimpleName().equals("Selecciona")){ //Si es un objeto selecciona
                        Selecciona seleccion=(Selecciona)r; //Casteamos el objeto a la clase selecciona
                        if(o.getIdOpcion()==seleccion.getIdOpcion()){ //En caso de que el idOpcion coincida con el id de la seleccion
                            cbx.setSelected(true); //Marcamos la opcion
                        }
                    }
                }
            }
            cbx.addActionListener(new java.awt.event.ActionListener() { //Agregamos listener a check box
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    RespuestasDao.actualizar_seleccion(new Selecciona(cedula,o.getIdOpcion()),lista_opciones,tipo,orden); //En caso que se presione el checkbox se manda a actualizar la seleccion
                }
            });         
            this.grbOpciones.add(cbx); //Agregamos el checkbox a el panel de opciones
        }
    }
    
    public void AgregarAreaRespuestaAbierta(LinkedList<Object> lista_respuestas, String tipo){
        txt.setText(""); //Borramos el contenido anterior del txt
        txt.setLineWrap(true); //Activamos el salto de linea al llegar al borde
        if(lista_respuestas!=null){ //Si hay respuesta para esta pregunta
            for(Object r: lista_respuestas){ //Recorremos la lista de respuestas
                if(r.getClass().getSimpleName().equals("Responde")){ //Si es instancia de la clase responde
                    Responde respuesta=(Responde)r; //Casteamos el objeto a la clase responde
                    txt.setText(respuesta.getRespuesta()); //Setea la respuesta
                }
            }
        }
        txt.addKeyListener(new java.awt.event.KeyListener() { //agregamos listener al txt
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) { //Al mantener presionado
                if(txt.getText().length()>300 && e.getKeyCode()!=KeyEvent.VK_DELETE){ //Si la longitud del texto es mayor que 300
                    txt.setText(txt.getText().substring(0, 300)); //Se toma solo los primeros 300 caracteres
                }
            }
            @Override
            public void keyReleased(KeyEvent e) { //Al subir la tecla
                if(txt.getText().length()<=300 && e.getKeyChar()!=13){ //Si el texto tiene menos de 300 caracteres
                    RespuestasDao.actualizar_seleccion(new Responde(cedula,((Pregunta)lista_preguntas.get(orden-1)).getIdPregunta(),txt.getText()),null,tipo,orden);               
                }else if(txt.getText().length()>300 && e.getKeyCode()!=KeyEvent.VK_DELETE){
                    JOptionPane.showMessageDialog(grbPregunta, "Se permiten máximo 300 caracteres como respuesta");
                    txt.setText(txt.getText().substring(0, txt.getText().length()-1));
                    
                }
            }
        });
        this.grbOpciones.add(txt);
    }
    
    private boolean ComprobarRespuestaObligatoria(){ //Verifica si respondio la pregunta obligatoria
        Component[] componentes=this.grbOpciones.getComponents(); //Recupera los componentes de opcion
        boolean flag=false; //Para validar seleccion
        for(Component c: componentes){ //Recorremos los componentes del panel de opcion
            if(c.getClass().getSimpleName().equals("JCheckBox")){ //Si es un check box se lo castea a esa clase
                if(((JCheckBox)c).isSelected()) flag=true; //Si esta seleccionado se hace true
            }else if(c.getClass().getSimpleName().equals("JRadioButton")){ //Si es un JRadio se lo castea a esa clase
                if(((JRadioButton)c).isSelected()) flag=true; //Si esta seleccionado se hace true
            }else if(c.getClass().getSimpleName().equals("JTextArea")){ //Si es un textArea
                if(((JTextArea)c).getText().trim().length()>0) flag=true; //Se lo castea a txtArea y se comprueba si tiene texto
            }
        }
        return flag; //True si estaba seleccionado
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grbPrincipal = new javax.swing.JPanel();
        grbEncabezado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblIDCuestionarioB = new javax.swing.JLabel();
        lblFechaCreacionB = new javax.swing.JLabel();
        lblDescripcionB = new javax.swing.JLabel();
        lblFechaCreacion = new javax.swing.JLabel();
        lblTituloB1 = new javax.swing.JLabel();
        lbliDCuestionario = new javax.swing.JLabel();
        lblFechaCreacionB1 = new javax.swing.JLabel();
        lblDescripcion = new javax.swing.JLabel();
        lblCometario = new javax.swing.JLabel();
        grbBotones = new javax.swing.JPanel();
        btnAnterior = new javax.swing.JButton();
        btnFinalizar = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        grbPregunta = new javax.swing.JPanel();
        lblEnunciado = new javax.swing.JLabel();
        grbOpciones = new javax.swing.JPanel();
        lblObligatorio = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Resolver Cuestionario");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grbPrincipal.setBackground(new java.awt.Color(204, 204, 204));
        grbPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grbEncabezado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        grbEncabezado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTitulo.setText("Título de la encuesta");
        grbEncabezado.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, 20));

        lblIDCuestionarioB.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblIDCuestionarioB.setText("idCuestionario:");
        grbEncabezado.add(lblIDCuestionarioB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        lblFechaCreacionB.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblFechaCreacionB.setText("Fecha de creación:");
        grbEncabezado.add(lblFechaCreacionB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        lblDescripcionB.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblDescripcionB.setText("Descripción:");
        grbEncabezado.add(lblDescripcionB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        lblFechaCreacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblFechaCreacion.setText("10/09/2000");
        grbEncabezado.add(lblFechaCreacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, -1, 20));

        lblTituloB1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblTituloB1.setText("Título:");
        grbEncabezado.add(lblTituloB1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        lbliDCuestionario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbliDCuestionario.setText("ID del cuestionario a resolver");
        grbEncabezado.add(lbliDCuestionario, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 160, 20));

        lblFechaCreacionB1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblFechaCreacionB1.setText("Comentario:");
        grbEncabezado.add(lblFechaCreacionB1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        lblDescripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDescripcion.setText("Conocer cuales son los principios de diseño SOLID");
        grbEncabezado.add(lblDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 420, 20));

        lblCometario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblCometario.setText("Es un cuestionario herramienta para el análisis de conocimientos");
        grbEncabezado.add(lblCometario, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, -1, 20));

        grbPrincipal.add(grbEncabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 590, 120));

        grbBotones.setBackground(new java.awt.Color(204, 204, 204));
        grbBotones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAnterior.setText("Anterior");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        grbBotones.add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, 80, 30));

        btnFinalizar.setText("Finalizar");
        btnFinalizar.setEnabled(false);
        btnFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarActionPerformed(evt);
            }
        });
        grbBotones.add(btnFinalizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, -1, 30));

        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        grbBotones.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 90, 30));

        grbPrincipal.add(grbBotones, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 590, 30));

        grbPregunta.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregunta Nº1"));
        grbPregunta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEnunciado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblEnunciado.setText("¿Que entiende por SOLID?");
        grbPregunta.add(lblEnunciado, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));
        grbPregunta.add(grbOpciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 550, 220));

        lblObligatorio.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        lblObligatorio.setForeground(new java.awt.Color(255, 51, 0));
        lblObligatorio.setText("*Obligatorio");
        grbPregunta.add(lblObligatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 20));

        grbPrincipal.add(grbPregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 590, 310));

        getContentPane().add(grbPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 630, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
 
    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        if(orden!=1){ //Si no es la primera pregunta regresamos una
            orden--;//se regresa en el orden
            this.grbOpciones.removeAll();//se remueven los componentes
            this.MostrarPregunta(); //Actualiza la pregunta en pantalla
            this.grbPregunta.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregunta Nº"+orden));
        }
        this.Botones();
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        if(orden<lista_preguntas.size()){ //Si no es la última pregunta
            if(this.lblObligatorio.isVisible()){//se verifica que la pregunta sea obligatoria
                if(!this.ComprobarRespuestaObligatoria()){//se comprueba que la respuesta se llene
                    JOptionPane.showMessageDialog(this, "Debe responder pregunta obligatoria","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                } 
            }            
            orden++;//se cambia a la siguiente pregunta
            this.grbOpciones.removeAll();//se quitan los componentes
            this.MostrarPregunta();//se muestra la pregunta
            this.grbPregunta.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregunta Nº"+orden));
        }
        this.Botones();//Maneja activacion y desactivación botón anterior siguiente y finalizar
    }//GEN-LAST:event_btnSiguienteActionPerformed
    
    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        if(this.lblObligatorio.isVisible()){//en caso de que la pregunta sea obligatoria
            if(!this.ComprobarRespuestaObligatoria()){//comprueba que se haya colocado una respuesta
                JOptionPane.showMessageDialog(this, "Debe responder pregunta obligatoria","Error",JOptionPane.ERROR_MESSAGE);
                return;
            } 
        }
        try{
           this.RespuestasDao.guardar_respuestas();//Guardamos en la BD las respuestas
           JOptionPane.showMessageDialog(this, "Cuestionario finalizado con éxito");
           //Se agrega el contenido a la tabla de cuestionarios respondidos
           DefaultTableModel model =(DefaultTableModel) this.v_anterior.jgdCResueltos.getModel();
           model.addRow(new Object[]{cuestionario.getIdCuestionario(),cuestionario.getFechaCreacion(),cuestionario.getTitulo(),cuestionario.getDescripcion(),cuestionario.getComentario()});
        }
        catch (Error e){//error al tratar de guardar las respuestas
           JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        //Retorno a la vista anterior
        this.v_anterior.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnFinalizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAnterior;
    public javax.swing.JButton btnFinalizar;
    public javax.swing.JButton btnSiguiente;
    private javax.swing.JPanel grbBotones;
    private javax.swing.JPanel grbEncabezado;
    private javax.swing.JPanel grbOpciones;
    public javax.swing.JPanel grbPregunta;
    private javax.swing.JPanel grbPrincipal;
    public javax.swing.JLabel lblCometario;
    public javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblDescripcionB;
    public javax.swing.JLabel lblEnunciado;
    public javax.swing.JLabel lblFechaCreacion;
    private javax.swing.JLabel lblFechaCreacionB;
    private javax.swing.JLabel lblFechaCreacionB1;
    private javax.swing.JLabel lblIDCuestionarioB;
    public javax.swing.JLabel lblObligatorio;
    public javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTituloB1;
    public javax.swing.JLabel lbliDCuestionario;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {//Cuando se cierra la ventana
        if(!(JOptionPane.showConfirmDialog(this, "Está seguro de cancelar la resolución del cuestionario", "Advertencia", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION)){
            return;//Si se cancela el cierre de la ventana
        }
        this.v_anterior.setVisible(true);
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
