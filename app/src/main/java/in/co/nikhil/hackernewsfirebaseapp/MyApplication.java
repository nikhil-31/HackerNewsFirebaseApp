package in.co.nikhil.hackernewsfirebaseapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import in.co.nikhil.hackernewsfirebaseapp.di.component.ApplicationComponent;

import in.co.nikhil.hackernewsfirebaseapp.di.component.DaggerApplicationComponent;
import in.co.nikhil.hackernewsfirebaseapp.di.module.ApplicationModule;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by nik on 2/2/2018.
 */

public class MyApplication extends Application {

  private ApplicationComponent component;

  @Override
  public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());

    component = DaggerApplicationComponent
        .builder()
        .applicationModule(new ApplicationModule(this))
        .build();

    // Init Realm
    Realm.init(this);
  }

  public ApplicationComponent getComponent() {
    return component;
  }
}
