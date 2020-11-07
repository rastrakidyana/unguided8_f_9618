package com.example.gd8_f_9618;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etNim, etPassword;
    private MaterialButton btnLogin;
    private String sNim, sPassword;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "userLogin";
    private static final String KEY_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);

        etNim = findViewById(R.id.etNim);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String id = sharedPreferences.getString(KEY_ID, null);

        if (id != null){
            if (!id.equals("0")) {
                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNim.getText().toString().isEmpty()){
                    etNim.setError("Isikan dengan benar");
                    etNim.requestFocus();
                } else if (etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Isikan dengan benar");
                    etPassword.requestFocus();
                } else {
                    progressDialog.show();
                    requestLogin();
                }
            }
        });
    }

    private void requestLogin() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> log = apiService.login(etNim.getText().toString(), etPassword.getText().toString());

        log.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body()!=null){
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if (response.body().getMessage().equals("Berhasil login")){
                        if (response.body().getUsers().get(0).getId().equals("0")){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_ID, response.body().getUsers().get(0).getId());
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}