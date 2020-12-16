package com.example.zapchastochki.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zapchastochki.AdminActivity;
import com.example.zapchastochki.R;
import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.Customer;
import com.example.zapchastochki.utils.Sync;


public class ProfileFragment extends Fragment {
    Button bAdmin, bSave;
    Button bSync;
    EditText etName, etMobile;
    TextView tId, etUname, etPassword;
    DBHelper dbHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        dbHelper = new DBHelper(this.getContext());
        //ui
        bAdmin = view.findViewById(R.id.toAdmin);
        bSave = view.findViewById(R.id.b_profile_save_changes);
        tId = view.findViewById(R.id.et_profile_id);
        etUname = view.findViewById(R.id.et_profile_username);
        etPassword = view.findViewById(R.id.et_profile_password);
        etName = view.findViewById(R.id.et_profile_name);
        etMobile = view.findViewById(R.id.et_profile_mibile);
        //set ui
        tId.setText(String.valueOf(Customer.CURRENT_USER.getId()));
        etUname.setText(Customer.CURRENT_USER.getUsername());
        etPassword.setText(Customer.CURRENT_USER.getPassword());
        etName.setText(Customer.CURRENT_USER.getName());
        etMobile.setText(Customer.CURRENT_USER.getMobile());

        bSync = view.findViewById(R.id.b_sync);

        bAdmin.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), AdminActivity.class)));

        bSave.setOnClickListener(view1 ->{
            Customer.CURRENT_USER.setName(etName.getText().toString());
            Customer.CURRENT_USER.setMobile(etMobile.getText().toString());
            dbHelper.updateProfile();

        });

        bSync.setOnClickListener(view1 ->{
            Toast.makeText(getContext(), "Синхронизация", Toast.LENGTH_SHORT).show();
            Intent intentService = new Intent(getContext(), Sync.class);
            getContext()
                    .startService(intentService);

        });

        return view;
    }
}
