package com.tecmanic.goservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import com.tecmanic.goservices.Adapter.Cancelbookin_adapter;

import com.tecmanic.goservices.Extra.CustomVolleyJsonRequest;
import com.tecmanic.goservices.Extra.RecyclerTouchListener;
import com.tecmanic.goservices.ModelClass.Cancel_bookin_model;

import static com.tecmanic.goservices.Extra.Config.bookingCancelReasonList;
import static com.tecmanic.goservices.Extra.Config.bookingcancellation;

public class BookingCancellationActivity extends AppCompatActivity {

    List<Cancel_bookin_model> cancelBookinModelList=new ArrayList<>();
    Cancelbookin_adapter cancelbookin_adapter;
    RecyclerView recyclerView;
    EditText et_reson;
    String reasons;
    ProgressDialog progressDialog;
    String bookingid;
    Button otherreason,submit;
    LinearLayout l1_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_cancellation);
        init();
    }

    private void init() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        l1_check=findViewById(R.id.l1_check);
        otherreason=findViewById(R.id.otherreason);
        recyclerView=findViewById(R.id.rc_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bookingid=getIntent().getStringExtra("bId");
        et_reson=findViewById(R.id.et_reason);


        otherreason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1_check.setVisibility(View.VISIBLE);
                otherreason.setVisibility(View.GONE);
            }
        });

        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_reson.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Write Reason", Toast.LENGTH_SHORT).show();
                }
                else {
                    reasons=et_reson.getText().toString();

                    progressDialog.show();
                    cancelbooking(reasons);
                }
            }
        });
        otherreason=findViewById(R.id.otherreason);
        cancellist();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                reasons=cancelBookinModelList.get(position).getCancel_points();

                progressDialog.show();
                cancelbooking(reasons);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
    private void cancellist() {
        progressDialog.show();
        cancelBookinModelList.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("parrent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                bookingCancelReasonList, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("", response.toString());

                progressDialog.dismiss();
                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {

                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Cancel_bookin_model>>() {
                            }.getType();
                            cancelBookinModelList = gson.fromJson(response.getString("data"), listType);
                            cancelbookin_adapter = new Cancelbookin_adapter(getApplicationContext(),cancelBookinModelList);
                            recyclerView.setAdapter(cancelbookin_adapter);
                            cancelbookin_adapter.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (getApplicationContext() != null) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

    private void cancelbooking(String reaons) {

        progressDialog.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("booking_id",bookingid);
        params.put("cancel_reason",reaons);
       // Log.d("reaons", reaons);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                bookingcancellation, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("", response.toString());

                progressDialog.dismiss();
                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            progressDialog.dismiss();
                           Intent intent=new Intent(getApplicationContext(),HomePageActivity.class);
                           startActivity(intent);
                           finish();
                        }
                        else {
                            progressDialog.dismiss();
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
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (getApplicationContext() != null) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

}