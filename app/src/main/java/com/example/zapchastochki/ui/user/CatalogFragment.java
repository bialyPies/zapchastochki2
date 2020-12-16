package com.example.zapchastochki.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.AdminActivity;
import com.example.zapchastochki.R;
import com.example.zapchastochki.adapters.DetailRecyclerAdapter;
import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.Customer;
import com.example.zapchastochki.models.DetailDescr;
import com.example.zapchastochki.ui.admin.AddDetailFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CatalogFragment extends Fragment implements DetailRecyclerAdapter.OnDetailListener {

    private RecyclerView recyclerView;
    private DetailRecyclerAdapter adapter;
    private ArrayList<DetailDescr> details;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        dbHelper = new DBHelper(this.getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        details = dbHelper.getAllDetails();
        adapter = new DetailRecyclerAdapter(this.getContext(), details, this);
        recyclerView.setAdapter(adapter);

        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onDetailClick(int position) {

        if (dbHelper.isExistInBasket(Customer.CURRENT_USER.getId(), details.get(position).getId())){
            Toast.makeText(getContext(), "detail is already in the basket", Toast.LENGTH_LONG).show();
            return;
        }
        long res = dbHelper.addToBasket(Customer.CURRENT_USER.getId(), details.get(position).getId());
        if (res == 0){
            Toast.makeText(getContext(), "smth is wrong", Toast.LENGTH_LONG).show();
        } else Toast.makeText(getContext(), "detail is added to basket", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_catalog_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
