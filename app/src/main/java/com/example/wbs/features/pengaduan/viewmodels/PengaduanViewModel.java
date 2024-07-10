package com.example.wbs.features.pengaduan.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.features.pengaduan.model.PengaduanModel;
import com.example.wbs.features.pengaduan.repositories.PengaduanRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@HiltViewModel
public class PengaduanViewModel extends ViewModel {
    private  PengaduanRepository pengaduanRepository;

    @Inject

    public PengaduanViewModel(PengaduanRepository pengaduanRepository) {
        this.pengaduanRepository = pengaduanRepository;
    }




   public LiveData<ResponseApiModel<List<PengaduanModel>>> getPengaduanUser(String id, String role) {
        return pengaduanRepository.getPengaduanUser(id, role);
   }

   public LiveData<ResponseApiModel> storePengaduan(Map<String, RequestBody> map, MultipartBody.Part file, boolean isWithImage) {
        return pengaduanRepository.storePengaduan(map, file, isWithImage);
   }
}
