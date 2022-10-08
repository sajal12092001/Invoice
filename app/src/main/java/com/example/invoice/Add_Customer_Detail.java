package com.example.invoice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Add_Customer_Detail extends AppCompatActivity {
    EditText name, address, mob;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_customer_detail);
        setTitle("Customer Details");
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        mob = findViewById(R.id.mobile);

        next = findViewById(R.id.nextbutton);
        next.setOnClickListener(view -> {
            String cname = name.getText().toString().trim().toUpperCase();
            String caddress = address.getText().toString().trim().toUpperCase();
            String cmob = mob.getText().toString().trim().toUpperCase();

            SharedPreferences sharedPreferences = getSharedPreferences("customer", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", cname);
            editor.putString("address", caddress);
            editor.putString("mobile", cmob);
            editor.apply();

            if (cname.isEmpty()) {
                name.setError("Please enter the customer name");
            } else {
                SharedPreferences sharedPreferences1 = getSharedPreferences("check_exist_file", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("file_name", "");
                editor1.apply();

                new Conn(this).delete_item_details();
                Intent intent = new Intent(Add_Customer_Detail.this, Items_Recyclerview.class);
                startActivity(intent);

            }
        });
    }
}