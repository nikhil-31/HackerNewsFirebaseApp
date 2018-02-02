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

import javax.inject.Inject;

import in.co.nikhil.hackernewsfirebaseapp.MyApplication;
import in.co.nikhil.hackernewsfirebaseapp.R;
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

  Realm mRealm;
  RealmConfiguration mRealmConfiguration;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Dagger
    ((MyApplication) getApplication()).getComponent().inject(this);

    mRealm = Realm.getInstance(getRealmConfiguration());
    RealmResults<HackerStory> hackerStories = mRealm
        .where(HackerStory.class)
        .findAll();





    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    mGson = gsonBuilder.create();

    sendTopStoriesRequest(URLs.TOP_STORIES);
  }

  public RealmConfiguration getRealmConfiguration(){
    if (mRealmConfiguration == null){
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
            Toast.makeText(MainActivity.this, "Response " + response.toString(), Toast.LENGTH_SHORT).show();

            for (int i = 0; i < response.length(); i++) {
              try {
                String storyId = response.getString(i);
                String url = URLs.GET_STORY + storyId + URLs.PRINT_PRETTY;

                sendRetrieveStoryDetailsRequest(url);
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
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
        Toast.makeText(MainActivity.this, "Response " + response.toString(), Toast.LENGTH_SHORT).show();

        Story story = mGson.fromJson(response.toString(), Story.class);


      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
      }
    });
    mRequestQueue.add(request);
  }


}
