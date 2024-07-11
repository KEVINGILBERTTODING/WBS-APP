package com.example.wbs.features.petugas.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.services.ApiService;
import com.example.wbs.features.profile.models.UserModelProfile;
import com.example.wbs.utils.constants.Constants;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetugasRepository {
    private ApiService apiService;
    @Inject

    public PetugasRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseApiModel<List<UserModelProfile>>> getPetugas() {
        MutableLiveData<ResponseApiModel<List<UserModelProfile>>> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.getPetugas().enqueue(new Callback<ResponseApiModel<List<UserModelProfile>>>() {
            @Override
            public void onResponse(Call<ResponseApiModel<List<UserModelProfile>>> call, Response<ResponseApiModel<List<UserModelProfile>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(true, Constants.SUCCESS, response.body().getData()));
                }else {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(false, Constants.SOMETHING_WENT_WRONG, null));

                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel<List<UserModelProfile>>> call, Throwable t) {
                responseApiModelMutableLiveData.postValue(new ResponseApiModel(false, Constants.SERVER_ERROR, null));


            }
        });
        return responseApiModelMutableLiveData;
    }

    public LiveData<ResponseApiModel> store(HashMap<String, Object> data) {
        MutableLiveData<ResponseApiModel> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.storePetugas(data).enqueue(new Callback<ResponseApiModel>() {
            @Override
            public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(true, Constants.SUCCESS, null));
                }else {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(false, Constants.SOMETHING_WENT_WRONG, null));

                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel> call, Throwable t) {
                responseApiModelMutableLiveData.postValue(new ResponseApiModel(false, Constants.SERVER_ERROR, null));


            }
        });
        return responseApiModelMutableLiveData;
    }

    public LiveData<ResponseApiModel> update(HashMap<String, Object> data) {
        MutableLiveData<ResponseApiModel> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.updatePetugas(data).enqueue(new Callback<ResponseApiModel>() {
            @Override
            public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(true, Constants.SUCCESS, null));
                }else {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(false, Constants.SOMETHING_WENT_WRONG, null));

                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel> call, Throwable t) {
                responseApiModelMutableLiveData.postValue(new ResponseApiModel(false, Constants.SERVER_ERROR, null));


            }
        });
        return responseApiModelMutableLiveData;
    }

    public LiveData<ResponseApiModel> destroy(int petugasId) {
        MutableLiveData<ResponseApiModel> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.destrotPetugas(petugasId).enqueue(new Callback<ResponseApiModel>() {
            @Override
            public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(true, Constants.SUCCESS, null));
                }else {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(false, Constants.SOMETHING_WENT_WRONG, null));

                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel> call, Throwable t) {
                responseApiModelMutableLiveData.postValue(new ResponseApiModel(false, Constants.SERVER_ERROR, null));


            }
        });
        return responseApiModelMutableLiveData;
    }
}
