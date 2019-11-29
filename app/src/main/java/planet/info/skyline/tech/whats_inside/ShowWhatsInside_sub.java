package planet.info.skyline.tech.whats_inside;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.R;
import planet.info.skyline.adapter.Adapter_Sub_WhatsInside;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.util.Utility;


public class ShowWhatsInside_sub extends BaseActivity {
    ListView jobs_list_View;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_whats_inside_sub);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jobs_list_View = (ListView) findViewById(R.id.cart_listview);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String url = b.getString("url");
            getData(url);
        }
    }


    public void getData(String url) {
        final ProgressDialog ringProgressDialog = new ProgressDialog(ShowWhatsInside_sub.this);
        ringProgressDialog.setMessage("Kindly wait...");
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        JsonObjectRequest bb = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub

                //    tv_msg.setVisibility(View.GONE);
                try {
                    JSONArray jsonArryy = obj.getJSONArray("cds");
                    ArrayList<HashMap<String, String>> al_data;

                    try {

                        ArrayList<String> al = new ArrayList<>();
                        al.add("qq");
                        al.add("descr");
                        al.add("crate_number");
                        al.add("cur_loacation");
                        al_data = Utility.JSONEncoding(jsonArryy, al);
                        setData(al_data);
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    //


                    ringProgressDialog.dismiss();


                } catch (Exception e) {
                    e.getMessage();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                ringProgressDialog.dismiss();
                //  tv_msg.setVisibility(View.VISIBLE);


            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }
public void setData(ArrayList<HashMap<String,String>> data)
{
    Adapter_Sub_WhatsInside ad = new Adapter_Sub_WhatsInside(ShowWhatsInside_sub.this, data);
    jobs_list_View.setAdapter(ad);

}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
