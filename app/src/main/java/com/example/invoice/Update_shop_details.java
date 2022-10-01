package com.example.invoice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class Update_shop_details extends AppCompatActivity {

    Conn conn = new Conn(this);
    EditText gstno, shopname, shopaddress, city, district, pincode, mob1, mob2;
    Spinner state;
    Button setbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shop_details);
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

        List your_array = Arrays.asList(getResources().getStringArray(R.array.state));


        Cursor getshopdetails_cursor = conn.get_shop_details();
        if (getshopdetails_cursor.moveToFirst()) {
            gstno.setText(getshopdetails_cursor.getString(1));
            shopname.setText(getshopdetails_cursor.getString(2));
            shopaddress.setText(getshopdetails_cursor.getString(3) );
            city.setText( getshopdetails_cursor.getString(4) );
            district.setText( getshopdetails_cursor.getString(5));
            state.setSelection(your_array.indexOf( getshopdetails_cursor.getString(6)));
            pincode.setText( getshopdetails_cursor.getString(7));

                mob1.setText(getshopdetails_cursor.getString(8));

                mob2.setText( getshopdetails_cursor.getString(9));

        }

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
                Intent intent = new Intent(getApplicationContext(), Items_Recyclerview.class);
                startActivity(intent);
            }
        });
    }
}