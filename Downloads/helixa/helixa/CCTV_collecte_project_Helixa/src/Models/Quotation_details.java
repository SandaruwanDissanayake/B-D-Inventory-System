
package Models;


public class Quotation_details {
    private String no;
     private String product;
     private String qty;
     private String price;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Quotation_details(String no, String product, String qty, String price) {
        this.no = no;
        this.product = product;
        this.qty = qty;
        this.price = price;
    }
    
}

