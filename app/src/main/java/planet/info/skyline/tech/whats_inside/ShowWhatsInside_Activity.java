package planet.info.skyline.tech.whats_inside;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import planet.info.skyline.network.Api;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class ShowWhatsInside_Activity extends BaseActivity {
    private FragmentTabHost mTabHost;
    Bundle bundle_element,
            bundle_graphics, bundle_crates;


    String crateId;
    ArrayList<HashMap<String, String>> al_element1;
    ArrayList<HashMap<String, String>> al_element2;
    ProgressDialog ringProgressDialog;
    ArrayList<HashMap<String, String>> al_Crates1;
    ArrayList<HashMap<String, String>> al_Crates2;

    ArrayList<HashMap<String, String>> al_graphics1;
    ArrayList<HashMap<String, String>> al_graphics2;


    int crateCount_1 = 0, crateCount_2 = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_inside);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.realtabcontent);


        Bundle b = getIntent().getExtras();
        if (b != null) {
            crateId = b.getString(Utility.KEY_CRATE_ID, null);
        }
        if (crateId != null) {
            String urlCrateId = URL_EP1 + Api.API_SHOW_CRATE + crateId;

            if (new ConnectionDetector(ShowWhatsInside_Activity.this).isConnectingToInternet()) {

                getCrateData(urlCrateId);
            } else {
                Toast.makeText(ShowWhatsInside_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }








        } else {
            Toast.makeText(getApplicationContext(), "Invalid Crate Number", Toast.LENGTH_SHORT).show();
        }


    }


    private View gettabtxt(String stringtxt) {
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = (View) inflater1.inflate(R.layout.tabhost_custom_tabs, null);
        TextView tv1 = (TextView) v1.findViewById(R.id.txttitle);
        tv1.setText(stringtxt);
        ImageView img = (ImageView) v1.findViewById(R.id.imageView);
        img.setVisibility(View.GONE);
        return v1;
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


    public void getCrateData(String urlCrateId) {
        ringProgressDialog = new ProgressDialog(ShowWhatsInside_Activity.this);
        ringProgressDialog.setMessage("Kindly wait...");
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        JsonObjectRequest bb = new JsonObjectRequest(Request.Method.GET, urlCrateId,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub

                //    tv_msg.setVisibility(View.GONE);
                try {
                    JSONArray jsonArryy = obj.getJSONArray("data");
                    JSONObject obj1 = jsonArryy.getJSONObject(0);

                    //Element 1
                    JSONArray jsonArray1 = null;
                    al_element1 = new ArrayList<>();
                    bundle_element = new Bundle();
                    bundle_element.putString(Utility.KEY_WHATSINSIDE_TAB_ID, "0");
                    try {
                        jsonArray1 = obj1.getJSONArray("element1");
                        ArrayList<String> al = new ArrayList<>();
                        al.add("ename");
                        al.add("descr");
                        al.add("quan");
                        al.add("cnt");
                        al.add("typename");
                        al.add("main_acc");
                        al.add("ufile");

                        al_element1 = Utility.JSONEncoding(jsonArray1, al);
                        //   bundle_element.putSerializable(Utility.KEY_BUNDLE_ELEMENT_1, al_element1);

                    } catch (Exception e) {
                        e.getMessage();
                    }

                    //

                    //Element 2
                    JSONArray jsonArray2 = null;
                    al_element2 = new ArrayList<>();
                    try {
                        jsonArray2 = obj1.getJSONArray("element2");
                        ArrayList<String> al2 = new ArrayList<>();
                        al2.add("ename");
                        al2.add("descr");
                        al2.add("quan");
                        al2.add("cnt");
                        al2.add("typename");
                        al2.add("main_acc");
                        al2.add("ufile");

                        al_element2 = Utility.JSONEncoding(jsonArray2, al2);
                        //   bundle_element.putSerializable(Utility.KEY_BUNDLE_ELEMENT_2, al_element2);

                    } catch (Exception e) {
                        e.getMessage();
                    }


                    //
                    //Graphics 1
                    JSONArray jsonArray3 = null;
                    al_graphics1 = new ArrayList<>();
                    bundle_graphics = new Bundle();
                    bundle_graphics.putString(Utility.KEY_WHATSINSIDE_TAB_ID, "1");
                    try {
                        jsonArray3 = obj1.getJSONArray("graphics1");
                        ArrayList<String> al2 = new ArrayList<>();
                        al2.add("TYPE");
                        al2.add("configure_id");
                        al2.add("grap_name");
                        al2.add("gtype");
                        al2.add("description");
                        //  al2.add("cnt");
                        al2.add("ImageBig");
                        al2.add("cnt2");
                        al2.add("mquantity");
                        al2.add("main_acc");

                        al_graphics1 = Utility.JSONEncoding(jsonArray3, al2);

                        //  bundle_graphics.putSerializable(Utility.KEY_BUNDLE_GRAPHICS_1, al_graphics1);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //
                    //Graphics 2
                    JSONArray jsonArray4 = null;
                    al_graphics2 = new ArrayList<>();
                    try {
                        jsonArray4 = obj1.getJSONArray("graphics2");
                        ArrayList<String> al2 = new ArrayList<>();
                        al2.add("TYPE");
                        al2.add("configure_id");
                        al2.add("grap_name");
                        al2.add("gtype");
                        al2.add("description");
                        // al2.add("cnt");
                        al2.add("ImageBig");
                        al2.add("cnt2");
                        al2.add("mquantity");
                        al2.add("main_acc");
                        al_graphics2 = Utility.JSONEncoding(jsonArray4, al2);
                        // bundle_graphics.putSerializable(Utility.KEY_BUNDLE_GRAPHICS_2, al_graphics2);

                    } catch (Exception e) {
                        e.getMessage();
                    }

                    //

                    //Crates 1
                    JSONArray jsonArray5 = null;
                    al_Crates1 = new ArrayList<>();
                    bundle_crates = new Bundle();
                    bundle_crates.putString(Utility.KEY_WHATSINSIDE_TAB_ID, "2");
                    try {
                        jsonArray5 = obj1.getJSONArray("crates1");
                        ArrayList<String> al3 = new ArrayList<>();
                        al3.add("id");
                        al3.add("crates_configure");
                        al3.add("crate_number");
                        al_Crates1 = Utility.JSONEncoding(jsonArray5, al3);

                        //  bundle_crates.putSerializable(Utility.KEY_BUNDLE_CRATES_1, al_Crates1);
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    //

                    //Crates 2
                    JSONArray jsonArray6 = null;
                    al_Crates2 = new ArrayList<>();
                    try {
                        jsonArray6 = obj1.getJSONArray("crates2");
                        ArrayList<String> al3 = new ArrayList<>();
                        al3.add("id");
                        al3.add("crates_configure");
                        al3.add("crate_number");
                        al_Crates2 = Utility.JSONEncoding(jsonArray6, al3);
                        //  bundle_crates.putSerializable(Utility.KEY_BUNDLE_CRATES_2, al_Crates2);

                    } catch (Exception e) {
                        e.getMessage();
                    }

                    //

                    //  setTabHost();
                    //  ringProgressDialog.dismiss();


                    if (new ConnectionDetector(ShowWhatsInside_Activity.this).isConnectingToInternet()) {

                        new Task_checkImgExists_element1().execute();
                    } else {
                        Toast.makeText(ShowWhatsInside_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }


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


    private class Task_checkImgExists_element1 extends AsyncTask<String, Void, String> {
        //  ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            // progressDoalog = new ProgressDialog(ShowWhatsInside_Activity.this);
            //  progressDoalog.setMessage(getString(R.string.Loading_text));
            //  progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //  progressDoalog.setCancelable(true);
            //   progressDoalog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < al_element1.size(); i++) {

                String url = al_element1.get(i).get("ufile");
                url = url.replaceAll(Pattern.quote(".."), "");
              //  url = URL_EP1 + "/admin/uploads/accessories/" + url;
                url = URL_EP1 + Api.API_ELEMENT_PATH+ url;

                url = url.replaceAll(Pattern.quote(" "), "%20");

                try {
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                    con.setRequestMethod("HEAD");
                    //  System.out.println(con.getResponseCode());
                    // return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    } else {
                        HashMap<String, String> hashMap = al_element1.get(i);
                        hashMap.put("ufile", "");
                        al_element1.set(i, hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String str) {

            //  if (progressDoalog.isShowing()) {
            //      progressDoalog.cancel();
            //  }
            bundle_element.putSerializable(Utility.KEY_BUNDLE_ELEMENT_1, al_element1);

            new Task_checkImgExists_element2().execute();

        }
    }

    private class Task_checkImgExists_element2 extends AsyncTask<String, Void, String> {
        //  ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            // progressDoalog = new ProgressDialog(ShowWhatsInside_Activity.this);
            //  progressDoalog.setMessage(getString(R.string.Loading_text));
            //  progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //  progressDoalog.setCancelable(true);
            //   progressDoalog.show();

        }

        @Override
        protected String doInBackground(String... params) {


            for (int i = 0; i < al_element2.size(); i++) {

                String url = al_element2.get(i).get("ufile");
                url = url.replaceAll(Pattern.quote(".."), "");
                url = URL_EP1 + Api.API_ELEMENT_PATH + url;
                url = url.replaceAll(Pattern.quote(" "), "%20");


                try {
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                    con.setRequestMethod("HEAD");
                    // System.out.println(con.getResponseCode());
                    // return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    } else {
                        HashMap<String, String> hashMap = al_element2.get(i);
                        hashMap.put("ufile", "");
                        al_element2.set(i, hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String str) {

            //  if (progressDoalog.isShowing()) {
            //      progressDoalog.cancel();
            //  }
            bundle_element.putSerializable(Utility.KEY_BUNDLE_ELEMENT_2, al_element2);
            new Task_checkImgExists_Graphics1().execute();
            //  setTabHost();
        }
    }

    private class Task_checkImgExists_Graphics1 extends AsyncTask<String, Void, String> {
        //  ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            // progressDoalog = new ProgressDialog(ShowWhatsInside_Activity.this);
            //  progressDoalog.setMessage(getString(R.string.Loading_text));
            //  progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //  progressDoalog.setCancelable(true);
            //   progressDoalog.show();

        }

        @Override
        protected String doInBackground(String... params) {


            for (int i = 0; i < al_graphics1.size(); i++) {

                String url = al_graphics1.get(i).get("ImageBig");
                url = url.replaceAll(Pattern.quote(".."), "");

                url = URL_EP1 + "/admin" + url;
                url = url.replaceAll(Pattern.quote(" "), "%20");

                try {
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                    con.setRequestMethod("HEAD");
                    //  System.out.println(con.getResponseCode());
                    // return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    } else {
                        HashMap<String, String> hashMap = al_graphics1.get(i);
                        hashMap.put("ImageBig", "");
                        al_graphics1.set(i, hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String str) {

            //  if (progressDoalog.isShowing()) {
            //      progressDoalog.cancel();
            //  }
            bundle_graphics.putSerializable(Utility.KEY_BUNDLE_GRAPHICS_1, al_graphics1);

            new Task_checkImgExists_Graphics2().execute();

        }
    }

    private class Task_checkImgExists_Graphics2 extends AsyncTask<String, Void, String> {
        //  ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            // progressDoalog = new ProgressDialog(ShowWhatsInside_Activity.this);
            //  progressDoalog.setMessage(getString(R.string.Loading_text));
            //  progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //  progressDoalog.setCancelable(true);
            //   progressDoalog.show();

        }

        @Override
        protected String doInBackground(String... params) {


            for (int i = 0; i < al_graphics2.size(); i++) {

                String url = al_graphics2.get(i).get("ImageBig");
                url = url.replaceAll(Pattern.quote(".."), "");

                url = URL_EP1 + "/admin" + url;
                url = url.replaceAll(Pattern.quote(" "), "%20");

                try {
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                    con.setRequestMethod("HEAD");
                    // System.out.println(con.getResponseCode());
                    // return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    } else {
                        HashMap<String, String> hashMap = al_graphics2.get(i);
                        hashMap.put("ImageBig", "");
                        al_graphics2.set(i, hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String str) {

            //  if (progressDoalog.isShowing()) {
            //      progressDoalog.cancel();
            //  }
            bundle_graphics.putSerializable(Utility.KEY_BUNDLE_GRAPHICS_2, al_graphics2);


            crateCount_1 = 0;
            crateCount_2 = 0;
            if (al_Crates1.size() > crateCount_1) {
                getInsideCrate_1_Info();
            } else if (al_Crates2.size() > crateCount_2) {
                getInsideCrate_2_Info();
            } else {
                setTabHost();
            }


        }
    }


    public void getInsideCrate_1_Info() {

        final HashMap<String, String> hashMap = al_Crates1.get(crateCount_1);
        String crate_id = hashMap.get("id");
        String urlCrateId = URL_EP1 + Api.API_SHOW_CRATE + crate_id;


        JsonObjectRequest bb = new JsonObjectRequest(Request.Method.GET, urlCrateId,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //    tv_msg.setVisibility(View.GONE);
                int size = 0;
                try {
                    JSONArray jsonArryy = obj.getJSONArray("data");
                    JSONObject obj1 = jsonArryy.getJSONObject(0);

                    //Element 1
                    JSONArray jsonArray1 = new JSONArray();
                    try {
                        jsonArray1 = obj1.getJSONArray("element1");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //
                    //Element 2
                    JSONArray jsonArray2 = new JSONArray();
                    try {
                        jsonArray2 = obj1.getJSONArray("element2");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //
                    //Graphics 1
                    JSONArray jsonArray3 = new JSONArray();
                    try {
                        jsonArray3 = obj1.getJSONArray("graphics1");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    //Graphics 2
                    JSONArray jsonArray4 = new JSONArray();
                    try {
                        jsonArray4 = obj1.getJSONArray("graphics2");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //
                    //Crates 1
                    JSONArray jsonArray5 = new JSONArray();
                    try {
                        jsonArray5 = obj1.getJSONArray("crates1");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //
                    //Crates 2
                    JSONArray jsonArray6 = new JSONArray();
                    try {
                        jsonArray6 = obj1.getJSONArray("crates2");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    size = jsonArray1.length() + jsonArray2.length() + jsonArray3.length() + jsonArray4.length() + jsonArray5.length() + jsonArray6.length();

                    if (size > 0) {
                        hashMap.put(Utility.KEY_IS_EMPTY, "false");
                        al_Crates1.set(crateCount_1, hashMap);
                    } else {
                        hashMap.put(Utility.KEY_IS_EMPTY, "true");
                        al_Crates1.set(crateCount_1, hashMap);
                    }

                    crateCount_1 = crateCount_1 + 1;
                    if (al_Crates1.size() > crateCount_1) {
                        getInsideCrate_1_Info();
                    } else {

                        bundle_crates.putSerializable(Utility.KEY_BUNDLE_CRATES_1, al_Crates1);
                        crateCount_2 = 0;

                        if (al_Crates2.size() > crateCount_2) {
                            getInsideCrate_2_Info();
                        } else {
                            setTabHost();
                        }

                    }


                } catch (Exception e) {
                    e.getMessage();

                    bundle_crates.putSerializable(Utility.KEY_BUNDLE_CRATES_1, al_Crates1);
                    crateCount_2 = 0;

                    if (al_Crates2.size() > crateCount_2) {
                        getInsideCrate_2_Info();
                    } else {
                        setTabHost();
                    }


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //  ringProgressDialog.dismiss();
                //  tv_msg.setVisibility(View.VISIBLE);

                bundle_crates.putSerializable(Utility.KEY_BUNDLE_CRATES_1, al_Crates1);
                crateCount_2 = 0;
                if (al_Crates2.size() > crateCount_2) {
                    getInsideCrate_2_Info();
                } else {
                    setTabHost();
                }

            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void getInsideCrate_2_Info() {


        final HashMap<String, String> hashMap = al_Crates2.get(crateCount_2);
        String crate_id = hashMap.get("id");
        String urlCrateId = URL_EP1 + Api.API_SHOW_CRATE+ crate_id;


        JsonObjectRequest bb = new JsonObjectRequest(Request.Method.GET, urlCrateId,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //    tv_msg.setVisibility(View.GONE);
                int size = 0;
                try {
                    JSONArray jsonArryy = obj.getJSONArray("data");
                    JSONObject obj1 = jsonArryy.getJSONObject(0);

                    //Element 1
                    JSONArray jsonArray1 = new JSONArray();
                    try {
                        jsonArray1 = obj1.getJSONArray("element1");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //
                    //Element 2
                    JSONArray jsonArray2 = new JSONArray();
                    try {
                        jsonArray2 = obj1.getJSONArray("element2");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //
                    //Graphics 1
                    JSONArray jsonArray3 = new JSONArray();
                    try {
                        jsonArray3 = obj1.getJSONArray("graphics1");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    //Graphics 2
                    JSONArray jsonArray4 = new JSONArray();
                    try {
                        jsonArray4 = obj1.getJSONArray("graphics2");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //
                    //Crates 1
                    JSONArray jsonArray5 = new JSONArray();
                    try {
                        jsonArray5 = obj1.getJSONArray("crates1");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    //
                    //Crates 2
                    JSONArray jsonArray6 = new JSONArray();
                    try {
                        jsonArray6 = obj1.getJSONArray("crates2");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    size = jsonArray1.length() + jsonArray2.length() + jsonArray3.length() + jsonArray4.length() + jsonArray5.length() + jsonArray6.length();

                    if (size > 0) {
                        hashMap.put(Utility.KEY_IS_EMPTY, "false");
                        al_Crates2.set(crateCount_2, hashMap);
                    } else {
                        hashMap.put(Utility.KEY_IS_EMPTY, "true");
                        al_Crates2.set(crateCount_2, hashMap);
                    }

                    crateCount_2 = crateCount_2 + 1;
                    if (al_Crates2.size() > crateCount_2) {
                        getInsideCrate_2_Info();
                    } else {
                        bundle_crates.putSerializable(Utility.KEY_BUNDLE_CRATES_2, al_Crates2);
                        setTabHost();
                    }


                } catch (Exception e) {
                    e.getMessage();
                    bundle_crates.putSerializable(Utility.KEY_BUNDLE_CRATES_2, al_Crates2);
                    setTabHost();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //  ringProgressDialog.dismiss();
                //  tv_msg.setVisibility(View.VISIBLE);
                bundle_crates.putSerializable(Utility.KEY_BUNDLE_CRATES_2, al_Crates2);
                setTabHost();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    private void setTabHost() {

        mTabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getResources().getColor(R.color.gray_btn_bg_color));
        drawable.setSize(1, 1);
        mTabHost.getTabWidget().setDividerDrawable(drawable);

        mTabHost.addTab(mTabHost.newTabSpec("0").setIndicator(gettabtxt("Element(s)")), ShowWhatsInside_Fragment.class, bundle_element);
        mTabHost.addTab(mTabHost.newTabSpec("1").setIndicator(gettabtxt("Graphic(s)")), ShowWhatsInside_Fragment.class, bundle_graphics);
        mTabHost.addTab(mTabHost.newTabSpec("2").setIndicator(gettabtxt("Crate(s)")), ShowWhatsInside_Fragment.class, bundle_crates);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.tab_unselected));//#5F3C3A

        }
        mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(getResources().getColor(R.color.tab_selected));
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub

                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.tab_unselected));
                }
                mTabHost.getTabWidget().getChildAt(Integer.parseInt(tabId)).setBackgroundColor(getResources().getColor(R.color.tab_selected));
            }

        });


        if (ringProgressDialog.isShowing()) {
            ringProgressDialog.dismiss();
        }

    }


}
