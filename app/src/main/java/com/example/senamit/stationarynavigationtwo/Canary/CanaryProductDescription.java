package com.example.senamit.stationarynavigationtwo.Canary;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.senamit.stationarynavigationtwo.Product;
import com.example.senamit.stationarynavigationtwo.ProductForSaleViewModel;
import com.example.senamit.stationarynavigationtwo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class CanaryProductDescription extends Fragment {

    private static final String TAG = CanaryProductDescription.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
    private static final String PRODUCT_INDEX = "product_index";

    private Context context;
    private String productId;
    private int clickedItemIndex;
    private Product product;

    private TextView mTxtProductName;
    private TextView mTxtProductPrice;
    private ImageView mProductImage;

    private ProductForSaleViewModel mViewModel;
    private DatabaseReference mDatabase;
    private LiveData<DataSnapshot> liveData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getArguments().getString(PRODUCT_KEY);
        clickedItemIndex = getArguments().getInt(PRODUCT_INDEX);
        Log.i(TAG, "inside oncreate product description "+productId);
        Log.i(TAG, "inside oncreate product description "+clickedItemIndex);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.activity_canary_product_description, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtProductName = view.findViewById(R.id.txtProductName);
        mTxtProductPrice = view.findViewById(R.id.txtProductPrice);
        mProductImage = view.findViewById(R.id.imageProduct);
        mViewModel = ViewModelProviders.of(this).get(ProductForSaleViewModel.class);
        liveData= mViewModel.getProductMutableLiveData(productId);
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    Log.i(TAG, "inside onChanged method of livedata observer of product desc");
                    product= dataSnapshot.getValue(Product.class);
                    Log.i(TAG, "the product is "+product);
                    mTxtProductName.setText(product.getProductName());
                    mTxtProductPrice.setText(product.getProductPrice());
                    Picasso.with(context).load(product.getImageUrl()).into(mProductImage);

                }
            }
        });

    }
}
