package com.example.mydns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.tvPrice.setText(product.getPrice());
        holder.tvDescription.setText(product.getDescription());
        holder.imgProduct.setImageResource(product.getImage());
        holder.itemView.setOnClickListener(v->{
            Toast.makeText(v.getContext(),"Вы выбрали: "+ product.getName(),Toast.LENGTH_SHORT).show();
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
