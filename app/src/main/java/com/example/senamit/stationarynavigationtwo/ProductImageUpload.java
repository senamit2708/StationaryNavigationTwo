package com.example.senamit.stationarynavigationtwo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.flags.IFlagProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ProductImageUpload extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProductImageUpload.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST= 71;

    Button mBtnUpload;
    Button mBtnSelect;
    ImageView mImage;
    //private Uri filePath;
    private Uri uri;
    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image_upload);
        mBtnUpload = findViewById(R.id.btnUpload);
        mBtnSelect = findViewById(R.id.btnSelect);
        mImage = findViewById(R.id.image);

        mBtnSelect.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);

        mRef = FirebaseStorage.getInstance().getReference();


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
            default:
                Log.i(TAG, "no click button found");
        }
    }

    private void uploadImage() {
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
