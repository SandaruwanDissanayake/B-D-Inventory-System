/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.DialogBox;

import GUI.MainFrame.Login;
import GUI.MainPanel.Payments;
import Models.InstallmentBean;
import Models.MySQL;
import java.awt.Color;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Mr. SP
 */
public class ShowInvoiceDetails extends javax.swing.JDialog {

    private Payments pay;
    private Object invoiceNO;
    Payments paymentFrame;

    /**
     * Creates new form ShowInvoiceDetails
     */
    public ShowInvoiceDetails(java.awt.Frame parent, Payments py, boolean modal, Object invoiceNumber) {
        super(parent, modal);
        initComponents();
        this.paymentFrame = py;
        this.invoiceNO = invoiceNumber;
        showEditButton(false);
        loadPayementType();
        loadData();

    }

    public ShowInvoiceDetails() {

    }

    private void loadPayementType() {

        try {

            ResultSet resultset = MySQL.execute("SELECT * FROM `payment_type`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultset.next()) {

                vector.add(resultset.getString("type"));

            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }

    }

    public void showEditButton(boolean show) {
        jLabel35.setVisible(show);
    }

    private void disableFields() {
        jComboBox1.setEnabled(false);
        jTextField1.setEnabled(false);
        jButton1.setEnabled(false);
    }

    private void loadData() {
        jLabel2.setText(invoiceNO.toString());

        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `invoice` "
                    + "INNER JOIN `payment_option` ON `invoice`.payment_option_id=`payment_option`.`id`\n"
                    + "INNER JOIN `customer_type` ON `invoice`.`customer_type_id`=`customer_type`.`id`\n"
                    + "INNER JOIN `customer_details` ON `invoice`.customer_details_nic=`customer_details`.`nic` WHERE `invoice`.`id`='" + invoiceNO + "'");

            ResultSet productCountSet = MySQL.execute("SELECT * FROM `invoice_item` WHERE `invoice_id`='" + invoiceNO + "'");
            int rowCount = 0;
            while (productCountSet.next()) {
                rowCount++;

            }
            if (resultSet.next()) {

                jLabel4.setText(resultSet.getString("date_time"));

                //split the date
                String[] parts = resultSet.getString("date_time").split(" ");
                String dateString = parts[0];

                //calculate due date
                if (resultSet.getInt("payment_option_id") == 1) {
                    jLabel6.setText("Full Payment");
                    disableFields();
                } else if (resultSet.getInt("payment_option_id") == 2) {
                    jLabel6.setText(new Payments().calculateDueDate(dateString, 1));
                } else if (resultSet.getInt("payment_option_id") == 3) {
                    jLabel6.setText(new Payments().calculateDueDate(dateString, 3));
                }

                ResultSet paymentValue = MySQL.execute("SELECT * FROM `payment_value` WHERE `invoice_id`='" + invoiceNO + "'");
                if (paymentValue.next()) {
                    if (paymentValue.getInt("payment_status_id") == 1) {
                        disableFields();
                    }
                    int paidAmount = resultSet.getInt("total_amount") - paymentValue.getInt("total");
                    jLabel12.setText("Rs. " + String.valueOf(paidAmount) + ".00");
                    jLabel14.setText("Rs. " + String.valueOf(paymentValue.getInt("total")) + ".00");
                } else {
                    jLabel12.setText("Rs. " + resultSet.getString("total_amount") + ".00");
                    jLabel14.setText("Rs. 0.00");
                }

                jLabel7.setText(String.valueOf(rowCount));
                jLabel9.setText("Rs. " + String.valueOf(resultSet.getInt("total_amount")) + ".00");
                jLabel33.setText("Rs. " + String.valueOf(resultSet.getInt("paid_amount")) + ".00");

                //calculate the due amount
                jLabel14.setForeground(Color.red);
                jLabel15.setText(resultSet.getString("option"));

                //installments
                loadInstalments();

                //customer details
                jLabel20.setText(resultSet.getString("type"));
                jLabel21.setText(resultSet.getString("full_name"));
                jLabel23.setText(resultSet.getString("nic"));
                jLabel25.setText(resultSet.getString("mobile"));
                jLabel27.setText("" + resultSet.getString("address_line1") + ", ");
                jLabel34.setText(resultSet.getString("address_line2"));
            }

        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }

    }

