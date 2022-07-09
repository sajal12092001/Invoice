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
        Conn conn = new Conn(this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cname = name.getText().toString().trim().toUpperCase();
                String caddress = address.getText().toString().trim().toUpperCase();
                String cmob = mob.getText().toString().trim().toUpperCase();
                if (cname.isEmpty()) {
                    name.setError("Please enter the name customer name");
                } else {
                    boolean check = conn.add_customers_details(cname, caddress, cmob);
                    if (check) {
                        Intent intent = new Intent(Add_Customer_Detail.this, Add_Items.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}