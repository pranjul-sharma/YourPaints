package com.yourpaints.yourpaints;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yourpaints.yourpaints.model.Post;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WritePostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;
    public static final String USER_POST_PATH = "posts";
    public static final String USER_POSTS = "user_posts";
    private static final String TAG = WritePostActivity.class.getSimpleName();
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference imgStorageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference postDatabaseReference;
    FirebaseUser currentUser;
    Uri filePath;

    @BindView(R.id.iv_upload_img)
    ImageView uploadImg;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.action_add_photo)
    TextView addPhotoView;

    @BindView(R.id.btn_post)
    TextView button;

    @BindView(R.id.et_written_post)
    EditText etPostData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.write_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            imgStorageReference = firebaseStorage.getReference().child(USER_POSTS).child(currentUser.getUid()).child(USER_POST_PATH);
            postDatabaseReference = firebaseDatabase.getReference().child(USER_POSTS).child(currentUser.getUid()).child(USER_POST_PATH);
        }

        addPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (currentUser != null) {
                    String postData = etPostData.getText().toString();
                    if (!TextUtils.isEmpty(postData)) {
                        if (filePath != null) {
                            uploadImageToFirebaseAndPost(postData);
                        } else {
                            uploadPost(postData, null);
                        }
                    } else {
                        Snackbar.make(view, R.string.empty_invalid_post, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(view, R.string.login_first,Snackbar.LENGTH_SHORT)
                            .setAction(R.string.login, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setActionTextColor(Color.WHITE)
                            .show();
                }
            }
        });
    }

    public void uploadPost(String postContent, String imgDownloadUrl) {
        Post post = new Post(currentUser.getDisplayName(), postContent, imgDownloadUrl);
        postDatabaseReference.push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), R.string.success_post_toast, Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), R.string.failed_post_toast, Toast.LENGTH_LONG).show();
            }
        });

        Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void uploadImageToFirebaseAndPost(final String postContent) {
        if (!TextUtils.isEmpty(filePath.toString())) {
            final StorageReference photoRef = imgStorageReference.child(String.valueOf(new Date().getTime()));
            photoRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uploadPost(postContent, uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),R.string.error_url, Toast.LENGTH_SHORT).show();
                            photoRef.delete();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void showFileChooser() {
        Intent imageChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageChooserIntent.setType("image/jpeg");
        imageChooserIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(imageChooserIntent, getString(R.string.select_a_pic)), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImg.setImageBitmap(bmp);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
