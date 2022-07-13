package com.example.invoice;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Add_Items_Adapter extends RecyclerView.Adapter<Add_Items_Adapter.viewholder> {

    ArrayList<Add_Items_Model> datalist;

    public Add_Items_Adapter(ArrayList<Add_Items_Model> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_single, parent, false);
        return new viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.id.setText(datalist.get(position).getId());
        holder.name.setText(datalist.get(position).getName());
        holder.qtyrate.setText(datalist.get(position).getRate() + "X" + datalist.get(position).getQuantity());
        holder.subtotal.setText(datalist.get(position).getSubtotal());

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView id,name, qtyrate, subtotal;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.itemname);
            qtyrate = itemView.findViewById(R.id.quantityrate);
            subtotal = itemView.findViewById(R.id.subtotal);
        }
    }
}
