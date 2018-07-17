package com.example.senamit.stationarynavigationtwo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

/**
 * Created by senamit on 7/7/18.
 */

public class ProductForSaleAdapter extends RecyclerView.Adapter<ProductForSaleAdapter.ViewHolder> {

    private static final String TAG = ProductForSaleAdapter.class.getSimpleName();

//    private ProductItemClickListerner mListener;
    private Context context;
    private List<Product> product;
    String productId;
    Bundle bundle;

    private static final String PRODUCT_KEY = "product_key";

//    public ProductForSaleAdapter(Context context, ProductItemClickListerner listerner) {
//        this.context = context;
//        mListener = listerner;
//
//    }

    public ProductForSaleAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_product_list_recycler_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//         productId = product.get(position).getProductNumber();

        if (product!= null){
            holder.txtProductNumber.setText(product.get(position).getProductNumber());
            holder.txtProductName.setText(product.get(position).getProductName());
            holder.txtProductPrice.setText(product.get(position).getProductPrice());
            String imageUrl = product.get(position).getImageUrl();
            Picasso.with(context).load(imageUrl).into(holder.imageProduct);
        }else {
            holder.txtProductNumber.setText("no product found");
            holder.txtProductName.setText("no product found");
            holder.txtProductPrice.setText("no product found");
        }
//        createOnClickListener(productId);
    }

//    private void createOnClickListener(String productId){
//
//         new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i(TAG, "inside createonclicklistener recycler adapter");
//                Navigation.findNavController(view).navigate(R.id.action_canaryProductForSaleFragment_to_canaryProductDescription);
//            }
//        };
//    }

    @Override
    public int getItemCount() {
        if (product!= null){
            Log.i(TAG, "the size of product is "+product.size());
            return product.size();
        }else{
            return 0;
        }
    }

    public void setProduct(List<Product> mProduct){
        product = mProduct;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtProductNumber;
        TextView txtProductName;
        TextView txtProductPrice;
        ImageView imageProduct;
        ImageButton btnFavorite;
        public ViewHolder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int clickedItemIndex= getAdapterPosition();
            productId= product.get(clickedItemIndex).getProductNumber();
             bundle = new Bundle();
            bundle.putString(PRODUCT_KEY, productId);
            Log.i(TAG, "inside createonclicklistener recycler adapter, productId is"+productId);
            Navigation.findNavController(view).navigate(R.id.action_canaryProductForSaleFragment_to_canaryProductDescription, bundle);
        }
    }

}
