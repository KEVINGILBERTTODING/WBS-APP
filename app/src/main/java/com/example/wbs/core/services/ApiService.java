package com.example.wbs.core.services;

import com.example.wbs.core.models.ResponseApiModel;
import com.example.wbs.core.models.UserModel;
import com.example.wbs.features.kriteria.model.KriteriaModel;
import com.example.wbs.features.pengaduan.model.PengaduanModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    public static final String IP_ADDRESS = "192.168.43.215"; // kantor
//
    public static final String END_POINT = "http://" + IP_ADDRESS + ":8000/api/";
    public static final String BASE_URL =  "http://" + IP_ADDRESS + ":8000";

    @FormUrlEncoded
    @POST("login")
    Call<ResponseApiModel<UserModel>> login(
            @FieldMap HashMap<String, Object> data);

    @GET("user/pengaduan")
    Call<ResponseApiModel<List<PengaduanModel>>> getPengaduanUser(
            @Query("user_id") String userId,
            @Query("role") String role
    );

    @Multipart
    @POST("user/pengaduan/store")
    Call<ResponseApiModel> storePengaduanWithImage(
            @PartMap Map<String, RequestBody> data,
            @Part MultipartBody.Part file

    );
    @Multipart
    @POST("user/pengaduan/store")
    Call<ResponseApiModel> storePengaduanNoImage(
            @PartMap Map<String, RequestBody> data

    );

    @FormUrlEncoded
    @POST("user/tanggapan/store")
    Call<ResponseApiModel> userStoreChat(
            @FieldMap HashMap<String, Object> data);

    @GET("user/kriteria")
    Call<ResponseApiModel<List<KriteriaModel>>> getKriteria();

