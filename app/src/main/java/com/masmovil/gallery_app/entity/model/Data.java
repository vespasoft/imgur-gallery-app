package com.masmovil.gallery_app.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luisvespa on 06/10/18.
 */

public class Data implements Serializable {
    @SerializedName("data")
    @Expose
    private List<Gallery> data;

    public Data(List<Gallery> data) {
        this.data = data;
    }

    public List<Gallery> getData() {
        return data;
    }

    public void setData(List<Gallery> data) {
        this.data = data;
    }
}
