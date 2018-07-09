package com.example.senamit.stationarynavigationtwo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
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

    private final LiveData<Product> productLiveData = Transformations.map(liveData, new Deserializer());

    public ProductForSaleViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "inside the constructor of view model ");
    }
    public LiveData<Product> getDataSnapshotLiveData(){
        return productLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, Product> {
        @Override
        public Product apply(DataSnapshot dataSnapshot) {
            return dataSnapshot.child("-LH--vB_4LZVAK_SS1J1").getValue(Product.class);
        }
    }
}
