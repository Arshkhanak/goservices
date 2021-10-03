package com.tecmanic.goservices.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.tecmanic.goservices.ModelClass.SubCatChildModelclass;

/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;*/
import com.squareup.picasso.Picasso;
import com.tecmanic.goservices.R;

import static com.tecmanic.goservices.Extra.Config.IMAGE_URL;


public class CategoryHorizontalRecycleAdapter extends RecyclerView.Adapter<CategoryHorizontalRecycleAdapter.MyViewHolder> {

    Context context;

    private List<SubCatChildModelclass> OfferList;
    int myPos = 0;
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


    public CategoryHorizontalRecycleAdapter(Context context, List<SubCatChildModelclass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public CategoryHorizontalRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_horizontal_list, parent, false);


        return new CategoryHorizontalRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final SubCatChildModelclass lists = OfferList.get(position);

        holder.title.setText(lists.getChild_name());
        Picasso.with(context).load(IMAGE_URL + lists.getChild_img()).error(R.drawable.ic_about).into(holder.image);

       /* Glide.with(context)
                .load(IMAGE_URL + lists.getChild_img())
                .placeholder(R.drawable.ic_about)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);*/

        if (myPos == position){
            holder.title.setTextColor(Color.parseColor("#ffffff"));//38393f
           // holder.linear.setBackgroundColor(Color.parseColor("#00000000"));
            holder.title.setBackgroundResource(R.drawable.rectangular_fullblack);

        }
        else {

            holder.title.setTextColor(Color.parseColor("#000000"));//acacac
           // holder.linear.setBackgroundColor(Color.parseColor("#90ffffff"));
            holder.title.setBackgroundResource(R.drawable.rectangular_black);

        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myPos=position;
                notifyDataSetChanged();

              /*  if (myPos == position){
                    holder.title.setTextColor(Color.parseColor("#ffffff"));//38393f
                    // holder.linear.setBackgroundColor(Color.parseColor("#00000000"));
                    holder.title.setBackgroundResource(R.drawable.rectangular_fullblack);

                }
                else {

                    holder.title.setTextColor(Color.parseColor("#000000"));//acacac
                    // holder.linear.setBackgroundColor(Color.parseColor("#90ffffff"));
                    holder.title.setBackgroundResource(R.drawable.rectangular_black);

                }
                if(myPos==0) {
                    myPos = position;

                }else{
                    myPos=position;
                    notifyDataSetChanged();
                }*/
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myPos=position;
                notifyDataSetChanged();

              /*  if (myPos == position){
                    holder.title.setTextColor(Color.parseColor("#ffffff"));//38393f
                    // holder.linear.setBackgroundColor(Color.parseColor("#00000000"));
                    holder.title.setBackgroundResource(R.drawable.rectangular_fullblack);

                }
                else {

                    holder.title.setTextColor(Color.parseColor("#000000"));//acacac
                    // holder.linear.setBackgroundColor(Color.parseColor("#90ffffff"));
                    holder.title.setBackgroundResource(R.drawable.rectangular_black);

                }
                if(myPos==0) {
                    myPos = position;

                }else{
                    myPos=position;
                    notifyDataSetChanged();
                }*/
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myPos=position;
                notifyDataSetChanged();

              /*  if (myPos == position){
                    holder.title.setTextColor(Color.parseColor("#ffffff"));//38393f
                    // holder.linear.setBackgroundColor(Color.parseColor("#00000000"));
                    holder.title.setBackgroundResource(R.drawable.rectangular_fullblack);

                }
                else {

                    holder.title.setTextColor(Color.parseColor("#000000"));//acacac
                    // holder.linear.setBackgroundColor(Color.parseColor("#90ffffff"));
                    holder.title.setBackgroundResource(R.drawable.rectangular_black);

                }
                if(myPos==0) {
                    myPos = position;

                }else{
                    myPos=position;
                    notifyDataSetChanged();
                }*/
            }
        });
    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


