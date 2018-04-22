package com.example.jakobsuell.spotd;

import android.annotation.SuppressLint;
import android.app.Application;

import models.User;

@SuppressLint("Registered")
public class Globals extends Application {

    public boolean isPicassoSingletonAssigned = false;
    public final String firebaseURI = "gs://spotd-11132.appspot.com/";

    public User currentUser;

    public Globals() {

    }



}
