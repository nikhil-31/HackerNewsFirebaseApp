package in.co.nikhil.hackernewsfirebaseapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.co.nikhil.hackernewsfirebaseapp.R;

public class MainActivity extends AppCompatActivity {

  private static final int RC_SIGN_IN = 1;

  private FirebaseAuth mFirebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;

  @Override
  protected void onStart() {
    super.onStart();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mFirebaseAuth = FirebaseAuth.getInstance();

    // Firebase Authentication Variables
    mAuthStateListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
//          setLogin();

        }
      }
    };


  }

  public void checkLogin(View view) {

  }


  @Override
  protected void onResume() {
    super.onResume();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
  }

  @Override
  protected void onPause() {
    super.onPause();
    mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
  }


}
