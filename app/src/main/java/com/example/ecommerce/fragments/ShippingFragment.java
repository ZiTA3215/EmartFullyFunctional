package com.example.ecommerce.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.actvities.ChangePassword;
import com.example.ecommerce.adapters.CategoryAdapter;
import com.example.ecommerce.adapters.ShippingAdapter;
import com.example.ecommerce.models.ShippingModel;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShippingFragment extends Fragment {


    ShippingAdapter shippingAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    RecyclerView recyclerView;

    LinearLayout linearLayout;

    private List<ShippingModel> shippingModelList;




    public ShippingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_shipping, container, false);


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


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();




        recyclerView = root.findViewById(R.id.shipping_rec);

        linearLayout = root.findViewById(R.id.shippinglayout);
        linearLayout.setVisibility(View.GONE);



        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        shippingModelList = new ArrayList<>();
        shippingAdapter = new ShippingAdapter(getActivity(), shippingModelList);
        recyclerView.setAdapter(shippingAdapter);
        shippingAdapter.notifyDataSetChanged();

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                        String documentId = doc.getId();


                        ShippingModel shippingModel = doc.toObject(ShippingModel.class);
                        shippingModel.setDocumentId(documentId);
                        shippingModelList.add(shippingModel);
                        shippingAdapter.notifyDataSetChanged();

                        linearLayout.setVisibility(View.VISIBLE);

                    }
                } else {

                    Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();

                }
            }
        });



        return root;



    }




    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }


}


















