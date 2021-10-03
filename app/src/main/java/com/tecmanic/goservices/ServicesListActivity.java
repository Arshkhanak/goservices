package com.tecmanic.goservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tecmanic.goservices.Adapter.CategoryHorizontalRecycleAdapter;
import com.tecmanic.goservices.Adapter.CategorySubRecycleAdapter;
import com.tecmanic.goservices.Extra.Config;
import com.tecmanic.goservices.Extra.CustomVolleyJsonRequest;
import com.tecmanic.goservices.Extra.DatabaseHandler;
import com.tecmanic.goservices.Extra.RecyclerTouchListener;
import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.ModelClass.ChildServiceModelClss;
import com.tecmanic.goservices.ModelClass.SubCatChildModelclass;
import at.grabner.circleprogress.CircleProgressView;

import static com.tecmanic.goservices.Extra.Config.HomeChildCategory;
import static com.tecmanic.goservices.Extra.Config.HomeService;


public class ServicesListActivity extends AppCompatActivity {



    DatabaseHandler dbcart;
    ImageView back_img,circle,search;
    TextView title;
    CardView CardReview;
    CircleProgressView circleProgressView;
    /*horizontal recyerview data is here*/

    private List<SubCatChildModelclass>homeCategoryModelClasses=new ArrayList<>();
    private RecyclerView recyclerView;
    private CategoryHorizontalRecycleAdapter bAdapter;

    /*subcategory data is here*/

    private  List<ChildServiceModelClss>categorySubModelClasses=new ArrayList<>();
    private RecyclerView recyclerView1;
    private CategorySubRecycleAdapter Adapter;

    String childID,SubCatId,latitude,longitude;
    Session_management sessionManagement;
    ProgressDialog progressDialog;
    TextView rating,tDesc,txtPname,noData;
    String SubCategoryNAme;
    public static TextView tv_items,tv_price,summary;
    public  static LinearLayout check,bottom_linearr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_at_home_women_one);

        noData=findViewById(R.id.noData);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        sessionManagement=new Session_management(getApplicationContext());
        dbcart=new DatabaseHandler(this);
        childID=getIntent().getStringExtra("childCat_id");
        SubCatId=getIntent().getStringExtra("SubCat_id");
        SubCategoryNAme=getIntent().getStringExtra("SubCatNAme");

        latitude=sessionManagement.Lat();
        longitude=sessionManagement.Lng();
        rating = findViewById(R.id.txtRate);
        tDesc = findViewById(R.id.txtDec);
        txtPname = findViewById(R.id.txtPname);
        CardReview = findViewById(R.id.CardReview);
        CardReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Rating_review.class);
                startActivity(intent);
            }
        });


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionbar_color)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.actionbar_color)); //status bar or the time bar at the top
        }

        bottom_linearr = findViewById(R.id.bottom_linear);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.title);
        title.setText(SubCategoryNAme);
        txtPname.setText(SubCategoryNAme);
//        circle = findViewById(R.id.circle);
//        circle.setVisibility(View.GONE);

        circleProgressView = findViewById(R.id.circleView);
        circleProgressView.setVisibility(View.VISIBLE);
        circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkgreen));
        circleProgressView.setTextSize(20);
        circleProgressView.setBarColor(getResources().getColor(R.color.darkgreen));
        circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkgreen));
        circleProgressView.setValue(Float.parseFloat("10"));

        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);
     //   search.setImageResource(R.drawable.ic_colorful_circle);

        check=findViewById(R.id.check);
        tv_items=findViewById(R.id.items);
        tv_price=findViewById(R.id.price);
        summary=findViewById(R.id.summary);
        if(sessionManagement.isLoggedIn()) {
            summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Add_on_Activity.class);
                    intent.putExtra("child_category_id", childID);
                    startActivity(intent);
                }
            });
            bottom_linearr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), Add_on_Activity.class);
                    intent.putExtra("child_category_id",childID);
                    startActivity(intent);
                }
            });
        }else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        /*horizontal category recyclerview code is here*/

        recyclerView = findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ServicesListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               String id=homeCategoryModelClasses.get(position).getChild_category_id();
                String name=homeCategoryModelClasses.get(position).getChild_name();
                txtPname.setText(name);
                hitServiceUrl(id);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


         /*vertical category recyclerview code is here*/

        recyclerView1 = findViewById(R.id.recyclerview_waxing);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(ServicesListActivity.this);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        if(isOnline()){

            hitChild_category(SubCatId,recyclerView);
            ratingView();
            hitServiceUrl(childID);
        } else {
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }


        if (dbcart.getCartCount()>0){
            int countquant=dbcart.getCartCount();
            tv_items.setText(String.valueOf(countquant));

            tv_price.setText(getResources().getString(R.string.currency)+dbcart.getTotalAmount());

            bottom_linearr.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);

        }
        else {

            tv_items.setText("0");

            tv_price.setText("0");

            bottom_linearr.setVisibility(View.GONE);
            check.setVisibility(View.GONE);

        }
    }

    private void ratingView() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.totalRating, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Tag",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if(status.equalsIgnoreCase("1")){
                        String data = jsonObject.getString("data");
                        double rate=Double.parseDouble(data);
                        rating.setText(String.valueOf(rate));
                    }
                    else {
                        CardReview.setVisibility(View.GONE);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void hitChild_category(String sub_category_id, RecyclerView recyclerView) {
        progressDialog.show();
        homeCategoryModelClasses.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("sub_category_id", sub_category_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                HomeChildCategory, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try{
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<SubCatChildModelclass>>() {
                            }.getType();
                            homeCategoryModelClasses = gson.fromJson(response.getString("data"), listType);
                            bAdapter = new CategoryHorizontalRecycleAdapter(getApplicationContext(),homeCategoryModelClasses);
                            recyclerView.setAdapter(bAdapter);
                            bAdapter.notifyDataSetChanged();
                        }
                        else {
                        }
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }


    private void hitServiceUrl(String childID) {
        progressDialog.show();
        categorySubModelClasses.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("city_id",sessionManagement.getCityId());
        params.put("child_category_id", childID);
//         Log.d("losssdng", sessionManagement.getCityId());
//        Log.d("latryuit", childID);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                HomeService, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.show();
                try{
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        progressDialog.dismiss();
                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<ChildServiceModelClss>>() {
                            }.getType();
                            categorySubModelClasses = gson.fromJson(response.getString("data"), listType);
                            Adapter = new CategorySubRecycleAdapter(getApplicationContext(),categorySubModelClasses);
                            recyclerView1.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            recyclerView1.setAdapter(Adapter);
                            Adapter.notifyDataSetChanged();
                          }

                        else {
                            recyclerView1.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                  //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
