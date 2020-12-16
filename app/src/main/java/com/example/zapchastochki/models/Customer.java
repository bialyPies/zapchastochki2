package com.example.zapchastochki.models;

import java.io.Serializable;

public class Customer implements Serializable { //CUSTOMER
    public static final Customer CURRENT_USER = new Customer();

    private long id;
    private String username;
    private String name = "no name";
    private String mobile = "0";
    private String password;
    private boolean isAdmin;

    private int dateLastChange;
    private int timeLastChange;

    public Customer(){}
    public Customer(long id, String name, String mobile){
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }
    public Customer(long id, String username, String name, String mobile, String password,
                boolean isAdmin){
        this.id = id;
        this.username = username;
        this.name = name;
        this.mobile = mobile;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    public Customer(long id, String username, String name, String mobile, String password,
                boolean isAdmin, int dateLastChange, int timeLastChange){
        this.id = id;
        this.username = username;
        this.name = name;
        this.mobile = mobile;
        this.password = password;
        this.isAdmin = isAdmin;

        this.dateLastChange = dateLastChange;
        this.timeLastChange = timeLastChange;
    }

    public long getId(){return id;}
    public void setId(long id){this.id = id;}
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getIsAmin(){return  isAdmin;}
    public void setIsAdmin(boolean isAdmin){this.isAdmin = isAdmin;}

    public int getDateLastChange(){return this.dateLastChange;}
    public void setDateLastChange(int dateLastChange){this.dateLastChange = dateLastChange;}
    public int getTimeLastChange(){return this.timeLastChange;}
    public void setTimeLastChange(int timeLastChange){this.timeLastChange = timeLastChange;}
}
