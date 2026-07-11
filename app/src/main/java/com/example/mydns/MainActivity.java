package com.example.mydns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mydns.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<Product>products;
    private ProductAdapter adapter;
    private CartAdapter cartAdapter;
    private String userName="Пользователь";
    private String accessToken;
    private String userId;
    private SharedPreferences preferences;
    private OkHttpClient client=new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences =getSharedPreferences("auth_data",MODE_PRIVATE);
        accessToken=preferences.getString("access_token",null);
        userId=preferences.getString("user_id",null);
//        String nameFromIntent=getIntent().getStringExtra("user_name");
//        if(nameFromIntent!=null && !nameFromIntent.isEmpty()){
//            userName=nameFromIntent;
//        }
        binding.tvProfileName.setText("Имя: загрузка...");
        binding.etProfileName.setText("");
        loadUserProfile();
        setupProducts();
        setupCart();
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
        binding.btnOrder.setOnClickListener(v-> {
            Intent intent = new Intent(MainActivity.this,
                    OrderActivity.class);
            startActivity(intent);
        });
        binding.btnLogout.setOnClickListener(v->{
            preferences.edit().clear().apply();
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
//    @Override
//    protected void onResume(){
//        super.onResume();
//        if (cartAdapter!=null){
//            cartAdapter.notifyDataSetChanged();
//        }
//    }
    private void setupProducts(){
        products=new ArrayList<>();
        adapter=new ProductAdapter(products);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerView.setAdapter(adapter);
        loadProductsFromSupabase();
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
            if(id==R.id.nav_cart){
                showCart();
                return true;
            }
            return false;
        });
    }
    private void showHome(){
        binding.frameHome.setVisibility(View.VISIBLE);
        binding.frameProfile.setVisibility(View.GONE);
        binding.frameShop.setVisibility(View.GONE);
        binding.frameCart.setVisibility(View.GONE);

    }
    private void showProfile(){
        binding.frameHome.setVisibility(View.GONE);
        binding.frameProfile.setVisibility(View.VISIBLE);
        binding.frameShop.setVisibility(View.GONE);
        binding.frameCart.setVisibility(View.GONE);

    }
    private void showShop(){
        binding.frameHome.setVisibility(View.GONE);
        binding.frameProfile.setVisibility(View.GONE);
        binding.frameShop.setVisibility(View.VISIBLE);
        binding.frameCart.setVisibility(View.GONE);

    }
    private void showCart(){
        binding.frameHome.setVisibility(View.GONE);
        binding.frameProfile.setVisibility(View.GONE);
        binding.frameShop.setVisibility(View.GONE);
        binding.frameCart.setVisibility(View.VISIBLE);
        setupCart();

    }

    private void setupCart(){
        cartAdapter =new CartAdapter(CartManager.cartProducts,this::updateTotalPrice);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.cartRecyclerView.setAdapter(cartAdapter);
        updateTotalPrice();

    }
    private void updateTotalPrice(){
        int total=0;
        for(CartItem item:CartManager.cartProducts){
            total+=item.getTotalPrice();
        }
        binding.tvTotalPrice.setText("Итого: "+total+" ₽");
        Log.d("DEBAG","summa"+total);
    }
    private void loadProductsFromSupabase(){
        Request request=new Request.Builder()
                .url(SupabaseClient.URL+"/rest/v1/products?select=*")
                .addHeader("apikey",SupabaseClient.API_KEY)
                .addHeader("Authorization","Bearer "+SupabaseClient.API_KEY)
                .addHeader("Content-Type","application/json")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(()-> Toast.makeText(MainActivity.this,
                        "Ошибка загрузки товаров"+e.getMessage(),
                        Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody=response.body().string();
                if(response.isSuccessful()){
                    try {
                        JSONArray array=new JSONArray(responseBody);
                        products.clear();
                        for(int i=0;i<array.length();i++){
                            JSONObject object=array.getJSONObject(i);
                            long id=object.getLong("id");
                            String name=object.getString("name");
                            int price=object.getInt("price");
                            String description=object.optString("description","");
                            int quantity=object.optInt("quantity",0);
                            String imageUrl=object.optString("image_url","");
                            Product product=new Product(
                                    id,
                                    name,
                                    price,
                                    quantity,
                                    description,
                                    imageUrl
                            );
                            products.add(product);
                        }
                        runOnUiThread(()->adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        runOnUiThread(()-> Toast.makeText(MainActivity.this,
                                "Ошибка обработки товаров",
                                Toast.LENGTH_SHORT).show());
                    }
                }
                else {
                    runOnUiThread(()-> Toast.makeText(MainActivity.this,
                            "Ошибка Supabase "+ response.code()+"\n"+responseBody,
                            Toast.LENGTH_SHORT).show());
                    Log.d("SUPABASE",responseBody);
                }
            }
        });

    }
    private void loadUserProfile(){
        if(accessToken==null||userId==null){
            binding.tvProfileName.setText("Имя: загрузка...");
            return;
        }
        Request request=new Request.Builder()
                .url(SupabaseClient.URL+"/rest/v1/profiles"+"?id=eq."+userId+"&select=name")
                .addHeader("apikey",SupabaseClient.API_KEY)
                .addHeader("Authorization","Bearer "+accessToken)
                .addHeader("Content-Type","application/json")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(()-> Toast.makeText(MainActivity.this,
                        "Ошибка загрузки профиля"+e.getMessage(),
                        Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody=response.body()!=null?response.body().string():"";
                if(!response.isSuccessful()){
                    runOnUiThread(()-> Toast.makeText(MainActivity.this,
                            "Ошибка Загрузки профиля "+ response.code()+"\n"+responseBody,
                            Toast.LENGTH_SHORT).show());
                    Log.d("SUPABASE",responseBody);
                    return;
                }
                try {
                    JSONArray array=new JSONArray(responseBody);
                    if(array.length()==0){
                        runOnUiThread(()->
                                binding.tvProfileName.setText("Имя пользователя"));
                        return;
                    }

                    JSONObject profile=array.getJSONObject(0);
                    String loadedName=profile.optString("name","Пользователь");
                    userName=loadedName;
                    runOnUiThread(()->{
                            binding.tvProfileName.setText("Имя: "+userName);
                            binding.etProfileName.setText(userName);
                    });

                } catch (JSONException e) {
                    runOnUiThread(()-> Toast.makeText(MainActivity.this,
                            "Ошибка обработки профиля",
                            Toast.LENGTH_SHORT).show());
                }

            }
        });

    }
}