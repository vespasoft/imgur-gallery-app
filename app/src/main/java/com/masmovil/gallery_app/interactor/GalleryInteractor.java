package com.masmovil.gallery_app.interactor;

import com.masmovil.gallery_app.entity.api.client.UserService;
import com.masmovil.gallery_app.entity.model.Data;
import com.masmovil.gallery_app.entity.model.UserToken;
import com.masmovil.gallery_app.presenter.GalleryContracts;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by luisvespa on 06/09/18.
 */

public class GalleryInteractor implements GalleryContracts.Interactor {

    UserService userService;

    public GalleryInteractor(UserService userService) {
        this.userService = userService;
    }

    public Single<UserToken> newAccessToken(Map<String, String> map) { return userService.newAccessToken(map); }

    public Single<Data> getGallery(String accessToken) { return userService.getGallery(accessToken); }

    public Completable deleteImage(String imageHash) { return userService.deleteImage(imageHash); }

    @Override
    public void unRegister() {
        userService = null;
    }

}
