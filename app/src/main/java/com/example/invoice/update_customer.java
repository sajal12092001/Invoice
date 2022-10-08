package com.example.invoice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class update_customer extends AppCompatActivity {
    EditText name, address, mob;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer);
        setTitle("Update Customer Details");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = getSharedPreferences("customer", MODE_PRIVATE);

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        mob = findViewById(R.id.mobile);

        name.setText(sharedPreferences.getString("name", ""));
        address.setText(sharedPreferences.getString("address", ""));
        mob.setText(sharedPreferences.getString("mobile", ""));


        next = findViewById(R.id.nextbutton);
        next.setOnClickListener(view -> {
            String cname = name.getText().toString().trim().toUpperCase();
            String caddress = address.getText().toString().trim().toUpperCase();
            String cmob = mob.getText().toString().trim().toUpperCase();


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", cname);
            editor.putString("address", caddress);
            editor.putString("mobile", cmob);
            editor.apply();

            if (cname.isEmpty()) {
                name.setError("Please enter the customer name");
            } else {
                Intent intent = new Intent(getApplicationContext(), Items_Recyclerview.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}