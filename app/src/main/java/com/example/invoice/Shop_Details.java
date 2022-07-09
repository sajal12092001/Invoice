package com.example.invoice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Shop_Details extends AppCompatActivity {
    Conn conn = new Conn(this);
    EditText gstno, shopname, shopaddress, city, district, pincode, mob1, mob2;
    Spinner state;
    Button setbutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Cursor check = conn.get_shop_details();
        if (check.getCount() == 1) {
            Intent intent = new Intent(Shop_Details.this, Add_Customer_Detail.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.shop_details);
            setTitle("Shop Details");


            gstno = findViewById(R.id.gstno);
            shopname = findViewById(R.id.shopname);
            shopaddress = findViewById(R.id.shopaddress);
            city = findViewById(R.id.city);
            district = findViewById(R.id.district);
            state = findViewById(R.id.state);
            pincode = findViewById(R.id.pincode);
            mob1 = findViewById(R.id.mob1);
            mob2 = findViewById(R.id.mob2);
            setbutton = findViewById(R.id.setbutton);

            setbutton.setOnClickListener(view -> {
                String gst = gstno.getText().toString().trim().toUpperCase();
                String name = shopname.getText().toString().trim().toUpperCase();
                String address = shopaddress.getText().toString().trim().toUpperCase();
                String tcity = city.getText().toString().trim().toUpperCase();
                String tdistrict = district.getText().toString().trim().toUpperCase();
                String tstate = state.getSelectedItem().toString().trim().toUpperCase();
                String tpincode = pincode.getText().toString().trim().toUpperCase();
                String tmob1 = mob1.getText().toString().trim().toUpperCase();
                String tmob2 = mob2.getText().toString().trim().toUpperCase();

                boolean check1 = conn.add_shop_details(gst, name, address, tcity, tdistrict, tstate, tpincode, tmob1, tmob2);
                if (check1) {
                    Intent intent = new Intent(Shop_Details.this, Add_Customer_Detail.class);
                    startActivity(intent);

                }
            });

        }


    }
}
