package com.example.wbs.features.pengguna.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.databinding.FragmentPenggunaBinding;
import com.example.wbs.databinding.FragmentPetugasBinding;
import com.example.wbs.features.SpinnerAdapter;
import com.example.wbs.features.auth.ui.activities.AuthActivity;
import com.example.wbs.features.kriteria.model.KriteriaModel;
import com.example.wbs.features.kriteria.viewmodel.KriteriaViewModel;
import com.example.wbs.features.pengguna.ui.adapters.PenggunaAdapter;
import com.example.wbs.features.pengguna.viewmodels.PenggunaViewModel;
import com.example.wbs.features.petugas.ui.adapters.PetugasAdapter;
import com.example.wbs.features.petugas.viewmodels.PetugasViewModel;
import com.example.wbs.features.profile.models.UserModelProfile;
import com.example.wbs.utils.listener.OnClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PenggunaFragment extends Fragment implements OnClickListener {
    private FragmentPenggunaBinding binding;
    private UserModelProfile userModelProfile;
    private PenggunaViewModel penggunaViewModel;
    private boolean isEdit = false;
    private List<String> levelList =  new ArrayList<>();

    private PenggunaAdapter penggunaAdapter;
    private String jenisKelaminSelected ;
    private List<String> jkList = new ArrayList<>();
    private BottomSheetBehavior bottomSheetStore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPenggunaBinding.inflate(inflater, container, false);
        init();
        listener();



        return binding.getRoot();
    }

    private void getData() {
        binding.pb.setVisibility(View.VISIBLE);
        binding.rv.setVisibility(View.GONE);
        binding.rv.setAdapter(null);
       penggunaViewModel.getData().observe(getViewLifecycleOwner(), new Observer<ResponseApiModel<List<UserModelProfile>>>() {
            @Override
            public void onChanged(ResponseApiModel<List<UserModelProfile>> listResponseApiModel) {
                binding.pb.setVisibility(View.GONE);
                binding.rv.setVisibility(View.VISIBLE);
                if (listResponseApiModel.getStatus()) {
                    penggunaAdapter = new PenggunaAdapter(requireContext(), listResponseApiModel.getData());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rv.setAdapter(penggunaAdapter);
                    binding.rv.setLayoutManager(linearLayoutManager);
                    penggunaAdapter.setOnClickListener(PenggunaFragment.this);
                }else {
                    showToast(listResponseApiModel.getMessage());
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void listener() {
        binding.vOverlay.setOnClickListener(v -> hideBottomSheet());
        binding.fabAdd.setOnClickListener(v -> {
            isEdit = false;
            showBottomSheet();
        });

        binding.etTglLahir.setOnClickListener(v -> getDatePicker(binding.etTglLahir));

        binding.btnStoreCart.setOnClickListener(v -> formValidate());
    }



    private void init() {
        penggunaViewModel = new ViewModelProvider(this).get(PenggunaViewModel.class);
        initBottomSheet();
        initJkSpinner();
        getData();
    }

    private void initJkSpinner() {
        jkList.add("Laki-laki");
        jkList.add("Perempuan");
        SpinnerAdapter<String> adapter = new SpinnerAdapter<>(requireContext(), jkList);
        binding.spinnerJk.setAdapter(adapter);

        binding.spinnerJk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = jkList.get(position);
                if (selected.equals("Laki-laki")) {
                    jenisKelaminSelected = "laki";
                }else {
                    jenisKelaminSelected = "perempuan";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initBottomSheet() {
        setUpBottomSheet();
        bottomSheetStore.setState(BottomSheetBehavior.STATE_HIDDEN);
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

    private void getDatePicker(EditText tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d",dayOfMonth);
                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);

            }
        });

        datePickerDialog.show();
    }


    private void showBottomSheet() {

        binding.vOverlay.setVisibility(View.VISIBLE);

        bottomSheetStore.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void formValidate() {
            String email = binding.etEmail.getText().toString();
            String telp = binding.etPhone.getText().toString();
            String fullName = binding.etName.getText().toString();
            String jabatan = binding.etJabatan.getText().toString();
            String tglLahir = binding.etTglLahir.getText().toString();
            String username = binding.etUsername.getText().toString();




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

            if (tglLahir.isEmpty()) {
                binding.tilTglLahir.setError("Tidak boleh kosong");
                return;
            }




            if (username.isEmpty()) {
                binding.tilUsername.setError("Tidak boleh kosong");
                return;
            }

            if (jenisKelaminSelected.equals("")) {
                showToast("Silahkan pilih jenis kelamin");
                return;
            }

            HashMap<String, Object> data = new HashMap<>();

            data.put("email", email);
            data.put("nama", fullName);
            data.put("telp", telp);
            data.put("jabatan", jabatan);
            data.put("username", username);
            data.put("gender", jenisKelaminSelected);
            data.put("tgl_lahir", tglLahir);

            if (!binding.etPassword.getText().toString().isEmpty()) {
                data.put("password", binding.etPassword.getText().toString());
            }

            store(data);

        }

    private void store(HashMap<String, Object> data) {
        Log.d("DATA", "updateProfile: " + data);
        binding.progresBarStore.setVisibility(View.VISIBLE);
        binding.btnStoreCart.setVisibility(View.GONE);
        penggunaViewModel.store(data).observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
            @Override
            public void onChanged(ResponseApiModel responseApiModel) {
                binding.progresBarStore.setVisibility(View.GONE);
                binding.btnStoreCart.setVisibility(View.VISIBLE);
                if (responseApiModel.getStatus()) {
                    showToast("Berhasil menyimpan data");
                    hideBottomSheet();

                    getData();

                }else {
                    showToast(responseApiModel.getMessage());
                }
            }
        });

    }





    private void hideBottomSheet() {
        bottomSheetStore.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
        clearInputCart();
    }

    private void clearInputCart() {
        binding.etName.setText("");
        binding.etJabatan.setText("");
        binding.etTglLahir.setText("");
        binding.etUsername.setText("");
        binding.spinnerJk.setSelection(0);
        binding.etPassword.setText("");
        jenisKelaminSelected = null;
        binding.btnStoreCart.setVisibility(View.VISIBLE);



    }

    @Override
    public void setOnClickListener(int position, String action, Object object) {

        if (object instanceof UserModelProfile) {
            userModelProfile = (UserModelProfile) object;
            isEdit = true;
            setData();

        }

    }

    private void setData() {
        binding.etName.setText(userModelProfile.getNama());
        binding.etJabatan.setText(userModelProfile.getJabatan());
        binding.etTglLahir.setText(userModelProfile.getTgl_lahir());
        binding.etUsername.setText(userModelProfile.getUsername());

        if (userModelProfile.getGender().equals("laki")) {
            binding.spinnerJk.setSelection(0);
        }else {
            binding.spinnerJk.setSelection(1);
        }
        binding.etEmail.setText(userModelProfile.getEmail());
        binding.etPhone.setText(userModelProfile.getTelp());
        binding.tvState.setText("Detail Pengguna");
        binding.btnStoreCart.setVisibility(View.GONE);


        showBottomSheet();
    }
}