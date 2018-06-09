package com.masmovil.gallery_app.entity.api.retrofit;


import com.masmovil.gallery_app.app.AppConstants;

/**
 * Created by luisvespa on 12/13/17.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static UserRetrofitServices getAPIUserService() {

        return RetrofitClient.getClient(AppConstants.IMGUR_API).create(UserRetrofitServices.class);
    }

}
