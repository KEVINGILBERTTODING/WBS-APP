package com.example.wbs.features.auth.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.models.UserModel;
import com.example.wbs.features.auth.repositories.AuthRepository;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;
    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<ResponseApiModel<UserModel>> login(HashMap<String, Object> data) {
        return authRepository.login(data);
    }
}
