package com.example.friendfinderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.friendfinderapp.Constants.ConfigurationAll;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceListViewHolder> {

    List<PlaceList> placeListList;
    onPlaceListListener onPlaceListListener;

    public PlaceListAdapter(List<PlaceList> placeListList, onPlaceListListener onPlaceListListener) {
        this.placeListList = placeListList;
        this.onPlaceListListener = onPlaceListListener;
    }

    @NonNull
    @Override
    public PlaceListAdapter.PlaceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.place_list_item, parent, false);
        return new PlaceListViewHolder(view, onPlaceListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceListAdapter.PlaceListViewHolder holder, int position) {
        holder.txt_place_name.setText(placeListList.get(position).getPlace_name());
        holder.txt_place_price.setText("IDR " + placeListList.get(position).getPrice());

        String[] start = placeListList.get(position).getPlace_open_time().split(":");
        String open = start[0] + ":" + start[1];

        String[] finish = placeListList.get(position).getPlace_close_time().split(":");
        String close = finish[0] + ":" + finish[1];

        holder.txt_place_time.setText(open + " - " + close);

        Glide.with(holder.itemView).load(ConfigurationAll.ImageURL + placeListList.get(position).getPlace_picture())
                .into(holder.img_place_image);
    }

    @Override
    public int getItemCount() {
        return placeListList.size();
    }

    public static class PlaceListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txt_place_name, txt_place_price, txt_place_time;
        private ImageView img_place_image;

        onPlaceListListener onPlaceListListener;

        public PlaceListViewHolder(@NonNull View itemView, onPlaceListListener onPlaceListListener) {
            super(itemView);

            txt_place_name = itemView.findViewById(R.id.txt_place_name);
            txt_place_price = itemView.findViewById(R.id.txt_place_price);
            txt_place_time = itemView.findViewById(R.id.txt_place_time);
            img_place_image = itemView.findViewById(R.id.img_place_image);

            this.onPlaceListListener = onPlaceListListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPlaceListListener.onPlaceListClick(getAdapterPosition());
        }
    }
    public interface onPlaceListListener {
        void onPlaceListClick(int position);
    }
}
