package com.example.zapchastochki.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.zapchastochki.FingerprintActivity;
import com.example.zapchastochki.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.Executor;

public class FingerprintFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    View view;
    FragmentManager fm;
    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fingerprint, container, false);
        Button btnLogin;
        TextView txtMsg;
        fm = getActivity().getSupportFragmentManager();
        fragment = this;

        Context ctx = getContext();
        btnLogin = view.findViewById(R.id.btnLogin);
        txtMsg = view.findViewById(R.id.txt_msg);
        final BiometricManager biometricManager = BiometricManager.from(ctx);

        switch (biometricManager.canAuthenticate()){

            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(ctx,"You can use your fingerprint to login",Toast.LENGTH_LONG).show();
                txtMsg.setText("Use Fingerprint to Access");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(ctx,"No fingerprint sensor",Toast.LENGTH_LONG).show();
                txtMsg.setText("No fingerprint sensor");
                btnLogin.setVisibility(View.INVISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(ctx,"Biometric sensor is not available",Toast.LENGTH_LONG).show();
                txtMsg.setText("Biometric sensor is not available");
                btnLogin.setVisibility(View.INVISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(ctx,"Your device don't have any fingerprint, check your security setting",Toast.LENGTH_LONG).show();
                txtMsg.setText("Your device don't have any fingerprint, check your security setting");
                btnLogin.setVisibility(View.INVISIBLE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(ctx);


        final BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) ctx, executor, new BiometricPrompt.AuthenticationCallback(){

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(ctx,"Login Success",Toast.LENGTH_LONG).show();

                fm.beginTransaction().remove(fragment).commit();

                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setVisibility(View.VISIBLE);
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

        return view;
    }
}
