/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.MainPanel;

import GUI.MainFrame.Login;
import Models.Backup;
import Models.MySQL;
import Models.ProductBeanAMA;
import static java.awt.AWTEventMulticaster.add;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PCnet Computers
 */
public class AddProductQuotation extends javax.swing.JDialog {

    AddNewQuotation addNewQuotation;
//    private ProductBeanAMA productBeanAMA = new ProductBeanAMA();

    /**
     * Creates new form NewJDialog
     */
    public AddProductQuotation(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public AddProductQuotation(AddNewQuotation parent, boolean modal) {
        this.addNewQuotation = parent;
        initComponents();
//        setAlwaysOnTop(true);
        
    }

    public void searchProduct() {
        String customer_type = addNewQuotation.jComboBox1.getSelectedItem().toString();
        String barcode = jTextField1.getText();
        String model = jTextField2.getText();

        if (barcode.isBlank()) {

            if (model.isBlank()) {
                JOptionPane.showMessageDialog(this, "Plese Enter The Barcode OR Model Number", "warning", JOptionPane.WARNING_MESSAGE);

            }
        }
        if (customer_type.equals("Select Customer Type")) {
            JOptionPane.showMessageDialog(this, "Plese Select The Customer Type", "warning", JOptionPane.WARNING_MESSAGE);
            this.dispose();

        } else {
            try {
                ResultSet resultSet = MySQL.execute("SELECT product.id AS barcode,product.title  AS title, product.description AS description,product.model_number  AS model_number , brand.name AS brand , category.name AS category ,stock.qty AS all_qty ,stock.shop_price AS shop_price, stock.daily_cutomer_price AS daily_cutomer_price, stock.new_customer_price AS new_customer_price FROM product INNER JOIN category ON product.category_id = category.id   INNER JOIN brand ON product.brand_id = brand.id  INNER JOIN stock ON product.id = stock.product_id WHERE product.id = '" + barcode + "' OR product.model_number = '" + model + "'");
                
            
                if (resultSet.next()) {
                    String P_barcode = resultSet.getString("barcode");
                    String productTitle = resultSet.getString("title");
                    String productModel = resultSet.getString("model_number");
                    String brand = resultSet.getString("brand");
                    String category = resultSet.getString("category");
                    
                    ///stock qty
                    String availableQuantity = resultSet.getString("all_qty");
                    
                    ///stock qty  
                    
                    
                    String description = resultSet.getString("description");

                    String item_price;

                    if (customer_type.equals("Shop")) {
                        item_price = resultSet.getString("shop_price");
                    } else if (customer_type.equals("Daily Customer")) {
                        item_price = resultSet.getString("daily_cutomer_price");
                    } else {
                        item_price = resultSet.getString("new_customer_price");
                    }

                    jTextField3.setText(P_barcode);
                    jTextField4.setText(productTitle);
                    jTextField5.setText(productModel);
                    jTextField6.setText(brand);
                    jTextField11.setText(category);
                    jTextField7.setText(availableQuantity);
                    jTextArea1.setText(description);
                    jTextField10.setText(item_price);

                    jTextField9.grabFocus();

                }else{
                    JOptionPane.showMessageDialog(this, "Plese Enter Valid details in Product", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception e) {
                 Login.log1.warning(e.getMessage());
            }
        }
    }

    public void check_stock(String qty) {
        try {
            String barcode = jTextField3.getText();

            if (barcode.isBlank()) {
                JOptionPane.showMessageDialog(this, "Plese Select Product", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                ResultSet rs = MySQL.execute("SELECT * FROM `stock` WHERE `product_id`='" + barcode + "' ");
                if (rs.next()) {
                    Integer stock_qty = Integer.parseInt(rs.getString("qty"));

                    if (Integer.parseInt(qty) <= stock_qty) {

                    } else {
                        JOptionPane.showMessageDialog(this, "Lower Stock ... There are only ' " + stock_qty + " ' units of this product in stock", "Warning", JOptionPane.WARNING_MESSAGE);
//                        jTextField9.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "This Item Haven't Stock", "Warning", JOptionPane.WARNING_MESSAGE);

                }
            }
        } catch (Exception e) {
             Login.log1.warning(e.getMessage());
        }
    }
        public void reset_Fields(){
          jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextArea1.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
    }

        public void addProductQuotationTable(){
            DefaultTableModel dtm = (DefaultTableModel) addNewQuotation.jTable1.getModel();
        
        String barcode = jTextField3.getText();
        String title = jTextField4.getText();
        String model = jTextField5.getText();
        String brand = jTextField6.getText();
        String description = jTextArea1.getText();
        String qty = jTextField9.getText();
        String price = jTextField10.getText();

        String cus_type = addNewQuotation.jComboBox1.getSelectedItem().toString();
         if (cus_type.equals("Select Customer Type")) {
            JOptionPane.showMessageDialog(this, "Plese Select The Customer Type", "warning", JOptionPane.WARNING_MESSAGE);
         }else if(qty.equals("0")){
            JOptionPane.showMessageDialog(this, "please Enter The Quantity ... " , "Warning" , JOptionPane.WARNING_MESSAGE);
        }else if(qty.isBlank()){
             JOptionPane.showMessageDialog(this, "please Enter The Quantity ... " , "Warning" , JOptionPane.WARNING_MESSAGE);
        }else{
            
            
            boolean isFound = false;
            int x = -1;
            int m = 1 ;
            
        for (int i = 0; i < dtm.getRowCount(); i++) {
            m = m+ 1;
            
           String p_barcode = addNewQuotation.jTable1.getValueAt(i, 1).toString();
          
           if (barcode.equals(p_barcode)) {
                    isFound = true;
                    x = i;
                    break;
                }
           
           
        }
        
         if (isFound) {
              int option = JOptionPane.showConfirmDialog(this, "This prduct is already added. Do you want to update Quantity?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    
                    
                    int oldQty = Integer.parseInt(addNewQuotation.jTable1.getValueAt(x, 7).toString());
                    int finalQty = oldQty + Integer.parseInt(qty);
                    
                    Double new_price = Double.parseDouble(addNewQuotation.jTable1.getValueAt(x, 6).toString()) * finalQty;
                    
                    addNewQuotation.jTable1.setValueAt(String.valueOf(finalQty), x, 7);
                    addNewQuotation.jTable1.setValueAt(String.valueOf(new_price), x, 8);
                    
                }else{
                   
                }
             
         }else{
        
        Vector v = new Vector();
        
        v.add(m);
        v.add(barcode);
        v.add(title);
        v.add(description);
        v.add(model);
        v.add(brand);
        v.add(price);
        v.add(qty);
        
        Double item_total = Double.parseDouble(price)* Integer.parseInt(qty); 
        v.add(item_total);
        
        dtm.addRow(v);
      
        

        }
        addNewQuotation.calculate_Total();
   reset_Fields();
        
        this.dispose();
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

        jLabel13 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jTextField11 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();

        jLabel13.setText("Item");

        jTextField8.setEditable(false);
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Add Product to Quotation");

        jLabel2.setText("Barcode");

        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel3.setText("Product Model");

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Barcode Number");

        jTextField3.setEditable(false);

        jLabel5.setText("Product Title");

        jTextField4.setEditable(false);
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel6.setText("Model Number");

        jTextField5.setEditable(false);

        jLabel7.setText("Brand");

        jTextField6.setEditable(false);
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel8.setText("Available Quantity");

        jTextField7.setEditable(false);

        jLabel9.setText("Description");

        jTextField9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField9KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField9KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField9KeyTyped(evt);
            }
        });

        jLabel10.setText("Quantity");

        jTextField10.setEditable(false);

        jLabel11.setText("Price");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setText("Add to Quotation");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton2KeyReleased(evt);
            }
        });

        jTextField11.setEditable(false);
        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });

        jLabel14.setText("Category");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField9)
                    .addComponent(jTextField10)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(25, 25, 25)
                        .addComponent(jTextField7))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(91, 91, 91)
                        .addComponent(jTextField6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(41, 41, 41)
                        .addComponent(jTextField5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(55, 55, 55)
                        .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(32, 32, 32)
                        .addComponent(jTextField3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(62, 62, 62)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(74, 74, 74)
                        .addComponent(jTextField11))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField11))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField7))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField9)
                .addGap(9, 9, 9)
                .addComponent(jLabel11)
                .addGap(9, 9, 9)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jButton2)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(21, 21, 21))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        addProductQuotationTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        searchProduct();


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jTextField9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyTyped

        String qty = jTextField9.getText();
        check_stock(qty);
        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("[0-9]|[1-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9][0-9][0-9]").matcher(text).matches()) {
            evt.consume();
        }

