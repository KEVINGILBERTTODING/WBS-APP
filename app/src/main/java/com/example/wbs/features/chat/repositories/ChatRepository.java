package com.example.wbs.features.chat.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.services.ApiService;
import com.example.wbs.utils.constants.Constants;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository {
    private ApiService apiService;
    @Inject
    public ChatRepository(ApiService apiService) {
        this.apiService = apiService;
    }
    public LiveData<ResponseApiModel> storeChat(boolean isPengadu, HashMap<String, Object> data) {
        MutableLiveData<ResponseApiModel> responseApiModelMutableLiveData = new MutableLiveData<>();
        if (isPengadu) {
            apiService.userStoreChat(data).enqueue(new Callback<ResponseApiModel>() {
                @Override
                public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                    if (response.isSuccessful()) {
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
        }
        return responseApiModelMutableLiveData;
    }

}
