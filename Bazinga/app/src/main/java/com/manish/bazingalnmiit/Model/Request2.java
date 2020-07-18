package com.manish.bazingalnmiit.Model;

import java.util.List;

public class Request2 {

    private String CHECKSUMHASH;
    private String address;
    private List<Order> foods;
    private String phone;
    private long total;
    private String uid;

    public Request2() {
    }

    public Request2(String CHECKSUMHASH, String address, List<Order> foods, String phone, long total, String uid) {
        this.CHECKSUMHASH = CHECKSUMHASH;
        this.address = address;
        this.foods = foods;
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

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
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