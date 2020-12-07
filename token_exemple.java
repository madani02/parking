package com.example.lyes.parking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lyes on 23-09-2019.
 */

public class token_exemple {

    @SerializedName("token")
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
