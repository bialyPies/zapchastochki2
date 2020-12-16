package com.example.zapchastochki.utils;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.zapchastochki.MainActivity;
import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.Customer;
import com.example.zapchastochki.models.DetailDescr;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.Date;

public class SyncOnRegistration extends IntentService {
    private OkHttpClient client;
    private DBHelper dbHelper;

    private int lastUpdateDate;
    private int lastUpdateTime;

    public SyncOnRegistration()
    {
        super("RegSynchronizer");
    }

    public void onCreate() {
        super.onCreate();

        client = new OkHttpClient();
        dbHelper = new DBHelper(getApplicationContext());

        lastUpdateDate = Utils.settings.getValue("lastUpdateDate", 0);
        lastUpdateTime = Utils.settings.getValue("lastUpdateTime", 0);
//        lastUpdateDate = 0;
//        lastUpdateTime = 0;
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(Internet.checkInternetConenction(this)) {
            if (!Internet.serverIsReachable()){
                return;
            }
        }
        else{
            return;
        }

        // обновляемся (от независимых к зависимым)
        SyncCustomerTable();

        //чтобы потом обновить при синхронизации
//        lastUpdateDate = DateTime.getDateSpecialFormat(new Date());
//        Date dateWithoutSomeMinutes = DateTime.addMinutes(new Date(), -2);
//        lastUpdateTime = DateTime.getTimeSpecialFormat(dateWithoutSomeMinutes);
//
//        // сохраняем время последнего ообновления
//        Utils.settings.setValue("lastUpdateDate", lastUpdateDate);
//        Utils.settings.setValue("lastUpdateTime", lastUpdateTime);
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

        //вставляем с сервера
        for(Customer serverItemToInsert : collectionToInsert){
            dbHelper.insertCustomerId(serverItemToInsert);
        }
        //update
        for(Customer serverItemToUpdate : collectionToUpdate){
            dbHelper.updateCustomer(serverItemToUpdate);
        }

        //insert new registered user
        long id = dbHelper.insertCustomer(Customer.CURRENT_USER);
        if (id != 0) {
            //to server // передача серверу обновленных данных со стороны телефона
            ArrayList<Customer> localNewList = dbHelper.getNewCustomerInLocal(lastUpdateDate, lastUpdateTime);
            if(localNewList.size() > 0)
                syncHelper.upgrade(localNewList);
        }
    }
}
