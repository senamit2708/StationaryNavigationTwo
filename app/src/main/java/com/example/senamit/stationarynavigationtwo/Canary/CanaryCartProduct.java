package com.example.senamit.stationarynavigationtwo.Canary;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senamit.stationarynavigationtwo.CartProductAdapter;
import com.example.senamit.stationarynavigationtwo.Product;
import com.example.senamit.stationarynavigationtwo.ProductForSaleViewModel;
import com.example.senamit.stationarynavigationtwo.R;
import com.example.senamit.stationarynavigationtwo.UserCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.List;


public class CanaryCartProduct extends Fragment {

    private static final String TAG = CanaryCartProduct.class.getSimpleName();

    private Context context;
    private String mUserId;
//    private UserCart userCart;
    private List<UserCart> userCart;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CartProductAdapter mAdapter;

    private ProductCartViewModel mViewModel;

    private FirebaseUser mFirebaseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context= container.getContext();
        View view = inflater.inflate(R.layout.activity_canary_cart_product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mFirebaseUser.getUid();

        mViewModel = ViewModelProviders.of(this).get(ProductCartViewModel.class);
        mRecyclerView = view.findViewById(R.id.recycler_cart);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new CartProductAdapter(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mViewModel.getCartData(mUserId).observe(this, new Observer<List<UserCart>>() {
            @Override
            public void onChanged(@Nullable List<UserCart> userCarts) {
                if (userCarts!= null){
                    int size= userCarts.size();
//                    if (userCarts.get(size).getProductPrice()!=null){
                        mAdapter.setCartProduct(userCarts);
//                    }

                }
            }
        });

//        mViewModel.getProductData().observe(this, new Observer<List<Product>>() {
//            @Override
//            public void onChanged(@Nullable List<Product> products) {
//                if (products!= null){
//                    mAdapter.setProduct(products);
//                }
//            }
//        });

    }
}
