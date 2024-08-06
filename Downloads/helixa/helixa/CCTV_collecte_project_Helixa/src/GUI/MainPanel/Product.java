/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.DialogBox.AddNewProductBrand;
import GUI.DialogBox.AddNewProductCategory;
import GUI.DialogBox.AddSerialNumber;
import GUI.MainFrame.Login;
import GUI.MainFrame.MainFrame;
import Models.Backup;
import Models.MySQL;
import Models.UserManagementBean;
import Models.Validation;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Sandaruwan
 */
public class Product extends javax.swing.JPanel {

    private MainFrame mainFrame;
    HashMap<String, Integer> userStatusMap = new HashMap<>();
    HashMap<String, Integer> brandMap = new HashMap<>();
    HashMap<String, Integer> categoryMap = new HashMap<>();

    HashMap<Integer, String> productIdMap = new HashMap<>();
    String query = "SELECT * FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` INNER JOIN `brand` ON `brand`.`id`=`product`.`brand_id`";

    public Product(MainFrame mainFrame) {
        initComponents();
        this.mainFrame = mainFrame;
        loaduserStatus();
        loadBrandProduct();
        loadCategoryProduct();
        loadTable(query);
        jButton3.setVisible(false);
    }

    private void loadTable(String query) {
        try {
            ResultSet resultSet = MySQL.execute(query);
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            int tableId = 0;
            while (resultSet.next()) {
                tableId = tableId + 1;
                Vector<Object> vector = new Vector<>();
                vector.add(tableId);

                productIdMap.put(tableId, resultSet.getString("product.id"));
                vector.add(resultSet.getString("title"));
                vector.add(resultSet.getString("model_number"));
                vector.add(resultSet.getString("description"));
                vector.add(resultSet.getString("category.name"));
                vector.add(resultSet.getString("brand.name"));

                model.addRow(vector);

            }
        } catch (Exception e) {
                                  Login.log1.warning(e.getMessage());

        }

    }

    private void searchProduct() {
        String productTitle = jTextField1.getText().trim();
        String modelNumber = jTextField4.getText().trim();
        String selectedCategory = (String) jComboBox1.getSelectedItem();
        String selectedBrand = (String) jComboBox4.getSelectedItem();

        StringBuilder queryBuilder = new StringBuilder(
                "SELECT * FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` "
                + "INNER JOIN `brand` ON `brand`.`id`=`product`.`brand_id` WHERE 1=1");

        if (!productTitle.isEmpty()) {
            queryBuilder.append(" AND `product`.`title` LIKE '%").append(productTitle).append("%'");
        }
        if (!modelNumber.isEmpty()) {
            queryBuilder.append(" AND `product`.`model_number` LIKE '%").append(modelNumber).append("%'");
        }
        if (categoryMap.containsKey(selectedCategory) && !selectedCategory.equals("Select")) {
            int categoryId = categoryMap.get(selectedCategory);
            queryBuilder.append(" AND `product`.`category_id` = ").append(categoryId);
        }
        if (brandMap.containsKey(selectedBrand) && !selectedBrand.equals("Select")) {
            int brandId = brandMap.get(selectedBrand);
            queryBuilder.append(" AND `product`.`brand_id` = ").append(brandId);
        }

        loadTable(queryBuilder.toString());
    }

    public void loadCategoryProduct() {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `category`");

            Vector<Object> vector = new Vector<>();
            vector.add("Select");
            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
                categoryMap.put(resultSet.getString("name"), resultSet.getInt("id"));

            }
            DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBox2.getModel();

            model.removeAllElements();
            model.addAll(vector);
            jComboBox2.setSelectedIndex(0);

            DefaultComboBoxModel model1 = (DefaultComboBoxModel) jComboBox1.getModel();

