package com.example.friendfinderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfinderapp.API.APIRequestData;
import com.example.friendfinderapp.API.RetroServer;
import com.example.friendfinderapp.Constants.ConfigurationAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HomeSeeAllFragment extends Fragment implements EventAdapter.OnEventListener {

    private List<Event> events = new ArrayList<>();

    // recycler view init
    RecyclerView recyclerViewEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_see_all, container, false);

        // init
        NavController navController = Navigation.findNavController((Activity) view.getContext(), R.id.fragment);
        ImageView btn_back_to_home = view.findViewById(R.id.btn_back_to_home);

        // event
        // back to home
        btn_back_to_home.setOnClickListener(v -> navController.navigate(R.id.homeFragment));

        // categories
        /*
        addCategoryItem();
        RecyclerView recyclerViewCategories = view.findViewById(R.id.recycle_view_category);
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        recyclerViewCategories.setAdapter(categoryAdapter);
        RecyclerView.LayoutManager layoutManagerCategories = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManagerCategories);
        */

        // event class
        addEventItem();
        recyclerViewEvent = view.findViewById(R.id.recycle_view_event);
        RecyclerView.LayoutManager layoutManagerEvent = new LinearLayoutManager(view.getContext());
        recyclerViewEvent.setLayoutManager(layoutManagerEvent);

        EditText txt_search_event = view.findViewById(R.id.txt_search_event);
        txt_search_event.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        CardView btn_search_event = view.findViewById(R.id.btn_search_event);
        btn_search_event.setOnClickListener(v -> {
            events.clear();
            if (String.valueOf(txt_search_event.getText()).equals("")) {
                addEventItem();
            } else {
                APIRequestData apiRequestData = RetroServer.konekRetro().create(APIRequestData.class);
                Call<List<userEvent>> call = apiRequestData.resEventByKeyword(String.valueOf(txt_search_event.getText()));
                call.enqueue(new Callback<List<userEvent>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<userEvent>> call, @NonNull retrofit2.Response<List<userEvent>> response) {
                        List<userEvent> userEvents = response.body();
                        assert userEvents != null;
                        for (userEvent userEvent : userEvents) {
                            String id = userEvent.getId();
                            String name_event = userEvent.getName_event();
                            String event_owner = userEvent.getEvent_owner();
                            String contact_person = userEvent.getContact_person();
                            String description = userEvent.getDescription();
                            String event_picture = userEvent.getEvent_picture();
                            String event_start_date = userEvent.getEvent_start_date();
                            String event_end_date = userEvent.getEvent_end_date();
                            String price = userEvent.getPrice();
                            String location = userEvent.getLocation();
                            String category = userEvent.getCategory();

                            Event event = new Event(id, name_event, event_owner, contact_person, description, event_picture, event_start_date, event_end_date, price, location, category);
                            events.add(event);
                        }
                        EventAdapter eventAdapter = new EventAdapter(events, HomeSeeAllFragment.this);
                        recyclerViewEvent.setAdapter(eventAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<userEvent>> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Data Not found!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ImageView btn_refresh_event = view.findViewById(R.id.btn_refresh_event);
        btn_refresh_event.setOnClickListener(v -> {
            addEventItem();
            txt_search_event.setText("");
        });

        return view;
    }

    // add event item
    private void addEventItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConfigurationAll.EVENT_URL, response -> {
            try {
                JSONArray eventArray = new JSONArray(response);
                for (int i = 0; i < eventArray.length(); i++) {
                    JSONObject eventsJSONObject = eventArray.getJSONObject(i);
                    String id = eventsJSONObject.getString("id");
                    String name_event = eventsJSONObject.getString("name_event");
                    String event_owner = eventsJSONObject.getString("event_owner");
                    String contact_person = eventsJSONObject.getString("contact_person");
                    String description = eventsJSONObject.getString("description");
                    String event_picture = eventsJSONObject.getString("event_picture");
                    String event_start_date = eventsJSONObject.getString("event_start_date");
                    String event_end_date = eventsJSONObject.getString("event_end_date");
                    String price = eventsJSONObject.getString("price");
                    String location = eventsJSONObject.getString("location");
                    String category = eventsJSONObject.getString("category");

                    Event event = new Event(id, name_event, event_owner, contact_person, description, event_picture, event_start_date, event_end_date, price, location, category);
                    events.add(event);
                }
                EventAdapter eventAdapter = new EventAdapter(events, HomeSeeAllFragment.this);
                recyclerViewEvent.setAdapter(eventAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }

    // event click
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
}