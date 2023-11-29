
package parte_5;
import javax.swing.table.DefaultTableModel;

public class VistaS19 extends javax.swing.JFrame {
    DefaultTableModel diseño=new DefaultTableModel();
    void Llenado(){
        for (S19 auxiliar : Proceso_S19.DatosS19) {
            diseño.addRow(new Object[]{auxiliar.getSn(),auxiliar.getCc(),auxiliar.getAddr(),auxiliar.getData(),auxiliar.getCk()});
        }
    } //fin de metodo llenar
    
    void Vaciado (){
        for (int i=diseño.getRowCount()- 1; i >= 0; i--) {
            diseño.removeRow(i);
        }
    }
    
    void inicializacion(){
        String[] titulo = new String[]{"Sn","CC","ADDR","DATA","CK"};
        diseño.setColumnIdentifiers(titulo);
        TablaCod.setModel(diseño);
        Vaciado();
        Llenado();
    }
    public VistaS19() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaCod = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

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

        jLabel1.setFont(new java.awt.Font("Yu Gothic Medium", 1, 24)); // NOI18N
        jLabel1.setText("S19");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(652, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addContainerGap(144, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(45, 45, 45)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaCod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
