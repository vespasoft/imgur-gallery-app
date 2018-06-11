package com.masmovil.gallery_app.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.masmovil.gallery_app.entity.model.UserToken;

public class UserPreferences {

    private static final String TAG = UserPreferences.class.getSimpleName();

    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static UserPreferences INSTANCE;

    static final String SHARED_PREFERENCES_NAME = "imgur_gallery_app";

    public UserPreferences(Context context) {
        this._context = context;
        preferences = _context.getSharedPreferences(SHARED_PREFERENCES_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(preferences.getString("access_token", null));
    }

    public String getRefreshToken() {
        return preferences.getString("refresh_token", null);
    }

    public String getAccessToken() {
        return preferences.getString("access_token", null);
    }

    public String getUsername() {
        return preferences.getString("account_username", null);
    }

    public void saveRefreshToken(String refreshToken, String accessToken, long expiresIn) {
        editor.putString("access_token", accessToken);
        editor.putString("refresh_token", refreshToken);
        editor.putLong("expires_in", expiresIn);
        editor.commit();
    }

    public void saveUserToken(UserToken userToken) {
        editor.putString("access_token", userToken.getAccessToken());
        editor.putString("refresh_token", userToken.getRefreshToken());
        editor.putLong("expires_in", userToken.getExpiresIn());
        editor.putString("account_username", userToken.getAccountUsername());
        editor.putString("token_type", userToken.getTokenType());
        editor.commit();
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

}
