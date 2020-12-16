package com.example.zapchastochki.ui.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.R;
import com.example.zapchastochki.adapters.AdminDetailRecyclerAdapter;
import com.example.zapchastochki.adapters.OrderedDetailRecyclerAdapter;
import com.example.zapchastochki.database.DBHelper;
import com.example.zapchastochki.models.DetailStatus;
import com.example.zapchastochki.models.Order;
import com.example.zapchastochki.models.OrderedDetail;
import com.example.zapchastochki.utils.Utils;

import java.util.ArrayList;


/* Отображение списка деталей в заказе на вкладке админа
* Admin > Orders > click any order to see included details */
public class AdminOrdersDetailsFragment extends Fragment implements OrderedDetailRecyclerAdapter.OnDetailListener {
    DBHelper dbHelper;
    //rv
    private RecyclerView recyclerView;
    private OrderedDetailRecyclerAdapter adapter;
    //another
    private ArrayList<OrderedDetail> details;
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
        details = dbHelper.getOrderedDetailsInOrder(order.getId());
        adapter = new OrderedDetailRecyclerAdapter(getContext(), details, this);
        recyclerView.setAdapter(adapter);


        return view;
    }
    @Override
    public void onDetailClick(View view, int position) {
        showActionsDialog(view, position);
    }

    private void showActionsDialog(View view,  final int position) {
        final String[] statusArray = Utils.getNames(DetailStatus.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose detail status");
        builder.setItems(statusArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.updateDetailStatus(details.get(position), statusArray[which]); //do it
                details.get(position).setDetailStatus(DetailStatus.valueOf(statusArray[which]));
                adapter.notifyItemChanged(position);
            }
        });
        builder.show();
    }
}
