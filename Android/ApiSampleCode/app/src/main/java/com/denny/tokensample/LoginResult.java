package com.denny.tokensample;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("result")
    private boolean result;
    @SerializedName("rtntoken")
    private String rtntoken;

    public boolean getresult() {
        return result;
    }

    public void setresult(boolean result) {
        this.result = result;
    }

    public String getrtntoken() {
        return rtntoken;
    }

    public void setrtntoken(String rtntoken) {
        this.rtntoken = rtntoken;
    }

}
