
package PRESENTACION;

import NEGOCIO.Validar;
import NEGOCIO.CuestionariosDao;
import NEGOCIO.Objetos.Cuestionario;
import NEGOCIO.Objetos.Opcion;
import NEGOCIO.Objetos.Pregunta;
import NEGOCIO.Objetos.PreguntaAbierta;
import NEGOCIO.Objetos.PreguntaMultiple;
import NEGOCIO.Objetos.Usuario;
import NEGOCIO.PreguntasDao;
import NEGOCIO.UsuariosDao;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import oracle.sql.TIMESTAMP;

public class VistaEncuestador extends javax.swing.JFrame implements WindowListener{
    private VistaPrincipal v_principal;
    private int orden; //Orden de las preguntas (1..n)
    private String cedula; //Cedula del encuestador
    private Cuestionario Cuestionario; //Objeto cuestionario creado al iniciar cuestionario
    protected CuestionariosDao CuestionariosDao; //Maneja Cuestionario
    private PreguntasDao PreguntasDao; //Maneja Preguntas
    private UsuariosDao usuarioActual;

    public VistaEncuestador(String cedula, String contrasenia, VistaPrincipal v_principal) {
        this.v_principal=v_principal;
        initComponents();
        this.addWindowListener(this);
        usuarioActual=new UsuariosDao(cedula,contrasenia);
        this.grbBotones.setVisible(false); 
        this.cedula=cedula; //Setear cedula del encuestador
        this.iniciar();
    } //Constructor

    private void iniciar(){
        this.orden=1; //Primera pregunta
        this.CuestionariosDao=new CuestionariosDao(); //Para manejo de cuestionarios
        this.PreguntasDao=new PreguntasDao(); //Maneja preguntas y opciones
        this.lbliDEncuestador.setText(this.cedula);
        this.setVisible(true);
        this.RefrescarTablaCuestionarios(CuestionariosDao.cargar_cuestionarios_encuestador(this.cedula)); //Cuestionarios disponibles
        this.ActualizarCombo();
        this.EmpezarCuestionario(false); //Panel de preguntas oculto
    } //Arranque
    
