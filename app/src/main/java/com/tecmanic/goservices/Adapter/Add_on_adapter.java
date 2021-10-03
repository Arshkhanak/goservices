package com.tecmanic.goservices.Adapter;

import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.goservices.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.ModelClass.Add_on_model;

import static com.tecmanic.goservices.Extra.Config.cat_id_json_array;


public class Add_on_adapter extends RecyclerView.Adapter<Add_on_adapter.MyViewHolder> {

    Context context;
    int sum = 0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Integer> pricelist=new ArrayList<>();
    private List<Add_on_model> OfferList;

    Session_management session_management;
    private static int lastCheckedPos = 0;
    private static RadioButton lastChecked = null;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView price;
        CheckBox title;
        Boolean check = true;
        LinearLayout lladdon;

        public MyViewHolder(View view) {
            super(view);
            title = (CheckBox) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            lladdon=  view.findViewById(R.id.lladdon);
        }
    }


    public Add_on_adapter(Context context, List<Add_on_model> offerList) {
        this.OfferList = offerList;

        session_management=new Session_management(context);
        this.context = context;
    }

    @Override
    public Add_on_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_addon, parent, false);

        return new Add_on_adapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull Add_on_adapter.MyViewHolder holder, final int position) {

        sharedPreferences=context.getSharedPreferences("value",0);
        editor=sharedPreferences.edit();
        final Add_on_model lists = OfferList.get(position);
        cat_id_json_array = new JSONArray();

        holder.title.setText(lists.getAdd_on_des());

        JSONObject cat_id_obj = new JSONObject();

        holder.price.setText(context.getResources().getString(R.string.currency)+lists.getAdd_on_price());


        holder.title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    holder.check = true;
                    holder.lladdon.setBackgroundResource(R.drawable.rec_grey);
                    for (int i = 0; i < cat_id_json_array.length(); i++) {
                        try {
                            JSONObject obj = cat_id_json_array.getJSONObject(i);

                            String id = obj.getString("addon_id");

                            if (id.contains(lists.getDes_id())) {

                                pricelist.remove(i);
                                cat_id_json_array.remove(i);
                                cat_id_obj.put("addon_id","");
                                cat_id_obj.put("addon_name","");
                                cat_id_obj.put("addon_price","");
                                cat_id_json_array.put(cat_id_obj);
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    int smm = 0;
                    /*for(int j = 0; j < pricelist.size(); j++){

                        summ+= pricelist.get(j);

                    }*/

                    Log.d("adhdfh", String.valueOf(sum));
                    editor.putString("addon", String.valueOf(smm));
                    editor.apply();
                    editor.commit();

                }
              else  if (isChecked) {

                    if (holder.check) {
                        holder.check = false;
                        holder.lladdon.setBackgroundResource(R.drawable.blue_dateday_rect);
                        pricelist.add(Integer.valueOf(lists.getAdd_on_price()));

                        try {
                         int sum=0;
                           // String name = null;
                            for(int i = 0; i < pricelist.size(); i++){
                                sum += pricelist.get(i);
                              //  name=OfferList.get(i).getAdd_on_des();
                            }
                            editor.putString("addon", String.valueOf(sum));
                           // editor.putString("addname", name);
                            editor.apply();
                            editor.commit();
                            cat_id_obj.put("addon_id", lists.getDes_id());
                            cat_id_obj.put("addon_name", lists.getAdd_on_des());
                            cat_id_obj.put("addon_price", lists.getAdd_on_price());
                            cat_id_json_array.put(cat_id_obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {

                    holder.check = true;
                    holder.lladdon.setBackgroundResource(R.drawable.rec_grey);

                    for (int i = 0; i < cat_id_json_array.length(); i++) {

                        try {
                            JSONObject obj = cat_id_json_array.getJSONObject(i);

                            String id = obj.getString("addon_id");

                            if (id.contains(lists.getDes_id())) {

                                pricelist.remove(i);
                                cat_id_json_array.remove(i);
                                cat_id_obj.put("addon_id","");
                                cat_id_obj.put("addon_name","");
                                cat_id_obj.put("addon_price","");
                                cat_id_json_array.put(cat_id_obj);
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    int summ=0;
                    /*for(int j = 0; j < pricelist.size(); j++){

                        summ+= pricelist.get(j);

                    }*/

                    Log.d("adhdfh", String.valueOf(sum));
                    editor.putString("addon", String.valueOf(summ));
                    editor.apply();
                    editor.commit();

                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}