            model1.removeAllElements();
            model1.addAll(vector);
            jComboBox1.setSelectedIndex(0);
        } catch (Exception e) {
                                   Login.log1.warning(e.getMessage());

        }
    }

    public void loadBrandProduct() {
        try {
            ResultSet resultset = MySQL.execute("SELECT * FROM `brand`");

            Vector<Object> vector = new Vector<>();
            vector.add("Select");
            while (resultset.next()) {
                vector.add(resultset.getString("name"));
                brandMap.put(resultset.getString("name"), resultset.getInt("id"));

            }
            DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBox3.getModel();
            model.removeAllElements();
            model.addAll(vector);
            jComboBox3.setSelectedIndex(0);

            DefaultComboBoxModel model1 = (DefaultComboBoxModel) jComboBox4.getModel();
            model1.removeAllElements();
            model1.addAll(vector);
            jComboBox4.setSelectedIndex(0);

        } catch (Exception e) {
                                  Login.log1.warning(e.getMessage());

        }

    }

    private void loaduserStatus() {
        try {
            ResultSet resultset = MySQL.execute("SELECT * FROM `user_status`");

            Vector<String> vector = new Vector<>();

            while (resultset.next()) {
                vector.add(resultset.getString("status"));
                userStatusMap.put(resultset.getString("status"), resultset.getInt("id"));
            }

//            DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) jComboBox1.getModel();
//            comboBoxModel.removeAllElements();
//            comboBoxModel.addAll(vector);
//            jComboBox1.setSelectedIndex(0);
        } catch (Exception e) {
                                  Login.log1.warning(e.getMessage());

        }
    }

    private void reset() {

        jTextField5.setEnabled(true);
        jTextField5.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jTextField4.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextArea1.setText("");
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jLabel3.setText("Add Product");
        jButton1.setText("Add Product");
        loadTable("SELECT * FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` INNER JOIN `brand` ON `brand`.`id`=`product`.`brand_id`");

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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(601, 100));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Products");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1138, 1138, 1138))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setMinimumSize(new java.awt.Dimension(150, 100));
        jPanel2.setPreferredSize(new java.awt.Dimension(225, 231));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Add New Product");

        jLabel4.setText("Product Title");

        jLabel5.setText("Product Model");

        jLabel6.setText("Category");

        jButton1.setText("Add Product");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setText("Brand");

        jLabel9.setText("Description");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton4.setText("+");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("+");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel12.setText("Barcode");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField2)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(87, 87, 87)
                        .addComponent(jButton5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(87, 87, 87)
                        .addComponent(jButton4))
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(44, 44, 44))
        );

        add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel3.setPreferredSize(new java.awt.Dimension(25, 231));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 487, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel4.setPreferredSize(new java.awt.Dimension(601, 35));

        jButton3.setText("Print Barcode");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(1276, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel9.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Product Title");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Product Model");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Category");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox1PropertyChange(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Brand");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jButton2.setText("Reset");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 437, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(12, 12, 12)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "#", "Product Title", "Product Model", "Description", "Category", "Brand"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(30);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(300);
            jTable2.getColumnModel().getColumn(4).setResizable(false);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(30);
            jTable2.getColumnModel().getColumn(5).setResizable(false);
            jTable2.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        jPanel9.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        add(jPanel9, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:

        searchProduct();

//        if (evt.getKeyCode() == 10) {
//            String username = jTextField1.getText();
//            loadTable("SELECT * FROM `user` INNER JOIN `user_type` ON `user`.`user_type_id` = `user_type`.`id` INNER JOIN `user_status` ON  `user_status`.`id`=`user`.`user_status_id` WHERE `type`='Cashier' AND `username` LIKE '" + username + "%'");
//        }

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:

        int selectedRow = jTable2.getSelectedRow();
        if (evt.getClickCount() == 2) {

            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to update the product details?",
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {

                jButton3.setVisible(true);
                jLabel3.setText("Update Product");
                jButton1.setText("Update");

//            String id = String.valueOf(jTable2.getValueAt(selectedRow, 0));
                String title = String.valueOf(jTable2.getValueAt(selectedRow, 1));
                String model = String.valueOf(jTable2.getValueAt(selectedRow, 2));
                String description = String.valueOf(jTable2.getValueAt(selectedRow, 3));
                String category = String.valueOf(jTable2.getValueAt(selectedRow, 4));
                String brand = String.valueOf(jTable2.getValueAt(selectedRow, 5));

                int id = (int) jTable2.getValueAt(jTable2.getSelectedRow(), 0);
//                String productId = String.valueOf(productIdMap.get(id));
                jTextField5.setEnabled(false);
                jTextField5.setText(String.valueOf(productIdMap.get(id)));
                jComboBox2.setSelectedItem(category);
                jComboBox3.setSelectedItem(brand);
                jTextField2.setText(title);
                jTextField3.setText(model);
                jTextArea1.setText(description);

            } else {
                jTable2.clearSelection();
            }

        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int row = jTable2.getSelectedRow();

        String title = jTextField2.getText();
        String model = jTextField3.getText();
        String description = jTextArea1.getText();

        String brand = String.valueOf(jComboBox3.getSelectedItem());
        String category = String.valueOf(jComboBox2.getSelectedItem());

//        int brandId = brandMap.get();
//        int category = brandMap.get(String.valueOf(jComboBox2.getSelectedItem()));
        System.out.println(title);
        System.out.println(model);
        System.out.println(description);
//        System.out.println(brandId);
//        System.out.println(categoryId);

        if (title.isEmpty() || model.isEmpty() || brand.equals("Select") || category.equals("Select") || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Incomplete Form",
                    JOptionPane.WARNING_MESSAGE);

        } else {
            int brandId = brandMap.get(brand);
            int categoryId = categoryMap.get(category);

//            System.out.println(brandId);
//            System.out.println(categoryId);
            if (row != -1) {
                int id = (int) jTable2.getValueAt(jTable2.getSelectedRow(), 0);
                String productId = String.valueOf(productIdMap.get(id));

                try {
                    MySQL.execute("UPDATE `product` SET `title`='" + title + "',`description`='" + description + "',`model_number`='" + model + "',`brand_id`='" + brandId + "',`category_id`='" + categoryId + "' WHERE `id`='" + productId + "'");
                    int option = JOptionPane.showConfirmDialog(
                            this,
                            "The product has been successfully updated in the system. Do you want to print the barcode?",
                            "Update Successful",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (option == JOptionPane.YES_OPTION) {
                        // Code to print the barcode
                        HashMap<String, Object> hashmapBarcode = new HashMap<>();
                        hashmapBarcode.put("barcodeNumber", productId);
                        hashmapBarcode.put("producttitle", title);
                        try {
                            JasperCompileManager.compileReportToFile("src/Report/barcord.jrxml");

                            JasperPrint report = JasperFillManager.fillReport("src/Report/barcord.jasper", hashmapBarcode, new JREmptyDataSource());
                            JasperViewer.viewReport(report, false);

                        } catch (Exception e) {
                                                  Login.log1.warning(e.getMessage());

                        }

                    }

                    loadTable("SELECT * FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` INNER JOIN `brand` ON `brand`.`id`=`product`.`brand_id` INNER JOIN `stock` ON `stock`.`product_id`= `product`.`id` ");
                    reset();

                } catch (Exception ex) {
                                           Login.log1.warning(ex.getMessage());

                    Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

//                String uniqueID = UUID.randomUUID().toString();
                String productBarcodeId = "";
//                String uniqueID = 
                if (jTextField5.getText().isEmpty()) {
                    productBarcodeId = String.valueOf(System.currentTimeMillis());
                } else {

                    productBarcodeId = jTextField5.getText();

                }

                try {
                    ResultSet resultSet = MySQL.execute("SELECT * FROM `product` WHERE `id`='" + productBarcodeId + "' ");

                    if (resultSet.next()) {

                        JOptionPane.showMessageDialog(this,
                                "This product is already registered in the system.",
                                "Duplicate Product Warning",
                                JOptionPane.WARNING_MESSAGE);
                    } else {

                        MySQL.execute("INSERT INTO `product` VALUES ('" + productBarcodeId + "','" + categoryId + "','" + brandId + "','" + model + "','" + title + "','" + description + "')");

                        int option = JOptionPane.showConfirmDialog(
                                this,
                                "The product has been successfully inserted into the system. Do you want to print the barcode?",
                                "Update Successful",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );

                        if (option == JOptionPane.YES_OPTION) {

                            HashMap<String, Object> hashmapBarcode = new HashMap<>();
                            hashmapBarcode.put("barcodeNumber", productBarcodeId);
                            hashmapBarcode.put("producttitle", title);
                            try {
                                JasperCompileManager.compileReportToFile("src/Report/barcord.jrxml");

                                JasperPrint report = JasperFillManager.fillReport("src/Report/barcord.jasper", hashmapBarcode, new JREmptyDataSource());
                                JasperViewer.viewReport(report, false);

                            } catch (Exception e) {
                                                       Login.log1.warning(e.getMessage());

                            }

                        }

                        loadTable("SELECT * FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` INNER JOIN `brand` ON `brand`.`id`=`product`.`brand_id` INNER JOIN stock ON stock.product_id=product.id");
                        reset();

                    }
                } catch (Exception ex) {
                                           Login.log1.warning(ex.getMessage());

                    Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
        searchProduct();
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jComboBox1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox1PropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBox1PropertyChange

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        searchProduct();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // TODO add your handling code here:
        searchProduct();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        boolean product = true;
        AddNewProductCategory addNewProductCategory = new AddNewProductCategory(parentFrame, true, this, product);
        addNewProductCategory.setVisible(true);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        boolean product = true;
        AddNewProductBrand addNewProductBrand = new AddNewProductBrand(parentFrame, true, this, product);
        addNewProductBrand.setVisible(true);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow != -1) {

            String barcode = jTextField5.getText();
            String title = jTextField2.getText();

            HashMap<String, Object> hashmapBarcode = new HashMap<>();
            hashmapBarcode.put("barcodeNumber", barcode);
            hashmapBarcode.put("producttitle", title);
            try {
                JasperCompileManager.compileReportToFile("src/Report/barcord.jrxml");

                JasperPrint report = JasperFillManager.fillReport("src/Report/barcord.jasper", hashmapBarcode, new JREmptyDataSource());
                JasperViewer.viewReport(report, false);

            } catch (Exception e) {
                                      Login.log1.warning(e.getMessage());

            }

        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a product from the list.",
                    "Product Selection Required",
                    JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
