package com.example.anygift.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadImageResult {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("path")
    @Expose
    private String path;

    public UploadImageResult(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "uploadingResults{" +
                "message=" + message +
                ", path=" + path +
                '}';
    }
}
