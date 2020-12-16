package com.example.zapchastochki.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.R;
import com.example.zapchastochki.adapters.AdminDetailRecyclerAdapter;
import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.Customer;
import com.example.zapchastochki.models.DetailDescr;


import java.util.ArrayList;

public class BasketFragment extends Fragment implements AdminDetailRecyclerAdapter.OnDetailListener {

    private TextView tTotalSum;
    private Button bOrder;
    private RecyclerView recyclerView;
    private AdminDetailRecyclerAdapter adapter;
    private ArrayList<DetailDescr> details;
    private ArrayList<DetailDescr> detailsInOrder = new ArrayList<>();
    private double totalSum = 0;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        dbHelper = new DBHelper(this.getContext());
        //get ui
        recyclerView = view.findViewById(R.id.rv_basket);
        tTotalSum = view.findViewById(R.id.basket_total_price);
        bOrder = view.findViewById(R.id.b_basket_order);

        //set ui
        tTotalSum.setText("0");
        bOrder.setOnClickListener(onOrderSaveClickListener);

        //recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        details = dbHelper.getBasketById(Customer.CURRENT_USER.getId());
        adapter = new AdminDetailRecyclerAdapter(getContext(), details, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDetailClick(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.popup_menu_on_basket);
        popupMenu.setOnMenuItemClickListener((menu) -> {
            switch (menu.getItemId()) {
                case R.id.action_add_to_order: {
                    detailsInOrder.add(details.get(position));
                    totalSum += details.get(position).getPrice();
                    tTotalSum.setText(String.valueOf(totalSum));
                    view.setBackgroundResource(R.color.selected);
                    return true;
                }
                case R.id.action_delete_from_basket:{
                    dbHelper.deleteFromBasket(details.get(position).getId());
                    details.remove(position);
                    adapter.notifyItemRemoved(position);
                    return true;
                }
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    final View.OnClickListener onOrderSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment newFragment = new ShowOrderDetailsFragment();
            //put object
            Bundle bundle = new Bundle();
            bundle.putSerializable(ArrayList.class.getCanonicalName() + DetailDescr.class.getCanonicalName(), detailsInOrder);
            newFragment.setArguments(bundle);
            //transaction
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };
}
