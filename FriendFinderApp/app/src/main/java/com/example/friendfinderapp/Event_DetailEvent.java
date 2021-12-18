package com.example.friendfinderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Event_DetailEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__detail_event);

        // init
        TextView detail_event_name = findViewById(R.id.detail_event_name);
        ImageView detail_event_image = findViewById(R.id.detail_event_image);
        ImageView btn_back_to_events = findViewById(R.id.btn_back_to_events);
        TextView detail_event_date = findViewById(R.id.detail_event_date);

        // get parsing data
//        if (getIntent() != null) {
//                String event_name = getIntent().getStringExtra("event_name").toString();
//                int event_image = getIntent().getIntExtra("event_image", 0);
//                String event_date = getIntent().getStringExtra("event_date").toString();
//                detail_event_name.setText(event_name);
//                detail_event_image.setImageResource(event_image);
//                detail_event_date.setText(event_date);
//        }
        ;

        btn_back_to_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}