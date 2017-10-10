package br.com.carregai.carregai2.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.model.User;
import br.com.carregai.carregai2.utils.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;

    @BindView(R.id.cv_user_logo)
    CircleImageView mImageView;

    /*@BindView(R.id.tv_saldo_atual)
    TextView mSaldoAtual;

    @BindView(R.id.tv_recarga)
    TextView mRecarga;

    @BindView(R.id.tv_gasto_diario)
    TextView mGastoDiario;*/

    @BindView(R.id.settings_toolbar)
    Toolbar mToolbar;

    private ProgressDialog mDialog;

    private StorageReference mImageStorage;

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private User user;

    // informações pessoais do usuário
    @BindView(R.id.et_user_name)
    TextView mUserName;

    @BindView(R.id.et_user_email)
    TextView mUserEmail;

    @BindView(R.id.spinner)
    Spinner mSpinner;

    private String[] gender = new String[]{"Masculino", "Feminino"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Configurações");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = EventBus.getDefault().removeStickyEvent(User.class);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerArrayAdapter);

        if(user != null){
            mUserName.setText(user.getName());
            mUserEmail.setText(user.getEmail());
        }

        mDialog = new ProgressDialog(this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        String currentUserID = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUserID);

      /*  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mSaldoAtual.setText(Utility.formatValue(sp.getFloat("saldo_atual", 0)));
        mRecarga.setText(Utility.formatValue(sp.getFloat("valor_recarga", 0)));
        mGastoDiario.setText(Utility.formatValue(sp.getFloat("valor_diario", 0)));*/

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue(String.class);

                if(image != null){
                    Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            mImageView.setImageResource(R.drawable.ic_avatar);
                        }
                    });
                    builder.downloader(new OkHttpDownloader(getApplicationContext()));
                    builder.build().load(image).into(mImageView);

/*
                    Picasso.with(getApplicationContext()).
                            load(image).
                            into(mImageView);*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void handlerViagemExtra(View v){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Viagem Extra");
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        final CurrencyEditText input = new CurrencyEditText(this, null);
        input.setText(Utility.formatValue(sp.getFloat("viagem_extra", 0)));
        builder.setView(input);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putFloat("viagem_extra", Utility.stringToFloat(input.getText().toString()));
                editor.commit();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

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
