package com.example.wbs.features.pengaduan.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.models.UserModel;
import com.example.wbs.core.services.ApiService;
import com.example.wbs.features.pengaduan.model.PengaduanModel;
import com.example.wbs.utils.constants.Constants;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PartMap;

public class PengaduanRepository {
    private ApiService apiService;
    @Inject
    public PengaduanRepository(ApiService apiService) {
        this.apiService = apiService;
    }
    public LiveData<ResponseApiModel<List<PengaduanModel>>> getPengaduanUser(String id, String role) {
        MutableLiveData<ResponseApiModel<List<PengaduanModel>>> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.getPengaduanUser(id, role).enqueue(new Callback<ResponseApiModel<List<PengaduanModel>>>() {
            @Override
            public void onResponse(Call<ResponseApiModel<List<PengaduanModel>>> call, Response<ResponseApiModel<List<PengaduanModel>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(true, Constants.SUCCESS, response.body().getData()));

                }else  {
                    Gson gson = new Gson();
                    ResponseApiModel responseApiModel = gson.fromJson(response.errorBody().charStream(), ResponseApiModel.class);
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, responseApiModel.getMessage(), null));

                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel<List<PengaduanModel>>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, Constants.SERVER_ERROR, null));

            }
        });
        return responseApiModelMutableLiveData;
    }

    public LiveData<ResponseApiModel> storePengaduan(Map<String, RequestBody> map, MultipartBody.Part file, boolean isWithImage) {
        MutableLiveData<ResponseApiModel> responseApiModelMutableLiveData = new MutableLiveData<>();
       if (isWithImage) {
           apiService.storePengaduanWithImage(map, file).enqueue(new Callback<ResponseApiModel>() {
               @Override
               public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                   if (response.isSuccessful() ) {
                       responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(true, Constants.SUCCESS, null));

                   }else  {
                       Gson gson = new Gson();
                       ResponseApiModel responseApiModel = gson.fromJson(response.errorBody().charStream(), ResponseApiModel.class);
                       responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, responseApiModel.getMessage(), null));

                   }
               }

               @Override
               public void onFailure(Call<ResponseApiModel> call, Throwable t) {
                   Log.d("TAG", "onFailure: " + t.getMessage());
                   responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, Constants.SERVER_ERROR, null));

               }
           });
       }else {
           apiService.storePengaduanNoImage(map).enqueue(new Callback<ResponseApiModel>() {
               @Override
               public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                   if (response.isSuccessful() ) {
                       responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(true, Constants.SUCCESS, null));

                   }else  {
                       Gson gson = new Gson();
                       ResponseApiModel responseApiModel = gson.fromJson(response.errorBody().charStream(), ResponseApiModel.class);
                       responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, responseApiModel.getMessage(), null));

                   }
               }

               @Override
               public void onFailure(Call<ResponseApiModel> call, Throwable t) {
                   Log.d("TAG", "onFailure: " + t.getMessage());
                   responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, Constants.SERVER_ERROR, null));

               }
           });
       }
        return responseApiModelMutableLiveData;
    }

    public LiveData<ResponseApiModel> updatePengaduan(HashMap<String, Object>data) {
        MutableLiveData<ResponseApiModel> responseApiModelMutableLiveData = new MutableLiveData<>();
        apiService.adminUpdateStatus(data).enqueue(new Callback<ResponseApiModel>() {
            @Override
            public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                if (response.isSuccessful() ) {
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(true, Constants.SUCCESS, null));

                }else  {
                    Gson gson = new Gson();
                    ResponseApiModel responseApiModel = gson.fromJson(response.errorBody().charStream(), ResponseApiModel.class);
                    responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, responseApiModel.getMessage(), null));

                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                responseApiModelMutableLiveData.postValue(new ResponseApiModel<>(false, Constants.SERVER_ERROR, null));

            }
        });
        return responseApiModelMutableLiveData;
    }
}
