/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.DialogBox;

import GUI.MainFrame.Login;
import GUI.MainFrame.MainFrame;
import static GUI.MainFrame.MainFrame.jPanel5;
import GUI.MainPanel.ReturnManagmentWelcome;
import Models.IssueProductsForReturn;
import Models.MySQL;
import Models.ReIssueProductBean;
import Models.ReturnProductBean;
import Models.ReturnProductReportBean;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author HP
 */
public class ChangeProducts extends javax.swing.JDialog {

    /**
     * Creates new form ChangeProducts
     */
    private double totalValue;
    private String invoiceId;
    private String formattedDate;
    private ReturnProductsConfirmation returnProductsConfirmation;
    private int customerTypeId;
    private int paymentPlanId;
    HashMap<String, ReIssueProductBean> issuedProducts = new HashMap<>();
    HashMap<String, ReturnProductBean> returnedProducts = new HashMap<>();
    private MainFrame mainFram;

    public ChangeProducts(Frame parent, boolean modal, ReturnProductsConfirmation returnProductsConfirmation, int customerTypeId, HashMap<String, ReturnProductBean> returnedProducts, MainFrame mf) {
        super(parent, modal);
        initComponents();
        this.returnProductsConfirmation = returnProductsConfirmation;
//        ((AbstractDocument) jTextField4.getDocument()).setDocumentFilter(new DoubleDocumentFilter());
        this.customerTypeId = customerTypeId;

        // Add a KeyListener to update the difference when the value changes
        this.returnedProducts = returnedProducts;
        jButton1.setVisible(false);

//        returnProductsConfirmation.viewP();
        this.mainFram = mf;

    }

