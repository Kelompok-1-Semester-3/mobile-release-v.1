package com.example.friendfinderapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.friendfinderapp.API.APIRequestData;
import com.example.friendfinderapp.API.RetroServer;
import com.example.friendfinderapp.Constants.ConfigurationAll;
import com.example.friendfinderapp.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserEvent extends AppCompatActivity {

    private int id;
    private  TextView detail_event_name;
    private  String event_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_event);

        // init
        ImageView btn_back_to_see_all = findViewById(R.id.btn_back_to_see_all);
        detail_event_name = findViewById(R.id.detail_event_name);
        ImageView detail_event_image = findViewById(R.id.detail_event_image);
        TextView detail_event_date = findViewById(R.id.detail_event_date);
        TextView detail_contact_person = findViewById(R.id.detail_contact_person);
        TextView detail_owner_name = findViewById(R.id.detail_owner_name);
        TextView detail_description = findViewById(R.id.detail_description);
        CardView btn_edit_event = findViewById(R.id.btn_edit_event);
        CardView btn_delete_event_user = findViewById(R.id.btn_delete_event);

        btn_edit_event.setOnClickListener(v -> getDetailEvent());

        // get parsing data
        if (getIntent() != null) {
            id = Integer.parseInt(getIntent().getStringExtra("id"));
            String event_name = getIntent().getStringExtra("event_name");
            String event_picture = getIntent().getStringExtra("event_picture");
            String start_date = getIntent().getStringExtra("start_date");
            String event_owner = getIntent().getStringExtra("event_owner");
            String contact_person = getIntent().getStringExtra("contact_person");
            String description = getIntent().getStringExtra("description");

            detail_event_name.setText(event_name);
            detail_event_date.setText(start_date);
            detail_contact_person.setText(contact_person);
            detail_owner_name.setText(event_owner);
            detail_description.setText(description);
            System.out.println(id);

            Glide.with(this).load(ConfigurationAll.ImageURL + event_picture).into(detail_event_image);

        }

        btn_back_to_see_all.setOnClickListener(new View.OnClickListener() {
            @MainThread
            public void onBackPressed() {
                getOnBackPressedDispatcher().onBackPressed();
            }

            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        btn_delete_event_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                kurang refresh

                AlertDialog.Builder dialogPesan = new AlertDialog.Builder(DetailUserEvent.this);
                dialogPesan.setMessage("Are you sure to delete this event : " + event_name + " ?");
                dialogPesan.setCancelable(true);

                DialogInterface.OnClickListener diaOnClickListener = (dialog, which) -> {

                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            DeletUserEvent();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };
                dialogPesan.setPositiveButton("Delete", diaOnClickListener);
                dialogPesan.setNegativeButton("No", diaOnClickListener);
                AlertDialog dialog = dialogPesan.create();
                dialog.show();
            }
        });
    }

    private void getDetailEvent() {
        APIRequestData apiRequestData = RetroServer.konekRetro().create(APIRequestData.class);
        Call<userEvent> call = apiRequestData.resGetDetailEvent(String.valueOf(id));
        call.enqueue(new Callback<userEvent>() {
            @Override
            public void onResponse(@NonNull Call<userEvent> call, @NonNull Response<userEvent> response) {
                userEvent event = response.body();
                Intent intent = new Intent(DetailUserEvent.this, EditEvent.class);
                assert event != null;
                intent.putExtra("id", event.getId());
                intent.putExtra("event_name", event.getName_event());
                intent.putExtra("event_owner", event.getEvent_owner());
                intent.putExtra("contact_person", event.getContact_person());
                intent.putExtra("description", event.getDescription());
                intent.putExtra("event_picture", event.getEvent_picture());
                intent.putExtra("category_id", event.getCategory());
                intent.putExtra("event_start_date", event.getEvent_start_date());
                intent.putExtra("event_end_date", event.getEvent_end_date());
                intent.putExtra("price", event.getPrice());
                intent.putExtra("location", event.getLocation());
                startActivity(intent);
            }

            @Override
            public void onFailure(@NonNull Call<userEvent> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void DeletUserEvent() {
        APIRequestData apiRequestData = RetroServer.konekRetro().create(APIRequestData.class);
        Call<ResponseModel> distroy = apiRequestData.DistroyUserEvent(id);
        distroy.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                String Pesan = response.body().getPesan();

                Toast.makeText(getApplicationContext(), Pesan, Toast.LENGTH_SHORT).show();
                getOnBackPressedDispatcher().onBackPressed();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

                Toast.makeText(getApplicationContext(), "Koneksi gagal ", Toast.LENGTH_SHORT).show();

            }
        });
    }
}