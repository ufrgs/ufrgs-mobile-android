package com.cpd.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Theo on 08/09/17.
 */

public class Tickets {
    @SerializedName("nrtiquete")
    @Expose
    public String nrtiquete;
    @SerializedName("nrrefeicoestotal")
    @Expose
    public String nrrefeicoestotal;
    @SerializedName("nrrefeicoesresta")
    @Expose
    public String nrrefeicoesresta;
}
