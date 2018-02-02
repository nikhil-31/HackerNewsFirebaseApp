package in.co.nikhil.hackernewsfirebaseapp.di.component;

import javax.inject.Singleton;

import dagger.Component;
import in.co.nikhil.hackernewsfirebaseapp.activity.DetailActivity;
import in.co.nikhil.hackernewsfirebaseapp.activity.MainActivity;
import in.co.nikhil.hackernewsfirebaseapp.di.module.ApplicationModule;
import in.co.nikhil.hackernewsfirebaseapp.fragments.ArticleWebFragment;

/**
 * Created by nik on 2/2/2018.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

  void inject(MainActivity target);

  void inject(DetailActivity target);

  void inject(ArticleWebFragment target);

}
