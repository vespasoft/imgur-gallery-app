package com.masmovil.gallery_app.presenter;

import android.text.TextUtils;

import com.masmovil.gallery_app.app.UserPreferences;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.Upload;
import com.masmovil.gallery_app.interactor.UploadInteractor;
import com.masmovil.gallery_app.router.UploadRouter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.ConsumerSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Created by luisvespa on 06/10/18.
 */

public class UploadPresenter extends Presenter<UploadContracts.View> implements UploadContracts.Presenter {
    private UploadInteractor interactor;
    private UploadContracts.Router router;
    private UserPreferences userPreferences;

    public UploadPresenter(UploadInteractor interactor, UploadRouter usersRouter) {
        this.interactor = interactor;
        this.router = usersRouter;
    }

    @Override
    public void onDestroy() {
        this.interactor.unRegister();
        this.interactor = null;
        this.router.unRegister();
        this.router = null;
    }

    @Override
    public void uploadImage(Upload uploadFile) {
        this.userPreferences = new UserPreferences(getView().context());
        String username = userPreferences.getUsername();

        interactor.upload(uploadFile.title, uploadFile.description, uploadFile.albumId, username, uploadFile.image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Gallery>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Gallery gallery) {
                        if (gallery == null) {
                            /*
                             Notify image was NOT uploaded successfully
                            */
                            getView().createFailedUploadNotification();
                            return;
                        }
                        /*
                        Notify image was uploaded successfully
                        */
                        if (!TextUtils.isEmpty(gallery.getId())) {
                            getView().createUploadedNotification(gallery);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().createFailedUploadNotification();
                    }
                });
    }

    @Override
    public void goToGalleryScreen() {
        router.presentGalleryScreen();
    }
}
