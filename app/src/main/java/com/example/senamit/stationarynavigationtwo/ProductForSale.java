package com.example.senamit.stationarynavigationtwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductForSale extends AppCompatActivity {

    private static final String TAG = ProductForSale.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
    String key = null;

   private DatabaseReference mDatabase;

   TextView txtProductName;
   TextView txtProductNumber;
   FloatingActionButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_for_sale);

        txtProductName = findViewById(R.id.txtProductName);
        txtProductNumber= findViewById(R.id.txtProductNumber);
        fabButton = findViewById(R.id.fab);

        String key = getIntent().getStringExtra(PRODUCT_KEY);
        Log.i(TAG, "the key is "+key);


        if (key != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("products").child(key);
            Log.i(TAG, "the mdatabase ref is " + mDatabase);



            ValueEventListener productListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i(TAG, "inside onDataChange method");
                    Product product = dataSnapshot.getValue(Product.class);
                    txtProductName.setText(product.getProductName());
                    txtProductNumber.setText(product.getProductNumber());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(ProductForSale.this, "failed to load product", Toast.LENGTH_SHORT).show();
                }
            };
            mDatabase.addValueEventListener(productListener);
        }




        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductForSale.this, ProductEntry.class));
                finish();
            }
        });
    }
}
