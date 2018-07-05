package com.example.senamit.stationarynavigationtwo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductForSale extends AppCompatActivity {

    private static final String TAG = ProductForSale.class.getSimpleName();

   private DatabaseReference mFirebaseDatabaseReference;

   TextView txtUserName;
   TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_for_sale);

        txtUserName = findViewById(R.id.txtUserName);
        txtEmail= findViewById(R.id.txtEmail);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("love u ").setValue("love u bro");

        Log.i(TAG, "reference is "+mFirebaseDatabaseReference);
        DatabaseReference mDatabase = mFirebaseDatabaseReference.child("Pencil");
        mDatabase.child("ThirdUser").setValue("HaHa");


        User users1 = new User("amit sen", "senamit7564@gmail.com");
        Log.i(TAG, "the username is "+users1.getUsername().toString());
        Log.i(TAG, "the email is "+users1.getEmailId());
        mFirebaseDatabaseReference.child("test").push().setValue(users1);
        User users2 = new User("user2", "user2.com");
        mFirebaseDatabaseReference.child("test").push().setValue(users2);

        mFirebaseDatabaseReference.child("pencil").child("ThirdUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                txtUserName.setText(value);
                Log.d(TAG, "data loaded successfully, value is "+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.w(TAG, "failed to load the data");
            }
        });


    }
}
