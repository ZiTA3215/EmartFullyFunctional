package com.example.ecommerce.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.actvities.ChangePassword;
import com.example.ecommerce.actvities.ProfileActivity;
import com.example.ecommerce.actvities.ShowAllActivity;
import com.example.ecommerce.actvities.StartActivity;
import com.example.ecommerce.adapters.CategoryAdapter;
import com.example.ecommerce.adapters.ImageSliderAdapter;
import com.example.ecommerce.adapters.NewProductsAdapter;
import com.example.ecommerce.adapters.PopularProductAdapter;
import com.example.ecommerce.adapters.ShowAllAdapter;
import com.example.ecommerce.models.CategoryModel;
import com.example.ecommerce.models.NewProductsModel;
import com.example.ecommerce.models.PopularProductModel;
import com.example.ecommerce.models.ShowAllModel;
import com.example.ecommerce.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    CircleImageView profileimg;

    Button update, changepassword;
    Button password;
    ImageView update2;
    EditText name, email;

    Uri imageURI;
    Toolbar toolbar;

    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_profile, container, false);

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        changepassword = root.findViewById(R.id.profile_password);
        update2 = root.findViewById(R.id.update2);
        update = root.findViewById(R.id.update);
        profileimg = root.findViewById(R.id.profile_image);
        name = root.findViewById(R.id.profile_name);
        email = root.findViewById(R.id.profile_email);
        email.setEnabled(false);
        password = root.findViewById(R.id.profile_password);
        // Getting application context
        Context context = getActivity();


        if (!isConnected()) {
            new AlertDialog.Builder(getActivity())
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection Error")
                    .setMessage("Please Check Your Internet Connection\n  .Turn On WIFI\n  .Turn On Mobile Data\n  .Try Again Later")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity();
                        }
                    })
                    .show();
        } else {


        }

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        email.setText(userModel.getUseremail());
                        name.setText(userModel.getUsername());


                        Glide.with(getContext()).load(userModel.getProfileImg()).into(profileimg);


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
                Toast.makeText(getActivity(),"Your account has been updated",Toast.LENGTH_SHORT).show();


            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePassword.class);
                startActivity(intent);

            }
        });


        return root;

    }

    private void updateUserProfile() {


    }

    private void uploadiamge() {

        if (imageURI != null) {

            final StorageReference reference = storage.getReference().child("Profilepics/").child(FirebaseAuth.getInstance().getUid());


            reference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(),"Uploaded successfully your account will be updated shortly! ",Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profileImg").setValue(uri.toString());
                            Toast.makeText(getActivity(),"Profile picture uploaded!",Toast.LENGTH_SHORT).show();
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
                    if (result != null) {
                        profileimg.setImageURI(result);
                        imageURI = result;
                    }
                }
            });


    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }


}


















