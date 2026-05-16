package com.example.mydns;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydns.databinding.ActivityProductBinding;

public class ProductActivity extends AppCompatActivity {
    private ActivityProductBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String name= getIntent().getStringExtra("product_name");
        String price= getIntent().getStringExtra("product_price");
        String description= getIntent().getStringExtra("product_description");
        int image=getIntent().getIntExtra("product_image",
                R.drawable.ic_car);
        binding.tvProductName.setText(name);
        binding.tvProductPrice.setText(price);
        binding.tvProductDescription.setText(description);
        binding.imgProduct.setImageResource(image);

        binding.btnAddCart.setOnClickListener(v->{
            Toast.makeText(this,"Товар добавлен в корзину",
                    Toast.LENGTH_SHORT).show();
        });
        binding.btnBack.setOnClickListener(v->
            finish());

    }
}