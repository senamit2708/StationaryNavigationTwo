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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.senamit.stationarynavigationtwo.Product;
import com.example.senamit.stationarynavigationtwo.ProductForSaleViewModel;
import com.example.senamit.stationarynavigationtwo.R;
import com.example.senamit.stationarynavigationtwo.UserCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import androidx.navigation.Navigation;

public class CanaryProductDescription extends Fragment implements View.OnClickListener{

    private static final String TAG = CanaryProductDescription.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
    private static final String PRODUCT_INDEX = "product_index";

    private Context context;
    private String mProductNumber;
    private int clickedItemIndex;
    private Product product;
    private String userId;

    private TextView mTxtProductName;
    private TextView mTxtProductPrice;
    private ImageView mProductImage;
    private Button mBtnAddToCart;
    private Button mBtnBuyNow;

    private ProductForSaleViewModel mViewModel;
    private DatabaseReference mDatabase;
    private DatabaseReference mUserDatabase;
    private LiveData<DataSnapshot> liveData;
    private FirebaseUser mFirebaseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductNumber = getArguments().getString(PRODUCT_KEY);
        clickedItemIndex = getArguments().getInt(PRODUCT_INDEX);
        Log.i(TAG, "inside oncreate product description "+mProductNumber);
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
        mBtnAddToCart = view.findViewById(R.id.btnAddToCart);
        mBtnBuyNow = view.findViewById(R.id.btnBuyNow);

        mBtnBuyNow.setOnClickListener(this);
        mBtnAddToCart.setOnClickListener(this);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mViewModel = ViewModelProviders.of(this).get(ProductForSaleViewModel.class);

        liveData= mViewModel.getProductMutableLiveData(mProductNumber);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddToCart:
                pushProductToCart();
//                Navigation.findNavController(view).navigate();
                break;
            case R.id.btnBuyNow:
                Navigation.findNavController(view).navigate(R.id.action_canaryProductDescription_to_canaryCartProduct);

            default:
                Log.i(TAG, "Select any other click option");
        }
    }

    private void pushProductToCart() {
        UserCart cart = new UserCart(mProductNumber);
        Map<String, Object> cartValue = cart.toMap();
        Map<String, Object> childUpdate = new HashMap<>();
        Log.i(TAG, "username is "+mFirebaseUser.getUid());
        mUserDatabase = mDatabase.child("users").child(userId).child("cart");
        childUpdate.put("/"+mProductNumber+"/", cartValue);
        mUserDatabase.updateChildren(childUpdate);
    }
}
