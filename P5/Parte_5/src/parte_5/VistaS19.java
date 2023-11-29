
package parte_5;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class VistaS19 extends javax.swing.JFrame {
    DefaultTableModel diseño=new DefaultTableModel();
    void Llenado(){//Llenado de tabla con los S del S19
        for (S19 auxiliar : Proceso_S19.DatosS19) {//For para recorrer la lista en la que estan
            //añade cada s en una fila
            diseño.addRow(new Object[]{auxiliar.getSn(),auxiliar.getCc(),auxiliar.getAddr(),auxiliar.getData(),auxiliar.getCk()});
        }//Fin del for
    } //fin de metodo llenar
    
    void Vaciado (){//Borra todas las filas de la tabla
        for (int i=diseño.getRowCount()- 1; i >= 0; i--) {//For para recorrer las filas de la tabla
            diseño.removeRow(i);//Elimina la fila
        }//Fin for
    }//Fin vaciado
    
    void inicializacion(){//Metodo para personalizar y cargar la tabla
        String[] titulo = new String[]{"Sn","CC","ADDR","DATA","CK"};//titulos de las columnas
        diseño.setColumnIdentifiers(titulo);//Agrega los titulos
        TablaS19.setModel(diseño);//Lo personaliza en la tabla
        Vaciado();//Se vacia antes de volver a cargar por si ya tenia datos
        Llenado();//Agrega los s
        // Centrar todas las celdas de la tabla
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        for (int i = 0; i < TablaS19.getColumnCount(); i++) {
            TablaS19.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }//Para centrar cada columna
    }//Fin inicialización
    
    public VistaS19() {//Metodo principal
        initComponents();//Carga de todo el frame
    }//Fin VistaS19

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaS19 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TablaS19 = new javax.swing.JTable(){
            public boolean isCellEditable(int row, int column) {
                // Devuelve false para hacer que todas las celdas sean de solo lectura
                return false;
            }
        };
        TablaS19.setBackground(new java.awt.Color(204, 255, 255));
        TablaS19.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        TablaS19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TablaS19.setModel(new javax.swing.table.DefaultTableModel(
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
        TablaS19.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TablaS19.setSelectionBackground(new java.awt.Color(0, 204, 204));
        jScrollPane1.setViewportView(TablaS19);

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
    private javax.swing.JTable TablaS19;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
