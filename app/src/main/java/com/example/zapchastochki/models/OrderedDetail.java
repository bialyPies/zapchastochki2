package com.example.zapchastochki.models;

public class OrderedDetail extends DetailDescr {
    private long idLink;
    private DetailStatus detailStatus;


    public OrderedDetail(){
    }
    public OrderedDetail (long id, String name, String path, double price, int releaseYear,
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

    public long getIdLink(){return this.idLink;}
    public void setIdLink(long idLink){this.idLink = idLink;}
    public DetailStatus getDetailStatus(){return this.detailStatus;}
    public void setDetailStatus(DetailStatus detailStatus){this.detailStatus = detailStatus;}

    public int getDateLastChange(){return this.dateLastChange;}
    public void setDateLastChange(int dateLastChange){this.dateLastChange = dateLastChange;}
    public int getTimeLastChange(){return this.timeLastChange;}
    public void setTimeLastChange(int timeLastChange){this.timeLastChange = timeLastChange;}
}
