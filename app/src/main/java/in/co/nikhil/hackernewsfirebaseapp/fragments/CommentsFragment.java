package in.co.nikhil.hackernewsfirebaseapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Toast;

import javax.inject.Inject;

import in.co.nikhil.hackernewsfirebaseapp.MyApplication;
import in.co.nikhil.hackernewsfirebaseapp.R;
import in.co.nikhil.hackernewsfirebaseapp.data.HackerStory;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends Fragment {

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  @Inject
  Realm mRealm;

  private long mStoryId;

  public CommentsFragment() {
    // Required empty public constructor
  }

  public static CommentsFragment newInstance(String param1, String param2) {
    CommentsFragment fragment = new CommentsFragment();
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
    View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

    Bundle bundle = getActivity().getIntent().getExtras();
    if (bundle != null) {
      mStoryId = bundle.getLong("storyId");
    }

    HackerStory hackerStory = mRealm.where(HackerStory.class)
        .equalTo("mId", mStoryId)
        .findFirst();
    if (hackerStory != null) {

      Toast.makeText(getActivity(), "Comments JSON " + hackerStory.getCommentsJson(), Toast.LENGTH_SHORT).show();


    }
    return rootView;

  }




}
