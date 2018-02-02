package in.co.nikhil.hackernewsfirebaseapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.co.nikhil.hackernewsfirebaseapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends Fragment {

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

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
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_comments, container, false);
  }

}