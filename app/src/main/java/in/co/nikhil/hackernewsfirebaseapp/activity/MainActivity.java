package in.co.nikhil.hackernewsfirebaseapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import in.co.nikhil.hackernewsfirebaseapp.data.realm.HackerStory;
import in.co.nikhil.hackernewsfirebaseapp.data.remoteModel.Story;
import in.co.nikhil.hackernewsfirebaseapp.ultis.URLs;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements StoryRealmAdapter.itemClick {
  private static final String TAG = "MainActivity";

  @Inject
  RequestQueue mRequestQueue;

  private Gson mGson;
  private FirebaseAuth mFirebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;

  @BindView(R.id.story_recycler_view)
  RealmRecyclerView mRealmRecyclerView;
  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @Inject
  Realm mRealm;

  @Override
  protected void onStart() {
    super.onStart();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    // Firebase init
    mFirebaseAuth = FirebaseAuth.getInstance();

    setSupportActionBar(mToolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.top_stories);
    }

    // Dagger
    ((MyApplication) getApplication()).getComponent().inject(this);

    // Gson Init
    GsonBuilder gsonBuilder = new GsonBuilder();
    mGson = gsonBuilder.create();

    // Realm Init
    RealmResults<HackerStory> hackerStories = mRealm
        .where(HackerStory.class)
        .findAll();

    // Checks whether the user is authenticated
    mAuthStateListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
          Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
          loginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
          startActivity(loginIntent);
        }
      }
    };

    // Realm recycler view adapter
    StoryRealmAdapter storyRealmAdapter = new StoryRealmAdapter(this
        , hackerStories
        , true
        , true);
    storyRealmAdapter.setItemClick(this);
    mRealmRecyclerView.setAdapter(storyRealmAdapter);
    mRealmRecyclerView.setOnRefreshListener(new RealmRecyclerView.OnRefreshListener() {
      @Override
      public void onRefresh() {
        sendTopStoriesRequest(URLs.TOP_STORIES);
      }
    });
    sendTopStoriesRequest(URLs.TOP_STORIES);
  }

  private void sendTopStoriesRequest(String url) {
    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET
        , url
        , null
        , new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray response) {
        mRealmRecyclerView.setRefreshing(false);
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
        mRealmRecyclerView.setRefreshing(false);
      }
    });
    mRequestQueue.add(request);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  private void sendRetrieveStoryDetailsRequest(String url) {
    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
        , url
        , null
        , new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        final Story story = mGson.fromJson(response.toString(), Story.class);
        String kidsString = "";
        try {
          kidsString = response.getString("kids");
        } catch (JSONException e) {
          e.printStackTrace();
        }
        final String finalKidsString = kidsString;
        mRealm.executeTransactionAsync(new Realm.Transaction() {
          @Override
          public void execute(Realm realm) {
            HackerStory hackerStory = realm.createObject(HackerStory.class, story.getId());
            hackerStory.setArticleTitle(story.getTitle());
            hackerStory.setScore(story.getScore().toString());
            hackerStory.setUrl(story.getUrl());
            hackerStory.setDatetime(story.getTime().toString());
            hackerStory.setSubmitter(story.getBy());
            hackerStory.setCommentsJson(finalKidsString);
            List<Integer> kids = story.getKids();
            String commentsSize = String.valueOf(kids.size());
            hackerStory.setComments(commentsSize);
            realm.copyToRealmOrUpdate(hackerStory);
          }
        });

      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    });
    mRequestQueue.add(request);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_logout) {
      logout();
    }
    return super.onOptionsItemSelected(item);
  }

  private void logout() {
    mFirebaseAuth.signOut();
  }

  @Override
  public void itemClicked(HackerStory story) {
    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
    intent.putExtra("storyId", story.getId());
    startActivity(intent);
  }
}
