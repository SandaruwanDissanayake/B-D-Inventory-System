/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MainPanel;

//import GUI.DialogBox.AddProductQuotation;
import GUI.DialogBox.ArrearsPaymentDetails;

import GUI.DialogBox.UpcomingPaymentDetails;
import GUI.MainFrame.Login;
import Models.MySQL;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import javax.swing.SwingUtilities;
import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Sandaruwan
 */
public class Dashboard extends javax.swing.JPanel {

    int dueAmount = 0;
    int upcomingAmount = 0;

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
        totalEarnings();
        totalDuePayments();
        LoadLineChart();
        loadPieChart();

    }

    private void loadPieChart() {
        int fullPaymentTotal = 0;
        int otherPlansTotal = 0;
        try {
            ResultSet otherPlansSet = MySQL.execute("SELECT * FROM `invoice` INNER JOIN `payment_value` ON `invoice`.`id`=`payment_value`.`invoice_id` WHERE `invoice`.`payment_option_id`!='1' AND `payment_value`.`payment_status_id`='1'");
            while (otherPlansSet.next()) {
                otherPlansTotal += otherPlansSet.getInt("total_amount");

            }
            ResultSet fullPaymentSet = MySQL.execute("SELECT * FROM `invoice` WHERE `invoice`.`payment_option_id`='1' ");
            while (fullPaymentSet.next()) {
                fullPaymentTotal += fullPaymentSet.getInt("total_amount");

            }
        } catch (Exception ex) {
                                  Login.log1.warning(ex.getMessage());

        }

        int completedPaymentsTotalAmount = fullPaymentTotal + otherPlansTotal;

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Due Payments ( Rs. " + this.dueAmount + ".00 )", this.dueAmount);
        dataset.setValue("Upcoming Payments ( Rs. " + this.upcomingAmount + ".00 )", this.upcomingAmount);
        dataset.setValue("Completed Payments ( Rs. " + completedPaymentsTotalAmount + ".00 )", completedPaymentsTotalAmount);

        createPieChart(dataset);
    }

    private void createPieChart(DefaultPieDataset dataset) {

        JFreeChart chart1 = ChartFactory.createPieChart("", dataset);
        chart1.setBackgroundPaint(new Color(0, 0, 0, 0));

//        chart1.setBackgroundPaint(new Color(187, 187, 187));
        ChartPanel panel = new ChartPanel(chart1);
        panel.setBackground(new Color(0, 0, 0, 0));
        jPanel9.add(panel, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(jPanel9);

    }

    private void LoadLineChart() {
        Date today = new Date();
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat monthInName = new SimpleDateFormat("MMM");

        int yearInt = Integer.parseInt(year.format(today));
        int monthInt = Integer.parseInt(month.format(today));
        String monthName = monthInName.format(today);

        LocalDate today1 = LocalDate.now();
        LocalDate givenDate = LocalDate.of(yearInt, monthInt, 1);

        long daysBetween = ChronoUnit.DAYS.between(givenDate, today1);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 1; i <= daysBetween + 1; i++) {
            int totalAmount = 0;
            String dateString = String.valueOf(yearInt) + "-" + String.valueOf(monthInt) + "-" + i;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String parse = dateFormat.format(dateFormat.parse(dateString));
                ResultSet resultSet = MySQL.execute("SELECT * FROM `invoice` WHERE `date_time` LIKE '" + parse + "%'");
                while (resultSet.next()) {
                    totalAmount += resultSet.getInt("total_amount");
                }

            } catch (Exception ex) {
                                       Login.log1.warning(ex.getMessage());

            }

            dataset.setValue(totalAmount, "Amount", String.valueOf(i));

        }
        jLabel14.setText("This Month Earnings " + "( " + monthName + " )");
        createLineChart(dataset);
        // Replace with your desired date
    }

    private void totalDuePayments() {
        int dueAmount = 0;
        int upcomingAmount = 0;
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `invoice` INNER JOIN `payment_value` ON `invoice`.`id`=`payment_value`.`invoice_id` WHERE `invoice`.`payment_option_id`!='1' AND `payment_value`.`payment_status_id`='2'");
            while (resultSet.next()) {
                Date today = new Date();
                Date invoiceDate = resultSet.getDate("date_time");

                Calendar cal = Calendar.getInstance();
                cal.setTime(invoiceDate);

                if (resultSet.getInt("payment_option_id") == 2) {
                    cal.add(Calendar.DATE, 31);

                    if (today.compareTo(cal.getTime()) >= 0) {
                        dueAmount += resultSet.getInt("total");
                    } else {
                        upcomingAmount += resultSet.getInt("total");

                    }

                } else if (resultSet.getInt("payment_option_id") == 3) {
                    cal.add(Calendar.DATE, 91);

                    if (today.compareTo(cal.getTime()) >= 0) {
                        dueAmount += resultSet.getInt("total");
                    } else {
                        upcomingAmount += resultSet.getInt("total");

                    }
                }

            }
        } catch (Exception ex) {
                                  Login.log1.warning(ex.getMessage());

        }
        this.dueAmount = dueAmount;
        this.upcomingAmount = upcomingAmount;
        jLabel10.setText("Rs. " + dueAmount + ".00");
        jLabel13.setText("Rs. " + upcomingAmount + ".00");
    }

    private void totalEarnings() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-01 00:00:01");
        String thisMonth = dateFormat.format(date);
        int totalInvoiceCountThisMonth = 0;
        int totalEarningThisMonth = 0;
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `invoice` WHERE `date_time`>'" + thisMonth + "'");
            while (resultSet.next()) {
                totalInvoiceCountThisMonth++;
                totalEarningThisMonth += resultSet.getInt("total_amount");
            }
        } catch (Exception ex) {
                                  Login.log1.warning(ex.getMessage());

        }
        jLabel6.setText("Rs. " + totalEarningThisMonth + ".00");
        jLabel8.setText(totalInvoiceCountThisMonth + " Invoices");

    }

    private void createLineChart(DefaultCategoryDataset dataset) {

        JFreeChart chart1 = ChartFactory.createLineChart("This Month Earnings", "Date", "Amount", dataset);
        chart1.setBackgroundPaint(new Color(0, 0, 0, 0));
        chart1.getCategoryPlot().setBackgroundPaint(new Color(0, 0, 0, 0));
        chart1.setBorderVisible(false);
        ChartPanel chartPanel = new ChartPanel(chart1);

        jPanel15.add(chartPanel, BorderLayout.CENTER);

        SwingUtilities.updateComponentTreeUI(jPanel15);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        panelRound1 = new GUI.Models.PanelRound();
        jImagePanel1 = new main.JImagePanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        panelRound2 = new GUI.Models.PanelRound();
        jImagePanel2 = new main.JImagePanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        panelRound3 = new GUI.Models.PanelRound();
        jImagePanel3 = new main.JImagePanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        panelRound4 = new GUI.Models.PanelRound();
        jImagePanel4 = new main.JImagePanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(25, 231));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 305, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(738, 200));
        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Dashboard");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addContainerGap(787, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel1);

        jPanel7.setPreferredSize(new java.awt.Dimension(738, 120));

        jPanel8.setPreferredSize(new java.awt.Dimension(25, 100));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel10.setPreferredSize(new java.awt.Dimension(25, 100));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel13.setLayout(new java.awt.GridLayout(1, 4, 35, 0));

        panelRound1.setBackground(new java.awt.Color(7, 45, 132));
        panelRound1.setRoundBottomLeft(20);
        panelRound1.setRoundBottomRight(20);
        panelRound1.setRoundTopLeft(20);
        panelRound1.setRoundTopRight(20);

        jImagePanel1.setBackground(new java.awt.Color(5, 51, 47));
        jImagePanel1.setForeground(new java.awt.Color(5, 51, 47));
        jImagePanel1.setFitToPanel(true);
        jImagePanel1.setImageIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/invoice_dollar_icon.png"))); // NOI18N
        jImagePanel1.setPreferredSize(new java.awt.Dimension(12, 18));
        jImagePanel1.setSmoothRendering(true);

        javax.swing.GroupLayout jImagePanel1Layout = new javax.swing.GroupLayout(jImagePanel1);
        jImagePanel1.setLayout(jImagePanel1Layout);
        jImagePanel1Layout.setHorizontalGroup(
            jImagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jImagePanel1Layout.setVerticalGroup(
            jImagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("This Month Earning");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Rs. 140,333.00");

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jImagePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jImagePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel13.add(panelRound1);

        panelRound2.setBackground(new java.awt.Color(7, 45, 132));
        panelRound2.setRoundBottomLeft(20);
        panelRound2.setRoundBottomRight(20);
        panelRound2.setRoundTopLeft(20);
        panelRound2.setRoundTopRight(20);

        jImagePanel2.setBackground(new java.awt.Color(5, 51, 47));
        jImagePanel2.setForeground(new java.awt.Color(5, 51, 47));
        jImagePanel2.setFitToPanel(true);
        jImagePanel2.setImageIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/invoice_dollar_icon.png"))); // NOI18N
        jImagePanel2.setPreferredSize(new java.awt.Dimension(12, 18));
        jImagePanel2.setSmoothRendering(true);

        javax.swing.GroupLayout jImagePanel2Layout = new javax.swing.GroupLayout(jImagePanel2);
        jImagePanel2.setLayout(jImagePanel2Layout);
        jImagePanel2Layout.setHorizontalGroup(
            jImagePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jImagePanel2Layout.setVerticalGroup(
            jImagePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("This Month Sellings");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("139 Invoices");

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jImagePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jImagePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel13.add(panelRound2);

        panelRound3.setBackground(new java.awt.Color(7, 45, 132));
        panelRound3.setRoundBottomLeft(20);
        panelRound3.setRoundBottomRight(20);
        panelRound3.setRoundTopLeft(20);
        panelRound3.setRoundTopRight(20);
        panelRound3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelRound3MouseReleased(evt);
            }
        });

        jImagePanel3.setBackground(new java.awt.Color(5, 51, 47));
        jImagePanel3.setForeground(new java.awt.Color(5, 51, 47));
        jImagePanel3.setFitToPanel(true);
        jImagePanel3.setImageIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/invoice_dollar_icon.png"))); // NOI18N
        jImagePanel3.setPreferredSize(new java.awt.Dimension(12, 18));

        javax.swing.GroupLayout jImagePanel3Layout = new javax.swing.GroupLayout(jImagePanel3);
        jImagePanel3.setLayout(jImagePanel3Layout);
        jImagePanel3Layout.setHorizontalGroup(
            jImagePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jImagePanel3Layout.setVerticalGroup(
            jImagePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total Due Payments");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("See more...");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel11MouseReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Rs. 50,333.00");

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel11))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addComponent(jImagePanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jImagePanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addContainerGap())
        );

        jPanel13.add(panelRound3);

        panelRound4.setBackground(new java.awt.Color(7, 45, 132));
        panelRound4.setRoundBottomLeft(20);
        panelRound4.setRoundBottomRight(20);
        panelRound4.setRoundTopLeft(20);
        panelRound4.setRoundTopRight(20);

        jImagePanel4.setBackground(new java.awt.Color(5, 51, 47));
        jImagePanel4.setForeground(new java.awt.Color(5, 51, 47));
        jImagePanel4.setFitToPanel(true);
        jImagePanel4.setImageIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/invoice_dollar_icon.png"))); // NOI18N
        jImagePanel4.setPreferredSize(new java.awt.Dimension(12, 18));

        javax.swing.GroupLayout jImagePanel4Layout = new javax.swing.GroupLayout(jImagePanel4);
        jImagePanel4.setLayout(jImagePanel4Layout);
        jImagePanel4Layout.setHorizontalGroup(
            jImagePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jImagePanel4Layout.setVerticalGroup(
            jImagePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Total Upcoming Payments");

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("See more...");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel12MouseReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Rs. 50,333.00");

        javax.swing.GroupLayout panelRound4Layout = new javax.swing.GroupLayout(panelRound4);
        panelRound4.setLayout(panelRound4Layout);
        panelRound4Layout.setHorizontalGroup(
            panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRound4Layout.createSequentialGroup()
                        .addComponent(jImagePanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel12)))
                .addContainerGap())
            .addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelRound4Layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(15, Short.MAX_VALUE)))
        );
        panelRound4Layout.setVerticalGroup(
            panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jImagePanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addContainerGap())
            .addGroup(panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelRound4Layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(jLabel13)
                    .addContainerGap(34, Short.MAX_VALUE)))
        );

        jPanel13.add(panelRound4);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel3.add(jPanel7);

        add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setPreferredSize(new java.awt.Dimension(25, 231));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 305, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(738, 25));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 935, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.PAGE_END);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel11.setPreferredSize(new java.awt.Dimension(300, 305));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Total Payments");

        jPanel9.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel15)
                .addContainerGap(190, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.add(jPanel11, java.awt.BorderLayout.LINE_END);

        jPanel12.setPreferredSize(new java.awt.Dimension(881, 25));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 885, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("This Month Earnings");

        jPanel15.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 446, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.add(jPanel14, java.awt.BorderLayout.CENTER);

        add(jPanel6, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void panelRound3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound3MouseReleased

        // TODO add your handling code here:
    }//GEN-LAST:event_panelRound3MouseReleased

    private void jLabel11MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseReleased
        new ArrearsPaymentDetails(this, true).setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel11MouseReleased

    private void jLabel12MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseReleased
        new UpcomingPaymentDetails(this, true).setVisible(true);

// TODO add your handling code here:
    }//GEN-LAST:event_jLabel12MouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private main.JImagePanel jImagePanel1;
    private main.JImagePanel jImagePanel2;
    private main.JImagePanel jImagePanel3;
    private main.JImagePanel jImagePanel4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private GUI.Models.PanelRound panelRound1;
    private GUI.Models.PanelRound panelRound2;
    private GUI.Models.PanelRound panelRound3;
    private GUI.Models.PanelRound panelRound4;
    // End of variables declaration//GEN-END:variables
}
