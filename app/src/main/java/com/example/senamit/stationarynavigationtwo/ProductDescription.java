package com.example.senamit.stationarynavigationtwo;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ProductDescription extends AppCompatActivity {

    private final static String TAG = ProductDescription.class.getSimpleName();

    private ProductForSaleViewModel mProductForSaleViewModel;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

    }
}
