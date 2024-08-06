/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.DialogBox;

import GUI.MainFrame.Login;
import GUI.MainPanel.GRN;
import Models.AddGRNBean;
import Models.MySQL;
import java.awt.Label;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sandaruwan
 */
public class AddSerialNumber extends javax.swing.JDialog {

    int tableId = 0;
    private GRN grn;
    private HashMap<Integer, AddGRNBean> addGRNProductBeanMap;
    private AddGRNBean currentBean;
    private AddProductToGRN addProductGrn;
    private int productQty = 0;

    public AddSerialNumber(java.awt.Frame parent, boolean modal, String title, String modelNumber, int qty, HashMap<Integer, AddGRNBean> addGRNProductBeanMap, GRN grnframe, AddGRNBean currentBean, AddProductToGRN addProductgrn) {
        super(parent, modal);
        initComponents();
        this.addGRNProductBeanMap = addGRNProductBeanMap;
        this.currentBean = currentBean;
        this.grn = grnframe;
        jLabel2.setText(title);
        jLabel3.setText(modelNumber);
        jButton3.setVisible(false);
        this.addProductGrn = addProductgrn;
        this.productQty = qty;
        loadSerialNumber();

    }

    boolean edit;

    public AddSerialNumber(java.awt.Frame parent, boolean modal, String title, String modelNumber, int qty, HashMap<Integer, AddGRNBean> addGRNProductBeanMap, GRN grnframe, AddGRNBean currentBean, boolean edit) {
        super(parent, modal);
        initComponents();
        this.edit = edit;
        this.addGRNProductBeanMap = addGRNProductBeanMap;
        this.currentBean = currentBean;
        this.grn = grnframe;
        jLabel2.setText(title);
        jLabel3.setText(modelNumber);
        loadSerialNumber();
        jButton3.setVisible(false);
    }

    private boolean checkSerialNumber() {
        String serialNumberToCheck = jTextField1.getText().trim(); // Get and trim the serial number from the text field
        int rowCount = jTable1.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            Object value = jTable1.getValueAt(i, 1); // Retrieve the value at the specific cell

            if (value != null && value.toString().trim().equals(serialNumberToCheck)) {
                JOptionPane.showMessageDialog(this, "This serial number is already added", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private boolean checkSndb() {
        String serialNumberToCheck = jTextField1.getText().trim();

        try {
            ResultSet snNum = MySQL.execute("SELECT * FROM `product_sn` WHERE `serial_number`='" + serialNumberToCheck + "'");

            if (snNum.next()) {
                JOptionPane.showMessageDialog(this, "This serial number alredy added the system", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;

            }

        } catch (Exception ex) {
            Login.log1.warning(ex.getMessage());
        }
        return true;
    }

    private void loadSerialNumber() {
        String modelNumber = jLabel3.getText();
        String title = jLabel2.getText();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        int indexNumber = 0;
        for (Map.Entry<Integer, AddGRNBean> entry : addGRNProductBeanMap.entrySet()) {

            if (entry.getValue().getModelNumber().equals(jLabel3.getText()) && entry.getValue().getTitle().equals(jLabel2.getText())) {
                System.out.println("Serial number :" + entry.getValue().getSerialNumberList());

                for (String serialNumber : entry.getValue().getSerialNumberList()) {
                    Vector<Object> vector = new Vector<>();

                    indexNumber++;
                    vector.add(indexNumber);
                    vector.add(serialNumber);
                    model.addRow(vector);

                }

            }

        }

    }

    private void addSerialNumber() {

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        String serialNumber = jTextField1.getText();

        if (!serialNumber.isEmpty()) {
            if (checkSerialNumber() && checkSndb()) {
                tableId++;
                Vector<Object> vector = new Vector<>();
                vector.add(tableId);
                vector.add(serialNumber);

                model.addRow(vector);
                jTextField1.setText("");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Pleace Enter Serial Number", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Add Serial Number");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Hikvision Light weight camera night vision colour 2MP");

        jLabel3.setText("DS-2CE17D0T-LFS");

        jLabel4.setText("Serial Number");

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Serial number"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(380);
        }

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Remove");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        addSerialNumber();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        String modelNumber = jLabel3.getText();
        String title = jLabel2.getText();

        int rowCount = jTable1.getRowCount();

        if (rowCount != this.productQty) {
            JOptionPane.showMessageDialog(this,
                    "Product quantity and serial number do not match. Please enter all product serial numbers.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } else {

            for (Map.Entry<Integer, AddGRNBean> beanDate : addGRNProductBeanMap.entrySet()) {

                if (beanDate.getValue().getModelNumber().equals(modelNumber) && beanDate.getValue().getTitle().equals(title)) {
                    for (int i = 0; i < jTable1.getRowCount(); i++) {
                        Object value = jTable1.getValueAt(i, 1);

                        currentBean.addSerialNumber(value.toString());
                    }

                }

            }
            if (!edit) {
                addProductGrn.clearAddProducttoGRN();
            }
            this.dispose();
        }
//        System.out.println("hello");
//
//        String title = jLabel2.getText();
//
//        currentBean.setModelNumber(modelNumber);
//        currentBean.setTitle(title);
//        for (int i = 0; i < jTable1.getRowCount(); i++) {
//            Object value = jTable1.getValueAt(i, 1);
//            System.out.println(value.toString());
//            currentBean.addSerialNumber(value.toString());
//
//        }
//
//        addGRNProductBeanMap.put(grn.mapIndex, currentBean);
//        System.out.println(grn.mapIndex);
//        System.out.println(currentBean.getTitle());
//        System.out.println("suc");
//
//        System.out.println("out for");

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow != -1) {

            String modelNumber = jLabel3.getText();
            String titleSerialNumber = jLabel2.getText();
            String serialNumber = String.valueOf(jTable1.getValueAt(selectedRow, 1));

            try {
                for (Map.Entry<Integer, AddGRNBean> entry : addGRNProductBeanMap.entrySet()) {
                    AddGRNBean bean = entry.getValue();

                    if (modelNumber.equals(bean.getModelNumber()) && titleSerialNumber.equals(bean.getTitle())) {
                        // Remove the serial number from the list
                        bean.getSerialNumberList().remove(serialNumber);
                        break;
                    }
                }

            } catch (Exception e) {
                Login.log1.warning(e.getMessage());
            }
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.removeRow(selectedRow);

            loadSerialNumber();

            jTable1.revalidate();
            jTable1.repaint();

            if (jTable1.getRowCount() == 0) {
                jButton3.setVisible(false);
            }

        } else {
            JOptionPane.showMessageDialog(null, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {
            jButton3.setVisible(true);
        }


    }//GEN-LAST:event_jTable1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddSerialNumber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddSerialNumber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddSerialNumber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddSerialNumber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                AddSerialNumber dialog = new AddSerialNumber(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
