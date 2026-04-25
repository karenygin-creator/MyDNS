package com.example.mydns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mydns.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<Product>products;
    private ProductAdapter adapter;
    private String userName="Пользователь";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String nameFromIntent=getIntent().getStringExtra("user_name");
        if(nameFromIntent!=null && !nameFromIntent.isEmpty()){
            userName=nameFromIntent;
        }
        binding.tvProfileName.setText("Имя: " + userName);
        binding.etProfileName.setText(userName);
        setupProducts();
        setupButtonMenu();
        showHome();
        binding.btnSaveProfile.setOnClickListener(v->{
            String newName=binding.etProfileName.getText().toString().trim();
            if(newName.isEmpty()){
                binding.etProfileName.setError("Введите имя");
                return;
            }
            userName=newName;
            binding.tvProfileName.setText("Имя: " + userName);
            Toast.makeText(this,"Имя сохранено",Toast.LENGTH_SHORT).show();

        });
        binding.btnLogout.setOnClickListener(v->{
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
    private void setupProducts(){
        products=new ArrayList<>();
        products.add(new Product("Ноутбук Asus","59 999 ₽","good",R.drawable.ic_car));
        products.add(new Product("Смартфон Samsung","59 999 ₽","good",R.drawable.ic_car));
        products.add(new Product("Телевизор LG","59 999 ₽","good",R.drawable.ic_car));
        products.add(new Product("Наушники","59 999 ₽","good",R.drawable.ic_car));
        products.add(new Product("Планшет","59 999 ₽","good",R.drawable.ic_car));
        products.add(new Product("Монитор","59 999 ₽","good",R.drawable.ic_car));
        adapter=new ProductAdapter(products);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerView.setAdapter(adapter);
    }
    private void setupButtonMenu(){
        binding.bottomNav.setOnItemSelectedListener(item->{
            int id= item.getItemId();
            if(id==R.id.nav_home){
                showHome();
                return true;
            }
            if(id==R.id.nav_profile){
                showProfile();
                return true;
            }
            if(id==R.id.nav_shop){
                showShop();
                return true;
            }
            return false;
        });
    }
    private void showHome(){
        binding.frameHome.setVisibility(View.VISIBLE);
        binding.frameProfile.setVisibility(View.GONE);
        binding.frameShop.setVisibility(View.GONE);

    }
    private void showProfile(){
        binding.frameHome.setVisibility(View.GONE);
        binding.frameProfile.setVisibility(View.VISIBLE);
        binding.frameShop.setVisibility(View.GONE);

    }
    private void showShop(){
        binding.frameHome.setVisibility(View.GONE);
        binding.frameProfile.setVisibility(View.GONE);
        binding.frameShop.setVisibility(View.VISIBLE);

    }
}