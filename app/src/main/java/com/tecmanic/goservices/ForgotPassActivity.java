package com.tecmanic.goservices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tecmanic.goservices.Adapter.SelectCountryRecycleAdapter;
import com.tecmanic.goservices.Extra.Config;
import com.tecmanic.goservices.ModelClass.SelectCountryModelClass;

public class ForgotPassActivity extends AppCompatActivity {
    ImageView back_img;
    ProgressDialog progressDialog;
    Button button;
    EditText etMob;
    Dialog slideDialog;
    LinearLayout sppiner;
    ImageView img;
    TextView txt;

    private ArrayList<SelectCountryModelClass> selectCountryModelClasses;
    private RecyclerView recyclerView;
    private SelectCountryRecycleAdapter bAdapter;
    private Integer image[] = {R.drawable.ic_india,R.drawable.ic_botswana,R.drawable.ic_canada,R.drawable.ic_mexico,R.drawable.ic_australia,R.drawable.ic_brazil,
            R.drawable.ic_russia};
    private String country_name[] = {"India","Botswana","Canada","Mexico","Australia","Brazil","Russia"};
    private String country_code[] = {"+91","+267","+1","+52","+61","+91","+43"};

/*
    private Integer image[] = {R.drawable.ic_india,R.drawable.ic_canada,R.drawable.ic_mexico,R.drawable.ic_australia,R.drawable.ic_brazil,
            R.drawable.ic_russia};
    private String country_name[] = {"India","Canada","Mexico","Australia","Brazil","Russia"};
    private String country_code[] = {"+91","+1","+52","+61","+91","+43"};
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        init();
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait!");
        progressDialog.setCancelable(false);
        back_img = findViewById(R.id.back_img);
        button = findViewById(R.id.button);
        img = findViewById(R.id.image);
        txt = findViewById(R.id.country_code);
        etMob= findViewById(R.id.etMob);
        sppiner = findViewById(R.id.sppiner);
        sppiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideDialog = new Dialog(ForgotPassActivity.this, R.style.CustomDialogAnimation);
                slideDialog.setContentView(R.layout.select_country_popup);


                slideDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Setting dialogview
                Window window = slideDialog.getWindow();
                //  window.setGravity(Gravity.BOTTOM);

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                slideDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
                layoutParams.copyFrom(slideDialog.getWindow().getAttributes());

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.65);

                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = height;
                layoutParams.gravity = Gravity.BOTTOM;


                recyclerView = slideDialog.findViewById(R.id.recyclerview);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                selectCountryModelClasses = new ArrayList<>();

                for (int i = 0; i < image.length; i++) {
                    SelectCountryModelClass mycreditList = new SelectCountryModelClass(image[i],country_name[i],country_code[i]);
                    selectCountryModelClasses.add(mycreditList);
                }
                bAdapter = new SelectCountryRecycleAdapter(ForgotPassActivity.this,selectCountryModelClasses);
                recyclerView.setAdapter(bAdapter);

                slideDialog.getWindow().setAttributes(layoutParams);
                slideDialog.setCancelable(true);
                slideDialog.setCanceledOnTouchOutside(true);
                slideDialog.show();
            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMob.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter mobile no.",Toast.LENGTH_SHORT).show(); }

                else if(!isOnline()){
                    Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
                }else {
                    hitUrl();
                }
            }
        });

    }

    private void hitUrl() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FORGOTOTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("mob sned",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("1")){
                        JSONObject resultObj = jsonObject.getJSONObject("data");

                        Toast.makeText(getApplicationContext(), msg+"", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(),OTPActivity.class);
                        intent.putExtra("MobNo",etMob.getText().toString());
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
                param.put("user_phone",txt.getText().toString()+etMob.getText().toString());
               // Log.d("androidID",txt.getText().toString()+etMob.getText().toString());
               // txt.getText().toString()+
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    public void selectedCountry(Integer image, String country_code) {
        img.setImageResource(image);
        txt.setText(country_code);
        slideDialog.dismiss();
    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}