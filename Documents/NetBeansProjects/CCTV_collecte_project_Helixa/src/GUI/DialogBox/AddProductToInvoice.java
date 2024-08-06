/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.DialogBox;

import GUI.MainFrame.Login;
import GUI.MainPanel.Invoice;
import Models.InvoiceBean;
import Models.MySQL;
import com.mysql.cj.xdevapi.Result;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CNN COMPUTERS
 */
public class AddProductToInvoice extends javax.swing.JDialog {

//    public HashMap<String, String> periodmap = new LinkedHashMap<>();
//    public HashMap<String, String> warrantyprice = new HashMap<>();
    public HashMap<String, String> periodmap = new LinkedHashMap<>();

    public String getStockId() {
        return stockId;
    }

    /**
     * @param stockId the stockId to set
     */
    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    /**
     * @return the sqty
     */
    public int getSqty() {
        return sqty;
    }

    /**
     * @param sqty the sqty to set
     */
    public void setSqty(int sqty) {
        this.sqty = sqty;
    }

    /**
     * @return the shopPrice
     */
    public String getShopPrice() {
        return shopPrice;
    }

    /**
     * @param shopPrice the shopPrice to set
     */
    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    /**
     * @return the specialPrice
     */
    public String getSpecialPrice() {
        return specialPrice;
    }

    /**
     * @param specialPrice the specialPrice to set
     */
    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    /**
     * @return the setupPrice
     */
    public String getSetupPrice() {
        return setupPrice;
    }

    /**
     * @param setupPrice the setupPrice to set
     */
    public void setSetupPrice(String setupPrice) {
        this.setupPrice = setupPrice;
    }

    public JTextField getJTextField72() {
        return jTextField72;
    }

    public JTextField getJTextField11() {
        return jTextField11;
    }

    private String stockId;
    private int sqty;
    private String shopPrice;
    private String specialPrice;
    private String setupPrice;
    private boolean accessA = true;
    private boolean accessB = true;
    private boolean accessC = true;
    private String usertype;
    public Invoice inv;
    private HashMap<String, String> lookwp = new LinkedHashMap<>();

    public AddProductToInvoice(java.awt.Frame parent, boolean modal, String usertype, Invoice inv) {
        super(parent, modal);
        initComponents();
        this.usertype = usertype;
        jButton16.setEnabled(false);
        this.inv = inv;
        jTextField72.grabFocus();
        jCheckBox1.setSelected(false);
        jCheckBox1.setEnabled(false);
        loadWarrantyPeriod();
    }