    @Override
    public void dispose() {
        returnProductsConfirmation.arrangeReturnProduct();

        super.dispose();

    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public void setTotalValue(double totalValue, String invoiceId, int paymentPlanId) {
        this.totalValue = totalValue;
        this.invoiceId = invoiceId;
        jLabel13.setText(String.valueOf("You Have Left Rs. " + totalValue));
        this.paymentPlanId = paymentPlanId;

    }

    private void updateTotalValues() {
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        int total_quantity = 0;
        double total_value = 0.0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int quantity = Integer.parseInt(tableModel.getValueAt(i, 7).toString());
            double itemPrice = Double.parseDouble(tableModel.getValueAt(i, 6).toString());
            total_quantity += quantity;
            total_value += quantity * itemPrice;
        }

        jLabel6.setText(String.valueOf(total_quantity));
        jLabel8.setText(String.valueOf(total_value));
    }

//    private void updateDifference() {
//        try {
//            double amount = Double.parseDouble(jTextField4.getText()); // Get entered amount
//            double total_value = Double.parseDouble(jLabel8.getText());
//            double value = total_value - totalValue;
//            double difference = amount - value;
//
//            jLabel13.setText(String.format("%.2f", difference)); // Update difference label
//
//        } catch (NumberFormatException e) {
//            jLabel13.setText("0.0"); // Reset difference label on error
//        }
//    }
//    private double parseDouble(String value) throws ParseException {
//        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
//        Number number = format.parse(value);
//        return number.doubleValue();
//    }
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
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(766, 65));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Issue Another Product");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addContainerGap(417, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(766, 70));

        jLabel2.setText("Barcode");

        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTextField1MouseReleased(evt);
            }
        });
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

        jLabel3.setText("Serial Number");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel4.setText("Product Model");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(288, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(25, 25, 25))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Product Title", "Product Model", "Barcode", "Serial Number", "Brand", "Item Price", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable1FocusLost(evt);
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setText("Total Quantity :");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("0");

        jLabel7.setText("Total Amount :");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("0.00");

        jButton2.setText("Print Receipt");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setText("Paid Amount :");

        jPanel5.setBackground(new java.awt.Color(7, 45, 132));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("0.00");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Check before Print");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jFormattedTextField1CaretUpdate(evt);
            }
        });

        jLabel9.setText("Balance :");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("0.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 433, Short.MAX_VALUE)
                                .addComponent(jCheckBox1))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(44, 44, 44)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(jFormattedTextField1))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(24, 24, 24))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jCheckBox1))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 821, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void printReport(String total, String openingBalnce, String grandTotal, String paidAmount, String balance) throws Exception {
        Vector<ReturnProductReportBean> fields = new Vector<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("invoiceNumber", this.invoiceId);
        params.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        params.put("totalAmount", total + ".00");
        params.put("openingBalance", openingBalnce + ".00");
        params.put("grandTotal", grandTotal + ".00");
        params.put("paidAmount", paidAmount + ".00");
        params.put("balance", balance + ".00");

        ResultSet invoice = MySQL.execute("SELECT customer_details_nic FROM `invoice` WHERE `invoice`.`id`='" + this.invoiceId + "'");
        if (invoice.next()) {
            if (invoice.getString("customer_details_nic") != null) {
                ResultSet customerDetails = MySQL.execute("SELECT * FROM `customer_details` WHERE `nic`='" + invoice.getString("customer_details_nic") + "'");
                if (customerDetails.next()) {
                    params.put("name", customerDetails.getString("full_name"));
                    params.put("mobileNumber", customerDetails.getString("mobile"));
                    params.put("addressLine1", customerDetails.getString("address_line1"));
                    params.put("addressLine2", customerDetails.getString("address_line2"));
                } else {
                    params.put("name", "");
                    params.put("mobileNumber", "");
                    params.put("addressLine1", "");
                    params.put("addressLine2", "");
                }
            } else {
                params.put("name", "");
                params.put("mobileNumber", "");
                params.put("addressLine1", "");
                params.put("addressLine2", "");
            }

        }

        int i = 1;
        int totalQty = 0;
        for (Map.Entry<String, ReIssueProductBean> pair : issuedProducts.entrySet()) {
            ReturnProductReportBean bean = new ReturnProductReportBean();
            ReIssueProductBean product = pair.getValue();

            ResultSet productDetails = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` WHERE `stock`.`id`='" + product.getStockId() + "'");
            bean.setIndex(String.valueOf(i));
            if (productDetails.next()) {
                bean.setTitle(productDetails.getString("title"));
                switch (this.customerTypeId) {
                    case 1:
                        bean.setPrice(productDetails.getString("shop_price"));
                        break;
                    case 2:
                        bean.setPrice(productDetails.getString("daily_cutomer_price"));
                        break;
                    case 3:
                        bean.setPrice(productDetails.getString("new_customer_price"));
                        break;
                    default:
                        break;
                }
            }
            totalQty += product.getQty();
            bean.setQty(String.valueOf(product.getQty()));
            fields.add(bean);
            i++;

        }
        params.put("itemCount", String.valueOf(totalQty));

        JasperCompileManager.compileReportToFile("src/Report/returnProduct.jrxml");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(fields);
        JasperPrint report = JasperFillManager.fillReport("src/Report/returnProduct.jasper", params, dataSource);
//            this.paymentFrame.loadInvoiceDetails("");
        this.dispose();

        if (jCheckBox1.isSelected()) {
            JasperViewer jasperViewer = new JasperViewer(report, false);
            jasperViewer.setAlwaysOnTop(true);
            jasperViewer.setVisible(true);
        } else {
            JasperPrintManager.printReport(report, false);
        }
//        
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
//        viewt();
        IssueProductsForReturn issueProductsForReturn = new IssueProductsForReturn(returnedProducts, issuedProducts, invoiceId);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        if (model.getRowCount() > 0) {
            String amountText = jFormattedTextField1.getText();
            if (amountText.isEmpty()) {
                amountText = "0";
            }
            double paidAmount = Double.parseDouble(amountText);
            int calculatedTotal = this.tableTotal;
            double balance = this.totalValue - calculatedTotal;
            if (this.paymentPlanId != 1) {
                if (balance >= 0) {
                    issueProductsForReturn.setvalues(balance, 0, 1);

                } else {
                    if (paidAmount >= (-balance)) {
                        issueProductsForReturn.setvalues((-balance), 0, 2);

                    } else {
                        issueProductsForReturn.setvalues((-balance), paidAmount, 3);

                    }
                }

            } else {
                if (balance >= 0) {

                    issueProductsForReturn.setvalues(0, 0, 4);

                } else {
                    if (paidAmount >= (-balance)) {
                        issueProductsForReturn.setvalues((-balance), 0, 5);

                    } else {

                        JOptionPane.showMessageDialog(this, "Can not continue without pay full amount.", "Warning", JOptionPane.WARNING_MESSAGE);
                        updateTotal();
                    }

                }

            }
            try {
                issueProductsForReturn.updateAllDetails();

                double netBalance = paidAmount - (-balance);
                System.out.println(balance);
                printReport(String.valueOf(this.tableTotal), String.valueOf((int) this.totalValue), String.valueOf((int) -balance), String.valueOf((int) paidAmount), String.valueOf((int) netBalance));

            } catch (Exception ex) {
                Login.log1.warning(ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Add Product to Continue", "Warning", JOptionPane.WARNING_MESSAGE);

        }

//String invId = this.invoiceId;
//        try {
//            ResultSet resultSet = MySQL.execute("SELECT `paid_amount`,`total_amount`,"
//                    + "`customer_details`.`full_name`,`customer_details`.`address_line1`,"
//                    + "`customer_details`.`address_line2`,`customer_details`.`mobile`"
//                    + " FROM `invoice`"
//                    + "INNER JOIN `customer_details` ON `customer_details`.`nic` = `invoice`.`customer_details_nic`"
//                    + " WHERE id = '" + invId + "'");
//            if (resultSet.next()) {
//                double paid_amount = resultSet.getDouble("paid_amount");
//                double total_amount = resultSet.getDouble("total_amount");
//                double totval = 0, giveMon = 0, balance = 0;
//                try {
//                    totval = Double.parseDouble(jLabel8.getText());
//                    giveMon = Double.parseDouble(jTextField4.getText());
//                    balance = Double.parseDouble(jLabel13.getText());
//                } catch (NumberFormatException e) {
//                    System.out.println("Error parsing numeric values: " + e.getMessage());
//                    e.printStackTrace();
//                    return; // Exit the method to avoid further errors
//                }
//                String addressline1 = resultSet.getString("address_line1");
//                String addressline2 = resultSet.getString("address_line2");
//                String mobile = resultSet.getString("mobile");
//                String name = resultSet.getString("full_name");
//                double new_paid_amount = giveMon - balance;
//                double tot_paid_amount = new_paid_amount + paid_amount;
//                double new_tot_amount = total_amount - totalValue;
//                double final_amount = new_tot_amount + totval;
//                String qty = jLabel6.getText();
//
//                if (returnProductsConfirmation != null) {
//                    returnProductsConfirmation.updateProcess(totalValue, invoiceId);  // Pass additional data
//                } else {
//                    System.out.println("returnProductsConfirmation object is null!");
//                }
//
//                MySQL.execute("UPDATE invoice SET paid_amount = '" + tot_paid_amount + "', total_amount = '" + final_amount + "' WHERE id = '" + invId + "'");
//
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("date", formattedDate);
//            map.put("invoiceNumber", invId);
//            map.put("name", name);
//            map.put("addressLine1", addressline1);
//            map.put("addressLine2", addressline2);
//            map.put("mobileNumber", mobile);
//            map.put("totalQty", qty );
//            map.put("grandTotal", final_amount);
//            map.put("paidAmount", tot_paid_amount);
//            String reportPath = "src//Report//ReturnProduct.jrxml";
//            JasperCompileManager.compileReport(reportPath);
//            JRDataSource dataSource = new JREmptyDataSource();
//            JasperPrint print = JasperFillManager.fillReport(reportPath, map, dataSource);
//            JasperViewer.viewReport(print, false);
//            } else {
//                System.out.println("Invoice ID not found: " + invId);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseReleased

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1MouseReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addProductToTable(jTextField1.getText(), "barcode", 3, "WHERE `product_id`='" + jTextField1.getText() + "' ");

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addProductToTable(jTextField3.getText(), "model", 2, "INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` WHERE `model_number`='" + jTextField3.getText() + "' ");

        }
// TODO add your handling code here:
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addProductToTable(jTextField2.getText(), "serial", 4, "");

        }
