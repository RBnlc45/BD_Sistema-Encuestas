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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;


public class VistaRevisarCuestionario extends javax.swing.JFrame implements WindowListener{
    private JFrame v_anterior;
    private String cedula; //Cedula del encuestado
    private boolean bool; //Para mostrar o no las respuestas
    private int idCuestionario; //Para guardar el id del cuestionario que estamos revisando
    private int orden=1; //Iniciamos en la primera pregunta
    private LinkedList<Object> lista_preguntas; //Lista de preguntas
    private Map<Integer, LinkedList<Opcion>> mapa_opciones; //Mapa de todas las opciones
    private RespuestasDao RespuestasDao; //Para obtener los datos de la respuesta
    private JTextArea txt; //Area de texto
    
    public VistaRevisarCuestionario(String cedula,Cuestionario cuestionario, JFrame vista, boolean bool) {
        initComponents();
        this.addWindowListener(this);
        txt=new JTextArea(70,40); //Instancia para mostrar cuadro de texto
        //DATOS ENCUESTA/ENCUESTADO
        this.v_anterior=vista; //Vista anterior
        this.cedula=cedula; //Cedula del encuestado
        this.bool=bool; //Si es true mostramos las selecciones a la pregunta (solo cuando se instancia esta vista desde vista encuestado)
        this.idCuestionario=cuestionario.getIdCuestionario(); //0btenemos el id de cuestionario seleccionado para revisar
        //LBLS BUTTONS, seteamos todos los datos
        this.lbliDCuestionario.setText(this.idCuestionario+"");
        this.lblTitulo.setText(cuestionario.getTitulo());
        this.lblDescripcion.setText(cuestionario.getDescripcion());
        this.lblFechaCreacion.setText(cuestionario.getFechaCreacion().toString());
        this.lblCometario.setText(cuestionario.getComentario());
        this.btnAnterior.setEnabled(false);
        //PREGUNTAS/OPCIONES
        this.lista_preguntas=new PreguntasDao().cargar_preguntas(idCuestionario); //Obtengo lista de objetos preguntas para un idCuestionario dado       
        this.mapa_opciones=new OpcionesDao().cargar_opciones(this.idCuestionario, this.lista_preguntas); //Obtenemos un mapa de opciones para todas las preguntas
        //DAO RESPUESTAS
        this.RespuestasDao=new RespuestasDao();
        //PROCESOS
        if(lista_preguntas!=null && mapa_opciones!=null){
            this.MostrarPregunta(); //Muestra la pregunta
            this.Botones(); //Configura botones
        }else{
            this.setVisible(true); //Si no carga los datos se econden los paneles
            this.grbEncabezado.setVisible(false);
            this.grbPregunta.setVisible(false);
            this.btnSiguiente.setEnabled(false);
            JOptionPane.showMessageDialog(this,"No tiene los permisos suficientes para revisar el cuestionario","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void Botones(){ //Maneja activacion y desactivacion boton anterior siguiente y finalizar
        if(this.lista_preguntas.size()==1){ //Si solo tiene una pregunta
            this.btnAnterior.setEnabled(false);
            this.btnSiguiente.setEnabled(false);
        }else if(orden==1){ //Si es la primera y no ultima pregunta
            this.btnAnterior.setEnabled(false);
            this.btnSiguiente.setEnabled(true);
        }else if(orden==this.lista_preguntas.size()){ //Si es la ultima pregunta
            this.btnAnterior.setEnabled(true);
            this.btnSiguiente.setEnabled(false);
        }else{ //Si no es la primera ni la ultima pregunta
            this.btnAnterior.setEnabled(true);
            this.btnSiguiente.setEnabled(true);
        }
    }
    
    public void MostrarPregunta(){
        this.lblObligatorio.setVisible(false);
        //DATOS PREGUNTA
        Object pregunta_obj=this.lista_preguntas.get(orden-1); //Obtenemos la pregunta dada por el orden en el que nos encontremos
        String clase=pregunta_obj.getClass().getSimpleName(); //Obtenemos el tipo de la pregunta
        this.lblEnunciado.setText(((Pregunta)pregunta_obj).getEnunciado()); //Seteamos el enunciado
        //OPCIONES PREGUNTA
        LinkedList<Opcion> lista_opciones=this.mapa_opciones.get(orden); //Obtenemos la lista de opciones para la pregunta    
        //PRESENTAR PREGUNTA
        if(clase.equals("PreguntaMultiple")){ //Si es una pregunta multiple
            PreguntaMultiple pregunta_multiple=(PreguntaMultiple)pregunta_obj;//Casteamos el objeto pregunta a pregunta multiple
            if(pregunta_multiple.getObligatoria().equals("V")) this.lblObligatorio.setVisible(true); //Si la pregunta es obligatoria mostramos el lbl
            if(pregunta_multiple.getTipo().equals("Unica") || pregunta_multiple.getTipo().equals("V/F")){ //Si es unica o verdadero 
                this.AgregarOpcionesRespuestaUnica(lista_opciones, pregunta_multiple.getIdPregunta()); //Se agregar opciones con estilo seleccion unica
            }else{
                this.AgregarOpcionesRespuestaMultiple(lista_opciones, pregunta_multiple.getIdPregunta()); //Se agrega opciones con estilo seleccion multiple
            }
        }else if(clase.equals("PreguntaAbierta")){ //En caso que sea de clase pregunta abierta
            PreguntaAbierta pregunta_abierta=(PreguntaAbierta)pregunta_obj; //Casteamos el objeto a pregunta abierta
            if(pregunta_abierta.getObligatoria().equals("V")) this.lblObligatorio.setVisible(true); //Si es obligatoria se muestra el lbl obligatoria
            this.AgregarAreaRespuestaAbierta(RespuestasDao.cargar_respuestas_abierta(this.cedula,pregunta_abierta.getIdPregunta()), "Abierta"); //Se manda a agregar el txt para pregunta abierta
        }
    } 
       
    public void AgregarOpcionesRespuestaUnica(LinkedList<Opcion> lista_opciones, int idPregunta){
        ButtonGroup rbtg=new ButtonGroup(); //Grupo de botones
        for(Opcion o: lista_opciones){ //Recorremos la lista de opciones
            String opcion=o.getContenido(); //Obtenemos el texto de la opcion
            JRadioButton rbt=new JRadioButton(opcion); //Creamos boton con el texto de la opcion
            LinkedList<Selecciona> lista_selecciones=new LinkedList<>();
            if(this.bool) lista_selecciones=RespuestasDao.cargar_respuestas_multiple(this.cedula,o.getIdOpcion()); //Recuperamos las selecciones en caso de que instanciemos esta ventana se haya instanciado desde la vista de encuestado
            if(lista_selecciones!=null && lista_selecciones.size()>0 && lista_selecciones.get(0).getIdOpcion()==o.getIdOpcion()){
                rbt.setSelected(true); //Seleccionamos el boton en caso de que tenga la seleccion en su lista de selecciones
            }
            rbtg.add(rbt); //Agregamos al grupo el radio button
            rbt.setEnabled(false); //Desactivamos el radio button por que solo es para revisar no para seleccionar 
            this.grbOpciones.add(rbt); //Agregamos al panel el radio button
        }
    }
    
    public void AgregarOpcionesRespuestaMultiple(LinkedList<Opcion> lista_opciones, int idPregunta){
        for(Opcion o: lista_opciones){
            String opcion=o.getContenido();
            JCheckBox cbx=new JCheckBox(opcion); //Instanciamos un check box con nombre de la opcion
            LinkedList<Selecciona> lista_selecciones=new LinkedList<>();
            if(this.bool) lista_selecciones=RespuestasDao.cargar_respuestas_multiple(this.cedula,o.getIdOpcion()); //Si se instancio desde encuestado la ventana mostramos las opciones seleccionadas
            if(lista_selecciones!=null && lista_selecciones.size()>0 && lista_selecciones.get(0).getIdOpcion()==o.getIdOpcion()){
                cbx.setSelected(true); //En caso de que coincida la opcion con la seleccion del usuario seleccionamos el ceck box
            }
            cbx.setEnabled(false); //Desabilitamos el check box por que solo es para revisar no modificar
            this.grbOpciones.add(cbx); //Agregamos al panel
        }
    }
    
    public void AgregarAreaRespuestaAbierta(LinkedList<Object> lista_respuestas, String tipo){
        txt.setText("");
        txt.setLineWrap(true);
        if(this.bool && lista_respuestas!=null){ //Si hay respuesta para esta pregunta y fue instanciada desde vista de encuestador
            for(Object r: lista_respuestas){ //Rcorremos la lista de las respuestas
                if(r.getClass().getSimpleName().equals("Responde")){
                    Responde respuesta=(Responde)r; //Cateamos el objeto a respuesta
                    txt.setText(respuesta.getRespuesta()); //Setea la respuesta
                }
            }
        }
        txt.setEnabled(false); //Desabilitamos la edicion del txt
        this.grbOpciones.add(txt); //Agrega el txt a el panel
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
        btnSiguiente = new javax.swing.JButton();
        grbPregunta = new javax.swing.JPanel();
        lblEnunciado = new javax.swing.JLabel();
        grbOpciones = new javax.swing.JPanel();
        lblObligatorio = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Revisión Cuestionario");
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

        grbPrincipal.add(grbEncabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 590, 120));

        grbBotones.setBackground(new java.awt.Color(204, 204, 204));
        grbBotones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAnterior.setText("Anterior");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        grbBotones.add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 80, 30));

        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        grbBotones.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 100, 30));

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

        grbPrincipal.add(grbPregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 590, 310));

        getContentPane().add(grbPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        if(orden!=1){ //Si no es la primera pregunta regresamos una
            orden--; //Disminuimos el numero de pregunta
            this.grbOpciones.removeAll(); //Quitamos los contenidos anteriores
            this.MostrarPregunta(); //Actualiza la pregunta en pantalla
            this.grbPregunta.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregunta Nº"+orden)); //Seteamos el borde con numero de pregunta
        }
        this.Botones(); //Seteamos los botones
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        // TODO add your handling code here:
        if(orden<lista_preguntas.size()){ //Si no es la ultima pregunta
            orden++; //Aumentamos el numero de preguntas
            this.grbOpciones.removeAll(); //Quitamos los componentes anteriores
            this.MostrarPregunta(); //Actualizamos la pregunta
            this.grbPregunta.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregunta Nº"+orden)); //Seteamos el borde de la pregunta
        }
        this.Botones(); //Seteamos los botones
    }//GEN-LAST:event_btnSiguienteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAnterior;
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
    public void windowOpened(WindowEvent e) { }
    @Override
    public void windowClosing(WindowEvent e) {
        this.v_anterior.setVisible(true); //Mostramos la ventana anterior
        this.dispose(); //Cerramos esta
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
