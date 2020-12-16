package com.example.zapchastochki.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.R;
import com.example.zapchastochki.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> implements Filterable {
    private ArrayList<Order> orders = new ArrayList<>();
    private OrderRecyclerAdapter.OnOrderClickListener mOnOrderClickListener;
    Context context;

    public OrderRecyclerAdapter(Context context, ArrayList<Order> orders, OrderRecyclerAdapter.OnOrderClickListener mOnOrderClickListener){
        this.context = context;
        this.orders = orders;
        this.mOnOrderClickListener = mOnOrderClickListener;
        this.ordersFull = new ArrayList<>(orders);
    }

    @NonNull
    @Override
    public OrderRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderRecyclerAdapter.ViewHolder(view, mOnOrderClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.setDetail(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tIdOrder, tIdCustomer, tName, tDate, tComplitionDate, tTotalPrice, tAmount, tStatus;

        OrderRecyclerAdapter.OnOrderClickListener onOrderClickListener;

        public ViewHolder(@NonNull View itemView, OrderRecyclerAdapter.OnOrderClickListener onOrderClickListener) {
            super(itemView);
            tIdOrder = itemView.findViewById(R.id.t_id);
            tIdCustomer = itemView.findViewById(R.id.t_idCustomer);
            tName = itemView.findViewById(R.id.t_name);
            tDate = itemView.findViewById(R.id.t_date);
            tComplitionDate = itemView.findViewById(R.id.t_complitionDate);
            tTotalPrice = itemView.findViewById(R.id.t_total_price);
            tAmount = itemView.findViewById(R.id.t_amount);
            tStatus = itemView.findViewById(R.id.t_status);

            this.onOrderClickListener = onOrderClickListener;
            itemView.setOnClickListener(this);
        }

        public void setDetail(Order order){
            tIdOrder.setText(String.valueOf(order.getId()));
            tIdCustomer.setText(String.valueOf(order.getIdCustomer()));
            tName.setText(String.valueOf(order.getCustomer().getName()));
            tDate.setText(String.valueOf(order.getOrderDate()));
            tComplitionDate.setText(String.valueOf(order.getCompletionDate()));
            tTotalPrice.setText(String.valueOf(order.getTotalCost()));
            tAmount.setText(String.valueOf(order.getTotalNumber()));
            tStatus.setText(String.valueOf(order.getOrderStatus()));
        }

        @Override
        public void onClick(View v) {
            onOrderClickListener.onOrderClick(v, getAdapterPosition());
        }
    }

    public interface OnOrderClickListener{
        void onOrderClick(View view, int position);
    }

    //Search
    private ArrayList<Order> ordersFull;
    private Filter detailsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Order> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(ordersFull);
            } else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Order item : ordersFull){
                    if (item.getCustomer().getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            orders.clear();
            orders.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return detailsFilter;
    }
}
