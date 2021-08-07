package com.example.ecommerce.actvities;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerce.R;
import com.example.ecommerce.adapters.ShowAllAdapter;
import com.example.ecommerce.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        bottomNavigationView = findViewById(R.id.bottombar);

        toolbar = findViewById(R.id.show_all_toolbar);
        bottomNavigationView = findViewById(R.id.bottombar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        //image slider
        ImageSlider imageSlider = findViewById(R.id.image_slider2);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.emart2, "Discount On All Items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.shop1, "Everything Online", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.shop2, "Zero Fees", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);





        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        String type = getIntent().getStringExtra("type");

        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this,showAllModelList);
        recyclerView.setAdapter(showAllAdapter);


        if (type == null || type.isEmpty()){

            firestore.collection("ShowAll")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()){
                                for (DocumentSnapshot doc:task.getResult().getDocuments()){

                                    ShowAllModel showAllModel=doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });


        }

        if (type !=null && type.equalsIgnoreCase("Men")){

            firestore.collection("ShowAll").whereEqualTo("type", "men")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()){
                                for (DocumentSnapshot doc:task.getResult().getDocuments()){

                                    ShowAllModel showAllModel=doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });


        }if (type !=null && type.equalsIgnoreCase("Woman")){

            firestore.collection("ShowAll").whereEqualTo("type", "woman")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()){
                                for (DocumentSnapshot doc:task.getResult().getDocuments()){

                                    ShowAllModel showAllModel=doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });


        } if (type !=null && type.equalsIgnoreCase("Pets")){

        firestore.collection("ShowAll").whereEqualTo("type", "pets")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){
                            for (DocumentSnapshot doc:task.getResult().getDocuments()){

                                ShowAllModel showAllModel=doc.toObject(ShowAllModel.class);
                                showAllModelList.add(showAllModel);
                                showAllAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });


    }if (type !=null && type.equalsIgnoreCase("camera")){

            firestore.collection("ShowAll").whereEqualTo("type", "camera")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()){
                                for (DocumentSnapshot doc:task.getResult().getDocuments()){

                                    ShowAllModel showAllModel=doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });



        }if (type !=null && type.equalsIgnoreCase("Tech")){

            firestore.collection("ShowAll").whereEqualTo("type", "tech")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()){
                                for (DocumentSnapshot doc:task.getResult().getDocuments()){

                                    ShowAllModel showAllModel=doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });


        }


    }
}