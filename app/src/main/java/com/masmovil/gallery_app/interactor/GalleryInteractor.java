package com.masmovil.gallery_app.interactor;

import com.masmovil.gallery_app.entity.api.client.UserService;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.UserToken;
import com.masmovil.gallery_app.presenter.GalleryContracts;

import java.util.List;
import java.util.Map;

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

    public Single<List<Gallery>> getGallery() { return userService.getGallery(); }


    @Override
    public void unRegister() {
        userService = null;
    }
}
