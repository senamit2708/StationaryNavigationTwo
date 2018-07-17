package com.example.senamit.stationarynavigationtwo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductForSale extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = ProductForSale.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
    private static final String PRODUCT_SEND = "product_Send";
    String key = null;

   private DatabaseReference mDatabase;

   TextView txtProductName;
   TextView txtProductNumber;
   FloatingActionButton fabButton;


   private RecyclerView mRecyclerView;
   private RecyclerView.LayoutManager mLayoutManager;
   private ProductForSaleAdapter mAdapter;
   private ProductForSaleViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_for_sale);

        Log.i(TAG, "the value of mViewModel is "+mViewModel);
       mViewModel = ViewModelProviders.of(this).get(ProductForSaleViewModel.class);
        Log.i(TAG, "the value of mViewModel is "+mViewModel);
        mRecyclerView = findViewById(R.id.recycler_product);
        mLayoutManager = new GridLayoutManager(this, 2);
        mAdapter = new ProductForSaleAdapter(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        fabButton = findViewById(R.id.fab);


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductForSale.this, ProductEntry.class));
                finish();
            }
        });

        mViewModel.getDataSnapshotLiveData().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products!= null){
                    Log.i(TAG, "inside the onchanged method, the size of product "+products.size());
                    mAdapter.setProduct(products);
                }
            }
        });

    }


    @Override
    public void onClick(View view) {

    }

//    @Override
//    public void onProductItemClick(int clickedItemIndex, Product product) {
//        Log.i(TAG, "the product selected is "+product.getProductNumber());
//        Intent intent = new Intent(ProductForSale.this, ProductDescription.class);
//
//    }
}
