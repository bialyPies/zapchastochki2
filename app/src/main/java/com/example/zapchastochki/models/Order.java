package com.example.zapchastochki.models;

import java.io.Serializable;

public class Order implements Serializable {

    private long id;
    private long idCustomer;
    private OrderStatus orderStatus; //enum
    private String orderDate;
    private String completionDate;
    private double totalCost;
    private int totalNumber;

    private int dateLastChange;
    private int timeLastChange;

    private Customer customer;

    public Order(){}
    public Order (long id, long idCustomer, OrderStatus orderStatus,  String date,
                  String completionDate, double totalCost, int totalNumber){
        this.id = id;
        this.idCustomer = idCustomer;
        this.orderStatus = orderStatus;
        this.orderDate = date;
        this.completionDate = completionDate;
        this.totalCost = totalCost;
        this.totalNumber = totalNumber;
    }
    public Order (long id, long idCustomer, OrderStatus orderStatus,  String date,
                  String completionDate, double totalCost, int totalNumber,
                  int dateLastChange, int timeLastChange){
        this.id = id;
        this.idCustomer = idCustomer;
        this.orderStatus = orderStatus;
        this.orderDate = date;
        this.completionDate = completionDate;
        this.totalCost = totalCost;
        this.totalNumber = totalNumber;

        this.dateLastChange = dateLastChange;
        this.timeLastChange = timeLastChange;
    }

    public long getId(){return id;}
    public void setId(long id){this.id = id;}
    public long getIdCustomer(){return idCustomer;}
    public void setIdCustomer(long idCustomer){this.idCustomer = idCustomer;}
    public OrderStatus getOrderStatus (){return orderStatus;}
    public void setOrderStatus(OrderStatus orderStatus){this.orderStatus = orderStatus;}
    public String getOrderDate(){return orderDate;}
    public void setOrderDate(String orderDate){this.orderDate = orderDate;}
    public String getCompletionDate (){return completionDate;}
    public void setCompletionDate(String completionDate){this.completionDate = completionDate;}
    public double getTotalCost (){return totalCost;}
    public void setTotalCost(double totalCost){this.totalCost = totalCost;}
    public int getTotalNumber (){return totalNumber;}
    public void setTotalNumber(int totalNumber){this.totalNumber = totalNumber;}

    public Customer getCustomer(){return customer;}
    public void setCustomer(Customer user){this.customer = user;}

    public int getDateLastChange(){return this.dateLastChange;}
    public void setDateLastChange(int dateLastChange){this.dateLastChange = dateLastChange;}
    public int getTimeLastChange(){return this.timeLastChange;}
    public void setTimeLastChange(int timeLastChange){this.timeLastChange = timeLastChange;}
}
