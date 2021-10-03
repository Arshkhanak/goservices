package com.tecmanic.goservices;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tecmanic.goservices.Adapter.Service_heading_adapter;
import com.tecmanic.goservices.Extra.CustomVolleyJsonRequest;
import com.tecmanic.goservices.Extra.DatabaseHandler;
import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.ModelClass.ServiceDetalisModelClass;
import at.grabner.circleprogress.CircleProgressView;
import me.relex.circleindicator.CircleIndicator;

import static com.tecmanic.goservices.Extra.Config.IMAGE_URL;
import static com.tecmanic.goservices.Extra.Config.ServiceDetails;

public class ServicesFullDetails extends AppCompatActivity {

    TextView title,number;
    ImageView back_img,search;

    private ViewPager viewPager;
    private Service_heading_adapter subCategoryPagerAdapter;

    LinearLayout linear_add,linear_count,bottom_linear;
    ImageView add,minus;

    int count=1,adult=1;
    CircleProgressView circleProgressView;
    String servcId;
    ProgressDialog progressDialog;
    String serviceName,servicePRice;
    TextView pName,pPrice,brandName;
    ImageView imagevIew;
    private List<ServiceDetalisModelClass> service_heading_models = new ArrayList<>();
    RecyclerView rc_detail;
    LinearLayout check;
    TextView tv_items,tv_price,summary;
    Session_management session_management;
    DatabaseHandler dbcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_at_home_women_two);

        session_management=new Session_management(this);
        dbcart=new DatabaseHandler(this);

        check=findViewById(R.id.check);
        bottom_linear=findViewById(R.id.bottom_linear);
        summary=findViewById(R.id.summary);
        tv_items=findViewById(R.id.items);
        tv_price=findViewById(R.id.price);


        servcId=getIntent().getStringExtra("siD");
        serviceName=getIntent().getStringExtra("sNAme");
        servicePRice=getIntent().getStringExtra("sPrice");

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);

        rc_detail=findViewById(R.id.rc_heading);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rc_detail.setLayoutManager(layoutManager);
        rc_detail.setItemAnimator(new DefaultItemAnimator());


        circleProgressView = findViewById(R.id.circleView);
        circleProgressView.setVisibility(View.VISIBLE);
        circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkgreen));
        circleProgressView.setTextSize(20);
        circleProgressView.setBarColor(getResources().getColor(R.color.darkgreen));
        circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkgreen));
        circleProgressView.setValue(Float.parseFloat("30"));

        imagevIew= findViewById(R.id.imagevIew);
        pName = findViewById(R.id.txttitle);
        pPrice = findViewById(R.id.txtprice);
        brandName = findViewById(R.id.brandName);
        bottom_linear = findViewById(R.id.bottom_linear);

        linear_add = findViewById(R.id.linear_add);
        linear_count = findViewById(R.id.linear_count);
        add = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        number = findViewById(R.id.number);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                number.setText(String.valueOf(count));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count!=1){
                    count--;
                    number.setText(String.valueOf(count));
                }
            }
        });

        title = findViewById(R.id.title);
        title.setText(serviceName);
        pName.setText(serviceName);
        pPrice.setText(getResources().getString(R.string.currency)+servicePRice);

        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);


         /*view pager and indicator code is here*/


      /*  subCategoryPagerAdapter = new SubCategoryPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(subCategoryPagerAdapter);
        indicator.setViewPager(viewPager);
        subCategoryPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
*/

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(isOnline()){
            ServiceDetailsUrl(servcId);
        } else {
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }

        if (dbcart.getCartCount()>0){
            int countquant=dbcart.getCartCount();
            tv_items.setText(String.valueOf(countquant));

            tv_price.setText(getResources().getString(R.string.currency)+dbcart.getTotalAmount());

            bottom_linear.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);

        }
        else {

            tv_items.setText("0");

            tv_price.setText("0");

            bottom_linear.setVisibility(View.GONE);
            check.setVisibility(View.GONE);

        }
    }

    private void ServiceDetailsUrl(String servcId) {
        progressDialog.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("service_id",servcId);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ServiceDetails, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try{
                  /*  if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<ServiceDetalisModelClass>>() {
                            }.getType();
                            serviceDetalisModelClasses = gson.fromJson(response.getString("data"), listType);
                            for (int i=0;i<serviceDetalisModelClasses.size();i++) {
                                brandName.setText(serviceDetalisModelClasses.get(i).getBlog_heading());
                                Glide.with(getApplicationContext())
                                        .load(IMAGE_URL + serviceDetalisModelClasses.get(i).getService_image())
                                        .placeholder(R.drawable.ic_about)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .dontAnimate()
                                        .into(imagevIew);

                            }
                            }
                        else {}
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
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });*/

                    JSONArray jsonArray=response.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++) {


                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String st = jsonObject.getString("points");
                        String blogtitle = jsonObject.getString("blog_heading");
                        String service_image = jsonObject.getString("service_img");

                        ServiceDetalisModelClass service_heading_model = new ServiceDetalisModelClass();

                        service_heading_model.setBlog_heading(blogtitle);
                        service_heading_model.setBlog_point(st);

                        service_heading_models.add(service_heading_model);

                       /* Glide.with(getApplicationContext())
                                .load(IMAGE_URL + service_image)
                                .placeholder(R.drawable.ic_about)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontAnimate()
                                .into(imagevIew);*/
                        Picasso.with(getApplicationContext()).load(IMAGE_URL + service_image).error(R.drawable.ic_about).into(imagevIew);

                    }
                    progressDialog.dismiss();

                    subCategoryPagerAdapter=new Service_heading_adapter(getApplicationContext(),service_heading_models);
                    rc_detail.setAdapter(subCategoryPagerAdapter);
                    subCategoryPagerAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
