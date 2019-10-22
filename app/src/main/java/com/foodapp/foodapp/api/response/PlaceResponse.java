package com.foodapp.foodapp.api.response;

abstract public class PlaceResponse {

    public abstract void post(String name);

    public abstract void onComplete();

    public abstract void fail(Exception exception);
}
