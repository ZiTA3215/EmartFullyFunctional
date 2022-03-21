package com.oldrich.ecommerce.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oldrich.ecommerce.R;
import com.oldrich.ecommerce.actvities.ShowAllActivity2;
import com.oldrich.ecommerce.adapters.CategoryAdapter;
import com.oldrich.ecommerce.adapters.ImageSliderAdapter;
import com.oldrich.ecommerce.adapters.NewProductsAdapter;
import com.oldrich.ecommerce.adapters.PopularProductAdapter;
import com.oldrich.ecommerce.adapters.ShowAllAdapter;
import com.oldrich.ecommerce.models.CategoryModel;
import com.oldrich.ecommerce.models.NewProductsModel;
import com.oldrich.ecommerce.models.PopularProductModel;
import com.oldrich.ecommerce.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {



    TextView catShowAll, popularShowAll, newProductShowAll;

    SliderView sliderView;
    int TotalCounts;

    LinearLayout linearLayout;

    ProgressBar progressDialog;

    RecyclerView catRecyclerview , newProductRecyclerview, popularRecyclerview;


    //Category recyclerview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    ///////////////////////////Search View
   Button search_box;
    private List<ShowAllModel> showAllModelList;
    private RecyclerView recyclerViewSearch;
    private ShowAllAdapter showAllAdapter;

    //new products Recycler view
    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;

    //PopularProducts

    PopularProductAdapter popularProductAdapter;
    List<PopularProductModel> popularProductModelList;


    //Firestore
    FirebaseFirestore db;


    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);

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

        db = FirebaseFirestore.getInstance();


        search_box = root.findViewById(R.id.search_box);

        sliderView = root.findViewById(R.id.imageSlider);

        progressDialog = new ProgressBar(getActivity());
        catRecyclerview = root.findViewById(R.id.rec_category);
        newProductRecyclerview = root.findViewById(R.id.new_product_rec);
        popularRecyclerview = root.findViewById(R.id.popular_rec);
        catShowAll = root.findViewById(R.id.popular_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);
       newProductShowAll = root.findViewById(R.id.newProducts_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);
        newProductShowAll = root.findViewById(R.id.newProducts_see_all);

        FirebaseDatabase.getInstance().getReference("ImagesLinks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long counts = snapshot.getChildrenCount();

                TotalCounts= counts.intValue();

                sliderView.setSliderAdapter(new ImageSliderAdapter(HomeFragment.this,TotalCounts));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        search_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowAllActivity2.class);
                startActivity(intent);


            }
        });


       catShowAll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           Intent intent = new Intent(getContext(), ShowAllActivity2.class);
           startActivity(intent);
           }


       });

        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity2.class);
                startActivity(intent);
            }


        });

        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity2.class);
                startActivity(intent);
            }


        });


        linearLayout = root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE);




        //Category
        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();

                                linearLayout.setVisibility(View.VISIBLE);




                            }
                        } else {

                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        //New Products
        newProductRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        newProductsModelList = new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getContext(),newProductsModelList);
        newProductRecyclerview.setAdapter(newProductsAdapter);

        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                NewProductsModel newProductsModel = document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                                newProductsAdapter.notifyDataSetChanged();


                            }
                        } else {

                            Toast.makeText(getActivity(),""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


        //Popular Products
       popularRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
       popularProductModelList = new ArrayList<>();
       popularProductAdapter= new PopularProductAdapter(getContext(),popularProductModelList);
        popularRecyclerview.setAdapter(popularProductAdapter);


        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                PopularProductModel popularProductModel = document.toObject(PopularProductModel.class);
                               popularProductModelList.add(popularProductModel);
                                popularProductAdapter.notifyDataSetChanged();


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