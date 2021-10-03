package com.tecmanic.goservices.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.goservices.Cart_Activity;
import com.tecmanic.goservices.R;

import java.util.List;

import com.tecmanic.goservices.Extra.DatabaseHandler;
import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.ModelClass.Add_on_model;

public class AddonShowadapter extends RecyclerView.Adapter<AddonShowadapter.ProductHolder> {
   List<Add_on_model> list;
    Activity activity;
    String Reward;
    SharedPreferences preferences;
    String language;

    Session_management session_management;
    int lastpostion;
    DatabaseHandler dbHandler;





    public AddonShowadapter(Cart_Activity activity, List<Add_on_model> ss) {
        this.activity=activity;
        this.list=ss;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addons_addlist, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {

        Add_on_model sas=list.get(position);
        holder.tv_title.setText(sas.getAdd_on_des());

       // holder.tv_contetiy.setText(map_list.get("qty"));

      }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_price,tv_contetiy;
        public ImageView iv_plus, iv_minus;

        public ProductHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}




/*public class AddonShowadapter extends BaseAdapter {

    private Activity activity;
    // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
    private static ArrayList title, notice;
    private static LayoutInflater inflater = null;
    List<String> ss;
    public AddonShowadapter(Activity a, ArrayList b, ArrayList bod) {
        activity = a;
        this.title = b;
        this.notice = bod;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public AddonShowadapter(Activity activity, List<String> ss) {
        this.activity=activity;
        this.ss=ss;

    }

    public int getCount() {
        return title.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.item_cart, null);

        TextView title2 = (TextView) vi.findViewById(R.id.title); // title
        String song = title.get(position).toString();
        title2.setText(song);


        TextView title22 = (TextView) vi.findViewById(R.id.price); // notice
        String song2 = notice.get(position).toString();
        title22.setText(song2);

        return vi;

    }*/
