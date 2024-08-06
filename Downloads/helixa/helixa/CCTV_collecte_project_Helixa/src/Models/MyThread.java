/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author CNN COMPUTERS
 */
public class MyThread implements Runnable {

    private String id;
    private HashMap<String, String> chequemap;
    private String sitem;
    private String paid;

    public MyThread(String id, HashMap<String, String> chequemap, String sitem,String paid) {
        this.id = id;
        this.chequemap = chequemap;
        this.sitem = sitem;
        this.paid = paid;
    }

    @Override
    public void run() {
        if (!chequemap.isEmpty()) {
            try {
                MySQL.execute("INSERT INTO `check` (`check_no`,`date_time`,`amount`,`place`,`bank_name`,`invoice_id`,`cheque_status_id`) "
                        + "VALUES ('" + chequemap.get("cheque") + "','" + chequemap.get("date") + "','" + chequemap.get("ammount") + "','" + chequemap.get("place") + "','" + chequemap.get("bank") + "','" + id + "','2')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(!sitem.equals("Full Payment")){
            try {
                MySQL.execute("INSERT INTO `payment_value` (`total`,`date`,`invoice_id`,`payment_status_id`) "
                        + "VALUES ('"+paid+"','"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"','"+id+"','2')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
