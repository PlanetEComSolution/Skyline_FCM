package planet.info.skyline.old_activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.adapter.Missingcreateadapter;
import planet.info.skyline.adapter.Selectedcreateadapter;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.model.Misingcreate;
import planet.info.skyline.model.Selectcreate;
import planet.info.skyline.tech.billable_timesheet.Clock_Submit_Type_Activity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_missing_crate;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class Selectmissing extends BaseActivity {
    String urlforselect = "";
    private ProgressDialog pDialog;
    private List<Misingcreate> createlist = new ArrayList<Misingcreate>();
    private List<Selectcreate> createlistSelectcreate = new ArrayList<Selectcreate>();
    Missingcreateadapter adaptermiss;
    SharedPreferences sp;
    Editor ed;
    Selectedcreateadapter adapterselect;
    ListView missing, select;
    private TextView timerValue;

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;

    long timeSwapBuff = 0L;

    long updatedTime = 0L;
    String webhit = "";
    LinearLayout stopww;
    ImageView merchantname, missingim;
    DisplayImageOptions options;
    TextView textView1;
    ImageView homeacti;
    ImageView gomissi;
    String selectcrateforworkstation = "";
    String allmissingg = "";
    String urlofwebservice = URL_EP2+"/WebService/techlogin_service.asmx?op=missing_crate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selectmissing_new);
        stopww = (LinearLayout) findViewById(R.id.innnnnn);
        sp = getApplicationContext().getSharedPreferences("skyline",
                getApplicationContext().MODE_PRIVATE);
        ed = sp.edit();
        String firstscreenselect = sp.getString("select", "");
        Intent ii = getIntent();
        String missingcreate = ii.getStringExtra("missingcreate");//
        missingim = (ImageView) findViewById(R.id.missing);
        missingim.setVisibility(View.VISIBLE);
        String selectcreate = ii.getStringExtra("selectcreate");
        timerValue = (TextView) findViewById(R.id.timer);
        // startTime = SystemClock.uptimeMillis();
        merchantname = (ImageView) findViewById(R.id.merchantname);
        textView1 = (TextView) findViewById(R.id.textView1);
        merchantname.setImageResource(R.drawable.exhibitlogoa);
        gomissi = (ImageView) findViewById(R.id.gomissi);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver, new IntentFilter("subtask"));

        // long hh= sp.getLong("times", 0);
        // startTime = SystemClock.uptimeMillis();
        // startTime=startTime-hh;
        // customHandler.postDelayed(updateTimerThread, 1000);

        homeacti = (ImageView) findViewById(R.id.homeacti);

        gomissi.setClickable(true);


        homeacti.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Selectmissing.this,
                        Clock_Submit_Type_Activity.class);

                startActivity(ii);
                // finish();

            }
        });

        missing = (ListView) findViewById(R.id.listViewmissing);
        select = (ListView) findViewById(R.id.listViewselect);
        String dd = ii.getStringExtra("uuu");
        try {
            dd = dd.replace("sel=", "sel=" + firstscreenselect);
        } catch (Exception e) {
            // dd=URL_EP1+"/crate_web_service_updates.php?sel="+firstscreenselect+"&mis=";

        }
        String selectcreatse = dd;// ii.getStringExtra("uuu");
        String fd = selectcreatse;
        String dss = selectcreate;
        // missing.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        //
        //
        // }
        // });
        // select.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        //
        //
        // }
        // });
        sp = getApplicationContext().getSharedPreferences("skyline",
                MODE_PRIVATE);
        ed = sp.edit();
        merchantname = (ImageView) findViewById(R.id.merchantname);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.skylinelogopng)

                .showImageForEmptyUri(R.drawable.skylinelogopng)
                .showImageOnFail(R.drawable.skylinelogopng).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

      //  String imageloc = sp.getString("imglo", "");
        String imageloc =	Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
        if (imageloc.equals("") || imageloc.equalsIgnoreCase("")) {
          //  ed.putString("imglo", "").commit();
            Shared_Preference.setCLIENT_IMAGE_LOGO_URL(this,"");
            missingim.setVisibility(View.GONE);
        } else {
            imageLoadery.displayImage(imageloc, missingim, options);
        }
      //  String name = sp.getString("name", "");
        String name  = Shared_Preference.getCLIENT_NAME(Selectmissing.this);
        textView1.setText(name);
        try {
            adaptermiss = new Missingcreateadapter(this, createlist);
            adapterselect = new Selectedcreateadapter(this,
                    createlistSelectcreate);
            missing.setAdapter(adaptermiss);
            select.setAdapter(adapterselect);




        } catch (Exception e) {

        }
        urlforselect = selectcreatse;

        try {
            if (urlforselect.contains(",&mis")) {
                urlforselect = urlforselect.replace(",&mis", "&mis");
            } else {

            }
        } catch (Exception e) {

        }
        selectcrateforworkstation = urlforselect;

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        getjsonobject();
        try {
            stopww.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
        /*			stopwork1();//
					String dd = sp.getString("jobid", "");
					String clientidme = sp.getString("clientid", "");
					// webhit=URL_EP2+"/Register/auto_generate_event2.aspx?swo_id="+dd+"&status=stop";
					webhit = URL_EP2+"/Register/auto_generate_event2.aspx?done_by="
							+ clientidme + "&swo_id=" + dd + "&status=stop";
					Call_Api_MoveCrateToBin_Freight_Area();
					// callifworkwithnonbillable(7);
					alertforsurework();
					comment on date 10/2/2015
					*/
                    startActivity(new Intent(Selectmissing.this, Routinestopingwork.class));
                    finish();
                    // Boolean bb = sp.getBoolean("billable", false);
                    // if (bb) {
                    // callifworkwithnonbillable(5);
                    // } else {
                    // Intent i = new Intent(getApplicationContext(),
                    // MainActivity.class);
                    // // set the new task and clear flags
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    // | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    // startActivity(i);
                    // }
                }
            });
        } catch (Exception e) {
            Log.e("sds", e.toString());
        }
     gomissi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    // TODO Auto-generated method stub
                    if (adaptermiss.isEmpty()) {
                        String rough = selectcrateforworkstation;
                        String result = rough.substring(rough.indexOf("sel=") + 4, rough.indexOf("&mis"));
                        Intent ii = new Intent(Selectmissing.this, Scanworklocationstart.class);
                        ii.putExtra("realselect", result);
                        int x = adapterselect.getCount();
                        ed.putInt("select_total", x);
                        ed.commit();
                        ii.putExtra("tot", x);
                        startActivity(ii);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Not allowed when have missing crate ",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }
            }
        });

    }

    public void getjsonobject() {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, urlforselect,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                parseJsonFeed(obj);//allmissingg
                if (new ConnectionDetector(Selectmissing.this).isConnectingToInternet()) {
                    new asyntask().execute();

                } else {
                    Toast.makeText(Selectmissing.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }




                //hideprogressdialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                hideprogressdialog();

            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    public void showprogressdialog() {
        pDialog.show();
    }

    public void hideprogressdialog() {
        pDialog.dismiss();
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("cds");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                Misingcreate item = new Misingcreate();
                String foundmis = feedObj.getString("found_miss");
                if (foundmis.equalsIgnoreCase("found")
                        || foundmis.equals("found")) {
                    Selectcreate itemsel = new Selectcreate();
                    itemsel.setCretateid(feedObj.getString("id"));
                    itemsel.setDescription(feedObj.getString("description"));
                    itemsel.setLocationcrate(feedObj.getString("cur_loacation"));
                    item.setFound_miss(feedObj.getString("found_miss"));
                    itemsel.setUnique_crate_id(feedObj.getString("uni_crates"));

                    item.setSelectim(0);
                    item.setMissim(0);
                    itemsel.setPos(i);

                    createlistSelectcreate.add(itemsel);
                } else {
                    String unicod = feedObj.getString("uni_crates");
                    item.setCretateid(feedObj.getString("id"));
                    item.setDescription(feedObj.getString("description"));
                    item.setLocationcrate(feedObj.getString("cur_loacation"));
                    item.setFound_miss(feedObj.getString("found_miss"));
                    item.setUnique_crate_id(unicod);
                    item.setPos(i);
                    item.setSelectim(0);
                    item.setMissim(0);
                    createlist.add(item);
                    allmissingg = allmissingg + " ," + unicod;

                }
				/*
				 * Selectcreate itemsel=new Selectcreate();
				 * item.setCretateid(feedObj.getString("id"));
				 * item.setDescription(feedObj.getString("description"));
				 * item.setLocationcrate(feedObj.getString("cur_loacation"));
				 * item.setFound_miss(feedObj.getString("found_miss"));
				 * item.setPos(i);
				 * 
				 * itemsel.setCretateid(feedObj.getString("id"));
				 * itemsel.setDescription(feedObj.getString("description"));
				 * itemsel.setLocationcrate(feedObj.getString("cur_loacation"));
				 * item.setFound_miss(feedObj.getString("found_miss"));
				 * itemsel.setPos(i);
				 * 
				 * 
				 * createlistSelectcreate.add(itemsel); createlist.add(item);
				 */
            }

            // notify data changes to list adapater
            adaptermiss.notifyDataSetChanged();
            adapterselect.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getjsonobject1() {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, webhit, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject obj) {
                        // TODO Auto-generated method stub
                        // finish();
                        // java.lang.System.exit(0);
                        ed.putLong("times", 0);

                        // hideprogressdialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                // hideprogressdialog();
                ed.putLong("times", 0);
                // finish();
                // java.lang.System.exit(0);
                // moveTaskToBack(false);
                Log.e(" mainactivity", arg0.toString());

            }
        });
		/*
		 * JsonObjectRequest jsonobj =new JsonObjectRequest(urlskyline, null,
		 * new Response.Listener<JSONObject>() {
		 * 
		 * @Override public void onResponse(JSONObject arg0) { // TODO
		 * Auto-generated method stub
		 * 
		 * } }, new Response.ErrorListener() {
		 * 
		 * @Override public void onErrorResponse(VolleyError arg0) { // TODO
		 * Auto-generated method stub
		 * 
		 * } });
		 */
        AppController.getInstance().addToRequestQueue(bb);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.selectmissing, menu);
        return true;
    }

    // private Runnable updateTimerThread = new Runnable() {
    //
    //
    //
    // public void run() {
    //
    //
    //
    // timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
    //
    //
    //
    // updatedTime = timeSwapBuff + timeInMilliseconds;
    //
    // ed.putLong("times", updatedTime).commit();
    //
    // int secs = (int) (updatedTime / 1000);
    //
    // int mins = secs / 60;
    //
    // secs = secs % 60;
    //
    // int milliseconds = (int) (updatedTime % 1000);
    //
    // // timerValue.setText("" + mins + ":"
    // // + String.format("%02d", secs) + ":"
    // //
    // // + String.format("%03d", milliseconds));
    // // timerValue.setText("Time Clock " + mins + ":"
    // // + String.format("%02d", secs));// + ":"+ String.format("%03d",
    // milliseconds));
    // //Toast.makeText(getApplicationContext(), "select", 100).show();
    // Log.e("select", "select");
    // customHandler.postDelayed(this, 1000);
    //
    // }
    //
    //
    //
    // };

    public void stopwork1() {
       // stopService(new Intent(Selectmissing.this, Timerclass.class));
        // ed.putLong("times", 0).commit();
        // customHandler.removeCallbacks(updateTimerThread);
        // customHandler.removeCallbacksAndMessages(null);
        // ed.putLong("times", 0).commit();
        // try {
        // Thread.sleep(1000);
        // //ed.remove("times").commit();
        // ed.putLong("times", 0).commit();
        // // customHandler.removeCallbacks(updateTimerThread);
        // finish();
        // //System.exit(0);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // ed.remove("times").commit();
        // customHandler.removeCallbacks(updateTimerThread);
        // finish();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        // customHandler.removeCallbacks(updateTimerThread);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // customHandler.removeCallbacks(updateTimerThread);
        super.onBackPressed();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Toast.makeText(Subtaskpage.this, "sub task added", 5000).show();
            // adapter1.notifyDataSetChanged();
            String taskida = intent.getStringExtra("value");
            String type = intent.getStringExtra("type");// /type
            timerValue.setText(taskida);
            // if(type.equalsIgnoreCase("subtask")){
            // if(taskida.equals(Taskid))
            // getlistsubtaskdata ();//
            // }
            // else if(type.equalsIgnoreCase("note")){
            // if(taskida.equals(Taskid))
            // getnotelistdata();
            // }
            // else{
            //
            // }

        }
    };

    public void callifworkwithnonbillable(int x) {
        try {
            // if (x == 7) {
            // Toast.makeText(Selectmissing.this,
            // "Please scan workstation where you work",
            // Toast.LENGTH_LONG).show();
            // }
            // if (x == 8) {
            // Toast.makeText(Selectmissing.this, "Please scan Labour-code",
            // Toast.LENGTH_LONG).show();
            // }
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, x);

        } catch (Exception e) {

            Uri marketUri = Uri
                    .parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 5) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }

            if (requestCode == 7) {
                // Toast.makeText(Selectmissing.this, "Successfully done",
                // Toast.LENGTH_LONG).show();

             //   Boolean bb = sp.getBoolean("billable", false);

                Boolean bb =   Shared_Preference.getJOB_TYPE_IS_BILLABLE(this);
                if (bb) {
                    // callifworkwithnonbillable();
                    // dialogforshowingbillornonbill();
                    // callifworkwithnonbillable(8);
                    alertforlabourcode();
                } else {
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    // set the new task and clear flags
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
            if (requestCode == 8) {
                // Toast.makeText(Selectmissing.this, "Successfully done",
                // Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }
    }

    public void alertforsurework() {
        AlertDialog.Builder dial = new AlertDialog.Builder(Selectmissing.this);
        dial.setCancelable(false);
        dial.setTitle("Select");
        dial.setMessage("Are you working on your old workstation ?");
        dial.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // LocateCrates.this.finish();
                // callifworkwithnonbillable(8);
              //  Boolean bb = sp.getBoolean("billable", false);
                Boolean bb =   Shared_Preference.getJOB_TYPE_IS_BILLABLE(Selectmissing.this);

                if (bb) {
                    // callifworkwithnonbillable();
                    // dialogforshowingbillornonbill();
                    // callifworkwithnonbillable(8);
                    alertforlabourcode();
                } else {
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    // set the new task and clear flags
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }

            }
        });
        dial.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // callifworkwithnonbillable();
                alertforworkstation();
                // callifworkwithnonbillable(7);

            }
        });
        AlertDialog dd = dial.create();
        dd.show();
    }

    public void alertforlabourcode() {
        AlertDialog.Builder dial = new AlertDialog.Builder(Selectmissing.this);
        dial.setCancelable(false);
        dial.setTitle("Select");
        dial.setMessage("You need to  scan your Labour code");
        dial.setNeutralButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // LocateCrates.this.finish();
                // callifworkwithnonbillable(8);
               // Boolean bb = sp.getBoolean("billable", false);
                Boolean bb =   Shared_Preference.getJOB_TYPE_IS_BILLABLE(Selectmissing.this);

                if (bb) {
                    // callifworkwithnonbillable();
                    // dialogforshowingbillornonbill();
                    callifworkwithnonbillable(8);
                } else {
                    Selectmissing.this.finish();
                }

            }
        });

        AlertDialog dd = dial.create();
        dd.show();
    }

    public void alertforworkstation() {
        AlertDialog.Builder dial = new AlertDialog.Builder(Selectmissing.this);
        dial.setCancelable(false);
        dial.setTitle("Select");
        dial.setMessage("You need to scan workstation where you work now");
        dial.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // LocateCrates.this.finish();
                // callifworkwithnonbillable(8);
                // Boolean bb = sp.getBoolean("billable", false);
                // if (bb) {
                // // callifworkwithnonbillable();
                // // dialogforshowingbillornonbill();
                // callifworkwithnonbillable(8);
                // } else {
                // Selectmissing.this.finish();
                // }
                callifworkwithnonbillable(7);

            }
        });

        AlertDialog dd = dial.create();
        dd.show();
    }

    public void Deletecategory() {
        String receivedString = "";
        int check_gotocatch = 0;
        try {
            if (allmissingg.endsWith(",")) {
                allmissingg = allmissingg.substring(0, allmissingg.length() - 1);
            }
        } catch (Exception e) {

        }
        final String NAMESPACE =KEY_NAMESPACE+"";
        final String URL = urlofwebservice;
        final String SOAP_ACTION =KEY_NAMESPACE+API_missing_crate;
        final String METHOD_NAME =API_missing_crate;
        // Create SOAP request
       // String uname = sp.getString("jobid", "");
        String uname =   Shared_Preference.getSWO_ID(this);

       // String upass = sp.getString("clientid", "");
        String upass= Shared_Preference.getLOGIN_USER_ID(Selectmissing.this);
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("swo_id", uname);
        request.addProperty("doneby", upass);
        request.addProperty("region", allmissingg);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);


        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j); //if complex type is present then you can cast this to SoapObject and if primitive type is returned you can use toString() to get actuall value.
            }
            receivedString = ks.toString();
            //Toast.makeText(getBaseContext(), receivedString, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
//		resultmy=3;
//	//Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
//	check_gotocatch++;
            e.printStackTrace();
        }
        if (check_gotocatch == 0) {
            try {
                String Jsonstring = receivedString;
                //Toast.makeText(getApplicationContext(),
//			Jsonstring, Toast.LENGTH_LONG).show();
                String news = Jsonstring.substring(Jsonstring.indexOf("["));
                String n1 = news;
                JSONArray jArray = new JSONArray(n1);
                int len = jArray.length();
                for (int k = 0; k < (jArray.length()); k++) {
                    String name;
                    String mob;
                    String id;
                    JSONObject json_obj = jArray.getJSONObject(k);
                    String resuSt = json_obj.getString("result");
                    if (resuSt.equals("0") || resuSt.equalsIgnoreCase("0") || resuSt == "0") {

                    } else if (resuSt.equals("1") || resuSt.equalsIgnoreCase("1") || resuSt == "1") {

                    } else {

                    }


                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

    public class asyntask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showprogressdialog();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            hideprogressdialog();
            //Toast.makeText(getApplicationContext(), "good",5000).show();
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Deletecategory();
            return null;
        }

    }
}
