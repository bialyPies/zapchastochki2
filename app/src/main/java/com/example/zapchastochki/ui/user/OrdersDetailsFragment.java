package com.example.zapchastochki.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.R;
import com.example.zapchastochki.adapters.AdminDetailRecyclerAdapter;
import com.example.zapchastochki.adapters.OrderRecyclerAdapter;
import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.DetailDescr;
import com.example.zapchastochki.models.Order;

import java.util.ArrayList;

public class OrdersDetailsFragment extends Fragment implements AdminDetailRecyclerAdapter.OnDetailListener {
    DBHelper dbHelper;
    //rv
    private RecyclerView recyclerView;
    private AdminDetailRecyclerAdapter adapter;
    //another
    private ArrayList<DetailDescr> details;
    private Order order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        dbHelper = new DBHelper(this.getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            order = (Order) bundle.getSerializable(Order.class.getCanonicalName());
        }
        recyclerView = view.findViewById(R.id.rv_orders);
        //recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        details = dbHelper.getDetailsInOrder(order.getId());
        adapter = new AdminDetailRecyclerAdapter(getContext(), details, this);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onDetailClick(View view, int position) {

    }
}
