package com.example.friendfinderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfinderapp.Constants.ConfigurationAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class seeAllPlaceFragment extends Fragment implements PlaceListAdapter.onPlaceListListener {

    private RecyclerView recycle_view_list_place;
    private List<PlaceList> placeLists = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_see_all_place, container, false);

        // init
        NavController navController = Navigation.findNavController((Activity) view.getContext(), R.id.fragment);
        ImageView btn_back_to_home = view.findViewById(R.id.btn_back_to_home);

        // event
        // back to home
        btn_back_to_home.setOnClickListener(v -> navController.navigate(R.id.homeFragment));

        // place data
        addPlaceListData();
        recycle_view_list_place = view.findViewById(R.id.recycle_view_list_place);
        RecyclerView.LayoutManager placeLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recycle_view_list_place.setLayoutManager(placeLayoutManager);

        return view;
    }

    private void addPlaceListData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConfigurationAll.PLACE_URL, response -> {
            try {
                JSONArray placeArray = new JSONArray(response);
                for (int i = 0; i < placeArray.length(); i++) {
                    JSONObject placeObj = placeArray.getJSONObject(i);
                    String id = placeObj.getString("id");
                    String place_name = placeObj.getString("place_name");
                    String place_owner = placeObj.getString("place_owner");
                    String contact_person = placeObj.getString("contact_person");
                    String description = placeObj.getString("description");
                    String place_picture = placeObj.getString("place_picture");
                    String place_open_time = placeObj.getString("place_open_time");
                    String place_close_time = placeObj.getString("place_close_time");
                    String price = placeObj.getString("price");
                    String location = placeObj.getString("location");
                    String category = placeObj.getString("category");

                    PlaceList placeList = new PlaceList(id, place_name, place_owner, price, location, description, place_picture, place_open_time, place_close_time, contact_person, category);
                    placeLists.add(placeList);
                }

                PlaceListAdapter placeListAdapter = new PlaceListAdapter(placeLists, seeAllPlaceFragment.this);
                recycle_view_list_place.setAdapter(placeListAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(getContext(), "Data place tidak ada!", Toast.LENGTH_SHORT).show();
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);        
    }

    @Override
    public void onPlaceListClick(int position) {
        Intent intent = new Intent(this.getContext(), DetailPlace.class);

        intent.putExtra("place_name", placeLists.get(position).getPlace_name());
        intent.putExtra("place_picture", placeLists.get(position).getPlace_picture());
        intent.putExtra("description", placeLists.get(position).getDescription());
        intent.putExtra("place_owner", placeLists.get(position).getPlace_owner());
        intent.putExtra("place_price", placeLists.get(position).getPrice());
        intent.putExtra("place_time_schedule", placeLists.get(position).getPlace_open_time());
        intent.putExtra("location", placeLists.get(position).getLocation());
        intent.putExtra("contact_person", placeLists.get(position).getContact_person());

        startActivity(intent);
    }
}