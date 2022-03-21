package com.oldrich.ecommerce.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.oldrich.ecommerce.R;
import com.oldrich.ecommerce.fragments.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class ImageSliderAdapter extends SliderViewAdapter<SliderViewHolder> {
    HomeFragment context;
    int setTotalCount;
    String ImagesLink;

    public ImageSliderAdapter(HomeFragment context, int setTotalCount) {
        this.setTotalCount = setTotalCount;
        this.context = context;
        int setTotaltcount;
    }



    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_layout,parent, false);
        return  new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        FirebaseDatabase.getInstance().getReference("ImagesLinks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch (position){
                    case 0:
                        ImagesLink = snapshot.child("1").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;
                    case 1:
                        ImagesLink = snapshot.child("2").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;
                    case 2:
                        ImagesLink = snapshot.child("3").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;
                    case 3:
                        ImagesLink = snapshot.child("4").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;
                    case 4 :
                        ImagesLink = snapshot.child("5").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;
                    case 5:
                        ImagesLink = snapshot.child("6").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;
                    case 6:
                        ImagesLink = snapshot.child("7").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;
                    case 7:
                        ImagesLink = snapshot.child("8").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;
                    case 8:
                        ImagesLink = snapshot.child("9").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;

                    case 9:
                        ImagesLink = snapshot.child("10").getValue().toString();
                        Glide.with(viewHolder.itemView).load(ImagesLink).into(viewHolder.sliderimageview);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getCount() {
        return setTotalCount;
    }
}

class SliderViewHolder extends SliderViewAdapter.ViewHolder{
    ImageView sliderimageview;
    View itemview;


    public SliderViewHolder(View itemView) {
        super(itemView);
        this.itemview = itemView;
        sliderimageview = itemView.findViewById(R.id.slider_img);
    }
}