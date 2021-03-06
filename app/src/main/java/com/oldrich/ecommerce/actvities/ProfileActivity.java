package com.oldrich.ecommerce.actvities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.oldrich.ecommerce.R;
import com.oldrich.ecommerce.Webview;
import com.oldrich.ecommerce.models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profileimg;

    Button update;
    Button password;
    ImageView update2;
    EditText name, email;

    Uri imageURI;
    Toolbar toolbar;

    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    BottomNavigationView bottomNavigationView;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bottomNavigationView = findViewById(R.id.bottombar);
        bottomNavigationView.setItemIconTintList(null);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        update2 = findViewById(R.id.update2);
        update = findViewById(R.id.update);
        profileimg = findViewById(R.id.profile_image);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        email.setEnabled(false);
        password = findViewById(R.id.profile_password);




        bottomNavigationView = findViewById(R.id.bottombar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.messages:


                        startActivity(new Intent(ProfileActivity.this, Webview.class));

                        return true;

                    case R.id.home:


                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));

                        return true;


                    case R.id.account:

                        startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));


                        return true;

                    case R.id.shipping:

                        startActivity(new Intent(ProfileActivity.this, ShippingActivity.class));


                        return true;


                    default:
                        return false;
                }


            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        email.setText(userModel.getUseremail());
                        name.setText(userModel.getUsername());



                        Glide.with(getApplicationContext()).load(userModel.getProfileImg()).into(profileimg);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {



                    }
                });

        update2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");


            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadiamge();
                updateUserProfile();

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Users");

                String username = name.getText().toString();
                String useremail = email.getText().toString();



                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .child("username").setValue(username);
                Toast.makeText(getBaseContext(), "Your Account Has Been Updated", Toast.LENGTH_SHORT).show();








            }
        });



    }

    private void updateUserProfile() {


    }

    private void uploadiamge() {

        if (imageURI != null) {

            final StorageReference reference = storage.getReference().child("Profilepics/").child(FirebaseAuth.getInstance().getUid());


            reference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getBaseContext(), "Uploaded successfully your User account will be updated shortly", Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profileImg").setValue(uri.toString());
                            Toast.makeText(getBaseContext(), "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        profileimg.setImageURI(result);
                        imageURI = result;
                    }
                }
            });



    }
