package com.masmovil.gallery_app.view.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.masmovil.gallery_app.R;
import com.masmovil.gallery_app.entity.api.client.UserClient;
import com.masmovil.gallery_app.entity.model.Gallery;
import com.masmovil.gallery_app.entity.model.Upload;
import com.masmovil.gallery_app.interactor.GalleryInteractor;
import com.masmovil.gallery_app.interactor.UploadInteractor;
import com.masmovil.gallery_app.presenter.GalleryPresenter;
import com.masmovil.gallery_app.presenter.UploadContracts;
import com.masmovil.gallery_app.presenter.UploadPresenter;
import com.masmovil.gallery_app.router.GalleryRouter;
import com.masmovil.gallery_app.router.UploadRouter;
import com.masmovil.gallery_app.view.util.CommonUtils;
import com.masmovil.gallery_app.view.util.DocumentUtil;
import com.masmovil.gallery_app.view.util.IntentUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadActivity extends AppCompatActivity implements UploadContracts.View {

    public final static String TAG = UploadActivity.class.getSimpleName();

    @BindView(R.id.imageview)
    ImageView uploadImage;
    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextDescription)
    EditText editTextDescription;
    @BindView(R.id.titleWrapper)
    TextInputLayout titleWrapper;
    @BindView(R.id.descriptionWrapper)
    TextInputLayout descriptionWrapper;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.contentLayout)
    View mView;

    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent
    private UploadPresenter uploadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_24dp));


        uploadPresenter = new UploadPresenter(new UploadInteractor(new UserClient()), new UploadRouter(this));
        uploadPresenter.setView(this);

        if (shouldAskPermissions()) {
            askPermissions();
        }
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null; Bitmap bitmap = null;
        switch (requestCode) {
            case IntentUtil.REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bundle extras = data.getExtras();
                        bitmap = (Bitmap) extras.get("data");
                        uploadImage.setImageBitmap(bitmap);
                        new fileFromBitmap(bitmap, getApplicationContext()).execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

        }

    }

    @OnClick(R.id.imageview)
    public void onChooseImage() {
        editTextDescription.clearFocus();
        editTextTitle.clearFocus();
        IntentUtil.dispatchTakePictureIntent(this);
    }

    private void clearInput() {
        editTextTitle.setText("");
        editTextDescription.clearFocus();
        editTextDescription.setText("");
        editTextTitle.clearFocus();
        uploadImage.setImageResource(R.drawable.ic_collections_black_24dp);
    }

    @OnClick(R.id.fab)
    public void uploadImage() {
        CommonUtils.hideKeyBoard(this);
        /*
          Create the @Upload object
         */
        if (chosenFile == null) {
            CommonUtils.showSnackBar(this, uploadImage, "A photo is requiered");
            return;
        }
        createUpload(chosenFile);
        if (TextUtils.isEmpty(upload.title)) {
            editTextTitle.setError("Not a valid title!");
            return;
        }
        if (TextUtils.isEmpty(upload.description)) {
            editTextDescription.setError("Not a valid description!");
            return;
        }
        titleWrapper.setErrorEnabled(false);
        descriptionWrapper.setErrorEnabled(false);
        uploadPresenter.uploadImage(upload);
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
        upload.title = editTextTitle.getText().toString();
        upload.description = editTextDescription.getText().toString();
    }


    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void createFailedUploadNotification() {
        Log.i(TAG, "filed uploaded notification");
    }

    @Override
    public void createUploadedNotification() {
        CommonUtils.showSnackBar(this, mView, "the photo has been uploaded");
        finish();
    }

    @Override
    public void showConnectionErrorMessage() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                uploadPresenter.goToGalleryScreen();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public Context context() {
        return this;
    }

    public class fileFromBitmap extends AsyncTask<Void, Integer, String> {

        Context context;
        Bitmap bitmap;
        String path_external = Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg";

        public fileFromBitmap(Bitmap bitmap, Context context) {
            this.bitmap = bitmap;
            this.context= context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before executing doInBackground
            // update your UI
            // exp; make progressbar visible
        }

        @Override
        protected String doInBackground(Void... params) {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            chosenFile = new File(path_external);
            try {
                FileOutputStream fo = new FileOutputStream(chosenFile);
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // back to main thread after finishing doInBackground
            // update your UI or take action after
            // exp; make progressbar gone
            Toast.makeText(context, "File has been created in the path external", Toast.LENGTH_LONG).show();
        }
    }
}




