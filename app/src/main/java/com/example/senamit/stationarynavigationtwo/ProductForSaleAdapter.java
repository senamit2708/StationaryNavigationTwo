package com.example.senamit.stationarynavigationtwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by senamit on 7/7/18.
 */

public class ProductForSaleAdapter extends RecyclerView.Adapter<ProductForSaleAdapter.ViewHolder> {

    private static final String TAG = ProductForSaleAdapter.class.getSimpleName();

    private Context context;
    private Product product;

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

        if (product!= null){
            holder.txtProductNumber.setText(product.getProductNumber());
            holder.txtProductName.setText(product.getProductName());
            holder.txtProductPrice.setText(product.getProductPrice());
        }else {
            holder.txtProductNumber.setText("no product found");
            holder.txtProductName.setText("no product found");
            holder.txtProductPrice.setText("no product found");
        }
    }

    @Override
    public int getItemCount() {
        if (product!= null){
            return 1;
        }else{
            return 0;
        }
    }

    public void setProduct(Product mProduct){
        product = mProduct;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtProductNumber;
        TextView txtProductName;
        TextView txtProductPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
        }
    }
}
