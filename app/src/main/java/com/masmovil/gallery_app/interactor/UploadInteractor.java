package com.masmovil.gallery_app.interactor;

import com.masmovil.gallery_app.entity.api.client.UserService;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.UserToken;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by luisvespa on 06/09/18.
 */

public class UploadInteractor {

    UserService userService;

    public UploadInteractor(UserService userService) {
        this.userService = userService;
    }

    public Completable upload(Map<String, String> map) { return userService.upload(map); }

}
