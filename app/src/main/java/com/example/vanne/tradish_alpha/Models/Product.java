package com.example.vanne.tradish_alpha.Models;

public class Product
{
    private String m_name;
    private double m_value;

    public Product(String name, double value)
    {
        m_name = name;
        m_value = value;
    }

    public String getName()
    {
        return m_name;
    }

    public double getValue()
    {
        return m_value;
    }
}

