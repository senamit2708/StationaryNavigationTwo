package com.example.senamit.stationarynavigationtwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private static final String TAG = CartProductAdapter.class.getSimpleName();

    private Context context;
    private List<UserCart> cartProductList;
    private List<Product> productList;

    public CartProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_canary_cart_product_recycler,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cartProductList!= null){
            Log.i(TAG, "inside onBindViewHolder "+cartProductList.get(position).getProductNumber());
            holder.txtProductNumber.setText(cartProductList.get(position).getProductNumber());
            holder.txtProductPrice.setText(cartProductList.get(position).getProductPrice());
        }
//        if (productList!= null){
//            holder.txtProductPrice.setText(productList.get(position).getProductPrice());
//        }
        else {
            Log.i(TAG, "the cartProductList is null");
//            holder.txtProductNumber.setText("No Product found");
            holder.txtProductPrice.setText("price not loaded");
        }

    }

    @Override
    public int getItemCount() {
        if (cartProductList != null){
            return cartProductList.size();
        }else {
            return 0;
        }

    }

    public void setCartProduct(List<UserCart> cartProductList){
        this.cartProductList = cartProductList;
        notifyDataSetChanged();
        Log.i(TAG, "inside setCartProduct method");
    }

//    public void setProduct(List<Product> productList) {
//        this.productList = productList;
//        notifyDataSetChanged();
//        Log.i(TAG, "inside setProduct method");
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       ImageView imageProduct;
       TextView txtProductName;
       TextView txtProductNumber;
       TextView txtProductPrice;
       Button btnRemove;
       Button btnSaveForLater;

        public ViewHolder(View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            btnSaveForLater = itemView.findViewById(R.id.btnSaveForLater);
        }
    }
}
