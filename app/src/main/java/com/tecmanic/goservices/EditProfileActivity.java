package com.tecmanic.goservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tecmanic.goservices.Adapter.CustomAdapter;
import com.tecmanic.goservices.Extra.CustomVolleyJsonRequest;
import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.ModelClass.SearchModel;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tecmanic.goservices.Extra.Config.CityListUrl;
import static com.tecmanic.goservices.Extra.Config.IMAGE_URL;
import static com.tecmanic.goservices.Extra.Config.Profile;
import static com.tecmanic.goservices.Extra.Config.ProfileEdit;

public class EditProfileActivity extends AppCompatActivity {

    ImageView back_img,circle,search;
    TextView title,btnSave;
    EditText eName,eEmail,ePhone;
    ProgressDialog progressDialog;
    Session_management sessionManagement;
    String userId;
    RelativeLayout rlImage;
    CircleImageView pImage;
    private static final int  RESULT_LOAD_IMAGE = 1;
    Bitmap bitmap;
    Uri filePath;
    String encodedImage;
    String city_id,cityName;
    private AppCompatSpinner city;
    final List<SearchModel> citylist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
    }

    private void init() {
        sessionManagement=new Session_management(this);
        userId= sessionManagement.userId();
        Log.d("dfgh",userId);
        /*category recyclerview code is here*/

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionbar_color)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.actionbar_color)); //status bar or the time bar at the top
        }

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        city=findViewById(R.id.city);

        title = findViewById(R.id.title);
        title.setText("Edit Profile");
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        pImage= findViewById(R.id.pImage);
        rlImage= findViewById(R.id.rlImage);
        eName= findViewById(R.id.eTname);
        eEmail= findViewById(R.id.etEmail);
        ePhone= findViewById(R.id.etPhone);
        btnSave= findViewById(R.id.btnSvae);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){

                save_EditprofileUrl(encodedImage); }
                else {
                    Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        rlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser1();
            }
        });

        if(isOnline()){
            userProfileUrl();
            cityUrl();
        }
        else {
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
    }
    private void showFileChooser1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE) {

            // selectedImage = data.getData();

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
             //   Glide.get(getApplicationContext()).clearMemory();

                   /* Glide.with(getApplicationContext()).asBitmap()

                            .load(bitmap)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    imageview_of_myprofile_page.setImageBitmap(bitmap);

                                }
                            });*/
                Picasso.with(getApplicationContext()).load(String.valueOf(bitmap)).error(R.drawable.logo_rapid).into(pImage);

//                Glide.with(getApplicationContext()).load(bitmap).apply(RequestOptions.skipMemoryCacheOf(true))
//                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(pImage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            progressDialog.show();
            save_EditprofileUrl(encodedImage);
        }

    }
/*
    private void save_EditprofileUrl() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ProfileEdit,
                new Response.Listener<NetworkResponse>() {

                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Log.d("ResponseBank",json);

                            JSONObject obj = new JSONObject(new String(response.data));
                            String status = obj.getString("status");
                            String msg = obj.getString("message");

                            if (status.contains("1")) {
                                btnSave.setEnabled(false);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                btnSave.setEnabled(true);
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try{}
                catch (Exception e){
                    e.printStackTrace();
                    e.toString();
                } }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //  String image = BitMapToString(bitmap);
                params.put("id",userId);
                params.put("user_phone",ePhone.getText().toString().trim());
                params.put("user_name",eName.getText().toString().trim());
                params.put("user_email",eEmail.getText().toString().trim());
                return params;
            }
            */
/*
             * Here we are passing image by renaming it with a unique name
             * *//*

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("user_profile",new DataPart(imagename + ".png", encodeToBase64(String.valueOf(bitmap))));
                Log.d("user_profile", String.valueOf(bitmap));
                return params;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(volleyMultipartRequest);
    }
*/

    private void save_EditprofileUrl(String encodedImage) {
        progressDialog.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_add_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id",userId);
        params.put("user_phone",ePhone.getText().toString().trim());
        params.put("user_name",eName.getText().toString().trim());
        params.put("user_email",eEmail.getText().toString().trim());
        params.put("user_profile",encodedImage);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ProfileEdit, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                progressDialog.dismiss();
                try {
                    String status = response.getString("status");
                    String msg = response.getString("message");
                    if (status.contains("1")) {
                        btnSave.setEnabled(false);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), msg+"", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), msg+"", Toast.LENGTH_SHORT).show();
                        btnSave.setEnabled(true);
                        progressDialog.dismiss();
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
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }
    private void userProfileUrl() {
        progressDialog.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Profile, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONObject obj = response.getJSONObject("data");
                        String user_id = obj.getString("id");
                        String user_fullname = obj.getString("user_name");
                        String user_email = obj.getString("user_email");
                        String user_phone = obj.getString("user_phone");
                        String user_image = obj.getString("user_image");
                        //  String password = obj.getString("user_password");
                        Picasso.with(getApplicationContext()).load(IMAGE_URL+user_image).error(R.drawable.logo_rapid).into(pImage);

                   /*   *//*  Glide.with(getApplicationContext()).load(IMAGE_URL+user_image).apply(RequestOptions.skipMemoryCacheOf(true))
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(pImage);
                      *//*  Glide.with(getApplicationContext()).
                                load(IMAGE_URL+user_image).
                                listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                                boolean isFirstResource) {
                                        if (e != null) {
                                            Log.d("glideexeception", e.getMessage());
                                            e.printStackTrace();
                                        }
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                                   DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                }).
                                into(pImage);*/
                        eEmail.setText(user_email);
                        eName.setText(user_fullname);
                        ePhone.setText(user_phone);

                    }}
                catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
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
    private void cityUrl() {
        // progressDialog.show();
        citylist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CityListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cityyyyyyyy", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        citylist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            city_id = jsonObject1.getString("city_id");
                            String city_name = jsonObject1.getString("city_name");

                            //    recyclerViewCity.setVisibility(View.VISIBLE);
                            SearchModel cs = new SearchModel(city_id, city_name);

                            citylist.add(cs);
                        }
                        progressDialog.dismiss();

                        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), citylist);
                        city.setAdapter(customAdapter);
                        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                cityName=citylist.get(i).getpNAme();
                                city_id=citylist.get(i).getId();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
                    } else {
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("parent","");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}