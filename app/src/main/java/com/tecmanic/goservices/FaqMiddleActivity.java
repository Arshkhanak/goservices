package com.tecmanic.goservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tecmanic.goservices.Adapter.FAQmiddleAdapter;
import com.tecmanic.goservices.Extra.CustomVolleyJsonRequest;
import com.tecmanic.goservices.ModelClass.FaqQuesModelClss;

import static com.tecmanic.goservices.Extra.Config.FAQquesList;

public class FaqMiddleActivity extends AppCompatActivity {

    ImageView back_img,circle,search;
    TextView title;
    RecyclerView recyclerview;
    FAQmiddleAdapter adapter;
    List<FaqQuesModelClss> faqList=new ArrayList<>();
    ProgressDialog progressDialog;
    String faqId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_middle);
        init();
    }

    private void init() {
        faqId=getIntent().getStringExtra("faqId");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionbar_color)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.actionbar_color)); //status bar or the time bar at the top
        }
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.title);
        title.setText("F.A.Q.");
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        recyclerview=findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager1);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        if(isOnline()){
            faqQuestionUrl(faqId);
        } else {
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
}

    private void faqQuestionUrl(String faqId) {
        progressDialog.show();
        faqList.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("faq_cat_id",faqId);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                FAQquesList, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try{
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<FaqQuesModelClss>>() {
                            }.getType();
                            faqList = gson.fromJson(response.getString("data"), listType);
                            adapter = new FAQmiddleAdapter(getApplicationContext(),faqList);
                            recyclerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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