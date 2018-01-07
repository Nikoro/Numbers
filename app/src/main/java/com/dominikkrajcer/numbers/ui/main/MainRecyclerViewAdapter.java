package com.dominikkrajcer.numbers.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dominikkrajcer.numbers.R;
import com.dominikkrajcer.numbers.data.Number;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Number> numberList = new ArrayList<>();
    private int selectedItem = 0;
    private OnItemClickListener onItemClickListener;


    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setNumberList(List<Number> numberList) {
        this.numberList = numberList;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Number number = numberList.get(position);
        ((NumberViewHolder) holder).numberTV.setText(number.getName());
        ((NumberViewHolder) holder).numberIV.setImageBitmap(null);
        ((NumberViewHolder) holder).itemView.setSelected(selectedItem == position);
        ((NumberViewHolder) holder).itemView.setTag(number);

        Picasso.with(((NumberViewHolder) holder).numberIV.getContext()).load(number.getImageUrl()).into(((NumberViewHolder) holder).numberIV);

    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }


    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView numberIV;
        TextView numberTV;

        public NumberViewHolder(View itemView) {
            super(itemView);
            numberIV = itemView.findViewById(R.id.numberIV);
            numberTV = itemView.findViewById(R.id.numberTV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            notifyItemChanged(selectedItem);
            selectedItem = getAdapterPosition();
            notifyItemChanged(selectedItem);
            onItemClickListener.onItemClick((Number) view.getTag());
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Number number);

    }
}
