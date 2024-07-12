package com.example.wbs.features.kriteria.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.databinding.FragmentKriteriaBinding;
import com.example.wbs.databinding.FragmentPetugasBinding;
import com.example.wbs.features.SpinnerAdapter;
import com.example.wbs.features.kriteria.model.KriteriaModel;
import com.example.wbs.features.kriteria.ui.adapters.KriteriaAdapter;
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
public class KriteriaFragment extends Fragment implements OnClickListener {
    private FragmentKriteriaBinding binding;
    private PetugasViewModel petugasViewModel;
    private KriteriaAdapter kriteriaAdapter;
    private KriteriaViewModel kriteriaViewModel;
    private boolean isEdit = false;
    private int kriteriaSelected = 0;
    private KriteriaModel kriteriaModel;
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
        binding = FragmentKriteriaBinding.inflate(inflater, container, false);
        init();
        listener();



        return binding.getRoot();
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void listener() {
        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
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
        kriteriaViewModel.destroy(kriteriaModel.getId())
                .observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
                    @Override
                    public void onChanged(ResponseApiModel responseApiModel) {
                        binding.progresBarDestroy.setVisibility(View.GONE);
                        binding.btnDestroy.setVisibility(View.VISIBLE);
                        if (responseApiModel.getStatus()) {
                            showToast("Berhasil menghapus data");
                            hideBottomSheet();
                            getKriteria();
                        } else {
                            showToast(responseApiModel.getMessage());
                        }
                    }
                });
    }

    private void formValidate() {


        if (binding.etNameKriteria.getText().toString().isEmpty()) {
            binding.tilNameKriteria.setError("Tidak boleh kosong");
            return;
        }





        if (isEdit) {
            if (kriteriaModel == null && kriteriaModel.getId() == 0) {
                showToast("Kriteria tidak valid");
                return;
            }
        }



        HashMap<String, Object> data = new HashMap<>();

        data.put("nama_kriteria", binding.etNameKriteria.getText().toString());


        if (isEdit) {
            data.put("id_kriteria", kriteriaModel.getId());
            data.put("isEdit", 1);
        }else {
            data.put("isEdit", 0);

        }

        storeEdit(data);




    }

    private void storeEdit(HashMap<String, Object> data) {

        Log.d("TAG", "storeEdit: " + data.toString());
        binding.progresBarStore.setVisibility(View.VISIBLE);
        binding.btnStoreCart.setVisibility(View.GONE);

            kriteriaViewModel.storeUpdate(data).observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
                @Override
                public void onChanged(ResponseApiModel responseApiModel) {
                    binding.progresBarStore.setVisibility(View.GONE);
                    binding.btnStoreCart.setVisibility(View.VISIBLE);
                    if (responseApiModel.getStatus()) {
                        if (isEdit) {
                            showToast("Berhasil mengubah data");
                        } else {
                            showToast("Berhasil menambah data");
                        }
                        hideBottomSheet();
                        getKriteria();
                    } else {
                        showToast(responseApiModel.getMessage());
                    }
                }
            });

    }


    private void init() {
        petugasViewModel = new ViewModelProvider(this).get(PetugasViewModel.class);
        kriteriaViewModel = new ViewModelProvider(this).get(KriteriaViewModel.class);
        initBottomSheet();


        getKriteria();
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
        binding.pb.setVisibility(View.VISIBLE);
        binding.rv.setAdapter(null);
        binding.rv.setVisibility(View.GONE);
        binding.lrEmpty.setVisibility(View.GONE);
        kriteriaViewModel.getKriteria().observe(getViewLifecycleOwner(), new Observer<ResponseApiModel<List<KriteriaModel>>>() {
            @Override
            public void onChanged(ResponseApiModel<List<KriteriaModel>> listResponseApiModel) {
                binding.pb.setVisibility(View.GONE);
                binding.rv.setVisibility(View.VISIBLE);
                if (listResponseApiModel.getStatus() && listResponseApiModel.getData() != null
                && listResponseApiModel.getData().size() > 0) {
                    kriteriaAdapter = new KriteriaAdapter(requireContext(), listResponseApiModel.getData());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rv.setAdapter(kriteriaAdapter);
                    binding.rv.setLayoutManager(linearLayoutManager);
                    kriteriaAdapter.setOnClickListener(KriteriaFragment.this);


                }else {
                    binding.lrEmpty.setVisibility(View.VISIBLE);
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

        binding.etNameKriteria.setText("");

        binding.tvState.setText("Tambah Data Petugas");
        binding.rlDestroy.setVisibility(View.GONE);
        kriteriaModel = null;
    }

    @Override
    public void setOnClickListener(int position, String action, Object object) {

        if (object instanceof KriteriaModel) {
            kriteriaModel = (KriteriaModel) object;
            isEdit = true;
            setData();

        }

    }

    private void setData() {
        binding.etNameKriteria.setText(kriteriaModel.getNama_kriteria());
        binding.tvState.setText("Ubah Data Kriteria");
        binding.rlDestroy.setVisibility(View.VISIBLE);

        showBottomSheet();
    }
}