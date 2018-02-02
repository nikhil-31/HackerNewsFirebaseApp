package in.co.nikhil.hackernewsfirebaseapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import in.co.nikhil.hackernewsfirebaseapp.MyApplication;
import in.co.nikhil.hackernewsfirebaseapp.R;
import in.co.nikhil.hackernewsfirebaseapp.adapter.StoryRealmAdapter;
import in.co.nikhil.hackernewsfirebaseapp.data.HackerStory;
import in.co.nikhil.hackernewsfirebaseapp.data.remoteModel.Story;
import in.co.nikhil.hackernewsfirebaseapp.ultis.URLs;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";

  @Inject
  RequestQueue mRequestQueue;

  private Gson mGson;

  @BindView(R.id.story_recycler_view)
  RealmRecyclerView mRealmRecyclerView;

  Realm mRealm;
  RealmConfiguration mRealmConfiguration;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    // Dagger
    ((MyApplication) getApplication()).getComponent().inject(this);
    GsonBuilder gsonBuilder = new GsonBuilder();
    mGson = gsonBuilder.create();

    mRealm = Realm.getInstance(getRealmConfiguration());
    RealmResults<HackerStory> hackerStories = mRealm
        .where(HackerStory.class)
        .findAll();


    StoryRealmAdapter storyRealmAdapter = new StoryRealmAdapter(this
        , hackerStories
        , true
        , true);
    mRealmRecyclerView.setAdapter(storyRealmAdapter);

    sendTopStoriesRequest(URLs.TOP_STORIES);
  }

  public RealmConfiguration getRealmConfiguration() {
    if (mRealmConfiguration == null) {
      mRealmConfiguration = new RealmConfiguration
          .Builder()
          .deleteRealmIfMigrationNeeded()
          .build();
    }
    return mRealmConfiguration;
  }


  private void sendTopStoriesRequest(String url) {

    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
        url,
        null,
        new Response.Listener<JSONArray>() {
          @Override
          public void onResponse(JSONArray response) {
            for (int i = 0; i < response.length(); i++) {
              try {

                long storyId = response.getLong(i);
                String url = URLs.GET_STORY + storyId + URLs.PRINT_PRETTY;
                HackerStory hackerStory1 = mRealm.where(HackerStory.class)
                    .equalTo("mId", storyId)
                    .findFirst();

                if (hackerStory1 == null) {
                  sendRetrieveStoryDetailsRequest(url);
                }

              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
//        Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
      }
    });
    mRequestQueue.add(request);
  }

  private void sendRetrieveStoryDetailsRequest(String url) {

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
        , url
        , null
        , new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        Log.v(TAG, "Response" + response.toString());

        Story story = mGson.fromJson(response.toString(), Story.class);
        Log.v(TAG, "Story id - " + story.getId());

        mRealm.beginTransaction();
        HackerStory hackerStory = mRealm.createObject(HackerStory.class, story.getId());
        hackerStory.setArticleTitle(story.getTitle());
        hackerStory.setScore(story.getScore().toString());
        hackerStory.setUrl(story.getUrl());
        hackerStory.setDatetime(story.getTime().toString());
        hackerStory.setSubmitter(story.getBy());
        List<Integer> kids = story.getKids();
        String commentsSize = String.valueOf(kids.size());
        hackerStory.setComments(commentsSize);
        mRealm.commitTransaction();
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
//        Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
      }
    });
    mRequestQueue.add(request);
  }


}
