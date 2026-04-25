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

import com.example.mydns.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences =getSharedPreferences("user_data",MODE_PRIVATE);
        binding.btnRegister.setOnClickListener(v->{
            String name=binding.etName.getText().toString().trim();
            String email=binding.etEmail.getText().toString().trim();
            String password=binding.etPassword.getText().toString().trim();
            if(name.isEmpty()){
                binding.etName.setError("Введите имя");
                return;
            }
            if(email.isEmpty()){
                binding.etEmail.setError("Введите email");
                return;
            }
            if(password.isEmpty()){
                binding.etPassword.setError("Введите password");
                return;
            }
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("name",name);
            editor.putString("email",email);
            editor.putString("password",password);
            editor.apply();
            Toast.makeText(this,"Регистрация успешно",
                    Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(RegisterActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();

        });
    }
}