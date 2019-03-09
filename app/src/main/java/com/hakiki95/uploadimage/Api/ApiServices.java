package com.hakiki95.uploadimage.Api;

import com.hakiki95.uploadimage.Model.MakananResponse;
import com.hakiki95.uploadimage.Model.ResponseApiModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by hakiki95 on 4/26/2017.
 */

public interface ApiServices {

    @Multipart
    @POST("uploadimage.php")
    Call<ResponseApiModel> uploadImage (@Part MultipartBody.Part image);

    @Multipart
    @POST("uploadmakanan.php")
    Call<MakananResponse> uploadMakanan(
            @Part("iduser") int iduser,
            @Part("idkategori") int idkategori,
            @Part("namamakanan") RequestBody namamakanan,
            @Part("descmakanan") RequestBody descmakanan,
            @Part("timeinsert") RequestBody time,
            @Part MultipartBody.Part image
            );
}
