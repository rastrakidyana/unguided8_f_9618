package com.example.gd8_f_9618;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView twNama, twNim, twFakultas, twProdi, twJenisKelamin;
    private String sIdUser, sNama, sNim, sFakultas, sProdi, sJenisKelamin;
    private MaterialButton btnLogout, btnEdit;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "userLogin";
    private static final String KEY_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressDialog = new ProgressDialog(this);

        twNama = findViewById(R.id.twNama);
        twNim = findViewById(R.id.twNim);
        twFakultas = findViewById(R.id.twFakultas);
        twProdi = findViewById(R.id.twProdi);
        twJenisKelamin = findViewById(R.id.twJenisKelamin);
        btnLogout = findViewById(R.id.btnLogout);
        btnEdit = findViewById(R.id.btnEdit);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        sIdUser = sharedPreferences.getString(KEY_ID, null);

        loadUserById(sIdUser);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser(sIdUser);
            }
        });

    }

    private void loadUserById(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> add = apiService.getUserById(id, "data");

        add.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                sNama = response.body().getUsers().get(0).getNama();
                sNim = response.body().getUsers().get(0).getNim();
                sFakultas = response.body().getUsers().get(0).getFakultas();
                sProdi = response.body().getUsers().get(0).getProdi();
                sJenisKelamin = response.body().getUsers().get(0).getJenis_kelamin();

                twNama.setText(sNama);
                twNim.setText(sNim);
                twFakultas.setText(sFakultas);
                twProdi.setText(sProdi);
                twJenisKelamin.setText(sJenisKelamin);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void editUser(String id){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> edt = apiService.getUserById(id, "data");

        edt.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                progressDialog.dismiss();
                Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
                intent.putExtra("idx", response.body().getUsers().get(0).getId());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}