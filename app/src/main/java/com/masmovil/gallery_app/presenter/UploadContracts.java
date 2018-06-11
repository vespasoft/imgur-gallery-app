package com.masmovil.gallery_app.presenter;

import android.support.v7.view.ActionMode;

import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.Upload;

import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by luisvespa on 06/08/18.
 */

public class UploadContracts {

    public interface View extends com.masmovil.gallery_app.presenter.Presenter.View {

        void showLoading(boolean show);

        void createFailedUploadNotification();

        void createUploadedNotification(Gallery gallery);

        void showConnectionErrorMessage();

    }

    public interface Presenter  {
        void onDestroy();

        void uploadImage(Upload uploadFile);

        void goToGalleryScreen();
    }

    public interface Interactor {
        void unRegister();
    }

    public interface Router {
        void unRegister();

        void presentGalleryScreen();

    }
}
