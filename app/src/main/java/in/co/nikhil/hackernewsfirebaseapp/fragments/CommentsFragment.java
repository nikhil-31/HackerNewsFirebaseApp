package in.co.nikhil.hackernewsfirebaseapp.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.nikhil.hackernewsfirebaseapp.MyApplication;
import in.co.nikhil.hackernewsfirebaseapp.R;
import in.co.nikhil.hackernewsfirebaseapp.adapter.CommentsAdapter;
import in.co.nikhil.hackernewsfirebaseapp.data.pojo.CommentsPojo;
import in.co.nikhil.hackernewsfirebaseapp.data.realm.HackerStory;
import in.co.nikhil.hackernewsfirebaseapp.data.remoteModel.Comment;
import in.co.nikhil.hackernewsfirebaseapp.ultis.URLs;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends Fragment {

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  @Inject
  Realm mRealm;
  @Inject
  RequestQueue mRequestQueue;

  private long mStoryId;
  private Gson mGson;
  CommentsAdapter mCommentsAdapter;

  @BindView(R.id.comments_recycler_view)
  RecyclerView mRecyclerView;
  ArrayList<CommentsPojo> mCommentsArrayList = new ArrayList<>();

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
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_comments, container, false);
    ButterKnife.bind(this, rootView);

    Bundle bundle = getActivity().getIntent().getExtras();
    if (bundle != null) {
      mStoryId = bundle.getLong("storyId");
    }

    // GSON Init
    GsonBuilder gsonBuilder = new GsonBuilder();
    mGson = gsonBuilder.create();

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(linearLayoutManager);

    mCommentsAdapter = new CommentsAdapter(getActivity());
    mRecyclerView.setAdapter(mCommentsAdapter);

    HackerStory hackerStory = mRealm.where(HackerStory.class)
        .equalTo("mId", mStoryId)
        .findFirst();
    if (hackerStory != null) {
      mCommentsArrayList.clear();
      String commentsArray = hackerStory.getCommentsJson();

      try {
        JSONArray array = new JSONArray(commentsArray);
        for (int i = 0; i < array.length(); i++) {
          String url = URLs.GET_STORY + array.getString(i) + URLs.PRINT_PRETTY;
          sendRetrieveCommentsRequest(url);
        }

      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return rootView;
  }


  private void sendRetrieveCommentsRequest(String url) {

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
        , url
        , null
        , new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        Comment comment = mGson.fromJson(response.toString(), Comment.class);

        CommentsPojo commentsPojo = new CommentsPojo();
        commentsPojo.setAuthor(comment.getBy());
        commentsPojo.setComment(comment.getText());
        commentsPojo.setDateTime(comment.getTime());

        mCommentsArrayList.add(commentsPojo);
        mCommentsAdapter.setArrayList(mCommentsArrayList);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });
    mRequestQueue.add(request);

  }

}
