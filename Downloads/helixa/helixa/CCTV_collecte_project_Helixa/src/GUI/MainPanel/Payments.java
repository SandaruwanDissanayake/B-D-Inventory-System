/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;


import GUI.DialogBox.ShowInvoiceDetails;
import GUI.MainFrame.Login;
import Models.MySQL;
import java.awt.Frame;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sandaruwan
 */
public class Payments extends javax.swing.JPanel {

    /**
     * Creates new form Payments
     */
    public Payments() {
        initComponents();
        loadInvoiceDetails("");

    }

    public void loadInvoiceDetails(String quryAttach) {
        try {

            ResultSet resultset = MySQL.execute("SELECT * FROM `invoice` \n"
                    + "INNER JOIN `customer_details` ON `invoice`.customer_details_nic=`customer_details`.`nic` \n"
                    + "INNER JOIN `payment_option` ON `invoice`.payment_option_id=`payment_option`.`id`" + quryAttach + "\n"
                    + "ORDER BY `date_time` DESC");

            DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
            tableModel.setRowCount(0);

            while (resultset.next()) {

                Vector<String> vector = new Vector();
                vector.add(resultset.getString("id"));

                //split the date
                String[] parts = resultset.getString("date_time").split(" ");
                String dateString = parts[0];
                String timeString = parts[1];

                vector.add(dateString);
                int dueAmount = 0;
                int paidAmount = resultset.getInt("total_amount");
                
                if(resultset.getInt("payment_option_id")!=1){
                    
                    ResultSet paymentValueRow = MySQL.execute("SELECT * FROM `payment_value` WHERE `invoice_id`='"+resultset.getString("id")+"'");
                    if(paymentValueRow.next()){
                         dueAmount = paymentValueRow.getInt("total");
                         paidAmount = resultset.getInt("total_amount")- dueAmount;
                    }
                }
                vector.add(String.valueOf(resultset.getInt("total_amount")));
                vector.add(String.valueOf(paidAmount));
                

                //calculate the balance amount
                vector.add(String.valueOf(dueAmount));

                //calculate the due date            
                if (resultset.getInt("payment_option_id") == 1) {
                    vector.add("Completed");
                } else if (resultset.getInt("payment_option_id") == 2) {

                    vector.add(calculateDueDate(dateString, 1));
                } else if (resultset.getInt("payment_option_id") == 3) {

                    vector.add(calculateDueDate(dateString, 3));
                }

                vector.add(resultset.getString("option"));
                vector.add(resultset.getString("full_name"));

                tableModel.addRow(vector);
                jTable2.setModel(tableModel);

            }

        } catch (Exception e) {
                                   Login.log1.warning(e.getMessage());

        }
    }

    public String calculateDueDate(String invoiceDate, int months) {

        DateTimeFormatter formatdatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(invoiceDate, formatdatetime);
        LocalDate duedate = date.plus(months, ChronoUnit.MONTHS);

        return String.valueOf(duedate);
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
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(700, 500));
        setLayout(new java.awt.BorderLayout(0, 20));

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 150));
        jPanel1.setLayout(new java.awt.GridLayout(2, 1));

        jPanel4.setPreferredSize(new java.awt.Dimension(700, 50));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Payments");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addContainerGap(568, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Invoice Number");

        jTextField1.setMaximumSize(new java.awt.Dimension(190, 28));
        jTextField1.setMinimumSize(new java.awt.Dimension(100, 23));
        jTextField1.setPreferredSize(new java.awt.Dimension(160, 28));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Date of Invoice");

        jButton1.setText("Search");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setMaximumSize(new java.awt.Dimension(100, 25));
        jButton1.setMinimumSize(new java.awt.Dimension(100, 23));
        jButton1.setPreferredSize(new java.awt.Dimension(100, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jDateChooser1.setPreferredSize(new java.awt.Dimension(160, 28));
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jButton2.setText("Clear");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setMaximumSize(new java.awt.Dimension(100, 25));
        jButton2.setMinimumSize(new java.awt.Dimension(100, 23));
        jButton2.setPreferredSize(new java.awt.Dimension(100, 25));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel1.add(jPanel3);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setMaximumSize(new java.awt.Dimension(23, 32767));
        jPanel5.setMinimumSize(new java.awt.Dimension(23, 370));
        jPanel5.setPreferredSize(new java.awt.Dimension(23, 370));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 372, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel5, java.awt.BorderLayout.LINE_START);

        jPanel6.setMaximumSize(new java.awt.Dimension(23, 32767));
        jPanel6.setMinimumSize(new java.awt.Dimension(23, 372));
        jPanel6.setPreferredSize(new java.awt.Dimension(23, 372));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 372, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel6, java.awt.BorderLayout.LINE_END);

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 23));
        jPanel7.setMinimumSize(new java.awt.Dimension(100, 23));
        jPanel7.setPreferredSize(new java.awt.Dimension(700, 23));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseReleased(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice Number", "Invoice Date", "Total Amount", "Paid Amount", "Balance Amount", "Due Date", "Payment Plan", "Customer Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        searchData();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void searchData() {

        String invoiceNumber = jTextField1.getText();
        Date selectedDate = jDateChooser1.getDate();

        if (invoiceNumber.isEmpty() && selectedDate == null) {
            loadInvoiceDetails("");
        } else if (invoiceNumber.isEmpty() && selectedDate != null) {
            System.out.println("ok");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(selectedDate);
            loadInvoiceDetails("WHERE DATE(date_time) = '" + formattedDate + "'");
        } else if (!invoiceNumber.isEmpty() && selectedDate == null) {
            loadInvoiceDetails("WHERE `invoice`.`id`='" + invoiceNumber + "'");
        } else if (!invoiceNumber.isEmpty() && selectedDate != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(selectedDate);
            loadInvoiceDetails("WHERE `invoice`.`id`='" + invoiceNumber + "' AND DATE(date_time) = '" + formattedDate + "' ");
        }

    }
    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        if (evt.getClickCount() == 2) {
            System.out.println("doubleclick");
            int selectedRow = jTable2.getSelectedRow();
            Object invoiceNumber = jTable2.getValueAt(selectedRow, 0);
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            new ShowInvoiceDetails(parentFrame,this, true, invoiceNumber).setVisible(true);
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        searchData();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        searchData();
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        searchData();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadInvoiceDetails("");
        jTextField1.setText("");
        jDateChooser1.setDate(null);
        
// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void jScrollPane2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane2MouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
