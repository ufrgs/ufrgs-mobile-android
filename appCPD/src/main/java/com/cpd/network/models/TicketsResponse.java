package com.cpd.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Theo on 08/09/17.
 */

public class TicketsResponse {
    @SerializedName("tiquetes")
    @Expose
    public List<Tickets> tiquetes = null;
}
