package com.masmovil.gallery_app.presenter;

import android.support.v7.view.ActionMode;

import com.masmovil.gallery_app.entity.model.Gallery;

import java.util.List;

/**
 * Created by luisvespa on 06/08/18.
 */

public class GalleryContracts {

    public interface View extends com.masmovil.gallery_app.presenter.Presenter.View {

        void showLoading(boolean show);

        void showErrorMessage(String message);

        void showNotFoundMessage();

        void showConnectionErrorMessage();

        void renderImages(List<Gallery> images);
    }

    public interface Presenter  {
        void onDestroy();

        void newAccessToken();

        void deleteImage(String imageHash, final ActionMode mode);

        void getAllGallery();

        void goToLoginScreen();
    }

    public interface Interactor {
        void unRegister();
    }

    public interface Router {
        void unRegister();

        void presentLoginScreen();

    }
}
