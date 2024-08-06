/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.DialogBox.GrnHistoryDetails;
import GUI.MainFrame.Login;
import Models.MySQL;
import java.awt.Frame;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sandaruwan
 */
public class GRNHistory extends javax.swing.JPanel {

    /**
     * Creates new form UserActivityReports
     */
    public GRNHistory() {
        initComponents();
        loadTable("SELECT * FROM `grn` INNER JOIN `user` ON `user`.`nic` = `grn`.`user_nic` INNER JOIN `grn_payment` ON `grn`.`id` = `grn_payment`.`grn_id`  INNER JOIN `payment_type` ON `grn_payment`.`payment_type_id` = `payment_type`.`id` ORDER BY `grn`.`date_time` DESC");
    }
    
    private void loadTable(String query) {
        
        try {
            ResultSet resultSet = MySQL.execute(query);
            
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);
            int indexNumber = 0;
            int allGrnAmount = 0;
            while (resultSet.next()) {
                indexNumber++;
                
                Vector<String> vector = new Vector<>();
                vector.add(String.valueOf(indexNumber));
                vector.add(String.valueOf(resultSet.getInt("grn.id")));
                vector.add(resultSet.getString("user.fname") + " " + resultSet.getString("lname"));
                vector.add(resultSet.getString("grn.date_time"));
                
                int itemCount = 0;
                int totalAmount = 0;
                ResultSet resultset2 = MySQL.execute("SELECT * FROM `grn_item` WHERE `grn_id`='" + resultSet.getString("id") + "'");
                while (resultset2.next()) {
                    itemCount = itemCount + resultset2.getInt("quantity");
                    totalAmount = totalAmount + (int) (resultset2.getInt("quantity") * resultset2.getDouble("buying_price"));
                }
                
                allGrnAmount = allGrnAmount + totalAmount;
                
                vector.add(String.valueOf(itemCount));
                vector.add(String.valueOf("LKR : " + totalAmount + ".00"));
                
                ResultSet resultset3 = MySQL.execute("SELECT `supplier_details`.`branch_name`,`supplier_details`.`contact_no` FROM `grn` INNER JOIN `supplier_details` ON  `grn`.`supplier_details_id` = `supplier_details`.`id` WHERE `grn`.`id` = '"+resultSet.getInt("grn.id")+"' ");
                
                if(resultset3.next()){
                    vector.add(resultset3.getString("branch_name"));
                    vector.add(resultset3.getString("contact_no"));
                   
                }else{
                    vector.add("");
                    vector.add("");
                }
                
                vector.add(resultSet.getString("type"));
                
                
                
                model.addRow(vector);
                
            }
            jTable2.setModel(model);
            jLabel4.setText(String.valueOf("LKR : " + allGrnAmount + ".00"));
        } catch (Exception e) {
                                  Login.log1.warning(e.getMessage());

        }
        
    }
    
    private void searchGrn() {
        Date fromDate = jDateChooser1.getDate();
        Date toDate = jDateChooser2.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateString = fromDate != null ? sdf.format(fromDate) : "";
        String toDateString = toDate != null ? sdf.format(toDate) : "";
        
//        if (fromDate == null && toDate == null) {
//            JOptionPane.showMessageDialog(this, "Please select both from and to dates.", "Date Selection Error", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
        String query;
        
        String searchtextF = searchtextField.getText();

        
        if(!searchtextF.isBlank()){
            ///
        String SearchText =   searchtextField.getText();
        if(jCheckBox1.isSelected()){
            query = "SELECT * FROM `grn` INNER JOIN `user` ON `user`.`nic` = `grn`.`user_nic` INNER JOIN `grn_payment` ON `grn`.`id` = `grn_payment`.`grn_id`  INNER JOIN `payment_type` ON `grn_payment`.`payment_type_id` = `payment_type`.`id` INNER JOIN `grn_cheque` ON `grn_payment`.`id` = `grn_cheque`.`grn_payment_id` INNER JOIN `cheque_status` ON  `grn_cheque`.`cheque_status_id` = `cheque_status`.`id` INNER JOIN `supplier_details` ON `grn`.`supplier_details_id` = `supplier_details`.`id` WHERE ";
            
            query += " `grn_cheque`.`cheque_no` = '"+SearchText+"'  " ;
        }else{
            query = "SELECT * FROM `grn` INNER JOIN `user` ON `user`.`nic` = `grn`.`user_nic` INNER JOIN `grn_payment` ON `grn`.`id` = `grn_payment`.`grn_id`  INNER JOIN `payment_type` ON `grn_payment`.`payment_type_id` = `payment_type`.`id` INNER JOIN `supplier_details` ON `grn`.`supplier_details_id` = `supplier_details`.`id` WHERE ";
            
            query += " `supplier_details`.`branch_name` LIKE '%"+SearchText+"%' OR `supplier_details`.`contact_no` = '"+SearchText+"' OR `grn_payment`.`supplier_invoice_id` = '"+SearchText+"'  ";
        }
            
      
        if (fromDate == null && toDate == null ) {
         
        query += " ORDER BY `grn`.`id` DESC";
        } 
        if (fromDate != null && toDate == null ) {
        query += " AND `date_time` >= '" + fromDateString + "'  ORDER BY `grn`.`id` DESC ";
        } 
        if (fromDate == null && toDate != null) {
            query += " AND `date_time` <= '" + toDateString + "' ORDER BY `grn`.`id` DESC ";
        } 
        if (fromDate != null && toDate != null) {
            query += " AND `date_time` BETWEEN '" + fromDateString + "' AND '" + toDateString + "'  ORDER BY `grn`.`id` DESC ";
        } 
        
            
        }else{
            
            
            ///
            
        if(jCheckBox1.isSelected()){
        query = "SELECT * FROM `grn` INNER JOIN `user` ON `user`.`nic` = `grn`.`user_nic` INNER JOIN `grn_payment` ON `grn`.`id` = `grn_payment`.`grn_id`  INNER JOIN `payment_type` ON `grn_payment`.`payment_type_id` = `payment_type`.`id` INNER JOIN `grn_cheque` ON `grn_payment`.`id` = `grn_cheque`.`grn_payment_id` INNER JOIN `cheque_status` ON  `grn_cheque`.`cheque_status_id` = `cheque_status`.`id`";
        
           // System.out.println("not word is select");
        }else{
            query = "SELECT * FROM `grn` INNER JOIN `user` ON `user`.`nic` = `grn`.`user_nic` INNER JOIN `grn_payment` ON `grn`.`id` = `grn_payment`.`grn_id`  INNER JOIN `payment_type` ON `grn_payment`.`payment_type_id` = `payment_type`.`id` ";
 
          //  System.out.println("not word is not select");
        }
            
            ///
 
    
        if (fromDate != null && toDate == null ) {
            query += " WHERE `date_time` >= '" + fromDateString + "' ORDER BY `grn`.`id` DESC ";
        } 
        if (fromDate == null && toDate != null) {
            query += " WHERE `date_time` <= '" + toDateString + "' ORDER BY `grn`.`id` DESC ";
        } 
        if (fromDate != null && toDate != null) {
            query += " WHERE `date_time` BETWEEN '" + fromDateString + "' AND '" + toDateString + "' ORDER BY `grn`.`id` DESC ";
        }
            
        }
        
        
  
        
        
        
        loadTable(query);
    }
    
    private void reset() {
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        searchtextField.setText("");
        jCheckBox1.setSelected(false);
        searchGrn();
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
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton3 = new javax.swing.JButton();
        searchtextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(20, 190));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 431, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(716, 150));
        jPanel2.setLayout(new java.awt.GridLayout(4, 1));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel5);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Good Receive Note History");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("LKR 120000000.00");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Total Amount :");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 231, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(24, 24, 24))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel6);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("From");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("To");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Supplier Name / Invoice ID");

        jCheckBox1.setText("Do you want to search cheque Number ?");
        jCheckBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(108, 108, 108)
                .addComponent(jLabel6)
                .addGap(34, 34, 34)
                .addComponent(jCheckBox1)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jCheckBox1))
                .addContainerGap())
        );

        jPanel2.add(jPanel7);

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        searchtextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchtextFieldActionPerformed(evt);
            }
        });
        searchtextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchtextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(searchtextField, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(23, 23, 23))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton3)
                        .addComponent(searchtextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel8);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(20, 190));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 431, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.LINE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(716, 25));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "GRN ID", "User", "Date", "Item Count", "Total Amount", "supplier", "supplier Contact", "payment type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setGridColor(new java.awt.Color(204, 255, 204));
        jTable2.setRowHeight(25);
        jTable2.setRowMargin(2);
        jTable2.setShowGrid(false);
        jTable2.setShowHorizontalLines(true);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable2.getColumnModel().getColumn(3).setResizable(false);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(90);
            jTable2.getColumnModel().getColumn(4).setResizable(false);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(5).setResizable(false);
            jTable2.getColumnModel().getColumn(6).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(7).setPreferredWidth(70);
            jTable2.getColumnModel().getColumn(8).setResizable(false);
            jTable2.getColumnModel().getColumn(8).setPreferredWidth(60);
        }

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        searchGrn();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        reset();

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {
            
            int rowNumber = jTable2.getSelectedRow();
            
            if (rowNumber != -1) {
                
                int grnId = Integer.valueOf(jTable2.getValueAt(rowNumber, 1).toString());
                
                Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
                
                GrnHistoryDetails grnHistoryDetails = new GrnHistoryDetails(parentFrame, true, grnId);
                grnHistoryDetails.setVisible(true);
                
            }
            
        }

    }//GEN-LAST:event_jTable2MouseClicked

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
        
    }//GEN-LAST:event_jCheckBox1StateChanged

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
 if( jCheckBox1.isSelected()){
  jLabel6.setText("Search cheque Number");
 }else{
      jLabel6.setText("Supplier Name /Supplier Invoice ID");
 }
 searchGrn();
 
 
 
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
 searchGrn();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
 searchGrn();
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void searchtextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchtextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchtextFieldActionPerformed

    private void searchtextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextFieldKeyReleased
  searchGrn();
    }//GEN-LAST:event_searchtextFieldKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField searchtextField;
    // End of variables declaration//GEN-END:variables
}
