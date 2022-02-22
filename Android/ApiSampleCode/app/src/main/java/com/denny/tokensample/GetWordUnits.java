package com.denny.tokensample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tp00186 on 2018/8/17.
 */

public class GetWordUnits {
    @SerializedName("Name")
    private String name;
    @SerializedName("No")
    private String no;
    @SerializedName("Qty")
    private float qty;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }
}
