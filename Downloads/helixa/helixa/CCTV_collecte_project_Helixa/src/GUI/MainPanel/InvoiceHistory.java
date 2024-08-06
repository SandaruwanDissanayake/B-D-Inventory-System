/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.DialogBox.InvoiceItemDetails;
import GUI.MainFrame.Login;
import Models.MySQL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sandaruwan
 */
public class InvoiceHistory extends javax.swing.JPanel {

    private HashMap<String, String> map = new HashMap<>();
    private HashMap<String, String> typemap = new HashMap<>();
    public HashMap<String, ArrayList<String>> tanddisemap = new HashMap<>();

    public InvoiceHistory() {
        initComponents();
        loadOption();
        loadTable();
    }

    private void reset() {
        jTextField1.setText("");
        jDateChooser1.setDate(null);
        jComboBox1.setSelectedIndex(0);
        jTextField2.setText("");
        loadTable();
    }

    private void loadOption() {
        Vector<String> v = new Vector<>();

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `payment_option`");
            while (rs.next()) {
                v.add(rs.getString("option"));
                map.put(rs.getString("option"), rs.getString("id"));
            }
            DefaultComboBoxModel m = new DefaultComboBoxModel(v);
            jComboBox1.setModel(m);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }

    private void loadTable() {
        String id = jTextField1.getText();
        Date date = jDateChooser1.getDate();
        int option = jComboBox1.getSelectedIndex();

        String query = "SELECT * FROM `invoice`";
        boolean b = false;
        if (!id.isEmpty()) {
            query += " WHERE `id` = '" + id + "%'";
            b = true;
        }

        if (date != null) {
            if (b) {
                query += " AND `date_time` >= '" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "'";
            } else {
                query += " WHERE `date_time` >= '" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "'";
                b = true;
            }
        }

        if (option == 0) {
            if (b) {
                query += " AND `payment_option_id` = '" + map.get(String.valueOf(jComboBox1.getSelectedItem())) + "'";
            } else {
                query += " WHERE `payment_option_id` = '" + map.get(String.valueOf(jComboBox1.getSelectedItem())) + "'";
            }
        } else if (option == 1) {
            if (b) {
                query += " AND `payment_option_id` = '" + map.get(String.valueOf(jComboBox1.getSelectedItem())) + "'";
            } else {
                query += " WHERE `payment_option_id` = '" + map.get(String.valueOf(jComboBox1.getSelectedItem())) + "'";
            }
        } else if (option == 2) {
            if (b) {
                query += " AND `payment_option_id` = '" + map.get(String.valueOf(jComboBox1.getSelectedItem())) + "'";
            } else {
                query += " WHERE `payment_option_id` = '" + map.get(String.valueOf(jComboBox1.getSelectedItem())) + "'";
            }
        }
        boolean testb = true;

        query += " ORDER BY `date_time` DESC";
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        int index = 1;
        try {
            ResultSet rs = MySQL.execute(query);
            while (rs.next()) {
                testb = true;
                if (!jTextField2.getText().isEmpty()) {
                    ResultSet result = MySQL.execute("SELECT `nic` FROM `customer_details` WHERE `nic` = '" + rs.getString("customer_details_nic") + "'");
                    if (result.next()) {
                        if (!result.getString("nic").equals(jTextField2.getText())) {
                            testb = false;
//                            model.setRowCount(0);
                        }
                    } else {
                        testb = false;
                    }
//                    System.out.println("awo");
                }
                if (testb) {
                    Vector<String> v = new Vector<>();
                    v.add(String.valueOf(index));
                    typemap.put(String.valueOf(index), rs.getString("customer_type_id"));
                    ArrayList<String> al = new ArrayList<>();
                    al.add(rs.getString("service_charge"));
                    al.add(rs.getString("offers"));
                    tanddisemap.put(String.valueOf(index), al);
                    index++;
                    v.add(rs.getString("id"));
                    v.add(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("date_time"))));
                    v.add(new SimpleDateFormat("HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("date_time"))));
                    try {
                        ResultSet res = MySQL.execute("SELECT SUM(`quantity`) AS `qty` FROM `invoice_item` WHERE `invoice_id` = '" + rs.getString("id") + "'");
                        if (res.next()) {
                            v.add(res.getString("qty"));
                        }
                    } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

                    }
                    double sumamount = 0;
                    v.add(String.valueOf((rs.getDouble("total_amount") + rs.getDouble("service_charge")) - rs.getDouble("offers")));
                    try {
                        ResultSet result = MySQL.execute("SELECT SUM(`amount`) AS `samount` FROM `loan_installments` WHERE `invoice_id` = '" + rs.getString("id") + "'");
                        if (result.next()) {
                            sumamount = sumamount + result.getDouble("samount");

                        }
                    } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

                    }

                    try {
                        ResultSet subresult = MySQL.execute("SELECT SUM(`amount`) AS `camount` FROM `check` WHERE `invoice_id` = '" + rs.getString("id") + "' AND `cheque_status_id` = '3'");
                        if (subresult.next()) {
                            sumamount = sumamount - subresult.getDouble("camount");
                        }
                    } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

                    }
                    double dueAmount = ((rs.getDouble("total_amount") + rs.getDouble("service_charge")) - rs.getDouble("offers")) - rs.getDouble("paid_amount") - sumamount;
                    v.add(String.valueOf(dueAmount));
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (entry.getValue().equals(rs.getString("payment_option_id"))) {
                            v.add(entry.getKey());
                            break;
                        }
                    }
                    v.add(rs.getString("user_nic"));
                    model.addRow(v);
                }

            }

        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jTextField2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

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
            .addGap(0, 427, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(716, 150));
        jPanel2.setLayout(new java.awt.GridLayout(4, 1));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 965, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel5);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Invoice History");

        jButton2.setText("...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 724, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(22, 22, 22))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton2))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel6);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Date");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Invoice ID");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Payment Plan");

        jLabel4.setText("Mobile");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel3)
                .addGap(108, 108, 108)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jLabel4)
                .addContainerGap(400, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        jPanel2.add(jPanel7);

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

        jButton1.setText("Search");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jDateChooser1.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jDateChooser1ComponentAdded(evt);
            }
        });
        jDateChooser1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jDateChooser1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jDateChooser1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jDateChooser1FocusGained(evt);
            }
        });
        jDateChooser1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jDateChooser1ComponentShown(evt);
            }
        });
        jDateChooser1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jDateChooser1CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jDateChooser1InputMethodTextChanged(evt);
            }
        });
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });
        jDateChooser1.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                jDateChooser1VetoableChange(evt);
            }
        });

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(20, 20, 20))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 14, Short.MAX_VALUE))
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
            .addGap(0, 427, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.LINE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(716, 25));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 965, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Invoice ID", "Date", "Time", "Total Items", "Total Amount", "Due amount", "Payment Plan", "Cashier"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
        }

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        // TODO add your handling code here:


    }//GEN-LAST:event_jButton1MouseReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {

            int row = jTable1.getSelectedRow();
            String inv = String.valueOf(jTable1.getValueAt(row, 1));
            String date = String.valueOf(jTable1.getValueAt(row, 2));
            String time = String.valueOf(jTable1.getValueAt(row, 3));
            String totalitem = String.valueOf(jTable1.getValueAt(row, 4));
            String totalamount = String.valueOf(jTable1.getValueAt(row, 5));
            String dueamount = String.valueOf(jTable1.getValueAt(row, 6));
            String plan = String.valueOf(jTable1.getValueAt(row, 7));
            String tid = typemap.get(String.valueOf(jTable1.getValueAt(row, 0)));
            String rowindex = String.valueOf(jTable1.getValueAt(row, 0));
