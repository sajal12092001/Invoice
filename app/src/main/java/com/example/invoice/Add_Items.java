package com.example.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Objects;

public class Add_Items extends AppCompatActivity {
    EditText name, rate, quantity;
    Spinner selectsize, size;
    Button add;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Conn conn = new Conn(this);
        name = findViewById(R.id.itemname);
        rate = findViewById(R.id.itemrate);
        quantity = findViewById(R.id.itemquantity);
        selectsize = findViewById(R.id.selectsize);
        size = findViewById(R.id.size);
        radioGroup = findViewById(R.id.radiogroup);
        add = findViewById(R.id.additem);

        selectsize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectsize.getSelectedItem().equals("Number")) {
                    size.setEnabled(true);
                    String[] numbersize = {"35 cm", "40 cm", "45 cm", "50 cm", "55 cm", "60 cm", "65 cm", "70 cm", "75 cm", "80 cm", "85 cm", "90 cm", "95 cm", "100 cm", "105 cm", "110 cm"};
                    ArrayAdapter<String> numberadapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, numbersize);
                    size.setAdapter(numberadapter);
                } else if (selectsize.getSelectedItem().equals("Roman")) {
                    size.setEnabled(true);
                    String[] romansize = {"S", "M", "L", "XL", "XXL", "XXXL"};
                    ArrayAdapter<String> romanadapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, romansize);
                    size.setAdapter(romanadapter);
                } else if (selectsize.getSelectedItem().equals("-Select Size-")) {
                    size.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                size.setEnabled(false);

            }
        });

        add.setOnClickListener(view -> {
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
                Toast.makeText(Add_Items.this, "Please select item size", Toast.LENGTH_SHORT).show();

            }


            if (yourVote.equals("Garments")) {
                double totalprice = item_price * item_qty;
                double discount = ((double) 25 / 100) * totalprice;
                double resttot = totalprice - discount;
                conn.add_items(item_name, item_size, "" + item_price, "" + item_qty, "" + totalprice, "(-25%) " + discount, "" + resttot, "" + selectsize.getSelectedItem(), "Garments");
                startActivity(new Intent(Add_Items.this, Items_Recyclerview.class));
                finish();
            } else if (yourVote.equals("Under Garments")) {
                double totalprice = item_price * item_qty;
                String discount = new DecimalFormat("##.00").format((((double) 35 / 100) * totalprice));
                double resttot = totalprice - Double.parseDouble(discount);

                conn.add_items(item_name, item_size, "" + item_price, "" + item_qty, "" + totalprice, "(-35%) " + discount, "" + resttot, "" + selectsize.getSelectedItem(), "Under Garments");
                startActivity(new Intent(Add_Items.this, Items_Recyclerview.class));
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