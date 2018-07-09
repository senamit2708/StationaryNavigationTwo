package com.example.senamit.stationarynavigationtwo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.arch.core.util.Function;

/**
 * Created by senamit on 7/7/18.
 */

public class ProductForSaleViewModel extends AndroidViewModel {

    private static final String TAG = ProductForSaleViewModel.class.getSimpleName();

    private static final DatabaseReference PRODUCT_FOR_SALE = FirebaseDatabase
            .getInstance().getReference("/products");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(PRODUCT_FOR_SALE);

//    private final LiveData<Product> productLiveData = Transformations.map(liveData, new Deserializer());
    private final MediatorLiveData<Product> productLiveData = new MediatorLiveData<>();

    public ProductForSaleViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "inside the constructor of view model ");
        productLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                if (dataSnapshot!= null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            productLiveData.postValue(dataSnapshot.child("-LH--vB_4LZVAK_SS1J1").getValue(Product.class));
                        }
                    }).start();
                }else {
                    productLiveData.setValue(null);
                }
            }
        });

    }
    public LiveData<Product> getDataSnapshotLiveData(){
        return productLiveData;
    }

//    private class Deserializer implements Function<DataSnapshot, Product> {
//        @Override
//        public Product apply(DataSnapshot dataSnapshot) {
//            return dataSnapshot.child("-LH--vB_4LZVAK_SS1J1").getValue(Product.class);
//        }
//    }
}
