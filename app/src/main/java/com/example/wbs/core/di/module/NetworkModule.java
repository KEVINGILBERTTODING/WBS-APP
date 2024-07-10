package com.example.wbs.core.di.module;


import com.example.wbs.core.services.ApiService;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    @Provides
    @Named("api_provider")
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(ApiService.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Inject
    public ApiService apiService(@Named("api_provider") Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
