package com.example.friendfinderapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.friendfinderapp.API.APIRequestData;
import com.example.friendfinderapp.API.RetroServer;
import com.example.friendfinderapp.Constants.ConfigurationAll;
import com.example.friendfinderapp.Model.CategoryModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEvent extends AppCompatActivity {

    // image
    int SELECT_PHOTO = 1;
    private Bitmap bitmap;
    private ImageView iv_event_picture;
    private Spinner spinner;
    private SimpleDateFormat simpleDateFormat;
    private EditText txt_event_name, txt_event_owner, txt_contact_person, txt_description, txt_price, txt_location;
    private TextView txt_start_date, txt_end_date, txt_id;
    // form credential
    private String id, event_name, event_owner, contact_person, description,
            start_date, end_date, location, event_picture, event_start_date, event_end_date;
    private int category_id;
    private String price;
    private int category_id_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // init
        txt_id = findViewById(R.id.txt_id);
        txt_event_name = findViewById(R.id.et_event_name);
        txt_event_owner = findViewById(R.id.et_event_owner);
        txt_contact_person = findViewById(R.id.et_contact_person);
        txt_description = findViewById(R.id.et_description);
        txt_price = findViewById(R.id.et_price);
        txt_location = findViewById(R.id.et_location);

        spinner = findViewById(R.id.cb_category);
        getAllCategoriesEvent();

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        txt_start_date = findViewById(R.id.txt_start_date);
        txt_end_date = findViewById(R.id.txt_end_date);

        iv_event_picture = findViewById(R.id.iv_event_picture);
        Button btn_event_picture = findViewById(R.id.btn_event_picture);
        Button btn_submit_edit_event = findViewById(R.id.btn_submit_edit_event);

        btn_event_picture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_PHOTO);
        });

        btn_submit_edit_event.setOnClickListener(v -> {
            event_name = txt_event_name.getText().toString();
            event_owner = txt_event_owner.getText().toString();
            contact_person = txt_contact_person.getText().toString();
            description = txt_description.getText().toString();
            start_date = txt_start_date.getText().toString();
            end_date = txt_end_date.getText().toString();
            price = txt_price.getText().toString();
            location = txt_location.getText().toString();
            id = txt_id.getText().toString();
            StringRequest request = new StringRequest(Request.Method.POST, ConfigurationAll.urlEditEvent, response -> {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                resetField();
                getOnBackPressedDispatcher().onBackPressed();

            }, error -> {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(error.toString());
            }) {
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> param = new HashMap<>();
                    String event_picture = imageToString(bitmap);
                    param.put("id", id);
                    param.put("name_event", event_name);
                    param.put("event_owner", event_owner);
                    param.put("contact_person", contact_person);
                    param.put("description", description);
                    param.put("event_picture", event_picture);
                    param.put("category_id", String.valueOf(category_id));
                    param.put("event_start_date", start_date);
                    param.put("event_end_date", end_date);
                    param.put("price", String.valueOf(price));
                    param.put("location", location);
                    param.put("created_by", ConfigurationAll.user_id);
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(EditEvent.this);
            requestQueue.add(request);
        });


        Button btn_start_date = findViewById(R.id.btn_start_date);
        Button btn_end_date = findViewById(R.id.btn_end_date);

        btn_start_date.setOnClickListener(v -> showDatePickerDialog(txt_start_date));
        btn_end_date.setOnClickListener(v -> showDatePickerDialog(txt_end_date));

        ImageView btn_back_to_user_event = findViewById(R.id.btn_back_to_user_event);
        btn_back_to_user_event.setOnClickListener(new View.OnClickListener() {
            public void onBackPressed() {
                getOnBackPressedDispatcher().onBackPressed();
            }

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // get data edit
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            event_name = getIntent().getStringExtra("event_name");
            event_owner = getIntent().getStringExtra("event_owner");
            contact_person = getIntent().getStringExtra("contact_person");
            description = getIntent().getStringExtra("description");
            event_picture = getIntent().getStringExtra("event_picture");
            category_id_edit = getIntent().getIntExtra("category_id", 0);
            event_start_date = getIntent().getStringExtra("event_start_date");
            event_end_date = getIntent().getStringExtra("event_end_date");
            price = getIntent().getStringExtra("price");
            location = getIntent().getStringExtra("location");

            // set all field with new data edit
            txt_id.setText(id);
            Glide.with(this)
                    .asBitmap()
                    .load(ConfigurationAll.ImageURL + event_picture)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            bitmap = resource;
                            iv_event_picture.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

            txt_event_name.setText(event_name);
            txt_event_owner.setText(event_owner);
            txt_contact_person.setText(contact_person);
            txt_description.setText(description);
            txt_start_date.setText(event_start_date);
            txt_end_date.setText(event_end_date);
            txt_price.setText(price);
            txt_location.setText(location);
            System.out.println(price + " : " + contact_person);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getData() != null) {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    iv_event_picture.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showDatePickerDialog(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(year, month, dayOfMonth);
            textView.setText(simpleDateFormat.format(calendar1.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getAllCategoriesEvent() {
        APIRequestData apiRequestData = RetroServer.konekRetro().create(APIRequestData.class);
        Call<List<CategoryModel>> call = apiRequestData.resGetAllCategories();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryModel>> call, @NonNull Response<List<CategoryModel>> response) {
                List<CategoryModel> categoryModels = response.body();
                List<String> list_categories = new ArrayList<>();
                assert categoryModels != null;
                for (CategoryModel categoryModel : categoryModels) {
                    list_categories.add(categoryModel.getName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, list_categories);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String category_name = list_categories.get(position);
                        switch (category_name) {
                            case "Art":
                                category_id = 3;
                                break;
                            case "Design":
                                category_id = 1;
                                break;
                            case "Education":
                                category_id = 4;
                                break;
                            case "Sport":
                                category_id = 2;
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + category_name);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryModel>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void resetField() {
        txt_event_name.setText("");
        txt_event_owner.setText("");
        txt_price.setText("");
        txt_location.setText("");
        txt_description.setText("");
        txt_start_date.setText("");
        txt_end_date.setText("");
        txt_contact_person.setText("");
        txt_id.setText("");
        iv_event_picture.setImageResource(R.mipmap.event1);
    }
}