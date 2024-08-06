/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.DialogBox.AddProductToGRN;
import GUI.DialogBox.SupplierDetails;
import GUI.MainFrame.Login;
import GUI.MainFrame.MainFrame;
import Models.AddGRNBean;
import Models.GrnReportBean;
import Models.MySQL;
import Models.SupplierDetailsBean;
import Models.UserBean;
import java.awt.Frame;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Sandaruwan
 */
public class GRN extends javax.swing.JPanel {

    SupplierDetailsBean supplierDetailsBean = new SupplierDetailsBean();
    private HashMap<Integer, AddGRNBean> addGRNProductBeanMap;
    public int mapIndex = 0;
    private HashMap<String, Integer> branchMap = new HashMap<>();
    private MainFrame mainFame;
    long lastGRNInsertId;

    Vector<GrnReportBean> v = new Vector<>();
    HashMap<String, Object> hashMap = new HashMap<>();

    /**
     * Creates new form UserActivityReports
     */
    public GRN(MainFrame mainframe) {
        initComponents();
        this.mainFame = mainframe;
        addGRNProductBeanMap = new HashMap<>();
        loadGRNTable();

        jButton3.setVisible(false);
        jButton4.setVisible(false);
        loadSupplierDetails();
        jLabel15.setVisible(false);
        paymenttype();
        hidecomponents();
                jFormattedTextField6.setEnabled(false);
        jComboBox1.setEnabled(false);

    }

    public void hidecomponents(){
        

        
        
        jFormattedTextField8.setVisible(false);
        jDateChooser1.setVisible(false);
        jFormattedTextField4.setVisible(false);
        jFormattedTextField7.setVisible(false);
        
        jLabel16.setVisible(false);
        jLabel17.setVisible(false);
        jLabel18.setVisible(false);
        jLabel19.setVisible(false);
    }
        public void visiblecomponents(){
        jFormattedTextField8.setVisible(true);
        jDateChooser1.setVisible(true);
        jFormattedTextField4.setVisible(true);
        jFormattedTextField7.setVisible(true);
        
        jLabel16.setVisible(true);
        jLabel17.setVisible(true);
        jLabel18.setVisible(true);
        jLabel19.setVisible(true);
    }
    
