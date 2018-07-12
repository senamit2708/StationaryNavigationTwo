package com.example.senamit.stationarynavigationtwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.flags.IFlagProvider;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class ProductImageUpload extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProductImageUpload.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST= 71;

    Button mBtnUpload;
    Button mBtnSelect;
    Button mBtnDownload;
    ImageView mImage;
    ImageView mImageDownload;
    //private Uri filePath;
    private Uri uri;
    private Uri downloadImageUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image_upload);
        mBtnUpload = findViewById(R.id.btnUpload);
        mBtnSelect = findViewById(R.id.btnSelect);
        mBtnDownload = findViewById(R.id.btnDownload);
        mImage = findViewById(R.id.image);
        mImageDownload = findViewById(R.id.imageDownload);

        mBtnSelect.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);
        mBtnDownload.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReference();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSelect:
                chooseImage();

                break;
            case R.id.btnUpload:
                uploadImage();
                break;
            case R.id.btnDownload:
                downloadImage();

                break;
            default:
                Log.i(TAG, "no click button found");
        }
    }

    private void downloadImage() {
        String url = downloadImageUri.toString();
        Picasso.with(this).load(url).into(mImageDownload);
    }

    private void uploadImage() {
        if (uri!= null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("uploading....");
            progressDialog.show();
            final StorageReference ref = storageReference.child("product/images/"+ UUID.randomUUID().toString());
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(ProductImageUpload.this, "Upload successful", Toast.LENGTH_SHORT).show();
                  getUrlOfImage();

                }

                private void getUrlOfImage() {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadImageUri= uri;
                            Log.i(TAG, "the download link is "+uri);
                        }
                    });
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProductImageUpload.this,
                                    "Upload Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");

                        }
                    })
            .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            // Forward any exceptions
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            Log.i(TAG, "the download url is "+ref.getDownloadUrl());
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
