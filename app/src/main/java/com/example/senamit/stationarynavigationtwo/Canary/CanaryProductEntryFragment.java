package com.example.senamit.stationarynavigationtwo.Canary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.senamit.stationarynavigationtwo.Product;
import com.example.senamit.stationarynavigationtwo.ProductEntry;
import com.example.senamit.stationarynavigationtwo.ProductForSale;
import com.example.senamit.stationarynavigationtwo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.navigation.Navigation;

public class CanaryProductEntryFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = CanaryProductEntryFragment.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
    private final int PICK_IMAGE_REQUEST= 71;

    private Context context;

    EditText mEdtProductNumber, mEdtProductName, mEdtProductPrice;
    Button mBtnSubmit;
    Button mBtnUpload;
    Button mBtnSelect;
    ImageView mImage;

    //private Uri filePath;
    private Uri uri;
    private String firebaseImageName;
    private Uri downloadImageUri;


    //database reference
    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       context = container.getContext();
        View view = inflater.inflate(R.layout.activity_canary_product_entry_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEdtProductNumber= view.findViewById(R.id.edt_product_number);
        mEdtProductName= view.findViewById(R.id.edt_product_name);
        mEdtProductPrice = view.findViewById(R.id.edt_price);
        mBtnSubmit = view.findViewById(R.id.btn_submit);
        mBtnUpload = view.findViewById(R.id.btnUpload);
        mBtnSelect = view.findViewById(R.id.btnSelect);
        mImage = view.findViewById(R.id.image);

        mBtnSubmit.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);
        mBtnSelect.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                submitProduct();
                break;
            case R.id.btnSelect:
                chooseImage();

                break;
            case R.id.btnUpload:
                uploadImage();
                break;
            default:
                Log.i(TAG, "no click button found");
        }
    }

    private void uploadImage() {
        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("uploading....");
            progressDialog.show();
            final StorageReference ref = storageReference.child("product/images/" + UUID.randomUUID().toString());
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    firebaseImageName = taskSnapshot.getMetadata().getName();
                    Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show();
                    getUrlOfImage();

                }

                private void getUrlOfImage() {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadImageUri = uri;
                            Log.i(TAG, "the download link is " + uri);
                        }
                    });
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,
                                    "Upload Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");

                        }
                    })
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            // Forward any exceptions
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            Log.i(TAG, "the download url is " + ref.getDownloadUrl());
                            return ref.getDownloadUrl();
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==getActivity().RESULT_OK
                && data!= null && data.getData() != null){

            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                mImage.setImageBitmap(bitmap);
                Log.i(TAG, "the image is uploaded successfully");

            } catch (IOException e) {
                Log.e(TAG, "inside the exception block of image upload");
                e.printStackTrace();
            }

        }
    }

    private void submitProduct() {
        final String productNumber = mEdtProductNumber.getText().toString();
        final String productName = mEdtProductName.getText().toString();
        final String productPrice = mEdtProductPrice.getText().toString();

        //product number is required
        if (TextUtils.isEmpty(productNumber)){
            mEdtProductNumber.setError("REQUIRED");
            return;
        }
        //product name is required
        if (TextUtils.isEmpty(productName)){
            mEdtProductName.setError("REQUIRED");
            return;
        }
        //pirce is required
        if (TextUtils.isEmpty(productPrice)){
            mEdtProductPrice.setError("REQURED");
            return;
        }
        if (downloadImageUri==null ){
            Log.i(TAG, "upload image unsuccessful, please upload");
        }
        //Disable multiple record entry of product, by disabling the entry of product.
        setEditingEnable(false);

        Toast.makeText(context, "the product is uploading ", Toast.LENGTH_SHORT).show();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String key = writeNewPost(userId,productNumber, productName, productPrice );
        setEditingEnable(true);
        reloadData();
    }

    private void reloadData() {
        Toast.makeText(context, "Product loaded successfully in database", Toast.LENGTH_SHORT).show();
        mEdtProductNumber.setText("");
        mEdtProductName.setText("");
        mEdtProductPrice.setText("");
        //https://stackoverflow.com/questions/2859212/how-to-clear-an-imageview-in-android
        mImage.setImageResource(android.R.color.transparent);


    }

    private void setEditingEnable(boolean enabled) {
        mEdtProductNumber.setEnabled(enabled);
        mEdtProductName.setEnabled(enabled);
        mEdtProductPrice.setEnabled(enabled);

        if (enabled){
            Log.w(TAG, "Button is visible now");
            mBtnSubmit.setVisibility(View.VISIBLE);
        }else {
            Log.w(TAG, "Buton is invisible now");
            mBtnSubmit.setVisibility(View.GONE);
        }
    }
    private String writeNewPost(String userId, String productNumber, String productName, String productPrice) {
//        String key = mDatabase.child("products").push().getKey();

        Product product = new Product(productName, productNumber, productPrice, downloadImageUri.toString());
        Map<String, Object> productValues = product.toMap();

        Map<String, Object> childUpdate = new HashMap<>();
        //here we can use multiple put to the childUpdate to insert data in multiple nodes at a time.
        childUpdate.put("/products/"+productNumber, productValues);
        mDatabase.updateChildren(childUpdate);
        return productNumber;


    }
}
