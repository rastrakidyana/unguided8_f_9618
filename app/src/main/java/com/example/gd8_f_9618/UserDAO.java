package com.example.gd8_f_9618;

import com.google.gson.annotations.SerializedName;

public class UserDAO {

    @SerializedName("id")
    private String id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("nim")
    private String nim;

    @SerializedName("prodi")
    private String prodi;

    @SerializedName("fakultas")
    private String fakultas;

    @SerializedName("jenis_kelamin")
    private String jenis_kelamin;

    @SerializedName("password")
    private String password;

    public UserDAO(String id, String nama, String nim, String prodi, String fakultas, String jenis_kelamin, String password) {
        this.id = id;
        this.nama = nama;
        this.nim = nim;
        this.prodi = prodi;
        this.fakultas = fakultas;
        this.jenis_kelamin = jenis_kelamin;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
