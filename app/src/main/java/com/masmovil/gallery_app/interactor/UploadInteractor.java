package com.masmovil.gallery_app.interactor;

import com.masmovil.gallery_app.entity.api.client.UserService;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.UserToken;
import com.masmovil.gallery_app.presenter.UploadContracts;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.RequestBody;

/**
 * Created by luisvespa on 06/09/18.
 */

public class UploadInteractor implements UploadContracts.Interactor {

    UserService userService;

    public UploadInteractor(UserService userService) {
        this.userService = userService;
    }

    public Single<Gallery> upload(String title, String description, String albumId, String username, File file) {
        return userService.upload(title, description, albumId, username, file);
    }

    @Override
    public void unRegister() {
        this.userService = null;
    }
}
