package com.example.zapchastochki.utils;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.Customer;
import com.example.zapchastochki.models.Order;
import com.example.zapchastochki.models.OrderedDet;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.Date;

public class SyncStatuses extends IntentService {
    private OkHttpClient client;
    private DBHelper dbHelper;

    private int lastUpdateDate;
    private int lastUpdateTime;

    public SyncStatuses()
    {
        super("Synchronizer");
    }

    public void onCreate() {
        super.onCreate();

        client = new OkHttpClient();
        dbHelper = new DBHelper(getApplicationContext());

        lastUpdateDate = Utils.settings.getValue("lastUpdateDate", 0);
        lastUpdateTime = Utils.settings.getValue("lastUpdateTime", 0);
    }

    protected void onHandleIntent(@Nullable Intent intent) {

        if(Internet.checkInternetConenction(this)) {
            if (!Internet.serverIsReachable()){
                return;
            }
        }
        else{
            return;
        }

        //syncDetailStatus();
        syncOrderStatus();

        lastUpdateDate = DateTime.getDateSpecialFormat(new Date());
        Date dateWithoutSomeMinutes = DateTime.addMinutes(new Date(), -2);
        lastUpdateTime = DateTime.getTimeSpecialFormat(dateWithoutSomeMinutes);

        // сохраняем время последнего ообновления
        Utils.settings.setValue("lastUpdateDate", lastUpdateDate);
        Utils.settings.setValue("lastUpdateTime", lastUpdateTime);
    }
    private void syncOrderStatus(){
        SyncSomeTable<Order> syncHelper = new SyncSomeTable<>(client, new TypeToken<ArrayList<Order>>(){});

        ArrayList<Order> fromServerList = syncHelper.updateSratuses(lastUpdateDate, lastUpdateTime, Customer.CURRENT_USER.getId());
        ArrayList<Order> collectionToUpdate = new ArrayList();

        for(Order serverItem : fromServerList){
            ArrayList<Order> localList = dbHelper.getOrdersById(Customer.CURRENT_USER.getId());
            if (localList.size() > 0){
                Order item = localList.get(0);
                if(item.getDateLastChange() < serverItem.getDateLastChange()
                        || (item.getDateLastChange() == serverItem.getDateLastChange()
                        && item.getTimeLastChange() < serverItem.getTimeLastChange() )){
                    collectionToUpdate.add(serverItem);
                }
            }
        }

        for(Order serverItemToUpdate : collectionToUpdate){
            dbHelper.updateOrderCompletionDateAndStatus(serverItemToUpdate);
        }
    }
    // у детали ид заказа отправлять //циклом пройтись по всем в листе
    private void syncDetailStatus() {
        SyncSomeTable<OrderedDet> syncHelper = new SyncSomeTable<>(client, new TypeToken<ArrayList<OrderedDet>>(){});

        ArrayList<OrderedDet> fromServerList = syncHelper.update(lastUpdateDate, lastUpdateTime);
        ArrayList<Order> collectionToUpdate = new ArrayList();

        for(OrderedDet serverItem : fromServerList){
            ArrayList<OrderedDet> localList = dbHelper.getAllOrderedDetailsInLocal(serverItem.getId());
            if (localList.size() > 0){
                OrderedDet item = localList.get(0);
                if(item.getDateLastChange() < serverItem.getDateLastChange()
                        || (item.getDateLastChange() == serverItem.getDateLastChange()
                        && item.getTimeLastChange() < serverItem.getTimeLastChange() )){
                    //collectionToUpdate.add(serverItem);
                }
            }
        }
        //update
        /*for(OrderedDet serverItemToUpdate : collectionToUpdate){
            dbHelper.updateOrderedDetailStatus(serverItemToUpdate);
        }*/
    }
}
