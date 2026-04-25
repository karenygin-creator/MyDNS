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

import com.example.mydns.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences =getSharedPreferences("user_data",MODE_PRIVATE);
        binding.btnLogin.setOnClickListener(v->{

            String email=binding.etEmail.getText().toString().trim();
            String password=binding.etPassword.getText().toString().trim();
            String savedName=preferences.getString("name","");
            String savedEmail=preferences.getString("email","");
            String savedPassword=preferences.getString("password","");

            if(email.isEmpty()){
                binding.etEmail.setError("Введите email");
                return;
            }
            if(password.isEmpty()){
                binding.etPassword.setError("Введите password");
                return;
            }
           if(email.equals(savedEmail)&&password.equals(savedPassword)){
               Toast.makeText(this,"Вход успешно",
                       Toast.LENGTH_SHORT).show();
               Intent intent=new Intent(LoginActivity.this,
                       MainActivity.class);
               intent.putExtra("user_name",savedName);
               startActivity(intent);
               finish();
           }
           else{
               Toast.makeText(this,"Неверный email или password",
                       Toast.LENGTH_SHORT).show();
           }
        });
        binding.btnGoRegister.setOnClickListener(v->{
            Intent intent=new Intent(LoginActivity.this,
                    RegisterActivity.class);
            startActivity(intent);
        });
    }
}