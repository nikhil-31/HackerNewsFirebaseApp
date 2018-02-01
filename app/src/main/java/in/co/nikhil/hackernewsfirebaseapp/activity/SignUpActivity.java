package in.co.nikhil.hackernewsfirebaseapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.nikhil.hackernewsfirebaseapp.R;

public class SignUpActivity extends AppCompatActivity {

  @BindView(R.id.sign_up_username)
  TextInputEditText mSignUpUserName;
  @BindView(R.id.sign_up_password)
  TextInputEditText mSignUpPassword;

  private FirebaseAuth mFirebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    ButterKnife.bind(this);

    // Firebase Authentication Variables
    mFirebaseAuth = FirebaseAuth.getInstance();

  }

  public void createAccount(View view) {

    String email = mSignUpUserName.getText().toString().trim();
    String password = mSignUpPassword.getText().toString().trim();

    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

      mFirebaseAuth.createUserWithEmailAndPassword(email, password)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                String userId = mFirebaseAuth.getCurrentUser().getUid();

                Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);

              } else if (!task.isSuccessful()) {
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                  Toast.makeText(SignUpActivity.this, R.string.user_exists, Toast.LENGTH_SHORT).show();
                }
                if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                  Toast.makeText(SignUpActivity.this, R.string.please_select_strong_password, Toast.LENGTH_SHORT).show();
                }
//                mProgress.dismiss();
              }


            }
          });
    } else {
      Toast.makeText(this, getString(R.string.all_fields_required), Toast.LENGTH_SHORT).show();
    }


  }

}