    public void RefrescarTablaCuestionarios(LinkedList<Cuestionario> contenedor){
        if(contenedor==null){
            JOptionPane.showMessageDialog(this,"No tiene los permisos suficientes para visualizar los cuestionarios creados","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model=(DefaultTableModel) this.jgdCCreados.getModel();
        int size=model.getRowCount();
        
        for(int i=size-1;i>=0;i--){ //borra toda la tabla
        	model.removeRow(i);
        }
        for(int i=0; i<contenedor.size();i++){
        	model.insertRow(model.getRowCount(), new Object[] {});
        	model.setValueAt(contenedor.get(i).getIdCuestionario(),  model.getRowCount()-1, 0);
                model.setValueAt(contenedor.get(i).getFechaCreacion(),  model.getRowCount()-1, 1);
                model.setValueAt(contenedor.get(i).getTitulo(),  model.getRowCount()-1, 2);
                model.setValueAt(contenedor.get(i).getDescripcion(),  model.getRowCount()-1, 3);
                model.setValueAt(contenedor.get(i).getComentario(),  model.getRowCount()-1, 4);
        }
    }
    
    public void ActualizarCombo(){
        LinkedList<Cuestionario> lista_cuestionarios=CuestionariosDao.cargar_cuestionarios_encuestador(this.cedula); //Carga cuestionarios creados por un encuestador
        if(lista_cuestionarios==null) return; //Si la lista de cuestionarios no es null
        DefaultComboBoxModel cbxModel=new DefaultComboBoxModel();
        for(int i=0; i<lista_cuestionarios.size(); i++){ //Ingresa los cuestionarios en el combo box de cuestionarios
            Cuestionario c=lista_cuestionarios.get(i);
            cbxModel.insertElementAt(c,i);
        }
        cbxModel.setSelectedItem(cbxModel.getElementAt(0)); //Selecciona el primer elemento
        this.cbxidCuestionario.setModel(cbxModel); //Setea el modelo al combo
        if(this.cbxidCuestionario.getSelectedItem()!=null) this.RefrescarTablaUsuarios(CuestionariosDao.cargar_usuarios_cuestionario(((Cuestionario)this.cbxidCuestionario.getSelectedItem()).getIdCuestionario()));
        else this.RefrescarTablaUsuarios(new LinkedList<>());  //Si no hay cuestionarios muestra la tabla vacia
    } 
    
    public void RefrescarTablaUsuarios(LinkedList<Usuario> contenedor){
        DefaultTableModel model=(DefaultTableModel) this.jgdUsuarios.getModel();
        int size=model.getRowCount();
        
        for(int i=size-1;i>=0;i--){ //borra toda la tabla
        	model.removeRow(i);
        }
        for(int i=0; i<contenedor.size();i++){
        	model.insertRow(model.getRowCount(), new Object[] {});
        	model.setValueAt(contenedor.get(i).getCedula(),  model.getRowCount()-1, 0);
                model.setValueAt(contenedor.get(i).getNombre(),  model.getRowCount()-1, 1);
                model.setValueAt(contenedor.get(i).getApellido(),  model.getRowCount()-1, 2);
                model.setValueAt(contenedor.get(i).getTelefono(),  model.getRowCount()-1, 3);
                model.setValueAt(contenedor.get(i).getDireccion(),  model.getRowCount()-1, 4);
                model.setValueAt(contenedor.get(i).getMail(),  model.getRowCount()-1, 5);    
        }
    }
    
    public void LimpiarTextos(){ //Limpia todos los elementos de la vista para resolver cuesiotnario
        this.txtComentario.setText("");
        this.txtTituloEncuesta.setText("");
        this.rtxtDescripcion.setText("");
        this.rtxtEnunciado.setText("");
        this.lstOpciones.setModel(new DefaultListModel());
    }
    
    public static String textAreaDialog(Object obj, String title) { //Instancia del area de texto que emerge al presionar agregar botones
        if(title == null) {
            title = "Ingrese la opcion";
        }
        JTextArea textArea = new JTextArea();
        textArea.setColumns(30);
        textArea.setRows(7);
        textArea.setLineWrap(true); //Para saltar la linea al llegar al borde
        textArea.setWrapStyleWord(true); //Estilo de salto
        textArea.setSize(textArea.getPreferredSize().width, textArea.getPreferredSize().height); //Tamanio
        int ret = JOptionPane.showConfirmDialog((Component) obj, new JScrollPane(textArea), title, JOptionPane.OK_CANCEL_OPTION); //Instancia la ventana emergente con el area de texto
        if (ret == 0) { //Si ingreso algun texto
            return textArea.getText();
        }
        return null;
    } //Crea ventana emergente para insertar opciones
    
    private void ReiniciarPanelCrear(){ //Reinicia el panel de la pregunta al comenar el cuestionario
        this.orden=1; //Regresa el orden a 1
        this.grbPregunta.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregunta Nº "+this.orden)); //Muestra el borde con el numero de pregunta
        this.LimpiarTextos(); //Limpia los recuadros de texto
        this.ConfigurarPanelPregunta(this.cbxTipoPregunta.getModel().getSelectedItem().toString()); //Configutamos el panel con el tipo de pregunta que este en el combo box
        this.EmpezarCuestionario(false);
        this.ActualizarCombo();
    } //Reinicia el panel para crear cuestionario
    
    public void EmpezarCuestionario(boolean bool){ 
        this.btnEmpezarCuestionario.setVisible(!bool);
        this.txtTituloEncuesta.setEditable(!bool);
        this.txtComentario.setEditable(!bool);
        this.rtxtDescripcion.setEditable(!bool);
        this.grbPregunta.setVisible(bool);
        this.btnAgregarPregunta.setEnabled(bool);
        this.btnEliminarPregunta.setEnabled(bool);
        this.btnGrabarEncuesta.setVisible(bool);
        this.Botones(); //Anterior y Siguiente
    } //Encargado de los botones y labels al presionar el btn empezar cuestionario
    
    public boolean ValidarCamposIniciar(String[] campos){ //Valida los campos del encabezado del cuestionario
        if(Validar.campos_vacios(campos).equals("Error")){ //Validar campos vacios
            JOptionPane.showMessageDialog(this, "No se pueden dejar campos vacios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }else if(!Validar.numero_caracteres(80, campos[0].length())){ //Validar 30 caracteres correo
            JOptionPane.showMessageDialog(this, "El título debe tener como máximo 80 caracteres","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }else if(!Validar.numero_caracteres(300, campos[1].length())){ //Validar 300 caracteres
            JOptionPane.showMessageDialog(this, "La descripción debe tener como máximo 300 caracteres","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }else if(!Validar.numero_caracteres(150, campos[2].length())){ //Validar 150 caracteres
           JOptionPane.showMessageDialog(this, "El comentario debe tener como máximo 150 caracteres","Error",JOptionPane.ERROR_MESSAGE);
           return false; 
        }
        return true;
    } //Campos del encabezado
    
    private void ConfigurarPanelPregunta(String tipo){
        this.grbPregunta.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregunta Nº"+this.orden)); //Muestra el borde con n pregunta
        switch (tipo) { //Aanaliza el tipo para mostrar los paneles
            case "Abierta":
                lblInfoOpciones.setText("Premite al encuestado ingresar su respuesta.");
                grbBotones.setVisible(false);
                break;
            case "Unica": //Abilita los botones para agregar opciones
                lblInfoOpciones.setText("A continuacion ingrese las opciones que tendra la respuesta.");
                grbBotones.setVisible(true);
                btnAgregarOpcion.setEnabled(true);
                btnEliminarOpcion.setEnabled(true);
                lstOpciones.setModel(new DefaultListModel());
                break;
            case "Multiple": //Abilita los botones para agregar opciones
                grbBotones.setVisible(true);
                lblInfoOpciones.setText("A continuacion ingrese las opciones que tendra la respuesta.");
                btnAgregarOpcion.setEnabled(true);
                btnEliminarOpcion.setEnabled(true);
                lstOpciones.setModel(new DefaultListModel());
                break;
            case "V/F": //Desactiva boton agregar y eliminar opciones
                lblInfoOpciones.setText("Premite al encuestado escoger entre Verdadero y Falso.");
                grbBotones.setVisible(true);
                btnAgregarOpcion.setEnabled(false);
                btnEliminarOpcion.setEnabled(false);
                DefaultListModel model=new DefaultListModel();
                model.addElement("Verdadero");
                model.addElement("Falso");
                lstOpciones.setModel(model);
                break;
            default:
                break;
        }
    }
    
    //Util para navegar entre preguntas
    public void MostrarPregunta(Pregunta pregunta){
        this.rtxtEnunciado.setText(pregunta.getEnunciado()); //Seteamos Enunciado de la pregunta
        if(pregunta.getObligatoria().equals("V")) this.cbxObligatoria.setSelected(true); //Si es pregunta oblicatoria se selecciona el cbx
        else this.cbxObligatoria.setSelected(false); //Si no es obligatoria se lo deselecciona
        this.grbPregunta.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregunta Nº"+orden)); //Se establece el borde
        String tipo=""; 
        if(pregunta.getClass().getSimpleName().equals("PreguntaAbierta")){ //obtenemos el tipo
            tipo="Abierta";
        }else{
            tipo=((PreguntaMultiple)pregunta).getTipo();
        }
        this.cbxTipoPregunta.setSelectedItem(tipo); //Seleccionamos en el combo box el tipo de pregunta
        this.ConfigurarPanelPregunta(tipo); //Configura los paneles dependiendo el tipo de pregunta
        if(!tipo.equals("Abierta")){ // Si no es abierta
           DefaultListModel model=new DefaultListModel();
           LinkedList<Opcion> opciones=this.PreguntasDao.recuperar_opciones(pregunta); //Recuperamos las opciones de la pregunta
           for(Opcion o: opciones){
               model.addElement(o.getContenido());
           }
           this.lstOpciones.setModel(model); //Seteamos el modelo con la lista de preguntas
        }
    }
    
    //Analiza el numero de preguntas en el contenedor y el numero de pregunta acutual para habilitar o desabilitar botones
    public void Botones(){ //Maneja activacion y desactivacion boton anterior siguiente y finalizar
        int num_preguntas=this.PreguntasDao.numero_preguntas();
        this.btnAnterior.setEnabled(false);
        this.btnSiguiente.setEnabled(false);
        if(orden==1 && num_preguntas!=0){ //Si es la primera pregunta y el contenedor de preguntas no tiene 0
            this.btnSiguiente.setEnabled(true);
            if(this.btnAgregarPregunta.getText().equals("Agregar Pregunta")) this.btnAgregarPregunta.setText("Actualizar Pregunta");
        }else if(orden==num_preguntas+1 && num_preguntas!=0){ //Ultima pregunta y no hay 0 preguntas
            this.btnAnterior.setEnabled(true);
            if(this.btnAgregarPregunta.getText().equals("Actualizar Pregunta")) this.btnAgregarPregunta.setText("Agregar Pregunta");
        }else if(num_preguntas!=0 && num_preguntas+1!=this.orden){ //Ni primera ni la que toca agregar
            this.btnAnterior.setEnabled(true);
            this.btnSiguiente.setEnabled(true);
            if(this.btnAgregarPregunta.getText().equals("Agregar Pregunta")) this.btnAgregarPregunta.setText("Actualizar Pregunta");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPaneEncuestador = new javax.swing.JTabbedPane();
        grbCestionariosCreados = new javax.swing.JPanel();
        grbCuestionariosCreados = new javax.swing.JPanel();
        jScrollPaneCD = new javax.swing.JScrollPane();
        jgdCCreados = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnPerfil = new javax.swing.JButton();
        btnRevisar = new javax.swing.JButton();
        grbRespuestasCuestionarios = new javax.swing.JPanel();
        grbRCuestionarios = new javax.swing.JPanel();
        jScrollPaneUsuariosResponden = new javax.swing.JScrollPane();
        jgdUsuarios = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbxidCuestionario = new javax.swing.JComboBox<>();
        btnRespuestas = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        grbCrearCuestionario = new javax.swing.JPanel();
        grbCCuestionario = new javax.swing.JPanel();
        grbEncabezado = new javax.swing.JPanel();
        lblAutor = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblDesripcion = new javax.swing.JLabel();
        lbliDEncuestador = new javax.swing.JLabel();
        lblComentario = new javax.swing.JLabel();
        grbTxtEncabezado = new javax.swing.JPanel();
        jScrollPaneDescripcion = new javax.swing.JScrollPane();
        rtxtDescripcion = new javax.swing.JTextArea();
        txtTituloEncuesta = new javax.swing.JTextField();
        txtComentario = new javax.swing.JTextField();
        btnEmpezarCuestionario = new javax.swing.JButton();
        btnGrabarEncuesta = new javax.swing.JButton();
        grbPregunta = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        rtxtEnunciado = new javax.swing.JTextArea();
        cbxTipoPregunta = new javax.swing.JComboBox<>();
        grbEstructuraPregunta = new javax.swing.JPanel();
        lblInfoOpciones = new javax.swing.JLabel();
        grbBotones = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstOpciones = new javax.swing.JList<>();
        btnAgregarOpcion = new javax.swing.JButton();
        btnEliminarOpcion = new javax.swing.JButton();
        cbxObligatoria = new javax.swing.JCheckBox();
        btnAgregarPregunta = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnEliminarPregunta = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Encuestador");

        jTabbedPaneEncuestador.setMinimumSize(new java.awt.Dimension(603, 419));

        grbCestionariosCreados.setBackground(new java.awt.Color(204, 204, 204));
        grbCestionariosCreados.setPreferredSize(new java.awt.Dimension(643, 448));

        grbCuestionariosCreados.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jgdCCreados.getTableHeader().setReorderingAllowed(false);
        jgdCCreados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idCuestionario", "Fecha", "Título", "Descripción", "Comentario"
            }
        ));
        jScrollPaneCD.setViewportView(jgdCCreados);
        if (jgdCCreados.getColumnModel().getColumnCount() > 0) {
            jgdCCreados.getColumnModel().getColumn(0).setPreferredWidth(35);
        }

        grbCuestionariosCreados.add(jScrollPaneCD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 800, 350));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("CUESTIONARIOS CREADOS");
        grbCuestionariosCreados.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        btnEliminar.setText("Eliminar Cuestionario");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        grbCuestionariosCreados.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 420, 160, 30));

        btnPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PRESENTACION/iconos/perfil.png"))); // NOI18N
        btnPerfil.setFocusable(false);
        btnPerfil.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPerfil.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerfilActionPerformed(evt);
            }
        });
        grbCuestionariosCreados.add(btnPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(756, 0, 40, 40));

        btnRevisar.setText("Revisar Cuestionario");
        btnRevisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevisarActionPerformed(evt);
            }
        });
        grbCuestionariosCreados.add(btnRevisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 420, 160, 30));

        javax.swing.GroupLayout grbCestionariosCreadosLayout = new javax.swing.GroupLayout(grbCestionariosCreados);
        grbCestionariosCreados.setLayout(grbCestionariosCreadosLayout);
        grbCestionariosCreadosLayout.setHorizontalGroup(
            grbCestionariosCreadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbCestionariosCreadosLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(grbCuestionariosCreados, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        grbCestionariosCreadosLayout.setVerticalGroup(
            grbCestionariosCreadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbCestionariosCreadosLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(grbCuestionariosCreados, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jTabbedPaneEncuestador.addTab("CUESTIONARIOS CREADOS", grbCestionariosCreados);

        grbRespuestasCuestionarios.setBackground(new java.awt.Color(204, 204, 204));
        grbRespuestasCuestionarios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grbRCuestionarios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jgdUsuarios.getTableHeader().setReorderingAllowed(false);
        jgdUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cédula", "Nombre", "Apellido", "Teléfono", "Dirección", "Correo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneUsuariosResponden.setViewportView(jgdUsuarios);
        if (jgdUsuarios.getColumnModel().getColumnCount() > 0) {
            jgdUsuarios.getColumnModel().getColumn(0).setResizable(false);
            jgdUsuarios.getColumnModel().getColumn(0).setPreferredWidth(40);
            jgdUsuarios.getColumnModel().getColumn(1).setResizable(false);
            jgdUsuarios.getColumnModel().getColumn(1).setPreferredWidth(40);
            jgdUsuarios.getColumnModel().getColumn(2).setResizable(false);
            jgdUsuarios.getColumnModel().getColumn(2).setPreferredWidth(40);
            jgdUsuarios.getColumnModel().getColumn(3).setResizable(false);
            jgdUsuarios.getColumnModel().getColumn(3).setPreferredWidth(30);
            jgdUsuarios.getColumnModel().getColumn(4).setResizable(false);
            jgdUsuarios.getColumnModel().getColumn(5).setResizable(false);
            jgdUsuarios.getColumnModel().getColumn(5).setPreferredWidth(105);
        }

        grbRCuestionarios.add(jScrollPaneUsuariosResponden, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 800, 360));

        jLabel7.setText("Cuestionario:");
        grbRCuestionarios.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel6.setText("RESPUESTAS DE CUESTIONARIOS CREADOS");
        grbRCuestionarios.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        cbxidCuestionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxidCuestionarioActionPerformed(evt);
            }
        });
        grbRCuestionarios.add(cbxidCuestionario, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 170, -1));

        btnRespuestas.setText("Revisar Respuestas");
        btnRespuestas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRespuestasActionPerformed(evt);
            }
        });
        grbRCuestionarios.add(btnRespuestas, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 430, 160, 30));

        grbRespuestasCuestionarios.add(grbRCuestionarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 20, 800, 470));
        grbRespuestasCuestionarios.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 182, 40, 0));

        jTabbedPaneEncuestador.addTab("RESPUESTAS A CUESTIONARIOS CREADOS", grbRespuestasCuestionarios);

        grbCrearCuestionario.setBackground(new java.awt.Color(204, 204, 204));
        grbCrearCuestionario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grbCCuestionario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        grbEncabezado.setBorder(javax.swing.BorderFactory.createTitledBorder("Encabezado"));
        grbEncabezado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblAutor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAutor.setText("Autor:");
        grbEncabezado.add(lblAutor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, 20));

        lblTitulo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTitulo.setText("Título:");
        grbEncabezado.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, 20));

        lblDesripcion.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDesripcion.setText("Descripción:");
        grbEncabezado.add(lblDesripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, 20));

        lbliDEncuestador.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbliDEncuestador.setText("0106774615");
        grbEncabezado.add(lbliDEncuestador, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 100, -1));

        lblComentario.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblComentario.setText("Comentario:");
        grbEncabezado.add(lblComentario, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, 20));

        grbTxtEncabezado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rtxtDescripcion.setColumns(20);
        rtxtDescripcion.setLineWrap(true);
        rtxtDescripcion.setRows(5);
        jScrollPaneDescripcion.setViewportView(rtxtDescripcion);

        grbTxtEncabezado.add(jScrollPaneDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 410, 50));
        grbTxtEncabezado.add(txtTituloEncuesta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, -1));
        grbTxtEncabezado.add(txtComentario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 410, -1));

        grbEncabezado.add(grbTxtEncabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, -1, -1));

        btnEmpezarCuestionario.setText("Empezar Cuestionario");
        btnEmpezarCuestionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpezarCuestionarioActionPerformed(evt);
            }
        });
        grbEncabezado.add(btnEmpezarCuestionario, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 160, 23));

        btnGrabarEncuesta.setText("Grabar Encuesta");
        btnGrabarEncuesta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarEncuestaActionPerformed(evt);
            }
        });
        grbEncabezado.add(btnGrabarEncuesta, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 160, -1));

        grbCCuestionario.add(grbEncabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 590, 180));

        grbPregunta.setBorder(javax.swing.BorderFactory.createTitledBorder("Pregunta Nº1"));

        rtxtEnunciado.setColumns(20);
        rtxtEnunciado.setLineWrap(true);
        rtxtEnunciado.setRows(5);
        jScrollPane2.setViewportView(rtxtEnunciado);

        cbxTipoPregunta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Abierta", "Unica", "Multiple", "V/F" }));
        cbxTipoPregunta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTipoPreguntaActionPerformed(evt);
            }
        });

        grbEstructuraPregunta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblInfoOpciones.setText("A continuación ingrese las opciones que tendra la respuesta.");

        grbBotones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstOpciones.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(lstOpciones);

        grbBotones.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 190, 83));

        btnAgregarOpcion.setText("Agregar Opción");
        btnAgregarOpcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarOpcionActionPerformed(evt);
            }
        });
        grbBotones.add(btnAgregarOpcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 140, 23));

        btnEliminarOpcion.setText("Eliminar Opción");
        btnEliminarOpcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarOpcionActionPerformed(evt);
            }
        });
        grbBotones.add(btnEliminarOpcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 140, -1));

        javax.swing.GroupLayout grbEstructuraPreguntaLayout = new javax.swing.GroupLayout(grbEstructuraPregunta);
        grbEstructuraPregunta.setLayout(grbEstructuraPreguntaLayout);
        grbEstructuraPreguntaLayout.setHorizontalGroup(
            grbEstructuraPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbEstructuraPreguntaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
            .addComponent(grbBotones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        grbEstructuraPreguntaLayout.setVerticalGroup(
            grbEstructuraPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbEstructuraPreguntaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoOpciones)
                .addGap(18, 18, 18)
                .addComponent(grbBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        cbxObligatoria.setForeground(new java.awt.Color(255, 51, 0));
        cbxObligatoria.setText("Obligatoria");

        javax.swing.GroupLayout grbPreguntaLayout = new javax.swing.GroupLayout(grbPregunta);
        grbPregunta.setLayout(grbPreguntaLayout);
        grbPreguntaLayout.setHorizontalGroup(
            grbPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbPreguntaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
            .addGroup(grbPreguntaLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(grbPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxTipoPregunta, 0, 109, Short.MAX_VALUE)
                    .addComponent(cbxObligatoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(grbEstructuraPregunta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        grbPreguntaLayout.setVerticalGroup(
            grbPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grbPreguntaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(grbPreguntaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(grbPreguntaLayout.createSequentialGroup()
                        .addComponent(cbxTipoPregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(cbxObligatoria)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(grbEstructuraPregunta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        grbCCuestionario.add(grbPregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 590, 260));

        btnAgregarPregunta.setText("Agregar Pregunta");
        btnAgregarPregunta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPreguntaActionPerformed(evt);
            }
        });
        grbCCuestionario.add(btnAgregarPregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 470, 150, 30));

        btnSiguiente.setText(">");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        grbCCuestionario.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 470, 50, 30));

        btnAnterior.setText("<");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        grbCCuestionario.add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 470, 50, 30));

        btnEliminarPregunta.setText("Eliminar Pregunta");
        btnEliminarPregunta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPreguntaActionPerformed(evt);
            }
        });
        grbCCuestionario.add(btnEliminarPregunta, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 470, 140, 30));

        grbCrearCuestionario.add(grbCCuestionario, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 0, 680, 520));

        jTabbedPaneEncuestador.addTab("CREAR CUESTIONARIO", grbCrearCuestionario);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneEncuestador, javax.swing.GroupLayout.PREFERRED_SIZE, 857, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPaneEncuestador, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private void cbxTipoPreguntaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTipoPreguntaActionPerformed
        String tipo=cbxTipoPregunta.getSelectedItem().toString(); //Si se cambia el tipo de pregunta en el combo box
        this.ConfigurarPanelPregunta(tipo); //Se configura el panel con el tipo de pregunta seleccionada 
    }//GEN-LAST:event_cbxTipoPreguntaActionPerformed
    
    //Guardar la encuesta con sus preguntas y opciones
    private void btnGrabarEncuestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarEncuestaActionPerformed
        if(PreguntasDao.numero_preguntas()>0){ //si el cuestionario tiene preguntas
            String[] mensaje=new CuestionariosDao().guardar_cuestionario(this.Cuestionario,this.PreguntasDao); //Se envia a guardar cuestionario
            if(mensaje[0].equals("Informacion")){
                JOptionPane.showMessageDialog(this, mensaje[1],mensaje[0],JOptionPane.INFORMATION_MESSAGE);
                DefaultTableModel model=((DefaultTableModel)jgdCCreados.getModel());
                model.insertRow(model.getRowCount(), new Object[] {this.Cuestionario.getIdCuestionario(),this.Cuestionario.getFechaCreacion(),
                //Ingresa nuevo cuestionario a la tabla de cuestionarios
                this.Cuestionario.getTitulo(),this.Cuestionario.getDescripcion(),this.Cuestionario.getComentario()});
                jgdCCreados.setModel(model);   
                this.ReiniciarPanelCrear(); //Resetea panel al grabar cuestionario
            }else{
                JOptionPane.showMessageDialog(this, mensaje[1],mensaje[0],JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this,"No puede guardar una encuesta sin preguntas","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGrabarEncuestaActionPerformed
    
    //Agregar pregunta a la lista de preguntas
    private void btnAgregarPreguntaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPreguntaActionPerformed
        if(this.rtxtEnunciado.getText().equals("")){ //Valida enunciado vacio
            JOptionPane.showMessageDialog(this, "El enunciado de la pregunta no puede estar vacio","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }else if(!Validar.numero_caracteres(400, this.rtxtEnunciado.getText().length())){ //Valida 400 caracteres de enunciado
            JOptionPane.showMessageDialog(this, "El enunciado de la pregunta debe tener como máximo 400 digitos","Error",JOptionPane.ERROR_MESSAGE);
           return; 
        }
        String tipo=this.cbxTipoPregunta.getModel().getSelectedItem().toString(); //Tipo de pregunta creada
        String obligatoria="F"; //No es obligatoria
        if(this.cbxObligatoria.isSelected()){ //Si esta seleccionado el check es obligatoria
            obligatoria="V";
        }
        int num_a=PreguntasDao.numero_preguntas(); //Numero antes de agregar
        if(tipo.equals("Abierta")){
            PreguntasDao.agregar_pregunta(new PreguntaAbierta(null,0,this.rtxtEnunciado.getText(),this.orden,obligatoria,this.Cuestionario),new JList());
        }else{
            if(this.lstOpciones.getModel().getSize()<1){
                JOptionPane.showMessageDialog(this, "No puede ingresar una pregunta sin opciones","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            PreguntasDao.agregar_pregunta(new PreguntaMultiple(null,0,this.rtxtEnunciado.getText(),this.orden,obligatoria,tipo,this.Cuestionario),this.lstOpciones); //Agregamos la pregunta
        }
        if(PreguntasDao.numero_preguntas()>num_a){ //Nueva pregunta
            JOptionPane.showMessageDialog(this, "Pregunta agregada correctamente");
            this.orden++;
            this.rtxtEnunciado.setText("");
            this.cbxObligatoria.setSelected(false);
            this.ConfigurarPanelPregunta(tipo);
            this.Botones();
        }else{
            JOptionPane.showMessageDialog(this, "Pregunta modificada correctamente");
        }
        
    }//GEN-LAST:event_btnAgregarPreguntaActionPerformed
    
    //Eliminar cuestionario de la lista de cuestionarios creados
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int indice=this.jgdCCreados.getSelectedRow();
        if(indice!=-1){ //Si selecciono un cuestionario
            if(!(JOptionPane.showConfirmDialog(this, "Esta seguro de eliminar el cuestionario", "Advertencia", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION)){
                 return;
            }
            int idCEliminar= (int) this.jgdCCreados.getModel().getValueAt(indice, 0);
            String[] mensaje=this.CuestionariosDao.eliminar_cuestionario(idCEliminar, null, "", "", "", ""); //Envia a eliminar
            if(!mensaje[0].equals("Error")){ //Si se elimino correctamente
                ((DefaultTableModel)jgdCCreados.getModel()).removeRow(indice);
                this.ActualizarCombo();
            }else{
                JOptionPane.showMessageDialog(this, mensaje[1], mensaje[0], JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Primero seleccione un cuestionario de la tabla", "Error!",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    //AAgregar opcion a pregunta multiple o unica
    private void btnAgregarOpcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarOpcionActionPerformed
        String opcion=textAreaDialog(this,"Ingrese el contenido de la opción: ");
        if(opcion==null){ //Si cierra
            return;
        }else if(opcion.equals("")){ //Si ingresa vacia
            JOptionPane.showMessageDialog(this, "No puede agregar una opcion en blanco", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }else if(!Validar.numero_caracteres(200, opcion.length())){
            JOptionPane.showMessageDialog(this, "La opción debe tener como máximo 200 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultListModel model=(DefaultListModel) lstOpciones.getModel();
        model.addElement(opcion);
        lstOpciones.setModel(model); //Se agrega al modelo la opcion agregada
    }//GEN-LAST:event_btnAgregarOpcionActionPerformed
    
    //Eliminar opcion a pregunta multiple o unica
    private void btnEliminarOpcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarOpcionActionPerformed
        int indice=lstOpciones.getSelectedIndex(); //Obtiene el indice de la opcion seleccionada
        DefaultListModel model=(DefaultListModel) lstOpciones.getModel(); //Obtenemos el modelo del JList de opciones
        if(indice!=-1 && indice<model.getSize()){ //Si selecciono una opcion
            model.remove(indice); //Elimina la opcion en la posicion deseada
            lstOpciones.setModel(model); //Vuelve a asignar el modelo
        }else{
            JOptionPane.showMessageDialog(this,"Seleccione una opcion de la lista para eliminarla");
        }
    }//GEN-LAST:event_btnEliminarOpcionActionPerformed

    //Accion al cambiar la seleccion en el combo box con los cuestionarios de los cuales queremos ver los usuarios que los resolvieron
    private void cbxidCuestionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxidCuestionarioActionPerformed
        //Cargamos los usuarios que han enviado una respuesta al cuestionario seleccionado en el combo box
        this.RefrescarTablaUsuarios(CuestionariosDao.cargar_usuarios_cuestionario(((Cuestionario)this.cbxidCuestionario.getSelectedItem()).getIdCuestionario()));
    }//GEN-LAST:event_cbxidCuestionarioActionPerformed

    //Empezar cuestionario, se requiere mostrar panel para agregar las preguntas
    private void btnEmpezarCuestionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpezarCuestionarioActionPerformed
        if(!this.CuestionariosDao.restriccion_cuestionarios_creados(this.cedula)){ //Si ya creo 10 cuestionarios
            JOptionPane.showMessageDialog(this, "No esta permitido crear mas de 10 cuestioanrios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } //Si ya tiene 10 cuestionarios
        TIMESTAMP fecha=new TIMESTAMP(new java.sql.Timestamp(System.currentTimeMillis())); //Fecha actual
        String[] campos={this.txtTituloEncuesta.getText(),this.rtxtDescripcion.getText(), this.txtComentario.getText()};
        if(!this.ValidarCamposIniciar(campos)) return;
        this.Cuestionario=new Cuestionario(null, fecha, campos[0], campos[1], campos[2], this.cedula);
        this.EmpezarCuestionario(true); //Empezamos cuestionario mandamos a setear los componentes de la ventana
        
    }//GEN-LAST:event_btnEmpezarCuestionarioActionPerformed
    
    //Boton para revisar las respuestas de un usuario a un cuesttionario determinado
    private void btnRespuestasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRespuestasActionPerformed
        int indice=this.jgdUsuarios.getSelectedRow();
        if(indice==-1){
            JOptionPane.showMessageDialog(this, "Primero seleccione un usuario de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } //Si ha seleccionado una fila
        Cuestionario cuestionario=(Cuestionario)this.cbxidCuestionario.getModel().getSelectedItem(); //Obtenemos el cuestionario seleccionado en el combo de cuestionarios para los que hay una respuesta
        String cedula=(String)this.jgdUsuarios.getModel().getValueAt(indice, 0); //Obtenemos el numero de cedula del usuario seleccionado que respondio al cuestionario indicado
        new VistaRevisarCuestionario(cedula,cuestionario,this,true).setVisible(true); //Instancia vista revisar cuestionario con los datos del cuestionario seleccionado
        this.setVisible(false); //Escondemos la vista actual
    }//GEN-LAST:event_btnRespuestasActionPerformed

    //Boton pregunta siguiente
    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        this.orden++; //Aumenta el orden de la pregunta al presionar siguiente
        if(this.orden<this.PreguntasDao.numero_preguntas()+1){ //Si la pregunta a la que queremos desplazarnos es menor que la cantidad de preguntas existentes
            this.MostrarPregunta(this.PreguntasDao.recuperar_pregunta(this.orden)); //Manda a mostrar la pregunta en la que estamos posicionados
        }else if(this.orden==this.PreguntasDao.numero_preguntas()+1){ //Recupera la pregunta a mostrar
            this.rtxtEnunciado.setText(""); //Si es la posicion de numero de preguntas mas 1 es el panel donde vamos a agregar la nueva pregunta
            this.cbxObligatoria.setSelected(false); //Deseleccionamos obligatoria
            this.ConfigurarPanelPregunta(this.cbxTipoPregunta.getModel().getSelectedItem().toString()); //Configuramos el panel de la pregunta con el tipo de pregunta que este seleccionado en el combo
        }
        this.Botones();
    }//GEN-LAST:event_btnSiguienteActionPerformed

    //Boton pregunta anterior
    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        if(this.orden!=1){ //Si no es la primera pregunta
            this.orden--; //Disminuimos la posicion de la pregunta actual
            this.MostrarPregunta(this.PreguntasDao.recuperar_pregunta(this.orden)); //Recupera la pregunta a mostrar
        }
        this.Botones(); //Mandamos a configurar los botones
    }//GEN-LAST:event_btnAnteriorActionPerformed

    //Revisar perfil del usuario
    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilActionPerformed
        this.setVisible(false); //Se esconde esta vista
        new VistaEditarUsuario(new UsuariosDao().recuperar_usuario(this.cedula), this.v_principal, null, this, null).setVisible(true);
    }//GEN-LAST:event_btnPerfilActionPerformed

    //Revisar el cuestionario creado por el encuestador
    private void btnRevisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevisarActionPerformed
        int indice=this.jgdCCreados.getSelectedRow();
        if(indice==-1){ //Si no se selecciono una fila
            JOptionPane.showMessageDialog(this, "Primero seleccione un cuestionario de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Cuestionario cuestionario = new Cuestionario(); //Instanciamos objeto cuestionario y le seteamos sus respectivos datos
        cuestionario.setIdCuestionario((Integer)this.jgdCCreados.getModel().getValueAt(indice, 0));
        cuestionario.setFechaCreacion((TIMESTAMP) this.jgdCCreados.getModel().getValueAt(indice, 1));
        cuestionario.setTitulo((String) this.jgdCCreados.getModel().getValueAt(indice, 2));
        cuestionario.setDescripcion((String) this.jgdCCreados.getModel().getValueAt(indice, 3));
        cuestionario.setComentario((String) this.jgdCCreados.getModel().getValueAt(indice, 4));
        this.setVisible(false); //Escondemos este panel
        new VistaRevisarCuestionario(this.cedula,cuestionario,this,false).setVisible(true); //Pasamos a la vista para revisar el cuestionario seleccionado
    }//GEN-LAST:event_btnRevisarActionPerformed

    //Eliminar una pregunta de un cuestioanrio que estamos creando
    private void btnEliminarPreguntaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPreguntaActionPerformed
        String tipo=this.cbxTipoPregunta.getSelectedItem().toString(); //Se toma el tipo de pregunta que se esta eliminando
        if(!tipo.equals("Abierta")) tipo="PreguntaMultiple"; //Si no es pregunta abierta seteamos el tipo como pregunta multiple
        PreguntasDao.eliminar_pregunta(this.orden,tipo); //Enviamos a eliminar la pregunta deseada de la lista de preguntas
        if(this.orden<this.PreguntasDao.numero_preguntas()+1){ //Si la pregunta a la que queremos desplazarnos es menor que la cantidad de preguntas existentes
            this.MostrarPregunta(this.PreguntasDao.recuperar_pregunta(this.orden)); //Mostramos la pregunta en la que estamos posicionados
        }else if(this.orden==this.PreguntasDao.numero_preguntas()+1){ //Si estamos posicionados en la siguiente pregunta a agregar
            this.rtxtEnunciado.setText(""); //Limpiamos el enunciado
            this.cbxObligatoria.setSelected(false); //Deseleccionamos obligatoria
            this.btnAgregarPregunta.setText("Agregar Pregunta"); //Cambiamos el texto del boton de agregar pregunta
            this.ConfigurarPanelPregunta(this.cbxTipoPregunta.getModel().getSelectedItem().toString()); //Configuramos el panel de la pregunta con el tipo de pregunta que este seleccionado
        }
        this.Botones();
    }//GEN-LAST:event_btnEliminarPreguntaActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarOpcion;
    public javax.swing.JButton btnAgregarPregunta;
    public javax.swing.JButton btnAnterior;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnEliminarOpcion;
    public javax.swing.JButton btnEliminarPregunta;
    public javax.swing.JButton btnEmpezarCuestionario;
    public javax.swing.JButton btnGrabarEncuesta;
    private javax.swing.JButton btnPerfil;
    public javax.swing.JButton btnRespuestas;
    public javax.swing.JButton btnRevisar;
    public javax.swing.JButton btnSiguiente;
    public javax.swing.JCheckBox cbxObligatoria;
    public javax.swing.JComboBox<String> cbxTipoPregunta;
    public javax.swing.JComboBox<String> cbxidCuestionario;
    private javax.swing.JPanel grbBotones;
    private javax.swing.JPanel grbCCuestionario;
    private javax.swing.JPanel grbCestionariosCreados;
    private javax.swing.JPanel grbCrearCuestionario;
    private javax.swing.JPanel grbCuestionariosCreados;
    private javax.swing.JPanel grbEncabezado;
    public javax.swing.JPanel grbEstructuraPregunta;
    public javax.swing.JPanel grbPregunta;
    private javax.swing.JPanel grbRCuestionarios;
    private javax.swing.JPanel grbRespuestasCuestionarios;
    public javax.swing.JPanel grbTxtEncabezado;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPaneCD;
    private javax.swing.JScrollPane jScrollPaneDescripcion;
    private javax.swing.JScrollPane jScrollPaneUsuariosResponden;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPaneEncuestador;
    public javax.swing.JTable jgdCCreados;
    public javax.swing.JTable jgdUsuarios;
    public javax.swing.JLabel lblAutor;
    private javax.swing.JLabel lblComentario;
    private javax.swing.JLabel lblDesripcion;
    private javax.swing.JLabel lblInfoOpciones;
    public javax.swing.JLabel lblTitulo;
    public javax.swing.JLabel lbliDEncuestador;
    public javax.swing.JList<String> lstOpciones;
    public javax.swing.JTextArea rtxtDescripcion;
    public javax.swing.JTextArea rtxtEnunciado;
    public javax.swing.JTextField txtComentario;
    public javax.swing.JTextField txtTituloEncuesta;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        usuarioActual.cerrar_sesion(); //Cerramos la sesion del usuario
        this.v_principal.setVisible(true); //Hacemos visible la vista anterior
        this.dispose(); //Cerramos esta vista
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
