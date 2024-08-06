/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.DialogBox;

import GUI.MainPanel.GRN;
import Models.AddGRNBean;
import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JOptionPane;

/**
 *
 * @author Sandaruwan
 */
public class AddProductWarranty extends javax.swing.JDialog {

    /**
     * Creates new form AddProductWarranty
     */
    private String title;
    private String modelNumber;
    private GRN grn;
    private HashMap<Integer, AddGRNBean> addGRNProductBeanMap;
    private AddGRNBean currentBean;
    private AddProductToGRN addProductGrn;
    private boolean edit;

    public AddProductWarranty(Frame parent, boolean modal, String title, String modelNum, HashMap<Integer, AddGRNBean> addGRNProductBeanMap, GRN grnframe, AddGRNBean currentBean, boolean edit) {
        super(parent, modal);
        initComponents();
        this.modelNumber = modelNum;
        this.title = title;
        jLabel2.setText(title);
        jLabel3.setText(modelNum);
        this.addGRNProductBeanMap = addGRNProductBeanMap;
        this.currentBean = currentBean;
        this.grn = grnframe;
        this.edit = edit;
        loadWarranty();
        checkBoxValidation();

//        this.addProductGrn = addProductgrn;
    }

    public AddProductWarranty(Frame parent, boolean modal, String title, String modelNum, HashMap<Integer, AddGRNBean> addGRNProductBeanMap, GRN grnframe, AddGRNBean currentBean) {
        super(parent, modal);
        initComponents();
        this.modelNumber = modelNum;
        this.title = title;
        jLabel2.setText(title);
        jLabel3.setText(modelNum);
        this.addGRNProductBeanMap = addGRNProductBeanMap;
        this.currentBean = currentBean;
        this.grn = grnframe;
//        jCheckBox2.setEnabled(false);
//        jCheckBox3.setEnabled(false);
//        jCheckBox4.setEnabled(false);
        checkBoxValidation();
    }

    private void loadWarranty() {

        if (edit) {
            System.out.println("warranty");
            for (Map.Entry<Integer, AddGRNBean> entry : addGRNProductBeanMap.entrySet()) {

                if (entry.getValue().getModelNumber().equals(modelNumber) && entry.getValue().getTitle().equals(title)) {

                    String sixMonth = entry.getValue().getSixMonthWarranty();
                    String oneYear = entry.getValue().getOneYearWarranty();
                    String twoYear = entry.getValue().getTwoYearWarranty();

                    jFormattedTextField1.setText(sixMonth);
                    jFormattedTextField3.setText(oneYear);
                    jFormattedTextField4.setText(twoYear);

                    if (entry.getValue().isCompanyWarranty()) {
                        jCheckBox1.setSelected(true);
                        if (entry.getValue().getCompanyWarrantyTime().equals("sixMonth")) {
                            jCheckBox2.setSelected(true);
                        } else if (entry.getValue().getCompanyWarrantyTime().equals("oneYear")) {
                            jCheckBox3.setSelected(true);
                        } else if (entry.getValue().getCompanyWarrantyTime().equals("twoYear")) {
                            jCheckBox4.setSelected(true);
                        }
                    }

                }

            }

        }

    }

    private void saveWarrantyPrice() {

        String sixMonth = "0";
        String oneYearString = "0";
        String twoYearString = "0";

        if (!jFormattedTextField1.getText().isEmpty()) {
            sixMonth = jFormattedTextField1.getText();
        }
        if (!jFormattedTextField3.getText().isEmpty()) {
            oneYearString = jFormattedTextField3.getText();
        }
        if (!jFormattedTextField4.getText().isEmpty()) {
            twoYearString = jFormattedTextField4.getText();
        }

        if (!jCheckBox1.isSelected() && sixMonth.equals("0") && oneYearString.equals("0") && twoYearString.equals("0")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter either the shop warranty price or the company warranty time period.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        } else {
            for (Map.Entry<Integer, AddGRNBean> entry : addGRNProductBeanMap.entrySet()) {
                if (entry.getValue().getModelNumber().equals(modelNumber) && entry.getValue().getTitle().equals(title)) {

                    AddGRNBean bean = entry.getValue();

                    if (jCheckBox1.isSelected()) {
                        bean.setCompanyWarranty(true);

                        if (jCheckBox2.isSelected()) {
                            bean.setCompanyWarrantyTime("sixMonth");
                        } else if (jCheckBox3.isSelected()) {
                            bean.setCompanyWarrantyTime("oneYear");
                        } else if (jCheckBox4.isSelected()) {
                            bean.setCompanyWarrantyTime("twoYear");
                        }

                    } else {
                        bean.setSixMonthWarranty(sixMonth);
                        bean.setOneYearWarranty(oneYearString);
                        bean.setTwoYearWarranty(twoYearString);
                        bean.setCompanyWarranty(false); // Ensure to reset company warranty if not selected
                        bean.setCompanyWarrantyTime(""); // Clear the warranty time if not company warranty
                    }

                    // Update the current bean reference
                    currentBean = bean;
                    // Update the map entry
                    addGRNProductBeanMap.put(entry.getKey(), bean);
                }
            }
            this.dispose();
        }
    }

    private void checkBoxValidation() {

        boolean companyWarranty = jCheckBox1.isSelected();

        if (companyWarranty) {

            jFormattedTextField1.setEnabled(false);
            jFormattedTextField3.setEnabled(false);
            jFormattedTextField4.setEnabled(false);

            jCheckBox2.setEnabled(true);
            jCheckBox3.setEnabled(true);
            jCheckBox4.setEnabled(true);

            jFormattedTextField1.setText("");
            jFormattedTextField3.setText("");
            jFormattedTextField4.setText("");

        } else {
            jFormattedTextField1.setEnabled(true);
            jFormattedTextField3.setEnabled(true);
            jFormattedTextField4.setEnabled(true);

            jCheckBox2.setEnabled(false);
            jCheckBox3.setEnabled(false);
            jCheckBox4.setEnabled(false);

            jCheckBox2.setSelected(false);
            jCheckBox3.setSelected(false);
            jCheckBox4.setSelected(false);

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

        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();

        jFormattedTextField2.setText("jFormattedTextField2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Product Warranty");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("jLabel2");

        jLabel3.setText("jLabel2");

        jLabel4.setText("6 Month");

        jLabel5.setText("1 Year");

        jLabel6.setText("2 Year");

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setText("Company Warranty");

        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });

        buttonGroup1.add(jCheckBox2);

        buttonGroup1.add(jCheckBox3);

        buttonGroup1.add(jCheckBox4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jFormattedTextField1)
                    .addComponent(jFormattedTextField3)
                    .addComponent(jFormattedTextField4)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 176, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox1))
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jCheckBox1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jCheckBox2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5))
                            .addComponent(jCheckBox3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addComponent(jCheckBox4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        saveWarrantyPrice();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_jCheckBox1StateChanged

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:

        checkBoxValidation();


    }//GEN-LAST:event_jCheckBox1ItemStateChanged

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
            java.util.logging.Logger.getLogger(AddProductWarranty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddProductWarranty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddProductWarranty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddProductWarranty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
