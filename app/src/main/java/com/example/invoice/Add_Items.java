package com.example.invoice;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Add_Items extends AppCompatActivity {
    EditText name, rate, quantity;
    Button add, finish, clear;
    TextView sr, tname, tquantity, trate, ttotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);
        setTitle("Enter Items");

        name = findViewById(R.id.item);
        rate = findViewById(R.id.rate);
        quantity = findViewById(R.id.quantity);

        add = findViewById(R.id.additem);
        finish = findViewById(R.id.finish);
        clear = findViewById(R.id.clear);

        sr = findViewById(R.id.tsrno);
        tname = findViewById(R.id.tname);
        tquantity = findViewById(R.id.tquantity);
        trate = findViewById(R.id.trate);
        ttotal = findViewById(R.id.ttotal);
        Conn conn = new Conn(this);

        add.setOnClickListener(view -> {
            String itemname = name.getText().toString().trim().toUpperCase();
            double itemrate = Double.parseDouble(rate.getText().toString().trim());
            int itemquantity = Integer.parseInt(quantity.getText().toString().trim());
            double total = itemrate * itemquantity;

            boolean check = conn.add_items(itemname, itemquantity, itemrate, total);
            if (check) {
                Toast.makeText(Add_Items.this, "Success", Toast.LENGTH_SHORT).show();
                name.setText("");
                rate.setText("");
                quantity.setText("");
                sr.setText("");
                tname.setText("");
                tquantity.setText("");
                trate.setText("");
                ttotal.setText("");
                Cursor cursor = conn.get_items();
                if (cursor.moveToFirst()) {

                    sr.setText(cursor.getString(0) + "\n");
                    tname.setText(cursor.getString(1) + "\n");
                    tquantity.setText(cursor.getString(2) + "\n");
                    trate.setText(cursor.getString(3) + "\n");
                    ttotal.setText(cursor.getString(4) + "\n");
                }


                while (cursor.moveToNext()) {


                    sr.append(cursor.getString(0) + "\n");
                    tname.append(cursor.getString(1) + "\n");
                    tquantity.append(cursor.getString(2) + "\n");
                    trate.append(cursor.getString(3) + "\n");
                    ttotal.append(cursor.getString(4) + "\n");
                }

            } else
                Toast.makeText(Add_Items.this, "Failure", Toast.LENGTH_SHORT).show();
        });

        finish.setOnClickListener(view -> {

        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sr.setText("");
                tname.setText("");
                tquantity.setText("");
                trate.setText("");
                ttotal.setText("");
                conn.delete_create_item_table();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


}