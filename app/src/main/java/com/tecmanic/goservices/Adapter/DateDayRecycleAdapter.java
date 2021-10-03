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


public class DateDayRecycleAdapter extends RecyclerView.Adapter<DateDayRecycleAdapter.MyViewHolder> {

    Context context;

    boolean showingfirst = true;
    int myPos = 0;

    private List<DateDayModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView date,day;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);


            date = (TextView) view.findViewById(R.id.date);
            day = (TextView) view.findViewById(R.id.day);
            linear = (LinearLayout) view.findViewById(R.id.linear);


        }

    }


    public DateDayRecycleAdapter(Context context, List<DateDayModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public DateDayRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dateday_list, parent, false);


        return new DateDayRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DateDayModelClass lists = OfferList.get(position);

        holder.date.setText(lists.getDate());
        holder.day.setText(lists.getDay());




        if (myPos == position){
            holder.date.setTextColor(Color.parseColor("#6685ff"));
            holder.day.setTextColor(Color.parseColor("#6685ff"));
            holder.linear.setBackgroundResource(R.drawable.blue_dateday_rect);
        }else {

            holder.date.setTextColor(Color.parseColor("#8f909e"));
            holder.day.setTextColor(Color.parseColor("#8f909e"));
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


