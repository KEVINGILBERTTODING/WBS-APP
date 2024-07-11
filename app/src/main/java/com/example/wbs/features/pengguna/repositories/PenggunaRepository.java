package com.example.wbs.features.pengguna.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.services.ApiService;
import com.example.wbs.features.profile.models.UserModelProfile;
import com.example.wbs.utils.constants.Constants;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenggunaRepository {
    private ApiService apiService;
    @Inject

    public PenggunaRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseApiModel<List<UserModelProfile>>> getPengguna() {
        MutableLiveData<ResponseApiModel<List<UserModelProfile>>> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.getPengguna().enqueue(new Callback<ResponseApiModel<List<UserModelProfile>>>() {
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
        apiService.storePengguna(data).enqueue(new Callback<ResponseApiModel>() {
            @Override
            public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(true, Constants.SUCCESS, null));
                }else {
                    Log.d("TAG", "onResponse: " + response);
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
