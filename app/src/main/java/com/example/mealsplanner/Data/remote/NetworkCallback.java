package com.example.mealsplanner.Data.remote;


import java.util.List;

public interface NetworkCallback {
    public <T> void onSuccessResult(List<T> result);
    public  void onFailureResult(String errMsg);

}
