package com.example.ecommerce.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerce.R;
import com.example.ecommerce.actvities.ShowAllActivity;
import com.example.ecommerce.adapters.CategoryAdapter;
import com.example.ecommerce.adapters.ImageSliderAdapter;
import com.example.ecommerce.adapters.NewProductsAdapter;
import com.example.ecommerce.adapters.PopularProductAdapter;
import com.example.ecommerce.adapters.ShowAllAdapter;
import com.example.ecommerce.models.CategoryModel;
import com.example.ecommerce.models.NewProductsModel;
import com.example.ecommerce.models.PopularProductModel;
import com.example.ecommerce.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
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
    EditText search_box;
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

        db = FirebaseFirestore.getInstance();

        sliderView = root.findViewById(R.id.imageSlider);

        progressDialog = new ProgressBar(getActivity());
        catRecyclerview = root.findViewById(R.id.rec_category);
        newProductRecyclerview = root.findViewById(R.id.new_product_rec);
        popularRecyclerview = root.findViewById(R.id.popular_rec);
        catShowAll = root.findViewById(R.id.popular_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);
       newProductShowAll = root.findViewById(R.id.newProducts_see_all);
       catShowAll = root.findViewById(R.id.category_see_all);
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


       catShowAll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           Intent intent = new Intent(getContext(), ShowAllActivity.class);
           startActivity(intent);
           }


       });

        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }


        });

        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
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

        ///////SearchView
        recyclerViewSearch = root.findViewById(R.id.search_rec);
        search_box = root.findViewById(R.id.search_box);
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(getContext(),showAllModelList);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(showAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty()){
                    showAllModelList.clear();
                    showAllAdapter.notifyDataSetChanged();
                }else{

                    searchProdcut(s.toString());
                }

            }
        });

        return root;
    }

    private void searchProdcut(String type) {

        if (!type.isEmpty()){
            db.collection("AllProducts").whereGreaterThanOrEqualTo("name", type).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful() && task.getResult()!= null){

                                showAllModelList.clear();
                                showAllAdapter.notifyDataSetChanged();
                                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                    });
        }
    }


}