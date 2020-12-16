package com.example.zapchastochki.models;

public class OrderedDet {
    private long id;
    private long idOrder;
    private long idDetail;
    private DetailStatus detailStatus;
    private int dateLastChange;
    private int timeLastChange;

    public OrderedDet(){}
    public OrderedDet(long id, long idOrder, long idDetail, DetailStatus detailStatus,
                      int dateLastChange, int timeLastChange){
        this.id = id;
        this.idDetail = idDetail;
        this.idOrder = idOrder;
        this.detailStatus = detailStatus;
        this.dateLastChange = dateLastChange;
        this.timeLastChange = timeLastChange;
    }

    public long getId(){return id;}
    public void setId(long id){this.id = id;}
    public long getIdOrder(){return idOrder;}
    public void setIdOrder(long idOrder){this.idOrder = idOrder;}
    public long getIdDetail(){return idDetail;}
    public void setIdDetail(long idDetail){this.idDetail = idDetail;}
    public DetailStatus getDetailStatus(){return detailStatus;}
    public void setDetailStatus(DetailStatus detailStatus){this.detailStatus = detailStatus;}
    public int getDateLastChange(){return this.dateLastChange;}
    public void setDateLastChange(int dateLastChange){this.dateLastChange = dateLastChange;}
    public int getTimeLastChange(){return this.timeLastChange;}
    public void setTimeLastChange(int timeLastChange){this.timeLastChange = timeLastChange;}
}
