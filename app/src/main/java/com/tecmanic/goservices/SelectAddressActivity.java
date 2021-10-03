package com.tecmanic.goservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import com.tecmanic.goservices.Adapter.ShowAddressAdapter;
import com.tecmanic.goservices.Extra.CustomVolleyJsonRequest;
import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.ModelClass.SelectAdressModelClss;
import at.grabner.circleprogress.CircleProgressView;

import static com.tecmanic.goservices.Extra.Config.ShowAddress;

public class SelectAddressActivity extends AppCompatActivity {

    TextView title,AddAddress,number1;
    ImageView back_img,search;
    int layout;

    CircleProgressView circleProgressView;
    List<SelectAdressModelClss> list=new ArrayList<>();
    RecyclerView recyclerView;
    ShowAddressAdapter bAdapter;
    ProgressDialog progressDialog;
    String userId;
    Session_management sessionManagement;
    Button bottom_linear;
    String location_id = "";
    String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        title = findViewById(R.id.title);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);

        sessionManagement=new Session_management(this);
        userId=sessionManagement.userId();

        bottom_linear = findViewById(R.id.btn);
        recyclerView = findViewById(R.id.recyclerSelectAddr);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bottom_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptOrder();
            }
        });
        title.setText("Select Address");

      /*  Intent i=getIntent();
        layout = i.getIntExtra("layout",0);
        if(layout==1){
            title.setText("Salon at home for Women");
            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("60"));
        }
        if(layout==2){
            title.setText("Attending Wedding, Party etc.");
*/
            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkgreen));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.darkgreen));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkgreen));
            circleProgressView.setValue(Float.parseFloat("60"));



        AddAddress = findViewById(R.id.AddAddress);
        AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAddressActivity.this,AddAddressmainActivity.class);
                intent.putExtra("layout",layout);
                startActivity(intent);
            }
        });


        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(isOnline()){
            showAddress();
        }else{
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddress() {
        progressDialog.show();
        list.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",userId);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ShowAddress, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try{
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<SelectAdressModelClss>>() {
                            }.getType();
                            list = gson.fromJson(response.getString("data"), listType);
                            bAdapter = new ShowAddressAdapter(getApplicationContext(),list);
                            recyclerView.setAdapter(bAdapter);
                            bAdapter.notifyDataSetChanged();


                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please add address to continue..",Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

    private void attemptOrder() {

        //String getaddress = et_address.getText().toString();


        boolean cancel = false;


        if (!list.isEmpty()) {
         //   Toast.makeText(SelectAddressActivity.this, "Please Select Address", Toast.LENGTH_SHORT).show();
            if (!bAdapter.ischeckd()) {
               // Toast.makeText(SelectAddressActivity.this, "Please Select Address", Toast.LENGTH_SHORT).show();
                cancel = true;
            } else {
                location_id = bAdapter.getlocation_id();
                address = bAdapter.getaddress();
            }
        } else {
            Toast.makeText(SelectAddressActivity.this, "Please Add Address", Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getaddress)) {
            Toast.makeText(getActivity(), "Please add your address", Toast.LENGTH_SHORT).show();
            cancel = true;
        }*/

        if (!cancel) {
            //Toast.makeText(getActivity(), "date:"+getdate+"Fromtime:"+getfrom_time+"Todate:"+getto_time, Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(SelectAddressActivity.this, Delivery_date_time.class);
            intent.putExtra("address_id",location_id);

            startActivity(intent);


        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void onResume() {
        super.onResume();
        showAddress();
    }

}
