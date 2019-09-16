package com.cpd.network.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;

/**
 * Created by theolm on 19/05/17.
 */

public class News implements Serializable {

    @SerializedName("titulo")
    @Expose
    public String titulo;
    @SerializedName("chamada")
    @Expose
    public String chamada;
    @SerializedName("data")
    @Expose
    public String data;
    @SerializedName("image_thumb")
    @Expose
    public String imageThumb;
    @SerializedName("image_large")
    @Expose
    public String imageLarge;
    @SerializedName("imagem")
    @Expose
    public Object imagem;
    @SerializedName("texto")
    @Expose
    public String texto;

    public NewsImage getImage(){
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(imagem).getAsJsonObject();
            NewsImage newsImage = gson.fromJson(jsonObject, NewsImage.class);
            return newsImage;
        } catch (Exception e){
            return null;
        }
    }

    public News(LinkedTreeMap linkedTreeMap) {
        titulo = (String) linkedTreeMap.get("titulo");
        chamada = (String) linkedTreeMap.get("chamada");
        data = (String) linkedTreeMap.get("data");
        imageThumb = (String) linkedTreeMap.get("imageThumb");
        imageLarge = (String) linkedTreeMap.get("imageLarge");
        imagem = (Object) linkedTreeMap.get("imagem");
        texto = (String) linkedTreeMap.get("texto");
    }

}
