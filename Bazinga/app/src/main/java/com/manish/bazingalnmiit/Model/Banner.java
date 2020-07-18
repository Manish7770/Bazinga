package com.manish.bazingalnmiit.Model;

public class Banner {
    private String FoodId,Name;

    public Banner() {
    }

    public Banner(String foodId, String name) {
        FoodId = foodId;
        Name = name;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