//
//    @FormUrlEncoded
//    @POST("login")
//    Call<ResponseApiModel<UserModel>> login(
//            @FieldMap HashMap<String, String> map
//            );
//
//
//    @GET("superadmin/operator")
//    Call<ResponseApiModel<List<OperatorModel>>> getOperator();
//
//    @GET("superadmin/perangkatdaerah")
//    Call<ResponseApiModel<List<PerangkatDaerahModel>>> getPd();
//
//    @GET("superadmin/perangkatdaerahall")
//    Call<ResponseApiModel<List<PerangkatDaerahModel>>> getPdAll();
//    @FormUrlEncoded
//    @POST("superadmin/operator/store")
//    Call<ResponseApiModel> storeOperator(
//            @FieldMap HashMap<String, String> map
//    );
//
//    @FormUrlEncoded
//    @POST("superadmin/operator/update")
//    Call<ResponseApiModel> updateOperator(
//            @FieldMap HashMap<String, String> map
//    );
//
//
//
//    @DELETE("superadmin/operator/destroy/{id}")
//    Call<ResponseApiModel> destroyOperator(
//            @Path("id") int id
//    );
//
//    @GET("superadmin/indikator")
//    Call<ResponseApiModel<List<IndikatorModel>>> getAllIndikator();
//
//    @POST("superadmin/perangkatdaerah/store")
//    Call<ResponseApiModel> storePd(
//            @Body PdDataRequest pdDataRequest
//            );
//    @DELETE("superadmin/perangkatdaerah/destroy/{id}")
//    Call<ResponseApiModel> destroyPd(
//            @Path("id") int id
//    );
//
//    @GET("superadmin/bataswaktu")
//    Call<ResponseApiModel<List<BatasWaktuModel>>> getAllBatasWaktu();
//
//    @FormUrlEncoded
//    @POST("superadmin/bataswaktu/store")
//    Call<ResponseApiModel> storeBw(
//            @FieldMap HashMap<String, String> map
//    );
//
//    @DELETE("superadmin/bataswaktu/destroy/{id}")
//    Call<ResponseApiModel> destroyBw(
//            @Path("id") int id
//    );
//
//
//    @FormUrlEncoded
//    @POST("superadmin/bataswaktu/update")
//    Call<ResponseApiModel> updateBw(
//            @FieldMap HashMap<String, String> map
//    );
//
//    @GET("superadmin/pertanyaanumum")
//    Call<ResponseApiModel<PuModel>> getPertanyaanUmum();
//
//    @FormUrlEncoded
//    @POST("superadmin/pertanyaanumum/update")
//    Call<ResponseApiModel> updatePu(
//            @FieldMap HashMap<String, String> map
//    );
//    @GET("superadmin/aspek")
//    Call<ResponseApiModel<List<AspekModel>>> getAspek();
//
//    @FormUrlEncoded
//    @POST("superadmin/indikator/store")
//    Call<ResponseApiModel> storeIndikator(
//            @FieldMap HashMap<String, String> data
//    );
//
//    @FormUrlEncoded
//    @POST("superadmin/indikator/update")
//    Call<ResponseApiModel> updateIndikator(
//            @FieldMap HashMap<String, String> data
//    );
//
//    @DELETE("superadmin/indikator/destroy/{id}")
//    Call<ResponseApiModel> destroyIndikator(
//            @Path("id") int id
//    );
//
//    @GET("superadmin/profile/{id}")
//    Call<ResponseApiModel<UserModel>> profileSuperAdmin(
//            @Path("id") int id
//    );
//
//    @FormUrlEncoded
//    @POST("superadmin/profile/update/profile")
//    Call<ResponseApiModel> updateSuperAdminProfile(
//            @FieldMap HashMap<String, String> data
//    );
//
//    @FormUrlEncoded
//    @POST("superadmin/profile/update/password")
//    Call<ResponseApiModel> updateSuperAdminPw(
//            @FieldMap HashMap<String, String> data
//    );
//
//
//    @GET("main/{year}")
//    Call<ResponseApiModel<MainModel>> getData(
//            @Path("year") String year
//    );
//
//    @GET("main/download/{year}")
//    Call<ResponseBody> downloadReport(
//            @Path("year") String year
//    );
//
//    @GET("dashboard")
//    Call<ResponseApiModel<MainModel>> getDataDashboard(
//
//    );
//
//    @GET("operator/profile/{id}")
//    Call<ResponseApiModel<UserModel>> getOperatorById(
//            @Path("id") int id
//    );
//
//    @GET("operator/penilaian/checkstatus/{id}")
//    Call<ResponseApiModel<StatusAlertModel>> checkStatusPenilaian(
//            @Path("id") int id
//    );
//
//    @GET("operator/penilaian")
//    Call<ResponseApiModel<PenilaianModel>> getPenilaian(
//            @QueryMap HashMap<String, Object> map
//    );
//
//    @GET("operator/penilaian/download/{id}")
//    Call<ResponseBody> downloadEvidence(
//            @Path("id") String id
//    );
//
//    @Multipart
//    @POST("operator/penilaian/data/store")
//    Call<ResponseApiModel> storePenilaianWithEvidence(
//            @PartMap Map<String, RequestBody> fieldMap,
//            @Part MultipartBody.Part multiPart
//            );
//
//
//    @Multipart
//    @POST("operator/penilaian/data/store")
//    Call<ResponseApiModel> storePenilaianNoEvidence(
//            @PartMap Map<String, RequestBody> fieldMap
//    );
//
//    @GET("operator/penilaian/filter")
//    Call<ResponseApiModel<AllPenilaianModel>> getAllPenilaan(
//            @QueryMap HashMap<String, String> map
//    );
//
//    @GET("operator/pu/download/{filename}")
//    Call<ResponseBody> downloadFilePu(
//            @Path("filename") String fileName
//    );
//
//
//    @Multipart
//    @POST("operator/pu/store")
//    Call<ResponseApiModel> storePuWithFile(
//            @PartMap Map<String, RequestBody> map,
//            @Part List<MultipartBody.Part> partList
//    );
//
//    @Multipart
//    @POST("operator/pu/store")
//    Call<ResponseApiModel> storePu(
//            @PartMap Map<String, RequestBody> map
//    );
//
//    @Multipart
//    @POST("superadmin/profile/update/photo")
//    Call<ResponseApiModel> updatePhotoProfile(
//            @PartMap HashMap<String, RequestBody> map,
//            @Part MultipartBody.Part part);
//
//









}
