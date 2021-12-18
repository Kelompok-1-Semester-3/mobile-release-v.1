package com.example.friendfinderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.friendfinderapp.Constants.ConfigurationAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements EventAdapter.OnEventListener, ThumbnailEventAdapter.OnEventListener, ThumbnailPlaceAdapter.OnEventListener {

    RequestQueue requestQueue;
    private final List<ThumbnailEvent> thumbnailEvents = new ArrayList<>();
    //    private ArrayList<Category> categories;
    private List<Event> events = new ArrayList<>();
    private List<ThumbnailPlace> thumbnailPlaces = new ArrayList<>();

    // recycler view init
    RecyclerView recyclerViewEvent, recyclerViewThumbnailEvent, recyclerViewThumbnailPlace;

    // url for get all event data

    public static String username = "", profile = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // init
        TextView tvNama = view.findViewById(R.id.tvNama);
        ImageView iv_user_profile = view.findViewById(R.id.iv_user_profile);

        // set data
        tvNama.setText(ConfigurationAll.fullname);
        if (profile != null) {
            Glide.with(view.getContext()).load(ConfigurationAll.ImageURL + ConfigurationAll.profile).into(iv_user_profile);
        } else {
            iv_user_profile.setImageResource(R.mipmap.hero);
        }

        TextView txt_see_all_place = view.findViewById(R.id.txt_see_all_place);
        txt_see_all_place.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController((Activity) view.getContext(), R.id.fragment);
            navController.navigate(R.id.seeAllPlaceFragment);
        });

        TextView txt_link_seeAll_event = view.findViewById(R.id.txt_link_seeAll_event);
        txt_link_seeAll_event.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController((Activity) view.getContext(), R.id.fragment);
            navController.navigate(R.id.homeSeeAllFragment2);
        });

        // thumbnail event
        addEventThumbnailItem();
        recyclerViewThumbnailEvent = view.findViewById(R.id.recycle_view_event_thumbnail);
        RecyclerView.LayoutManager layoutManagerThumbnailEven = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewThumbnailEvent.setLayoutManager(layoutManagerThumbnailEven);

        // categories
        /*addCategoryItem();
        RecyclerView recyclerViewCategories = view.findViewById(R.id.recycle_view_category);
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        recyclerViewCategories.setAdapter(categoryAdapter);
        RecyclerView.LayoutManager layoutManagerCategories = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManagerCategories);*/

        // event class
        addEventItem();
        recyclerViewEvent = view.findViewById(R.id.recycle_view_event);
        RecyclerView.LayoutManager layoutManagerEvent = new LinearLayoutManager(view.getContext());
        recyclerViewEvent.setLayoutManager(layoutManagerEvent);

        // thumbnail places
        addThumbnailPlaceItem();
        recyclerViewThumbnailPlace = view.findViewById(R.id.recycle_view_place_thumbnail);
        RecyclerView.LayoutManager layoutManagerThumbnailPlace = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewThumbnailPlace.setLayoutManager(layoutManagerThumbnailPlace);
        return view;
    }


    // add thumbnail event item
    private void addEventThumbnailItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConfigurationAll.THUMBNAIL_EVENT_URL, response -> {
            try {
                JSONArray thumbnailEventArray = new JSONArray(response);
                for (int i = 0; i < thumbnailEventArray.length(); i++) {
                    JSONObject thumbnailEventsJSONObject = thumbnailEventArray.getJSONObject(i);
                    int id = thumbnailEventsJSONObject.getInt("id");
                    String name_event = thumbnailEventsJSONObject.getString("name_event");
                    String event_start_date = thumbnailEventsJSONObject.getString("event_start_date");
                    String event_picture = thumbnailEventsJSONObject.getString("event_picture");
                    String category = thumbnailEventsJSONObject.getString("category");

                    ThumbnailEvent thumbnailEvent = new ThumbnailEvent(name_event, event_start_date, category, event_picture, id);
                    thumbnailEvents.add(thumbnailEvent);
                }
                ThumbnailEventAdapter thumbnailEventAdapter = new ThumbnailEventAdapter(thumbnailEvents, HomeFragment.this);
                recyclerViewThumbnailEvent.setAdapter(thumbnailEventAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    // add category item
    /*private void addCategoryItem() {
        categories = new ArrayList<>();
        categories.add(new Category("Sport", R.mipmap.category));
        categories.add(new Category("Education", R.mipmap.category1));
        categories.add(new Category("Hangout", R.mipmap.category2));
        categories.add(new Category("Vacation", R.mipmap.category3));
    }*/

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
                EventAdapter eventAdapter = new EventAdapter(events, HomeFragment.this);
                recyclerViewEvent.setAdapter(eventAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    // add thumbnail place item
    private void addThumbnailPlaceItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConfigurationAll.THUMBNAIL_PLACE_URL, response -> {
            try {
                JSONArray thumbnailPlaceArray = new JSONArray(response);
                for (int i = 0; i < thumbnailPlaceArray.length(); i++) {
                    JSONObject thumbnailPlaceObject = thumbnailPlaceArray.getJSONObject(i);
                    String id = thumbnailPlaceObject.getString("id");
                    String place_name = thumbnailPlaceObject.getString("place_name");
                    String place_owner = thumbnailPlaceObject.getString("place_owner");
                    String price = thumbnailPlaceObject.getString("price");
                    String location = thumbnailPlaceObject.getString("location");
                    String description = thumbnailPlaceObject.getString("description");
                    String place_picture = thumbnailPlaceObject.getString("place_picture");
                    String place_open_time = thumbnailPlaceObject.getString("place_open_time");
                    String place_close_time = thumbnailPlaceObject.getString("place_close_time");
                    String contact_person = thumbnailPlaceObject.getString("contact_person");
                    String category = thumbnailPlaceObject.getString("category");

                    ThumbnailPlace thumbnailPlace = new ThumbnailPlace(id, place_name, place_owner, price,
                            location, description, place_picture, place_open_time, place_close_time,
                            contact_person, category);
                    thumbnailPlaces.add(thumbnailPlace);
                }
                ThumbnailPlaceAdapter thumbnailPlaceAdapter = new ThumbnailPlaceAdapter(thumbnailPlaces, HomeFragment.this);
                recyclerViewThumbnailPlace.setAdapter(thumbnailPlaceAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(getContext()).add(stringRequest);
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

    // event click
    @Override
    public void onThumbnailEventClick(int position) {
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

    @Override
    public void onThumbnailPlaceClick(int position) {
        Intent intent = new Intent(this.getContext(), DetailPlace.class);

        intent.putExtra("place_name", thumbnailPlaces.get(position).getPlace_name());
        intent.putExtra("place_picture", thumbnailPlaces.get(position).getPlace_picture());
        intent.putExtra("description", thumbnailPlaces.get(position).getDescription());
        intent.putExtra("place_owner", thumbnailPlaces.get(position).getPlace_owner());
        intent.putExtra("place_price", thumbnailPlaces.get(position).getPrice());
        intent.putExtra("place_time_schedule", thumbnailPlaces.get(position).getPlace_open_time());
        intent.putExtra("location", thumbnailPlaces.get(position).getLocation());
        intent.putExtra("contact_person", thumbnailPlaces.get(position).getContact_person());

        startActivity(intent);
    }
}