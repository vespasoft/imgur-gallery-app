package com.masmovil.gallery_app.entity.api.client;


import com.masmovil.gallery_app.app.AppConstants;
import com.masmovil.gallery_app.entity.api.retrofit.ApiUtils;
import com.masmovil.gallery_app.entity.api.retrofit.RetrofitClient;
import com.masmovil.gallery_app.entity.model.Data;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.UserToken;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    public Single<Gallery> upload(String title, String description, String albumId, String username, File file) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
        return ApiUtils.getAPIUserService().upload(AppConstants.HEADER_CLIENTID, title, description, albumId, username, fileBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
