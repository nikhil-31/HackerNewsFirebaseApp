package in.co.nikhil.hackernewsfirebaseapp.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.nikhil.hackernewsfirebaseapp.MyApplication;
import in.co.nikhil.hackernewsfirebaseapp.R;
import in.co.nikhil.hackernewsfirebaseapp.data.HackerStory;
import in.co.nikhil.hackernewsfirebaseapp.fragments.ArticleWebFragment;
import in.co.nikhil.hackernewsfirebaseapp.fragments.CommentsFragment;
import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {

  private static final int COMMENTS = 0;
  private static final int ARTICLE_WEB_VIEW = 1;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.pager)
  ViewPager mViewPager;
  @BindView(R.id.tabs)
  TabLayout mTabs;
  @BindView(R.id.detail_title)
  TextView mTitle;
  @BindView(R.id.detail_url)
  TextView mUrl;
  @BindView(R.id.detail_time_user)
  TextView mTimeUser;

  private long mStoryId;
  private boolean mUser = false;

  @Inject
  Realm mRealm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.bind(this);

    ((MyApplication) getApplication()).getComponent().inject(this);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      mStoryId = bundle.getLong("storyId");
    }

    HackerStory hackerStory = mRealm.where(HackerStory.class)
        .equalTo("mId", mStoryId)
        .findFirst();
    if (hackerStory != null) {
      mTitle.setText(hackerStory.getArticleTitle());
      mUrl.setText(hackerStory.getUrl());

      long timeMillis = System.currentTimeMillis() / 1000 - Long.parseLong(hackerStory.getDatetime());

      long seconds = timeMillis / 1000;
      long minutes = seconds / 60;
      long hours = minutes / 60;
      long days = hours / 24;

      if (days == 0) {
        String hoursMinutes = hours % 24 + "H " + minutes % 60 + "M " + seconds % 60 + "S . " + hackerStory.getSubmitter();
        mTimeUser.setText(hoursMinutes);
      } else {
        String time = days + "D " + hours % 24 + "H " + minutes % 60 + "M " + seconds % 60 + "S . " + hackerStory.getSubmitter();
        mTimeUser.setText(time);
      }

      if (hackerStory.getUrl().equals("")){
        mUser = true;
      }
    } else {
      Toast.makeText(this, R.string.error_not_found, Toast.LENGTH_SHORT).show();
    }

    setSupportActionBar(mToolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setTitle("");
    }
    mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    if (mUser) {
      mViewPager.setAdapter(new MyCommentsAdapter(getSupportFragmentManager()));
      mTabs.setupWithViewPager(mViewPager);
    } else {
      mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
      mTabs.setupWithViewPager(mViewPager);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private class MyPagerAdapter extends FragmentStatePagerAdapter {
    MyPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment = null;
      switch (position) {
        case COMMENTS:
          fragment = CommentsFragment.newInstance("", "");
          break;
        case ARTICLE_WEB_VIEW:
          fragment = ArticleWebFragment.newInstance("", "");
          break;

      }
      return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return getResources().getStringArray(R.array.tabs)[position];
    }

    @Override
    public int getCount() {
      return 2;
    }
  }

  private class MyCommentsAdapter extends FragmentStatePagerAdapter {
    MyCommentsAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment = null;
      switch (position) {
        case COMMENTS:
          fragment = CommentsFragment.newInstance("", "");
          break;
      }
      return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return getResources().getStringArray(R.array.tabs)[position];
    }

    @Override
    public int getCount() {
      return 1;
    }
  }


}
