package com.example.invoice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Add_Items extends AppCompatActivity {
    TextView grandtot;
    EditText name, rate, quantity, discount;
    Button add, finish;
    RecyclerView recview;
    ArrayList<Add_Items_Model> datalist;
    Add_Items_Adapter adapter;
    double grandtotal = 0;
    String id = "";
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getLayoutPosition();
            if (direction == ItemTouchHelper.LEFT) {
                id = "";
                id = datalist.get(position).getId();
                name.setText(datalist.get(position).getName());
                rate.setText(datalist.get(position).getRate());
                quantity.setText(datalist.get(position).getQuantity());
                add.setText("Update");
                datalist.remove(position);
                adapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .setActionIconTint(ContextCompat.getColor(Add_Items.this, R.color.black))
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Add_Items.this, R.color.blue))
                    .addSwipeLeftActionIcon(R.drawable.edit_icon)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

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
        discount = findViewById(R.id.editdiscount);

        recview = findViewById(R.id.recviewitems);
        recview.setLayoutManager(new LinearLayoutManager(this));

        datalist = new ArrayList<>();

        add = findViewById(R.id.additem);
        finish = findViewById(R.id.finish);

        add.setOnClickListener(view -> {

            String itemname = name.getText().toString().toUpperCase().trim();
            String itemrate = rate.getText().toString().toUpperCase().trim();
            String itemqty = quantity.getText().toString().toUpperCase().trim();

            String s = "" + (Double.parseDouble(itemrate) * Integer.parseInt(itemqty));
            if (!id.isEmpty()) {


                Conn conn = new Conn(this);
                conn.update_item(id, itemname, itemrate, itemqty, s);
                setRecview();
                grandtot.setText("" + grandtotal);
                id = "";
                name.setText("");
                quantity.setText("");
                rate.setText("");
                add.setText("Add Items");

            } else {
                if (itemname.isEmpty() && itemrate.isEmpty() && itemqty.isEmpty()) {
                    name.setError("Enter the item name");

                    rate.setError("Enter the item price");
                    quantity.setError("Enter the wuantity");
                } else {


                    Conn conn = new Conn(this);
                    conn.add_items(itemname, itemrate, itemqty, s);

                    setRecview();
                    grandtot.setText("" + grandtotal);
                    name.setText("");
                    quantity.setText("");
                    rate.setText("");
                }

            }


        });

        finish.setOnClickListener(view -> {
            if (discount.getText().toString().trim().isEmpty()) {
                SharedPreferences sharedPreference = this.getSharedPreferences("itemtotal", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.putString("discount", discount.getText().toString().trim());
                editor.putString("grandtotal", "" + grandtotal);

                // double disrate = (Double.parseDouble(discount.getText().toString().trim()) * grandtotal) / 100;
                //editor.putString("disrate", "" + disrate);
                //editor.putString("discountTtot", "" + (grandtotal - disrate));
                editor.apply();
                Intent intent = new Intent(Add_Items.this, Invoice.class);
                intent.putExtra("date", getdate());
                startActivity(intent);

                //Toast.makeText(this,discount.getText().toString().trim()+"\n"+grandtotal+"\n"+disrate+"\n"+(grandtotal-disrate) , Toast.LENGTH_SHORT).show();

            } else {
                SharedPreferences sharedPreference = this.getSharedPreferences("itemtotal", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.putString("discount", discount.getText().toString().trim());
                editor.putString("grandtotal", "" + grandtotal);

                double disrate = (Double.parseDouble(discount.getText().toString().trim()) * grandtotal) / 100;
                editor.putString("disrate", "" + disrate);
                editor.putString("discountTtot", "" + (grandtotal - disrate));
                editor.apply();
                Intent intent = new Intent(Add_Items.this, Invoice.class);
                intent.putExtra("date", getdate());
                startActivity(intent);

                //Toast.makeText(this,discount.getText().toString().trim()+"\n"+grandtotal+"\n"+disrate+"\n"+(grandtotal-disrate) , Toast.LENGTH_SHORT).show();


            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRecview() {
        grandtotal = 0;
        datalist.clear();
        Conn conn = new Conn(this);
        Cursor cursor = conn.get_items();
        while (cursor.moveToNext()) {
            grandtotal += Double.parseDouble(cursor.getString(4));
            Add_Items_Model obj = new Add_Items_Model(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            datalist.add(obj);
            adapter = new Add_Items_Adapter(datalist);
            recview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recview);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getdate() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, dd MMM yyyy hh:mm:ss a");
        return myDateObj.format(myFormatObj);
    }

}