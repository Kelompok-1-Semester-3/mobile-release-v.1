package com.example.friendfinderapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendfinderapp.API.APIRequestData;
import com.example.friendfinderapp.API.RetroServer;
import com.example.friendfinderapp.Model.ResponseModel;
import com.example.friendfinderapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    private EditText etFullname,etPhone, etEmail, etPassword;
    private Button btnSignUp;
    private String fullname, phone,email, password;
    private TextView link_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etFullname = findViewById(R.id.edFullname);
        etPhone = findViewById(R.id.edPhone);
        etEmail = findViewById(R.id.edEmail);
        etPassword = findViewById(R.id.edPassword);
        btnSignUp = findViewById(R.id.btn_sign_up);
        link_sign_in = findViewById(R.id.link_sign_in);

        btnSignUp.setOnClickListener(v -> {
            fullname = etFullname.getText().toString();
            phone = etPhone.getText().toString();
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();

            if (fullname.trim().length() == 0) {
                etFullname.setError("field tidak boleh kosong");
            } else if (phone.trim().length() == 0) {
                etFullname.setError("field tidak boleh kosong");
            }else if (email.trim().length() == 0) {
                etFullname.setError("field tidak boleh kosong");
            } else if (password.trim().length() == 0) {
                etFullname.setError("field tidak boleh kosong");
            } else {
                Register();
            }
        });

        link_sign_in.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
        });
    }

    private void resetField() {
        etFullname = findViewById(R.id.edFullname);
        etPhone = findViewById(R.id.edPhone);
        etEmail = findViewById(R.id.edEmail);
        etPassword = findViewById(R.id.edPassword);

        etFullname.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etPassword.setText("");
    }

    private void Register() {
        APIRequestData adrData = RetroServer.konekRetro().create(APIRequestData.class);
        Call<ResponseModel> SignUp = adrData.ardAddData(fullname,phone, email, password);

        SignUp.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                String pesan = response.body().getPesan();
                Toast.makeText(SignUp.this, pesan, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(SignUp.this, "Gagal menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                resetField();
                finish();
            }
        });
    }
}