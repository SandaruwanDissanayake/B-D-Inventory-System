/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.DialogBox.ChequeStatus;
import GUI.MainFrame.Login;
import GUI.MainFrame.MainFrame;
import Models.MySQL;
import com.mysql.cj.protocol.Resultset;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class ChequeHistory extends javax.swing.JPanel {

     private HashMap<String, String> statusMap =new HashMap<>();
    /**
     * Creates new form ChequeHistory
     */
    public ChequeHistory() {
        initComponents();
        loadChequeDetails();
        loadChequeStatus();
    }
    
   
    private void loadChequeStatus() {
        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `cheque_status`");
            Vector vector = new Vector();
            vector.add("Select Status");

            while (resultSet.next()) {
                String status = resultSet.getString("status");
                String statusid = resultSet.getString("id");
                vector.add(status);

            }

            DefaultComboBoxModel combobox = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(combobox);

        } catch (Exception e) {
                       Login.log1.warning(e.getMessage());

        }

    }

    public void loadChequeDetails() {
        try {
           
            ResultSet resultSet = MySQL.execute("SELECT * FROM `check`"
                    + "INNER JOIN `cheque_status` ON `cheque_status`.`id` = `check`.`cheque_status_id`"
                    + "ORDER BY `check`.`id` ASC");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                String id = resultSet.getString("check.id");
                String chqNo = resultSet.getString("check.check_no");
                String bankName = resultSet.getString("check.bank_name");
                String amount = resultSet.getString("check.amount");
                String chqDate = resultSet.getString("check.date_time");
                String status = resultSet.getString("cheque_status.status");
                String exchangedDate = resultSet.getString("check.exchanged_date");
                

                Vector vector = new Vector();
                vector.add(id);
                vector.add(chqNo);
                vector.add(bankName);
                vector.add(amount);
                vector.add(chqDate);
                vector.add(status);
                
               if (status.equals("Exchanged") && exchangedDate != null) {
                vector.add(exchangedDate);
            } else {
                vector.add("Not Exchanged");
            }
                

                model.addRow(vector);

            }
        } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

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
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(664, 60));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Cheque History");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setPreferredSize(new java.awt.Dimension(676, 70));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Cheque Number");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Cheque Status");

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setPreferredSize(new java.awt.Dimension(80, 22));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(320, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(30, 30, 30))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 30, 30, 30));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "55", "NSB", "5000", "2024-5-8", "exchange", "2024-5-4"}
            },
            new String [] {
                "#", "Cheque Number", "Bank name", "Amount", "Date in Cheque", "Status", "Exchange date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(1);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
        }

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1) {
                Object chequeNumber = jTable1.getValueAt(selectedRow, 1);
                Object bank = jTable1.getValueAt(selectedRow, 2);
                Object amount = jTable1.getValueAt(selectedRow, 3);
                Object dateInCheque = jTable1.getValueAt(selectedRow, 4);
                Object chequeStatus = jTable1.getValueAt(selectedRow, 5);
                Object exchangedDate = jTable1.getValueAt(selectedRow, 6);

                // Show the details in a dialog
                Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
                ChequeStatus ChequeStatusFrame = new ChequeStatus(parentFrame,this, chequeNumber, dateInCheque, bank, amount, chequeStatus, exchangedDate);
                ChequeStatusFrame.setVisible(true);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        String cheqNo = jTextField1.getText().trim();
        String status = (String) jComboBox1.getSelectedItem();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

//        ResultSet resultSet = null;
        try {
            ResultSet resultSet = null;

             StringBuilder query = new StringBuilder("SELECT * FROM `check` "
                     + "INNER JOIN `cheque_status` ON `cheque_status`.`id` = `check`.`cheque_status_id` WHERE ");

            if (!cheqNo.isEmpty() && status.equals("Select Status")) {
              query.append("`check`.`check_no` ='").append(cheqNo).append("'");
            } else if (cheqNo.isEmpty() && !status.equals("Select Status")) {
                query.append("`cheque_status`.`status` ='").append(status).append("'");
            } else if (!cheqNo.isEmpty() && !status.equals("Select Status")) {
              query.append("`check`.`check_no` ='").append(cheqNo).append("' AND ").append("`cheque_status`.`status` ='").append(status).append("'");

            }else if (cheqNo.isEmpty() && status.equals("Select Status")){
                JOptionPane.showMessageDialog(this, "Please input Cheque Number Or Cheque Status","Error",JOptionPane.ERROR_MESSAGE);
                
                jTextField1.setText("");
                loadChequeDetails();
                loadChequeStatus();
                return;
            }
             

             resultSet = MySQL.execute(query.toString());

        while (resultSet.next()) {
            String id = resultSet.getString("check.id");
            String chequeNo = resultSet.getString("check.check_no");
            String bankName = resultSet.getString("check.bank_name");
            String amount = resultSet.getString("check.amount");
            String chqDate = resultSet.getString("check.date_time");
            String exchangedDate = resultSet.getString("check.exchanged_date");
            String chqStatus = resultSet.getString("cheque_status.status");

            Vector vector = new Vector();
            vector.add(id);
            vector.add(chequeNo);
            vector.add(bankName);
            vector.add(amount);
            vector.add(chqDate);
            vector.add(chqStatus);
            
            if (status.equals("Exchanged") && exchangedDate != null) {
                vector.add(exchangedDate);
            } else {
                vector.add("Not Exchanged");
            }

            model.addRow(vector);

            }
        } catch (Exception e) {
                       Login.log1.warning(e.getMessage());

        }


    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
