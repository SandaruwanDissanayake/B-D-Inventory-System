/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.DialogBox;

import GUI.MainFrame.Login;
import GUI.MainFrame.MainFrame;
import GUI.MainPanel.ReturnManagementProducts;
import Models.MySQL;
import Models.ReturnProductBean;
import com.mysql.cj.jdbc.PreparedStatementWrapper;
import java.sql.Connection;
import java.awt.Component;
import java.awt.Frame;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
//import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Mr. SP
 */
public class ReturnProductsConfirmation extends javax.swing.JDialog {
//    ReturnManagementProducts returnManagementProducts;

    /**
     * Creates new form ReturnProductsConfirmation
     */
    private DefaultTableModel tableModel;
    private String invoiceId;
    private ReturnProductsConfirmation returnProductsConfirmation;
    private int customerTypeId;
    private int paymentPlanId;
    private HashMap<String, ReturnProductBean> returnProduct;
    private HashMap<String, ReturnProductBean> returnProductcopy;
    private ReturnManagementProducts rmp;
    private MainFrame mainFram;

    public ReturnProductsConfirmation(Frame parent, ReturnManagementProducts rmp, Object[][] rowData, String invoiceId, int customerTypeId, int paymentPlanId, HashMap<String, ReturnProductBean> returnProduct, MainFrame mf) {
        super(parent, true);
        initComponents();
        loadTable(rowData);
        this.invoiceId = invoiceId;
        updateTotal();
        this.customerTypeId = customerTypeId;
        this.paymentPlanId = paymentPlanId;
        this.returnProduct = returnProduct;
        this.returnProductcopy = returnProduct;
        this.rmp = rmp;
        this.mainFram = mf;
        jButton1.grabFocus();
    }

    public ReturnProductsConfirmation() {
        initComponents();
    }

    private void loadTable(Object[][] rowData) {

        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);

