package com.denny.tokensample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tp00186 on 2018/8/16.
 */

public class ErrorUnits {
    @SerializedName("Message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
