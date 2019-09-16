package com.cpd.network.models;

import android.graphics.Color;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.cpd.ufrgsmobile.R;

/**
 * Created by Theo on 02/06/17.
 */

public class Ru {
    @SerializedName("nome")
    @Expose
    public String nome;
    @SerializedName("cardapio")
    @Expose
    public String cardapio;
    @SerializedName("number")
    @Expose
    public int number;

    public int getColor() {
        switch (number) {
            case 1:
                return Color.parseColor("#E1AB1D");
            case 2:
                return Color.parseColor("#182F3A");
            case 3:
                return Color.parseColor("#5E9A47");
            case 4:
                return Color.parseColor("#D07D0E");
            case 5:
                return Color.parseColor("#E13E3C");
            case 6:
                return Color.parseColor("#585B5C");
            case 7:
                return Color.parseColor("#0C4F88");
            default:
                return Color.parseColor("#000000");
        }
    }


    public int getImage() {
        switch (number) {
            case 1:
                return R.drawable.ru1;
            case 2:
                return R.drawable.ru2;
            case 3:
                return R.drawable.ru3;
            case 4:
                return R.drawable.ru4;
            case 5:
                return R.drawable.ru5;
            case 6:
                return R.drawable.ru6;
            case 7:
                return R.drawable.ru7;
            default:
                return R.drawable.ru1;
        }
    }

}
