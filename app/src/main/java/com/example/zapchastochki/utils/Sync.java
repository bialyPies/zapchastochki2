package com.example.zapchastochki.utils;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.Customer;
import com.example.zapchastochki.models.DetailDescr;
import com.example.zapchastochki.models.Order;
import com.example.zapchastochki.models.OrderedDet;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.Date;

public class Sync extends IntentService {
    private OkHttpClient client;
    private DBHelper dbHelper;

    private int lastUpdateDate;
    private int lastUpdateTime;

    public Sync()
    {
        super("Synchronizer");
    }

    public void onCreate() {
        super.onCreate();

        client = new OkHttpClient();
        dbHelper = new DBHelper(getApplicationContext());

        lastUpdateDate = Utils.settings.getValue("lastUpdateDate", 0);
        lastUpdateTime = Utils.settings.getValue("lastUpdateTime", 0);
        /*lastUpdateDate = 0;
        lastUpdateTime = 0;*/
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // чекаем соедиенние с интернетом
        if(Internet.checkInternetConenction(this)) {
            // проверка доступен ли сервер
            if (!Internet.serverIsReachable()){
                //Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            //Toast.makeText(getApplicationContext(), "no2", Toast.LENGTH_SHORT).show();
            return;
        }
        //Toast.makeText(getApplicationContext(), "uppp", Toast.LENGTH_SHORT).show();

        // обновляемся (от независимых к зависимым)
        SyncDetaildescTable();
        SyncCustomerTable();
        SyncOrdersTable();
        SyncOrderedDetTable();

        lastUpdateDate = DateTime.getDateSpecialFormat(new Date());
        Date dateWithoutSomeMinutes = DateTime.addMinutes(new Date(), -2);
        lastUpdateTime = DateTime.getTimeSpecialFormat(dateWithoutSomeMinutes);

        // сохраняем время последнего ообновления
        Utils.settings.setValue("lastUpdateDate", lastUpdateDate);
        Utils.settings.setValue("lastUpdateTime", lastUpdateTime);
    }


    private void SyncDetaildescTable() {
        SyncSomeTable<DetailDescr> syncHelper =
                new SyncSomeTable<>(client, new TypeToken<ArrayList<DetailDescr>>(){});

        ArrayList<DetailDescr> fromServerList = syncHelper.update(lastUpdateDate, lastUpdateTime);
        ArrayList<DetailDescr> collectionToUpdate = new ArrayList();
        ArrayList<DetailDescr> collectionToInsert = new ArrayList();

        for(DetailDescr serverItem : fromServerList){
            ArrayList<DetailDescr> localList = dbHelper.getAllDetailsInLocal(serverItem.getId());
            if (localList.size() > 0){
                DetailDescr item = localList.get(0);
                if(item.getDateLastChange() < serverItem.getDateLastChange()
                        || (item.getDateLastChange() == serverItem.getDateLastChange()
                        && item.getTimeLastChange() < serverItem.getTimeLastChange() )){
                    collectionToUpdate.add(serverItem);
                }
            }else collectionToInsert.add(serverItem);
        }

        //вставляем с сервера
        for(DetailDescr serverItemToInsert : collectionToInsert){
            dbHelper.insertDetailId(serverItemToInsert);
        }
        //update
        for(DetailDescr serverItemToUpdate : collectionToUpdate){
            dbHelper.updateDetail(serverItemToUpdate);
        }

        //to server // передача серверу обновленных данных со стороны телефона
        ArrayList<DetailDescr> localNewList = dbHelper.getNewDetails(lastUpdateDate, lastUpdateTime);
        if(localNewList.size() > 0)
            syncHelper.upgrade(localNewList);
    }
    private void SyncCustomerTable() {
        SyncSomeTable<Customer> syncHelper =
                new SyncSomeTable<>(client, new TypeToken<ArrayList<Customer>>(){});

        ArrayList<Customer> fromServerList = syncHelper.update(lastUpdateDate, lastUpdateTime);
        ArrayList<Customer> collectionToUpdate = new ArrayList();
        ArrayList<Customer> collectionToInsert = new ArrayList();

        //sort data to list
        for(Customer serverItem : fromServerList){
            ArrayList<Customer> localList = dbHelper.getAllCustomerInLocal(serverItem.getId());
            if (localList.size() > 0){
                Customer customer = localList.get(0);
                if(customer.getDateLastChange() < serverItem.getDateLastChange()
                        || (customer.getDateLastChange() == serverItem.getDateLastChange()
                        && customer.getTimeLastChange() < serverItem.getTimeLastChange() )){
                    collectionToUpdate.add(serverItem);
                }
            }else collectionToInsert.add(serverItem);
        }

        for(Customer serverItemToInsert : collectionToInsert){
            dbHelper.insertCustomerId(serverItemToInsert);
        }
        for(Customer serverItemToUpdate : collectionToUpdate){
            dbHelper.updateCustomer(serverItemToUpdate);
        }

        //to server
        ArrayList<Customer> localNewList = dbHelper.getNewCustomerInLocal(lastUpdateDate, lastUpdateTime);
        if(localNewList.size() > 0)
            syncHelper.upgrade(localNewList);
    }
    private void SyncOrdersTable() {
        SyncSomeTable<Order> syncHelper = new SyncSomeTable<>(client, new TypeToken<ArrayList<Order>>(){});

        ArrayList<Order> fromServerList = syncHelper.update(lastUpdateDate, lastUpdateTime);
        ArrayList<Order> collectionToUpdate = new ArrayList();
        ArrayList<Order> collectionToInsert = new ArrayList();

        //sort data to list
        for(Order serverItem : fromServerList){
            ArrayList<Order> localList = dbHelper.getAllOrdersInLocal(serverItem.getId());
            if (localList.size() > 0){
                Order item = localList.get(0);
                if(item.getDateLastChange() < serverItem.getDateLastChange()
                        || (item.getDateLastChange() == serverItem.getDateLastChange()
                        && item.getTimeLastChange() < serverItem.getTimeLastChange() )){
                    collectionToUpdate.add(serverItem);
                }
            }else collectionToInsert.add(serverItem);
        }

        //вставляем с сервера
        for(Order serverItemToInsert : collectionToInsert){
            dbHelper.insertOrderId(serverItemToInsert);
        }
        //update
        for(Order serverItemToUpdate : collectionToUpdate){
            dbHelper.updateOrderCompletionDateAndStatus(serverItemToUpdate);
        }

        //to server // передача серверу обновленных данных со стороны телефона
        ArrayList<Order> localNewList = dbHelper.getNewOrderInLocal(lastUpdateDate, lastUpdateTime);
        if(localNewList.size() > 0)
            syncHelper.upgrade(localNewList);
    }
    private void SyncOrderedDetTable(){
        SyncSomeTable<OrderedDet> syncHelper = new SyncSomeTable<>(client, new TypeToken<ArrayList<OrderedDet>>(){});

        ArrayList<OrderedDet> fromServerList = syncHelper.update(lastUpdateDate, lastUpdateTime);
        ArrayList<OrderedDet> collectionToUpdate = new ArrayList();
        ArrayList<OrderedDet> collectionToInsert = new ArrayList();


        for(OrderedDet serverItem : fromServerList){
            ArrayList<OrderedDet> localList = dbHelper.getAllOrderedDetailsInLocal(serverItem.getId());
            if (localList.size() > 0){
                OrderedDet item = localList.get(0);
                if(item.getDateLastChange() < serverItem.getDateLastChange()
                        || (item.getDateLastChange() == serverItem.getDateLastChange()
                        && item.getTimeLastChange() < serverItem.getTimeLastChange() )){
                    collectionToUpdate.add(serverItem);
                }
            }else collectionToInsert.add(serverItem);
        }

        //вставляем с сервера
        for(OrderedDet serverItemToInsert : collectionToInsert){
            dbHelper.insertOrderedDetailId(serverItemToInsert);
        }
        //update
        for(OrderedDet serverItemToUpdate : collectionToUpdate){
            dbHelper.updateOrderedDetailStatus(serverItemToUpdate);
        }

        //to server // передача серверу обновленных данных со стороны телефона
        ArrayList<OrderedDet> localNewList = dbHelper.getNewOrderedDetInLocal(lastUpdateDate, lastUpdateTime);
        if(localNewList.size() > 0)
            syncHelper.upgrade(localNewList);
    }
}
