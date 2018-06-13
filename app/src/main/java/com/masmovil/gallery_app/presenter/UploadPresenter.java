package com.masmovil.gallery_app.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.masmovil.gallery_app.app.UserPreferences;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.Upload;
import com.masmovil.gallery_app.interactor.UploadInteractor;
import com.masmovil.gallery_app.router.UploadRouter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.ConsumerSingleObserver;
import io.reactivex.observers.DisposableCompletableObserver;
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
        getView().showLoading(true);
        this.userPreferences = new UserPreferences(getView().context());
        String accessToken = "Bearer " + userPreferences.getAccessToken();
        interactor.upload(accessToken, uploadFile.title, uploadFile.description, uploadFile.image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        getView().showLoading(false);
                        getView().createUploadedNotification();
                        goToGalleryScreen();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showLoading(false);
                        getView().createFailedUploadNotification();
                        Log.e("UploadPresenter", e.toString());
                    }
                });
    }

    @Override
    public void goToGalleryScreen() {
        router.presentGalleryScreen();
    }
}
