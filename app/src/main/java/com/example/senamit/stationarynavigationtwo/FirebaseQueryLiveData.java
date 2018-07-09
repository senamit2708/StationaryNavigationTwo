package com.example.senamit.stationarynavigationtwo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by senamit on 9/7/18.
 */

public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {

    private static final String TAG = FirebaseQueryLiveData.class.getSimpleName();

    private Query query;
    private MyValueEventListener listener = new MyValueEventListener();

    public FirebaseQueryLiveData(Query query) {
        Log.i(TAG, "inside the constructor query "+query);
        this.query = query;
    }

    public FirebaseQueryLiveData(DatabaseReference ref) {
        Log.i(TAG, "inside constructor "+ref);
        this.query = ref;
    }


    @Override
    protected void onActive() {
        super.onActive();
        query.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        query.removeEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.i(TAG, "cant listen to query, live data class error");
        }
    }
}
