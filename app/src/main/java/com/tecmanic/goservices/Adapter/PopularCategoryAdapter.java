package com.tecmanic.goservices.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
/*
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;*/
import com.squareup.picasso.Picasso;
import com.tecmanic.goservices.PopularServicesActivity;
import com.tecmanic.goservices.R;

import java.util.List;

import com.tecmanic.goservices.ModelClass.CategoryModel;
import com.tecmanic.goservices.fragment.HomeFragment;

import static com.tecmanic.goservices.Extra.Config.IMAGE_URL;


public class PopularCategoryAdapter extends RecyclerView.Adapter<PopularCategoryAdapter.MyViewHolder> {

    HomeFragment homeFragment;
    private List<CategoryModel> cateList;
    FragmentActivity activity;
    Context context;
    Class<HomeFragment> homeFragmentClass;



    public PopularCategoryAdapter(Context context, List<CategoryModel> cateList, Class<HomeFragment> homeFragmentClass) {
        this.context=context;
        this.cateList=cateList;
        this.homeFragmentClass=homeFragmentClass;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ProdNAme;
        ImageView icons1;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            ProdNAme = (TextView) view.findViewById(R.id.ProdName);
            icons1 = (ImageView) view.findViewById(R.id.icons1);
            linearLayout=view.findViewById(R.id.linearlayout);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_homeproduct_r1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CategoryModel cc = cateList.get(position);
        holder.ProdNAme.setText(cc.getpName());
        Picasso.with(context).load(IMAGE_URL+cc.getpImage()).into(holder.icons1);
      /*  Glide.with(context)
                .load(IMAGE_URL+cc.getpImage())
                .placeholder(R.drawable.ic_about)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.icons1);
*/
        //holder.icons1.setImageResource(Integer.parseInt(cc.getpImage()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PopularServicesActivity.class);
                intent.putExtra("categoryid",cateList.get(position).getId());
                intent.putExtra("child_category_id",cateList.get(position).getPchildcatId());
              //  intent.putExtra("layout",position);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cateList.size();
    }

}
