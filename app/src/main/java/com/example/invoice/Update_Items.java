package com.example.invoice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Update_Items extends AppCompatActivity {

    EditText name, rate, quantity;
    Spinner selectsize, size;
    Button add;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    String id = "";
    String sizetype = "";
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_items);
        setTitle("Update Items");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        List numbersize = Arrays.asList("35 cm", "40 cm", "45 cm", "50 cm", "55 cm", "60 cm", "65 cm", "70 cm", "75 cm", "80 cm", "85 cm", "90 cm", "95 cm", "100 cm", "105 cm", "110 cm");
        ArrayAdapter numberadapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, numbersize);

        List romansize = Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL");
        ArrayAdapter romanadapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, romansize);


        id = getIntent().getStringExtra("id");
        sizetype = getIntent().getStringExtra("sizetype");
        category = getIntent().getStringExtra("category");
        // Toast.makeText(this, ""+sizetype, Toast.LENGTH_SHORT).show();
        Conn conn = new Conn(this);
        name = findViewById(R.id.itemname);
        rate = findViewById(R.id.itemrate);
        quantity = findViewById(R.id.itemquantity);
        selectsize = findViewById(R.id.selectsize);
        size = findViewById(R.id.size);
        radioGroup = findViewById(R.id.radiogroup);
        add = findViewById(R.id.additem);


        Cursor cursor = conn.getSingleItmes(id);

        if (cursor.moveToFirst()) {
            name.setText(cursor.getString(1));
            rate.setText(cursor.getString(3));
            quantity.setText(cursor.getString(4));
            if (sizetype.equals("Number")) {

                selectsize.setSelection(1);
                size.setAdapter(numberadapter);
                size.setSelection(numbersize.indexOf(cursor.getString(2)));
                selectsize.setEnabled(false);

            } else if (sizetype.equals("Roman")) {

                selectsize.setSelection(2);
                size.setAdapter(romanadapter);
                size.setSelection(romansize.indexOf(cursor.getString(2)));
                selectsize.setEnabled(false);

            }

        }

        if (category.equals("Garments")) {
            RadioButton graments = findViewById(R.id.garments);
            graments.setChecked(true);
        } else {
            RadioButton undergraments = findViewById(R.id.under_garments);
            undergraments.setChecked(true);
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item_name = name.getText().toString().toUpperCase().trim();
                String item_size = "" + size.getSelectedItem();
                double item_price = Double.parseDouble(rate.getText().toString().trim());
                int item_qty = Integer.parseInt(quantity.getText().toString().trim());

                selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                //get RadioButton text
                String yourVote = selectedRadioButton.getText().toString();

                if (item_name.equals("") && ("" + item_price).equals("") && ("" + item_qty).equals("")) {

                    name.setError("Enter the item name");
                    rate.setError("Enter the item price");
                    quantity.setError("Enter the quantity");

                } else if (selectsize.getSelectedItem().equals("-Select Size-")) {
                    Toast.makeText(getApplicationContext(), "Please select item size", Toast.LENGTH_SHORT).show();
                    size.setEnabled(true);
                    size.setAdapter(numberadapter);

                }


                if (yourVote.equals("Garments")) {
                    double totalprice = item_price * item_qty;
                    double discount = ((double) 25 / 100) * totalprice;
                    double resttot = totalprice - discount;

                    conn.update_item(id, item_name, item_size, "" + item_price, "" + item_qty, "" + totalprice, "(-25%) " + discount, "" + resttot, "" + selectsize.getSelectedItem(), "Garments");
                    startActivity(new Intent(getApplicationContext(), Items_Recyclerview.class));
                    finish();
                } else if (yourVote.equals("Under Garments")) {
                    double totalprice = item_price * item_qty;
                    double discount = ((double) 35 / 100) * totalprice;
                    double resttot = totalprice - discount;
                    conn.update_item(id, item_name, item_size, "" + item_price, "" + item_qty, "" + totalprice, "(-35%) " + discount, "" + resttot, "" + selectsize.getSelectedItem(), "Under Garments");
                    startActivity(new Intent(getApplicationContext(), Items_Recyclerview.class));
                    finish();
                }

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