package com.cpd.network.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;

/**
 * Created by Hermes Tessaro on 16/10/18.
 */


public class Notifications implements Serializable{

    @SerializedName("TituloNotificacao")
    @Expose
    public String titulo;

    @SerializedName("TextoNotificacao")
    @Expose
    public String texto;

    public Notifications(LinkedTreeMap linkedTreeMap){
        titulo = (String) linkedTreeMap.get("titulo");
        texto = (String) linkedTreeMap.get("texto");
    }

}
