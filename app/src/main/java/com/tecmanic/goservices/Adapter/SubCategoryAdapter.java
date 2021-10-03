package com.tecmanic.goservices.Adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;*/
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.tecmanic.goservices.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tecmanic.goservices.Extra.CustomVolleyJsonRequest;
import com.tecmanic.goservices.ModelClass.SubCatChildModelclass;
import com.tecmanic.goservices.ModelClass.SubCateModelClass;

import static com.tecmanic.goservices.Extra.Config.HomeChildCategory;
import static com.tecmanic.goservices.Extra.Config.IMAGE_URL;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    Context context;
    private List<SubCateModelClass> subcateList;
    private List<SubCatChildModelclass> subcatechildList;
    SubCateChildAdapter adapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView proImag,arrow,arrowUp;
        boolean mines = true;
        RecyclerView recyclerView;
        LinearLayout linear;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView1);
            proImag = (ImageView) view.findViewById(R.id.imageView1);
            arrow = (ImageView) view.findViewById(R.id.arrow);
            arrowUp = (ImageView) view.findViewById(R.id.arrow1);
            recyclerView=view.findViewById(R.id.recyclerChildCate);
            linear=view.findViewById(R.id.linear);

        }
    }


    public SubCategoryAdapter(Context context,List<SubCateModelClass> subcateList) {
        this.context = context;
        this.subcateList = subcateList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubCateModelClass movie = subcateList.get(position);
        holder.title.setText(movie.getSub_category_name());

       /* Glide.with(context)
                .load(IMAGE_URL + movie.getSub_category_img())
                .placeholder(R.drawable.ic_about)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.proImag);*/
        Picasso.with(context).load(IMAGE_URL + movie.getSub_category_img()).error(R.drawable.ic_about).into(holder.proImag);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mines) {
                    holder.mines = false;

                    holder.linear.setVisibility(View.VISIBLE);

                    holder.recyclerView.setVisibility(View.VISIBLE);
                        holder.arrow.setVisibility(View.GONE);
                        holder.arrowUp.setVisibility(View.VISIBLE);
                    hitChild_category(movie.getSub_category_id(),holder.recyclerView);
                }
                 else {
                    holder.mines = true;
                    holder.linear.setVisibility(View.GONE);
                    holder.recyclerView.setVisibility(View.GONE);
                    holder.arrow.setVisibility(View.VISIBLE);
                    holder.arrowUp.setVisibility(View.GONE);

                }}

            private void hitChild_category(String sub_category_id, RecyclerView recyclerView) {
                // subcatechildList.clear();
                String tag_json_obj = "json_category_req";
                Map<String, String> params = new HashMap<String, String>();
                params.put("sub_category_id", sub_category_id);
                CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                        HomeChildCategory, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("TAG", response.toString());
                        try{
                            if (response != null && response.length() > 0) {
                                String status = response.getString("status");

                                if (status.contains("1")) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<SubCatChildModelclass>>() {
                                    }.getType();
                                    subcatechildList = gson.fromJson(response.getString("data"), listType);
                                    adapter = new SubCateChildAdapter(context,subcatechildList);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                                else {

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("TAG", "Error: " + error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                           // Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.getCache().clear();
                requestQueue.add(jsonObjReq);
            }


        });
    }



    @Override
    public int getItemCount() {
        return subcateList.size();
    }
}

