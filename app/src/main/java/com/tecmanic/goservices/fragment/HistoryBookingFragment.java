package com.tecmanic.goservices.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.tecmanic.goservices.Extra.Config;
import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.ModelClass.HistoryBookinModelclass;
import com.tecmanic.goservices.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tecmanic.goservices.Adapter.BookingHistoryAdapter;

import com.tecmanic.goservices.Extra.CustomVolleyJsonRequest;


public class HistoryBookingFragment extends Fragment {


    private View view;


    private List<HistoryBookinModelclass> historyBookingModelClasses =new ArrayList<>();
    public static RecyclerView recyclerView;
    private BookingHistoryAdapter bAdapter;

    private String title[] = {"Salon at home for Women","Massage for Men"};
    ProgressDialog progressDialog;
    Session_management sessionManagement;
    String userID;
    TextView btnBook;
   public static FrameLayout noData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_historybokking, container, false);

        sessionManagement=new Session_management(getContext());
        userID=sessionManagement.userId();
        noData= view.findViewById(R.id.noData);
        btnBook= view.findViewById(R.id.btnBook);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        /*category recyclerview code is here*/

        recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment = new HomeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayout, fragment);
                transaction.commit();            }
        });

        if(isOnline()){
            hitHistoryUrl(userID);
        }
        else {
            Toast.makeText(getContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
        return  view;
    }

    private void hitHistoryUrl(String userID) {
        progressDialog.show();
        historyBookingModelClasses.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id",userID);
       /* CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                bookingHISTORY, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                if (response != null && response.length() > 0) {

                Gson gson = new Gson();
                Type listType = new TypeToken<List<HistoryBookinModelclass>>() {
                }.getType();
                historyBookingModelClasses = gson.fromJson(response.toString(), listType);
                bAdapter = new BookingHistoryAdapter(getContext(), historyBookingModelClasses);
                recyclerView.setAdapter(bAdapter);
                bAdapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }
                else {
                    recyclerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }}*/
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Config.bookingHISTORY, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<HistoryBookinModelclass>>() {
                            }.getType();
                            historyBookingModelClasses = gson.fromJson(response.getString("data"), listType);
                            bAdapter = new BookingHistoryAdapter(getContext(), historyBookingModelClasses);
                            recyclerView.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            recyclerView.setAdapter(bAdapter);
                            bAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
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
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
