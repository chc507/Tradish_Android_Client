package com.example.vanne.tradish_alpha.Models;

import android.app.ProgressDialog;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Cart
{
    private Map<Product, Integer> m_cart;
    private double m_value = 0;

    public Cart()
    {
        m_cart = new LinkedHashMap<>();
    }

    public void addToCart(Product product)
    {
        if(m_cart.containsKey(product))
            m_cart.put(product, m_cart.get(product) + 1);
        else
            m_cart.put(product, 1);

        m_value += product.getValue();
    }

    public void removeFromCart(Product product){
        if(m_cart.containsKey(product)) {
            m_value -= product.getValue();
            if (m_cart.get(product) > 1) {
                //if quantity is greater than 1, remove the value corresponding to the key
                m_cart.put(product, m_cart.get(product) - 1);
            } else {
                //if quantity is less than 1, remove it from the linked hash map
                m_cart.remove(product);
            }
        }
    }

    public int getQuantity(Product product)
    {
        return m_cart.get(product);
    }

    public Set getProducts()
    {
        return m_cart.keySet();
    }

    public void empty()
    {
        m_cart.clear();
        m_value = 0;
    }

    public double getValue()
    {
        return m_value;
    }

    public int getSize()
    {
        return m_cart.size();
    }
}
