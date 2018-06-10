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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.masmovil.gallery_app.view.listener.ClickListener;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.view.ActionMode;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GalleryContracts.View {
    private static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
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
        actionModeCallback = new ActionModeCallback();

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

        galleryPresenter.getAllGallery();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //galleryPresenter.newAccessToken();
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
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i("MainAcitivty", "has been clicked the image "+position );
                enableActionMode(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                try {
                    enableActionMode(position);
                } catch (Exception ex){
                    Log.e(TAG, "error in onLongClick. " + ex.toString());
                }
            }
        }));

        mAdapter.notifyDataSetChanged();
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }


    private void toggleSelection(int position) {
        mAdapter.setSelectedItem(position);
        mAdapter.notifyDataSetChanged();

        if (position < 0) {
            actionMode.finish();
        } else {
            //mActivity.showToolbar(false);
            actionMode.setTitle(String.valueOf(position));
            actionMode.invalidate();
        }
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_view:
                    Log.i("MainActivity", "action view has been clicked");
                    return true;

                case R.id.action_delete:
                    Log.i("MainActivity", "action delete has been clicked");
                    return true;

                default:
                    mode.finish();
                    return true;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.setSelectedItem(-1);
            actionMode = null;
            //mActivity.showToolbar(true);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    //galleryPresenter.getAllGallery();
                }
            });
        }
    }
}
