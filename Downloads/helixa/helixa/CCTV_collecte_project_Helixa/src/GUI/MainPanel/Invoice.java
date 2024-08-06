/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.DialogBox.AddChequeAtInvoice;
import GUI.DialogBox.AddProductToInvoice;
import GUI.DialogBox.PaymentPlan;
import GUI.DialogBox.UpdateInvoiceQty;
import GUI.MainFrame.Login;
import GUI.MainFrame.MainFrame;
import Models.InvoiceBean;
import Models.InvoiceLoadBean;
import Models.MySQL;
import Models.MyThread;
import Models.UserBean;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Sandaruwan
 */
public class Invoice extends javax.swing.JPanel {

    public HashMap<Integer, InvoiceBean> map = new HashMap<>();
    private HashMap<Integer, InvoiceBean> backupmap = new HashMap<>();
    private HashMap<String, String> paymentoptionmap = new HashMap<>();
    private HashMap<String, String> paymenttype = new HashMap<>();
    public HashMap<String, String> chequemap = new HashMap<>();
    public HashMap<String, String> customermap = new HashMap<>();
    public HashMap<String, String> customertype = new HashMap<>();
    public Vector<String> checkSerial = new Vector<>();
    private UserBean userbean;
    private HashMap<String, String> wpload = new HashMap<>();
    private static int tinv;

    public Invoice(UserBean userbean) {

        initComponents();
//        this.mf = mf; 
//        jLabel2.setText(String.valueOf(mf.todayINV));
        jLabel2.setText(String.valueOf(tinv));
        loadPaymentPlan();
        paymenttype();
        loadCustomerType();
        jComboBox3.setEnabled(false);
        jComboBox2.setEnabled(false);
        jButton4.setEnabled(false);
        this.userbean = userbean;
        loadWarrantyPeriod();
    }

