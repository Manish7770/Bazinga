package com.manish.bazingaadmin.Model;

public class Order {
    private long packingCharge;
    private long price;
    private String productid;
    private String productName;
    private String quantity;
    private String uid;


    public Order() {
    }

    public Order(long packingCharge, long price, String productid, String productName, String quantity, String uid) {
        this.packingCharge = packingCharge;
        this.price = price;
        this.productid = productid;
        this.productName = productName;
        this.quantity = quantity;
        this.uid = uid;
    }

    public long getPackingCharge() {
        return packingCharge;
    }

    public void setPackingCharge(long packingCharge) {
        this.packingCharge = packingCharge;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
