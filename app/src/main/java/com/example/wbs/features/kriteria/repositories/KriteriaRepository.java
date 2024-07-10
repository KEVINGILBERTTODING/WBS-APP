package com.example.wbs.features.kriteria.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.services.ApiService;
import com.example.wbs.features.kriteria.model.KriteriaModel;
import com.example.wbs.utils.constants.Constants;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KriteriaRepository {
    private ApiService apiService;

    @Inject
    public KriteriaRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseApiModel<List<KriteriaModel>>> getKriteria() {
        MutableLiveData<ResponseApiModel<List<KriteriaModel>>> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.getKriteria().enqueue(new Callback<ResponseApiModel<List<KriteriaModel>>>() {
            @Override
            public void onResponse(Call<ResponseApiModel<List<KriteriaModel>>> call, Response<ResponseApiModel<List<KriteriaModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(true, Constants.SUCCESS, response.body().getData()));
                }else {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, Constants.SOMETHING_WENT_WRONG, null));

                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel<List<KriteriaModel>>> call, Throwable t) {
                responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, Constants.SERVER_ERROR, null));


            }
        });
        return responseApiModelMutableLiveData;
    }
}
