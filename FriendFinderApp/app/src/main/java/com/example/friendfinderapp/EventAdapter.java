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

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events;

    private OnEventListener onEventListener;

    public EventAdapter(List<Event> events, OnEventListener onEventListener) {
        this.events = events;
        this.onEventListener = onEventListener;
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view, onEventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        holder.txt_event_name.setText(events.get(position).getName_event());
        holder.txt_event_date.setText(events.get(position).getEvent_start_date());
        holder.event_id.setText(String.valueOf(events.get(position).getId()));

        Glide.with(holder.itemView).load(ConfigurationAll.ImageURL + events.get(position).getEvent_picture()).into(holder.img_event_image);
    }

    @Override
    public int getItemCount() {
        return (events != null) ? events.size() : 0;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_event_name, txt_event_date, event_id;
        private ImageView img_event_image;

        OnEventListener onEventListener;
        public EventViewHolder(@NonNull View itemView, OnEventListener onEventListener) {
            super(itemView);

            txt_event_name = itemView.findViewById(R.id.txt_event_name);
            txt_event_date = itemView.findViewById(R.id.txt_event_date);
            img_event_image = itemView.findViewById(R.id.img_event_image);
            event_id = itemView.findViewById(R.id.event_id);

            this.onEventListener = onEventListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onEventListener.onEventClick(getAdapterPosition());
        }
    }

    public interface OnEventListener {
        void onEventClick(int position);
    }
}
