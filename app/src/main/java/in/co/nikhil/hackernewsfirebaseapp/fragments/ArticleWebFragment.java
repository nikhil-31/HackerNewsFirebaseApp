package in.co.nikhil.hackernewsfirebaseapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.nikhil.hackernewsfirebaseapp.MyApplication;
import in.co.nikhil.hackernewsfirebaseapp.R;
import in.co.nikhil.hackernewsfirebaseapp.data.HackerStory;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleWebFragment extends Fragment {

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  @BindView(R.id.webView)
  WebView mWebView;

  public ArticleWebFragment() {
    // Required empty public constructor
  }

  @Inject
  Realm mRealm;

  private long mStoryId;

  public static ArticleWebFragment newInstance(String param1, String param2) {
    ArticleWebFragment fragment = new ArticleWebFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getActivity() != null) {
      ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_article_web, container, false);
    ButterKnife.bind(this, rootView);

    Bundle bundle = getActivity().getIntent().getExtras();
    if (bundle != null) {
      mStoryId = bundle.getLong("storyId");
    }

    HackerStory hackerStory = mRealm.where(HackerStory.class)
        .equalTo("mId", mStoryId)
        .findFirst();

    if (hackerStory != null) {
      mWebView.setWebViewClient(new CustomWebViewClient());
      WebSettings webSetting = mWebView.getSettings();
      webSetting.setDisplayZoomControls(true);
      mWebView.loadUrl(hackerStory.getUrl());
    }
    return rootView;
  }

  private class CustomWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      return true;
    }
  }

}
