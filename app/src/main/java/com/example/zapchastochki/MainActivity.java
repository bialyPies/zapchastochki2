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

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        //database
        db = new DBHelper(this).getWritableDatabase();

        //bottom navigation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.bringToFront();
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        //for fragment's navigation
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //for activity's navigation
        /*bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            switch (item.getItemId()) {

                case R.id.page_2:
                    startActivity(new Intent(getApplicationContext(), BasketActivity.class));
                    return true;
                case R.id.page_3:
                    startActivity(new Intent(getApplicationContext(), CatalogActivity.class));
                    return true;
                case R.id.page_4:
                    //startActivity(new Intent(getApplicationContext(), otherActivity.class));
                    return true;
            }
            return false;
        });
        bottomNavigationView.setOnNavigationItemReselectedListener(item ->{
           switch (item.getItemId()) {
                case R.id.page_2:
                case R.id.page_3:
                case R.id.page_4:
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        bottomNavigationView.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.nav_host_fragment, new FingerprintFragment())
                .commit();
    }
}