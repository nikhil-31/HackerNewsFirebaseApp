package in.co.nikhil.hackernewsfirebaseapp.di.module;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nik on 2/2/2018.
 */

@Module
public class ApplicationModule {

  private Application application;

  public ApplicationModule(Application application) {
    this.application = application;
  }

  @Provides
  @Singleton
  public Context providesApplicationContext() {
    return application;
  }

  @Provides
  @Singleton
  public RequestQueue provideRequestQueue() {
    return Volley.newRequestQueue(application);
  }
}