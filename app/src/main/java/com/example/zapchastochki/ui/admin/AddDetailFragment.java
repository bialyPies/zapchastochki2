package com.example.zapchastochki.ui.admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import net.sqlcipher.database.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zapchastochki.R;
import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.DetailDescr;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AddDetailFragment extends Fragment {
    private static final int REQUEST_PERMISSION_WRITE = 1001;
    final int REQUEST_CODE_GALLERY = 999;
    FragmentManager fragmentManager;
    TextView tImgPath;
    EditText tname, tprice, tnum, tyear;
    Button bchoose, bSave;
    ImageView imgview;

    public static DBHelper dbHelper;
    SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        checkPermissions();
        View view = inflater.inflate(R.layout.fragment_admin_add_detail, container, false);
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        fragmentManager = getActivity().getSupportFragmentManager();

        tImgPath = view.findViewById(R.id.t_img_path);
        tname = view.findViewById(R.id.et_name);
        tprice = view.findViewById(R.id.et_price);
        tnum = view.findViewById(R.id.et_num);
        tyear = view.findViewById(R.id.et_year);
        bchoose = view.findViewById(R.id.bChoose);
        bSave = view.findViewById(R.id.bSave);
        imgview = view.findViewById(R.id.img_add_view);

        bchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick_save(view);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_.png";
                String path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath();
                File dir = new File(path, "zapchastochki");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, imageFileName);

                //checking if we can write to external memory
                String externalStorageState = Environment.getExternalStorageState();
                if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {

                    try (FileOutputStream out = new FileOutputStream(file)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (IOException e) {
                        Log.d("CreateNew", e.getMessage());
                    }

                    tImgPath.setText(file.getAbsolutePath());
                    imgview.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //permissions
    private boolean checkPermissions() {
        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Toast.makeText(this.getContext(), "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    public void onClick_save(View view) {
        //get values from ui
        String sName = tname.getText().toString();
        String sPrice = tprice.getText().toString();
        String sYear = tyear.getText().toString();
        String sNum = tnum.getText().toString();
        String sPath = tImgPath.getText().toString();

        //validation
        if (sName.isEmpty() || sPrice.isEmpty() || sYear.isEmpty() || sNum.isEmpty() || sPath.isEmpty()){
            Toast.makeText(getContext(), "Fill in the fields", Toast.LENGTH_LONG).show();
            return;
        }
        //check year
        int year = Integer.valueOf(sYear);
        //Year year = Year.parse(sYear);
        if(year < 1800 || year > 2020){
            tyear.setHint("Enter correct year");
            Toast.makeText(getContext(), "incorrect year", Toast.LENGTH_LONG).show();
            return;
        }

        //create Detail
        DetailDescr sDetail = new DetailDescr();
        sDetail.setName(sName);
        sDetail.setPath(sPath);
        sDetail.setReleaseYear(year);
        sDetail.setPrice(Double.valueOf(sPrice));
        sDetail.setNum(Integer.valueOf(sNum));
        //insert detail
        long id = dbHelper.insertDetail(sDetail);

        //open catalog
        Fragment newFragment = new AdminDetailCatalogFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.adm_nav_host_fragment, newFragment);
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.commit();
    }

}
