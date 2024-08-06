package Models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sandaruwan
 */
public class AddGRNBean {

    public boolean isCompanyWarranty() {
        return companyWarranty;
    }

    public void setCompanyWarranty(boolean companyWarranty) {
        this.companyWarranty = companyWarranty;
    }

    public String getCompanyWarrantyTime() {
        return companyWarrantyTime;
    }

    public void setCompanyWarrantyTime(String companyWarrantyTime) {
        this.companyWarrantyTime = companyWarrantyTime;
    }

    public String getSixMonthWarranty() {
        return sixMonthWarranty;
    }

    public void setSixMonthWarranty(String sixMonthWarranty) {
        this.sixMonthWarranty = sixMonthWarranty;
    }

    public String getOneYearWarranty() {
        return oneYearWarranty;
    }

    public void setOneYearWarranty(String oneYearWarranty) {
        this.oneYearWarranty = oneYearWarranty;
    }

    public String getTwoYearWarranty() {
        return twoYearWarranty;
    }

    public void setTwoYearWarranty(String twoYearWarranty) {
        this.twoYearWarranty = twoYearWarranty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String description) {
        this.modelNumber = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(String buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getPriceNewCustomer() {
        return priceNewCustomer;
    }

    public void setPriceNewCustomer(String priceNewCustomer) {
        this.priceNewCustomer = priceNewCustomer;
    }

    public String getPriceDailyCustomer() {
        return priceDailyCustomer;
    }

    public void setPriceDailyCustomer(String priceDailyCustomer) {
        this.priceDailyCustomer = priceDailyCustomer;
    }

    public String getPriceShop() {
        return priceShop;
    }

    public void setPriceShop(String priceShop) {
        this.priceShop = priceShop;
    }

    public AddGRNBean() {
        this.serialNumberList = new ArrayList<>();
    }

    public List<String> getSerialNumberList() {
        return serialNumberList;
    }

    public void addSerialNumber(String serialNumber) {
        this.serialNumberList.add(serialNumber);
    }

    private String title = "null";
    private String category = "null";
    private String brand = "null";
    private String modelNumber = "null";
    private int qty = 0;
    private String buyingPrice = "null";
    private String priceNewCustomer = "null";
    private String priceDailyCustomer = "null";
    private String priceShop = "null";
    private List<String> serialNumberList;
    private String sixMonthWarranty = "0";
    private String oneYearWarranty = "0";
    private String twoYearWarranty = "0";
    private boolean companyWarranty = false;
    private String companyWarrantyTime = "";

}
