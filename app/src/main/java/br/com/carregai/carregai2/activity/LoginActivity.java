package br.com.carregai.carregai2.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.carregai.carregai2.MainActivity;
import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.utils.DialogUtils;
import br.com.carregai.carregai2.utils.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.input_email)
    EditText mLogin;

    @BindView(R.id.input_password)
    EditText mPassword;

    private FirebaseAuth mAuth;

    private LoginButton mLoginFacebook;
    private CallbackManager mCallbackManager;

    private SignInButton mGoogleLogin;

    private ImageView mLoginGoogle;

    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };

        mLoginFacebook = (LoginButton)findViewById(R.id.login_fb);
        mGoogleLogin = (SignInButton)findViewById(R.id.login_google);


        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
     /*   GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // connection failed, should be handled
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void forgetPassword(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Email");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        builder.setView(input);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = input.getText().toString().trim();

                if(!TextUtils.isEmpty(email)){
                    DialogUtils.loadingDialog(LoginActivity.this);
                    resetPassword(email);
                    dialog.dismiss();
                }else{

                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void resetPassword(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DialogUtils.hideLoadingDialog();
                        Toast.makeText(LoginActivity.this, "Acabamos de te enviar as instruções de como recuperar sua conta.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DialogUtils.hideLoadingDialog();
                if (e.getClass() == FirebaseAuthInvalidUserException.class) {
                    Toast.makeText(LoginActivity.this,
                            "Usuário não encontrado", Toast.LENGTH_LONG).show();

                    return;
                }
                if (e.getClass() == FirebaseException.class) {
                    Toast.makeText(LoginActivity.this,
                            "Email inválido", Toast.LENGTH_LONG).show();
                }
                e.printStackTrace();
            }
        });
    }

    private void validateDateFromEditText(){
        userEmail = mLogin.getText().toString().trim();
        userPassword = mPassword.getText().toString().trim();

        if(!Utility.isEmailValid(userEmail)){
            mLogin.setError("Email inválido.");
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            mPassword.setError("Senha inválida.");
            return;
        }
        loginWithEmailAndPassword();
    }

    private void loginWithEmailAndPassword() {
        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void handlerLogin(View v){
        validateDateFromEditText();
    }

    public void register(View v){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void loginWithFacebook(View view) {
        mCallbackManager = CallbackManager.Factory.create();
        mLoginFacebook.setReadPermissions("email", "public_profile");
        mLoginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAcessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {


            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Erro: " +error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    private void handleFacebookAcessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        DialogUtils.loadingDialog(this);

        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        DialogUtils.hideLoadingDialog();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        DialogUtils.hideLoadingDialog();

                        if (e.getClass() == FirebaseAuthUserCollisionException.class) {
                            Toast.makeText(LoginActivity.this, "Este email está vinculado a uma outra conta com o facebook.", Toast.LENGTH_LONG).show();
                            LoginManager.getInstance().logOut();
                        }
                        if(e.getClass() == FirebaseAuthInvalidUserException.class){
                            Toast.makeText(LoginActivity.this, "Email não cadastrado no nosso sistema", Toast.LENGTH_LONG).show();
                        }
                        if(e.getClass() == FirebaseAuthInvalidCredentialsException.class){
                            Toast.makeText(LoginActivity.this, "Senha inválida", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Erro", Toast.LENGTH_LONG).show();
                        }
                        e.printStackTrace();
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            DialogUtils.loadingDialog(this);

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                DialogUtils.hideLoadingDialog();
            } else {
                DialogUtils.hideLoadingDialog();
            }
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}