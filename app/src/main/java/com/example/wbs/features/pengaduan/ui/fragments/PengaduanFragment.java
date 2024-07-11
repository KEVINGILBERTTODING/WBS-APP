package com.example.wbs.features.pengaduan.ui.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wbs.R;
import com.example.wbs.core.models.ResponseApiDownloadModel;
import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.models.SharedUserModel;
import com.example.wbs.core.services.UserService;
import com.example.wbs.databinding.FragmentPengaduanBinding;
import com.example.wbs.features.SpinnerAdapter;
import com.example.wbs.features.auth.ui.activities.AuthActivity;
import com.example.wbs.features.chat.ui.fragments.ChatFragment;
import com.example.wbs.features.kriteria.model.KriteriaModel;
import com.example.wbs.features.kriteria.viewmodel.KriteriaViewModel;
import com.example.wbs.features.pengaduan.model.PengaduanModel;
import com.example.wbs.features.pengaduan.ui.adapters.PengaduanAdapter;
import com.example.wbs.features.pengaduan.viewmodels.PengaduanViewModel;
import com.example.wbs.utils.constants.Constants;
import com.example.wbs.utils.listener.OnClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

@AndroidEntryPoint
public class PengaduanFragment extends Fragment implements OnClickListener {
    private FragmentPengaduanBinding binding;
    private PengaduanViewModel pengaduanViewModel;
    private UserService userService;
    private SharedUserModel sharedUserModel;
    private KriteriaViewModel kriteriaViewModel;
    private PengaduanAdapter pengaduanAdapter;
    private BottomSheetBehavior bottomSheetStore;
    private int kriteriaSelected =  0;
    private String dateFromState, dateEndState;
    private Uri fileUri;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPengaduanBinding.inflate(inflater, container, false);
        init();
        listener();



