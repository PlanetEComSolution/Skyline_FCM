package planet.info.skyline.tech.locate_crates;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import planet.info.skyline.network.Api;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.R;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivity;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.CreateDetails;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


public class LocateCrates extends BaseActivity {

    // private static final String url = "http://api.androidhive.info/json/movies.json";
  //  final ArrayList<String> list = new ArrayList<String>();
  //  public ProgressDialog pDialogm;
   // String webhit = "";
   // String[] countryArray = {"India", "Pakistan", "USA", "UK"};
   // String a, aa, epclient_id;
  //  String clientidme;
  //  long timeInMilliseconds = 0L;
  //  long timeSwapBuff = 0L;
   // long updatedTime = 0L;
    SharedPreferences sp;
    Editor ed;
    ImageView merchantname;
    TextView clientname, text_noCrate, txtvw_selectCrate;
    // ImageView btn_StopWork;
    ImageView missing, homeacti;
    String imageloc = "";
   // DisplayImageOptions options;
    int zz;
    // LinearLayout stopworkl;
    // ImageView  govleraselect;
    // ImageView imageupload;
    RecyclerView listView;
    ImageView img_search;
    EditText et_search;
    boolean ServerNotAvailable = true;
    private String urlskyline;//
    // "http://exhibitpower.com/crate_web_service.php?id=1321";
    private ProgressDialog pDialog;
    private List<CreateDetails> createlist = new ArrayList<CreateDetails>();
//    private HomeCreateAdapter adapters;


