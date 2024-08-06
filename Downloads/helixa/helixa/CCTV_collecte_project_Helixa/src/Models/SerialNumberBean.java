/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sandaruwan
 */
public class SerialNumberBean {

    private List<String> serialNumberList;
    private int indexNumber;

    public SerialNumberBean() {
        this.serialNumberList = new ArrayList<>();
    }

    public List<String> getSerialNumberList() {
        return serialNumberList;
    }

    public void addSerialNumber(String serialNumber) {
        this.serialNumberList.add(serialNumber);
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }
}
