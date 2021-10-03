package com.tecmanic.goservices.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.tecmanic.goservices.ModelClass.DateDayModelClass;
import com.tecmanic.goservices.R;


public class TimeRecycleAdapter extends RecyclerView.Adapter<TimeRecycleAdapter.MyViewHolder> {

    Context context;

    boolean showingfirst = true;
    int myPos = 0;

    private List<DateDayModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView time,hours;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);


            time = (TextView) view.findViewById(R.id.time);
            hours = (TextView) view.findViewById(R.id.hours);
            linear = (LinearLayout) view.findViewById(R.id.linear);


        }

    }


    public TimeRecycleAdapter(Context context, List<DateDayModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public TimeRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_list, parent, false);


        return new TimeRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DateDayModelClass lists = OfferList.get(position);

        holder.time.setText(lists.getDate());
        holder.hours.setText(lists.getDay());




        if (myPos == position){
            holder.time.setTextColor(Color.parseColor("#6685ff"));
            holder.hours.setTextColor(Color.parseColor("#6685ff"));
            holder.linear.setBackgroundResource(R.drawable.blue_dateday_rect);
        }else {

            holder.time.setTextColor(Color.parseColor("#8f909e"));
            holder.hours.setTextColor(Color.parseColor("#8f909e"));
            holder.linear.setBackgroundResource(R.drawable.gray_dateday_rect);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPos = position;
                notifyDataSetChanged();

            }
        });


    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


