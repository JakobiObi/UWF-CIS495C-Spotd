package com.example.jakobsuell.spotd;

import android.app.Application;

public class ApplicationController extends Application {

    public boolean isPicassoSingletonAssigned = false;
    public final String firebaseURI = "gs://spotd-11132.appspot.com/";

    public ApplicationController() {

    }



}
