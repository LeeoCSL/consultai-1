package br.com.carregai.carregai2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import br.com.carregai.carregai2.MainActivity;
import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.adapter.BrPhoneNumberFormatter;
import br.com.carregai.carregai2.model.User;
import br.com.carregai.carregai2.utils.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int GALLERY_PICK = 1;

    @BindView(R.id.input_login_register)
    TextInputLayout mEmail;

    @BindView(R.id.input_password_register)
    TextInputLayout mPassword;

    @BindView(R.id.input_name_register)
    TextInputLayout mName;

    @BindView(R.id.input_phone_register)
    TextInputLayout mPhone;

    @BindView(R.id.logo_register)
    CircleImageView mPhoto;

    private StorageReference mImageStorage;

    private DatabaseReference mUserDatabase;

    private ProgressDialog mDialog;

    private FirebaseAnalytics mFirebaseAnalytics;

    SharedPreferences sharedPref;

    private String userName, userEmail, userPassword, userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        String userID = FirebaseInstanceId.getInstance().getId();
        mUserDatabase = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userID);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        BrPhoneNumberFormatter addLineNumberFormatter = new BrPhoneNumberFormatter(new WeakReference<EditText>(mPhone.getEditText()));
        mPhone.getEditText().addTextChangedListener(addLineNumberFormatter);

        mAuth = FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(this);

        mEmail = (TextInputLayout)findViewById(R.id.input_login_register);
        mPassword = (TextInputLayout)findViewById(R.id.input_password_register);
    }

    public void handlerLogin(View v){
        finish();
    }

    public void handlerRegister(View v){
        validateDataFromEditText();
    }

    private void validateDataFromEditText(){
        userName = mName.getEditText().getText().toString().trim();
        userEmail = mEmail.getEditText().getText().toString().trim();
        userPassword = mPassword.getEditText().getText().toString().trim();
        userPhone = mPhone.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(userName)){
            mName.setError("O campo nome está vazio.");
            return;
        }

        if(!Utility.isEmailValid(userEmail)){
            mEmail.setError("Email inválido.");
            return;
        }

        if(userPassword.length() < 6){
            mPassword.setError("Sua senha deve ter no mínimo 6 caracteres.");
            return;
        }

        if(mPhone.getEditText().getText().toString().isEmpty()){
            mPhone.setError("Número de telefone vazio.");
            return;
        }
        createUser(userEmail, userPassword);
    }

    private void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

                        UserProfileChangeRequest profUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(authResult.getUser().getDisplayName())
                                .setPhotoUri(Uri.parse("https://api.adorable.io/avatars/285/"
                                        + authResult.getUser().getDisplayName() + ".png"))
                                .build();

                        authResult.getUser().updateProfile(profUpdate);

                        User user = new User();
                        user.setEmail(userEmail);
                        user.setName(userName);
                        user.setPhone(userPhone);

                        ref.setValue(user);

                        Bundle bundle  = new Bundle();
                        bundle.putString("email", userEmail);
                        bundle.putString("nome", userName);
                        mFirebaseAnalytics.logEvent("cadastro_ok", bundle);

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this,  "Algum erro ocorreu. Tente novamente.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
        UPLOAD DE FOTO
     */
     public void toChangeImage(View v){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageURI = data.getData();

            CropImage.activity(imageURI)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mDialog.setTitle("Aguarde...");
                mDialog.setMessage("Estamos carregando a sua imagem.");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

                Uri resultUri = result.getUri();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(resultUri).build();
                user.updateProfile(request);


                String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                final File thumbFilePath = new File(resultUri.getPath());

                Bitmap thumbNail;

                byte[] thumbBytes = null;

                try {
                    thumbNail =
                            new Compressor(this)
                                    .setMaxWidth(200)
                                    .setMaxHeight(200)
                                    .setQuality(75)
                                    .compressToBitmap(thumbFilePath);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumbNail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumbBytes = baos.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                StorageReference filePath = mImageStorage.child("profile_images")
                        .child(currentUserID +".jpg");

                final StorageReference thumbnailFilePath = mImageStorage.child("profile_images")
                        .child("thumbs").child(currentUserID + ".jpg");

                final byte[] finalThumbBytes = thumbBytes;

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            String downloadURL = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumbnailFilePath.putBytes(finalThumbBytes);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()){

                                    }
                                }
                            });

                            mUserDatabase.child("image").setValue(downloadURL)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Sua imagem foi salva.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro ao salvar sua imagem.", Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
