package com.example.facebookintegration.controller;

import org.json.JSONObject;


public interface ResponseListener {

    void successResponse(String successResponse, int flag);

    void successResponse(JSONObject jsonObject, int flag);

    void errorResponse(String errorResponse, int flag);

    void removeProgress(Boolean hideFlag);

}
