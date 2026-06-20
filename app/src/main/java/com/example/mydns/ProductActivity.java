package com.example.mydns;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.mydns.databinding.ActivityProductBinding;

public class ProductActivity extends AppCompatActivity {
    private ActivityProductBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        long id=getIntent().getLongExtra("product_id",0);
        String name=getIntent().getStringExtra("product_name");
        int price=getIntent().getIntExtra("product_price",0);
        String description=getIntent().getStringExtra("product_description");
        int quantity=getIntent().getIntExtra("quantity",0);
        String imageUrl=getIntent().getStringExtra("product_image_url");
        binding.tvProductName.setText(name);
        binding.tvProductPrice.setText(price + " ₽");
        binding.tvProductDescription.setText(description);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_car)
                .error(R.drawable.ic_car)
                .into(binding.imgProduct);


        binding.btnAddCart.setOnClickListener(v->{
            Product product=new Product(
                    id,
                    name,
                    price,
                    quantity,
                    description,
                    imageUrl
            );
            CartManager.addProduct(product);
            Toast.makeText(this,"Товар добавлен в корзину",
                    Toast.LENGTH_SHORT).show();
        });
        binding.btnBack.setOnClickListener(v->
            finish());

    }
}