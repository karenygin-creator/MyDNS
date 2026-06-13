package com.example.mydns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydns.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private SharedPreferences preferences;
    private final OkHttpClient client=new OkHttpClient();
    private String registerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            if(password.length()<6){
                binding.etPassword.setError("Пароль минимум 6 символов");
                return;
            }
            registerName=name;
            registerUser(email,password);
//            SharedPreferences.Editor editor=preferences.edit();
//            editor.putString("name",name);
//            editor.putString("email",email);
//            editor.putString("password",password);
//            editor.apply();
//            Toast.makeText(this,"Регистрация успешно",
//                    Toast.LENGTH_SHORT).show();
//
//            Intent intent=new Intent(RegisterActivity.this,
//                    LoginActivity.class);
//            startActivity(intent);
//            finish();

        });
    }
    private void registerUser(String email,String password){
        try {
            JSONObject json=new JSONObject();
            json.put("email",email);
            json.put("password",password);

            RequestBody body=RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            Request request=new Request.Builder()
                    .url(SupabaseClient.URL+"/auth/v1/signup")
                    .addHeader("apikey",SupabaseClient.API_KEY)
                    .addHeader("Content-Type","application/json")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(()-> Toast.makeText(RegisterActivity.this,
                                "Ошибка подключения",
                                Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String responseBody=response.body().string();
                        if(response.isSuccessful()){
                            try {
                                JSONObject object=new JSONObject(responseBody);
                                String accessToken=object.getString("access_token");
                                String userId=object.getJSONObject("user")
                                        .getString("id");
                                createProfile(userId,registerName,accessToken);
                            } catch (JSONException e) {
                                runOnUiThread(()-> Toast.makeText(RegisterActivity.this,
                                        "Ошибка обработки ответа",
                                        Toast.LENGTH_SHORT).show());
                            }
                        }
                        else {
                            runOnUiThread(()-> Toast.makeText(RegisterActivity.this,
                                    "Ошибка регистрации "+ response.code()+"\n"+responseBody,
                                    Toast.LENGTH_SHORT).show());
                            Log
                        }
                }
            });
        } catch (JSONException e) {
            runOnUiThread(()-> Toast.makeText(RegisterActivity.this,
                    "Ошибка данных ",
                    Toast.LENGTH_SHORT).show());
        }
    }
    private void createProfile(String userId,String name,String accessToken){
        try {
            JSONObject json=new JSONObject();
            json.put("id",userId);
            json.put("name",name);

            RequestBody body=RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            Request request=new Request.Builder()
                    .url(SupabaseClient.URL+"/rest/v1/profile")
                    .addHeader("apikey",SupabaseClient.API_KEY)
                    .addHeader("Authorization","Bearer"+accessToken)
                    .addHeader("Content-Type","application/json")
                    .addHeader("Prefer","return=minimal")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(()-> Toast.makeText(RegisterActivity.this,
                            "Профиль не создан",
                            Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        runOnUiThread(()-> Toast.makeText(RegisterActivity.this,
                                "Регистрация успешна",
                                Toast.LENGTH_SHORT).show());
                        Intent intent=new Intent(RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        String error=response.body().string();
                        runOnUiThread(()-> Toast.makeText(RegisterActivity.this,
                                "Ошибка профиля "+error,
                                Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } catch (JSONException e) {
            runOnUiThread(()-> Toast.makeText(RegisterActivity.this,
                    "Ошибка профиля ",
                    Toast.LENGTH_SHORT).show());
        }
    }
}