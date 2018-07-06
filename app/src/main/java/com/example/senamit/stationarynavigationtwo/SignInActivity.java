package com.example.senamit.stationarynavigationtwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.AuthProvider;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = SignInActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private DatabaseReference mDatabase;

    private EditText mPhoneNumberField;
    private EditText mPhoneOtp;
    private Button btnPhoneNumber;
    private Button btnSubmit;
    private Button btnResendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mPhoneNumberField = findViewById(R.id.edt_phone_number);
        mPhoneOtp = findViewById(R.id.edt_phone_otp);
        btnPhoneNumber = findViewById(R.id.btn_phone_number_enter);
        btnSubmit = findViewById(R.id.btn_sign_in);
        btnResendOtp = findViewById(R.id.btn_resend_otp);

        btnPhoneNumber.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnResendOtp.setOnClickListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.i(TAG, "inside onverification completed method");
//                mVerificationInProgress = false;
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }



            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.i(TAG, "inside onverificationfailed method");
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    mPhoneNumberField.setError("Invalid phone number");
                    Log.i(TAG, "inside the exception one of verification failed");
                }else if (e instanceof FirebaseTooManyRequestsException){
                    Log.i(TAG, "inside the exception two of verification failed");

                    Toast.makeText(SignInActivity.this, "Please try another way of login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG, "the code sent is "+verificationId);
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;

            }
        };

    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mFirebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "login credential successful");
                            FirebaseUser user = task.getResult().getUser();
                            Log.i(TAG, "the user is "+user);
                            onAuthSuccess(user);
                        }
                       else {
                            Log.i(TAG, "login failed inside signInwithphoneauthcredential");
                        }

                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String phoneNumber = mPhoneNumberField.getText().toString();
        writeNewUser(user.getUid(), phoneNumber);
        startActivity(new Intent(SignInActivity.this, MainActivityFirst.class));

    }

    private void writeNewUser(String userId, String phoneNumber) {
                User user = new User(phoneNumber);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        Log.i(TAG, "inside start phone number verification method");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback);
        mVerificationInProgress = true;

    }

    private boolean validatePhoneNumber( ) {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)){
            mPhoneNumberField.setError("invalid mobile number");
            Log.i(TAG, "inside validate phone number");
            Toast.makeText(SignInActivity.this, "invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null){
            Log.i(TAG, "user already avaibale");
        }
        Log.i(TAG, "inside the onstart method");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_phone_number_enter:
                if(!validatePhoneNumber()){
                    Log.i(TAG, "inside the if statement");
                    return;
                }
                Log.i(TAG, "inside the else statement ");
                String phoneNumber = mPhoneNumberField.getText().toString();
                Log.d(TAG, "phone number is "+phoneNumber);
                startPhoneNumberVerification(phoneNumber);
                break;
            case R.id.btn_sign_in:
                String code = mPhoneOtp.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mPhoneOtp.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.btn_resend_otp:
                Log.i(TAG, "inside resend otp method");
                break;
            default:
                Log.i(TAG, "no button found of such type");

        }

    }
}
