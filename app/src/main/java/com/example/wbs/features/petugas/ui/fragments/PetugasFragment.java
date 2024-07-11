package com.example.wbs.features.petugas.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.wbs.R;
import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.databinding.FragmentPengaduanBinding;
import com.example.wbs.databinding.FragmentPetugasBinding;
import com.example.wbs.features.SpinnerAdapter;
import com.example.wbs.features.kriteria.model.KriteriaModel;
import com.example.wbs.features.kriteria.viewmodel.KriteriaViewModel;
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
public class PetugasFragment extends Fragment implements OnClickListener {
    private FragmentPetugasBinding binding;
    private UserModelProfile userModelProfile;
    private PetugasViewModel petugasViewModel;
    private KriteriaViewModel kriteriaViewModel;
    private boolean isEdit = false;
    private int kriteriaSelected = 0;
    private String levelSelected = "";
    private List<String> levelList =  new ArrayList<>();

    private PetugasAdapter petugasAdapter;
    private BottomSheetBehavior bottomSheetStore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPetugasBinding.inflate(inflater, container, false);
        init();
        listener();



        return binding.getRoot();
    }

    private void getData() {
        binding.pb.setVisibility(View.VISIBLE);
        binding.rv.setVisibility(View.GONE);
        binding.rv.setAdapter(null);
        petugasViewModel.getData().observe(getViewLifecycleOwner(), new Observer<ResponseApiModel<List<UserModelProfile>>>() {
            @Override
            public void onChanged(ResponseApiModel<List<UserModelProfile>> listResponseApiModel) {
                binding.pb.setVisibility(View.GONE);
                binding.rv.setVisibility(View.VISIBLE);
                if (listResponseApiModel.getStatus()) {
                    petugasAdapter = new PetugasAdapter(requireContext(), listResponseApiModel.getData());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rv.setAdapter(petugasAdapter);
                    binding.rv.setLayoutManager(linearLayoutManager);
                    petugasAdapter.setOnClickListener(PetugasFragment.this);
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

        binding.btnDestroy.setOnClickListener(v -> destroy());

        binding.btnStoreCart.setOnClickListener(v -> formValidate());
    }

    private void destroy() {
        binding.progresBarDestroy.setVisibility(View.VISIBLE);
        binding.btnDestroy.setVisibility(View.GONE);
        petugasViewModel.destroy(userModelProfile.getId_petugas())
                .observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
                    @Override
                    public void onChanged(ResponseApiModel responseApiModel) {
                        binding.progresBarDestroy.setVisibility(View.GONE);
                        binding.btnDestroy.setVisibility(View.VISIBLE);
                        if (responseApiModel.getStatus()) {
                            showToast("Berhasil menghapus data");
                            hideBottomSheet();
                            getData();
                        } else {
                            showToast(responseApiModel.getMessage());
                        }
                    }
                });
    }

    private void formValidate() {
        if (isEdit && userModelProfile.getId_petugas() == 0) {
            showToast("Data tidak ditemukan");
            return;
        }



        if (binding.etNameAdmin.getText().toString().isEmpty()) {
            binding.tilNameAdmin.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etUsernameAdmin.getText().toString().isEmpty()) {
            binding.tilUsernameAdmin.setError("Tidak boleh kosong");
            return;

        }

        if (binding.etPasswordAdmin.getText().toString().isEmpty()) {
            binding.tilPasswordAdmin.setError("Tidak boleh kosong");
            return;
        }

        if (kriteriaSelected == 0) {
            showToast("Kriteria tidak boleh kosong");
            return;

        }

        if (binding.etPhoneAdmin.getText().toString().isEmpty()) {
            binding.tilPhoneAdmin.setError("Tidak boleh kosong");
            return;
        }

        if (levelSelected == null) {
            showToast("Level tidak boleh kosong");
            return;
        }

        HashMap<String, Object> data = new HashMap<>();

        data.put("nama_petugas", binding.etNameAdmin.getText().toString());
        data.put("username", binding.etUsernameAdmin.getText().toString());
        data.put("password", binding.etPasswordAdmin.getText().toString());
        data.put("id_kriteria", kriteriaSelected);
        data.put("telp", binding.etPhoneAdmin.getText().toString());
        data.put("level", levelSelected);

        if (isEdit) {
            data.put("id_petugas", userModelProfile.getId_petugas());
        }

        storeEdit(data);




    }

    private void storeEdit(HashMap<String, Object> data) {

        Log.d("TAG", "storeEdit: " + data.toString());
        binding.progresBarStore.setVisibility(View.VISIBLE);
        binding.btnStoreCart.setVisibility(View.GONE);
        if (isEdit) {
            petugasViewModel.update(data).observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
                @Override
                public void onChanged(ResponseApiModel responseApiModel) {
                    binding.progresBarStore.setVisibility(View.GONE);
                    binding.btnStoreCart.setVisibility(View.VISIBLE);
                    if (responseApiModel.getStatus()) {
                        showToast("Berhasil mengubah data");
                        hideBottomSheet();
                        getData();
                    } else {
                        showToast(responseApiModel.getMessage());
                    }
                }
            });
        }else {
            petugasViewModel.store(data).observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
                @Override
                public void onChanged(ResponseApiModel responseApiModel) {
                    binding.progresBarStore.setVisibility(View.GONE);
                    binding.btnStoreCart.setVisibility(View.VISIBLE);
                    if (responseApiModel.getStatus()) {
                        showToast("Berhasil menambah data");

                        showToast(responseApiModel.getMessage());
                        hideBottomSheet();
                        getData();
                    } else {
                        showToast(responseApiModel.getMessage());
                    }
                }
            });
        }
    }


    private void init() {
        petugasViewModel = new ViewModelProvider(this).get(PetugasViewModel.class);
        kriteriaViewModel = new ViewModelProvider(this).get(KriteriaViewModel.class);
        initBottomSheet();
        initSpinnerLevel();

        getData();
        getKriteria();
    }

    private void initSpinnerLevel() {
        levelList.add("admin");
        levelList.add("petugas");
        SpinnerAdapter<String> adapter = new SpinnerAdapter<>(requireContext(), levelList);
        binding.spinnerLevel.setAdapter(adapter);
        binding.spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                levelSelected = levelList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                levelSelected = null;

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

    private void showBottomSheet() {

        binding.vOverlay.setVisibility(View.VISIBLE);

        bottomSheetStore.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void getKriteria() {
        kriteriaViewModel.getKriteria().observe(getViewLifecycleOwner(), new Observer<ResponseApiModel<List<KriteriaModel>>>() {
            @Override
            public void onChanged(ResponseApiModel<List<KriteriaModel>> listResponseApiModel) {
                if (listResponseApiModel.getStatus() && listResponseApiModel.getData() != null) {
                    SpinnerAdapter<KriteriaModel> spinnerAdapter = new SpinnerAdapter<>(requireContext(), listResponseApiModel.getData());
                    binding.spinnerKriteria.setAdapter(spinnerAdapter);

                    binding.spinnerKriteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            kriteriaSelected = listResponseApiModel.getData().get(position).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            kriteriaSelected = 0;

                        }
                    });
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

        levelSelected = null;
        kriteriaSelected = 0;

        binding.etPhoneAdmin.setText("");
        binding.etPasswordAdmin.setText("");
        binding.etUsernameAdmin.setText("");
        binding.etNameAdmin.setText("");
        binding.tvState.setText("Tambah Data Petugas");
        binding.rlDestroy.setVisibility(View.GONE);
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
        binding.etNameAdmin.setText(userModelProfile.getNama_petugas());
        binding.etUsernameAdmin.setText(userModelProfile.getUsername());
        binding.etPhoneAdmin.setText(userModelProfile.getTelp());
        kriteriaSelected = userModelProfile.getId_kriteria();
        levelSelected = userModelProfile.getLevel();
        binding.tvState.setText("Ubah Data Petugas");
        binding.rlDestroy.setVisibility(View.VISIBLE);

        showBottomSheet();
    }
}