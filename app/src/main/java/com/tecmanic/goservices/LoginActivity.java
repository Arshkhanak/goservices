package com.tecmanic.goservices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.provider.Settings;
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
import android.widget.Spinner;
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
import java.util.Objects;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tecmanic.goservices.Adapter.SelectCountryRecycleAdapter;
import com.tecmanic.goservices.Extra.Config;
import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.ModelClass.SelectCountryModelClass;

public class LoginActivity extends AppCompatActivity {

    Spinner spinner;
    Button button;
    Dialog slideDialog;
    LinearLayout sppiner;
    ImageView img;
    TextView txt,skip;

     ArrayList<SelectCountryModelClass> selectCountryModelClasses;
     RecyclerView recyclerView;
     SelectCountryRecycleAdapter bAdapter;

    private Integer image[] = {R.drawable.ic_india,R.drawable.ic_botswana,R.drawable.ic_canada,R.drawable.ic_mexico,R.drawable.ic_australia,R.drawable.ic_brazil,
                               R.drawable.ic_russia};
    private String country_name[] = {"India","Botswana","Canada","Mexico","Australia","Brazil","Russia"};
    private String country_code[] = {"+91","+267","+1","+52","+61","+91","+43"};

    EditText etmobile,etPass;
    TextView txtForgetPass,txtSignUp;
    ProgressDialog progressDialog;
    String androidID;
    String token;
    Session_management sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait!");
        progressDialog.setCancelable(false);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        token = "";
                        return;
                    }
                    token = Objects.requireNonNull(task.getResult()).getToken();
                });
        sessionManagement = new Session_management(LoginActivity.this);


        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        txtForgetPass=findViewById(R.id.txtForgetPass);
        txtSignUp=findViewById(R.id.txtSignUp);
        etmobile = findViewById(R.id.etMob);
        etPass = findViewById(R.id.etPassword);
        button = findViewById(R.id.button);
        skip = findViewById(R.id.skip);
        img = findViewById(R.id.image);
        txt = findViewById(R.id.country_code);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ForgotPassActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etmobile.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Mobile Number required!",Toast.LENGTH_SHORT).show(); }
                else if (etmobile.getText().toString().trim().length()>10) {
                    Toast.makeText(getApplicationContext(),"Valid Mobile Number required!",Toast.LENGTH_SHORT).show(); }
                else if(etPass.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Password required!",Toast.LENGTH_SHORT).show();
                }
                else if(!isOnline()){
                    Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
                }else {
                    loginUrl();
                } }
        });


        sppiner = findViewById(R.id.sppiner);
        sppiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideDialog = new Dialog(LoginActivity.this, R.style.CustomDialogAnimation);
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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LoginActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                selectCountryModelClasses = new ArrayList<>();

                for (int i = 0; i < image.length; i++) {
                    SelectCountryModelClass mycreditList = new SelectCountryModelClass(image[i],country_name[i],country_code[i]);
                    selectCountryModelClasses.add(mycreditList);
                }
                bAdapter = new SelectCountryRecycleAdapter(LoginActivity.this,selectCountryModelClasses);
                recyclerView.setAdapter(bAdapter);

                slideDialog.getWindow().setAttributes(layoutParams);
                slideDialog.setCancelable(true);
                slideDialog.setCanceledOnTouchOutside(true);
                slideDialog.show();
            }
        });

    }

    private void loginUrl() {

        if (token != null && !token.equalsIgnoreCase("")) {
            hitService();
        } else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            token = task.getResult().getToken();
                            loginUrl();
                        } else {
                            token = "";
                        }
                    });
        }
    }

    private void hitService() {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Login",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){
                        JSONObject resultObj = jsonObject.getJSONObject("data");

                        String user_id = resultObj.getString("id");
                        String user_fullname = resultObj.getString("user_name");
                        String user_email = resultObj.getString("user_email");
                        String user_phone = resultObj.getString("user_phone");
                        String user_image = resultObj.getString("user_image");
                        String cityid= resultObj.getString("city_id");
                        String cityname= resultObj.getString("city_name");

                         sessionManagement = new Session_management(LoginActivity.this);
                        sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, user_image,etPass.getText().toString());
                        sessionManagement.setCityID(cityid);
                        sessionManagement.setCityName(cityname);

                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(),HomePageActivity.class);
                        startActivity(intent);
                    }
                    else if(status.equalsIgnoreCase("3")){
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                    else{
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
                param.put("device_id",token);
                param.put("user_phone",txt.getText().toString()+etmobile.getText().toString());
                param.put("password",etPass.getText().toString());

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
