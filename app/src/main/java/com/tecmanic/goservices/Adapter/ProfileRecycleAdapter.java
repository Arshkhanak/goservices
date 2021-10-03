package com.tecmanic.goservices.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.tecmanic.goservices.ModelClass.HomeCategoryModelClass;

import com.tecmanic.goservices.R;


public class ProfileRecycleAdapter extends RecyclerView.Adapter<ProfileRecycleAdapter.MyViewHolder> {

    Context context;


    private List<HomeCategoryModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView title;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            linear = (LinearLayout) view.findViewById(R.id.linear);


        }

    }


    public ProfileRecycleAdapter(Context context, List<HomeCategoryModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public ProfileRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_list, parent, false);


        return new ProfileRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final HomeCategoryModelClass lists = OfferList.get(position);
        holder.image.setImageResource(lists.getImage());
        holder.title.setText(lists.getTitle());

        if (position==7){

            holder.linear.setVisibility(View.GONE);
        }else {

            holder.linear.setVisibility(View.VISIBLE);

        }




    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


