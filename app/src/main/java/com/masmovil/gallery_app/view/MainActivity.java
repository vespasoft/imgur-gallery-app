package com.masmovil.gallery_app.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.masmovil.gallery_app.R;
import com.masmovil.gallery_app.app.UserPreferences;
import com.masmovil.gallery_app.entity.api.client.UserClient;
import com.masmovil.gallery_app.interactor.GalleryInteractor;
import com.masmovil.gallery_app.presenter.GalleryContracts;
import com.masmovil.gallery_app.presenter.GalleryPresenter;
import com.masmovil.gallery_app.router.GalleryRouter;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements GalleryContracts.View {

    private GalleryPresenter galleryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        galleryPresenter = new GalleryPresenter(new GalleryInteractor(new UserClient()), new GalleryRouter(this));
        galleryPresenter.setView(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        UserPreferences preferences = new UserPreferences(this);
        if (!preferences.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        galleryPresenter.newAccessToken();
    }

    @Override
    public Context context() {
        return getApplicationContext();
    }
}
