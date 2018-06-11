package com.masmovil.gallery_app.router;

import android.app.Activity;
import android.content.Intent;

import com.masmovil.gallery_app.entity.model.Upload;
import com.masmovil.gallery_app.presenter.GalleryContracts;
import com.masmovil.gallery_app.presenter.UploadContracts;
import com.masmovil.gallery_app.view.activities.MainActivity;
import com.masmovil.gallery_app.view.activities.UploadActivity;

/**
 * Created by luisvespa on 06/09/18.
 */

public class UploadRouter implements UploadContracts.Router {

    private Activity activity;

    public UploadRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void unRegister() {
        activity = null;
    }

    @Override
    public void presentGalleryScreen() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

}
