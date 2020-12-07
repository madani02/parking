package com.example.lyes.parking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lyes on 12-09-2019.
 */
public class Login_exemple {

    @SerializedName( "agent_name" )
    String agent_name;

    @SerializedName( "agent_pass" )
    String agent_pass;


    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getAgent_pass() {
        return agent_pass;
    }

    public void setAgent_pass(String agent_pass) {
        this.agent_pass = agent_pass;
    }
}
