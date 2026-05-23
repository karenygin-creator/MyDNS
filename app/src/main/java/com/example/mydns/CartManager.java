package com.example.mydns;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    public static List<CartItem>
    cartProducts=new ArrayList<>();
    public static void addProduct(Product product){
        for (CartItem item:cartProducts){
            if(item.getProduct().getName().equals(product.getName())){
                item.plus();
                return;
            }

        }
        cartProducts.add(new CartItem(product));
    }
    public static void removeItem(CartItem item){
        cartProducts.remove(item);
    }
}
