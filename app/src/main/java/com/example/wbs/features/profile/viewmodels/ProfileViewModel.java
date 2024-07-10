package com.example.wbs.features.profile.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.features.profile.models.UserModelProfile;
import com.example.wbs.features.profile.repositories.ProfileRepository;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private ProfileRepository profileRepository;
    @Inject

    public ProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public LiveData<ResponseApiModel<UserModelProfile>> getProfile(HashMap<String, Object> data) {
        return profileRepository.getProfile(data);
    }

    public LiveData<ResponseApiModel> updateProfile(HashMap<String, Object> data) {
        return profileRepository.updateProfile(data);
    }
}