    private void loadWarrantyPeriod() {
        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `warranty_period`");
            while (rs.next()) {

                lookwp.put(rs.getString("id"), rs.getString("period"));
            }
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel15 = new javax.swing.JPanel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jTextField72 = new javax.swing.JTextField();
        jTextField73 = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        jTextField74 = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        jTextField75 = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        jTextField76 = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jTextField77 = new javax.swing.JTextField();
        jLabel93 = new javax.swing.JLabel();
        jTextField78 = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        jTextField79 = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        jTextField80 = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea8 = new javax.swing.JTextArea();
        jLabel97 = new javax.swing.JLabel();
        jTextField81 = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel98 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel15KeyPressed(evt);
            }
        });

        jLabel86.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel86.setText("Add Product to Invoice");

        jLabel87.setText("Barcode");

        jTextField72.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField72CaretUpdate(evt);
            }
        });
        jTextField72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField72ActionPerformed(evt);
            }
        });
        jTextField72.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField72KeyPressed(evt);
            }
        });

        jTextField73.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField73CaretUpdate(evt);
            }
        });
        jTextField73.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField73KeyPressed(evt);
            }
        });

        jLabel88.setText("Product Model");

        jButton15.setText("Search");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel89.setText("Barcode Number");

        jLabel90.setText("Serial Number");

        jTextField75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField75ActionPerformed(evt);
            }
        });

        jLabel91.setText("Product Title");

        jLabel92.setText("Model Number");

        jTextField77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField77ActionPerformed(evt);
            }
        });

        jLabel93.setText("Category");

        jTextField78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField78ActionPerformed(evt);
            }
        });

        jLabel94.setText("Description");

        jTextField79.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField79CaretUpdate(evt);
            }
        });
        jTextField79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField79ActionPerformed(evt);
            }
        });
        jTextField79.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField79KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField79KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField79KeyTyped(evt);
            }
        });

        jLabel95.setText("Quantity");

        jTextField80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField80ActionPerformed(evt);
            }
        });

        jLabel96.setText("Price");

        jTextArea8.setColumns(20);
        jTextArea8.setRows(5);
        jScrollPane8.setViewportView(jTextArea8);

        jLabel97.setText("Brand");

        jTextField81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField81ActionPerformed(evt);
            }
        });

        jButton16.setText("Add to Invoice");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton1.setText("Confirm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Warranty");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jCheckBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCheckBox1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField80)
                            .addComponent(jTextField79)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel94, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel93, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel92, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel90, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel95, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField74)
                                    .addComponent(jTextField75)
                                    .addComponent(jTextField76)
                                    .addComponent(jTextField77)
                                    .addComponent(jTextField81)
                                    .addComponent(jTextField78)
                                    .addComponent(jScrollPane8)))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox1)
                                    .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(18, 18, 18))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(jTextField74, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(jTextField75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel91)
                    .addComponent(jTextField76, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92)
                    .addComponent(jTextField77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(jTextField81))
                .addGap(14, 14, 14)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel93)
                    .addComponent(jTextField78))
                .addGap(14, 14, 14)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel94))
                .addGap(18, 18, 18)
                .addComponent(jLabel95)
                .addGap(14, 14, 14)
                .addComponent(jTextField79, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel96)
                .addGap(14, 14, 14)
                .addComponent(jTextField80, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton16)
                    .addComponent(jButton1))
                .addGap(27, 27, 27))
        );

        jLabel98.setText("Serial Number");

        jTextField11.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField11CaretUpdate(evt);
            }
        });
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField11KeyPressed(evt);
            }
        });

        jButton17.setText("Reset");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel87)
                            .addComponent(jTextField72, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel98, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField73, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel86))
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(jButton17))
                .addGap(16, 16, 16)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel87)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel88)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField73, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton15)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel98)
                        .addGap(28, 28, 28)))
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField75ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField75ActionPerformed

    private void jTextField77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField77ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField77ActionPerformed

    private void jTextField79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField79ActionPerformed

    }//GEN-LAST:event_jTextField79ActionPerformed

    private void jTextField81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField81ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField81ActionPerformed
