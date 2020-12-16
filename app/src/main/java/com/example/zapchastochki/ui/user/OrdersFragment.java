package com.example.zapchastochki.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.R;
import com.example.zapchastochki.adapters.AdminDetailRecyclerAdapter;
import com.example.zapchastochki.adapters.OrderRecyclerAdapter;
import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.Customer;
import com.example.zapchastochki.models.Order;
import com.example.zapchastochki.utils.Sync;
import com.example.zapchastochki.utils.SyncStatuses;

import java.util.ArrayList;

public class OrdersFragment extends Fragment implements OrderRecyclerAdapter.OnOrderClickListener {
    private RecyclerView recyclerView;
    private OrderRecyclerAdapter adapter;
    private ArrayList<Order> orders;
    DBHelper dbHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        dbHelper = new DBHelper(this.getContext());

        recyclerView = view.findViewById(R.id.rv_orders);

        //recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        orders = dbHelper.getOrdersById(Customer.CURRENT_USER.getId());
        adapter = new OrderRecyclerAdapter(getContext(), orders, this);
        recyclerView.setAdapter(adapter);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onOrderClick(View view, int position) {
        Fragment newFragment = new OrdersDetailsFragment();
        //put object
        Bundle bundle = new Bundle();
        bundle.putSerializable(Order.class.getCanonicalName(), orders.get(position));
        newFragment.setArguments(bundle);
        //transaction
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
                Intent intentService = new Intent(getContext(), SyncStatuses.class);
                getContext().startService(intentService);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}