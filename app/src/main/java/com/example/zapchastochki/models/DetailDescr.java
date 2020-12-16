package com.example.zapchastochki.models;

import java.io.Serializable;

public class DetailDescr implements Serializable {

    protected long id;
    protected String name;
    protected String path;
    protected double price;
    protected int releaseYear;
    protected int num;

    protected int dateLastChange;
    protected int timeLastChange;

    public DetailDescr (){}
    public DetailDescr (long id, String name, String path, double price, int releaseYear, int num){
        this.id = id;
        this.name = name;
        this.path = path;
        this.price = price;
        this.releaseYear = releaseYear;
        this.num = num;
    }
    public DetailDescr (long id, String name, String path, double price, int releaseYear,
                   int num, int dateLastChange, int timeLastChange){
        this.id = id;
        this.name = name;
        this.path = path;
        this.price = price;
        this.releaseYear = releaseYear;
        this.num = num;

        this.dateLastChange = dateLastChange;
        this.timeLastChange = timeLastChange;
    }

    public long getId(){return id;}
    public void setId(long id){this.id = id;}
    public String getName(){return name;}
    public void setName(String name){ this.name = name;}
    public String getPath(){return path;}
    public void setPath(String path){ this.path = path;}
    public double getPrice(){return price;}
    public void setPrice(double price){this.price = price;}
    public int getReleaseYear(){return releaseYear;}
    public void setReleaseYear(int year){this.releaseYear = year;}
    public int getNum(){return num;}
    public void setNum(int num){this.num = num;}

    public int getDateLastChange(){return this.dateLastChange;}
    public void setDateLastChange(int dateLastChange){this.dateLastChange = dateLastChange;}
    public int getTimeLastChange(){return this.timeLastChange;}
    public void setTimeLastChange(int timeLastChange){this.timeLastChange = timeLastChange;}
}
