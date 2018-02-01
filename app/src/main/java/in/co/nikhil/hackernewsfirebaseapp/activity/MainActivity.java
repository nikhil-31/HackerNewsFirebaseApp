package in.co.nikhil.hackernewsfirebaseapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import in.co.nikhil.hackernewsfirebaseapp.MyApplication;
import in.co.nikhil.hackernewsfirebaseapp.R;
import in.co.nikhil.hackernewsfirebaseapp.ultis.URLs;

public class MainActivity extends AppCompatActivity {

  @Inject
  RequestQueue mRequestQueue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ((MyApplication) getApplication()).getComponent().inject(this);


    sendNetworkRequest(URLs.TOP_STORIES);
  }

  private void sendNetworkRequest(String url) {

    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
        url,
        null,
        new Response.Listener<JSONArray>() {
          @Override
          public void onResponse(JSONArray response) {
            Toast.makeText(MainActivity.this, "Response " + response.toString(), Toast.LENGTH_SHORT).show();
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {


      }
    });
    mRequestQueue.add(request);

  }

}
