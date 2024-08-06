/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.MainFrame.Login;
import GUI.MainFrame.MainFrame;
import Models.MySQL;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mr. SP
 */
public class ReturnStock extends javax.swing.JPanel {

    private MainFrame mf;

    /**
     * Creates new form ReturnStock
     */
    public ReturnStock(MainFrame mf) {
        initComponents();
        this.mf = mf;
        loadTable("");
    }

    private int loadTable(String query) {
        int i = 0;
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        try {

            ResultSet productset = MySQL.execute("SELECT * FROM `return_product` INNER JOIN `invoice_item` ON `return_product`.`invoice_item_id`=`invoice_item`.`id` INNER JOIN `stock` ON `invoice_item`.`stock_id`=`stock`.`id` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` " + query + " ORDER BY `date_time` ASC");
            while (productset.next()) {
                ResultSet serialset = MySQL.execute("SELECT * FROM `returned_serials` WHERE `return_product_id`='" + productset.getString("return_product.id") + "'");
                String date = new SimpleDateFormat("yyyy-MM-dd").format(productset.getDate("return_product.date_time"));
                String time = new SimpleDateFormat("HH:mm:ss").format(productset.getTime("return_product.date_time"));

                if (serialset.next()) {
                    do {

                        Vector<String> v = new Vector<>();
                        v.add(String.valueOf(i + 1));
                        v.add(productset.getString("invoice_item.invoice_id"));
                        v.add(productset.getString("product.title"));
                        v.add(productset.getString("product.id"));
                        v.add(serialset.getString("serial_num"));
                        v.add("1");
                        v.add(date);
                        v.add(time);
                        model.addRow(v);
                        jTable1.setModel(model);
                        i++;
                    } while (serialset.next());

                } else {

                    Vector<String> v = new Vector<>();
                    v.add(String.valueOf(i + 1));
                    v.add(productset.getString("invoice_item.invoice_id"));
                    v.add(productset.getString("product.title"));
                    v.add(productset.getString("product.id"));
                    v.add("No Serial");
                    v.add(productset.getString("invoice_item.return_qty"));
                    v.add(date);
                    v.add(time);
                    model.addRow(v);
                    jTable1.setModel(model);
                    i++;

                }

            }
        } catch (Exception ex) {
                                  Login.log1.warning(ex.getMessage());

        }

        return i;
    }

    private int serachProduct(String id) {
        int count = loadTable("WHERE `product`.`id`='" + id + "'");
        return count;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Returned Stock");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Invoice Id", "Product Title", "Barcode", "Serial Number", "Quantity", "Return Date", "Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(17);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
        }

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Barcode");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Back");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel3MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = jTextField1.getText();
            if (text.isEmpty() || text.isBlank()) {
                loadTable("");
                jTextField1.setText("");
                jTextField1.grabFocus();

            } else {
                int count = serachProduct(text);
//                System.out.println(count);
                if (count == 0) {
                    loadTable("");
                    JOptionPane.showMessageDialog(this, "Invalid Number. Please use barcode number to search Products.", "Warning", JOptionPane.WARNING_MESSAGE);

                }
                jTextField1.setText("");
                jTextField1.grabFocus();
            }

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadTable("");
        jTextField1.grabFocus();
        jTextField1.setText("");
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseReleased
        MainFrame.jPanel5.removeAll();

        ReturnManagmentWelcome welcomeFrame = new ReturnManagmentWelcome(mf);
        MainFrame.jPanel5.add(welcomeFrame);
        MainFrame.jPanel5.repaint();
        MainFrame.jPanel5.revalidate();
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
