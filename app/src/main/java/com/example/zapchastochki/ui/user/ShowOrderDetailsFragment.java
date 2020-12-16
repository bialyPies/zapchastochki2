package com.example.zapchastochki.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import com.example.zapchastochki.models.Order;

import java.util.ArrayList;

public class ShowOrderDetailsFragment extends Fragment implements AdminDetailRecyclerAdapter.OnDetailListener {
    DBHelper dbHelper;
    //get ui
    private Button bOrder;
    private TextView tTotalSum;
    //rv
    private RecyclerView recyclerView;
    private AdminDetailRecyclerAdapter adapter;
    //another
    private ArrayList<DetailDescr> details;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_order_details, container, false);
        //bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            details = (ArrayList<DetailDescr>) bundle.getSerializable(ArrayList.class.getCanonicalName() + DetailDescr.class.getCanonicalName());
        }

        dbHelper = new DBHelper(this.getContext());
        //get ui
        recyclerView = view.findViewById(R.id.rv_order_details);
        bOrder = view.findViewById(R.id.b_order);
        tTotalSum = view.findViewById(R.id.order_total_price);

        //set ui
        bOrder.setOnClickListener(onOrderClickListener);
        tTotalSum.setText(String.valueOf(calculateTotalSum()));

        //recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new AdminDetailRecyclerAdapter(getContext(), details, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDetailClick(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenu().add(1, 1, 1, "delete from order");

        popupMenu.setOnMenuItemClickListener((menu) -> {
            switch (menu.getItemId()) {
                 case 1:{
                     details.remove(position);
                     adapter.notifyItemRemoved(position);
                     tTotalSum.setText(String.valueOf(calculateTotalSum()));
                     return true;
                }
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    final View.OnClickListener onOrderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            long idOrder = dbHelper.createOrder(calculateTotalSum(), details.size());
            if (idOrder <= 0) try {
                throw new Exception("ploha");
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            for (DetailDescr detail : details){
                dbHelper.addToOrder(detail.getId(), idOrder);
            }
            Toast.makeText(getContext(), "order is commited (" + details.size() + ")", Toast.LENGTH_LONG).show();
        }
    };

    private double calculateTotalSum(){
        double totalSum = 0;
        for (DetailDescr detail : details){
            totalSum += detail.getPrice();
        }
        return totalSum;
    }
}
