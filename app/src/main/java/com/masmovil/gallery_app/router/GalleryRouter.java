package com.masmovil.gallery_app.router;

import android.app.Activity;
import android.content.Intent;

import com.masmovil.gallery_app.entity.api.client.UserClient;
import com.masmovil.gallery_app.interactor.GalleryInteractor;
import com.masmovil.gallery_app.presenter.GalleryContracts;
import com.masmovil.gallery_app.view.MainActivity;

/**
 * Created by luisvespa on 06/09/18.
 */

public class GalleryRouter implements GalleryContracts.Router {

    private Activity activity;

    public GalleryRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void unRegister() {
        activity = null;
    }

    @Override
    public void presentLoginScreen() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

}
