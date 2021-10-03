package com.tecmanic.goservices.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.goservices.Cart_Activity;
import com.tecmanic.goservices.Extra.Config;
import com.tecmanic.goservices.ModelClass.OffersModelClass;

import com.tecmanic.goservices.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tecmanic.goservices.Extra.Config.coupon_applied;
import static com.tecmanic.goservices.OffersActivity.ttm;


public class OffersRecycleAdapter extends RecyclerView.Adapter<OffersRecycleAdapter.MyViewHolder> {

    Context context;


    private List<OffersModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView title,tdesc;
        LinearLayout linear_add;

        public MyViewHolder(View view) {
            super(view);


            title = (TextView) view.findViewById(R.id.title);
            tdesc = (TextView) view.findViewById(R.id.tdesc);
            linear_add =  view.findViewById(R.id.linear_add);

        }

    }


    public OffersRecycleAdapter(Context context, List<OffersModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public OffersRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offers_list, parent, false);


        return new OffersRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final OffersModelClass lists = OfferList.get(position);

        holder.title.setText(lists.getCoupon_name());
        holder.tdesc.setText(lists.getDescription());

        holder.linear_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getId(lists.getCoupon_id());
               /* Intent intent = new Intent(context, Cart_Activity.class);
                intent.putExtra("couponid",lists.getCoupon_id());
                intent.putExtra("coupon",lists.getCoupon_name());
                context.startActivity(intent);*/

            }

            private void getId(final String coupon_id) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, coupon_applied, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("coupon_applied",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            String data = jsonObject.getString("data");
                            Config.Totalamount=data;
                            Intent intent = new Intent(context, Cart_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("couponid",lists.getCoupon_id());
                            intent.putExtra("coupon",lists.getCoupon_name());
                            intent.putExtra("remainingAmount",data);
                            context.startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> param = new HashMap<>();
                        param.put("total_amount",ttm);
                        param.put("coupon_id",coupon_id);

                        return param;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.getCache().clear();
                stringRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 90000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 0;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });
                requestQueue.add(stringRequest);


            }
        });
    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


