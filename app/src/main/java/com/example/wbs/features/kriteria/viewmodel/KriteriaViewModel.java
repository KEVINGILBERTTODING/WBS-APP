package com.example.wbs.features.kriteria.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.features.kriteria.model.KriteriaModel;
import com.example.wbs.features.kriteria.repositories.KriteriaRepository;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class KriteriaViewModel extends ViewModel {
    private KriteriaRepository kriteriaRepository;

    @Inject
    public KriteriaViewModel(KriteriaRepository kriteriaRepository) {
        this.kriteriaRepository = kriteriaRepository;
    }



    public LiveData<ResponseApiModel<List<KriteriaModel>>> getKriteria() {
        return kriteriaRepository.getKriteria();
    }

    public LiveData<ResponseApiModel> storeUpdate(HashMap<String, Object> data) {
        return kriteriaRepository.store(data);
    }
    public LiveData<ResponseApiModel> destroy(int id) {
        return kriteriaRepository.destroy(id);
    }

}
