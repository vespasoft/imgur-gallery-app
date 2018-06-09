package com.masmovil.gallery_app.app;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Application extends android.app.Application {
	
	private static final String TAG = Application.class.getSimpleName();

    private static Context context;

    public void onCreate(){
        super.onCreate();
        Application.context = getApplicationContext();
    }

    /**
     * http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android
     */
    public static Context getAppContext() {
        return Application.context;
    }
    
    public static String getVersionName() {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Package name not found.", e);
			return "1.0";
		}
    }

}
