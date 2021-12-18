package com.example.friendfinderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import retrofit2.Response;

public class seeAllPlaceFragment extends Fragment implements PlaceListAdapter.onPlaceListListener {

    private RecyclerView recycle_view_list_place;
    private List<PlaceList> placeLists = new ArrayList<>();
    private PlaceList placeListItem;
    private PlaceListAdapter placeListAdapter;


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

        CardView btn_search_place = view.findViewById(R.id.btn_search_place);
        EditText txt_search_place = view.findViewById(R.id.txt_search_place);

        btn_search_place.setOnClickListener(v -> {

            placeLists.clear();
            APIRequestData apiRequestData = RetroServer.konekRetro().create(APIRequestData.class);
            Call<List<PlaceList>> call = apiRequestData.resGetPlaceByKeyword(txt_search_place.getText().toString());
            call.enqueue(new Callback<List<PlaceList>>() {
                @Override
                public void onResponse(Call<List<PlaceList>> call, Response<List<PlaceList>> response) {
                    List<PlaceList> placeLists = response.body();
                    for (PlaceList placeList : placeLists) {
                        String id = placeList.getId();
                        String place_name = placeList.getPlace_name();
                        String place_owner = placeList.getPlace_owner();
                        String contact_person = placeList.getContact_person();
                        String description = placeList.getDescription();
                        String place_picture = placeList.getPlace_picture();
                        String place_open_time = placeList.getPlace_open_time();
                        String place_close_time = placeList.getPlace_close_time();
                        String price = placeList.getPrice();
                        String location = placeList.getLocation();
                        String category = placeList.getCategory();

                        placeListItem = new PlaceList(id, place_name, place_owner, price, location, description, place_picture, place_open_time, place_close_time, contact_person, category);
                        placeLists.add(placeListItem);
                    }

                    placeListAdapter = new PlaceListAdapter(placeLists, seeAllPlaceFragment.this);
                    recycle_view_list_place.setAdapter(placeListAdapter);
                }

                @Override
                public void onFailure(Call<List<PlaceList>> call, Throwable t) {

                }
            });
        });

        return view;
    }

    private void addPlaceListData() {
        placeLists.clear();
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

                    placeListItem = new PlaceList(id, place_name, place_owner, price, location, description, place_picture, place_open_time, place_close_time, contact_person, category);
                    placeLists.add(placeListItem);
                }

                placeListAdapter = new PlaceListAdapter(placeLists, seeAllPlaceFragment.this);
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