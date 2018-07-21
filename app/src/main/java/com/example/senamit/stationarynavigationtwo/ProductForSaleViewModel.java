package com.example.senamit.stationarynavigationtwo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.core.util.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by senamit on 7/7/18.
 */

public class ProductForSaleViewModel extends AndroidViewModel {

    private static final String TAG = ProductForSaleViewModel.class.getSimpleName();

    private static String mProductNumber;


    private static final DatabaseReference PRODUCT_FOR_SALE = FirebaseDatabase
            .getInstance().getReference("/products");
    private static  DatabaseReference PRODUCT_FOR_SEARCH ;


    private FirebaseQueryLiveData liveData ;
    private FirebaseQueryLiveData productDescriptionLiveData;
    private FirebaseQueryLiveData cartProductLiveData;

    private  MediatorLiveData<List<Product>> productLiveData;
    private MediatorLiveData<List<UserCart>> cartLiveData;

//    private MutableLiveData<Product> productDescription;

    public ProductForSaleViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "inside the constructor of view model ");


    }
    public LiveData<List<Product>> getDataSnapshotLiveData(){

        if (productLiveData==null){
            Log.i(TAG, "product live data is null");
            productLiveData = new MediatorLiveData<>();
            liveData = new FirebaseQueryLiveData(PRODUCT_FOR_SALE);

            loadProductLiveData();
        }
        Log.w(TAG, "the value for activity is "+productLiveData.toString());
        return productLiveData;
    }

    private void loadProductLiveData() {

        productLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<Product> productList = new ArrayList<>();
                            for (DataSnapshot productDataSnapshot : dataSnapshot.getChildren()){
                                Product product = productDataSnapshot.getValue(Product.class);
                                productList.add(product);
                                Log.i(TAG, "inside loadproduct live data"+product);

                            }
                            productLiveData.postValue(productList);
                        }
                    }).start();
                }else {
                    productLiveData.setValue(null);
                }
            }
        });
    }


    public LiveData<DataSnapshot> getProductMutableLiveData(String productNumber) {
        if (productDescriptionLiveData==null){
            mProductNumber=productNumber;
            PRODUCT_FOR_SEARCH = FirebaseDatabase
                    .getInstance().getReference("/products/"+ mProductNumber);
            productDescriptionLiveData=new FirebaseQueryLiveData(PRODUCT_FOR_SEARCH);
        }

        return productDescriptionLiveData;
    }



}