//    @Override
//    public void dispose() {
//        this.paymentFrame.loadInvoiceDetails("");
//        super.dispose();
//
//    }
    private void loadInstalments() {

        try {

            ResultSet resultset = MySQL.execute("SELECT * FROM `loan_installments`\n"
                    + "INNER JOIN `payment_type` ON `loan_installments`.`payment_type_id`=`payment_type`.`id`\n"
                    + "WHERE `invoice_id`='" + invoiceNO.toString() + "' ORDER BY `date_time` DESC");

            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
            tableModel.setRowCount(0);

            while (resultset.next()) {

                Vector<String> vector = new Vector();
                vector.add(resultset.getString("amount"));
                vector.add(resultset.getString("type"));
                vector.add(resultset.getString("date_time"));

                tableModel.addRow(vector);
                jTable1.setModel(tableModel);

            }

//            System.out.println(dueAmount);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }

    }

    String chequenumber = "";
    Date chequeDate = null;
    String bank = "";
    String chequeNote = "";

    private int getInsertID(String query) {
        int insertId = 0;
        if (query.startsWith("INSERT")) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cctv_shop_db", "root", "******");

                Statement statement = con.createStatement();
                statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    insertId = resultSet.getInt(1);
                }

                con.close();
                statement.close();
                resultSet.close();

            } catch (Exception ex) {
                Login.log1.warning(ex.getMessage());

            }
        }

        return insertId;

    }

    private void printReport() {
        try {
            int i = 1;

            HashMap<String, Object> parameters = new HashMap<>();
            ResultSet invoiceSet = MySQL.execute("SELECT * FROM `invoice` INNER JOIN `customer_details` ON `invoice`.`customer_details_nic`=`customer_details`.`nic` INNER JOIN `payment_value` ON `invoice`.`id`=`payment_value`.`invoice_id` WHERE `invoice_id`='" + invoiceNO.toString() + "'");

            if (invoiceSet.next()) {
                int total = invoiceSet.getInt("total_amount");
                int due = invoiceSet.getInt("total");
                int paid = total - due;
                if (due == 0) {
                    parameters.put("message", "Congratulations! You've completed your installment plan - enjoy your purchase!");

                } else {
                    parameters.put("message", "Payment successful! You're one step closer to completing your installment plan.");

                }
                parameters.put("name", invoiceSet.getString("full_name"));
                parameters.put("addressLine1", invoiceSet.getString("address_line1"));
                parameters.put("addressLine2", invoiceSet.getString("address_line2"));
                parameters.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                parameters.put("invoiceNumber", invoiceSet.getString("invoice.id"));
                parameters.put("mobileNumber", invoiceSet.getString("mobile"));
                parameters.put("totalAmount", "Rs. " + String.valueOf(invoiceSet.getInt("total_amount")) + ".00");
                parameters.put("paidAmount", "Rs. " + String.valueOf(paid) + ".00");
                parameters.put("dueAmount", "Rs. " + String.valueOf(invoiceSet.getInt("total")) + ".00");
            }

            Vector<InstallmentBean> fields = new Vector<>();

            InstallmentBean initialPayment = new InstallmentBean();
            initialPayment.setInstallmentAmount(invoiceSet.getString("paid_amount"));
            initialPayment.setInstallmentDate(new SimpleDateFormat("yyyy-MM-dd").format(invoiceSet.getDate("date_time")));
            initialPayment.setInstallmentName("Initial Payment");
            fields.add(initialPayment);

            ResultSet instalmentSet = MySQL.execute("SELECT * FROM `loan_installments` WHERE `invoice_id`='" + invoiceNO.toString() + "' ORDER BY `date_time` ASC");
            while (instalmentSet.next()) {
                InstallmentBean bean = new InstallmentBean();
                bean.setInstallmentAmount("Rs. " + String.valueOf(instalmentSet.getInt("amount")) + ".00");
                bean.setInstallmentDate(new SimpleDateFormat("yyyy-MM-dd").format(instalmentSet.getDate("date_time")));
                bean.setInstallmentName(i + " Installment");
                fields.add(bean);
                i++;
            }

            JasperCompileManager.compileReportToFile("src/Report/installmentPayment.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(fields);
            JasperPrint report = JasperFillManager.fillReport("src/Report/installmentPayment.jasper", parameters, dataSource);
            this.paymentFrame.loadInvoiceDetails("");
            this.dispose();

            if (jCheckBox1.isSelected()) {
                JasperViewer.viewReport(report, false);
            } else {
                JasperPrintManager.printReport(report, false);
            }

        } catch (Exception ex) {
            Login.log1.warning(ex.getMessage());

        }
    }

    public void setPaymentType() {
        jComboBox1.setSelectedIndex(0);
    }

    public void setChequeNumber(String cn) {
        chequenumber = cn;
    }

    public void setchequeDate(Date cd) {
        chequeDate = cd;
    }

    public void setbank(String b) {
        bank = b;
    }

    public void setChequeNote(String n) {
        chequeNote = n;
    }

    public String getChequeNumber() {
        return chequenumber;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public String getBank() {
        return bank;
    }

    public String getChequeNote() {
        return chequeNote;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Invoice Number :");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("0");

        jLabel3.setText("Date & Time:");

        jLabel4.setText("2024-11-11 13:42:09");

        jLabel5.setText("Due Date");

        jLabel6.setText("2024-11-11");

        jLabel7.setText("14");

        jLabel8.setText("Total items");

        jLabel9.setText("Rs 14,500.00");

        jLabel10.setText("Total Amount");

        jLabel11.setText("Paid Amount");

        jLabel12.setText("Rs 2,500.00");

        jLabel13.setText("Due Amount");

        jLabel14.setText("Rs 12,500.00");

        jLabel15.setText("One Month Plan");

        jLabel16.setText("Payment Plan");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Installments");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Amount", "Payment Method", "Date Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel18.setText("Customer Type :");

        jLabel19.setText("Full Name :");

        jLabel20.setText("Special");

        jLabel21.setText("Hasun Bandara");

        jLabel22.setText("NIC :");

        jLabel23.setText("200403511700");

        jLabel24.setText("Mobile :");

        jLabel25.setText("0767974013");

        jLabel26.setText("Address :");

        jLabel27.setText("No 11 pahala imbulgoda");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("Customer Details");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("Payment Details");

        jLabel30.setText("Payemnt type :");

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

        jLabel31.setText("Amount :");

        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel32.setText("Initial Amount");

        jLabel33.setText("Rs 14,500.00");

        jLabel34.setText("No 11 pahala imbulgoda");

        jLabel35.setText("Edit");
        jLabel35.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel35MouseReleased(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("View Report");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13)
                            .addComponent(jLabel16)
                            .addComponent(jLabel32))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel26))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel28)))
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(jLabel29)
                    .addComponent(jLabel17)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton1))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel30)
                                        .addComponent(jLabel31))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField1)
                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel35))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel28))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(jLabel33))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27))
                        .addGap(0, 0, 0)
                        .addComponent(jLabel34)))
                .addGap(25, 25, 25)
                .addComponent(jLabel17)
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel29)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jCheckBox1))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        if (jComboBox1.getSelectedIndex() == 2) {
            CollectchequeDetails ccd = new CollectchequeDetails(this);

            ccd.setVisible(true);
        } else {
            showEditButton(false);

            this.setChequeNumber("");
            this.setchequeDate(null);
            this.setbank("");
            this.setChequeNote("");
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jComboBox1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please Select the Payment Type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter installment Amount", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                int amount = Integer.parseInt(jTextField1.getText());

                try {
                    ResultSet paymentValue = MySQL.execute("SELECT * FROM `payment_value` WHERE `invoice_id`='" + invoiceNO + "'");
                    if (paymentValue.next()) {

                        if (paymentValue.getInt("total") >= amount) {
                            int confirmation = JOptionPane.showConfirmDialog(this, "Do you want to continue with this payment?", "Confirmation", JOptionPane.YES_NO_OPTION);

                            if (confirmation == JOptionPane.YES_OPTION) {
                                int status = 2;
                                int dueAmount = paymentValue.getInt("total") - amount;
                                if (dueAmount == 0) {
                                    status = 1;
                                }

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formatteddate = dateFormat.format(new Date());

                                if (jComboBox1.getSelectedIndex() == 1) {

                                    MySQL.execute("INSERT INTO `loan_installments`(`amount`,`date_time`,`invoice_id`,`payment_type_id`) "
                                            + "VALUES('" + jTextField1.getText() + "','" + formatteddate + "','" + invoiceNO.toString() + "','" + jComboBox1.getSelectedIndex() + "')");
                                } else if (jComboBox1.getSelectedIndex() == 2) {
                                    int loanRowId = getInsertID("INSERT INTO `loan_installments`(`amount`,`date_time`,`invoice_id`, `payment_type_id`) VALUES('" + jTextField1.getText() + "','" + formatteddate + "','" + invoiceNO.toString() + "','" + jComboBox1.getSelectedIndex() + "')");
                                    int chequeRowId = getInsertID("INSERT INTO `check`(`check_no`,`date_time`,`amount`,`place`,`bank_name`,`invoice_id`,`cheque_status_id`) VALUES('" + chequenumber + "', '" + dateFormat.format(chequeDate) + "', '" + jTextField1.getText() + "','null','" + bank + "', '" + invoiceNO + "', '2')");

                                    if (loanRowId == 0 && chequeRowId != 0) {

                                        MySQL.execute("DELETE FROM `check` WHERE `id`=''" + chequeRowId + "'");
                                    } else if (loanRowId != 0 && chequeRowId == 0) {

                                        MySQL.execute("DELETE FROM `loan_installments` WHERE `id`=''" + loanRowId + "'");
                                    } else {

                                        MySQL.execute("INSERT INTO `loan_installments_has_check`(`check_id`,`loan_installments_id`) VALUES('" + chequeRowId + "','" + loanRowId + "')");

                                    }

                                }

                                MySQL.execute("UPDATE `payment_value` SET `total`='" + String.valueOf(dueAmount) + "' ,`date`='" + formatteddate + "', `payment_status_id`='" + status + "' WHERE `invoice_id`='" + invoiceNO + "'");

                                loadInstalments();
                                loadData();
                                printReport();
                                //                                JOptionPane.showMessageDialog(this, "Installment Successfully Added", "", JOptionPane.DEFAULT_OPTION);
                                jComboBox1.setSelectedIndex(0);
                                jTextField1.setText("");

                            }

                        } else {
                            JOptionPane.showMessageDialog(this, "You trying to overpay. Please select valid amount.", "", JOptionPane.WARNING_MESSAGE);

                        }

                    }

                } catch (Exception ex) {
                    Login.log1.warning(ex.getMessage());

                }

            } catch (NumberFormatException ex) {
                Login.log1.warning(ex.getMessage());

                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter valid amount.", "", JOptionPane.WARNING_MESSAGE);

            }

            try {

            } catch (Exception ex) {
                Login.log1.warning(ex.getMessage());

            }

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel35MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel35MouseReleased
//                this.ccd.setVisible(true);
        CollectchequeDetails ccd = new CollectchequeDetails(this);

        ccd.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel35MouseReleased

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
            java.util.logging.Logger.getLogger(ShowInvoiceDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShowInvoiceDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShowInvoiceDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShowInvoiceDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ShowInvoiceDetails dialog = new ShowInvoiceDetails(new javax.swing.JFrame(), null, true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
