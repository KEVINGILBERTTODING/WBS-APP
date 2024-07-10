package com.example.wbs.features.chat.models;

public class ChatModel {

    private String id_pengaduan;
    private String tgl_tanggapan;
    private String tanggapan;
    private String id_petugas;
    private String id_pengguna;

    public ChatModel() {
    }

    public String getId_pengaduan() {
        return id_pengaduan;
    }

    public void setId_pengaduan(String id_pengaduan) {
        this.id_pengaduan = id_pengaduan;
    }

    public String getTgl_tanggapan() {
        return tgl_tanggapan;
    }

    public void setTgl_tanggapan(String tgl_tanggapan) {
        this.tgl_tanggapan = tgl_tanggapan;
    }

    public String getTanggapan() {
        return tanggapan;
    }

    public void setTanggapan(String tanggapan) {
        this.tanggapan = tanggapan;
    }

    public String getId_petugas() {
        return id_petugas;
    }

    public void setId_petugas(String id_petugas) {
        this.id_petugas = id_petugas;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
    }
}
