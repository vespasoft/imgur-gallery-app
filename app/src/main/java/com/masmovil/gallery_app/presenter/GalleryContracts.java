package com.masmovil.gallery_app.presenter;

import android.support.v7.view.ActionMode;

import com.masmovil.gallery_app.entity.model.Gallery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luisvespa on 06/08/18.
 */

public class GalleryContracts {

    public interface View extends com.masmovil.gallery_app.presenter.Presenter.View {

        void showLoading(boolean show);

        void showUsersNotFoundMessage(boolean show);

        void showConnectionErrorMessage(boolean show);

        void renderImages(List<Gallery> images);
    }

    public interface Presenter  {
        void onDestroy();

        void newAccessToken();

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
