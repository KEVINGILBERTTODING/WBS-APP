package com.example.wbs.features.dashboard.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wbs.R;
import com.example.wbs.core.models.SharedUserModel;
import com.example.wbs.core.services.UserService;
import com.example.wbs.databinding.FragmentDashboardBinding;
import com.example.wbs.features.auth.ui.activities.AuthActivity;


public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private SharedUserModel sharedUserModel;
    private UserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        init();

        listener();




       return binding.getRoot();
    }

    private void listener() {
        binding.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AuthActivity.class));
            requireActivity().finish();
        });
    }

    private void init() {
        userService = new UserService();
        userService.initSharedPref(requireContext());
        sharedUserModel = userService.getUserInfo();

        if (userService.getUserInfo().isLogin() == true ) {
            binding.btnLogin.setVisibility(View.GONE);

           binding.tvUsername.setText("Hi, " + userService.getUserInfo().getNama());

        }else {
            binding.btnLogin.setVisibility(View.VISIBLE);
        }
    }
}