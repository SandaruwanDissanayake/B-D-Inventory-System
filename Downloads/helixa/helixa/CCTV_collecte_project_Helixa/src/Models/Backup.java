/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Backup {

    public static void createBackup() {

        String location;
        String filename;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String d = sdf.format(new Date());

        try {
            location = "C:/ProgramData/DataBackup/Backup";
            filename = location + d + ".sql";

//            System.out.println("location" + location);
//            System.out.println("filename" + filename);
            Process p = null;

            Runtime runtime = Runtime.getRuntime();
  p = runtime.exec("C:/Program Files/MySQL/MySQL Server 8.0/bin/mysqldump.exe -uroot -p****** --add-drop-database -B cctv_shop_db -r" + filename);
            int processComplete = p.waitFor();
//
//            if (processComplete == 0) {
//                // sendEmail();
//                JOptionPane.showMessageDialog(null, "Backup Created Success");
//            } else {
//                JOptionPane.showMessageDialog(null, "Can't Created Backup");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        createBackup();
    }

}
