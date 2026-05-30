package com.example.mydns;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.example.mydns.databinding.ItemOrderProductBinding;

import org.jspecify.annotations.NonNull;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderViewHolder>{
    private List<CartItem> items;

    public OrderProductAdapter(List<CartItem> items){

        this.items=items;
        
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        ItemOrderProductBinding binding=ItemOrderProductBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);

        return new OrderViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder,int position){
        CartItem item =items.get(position);
        Product product=item.getProduct();
        holder.binding.tvOrderName.setText(product.getName());
        holder.binding.tvOrderPrice.setText(product.getPrice());
        holder.binding.imgOrderProduct.setImageResource(product.getImage());
        holder.binding.tvOrderCount.setText("Количество: "+ item.getCount());

    }
    @Override
    public int getItemCount(){
        return items.size();
    }
    static class OrderViewHolder extends RecyclerView.ViewHolder{
        ItemOrderProductBinding binding;
        public OrderViewHolder(ItemOrderProductBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
