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
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.nikhil.hackernewsfirebaseapp.R;
import in.co.nikhil.hackernewsfirebaseapp.fragments.ArticleWebFragment;
import in.co.nikhil.hackernewsfirebaseapp.fragments.CommentsFragment;

public class DetailActivity extends AppCompatActivity {

  private static final int COMMENTS = 0;
  private static final int ARTICLE_WEB_VIEW = 1;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.pager)
  ViewPager mViewPager;
  @BindView(R.id.tabs)
  TabLayout mTabs;

  private long mStoryId;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.bind(this);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      mStoryId = bundle.getLong("storyId");
    }

    setSupportActionBar(mToolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setTitle("");
    }
    Toast.makeText(this, "Story id " + mStoryId, Toast.LENGTH_SHORT).show();


    mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    mTabs.setupWithViewPager(mViewPager);
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

}
