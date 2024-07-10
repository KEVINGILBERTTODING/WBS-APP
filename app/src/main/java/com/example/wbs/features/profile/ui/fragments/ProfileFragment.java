package com.example.wbs.features.profile.ui.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.models.SharedUserModel;
import com.example.wbs.core.services.UserService;
import com.example.wbs.databinding.FragmentPengaduanBinding;
import com.example.wbs.databinding.FragmentProfileBinding;
import com.example.wbs.features.auth.ui.activities.AuthActivity;
import com.example.wbs.features.pengaduan.model.PengaduanModel;
import com.example.wbs.features.pengaduan.ui.adapters.PengaduanAdapter;
import com.example.wbs.features.pengaduan.viewmodels.PengaduanViewModel;
import com.example.wbs.utils.constants.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserService userService;
    private SharedUserModel sharedUserModel;
    private BottomSheetBehavior bottomSheetStore;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        init();
        listener();



        return binding.getRoot();
    }


    private void listener() {
        binding.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AuthActivity.class));
            requireActivity().finish();
        });

        binding.vOverlay.setOnClickListener(v -> hideBottomSheet());


        binding.btnStoreCart.setOnClickListener(v -> formValidate());

        binding.btnLogOut.setOnClickListener(v -> {
            userService.logOut();
            startActivity(new Intent(requireContext(), AuthActivity.class));
            requireActivity().finish();
        });



    }


    private void formValidate() {
        if (binding.etIsiLaporan.getText().toString().isEmpty()) {
            binding.tilNrp.setError("Tidak boleh kosong");
            return;
        }

        if (sharedUserModel.getUser_id() == 0 || sharedUserModel.isLogin() != true) {
            startActivity(new Intent(requireContext(), AuthActivity.class));
            requireActivity().finish();
            showToast("Silahkan login terlebih dahulu");
            return;
        }

        Map<String, RequestBody> map =  new HashMap<>();
        map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sharedUserModel.getUser_id())));
        map.put("isi_laporan", RequestBody.create(MediaType.parse("text/plain"), binding.etIsiLaporan.getText().toString()));

//
    }

    private void store(Map<String, RequestBody> map, MultipartBody.Part file, boolean b) {
        binding.progresBarStore.setVisibility(View.VISIBLE);
        binding.btnStoreCart.setVisibility(View.GONE);
//        pengaduanViewModel.storePengaduan(map, file, b).observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
//            @Override
//            public void onChanged(ResponseApiModel responseApiModel) {
//                binding.progresBarStore.setVisibility(View.GONE);
//                binding.btnStoreCart.setVisibility(View.VISIBLE);
//                if (responseApiModel.getStatus()) {
//                    showToast("Berhasil mengirim laporan");
//
//                    hideBottomSheet();
//                    getData();
//
//                }else {
//                    showToast(responseApiModel.getMessage());
//                }
//            }
//        });

    }



    private void setUpBottomSheet() {


        bottomSheetStore = BottomSheetBehavior.from(binding.bottomSheetRating);
        bottomSheetStore.setHideable(true);

        bottomSheetStore.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheet();

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showBottomSheet() {

        binding.vOverlay.setVisibility(View.VISIBLE);

        bottomSheetStore.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheet() {
        bottomSheetStore.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
        clearInputCart();
    }

    private void clearInputCart() {
        binding.etIsiLaporan.setText("");


    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void init() {
//        pengaduanViewModel = new ViewModelProvider(this).get(PengaduanViewModel.class);
        userService = new UserService();
        userService.initSharedPref(requireContext());
        sharedUserModel = userService.getUserInfo();

        if (sharedUserModel.isLogin() == true) {

        }else {
            binding.lrAccessDenied.setVisibility(View.VISIBLE);
            binding.btnLogOut.setVisibility(View.GONE);
        }
        setUpBottomSheet();
        bottomSheetStore.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}