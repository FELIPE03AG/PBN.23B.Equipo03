package parte_4;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class Tabla extends javax.swing.JFrame {
    DefaultTableModel diseño=new DefaultTableModel();
    String rutaArchivo = " ";
    //METODO PARA LLENAR LA TABLA
    void Llenado(){
        for (Linea auxiliar : Parte_4.LineasASM) {
            diseño.addRow(new Object[]{auxiliar.getConloc(),auxiliar.getEtiqueta(),auxiliar.getCodop(),auxiliar.getOperando(),auxiliar.getADDR(),
                                        auxiliar.getSize(),auxiliar.getPorCalcular(), auxiliar.getForm(), auxiliar.getCop()});
        }
    }
    
    public static String chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Elige el asm");

        // Filtro para buscar solo ciertas extenciones, en este caso que sean .asm
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.asm)", "asm");
        fileChooser.setFileFilter(filter);//Se guarda el archivo elegido

        int returnValue = fileChooser.showOpenDialog(null); // Abre el cuadro de dialogo para seleccionar

        if (returnValue == JFileChooser.APPROVE_OPTION) {//Si el archivo es valido
            String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();//Guarda la direccion del archivo
            return selectedFile;//Retorna la direccion del archivo
        } else if (returnValue == JFileChooser.CANCEL_OPTION) {// Si se cancela la sellecion
            JOptionPane.showMessageDialog(null, "No ha seleccionado ningún archivo", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        } else {
            return null; // Otros casos
        }
    }//Fin seleccionar el archivo
    
    public Tabla() {
        initComponents();
        String[] titulo = new String[]{"CONLOC","ETQ","CODOP","OPR","ADDR","SIZE","POR CALCULAR","FORM","COP"};
        diseño.setColumnIdentifiers(titulo);
        TablaCod.setModel(diseño);
        Llenado();
        
        // Centrar todas las celdas de la tabla
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        for (int i = 0; i < TablaCod.getColumnCount(); i++) {
            TablaCod.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaCod = new javax.swing.JTable();
        btnArchivo = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TablaCod = new javax.swing.JTable(){
            public boolean isCellEditable(int row, int column) {
                // Devuelve false para hacer que todas las celdas sean de solo lectura
                return false;
            }
        };
        TablaCod.setBackground(new java.awt.Color(204, 255, 255));
        TablaCod.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        TablaCod.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TablaCod.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TablaCod.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TablaCod.setSelectionBackground(new java.awt.Color(0, 204, 204));
        jScrollPane1.setViewportView(TablaCod);

        btnArchivo.setText("Abrir");
        btnArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArchivoActionPerformed(evt);
            }
        });

        jLabel1.setText("Abrir archivo distinto -->");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnArchivo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArchivoActionPerformed
        rutaArchivo=chooseFile();
        if(rutaArchivo!=null){
            Parte_4.Leer(rutaArchivo);//Llamo el metodo
            if (Parte_4.LineasASM.size() != 0) {
                Salvacion.BuscarCodop(Parte_4.LineasASM);
                Conloc.LlenarList(Parte_4.LineasASM);
                Conloc.LlenarTabsim(Parte_4.LineasASM);
                CalculoREL.buscarRels();
                this.dispose();
                new Tabla().setVisible(true);
            }
        }
    }//GEN-LAST:event_btnArchivoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaCod;
    private javax.swing.JButton btnArchivo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
