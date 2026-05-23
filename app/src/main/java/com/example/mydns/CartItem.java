package com.example.mydns;

import android.util.Log;

public class CartItem {
    private Product product;
    private int count;

    public CartItem(Product product) {
        this.product = product;
        this.count = 1;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }
    public void plus(){
        count++;
    }
    public void minus(){
        if(count>1){
            count--;
        }
    }
    public int getTotalPrice() {
        String price = product.getPrice().replaceAll("[^0-9]", "");
        return Integer.parseInt(price)*count;
    }
}
