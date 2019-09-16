package com.cpd.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by theolm on 19/05/17.
 */

public class NewsImage implements Serializable {
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("legenda")
    @Expose
    public String legenda;
    @SerializedName("width")
    @Expose
    public String width;
    @SerializedName("height")
    @Expose
    public String height;
}
