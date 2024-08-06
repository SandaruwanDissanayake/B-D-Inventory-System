/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.MainFrame.Login;
import Models.MySQL;
import Models.ReturnProductBean;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mr. SP
 */
public class ReturnProductTable extends javax.swing.JPanel {

    String invoiceId;
    public HashMap<String, ReturnProductBean> products = new HashMap<>();

    /**
     * Creates new form ReturnProductTable
     */
    public ReturnProductTable(String invoiceId) {
        initComponents();
        this.invoiceId = invoiceId;
        loadInvoiceId(invoiceId);
    }

    public void loadInvoiceId(String invoiceId) {

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `invoice` "
                    + "INNER JOIN `customer_details` ON `invoice`.`customer_details_nic` = `customer_details`.`nic` "
                    + "INNER JOIN `payment_option` ON `invoice`.`payment_option_id` = `payment_option`.`id` "
                    + "INNER JOIN `customer_type` ON `invoice`.`customer_type_id` = `customer_type`.`id` "
                    + "WHERE `invoice`.`id` ='" + this.invoiceId + "'");

            if (resultSet.next()) {

                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);
                int uniqueId = 1;
                ResultSet invoiceItemSet = MySQL.execute("SELECT * FROM `invoice_item`"
                        + "INNER JOIN `stock` ON `invoice_item`.`stock_id`=`stock`.`id`"
                        + "INNER JOIN `product` ON `stock`.`product_id`=`product`.`id`"
                        + "INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id`"
                        + "INNER JOIN `warranty_period` ON `invoice_item`.`warranty_period_id`=`warranty_period`.`id`"
                        + " WHERE `invoice_id`='" + this.invoiceId + "'");
                while (invoiceItemSet.next()) {

                    ReturnProductBean bean = new ReturnProductBean();
                    HashMap<String, Boolean> serials = new HashMap<>();
                    ResultSet serialNumberSet = MySQL.execute("SELECT * FROM `invoice_has_product_sn` INNER JOIN  `product_sn` ON `invoice_has_product_sn`.`product_sn_id`=`product_sn`.`id` WHERE `product_sn_id` IN (SELECT `id` FROM `product_sn` WHERE `grn_item_id`IN (SELECT `id` FROM `grn_item` WHERE `stock_id`='" + invoiceItemSet.getString("stock_id") + "')) AND `invoice_id`='" + this.invoiceId + "' ");
                    int returnQty = 0;
                    double warrantyPrice = 0;
                    ResultSet warrantyPrices = MySQL.execute("SELECT * FROM `warranty` WHERE `stock_id`='" + invoiceItemSet.getString("stock_id") + "'");

                    if (warrantyPrices.next()) {
                        switch (invoiceItemSet.getInt("warranty_period_id")) {
                            case 1:
                                warrantyPrice = 0;
                                break;
                            case 2:
                                warrantyPrice = warrantyPrices.getDouble("six_month");
                                break;
                            case 3:
                                warrantyPrice = warrantyPrices.getDouble("one_year");
                                break;
                            case 4:
                                warrantyPrice = warrantyPrices.getDouble("two_year");
                                break;
                            default:
                                break;
                        }
                    }
                    if (serialNumberSet.next()) {

                        do {

                            Vector<Object> v = new Vector<>();
                            v.add(Boolean.FALSE);
                            v.add(uniqueId);
                            v.add(invoiceItemSet.getString("title"));
                            v.add(invoiceItemSet.getString("model_number"));
                            v.add(invoiceItemSet.getString("product_id"));
                            v.add(serialNumberSet.getString("serial_number"));

                            v.add(invoiceItemSet.getString("name"));
                            serials.put(serialNumberSet.getString("serial_number"), false);
                            double price = 0;

                            switch (resultSet.getInt("customer_type_id")) {
                                case 1:
                                    price = invoiceItemSet.getDouble("shop_price") + warrantyPrice;
                                    v.add(String.valueOf(price));
                                    break;
                                case 2:
                                    price = invoiceItemSet.getDouble("daily_cutomer_price") + warrantyPrice;
                                    v.add(String.valueOf(price));
                                    break;
                                case 3:
                                    price = invoiceItemSet.getDouble("new_customer_price") + warrantyPrice;
                                    v.add(String.valueOf(price));
                                    break;
                                default:
                                    break;
                            }

                            v.add("1");
                            v.add(invoiceItemSet.getString("period"));

                            dtm.addRow(v);
                            jTable1.setModel(dtm);
                            uniqueId++;

                        } while (serialNumberSet.next());

                    } else {
                        returnQty = invoiceItemSet.getInt("quantity");
                        Vector<Object> v = new Vector<>();
                        v.add(Boolean.FALSE);
                        v.add(uniqueId);
                        v.add(invoiceItemSet.getString("title"));
                        v.add(invoiceItemSet.getString("model_number"));
                        v.add(invoiceItemSet.getString("product_id"));
                        v.add("No Serial Number");

                        v.add(invoiceItemSet.getString("name"));
                        double price = 0;

                        switch (resultSet.getInt("customer_type_id")) {
                            case 1:
                                price = invoiceItemSet.getDouble("shop_price") + warrantyPrice;
                                v.add(String.valueOf(price));
                                break;
                            case 2:
                                price = invoiceItemSet.getDouble("daily_cutomer_price") + warrantyPrice;
                                v.add(String.valueOf(price));
                                break;
                            case 3:
                                price = invoiceItemSet.getDouble("new_customer_price") + warrantyPrice;
                                v.add(String.valueOf(price));
                                break;
                            default:
                                break;
                        }
                        v.add(invoiceItemSet.getString("quantity"));
                        v.add(invoiceItemSet.getString("period"));

                        dtm.addRow(v);
                        jTable1.setModel(dtm);

                        uniqueId++;
                    }

                    bean.setInvoiceItemId(invoiceItemSet.getString("invoice_item.id"));
                    bean.setReturnQty(returnQty);
                    bean.setSerialNumbers(serials);
                    bean.setStockId(invoiceItemSet.getString("stock.id"));
                    bean.setIsReturn(false);
                    products.put(invoiceItemSet.getString("product.id"), bean);

                }
            }
        } catch (Exception e) {
                                  Login.log1.warning(e.getMessage());

            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "#", "Product Title", "Product Model", "Barcode", "Serial Number", "Brand", "Item Price", "Quantity", "Warranty"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(17);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(17);
        }

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
