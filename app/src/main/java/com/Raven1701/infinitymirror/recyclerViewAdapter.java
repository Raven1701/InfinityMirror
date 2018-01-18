package com.Raven1701.infinitymirror;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raven1701.infinitymirror.R;

import java.util.ArrayList;

/**
 * Created by Raven1701 on 18.01.2018.
 */

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {

    private ArrayList<Item> items = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    recyclerViewAdapter(Context context, ArrayList<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.items = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
           if(!items.get(position).brightness)holder.brigthnessIco.setVisibility(View.GONE);
            if(!items.get(position).colorPallete)holder.colorPaleteIco.setVisibility(View.GONE);
            if(!items.get(position).colorPallete2)holder.colorPalete2Ico.setVisibility(View.GONE);
            if(!items.get(position).delay)holder.timeIco.setVisibility(View.GONE);
            holder.textView.setText(items.get(position).tittle);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
      ImageView colorPaleteIco ;
        ImageView colorPalete2Ico;
        ImageView timeIco;
        ImageView brigthnessIco ;
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
             colorPaleteIco = itemView.findViewById(R.id.color);
             colorPalete2Ico = itemView.findViewById(R.id.color2);
             timeIco = itemView.findViewById(R.id.delay);
             brigthnessIco = itemView.findViewById(R.id.brigthness);
             textView = itemView.findViewById(R.id.textViewMode);

            itemView.setOnClickListener(this);}
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    Item getItem(int id) {
        return items.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}