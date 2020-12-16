package com.example.zapchastochki.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.R;
import com.example.zapchastochki.models.OrderedDetail;

import java.util.ArrayList;

public class OrderedDetailRecyclerAdapter extends RecyclerView.Adapter<OrderedDetailRecyclerAdapter.ViewHolder> {
    private ArrayList<OrderedDetail> details = new ArrayList<>();
    private OrderedDetailRecyclerAdapter.OnDetailListener mOnDetailListener;
    Context context;

    public OrderedDetailRecyclerAdapter(Context context, ArrayList<OrderedDetail> details, OrderedDetailRecyclerAdapter.OnDetailListener onDetailListener){
        this.context = context;
        this.details = details;
        this.mOnDetailListener = onDetailListener;
    }

    @NonNull
    @Override
    public OrderedDetailRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ordered_detail, parent, false);
        return new OrderedDetailRecyclerAdapter.ViewHolder(view, mOnDetailListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedDetailRecyclerAdapter.ViewHolder holder, int position) {
        OrderedDetail detail = details.get(position);
        holder.setDetail(detail);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tName, tPrice, tYear, tStatus;
        private ImageView tImg;
        OrderedDetailRecyclerAdapter.OnDetailListener onDetailListener;

        public ViewHolder(@NonNull View itemView, OrderedDetailRecyclerAdapter.OnDetailListener onDetailListener) {
            super(itemView);
            tName = itemView.findViewById(R.id.ordered_detail_name);
            tPrice = itemView.findViewById(R.id.ordered_detail_price);
            tYear = itemView.findViewById(R.id.ordered_detail_year);
            tStatus = itemView.findViewById(R.id.ordered_detail_status);
            tImg = itemView.findViewById(R.id.ordered_detail_img);

            this.onDetailListener = onDetailListener;
            itemView.setOnClickListener(this);
        }

        public void setDetail(OrderedDetail detail){
            tName.setText(detail.getName());
            tPrice.setText(String.valueOf(detail.getPrice()));
            tYear.setText(String.valueOf(detail.getReleaseYear()));
            tStatus.setText(String.valueOf(detail.getDetailStatus()));
            Bitmap bitmap = BitmapFactory.decodeFile(detail.getPath());
            tImg.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            onDetailListener.onDetailClick(v, getAdapterPosition());
        }
    }

    public interface OnDetailListener{
        void onDetailClick(View view, int position);
    }
}
