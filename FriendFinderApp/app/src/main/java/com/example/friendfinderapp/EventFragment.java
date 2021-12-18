package com.example.friendfinderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.friendfinderapp.API.APIRequestData;
import com.example.friendfinderapp.API.RetroServer;
import com.example.friendfinderapp.Constants.ConfigurationAll;
import com.example.friendfinderapp.Model.Event_Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFragment extends Fragment implements EventAdapter.OnEventListener, userEventAdapter.onUserEventListener {

    private final List<userEvent> events = new ArrayList<>();
    public SwipeRefreshLayout swl_refresh;
    FloatingActionButton btn_add_new_event;
    // recycler view init
    private RecyclerView recyclerViewEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        // init
        TextView txt_user_name = view.findViewById(R.id.txt_user_name);
        ImageView iv_user_profile = view.findViewById(R.id.iv_user_profile);
        swl_refresh = view.findViewById(R.id.swl_refresh);

        swl_refresh.setOnRefreshListener(() -> {
            swl_refresh.setRefreshing(true);
            addEventItem();
            swl_refresh.setRefreshing(false);
        });

        txt_user_name.setText(ConfigurationAll.fullname);
        Glide.with(EventFragment.this).load(ConfigurationAll.ImageURL + ConfigurationAll.profile)
                .into(iv_user_profile);

        btn_add_new_event = view.findViewById(R.id.btn_add_new_event);
        btn_add_new_event.setOnClickListener(v -> startActivity(new Intent(v.getContext(), EventForm.class)));

        // event class
        addEventItem();
        recyclerViewEvent = view.findViewById(R.id.recycle_view_user_event);
        RecyclerView.LayoutManager layoutManagerEvent = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewEvent.setLayoutManager(layoutManagerEvent);

        return view;
    }

    @Override
    public void onEventClick(int position) {
        Intent intent = new Intent(this.getContext(), DetailEvent.class);
        // 11 item
        intent.putExtra("event_name", events.get(position).getName_event());
        intent.putExtra("event_picture", events.get(position).getEvent_picture());
        intent.putExtra("start_date", events.get(position).getEvent_start_date());
        intent.putExtra("event_owner", events.get(position).getEvent_owner());
        intent.putExtra("contact_person", events.get(position).getContact_person());
        intent.putExtra("description", events.get(position).getDescription());

        startActivity(intent);
    }

    private void addEventItem() {
        events.clear();
        APIRequestData apiRequestData = RetroServer.konekRetro().create(APIRequestData.class);
        Call<List<Event_Model>> call = apiRequestData.resAllUserEvent(ConfigurationAll.user_id);
        call.enqueue(new Callback<List<Event_Model>>() {
            @Override
            public void onResponse(Call<List<Event_Model>> call, Response<List<Event_Model>> response) {
                List<Event_Model> event_models = response.body();
                for (Event_Model event_model : event_models) {
                    String id = event_model.getId();
                    String name_event = event_model.getName_event();
                    String event_owner = event_model.getEvent_owner();
                    String contact_person = event_model.getContact_person();
                    String description = event_model.getDescription();
                    String event_picture = event_model.getEvent_picture();
                    String event_start_date = event_model.getEvent_start_date();
                    String event_end_date = event_model.getEvent_end_date();
                    String price = event_model.getPrice();
                    String location = event_model.getLocation();
                    String category = event_model.getCategory();

                    userEvent event = new userEvent(id, name_event, event_owner, contact_person, description, event_picture, event_start_date, event_end_date, price, location, category);
                    events.add(event);
                }

                userEventAdapter eventAdapter = new userEventAdapter(events, EventFragment.this);
                recyclerViewEvent.setAdapter(eventAdapter);
            }

            @Override
            public void onFailure(Call<List<Event_Model>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onUserEventClick(int position) {
        Intent intent = new Intent(this.getContext(), DetailUserEvent.class);
        // 11 item
        intent.putExtra("id", events.get(position).getId());
        intent.putExtra("event_name", events.get(position).getName_event());
        intent.putExtra("event_picture", events.get(position).getEvent_picture());
        intent.putExtra("start_date", events.get(position).getEvent_start_date());
        intent.putExtra("event_owner", events.get(position).getEvent_owner());
        intent.putExtra("contact_person", events.get(position).getContact_person());
        intent.putExtra("description", events.get(position).getDescription());

        startActivity(intent);
    }
}