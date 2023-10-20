package parte_4;


import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class Tabla extends javax.swing.JFrame {
    DefaultTableModel diseño=new DefaultTableModel();
    
    //METODO PARA LLENAR LA TABLA
    void Llenado(){
        for (Linea auxiliar : Parte_4.LineasCodigo) {
            diseño.addRow(new Object[]{auxiliar.getConloc(),auxiliar.getEtiqueta(),auxiliar.getCodop(),auxiliar.getOperando(),auxiliar.getADDR(),
                                        auxiliar.getSize(),auxiliar.getPorCalcular()});
        }
    }
    public Tabla() {
        initComponents();
        String[] titulo = new String[]{"CONLOC","ETQ","CODOP","OPR","ADDR","SIZE","POR CALCULAR","FORMA","COP"};
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaCod = new javax.swing.JTable();

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaCod;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
