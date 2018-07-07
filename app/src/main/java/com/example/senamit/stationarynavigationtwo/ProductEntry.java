package com.example.senamit.stationarynavigationtwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProductEntry extends AppCompatActivity {

    private static final String TAG = ProductEntry.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";

    EditText mEdtProductNumber, mEdtProductName, mEdtProductPrice;
    Button mBtnSubmit;

    //database reference
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_entry);
        
        mEdtProductNumber= findViewById(R.id.edt_product_number);
        mEdtProductName= findViewById(R.id.edt_product_name);
        mEdtProductPrice = findViewById(R.id.edt_price);
        mBtnSubmit = findViewById(R.id.btn_submit);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProduct();
            }
        });


    }

    private void submitProduct() {
        final String productNumber = mEdtProductNumber.getText().toString();
        final String productName = mEdtProductName.getText().toString();
        final String productPrice = mEdtProductPrice.getText().toString();

        //product number is required
        if (TextUtils.isEmpty(productNumber)){
            mEdtProductNumber.setError("REQUIRED");
            return;
        }
        //product name is required
        if (TextUtils.isEmpty(productName)){
            mEdtProductName.setError("REQUIRED");
            return;
        }
        //pirce is required
        if (TextUtils.isEmpty(productPrice)){
            mEdtProductPrice.setError("REQURED");
            return;
        }
        //Disable multiple record entry of product, by disabling the entry of product.
        setEditingEnable(false);

        Toast.makeText(this, "the product is uploading ", Toast.LENGTH_SHORT).show();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String key = writeNewPost(userId,productNumber, productName, productPrice );
        setEditingEnable(true);
        showProduct(key);

    }

    private void showProduct(String key) {
        Intent intent = new Intent(ProductEntry.this, ProductForSale.class);
        intent.putExtra(PRODUCT_KEY, key);
        startActivity(intent);
        finish();
    }

    private String writeNewPost(String userId, String productNumber, String productName, String productPrice) {
        String key = mDatabase.child("products").push().getKey();
        Product product = new Product(productName, productNumber, productPrice);
        Map<String, Object> productValues = product.toMap();

        Map<String, Object> childUpdate = new HashMap<>();
        //here we can use multiple put to the childUpdate to insert data in multiple nodes at a time.
        childUpdate.put("/products/"+key, productValues);
        mDatabase.updateChildren(childUpdate);
        return key;


    }

    private void setEditingEnable(boolean enabled) {
        mEdtProductNumber.setEnabled(enabled);
        mEdtProductName.setEnabled(enabled);
        mEdtProductPrice.setEnabled(enabled);

        if (enabled){
            mBtnSubmit.setVisibility(View.VISIBLE);
        }else {
            mBtnSubmit.setVisibility(View.GONE);
        }
    }
}
