package com.example.lyes.parking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lyes on 21-09-2019.
 */

public class Notification_exemple {

    @SerializedName("id_notification")
    String id_notification;

    @SerializedName("title")
    String title;

    @SerializedName("message")
    String message;

    public String getId_notification() {
        return id_notification;
    }

    public void setId_notification(String id_notification) {
        this.id_notification = id_notification;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
