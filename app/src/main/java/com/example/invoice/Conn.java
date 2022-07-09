package com.example.invoice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLSyntaxErrorException;

public class Conn extends SQLiteOpenHelper {
    private static final String databasename = "invoice.db";

    public Conn(@Nullable Context context) {
        super(context, databasename, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE shop_details ( id INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL," +
                "gstno  VARCHAR (30) NOT NULL," +
                "shopname VARCHAR (120) NOT NULL," +
                "shopaddress VARCHAR (120) NOT NULL," +
                "city VARCHAR (20) NOT NULL," +
                "district VARCHAR (20) NOT NULL," +
                "state VARCHAR (20) NOT NULL," +
                "pincode VARCHAR (8) NOT NULL," +
                "mobile1 VARCHAR (10) NOT NULL," +
                "mobile2 VARCHAR (10));");

        sqLiteDatabase.execSQL("CREATE TABLE items (id INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL," +
                "items    VARCHAR (30) NOT NULL," +
                "quantity INTEGER      NOT NULL," +
                "rate     DOUBLE       NOT NULL," +
                "total    DOUBLE       NOT NULL);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }





    public boolean add_items(String items,int quantity,double rate,double total){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("items",items);
        c.put("quantity",quantity);
        c.put("rate",rate);
        c.put("total",total);
        long r=db.insert("items",null,c);
        return r!=-1;
    }

    public  Cursor get_items()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select *from items",null);
    }


    public boolean add_shop_details(String gstno,String shopname,String shopaddress,String city,String district,String state,String pincode,String mob1,String mob2)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("gstno",gstno);
        c.put("shopname",shopname);
        c.put("shopaddress",shopaddress);
        c.put("city",city);
        c.put("district",district);
        c.put("state",state);
        c.put("pincode",pincode);
        c.put("mobile1",mob1);
        c.put("mobile2",mob2);
        long r= db.insert("shop_details",null,c);
        return r!=-1;
    }
    public Cursor get_shop_details()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select *from shop_details",null);
    }

    public void delete_create_item_table() {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        sqLiteDatabase.execSQL("drop table items;");

        sqLiteDatabase.execSQL("CREATE TABLE items (id INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL," +
                "items    VARCHAR (30) NOT NULL," +
                "quantity INTEGER      NOT NULL," +
                "rate     DOUBLE       NOT NULL," +
                "total    DOUBLE       NOT NULL);");



    }
}
