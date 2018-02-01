package in.co.nikhil.hackernewsfirebaseapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
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

  private ProgressDialog mProgress;

  @Override
  protected void onStart() {
    super.onStart();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    // Get the firebase instance
    mFirebaseAuth = FirebaseAuth.getInstance();

    // Sets the google sign in button to wide size
    mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);

    // Initialize Progress Dialog
    mProgress = new ProgressDialog(this);

    // Firebase Authentication Variables
    mAuthStateListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
          Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
          mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(mainIntent);
        }
      }
    };

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
                  Toast.makeText(LoginActivity.this, getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
                } catch (FirebaseAuthInvalidUserException e) {
                  Toast.makeText(LoginActivity.this, getString(R.string.please_sign_up), Toast.LENGTH_SHORT).show();
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
    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
    startActivity(intent);
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
