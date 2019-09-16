package com.cpd.vos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by theolm on 15/07/16.
 */
public class RuApiReturn {
    @SerializedName("ru1")
    @Expose
    public RuVo ru1;
    @SerializedName("ru2")
    @Expose
    public RuVo ru2;
    @SerializedName("ru3")
    @Expose
    public RuVo ru3;
    @SerializedName("ru4")
    @Expose
    public RuVo ru4;
    @SerializedName("ru5")
    @Expose
    public RuVo ru5;
    @SerializedName("ru6")
    @Expose
    public RuVo ru6;

    public RuVo getRu1() {
        return ru1;
    }

    public void setRu1(RuVo ru1) {
        this.ru1 = ru1;
    }

    public RuVo getRu2() {
        return ru2;
    }

    public void setRu2(RuVo ru2) {
        this.ru2 = ru2;
    }

    public RuVo getRu3() {
        return ru3;
    }

    public void setRu3(RuVo ru3) {
        this.ru3 = ru3;
    }

    public RuVo getRu4() {
        return ru4;
    }

    public void setRu4(RuVo ru4) {
        this.ru4 = ru4;
    }

    public RuVo getRu5() {
        return ru5;
    }

    public void setRu5(RuVo ru5) {
        this.ru5 = ru5;
    }

    public RuVo getRu6() {
        return ru6;
    }

    public void setRu6(RuVo ru6) {
        this.ru6 = ru6;
    }
}
