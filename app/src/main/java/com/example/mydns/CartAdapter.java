package com.example.mydns;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mydns.databinding.ItemCartBinding;



import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<CartItem> cartItems;
    private Runnable onCartChanged;

    public CartAdapter(List<CartItem> cartItems,Runnable onCartChanged){

        this.cartItems=cartItems;
        this.onCartChanged=onCartChanged;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ItemCartBinding binding=ItemCartBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);

        return new CartViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder,int position){
       CartItem item =cartItems.get(position);
       Product product=item.getProduct();
        holder.binding.tvCartName.setText(product.getName());
        holder.binding.tvCartPrice.setText(product.getPrice()+" ₽");
//        holder.binding.imgCartProduct.setImageResource(R.drawable.ic_car);
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_car)
                .error(R.drawable.ic_car)
                .into(holder.binding.imgCartProduct);
        holder.binding.tvCount.setText(String.valueOf(item.getCount()));
        holder.binding.btnPlus.setOnClickListener(v->{
            item.plus();
            notifyItemChanged(holder.getAdapterPosition());
            onCartChanged.run();
        });
        holder.binding.btnMinus.setOnClickListener(v->{
            item.minus();
            notifyItemChanged(holder.getAdapterPosition());
            onCartChanged.run();
        });
        holder.binding.btnDelete.setOnClickListener(v->{
            int adapterPosition=holder.getAdapterPosition();
            if(adapterPosition!=RecyclerView.NO_POSITION){
                CartManager.removeItem(cartItems.get(adapterPosition));
                notifyItemRemoved(adapterPosition);
                onCartChanged.run();
            }

        });
    }
    @Override
    public int getItemCount(){
        return cartItems.size();
    }
    static class CartViewHolder extends RecyclerView.ViewHolder{
        ItemCartBinding binding;
        public CartViewHolder(ItemCartBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