        for (Object[] row : rowData) {
//           
            dtm.addRow(row);
            jTable1.setModel(dtm);
        }

//        updateTotal();
        jTable1.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 7) {
//                    updateTotal();
                    int firstRow = e.getFirstRow();
                    int column = e.getColumn();
                    Object valueAt = jTable1.getValueAt(firstRow, column);
//
                    Object name = rowData[firstRow][column];
//                    System.out.println(oldValue);

                    try {
                        int newValue = Integer.parseInt(valueAt.toString());
                        int oldValue = Integer.parseInt(name.toString());

                        if (newValue > 0 && newValue <= oldValue) {
                            updateTotal();
                        } else {
                            JOptionPane.showMessageDialog(null, "Enter Valid Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
                            jTable1.setValueAt(name, firstRow, column);

                        }

                    } catch (NumberFormatException ex) {
                                    Login.log1.warning(ex.getMessage());

                        JOptionPane.showMessageDialog(null, "Enter Valid Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
                        jTable1.setValueAt(name, firstRow, column);
                    }

                }
            }
        });
    }

    private double parseDouble(String value) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        Number number = format.parse(value);
        return number.doubleValue();
    }

    private void updateTotal() {
        int total_quantity = 0;
        double total_value = 0.0;
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                int quantity = Integer.parseInt(tableModel.getValueAt(i, 7).toString());
                total_quantity += quantity;

                double itemPrice = Double.parseDouble(tableModel.getValueAt(i, 6).toString());
                double products_value = quantity * itemPrice;
                total_value += products_value;
            } catch (Exception e) {
                // Handle parsing error
                            Login.log1.warning(e.getMessage());

                JOptionPane.showMessageDialog(this, "Invalid number format in row " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        jLabel4.setText(String.valueOf(total_quantity));
        jLabel5.setText(String.format("%.2f", total_value));
    }

    private Object[][] getTableData() {
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

        int rowCount = tableModel.getRowCount();
        int columnCount = tableModel.getColumnCount();
        Object[][] tableData = new Object[rowCount][columnCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                tableData[i][j] = tableModel.getValueAt(i, j);
            }
        }

        return tableData;
    }

    public void updateProcess(double totalValue, String invoiceId) {

        try {
            Double.parseDouble(jLabel5.getText());
        } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

            JOptionPane.showMessageDialog(this, "Invalid total value format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String formattedDate = "";

        try {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String productId = tableModel.getValueAt(i, 3).toString();
                String quantity = tableModel.getValueAt(i, 7).toString();
                String serialNumber = tableModel.getValueAt(i, 4).toString();
                String invId = this.invoiceId;
                int qty = Integer.parseInt(jLabel4.getText());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                formattedDate = sdf.format(now);

                MySQL.execute("INSERT INTO return_product (date_time, product_id, invoice_id)"
                        + "VALUES ('" + formattedDate + "',"
                        + "        (SELECT id FROM product WHERE id = '" + productId + "'),"
                        + "        (SELECT id FROM invoice WHERE id = '" + invId + "'));");

                MySQL.execute("UPDATE invoice_item SET return_qty = " + qty + ", return_status_id = " + 2 + ""
                        + " WHERE invoice_id = (SELECT id FROM invoice WHERE id = " + invId + ")"
                        + "AND stock_id = (SELECT id FROM stock WHERE product_id = " + productId + ");");

                if (!serialNumber.equals("NO")) {
                    MySQL.execute("UPDATE product_sn SET product_sn_status_id = '" + 2 + "'"
                            + " WHERE product_id = (SELECT id FROM product WHERE id = " + productId + ")"
                            + " AND serial_number = '" + serialNumber + "' ");
                }
            }
        } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(808, 65));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Confirm Products");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(553, 553, 553))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(11, 11, 11))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Product Title", "Product Model", "Barcode", "Seriel Number", "Brand", "Item Price", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable1PropertyChange(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTable1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("Total Quantity :");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("9");

        jLabel3.setText("Total Amount :");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(" 13444.00");

        jButton1.setText("Confirm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 768, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(22, 22, 22))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable1PropertyChange
//        int row = jTable1.getSelectedRow();
//        System.out.println(row);// TODO add your handling code here:
//        if(row!=-1){
//                 String newValue = (String) jTable1.getValueAt(row, 7);
//        System.out.println(row +" - value: "+newValue);   
//        }

// TODO add your handling code here:

    }//GEN-LAST:event_jTable1PropertyChange
    public void arrangeReturnProduct() {
        this.rmp.resetBean();

    }
    


    public void viewP() {
        for (Map.Entry<String, ReturnProductBean> product : this.returnProduct.entrySet()) {
            ReturnProductBean bean = product.getValue();
            System.out.println("invoice item id -" + bean.getInvoiceItemId());
            System.out.println("qty -" + bean.getReturnQty());
            System.out.println("stock id -" + bean.getStockId());
            System.out.println("status -" + bean.isIsReturn());
            for (Map.Entry<String, Boolean> s : bean.getSerialNumbers().entrySet()) {
                Boolean value = s.getValue();
                String key = s.getKey();
                System.out.println("     sn - " + key + " - " + value);
            }
            System.out.println(" ");
            System.out.println(" ");
        }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        double totalValue = Double.parseDouble(jLabel5.getText());
        String formattedDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        formattedDate = sdf.format(now);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String barcode = (String) jTable1.getValueAt(i, 3);
            String serial = (String) jTable1.getValueAt(i, 4);
            String defaultQty = (String) jTable1.getValueAt(i, 7);
            ReturnProductBean bean = this.returnProduct.get(barcode);
            HashMap<String, Boolean> serialNumbers = bean.getSerialNumbers();
            int newQty = bean.getReturnQty() + 1;

            if (!serialNumbers.isEmpty()) {
                serialNumbers.put(serial, true);
                bean.setReturnQty(newQty);
            } else {
                bean.setReturnQty(Integer.parseInt(defaultQty));

            }
            bean.setIsReturn(true);

        }


//        
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        ChangeProducts changeProducts = new ChangeProducts(parentFrame, true, this, this.customerTypeId,this.returnProduct,this.mainFram);
        changeProducts.setTotalValue(totalValue, invoiceId, paymentPlanId);
        changeProducts.setFormattedDate(formattedDate);
        changeProducts.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyTyped
        // TODO add your handling code here:
//        updateTotal();
//System.out.println("ok");
    }//GEN-LAST:event_jTable1KeyTyped

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
            java.util.logging.Logger.getLogger(ReturnProductsConfirmation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReturnProductsConfirmation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReturnProductsConfirmation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReturnProductsConfirmation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ReturnProductsConfirmation dialog = new ReturnProductsConfirmation(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
