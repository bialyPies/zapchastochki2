package com.example.zapchastochki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;

//import android.hardware.biometrics.BiometricManager;
//import android.hardware.fingerprint.FingerprintManager;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyStore;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;

public class FingerprintActivity extends AppCompatActivity {

    Button btnLogin;
    TextView txtMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        FingerprintActivity activity = this;
        btnLogin = findViewById(R.id.btnLogin);
        txtMsg = findViewById(R.id.txt_msg);
        final BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()){

            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(this,"You can use your fingerprint to login",Toast.LENGTH_LONG).show();
                txtMsg.setText("Use Fingerprint to Access");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this,"No fingerprint sensor",Toast.LENGTH_LONG).show();
                txtMsg.setText("No fingerprint sensor");
                btnLogin.setVisibility(View.INVISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this,"Biometric sensor is not available",Toast.LENGTH_LONG).show();
                txtMsg.setText("Biometric sensor is not available");
                btnLogin.setVisibility(View.INVISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this,"Your device don't have any fingerprint, check your security setting",Toast.LENGTH_LONG).show();
                txtMsg.setText("Your device don't have any fingerprint, check your security setting");
                btnLogin.setVisibility(View.INVISIBLE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);


        final BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback(){

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("User fingerprint to login")
                .setNegativeButtonText("cancel")
                .build();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }
}
