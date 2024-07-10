package com.example.wbs.features.profile.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.services.ApiService;
import com.example.wbs.features.profile.models.UserModelProfile;
import com.example.wbs.utils.constants.Constants;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private ApiService apiService;
    @Inject
    public ProfileRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseApiModel<UserModelProfile>> getProfile(HashMap<String, Object> data) {
        MutableLiveData<ResponseApiModel<UserModelProfile>> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.getProfile(data).enqueue(new Callback<ResponseApiModel<UserModelProfile>>() {
            @Override
            public void onResponse(Call<ResponseApiModel<UserModelProfile>> call, Response<ResponseApiModel<UserModelProfile>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(true, Constants.SUCCESS, response.body().getData()));
                }else {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, Constants.SOMETHING_WENT_WRONG, null));

                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel<UserModelProfile>> call, Throwable t) {
                responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, Constants.SERVER_ERROR, null));


            }
        });
        return responseApiModelMutableLiveData;
    }

    public LiveData<ResponseApiModel> updateProfile(HashMap<String, Object> data) {
        MutableLiveData<ResponseApiModel> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.updateProfile(data).enqueue(new Callback<ResponseApiModel>() {
            @Override
            public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                if (response.isSuccessful()) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel(true, Constants.SUCCESS, null));
                }else {
                    Log.d("ON RESPONSE", "onResponse: " + response);
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
