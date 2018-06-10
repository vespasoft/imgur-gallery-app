package com.masmovil.gallery_app.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.masmovil.gallery_app.app.AppConstants;
import com.masmovil.gallery_app.R;
import com.masmovil.gallery_app.app.UserPreferences;
import com.masmovil.gallery_app.presenter.GalleryContracts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenticationActivity extends AppCompatActivity {

    private static final Pattern accessTokenPattern = Pattern.compile("access_token=([^&]*)");
    private static final Pattern refreshTokenPattern = Pattern.compile("refresh_token=([^&]*)");
    private static final Pattern expiresInPattern = Pattern.compile("expires_in=(\\d+)");

    @BindView(R.id.loginWebView)
    WebView imgurWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

        setupWebView();

        imgurWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=" + AppConstants.MY_IMGUR_CLIENT_ID + "&response_type=token");

    }

    private void setupWebView() {
        imgurWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // intercept the tokens
                boolean tokensURL = false;
                if (url.startsWith(AppConstants.MY_IMGUR_REDIRECT_URL)) {
                    tokensURL = true;
                    Matcher m;

                    m = refreshTokenPattern.matcher(url);
                    m.find();
                    String refreshToken = m.group(1);

                    m = accessTokenPattern.matcher(url);
                    m.find();
                    String accessToken = m.group(1);

                    m = expiresInPattern.matcher(url);
                    m.find();
                    long expiresIn = Long.valueOf(m.group(1));

                    Log.i("refreshToken", refreshToken);
                    Log.i("accessToken", accessToken);
                    Log.i("expiresIn", ""+expiresIn);
                    UserPreferences preferences = new UserPreferences(AuthenticationActivity.this);
                    preferences.saveRefreshToken(refreshToken, accessToken, expiresIn);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AuthenticationActivity.this, R.string.logged_in, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    });
                }
                return tokensURL;
            }
        });
    }

}
