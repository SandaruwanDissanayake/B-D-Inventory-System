/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

import GUI.DialogBox.LoadSerialForStockManagement;
import GUI.DialogBox.loadWarrantyForStockManagement;
import GUI.MainFrame.Login;
import Models.MySQL;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.JTextField;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sandaruwan
 */
public class StockManagement extends javax.swing.JPanel {

    private final Vector<String> v = new Vector<>();
    private JTextField tf;
    private boolean hide_flag = false;
//    private HashMap<String, String> categoryMap = new HashMap<>();
//    private HashMap<String, String> brandMap = new HashMap<>();
    private final HashMap<String, ArrayList<String>> catNbrand = new HashMap<>();
    private HashMap<String, String> handleStockid = new HashMap<>();
    private HashMap<String, String> handleproductid = new HashMap<>();

    DefaultComboBoxModel dm;

    public StockManagement() {
        initComponents();
        loadMainTable();
        loadCategory();
        loadBrands();
        dm = (DefaultComboBoxModel) jComboBox2.getModel();
        loadModels();
        autoSuggest();
    }

    private void reset() {
        jTextField1.setText("");
        tf.setText("");
        jComboBox1.setSelectedIndex(0);
//        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        loadBrands();
        jCheckBox1.setSelected(false);
        jLabel3.setText("Product Title");
    }

    private void loadMainTable() {
        String query = "";
        if (jCheckBox1.isSelected() && !jTextField1.getText().isEmpty()) {
            query = "SELECT * FROM `product_sn` INNER JOIN"
                    + " `product` ON `product`.`id` = `product_sn`.`product_id` INNER JOIN"
                    + " `stock` ON `stock`.`product_id` = `product`.`id` INNER JOIN"
                    + " `category` ON `category`.`id` = `product`.`category_id` INNER JOIN"
                    + " `brand` ON `brand`.`id` = `product`.`brand_id` WHERE"
                    + " `stock`.`id` = (SELECT `stock_id` FROM `grn_item` WHERE `id` = (SELECT `grn_item_id` FROM `product_sn` WHERE `serial_number` = '" + jTextField1.getText() + "')) AND"
                    + " `product_sn`.`serial_number` = '" + jTextField1.getText() + "';";
        } else {
            query = "SELECT * FROM `stock` "
                    + "INNER JOIN `product` ON `stock`.`product_id` = `product`.`id` "
                    + "INNER JOIN `category` ON `category`.`id` = `product`.`category_id` "
                    + "INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id`";

            boolean status = false;
            if (!jTextField1.getText().isEmpty()) {
                query += " WHERE `product`.`title` LIKE '%" + jTextField1.getText() + "%'";
                status = true;
            }

            tf = (JTextField) combo.getEditor().getEditorComponent();

            if (!tf.getText().isEmpty()) {
                if (status) {
                    query += " AND `product`.`model_number` = '" + tf.getText() + "'";

                } else {
                    query += " WHERE `product`.`model_number` = '" + tf.getText() + "'";
                    status = true;
                }
            }

            if (jComboBox1.getSelectedIndex() != 0) {
                if (status) {
                    query += " AND `category`.`name` = '" + jComboBox1.getSelectedItem() + "'";

                } else {
                    query += " WHERE `category`.`name` = '" + jComboBox1.getSelectedItem() + "'";
                    status = true;
                }
            }
            if (jComboBox2.getSelectedIndex() != 0) {
                if (status) {
                    query += " AND `brand`.`name` = '" + jComboBox2.getSelectedItem() + "'";

                } else {
                    query += " WHERE `brand`.`name` = '" + jComboBox2.getSelectedItem() + "'";
                    status = true;
                }
            }
            if (!jComboBox3.getSelectedItem().equals("Select")) {
//            System.out.println("awaaa");
                if (jComboBox3.getSelectedItem().equals("Lower than 10")) {
//                System.out.println("Lower than 10");
                    if (status) {
                        query += " AND `stock`.`qty` < '10'";
                    } else {
                        query += " WHERE `stock`.`qty` < '10'";
                        status = true;
                    }
                } else if (jComboBox3.getSelectedItem().equals("Out of stock")) {
                    if (status) {
                        query += " AND `stock`.`qty` = '0'";
                    } else {
                        query += " WHERE `stock`.`qty` = '0'";
                        status = true;
                    }
                }

            }
        }

        try {
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);
            ResultSet rs = MySQL.execute(query);
            int index = 1;
            while (rs.next()) {
                handleStockid.put(String.valueOf(index), rs.getString("stock.id"));
                handleproductid.put(String.valueOf(index), rs.getString("product.id"));
                Vector<String> v = new Vector<>();
                v.add(String.valueOf(index));
                v.add(rs.getString("product.title"));
                v.add(rs.getString("product.description"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("product.model_number"));
                v.add(rs.getString("stock.qty"));

                ResultSet subrs = MySQL.execute("SELECT DISTINCT `buying_price` FROM `grn_item` "
                        + "WHERE `stock_id` = '" + rs.getString("stock.id") + "'");
                while (subrs.next()) {
                    v.add(subrs.getString("buying_price"));
                }

                v.add(rs.getString("stock.shop_price"));
                v.add(rs.getString("stock.daily_cutomer_price"));
                v.add(rs.getString("stock.new_customer_price"));
//                System.out.println(rs.getString("stock.new_customer_price"));

                model.addRow(v);
                index++;
//                System.out.println(rs.getString("category.name"));
                if (catNbrand.containsKey(rs.getString("category.name"))) {
                    boolean b = true;
                    for (String string : catNbrand.get(rs.getString("category.name"))) {
////                        
                        if (rs.getString("brand.name").equals(string)) {
                            b = false;
                            break;
                        }

//                         System.out.println(string);
                    }
                    if (b) {
                        catNbrand.get(rs.getString("category.name")).add(rs.getString("brand.name"));
                    }
//                                       
//                       System.out.println("ok");
                } else {
                    ArrayList<String> mod = new ArrayList<>();
                    mod.add(rs.getString("brand.name"));
                    catNbrand.put(rs.getString("category.name"), mod);

                }

            }
//            for (String string : catNbrand.keySet()) {
//                System.out.println(string);
//            }
//            for (Map.Entry m : catNbrand.entrySet()) {
//                System.out.println(m.getKey() + " " + m.getValue());
//            }
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }

    }

