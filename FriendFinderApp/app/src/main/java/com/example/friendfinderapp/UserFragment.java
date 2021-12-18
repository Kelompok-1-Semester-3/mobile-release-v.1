package com.example.friendfinderapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.example.friendfinderapp.Model.UserAccount;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private ImageView iv_user_profile;
    private EditText et_user_name, et_contact_person, et_email_user;
    private String profile, fullname, phone, email;
    private Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        et_user_name = view.findViewById(R.id.et_user_name);
        et_contact_person = view.findViewById(R.id.et_contact_person);
        et_email_user = view.findViewById(R.id.et_email_user);
        iv_user_profile= view.findViewById(R.id.iv_user_profile);
        Button btn_choose_user_profile = view.findViewById(R.id.btn_choose_user_profile);

        getDetailAccount();

        btn_choose_user_profile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        Button btn_update_account = view.findViewById(R.id.btn_update_account);
        btn_update_account.setOnClickListener(v -> {
            fullname = et_user_name.getText().toString();
            phone = et_contact_person.getText().toString();
            email = et_email_user.getText().toString();
            StringRequest request = new StringRequest(Request.Method.POST, ConfigurationAll.urlUpdateAccount , response -> {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                getDetailAccount();
                ConfigurationAll.fullname = fullname;
                ConfigurationAll.profile = profile;
            }, error -> {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }) {
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    String profile_code = imageToString(bitmap);
                    map.put("fullname", fullname);
                    map.put("phone", phone);
                    map.put("email", email);
                    map.put("profile", profile_code);
                    map.put("id", ConfigurationAll.user_id);
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);
        });

        return view;
    }

    public void getDetailAccount() {
        APIRequestData apiRequestData = RetroServer.konekRetro().create(APIRequestData.class);
        Call<UserAccount> userAccountCall = apiRequestData.resGetDetailAccount(ConfigurationAll.user_id);
        userAccountCall.enqueue(new Callback<UserAccount>() {
            @Override
            public void onResponse(@NonNull Call<UserAccount> call, @NonNull Response<UserAccount> response) {
                UserAccount userAccount = response.body();
                assert userAccount != null;
                fullname = userAccount.getFullname();
                phone = userAccount.getPhone();
                email = userAccount.getEmail();
                profile = userAccount.getProfile();

                // set field
                Glide.with(requireContext())
                    .asBitmap()
                    .load(ConfigurationAll.ImageURL + profile)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            bitmap = resource;
                            iv_user_profile.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
                et_user_name.setText(fullname);
                et_contact_person.setText(phone);
                et_email_user.setText(email);
            }

            @Override
            public void onFailure(Call<UserAccount> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            assert data != null;
            if (data.getData() != null) {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    iv_user_profile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}