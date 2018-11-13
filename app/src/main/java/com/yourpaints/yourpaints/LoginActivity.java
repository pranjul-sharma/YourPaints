package com.yourpaints.yourpaints;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yourpaints.yourpaints.model.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    public static final String USERS = "users";
    public static final String USER_DETAILS = "user_details";
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 100;
    @BindView(R.id.google_sign_in_btn)
    SignInButton signInButton;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.parent_layout)
    RelativeLayout relativeLayout;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userDataReference;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private boolean viaGoogleOrRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        setGoogleSignInButtonText();

        Slide slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(relativeLayout);
        slide.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.interpolator.linear_out_slow_in));
        slide.setDuration(2000);
        getWindow().setEnterTransition(slide);
    }

    private void setGoogleSignInButtonText() {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setText(R.string.common_signin_button_text_long);
                return;
            }
        }
    }

    private boolean isInvalidEmail(String email) {
        return TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    @OnClick(R.id.btn_login)
    public void btn_login(final View view) {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        if (isNetworkEnabled()) {
            processLogin(view, username, password);
        } else {
            Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_SHORT).show();
        }

    }

    public void processLogin(View view, String username, String password) {
        if (isInvalidEmail(username)) {
            Snackbar.make(view, R.string.invalid_username, Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Snackbar.make(view, R.string.invalid_password, Snackbar.LENGTH_SHORT).show();
        } else {
            signInWithEmail(view, username, password);
        }
    }

    private void signInWithEmail(final View view, final String username, final String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginUser(null);
                            callActivity();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Snackbar.make(view, R.string.invalid_user, Snackbar.LENGTH_INDEFINITE)
                                        .setAction(R.string.register, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final Dialog dialog = new Dialog(LoginActivity.this);
                                                dialog.setContentView(R.layout.layout_name_input_dialog);
                                                dialog.setCancelable(false);
                                                TextView tvOk = (TextView) dialog.findViewById(R.id.btn_continue);
                                                TextView tvCancel = (TextView) dialog.findViewById(R.id.btn_cancel);

                                                tvCancel.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.cancel();
                                                        Snackbar.make(view, R.string.register_failed, Snackbar.LENGTH_SHORT).show();
                                                    }
                                                });

                                                tvOk.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        EditText editText = dialog.findViewById(R.id.et_full_name);
                                                        if (!TextUtils.isEmpty(editText.getText().toString())) {
                                                            String fullName = editText.getText().toString().trim();
                                                            addUserToFirebaseUsers(fullName, username, password);
                                                        } else {
                                                            TextInputLayout firstnameLayout = dialog.findViewById(R.id.til_full_name);
                                                            firstnameLayout.setError(getString(R.string.empty_name));
                                                        }

                                                        dialog.cancel();
                                                    }
                                                });

                                                if (!dialog.isShowing())
                                                    dialog.show();
                                            }
                                        })
                                        .setActionTextColor(Color.rgb(255, 87, 34)).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Snackbar.make(view, R.string.invalid_credentails, Snackbar.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.v(TAG, e.getMessage());
                                Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    public void addUserToFirebaseUsers(final String fullname, final String username, String password) {
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            viaGoogleOrRegister = true;
                            loginUser(fullname);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void callActivity() {
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.google_sign_in_btn)
    public void google_sign_in_btn(View view) {
        Intent sinInIntent = mGoogleSignInClient.getSignInIntent();
        Log.v(TAG, "in on sign in clicked calling intent");
        startActivityForResult(sinInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                Log.v(TAG, "in on activity result");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                Log.w(TAG, "Gooel sign in failed", e);
                Toast.makeText(this, "google signin failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            viaGoogleOrRegister = true;
                            loginUser(null);
                        } else {
                            Toast.makeText(getApplicationContext(), "auth failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void loginUser(final String fullName) {
        final FirebaseUser user = mAuth.getCurrentUser();

        //userDataReference = firebaseDatabase.getReference().child(USERS).child(user.getUid()).child(USER_DETAILS);
        User user1 = new User(user.getEmail(),user.getUid(),user.getDisplayName());
        String userLocation = "/" + USERS + "/" + user.getUid() + "/" + USER_DETAILS + "/";
        final String usernameLocation = "/" + USERS + "/" + user.getUid() + "/" + USER_DETAILS + "/userName/";
        final Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(userLocation, user1);

        if (viaGoogleOrRegister) {
            firebaseDatabase.getReference().updateChildren(childUpdates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            childUpdates.clear();
                            if (!TextUtils.isEmpty(fullName)) {
                                childUpdates.put(usernameLocation, fullName);
                            }
                            firebaseDatabase.getReference().updateChildren(childUpdates);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),R.string.something_went_wrong_db,Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }
            });
        }
        Toast.makeText(getApplicationContext(), getString(R.string.success_login_gmail, user.getEmail()), Toast.LENGTH_SHORT).show();
        callActivity();

    }

    private boolean isNetworkEnabled() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(LoginActivity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
