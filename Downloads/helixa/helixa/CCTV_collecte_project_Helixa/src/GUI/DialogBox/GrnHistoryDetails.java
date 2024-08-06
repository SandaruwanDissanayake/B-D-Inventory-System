/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.DialogBox;

import GUI.MainFrame.Login;

import Models.GrnReportBean;
import Models.MySQL;
import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Sandaruwan
 */
public class GrnHistoryDetails extends javax.swing.JDialog {

    /**
     * Creates new form GrnHistoryDetails
     */
    int grnId;
    String GrnChequid;
    private Vector<GrnReportBean> v = new Vector<>();
    private HashMap<String, Object> hashMap = new HashMap<>();
    private HashMap<String, String> grnItemId = new HashMap<>();

    public GrnHistoryDetails(java.awt.Frame parent, boolean modal, int grnId) {
        super(parent, modal);
        initComponents();
        this.grnId = grnId;
        LoadChequeType();
        jLabel1.setText("#" + grnId);
        DisabaleComponents();
        loadDetails();
       
        
        
        
    }
    
  public void DisabaleComponents (){
      chequenumber.setEnabled(false);
      bank.setEnabled(false);
      branch.setEnabled(false);
   
  }
public void LoadChequeType(){
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `cheque_status`");
            Vector<String> v = new Vector<>();

            while (rs.next()) {
                v.add(rs.getString("status"));
               
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            jComboBox1.setModel(model);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }
            
    private void loadDetails() {

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `grn` WHERE `id`='" + grnId + "'");
            if (resultSet.next()) {

                jLabel3.setText(resultSet.getString("date_time"));

                if (resultSet.getString("grn.supplier_details_id") != null) {
                    ResultSet supplierDeata = MySQL.execute("SELECT * FROM `supplier_details` INNER JOIN `grn` ON `grn`.`supplier_details_id`=`supplier_details`.`id` WHERE `supplier_details`.`id`='" + resultSet.getString("grn.supplier_details_id") + "'");
                    if (supplierDeata.next()) {

                        jLabel5.setText(supplierDeata.getString("branch_name"));
                        jLabel6.setText(supplierDeata.getString("address_line1"));
                        jLabel7.setText(supplierDeata.getString("address_line2"));
                        jLabel8.setText(supplierDeata.getString("contact_no"));
                    }

                }

            }
            
             ResultSet rs4 = MySQL.execute("SELECT * FROM `grn_cheque` INNER JOIN `grn_payment` ON `grn_cheque`.`grn_payment_id` = `grn_payment`.`id` WHERE `grn_payment`.`grn_id` = '"+grnId+"'");
             if (rs4.next()) {
            ////
            ResultSet rs = MySQL.execute("SELECT `payment_type`.`type` FROM `grn` INNER JOIN `user` ON `user`.`nic` = `grn`.`user_nic` INNER JOIN `grn_payment` ON `grn`.`id` = `grn_payment`.`grn_id`  INNER JOIN `payment_type` ON `grn_payment`.`payment_type_id` = `payment_type`.`id` WHERE `grn`.`id` = '"+grnId+"'");
             if (rs.next()) {
                 
                 jLabel21.setText(rs.getString("type"));
                 
                  hashMap.put("paymentType", "Payment Type : " + rs.getString("type") );
                 
                 if(rs.getString("type").equals("Cheque")){
                     
                     ResultSet r2 = MySQL.execute("SELECT `grn_cheque`.`id`,`grn_cheque`.`cheque_no`,`grn_cheque`.`bank`,`grn_cheque`.`branch`,`grn_cheque`.`date`,`grn_cheque`.`cheque_status_id`,`cheque_status`.`status` FROM `grn` INNER JOIN `user` ON `user`.`nic` = `grn`.`user_nic` INNER JOIN `grn_payment` ON `grn`.`id` = `grn_payment`.`grn_id`  INNER JOIN `payment_type` ON `grn_payment`.`payment_type_id` = `payment_type`.`id` INNER JOIN `grn_cheque` ON `grn_payment`.`id` = `grn_cheque`.`grn_payment_id` INNER JOIN `cheque_status` ON `grn_cheque`.`cheque_status_id` = `cheque_status`.`id` WHERE `grn`.`id` = '"+grnId+"'");
            
                     if (r2.next()) {
                         
                     GrnChequid =  r2.getString("id");   
                     
                     chequenumber.setText(r2.getString("cheque_no"));
                     bank.setText(r2.getString("bank"));
                     branch.setText(r2.getString("branch"));
                      
                     
                  jComboBox1.setSelectedItem(r2.getString("status"));
                  
                  hashMap.put("chequeNo", "Cheque Number : " +chequenumber.getText());
                  hashMap.put("Bank", "Bank Name :" +r2.getString("bank") );
                  hashMap.put("Branch", "Branch Name :" +r2.getString("branch") );
                       
                     
                   String d = r2.getString("date");
                         
                     if(d != null){
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(d);
                    jDateChooser1.setDate(date);

                  hashMap.put("chequeDate", "Exchange Date  :" +r2.getString("date") );
                  
                     }else{
                        jDateChooser1.setEnabled(true); 
                        
                        hashMap.put("chequeDate", " " );
                     }
                     
                     }
                 }
             }
            }else{
               jButton2.setVisible(false);
               jLabel23.setVisible(false);
               jLabel25.setVisible(false);
               jLabel26.setVisible(false);
               jLabel28.setVisible(false);
               jLabel30.setVisible(false);
               
               
               chequenumber.setVisible(false);
               bank.setVisible(false);
               branch.setVisible(false);
               jComboBox1.setVisible(false);
               jDateChooser1.setVisible(false);
               jLabel21.setText("Cash");
               
                  hashMap.put("paymentType", "Cash" );
                  hashMap.put("chequeNo", " " );
                  hashMap.put("Bank", " " );
                  hashMap.put("Branch", " " );
                  hashMap.put("chequeDate", " " );
               
             }
            
            ////`

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);
            ResultSet productDetails = MySQL.execute("SELECT * FROM `grn_item` "
                    + "INNER JOIN `stock` ON `grn_item`.`stock_id`=`stock`.`id` "
                    + "INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` "
                    + "INNER JOIN `brand` ON `brand`.`id`=`product`.`brand_id` WHERE `grn_item`.`grn_id`='" + grnId + "'");

            int totalQuantitiy = 0;
            int indexNum = 0;
            int grandTotal = 0;
            while (productDetails.next()) {
                Vector<Object> vector = new Vector<>();
                indexNum++;
                vector.add(indexNum);
                vector.add(productDetails.getString("title"));
                vector.add(productDetails.getString("model_number"));
                vector.add(productDetails.getString("quantity"));

                totalQuantitiy = totalQuantitiy + Integer.parseInt(productDetails.getString("quantity"));

                vector.add(productDetails.getString("buying_price"));

                String quantityStr = productDetails.getString("quantity");
                Double buyingPriceStr = productDetails.getDouble("buying_price");

                GrnReportBean grnReportBean = new GrnReportBean();

                grnReportBean.setIndex(String.valueOf(indexNum));
                grnReportBean.setTitle(productDetails.getString("title") + "_" + productDetails.getString("brand.name") + "_" + productDetails.getString("model_number"));
                grnReportBean.setPrice(String.valueOf(buyingPriceStr) + "0");
                
                
               grnReportBean.setQty(quantityStr);
               int subTotal = (int)(buyingPriceStr * Integer.valueOf(quantityStr));
               grnReportBean.setSub(String.valueOf(subTotal) + ".00");

                v.add(grnReportBean);

                if (quantityStr != null && buyingPriceStr != null) {
                    int quantity = Integer.parseInt(quantityStr);

                    grandTotal += quantity * buyingPriceStr;
                }
                vector.add(productDetails.getString("daily_cutomer_price"));
                vector.add(productDetails.getString("new_customer_price"));
                vector.add(productDetails.getString("shop_price"));

                ResultSet snNumber = MySQL.execute("SELECT * FROM `product_sn` WHERE `grn_item_id`='" + productDetails.getString("grn_item.id") + "'");

                if (snNumber.next()) {
                    vector.add(true);
                } else {
                    vector.add(false);
                }
                model.addRow(vector);
                grnItemId.put(String.valueOf(indexNum), productDetails.getString("grn_item.id"));
            }
            jTable1.setModel(model);

            jLabel10.setText(String.valueOf(indexNum));

            jLabel12.setText(String.valueOf(totalQuantitiy));

            jLabel14.setText(String.valueOf(grandTotal));

            jLabel16.setText(resultSet.getString("paid_amount"));

            int dueAmount = (int) (grandTotal - resultSet.getDouble("paid_amount"));

            jLabel18.setText(String.valueOf(dueAmount));

        } catch (Exception ex) {
            Login.log1.warning(ex.getMessage());

            Logger.getLogger(GrnHistoryDetails.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void createReport() {

        hashMap.put("name", jLabel5.getText());
        hashMap.put("addressLine1", jLabel6.getText());
        hashMap.put("addressLine2", jLabel7.getText());
        hashMap.put("date", jLabel3.getText());
        hashMap.put("invoiceNumber",jLabel1.getText());

        hashMap.put("mobileNumber", jLabel8.getText());
        hashMap.put("itemCount", jLabel10.getText());
        hashMap.put("totalQty", jLabel12.getText());
        hashMap.put("grandTotal", "LKR : " + jLabel14.getText() + ".00");
        hashMap.put("paidAmount", "LKR : " + jLabel16.getText());
        hashMap.put("dueAmount", "LKR : " + jLabel18.getText() + ".00");

        try {
            JasperCompileManager.compileReportToFile("src/Report/GrnReport.jrxml");

            JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(v);
            JasperPrint report = JasperFillManager.fillReport("src/Report/GrnReport.jasper", hashMap, datasource);
            JasperViewer viewer = new JasperViewer(report, false);

            // Create a JDialog to contain the JasperViewer
            JDialog reportDialog = new JDialog(this, "Report", true);
            reportDialog.getContentPane().add(viewer.getContentPane());
            reportDialog.setSize(950, 700);
            reportDialog.setLocationRelativeTo(this);
            reportDialog.setAlwaysOnTop(true); // Ensure it is on top
            reportDialog.setModal(true); // Make it modal

            reportDialog.setVisible(true);

//            viewer.setVisible(true);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        bank = new javax.swing.JFormattedTextField();
        branch = new javax.swing.JFormattedTextField();
        chequenumber = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(787, 140));

        jLabel1.setText("#92384698");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("GRN Details");

        jLabel3.setText("2022.12.10 12:13AM");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 542, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(25, 347));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel4.setPreferredSize(new java.awt.Dimension(25, 347));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(787, 185));

        jLabel10.setText("jLabel10");

        jLabel11.setText("Item Count :");

        jLabel12.setText("jLabel10");

        jLabel13.setText("Total Quantity :");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("jLabel10");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Grand Total :");

        jLabel16.setText("jLabel10");

        jLabel17.setText("Paid Amount :");

        jLabel18.setForeground(new java.awt.Color(204, 0, 0));
        jLabel18.setText("jLabel10");

        jLabel19.setText("Due Amount :");

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel20.setText("payment type");

        jLabel21.setForeground(new java.awt.Color(255, 51, 0));
        jLabel21.setText(" ");

        jLabel23.setText("Chequi No :");

        jLabel25.setText("bank :");

        jLabel26.setText("Branch");

        jLabel28.setText("Date");

        jLabel30.setText("stetus");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox1MouseClicked(evt);
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
        jDateChooser1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateChooser1MouseClicked(evt);
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

        jButton2.setText("save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        bank.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        bank.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bank.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bank.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bankFocusGained(evt);
            }
        });
        bank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bankMouseClicked(evt);
            }
        });
        bank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bankActionPerformed(evt);
            }
        });
        bank.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bankKeyReleased(evt);
            }
        });

        branch.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        branch.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        branch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        branch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                branchFocusGained(evt);
            }
        });
        branch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                branchMouseClicked(evt);
            }
        });
        branch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                branchActionPerformed(evt);
            }
        });
        branch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                branchKeyReleased(evt);
            }
        });

        chequenumber.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        chequenumber.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        chequenumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chequenumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chequenumberFocusGained(evt);
            }
        });
        chequenumber.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chequenumberMouseClicked(evt);
            }
        });
        chequenumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chequenumberActionPerformed(evt);
            }
        });
        chequenumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                chequenumberKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chequenumber)
                    .addComponent(bank)
                    .addComponent(branch)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(178, 178, 178))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator3)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel19)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel17)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel15)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator1)
                        .addComponent(jSeparator4)
                        .addComponent(jSeparator5))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(19, 19, 19))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(1, 1, 1)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(5, 5, 5)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(chequenumber, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addComponent(jLabel26))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(bank, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel25))
                                        .addGap(5, 5, 5)
                                        .addComponent(branch, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1.add(jPanel5, java.awt.BorderLayout.PAGE_END);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Title", "Model Number", "Quantity", "Buying Price", "Daily Cutomer Price", "New Customer Price", "Shop Price", "Serial Number"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(20);
        }

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {

            int row = jTable1.getSelectedRow();
            String productTitle = String.valueOf(jTable1.getValueAt(row, 1));
            String modelNum = String.valueOf(jTable1.getValueAt(row, 2));
            String rowNum = String.valueOf(jTable1.getValueAt(row, 0));
            boolean serialStatus = (boolean) jTable1.getValueAt(row, 8);
            if (serialStatus) {
                Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);

                ShowSerialNumbers showSerialNumber = new ShowSerialNumbers(parentFrame, true, productTitle, modelNum, rowNum, this.grnItemId);
                showSerialNumber.setVisible(true);
            }

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        createReport();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        String type = jComboBox1.getSelectedItem().toString();

        if(type.equals("Cheque")){
//            visiblecomponents();

           
        }else{
//            hidecomponents();

        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jDateChooser1ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jDateChooser1ComponentAdded

    }//GEN-LAST:event_jDateChooser1ComponentAdded

    private void jDateChooser1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jDateChooser1AncestorAdded

    }//GEN-LAST:event_jDateChooser1AncestorAdded

    private void jDateChooser1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jDateChooser1FocusGained

    }//GEN-LAST:event_jDateChooser1FocusGained

    private void jDateChooser1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jDateChooser1ComponentShown

    }//GEN-LAST:event_jDateChooser1ComponentShown

    private void jDateChooser1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jDateChooser1CaretPositionChanged

    }//GEN-LAST:event_jDateChooser1CaretPositionChanged

    private void jDateChooser1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jDateChooser1InputMethodTextChanged

    }//GEN-LAST:event_jDateChooser1InputMethodTextChanged

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        //        loadTable();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser1VetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jDateChooser1VetoableChange

    }//GEN-LAST:event_jDateChooser1VetoableChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

       String Chequeno =   chequenumber.getText();
       String ChequeBank =   bank.getText();
       String Chequebranch =   branch.getText();
       String Chequestetus = jComboBox1.getSelectedItem().toString();
       Date getDate = jDateChooser1.getDate();
       
              
       
       
       if(Chequeno.isBlank()){
         JOptionPane.showMessageDialog(this, "Please Enter The Cheque Number ... !!! " , "Warning" , JOptionPane.WARNING_MESSAGE );
        }else if(ChequeBank.isBlank()){
        JOptionPane.showMessageDialog(this, "Please Enter The Cheque Bank ... !!! " , "Warning" , JOptionPane.WARNING_MESSAGE );
        }else if(Chequebranch.isBlank()){
        JOptionPane.showMessageDialog(this, "Please Enter The Cheque Branch ... !!! " , "Warning" , JOptionPane.WARNING_MESSAGE );
        }else if(Chequestetus.equals("Select Cheque Stetus")){
        JOptionPane.showMessageDialog(this, "Please Select The Cheque Stetus ... !!! " , "Warning" , JOptionPane.WARNING_MESSAGE );
        }else if(getDate == null){
            JOptionPane.showMessageDialog(this,"Please select Date " , "Warning" , JOptionPane.WARNING_MESSAGE);
        }else{
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       String date = sdf.format(getDate);
       
           try { 
                ResultSet rs1 = MySQL.execute("SELECT `id` AS `chequeStetus_id` FROM `cheque_status` WHERE `status` = '"+Chequestetus+"' ");    
                rs1.next();
 
        ResultSet rs = MySQL.execute("SELECT * FROM `grn_cheque` WHERE `cheque_no` = '"+Chequeno+"' ");
            
                 if(rs.next()){

                     MySQL.execute("UPDATE `grn_cheque` SET `date` = '"+date+"' , `bank` = '"+ChequeBank+"' , `branch` = '"+Chequebranch+"' , `cheque_status_id` = '"+rs1.getString("chequeStetus_id")+"'  WHERE `cheque_no` = '"+Chequeno+"' AND `id` = '"+GrnChequid+"' ");
                     
                 }else{
                     
                      int option = JOptionPane.showConfirmDialog(this, "Do you want to Update  Check Number ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {
                        
                         MySQL.execute("UPDATE `grn_cheque` SET `cheque_no` = '"+Chequeno+"', `date` = '"+date+"' , `bank` = '"+ChequeBank+"' , `branch` = '"+Chequebranch+"' , `cheque_status_id` = '"+rs1.getString("chequeStetus_id")+"'  WHERE  `id` = '"+GrnChequid+"' ");
                        
                    }else{
                        
                         MySQL.execute("UPDATE `grn_cheque` SET `date` = '"+date+"' , `bank` = '"+ChequeBank+"' , `branch` = '"+Chequebranch+"' , `cheque_status_id` = '"+rs1.getString("chequeStetus_id")+"'  WHERE  `id` = '"+GrnChequid+"' ");
                        
                    }
                     
                 }
                 
                 DisabaleComponents();
                 loadDetails();

        } catch (Exception e) {
            e.printStackTrace();
        }     
       }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void bankFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bankFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_bankFocusGained

    private void bankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bankActionPerformed

    private void bankKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bankKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_bankKeyReleased

    private void branchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_branchFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_branchFocusGained

    private void branchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_branchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_branchActionPerformed

    private void branchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_branchKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_branchKeyReleased

    private void chequenumberFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chequenumberFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_chequenumberFocusGained

    private void chequenumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequenumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chequenumberActionPerformed

    private void chequenumberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chequenumberKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_chequenumberKeyReleased

    private void chequenumberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chequenumberMouseClicked
 if(evt.getClickCount() == 2){
     chequenumber.setEnabled(true);
 }
    }//GEN-LAST:event_chequenumberMouseClicked

    private void bankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bankMouseClicked
 if(evt.getClickCount() == 2){
     bank.setEnabled(true);
 }
    }//GEN-LAST:event_bankMouseClicked

    private void branchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_branchMouseClicked
  if(evt.getClickCount() == 2){
     branch.setEnabled(true);
 }
    }//GEN-LAST:event_branchMouseClicked

    private void jDateChooser1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooser1MouseClicked
       if(evt.getClickCount() == 2){
    
 }
    }//GEN-LAST:event_jDateChooser1MouseClicked

    private void jComboBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseClicked
 if(evt.getClickCount() == 2){
     jComboBox1.setEnabled(true);
 }
    }//GEN-LAST:event_jComboBox1MouseClicked

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
            java.util.logging.Logger.getLogger(GrnHistoryDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GrnHistoryDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GrnHistoryDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GrnHistoryDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField bank;
    private javax.swing.JFormattedTextField branch;
    private javax.swing.JFormattedTextField chequenumber;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    public static javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
