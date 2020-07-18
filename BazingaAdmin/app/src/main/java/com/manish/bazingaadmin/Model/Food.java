package com.manish.bazingaadmin.Model;

public class Food {
    private String Name,
                   Price,
                   MenuId,
                   PackingCharge,
                   Stock;

    public Food() {
    }

    public Food(String name, String price, String menuId, String packingCharge, String stock) {
        Name = name;
        Price = price;
        MenuId = menuId;
        PackingCharge = packingCharge;
        Stock = stock;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getPackingCharge() {
        return PackingCharge;
    }

    public void setPackingCharge(String packingCharge) {
        PackingCharge = packingCharge;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }
}
