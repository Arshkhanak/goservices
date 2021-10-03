package com.tecmanic.goservices.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
/*mport com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;*/
import com.squareup.picasso.Picasso;
import com.tecmanic.goservices.Add_on_Activity;
import com.tecmanic.goservices.R;
import com.tecmanic.goservices.ReviewRatingActivity;
import com.tecmanic.goservices.ServicesFullDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tecmanic.goservices.Extra.CustomVolleyJsonRequest;
import com.tecmanic.goservices.Extra.DatabaseHandler;
import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.ModelClass.PopularServiceModelClss;

import static com.tecmanic.goservices.Extra.Config.IMAGE_URL;
import static com.tecmanic.goservices.Extra.Config.KEY_ID;
import static com.tecmanic.goservices.Extra.Config.showRating;
import static com.tecmanic.goservices.PopularServicesActivity.bottom_linearrr;
import static com.tecmanic.goservices.PopularServicesActivity.check;
import static com.tecmanic.goservices.PopularServicesActivity.tv_items;
import static com.tecmanic.goservices.PopularServicesActivity.tv_price;


public class PopluarAdapter extends RecyclerView.Adapter<PopluarAdapter.MyViewHolder> {

    Context context;
    private List<PopularServiceModelClss> OfferList;
    private int count = 1;
    String  ratingid;
    String user_id;
    DatabaseHandler dbcart;
    Session_management session_management;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title, number,tdesc,tprice,tmrp,tsave,timie,txtAddons;
        LinearLayout linear_add, linear_count,l1_rating,viewdetails,clock;
        ImageView minus, plus,image;
        RatingBar rating;
        public MyViewHolder(View view) {
            super(view);
            txtAddons=view.findViewById(R.id.txtAddons);
            clock=view.findViewById(R.id.clock);
            viewdetails=view.findViewById(R.id.viewdetails);
            l1_rating=view.findViewById(R.id.l1_rating);
            title = (TextView) view.findViewById(R.id.title);
            tdesc = (TextView) view.findViewById(R.id.tdesc);
            tprice = (TextView) view.findViewById(R.id.tprice);
            tmrp = (TextView) view.findViewById(R.id.tmrp);
            tsave = (TextView) view.findViewById(R.id.totalsave);
            timie = (TextView) view.findViewById(R.id.timie);
            number = (TextView) view.findViewById(R.id.number);
            linear_add = (LinearLayout) view.findViewById(R.id.linear_add);
            linear_count = (LinearLayout) view.findViewById(R.id.linear_count);
            minus = (ImageView) view.findViewById(R.id.minus);
            plus = (ImageView) view.findViewById(R.id.plus);
            image = (ImageView) view.findViewById(R.id.image);
            rating=view.findViewById(R.id.rating);
        }

    }


    public PopluarAdapter(Context context, List<PopularServiceModelClss> offerList) {
        this.OfferList = offerList;
        this.context = context;
        dbcart = new DatabaseHandler(context);
        session_management=new Session_management(context);

    }

    @Override
    public PopluarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_list, parent, false);

        return new PopluarAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        PopularServiceModelClss lists = OfferList.get(position);

        user_id=session_management.getUserDetails().get(KEY_ID);

        holder.title.setText(lists.getService_name());
        holder.timie.setText(lists.getTime()+" mins");
        holder.tmrp.setText(context.getResources().getString(R.string.currency)+lists.getMrp());
        holder.tmrp.setPaintFlags(holder.tmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.tprice.setText(context.getResources().getString(R.string.currency)+lists.getService_price());
        rating(holder.rating,lists.getService_id(),user_id);
        /*Glide.with(context)
                .load(IMAGE_URL + lists.getService_img())
                .placeholder(R.drawable.ic_about)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);*/

        Picasso.with(context).load(IMAGE_URL + lists.getService_img()).error(R.drawable.ic_about).into(holder.image);

        int qtyd = Integer.parseInt(dbcart.getInCartItemQty(OfferList.get(position).getService_id()));
        double price = Double.parseDouble(OfferList.get(position).getService_price());

        if (qtyd > 0) {
            holder.linear_add.setVisibility(View.GONE);
            holder.linear_count.setVisibility(View.VISIBLE);
            holder.number.setText("" + qtyd);
            holder.tprice.setText(context.getResources().getString(R.string.currency) + (price * qtyd));

        } else {
            holder.linear_add.setVisibility(View.VISIBLE);
            holder.linear_count.setVisibility(View.GONE);
            holder.tprice.setText(context.getResources().getString(R.string.currency)+OfferList.get(position).getService_price());

            holder.number.setText("" + 0);
        }

        if(holder.tmrp.getText().toString().equalsIgnoreCase(context.getResources().getString(R.string.currency)+"null")){
            holder.tmrp.setVisibility(View.GONE);
        }else{
            holder.tmrp.setVisibility(View.VISIBLE);
        }

        if(holder.timie.getText().toString().equalsIgnoreCase("null"+" mins")){
            holder.timie.setVisibility(View.GONE);
            holder.clock.setVisibility(View.GONE);
        }else{
            holder.clock.setVisibility(View.VISIBLE);
            holder.timie.setVisibility(View.VISIBLE);
        }


     /*   if (dbcart.isInCart(lists.getService_id())) {

            holder.number.setText(dbcart.getInCartItemQty(lists.getService_id()));
            holder.linear_count.setVisibility(View.VISIBLE);
            holder.linear_add.setVisibility(View.GONE);

        } else {

            holder.linear_count.setVisibility(View.GONE);
            holder.linear_add.setVisibility(View.VISIBLE);

        }*/
        holder.viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getId(OfferList.get(position).getService_id(),OfferList.get(position).getService_name(),OfferList.get(position).getService_price());

            }
        });
        holder.rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ReviewRatingActivity.class);
                intent.putExtra("service_id",lists.getService_id());
                context.startActivity(intent);
            }
        });

        holder.txtAddons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, Add_on_Activity.class);
                intent.putExtra("child_category_id",OfferList.get(position).getChild_category_id());
                context.startActivity(intent);
            }
        });

        holder.linear_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_linearrr.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);

               /* int count=1;
                holder.linear_count.setVisibility(View.VISIBLE);
                holder.linear_add.setVisibility(View.GONE);
                holder.number.setText(String.valueOf(count));

                HashMap<String, String> map = new HashMap<>();
                map.put("service_id", lists.getService_id());
                map.put("service_name", lists.getService_name());
                map.put("service_description", lists.getService_description());
                map.put("service_price", lists.getService_price());
                map.put("service_image", lists.getService_img());


                if (!holder.number.getText().toString().equalsIgnoreCase("0")) {
                    if (dbcart.isInCart(map.get("service_id"))) {
                        dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                    } else {
                        dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("service_id"));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("service_id")));

                int countquant=dbcart.getCartCount();

                Double price = Double.parseDouble(map.get("service_price"));



                tv_items.setText(String.valueOf(countquant));

                tv_price.setText(context.getResources().getString(R.string.currency)+dbcart.getTotalAmount());
*/
                holder.linear_add.setVisibility(View.GONE);
                holder.linear_count.setVisibility(View.VISIBLE);
                // CategoryGridList.get(position).setQuantity("1");
                holder.number.setText("1");
                updateMultiply(position, 1);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i = Integer.parseInt(dbcart.getInCartItemQty(lists.getService_id()));
