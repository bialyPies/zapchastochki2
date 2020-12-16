package com.example.zapchastochki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.Customer;
import com.example.zapchastochki.utils.Internet;
import com.example.zapchastochki.utils.Sync;
import com.example.zapchastochki.utils.SyncOnRegistration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_CANCELED;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT_PERMANENT;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_TIMEOUT;

public class LoginActivity extends AppCompatActivity {

    EditText elogin, epassword;
    Button bLogin, bRegister;
    DBHelper dbHelper;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);
        ctx = this;
        //dbHelper.deleteItAfterPlease();

        //screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        //ui
        elogin = findViewById(R.id.et_login);
        epassword = findViewById(R.id.et_password);
        bLogin = findViewById(R.id.b_login);
        bRegister = findViewById(R.id.b_register);



        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String login = elogin.getText().toString();
                String password = epassword.getText().toString();

                if (checkInput(login, password) && dbHelper.customerIsExist(login,password)){
                    dbHelper.selectCustomer(login, password);
                    if (Customer.CURRENT_USER.getIsAmin()){
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } else if(!dbHelper.customerIsExist(login,password)){
                    Toast.makeText(getBaseContext(), "User is not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String login = elogin.getText().toString();
                String password = epassword.getText().toString();

                if (checkInput(login, password) && !dbHelper.customerIsExist(login,password)){

                    Customer.CURRENT_USER.setUsername(login);
                    Customer.CURRENT_USER.setPassword(password);
                    Customer.CURRENT_USER.setMobile("0");
                    Customer.CURRENT_USER.setName("no name");

                    Toast.makeText(getBaseContext(), "Registration", Toast.LENGTH_SHORT).show();
                    Intent intentService = new Intent(getBaseContext(), SyncOnRegistration.class);
                    startService(intentService);

                    //insert
                    /*Customer.CURRENT_USER.setUsername(login);
                    Customer.CURRENT_USER.setPassword(password);
                    Customer.CURRENT_USER.setMobile("0");
                    Customer.CURRENT_USER.setName("no name");
                    long id = dbHelper.insertCustomer(Customer.CURRENT_USER);
                    if (id != 0) {
                        //enter
                        Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }*/
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        elogin.setText("");
        epassword.setText("");

//        //fingerprint
//        Executor executor = Executors.newSingleThreadExecutor();
//        LoginActivity activity = this;
//
//        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
//                .setTitle("Fingerprint Authentication")
//                .setSubtitle("Subtitle")
//                .setDescription("Place your fingertip on the scanner button to verify your identity")
//                .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finishAndRemoveTask();
//                    }
//                }).build();
//
//
//        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode, CharSequence errString) {
//                //super.onAuthenticationError(errorCode, errString);
//
//                if (errorCode == BIOMETRIC_ERROR_LOCKOUT
//                        || errorCode == BIOMETRIC_ERROR_LOCKOUT_PERMANENT
//                        || errorCode == BIOMETRIC_ERROR_TIMEOUT
//                        || errorCode == BIOMETRIC_ERROR_CANCELED
//                ){
//                    finishAndRemoveTask();
//                }
//
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(LoginActivity.this, "Authenticated", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                super.onAuthenticationFailed();
//            }
//        };
//
//        biometricPrompt.authenticate(new CancellationSignal(), executor, callback);
    }

    public boolean checkInput(String login, String password){
        if(login.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "fill in fields", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
