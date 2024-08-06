/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Vector;

public class ReIssueProductBean {
    
    private int qty;
    private String stockId;
    private String status;
    private Vector<String> serials;

    

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Vector<String> getSerials() {
        return serials;
    }

    public void setSerials(Vector<String> serials) {
        this.serials = serials;
    }
}
