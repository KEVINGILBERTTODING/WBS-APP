package com.example.wbs.core.models;

import okhttp3.ResponseBody;

public class ResponseApiDownloadModel {
    private String state;
    private String message;
    private ResponseBody responseBody;

    public ResponseApiDownloadModel(String state, String message, ResponseBody responseBody) {
        this.state = state;
        this.message = message;
        this.responseBody = responseBody;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }
}
