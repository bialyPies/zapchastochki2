package com.example.zapchastochki.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.zapchastochki.models.Customer;
import com.example.zapchastochki.models.DetailDescr;
import com.example.zapchastochki.models.DetailStatus;
import com.example.zapchastochki.models.Order;
import com.example.zapchastochki.models.OrderStatus;
import com.example.zapchastochki.models.OrderedDet;
import com.example.zapchastochki.models.OrderedDetail;
import com.example.zapchastochki.utils.DateTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;
    private static final String dbpassword = "password12345678";


//    public void deleteItAfterPlease(){
//        SQLiteDatabase db = this.getWritableDatabase(dbpassword);
//        db.execSQL("CREATE VIEW V_NAMEDORDEREDDETAILS AS\n" +
//                "\tSELECT ORDEREDDETAIL.ID ID, ORDEREDDETAIL.IDORDER IDORDER, ORDEREDDETAIL.IDDETAIL IDDETAIL,\n" +
//                "\t\tORDEREDDETAIL.STATUS STATUS, DETAILDESCR.NAME NAME, DETAILDESCR.IMAGE IMAGE, \n" +
//                "\t\tDETAILDESCR.PRICE PRICE, DETAILDESCR.RELEASEYEAR RELEASEYEAR, DETAILDESCR.NUM NUM\n" +
//                "\tFROM ORDEREDDETAIL \n" +
//                "\t\tJOIN DETAILDESCR ON ORDEREDDETAIL.IDDETAIL = DETAILDESCR.ID;");
//
//    }

    //CUSTOMER
    final String CUSTOMER = "CUSTOMER";
    final String CUSTOMER_ID = "ID";
    final String CUSTOMER_USERNAME = "USERNAME";
    final String CUSTOMER_NAME = "NAME";
    final String CUSTOMER_MOBILE = "MOBILE";
    final String CUSTOMER_PASSWORD = "PASSWORD";
    final String CUSTOMER_ISADMIN = "ISADMIN";
    //ORDERS
    final String ORDERS = "ORDERS";
    final String ORDERS_ID = "ID";
    final String ORDERS_IDCUSTOMER = "IDCUSTOMER";
    final String ORDERS_STATUS = "STATUS";  // ACCEPTED, COMPLETED, PARTIALY_COMPLETED, CANCELED; DEFAULT ACCEPTED
    final String ORDERS_ORDERDATE = "ORDERDATE";
    final String ORDERS_COMPLITIONDATE = "COMPLITIONDATE";
    final String ORDERS_TOTALCOST = "TOTALCOST";
    final String ORDERS_DETAILSNUMBER = "DETAILSNUMBER";
    //DETAILDESCR
    final String DETAILDESCR = "DETAILDESCR";
    final String DETAILDESCR_ID = "ID";
    final String DETAILDESCR_NAME = "NAME";
    final String DETAILDESCR_IMAGE = "IMAGE";
    final String DETAILDESCR_PRICE = "PRICE";
    final String DETAILDESCR_RELEASEYEAR = "RELEASEYEAR";
    final String DETAILDESCR_NUM = "NUM";
    final String DETAILDESCR_ISAVAILABLE = "ISAVAILABLE";
    //BASKET
    final String BASKET = "BASKET";
    final String BASKET_ID = "ID";
    final String BASKET_IDCUSTOMER = "IDCUSTOMER";
    final String BASKET_IDDETAIL = "IDDETAIL";
    //ORDEREDDETAIL
    final String ORDEREDDETAIL = "ORDEREDDETAIL";
    final String ORDEREDDETAIL_ID = "ID";
    final String ORDEREDDETAIL_IDORDER = "IDORDER";
    final String ORDEREDDETAIL_IDDETAIL = "IDDETAIL";
    final String ORDEREDDETAIL_STATUS = "STATUS";
    //"STATUS" TEXT DEFAULT "ORDERED" CHECK(STATUS IN ("NOT_IN_STACK","ARRIVED","ORDERED" denied
    final String V_NAMEDBASKET = "V_NAMEDBASKET";
    final String V_NAMEDORDERS = "V_NAMEDORDERS";
    final String V_NAMEDORDEREDDETAILS = "V_NAMEDORDEREDDETAILS";

    final String DATELASTCHANGE =  "DateLastChange";
    final String TIMELASTCHANGE =  "TimeLastChange";


    public DBHelper(@Nullable Context context) {
        super(context, "dbAnti3", null, 13);
        SQLiteDatabase.loadLibs(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not exists CUSTOMER(\n" +
                "\tID INTEGER PRIMARY KEY,\n" +
                "\tUSERNAME TEXT,\n" +
                "\tNAME TEXT,\n" +
                "\tMOBILE TEXT,\n" +
                "\tPASSWORD TEXT,\n" +
                "\tISADMIN INTEGER\n" +
                "\tCHECK (ISADMIN IN (0,1)) \n" +
                "\tDEFAULT 0,\n" +
                "\tDateLastChange integer,\n" +
                "\tTimeLastChange integer\n" +
                ");"
        );
        db.execSQL("CREATE TABLE if not exists DETAILDESCR(\n" +
                "\tID INTEGER PRIMARY KEY,\n" +
                "\tNAME TEXT NOT NULL,\n" +
                "\tIMAGE TEXT NOT NULL,\n" +
                "\tPRICE NUMERIC NOT NULL,\n" +
                "\tRELEASEYEAR INTEGER CHECK(RELEASEYEAR BETWEEN 1800 AND strftime('%Y', CURRENT_DATE)),\n" +
                "\tNUM INTEGER, \n" +
                "\tISAVAILABLE INTEGER CHECK (ISAVAILABLE IN (0,1)) DEFAULT 1,\n" +
                "\tDateLastChange integer,\n" +
                "\tTimeLastChange integer\n" +
                ");"
        );
        db.execSQL("CREATE TABLE if not exists ORDERS(\n" +
                "\tID INTEGER PRIMARY KEY,\n" +
                "\tIDCUSTOMER INTEGER,\n" +
                "\tSTATUS TEXT CHECK(STATUS IN (\"ACCEPTED\", \"COMPLETED\", \"PARTIALLY_COMPLETED\", \"CANCELED\")) DEFAULT \"ACCEPTED\",\n" +
                "\tORDERDATE TEXT,\n" +
                "\tCOMPLITIONDATE TEXT,\n" +
                "\tTOTALCOST NUM,\n" +
                "\tDETAILSNUMBER INTEGER,\n" +
                "\tDateLastChange integer,\n" +
                "\tTimeLastChange integer,\n" +
                "\tforeign key(IDCUSTOMER) references CUSTOMER(ID) on update cascade on delete cascade\n" +
                ");"
        );
        db.execSQL("CREATE TABLE if not exists BASKET(\n" +
                "\tID INTEGER PRIMARY KEY,\n" +
                "\tIDCUSTOMER INTEGER,\n" +
                "\tIDDETAIL INTEGER,\n" +
                "\tDateLastChange integer,\n" +
                "\tTimeLastChange integer,\n" +
                "\tforeign key(IDDETAIL) references DETAILDESCR(ID) on update cascade on delete cascade,\n" +
                "\tforeign key(IDCUSTOMER) references CUSTOMER(ID) on update cascade on delete cascade\n" +
                ");"
        );
        db.execSQL("CREATE TABLE if not exists ORDEREDDETAIL(\n" +
                "\tID INTEGER PRIMARY KEY,\n" +
                "\tIDORDER INTEGER,\n" +
                "\tIDDETAIL INTEGER,\n" +
                "\tSTATUS TEXT CHECK(STATUS IN (\"NOT_IN_STACK\", \"ARRIVED\", \"ORDERED\", \"DENIED\")) DEFAULT \"ORDERED\",\n" +
                "\tDateLastChange integer,\n" +
                "\tTimeLastChange integer,\n" +
                "\tforeign key(IDORDER) references ORDERS(ID) on update cascade on delete cascade,\n" +
                "\tforeign key(IDDETAIL) references DETAILDESCR(ID) on update cascade on delete cascade\n" +
                ");"
        );

        //ADMIN
        db.execSQL("INSERT INTO CUSTOMER( USERNAME, NAME, MOBILE, PASSWORD, ISADMIN) VALUES\n" +
                "(\"ADMIN\", \"ADMINI\", \"666\", \"ADMIN\", 1);");
        //VIEWS
        db.execSQL("CREATE VIEW if not exists V_NAMEDBASKET AS\n" +
                "\tSELECT BASKET.ID ID, BASKET.IDCUSTOMER IDCUSTOMER, BASKET.IDDETAIL IDDETAIL, \n" +
                "\t\tDETAILDESCR.NAME NAME, DETAILDESCR.IMAGE IMAGE, DETAILDESCR.PRICE PRICE, \n" +
                "\t\tDETAILDESCR.RELEASEYEAR RELEASEYEAR, DETAILDESCR.NUM NUM\n" +
                "\tFROM BASKET \n" +
                "\t\tJOIN DETAILDESCR ON BASKET.IDDETAIL = DETAILDESCR.ID;");

        db.execSQL("CREATE VIEW if not exists V_NAMEDORDERS AS\n" +
                "\tSELECT ORDERS.ID ID, ORDERS.IDCUSTOMER IDCUSTOMER, ORDERS.STATUS STATUS, ORDERS.ORDERDATE ORDERDATE,\n" +
                "\t\tORDERS.COMPLITIONDATE COMPLITIONDATE, ORDERS.TOTALCOST TOTALCOST, ORDERS.DETAILSNUMBER DETAILSNUMBER, \n" +
                "\t\tCUSTOMER.NAME NAME, CUSTOMER.MOBILE MOBILE\n" +
                "\tFROM ORDERS JOIN CUSTOMER ON ORDERS.IDCUSTOMER = CUSTOMER.ID;");

        db.execSQL("CREATE VIEW if not exists V_NAMEDORDEREDDETAILS AS\n" +
                "\tSELECT ORDEREDDETAIL.ID ID, ORDEREDDETAIL.IDORDER IDORDER, ORDEREDDETAIL.IDDETAIL IDDETAIL,\n" +
                "\t\tORDEREDDETAIL.STATUS STATUS, DETAILDESCR.NAME NAME, DETAILDESCR.IMAGE IMAGE, \n" +
                "\t\tDETAILDESCR.PRICE PRICE, DETAILDESCR.RELEASEYEAR RELEASEYEAR, DETAILDESCR.NUM NUM\n" +
                "\tFROM ORDEREDDETAIL \n" +
                "\t\tJOIN DETAILDESCR ON ORDEREDDETAIL.IDDETAIL = DETAILDESCR.ID;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP VIEW if exists V_NAMEDBASKET;");
        db.execSQL("DROP VIEW if exists V_NAMEDORDERS;");
        db.execSQL("DROP VIEW if exists V_NAMEDORDEREDDETAILS;");
        db.execSQL("DROP TABLE ORDEREDDETAIL;");
        db.execSQL("DROP TABLE if exists BASKET;");
        db.execSQL("DROP TABLE CUSTOMER;");
        db.execSQL("DROP TABLE DETAILDESCR;");
        db.execSQL("DROP TABLE ORDERS;");




        onCreate(db);
    }


    @Override
    public void onConfigure(SQLiteDatabase db){
        //db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }


    //last changed
    //setDateTimeLastChanged(values);
    public void setDateTimeLastChanged(ContentValues values){
        if(!values.containsKey("DateLastChange"))
            values.put("DateLastChange", DateTime.getDateSpecialFormat(new Date()));
        if(!values.containsKey("TimeLastChange"))
            values.put("TimeLastChange", DateTime.getTimeSpecialFormat(new Date()));
    }

    //sqlChipher

    public SQLiteDatabase getReadableDatabase() {
        return(super.getReadableDatabase(dbpassword));
    }

    public SQLiteDatabase getWritableDatabase() {
        return(super.getWritableDatabase(dbpassword));
    }



    //customer
    public boolean customerIsExist(String login, String password){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(CUSTOMER, new String[]{CUSTOMER_ID, CUSTOMER_USERNAME,
                        CUSTOMER_NAME, CUSTOMER_MOBILE, CUSTOMER_PASSWORD, CUSTOMER_ISADMIN},
                CUSTOMER_USERNAME + " = ? AND " + CUSTOMER_PASSWORD + " = ?",
                new String[]{login, password}, null, null, null, null);
        if (cursor.moveToFirst()) return true;
        else return false;
    }
    public void selectCustomer(String login, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CUSTOMER, new String[]{CUSTOMER_ID, CUSTOMER_USERNAME,
                CUSTOMER_NAME, CUSTOMER_MOBILE, CUSTOMER_PASSWORD, CUSTOMER_ISADMIN},
                CUSTOMER_USERNAME + " = ? AND " + CUSTOMER_PASSWORD + " = ?",
                new String[]{login, password}, null, null, null, null);
        if (cursor.moveToFirst()){
            Customer.CURRENT_USER.setId(cursor.getLong(cursor.getColumnIndex(CUSTOMER_ID)));
            Customer.CURRENT_USER.setUsername(cursor.getString(cursor.getColumnIndex(CUSTOMER_USERNAME)));
            Customer.CURRENT_USER.setName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
            Customer.CURRENT_USER.setMobile(cursor.getString(cursor.getColumnIndex(CUSTOMER_MOBILE)));
            Customer.CURRENT_USER.setPassword(cursor.getString(cursor.getColumnIndex(CUSTOMER_PASSWORD)));
            Customer.CURRENT_USER.setIsAdmin(cursor.getInt(cursor.getColumnIndex(CUSTOMER_ISADMIN)) == 1 ? true : false);
        } else return;
    }
    public void selectCustomer(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CUSTOMER, new String[]{CUSTOMER_ID, CUSTOMER_USERNAME,
                        CUSTOMER_NAME, CUSTOMER_MOBILE, CUSTOMER_PASSWORD, CUSTOMER_ISADMIN},
                CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor.moveToFirst()){
            Customer.CURRENT_USER.setId(cursor.getLong(cursor.getColumnIndex(CUSTOMER_ID)));
            Customer.CURRENT_USER.setUsername(cursor.getString(cursor.getColumnIndex(CUSTOMER_USERNAME)));
            Customer.CURRENT_USER.setName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
            Customer.CURRENT_USER.setMobile(cursor.getString(cursor.getColumnIndex(CUSTOMER_MOBILE)));
            Customer.CURRENT_USER.setPassword(cursor.getString(cursor.getColumnIndex(CUSTOMER_PASSWORD)));
            Customer.CURRENT_USER.setIsAdmin(cursor.getInt(cursor.getColumnIndex(CUSTOMER_ISADMIN)) == 1 ? true : false);
        } else return;
    }
    public long insertCustomer(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_USERNAME, customer.getUsername());
        values.put(CUSTOMER_PASSWORD, customer.getPassword());
        values.put(CUSTOMER_MOBILE, customer.getMobile());
        values.put(CUSTOMER_NAME, customer.getName());
        setDateTimeLastChanged(values);
        long id = db.insert(CUSTOMER, null, values);
        Customer.CURRENT_USER.setId(id);
        db.close();
        return id;
    }
    public long updateProfile(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NAME, Customer.CURRENT_USER.getName());
        values.put(CUSTOMER_MOBILE, Customer.CURRENT_USER.getMobile());
        setDateTimeLastChanged(values);
        int res = db.update(CUSTOMER, values, CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(Customer.CURRENT_USER.getId())});
        db.close();
        return res;
    }
    //customer sync
    public ArrayList<Customer> getAllCustomerInLocal(long id){
        ArrayList<Customer> customers = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CUSTOMER, new String[]{CUSTOMER_ID, CUSTOMER_USERNAME,
                        CUSTOMER_NAME, CUSTOMER_MOBILE, CUSTOMER_PASSWORD, CUSTOMER_ISADMIN,
                        DATELASTCHANGE, TIMELASTCHANGE},
                CUSTOMER_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                Customer customer = new Customer();
                customer.setId(cursor.getLong(cursor.getColumnIndex(CUSTOMER_ID)));
                customer.setUsername(cursor.getString(cursor.getColumnIndex(CUSTOMER_USERNAME)));
                customer.setName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                customer.setMobile(cursor.getString(cursor.getColumnIndex(CUSTOMER_MOBILE)));
                customer.setPassword(cursor.getString(cursor.getColumnIndex(CUSTOMER_PASSWORD)));
                customer.setIsAdmin(cursor.getInt(cursor.getColumnIndex(CUSTOMER_ISADMIN)) == 1 ? true : false);

                customer.setDateLastChange(cursor.getInt(cursor.getColumnIndex(DATELASTCHANGE)));
                customer.setTimeLastChange(cursor.getInt(cursor.getColumnIndex(TIMELASTCHANGE)));

                customers.add(customer);
            }while (cursor.moveToNext());
        }
        return customers;
    }
    public ArrayList<Customer> getNewCustomerInLocal(int lastUpdateDate, int lastUpdateTime){
        ArrayList<Customer> customers = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CUSTOMER, new String[]{CUSTOMER_ID, CUSTOMER_USERNAME,
                        CUSTOMER_NAME, CUSTOMER_MOBILE, CUSTOMER_PASSWORD, CUSTOMER_ISADMIN,
                        DATELASTCHANGE, TIMELASTCHANGE},
                DATELASTCHANGE + " >= ? or (" + DATELASTCHANGE + " == ? and " + TIMELASTCHANGE +" > ?)",
                new String[]{String.valueOf(lastUpdateDate), String.valueOf(lastUpdateDate),
                        String.valueOf(lastUpdateTime)}, null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                Customer customer = new Customer();
                customer.setId(cursor.getLong(cursor.getColumnIndex(CUSTOMER_ID)));
                customer.setUsername(cursor.getString(cursor.getColumnIndex(CUSTOMER_USERNAME)));
                customer.setName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                customer.setMobile(cursor.getString(cursor.getColumnIndex(CUSTOMER_MOBILE)));
                customer.setPassword(cursor.getString(cursor.getColumnIndex(CUSTOMER_PASSWORD)));
                customer.setIsAdmin(cursor.getInt(cursor.getColumnIndex(CUSTOMER_ISADMIN)) == 1 ? true : false);

                customer.setDateLastChange(cursor.getInt(cursor.getColumnIndex(DATELASTCHANGE)));
                customer.setTimeLastChange(cursor.getInt(cursor.getColumnIndex(TIMELASTCHANGE)));

                customers.add(customer);
            }while (cursor.moveToNext());
        }
        return customers;
    }
    public long insertCustomerId(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_ID, customer.getId());
        values.put(CUSTOMER_USERNAME, customer.getUsername());
        values.put(CUSTOMER_PASSWORD, customer.getPassword());
        values.put(CUSTOMER_MOBILE, customer.getMobile());
        values.put(CUSTOMER_NAME, customer.getName());
        values.put(CUSTOMER_ISADMIN, customer.getIsAmin() ? 1 : 0);
        setDateTimeLastChanged(values);
        long id = db.insert(CUSTOMER, null, values);
        //Customer.CURRENT_USER.setId(id);
        db.close();
        return id;
    }
    public long updateCustomer(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NAME, customer.getName());
        values.put(CUSTOMER_MOBILE, customer.getMobile());
        setDateTimeLastChanged(values);
        int res = db.update(CUSTOMER, values, CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer.getId())});
        db.close();
        return res;
    }
    //Detail
    public long insertDetail(DetailDescr detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DETAILDESCR_NAME, detail.getName());
        values.put(DETAILDESCR_PRICE, detail.getPrice());
        values.put(DETAILDESCR_IMAGE, detail.getPath());
        values.put(DETAILDESCR_RELEASEYEAR, String.valueOf(detail.getReleaseYear()));
        values.put(DETAILDESCR_NUM, detail.getNum());
        setDateTimeLastChanged(values);
        long id = db.insert(DETAILDESCR, null, values);
        db.close();
        return id;
    }
    public long insertDetailId(DetailDescr detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DETAILDESCR_ID, detail.getId());
        values.put(DETAILDESCR_NAME, detail.getName());
        values.put(DETAILDESCR_PRICE, detail.getPrice());
        values.put(DETAILDESCR_IMAGE, detail.getPath());
        values.put(DETAILDESCR_RELEASEYEAR, String.valueOf(detail.getReleaseYear()));
        values.put(DETAILDESCR_NUM, detail.getNum());
        setDateTimeLastChanged(values);
        long id = db.insert(DETAILDESCR, null, values);
        db.close();
        return id;
    }
    public int deleteDetail(long id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DETAILDESCR_ISAVAILABLE, 0);
        setDateTimeLastChanged(values);

        int res = db.update(DETAILDESCR, values, DETAILDESCR_ID + " = ?",
                new String[]{String.valueOf(id)});
        /*int res = db.delete(DETAILDESCR, DETAILDESCR_ID + " = ?",
                new String[]{String.valueOf(id)});*/
        db.close();
        return res;
    }
    public int updateDetail(DetailDescr detail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DETAILDESCR_NAME, detail.getName());
        values.put(DETAILDESCR_PRICE, detail.getPrice());
        values.put(DETAILDESCR_IMAGE, detail.getPath());
        values.put(DETAILDESCR_RELEASEYEAR, String.valueOf(detail.getReleaseYear()));
        values.put(DETAILDESCR_NUM, detail.getNum());
        setDateTimeLastChanged(values);
        int res = db.update(DETAILDESCR, values, DETAILDESCR_ID + " = ?",
                new String[]{String.valueOf(detail.getId())});
        db.close();
        return res;
    }
    public ArrayList<DetailDescr> getAllDetails(){
        ArrayList<DetailDescr> details = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(DETAILDESCR, new String[]{DETAILDESCR_ID, DETAILDESCR_NAME,
                        DETAILDESCR_PRICE, DETAILDESCR_IMAGE, DETAILDESCR_RELEASEYEAR,
                        DETAILDESCR_NUM, DATELASTCHANGE, TIMELASTCHANGE},
                DETAILDESCR_ISAVAILABLE + " = 1", null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                DetailDescr detail = new DetailDescr();
                detail.setId(cursor.getLong(cursor.getColumnIndex(DETAILDESCR_ID)));
                detail.setName(cursor.getString(cursor.getColumnIndex(DETAILDESCR_NAME)));
                detail.setPrice(cursor.getDouble(cursor.getColumnIndex(DETAILDESCR_PRICE)));
                detail.setPath(cursor.getString(cursor.getColumnIndex(DETAILDESCR_IMAGE)));
                detail.setReleaseYear(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DETAILDESCR_RELEASEYEAR))));
                detail.setNum(cursor.getInt(cursor.getColumnIndex(DETAILDESCR_NUM)));
                detail.setDateLastChange(cursor.getInt(cursor.getColumnIndex(DATELASTCHANGE)));
                detail.setTimeLastChange(cursor.getInt(cursor.getColumnIndex(TIMELASTCHANGE)));

                details.add(detail);
            } while (cursor.moveToNext());
        }

        return details;
    }
    //sync detdes
    public ArrayList<DetailDescr> getAllDetailsInLocal(long id){
        ArrayList<DetailDescr> details = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(DETAILDESCR, new String[]{DETAILDESCR_ID, DETAILDESCR_NAME,
                        DETAILDESCR_PRICE, DETAILDESCR_IMAGE, DETAILDESCR_RELEASEYEAR,
                        DETAILDESCR_NUM, DATELASTCHANGE, TIMELASTCHANGE},
                DETAILDESCR_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                DetailDescr detail = new DetailDescr();
                detail.setId(cursor.getLong(cursor.getColumnIndex(DETAILDESCR_ID)));
                detail.setName(cursor.getString(cursor.getColumnIndex(DETAILDESCR_NAME)));
                detail.setPrice(cursor.getDouble(cursor.getColumnIndex(DETAILDESCR_PRICE)));
                detail.setPath(cursor.getString(cursor.getColumnIndex(DETAILDESCR_IMAGE)));
                detail.setReleaseYear(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DETAILDESCR_RELEASEYEAR))));
                detail.setNum(cursor.getInt(cursor.getColumnIndex(DETAILDESCR_NUM)));
                detail.setDateLastChange(cursor.getInt(cursor.getColumnIndex(DATELASTCHANGE)));
                detail.setTimeLastChange(cursor.getInt(cursor.getColumnIndex(TIMELASTCHANGE)));

                details.add(detail);
            } while (cursor.moveToNext());
        }

        return details;
    }
    public ArrayList<DetailDescr> getNewDetails(int lastUpdateDate, int lastUpdateTime){
        ArrayList<DetailDescr> details = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(DETAILDESCR, new String[]{DETAILDESCR_ID, DETAILDESCR_NAME,
                        DETAILDESCR_PRICE, DETAILDESCR_IMAGE, DETAILDESCR_RELEASEYEAR,
                        DETAILDESCR_NUM, DATELASTCHANGE, TIMELASTCHANGE},
                DATELASTCHANGE + " >= ? or (" + DATELASTCHANGE + " == ? and " + TIMELASTCHANGE +" > ?)",
                new String[]{String.valueOf(lastUpdateDate), String.valueOf(lastUpdateDate),
                        String.valueOf(lastUpdateTime)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                DetailDescr detail = new DetailDescr();
                detail.setId(cursor.getLong(cursor.getColumnIndex(DETAILDESCR_ID)));
                detail.setName(cursor.getString(cursor.getColumnIndex(DETAILDESCR_NAME)));
                detail.setPrice(cursor.getDouble(cursor.getColumnIndex(DETAILDESCR_PRICE)));
                detail.setPath(cursor.getString(cursor.getColumnIndex(DETAILDESCR_IMAGE)));
                detail.setReleaseYear(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DETAILDESCR_RELEASEYEAR))));
                detail.setNum(cursor.getInt(cursor.getColumnIndex(DETAILDESCR_NUM)));
                detail.setDateLastChange(cursor.getInt(cursor.getColumnIndex(DATELASTCHANGE)));
                detail.setTimeLastChange(cursor.getInt(cursor.getColumnIndex(TIMELASTCHANGE)));

                details.add(detail);
            } while (cursor.moveToNext());
        }

        return details;
    }

    //Basket
    public ArrayList<DetailDescr> getBasketById(long id){
        ArrayList<DetailDescr> details = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        //SELECT IDDETAIL, NAME, PRICE, RELEASEYEAR, NUM FROM V_NAMEDBASKET WHERE IDCUSTOMER = 2;
        Cursor cursor = db.query(V_NAMEDBASKET, new String[]{BASKET_IDDETAIL, DETAILDESCR_NAME,
                        DETAILDESCR_IMAGE, DETAILDESCR_PRICE, DETAILDESCR_RELEASEYEAR,
                        DETAILDESCR_NUM},
                BASKET_IDCUSTOMER + " = ?",
                new String[]{String.valueOf(id)}, null, null,
                null, null);
        if (cursor.moveToFirst()) {
            do {
                DetailDescr detail = new DetailDescr();
                detail.setId(cursor.getLong(cursor.getColumnIndex(BASKET_IDDETAIL)));
                detail.setName(cursor.getString(cursor.getColumnIndex(DETAILDESCR_NAME)));
                detail.setPrice(cursor.getDouble(cursor.getColumnIndex(DETAILDESCR_PRICE)));
                detail.setPath(cursor.getString(cursor.getColumnIndex(DETAILDESCR_IMAGE)));
                detail.setReleaseYear(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DETAILDESCR_RELEASEYEAR))));
                detail.setNum(cursor.getInt(cursor.getColumnIndex(DETAILDESCR_NUM)));

                details.add(detail);
            } while (cursor.moveToNext());
        }
        return details;
    }
    public boolean isExistInBasket(long idCustomer, long idDetail){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(V_NAMEDBASKET, new String[]{BASKET_ID},
                BASKET_IDCUSTOMER + " = ? AND " + BASKET_IDDETAIL + " = ?",
                new String[]{String.valueOf(idCustomer), String.valueOf(idDetail)},
                null, null, null, null);
        if (cursor.getCount() > 0) return true;
        else return false;
    }
    public long addToBasket(long idCustomer, long idDetail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BASKET_IDCUSTOMER, idCustomer);
        values.put(BASKET_IDDETAIL, idDetail);
        long id = db.insert(BASKET, null, values);
        db.close();
        return id;
    }
    public int deleteFromBasket(long idDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(BASKET,
                BASKET_IDCUSTOMER + " = ? AND " + BASKET_IDDETAIL + " = ?",
                new String[]{String.valueOf(Customer.CURRENT_USER.getId()), String.valueOf(idDetail)});
        db.close();
        return res;
    }
    //order
    public long createOrder(double totalCost, int totalNum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDERS_IDCUSTOMER, Customer.CURRENT_USER.getId());
        values.put(ORDERS_ORDERDATE, String.valueOf(LocalDate.now()));
        values.put(ORDERS_TOTALCOST, totalCost);
        values.put(ORDERS_DETAILSNUMBER, totalNum);
        setDateTimeLastChanged(values);
        long id = db.insert(ORDERS, null, values);
        db.close();
        return id;
    }
    public long addToOrder(long idDetail, long idOrder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDEREDDETAIL_IDDETAIL, idDetail);
        values.put(ORDEREDDETAIL_IDORDER, idOrder);
        setDateTimeLastChanged(values);
        long id = db.insert(ORDEREDDETAIL, null, values);
        db.close();
        return id;
    }
    public ArrayList<Order> getOrdersById(long idCustomer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Order> orders = new ArrayList<>();
        // ID,  IDCUSTOMER, STATUS, ORDERDATE,\n" +
        // COMPLITIONDATE, ORDERS.TOTALCOST TOTALCOST, ORDERS.DETAILSNUMBER DETAILSNUMBER, \n" +
        // CUSTOMER.NAME NAME, CUSTOMER.MOBILE MOBILE\n" +
        Cursor cursor = db.query(V_NAMEDORDERS,
                new String[]{ORDERS_ID, ORDERS_IDCUSTOMER, ORDERS_STATUS, ORDERS_ORDERDATE,
                        ORDERS_COMPLITIONDATE, ORDERS_TOTALCOST, ORDERS_DETAILSNUMBER,
                        CUSTOMER_NAME, CUSTOMER_MOBILE},
                ORDERS_IDCUSTOMER + " = ?", new String[]{String.valueOf(idCustomer)},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getLong(cursor.getColumnIndex(ORDERS_ID)));
                order.setIdCustomer(cursor.getLong(cursor.getColumnIndex(ORDERS_IDCUSTOMER)));
                order.setOrderStatus(OrderStatus.valueOf(cursor.getString(cursor.getColumnIndex(ORDERS_STATUS))));
                order.setOrderDate(cursor.getString(cursor.getColumnIndex(ORDERS_ORDERDATE)));
                if(cursor.getString(cursor.getColumnIndex(ORDERS_COMPLITIONDATE)) != null)
                    order.setCompletionDate(cursor.getString(cursor.getColumnIndex(ORDERS_COMPLITIONDATE)));
                order.setTotalCost(cursor.getDouble(cursor.getColumnIndex(ORDERS_TOTALCOST)));
                order.setTotalNumber(cursor.getInt(cursor.getColumnIndex(ORDERS_DETAILSNUMBER)));
                order.setCustomer(new Customer(order.getIdCustomer(),
                        cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndex(CUSTOMER_MOBILE))));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        return orders;
    }
    public ArrayList<Order> getOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Order> orders = new ArrayList<>();
        Cursor cursor = db.query(V_NAMEDORDERS,
                new String[]{ORDERS_ID, ORDERS_IDCUSTOMER, ORDERS_STATUS, ORDERS_ORDERDATE,
                        ORDERS_COMPLITIONDATE, ORDERS_TOTALCOST, ORDERS_DETAILSNUMBER,
                        CUSTOMER_NAME, CUSTOMER_MOBILE},
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getLong(cursor.getColumnIndex(ORDERS_ID)));
                order.setIdCustomer(cursor.getLong(cursor.getColumnIndex(ORDERS_IDCUSTOMER)));
                order.setOrderStatus(OrderStatus.valueOf(cursor.getString(cursor.getColumnIndex(ORDERS_STATUS))));
                order.setOrderDate(cursor.getString(cursor.getColumnIndex(ORDERS_ORDERDATE)));
                if(cursor.getString(cursor.getColumnIndex(ORDERS_COMPLITIONDATE)) != null)
                    order.setCompletionDate(cursor.getString(cursor.getColumnIndex(ORDERS_COMPLITIONDATE)));
                order.setTotalCost(cursor.getDouble(cursor.getColumnIndex(ORDERS_TOTALCOST)));
                order.setTotalNumber(cursor.getInt(cursor.getColumnIndex(ORDERS_DETAILSNUMBER)));
                order.setCustomer(new Customer(order.getIdCustomer(),
                        cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndex(CUSTOMER_MOBILE))));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        return orders;
    }
    public ArrayList<DetailDescr> getDetailsInOrder(long idOrder){
        ArrayList<DetailDescr> details = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(V_NAMEDORDEREDDETAILS,
                new String[]{ORDEREDDETAIL_ID, ORDEREDDETAIL_IDORDER, ORDEREDDETAIL_IDDETAIL,
                ORDEREDDETAIL_STATUS, DETAILDESCR_NAME, DETAILDESCR_IMAGE, DETAILDESCR_PRICE,
                DETAILDESCR_RELEASEYEAR, DETAILDESCR_NUM}, ORDEREDDETAIL_IDORDER + " = ?",
                new String[]{String.valueOf(idOrder)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                DetailDescr detail = new DetailDescr();

                detail.setId(cursor.getLong(cursor.getColumnIndex(ORDEREDDETAIL_IDDETAIL)));
                detail.setName(cursor.getString(cursor.getColumnIndex(DETAILDESCR_NAME)));
                detail.setPrice(cursor.getDouble(cursor.getColumnIndex(DETAILDESCR_PRICE)));
                detail.setPath(cursor.getString(cursor.getColumnIndex(DETAILDESCR_IMAGE)));
                detail.setReleaseYear(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DETAILDESCR_RELEASEYEAR))));
                detail.setNum(cursor.getInt(cursor.getColumnIndex(DETAILDESCR_NUM)));
                //detail.setStatus(DetailStatus.valueOf(cursor.getString(cursor.getColumnIndex(ORDEREDDETAIL_STATUS))));

                details.add(detail);
            } while (cursor.moveToNext());
        }
        return details;
    }
    public ArrayList<OrderedDetail> getOrderedDetailsInOrder(long idOrder){
        ArrayList<OrderedDetail> details = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(V_NAMEDORDEREDDETAILS,
                new String[]{ORDEREDDETAIL_ID, ORDEREDDETAIL_IDORDER, ORDEREDDETAIL_IDDETAIL,
                        ORDEREDDETAIL_STATUS, DETAILDESCR_NAME, DETAILDESCR_IMAGE, DETAILDESCR_PRICE,
                        DETAILDESCR_RELEASEYEAR, DETAILDESCR_NUM}, ORDEREDDETAIL_IDORDER + " = ?",
                new String[]{String.valueOf(idOrder)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                OrderedDetail detail = new OrderedDetail();

                detail.setIdLink(cursor.getLong(cursor.getColumnIndex(ORDEREDDETAIL_ID)));
                detail.setId(cursor.getLong(cursor.getColumnIndex(ORDEREDDETAIL_IDDETAIL)));
                detail.setName(cursor.getString(cursor.getColumnIndex(DETAILDESCR_NAME)));
                detail.setPrice(cursor.getDouble(cursor.getColumnIndex(DETAILDESCR_PRICE)));
                detail.setPath(cursor.getString(cursor.getColumnIndex(DETAILDESCR_IMAGE)));
                detail.setReleaseYear(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DETAILDESCR_RELEASEYEAR))));
                detail.setNum(cursor.getInt(cursor.getColumnIndex(DETAILDESCR_NUM)));
                detail.setDetailStatus(DetailStatus.valueOf(cursor.getString(cursor.getColumnIndex(ORDEREDDETAIL_STATUS))));

                details.add(detail);
            } while (cursor.moveToNext());
        }
        return details;
    }
    //order sync
    public ArrayList<Order> getAllOrdersInLocal(long id){
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(ORDERS, new String[]{ORDERS_ID, ORDERS_IDCUSTOMER,
                        ORDERS_STATUS, ORDERS_ORDERDATE, ORDERS_COMPLITIONDATE,
                        ORDERS_TOTALCOST, ORDERS_DETAILSNUMBER, DATELASTCHANGE, TIMELASTCHANGE},
                ORDERS_ID + " = ? ", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getLong(cursor.getColumnIndex(ORDERS_ID)));
                order.setIdCustomer(cursor.getLong(cursor.getColumnIndex(ORDERS_IDCUSTOMER)));
                order.setOrderStatus(OrderStatus.valueOf(cursor.getString(cursor.getColumnIndex(ORDERS_STATUS))));
                order.setOrderDate(cursor.getString(cursor.getColumnIndex(ORDERS_ORDERDATE)));
                if(cursor.getString(cursor.getColumnIndex(ORDERS_COMPLITIONDATE)) != null)
                    order.setCompletionDate(cursor.getString(cursor.getColumnIndex(ORDERS_COMPLITIONDATE)));
                order.setTotalCost(cursor.getDouble(cursor.getColumnIndex(ORDERS_TOTALCOST)));
                order.setTotalNumber(cursor.getInt(cursor.getColumnIndex(ORDERS_DETAILSNUMBER)));
                order.setDateLastChange(cursor.getInt(cursor.getColumnIndex(DATELASTCHANGE)));
                order.setTimeLastChange(cursor.getInt(cursor.getColumnIndex(TIMELASTCHANGE)));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        return orders;
    }
    public long insertOrderId(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
            // ORDERS_DETAILSNUMBER, DATELASTCHANGE, TIMELASTCHANGE
        values.put(ORDERS_ID, order.getId());
        values.put(ORDERS_IDCUSTOMER, order.getIdCustomer());
        values.put(ORDERS_STATUS, String.valueOf(order.getOrderStatus()));
        values.put(ORDERS_ORDERDATE, order.getOrderDate());
        values.put(ORDERS_COMPLITIONDATE, order.getCompletionDate());
        values.put(ORDERS_TOTALCOST, order.getTotalCost());
        values.put(ORDERS_DETAILSNUMBER, order.getTotalNumber());
        setDateTimeLastChanged(values);
        long id = db.insert(ORDERS, null, values);
        db.close();
        return id;
    }
    public int updateOrderCompletionDateAndStatus(Order order){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDERS_STATUS, String.valueOf(order.getOrderStatus()));
        values.put(ORDERS_COMPLITIONDATE, order.getCompletionDate());
        setDateTimeLastChanged(values);

        int res = db.update(ORDERS, values, ORDERS_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
        db.close();
        return res;
    }
    public ArrayList<Order> getNewOrderInLocal(int lastUpdateDate, int lastUpdateTime){
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(ORDERS, new String[]{ORDERS_ID, ORDERS_IDCUSTOMER,
                        ORDERS_STATUS, ORDERS_ORDERDATE, ORDERS_COMPLITIONDATE,
                        ORDERS_TOTALCOST, ORDERS_DETAILSNUMBER, DATELASTCHANGE, TIMELASTCHANGE},
                DATELASTCHANGE + " >= ? or (" + DATELASTCHANGE + " == ? and " + TIMELASTCHANGE +" > ?)",
                new String[]{String.valueOf(lastUpdateDate), String.valueOf(lastUpdateDate),
                        String.valueOf(lastUpdateTime)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getLong(cursor.getColumnIndex(ORDERS_ID)));
                order.setIdCustomer(cursor.getLong(cursor.getColumnIndex(ORDERS_IDCUSTOMER)));
                order.setOrderStatus(OrderStatus.valueOf(cursor.getString(cursor.getColumnIndex(ORDERS_STATUS))));
                order.setOrderDate(cursor.getString(cursor.getColumnIndex(ORDERS_ORDERDATE)));
                if(cursor.getString(cursor.getColumnIndex(ORDERS_COMPLITIONDATE)) != null)
                    order.setCompletionDate(cursor.getString(cursor.getColumnIndex(ORDERS_COMPLITIONDATE)));
                order.setTotalCost(cursor.getDouble(cursor.getColumnIndex(ORDERS_TOTALCOST)));
                order.setTotalNumber(cursor.getInt(cursor.getColumnIndex(ORDERS_DETAILSNUMBER)));
                order.setDateLastChange(cursor.getInt(cursor.getColumnIndex(DATELASTCHANGE)));
                order.setTimeLastChange(cursor.getInt(cursor.getColumnIndex(TIMELASTCHANGE)));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        return orders;
    }

    //orderedDet sync
    public ArrayList<OrderedDet> getAllOrderedDetailsInLocal(long id){
        ArrayList<OrderedDet> orderedDets = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(ORDEREDDETAIL, new String[]{ORDEREDDETAIL_ID,
                ORDEREDDETAIL_IDORDER, ORDEREDDETAIL_IDDETAIL, ORDEREDDETAIL_STATUS,
                DATELASTCHANGE, TIMELASTCHANGE},ORDEREDDETAIL_ID + " = ?",
                new String[]{String.valueOf(id)}, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                OrderedDet orderedDet = new OrderedDet();
                orderedDet.setId(cursor.getLong(cursor.getColumnIndex(ORDEREDDETAIL_ID)));
                orderedDet.setIdOrder(cursor.getLong(cursor.getColumnIndex(ORDEREDDETAIL_IDORDER)));
                orderedDet.setIdDetail(cursor.getLong(cursor.getColumnIndex(ORDEREDDETAIL_IDDETAIL)));
                orderedDet.setDetailStatus(DetailStatus.valueOf(cursor.getString(cursor.getColumnIndex(ORDEREDDETAIL_STATUS))));
                orderedDet.setDateLastChange(cursor.getInt(cursor.getColumnIndex(DATELASTCHANGE)));
                orderedDet.setTimeLastChange(cursor.getInt(cursor.getColumnIndex(TIMELASTCHANGE)));
                orderedDets.add(orderedDet);
            } while (cursor.moveToNext());
        }
        return orderedDets;
    }
    public long insertOrderedDetailId(OrderedDet element){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ORDEREDDETAIL_ID, element.getId());
        values.put(ORDEREDDETAIL_IDORDER, element.getIdOrder());
        values.put(ORDEREDDETAIL_IDDETAIL, element.getIdDetail());
        values.put(ORDEREDDETAIL_STATUS, String.valueOf(element.getDetailStatus()));
        setDateTimeLastChanged(values);

        long id = db.insert(ORDEREDDETAIL, null, values);
        db.close();
        return id;
    }
    public int updateOrderedDetailStatus(OrderedDet element){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDEREDDETAIL_STATUS, String.valueOf(element.getDetailStatus()));
        setDateTimeLastChanged(values);

        int res = db.update(ORDERS, values, ORDERS_ID + " = ?",
                new String[]{String.valueOf(element.getId())});
        db.close();
        return res;
    }
    public ArrayList<OrderedDet> getNewOrderedDetInLocal(int lastUpdateDate, int lastUpdateTime){
        ArrayList<OrderedDet> elements = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(ORDEREDDETAIL, new String[]{ORDEREDDETAIL_ID,
                        ORDEREDDETAIL_IDORDER, ORDEREDDETAIL_IDDETAIL, ORDEREDDETAIL_STATUS,
                        DATELASTCHANGE, TIMELASTCHANGE},
                DATELASTCHANGE + " >= ? or (" + DATELASTCHANGE + " == ? and " + TIMELASTCHANGE +" > ?)",
                new String[]{String.valueOf(lastUpdateDate), String.valueOf(lastUpdateDate),
                        String.valueOf(lastUpdateTime)}, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                OrderedDet orderedDet = new OrderedDet();
                orderedDet.setId(cursor.getLong(cursor.getColumnIndex(ORDEREDDETAIL_ID)));
                orderedDet.setIdOrder(cursor.getLong(cursor.getColumnIndex(ORDEREDDETAIL_IDORDER)));
                orderedDet.setIdDetail(cursor.getLong(cursor.getColumnIndex(ORDEREDDETAIL_IDDETAIL)));
                orderedDet.setDetailStatus(DetailStatus.valueOf(cursor.getString(cursor.getColumnIndex(ORDEREDDETAIL_STATUS))));
                orderedDet.setDateLastChange(cursor.getInt(cursor.getColumnIndex(DATELASTCHANGE)));
                orderedDet.setTimeLastChange(cursor.getInt(cursor.getColumnIndex(TIMELASTCHANGE)));
                elements.add(orderedDet);
            } while (cursor.moveToNext());
        }
        return elements;
    }

    //во вкладке админа → заказы → список деталей
    public int updateDetailStatus(OrderedDetail orderedDetail, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDEREDDETAIL_STATUS, status);
        setDateTimeLastChanged(values);

        int res = db.update(ORDEREDDETAIL, values, ORDEREDDETAIL_ID + " = ?",
                new String[]{String.valueOf(orderedDetail.getIdLink())});
        db.close();
        return res;
    }
    public int updateOrderStatus(Order order, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDERS_STATUS, status);
        setDateTimeLastChanged(values);

        int res = db.update(ORDERS, values, ORDERS_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
        db.close();
        return res;
    }
    public int updateOrderCompletionDate(long idOrder, LocalDate completionDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDERS_COMPLITIONDATE, String.valueOf(completionDate));
        setDateTimeLastChanged(values);

        int res = db.update(ORDERS, values, ORDERS_ID + " = ?",
                new String[]{String.valueOf(idOrder)});
        db.close();
        return res;
    }
}
