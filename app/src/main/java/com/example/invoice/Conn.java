package com.example.invoice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Conn extends SQLiteOpenHelper {
    private static final String databasename = "invoice.db";

    public Conn(@Nullable Context context) {
        super(context, databasename, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE shop_details (\n" +
                "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT\n" +
                "                        NOT NULL,\n" +
                "    gstno       STRING  NOT NULL,\n" +
                "    shopname    STRING  NOT NULL,\n" +
                "    shopaddress STRING  NOT NULL,\n" +
                "    city        STRING  NOT NULL,\n" +
                "    district    STRING  NOT NULL,\n" +
                "    state       STRING  NOT NULL,\n" +
                "    pincode     STRING  NOT NULL,\n" +
                "    mobile1     STRING  NOT NULL,\n" +
                "    mobile2     STRING  NOT NULL\n" +
                ");\n");

        sqLiteDatabase.execSQL("CREATE TABLE item_details (\n" +
                "    id       INTEGER PRIMARY KEY ASC AUTOINCREMENT\n" +
                "                     NOT NULL,\n" +
                "    itemname STRING  NOT NULL,\n" +
                "    itemsize STRING  NOT NULL,\n" +
                "    itemrate STRING  NOT NULL,\n" +
                "    quantity STRING  NOT NULL,\n" +
                "    subtotal STRING  NOT NULL,\n" +
                "    itemdiscount STRING  NOT NULL,\n" +
                "    total STRING  NOT NULL\n," +
                "    sizetype STRING  NOT NULL\n," +
                "    category STRING  NOT NULL\n" +
                ");\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean add_shop_details(String gstno, String shopname, String shopaddress, String city, String district, String state, String pincode, String mob1, String mob2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("gstno", gstno);
        c.put("shopname", shopname);
        c.put("shopaddress", shopaddress);
        c.put("city", city);
        c.put("district", district);
        c.put("state", state);
        c.put("pincode", pincode);
        c.put("mobile1", mob1);
        c.put("mobile2", mob2);
        long r = db.insert("shop_details", null, c);
        return r != -1;
    }


    public Cursor getSingleItmes(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select *from item_details where id='" + id + "'", null);

    }

    public Cursor get_items() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select *from item_details", null);
    }


    public Cursor get_shop_details() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select *from shop_details", null);
    }

    public void delete_create_item_table() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.execSQL("drop table items;");

        sqLiteDatabase.execSQL("CREATE TABLE item_details (\n" +
                "    id       INTEGER PRIMARY KEY ASC AUTOINCREMENT\n" +
                "                     NOT NULL,\n" +
                "    itemname STRING  NOT NULL,\n" +
                "    itemrate STRING  NOT NULL,\n" +
                "    quantity STRING  NOT NULL,\n" +
                "    subtotal STRING  NOT NULL\n" +
                ");\n");
    }

    public void add_items(String items, String size, String rate, String quantity, String subtotal, String discount, String total, String sizetype, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("itemname", items);
        c.put("itemsize", size);
        c.put("itemrate", rate);
        c.put("quantity", quantity);
        c.put("subtotal", subtotal);
        c.put("itemdiscount", discount);
        c.put("total", total);
        c.put("sizetype", sizetype);
        c.put("category", category);
        db.insert("item_details", null, c);

    }

    public void update_item(String id, String items, String size, String rate, String quantity, String subtotal, String discount, String total, String sizetype, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update item_details set itemname='" + items + "',itemsize='" + size + "',itemrate='" + rate + "',quantity='" + quantity + "',subtotal='" + subtotal + "',itemdiscount='" + discount + "',total='" + total + "',sizetype='" + sizetype + "',category='" + category + "' where id='" + id + "';");
    }
}
