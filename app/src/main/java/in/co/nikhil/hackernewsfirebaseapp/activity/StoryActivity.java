package in.co.nikhil.hackernewsfirebaseapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.nikhil.hackernewsfirebaseapp.R;

public class StoryActivity extends AppCompatActivity {
  private static final String TAG = "StoryActivity";

  private static final int RC_SIGN_IN = 1;

  private FirebaseAuth mFirebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;

  @BindView(R.id.sign_in_button)
  SignInButton mGoogleSignInButton;
  @BindView(R.id.login_username)
  TextInputEditText mLoginUserName;
  @BindView(R.id.login_password)
  TextInputEditText mLoginPassword;

  private ProgressDialog mProgress;

  // Google Login
  private GoogleSignInClient mGoogleSignInClient;

//  @Override
//  protected void onStart() {
//    super.onStart();
//    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    // Get the firebase instance
//    mFirebaseAuth = FirebaseAuth.getInstance();
//
//    // Sets the google sign in button to wide size
//    mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);
//
//    // Initialize Progress Dialog
//    mProgress = new ProgressDialog(this);

    // TODO - Uncomment this code afterwards
    // Firebase Authentication Variables
//    mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//      @Override
//      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if (user != null) {
//          Intent mainIntent = new Intent(StoryActivity.this, MainActivity.class);
//          mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//          startActivity(mainIntent);
//        }
//      }
//    };

    Intent mainIntent = new Intent(StoryActivity.this, MainActivity.class);
    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(mainIntent);

//    mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        googleSignIn();
//      }
//    });
//
//    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(getString(R.string.default_web_client_id))
//        .requestEmail()
//        .build();
//
//    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

  }

  // Starts the google Sign In
  private void googleSignIn() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      mProgress.setTitle("Logging you in..");
      mProgress.show();
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account);
      } catch (ApiException e) {
        // Google Sign In failed, update UI appropriately
        Log.w(TAG, "Google sign in failed", e);
        // [START_EXCLUDE]
        Toast.makeText(this, "Google Sign In failed, Please Try Again", Toast.LENGTH_SHORT).show();
        // [END_EXCLUDE]
        mProgress.dismiss();
      }
    }
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
          // Sign in success, update UI with the signed-in user's information
          Log.d(TAG, "signInWithCredential:success");
          FirebaseUser user = mFirebaseAuth.getCurrentUser();
        } else {
          Log.w(TAG, "signInWithCredential:failure", task.getException());
        }
        mProgress.dismiss();
      }
    });
  }

  // Onclick method for login button
  public void checkLogin(View view) {
    String email = mLoginUserName.getText().toString().trim();
    String password = mLoginPassword.getText().toString().trim();

    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
      mProgress.setTitle("Logging you in..");
      mProgress.show();

      mFirebaseAuth.signInWithEmailAndPassword(email, password)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                mProgress.dismiss();
              } else if (!task.isSuccessful()) {
                try {
                  throw task.getException();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                  Toast.makeText(StoryActivity.this, getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
                } catch (FirebaseAuthInvalidUserException e) {
                  Toast.makeText(StoryActivity.this, getString(R.string.please_sign_up), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                  e.printStackTrace();
                }
                mProgress.dismiss();
              }
            }
          });
    }
  }

  // Onclick method for Sign Up button
  public void signUp(View view) {
    Intent intent = new Intent(StoryActivity.this, SignUpActivity.class);
    startActivity(intent);
  }


//  @Override
//  protected void onResume() {
//    super.onResume();
//    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//  }
//
//  @Override
//  protected void onPause() {
//    super.onPause();
//    mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
//  }


}