    private void loadWarrantyPeriod() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `warranty_period`");
            while (rs.next()) {
                wpload.put(rs.getString("period"), rs.getString("id"));
            }
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }

    public JTable getjTable1() {
        return jTable1;
    }

    public JComboBox getjComboBox3() {
        return jComboBox3;
    }

    private void loadCustomerType() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `customer_type`");
            Vector<String> v = new Vector<>();
            while (rs.next()) {
                v.add(rs.getString("type"));
                customertype.put(rs.getString("type"), rs.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            jComboBox1.setModel(model);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }

    private void loadPaymentPlan() {

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `payment_option`");
            Vector<String> v = new Vector<>();

            while (rs.next()) {
                v.add(rs.getString("option"));
                paymentoptionmap.put(rs.getString("option"), rs.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            jComboBox2.setModel(model);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }

    private void paymenttype() {

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `payment_type`");
            Vector<String> v = new Vector<>();

            while (rs.next()) {
                v.add(rs.getString("type"));
                paymenttype.put(rs.getString("type"), rs.getString("id"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            jComboBox3.setModel(model);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }

    public void loadinvtable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        for (Map.Entry<Integer, InvoiceBean> entry : map.entrySet()) {
            Vector<String> v = new Vector<>();
            v.add(String.valueOf(entry.getKey()));
            v.add(entry.getValue().getTitle());
            v.add(entry.getValue().getSn());
            v.add(entry.getValue().getBc());
            v.add(entry.getValue().getModel());
            v.add(entry.getValue().getBrand());
            v.add(entry.getValue().getPrice());
            v.add(entry.getValue().getQty());
            v.add(entry.getValue().getWarrantyprc());
            v.add(entry.getValue().getWaperiod());
            model.addRow(v);
        }

        logic();
        if (jTable1.getRowCount() == 0) {
            jComboBox3.setEnabled(false);
            jComboBox2.setEnabled(false);
            jButton4.setEnabled(false);
        } else {
            jComboBox3.setEnabled(true);
            jComboBox2.setEnabled(true);
            jButton4.setEnabled(true);
        }

    }

    public void logic() {
        double qty = 0;
        double total = 0;
        double wt = 0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            qty = qty + Double.parseDouble(String.valueOf(jTable1.getValueAt(i, 7)));
            if (jTable1.getValueAt(i, 8) != null) {
                wt = (Double.parseDouble(String.valueOf(jTable1.getValueAt(i, 8))) * Double.parseDouble(String.valueOf(jTable1.getValueAt(i, 7))));
            } else {
                wt = 0;
            }
//            
            total = total + wt + (Double.parseDouble(String.valueOf(jTable1.getValueAt(i, 7))) * Double.parseDouble(String.valueOf(jTable1.getValueAt(i, 6))));
        }
        jLabel13.setText(String.valueOf(qty));
        jLabel12.setText(String.valueOf(total));
        getBalance();
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
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(704, 150));
        jPanel1.setLayout(new java.awt.GridLayout(4, 1));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 938, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel5);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Invoice");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("0");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Today Invoices");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 649, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Customer Type");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addContainerGap(823, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 17, Short.MAX_VALUE)
                .addComponent(jLabel4))
        );

        jPanel1.add(jPanel7);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "shop", "special", "setup" }));

        jButton2.setText("Add Product to Invoice");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 551, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(21, 21, 21))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(20, 181));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel3.setPreferredSize(new java.awt.Dimension(20, 181));

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

        add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel4.setPreferredSize(new java.awt.Dimension(704, 230));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Payment Option");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Payment Type");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Total Quantity");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Total");

        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField3FocusGained(evt);
            }
        });
        jFormattedTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField3ActionPerformed(evt);
            }
        });
        jFormattedTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField3KeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Paid amount");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Grand Total");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("0");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("0");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("0");

        jButton1.setText("Print Invoice");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Service charge");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField1FocusGained(evt);
            }
        });
        jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Discount");

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField2FocusGained(evt);
            }
        });
        jFormattedTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField2KeyReleased(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Check before print");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton3.setText("Reset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("clear");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(21, 21, 21))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jFormattedTextField1)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(23, 23, 23))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel13)
                        .addComponent(jButton4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton3))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Product Title", "Serial Number", "Barcode", "Product Model", "Brand", "Price", "Quantity", "Warranty", "Duration"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setPreferredSize(new java.awt.Dimension(305, 100));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(90);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(9).setResizable(false);
        }

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jComboBox1.setEnabled(false);
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new AddProductToInvoice(parentFrame, true, String.valueOf(jComboBox1.getSelectedItem()), this).setVisible(true);

    }//GEN-LAST:event_jButton2ActionPerformed

    public JFormattedTextField getjFormattedTextField1() {
        return jFormattedTextField1;
    }

    public JFormattedTextField getjFormattedTextField2() {
        return jFormattedTextField2;
    }

    public JLabel getjLabel10() {
        return jLabel10;
    }

    public JLabel getjLabel11() {
        return jLabel11;
    }

    public void getBalance() {
        double paidam = 0;
        double service = 0;
        double discount = 0;
        if (!jFormattedTextField3.getText().isEmpty()) {
            paidam = Double.parseDouble(jFormattedTextField3.getText());
        }
        if (!jFormattedTextField1.getText().isEmpty()) {
            service = Double.parseDouble(jFormattedTextField1.getText());
        }
        if (!jFormattedTextField2.getText().isEmpty()) {
            discount = Double.parseDouble(jFormattedTextField2.getText());
        }

        double total = Double.parseDouble(jLabel12.getText());
        double balance = paidam - (total - discount + service);
//         double balance = paidam - total;
        if (jLabel10.getText().equals("Balance")) {
            jLabel11.setText(String.valueOf(balance));
        } else {
            jLabel11.setText(String.valueOf(total - discount + service));
        }

    }

    private void jFormattedTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField3KeyReleased
        jLabel10.setText("Balance");
        if (!jFormattedTextField3.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            jFormattedTextField3.setText("0");
        }
        getBalance();

    }//GEN-LAST:event_jFormattedTextField3KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2 && jTable1.getSelectedRow() != -1 && evt.getButton() == 1) {
            int row = jTable1.getSelectedRow();
            int i = JOptionPane.showConfirmDialog(this, "Do you want to remove this product?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (i == JOptionPane.YES_OPTION) {
                map.remove(Integer.parseInt(String.valueOf(jTable1.getValueAt(row, 0))));

                ArrayList<InvoiceBean> al = new ArrayList<>();
                for (InvoiceBean invb : map.values()) {
                    al.add(invb);
                }
                map.clear();
                for (int in = 0; in < al.size(); in++) {
                    map.put(in + 1, al.get(in));
                }
                if (!String.valueOf(jTable1.getValueAt(row, 2)).isEmpty()) {
                    checkSerial.remove(jTable1.getValueAt(row, 2));
                }
                loadinvtable();
                if (jTable1.getRowCount() == 0) {
                    resetInv();
                }
            }
        } else if (evt.getButton() == 3) {
//            System.out.println("3");
            int row = jTable1.getSelectedRow();
            if (row != -1) {
//                System.out.println(jTable1.getValueAt(row, 2));
                if (String.valueOf(jTable1.getValueAt(row, 2)).isEmpty()) {
                    int key = Integer.parseInt(String.valueOf(jTable1.getValueAt(row, 0)));
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    UpdateInvoiceQty uiq = new UpdateInvoiceQty(parentFrame, true, key, String.valueOf(jTable1.getValueAt(row, 1)), this);
                    uiq.setVisible(true);
                }

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jFormattedTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField3FocusGained
        jFormattedTextField3.setText("");
    }//GEN-LAST:event_jFormattedTextField3FocusGained

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            jFormattedTextField2.grabFocus();
        } else {
            double t = Double.parseDouble(jLabel12.getText());
            if (!jFormattedTextField1.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                jFormattedTextField1.setText("0");
            }

            getBalance();
        }

    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    private void jFormattedTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField2KeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            jFormattedTextField3.grabFocus();
        } else {
            if (!jFormattedTextField2.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                jFormattedTextField2.setText("0");
            }

            getBalance();
        }

    }//GEN-LAST:event_jFormattedTextField2KeyReleased

    private void jFormattedTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField1FocusGained
        jFormattedTextField1.setText("");
    }//GEN-LAST:event_jFormattedTextField1FocusGained

    private void jFormattedTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField2FocusGained
        jFormattedTextField2.setText("");
    }//GEN-LAST:event_jFormattedTextField2FocusGained

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed

        if (jTable1.getRowCount() != 0) {
            if (jComboBox2.getSelectedIndex() == 1 || jComboBox2.getSelectedIndex() == 2) {
//           System.out.println("pk");
                String para = "1Month (week 4)";
                if (jComboBox2.getSelectedIndex() == 2) {
                    para = "3Month (week 18)";
                }
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                PaymentPlan p = new PaymentPlan(parentFrame, true, para, this);
                p.setVisible(true);
            }
        }

    }//GEN-LAST:event_jComboBox2ActionPerformed

    public JFormattedTextField getjFormattedTextField3() {
        return jFormattedTextField3;
    }

    public JComboBox getjComboBox2() {
        return jComboBox2;
    }

    public String getFullAmount() {
        String total = jLabel12.getText();
        String service = jFormattedTextField1.getText();
        String discount = jFormattedTextField2.getText();
        if (service.isEmpty()) {
            service = "0.00";
        }
        if (discount.isEmpty()) {
            discount = "0.00";
        }
        String fam = String.valueOf((Double.parseDouble(total) + Double.parseDouble(service)) - Double.parseDouble(discount));
        return fam;
    }
    private int confirmMessagecheck = 0;
    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        if (jTable1.getRowCount() != 0 && confirmMessagecheck == 0) {
            int i = JOptionPane.showConfirmDialog(this, "Do you confirm payment option ?", "confirm", JOptionPane.YES_NO_OPTION);
            if (i == JOptionPane.YES_OPTION) {

                if (jComboBox3.getSelectedItem().equals("Cheque")) {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    AddChequeAtInvoice add = new AddChequeAtInvoice(parentFrame, true, this);
                    add.setVisible(true);
//                    jComboBox2.setEnabled(false);
                }

            } else if (i == JOptionPane.NO_OPTION) {
                clearSelections();
                jComboBox2.grabFocus();
            }
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void resetInv() {
        jComboBox1.setSelectedIndex(0);
        DefaultTableModel mod = (DefaultTableModel) jTable1.getModel();
        mod.setRowCount(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jComboBox3.setEnabled(false);
        jComboBox2.setEnabled(false);
        jButton4.setEnabled(false);
        jLabel13.setText("0");
        jFormattedTextField1.setValue(0.00);
        jFormattedTextField1.setEnabled(true);
        jFormattedTextField2.setValue(0.00);
        jFormattedTextField2.setEnabled(true);
        jLabel12.setText("0");
        jFormattedTextField3.setValue(0.00);
        jLabel11.setText("0");
        jCheckBox1.setSelected(true);
        jComboBox1.setEnabled(true);
        jFormattedTextField3.setEnabled(true);
//        jComboBox2.setEnabled(true);
        jLabel10.setText("Grand Total");
        map.clear();
        chequemap.clear();
        customermap.clear();
        checkSerial.clear();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        boolean printInvStatus = true;
        String total = jLabel12.getText();
        String service = jFormattedTextField1.getText();
        String discount = jFormattedTextField2.getText();
        String paid = jFormattedTextField3.getText();

        if (!total.equals("0")) {

            if (service.isEmpty()) {
                service = "0.00";
            }
            if (discount.isEmpty()) {
                discount = "0.00";
            }
            if (paid.isEmpty()) {
                paid = "0.00";
            }
            if (Double.parseDouble(paid) > (Double.parseDouble(total) + Double.parseDouble(service) - Double.parseDouble(discount))) {
                paid = String.valueOf((Double.parseDouble(total) + Double.parseDouble(service) - Double.parseDouble(discount)));
            }
            double grandTotal = (Double.parseDouble(total) + Double.parseDouble(service)) - Double.parseDouble(discount);
            if (jComboBox2.getSelectedIndex() == 0 && Double.parseDouble(paid) != grandTotal) {
                JOptionPane.showMessageDialog(this, "Please enter paid amount correctly !", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (jComboBox2.getSelectedIndex() != 0 && Double.parseDouble(paid) == grandTotal) {
                JOptionPane.showMessageDialog(this, "Paid amount is not match with payment option !", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                boolean b = false;

                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    if (!String.valueOf(jTable1.getValueAt(i, 2)).isEmpty()) {
                        if (jComboBox2.getSelectedIndex() == 0) {
                            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                            PaymentPlan p = new PaymentPlan(parentFrame, true, "Customer Details", this);
                            p.setVisible(true);
                            b = true;
                            break;
                        }
                    }
                }
                if (jComboBox2.getSelectedIndex() != 0) {
                    b = true;
                }

                if (b) {

                    if (customermap.get("status").equals("0")) {
                        try {
                            MySQL.execute("INSERT INTO `customer_details` (`nic`,`full_name`,`mobile`,`address_line1`,`address_line2`) "
                                    + "VALUES ('" + customermap.get("nic") + "','" + customermap.get("name") + "','" + customermap.get("mobile") + "','" + customermap.get("address1") + "','" + customermap.get("address2") + "')");
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }

                boolean insertCheque = false;
                if (!chequemap.isEmpty()) {
                    insertCheque = true;
                    if (paid.equals("0.00")) {
                        paid = chequemap.get("ammount");
                    }
                }
                String queryForInv = "INSERT INTO `invoice` (`date_time`,`paid_amount`,`total_amount`,`offers`,`service_charge`,`customer_type_id`,`payment_option_id`,`user_nic`,`payment_type_id`) " //`customer_details_nic`,
                        + "VALUES ('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "','" + paid + "','" + total + "','" + discount + "','" + service + "','" + customertype.get(jComboBox1.getSelectedItem()) + "','" + paymentoptionmap.get(jComboBox2.getSelectedItem()) + "','" + userbean.getNic() + "','" + paymenttype.get(jComboBox3.getSelectedItem()) + "')"; //'"+customDetails+"',

                if (b) {
                    queryForInv = queryForInv.replace("`payment_option_id`,`user_nic`", "`payment_option_id`,`customer_details_nic`,`user_nic`");
                    queryForInv = queryForInv.replace("'" + paymentoptionmap.get(jComboBox2.getSelectedItem()) + "','" + userbean.getNic() + "'", "'" + paymentoptionmap.get(jComboBox2.getSelectedItem()) + "','" + customermap.get("nic") + "','" + userbean.getNic() + "'");
                }

                String lastInsertedInv = "";
                try {

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cctv_shop_db", "root", "******");
                    Statement statement = connection2.createStatement();
                    statement.executeUpdate(queryForInv, Statement.RETURN_GENERATED_KEYS);

                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        lastInsertedInv = rs.getString(1);
                    }
                    connection2.close();

//                MySQL.execute(queryForInv);  
                } catch (Exception e) {
                    Login.log1.warning(e.getMessage());
                    printInvStatus = false;

                }
                Vector<InvoiceLoadBean> vector = new Vector<>();
                HashMap<String, InvoiceLoadBean> helpinvmap = new LinkedHashMap<>();
                int beanindex = 1;
                for (InvoiceBean inb : map.values()) {
                    try {
                        ResultSet rs = MySQL.execute("SELECT * FROM `invoice_item` WHERE `invoice_id` = '" + lastInsertedInv + "' AND `stock_id` = '" + inb.getStockId() + "'");
                        if (rs.next()) {
                            try {
                                MySQL.execute("UPDATE `invoice_item` SET `quantity` = `quantity`+'" + inb.getQty() + "' WHERE `invoice_id` = '" + lastInsertedInv + "' AND `stock_id` = '" + inb.getStockId() + "'");
                            } catch (Exception e) {
                                Login.log1.warning(e.getMessage());
                            }
                            int remainqty = Integer.parseInt(helpinvmap.get(inb.getStockId()).getZ());
                            int newqty = remainqty + 1;
                            helpinvmap.get(inb.getStockId()).setZ(String.valueOf(newqty));
                            helpinvmap.get(inb.getStockId()).setT(String.valueOf((Double.parseDouble(inb.getPrice())+Double.parseDouble(inb.getWarrantyprc()))*newqty));
                        } else {
                            String periodid = "1";
                            if (wpload.containsKey(inb.getWaperiod())) {
                                periodid = wpload.get(inb.getWaperiod());
                            }
                            try {
                                MySQL.execute("INSERT INTO `invoice_item` (`quantity`,`invoice_id`,`stock_id`,`return_status_id`,`warranty_period_id`) VALUES ('" + inb.getQty() + "','" + lastInsertedInv + "','" + inb.getStockId() + "','1','" + periodid + "')");  //invoice_item_status_id
                            } catch (Exception e) {
                                Login.log1.warning(e.getMessage());
                            }
                            String warrantydueration = "";
                            String warrantyprice = "";
                            if (inb.getWaperiod() != null) {
                                warrantydueration = "(" + inb.getWaperiod() + " Warranty)";
                                warrantyprice = " +(" + inb.getWarrantyprc() + ")";
                            }
                            String newwarrantyprice = "0.0";
                            if (!warrantyprice.isEmpty()) {
                                newwarrantyprice = inb.getWarrantyprc();
                            }
                            InvoiceLoadBean loadbean = new InvoiceLoadBean();
                            loadbean.setX(String.valueOf(beanindex));
                            loadbean.setY(inb.getTitle() + "-" + inb.getBrand() + " - " + inb.getModel() + " " + warrantydueration);
                            loadbean.setA(inb.getPrice() + " " + warrantyprice);
                            loadbean.setZ(inb.getQty());
                            loadbean.setT(String.valueOf((Double.parseDouble(inb.getPrice())+Double.parseDouble(newwarrantyprice))*Integer.parseInt(inb.getQty())));
                            helpinvmap.put(inb.getStockId(), loadbean);
                            beanindex++;

                        }

                    } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

                    }

                    try {
                        MySQL.execute("UPDATE `stock` SET `qty` = `qty`-'" + inb.getQty() + "' WHERE `id` = '" + inb.getStockId() + "'");
                    } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

                    }
                    if (!inb.getSn().isEmpty()) {
                        try {
                            ResultSet result = MySQL.execute("SELECT `id` FROM `product_sn` WHERE `serial_number` = '" + inb.getSn() + "' AND `product_id` = '" + inb.getBc() + "'");
                            if (result.next()) {
                                try {
                                    MySQL.execute("INSERT INTO `invoice_has_product_sn` (`invoice_id`,`product_sn_id`) VALUES ('" + lastInsertedInv + "','" + result.getString("id") + "')");
                                } catch (Exception e) {
                                    Login.log1.warning(e.getMessage());

                                }

                                try {
                                    MySQL.execute("UPDATE `product_sn` SET `product_sn_status_id` = '2' WHERE `id` = '" + result.getString("id") + "'");
                                } catch (Exception e) {
                                    Login.log1.warning(e.getMessage());

                                }

                            }
                        } catch (Exception e) {
                            Login.log1.warning(e.getMessage());

                        }
                    }
                }
                String duePayment = String.valueOf((Double.parseDouble(total) + Double.parseDouble(service)) - Double.parseDouble(discount) - Double.parseDouble(paid));
                if (insertCheque || b) {
                    MyThread mt = new MyThread(lastInsertedInv, chequemap, String.valueOf(jComboBox2.getSelectedItem()), duePayment);
                    Thread t2 = new Thread(mt);
                    t2.start();

                }

                for (InvoiceLoadBean value : helpinvmap.values()) {
                    vector.add(value);
                }

//            for (InvoiceLoadBean ii : vector) {
//                System.out.println(ii.getIndex());
//                System.out.println(ii.getProduct());
//                System.out.println(ii.getQty());
//                System.out.println(ii.getPrice());
//            }
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("Parameter1", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                parameters.put("Parameter2", lastInsertedInv);
                parameters.put("Parameter3", total);
                parameters.put("Parameter4", service);
                parameters.put("Parameter5", discount);
                parameters.put("Parameter6", String.valueOf((Double.parseDouble(total) + Double.parseDouble(service)) - Double.parseDouble(discount)));
                parameters.put("Parameter7", paid);
                parameters.put("Parameter8", duePayment);

                try {
                    JasperCompileManager.compileReportToFile("src/Report/CCTVshop.jrxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (printInvStatus) {
                    try {
                        JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(vector);
                        JasperPrint report = JasperFillManager.fillReport("src/Report/CCTVshop.jasper", parameters, datasource);

                        if (jCheckBox1.isSelected()) {
                            JasperViewer.viewReport(report, false);
                        } else {
                            JasperPrintManager.printReport(report, false);
                        }

                    } catch (Exception e) {
                        Login.log1.warning(e.getMessage());

                    }

                    resetInv();
//                mf.todayINV++;
                    tinv++;
//                jLabel2.setText(String.valueOf(mf.todayINV));
                    jLabel2.setText(String.valueOf(tinv));
                } else {
                    JOptionPane.showMessageDialog(this, "Somthing went wrong.", "Warning", JOptionPane.WARNING_MESSAGE);

                }

            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        resetInv();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void clearSelections() {
        confirmMessagecheck = 1;
        jComboBox2.setEnabled(true);
        jComboBox3.setEnabled(true);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        if (!chequemap.isEmpty()) {
            jFormattedTextField1.setEnabled(true);
            jFormattedTextField2.setEnabled(true);
            jFormattedTextField3.setEnabled(true);
            jFormattedTextField3.setValue(0.00);
            logic();
        }
        chequemap.clear();
        customermap.clear();
        confirmMessagecheck = 0;

        JOptionPane.showMessageDialog(this, "You need to select Payment Option and Payment Type again !", "Warning", JOptionPane.INFORMATION_MESSAGE);
    }
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        clearSelections();

    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}
