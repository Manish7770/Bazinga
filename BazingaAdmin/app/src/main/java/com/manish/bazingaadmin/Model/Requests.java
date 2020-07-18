package com.manish.bazingaadmin.Model;

import java.util.List;

public class Requests {

    private String CHECKSUMHASH;
    private String address;
    private int delivered;
    private List<Order> foods;
    private Boolean payment;
    private String phone;
    private long total;
    private String uid;

    public Requests() {
    }

    public Requests(String CHECKSUMHASH, String address, int delivered, List<Order> foods, Boolean payment, String phone, long total, String uid) {
        this.CHECKSUMHASH = CHECKSUMHASH;
        this.address = address;
        this.delivered = delivered;
        this.foods = foods;
        this.payment = payment;
        this.phone = phone;
        this.total = total;
        this.uid = uid;
    }

    public String getCHECKSUMHASH() {
        return CHECKSUMHASH;
    }

    public void setCHECKSUMHASH(String CHECKSUMHASH) {
        this.CHECKSUMHASH = CHECKSUMHASH;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}