//    private int mapindex = 1;
    private String warrantyPrice;
    private String WarrantyDuration;

    private void addToInvoiceDuty() {
        if (!jTextField79.getText().isEmpty() && Integer.parseInt(jTextField79.getText()) != 0) {
            boolean b = true;
            int mapindex = 0;
            if (inv.map.isEmpty()) {
                mapindex = 0;
            } else {
                for (Map.Entry<Integer, InvoiceBean> me : inv.map.entrySet()) {

                    if (me.getValue().getStockId().equals(getStockId())) {  //jTextField74.getText().equals(me.getValue().getBc()) && jTextField77.getText().equals(me.getValue().getModel()) && jTextField81.getText().equals(me.getValue().getBrand())&&
                        if (!jTextField75.getText().isEmpty() && jTextField75.getText().equals(me.getValue().getSn())) {
                            b = false;
                            break;
                        } else if (jTextField75.getText().isEmpty()) {
                            b = false;
                            break;
                        } else if (!jTextField75.getText().isEmpty() && getWarrantyPrice() != null && me.getValue().getWarrantyprc() == null) {
                            JOptionPane.showMessageDialog(this, "Sorry! You have already added products in same stock without warranty.", "Warning", JOptionPane.WARNING_MESSAGE);
                            b = false;
                            break;
                        } else if (!jTextField75.getText().isEmpty() && getWarrantyPrice() == null && me.getValue().getWarrantyprc() != null) {
                            JOptionPane.showMessageDialog(this, "Sorry! Add the warranty", "Warning", JOptionPane.WARNING_MESSAGE);
                            b = false;
                            break;
                        } else if (!jTextField75.getText().isEmpty() && getWarrantyPrice() != null && me.getValue().getWarrantyprc() != null && !getWarrantyPrice().equals(me.getValue().getWarrantyprc()) && getWarrantyDuration() != null && me.getValue().getWaperiod() != null && !getWarrantyDuration().equals(me.getValue().getWaperiod())) {
                            JOptionPane.showMessageDialog(this, "Sorry! different Warranties", "Warning", JOptionPane.WARNING_MESSAGE);
                            b = false;
                            break;
                        }
                    }

                    mapindex = me.getKey();
                }
            }
            if (b) {
                mapindex++;
                InvoiceBean ib = new InvoiceBean();
                ib.setTitle(jTextField76.getText());
                ib.setSn(jTextField75.getText());
                ib.setBc(jTextField74.getText());
                ib.setModel(jTextField77.getText());
                ib.setBrand(jTextField81.getText());
                ib.setPrice(jTextField80.getText());
                ib.setQty(jTextField79.getText());
                ib.setStockqty(getSqty());
                ib.setStockId(getStockId());
                ib.setWaperiod(getWarrantyDuration());
                ib.setWarrantyprc(getWarrantyPrice());

                inv.map.put(mapindex, ib);

                inv.loadinvtable();
                inv.checkSerial.add(jTextField75.getText());

            }
            reset();
            jTextField72.grabFocus();
        }
    }
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        addToInvoiceDuty();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jTextField80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField80ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField80ActionPerformed

    private void jTextField78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField78ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField78ActionPerformed

    private void searchButtonDuty() {
        String barcode = jTextField72.getText();
        String model = jTextField73.getText();
        String serial = jTextField11.getText();

        if (!barcode.isEmpty() && !model.isEmpty()) {
            jTextField74.setText(barcode);
            int qtyStatus = 0;
            if (!serial.isEmpty()) {
                qtyStatus = 1;
                jTextField75.setText(serial);

                if (getShopPrice() == null) {
//                    System.out.println("ull");
                    try {
                        ResultSet rs = MySQL.execute("SELECT `stock_id` FROM `grn_item` WHERE `grn_id` = (SELECT `grn_id` FROM `product_sn` WHERE `serial_number` = '" + serial + "')");
                        while (rs.next()) {
                            try {
                                ResultSet result = MySQL.execute("SELECT * FROM `stock` WHERE `id` = '" + rs.getString("stock_id") + "' AND `product_id` = '" + barcode + "'");
                                if (result.next()) {
                                    setStockId(result.getString("id"));
                                    setShopPrice(result.getString("shop_price"));
                                    setSpecialPrice(result.getString("daily_cutomer_price"));
                                    setSetupPrice(result.getString("new_customer_price"));
                                    setSqty(result.getInt("qty"));
                                }
                            } catch (Exception e) {
                                Login.log1.warning(e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        Login.log1.warning(e.getMessage());
                    }
                }

            }
            try {

                boolean hasresult = false;
                ResultSet rs = MySQL.execute("SELECT * FROM `warranty` WHERE `stock_id` = '" + getStockId() + "'");
                ResultSet rs2 = null;
                if (rs.next()) {
                    String wduration = "";
                    boolean check1 = false;
                    if (!rs.getString("sixMonth").equals("0.0")) {
                        periodmap.put(lookwp.get("2"), rs.getString("sixMonth"));
                        if (rs.getString("sixMonth").equals("1.0") && rs.getString("warranty_status_id").equals("1")) {
                            check1 = true;
                            wduration = lookwp.get("2");
                        }
                    }
                    if (!rs.getString("oneyear").equals("0.0")) {
                        periodmap.put(lookwp.get("3"), rs.getString("oneyear"));
                        if (rs.getString("oneyear").equals("1.0") && rs.getString("warranty_status_id").equals("1")) {
                            check1 = true;
                            wduration = lookwp.get("3");
                        }
                    }
                    if (!rs.getString("twoyear").equals("0.0")) {
                        periodmap.put(lookwp.get("4"), rs.getString("twoyear"));
                        if (rs.getString("twoyear").equals("1.0") && rs.getString("warranty_status_id").equals("1")) {
                            check1 = true;
                            wduration = lookwp.get("4");
                        }
                    }
//                    hasresult = true;
//                    rs2 = rs;
//                   jCheckBox1.setSelected(true);
                    jCheckBox1.setEnabled(true);
//                    System.out.println(rs.getString("oneyear"));
//                    System.out.println(rs.getString("sixMonth"));
                    if (check1) {
                        jCheckBox1.setSelected(true);
                        jCheckBox1.setEnabled(false);
                        setWarrantyDuration(wduration);
                        setWarrantyPrice("0.0");
                    }
                } else {
                    jCheckBox1.setSelected(false);
                    jCheckBox1.setEnabled(false);

                }
//                if (hasresult) {
////                    periodmap.put(rs2.getString("warranty_period.period"), rs2.getString("warranty_period.id"));
////                    warrantyprice.put(rs2.getString("warranty_period.period"), rs2.getString("warranty.warranty_price"));
////                    while (rs2.next()) {
////                        periodmap.put(rs.getString("warranty_period.period"), rs.getString("warranty_period.id"));
////                        warrantyprice.put(rs.getString("warranty_period.period"), rs.getString("warranty.warranty_price"));
////                    }
//                    if (!rs2.getString("sixMonth").equals("0.0")) {
//                        periodmap.put(lookwp.get("2"), rs2.getString("sixMonth"));
//                    }
//                    if (!rs2.getString("oneyear").equals("0.0")) {
//                        periodmap.put(lookwp.get("3"), rs2.getString("oneyear"));
//                    }
//                    if (!rs2.getString("twoyear").equals("0.0")) {
//                        periodmap.put(lookwp.get("4"), rs2.getString("twoyear"));
//                    }
//                    while (rs2.next()) {
//                        if (!rs.getString("sixMonth").equals("0.0")) {
//                            periodmap.put(lookwp.get("2"), rs.getString("sixMonth"));
//                        }
//                        if (!rs.getString("oneyear").equals("0.0")) {
//                            periodmap.put(lookwp.get("3"), rs.getString("oneyear"));
//                        }
//                        if (!rs.getString("twoyear").equals("0.0")) {
//                            periodmap.put(lookwp.get("4"), rs.getString("twoyear"));
//                        }
//                    }
//                }
            } catch (Exception e) {
                Login.log1.warning(e.getMessage());
            }
            jTextField74.setEditable(false);
            jTextField75.setEditable(false);
            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `product` INNER JOIN `category` ON `category`.`id` = `product`.`category_id` INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` WHERE `product`.`id` = '" + barcode + "'");
                while (rs.next()) {
                    jTextField76.setText(rs.getString("title"));
                    jTextField76.setEditable(false);
                    jTextField77.setText(model);
                    jTextField77.setEditable(false);
                    jTextField81.setText(rs.getString("brand.name"));
                    jTextField81.setEditable(false);
                    jTextField78.setText(rs.getString("category.name"));
                    jTextField78.setEditable(false);
                    jTextArea8.setText(rs.getString("description"));
                    jTextArea8.setEditable(false);
                    if (qtyStatus == 1) {
                        jTextField79.setText("1");
                        jTextField79.setEditable(false);
                    } else if (qtyStatus == 0) {
                        jTextField79.setEditable(true);
                        jTextField79.setText("");
                    }

                    if (usertype.equals("Shop")) {
                        jTextField80.setText(getShopPrice());
                    } else if (usertype.equals("Daily Customer")) {
                        jTextField80.setText(getSpecialPrice());
                    } else if (usertype.equals("New Customer")) {
                        jTextField80.setText(getSetupPrice());
                    }
                    jTextField80.setEditable(false);
                }
            } catch (Exception e) {
                Login.log1.warning(e.getMessage());
            }
            jButton16.setEnabled(true);

            jTextField79.grabFocus();

        } else {
            reset();
        }
    }

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        if (jButton15.isEnabled()) {
            searchButtonDuty();
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jTextField72CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField72CaretUpdate

        jTextField11.setEditable(false);
        jTextField73.setEditable(false);
        if (jTextField72.getText().length() > 5) {
            if (accessA) {
                accessB = false;
                accessC = false;
//                jTextField11.setText("");
//                jTextField73.setText("");
                if (jTextField72.getText().matches("^\\d+$")) {
//                    jTextField72.setEditable(false);
                    try {
                        Vector<HashMap> v = new Vector<>();
                        ResultSet rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN  `product` ON `product`.`id` = `stock`.`product_id` WHERE `stock`.`product_id` = '" + jTextField72.getText() + "' AND `stock`.`qty` > '0'  ");
                        while (rs.next()) {
                            HashMap<String, String> hashmap = new HashMap<>();
                            hashmap.put("stockid", rs.getString("stock.id"));
                            hashmap.put("model", rs.getString("model_number"));
                            v.add(hashmap);
                        }
//                        ResultSet rs = MySQL.execute("SELECT COUNT(*) AS `sum_id`,`product`.`model_number`,`stock`.`id` AS `stock_id` FROM `stock` INNER JOIN"
//                                + " `product` ON `product`.`id` = `stock`.`product_id` WHERE"
//                                + " `stock`.`product_id` = '" + jTextField72.getText() + "'");
                        if (!v.isEmpty()) {
                            jTextField73.setText(String.valueOf(v.get(0).get("model")));

                            if (v.size() >= 1) {
                                accessA = false;
                                jTextField72.setEditable(false);
                                int status = 0;
                                try {
                                    ResultSet result = MySQL.execute("SELECT * FROM `product_sn` WHERE `product_id` = '" + jTextField72.getText() + "' AND `product_sn_status_id` = '1'");
                                    if (result.next()) {
                                        status = 1;
                                    }
                                } catch (Exception e) {
                                    Login.log1.warning(e.getMessage());
                                }
                                SelectOneFromStock sofs = new SelectOneFromStock(this, true, jTextField72.getText(), this, status);
                                if (v.size() > 1) {

                                    sofs.setVisible(true);
                                } else {

                                    if (getJTextField11().getText().isEmpty() && status == 1) {
                                        SelectSerial serial = new SelectSerial(this, true, this, String.valueOf(v.get(0).get("stockid")));
                                        serial.setVisible(true);
                                    }
                                }
                            }
//                            
                        }

                    } catch (Exception e) {
                        Login.log1.warning(e.getMessage());
                    }

//            
                }
            }

        }


    }//GEN-LAST:event_jTextField72CaretUpdate

    private void jTextField72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField72ActionPerformed

    }//GEN-LAST:event_jTextField72ActionPerformed


    private void jTextField11CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField11CaretUpdate

        jTextField72.setEditable(false);
        jTextField73.setEditable(false);
        if (jTextField11.getText().length() > 5) {
            if (accessB) {
                accessA = false;
                accessC = false;
//                jTextField72.setText("");
//                jTextField73.setText("");
                try {

                    ResultSet rs = MySQL.execute("SELECT * FROM `product` WHERE `id` = (SELECT `product_id` FROM `product_sn` WHERE `serial_number` = '" + jTextField11.getText() + "' AND `product_sn_status_id` = '1')");
                    if (rs.next()) {
                        jTextField72.setText(rs.getString("id"));
                        jTextField73.setText(rs.getString("model_number"));
//                        jTextField11.setEditable(false);
//                    jTextField72.setEnabled(false);
//                    jTextField73.setEnabled(false);
//                    jTextField11.setEnabled(false);

                    }

                } catch (Exception e) {
                    Login.log1.warning(e.getMessage());
                }
            }
        }


    }//GEN-LAST:event_jTextField11CaretUpdate
    public void reset() {
        jTextField72.setText("");
        jTextField11.setText("");
        jTextField73.setText("");
        jTextField72.setEditable(true);
        jTextField11.setEditable(true);
        jTextField73.setEditable(true);
        accessA = true;
        accessB = true;
        accessC = true;
        setSetupPrice(null);
        setShopPrice(null);
        setSpecialPrice(null);
        setSqty(0);
        setStockId(null);
        setWarrantyDuration(null);
        setWarrantyPrice(null);
        jTextField74.setText("");
        jTextField74.setEditable(true);
        jTextField75.setText("");
        jTextField75.setEditable(true);
        jTextField76.setText("");
        jTextField76.setEditable(true);
        jTextField77.setText("");
        jTextField77.setEditable(true);
        jTextField81.setText("");
        jTextField81.setEditable(true);
        jTextField78.setText("");
        jTextField78.setEditable(true);
        jTextArea8.setText("");
        jTextArea8.setEditable(true);
        jTextField79.setText("");
        jTextField79.setEditable(true);
        jTextField80.setText("");
        jTextField80.setEditable(true);
        jButton16.setEnabled(false);
        jCheckBox1.setSelected(false);
        jCheckBox1.setEnabled(false);
        jButton15.setEnabled(true);
        periodmap.clear();
    }
    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        reset();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jTextField79KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField79KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jButton16.isEnabled() && !jTextField79.getText().isEmpty()) {
                addToInvoiceDuty();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!jTextField79.getText().isEmpty()) {
                addToInvoiceDuty();
                this.dispose();
            }
        }
    }//GEN-LAST:event_jTextField79KeyPressed

    public String productId;

    private void jTextField73CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField73CaretUpdate
        jTextField72.setEditable(false);
        jTextField11.setEditable(false);
        if (jTextField73.getText().length() > 5) {
            if (accessC) {
                accessA = false;
                accessB = false;
                boolean findpcount = false;
                try {
                    ResultSet rs = MySQL.execute("SELECT COUNT(`id`) AS `count` FROM `product` WHERE `product`.`model_number` = '" + jTextField73.getText() + "'");
                    if (rs.next()) {
                        if (rs.getInt("count") > 1) {
                            findpcount = true;
//                            System.out.println(rs.getInt("count"));
                        }
                    }
                } catch (Exception e) {
                    Login.log1.warning(e.getMessage());
                }

                if (findpcount) {
                    FindNeedProductForInvoice f = new FindNeedProductForInvoice(this, true, jTextField73.getText(), this);
                    f.setVisible(true);
                } else {
                    try {
                        ResultSet rs = MySQL.execute("SELECT * FROM `product` WHERE `product`.`model_number` = '" + jTextField73.getText() + "'");
                        if (rs.next()) {
                            jTextField72.setText(rs.getString("product.id"));
                            productId = rs.getString("product.id");
                        }
                    } catch (Exception e) {
                        Login.log1.warning(e.getMessage());
                    }
                }
                int status = 0;
//                        SelectSerial ss = new SelectSerial(this, true, this);
//                        ss.setVisible(true);
                try {
                    ResultSet statusres = MySQL.execute("SELECT * FROM `product_sn` WHERE `product_id` = '" + productId + "' AND `product_sn_status_id` = '1'");
                    if (statusres.next()) {
                        status = 1;
//                        System.out.println("sn");
                    }
                } catch (Exception e) {
                    Login.log1.warning(e.getMessage());
                }
                try {
                    Vector<HashMap> v = new Vector<>();
                    ResultSet rs = MySQL.execute("SELECT * FROM `stock` INNER JOIN"
                            + " `product` ON `product`.`id` = `stock`.`product_id` WHERE"
                            + " `stock`.`product_id` = '" + jTextField72.getText() + "' AND `stock`.`qty` > '0'");
                    while (rs.next()) {
                        HashMap<String, String> hashmap = new HashMap<>();
                        hashmap.put("stockid", rs.getString("stock.id"));
                        hashmap.put("model", rs.getString("model_number"));
                        v.add(hashmap);
                    }
//                    ResultSet res = MySQL.execute("SELECT COUNT(*) AS `sum_id`,`stock`.`id` AS `sid` FROM `stock` WHERE `product_id` = '" + productId + "'");
                    if (!v.isEmpty()) {
//                        System.out.println(res.getInt("sum_id"));
                        SelectOneFromStock s = new SelectOneFromStock(this, true, productId, this, status);
                        if (v.size() > 1) {

                            s.setVisible(true);
                        } else {
                            if (status == 1) {
                                SelectSerial sels = new SelectSerial(this, true, this, String.valueOf(v.get(0).get("stockid")));
                                sels.setVisible(true);
                            }
                        }
                    }
                } catch (Exception e) {
                    Login.log1.warning(e.getMessage());
                }

            }
        }
    }//GEN-LAST:event_jTextField73CaretUpdate

    private void jTextField79KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField79KeyTyped
        String txt = jTextField79.getText();
        System.out.println(txt);
//        if(jTextField79.getText().matches("^\\d+$")){
//            if(Integer.parseInt(jTextField79.getText())>getSqty()){
//                jTextField79.setText("");
//                jTextField79.setText(String.valueOf(getSqty()));
//            }
//        }else{
//            jTextField79.setText("");
//       }  
    }//GEN-LAST:event_jTextField79KeyTyped

    private void jTextField79CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField79CaretUpdate

    }//GEN-LAST:event_jTextField79CaretUpdate

    private void jTextField79KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField79KeyReleased
        if (jTextField79.getText().matches("^\\d+$")) {
            if (!jTextField79.getText().equals("0")) {
                if (Integer.parseInt(jTextField79.getText()) > getSqty()) {
                    jTextField79.setText("");
                    jTextField79.setText(String.valueOf(getSqty()));
                }
            } else {
                jTextField79.setText("");
            }

        } else {
            jTextField79.setText("");
        }
    }//GEN-LAST:event_jTextField79KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel15KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel15KeyPressed

    }//GEN-LAST:event_jPanel15KeyPressed

    private void jTextField72KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField72KeyPressed
        if (jButton15.isEnabled()) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!jButton16.isEnabled()) {
                    searchButtonDuty();
                }
            } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
                jTextField11.grabFocus();
            }
        }

    }//GEN-LAST:event_jTextField72KeyPressed

    private void jTextField11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyPressed
        if (jButton15.isEnabled()) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!jButton16.isEnabled()) {
                    searchButtonDuty();
                }
            }
        }

    }//GEN-LAST:event_jTextField11KeyPressed

    private void jTextField73KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField73KeyPressed
        if (jButton15.isEnabled()) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!jButton16.isEnabled()) {
                    searchButtonDuty();
                }
            }
        }

    }//GEN-LAST:event_jTextField73KeyPressed

    public JButton getjButton15(){
        return jButton15;
    }
    
    public JCheckBox getjCheckBox1() {
        return jCheckBox1;
    }

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            if (getStockId() != null) {
                SelectWarrantyPeriod wp = new SelectWarrantyPeriod(this, true, getStockId(), this);
                wp.setVisible(true);
            }
        }else{
            setWarrantyDuration(null);
            setWarrantyPrice(null);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCheckBox1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addToInvoiceDuty();
        }
    }//GEN-LAST:event_jCheckBox1KeyPressed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(AddProductToInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(AddProductToInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(AddProductToInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(AddProductToInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                AddProductToInvoice dialog = new AddProductToInvoice(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextArea jTextArea8;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField72;
    private javax.swing.JTextField jTextField73;
    private javax.swing.JTextField jTextField74;
    private javax.swing.JTextField jTextField75;
    private javax.swing.JTextField jTextField76;
    private javax.swing.JTextField jTextField77;
    private javax.swing.JTextField jTextField78;
    private javax.swing.JTextField jTextField79;
    private javax.swing.JTextField jTextField80;
    private javax.swing.JTextField jTextField81;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the warrantyPrice
     */
    public String getWarrantyPrice() {
        return warrantyPrice;
    }

    /**
     * @param warrantyPrice the warrantyPrice to set
     */
    public void setWarrantyPrice(String warrantyPrice) {
        this.warrantyPrice = warrantyPrice;
    }

    /**
     * @return the WarrantyDuration
     */
    public String getWarrantyDuration() {
        return WarrantyDuration;
    }

    /**
     * @param WarrantyDuration the WarrantyDuration to set
     */
    public void setWarrantyDuration(String WarrantyDuration) {
        this.WarrantyDuration = WarrantyDuration;
    }
}