    private void loadCategory() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `category`");
            Vector<String> v = new Vector<>();
            v.add("Select a category");
            while (rs.next()) {
                v.add(rs.getString("name"));
//                categoryMap.put(rs.getString("name"), rs.getString("id"));
            }
            DefaultComboBoxModel m = new DefaultComboBoxModel(v);
            jComboBox1.setModel(m);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }

    private void loadBrands() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `brand`");
            Vector<String> v = new Vector<>();
            v.add("Select a brand");
            while (rs.next()) {
                v.add(rs.getString("name"));
//                brandMap.put(rs.getString("name"), rs.getString("id"));
            }
            DefaultComboBoxModel m = new DefaultComboBoxModel(v);
            jComboBox2.setModel(m);
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }

    private void loadModels() {
        try {
            ResultSet rs = MySQL.execute("SELECT DISTINCT(`model_number`) FROM `product`");
            while (rs.next()) {
                v.addElement(rs.getString("model_number"));
            }
        } catch (Exception e) {
            Login.log1.warning(e.getMessage());

        }
    }

    private void setModel(DefaultComboBoxModel mdl, String str) {
        combo.setModel(mdl);
        combo.setSelectedIndex(-1);
        tf.setText(str);
    }

    private DefaultComboBoxModel getSuggestedModel(List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : list) {
            if (s.startsWith(text)) {
                m.addElement(s);
            }
        }
        return m;
    }

    private void autoSuggest() {
        combo.setEditable(true);
        tf = (JTextField) combo.getEditor().getEditorComponent();
        tf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        String text = tf.getText();
                        if (text.length() == 0) {
                            combo.hidePopup();
                            setModel(new DefaultComboBoxModel(v), text);
                        } else {
                            DefaultComboBoxModel m = getSuggestedModel(v, text);
                            if (m.getSize() == 0 || hide_flag) {
                                combo.hidePopup();
                                hide_flag = false;
                            } else {
                                setModel(m, text);
                                combo.showPopup();
                            }
                        }
                    }

                });
            }

            @Override
            public void keyPressed(KeyEvent e) {

                String text = tf.getText();
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_ENTER) {
                    if (v.contains(text)) {
                        Collections.sort(v);
                        setModel(getSuggestedModel(v, text), text);
                    }
                    hide_flag = true;

                    loadMainTable();

                } else if (code == KeyEvent.VK_ESCAPE) {
                    hide_flag = true;
                } else if (code == KeyEvent.VK_RIGHT) {
                    for (int i = 0; i < v.size(); i++) {
                        String str = v.elementAt(i);
                        if (str.startsWith(text)) {
                            combo.setSelectedIndex(-1);
                            if (!text.isEmpty()) {
                                tf.setText(str);
                            }
                            return;
                        }
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
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
        jButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        combo = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

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
            .addGap(0, 940, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel5);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Stock Management");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 654, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(20, 20, 20))
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
        jLabel2.setText("Product Model");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Product Title");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Brand");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Category");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Available Quantity");

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(114, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jCheckBox1))
                .addContainerGap())
        );

        jPanel2.add(jPanel7);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jButton1.setText("Search");
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

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Lower than 10", "Out of stock" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActionPerformed(evt);
            }
        });
        combo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboKeyPressed(evt);
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
                .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
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
            .addGap(0, 940, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Product Title", "Description", "Category", "Model", "Available Quantity", "Buying Price", "Price (Shop)", "Price (Special)", "Price (Setup)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(3).setResizable(false);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(4).setResizable(false);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(50);
            jTable2.getColumnModel().getColumn(5).setResizable(false);
            jTable2.getColumnModel().getColumn(6).setResizable(false);
            jTable2.getColumnModel().getColumn(7).setResizable(false);
            jTable2.getColumnModel().getColumn(8).setResizable(false);
            jTable2.getColumnModel().getColumn(9).setResizable(false);
        }

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadMainTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        loadMainTable();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        loadMainTable();

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (jComboBox1.getSelectedIndex() != 0) {

            Vector<String> v = new Vector<>();

            if (catNbrand.get(String.valueOf(jComboBox1.getSelectedItem())) != null) {
                for (String string : catNbrand.get(String.valueOf(jComboBox1.getSelectedItem()))) {

                    v.add(string);
                }

                DefaultComboBoxModel m = new DefaultComboBoxModel(v);
                jComboBox2.setModel(m);

            } else {
                jComboBox2.setModel(dm);
            }

        }
        loadMainTable();

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboActionPerformed

    }//GEN-LAST:event_comboActionPerformed

    private void comboKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboKeyPressed

    }//GEN-LAST:event_comboKeyPressed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        loadMainTable();
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        reset();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped

    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed

    }//GEN-LAST:event_jTextField1KeyPressed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            reset();
            jLabel3.setText("Serial Number");
            jCheckBox1.setSelected(true);
        } else {
            jLabel3.setText("Product Title");
            reset();
        }

    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        if (evt.getClickCount() == 2 && jTable2.getSelectedRow() != -1) {
            int row = jTable2.getSelectedRow();
            Vector<String> v = new Vector<>();
            try {

                ResultSet result = MySQL.execute("SELECT `id` FROM `grn_item` WHERE `stock_id` = '" + handleStockid.get(String.valueOf(jTable2.getValueAt(row, 0))) + "'");
                while (result.next()) {
                    try {
//            System.out.println(addptv.getJTextField72().getText());  

                        ResultSet rs = MySQL.execute("SELECT `serial_number` FROM `product_sn` WHERE `product_id` = '" + handleproductid.get(String.valueOf(jTable2.getValueAt(row, 0))) + "' AND `grn_item_id` = '" + result.getString("id") + "' AND `product_sn_status_id` = '1'");

                        while (rs.next()) {
                            v.add(rs.getString("serial_number"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Login.log1.warning(e.getMessage());

            }
            if (!v.isEmpty()) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                LoadSerialForStockManagement sm = new LoadSerialForStockManagement(parentFrame, true, v);
                sm.setVisible(true);
            }
        } else if (evt.getClickCount() == 1 && evt.getButton() == 3 && jTable2.getSelectedRow() != -1) {
            int row = jTable2.getSelectedRow();
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `warranty` WHERE `stock_id` = '" + handleStockid.get(String.valueOf(jTable2.getValueAt(row, 0))) + "'");

                if (rs.next()) {
                    HashMap<String, String> handlewarranty = new LinkedHashMap<>();
                    if (!rs.getString("sixMonth").equals("0.0")) {

                        if (rs.getString("sixMonth").equals("1.0") && rs.getString("warranty_status_id").equals("1")) {
                            handlewarranty.put("6 months", "0.0");
                        } else {
                            handlewarranty.put("6 months", rs.getString("sixMonth"));
                        }
                    }
                    if (!rs.getString("oneyear").equals("0.0")) {

                        if (rs.getString("oneyear").equals("1.0") && rs.getString("warranty_status_id").equals("1")) {
                            handlewarranty.put("1 year", "0.0");
                        } else {
                            handlewarranty.put("1 year", rs.getString("oneyear"));
                        }
                    }
                    if (!rs.getString("twoyear").equals("0.0")) {

                        if (rs.getString("twoyear").equals("1.0") && rs.getString("warranty_status_id").equals("1")) {
                            handlewarranty.put("2 years", "0.0");
                        } else {
                            handlewarranty.put("2 years", rs.getString("twoyear"));
                        }
                    }
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    loadWarrantyForStockManagement lw = new loadWarrantyForStockManagement(parentFrame, true, handlewarranty);
                    lw.setVisible(true);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
