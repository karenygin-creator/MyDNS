package com.example.mydns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydns.databinding.ActivityMainBinding;
import com.example.mydns.databinding.ActivityOrderBinding;

public class OrderActivity extends AppCompatActivity {
    private CartAdapter cartAdapter;
    private ActivityOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartAdapter =new CartAdapter(CartManager.cartProducts,this::updateTotalPrice);
        updateTotalPrice();

        binding.btnBack.setOnClickListener(v-> {
            Intent intent = new Intent(OrderActivity.this,
                    MainActivity.class);
            startActivity(intent);
        });


        binding.btnOrder.setOnClickListener(v-> {

            String name=binding.etName.getText().toString().trim();
            String number=binding.etNumber.getText().toString().trim();
            String address=binding.etAddress.getText().toString().trim();

            if(name.isEmpty()){
                binding.etName.setError("Введите имя");
                return;
            }
            if(number.isEmpty()){
                binding.etNumber.setError("Введите номер телефона");
                return;
            }
            if(address.isEmpty()){
                binding.etAddress.setError("Введите адрес");
                return;
            }
            Toast.makeText(this,"Успешно заказано",
                    Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(OrderActivity.this,
                    OrderSuccessActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateTotalPrice(){
        int total=0;
        for(CartItem item:CartManager.cartProducts){
            total+=item.getTotalPrice();
        }
        binding.tvTotalPriceOrder.setText("Итого: "+total+" ₽");
    }
}