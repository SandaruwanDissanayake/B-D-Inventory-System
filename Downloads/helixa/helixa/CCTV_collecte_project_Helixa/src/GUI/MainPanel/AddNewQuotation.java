/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.MainFrame.Login;
import Models.MySQL;
import Models.Quotation_details;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author PCnet Computers
 */
public class AddNewQuotation extends javax.swing.JPanel {

    public static AddProductQuotation addProductQuotation;

    /**
     * Creates new form NewJPanel
     */
    public AddNewQuotation() {
        initComponents();
        load_customer_type();
        jCheckBox1.setSelected(true);
 
    }

    public void resetId() {

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {

            jTable1.setValueAt(String.valueOf(i + 1), i, 0);

        }
    }

    private void jasperInvoicePrint(HashMap parameters, Vector getVector) {

        try {

            InputStream report = getClass().getResourceAsStream("/Report/Quotation_Report.jasper");

//            JasperDesign jd = JRXmlLoader.load(report);
//            JasperReport jasreport = JasperCompileManager.compileReport(jd);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(getVector);

            //   JREmptyDataSource dataSource = new JREmptyDataSource();
            JasperPrint jp = JasperFillManager.fillReport(report, parameters, dataSource);

            if (jCheckBox1.isSelected()) {
                JasperViewer.viewReport(jp, false);
            } else {
                JasperPrintManager.printReport(jp, false);
            }

        } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

        }
    }

    public void calculate_Total() {
        Double total = 00.00;
        int set_qty = 0;
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

        for (int i = 0; i < dtm.getRowCount(); i++) {
            String getTotal = jTable1.getValueAt(i, 8).toString();
            total = total + Double.parseDouble(getTotal);

            String new_qty = jTable1.getValueAt(i, 7).toString();
            set_qty = set_qty + Integer.parseInt(new_qty);
        }

        jLabel4.setText(String.valueOf(total));
        jLabel6.setText(String.valueOf(set_qty));
    }

    public void change_data() {

        String customer_type = jComboBox1.getSelectedItem().toString();
        if (customer_type.equals("Select Customer Type")) {
            //JOptionPane.showMessageDialog(this, "amanda", "warning", JOptionPane.WARNING_MESSAGE);

        } else {
            String pid;
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            int r = model.getRowCount();

            for (int i = 0; i < r; i++) {

                pid = jTable1.getValueAt(i, 1).toString();

                try {
                    ResultSet resultSet = MySQL.execute("SELECT * FROM `stock` WHERE `product_id` = '" + pid + "' ");

                    String item_price;

                    if (resultSet.next()) {

                        System.out.println("ok");  //  

                        if (customer_type.equals("Shop")) {
                            item_price = resultSet.getString("shop_price");
                        } else if (customer_type.equals("Daily Customer")) {
                            item_price = resultSet.getString("daily_cutomer_price");
                        } else {
                            item_price = resultSet.getString("new_customer_price");
                        }

                        
                        jTable1.setValueAt(item_price, i, 6);

                        String Qty = model.getValueAt(i, 7).toString();
                       

                        Double newitemTotal =Integer.parseInt(Qty) * Double.parseDouble(item_price);
                       
                        jTable1.setValueAt(newitemTotal, i, 8);

                        calculate_Total();
                    }
                } catch (Exception e) {
                                Login.log1.warning(e.getMessage());

                }

            }

        }

    }

    public void load_customer_type() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `customer_type`");

            Vector v = new Vector();
            v.add("Select Customer Type");

            while (rs.next()) {
                v.add(rs.getString("type"));
            }

            DefaultComboBoxModel dcb = new DefaultComboBoxModel(v);
            jComboBox1.setModel(dcb);

        } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

        }
    }

    public void cleardialogs() {
        if (addProductQuotation != null) {
            addProductQuotation.dispose();
            addProductQuotation = null;
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

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Add New Quotation");

        jLabel3.setText("Customer Type");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jComboBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox1PropertyChange(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "barcode", "Product Title", "Brand", "Product Model", "Description", "Unique Item Price", "Quantity", "ItemTotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jButton1.setText("Add Product to Quotation");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Total");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("0");

        jLabel5.setText("Number of Products");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("0");

        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 545, Short.MAX_VALUE)
                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 504, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(19, 19, 19))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jComboBox1.getSelectedItem().toString().equals("Select Customer Type")) {
            JOptionPane.showMessageDialog(this, "Plese Select Customer Type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            cleardialogs();
            addProductQuotation = new AddProductQuotation(this, true);

            addProductQuotation.setVisible(true);
//            jComboBox1.setEnabled(false);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        int rc = jTable1.getRowCount();
        Vector v = new Vector();
        String Total = jLabel4.getText();
        String All_qty = jLabel6.getText();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dNow = sdf.format(d);

        if (rc > 0) {

            for (int i = 0; i < rc; i++) {
                v.add(new Quotation_details(dtm.getValueAt(i, 0).toString(),
                        dtm.getValueAt(i, 5).toString() + " " + dtm.getValueAt(i, 2).toString() + " " + dtm.getValueAt(i, 3).toString(),
                        dtm.getValueAt(i, 7).toString(), dtm.getValueAt(i, 8).toString()));
            }

            HashMap parameters = new HashMap();
            parameters.put("Parameter1", dNow);
            parameters.put("Parameter2", All_qty);
            parameters.put("Parameter3", Total);

            jasperInvoicePrint(parameters, v);

        } else {
            JOptionPane.showMessageDialog(this, "Plese Select Product ", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        dtm.setRowCount(0);
        jComboBox1.setEnabled(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        if (evt.getClickCount() == 2) {

            String[] option = {"remove product", "edit quantity"};
            var editTable = JOptionPane.showOptionDialog(null, "Select your requirement", "Confirmation", 0, 2, null, option, option[0]);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            if (editTable == 0) {
                model.removeRow(jTable1.getSelectedRow());
                resetId();
//                JOptionPane.showMessageDialog(null, "Remove OK");
            } else if (editTable == 1) {
                String qty = model.getValueAt(jTable1.getSelectedRow(), 7).toString();
                var newQty = JOptionPane.showInputDialog("Edit Product Quantiy", qty);

                if (newQty.equals("")) {
                    jTable1.setValueAt(qty, jTable1.getSelectedRow(), 7);
                } else {
                    jTable1.setValueAt(newQty, jTable1.getSelectedRow(), 7);
                }
                String uniquPrice = model.getValueAt(jTable1.getSelectedRow(), 6).toString();
                String newitemTotal = String.valueOf(Integer.parseInt(newQty) * Double.parseDouble(uniquPrice));
                jTable1.setValueAt(newitemTotal, jTable1.getSelectedRow(), 8);

            } else {

            }

            calculate_Total();

//            int option = JOptionPane.showConfirmDialog(this, "Do You Want to remove this product ? ", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
//
//            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//            if (option == JOptionPane.YES_OPTION) {
//                model.removeRow(jTable1.getSelectedRow());
//
//            } else {
//            }
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        //jTable1.clearSelection();
        jComboBox1.setSelectedIndex(0);
        jComboBox1.setEnabled(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox1PropertyChange

        change_data();

    }//GEN-LAST:event_jComboBox1PropertyChange

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        change_data();
    }//GEN-LAST:event_jComboBox1ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    public javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
