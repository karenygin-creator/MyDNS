package com.example.mydns;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydns.databinding.ActivityMainBinding;

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
    }
}