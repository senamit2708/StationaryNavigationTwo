package com.example.senamit.stationarynavigationtwo;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by senamit on 6/7/18.
 */
@IgnoreExtraProperties
public class Product {
    String productName;
    String productNumber;
    String productPrice;

    public Product(String productName, String productNumber, String productPrice) {
        this.productName = productName;
        this.productNumber = productNumber;
        this.productPrice = productPrice;
    }

    public Product() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("productNumber", productNumber);
        result.put("productName", productName);
        result.put("productPrice", productPrice);

        return result;
    }
}
