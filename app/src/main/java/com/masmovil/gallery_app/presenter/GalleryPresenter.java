package com.masmovil.gallery_app.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.support.v7.view.ActionMode;

import com.masmovil.gallery_app.app.AppConstants;
import com.masmovil.gallery_app.app.UserPreferences;
import com.masmovil.gallery_app.entity.model.Data;
import com.masmovil.gallery_app.entity.model.UserToken;
import com.masmovil.gallery_app.interactor.GalleryInteractor;
import com.masmovil.gallery_app.router.GalleryRouter;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by luisvespa on 12/17/17.
 */

public class GalleryPresenter extends Presenter<GalleryContracts.View> implements GalleryContracts.Presenter {

    private GalleryInteractor interactor;
    private GalleryContracts.Router router;
    private UserPreferences userPreferences;

    public GalleryPresenter(GalleryInteractor interactor, GalleryRouter usersRouter) {
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
    public void newAccessToken() {

        this.userPreferences = new UserPreferences(getView().context());
        String refreshToken = userPreferences.getRefreshToken();

        if (refreshToken!=null) {
            Map<String, String> map = new HashMap<>();
            map.put("refresh_token", refreshToken);
            map.put("client_id", AppConstants.MY_IMGUR_CLIENT_ID);
            map.put("client_secret", AppConstants.MY_IMGUR_CLIENT_SECRET);
            map.put("grant_type", "refresh_token");

            interactor.newAccessToken(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserToken>() {
                    @Override
                    public void onSuccess(UserToken userToken) {
                        if (!TextUtils.isEmpty(userToken.getAccessToken())) {
                            userPreferences.saveRefreshToken(userToken.getRefreshToken(), userToken.getAccessToken(), userToken.getExpiresIn());
                            getAllGallery();
                            Log.i(TAG, "Got new access token "+ userToken.toString() );
                        }
                        else {
                            Log.i(TAG, "Could not get new access token");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }
                });
        }

    }

    @Override
    public void deleteImage(String imageHash, final ActionMode mode) {
        interactor.deleteImage(imageHash)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    getAllGallery();
                    mode.finish();
                }

                @Override
                public void onError(Throwable e) {
                    getView().showNotFoundMessage();
                    Log.e(TAG, e.getMessage());
                }
            });
    }

    @Override
    public void getAllGallery() {
        this.userPreferences = new UserPreferences(getView().context());
        String accessToken = "Bearer " + userPreferences.getAccessToken();

        if (accessToken!=null) {
            interactor.getGallery(accessToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<Data>() {
                        @Override
                        public void onSuccess(Data galleries) {

                            if (!galleries.getData().isEmpty()) {
                                Log.i(TAG, "The user have images "+ galleries.getData().get(0).toString() );
                                getView().renderImages(galleries.getData());
                            }
                            else {
                                Log.i(TAG, "Could not get images");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: " + e.getMessage());
                        }
                    });

        }
    }

    @Override
    public void loggedOut() {
        userPreferences.logout();
        router.presentLoginScreen();
    }

    @Override
    public void goToUploadScreen() {
        router.presentUploadScreen();
    }

}
