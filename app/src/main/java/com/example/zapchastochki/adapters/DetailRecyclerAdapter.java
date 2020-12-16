package com.example.zapchastochki.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zapchastochki.R;
import com.example.zapchastochki.models.DetailDescr;


import java.util.ArrayList;
import java.util.List;

public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerAdapter.ViewHolder> implements Filterable {

    private ArrayList<DetailDescr> details = new ArrayList<>();
    private OnDetailListener mOnDetailListener;
    Context context;

    public DetailRecyclerAdapter(Context context, ArrayList<DetailDescr> details, OnDetailListener onDetailListener){
        this.context = context;
        this.details=details;
        this.mOnDetailListener=onDetailListener;
        this.detailsFull = new ArrayList<>(details);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.catalog_item, parent, false);
        return new ViewHolder(view, mOnDetailListener);
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
        OnDetailListener onDetailListener;

        public ViewHolder(@NonNull View itemView, OnDetailListener onDetailListener) {
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
            onDetailListener.onDetailClick(getAdapterPosition());
        }
    }

    public interface OnDetailListener{
        void onDetailClick(int position);
    }


    //search
    private ArrayList<DetailDescr> detailsFull;
    private Filter detailsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DetailDescr> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(detailsFull);
            } else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (DetailDescr item : detailsFull){
                    if (item.getName().toLowerCase().contains(filterPattern)){
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
            details.clear();
            details.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return detailsFilter;
    }
}