//            CategoryGridList.get(position).setQuantity(String.valueOf(i - 1));
                if ((i - 1) < 0 || (i - 1) == 0) {
                    holder.linear_add.setVisibility(View.VISIBLE);
                    holder.linear_count.setVisibility(View.GONE);
                    holder.number.setText("" + 0);
                    holder.tprice.setText(context.getResources().getString(R.string.currency) + price);

                } else {
                    holder.number.setText("" + (i - 1));
                    holder.tprice.setText(context.getResources().getString(R.string.currency) + (price * (i - 1)));

                }
                updateMultiply(position, i - 1);
              /*  int count = 0;
                if (!holder.number.getText().toString().equalsIgnoreCase(""))
                    count = Integer.valueOf(holder.number.getText().toString());

                if (count > 0) {
                    count = count - 1;
                    holder.number.setText(String.valueOf(count));


                    HashMap<String, String> map = new HashMap<>();
                    map.put("service_id", lists.getService_id());
                    map.put("service_name", lists.getService_name());
                    map.put("service_description", lists.getService_description());
                    map.put("service_price", lists.getService_price());
                    map.put("service_image", lists.getService_img());


                    if (!holder.number.getText().toString().equalsIgnoreCase("0")) {
                        if (dbcart.isInCart(map.get("service_id"))) {
                            dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                        } else {
                            dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                        }
                    } else {
                        dbcart.removeItemFromCart(map.get("service_id"));
                    }

                    int countquant=dbcart.getCartCount();


                    holder.number.setText(String.valueOf(count));




                    tv_items.setText(String.valueOf(countquant));

                    tv_price.setText(context.getResources().getString(R.string.currency)+dbcart.getTotalAmount());

                }


                if(count<1){


                    holder.linear_count.setVisibility(View.GONE);
                    holder.linear_add.setVisibility(View.VISIBLE);




                    dbcart.removeItemFromCart(lists.getService_id());


                    int countquant=dbcart.getCartCount();



                    tv_items.setText(String.valueOf(countquant));

                    tv_price.setText(context.getResources().getString(R.string.currency)+dbcart.getTotalAmount());

                    if (dbcart.getCartCount()>0){
                        bottom_linearr.setVisibility(View.VISIBLE);
                        check.setVisibility(View.VISIBLE);

                    }
                    else {
                        bottom_linearr.setVisibility(View.GONE);
                        check.setVisibility(View.GONE);

                    }
                }
*/
            }

        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                int count = 0;

                count = Integer.valueOf(holder.number.getText().toString());


                if (count>0) {


                    count++;
                    HashMap<String, String> map = new HashMap<>();
                    map.put("service_id", lists.getService_id());
                    map.put("service_name", lists.getService_name());
                    map.put("service_description", lists.getService_description());
                    map.put("service_price", lists.getService_price());
                    map.put("service_image", lists.getService_img());

                    holder.number.setText(String.valueOf(count));
                    if (!holder.number.getText().toString().equalsIgnoreCase("0")) {
                        if (dbcart.isInCart(map.get("service_id"))) {
                            dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                        } else {
                            dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                        }
                    } else {
                        dbcart.removeItemFromCart(map.get("service_id"));
                    }
                    Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("service_id")));

                    int countquant = dbcart.getCartCount();

                    Double price = Double.parseDouble(map.get("service_price"));


                    tv_items.setText(String.valueOf(countquant));

                    tv_price.setText(context.getResources().getString(R.string.currency) + dbcart.getTotalAmount());*/
                holder.linear_add.setVisibility(View.GONE);
                holder.linear_count.setVisibility(View.VISIBLE);
                //  int i = Integer.parseInt(cc.getQuantity());
                int i = Integer.parseInt(dbcart.getInCartItemQty(lists.getService_id()));

                ///  CategoryGridList.get(position).setQuantity(String.valueOf(i+1));
                holder.number.setText(""+(i+1));
                holder.tprice.setText(context.getResources().getString(R.string.currency)+(price*(i+1)));
                updateMultiply(position, i + 1);


            }
        });

                                       }

    private void updateMultiply(int position, float i) {
        HashMap<String, String> map = new HashMap<>();
        map.put("service_id", OfferList.get(position).getService_id());
        map.put("service_name", OfferList.get(position).getService_name());
        map.put("service_description", OfferList.get(position).getService_description());
        map.put("service_price", OfferList.get(position).getService_price());
        map.put("service_image", OfferList.get(position).getService_img());

        if (i > 0) {
            if (dbcart.isInCart(map.get("service_id"))) {
                dbcart.setCart(map, i);
            } else {
                dbcart.setCart(map, i);
            }
        } else {
            dbcart.removeItemFromCart(map.get("service_id"));
        }
        try {
//            int items = (int) Double.parseDouble(dbcart.getInCartItemQty(map.get("varient_id")));
//            double price = Double.parseDouble(Objects.requireNonNull(map.get("price")).trim());
//            double mrp = Double.parseDouble(Objects.requireNonNull(map.get("mrp")).trim());
            //  Double reward = Double.parseDouble(map.get("rewards"));
            // tv_reward.setText("" + reward * items);
//            pDescrptn.setText(""+cartList.get(position).getpDes());
//            pPrice.setText("" +price* items);
//            txtQuan.setText("" + items);
//            pMrp.setText("" + mrp* items );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty",dbcart.getCartCount()).apply();



            }
        }catch (IndexOutOfBoundsException e){
            Log.d("qwer",e.toString());
        }

        if(dbcart.getCartCount()==0){

            bottom_linearrr.setVisibility(View.GONE);
            check.setVisibility(View.GONE);

        }
        else{
            bottom_linearrr.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
            updateData();
        }
    }
    private void updateData() {
        tv_items.setText(String.valueOf(dbcart.getCartCount()));

        tv_price.setText(context.getResources().getString(R.string.currency) + dbcart.getTotalAmount());

    }

    private void getId(String service_id, String service_name, String service_price) {
        Intent intent = new Intent(context, ServicesFullDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("siD",service_id);
        intent.putExtra("sNAme",service_name);
        intent.putExtra("sPrice",service_price);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }
    private void rating(RatingBar tv, String serviceid, String user_id) {

        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();


        params.put("id",user_id);
        params.put("service_id",serviceid);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                showRating, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("", response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {

                            JSONArray jsonArray=response.getJSONArray("data");

                            JSONObject jsonObject=jsonArray.getJSONObject(0);

                            ratingid=jsonObject.getString("rating_id");

                            String rating=jsonObject.getString("rating");



                            tv.setRating(Float.parseFloat(rating));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        mRequestQueue.add(jsonObjReq);
    }

}


