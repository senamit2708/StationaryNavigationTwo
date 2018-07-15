package com.example.senamit.stationarynavigationtwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProductEntry extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = ProductEntry.class.getSimpleName();
    private static final String PRODUCT_KEY = "product_key";
    private final int PICK_IMAGE_REQUEST= 71;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_entry);
        
        mEdtProductNumber= findViewById(R.id.edt_product_number);
        mEdtProductName= findViewById(R.id.edt_product_name);
        mEdtProductPrice = findViewById(R.id.edt_price);
        mBtnSubmit = findViewById(R.id.btn_submit);
        mBtnUpload = findViewById(R.id.btnUpload);
        mBtnSelect = findViewById(R.id.btnSelect);
        mImage = findViewById(R.id.image);

        mBtnSubmit.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);
        mBtnSelect.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        
//        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submitProduct();
//            }
//        });


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

        Toast.makeText(this, "the product is uploading ", Toast.LENGTH_SHORT).show();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String key = writeNewPost(userId,productNumber, productName, productPrice );
        setEditingEnable(true);
        showProduct(key);

    }

    private void showProduct(String key) {
        Intent intent = new Intent(ProductEntry.this, ProductForSale.class);
        intent.putExtra(PRODUCT_KEY, key);
        startActivity(intent);
        finish();
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

    private void setEditingEnable(boolean enabled) {
        mEdtProductNumber.setEnabled(enabled);
        mEdtProductName.setEnabled(enabled);
        mEdtProductPrice.setEnabled(enabled);

        if (enabled){
            mBtnSubmit.setVisibility(View.VISIBLE);
        }else {
            mBtnSubmit.setVisibility(View.GONE);
        }
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
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("uploading....");
            progressDialog.show();
            final StorageReference ref = storageReference.child("product/images/" + UUID.randomUUID().toString());
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    firebaseImageName = taskSnapshot.getMetadata().getName();
                    Toast.makeText(ProductEntry.this, "Upload successful", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ProductEntry.this,
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
        private void chooseImage () {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!= null && data.getData() != null){

            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mImage.setImageBitmap(bitmap);
                Log.i(TAG, "the image is uploaded successfully");

            } catch (IOException e) {
                Log.e(TAG, "inside the exception block of image upload");
                e.printStackTrace();
            }

        }
    }
}

