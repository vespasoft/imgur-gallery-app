package com.masmovil.gallery_app.entity.api.retrofit;

import com.masmovil.gallery_app.entity.model.Data;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.UserToken;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by luisvespa on 12/13/17.
 */

public interface UserRetrofitServices {

    @GET("/3/account/me/images")
    Single<Data> getGallery(@Header("Authorization") String accessToken);

    @DELETE("https://api.imgur.com/3/image/{imageHash}")
    Completable deleteImage(@Path("imageHash") String imageHash);

    @FormUrlEncoded
    @POST("/oauth2/token")
    Single<UserToken> newAccessToken(@Header("Authorization") String clientId, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/3/image")
    Completable upload(@Header("Authorization") String clientId, @FieldMap Map<String, String> map);

}
