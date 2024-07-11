package com.example.wbs.features.pengguna.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.features.pengguna.repositories.PenggunaRepository;
import com.example.wbs.features.petugas.repositories.PetugasRepository;
import com.example.wbs.features.profile.models.UserModelProfile;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PenggunaViewModel extends ViewModel {
    private PenggunaRepository petugasRepository;
    @Inject

    public PenggunaViewModel(PenggunaRepository petugasRepository) {
        this.petugasRepository = petugasRepository;
    }

    public LiveData<ResponseApiModel<List<UserModelProfile>>> getData() {
        return petugasRepository.getPengguna();
    }

    public LiveData<ResponseApiModel> store(HashMap<String, Object> data) {
        return petugasRepository.store(data);
    }


}
