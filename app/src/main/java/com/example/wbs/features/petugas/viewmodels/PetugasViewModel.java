package com.example.wbs.features.petugas.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.features.petugas.repositories.PetugasRepository;
import com.example.wbs.features.profile.models.UserModelProfile;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PetugasViewModel extends ViewModel {
    private PetugasRepository petugasRepository;
    @Inject

    public PetugasViewModel(PetugasRepository petugasRepository) {
        this.petugasRepository = petugasRepository;
    }

    public LiveData<ResponseApiModel<List<UserModelProfile>>> getData() {
        return petugasRepository.getPetugas();
    }

    public LiveData<ResponseApiModel> store(HashMap<String, Object> data) {
        return petugasRepository.store(data);
    }
    public LiveData<ResponseApiModel> update(HashMap<String, Object> data) {
        return petugasRepository.update(data);
    }
    public LiveData<ResponseApiModel> destroy(int petugasId) {
        return petugasRepository.destroy(petugasId);
    }


}
