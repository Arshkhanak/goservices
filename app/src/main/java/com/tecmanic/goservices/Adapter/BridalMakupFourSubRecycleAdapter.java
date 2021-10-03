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

import com.tecmanic.goservices.ModelClass.CategorySubModelClass;
import com.tecmanic.goservices.R;


public class BridalMakupFourSubRecycleAdapter extends RecyclerView.Adapter<BridalMakupFourSubRecycleAdapter.MyViewHolder> {

    Context context;


    private List<CategorySubModelClass> OfferList;
    private int count = 1;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title, number;
        LinearLayout linear_add, linear_count;
        ImageView minus, plus;


        public MyViewHolder(View view) {
            super(view);


            title = (TextView) view.findViewById(R.id.title);
            number = (TextView) view.findViewById(R.id.number);
            linear_add = (LinearLayout) view.findViewById(R.id.linear_add);
            linear_count = (LinearLayout) view.findViewById(R.id.linear_count);
            minus = (ImageView) view.findViewById(R.id.minus);
            plus = (ImageView) view.findViewById(R.id.plus);


        }

    }


    public BridalMakupFourSubRecycleAdapter(Context context, List<CategorySubModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public BridalMakupFourSubRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bridle_makup_four_list, parent, false);


        return new BridalMakupFourSubRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final CategorySubModelClass lists = OfferList.get(position);
        holder.title.setText(lists.getTitle());


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, SalonAtHomeWomenTwo.class);
//                context.startActivity(intent);
//            }
//        });


        holder.linear_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.linear_count.setVisibility(View.VISIBLE);
                holder.linear_add.setVisibility(View.GONE);
                holder.number.setText(String.valueOf(count));
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                holder.number.setText(String.valueOf(count));
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if (count >=0 )
                    holder.number.setText(String.valueOf(count));
                if(count==0){
                    holder.linear_count.setVisibility(View.GONE);
                    holder.linear_add.setVisibility(View.VISIBLE);
                    count=1;
                }
            }

        });

/*
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count= Integer.parseInt(String.valueOf(holder.number.getText()));
                count++;


                if (count==1){

                    holder.linear_add.setVisibility(View.GONE);
                    holder.linear_count.setVisibility(View.VISIBLE);

                }
                holder.number.setText(String.valueOf(count));
//                holder.minus.setVisibility(View.VISIBLE);
//                holder.number.setVisibility(View.VISIBLE);
            }

        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count= Integer.parseInt(String.valueOf(holder.number.getText()));

                count--;
                if (count== 0){

                    holder.linear_add.setVisibility(View.VISIBLE);
                    holder.linear_count.setVisibility(View.GONE);

                }
                holder.number.setText(String.valueOf(count));

            }

        });*/


    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