//        0,1,2,3,4,5,6,7,8,9,   |
//        1,2,3,4,9,0,1-9
//        1-9,0-9,0-9   909
//        1-9,1-9,0-9,,1-9
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9KeyTyped

    private void jTextField9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyReleased
 if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addProductQuotationTable();

        }
        String qty = jTextField9.getText();
        //check_stock(qty);
        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("[0-9]|[1-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9][0-9][0-9]]").matcher(text).matches()) {
            evt.consume();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9KeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchProduct();

        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            jTextField2.grabFocus();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchProduct();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            jTextField1.grabFocus();
        }//table eke colun ehata mehata yaawa

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9KeyPressed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        if(evt.getClickCount()==2){
            jTextField1.setText("");
            jTextField2.setText("");
        }
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseClicked
//        if(evt.getClickCount()==2){
//            jTextField1.setText("");
//            jTextField2.setText("");
//        }
    }//GEN-LAST:event_jTextField2MouseClicked

    private void jButton2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton2KeyReleased
       // TODO add your handling code here:
    }//GEN-LAST:event_jButton2KeyReleased

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
            java.util.logging.Logger.getLogger(AddProductQuotation.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddProductQuotation.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddProductQuotation.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddProductQuotation.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddProductQuotation dialog = new AddProductQuotation(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