// TODO add your handling code here:
    }//GEN-LAST:event_jTextField2KeyReleased

    private void viewBalance() {
        double balance = this.totalValue - this.tableTotal;
        String amountText = jFormattedTextField1.getText();
        if (amountText.isEmpty()) {
            amountText = "0";
        }
        try {
            double paidAmount = Double.parseDouble(amountText);

            if (balance < 0) {
                double customerBalance = paidAmount - (-balance);
                jLabel11.setText("Rs. " + customerBalance);
            } else {
                jLabel11.setText("0.00");

            }
        } catch (Exception e) {
            //Login.log1.warning(e.getMessage());
        }
    }


    private void jFormattedTextField1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jFormattedTextField1CaretUpdate
        viewBalance();
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField1CaretUpdate

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 1) {
            jButton1.setVisible(true);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusLost


    }//GEN-LAST:event_jTable1FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        }
        updateTotal();
        jButton1.setVisible(false);
        jTextField1.grabFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void clearFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");

    }
    private int tableTotal = 0;

    private void updateTotal() {
        int totalAmount = 0;
        int totalQty = 0;
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {

            double price = Double.parseDouble((String) jTable1.getValueAt(i, 6));
            int qty = Integer.parseInt((String) jTable1.getValueAt(i, 7));

            double itemTotal = price * qty;
            totalAmount += itemTotal;
            totalQty += qty;

        }
        jLabel6.setText(String.valueOf(totalQty));
        jLabel8.setText("Rs. " + String.valueOf(totalAmount) + ".0");
        viewBalance(totalAmount);

        this.tableTotal = totalAmount;
    }

    private void viewBalance(int totalAmount) {
        double balance = this.totalValue - totalAmount;
        Color red = new Color(203, 3, 3);
        Color blue = new Color(7, 45, 132);
        if (balance >= 0) {
            jLabel13.setText(String.valueOf("You Have Left Rs. " + balance));
            jPanel5.setBackground(blue);

        } else {
            jLabel13.setText(String.valueOf("You Should Pay Extra Rs. " + -balance));
            jPanel5.setBackground(red);
            jFormattedTextField1.setText(String.valueOf(-balance));
            jLabel11.setText("Rs. 0.00");
        }

    }

    public void addProductToTable(String id, String type, int column, String queryAppend) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        ReIssueProductBean bean = new ReIssueProductBean();

        try {
            if (type.equals("serial")) {
                ResultSet productDetailsSet = MySQL.execute("SELECT * FROM `product_sn` INNER JOIN `grn_item` ON `grn_item`.`id`=`product_sn`.`grn_item_id` INNER JOIN `stock` ON `stock`.`id`=`grn_item`.`stock_id` INNER JOIN `product` ON `product_sn`.`product_id`=`product`.`id` INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id` WHERE `product_sn`.`serial_number`='" + id + "'");
                if (productDetailsSet.next()) {
                    boolean isAdded = false;

                    for (int i = 0; i < model.getRowCount(); i++) {
                        String barcode = (String) jTable1.getValueAt(i, column);
                        if (barcode.equals(id)) {
                            isAdded = true;
                        }

                    }
                    if (!isAdded) {

                        if (this.issuedProducts.get(productDetailsSet.getString("stock.id")) == null) {
                            bean.setStockId(productDetailsSet.getString("stock.id"));
                            bean.setQty(1);
                            bean.setStatus("issued-for-return");

                            Vector<String> v = new Vector<>();
                            v.add(id);

                            bean.setSerials(v);
                            this.issuedProducts.put(productDetailsSet.getString("stock.id"), bean);
                        } else {
                            ReIssueProductBean get = this.issuedProducts.get(productDetailsSet.getString("stock.id"));
                            Vector<String> serials = get.getSerials();
                            serials.add(id);
                            int newQty = get.getQty() + 1;

                            get.setSerials(serials);
                            get.setQty(newQty);
                        }

                        Vector<String> v = new Vector<>();

                        v.add(String.valueOf(model.getRowCount() + 1));
                        v.add(productDetailsSet.getString("title"));
                        v.add(productDetailsSet.getString("model_number"));
                        v.add(productDetailsSet.getString("product.id"));
                        v.add(productDetailsSet.getString("product_sn.serial_number"));
                        v.add(productDetailsSet.getString("brand.name"));
                        switch (customerTypeId) {
                            case 1:
                                v.add(productDetailsSet.getString("shop_price"));
                                break;
                            case 2:
                                v.add(productDetailsSet.getString("daily_cutomer_price"));
                                break;
                            case 3:
                                v.add(productDetailsSet.getString("new_customer_price"));
                                break;
                            default:
                                break;
                        }
                        v.add("1");
                        model.addRow(v);
                        jTable1.setModel(model);
                        updateTotal();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "this product already added", "Warning", JOptionPane.WARNING_MESSAGE);
                        clearFields();

                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Enter valid serial number", "Warning", JOptionPane.WARNING_MESSAGE);

                }

            } else if (type.equals("stock")) {

                boolean isAdded = false;

                for (int i = 0; i < model.getRowCount(); i++) {
                    String barcode = (String) jTable1.getValueAt(i, column);
                    if (barcode.equals(queryAppend)) {
                        isAdded = true;
                        String currentQty = (String) jTable1.getValueAt(i, 7);
                        int parseInt = Integer.parseInt(currentQty);
                        parseInt++;
                        jTable1.setValueAt(String.valueOf(parseInt), i, 7);
                        ReIssueProductBean get = this.issuedProducts.get(id);
                        get.setQty(parseInt);
                        updateTotal();
                        clearFields();

                    }

                }

                if (!isAdded) {
                    ResultSet detatilSet = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`id`=`stock`.`product_id` INNER JOIN `brand` ON `brand`.`id`=`product`.`brand_id` WHERE `stock`.`id`='" + id + "'");

                    if (detatilSet.next()) {
                        bean.setStockId(detatilSet.getString("stock.id"));
                        bean.setQty(1);
                        bean.setStatus("issued-for-return");
                        this.issuedProducts.put(detatilSet.getString("stock.id"), bean);

                        Vector<String> v = new Vector<>();

                        v.add(String.valueOf(model.getRowCount() + 1));
                        v.add(detatilSet.getString("title"));
                        v.add(detatilSet.getString("model_number"));
                        v.add(detatilSet.getString("product.id"));
                        v.add("No Serial");
                        v.add(detatilSet.getString("brand.name"));
                        switch (customerTypeId) {
                            case 1:
                                v.add(detatilSet.getString("shop_price"));
                                break;
                            case 2:
                                v.add(detatilSet.getString("daily_cutomer_price"));
                                break;
                            case 3:
                                v.add(detatilSet.getString("new_customer_price"));
                                break;
                            default:
                                break;
                        }
                        v.add("1");
                        model.addRow(v);
                        jTable1.setModel(model);
                        updateTotal();
                        clearFields();

                    }
                }

            } else {
                ResultSet stockSet = MySQL.execute("SELECT COUNT(stock.id) AS row_count, stock.product_id AS id,stock.id AS stock_id FROM `stock` " + queryAppend);
                if (stockSet.next()) {
                    if (stockSet.getInt("row_count") == 1) {
                        ResultSet serialSet = MySQL.execute("SELECT COUNT(product_sn.id) AS row_count FROM `product_sn` WHERE `product_id`='" + stockSet.getString("id") + "'");
                        if (serialSet.next()) {
                            if (serialSet.getInt("row_count") == 0) {
                                boolean isAdded = false;

                                for (int i = 0; i < model.getRowCount(); i++) {
                                    String barcode = (String) jTable1.getValueAt(i, column);
                                    if (barcode.equals(id)) {
                                        isAdded = true;
                                        String currentQty = (String) jTable1.getValueAt(i, 7);
                                        int parseInt = Integer.parseInt(currentQty);
                                        parseInt++;
                                        jTable1.setValueAt(String.valueOf(parseInt), i, 7);
                                        ReIssueProductBean get = this.issuedProducts.get(stockSet.getString("stock_id"));
                                        get.setQty(parseInt);
                                        updateTotal();
                                        clearFields();

                                    }

                                }

                                if (!isAdded) {
                                    ResultSet detatilSet = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`id`=`stock`.`product_id` INNER JOIN `brand` ON `brand`.`id`=`product`.`brand_id` WHERE `product_id`='" + stockSet.getString("id") + "'");
                                    if (detatilSet.next()) {
                                        bean.setStockId(detatilSet.getString("stock.id"));
                                        bean.setQty(1);
                                        bean.setStatus("issued-for-return");
                                        this.issuedProducts.put(detatilSet.getString("stock.id"), bean);

                                        Vector<String> v = new Vector<>();

                                        v.add(String.valueOf(model.getRowCount() + 1));
                                        v.add(detatilSet.getString("title"));
                                        v.add(detatilSet.getString("model_number"));
                                        v.add(detatilSet.getString("product.id"));
                                        v.add("No Serial");
                                        v.add(detatilSet.getString("brand.name"));
                                        switch (customerTypeId) {
                                            case 1:
                                                v.add(detatilSet.getString("shop_price"));
                                                break;
                                            case 2:
                                                v.add(detatilSet.getString("daily_cutomer_price"));
                                                break;
                                            case 3:
                                                v.add(detatilSet.getString("new_customer_price"));
                                                break;
                                            default:
                                                break;
                                        }
                                        v.add("1");
                                        model.addRow(v);
                                        jTable1.setModel(model);
                                        updateTotal();
                                        clearFields();

                                    }
                                }

                            } else {

                                ShowAllSerials showAllSerials = new ShowAllSerials(this, true, this, stockSet.getString("stock_id"));
                                showAllSerials.setVisible(true);
                            }
                        }

                    } else if (stockSet.getInt("row_count") == 0) {
                        JOptionPane.showMessageDialog(this, "Invalid " + type + " Number", "Warning", JOptionPane.WARNING_MESSAGE);

                    } else if (stockSet.getInt("row_count") > 1) {
                        SelectStock selectStock = new SelectStock(this, true, id, this, customerTypeId);
                        selectStock.setVisible(true);

                    }
                } else {

                }
            }

        } catch (Exception ex) {
            Logger.getLogger(ChangeProducts.class.getName()).log(Level.SEVERE, null, ex);
            Login.log1.warning(ex.getMessage());
        }

    }

    public void viewt() {
        for (Map.Entry<String, ReIssueProductBean> product : this.issuedProducts.entrySet()) {
            ReIssueProductBean bean = product.getValue();
            System.out.println("stock id -" + bean.getStockId());
            System.out.println("qty -" + bean.getQty());
            System.out.println("status -" + bean.getStatus());
            Vector<String> serials = bean.getSerials();
            if (serials != null) {
                for (int j = 0; j < serials.size(); j++) {
                    String serial = serials.get(j);
                    System.out.println("     sn - " + serial);
                }
            }
            System.out.println(" ");
            System.out.println(" ");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

//        FlatMacLightLaf.setup();
//        
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ChangeProducts dialog = new ChangeProducts(new javax.swing.JFrame(), true);
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
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
