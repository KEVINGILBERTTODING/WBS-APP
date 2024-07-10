package com.example.wbs.features.pengaduan.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import retrofit2.http.PartMap;

public class PengaduanModel implements Parcelable {

    private int id_pengaduan;
     private String tgl_pengaduan;
     private int id_pengguna;
     private String isi_laporan;
     private String foto;
     private String status;
     private String created_at;
     private String updated_at;
     private String kode_pengaduan;
     private String nama;

    public PengaduanModel(int id_pengaduan, String tgl_pengaduan, int id_pengguna, String isi_laporan, String foto, String status, String created_at, String updated_at, String kode_pengaduan, String nama) {
        this.id_pengaduan = id_pengaduan;
        this.tgl_pengaduan = tgl_pengaduan;
        this.id_pengguna = id_pengguna;
        this.isi_laporan = isi_laporan;
        this.foto = foto;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.kode_pengaduan = kode_pengaduan;
        this.nama = nama;
    }

    protected PengaduanModel(Parcel in) {
        id_pengaduan = in.readInt();
        tgl_pengaduan = in.readString();
        id_pengguna = in.readInt();
        isi_laporan = in.readString();
        foto = in.readString();
        status = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        kode_pengaduan = in.readString();
        nama = in.readString();
    }

    public int getId_pengaduan() {
        return id_pengaduan;
    }

    public void setId_pengaduan(int id_pengaduan) {
        this.id_pengaduan = id_pengaduan;
    }

    public String getTgl_pengaduan() {
        return tgl_pengaduan;
    }

    public void setTgl_pengaduan(String tgl_pengaduan) {
        this.tgl_pengaduan = tgl_pengaduan;
    }

    public int getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(int id_pengguna) {
        this.id_pengguna = id_pengguna;
    }

    public String getIsi_laporan() {
        return isi_laporan;
    }

    public void setIsi_laporan(String isi_laporan) {
        this.isi_laporan = isi_laporan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getKode_pengaduan() {
        return kode_pengaduan;
    }

    public void setKode_pengaduan(String kode_pengaduan) {
        this.kode_pengaduan = kode_pengaduan;
    }

    public String getNama() {
        return nama;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id_pengaduan);
        dest.writeString(tgl_pengaduan);
        dest.writeInt(id_pengguna);
        dest.writeString(isi_laporan);
        dest.writeString(foto);
        dest.writeString(status);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(kode_pengaduan);
        dest.writeString(nama);

    }
}
