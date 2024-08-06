/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Mr. SP
 */
public class IssueProductsForReturn {

    private HashMap<String, ReturnProductBean> returnedProducts;
    private HashMap<String, ReIssueProductBean> issuedProducts;
    private String invoiceId;
    double value = 0;
    double paidAmount = 0;
    int status = 0;

    public IssueProductsForReturn(HashMap<String, ReturnProductBean> returnedProducts, HashMap<String, ReIssueProductBean> issuedProducts, String invoiceId) {
        this.returnedProducts = returnedProducts;
        this.issuedProducts = issuedProducts;
        this.invoiceId = invoiceId;
    }

    public String updateAllDetails() throws Exception {
        issueNewProducts();
        updateReturnProducts();
        updateAmounts();
        
        return "success";
        
    }

    public void setvalues(double value, double paidAmount, int status) {
        this.value = value;
        this.paidAmount = paidAmount;
        this.status = status;
    }

    private void updateAmounts() throws Exception {
        switch (this.status) {
            case 1:
                {
                    ResultSet invoiceTotal = MySQL.execute("SELECT * FROM `invoice` WHERE `id`='" + this.invoiceId + "'");
                    ResultSet paymentValue = MySQL.execute("SELECT * FROM `payment_value` WHERE `invoice_id`='" + this.invoiceId + "'");
                    if (invoiceTotal.next() && paymentValue.next()) {
                        double newInvoiceTotal = invoiceTotal.getInt("total_amount")-this.value;
                        double newPaymentTotal = paymentValue.getInt("total")-this.value;
                        MySQL.execute("UPDATE `invoice` SET `total_amount`='" + newInvoiceTotal + "' WHERE `id`='" + invoiceId + "'");
                        MySQL.execute("UPDATE `payment_value` SET `total`='" + newPaymentTotal + "' WHERE `invoice_id`='" + invoiceId + "'");
                    }       break;
                }
            case 2:
                {
                    ResultSet invoiceTotal = MySQL.execute("SELECT * FROM `invoice` WHERE `id`='" + this.invoiceId + "'");
                    if (invoiceTotal.next()) {
                        double newInvoiceTotal = invoiceTotal.getInt("total_amount")+this.value;
                        MySQL.execute("UPDATE `invoice` SET `total_amount`='" + newInvoiceTotal + "' WHERE `id`='" + invoiceId + "'");
                    }       break;
                }
            case 3:
                {
                    ResultSet invoiceTotal = MySQL.execute("SELECT * FROM `invoice` WHERE `id`='" + this.invoiceId + "'");
                    ResultSet paymentValue = MySQL.execute("SELECT * FROM `payment_value` WHERE `invoice_id`='" + this.invoiceId + "'");
                    if (invoiceTotal.next() && paymentValue.next()) {
                        double newInvoiceTotal = invoiceTotal.getInt("total_amount")+this.value;
                        double newPaymentTotal = paymentValue.getInt("total")+(this.value-this.paidAmount);
                        MySQL.execute("UPDATE `invoice` SET `total_amount`='" + newInvoiceTotal + "' WHERE `id`='" + invoiceId + "'");
                        MySQL.execute("UPDATE `payment_value` SET `total`='" + newPaymentTotal + "' WHERE `invoice_id`='" + invoiceId + "'");
                    }       break;
                }
            case 4:
                {
                    ResultSet invoiceTotal = MySQL.execute("SELECT * FROM `invoice` WHERE `id`='" + this.invoiceId + "'");
                    if (invoiceTotal.next()) {
                        double newInvoiceTotal = invoiceTotal.getInt("total_amount")-this.value;
                        MySQL.execute("UPDATE `invoice` SET `total_amount`='" + newInvoiceTotal + "' WHERE `id`='" + invoiceId + "'");
                    }       break;
                }
            case 5:
                {
                    ResultSet invoiceTotal = MySQL.execute("SELECT * FROM `invoice` WHERE `id`='" + this.invoiceId + "'");
                    if (invoiceTotal.next()) {
                        double newInvoiceTotal = invoiceTotal.getInt("total_amount")+this.value;
                        MySQL.execute("UPDATE `invoice` SET `total_amount`='" + newInvoiceTotal + "' WHERE `id`='" + invoiceId + "'");
                    }       break;
                }
            default:
                break;
        }
    }

    private void issueNewProducts() throws Exception {
        for (Map.Entry<String, ReIssueProductBean> pair : issuedProducts.entrySet()) {
            ReIssueProductBean product = pair.getValue();
            MySQL.execute("INSERT INTO `invoice_item`(`quantity`,`invoice_id`,`stock_id`,`return_status_id`,`warranty_period_id`) VALUES('" + product.getQty() + "','" + this.invoiceId + "','" + product.getStockId() + "','3','1')");

            ResultSet stockQty = MySQL.execute("SELECT qty FROM `stock` WHERE `id`='" + product.getStockId() + "'");
            if (stockQty.next()) {
                int availableQty = stockQty.getInt("qty");
                int changedStock = availableQty - product.getQty();
                MySQL.execute("UPDATE `stock` SET `qty`='" + changedStock + "' WHERE `id`='" + product.getStockId() + "'");

            }
            Vector<String> serials = product.getSerials();
            if (serials !=null) {
                for (String serial : product.getSerials()) {
                    MySQL.execute("UPDATE `product_sn` SET `product_sn_status_id`='2' WHERE `serial_number`='" + serial + "'");
                    ResultSet SerialId = MySQL.execute("SELECT id FROM `product_sn` WHERE `serial_number`='" + serial + "'");
                    if (SerialId.next()) {
                        MySQL.execute("INSERT INTO `invoice_has_product_sn`(`invoice_id`,`product_sn_id`) VALUES('" + this.invoiceId + "','" + SerialId.getInt("id") + "')");

                    }
                }
            }
        }
    }

    private void updateReturnProducts() throws Exception {
        for (Map.Entry<String, ReturnProductBean> pair : returnedProducts.entrySet()) {
            ReturnProductBean product = pair.getValue();
            if (product.isIsReturn()) {
                MySQL.execute("UPDATE `invoice_item` SET `return_status_id`='2' , `return_qty`='" + product.getReturnQty() + "' WHERE `id`='" + product.getInvoiceItemId() + "'");
                String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                if (!product.getSerialNumbers().isEmpty()) {
                    int insertID = getInsertID("INSERT INTO `return_product`(`date_time`,`invoice_item_id`) VALUES('" + today + "','" + product.getInvoiceItemId() + "') ");
                    for (Map.Entry<String, Boolean> serials : product.getSerialNumbers().entrySet()) {
                        if (serials.getValue()) {
                            MySQL.execute("UPDATE `product_sn` SET `product_sn_status_id`='3' WHERE `serial_number`='" + serials.getKey() + "'");
                            MySQL.execute("DELETE FROM `invoice_has_product_sn` WHERE `invoice_id`='"+this.invoiceId+"' AND `product_sn_id`=(SELECT ID FROM `product_sn` WHERE `serial_number`='"+serials.getKey()+"')");

                            if (insertID != 0) {
                                MySQL.execute("INSERT INTO `returned_serials` (`serial_num`,`return_product_id`) VALUES('" + serials.getKey() + "','" + insertID + "')");

                            } else {
                                System.out.println("mysql connection error");
                            }
                        }
                    }
                } else {
                    MySQL.execute("INSERT INTO `return_product`(`date_time`,`invoice_item_id`) VALUES('" + today + "','" + product.getInvoiceItemId() + "')");
                }

            }
        }
    }

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
                ex.printStackTrace();

            }
        }

        return insertId;

    }
}