    public void loadSupplierDetails() {
        System.out.println("load suplier out");

        if (supplierDetailsBean.getBrachName().equals("null")) {

            jLabel8.setVisible(false);
            jLabel9.setVisible(false);
            jLabel10.setVisible(false);
            jLabel11.setVisible(false);
            jLabel12.setVisible(false);
            jLabel13.setVisible(false);
            jLabel14.setVisible(false);

        } else {

            jLabel8.setVisible(true);
            jLabel9.setVisible(true);
            jLabel10.setVisible(true);
            jLabel11.setVisible(true);
            jLabel12.setVisible(true);
            jLabel13.setVisible(true);
            jLabel14.setVisible(true);
            jLabel15.setVisible(false);

            jLabel10.setText(supplierDetailsBean.getBrachName());
            jLabel11.setText(supplierDetailsBean.getAddressLine1());
            jLabel12.setText(supplierDetailsBean.getAddressLine2());
            jLabel14.setText(supplierDetailsBean.getContactNumber());
            jLabel15.setText(String.valueOf(supplierDetailsBean.getId()));

        }
    }
    

    
    private void addProductGrn() {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

//        String userNic = "199956868"; // mainFame.userBean.getNic();
//        String paidAmount = "100"; // jFormattedTextField1.getText();
        String userNic = mainFame.userBean.getNic();
        String paidAmount = jFormattedTextField1.getText();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date_time = now.format(formatter);
        String supplier_id = "";
        if (!jLabel15.getText().isEmpty()) {
            supplier_id = jLabel15.getText();
        }

        try {
            // Establish the connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cctv_shop_db", "root", "******");

            // Correct SQL statement with proper columns and parameter placeholders
            String insertSQL;
            if (!supplier_id.isEmpty()) {
                insertSQL = "INSERT INTO `grn` (`paid_amount`, `user_nic`, `date_time`, `supplier_details_id`) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);

                pstmt.setString(1, paidAmount);
                pstmt.setString(2, userNic);
                pstmt.setString(3, date_time);
                pstmt.setString(4, supplier_id);
            } else {
                insertSQL = "INSERT INTO `grn` (`paid_amount`, `user_nic`, `date_time`) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                // Set the parameters
                pstmt.setString(1, paidAmount);
                pstmt.setString(2, userNic);
                pstmt.setString(3, date_time);

            }

            // Execute the insert statement
            pstmt.executeUpdate();

            // Get the generated keys
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                 lastGRNInsertId = rs.getLong(1);
                hashMap.put("invoiceNumber", "#" + lastGRNInsertId);
                System.out.println("Last Inserted GRN ID: " + lastGRNInsertId);

                for (int i = 0; i < AddNewGRNTable.getRowCount(); i++) {
                    String title = String.valueOf(AddNewGRNTable.getValueAt(i, 1));
                    String productMOdel = String.valueOf(AddNewGRNTable.getValueAt(i, 2));
                    String category = String.valueOf(AddNewGRNTable.getValueAt(i, 3));
                    String brand = String.valueOf(AddNewGRNTable.getValueAt(i, 4));
                    String qty = String.valueOf(AddNewGRNTable.getValueAt(i, 5));
                    String buyingPrice = String.valueOf(AddNewGRNTable.getValueAt(i, 6));
                    String priceNew = String.valueOf(AddNewGRNTable.getValueAt(i, 7));
                    String priceDay = String.valueOf(AddNewGRNTable.getValueAt(i, 8));
                    String priceShop = String.valueOf(AddNewGRNTable.getValueAt(i, 9));

                    ResultSet stockData1 = MySQL.execute("SELECT * FROM `stock` INNER JOIN `product` ON `product`.`id`=`stock`.`product_id`  WHERE `model_number`='" + productMOdel + "' AND `title`='" + title + "'");

                    boolean notProduct = true;
                    boolean HaveSameProduct = false;

                    if (stockData1.next()) {
                        notProduct = false;

                        String sixMount = "0";
                        String oneYear = "0";
                        String twoYear = "0";
                        String companyWarrantyTime = "";
                        boolean companyWarranty = false;

                        for (Map.Entry<Integer, AddGRNBean> entryproduct : addGRNProductBeanMap.entrySet()) {

                            if (entryproduct.getValue().getModelNumber().equals(productMOdel) && entryproduct.getValue().getTitle().equals(title)) {

                                System.out.println("model num and title same " + productMOdel + title);

//                                System.out.println("stock id:" + stockData2.getString("id"));
                                sixMount = entryproduct.getValue().getSixMonthWarranty();
                                oneYear = entryproduct.getValue().getOneYearWarranty();
                                twoYear = entryproduct.getValue().getTwoYearWarranty();
                                companyWarrantyTime = entryproduct.getValue().getCompanyWarrantyTime();
                                companyWarranty = entryproduct.getValue().isCompanyWarranty();

                                System.out.println("six month:" + entryproduct.getValue().getSixMonthWarranty());
                                System.out.println("one year:" + entryproduct.getValue().getOneYearWarranty());
                                System.out.println("two year:" + entryproduct.getValue().getTwoYearWarranty());

                                if (entryproduct.getValue().isCompanyWarranty()) {

                                    if (entryproduct.getValue().getCompanyWarrantyTime().equals("sixMonth")) {
                                        sixMount = "1";
                                    } else if (entryproduct.getValue().getCompanyWarrantyTime().equals("oneYear")) {
                                        oneYear = "1";
                                    } else if (entryproduct.getValue().getCompanyWarrantyTime().equals("twoYear")) {
                                        twoYear = "1";
                                    }
                                }

                            }
                        }

                        ResultSet stockData2 = MySQL.execute("SELECT * FROM `stock` "
                                + "INNER JOIN `product` ON `product`.`id`=`stock`.`product_id`INNER JOIN `grn_item` ON `grn_item`.`stock_id`=`stock`.`id` INNER JOIN `warranty` ON `warranty`.`stock_id`=`stock`.`id`"
                                + "WHERE `model_number`='" + productMOdel + "' AND `title`='" + title + "' AND `new_customer_price`='" + priceNew + "' "
                                + "AND `daily_cutomer_price`='" + priceDay + "' AND `shop_price`='" + priceShop + "' AND `sixMonth`='" + sixMount + "' "
                                + "AND `oneyear`='" + oneYear + "' "
                                + "AND  `twoyear`='" + twoYear + "' AND `buying_price`='" + buyingPrice + "' ");

                        if (stockData2.next()) {
                            System.out.println("Same price  and warranty ");

                            for (Map.Entry<Integer, AddGRNBean> entryproduct : addGRNProductBeanMap.entrySet()) {

                                if (entryproduct.getValue().getModelNumber().equals(productMOdel) && entryproduct.getValue().getTitle().equals(title)) {

                                    System.out.println("model num and title same " + productMOdel + title);

                                    System.out.println("stock id:" + stockData2.getString("id"));
                                    System.out.println("six month:" + entryproduct.getValue().getSixMonthWarranty());
                                    System.out.println("one year:" + entryproduct.getValue().getOneYearWarranty());
                                    System.out.println("two year:" + entryproduct.getValue().getTwoYearWarranty());

//                                    if (entryproduct.getValue().isCompanyWarranty()) {
//
//                                        if (entryproduct.getValue().getCompanyWarrantyTime().equals("sixMonth")) {
//                                            sixMount = "1";
//                                        } else if (entryproduct.getValue().getCompanyWarrantyTime().equals("oneYear")) {
//                                            oneYear = "1";
//                                        } else if (entryproduct.getValue().getCompanyWarrantyTime().equals("twoYear")) {
//                                            twoYear = "1";
//                                        }
//                                    }
                                    ResultSet warrantyResultSet = MySQL.execute("SELECT * FROM `warranty` WHERE `stock_id`='" + stockData2.getString("id") + "'"
                                            + " AND `sixMonth`='" + sixMount + "' "
                                            + "AND `oneyear`='" + oneYear + "' "
                                            + "AND  `twoyear`='" + twoYear + "'");

                                    if (warrantyResultSet.next()) {
                                        HaveSameProduct = true;
                                        System.out.println("same waranty");
                                        int newqty = Integer.parseInt(qty) + stockData2.getInt("qty");
                                        MySQL.execute("UPDATE `stock` SET `qty`='" + newqty + "' WHERE `id`='" + stockData2.getInt("id") + "'");

                                        //MySQL.execute("UPDATE `stock` SET `qty`=`qty`+" + newqty + " WHERE `id`='" + stockData2.getInt("id") + "' ");
//                            MySQL.execute("INSERT INTO `grn_item` (`quantity`,`buying_price`,`grn_id`,`stock_id`) VALUES "
//                                    + "('" + qty + "','" + buyingPrice + "','" + lastGRNInsertId + "','" + stockData2.getInt("id") + "')");
                                        insertSQL = "INSERT INTO `grn_item` (`quantity`, `buying_price`, `grn_id`, `stock_id`) VALUES (?, ?, ?, ?)";
                                        pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);

                                        pstmt.setString(1, qty);
                                        pstmt.setString(2, buyingPrice);
                                        pstmt.setString(3, String.valueOf(lastGRNInsertId));
                                        pstmt.setString(4, String.valueOf(stockData2.getInt("id")));

                                        // Execute the insert statement
                                        pstmt.executeUpdate();

                                        // Get the generated keys
                                        rs = pstmt.getGeneratedKeys();
                                        if (rs.next()) {

                                            long lastInsertGrn_itemId = rs.getLong(1);

                                            System.out.println("Last Insert Grn_item ID : " + lastInsertGrn_itemId);

                                            for (Map.Entry<Integer, AddGRNBean> entrySn : addGRNProductBeanMap.entrySet()) {

                                                if (entrySn.getValue().getModelNumber().equals(productMOdel)) {

                                                    for (String serialNumbers : entrySn.getValue().getSerialNumberList()) {
                                                        System.out.println("Serial Number: " + serialNumbers);
                                                        MySQL.execute("INSERT INTO `product_sn` (`serial_number`,`grn_id`,`product_id`,`product_sn_status_id`,`grn_item_id`) VALUES "
                                                                + "('" + serialNumbers + "','" + lastGRNInsertId + "','" + stockData2.getString("product_id") + "','1','" + lastInsertGrn_itemId + "')");

                                                    }

                                                }

                                            }

                                        }
                                        break;

                                    }
                                }

                            }

                        } else {

                            String insertStockSQL = "INSERT INTO `stock` (`product_id`, `qty`, `shop_price`, `daily_cutomer_price`, `new_customer_price` ) VALUES (?, ?, ?, ?, ?)";
                            pstmt = conn.prepareStatement(insertStockSQL, PreparedStatement.RETURN_GENERATED_KEYS);

                            pstmt.setString(1, stockData1.getString("product_id"));
                            pstmt.setString(2, qty);
                            pstmt.setString(3, priceShop);
                            pstmt.setString(4, priceDay);
                            pstmt.setString(5, priceNew);

                            pstmt.executeUpdate();

                            rs = pstmt.getGeneratedKeys();
                            if (rs.next()) {
                                long lastStockInsertId = rs.getLong(1);
                                System.out.println("Last Inserted Stock ID: " + lastStockInsertId);
                                System.out.println("hellooooo");
                                if (sixMount.equals("0") && oneYear.equals("0") && twoYear.equals("0")) {
                                    System.out.println("hellooo 2");
                                    if (companyWarranty) {
                                        System.out.println("helloo 3");
                                        if (companyWarrantyTime.equals("sixMonth")) {
                                            MySQL.execute("INSERT INTO `warranty` (`sixMonth`, `oneyear`, `twoyear`, `stock_id`,`warranty_status_id`) VALUES "
                                                    + "('1', '0', '0', '" + lastStockInsertId + "','1')");

                                        } else if (companyWarrantyTime.equals("oneYear")) {
                                            MySQL.execute("INSERT INTO `warranty` (`sixMonth`, `oneyear`, `twoyear`, `stock_id`,`warranty_status_id`) VALUES "
                                                    + "('0', '1', '0', '" + lastStockInsertId + "','1')");
                                        } else if (companyWarrantyTime.equals("twoYear")) {
                                            MySQL.execute("INSERT INTO `warranty` (`sixMonth`, `oneyear`, `twoyear`, `stock_id`,`warranty_status_id`) VALUES "
                                                    + "('0', '0', '1', '" + lastStockInsertId + "','1')");
                                        }

                                    }

                                } else {
                                    System.out.println("hellooo 4");
                                    MySQL.execute("INSERT INTO `warranty` (`sixMonth`, `oneyear`, `twoyear`, `stock_id`,`warranty_status_id`) VALUES "
                                            + "('" + sixMount + "', '" + oneYear + "', '" + twoYear + "', '" + lastStockInsertId + "','2')");
                                }

                                insertSQL = "INSERT INTO `grn_item` (`quantity`, `buying_price`, `grn_id`, `stock_id`) VALUES (?, ?, ?, ?)";
                                pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);

                                pstmt.setString(1, qty);
                                pstmt.setString(2, buyingPrice);
                                pstmt.setString(3, String.valueOf(lastGRNInsertId));
                                pstmt.setString(4, String.valueOf(lastStockInsertId));

                                pstmt.executeUpdate();

                                // Get the generated keys
                                rs = pstmt.getGeneratedKeys();
                                if (rs.next()) {

                                    long lastInsertGrn_itemId = rs.getLong(1);

                                    System.out.println("Last Insert Grn_item ID : " + lastInsertGrn_itemId);

                                    for (Map.Entry<Integer, AddGRNBean> entry_sn : addGRNProductBeanMap.entrySet()) {

                                        if (entry_sn.getValue().getModelNumber().equals(productMOdel) && entry_sn.getValue().getTitle().equals(title)) {

                                            for (String serialNumbers : entry_sn.getValue().getSerialNumberList()) {
                                                System.out.println("Serial Number: " + serialNumbers);
                                                MySQL.execute("INSERT INTO `product_sn` (`serial_number`,`grn_id`,`product_id`,`product_sn_status_id`,`grn_item_id`) VALUES "
                                                        + "('" + serialNumbers + "','" + lastGRNInsertId + "','" + stockData1.getString("product_id") + "','1','" + lastInsertGrn_itemId + "')");

                                            }

                                        }

                                    }

                                }

                                //metanam serial number tiyenw nm ewat product sn ekat insert krnn oni
                            }
                        }
                        
                        

                    }

                    if (notProduct) {
                        //metant enw kiynne table eke product model ekat samana prodoct ekak stock eke ne kiyl. e nisa okkom table wlat me product eka insert krnn oni

                        ResultSet productData = MySQL.execute("SELECT * FROM `product` WHERE `model_number`='" + productMOdel + "' AND `title`='" + title + "'");

                        if (productData.next()) {
                            String insertStockSQL = "INSERT INTO `stock` (`product_id`, `qty`, `shop_price`, `daily_cutomer_price`, `new_customer_price` ) VALUES (?, ?, ?, ?, ?)";
                            pstmt = conn.prepareStatement(insertStockSQL, PreparedStatement.RETURN_GENERATED_KEYS);

                            pstmt.setString(1, productData.getString("id"));
                            pstmt.setString(2, qty);
                            pstmt.setString(3, priceShop);
                            pstmt.setString(4, priceDay);
                            pstmt.setString(5, priceNew);

                            pstmt.executeUpdate();

                            rs = pstmt.getGeneratedKeys();
                            if (rs.next()) {
                                long lastNewStockInsertId = rs.getLong(1);
                                System.out.println("Last Inserted New Stock ID: " + lastNewStockInsertId);

                                for (Map.Entry<Integer, AddGRNBean> entrywarrantyproduct : addGRNProductBeanMap.entrySet()) {

                                    if (entrywarrantyproduct.getValue().getModelNumber().equals(productMOdel) && entrywarrantyproduct.getValue().getTitle().equals(title)) {

                                        System.out.println("model num and title same " + productMOdel + title);

                                        String sixMonth = entrywarrantyproduct.getValue().getSixMonthWarranty();
                                        String oneYear = entrywarrantyproduct.getValue().getOneYearWarranty();
                                        String twoYear = entrywarrantyproduct.getValue().getTwoYearWarranty();

                                        System.out.println("six month new stock:" + entrywarrantyproduct.getValue().getSixMonthWarranty());
                                        System.out.println("one year new stock:" + entrywarrantyproduct.getValue().getOneYearWarranty());
                                        System.out.println("two year new stock:" + entrywarrantyproduct.getValue().getTwoYearWarranty());

                                        if (sixMonth.equals("0") && oneYear.equals("0") && twoYear.equals("0")) {

                                            System.out.println("all 0");
                                            if (entrywarrantyproduct.getValue().isCompanyWarranty()) {
                                                System.out.println("company warranty is true");
                                                if (entrywarrantyproduct.getValue().getCompanyWarrantyTime().equals("sixMonth")) {
                                                    MySQL.execute("INSERT INTO `warranty` (`sixMonth`, `oneyear`, `twoyear`, `stock_id`,`warranty_status_id`) VALUES "
                                                            + "('1', '0', '0', '" + lastNewStockInsertId + "','1')");

                                                } else if (entrywarrantyproduct.getValue().getCompanyWarrantyTime().equals("oneYear")) {
                                                    MySQL.execute("INSERT INTO `warranty` (`sixMonth`, `oneyear`, `twoyear`, `stock_id`,`warranty_status_id`) VALUES "
                                                            + "('0', '1', '0', '" + lastNewStockInsertId + "','1')");
                                                } else if (entrywarrantyproduct.getValue().getCompanyWarrantyTime().equals("twoYear")) {
                                                    MySQL.execute("INSERT INTO `warranty` (`sixMonth`, `oneyear`, `twoyear`, `stock_id`,`warranty_status_id`) VALUES "
                                                            + "('0', '0', '1', '" + lastNewStockInsertId + "','1')");
                                                }

                                            }

                                        } else {
                                            System.out.println("not all 0");

                                            MySQL.execute("INSERT INTO `warranty` (`sixMonth`, `oneyear`, `twoyear`, `stock_id`,`warranty_status_id`) VALUES "
                                                    + "('" + sixMonth + "', '" + oneYear + "', '" + twoYear + "', '" + lastNewStockInsertId + "','2')");
                                        }
//
//                                        MySQL.execute("INSERT INTO `warranty` (`sixMonth`, `oneyear`, `twoyear`, `stock_id`) VALUES "
//                                                + "('" + sixMonth + "', '" + oneYear + "', '" + twoYear + "', '" + lastNewStockInsertId + "')");

                                    }
                                }

//                                MySQL.execute("INSERT INTO `grn_item` (`quantity`,`buying_price`,`grn_id`,`stock_id`) VALUES "
//                                        + "('" + qty + "','" + buyingPrice + "','" + lastGRNInsertId + "','" + lastNewStockInsertId + "')");
                                insertSQL = "INSERT INTO `grn_item` (`quantity`, `buying_price`, `grn_id`, `stock_id`) VALUES (?, ?, ?, ?)";
                                pstmt = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);

                                pstmt.setString(1, qty);
                                pstmt.setString(2, buyingPrice);
                                pstmt.setString(3, String.valueOf(lastGRNInsertId));
                                pstmt.setString(4, String.valueOf(lastNewStockInsertId));

                                pstmt.executeUpdate();

                                // Get the generated keys
                                rs = pstmt.getGeneratedKeys();

                                if (rs.next()) {

                                    long lastInsertGrn_itemId = rs.getLong(1);

                                    System.out.println("Last Insert Grn_item ID : " + lastInsertGrn_itemId);

                                    for (Map.Entry<Integer, AddGRNBean> entry : addGRNProductBeanMap.entrySet()) {

                                        if (entry.getValue().getModelNumber().equals(productMOdel)) {

                                            for (String serialNumbers : entry.getValue().getSerialNumberList()) {
                                                System.out.println("Serial Number: " + serialNumbers);
                                                MySQL.execute("INSERT INTO `product_sn` (`serial_number`,`grn_id`,`product_id`,`product_sn_status_id`,`grn_item_id`) VALUES "
                                                        + "('" + serialNumbers + "','" + lastGRNInsertId + "','" + productData.getString("id") + "','1','" + lastInsertGrn_itemId + "')");

                                            }

                                        }

                                    }

                                }

//metanam serial number tiyenw nm ewat product sn ekat insert krnn oni
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "This Product dont have system", "warning", JOptionPane.WARNING_MESSAGE);
                        }

                    }
                    
                    
                    
                }
            }
        } catch (SQLException ex) {
            Login.log1.warning(ex.getMessage());

        } catch (Exception ex) {
            Login.log1.warning(ex.getMessage());

            Logger.getLogger(GRN.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Clean up resources
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
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

    private void calculation() {

        int rowCount = AddNewGRNTable.getRowCount();
        jTextField1.setText(String.valueOf(rowCount));

        int all_productQuantity = 0;
        int totalAmount = 0;

        for (int i = 0; i < rowCount; i++) {
            Object productqty = AddNewGRNTable.getValueAt(i, 5);
            Object buingPrice = AddNewGRNTable.getValueAt(i, 6);

            int singleProductCount = 0;
            singleProductCount = Integer.parseInt((String) productqty);

            int singleProductAmount = Integer.parseInt((String) buingPrice) * singleProductCount;
            totalAmount = totalAmount + singleProductAmount;
            all_productQuantity += singleProductCount;
        }
        jTextField5.setText(String.valueOf(totalAmount));
        jTextField4.setText(String.valueOf(all_productQuantity));

        Double paidAmount = Double.parseDouble(jFormattedTextField1.getText());

        Double balance = paidAmount - totalAmount;
        jTextField7.setText(String.valueOf(balance));

    }

    public void loadGRNTable() {

        DefaultTableModel model = (DefaultTableModel) AddNewGRNTable.getModel();
        model.setRowCount(0);
        for (Map.Entry<Integer, AddGRNBean> entry : addGRNProductBeanMap.entrySet()) {
            Vector<String> vector = new Vector<>();
            vector.add(String.valueOf(entry.getKey()));
            vector.add(entry.getValue().getTitle());
            vector.add(entry.getValue().getModelNumber());
            vector.add(entry.getValue().getCategory());
            vector.add(entry.getValue().getBrand());
            vector.add(String.valueOf(entry.getValue().getQty()));
            vector.add(entry.getValue().getBuyingPrice());
            vector.add(entry.getValue().getPriceNewCustomer());
            vector.add(entry.getValue().getPriceDailyCustomer());
            vector.add(entry.getValue().getPriceShop());

            model.addRow(vector);
        }

        calculation();
    }

    private void createReport() {
        

        int rowCount = AddNewGRNTable.getRowCount();

        for (int i = 0; i < rowCount; i++) {

            String indexNumber = String.valueOf(AddNewGRNTable.getValueAt(i, 0));
            String title = String.valueOf(AddNewGRNTable.getValueAt(i, 1));
            String modelNumber = String.valueOf(AddNewGRNTable.getValueAt(i, 2));
            String brand = String.valueOf(AddNewGRNTable.getValueAt(i, 4));
            String qty = String.valueOf(AddNewGRNTable.getValueAt(i, 5));
            String buyingPrice = String.valueOf(AddNewGRNTable.getValueAt(i, 6));

            GrnReportBean grnReportBean = new GrnReportBean();

            grnReportBean.setIndex(indexNumber);
            grnReportBean.setTitle(title + "_" + brand + "_" + modelNumber);
            grnReportBean.setPrice(buyingPrice+".00");
            grnReportBean.setQty(qty);

            int subTotal = Integer.parseInt(buyingPrice) * Integer.valueOf(qty);
            grnReportBean.setSub(String.valueOf(subTotal)+".00");
          
            v.add(grnReportBean);

        }

    }
 private void paymenttype() {

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `payment_type`");
            Vector<String> v = new Vector<>();

            v.add("Select Payment Type");
            while (rs.next()) {
                v.add(rs.getString("type"));
               
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(v);
            jComboBox1.setModel(model);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }
 
 public void allClear(){
                 v.clear();
            addGRNProductBeanMap.clear();
            DefaultTableModel model = (DefaultTableModel) AddNewGRNTable.getModel();
            model.setRowCount(0);
            jTextField1.setText("0");
            jTextField4.setText("0");
            jTextField5.setText("0");
            jFormattedTextField1.setValue(0.00);
            
            jLabel8.setText("0");
            jLabel9.setText("0");
            jLabel10.setText("0");
            jLabel11.setText("");
            jLabel12.setText("");
            jLabel13.setText("");
            jLabel14.setText("");
            
              
        jFormattedTextField8.setText("");
        jDateChooser1.setDate(null);
        jFormattedTextField4.setText("");
        jFormattedTextField7.setText("");
        jComboBox1.setSelectedItem("Select Payment Type");
 }
 
 public void printReort(Vector v , HashMap hashMap){
                 try {
                JasperCompileManager.compileReportToFile("src/Report/GrnReport.jrxml");

                JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(v);
                JasperPrint report = JasperFillManager.fillReport("src/Report/GrnReport.jasper", hashMap, datasource);
                JasperViewer.viewReport(report, false);

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
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jFormattedTextField6 = new javax.swing.JFormattedTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jFormattedTextField8 = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jFormattedTextField4 = new javax.swing.JTextField();
        jFormattedTextField7 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        AddNewGRNTable = new javax.swing.JTable();

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
        jPanel2.setLayout(new java.awt.GridLayout(3, 1));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1126, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel5);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Add New GRN");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addContainerGap(940, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 18, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel6);

        jButton1.setText("Add Product To GRN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setForeground(new java.awt.Color(255, 0, 0));
        jButton3.setText("Remove");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setForeground(new java.awt.Color(0, 0, 255));
        jButton4.setText("Edit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Add supplier Details");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 619, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(20, 20, 20))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(0, 23, Short.MAX_VALUE))
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

        jPanel4.setPreferredSize(new java.awt.Dimension(716, 190));

        jLabel2.setText("Product Count");

        jLabel3.setText("Total Quantity");

        jLabel4.setText("Total Amount");

        jButton2.setBackground(new java.awt.Color(204, 255, 204));
        jButton2.setText("Add GRN");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTextField1.setText("0");

        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTextField4.setText("0");

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTextField5.setText("0");

        jLabel6.setText("Paid Amount");

        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTextField7.setText("0");

        jLabel7.setText("Balance");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField1.setText("0.00");
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

        jLabel5.setText("Suppiler Invoice Id");

        jLabel16.setText("Cheque No");

        jLabel17.setText("Bank");

        jLabel18.setText("Branch");

        jLabel19.setText("Date");

        jFormattedTextField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        jFormattedTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField6FocusGained(evt);
            }
        });
        jFormattedTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField6ActionPerformed(evt);
            }
        });
        jFormattedTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField6KeyReleased(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Payment Type");

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

        jFormattedTextField8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        jFormattedTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jFormattedTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField8FocusGained(evt);
            }
        });
        jFormattedTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField8ActionPerformed(evt);
            }
        });
        jFormattedTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField8KeyReleased(evt);
            }
        });

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setText("____________________");

        jLabel9.setText("Branch :");

        jLabel8.setText("Address :");

        jLabel11.setText("____________________");

        jLabel12.setText("____________________");

        jLabel13.setText("Contact No :");

        jLabel14.setText("____________________");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addGap(78, 78, 78)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(43, 43, 43))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(31, 31, 31)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jFormattedTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(66, 66, 66)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField8)
                            .addComponent(jFormattedTextField4))))
                .addGap(50, 50, 50)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel21)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))))
                .addGap(94, 94, 94)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(22, 22, 22))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addGap(67, 67, 67))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jTextField1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField4)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jTextField5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jTextField7)))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jFormattedTextField6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addGap(45, 45, 45)
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGap(21, 21, 21)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))))
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_END);

        AddNewGRNTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Product Title", "Product Model", "Category", "Brand", "Quantity", "Buying Price", "Price (New Cutomer)", "Price (Daily Customer)", "Price (Shop)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        AddNewGRNTable.getTableHeader().setReorderingAllowed(false);
        AddNewGRNTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddNewGRNTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(AddNewGRNTable);
        if (AddNewGRNTable.getColumnModel().getColumnCount() > 0) {
            AddNewGRNTable.getColumnModel().getColumn(0).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(0).setPreferredWidth(10);
            AddNewGRNTable.getColumnModel().getColumn(1).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            AddNewGRNTable.getColumnModel().getColumn(2).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(2).setPreferredWidth(50);
            AddNewGRNTable.getColumnModel().getColumn(3).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(3).setPreferredWidth(50);
            AddNewGRNTable.getColumnModel().getColumn(4).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(4).setPreferredWidth(50);
            AddNewGRNTable.getColumnModel().getColumn(5).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(5).setPreferredWidth(50);
            AddNewGRNTable.getColumnModel().getColumn(6).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(6).setPreferredWidth(50);
            AddNewGRNTable.getColumnModel().getColumn(7).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(7).setPreferredWidth(50);
            AddNewGRNTable.getColumnModel().getColumn(8).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(8).setPreferredWidth(50);
            AddNewGRNTable.getColumnModel().getColumn(9).setResizable(false);
            AddNewGRNTable.getColumnModel().getColumn(9).setPreferredWidth(50);
        }

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        AddProductToGRN addProductGRN = new AddProductToGRN(parentFrame, true, addGRNProductBeanMap, this);
        addProductGRN.setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
