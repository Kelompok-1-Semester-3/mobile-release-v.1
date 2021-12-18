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

import java.util.ArrayList;
import java.util.List;

public class userEventAdapter extends RecyclerView.Adapter<userEventAdapter.userEventViewHolder> {

    private List<userEvent> events = new ArrayList<>();
    onUserEventListener onUserEventListener;

    public userEventAdapter(List<userEvent> events, onUserEventListener onUserEventListener) {
        this.events = events;
        this.onUserEventListener = onUserEventListener;
    }

    @NonNull
    @Override
    public userEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_event_item, parent, false);
        return new userEventViewHolder(view, onUserEventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull userEventViewHolder holder, int position) {
        holder.txt_event_name.setText(events.get(position).getName_event());
        holder.txt_event_date.setText(events.get(position).getEvent_start_date());
        holder.event_id.setText(String.valueOf(events.get(position).getId()));

        Glide.with(holder.itemView).load(ConfigurationAll.ImageURL + events.get(position).getEvent_picture()).into(holder.img_event_image);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class userEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txt_event_name, txt_event_date, event_id;
        private ImageView img_event_image;

        onUserEventListener onUserEventListener;
        public userEventViewHolder(@NonNull View itemView, onUserEventListener onUserEventListener) {
            super(itemView);

            txt_event_name = itemView.findViewById(R.id.txt_event_name);
            txt_event_date = itemView.findViewById(R.id.txt_event_date);
            img_event_image = itemView.findViewById(R.id.img_event_image);
            event_id = itemView.findViewById(R.id.event_id);

            this.onUserEventListener = onUserEventListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onUserEventListener.onUserEventClick(getAdapterPosition());
        }
    }

    public interface onUserEventListener {
        void onUserEventClick(int i);
    }
}