    private long startTime = 0L;
    private Handler customHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_homeact_new);

        sp = getApplicationContext().getSharedPreferences("skyline",
                MODE_PRIVATE);
        ed = sp.edit();
        listView = (RecyclerView) findViewById(R.id.listView1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        merchantname = (ImageView) findViewById(R.id.merchantname);

        merchantname.setImageResource(R.drawable.exhibitlogoa);
        clientname = (TextView) findViewById(R.id.textView1);
        text_noCrate = (TextView) findViewById(R.id.text_noCrate);

        txtvw_selectCrate = (TextView) findViewById(R.id.homescreen);
        text_noCrate.setVisibility(View.GONE);
        missing = findViewById(R.id.missing);
        missing.setVisibility(View.GONE);
        homeacti = findViewById(R.id.homeacti);

//        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.skylinelogopng)
//                .showImageForEmptyUri(R.drawable.skylinelogopng)
//                .showImageOnFail(R.drawable.skylinelogopng)
//                .cacheInMemory(true)
//                .cacheOnDisc(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .build();


        try {

           // String companyName = sp.getString("name", "");
            String companyName  = Shared_Preference.getCLIENT_NAME(this);
           // String logo = sp.getString("imglo", "");
            String logo =	Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
            imageloc = logo;

            if (imageloc.equals("") || imageloc.equalsIgnoreCase("")) {
                missing.setVisibility(View.GONE);
                clientname.setText(companyName);
                clientname.setVisibility(View.VISIBLE);
            } else {
                missing.setVisibility(View.VISIBLE);
                clientname.setVisibility(View.GONE);
                Glide
                        .with(LocateCrates.this)
                        .load(imageloc)     // Uri of the picture
                        .into(missing);
            }
        } catch (Exception e) {
            e.getCause();
        }


        homeacti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /*nks*/
        // Check if this module is accessed directly from home screen or after clock started


        final boolean TIMER_STARTED_FROM_BILLABLE_MODULE =   Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(this);
     //   final boolean TIMER_STARTED_FROM_BILLABLE_MODULE = sp.getBoolean(Utility.TIMER_STARTED_FROM_BILLABLE_MODULE, false);
        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            txtvw_selectCrate.setVisibility(View.VISIBLE);
            if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
             //   urlskyline = sp.getString("link", "");
                urlskyline = Shared_Preference.getLINK(this);
                getCrateList();
            } else {

                Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }
        } else {
            txtvw_selectCrate.setVisibility(View.GONE);
            Intent i = new Intent(LocateCrates.this, SelectCompanyActivity.class);
            i.putExtra(Utility.IS_JOB_MANDATORY,"0");
            i.putExtra(Utility.Show_DIALOG_SHOW_INFO,false);

            startActivityForResult(i, Utility.CODE_SELECT_COMPANY);
        }

        /**/

        search_Crate();


    }

    public void getCrateList() {
        try {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.Loading_text));
            pDialog.setCancelable(false);
            pDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }

        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, urlskyline,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                ServerNotAvailable = false;
                try {
                    parseJsonFeed(obj);
                } catch (Exception e) {
                    e.getMessage();
                }
                hideprogressdialog();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                ServerNotAvailable = true;
                if (createlist.size() > 0) {
                    text_noCrate.setVisibility(View.GONE);
                } else {
                    text_noCrate.setVisibility(View.VISIBLE);
                }
                hideprogressdialog();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    public void showprogressdialog() {

        try {
            pDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void hideprogressdialog() {


        try {
            pDialog.dismiss();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utility.CODE_SELECT_COMPANY) {

            if (resultCode == Activity.RESULT_OK) {
                try {
                    String CompID = data.getStringExtra("CompID");
                    if (CompID.equals("") || CompID == null) {
                        Toast.makeText(getApplicationContext(), "Please select company!", Toast.LENGTH_SHORT).show();
                        text_noCrate.setVisibility(View.VISIBLE);
                    } else {
                        urlskyline = URL_EP1 + Api.API_CRATES_LIST
                                + CompID;
                        if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                            getCrateList();
                        } else {
                            Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "Exception caught!", Toast.LENGTH_SHORT).show();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                finish();
            }
        }
    }

    private void parseJsonFeed(JSONObject response) {
/**
 * BHANU VERMA 17/02/2016
 */
        try {


            JSONArray Array = response.getJSONArray("data");
            JSONObject jsonObject = Array.getJSONObject(0);
               JSONArray client_info = jsonObject.getJSONArray("client_info");
               JSONObject jsonObject1 = client_info.getJSONObject(0);
              String companyLogo = jsonObject1.getString("logo");
               String companyName = jsonObject1.getString("name");


            try {

                if (companyLogo.equals("") ) {
                    missing.setVisibility(View.GONE);
                    clientname.setText(companyName);
                    clientname.setVisibility(View.VISIBLE);
                } else {
                    missing.setVisibility(View.VISIBLE);
                    clientname.setVisibility(View.GONE);
                    Glide
                            .with(LocateCrates.this)
                            .load(URL_EP1 + Api.API_COLLATERAL_PATH
                                    + companyLogo)     // Uri of the picture
                            .into(missing);
                }
            } catch (Exception e) {
                e.getCause();
            }



            JSONArray feedArray = jsonObject.getJSONArray("cds");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = feedArray.getJSONObject(i);
                CreateDetails item = new CreateDetails();
                item.setCretateid(feedObj.getString("id"));
                if (feedObj.getString("description") == null || feedObj.getString("description").equalsIgnoreCase("null") || feedObj.getString("description").trim().equalsIgnoreCase("")) {
                    item.setDescription("N/A");
                } else {
                    item.setDescription(feedObj.getString("description"));
                }
                item.setLocationcrate(feedObj.getString("cur_location"));
                item.setUnique_crate_id(feedObj.getString("crate_number"));
                item.setSelectim(0);
                item.setMissim(0);
                item.setPos(i);
                item.setSelected(false);
                createlist.add(item);
            }

            if (createlist.size() > 1) {
                Collections.reverse(createlist);
            }
            Log.d("BHANU", "" + createlist.toString());
            try {
                LocateCrateAdapter adapters = new LocateCrateAdapter(this, createlist);
                listView.setAdapter(adapters);

                if (createlist.size() > 0) {
                    text_noCrate.setVisibility(View.GONE);
                } else {
                    text_noCrate.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.getMessage();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (createlist.size() > 0) {
            text_noCrate.setVisibility(View.GONE);
        } else {
            text_noCrate.setVisibility(View.VISIBLE);
        }
    }


    public void search_Crate() {
        et_search = (EditText) findViewById(R.id.et_search);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    // et_search.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    // et_search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.searcher, 0, 0, 0);
                    // et_search.clearFocus();
                    et_search.setFocusable(false);
                    et_search.setFocusableInTouchMode(true);
                }
            }
        });
        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean gotfocus) {
                // TODO Auto-generated method stub
                if (gotfocus) {
                    et_search.setCompoundDrawables(null, null, null, null);
                    //   et_search.setHint("Search");
                } else if (!gotfocus) {
                    if (et_search.getText().length() == 0) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                        et_search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.searcher, 0, 0, 0);
                        //    et_search.setHint("");
                    }
                }
            }
        });

        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;

            }
        });
        img_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                search();

            }
        });

    }

    public void search() {
          /*     final ProgressDialog   progressDoalog = new ProgressDialog(LocateCrates.this);
               progressDoalog.setMessage("Searching , Kindly wait....");
               progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
               progressDoalog.setCancelable(false);
               progressDoalog.show();*/
        showprogressdialog();
        List<CreateDetails> new_list = new ArrayList<CreateDetails>();
        String search_text = et_search.getText().toString().trim().toLowerCase();
        for (int i = 0; i < createlist.size(); i++) {
            CreateDetails createDetails = createlist.get(i);
            String uniqueCrateId = createDetails.getUnique_crate_id().toLowerCase();
            String crateDesc = createDetails.getDescription().toLowerCase();
            if (uniqueCrateId.contains(search_text) || crateDesc.contains(search_text)) {
                new_list.add(createDetails);
            }
        }
        try {
            //    adapters = new HomeCreateAdapter(LocateCrates.this, new_list);

            LocateCrateAdapter adapters = new LocateCrateAdapter(this, new_list);

            listView.setAdapter(adapters);

            if (new_list.size() > 0) {
                text_noCrate.setVisibility(View.GONE);
            } else {
                text_noCrate.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("xsas", e.toString());
            // Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
        }
        // progressDoalog.dismiss();
        hideprogressdialog();
    }


    public class LocateCrateAdapter extends RecyclerView.Adapter<LocateCrateAdapter.MyViewHolder> {
        private List<CreateDetails> moviesList;

        public LocateCrateAdapter(Activity context, List<CreateDetails> moviesList) {
            this.moviesList = moviesList;

        }

        @Override
        public LocateCrateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_missing_crates_new, parent, false);

            return new LocateCrateAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final LocateCrateAdapter.MyViewHolder holder, final int position) {
            CreateDetails cc = moviesList.get(position);
            holder.creid.setText(cc.getUnique_crate_id());
            holder.credesc.setText(cc.getDescription());
            holder.index_no.setText(String.valueOf(position + 1));
            String location = cc.getLocationcrate();
            if (location == null || location.equalsIgnoreCase("null") || location.equalsIgnoreCase("")) {
                holder.creloca.setText("N/A");
            } else {
                holder.creloca.setText(location);
            }

            holder.selectt.setChecked(cc.isSelected());


            holder.selectt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        moviesList.get(position).setSelected(true);
                    } else {
                        moviesList.get(position).setSelected(false);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView creid;
            TextView credesc;
            TextView creloca;
            CheckBox selectt;
            Button index_no;


            public MyViewHolder(View convertView) {
                super(convertView);
                creid = (TextView) convertView.findViewById(R.id.createid);
                credesc = (TextView) convertView.findViewById(R.id.description);
                creloca = (TextView) convertView.findViewById(R.id.locationss);
                selectt = (CheckBox) convertView.findViewById(R.id.selects);
                index_no = (Button) convertView.findViewById(R.id.serial_no);

            }
        }
    }


}