try {
        if (AddNewGRNTable.getRowCount() != 0) {
            
             LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date_time = now.format(formatter);
            
                       //////////// grn_supplier_invoice
                        
                         String supplier_invoice_id = jFormattedTextField6.getText();
        String payment_type = jComboBox1.getSelectedItem().toString();
        String value = jTextField5.getText();
        String amount = jFormattedTextField1.getText();
 
        if(supplier_invoice_id.isBlank()){
            
            JOptionPane.showMessageDialog(this, "Plese Enter Suplier Invoice Id ... !" , "Warning" , JOptionPane.WARNING_MESSAGE);
            
        }else if(payment_type.equals("Select Payment Type")){
            JOptionPane.showMessageDialog(this, "Plese Select Payment Type ... !" , "Warning" , JOptionPane.WARNING_MESSAGE);
        }else if(amount.isBlank()){
            JOptionPane.showMessageDialog(this, "Plese Enter Cheque Amount ... !" , "Warning" , JOptionPane.WARNING_MESSAGE);  
        }else if(amount.equals("0.00")){
            JOptionPane.showMessageDialog(this, "Plese Enter Cheque Amount ... !" , "Warning" , JOptionPane.WARNING_MESSAGE);  
        }else{   
             addProductGrn();

                 MySQL.execute("INSERT INTO `grn_payment`(`value`,`supplier_invoice_id`,`grn_id`,`payment_type_id`) VALUES('"+value+"','"+supplier_invoice_id+"','"+lastGRNInsertId+"','"+jComboBox1.getSelectedIndex()+"') ;");
       
       
            createReport();
            hashMap.put("name", jLabel10.getText());
            hashMap.put("addressLine1", jLabel11.getText());
            hashMap.put("addressLine2", jLabel12.getText());
            hashMap.put("date", date_time); 
            hashMap.put("mobileNumber", jLabel14.getText());
            hashMap.put("itemCount", jTextField1.getText());
            hashMap.put("totalQty", jTextField4.getText());
            hashMap.put("grandTotal", "LKR : " + jTextField5.getText() + ".00");
            hashMap.put("paidAmount", "LKR : " + jFormattedTextField1.getText());
            hashMap.put("dueAmount", "LKR : " + jTextField7.getText() + "0  ");
            hashMap.put("supplierInvoiceID", "#" + jFormattedTextField6.getText());  
            hashMap.put("supplierInvoiceNum", supplier_invoice_id);
            hashMap.put("paymentType", "Payment Option : " + payment_type);
                 
         
          if(payment_type.equals("Cheque")){
             
        Date date = jDateChooser1.getDate();
        String cheque_no = jFormattedTextField8.getText();
        String bank = jFormattedTextField4.getText();
        String branch = jFormattedTextField7.getText();
        
        
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dNow ;
              
        if(cheque_no.isBlank()){
            JOptionPane.showMessageDialog(this, "Plese Enter Cheque Number ... !" , "Warning" , JOptionPane.WARNING_MESSAGE);
        }else if(bank.isBlank()){
            JOptionPane.showMessageDialog(this, "Plese Enter Bank Name ... !" , "Warning" , JOptionPane.WARNING_MESSAGE);
        }else if(branch.isBlank()){
            JOptionPane.showMessageDialog(this, "Plese Enter branch Name ... !" , "Warning" , JOptionPane.WARNING_MESSAGE);   
        }else if(amount.isBlank()){
            JOptionPane.showMessageDialog(this, "Plese Enter Cheque Amount ... !" , "Warning" , JOptionPane.WARNING_MESSAGE);  
        }else if(amount.equals("0.00")){
            JOptionPane.showMessageDialog(this, "Plese Enter Cheque Amount ... !" , "Warning" , JOptionPane.WARNING_MESSAGE);  
        }else{

         if(date == null){
            dNow = "";
        }else{
              dNow =  sdf.format(date);
         }

                ResultSet rs3 = MySQL.execute("SELECT `id` FROM `grn_payment` WHERE `value`='"+value+"' AND `supplier_invoice_id`='"+supplier_invoice_id+"' AND  `grn_id` = '"+lastGRNInsertId+"' AND `payment_type_id`= '"+jComboBox1.getSelectedIndex()+"' ");
                if(rs3.next()){

            MySQL.execute("INSERT INTO `grn_cheque`(`cheque_no`,`date`,`bank`,`branch`,`amount`,`grn_payment_id`,`cheque_status_id`) VALUES('"+cheque_no+"','"+dNow+"','"+bank+"','"+branch+"','"+amount+"','"+rs3.getString("id")+"','2')");

                 }
                

            hashMap.put("chequeNo", cheque_no);
            hashMap.put("chequeDate", "");
            hashMap.put("Bank", bank);
            hashMap.put("Branch", branch);

                printReort(v,hashMap);
                allClear();
                hidecomponents();
                }   
          }else{
              
            hashMap.put("chequeNo", "");
            hashMap.put("chequeDate", "");
            hashMap.put("Bank", "");
            hashMap.put("Branch", "");
            
                printReort(v,hashMap);
                allClear();
                hidecomponents();
          }
          
          
        }

 }
        } catch (Exception e) {
            }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void AddNewGRNTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddNewGRNTableMouseClicked
        // TODO add your handling code here:

        int clickCount = evt.getClickCount();

        if (clickCount == 2) {

            jButton3.setVisible(true);
            jButton4.setVisible(true);

        }

    }//GEN-LAST:event_AddNewGRNTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        int selectedRow = AddNewGRNTable.getSelectedRow();

        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) AddNewGRNTable.getModel();
