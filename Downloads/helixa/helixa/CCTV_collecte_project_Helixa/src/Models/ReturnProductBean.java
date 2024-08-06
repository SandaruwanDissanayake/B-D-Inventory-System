/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Mr. SP
 */
public class ReturnProductBean {
    
    private String invoiceItemId;
    private int returnQty;
    private HashMap<String,Boolean> serialNumbers;
    private String stockId;
    private boolean isReturn;

    public String getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(String invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public int getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(int returnQty) {
        this.returnQty = returnQty;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public boolean isIsReturn() {
        return isReturn;
    }

    public void setIsReturn(boolean isReturn) {
        this.isReturn = isReturn;
    }

    public HashMap<String,Boolean> getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(HashMap<String,Boolean> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }
    
}
