package com.masmovil.gallery_app.entity.api.retrofit;

import com.masmovil.gallery_app.entity.model.Data;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.UserToken;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    /**
     * @param clientId    #Type of authorization for upload
     * @param title       #Title of image
     * @param description #Description of image
     * @param albumId     #ID for album (if the user is adding this image to an album)
     * @param username    username for upload
     * @param file        image
     */
    @Multipart
    @POST("/3/image")
    Single<Gallery> upload(@Header("Authorization") String clientId,
                       @Query("title") String title,
                       @Query("description") String description,
                       @Query("album") String albumId,
                       @Query("account_url") String username,
                       @Part("file\"; filename=\"pp.png\" ") RequestBody file);

}
