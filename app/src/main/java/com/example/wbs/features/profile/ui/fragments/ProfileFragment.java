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
import android.widget.AdapterView;
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
import com.example.wbs.features.SpinnerAdapter;
import com.example.wbs.features.auth.ui.activities.AuthActivity;
import com.example.wbs.features.pengaduan.model.PengaduanModel;
import com.example.wbs.features.pengaduan.ui.adapters.PengaduanAdapter;
import com.example.wbs.features.pengaduan.viewmodels.PengaduanViewModel;
import com.example.wbs.features.profile.models.UserModelProfile;
import com.example.wbs.features.profile.viewmodels.ProfileViewModel;
import com.example.wbs.utils.constants.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    private ProfileViewModel profileViewModel;
    private UserModelProfile userModelProfile;
    private String genderSelected;
    private List<String> listGender = new ArrayList<>();



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



        binding.btnLogOut.setOnClickListener(v -> {
            userService.logOut();
            startActivity(new Intent(requireContext(), AuthActivity.class));
            requireActivity().finish();
        });

        binding.btnSave.setOnClickListener(v ->formValidate());

        binding.spinnerJk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gender = listGender.get(position);
                if (gender.equals("Laki-laki")) {
                    genderSelected = "laki";
                }else {
                    genderSelected = "perempuan";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    private void formValidate() {
       if (sharedUserModel.getRole().equals("pengguna")) {
           String email = binding.etEmail.getText().toString();
           String telp = binding.etPhone.getText().toString();
           String fullName = binding.etName.getText().toString();
           String jabatan = binding.etJabatan.getText().toString();
           String username = binding.etUsername.getText().toString();


           if (sharedUserModel.getUser_id() == 0 || sharedUserModel.isLogin() != true) {
               startActivity(new Intent(requireContext(), AuthActivity.class));
               requireActivity().finish();
               showToast("Silahkan login terlebih dahulu");
               return;
           }

           if (email.isEmpty()) {
               binding.tilEmail.setError("Tidak boleh kosong");
               return;
           }

           if (telp.isEmpty()) {
               binding.tilPhone.setError("Tidak boleh kosong");
               return;
           }

           if (fullName.isEmpty()) {
               binding.tilName.setError("Tidak boleh kosong");
               return;
           }

           if (jabatan.isEmpty()) {
               binding.tilJabatan.setError("Tidak boleh kosong");
               return;
           }




           if (username.isEmpty()) {
               binding.tilUsername.setError("Tidak boleh kosong");
               return;
           }

           if (genderSelected.equals("")) {
               showToast("Silahkan pilih jenis kelamin");
               return;
           }

           HashMap<String, Object> data = new HashMap<>();



           data.put("email", email);
           data.put("nama", fullName);
           data.put("telp", telp);
           data.put("jabatan", jabatan);
           data.put("username", username);
           data.put("user_id", sharedUserModel.getUser_id());
           data.put("gender", genderSelected);
           data.put("role", sharedUserModel.getRole());

           if (!binding.etPassword.getText().toString().isEmpty()) {
               data.put("password", binding.etPassword.getText().toString());
           }

           updateProfile(data);

       }else {
           String fullName = binding.etNameAdmin.getText().toString();
           String telp = binding.etPhoneAdmin.getText().toString();
           String username = binding.etUsernameAdmin.getText().toString();

           if (fullName.isEmpty()) {
               binding.tilNameAdmin.setError("Tidak boleh kosong");
               return;
           }

           if (telp.isEmpty()) {
               binding.tilPhoneAdmin.setError("Tidak boleh kosong");
               return;
           }


           if (username.isEmpty()) {
               binding.tilUsernameAdmin.setError("Tidak boleh kosong");
               return;
           }

           HashMap<String, Object> data = new HashMap<>();
           data.put("nama", fullName);
           data.put("telp", telp);
           data.put("role", sharedUserModel.getRole());
           data.put("user_id", sharedUserModel.getUser_id());
           data.put("username", username);

           if (!binding.etPasswordAdmin.getText().toString().isEmpty()) {
               data.put("password", binding.etPasswordAdmin.getText().toString());
           }

           updateProfile(data);
       }




//
    }

    private void updateProfile(HashMap<String, Object> data) {
        Log.d("DATA", "updateProfile: " + data);
        binding.progresBar.setVisibility(View.VISIBLE);
        binding.btnSave.setVisibility(View.GONE);
        profileViewModel.updateProfile(data).observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
            @Override
            public void onChanged(ResponseApiModel responseApiModel) {
                binding.progresBar.setVisibility(View.GONE);
                binding.btnSave.setVisibility(View.VISIBLE);
                if (responseApiModel.getStatus()) {
                    showToast("Berhasil mengubah profil");

                    getProfile();

                }else {
                    showToast(responseApiModel.getMessage());
                }
            }
        });

    }





    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void init() {

        userService = new UserService();
        userService.initSharedPref(requireContext());
        sharedUserModel = userService.getUserInfo();
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        if (sharedUserModel.isLogin() == true) {
            initGender();
            getProfile();

        }else {
            binding.lrAccessDenied.setVisibility(View.VISIBLE);
            binding.btnLogOut.setVisibility(View.GONE);
        }

    }

    private void initGender() {
        listGender.add("Laki-laki");
        listGender.add("Perempuan");
        SpinnerAdapter<String> spinnerAdapter = new SpinnerAdapter<>(requireActivity(), listGender);
        binding.spinnerJk.setAdapter(spinnerAdapter);
    }

    private void getProfile() {
        binding.progressBarMain.setVisibility(View.VISIBLE);
        binding.nestedScroll.setVisibility(View.GONE);
        HashMap<String,Object> data = new HashMap<>();
        data.put("user_id",sharedUserModel.getUser_id());
        data.put("role", sharedUserModel.getRole());
        profileViewModel.getProfile(data)
                .observe(getViewLifecycleOwner(), new Observer<ResponseApiModel<UserModelProfile>>() {
                    @Override
                    public void onChanged(ResponseApiModel<UserModelProfile> userModelProfileResponseApiModel) {
                        binding.progressBarMain.setVisibility(View.GONE);
                        binding.nestedScroll.setVisibility(View.VISIBLE);
                        if (userModelProfileResponseApiModel.getStatus()) {
                            userModelProfile = userModelProfileResponseApiModel.getData();
                            Log.d("DATA", "onChanged: " + userModelProfileResponseApiModel.getData().getNama());
                            setData();
                        }else {
                            showToast("Gagal mengambil data");
                            userModelProfile = null;
                        }
                    }
                });
    }

    private void setData() {
        if (sharedUserModel.getRole().equals("pengguna")) {
            binding.etEmail.setText(userModelProfile.getEmail());
            binding.etPhone.setText(userModelProfile.getTelp());
            binding.etName.setText(userModelProfile.getNama());
            binding.etJabatan.setText(userModelProfile.getJabatan());
            binding.etTglLahir.setText(userModelProfile.getTgl_lahir());
            binding.etUsername.setText(userModelProfile.getUsername());

            if (userModelProfile.getGender().equals("laki")) {
                binding.spinnerJk.setSelection(0);
            }else {
                binding.spinnerJk.setSelection(1);
            }
            binding.lrPengguna.setVisibility(View.VISIBLE);
            binding.lrPetugas.setVisibility(View.GONE);
        }else {

            binding.etNameAdmin.setText(userModelProfile.getNama_petugas());
            binding.etPhoneAdmin.setText(userModelProfile.getTelp());
            binding.etUsernameAdmin.setText(userModelProfile.getUsername());
            binding.lrPengguna.setVisibility(View.GONE);
            binding.lrPetugas.setVisibility(View.VISIBLE);
        }

    }
}