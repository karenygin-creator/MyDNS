package com.example.mydns;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mydns.databinding.ActivityOrderBinding;
import com.example.mydns.databinding.ActivityOrderSuccessBinding;


public class OrderSuccessActivity extends AppCompatActivity {

    private ActivityOrderSuccessBinding binding;
    private OrderProductAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityOrderSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v-> {
            Intent intent = new Intent(OrderSuccessActivity.this,
                    MainActivity.class);
            startActivity(intent);
        });

        setupCart();

    }

    private void setupCart(){
        orderAdapter =new OrderProductAdapter(CartManager.cartProducts);
        binding.orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.orderRecyclerView.setAdapter(orderAdapter);
        updateTotalPrice();

    }
    private void updateTotalPrice(){
        int total=0;
        for(CartItem item:CartManager.cartProducts){
            total+=item.getTotalPrice();
        }
        binding.tvTotalPrice.setText("Итого: "+total+" ₽");
    }
}