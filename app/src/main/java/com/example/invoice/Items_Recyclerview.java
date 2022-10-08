package com.example.invoice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Items_Recyclerview extends AppCompatActivity {
    Button finish;
    TextView grandtot;
    RecyclerView recview;
    CardView shopdet, custdet;
    ArrayList<Add_Items_Model> datalist;
    TextView gst, shopname, shopaddress, mobile, custname, custaddrress, custmobile;
    Add_Items_Adapter adapter;
    String cname, caddress, cmobile;
    double grandtotal = 0;


    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged", "InlinedApi"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items_recyclerview);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        setTitle("Invoice Details");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Conn conn = new Conn(this);

        gst = findViewById(R.id.gstnumber);
        shopname = findViewById(R.id.shopname);
        shopaddress = findViewById(R.id.shopaddress);
        mobile = findViewById(R.id.shopmobile);
        custname = findViewById(R.id.custname);
        custaddrress = findViewById(R.id.custaddress);
        custmobile = findViewById(R.id.custmobile);

        SharedPreferences sharedPreferences = getSharedPreferences("customer", MODE_PRIVATE);
        cname = sharedPreferences.getString("name", "");
        caddress = sharedPreferences.getString("address", "");
        cmobile = sharedPreferences.getString("mobile", "");

        shopdet = findViewById(R.id.shopdetails);
        custdet = findViewById(R.id.customerdetails);

        custname.setText(cname);
        custaddrress.setText(caddress);
        custmobile.setText(cmobile);


        Cursor getshopdetails_cursor = conn.get_shop_details();
        if (getshopdetails_cursor.moveToFirst()) {
            gst.setText(getshopdetails_cursor.getString(1));
            shopname.setText(getshopdetails_cursor.getString(2));
            shopaddress.setText(getshopdetails_cursor.getString(3) + ", " + getshopdetails_cursor.getString(4) + ", " + getshopdetails_cursor.getString(5) + ", " + getshopdetails_cursor.getString(6).toUpperCase() + ", " + getshopdetails_cursor.getString(7));
            if (getshopdetails_cursor.getString(9).equals(""))
                mobile.setText(getshopdetails_cursor.getString(8));
            else
                mobile.setText(getshopdetails_cursor.getString(8) + ", " + getshopdetails_cursor.getString(9));

        }


        shopdet.setOnClickListener(view -> startActivity(new Intent(Items_Recyclerview.this, Update_shop_details.class)));

        custdet.setOnClickListener(view -> startActivity(new Intent(Items_Recyclerview.this, update_customer.class)));

        grandtot = findViewById(R.id.grandtotal);

        recview = findViewById(R.id.recviewitems);
        recview.setLayoutManager(new LinearLayoutManager(this));

        datalist = new ArrayList<>();

        finish = findViewById(R.id.finish);


        Cursor cursor = conn.get_items();
        while (cursor.moveToNext()) {
            Add_Items_Model obj = new Add_Items_Model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
            datalist.add(obj);
            adapter = new Add_Items_Adapter(datalist);
            recview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            grandtotal += Double.parseDouble(cursor.getString(7));
        }

        grandtot.setText("Rs. " + grandtotal);
        finish.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), Invoice.class);
            intent.putExtra("grandtotal", "" + grandtotal);
            intent.putExtra("date", getdate());
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getdate() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, dd MMM yyyy hh-mm-ssa");
        return myDateObj.format(myFormatObj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.add_item) {
            startActivity(new Intent(Items_Recyclerview.this, Add_Items.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}