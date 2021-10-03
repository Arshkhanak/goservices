package com.tecmanic.goservices.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.tecmanic.goservices.ModelClass.HomeCateModelClass;
/*
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;*/
import com.squareup.picasso.Picasso;
import com.tecmanic.goservices.SubCategoryActivity;
import com.tecmanic.goservices.R;

import static com.tecmanic.goservices.Extra.Config.IMAGE_URL;


public class HomeCategoryRecycleAdapter extends RecyclerView.Adapter<HomeCategoryRecycleAdapter.MyViewHolder> {

    Context context;
    private List<HomeCateModelClass> OfferList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);

        }
    }
    public HomeCategoryRecycleAdapter(Context context, List<HomeCateModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public HomeCategoryRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_homeprod_r2, parent, false);
        return new HomeCategoryRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final HomeCateModelClass lists = OfferList.get(position);

       /* Glide.with(context)
                .load(IMAGE_URL + lists.getCategory_image())
                .placeholder(R.drawable.ic_about)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);*/
        Picasso.with(context).load(IMAGE_URL + lists.getCategory_image()).error(R.drawable.ic_about).into(holder.image);

        holder.title.setText(lists.getCategory_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra("categoryid",lists.getCategory_id());
                intent.putExtra("layout",position);
                Log.d("pos", String.valueOf(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return OfferList.size();
    }
}




