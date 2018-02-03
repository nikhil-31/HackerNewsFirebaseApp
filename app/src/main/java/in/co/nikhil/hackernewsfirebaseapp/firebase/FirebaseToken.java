package in.co.nikhil.hackernewsfirebaseapp.firebase;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by nikhil on 08-08-2017.
 */

public class FirebaseToken extends FirebaseInstanceIdService {
  private static final String LOG_TAG = FirebaseToken.class.getSimpleName();
  public static final String PREFS_NAME = "MyPrefsFile";

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();

  }


}
