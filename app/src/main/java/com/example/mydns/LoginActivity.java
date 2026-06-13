package com.example.mydns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydns.databinding.ActivityLoginBinding;

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

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private final OkHttpClient client=new OkHttpClient();
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences =getSharedPreferences("auth_data",MODE_PRIVATE);
        String accessToken=preferences.getString("access_token",null);
        if(accessToken!=null){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);

            startActivity(intent);
            finish();
            return;
        }
        binding.btnLogin.setOnClickListener(v->{

            String email=binding.etEmail.getText().toString().trim();
            String password=binding.etPassword.getText().toString().trim();


            if(email.isEmpty()){
                binding.etEmail.setError("Введите email");
                return;
            }
            if(password.isEmpty()){
                binding.etPassword.setError("Введите password");
                return;
            }
//           if(email.equals(savedEmail)&&password.equals(savedPassword)){
//               Toast.makeText(this,"Вход успешно",
//                       Toast.LENGTH_SHORT).show();
//               Intent intent=new Intent(LoginActivity.this,
//                       MainActivity.class);
//               intent.putExtra("user_name",savedName);
//               startActivity(intent);
//               finish();
//           }
//           else{
//               Toast.makeText(this,"Неверный email или password",
//                       Toast.LENGTH_SHORT).show();
//           }
            loginUser(email,password);
        });
        binding.btnGoRegister.setOnClickListener(v->{
            Intent intent=new Intent(LoginActivity.this,
                    RegisterActivity.class);
            startActivity(intent);
        });
    }
    private void loginUser(String email,String password){
        try {
            JSONObject json=new JSONObject();
            json.put("email",email);
            json.put("password",password);

            RequestBody body=RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            Request request=new Request.Builder()
                    .url(SupabaseClient.URL+"/auth/v1/token?grant_type=password")
                    .addHeader("apikey",SupabaseClient.API_KEY)
                    .addHeader("Content-Type","application/json")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(()-> Toast.makeText(LoginActivity.this,
                            "Ошибка подключения"+e.getMessage(),
                            Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody=response.body().string();
                    if(response.isSuccessful()){
                        try {
                            JSONObject object=new JSONObject(responseBody);
                            String accessToken=object.getString("access_token");
                            String refreshToken=object.getString("refresh_token");
                            JSONObject user=object.getJSONObject("user");
                            String userId=user.getString("id");
                            String userEmail=user.getString("email");

                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("access_token",accessToken);
                            editor.putString("refresh_token",refreshToken);
                            editor.putString("user_id",userId);
                            editor.putString("user_email",userEmail);
                            editor.apply();
                            runOnUiThread(()->Toast.makeText(LoginActivity.this,
                                    "Вход выполнен",
                                    Toast.LENGTH_SHORT).show());
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("user_name",userEmail);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            runOnUiThread(()-> Toast.makeText(LoginActivity.this,
                                    "Ошибка обработки ответа",
                                    Toast.LENGTH_SHORT).show());
                        }
                    }
                    else {
                        runOnUiThread(()-> Toast.makeText(LoginActivity.this,
                                "Ошибка регистрации "+ response.code()+"\n"+responseBody,
                                Toast.LENGTH_SHORT).show());
                        Log.d("SUPABASE",responseBody);
                    }
                }
            });
        } catch (JSONException e) {
            runOnUiThread(()-> Toast.makeText(LoginActivity.this,
                    "Ошибка данных ",
                    Toast.LENGTH_SHORT).show());
        }
    }
}