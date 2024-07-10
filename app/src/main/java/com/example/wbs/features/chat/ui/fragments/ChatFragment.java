package com.example.wbs.features.chat.ui.fragments;

import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wbs.R;
import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.models.SharedUserModel;
import com.example.wbs.core.services.ApiService;
import com.example.wbs.core.services.UserService;
import com.example.wbs.databinding.FragmentChatBinding;
import com.example.wbs.features.auth.ui.activities.AuthActivity;
import com.example.wbs.features.chat.models.ChatModel;
import com.example.wbs.features.chat.ui.adapters.ChatAdapter;
import com.example.wbs.features.chat.viewmodels.ChatViewModel;
import com.example.wbs.features.pengaduan.model.PengaduanModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends Fragment {
    private UserService userService;
    private SharedUserModel sharedUserModel;
    private ChatViewModel chatViewModel;
    private boolean isPengadu = false;
    private FragmentChatBinding binding;
    private PengaduanModel pengaduanModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null ) {
            pengaduanModel = getArguments().getParcelable("data");

        }else {
            sharedUserModel = null;
            showToast("Terjadi kesalahan");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);
        init();
        listener();


        return binding.getRoot();
    }

    private void init() {
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        if (pengaduanModel != null) {
            userService = new UserService();
            userService.initSharedPref(requireContext());
            sharedUserModel = userService.getUserInfo();
            if (sharedUserModel.getRole().equals("pengguna")) {
                binding.tvUsername.setText("Live chat");
                isPengadu = true;

            }else {
                binding.tvUsername.setText(pengaduanModel.getNama());
                isPengadu = false;
            }

            getPost();
        }

    }

    private void listener() {
        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack()
        );

        binding.btnSend.setOnClickListener(v -> formValidate());
    }

    private void formValidate() {
        if (userService.getUserInfo().getUser_id() == 0) {
            showToast("Silahkan login terlebih dahulu");
            startActivity(new Intent(requireActivity(), new AuthActivity().getClass()));
            requireActivity().finish();
        }

        if (binding.etIsiLaporan.getText().toString().isEmpty()) {
            binding.tilNrp.setError("Tidak boleh kosong");
            return;
        }

        if (isPengadu) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("user_id", sharedUserModel.getUser_id());
            data.put("id_pengaduan", pengaduanModel.getId_pengaduan());
            data.put("tanggapan", binding.etIsiLaporan.getText().toString());
            storeChat(data);
        }

        binding.btnSend.setOnClickListener(v -> formValidate());
    }


    private void getPost() {
        binding.nestedScroll.setVisibility(View.GONE);
        binding.progressBarMain.setVisibility(View.VISIBLE);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("chat");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<ChatModel> chatModel = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModel post = snapshot.getValue(ChatModel.class);



                    if (post.getId_pengaduan().equals(String.valueOf(pengaduanModel.getId_pengaduan()))) {
                        chatModel.add(post);

                    }

                }

                displayData(chatModel);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Terjadi kesalahan");
                // Handle jika terjadi kesalahan
                Log.e("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }
    private void displayData(List<ChatModel> chatModelList) {

        binding.nestedScroll.setVisibility(View.VISIBLE);
        binding.progressBarMain.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvChat.setLayoutManager(linearLayoutManager);
        ChatAdapter chatAdapter = new ChatAdapter(requireContext(), chatModelList, isPengadu);
        binding.rvChat.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();


        if (isPengadu) {
            binding.cvChatSender.setVisibility(View.VISIBLE);
            binding.tvIsiPesanSender.setText(pengaduanModel.getIsi_laporan());
            Glide.with(requireContext())
                    .load(ApiService.BASE_URL + "/storage/" + pengaduanModel.getFoto())
                    .into(binding.ivPengaduanSender);
            binding.tvDateSender.setText(pengaduanModel.getTgl_pengaduan());
        }else {
            binding.cvChat.setVisibility(View.VISIBLE);
            binding.tvIsiPesan.setText(pengaduanModel.getIsi_laporan());
            Glide.with(requireContext())
                    .load(ApiService.BASE_URL + "/storage/" + pengaduanModel.getFoto())
                    .into(binding.ivPengaduan);
            binding.tvDate.setText(pengaduanModel.getTgl_pengaduan());
        }


        binding.nestedScroll.post(new Runnable() {
            @Override
            public void run() {
                binding.nestedScroll.smoothScrollTo(0, binding.nestedScroll.getChildAt(0).getHeight());
            }
        });

    }



    private void storeChat(HashMap<String, Object> data) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.cvSend.setVisibility(View.GONE);
        chatViewModel.storeChat(isPengadu, data).observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
            @Override
            public void onChanged(ResponseApiModel responseApiModel) {
                binding.progressBar.setVisibility(View.GONE);
                binding.cvSend.setVisibility(View.VISIBLE);

                if (!responseApiModel.getStatus()) {
                    showToast(responseApiModel.getMessage());
                }else {
                    binding.etIsiLaporan.setText("");
                }
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}