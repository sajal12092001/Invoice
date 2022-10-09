package com.example.invoice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Items_Recyclerview extends AppCompatActivity {
    TextView finish, share;
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
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
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

        finish = findViewById(R.id.Download);
        share = findViewById(R.id.share);


        Cursor cursor = conn.get_items();
        while (cursor.moveToNext()) {
            Add_Items_Model obj = new Add_Items_Model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
            datalist.add(obj);
            adapter = new Add_Items_Adapter(datalist);
            recview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            grandtotal += Double.parseDouble(cursor.getString(7));


        }

        SharedPreferences sharedPreferences1 = getSharedPreferences("check_exist_file", MODE_PRIVATE);

        grandtot.setText("Rs. " + grandtotal);
        finish.setOnClickListener(view -> {

            if (new File(sharedPreferences1.getString("file_name", "")).exists()) {
                Toast.makeText(this, "This invoice is already created in this path -> " + sharedPreferences1.getString("file_name", ""), Toast.LENGTH_SHORT).show();

            } else {

                Intent intent = new Intent(getApplicationContext(), Invoice.class);
                intent.putExtra("grandtotal", "" + grandtotal);
                intent.putExtra("date", getdate());
                startActivity(intent);

            }


        });


        share.setOnClickListener(view -> {


            if (new File(sharedPreferences1.getString("file_name", "")).exists()) {

                 /*

            for veiwing created invoice

             */
//           // Get the URI Path of file.
                Uri uriPdfPath = FileProvider.getUriForFile(this, getApplicationContext().getPackageName(), new File(sharedPreferences1.getString("file_name", "")));
                Log.d("pdfPath", "" + uriPdfPath);

                // Start Intent to View PDF from the Installed Applications.
                Intent pdfOpenIntent = new Intent("android.intent.action.SEND");

                pdfOpenIntent.setType("application/pdf");
                pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenIntent.putExtra("android.intent.extra.STREAM", uriPdfPath);
                pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                try {
                    startActivity(pdfOpenIntent);
                } catch (ActivityNotFoundException activityNotFoundException) {
                    Toast.makeText(this, "There is no app to load corresponding PDF", Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(this, "You have to first download the invoice.", Toast.LENGTH_SHORT).show();

            }


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