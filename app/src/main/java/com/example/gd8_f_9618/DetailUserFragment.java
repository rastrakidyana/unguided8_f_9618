package com.example.gd8_f_9618;

import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailUserFragment extends DialogFragment {
    private TextView twNama, twNim, twFakultas, twProdi, twJenisKelamin;
    private String sIdUser, sNama, sNim, sFakultas, sProdi, sJenisKelamin;
    private ImageButton ibClose;
    private ProgressDialog progressDialog;
    private MaterialButton btnDelete, btnEdit;


    public static DetailUserFragment newInstance() {
        return new DetailUserFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_user, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();

        ibClose = v.findViewById(R.id.ibClose);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        twNama = v.findViewById(R.id.twNama);
        twNim = v.findViewById(R.id.twNim);
        twFakultas = v.findViewById(R.id.twFakultas);
        twProdi = v.findViewById(R.id.twProdi);
        twJenisKelamin = v.findViewById(R.id.twJenisKelamin);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnEdit = v.findViewById(R.id.btnEdit);

        sIdUser = getArguments().getString("id", "");
        loadUserById(sIdUser);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Konfirmasi")
                        .setMessage("Yakin ingin menghapus data ini ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser(sIdUser);
//                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                                transaction.hide(DetailUserFragment.this).commit();
                                Intent intent = getActivity().getIntent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                getActivity().overridePendingTransition(0, 0);
                                getActivity().finish();

                                getActivity().overridePendingTransition(0, 0);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser(sIdUser);
            }
        });

        return v;
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
                Toast.makeText(getContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void deleteUser(String id){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> del = apiService.deleteUser(id, "data");

        del.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Toast.makeText(getContext().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra("idx", response.body().getUsers().get(0).getId());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}