package com.masmovil.gallery_app.entity.api.client;


import com.masmovil.gallery_app.app.AppConstants;
import com.masmovil.gallery_app.entity.api.retrofit.ApiUtils;
import com.masmovil.gallery_app.entity.api.retrofit.RetrofitClient;
import com.masmovil.gallery_app.entity.model.Data;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.UserToken;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by luisvespa on 12/17/17.
 */

public class UserClient extends RetrofitClient implements UserService {

    @Override
    public Single<Data> getGallery(String accessToken) {
        return ApiUtils.getAPIUserService().getGallery(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable deleteImage(String accessToken, String imageHash) {
        return ApiUtils.getAPIUserService().deleteImage(accessToken, imageHash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<UserToken> newAccessToken(Map<String, String> map) {
        return ApiUtils.getAPIUserService().newAccessToken(AppConstants.HEADER_CLIENTID, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable upload(String accessToken, String title, String description, File file) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);

        Map<String, RequestBody> parameters = new HashMap<>();
        parameters.put("title", toRequestBody(title));
        parameters.put("description", toRequestBody(description));
        parameters.put("image", fileBody);

        return ApiUtils.getAPIUserService().upload(accessToken, parameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }
}
