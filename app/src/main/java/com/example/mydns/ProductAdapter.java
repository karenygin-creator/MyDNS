package com.example.mydns;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product=products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice()+" ₽");
        holder.tvDescription.setText(product.getDescription());
//        holder.imgProduct.setImageResource(R.drawable.ic_car);
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_car)
                .error(R.drawable.ic_car)
                .into(holder.imgProduct);
        holder.itemView.setOnClickListener(v->{
            Intent intent=new Intent(v.getContext(),ProductActivity.class);
            intent.putExtra("product_name",product.getName());
            intent.putExtra("product_price",product.getPrice());
            intent.putExtra("product_quantity",product.getQuantity());
            intent.putExtra("product_description",product.getDescription());
            intent.putExtra("product_image_url",product.getImageUrl());

            v.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount(){
       return  products.size();
    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        TextView tvName;
        TextView tvPrice;
        TextView tvDescription;
        public ProductViewHolder(@NonNull View itemView){
            super(itemView);
            imgProduct=itemView.findViewById(R.id.imgProduct);
            tvName=itemView.findViewById(R.id.tvName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvDescription=itemView.findViewById(R.id.tvDescription);
        }
    }
}
