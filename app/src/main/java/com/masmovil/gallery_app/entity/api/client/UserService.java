package com.masmovil.gallery_app.entity.api.client;

import com.masmovil.gallery_app.entity.model.Data;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.UserToken;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.FieldMap;

/**
 * Created by luisvespa on 12/17/17.
 */

public interface UserService {

    Single<Data> getGallery(String accessToken);

    Single<UserToken> newAccessToken(@FieldMap Map<String, String> map);

    Completable upload(@FieldMap Map<String, String> map);

}
