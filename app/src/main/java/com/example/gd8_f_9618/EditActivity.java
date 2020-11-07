package com.example.gd8_f_9618;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private EditText etNama, etNim, etPassword;
    private AutoCompleteTextView exposedDropdownFakultas, exposedDropdownProdi;
    private RadioGroup rgJenisKelamin;
    private RadioButton p, l;
    private MaterialButton btnCancel, btnUpdate;
    private String sProdi="" , sFakultas="" , sJenisKelamin="", sIdUser, id;
    private String[] saProdi = new String[] {"Informatika", "Manajemen", "Ilmu Komunikasi", "Ilmu Hukum"};
    private String[] saFakultas = new String[] {"FTI", "FBE", "FISIP", "FH"};
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "userLogin";
    private static final String KEY_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        progressDialog = new ProgressDialog(this);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etNama = findViewById(R.id.etNama);
        etNim = findViewById(R.id.etNim);
        exposedDropdownProdi = findViewById(R.id.edProdi);
        exposedDropdownFakultas = findViewById(R.id.edFakultas);
        rgJenisKelamin = findViewById(R.id.rgJenisKelamin);
        etPassword = findViewById(R.id.etPassword);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        p = findViewById(R.id.rbPerempuan);
        l = findViewById(R.id.rbLakiLaki);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        id = sharedPreferences.getString(KEY_ID, null);

        Bundle extras = getIntent().getExtras();
        sIdUser = extras.getString("idx");

        loadUserById(sIdUser);

        ArrayAdapter<String> adapterProdi = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saProdi);
        exposedDropdownProdi.setAdapter(adapterProdi);
        exposedDropdownProdi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sProdi = saProdi[i];
            }
        });

        ArrayAdapter<String> adapterFakultas = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saFakultas);
        exposedDropdownFakultas.setAdapter(adapterFakultas);
        exposedDropdownFakultas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sFakultas = saFakultas[i];
            }
        });

        rgJenisKelamin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbLakiLaki:
                        sJenisKelamin = "Laki-Laki";
                        break;
                    case R.id.rbPerempuan:
                        sJenisKelamin = "Perempuan";
                        break;
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNama.getText().toString().isEmpty()){
                    etNama.setError("Isikan dengan benar");
                    etNama.requestFocus();
                } else if (sProdi.isEmpty()){
                    exposedDropdownProdi.setError("Isikan dengan benar", null);
                    exposedDropdownProdi.requestFocus();
                } else if (sFakultas.isEmpty()){
                    exposedDropdownFakultas.setError("Isikan dengan benar", null);
                    exposedDropdownFakultas.requestFocus();
                } else if (etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Isikan dengan benar");
                    etPassword.requestFocus();
                } else {
                    progressDialog.show();
                    updateUser(sIdUser);
                    if (id != null){
                        Intent intent = new Intent(EditActivity.this, ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        EditActivity.this.overridePendingTransition(0, 0);
                        EditActivity.this.finish();
                        EditActivity.this.overridePendingTransition(0, 0);
                        startActivity(intent);
                    }else  {
                        Intent intent = new Intent(EditActivity.this, ShowListUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        EditActivity.this.overridePendingTransition(0, 0);
                        EditActivity.this.finish();
                        EditActivity.this.overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void loadUserById(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> add = apiService.getUserById(id, "data");

        add.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                etNama.setText(response.body().getUsers().get(0).getNama());
                etNim.setText(response.body().getUsers().get(0).getNim());
                sFakultas = response.body().getUsers().get(0).getFakultas();
                exposedDropdownFakultas.setText(response.body().getUsers().get(0).getFakultas(), false);
                sProdi = response.body().getUsers().get(0).getProdi();
                exposedDropdownProdi.setText(response.body().getUsers().get(0).getProdi(), false);
                sJenisKelamin = response.body().getUsers().get(0).getJenis_kelamin();
                if (sJenisKelamin.equals("Laki-Laki")){
                    l.setChecked(true);
                }
                else {
                    p.setChecked(true);
                }
                etPassword.setText(response.body().getUsers().get(0).getPassword());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void updateUser(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> upd = apiService.updateUser(id, "data", etNama.getText().toString(),
                sProdi, sFakultas, sJenisKelamin, etPassword.getText().toString());

        upd.enqueue(new Callback<UserResponse>(){
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Toast.makeText(EditActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}