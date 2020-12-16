package com.example.zapchastochki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import net.sqlcipher.database.SQLiteDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.ui.FingerprintFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;


public class AdminActivity extends AppCompatActivity {
    SQLiteDatabase db;
    BottomNavigationView bottomNavigationView;
    //добавить создание описания детали чтобы проверять дальше с картинкой

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        //screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        //database
        db = new DBHelper(this).getWritableDatabase();

        //bottom navigation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.bringToFront();
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        //for fragment's navigation
        NavController navController = Navigation.findNavController(this, R.id.adm_nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bottomNavigationView.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.adm_nav_host_fragment, new FingerprintFragment())
                .commit();
    }

}