        return binding.getRoot();
    }

    private void getData() {
        binding.rv.setAdapter(null);
        binding.lrEmpty.setVisibility(View.GONE);
        binding.lrAccessDenied.setVisibility(View.GONE);
        binding.rv.setVisibility(View.GONE);
        binding.cvDownload.setVisibility(View.GONE);
        binding.pb.setVisibility(View.VISIBLE);
        HashMap<String, Object> data = new HashMap<>();
        data.put("user_id", sharedUserModel.getUser_id());
        data.put("role", sharedUserModel.getRole());
        data.put("kriteria_id", sharedUserModel.getId_kriteria());
        pengaduanViewModel.getPengaduanUser(data)
                .observe(getViewLifecycleOwner(), new Observer<ResponseApiModel<List<PengaduanModel>>>() {
                    @Override
                    public void onChanged(ResponseApiModel<List<PengaduanModel>> listResponseApiModel) {
                        binding.rv.setVisibility(View.VISIBLE);
                        binding.pb.setVisibility(View.GONE);
                        if (listResponseApiModel.getStatus()) {
                            pengaduanAdapter = new PengaduanAdapter(requireContext(), listResponseApiModel.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                            binding.rv.setLayoutManager(linearLayoutManager);
                            binding.rv.setAdapter(pengaduanAdapter);
                            binding.rv.setHasFixedSize(true);
                            pengaduanAdapter.setOnClickListener(PengaduanFragment.this);


                        }else {
                            showToast(Constants.SOMETHING_WENT_WRONG);
                        }
                    }
                });
    }


    private void filterData(HashMap<String, Object> data){
        binding.rv.setAdapter(null);
        binding.progresBarFilter.setVisibility(View.VISIBLE);
        binding.btnFilterSubmit.setVisibility(View.GONE);
        binding.lrEmpty.setVisibility(View.GONE);
        binding.lrAccessDenied.setVisibility(View.GONE);
        binding.cvDownload.setVisibility(View.GONE);
        binding.rv.setVisibility(View.GONE);
        binding.pb.setVisibility(View.VISIBLE);

        pengaduanViewModel.filterPengaduan(data)
                .observe(getViewLifecycleOwner(), new Observer<ResponseApiModel<List<PengaduanModel>>>() {
                    @Override
                    public void onChanged(ResponseApiModel<List<PengaduanModel>> listResponseApiModel) {
                        binding.rv.setVisibility(View.VISIBLE);
                        binding.pb.setVisibility(View.GONE);
                        binding.progresBarFilter.setVisibility(View.GONE);
                        binding.btnFilterSubmit.setVisibility(View.VISIBLE);
                        if (listResponseApiModel.getStatus() && listResponseApiModel.getData() != null
                        && listResponseApiModel.getData().size() > 0) {
                            pengaduanAdapter = new PengaduanAdapter(requireContext(), listResponseApiModel.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                            binding.rv.setLayoutManager(linearLayoutManager);
                            binding.rv.setAdapter(pengaduanAdapter);
                            binding.rv.setHasFixedSize(true);
                            pengaduanAdapter.setOnClickListener(PengaduanFragment.this);
                            binding.cvDownload.setVisibility(View.VISIBLE);
                            hideBottomSheet();


                        }else {
                            showToast("Tidak ada data yang ditemukan");
                            binding.cvDownload.setVisibility(View.GONE);
                            dateFromState = "";
                            dateEndState = "";
                        }
                    }
                });
    }

    private void listener() {
        binding.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AuthActivity.class));
            requireActivity().finish();
        });

        binding.vOverlay.setOnClickListener(v -> hideBottomSheet());
        binding.fabAdd.setOnClickListener(v -> {
            binding.lrStore.setVisibility(View.VISIBLE);
            binding.lrFilter.setVisibility(View.GONE);

            showBottomSheet();
        });

        binding.btnDownload.setOnClickListener(v -> {
            if (checkPermission()) {
                formValidateDownload();
            }else {
                requestPermission();
            }
        });

        binding.btnFilterSubmit.setOnClickListener(v -> formValidateFilter());

        binding.btnStoreCart.setOnClickListener(v -> formValidate());

        binding.btnFilter.setOnClickListener(v -> {
            binding.lrFilter.setVisibility(View.VISIBLE);
            binding.lrStore.setVisibility(View.GONE);
            showBottomSheet();
        });
        binding.etDateFrom.setOnClickListener(v -> getDatePicker(binding.etDateFrom));
        binding.etDateEnd.setOnClickListener(v -> getDatePicker(binding.etDateEnd));


        binding.btnImagePicker.setOnClickListener(v -> {
            if (checkPermission()) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

            }else {
                requestPermission();
            }
        });
    }

    private void formValidateDownload() {
        if (dateFromState ==  null ||dateFromState.equals("")) {
            showToast("Silahkan pilih tanggal awal");
            return;
        }

        if (dateEndState ==  null ||dateEndState.equals("")) {
            showToast("Silahkan pilih tanggal awal");
            return;
        }

        if (sharedUserModel.getId_kriteria() == 0) {
            showToast("Silahkan pilih kriteria terlebih dahulu");
            return;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("date_from", dateFromState);
        data.put("date_end", dateEndState);
        data.put("id_kriteria", sharedUserModel.getId_kriteria());

        downloadLaporan(data);
    }

    private void downloadLaporan(HashMap<String, Object> data) {
        binding.cvDownload.setVisibility(View.GONE);
        binding.progressBarDownload.setVisibility(View.VISIBLE);
        pengaduanViewModel.downloadPengaduan(data).observe(getViewLifecycleOwner(), new Observer<ResponseApiDownloadModel>() {
            @Override
            public void onChanged(ResponseApiDownloadModel responseBodyResponseApiModel) {
                binding.progressBarDownload.setVisibility(View.GONE);
                binding.cvDownload.setVisibility(View.VISIBLE);
                if (responseBodyResponseApiModel.getState().equals(Constants.SUCCESS)
                        && responseBodyResponseApiModel.getResponseBody() != null) {
                    try {
                        // check permisssion
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            String fileName = "laporan-pengaduan.pdf";
                            savefile(fileName, responseBodyResponseApiModel.getResponseBody().bytes());
                        } else {
                            showToast("Akses tidak diberikan");
                            requestPermission();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    public void savefile(String fileName, byte[] data) {
        // Mendapatkan direktori folder Download
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File file = new File(downloadDir, fileName);

        try {
            // Membuat FileOutputStream untuk menulis data PDF ke file
            FileOutputStream fos = new FileOutputStream(file);

            // Menulis data PDF ke file
            fos.write(data);

            // Tutup FileOutputStream setelah selesai menulis
            fos.close();
            openPdfFile(file);
            Toast.makeText(requireContext(), "Berhasil mengunduh file, silahkan cek folder download", Toast.LENGTH_LONG).show();





        } catch (IOException e) {
            // Tangani kesalahan saat menyimpan file
            e.printStackTrace();
            Log.d("savePdfToDownloadFolder", "savePdfToDownloadFolder: " + e.getMessage());
            Toast.makeText(requireContext(), "Gagal menyimpan PDF", Toast.LENGTH_SHORT).show();
        }
    }


    private void openPdfFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri uri = FileProvider.getUriForFile(requireContext(), "com.example.piatk.fileprovider", file);

        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            requireContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Tidak ada aplikasi untuk membuka gambar", Toast.LENGTH_SHORT).show();
        }
    }


    private void formValidateFilter() {
        String dateFrom = binding.etDateFrom.getText().toString();
        String dateEnd = binding.etDateEnd.getText().toString();

        if (dateFrom.isEmpty()) {
            binding.tilDateFrom.setError("Tidak boleh kosong");
            return;

        }

        if (dateEnd.isEmpty()) {
            binding.tilDateEnd.setError("Tidak boleh kosong");
            return;
        }

        if (sharedUserModel.getId_kriteria() == 0) {
            showToast("Silahkan pilih kriteria terlebih dahulu");
            return;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("from", dateFrom);
        data.put("to", dateEnd);
        data.put("id_kriteria", sharedUserModel.getId_kriteria());

        dateFromState = dateFrom;
        dateEndState = dateEnd;


        filterData(data);
    }

    private boolean checkPermission() {
        int resultRead = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int resultWrite = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return resultRead == PackageManager.PERMISSION_GRANTED && resultWrite == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                100);
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
        map.put("id_kriteria", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(kriteriaSelected)));

        if (fileUri != null) {
            RequestBody requestBody = RequestBody.create(null, contentResolver(fileUri));
            MultipartBody.Part file = MultipartBody.Part.createFormData("foto", getFileNameFromUri(fileUri), requestBody);
            store(map, file, true);
        }else {
            store(map, null, false);
        }
    }

    private void store(Map<String, RequestBody> map, MultipartBody.Part file, boolean b) {
        binding.progresBarStore.setVisibility(View.VISIBLE);
        binding.btnStoreCart.setVisibility(View.GONE);
        pengaduanViewModel.storePengaduan(map, file, b).observe(getViewLifecycleOwner(), new Observer<ResponseApiModel>() {
            @Override
            public void onChanged(ResponseApiModel responseApiModel) {
                binding.progresBarStore.setVisibility(View.GONE);
                binding.btnStoreCart.setVisibility(View.VISIBLE);
                if (responseApiModel.getStatus()) {
                    showToast("Berhasil mengirim laporan");

                    hideBottomSheet();
                    getData();

                }else {
                    showToast(responseApiModel.getMessage());
                }
            }
        });

    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

                if (uri != null) {
                    fileUri = uri;
                    binding.ivGambar.setVisibility(View.VISIBLE);
                    binding.ivGambar.setImageURI(uri);

                }
            });


    private String getFileNameFromUri(Uri uri) {
        String fileName = null;

        // Definisikan kolom yang ingin Anda baca (DISPLAY_NAME)
        String[] projection = { MediaStore.MediaColumns.DISPLAY_NAME };

        // Gunakan ContentResolver untuk melakukan query pada URI
        try (Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null)) {
            // Periksa apakah cursor tidak null dan ada data yang tersedia
            if (cursor != null && cursor.moveToFirst()) {
                // Dapatkan nilai dari kolom DISPLAY_NAME
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                fileName = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            // Tangani kesalahan jika terjadi
            Log.e("TAG", "Failed to get file name from URI", e);
        }

        return fileName;
    }

    public byte[] readAllBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        // Baca data dari InputStream ke dalam buffer
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }


        // Kembalikan byte array dari ByteArrayOutputStream
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] contentResolver(Uri uri) {
        byte[] imageData = null;
        InputStream inputStream = null;

        try {
            // Dapatkan ContentResolver dari context
            ContentResolver contentResolver = requireContext().getContentResolver();
            String filename = getFileNameFromUri(uri);

            // Baca gambar dari URI
            inputStream = contentResolver.openInputStream(uri);

            // Baca semua byte dari input stream
            imageData = readAllBytesFromInputStream(inputStream);
        } catch (IOException e) {
            // Log atau tangani kesalahan IO
            e.printStackTrace();

            return new byte[0];
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return imageData;
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
        binding.lrFilter.setVisibility(View.GONE);
        binding.lrStore.setVisibility(View.GONE);
        bottomSheetStore.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
        clearInputCart();
    }

    private void clearInputCart() {
        binding.etIsiLaporan.setText("");
        fileUri = null;
        binding.ivGambar.setVisibility(View.GONE);
        binding.etDateFrom.setText("");
        binding.etDateEnd.setText("");
        binding.ivGambar.setImageDrawable(null);

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

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void init() {
        pengaduanViewModel = new ViewModelProvider(this).get(PengaduanViewModel.class);
        kriteriaViewModel = new ViewModelProvider(this).get(KriteriaViewModel.class);
        userService = new UserService();
        userService.initSharedPref(requireContext());
        sharedUserModel = userService.getUserInfo();

        if (sharedUserModel.isLogin() == true) {
            if (!sharedUserModel.getRole().equals("pengguna")) {
                binding.fabAdd.setVisibility(View.GONE);
                binding.lrMenu.setVisibility(View.VISIBLE);
            }
            getData();
            getKriteria();
        }else {
            binding.lrAccessDenied.setVisibility(View.VISIBLE);
            binding.fabAdd.setVisibility(View.GONE);
        }
        setUpBottomSheet();
        bottomSheetStore.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void setOnClickListener(int position, String action, Object object) {
        if (object != null) {
            if (object instanceof PengaduanModel) {
                PengaduanModel pengaduanModel = (PengaduanModel) object;
                Fragment fragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", pengaduanModel);
                fragment.setArguments(bundle);
                fragmentTransaction(fragment);
            }
        }
    }

    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.frameDashboard, fragment)
                .addToBackStack(null).commit();
    }
}
