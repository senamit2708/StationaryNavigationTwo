package com.example.senamit.stationarynavigationtwo;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserCart {

    private String productNumber;
    private String date="26th Aug 2019";

    public UserCart(String productNumber) {
        this.productNumber = productNumber;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("productNumber", productNumber);
        result.put("date", date);
        return result;
    }

}
