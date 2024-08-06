/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.DialogBox;

import GUI.MainFrame.Login;
import GUI.MainPanel.GRN;
import Models.MySQL;
import Models.SupplierDetailsBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Sandaruwan
 */
public class AddNewSupplier extends javax.swing.JDialog {

    /**
     * Creates new form AddNewSupplier
     */
    private SupplierDetailsBean supplierBean;
    private SupplierDetails supplierDetalisPanel;
    private GRN grnframe;

    public AddNewSupplier(java.awt.Frame parent, boolean modal, SupplierDetails supplierDetails, SupplierDetailsBean supplierBeans, GRN grnFrame) {
        super(parent, modal);
        initComponents();
        this.supplierBean = supplierBeans;
        this.supplierDetalisPanel = supplierDetails;
        this.grnframe = grnFrame;
        jLabel6.setVisible(false);

    }

    private boolean edit = false;
    private String branch;
    private String contactNum;

    public AddNewSupplier(java.awt.Frame parent, boolean modal, SupplierDetails supplierDetails, SupplierDetailsBean supplierBeans, GRN grnFrame, boolean edit, String brach, String contactNum) {
        super(parent, modal);
        initComponents();
        this.supplierBean = supplierBeans;
        this.supplierDetalisPanel = supplierDetails;
        this.grnframe = grnFrame;
        this.edit = edit;
        this.branch = brach;
        this.contactNum = contactNum;
        loadData();
        jLabel6.setVisible(false);

    }

    private void loadData() {
        if (edit) {
            jLabel1.setText("Update Supplier Details");
            jButton1.setText("Update");
            try {
                ResultSet result = MySQL.execute("SELECT * FROM `supplier_details` WHERE `branch_name`='" + this.branch + "' AND `contact_no`='" + this.contactNum + "'");

                if (result.next()) {
                    jTextField1.setText(result.getString("branch_name"));
                    jTextField2.setText(result.getString("contact_no"));
                    jTextField3.setText(result.getString("address_line1"));
                    jTextField4.setText(result.getString("address_line2"));
                    jLabel6.setText(result.getString("id"));
                }
            } catch (Exception ex) {
                Login.log1.warning(ex.getMessage());
            }

        }

    }

    private void addnewSupplier() {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String branchName = jTextField1.getText();
        String contactNumber = jTextField2.getText();
        String addressLine1 = jTextField3.getText();
        String addressLine2 = jTextField4.getText();

        if (branchName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pleace Enter Branch Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (edit) {
                    MySQL.execute("UPDATE `supplier_details` "
                            + "SET `branch_name`='" + branchName + "',`contact_no`='" + contactNumber + "',`address_line1`='" + addressLine1 + "',`address_line2`='" + addressLine2 + "' "
                            + "WHERE `id`='" + jLabel6.getText() + "'");
                    JOptionPane.showMessageDialog(this,
                            "The update was completed successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    this.supplierDetalisPanel.loadSupplierTable();
                    this.dispose();

                } else {
                    ResultSet resultSet = MySQL.execute("SELECT * FROM `supplier_details` WHERE `branch_name`='" + branchName + "'");
                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "This Branch Alredy Added", "Warning", JOptionPane.WARNING_MESSAGE);

                    } else {

                        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cctv_shop_db", "root", "******");

                        // Correct SQL statement with proper columns and parameter placeholders
                        String insertSQL = "INSERT INTO `supplier_details` (`branch_name`, `contact_no`, `address_line1`, `address_line2`) VALUES (?, ?, ?, ?)";
                        pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);

                        pstmt.setString(1, branchName);
                        pstmt.setString(2, contactNumber);
                        pstmt.setString(3, addressLine1);
                        pstmt.setString(4, addressLine2);

                        // Execute the insert statement
                        pstmt.executeUpdate();

                        // Get the generated keys
                        rs = pstmt.getGeneratedKeys();
                        if (rs.next()) {
                            long lastGRNInsertId = rs.getLong(1);
                            int intId = (int) lastGRNInsertId;
                            supplierBean.setId(intId);
                            supplierBean.setContactNumber(contactNumber);
                            supplierBean.setContactNumber(contactNumber);
                            supplierBean.setAddressLine1(addressLine1);
                            supplierBean.setAddressLine1(addressLine2);

                            grnframe.loadSupplierDetails();

                            supplierBean.setId(0);
                            supplierBean.setContactNumber("");
                            supplierBean.setContactNumber("");
                            supplierBean.setAddressLine1("");
                            supplierBean.setAddressLine1("");

                            this.dispose();
                            this.supplierDetalisPanel.dispose();

                        }

                    }
                }

            } catch (SQLException ex) {
                Login.log1.warning(ex.getMessage());
            } catch (Exception ex) {
                Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                // Clean up resources
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    Login.log1.warning(e.getMessage());
                }
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                } catch (SQLException e) {
                    Login.log1.warning(e.getMessage());
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    Login.log1.warning(e.getMessage());
                }
            }

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
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Add New Supplier");

        jLabel2.setText("Branch Name :");

        jLabel3.setText("Contact No :");

        jLabel4.setText("Address Line 1 :");

        jLabel5.setText("Address Line 2 :");

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1)
                    .addComponent(jTextField2)
                    .addComponent(jTextField3)
                    .addComponent(jTextField4)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(0, 306, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        addnewSupplier();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(AddNewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewSupplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
