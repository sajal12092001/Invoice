package com.example.invoice;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Add_Items extends AppCompatActivity {
    TextView grandtot;
    EditText name, rate, quantity;
    Button add, finish;
    RecyclerView recview;
    ArrayList<Add_Items_Model> datalist;
    Add_Items_Adapter adapter;
    double grandtotal=0;


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);
        setTitle("Enter Items");

        name = findViewById(R.id.item);
        rate = findViewById(R.id.rate);
        quantity = findViewById(R.id.quantity);
        grandtot = findViewById(R.id.grandtotal);

        recview=findViewById(R.id.recviewitems);
        recview.setLayoutManager(new LinearLayoutManager(this));

        datalist=new ArrayList<>();

        add = findViewById(R.id.additem);
        finish = findViewById(R.id.finish);

        add.setOnClickListener(view -> {

            String itemname=name.getText().toString().toUpperCase().trim();
            String itemrate=rate.getText().toString().toUpperCase().trim();
            String itemqty=quantity.getText().toString().toUpperCase().trim();

            if(itemname.isEmpty()&&itemrate.isEmpty()&&itemqty.isEmpty())
            {name.setError("Enter the item name");

            rate.setError("Enter the item price");
            quantity.setError("Enter the wuantity");}
            else {
                String subtotal = "" + (Double.parseDouble(itemrate) * Integer.parseInt(itemqty));

                Conn conn = new Conn(this);
                conn.add_items(itemname, itemrate, itemqty, subtotal);

                setRecview();
                grandtot.setText("" + grandtotal);
            }

        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRecview(){
        grandtotal=0;
        datalist.clear();
        Conn conn=new Conn(this);
        Cursor cursor=conn.get_items();
        while(cursor.moveToNext())
        {
            grandtotal+=Double.parseDouble(cursor.getString(4));
            Add_Items_Model obj=new Add_Items_Model(cursor.getString(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            datalist.add(obj);
            adapter=new Add_Items_Adapter(datalist);
            recview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}