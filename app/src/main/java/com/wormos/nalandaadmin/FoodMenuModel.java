package com.wormos.nalandaadmin;

public class FoodMenuModel {
    String menu,time;

    public FoodMenuModel() {
    }

    public FoodMenuModel(String menu, String time) {
        this.menu = menu;
        this.time = time;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
