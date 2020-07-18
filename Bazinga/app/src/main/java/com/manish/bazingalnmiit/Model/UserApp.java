package com.manish.bazingalnmiit.Model;
import android.widget.EditText;

public class UserApp {
    private String address;
    private String email;
    private String name;
    private String phoneNumber;

    public UserApp() {
    }

    public UserApp(String address, String email, String name, String phoneNumber) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

