package in.co.nikhil.hackernewsfirebaseapp;

import android.app.Application;

import in.co.nikhil.hackernewsfirebaseapp.di.component.ApplicationComponent;
import in.co.nikhil.hackernewsfirebaseapp.di.component.DaggerApplicationComponent;
import in.co.nikhil.hackernewsfirebaseapp.di.module.ApplicationModule;

/**
 * Created by nik on 2/2/2018.
 */

public class MyApplication extends Application {

  private ApplicationComponent component;
  @Override
  public void onCreate() {
    super.onCreate();

    component = DaggerApplicationComponent
        .builder()
        .applicationModule(new ApplicationModule(this))
        .build();

  }

  public ApplicationComponent getComponent() {
    return component;
  }
}