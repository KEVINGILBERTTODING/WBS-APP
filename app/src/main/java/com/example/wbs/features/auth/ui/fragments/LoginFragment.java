package com.example.wbs.features.auth.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wbs.R;
import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.models.SharedUserModel;
import com.example.wbs.core.models.UserModel;
import com.example.wbs.core.services.UserService;
import com.example.wbs.databinding.FragmentLoginBinding;
import com.example.wbs.features.auth.viewmodels.AuthViewModel;
import com.example.wbs.features.dashboard.ui.activities.MainDashboardActivity;
import com.example.wbs.utils.constants.Constants;

import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;
    private UserService userService;

  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        init();
        listener();
        
        
        return binding.getRoot();
    }

    private void listener() {
        binding.btnLogin.setOnClickListener(v -> {
            formValidate();
        });
    }

    private void formValidate() {
        if (binding.etUsername.getText().toString().isEmpty()){
            binding.tilNrp.setError("Username is required");
            return;
        }

        if (binding.etPassword.getText().toString().isEmpty()){
            binding.tilPassword.setError("Password is required");
            return;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("username", binding.etUsername.getText().toString());
        data.put("password", binding.etPassword.getText().toString());
        login(data);

    }

    private void login(HashMap<String, Object> data) {
        binding.btnLogin.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        authViewModel.login(data).observe(getViewLifecycleOwner(), new Observer<ResponseApiModel<UserModel>>() {
            @Override
            public void onChanged(ResponseApiModel<UserModel> userModelResponseApiModel) {
                binding.btnLogin.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

                if (userModelResponseApiModel.getStatus() && userModelResponseApiModel.getData() != null) {
                    showToast("Berhasil");
                    Log.d("TAG", "onChanged: " + userModelResponseApiModel.getData());
                    saveUserInfo(userModelResponseApiModel.getData());
                    startActivity(new Intent(requireActivity(), MainDashboardActivity.class));
                    requireActivity().finish();
                }else {
                    showToast(userModelResponseApiModel.getMessage());
                }
            }
        });

    }

    private void saveUserInfo(UserModel data) {
        SharedUserModel sharedUserModel = new SharedUserModel(
                data.getId(),
                data.getUsername(),
                data.getRole(),
                data.getNama(),
                true
        );
        userService.saveUser(sharedUserModel);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void init() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userService = new UserService();
        userService.initSharedPref(requireContext());
    }
}