//            System.out.println("Double clicked on row " + row + " with data: " + rowData);
            new InvoiceItemDetails(this, true, inv, date, time, totalitem, totalamount, dueamount, plan, tid, rowindex, this).setVisible(true);
        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        loadTable();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        loadTable();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jDateChooser1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jDateChooser1AncestorAdded

    }//GEN-LAST:event_jDateChooser1AncestorAdded

    private void jDateChooser1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jDateChooser1ComponentShown

    }//GEN-LAST:event_jDateChooser1ComponentShown

    private void jDateChooser1ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jDateChooser1ComponentAdded

    }//GEN-LAST:event_jDateChooser1ComponentAdded

    private void jDateChooser1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jDateChooser1FocusGained

    }//GEN-LAST:event_jDateChooser1FocusGained

    private void jDateChooser1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jDateChooser1CaretPositionChanged

    }//GEN-LAST:event_jDateChooser1CaretPositionChanged

    private void jDateChooser1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jDateChooser1InputMethodTextChanged

    }//GEN-LAST:event_jDateChooser1InputMethodTextChanged

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        loadTable();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser1VetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jDateChooser1VetoableChange

    }//GEN-LAST:event_jDateChooser1VetoableChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        reset();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        if(!jTextField2.getText().matches("^\\d+$")){
            jTextField2.setText("");
        }
        loadTable();

    }//GEN-LAST:event_jTextField2KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
