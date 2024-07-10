package com.example.wbs.features.kriteria.model;

import androidx.annotation.NonNull;

public class KriteriaModel {
    private int id;
    private String nama_kriteria;

    public KriteriaModel(int id, String nama_kriteria) {
        this.id = id;
        this.nama_kriteria = nama_kriteria;
    }

    @NonNull
    @Override
    public String toString() {
        return nama_kriteria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_kriteria() {
        return nama_kriteria;
    }

    public void setNama_kriteria(String nama_kriteria) {
        this.nama_kriteria = nama_kriteria;
    }
}
