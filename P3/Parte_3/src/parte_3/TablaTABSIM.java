
package parte_3;

import javax.swing.table.DefaultTableModel;

public class TablaTABSIM extends javax.swing.JFrame {
    DefaultTableModel diseño=new DefaultTableModel();
    public TablaTABSIM() {
        initComponents();
        String[] titulo = new String[]{"TIPO","Si","Ti"};
        diseño.setColumnIdentifiers(titulo);
        TABSIM.setModel(diseño);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        TABSIM = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TABSIM = new javax.swing.JTable(){
            public boolean isCellEditable(int row, int column) {
                // Devuelve false para hacer que todas las celdas sean de solo lectura
                return false;
            }
        };
        TABSIM.setBackground(new java.awt.Color(204, 255, 255));
        TABSIM.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        TABSIM.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TABSIM.setModel(new javax.swing.table.DefaultTableModel(
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
        TABSIM.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TABSIM.setSelectionBackground(new java.awt.Color(0, 204, 204));
        jScrollPane1.setViewportView(TABSIM);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TABSIM;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