//            model.removeRow(selectedRow);

            String index = String.valueOf(AddNewGRNTable.getValueAt(selectedRow, 0));

            try {
                int key = Integer.parseInt(index);
//                addGRNProductBeanMap.remove(key);
                System.out.println("table :" + key);

                System.out.println();
                System.out.println("key ek :" + addGRNProductBeanMap.get(key));
                addGRNProductBeanMap.remove(key);
                loadGRNTable();

            } catch (NumberFormatException e) {
                Login.log1.warning(e.getMessage());

            }

            loadGRNTable();
            AddNewGRNTable.revalidate();
            AddNewGRNTable.repaint();

            if (AddNewGRNTable.getRowCount() == 0) {

                jButton3.setVisible(false);
                jButton4.setVisible(false);

            }

        } else {
            JOptionPane.showMessageDialog(null, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        int selectedRow = AddNewGRNTable.getSelectedRow();

        String modelNumber = String.valueOf(AddNewGRNTable.getValueAt(selectedRow, 2));
        boolean edit = true;
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        AddProductToGRN addProductGRN = new AddProductToGRN(parentFrame, true, addGRNProductBeanMap, this, modelNumber, edit);
        addProductGRN.setVisible(true);


    }//GEN-LAST:event_jButton4ActionPerformed

    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased
        // TODO add your handling code here:

        if (!jFormattedTextField1.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) {

            jFormattedTextField1.setText("0");
        }

        calculation();
    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    private void jFormattedTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField1FocusGained
        // TODO add your handling code here:

        if (jFormattedTextField1.getText().equals("0.00")) {
            jFormattedTextField1.setText("");

        }

    }//GEN-LAST:event_jFormattedTextField1FocusGained

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        SupplierDetails supplierDetails = new SupplierDetails(parentFrame, true, this, supplierDetailsBean);
        supplierDetails.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jFormattedTextField6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField6FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField6FocusGained

    private void jFormattedTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField6ActionPerformed

    private void jFormattedTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField6KeyReleased

    }//GEN-LAST:event_jFormattedTextField6KeyReleased

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

    private void jFormattedTextField8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField8FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField8FocusGained

    private void jFormattedTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField8ActionPerformed

    private void jFormattedTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField8KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField8KeyReleased

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
  String type = jComboBox1.getSelectedItem().toString();
 
        if(type.equals("Cheque")){
            visiblecomponents();
            
            System.out.println(type);
        }else{
            hidecomponents();
          
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jFormattedTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable AddNewGRNTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    public static javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JTextField jFormattedTextField4;
    public static javax.swing.JFormattedTextField jFormattedTextField6;
    private javax.swing.JTextField jFormattedTextField7;
    private javax.swing.JFormattedTextField jFormattedTextField8;
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
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jTextField1;
    private javax.swing.JLabel jTextField4;
    private javax.swing.JLabel jTextField5;
    private javax.swing.JLabel jTextField7;
    // End of variables declaration//GEN-END:variables
}
