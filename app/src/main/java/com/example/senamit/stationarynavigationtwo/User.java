package com.example.senamit.stationarynavigationtwo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by senamit on 1/7/18.
 */
@IgnoreExtraProperties
public class User {
//   private String username;
   private String mobileNumber;

    public User(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public User() {
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
