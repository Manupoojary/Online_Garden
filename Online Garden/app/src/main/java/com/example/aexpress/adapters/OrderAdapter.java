package com.example.aexpress.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aexpress.R;
import com.example.aexpress.databinding.OrdersBinding;
import com.example.aexpress.model.myorders;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    Context context;
    ArrayList<myorders>orders;

    public OrderAdapter(Context context, ArrayList<myorders>orders) {
        this.context = context;
        this.orders = orders;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapter.OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position)
    {

        myorders order = orders.get(position);
//        holder.binding.orderphone.setText(order.getPhoneno());
        holder.binding.orderordercode.setText(order.getOrdercode());
        holder.binding.orderprice.setText(order.getAmount());
        holder.binding.ordershippingdate.setText(order.getShipdate());
        holder.binding.ordershiploc.setText(order.getShiploc());
        holder.binding.orderordstat.setText(order.getPaymentstatus());

    }

    @Override
    public int getItemCount() {
        return orders.size();

    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        OrdersBinding binding;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = OrdersBinding.bind(itemView);

        }
    }
}
