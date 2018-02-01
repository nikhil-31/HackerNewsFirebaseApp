package in.co.nikhil.hackernewsfirebaseapp.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.nikhil.hackernewsfirebaseapp.R;

public class LoginActivity extends AppCompatActivity {

  private static final int RC_SIGN_IN = 1;

  private FirebaseAuth mFirebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;

  @BindView(R.id.sign_in_button)
  SignInButton mGoogleSignInButton;
  @BindView(R.id.login_username)
  TextInputEditText mLoginUserName;
  @BindView(R.id.login_password)
  TextInputEditText mLoginPassword;


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

    // Get the firebase instance
    mFirebaseAuth = FirebaseAuth.getInstance();

    // Sets the google sign in button to wide size
    mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);

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

  // Onclick method for login button
  public void checkLogin(View view) {

  }

  // Onclick method for Sign Up button
  public void signUp(View view) {

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
