package com.masmovil.gallery_app.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.masmovil.gallery_app.R;
import com.masmovil.gallery_app.app.UserPreferences;
import com.masmovil.gallery_app.entity.api.client.UserClient;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.interactor.GalleryInteractor;
import com.masmovil.gallery_app.presenter.GalleryContracts;
import com.masmovil.gallery_app.presenter.GalleryPresenter;
import com.masmovil.gallery_app.router.GalleryRouter;
import com.masmovil.gallery_app.view.adapter.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GalleryContracts.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private List<Gallery> images = new ArrayList<>();
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;

    private GalleryPresenter galleryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        galleryPresenter = new GalleryPresenter(new GalleryInteractor(new UserClient()), new GalleryRouter(this));
        galleryPresenter.setView(this);

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

        this.configureRecyclerView();

    }

    private void configureRecyclerView() {
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void showUsersNotFoundMessage(boolean show) {

    }

    @Override
    public void showConnectionErrorMessage(boolean show) {

    }

    @Override
    public void renderImages(List<Gallery> images) {
        this.images = images;
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
