package com.example.senamit.stationarynavigationtwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

public class MainActivityFirst extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivityFirst.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private TextView mUserName;
    private Button mBtnLogOut;
    private  Button mBtnProductForSale;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);

        mUserName = findViewById(R.id.txtUserName);
        mBtnLogOut = findViewById(R.id.btn_logOut);
        mBtnProductForSale = findViewById(R.id.btn_productForSale);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mBtnLogOut.setOnClickListener(this);
        mBtnProductForSale.setOnClickListener(this);

        if (mFirebaseUser==null){
            Log.i(TAG, "inside mFirebaseUser==null");
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
            //always try to call return after finish(),
            //https://stackoverflow.com/questions/4924071/calling-finish-on-an-android-activity-doesnt-actually-finish
        }
        if (mFirebaseUser!= null){
            Log.i(TAG, "inside mFirebaseUser !=null");
            mUserName.setText(mFirebaseUser.getDisplayName());
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_logOut:
                mFirebaseAuth.signOut();
                startActivity(new Intent(MainActivityFirst.this, SignInActivity.class));
                break;
            case R.id.btn_productForSale:
                startActivity(new Intent(MainActivityFirst.this, ProductForSale.class));
                Log.i(TAG, "btn product for sale is clicked");
                break;
            default:
                Log.i(TAG, "no click button found");

        }
    }
}
