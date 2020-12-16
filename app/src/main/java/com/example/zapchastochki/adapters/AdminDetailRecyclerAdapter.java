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
import com.example.zapchastochki.models.DetailDescr;


import java.util.ArrayList;

public class AdminDetailRecyclerAdapter extends RecyclerView.Adapter<AdminDetailRecyclerAdapter.ViewHolder>{
    private ArrayList<DetailDescr> details = new ArrayList<>();
    private AdminDetailRecyclerAdapter.OnDetailListener mOnDetailListener;
    Context context;

    public AdminDetailRecyclerAdapter(Context context, ArrayList<DetailDescr> details, AdminDetailRecyclerAdapter.OnDetailListener onDetailListener){
        this.context = context;
        this.details=details;
        this.mOnDetailListener=onDetailListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.catalog_item, parent, false);
        return new AdminDetailRecyclerAdapter.ViewHolder(view, mOnDetailListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailDescr detail = details.get(position);
        holder.setDetail(detail);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tName, tPrice, tYear;
        private ImageView tImg;

        AdminDetailRecyclerAdapter.OnDetailListener onDetailListener;

        public ViewHolder(@NonNull View itemView, AdminDetailRecyclerAdapter.OnDetailListener onDetailListener) {
            super(itemView);
            tName = itemView.findViewById(R.id.catalogitemname);
            tPrice = itemView.findViewById(R.id.catalogitemprice);
            tYear = itemView.findViewById(R.id.catalogitemyear);
            tImg = itemView.findViewById(R.id.imgcatalog);

            this.onDetailListener = onDetailListener;
            itemView.setOnClickListener(this);
        }

        public void setDetail(DetailDescr detail){
            tName.setText(detail.getName());
            tPrice.setText(String.valueOf(detail.getPrice()));
            tYear.setText(String.valueOf(detail.getReleaseYear()));
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
