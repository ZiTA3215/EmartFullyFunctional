package com.oldrich.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oldrich.ecommerce.R;
import com.oldrich.ecommerce.models.ShippingModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ShippingAdapter extends RecyclerView.Adapter<ShippingAdapter.ViewHolder> {

    Context context;
    List<ShippingModel> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public ShippingAdapter (Context context, List<ShippingModel> list) {
        this.context = context;
        this.list = list;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shipping_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getShippingname());
        holder.tracking.setText(list.get(position).getTracking());
        holder.tracking2.setText(list.get(position).getTrackingTitle());
        Glide.with(context).load(list.get(position).getShippinglogourl()).into(holder.shipping_logo);
        Glide.with(context).load(list.get(position).getShippingurl()).into(holder.shipping_round);
    }

    @Override
    public  int getItemCount(){return list.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,tracking, tracking2;
        ImageView shipping_round, shipping_logo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.shipping_name);
            tracking =itemView.findViewById(R.id.tracking);
            tracking2 = itemView.findViewById(R.id.tracking1);

            shipping_round= itemView.findViewById(R.id.shipping_round);
            shipping_logo= itemView.findViewById(R.id.shipping_logo);

        }
    }
}
