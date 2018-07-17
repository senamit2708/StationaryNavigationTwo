package com.example.senamit.stationarynavigationtwo.Canary;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.senamit.stationarynavigationtwo.Product;
import com.example.senamit.stationarynavigationtwo.ProductEntry;
import com.example.senamit.stationarynavigationtwo.ProductForSale;
import com.example.senamit.stationarynavigationtwo.ProductForSaleAdapter;
import com.example.senamit.stationarynavigationtwo.ProductForSaleViewModel;
import com.example.senamit.stationarynavigationtwo.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import androidx.navigation.Navigation;

public class CanaryProductForSaleFragment extends Fragment  {
    private static final String TAG = CanaryProductForSaleFragment.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
    private static final String PRODUCT_SEND = "product_Send";
    String key = null;

    private Context context;

    private DatabaseReference mDatabase;

    TextView txtProductName;
    TextView txtProductNumber;
    FloatingActionButton fabButton;
    Button mBtnTest;


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductForSaleAdapter mAdapter;
    private ProductForSaleViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_canary_product_for_sale_fragment, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "the value of mViewModel is "+mViewModel);
        mViewModel = ViewModelProviders.of(this).get(ProductForSaleViewModel.class);
        Log.i(TAG, "the value of mViewModel is "+mViewModel);
        mRecyclerView = view.findViewById(R.id.recycler_product);
        mLayoutManager = new GridLayoutManager(context, 2);
        mAdapter = new ProductForSaleAdapter(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        fabButton = view.findViewById(R.id.fab);


        fabButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_canaryProductForSaleFragment_to_canaryProductEntryFragment, null));



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



//    @Override
//    public void onProductItemClick(int clickedItemIndex, Product product) {
//        Log.i(TAG,"inside the onproduct item click of fragment");
//        String productNumber = product.getProductNumber();
//        mViewModel.setProductNumber(productNumber);
//        Navigation.createNavigateOnClickListener(R.id.action_canaryProductForSaleFragment_to_canaryProductDescription, null);
//
//
//    }
}
