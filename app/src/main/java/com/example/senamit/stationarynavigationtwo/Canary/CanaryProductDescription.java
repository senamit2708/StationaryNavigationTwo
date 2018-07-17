package com.example.senamit.stationarynavigationtwo.Canary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senamit.stationarynavigationtwo.R;

public class CanaryProductDescription extends Fragment {

    private static final String TAG = CanaryProductDescription.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";

    private Context context;
    private String productId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getArguments().getString(PRODUCT_KEY);
        Log.i(TAG, "inside oncreate product description "+productId);
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
    }
}
