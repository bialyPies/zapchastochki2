package com.example.zapchastochki.ui.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.R;
import com.example.zapchastochki.adapters.OrderRecyclerAdapter;
import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.DetailStatus;
import com.example.zapchastochki.models.Order;
import com.example.zapchastochki.models.OrderStatus;
import com.example.zapchastochki.models.OrderedDetail;
import com.example.zapchastochki.ui.user.OrdersDetailsFragment;
import com.example.zapchastochki.utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;

public class AdminOrdersFragment extends Fragment implements OrderRecyclerAdapter.OnOrderClickListener{
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
        orders = dbHelper.getOrders();
        adapter = new OrderRecyclerAdapter(getContext(), orders, this);
        recyclerView.setAdapter(adapter);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onOrderClick(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenu().add(1, 1, 1, "show details");
        popupMenu.getMenu().add(1, 2, 2, "change order status");

        popupMenu.setOnMenuItemClickListener((menu) -> {
            switch (menu.getItemId()) {
                case 1:{
                    Fragment newFragment = new AdminOrdersDetailsFragment();
                    //put object
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Order.class.getCanonicalName(), orders.get(position));
                    newFragment.setArguments(bundle);
                    //transaction
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.adm_nav_host_fragment, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
                case 2:{
                    showActionsDialog(view, position);
                    return true;
                }
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void showActionsDialog(View view,  final int position) {
        final String[] statusArray = Utils.getNames(OrderStatus.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose order status");
        builder.setItems(statusArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.updateOrderStatus(orders.get(position), statusArray[which]);
                orders.get(position).setOrderStatus(OrderStatus.valueOf(statusArray[which]));

                //if completed 1 or canceled 3
                if(statusArray[which] == OrderStatus.COMPLETED.toString() ||
                        statusArray[which] == OrderStatus.CANCELED.toString()){
                    LocalDate now = LocalDate.now();
                    dbHelper.updateOrderCompletionDate(orders.get(position).getId(), now);
                    orders.get(position).setCompletionDate(String.valueOf(now));
                }

                adapter.notifyItemChanged(position);
            }
        });
        builder.show();
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
