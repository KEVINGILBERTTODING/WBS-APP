package com.example.wbs.features.auth.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.models.UserModel;
import com.example.wbs.core.services.ApiService;
import com.example.wbs.utils.constants.Constants;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;
    @Inject

    public AuthRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseApiModel<UserModel>> login(HashMap<String, Object> data) {
        MutableLiveData<ResponseApiModel<UserModel>> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.login(data).enqueue(new Callback<ResponseApiModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseApiModel<UserModel>> call, Response<ResponseApiModel<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(true, Constants.SUCCESS, response.body().getData()));

                }else  {
                    Gson gson = new Gson();
                    ResponseApiModel responseApiModel = gson.fromJson(response.errorBody().charStream(), ResponseApiModel.class);
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, responseApiModel.getMessage(), null));

                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel<UserModel>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, Constants.SERVER_ERROR, null));

            }
        });
        return responseApiModelMutableLiveData;
    }
}
