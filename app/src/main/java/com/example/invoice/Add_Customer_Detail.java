package com.example.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        mob = findViewById(R.id.mobile);

        next = findViewById(R.id.nextbutton);
        next.setOnClickListener(view -> {
            String cname = name.getText().toString().trim().toUpperCase();
            String caddress = address.getText().toString().trim().toUpperCase();
            String cmob = mob.getText().toString().trim().toUpperCase();
            if (cname.isEmpty()) {
                name.setError("Please enter the name customer name");
            } else {
                    Intent intent = new Intent(Add_Customer_Detail.this, Add_Items.class);
                    intent.putExtra("name",cname);
                    intent.putExtra("address",caddress);
                    intent.putExtra("mobile",cmob);
                    startActivity(intent);

            }
        });
    }
}