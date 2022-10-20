package com.example.invoice;

import android.annotation.SuppressLint;
import android.content.Intent;
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
    public void onBindViewHolder(@NonNull viewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.itemname.setText(datalist.get(position).getItemname());
        holder.itemsize.setText(datalist.get(position).getItemsize());
        holder.qtyrate.setText(datalist.get(position).getItemrate() + "X" + datalist.get(position).getQuantity());
        holder.subtotal.setText("Rs. " + datalist.get(position).getTotal());
        holder.discount.setText(datalist.get(position).getDiscount());
        holder.total.setText("Rs. " + datalist.get(position).getSubtotal());

        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(holder.itemView.getContext(), Update_Items.class);
            intent.putExtra("id", datalist.get(position).getId());
            intent.putExtra("sizetype", datalist.get(position).getSizetype());
            intent.putExtra("category", datalist.get(position).getCateogry());
            //Toast.makeText(holder.itemView.getContext(), ""+datalist.get(position).getSizetype(), Toast.LENGTH_SHORT).show();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            holder.itemView.getContext().startActivity(intent);


        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    static class viewholder extends RecyclerView.ViewHolder {
        TextView itemname, itemsize, qtyrate, subtotal, discount, total;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.itemname);
            itemsize = itemView.findViewById(R.id.itemsize);
            qtyrate = itemView.findViewById(R.id.quantity_rate);
            subtotal = itemView.findViewById(R.id.subtotal);
            discount = itemView.findViewById(R.id.discount);
            total = itemView.findViewById(R.id.total);
        }
    }
}
