package com.example.mydns;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mydns.databinding.ItemCartBinding;

import org.jspecify.annotations.NonNull;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<Product> products;
    public CartAdapter(List<Product> products){
        this.products=products;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        ItemCartBinding binding=ItemCartBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);

        return new CartViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder,int position){
        Product product=products.get(position);
        holder.binding.tvCartName.setText(product.getName());
        holder.binding.tvCartPrice.setText(product.getPrice());
        holder.binding.imgCartProduct.setImageResource(product.getImage());
    }
    @Override
    public int getItemCount(){
        return products.size();
    }
    static class CartViewHolder extends RecyclerView.ViewHolder{
        ItemCartBinding binding;
        public CartViewHolder(ItemCartBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
