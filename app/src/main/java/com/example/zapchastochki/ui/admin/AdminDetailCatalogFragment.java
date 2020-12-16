package com.example.zapchastochki.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.R;
import com.example.zapchastochki.adapters.AdminDetailRecyclerAdapter;

import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.DetailDescr;
import com.example.zapchastochki.utils.Sync;
import com.example.zapchastochki.utils.SyncOnRegistration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdminDetailCatalogFragment extends Fragment implements AdminDetailRecyclerAdapter.OnDetailListener{

    private RecyclerView recyclerView;
    private AdminDetailRecyclerAdapter adapter;
    FloatingActionButton fab;
    private ArrayList<DetailDescr> details;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_detail_catalog, container, false);
        //fab
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Fragment newFragment = new AddDetailFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.adm_nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        });

        dbHelper = new DBHelper(this.getContext());
        //recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        details = dbHelper.getAllDetails();
        adapter = new AdminDetailRecyclerAdapter(this.getContext(), details, this);
        recyclerView.setAdapter(adapter);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onDetailClick(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.popup_menu_on_list);
        popupMenu.setOnMenuItemClickListener((menu) -> {
            switch (menu.getItemId()) {
                case R.id.action_delete: {
                    try{
                        int res = dbHelper.deleteDetail(details.get(position).getId());
                        details.remove(position);
                        Toast.makeText(getContext(), "deleted" + res, Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                case R.id.action_edit:{
                    Fragment newFragment = new EditDetailFragment();
                    //put object
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DetailDescr.class.getCanonicalName(), details.get(position));
                    newFragment.setArguments(bundle);
                    //transaction
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.adm_nav_host_fragment, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
                default:
                    return false;
            }
        });
        popupMenu.show();
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.add(1, 1, 1, "Sync");
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Toast.makeText(getContext(), "Синхронизация", Toast.LENGTH_SHORT).show();
                Intent intentService = new Intent(getContext(), Sync.class);
                getContext().startService(intentService);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
