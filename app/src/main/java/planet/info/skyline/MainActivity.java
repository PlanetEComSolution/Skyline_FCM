package planet.info.skyline;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import planet.info.skyline.adapter.CompanyNameAdapter;
import planet.info.skyline.adapter.JobNameAdapter;
import planet.info.skyline.adapter.SavedTimeSheet_ListAdapter;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.floating_view.ChatHeadService;
import planet.info.skyline.model.Awo;
import planet.info.skyline.model.MySwo;
import planet.info.skyline.model.SavedTask;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.util.Utility.KEY_NAMESPACE;
import static planet.info.skyline.util.Utility.URL_EP1;
import static planet.info.skyline.util.Utility.URL_EP2;


public class MainActivity extends BaseActivity {
    private static final int CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private static final int WIFI_SETTING_REQUEST_CODE = 458;
    private static final int MOBILE_DATA_SETTING_REQUEST_CODE = 459;

    static MainActivity activityMain;
    String result;
    SharedPreferences sp;
    int count;
    Editor ed;
    int pankajtester_chutya;
    Dialog dial, swo_dialog;
    String nextact = "";//, webhit = "";
    String sdd = "1452,1545";// http://exhibitpower.com/crate_web_service.php?id=
    // String clientidme;
    String job = "", comp_id_name = "";
    // HashMap<String,String> company_id_list=new  HashMap<String,String>();
    ArrayList<String> swo_arrayList = new ArrayList<>(),
            comp_id_list = new ArrayList<>(),
            jobid_by_aman = new ArrayList<>(), swo_status = new ArrayList<>();
    ProgressDialog pDialog;
    int aman_geneious = 0;
    String comapny_id1;
    String company_name1;
    String job_descripition1;
    String status1;
    String show1;
    String jobtype1;
    List<String> company_id_list = new ArrayList<String>();
    List<String> company_Name_list = new ArrayList<String>();
    List<String> company_Logo_list = new ArrayList<String>();
    List<String> company_id_list_forIndex;
    List<String> company_Name_list_forIndex;
    List<String> job_Name_list_Desc_forIndex;
    CompanyNameAdapter companyNameAdapter;
    List<String> job_id_list = new ArrayList<String>();
    List<String> job_Name_list = new ArrayList<String>();
    List<String> job_des_list = new ArrayList<String>();
    List<String> status_list = new ArrayList<String>();
    List<String> show_list = new ArrayList<String>();
    List<String> jobtype_list = new ArrayList<String>();
    String JOB_ID_forNonBillable;
    String COMP_ID_forNonBillable;

    String main_jobid = "";
    AutoCompleteTextView job_name, company_name;
    String new_job_id, new_des;
    String main_status;////EDIT IN THIS
    String new_jobtype;
    String new_show;
    int my_logic_of = 12;
    boolean doubleBackToExitPressedOnce = false;
    boolean Show_Job_File_New = false;
    List<String> job_Name_list_Desc = new ArrayList<String>();
    LinearLayout ll_billable_row;
    LinearLayout ll_billable_row_new;
    boolean search_byJob = false;
    ArrayList<String> pause_arrayList, cl_id_list_paused, jobid_paused, jobid_pausedNew, jobName_paused, jobDesc_paused, compName_paused, swoStatus_paused, PausedTimesheetID;
    Dialog dialog_companyName, dialog_ScanSWO;
    AutoCompleteTextView et_JobName;
    ArrayList<HashMap<String, String>> list_AllJobs = new ArrayList<>(), list_AllJobs_forIndex = new ArrayList<>();
    String compName;
    //GetPausedJobs async_GetPausedJobs;
    boolean taskStartedforPauseList = false;
    String urlofwebservice11_new = URL_EP2 + "/WebService/techlogin_service.asmx?";
    String urlofwebservice = URL_EP2 + "/WebService/techlogin_service.asmx/bind_code";
    WifiManager wifiManager;
    // NetworkChangeReceiver broadCastReceiver = new NetworkChangeReceiver();
    AlertDialog alertDialog;
    String userRole = "";
    ArrayList<Awo> list_Awo = new ArrayList<>();
    Dialog showd;
    String SelectedAwo = "-1";

    String JOB_ID = "";

    ArrayList<MySwo> mySwoArrayList = new ArrayList<>();
    boolean IsMySwo = false;
    AlertDialog alertDialog_Paused;
    private long lastClickTime = 0;


    private String CLOCK_IN_BILLABLE_CODE = "";
    private String CLOCK_OUT_BILLABLE_CODE = "";
    private String IN_PROGRESS_SWO_STATUS = "";


    public static MainActivity getInstance() {
        return activityMain;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billablenonbillable_new);
        activityMain = this;
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage(getString(R.string.Loading_text));
        pDialog.setCancelable(false);
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
        ed = sp.edit();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        userRole = sp.getString(Utility.LOGIN_USER_ROLE, "");

        INITIALIZE_VIEWS();

        if (Utility.isAppLaunchedFirstTime(MainActivity.this)) {
            Utility.setAppNotLaunchedFirstTime(MainActivity.this);
            checkWifi();
        }


        if (Utility.isAppUpdated(MainActivity.this)) {
            String currentVersion = Utility.getAppVersion(MainActivity.this);
            String oldVersion = Utility.getOldVersion(MainActivity.this);
            String text = "App updated successfully from V " + oldVersion + " to V " + currentVersion + "";
            dialog_App_Updated(text, true);
        }


        try {
            //     ShortcutBadger.removeCount(MainActivity.this); //for 1.1.4+
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

          /* Init();
        checkWifi();*/
   /*Start chat head here again because if running clock will lost anywhere
    then when user will land  on this page by opening app  ,the clock should appear.*/
        Utility.showChatHead(getApplicationContext());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                result = contents;

                Log.d("BHANU", "" + contents + " " + result);
                String Swo_Id = "";
                String clientid = "";
                //     String dealerid = "";

                if (result.contains(",")) {
                    try {
                        String[] jj = result.split(",");// jobid,clientid
                        Swo_Id = jj[0];
                        clientid = jj[1];
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "SWO error!", Toast.LENGTH_SHORT).show();
                    }

                    if (clientid.equalsIgnoreCase("")) {
                        dialog_SearchByJob();
                        Toast.makeText(getApplicationContext(), "Please scan valid QR Code of SWO!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    nextact = URL_EP1 + "/crate_web_service.php?id="
                            + clientid;

                    ed.putString("link", nextact);
                    ed.putString("jobid", Swo_Id);
                    ed.putString(Utility.COMPANY_ID_BILLABLE, clientid);
                    ed.commit();
                    getCompanyInfo(nextact);
                    check(Swo_Id);

                } else {
                    Toast.makeText(getApplicationContext(), "Please scan valid QR Code of SWO!", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
            if (resultCode == RESULT_CANCELED) {
                // handle cancel
                //finish();
            }
        }
        //nks
        else if (requestCode == Utility.OVERLAY_PERMISSION_REQUEST_CODE) {
            {
            }
        } else if (requestCode == WIFI_SETTING_REQUEST_CODE) {
            // checkWifi();
            if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                new checkVersionUpdate().execute();
            } else {
                dialog_TurnOnMobileData();
            }

        } else if (requestCode == MOBILE_DATA_SETTING_REQUEST_CODE) {
            if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                new checkVersionUpdate().execute();
            } else {
                dialog_MobileDataNotTrunedON();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void showprogressdialog() {
        try {
            if (!(pDialog.isShowing())) {
                pDialog.show();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void hideprogressdialog() {
        try {
            if ((pDialog.isShowing())) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void dialog_SearchByJob()    /////by aman kaushik
    {
        dialog_ScanSWO = new Dialog(MainActivity.this);
        dialog_ScanSWO.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_ScanSWO.setContentView(R.layout.diloge_for_billable_only_new);
        dialog_ScanSWO.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_ScanSWO.setCancelable(true);
        ImageView closebtn = (ImageView) dialog_ScanSWO.findViewById(R.id.close);
        Button scanswo = (Button) dialog_ScanSWO.findViewById(R.id.scan);
        TextView or = (TextView) dialog_ScanSWO.findViewById(R.id.textView);

        /*if (userRole.equals(Utility.USER_ROLE_APC) ||
                userRole.equals(Utility.USER_ROLE_ARTIST)) {
            scanswo.setVisibility(View.GONE);
            or.setVisibility(View.GONE);
        } else {
            scanswo.setVisibility(View.VISIBLE);
            or.setVisibility(View.VISIBLE);
        }*/


        et_JobName = (AutoCompleteTextView) dialog_ScanSWO.findViewById(R.id.jobtext);
        Button btn_GO = (Button) dialog_ScanSWO.findViewById(R.id.Btn_Yes);

////new work
        et_JobName.setText("");
        JobNameAdapter jobDescAdapter = new JobNameAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list_AllJobs);
        et_JobName.setAdapter(jobDescAdapter);
        et_JobName.setDropDownHeight(550);

        et_JobName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_JobName.showDropDown();
                /*to clear autocomplete*/
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (et_JobName.getRight() - et_JobName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            et_JobName.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
                /**/

                return false;
            }
        });


        et_JobName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (userRole.equals(Utility.USER_ROLE_APC) ||
                        userRole.equals(Utility.USER_ROLE_ARTIST)) {


                } else {
                    dialog_ScanSWO.hide();
                }


                search_byJob = true;
                ///
                String job_txt = et_JobName.getText().toString();
                job_txt = job_txt.substring(0, job_txt.indexOf("\n"));
                et_JobName.setText(job_txt);

/*
                if (userRole.equals(Utility.USER_ROLE_APC) ||
                        userRole.equals(Utility.USER_ROLE_ARTIST)) {
                } else {
                    new CheckSwo().execute(job_txt);
                }*/
                new CheckSwo().execute(job_txt);
            }
        });

        et_JobName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_JobName.getText().length() == 0) {

                    et_JobName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    //refresh adapter
                    list_AllJobs = new ArrayList<>(list_AllJobs_forIndex);
                    JobNameAdapter jobDescAdapter = new JobNameAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list_AllJobs);
                    et_JobName.setAdapter(jobDescAdapter);
                    et_JobName.setDropDownHeight(550);

                } else {
                    et_JobName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                }
            }
        });


        ///// new work


        closebtn.setOnClickListener(

                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog_ScanSWO.dismiss();
                        } catch (Exception e) {
                            e.getMessage();
                        }


                    }

                });
        scanswo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                scanqr();
                try {
                    dialog_ScanSWO.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });

        btn_GO.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                job = et_JobName.getText().toString();
                ////

                boolean isCorrectJob = false;

                if (job.length() == 0) {
                    Toast.makeText(MainActivity.this, "Please select job!", Toast.LENGTH_SHORT).show();

                } else {


                    for (int j = 0; j < list_AllJobs_forIndex.size(); j++) {
                        HashMap<String, String> hashMap = list_AllJobs_forIndex.get(j);
                        String jobName = hashMap.get("jobName");
                        jobName = jobName.substring(0, jobName.indexOf("\n"));
                        if (jobName.equals(job)) {
                            comp_id_name = hashMap.get("compID");
                            String _JobId = hashMap.get("JobId");
                            ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, _JobId).commit();
                            Log.e("comp_id--->", comp_id_name);
                            Log.e("job_id---->", _JobId);
                            isCorrectJob = true;
                            break;
                        }
                    }

                    if (isCorrectJob) {
                        dialog_ScanSWO.hide();
                        if (comp_id_list != null && comp_id_list.size() > 0) {
                            ed.putString(Utility.COMPANY_ID_BILLABLE, comp_id_list.get(0)).commit();
                        }

                        if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                            new get_company_Area().execute(job);
                        } else {
                            Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Please enter correct job!", Toast.LENGTH_SHORT).show();
                    }


//nks
                }


            }
        });

        try {
            dialog_ScanSWO.show();
        } catch (Exception e) {
            e.getMessage();
        }


        ed.putBoolean("billable", false).commit();
        //dial.dismiss();


    }

    public void scanqr() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage(getApplicationContext().getPackageName());
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 1);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
    }

    /**
     * Bhanu Method for Job
     **/
    public void GetSwoByJob(String job) {
        swo_arrayList.clear();
        comp_id_list.clear();
        jobid_by_aman.clear();
        swo_status.clear();
        //  count = 0;
        Log.e("BHANU", job);
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
      /*  final String SOAP_ACTION = KEY_NAMESPACE+"GetSwoByJOb";
        final String METHOD_NAME = "GetSwoByJOb";*/
        final String SOAP_ACTION = KEY_NAMESPACE + "GetSwoByJObDealer";
        final String METHOD_NAME = "GetSwoByJObDealer";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //   request.addProperty("job", job);
        String dealerId = sp.getString(Utility.DEALER_ID, "");
        request.addProperty("job", job);
        request.addProperty("DealerID", dealerId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();


            JSONObject jsonObject = new JSONObject(resultstring);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String swo_id = jsonObject1.getString("swo_id");
                JOB_ID = jsonObject1.getString("JOB_ID");

                String JOB_DESC = jsonObject1.getString("JOB_DESC");
                String COMP_ID = jsonObject1.getString("COMP_ID");
                String SWO_NAME = jsonObject1.getString("swo_name");
                String SWO_Status_new = jsonObject1.getString("SWO_Status_new");
                //  String TXT_JOB = jsonObject1.getString("txt_Job");
                //    Log.d("BHANU", JOB_ID + " " + JOB_DESC + " " + COMP_ID + " " + SWO_NAME + " " + TXT_JOB);
                swo_arrayList.add(SWO_NAME);
                comp_id_list.add(COMP_ID);
                jobid_by_aman.add(swo_id);
                swo_status.add(SWO_Status_new);

            }

            //  }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void showChatHead_Billable(Context context, boolean isShowOverlayPermission) {
        // API22以下かチェック
       /* Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ");
        String time1 = sdf.format(dt);
        ed.putString("starttimenew", time1).commit();*/

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startService(new Intent(context, ChatHeadService.class));
            return;
        }
        // 他のアプリの上に表示できるかチェック
        if (Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(context, ChatHeadService.class);
            context.startService(intent);
            Set_Billable_Row();
        } else {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE);
        }

        // オーバレイパーミッションの表示
      /*  if (isShowOverlayPermission) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE);
            return;
        }*/


    }

    @SuppressLint("NewApi")
    private void showChatHead_Admin(Context context, boolean isShowOverlayPermission) {
        // API22以下かチェック
      /*  Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ");
        String time1 = sdf.format(dt);
        ed.putString("starttimenew", time1).commit();
   */
        ed.putBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, true).commit();//nks

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startService(new Intent(context, ChatHeadService.class));
            return;
        }
        // 他のアプリの上に表示できるかチェック
        if (Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(context, ChatHeadService.class);
            context.startService(intent);
            return;
        }

        // オーバレイパーミッションの表示
        if (isShowOverlayPermission) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE);
        }


    }

    public void dialog_SearchByCompany()    /////by aman kaushik
    {
        pankajtester_chutya = 0;
        dialog_companyName = new Dialog(MainActivity.this);
        dialog_companyName.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_companyName.setContentView(R.layout.test1_new);
        dialog_companyName.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_companyName.setCancelable(true);
        ImageView closebtn = (ImageView) dialog_companyName.findViewById(R.id.close);
        company_name = (AutoCompleteTextView) dialog_companyName.findViewById(R.id.company);
        job_name = (AutoCompleteTextView) dialog_companyName.findViewById(R.id.job);

        Button btn_GO = (Button) dialog_companyName.findViewById(R.id.go_button);
        Button scann_swo = (Button) dialog_companyName.findViewById(R.id.Scann);


        TextView or = (TextView) dialog_companyName.findViewById(R.id.or);
        dialog_companyName.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (userRole.equals(Utility.USER_ROLE_APC) ||
                userRole.equals(Utility.USER_ROLE_ARTIST)) {   // apc/artist

        } else {   // tech
            if (my_logic_of == 24) {//job file
                scann_swo.setVisibility(View.VISIBLE);
                or.setVisibility(View.VISIBLE);
            } else {
                scann_swo.setVisibility(View.GONE);
                or.setVisibility(View.GONE);
            }
        }


        //    scann_swo.setVisibility(View.VISIBLE);
        //    or.setVisibility(View.VISIBLE);


       /* if (userRole.equals(Utility.USER_ROLE_APC) ||
                userRole.equals(Utility.USER_ROLE_ARTIST)) {
            scann_swo.setVisibility(View.GONE);
            or.setVisibility(View.GONE);
        } else {
            scann_swo.setVisibility(View.VISIBLE);
            or.setVisibility(View.VISIBLE);
        }*/

        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, company_Name_list);
        //company_name.setAdapter(dataAdapter);
        companyNameAdapter = new CompanyNameAdapter(MainActivity.this, android.R.layout.simple_list_item_1, company_Name_list);
        company_name.setAdapter(companyNameAdapter);
        company_name.setDropDownHeight(550);
// ontouch for company name-->
        company_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                company_name.showDropDown();
                /*to clear autocomplete*/
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (company_name.getRight() - company_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            company_name.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
                /**/

                return false;
            }
        });

        //ontouch for job name---->
        job_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                job_name.showDropDown();
                /*to clear autocomplete*/
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (job_name.getRight() - job_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            job_name.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
                /**/

                return false;
            }
        });

        scann_swo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog_companyName.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                scanqr();
            }
        });

        company_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (company_name.getText().equals("--Select--") || (company_name.getText().length() == 0)) {

                } else {
                    pankajtester_chutya = 1;
                    //   String checc = company_name.getText().toString();
                    int index = company_Name_list_forIndex.indexOf(company_name.getText().toString());
                    comp_id_name = company_id_list_forIndex.get(index);


                    if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                        new get_company_job_id().execute(comp_id_name);
                    } else {
                        Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }


                }
            }
        });


        job_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                aman_geneious = 12;
                // int index = job_Name_list.indexOf(job_name.getText().toString());
                // int index = i;
                ////
                String job_txt = job_name.getText().toString();
                job_txt = job_txt.substring(0, job_txt.indexOf("\n"));

                int index = job_Name_list.indexOf(job_txt);
                ///
                main_jobid = job_Name_list.get(index);////EDIT IN THIS
                new_job_id = job_id_list.get(index);
                ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, new_job_id).apply();
                new_des = job_des_list.get(index);
                main_status = status_list.get(index);////EDIT I
///  THIS
                new_jobtype = jobtype_list.get(index);
                new_show = show_list.get(index);
                job_name.setText("");
                job_name.setText(main_jobid);
                if (my_logic_of != 24) {//check for for billable only

                    dialog_companyName.hide();
                    new CheckSwo().execute(main_jobid);
                }

            }
        });
        company_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (company_name.getText().length() == 0) {
                    job_name.setAdapter(null);
                    job_name.setText("");
                    company_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                    company_Name_list = new ArrayList<>(company_Name_list_forIndex);
                    companyNameAdapter = new CompanyNameAdapter(MainActivity.this, android.R.layout.simple_list_item_1, company_Name_list);
                    company_name.setAdapter(companyNameAdapter);
                    company_name.setDropDownHeight(550);

                } else {
                    company_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                    if (company_Name_list.size() == 0) {
                        company_name.setAdapter(null);
                    }

                }
            }
        });
        job_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (job_name.getText().length() == 0) {
                        job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                        if (company_name.getText().toString().length() == 0) {
                            job_name.setAdapter(null);
                        } else {
                            //refresh adapter
                            job_Name_list_Desc = new ArrayList<>(job_Name_list_Desc_forIndex);
                            CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(MainActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
                            job_name.setAdapter(jobDescAdapter);
                            job_name.setDropDownHeight(550);

                        }


                    } else {
                        job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }


        });
        closebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog_companyName.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });

        btn_GO.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                compName = company_name.getText().toString().trim();

                ed.putString(Utility.JOB_ID_BILLABLE, main_jobid).commit();
                ed.putString(Utility.COMPANY_ID_BILLABLE, comp_id_name).commit();

                /**/
                if (compName.equals("")) {
                    Toast.makeText(MainActivity.this, "Please select Company!", Toast.LENGTH_SHORT).show();
                } else if (!company_Name_list.contains(compName)) {
                    Toast.makeText(MainActivity.this, "Please enter correct Company Name!", Toast.LENGTH_SHORT).show();
                } else if (job_name.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Please select Job !", Toast.LENGTH_SHORT).show();
                } else if (!job_Name_list.contains(job_name.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "Please enter correct Job !", Toast.LENGTH_SHORT).show();
                } else {


                    try {
                        dialog_companyName.hide();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                  /*  if (aman_geneious == 12) {
                        Dialog_ShowJobInfo_1();
                    }
                    else {*/
                    if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                        new get_company_Area().execute(job_name.getText().toString());
                    } else {
                        Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    }

                    //}
                }

                /**/


           /*     if (company_name.getText().toString().trim().equalsIgnoreCase("")) {
                    if (job_name.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(MainActivity.this, "Please enter Job Id!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {

                            try {
                                dialog_companyName.dismiss();
                            } catch (Exception e) {
                                e.getMessage();
                            }


                            new get_company_Area().execute(job_name.getText().toString().trim());
                        } else {
                            Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (pankajtester_chutya == 1) {
                        compName = company_name.getText().toString();
                        if (company_name.getText().toString().equals("--Select--")) {
                            Toast.makeText(MainActivity.this, "Please select Company!", Toast.LENGTH_SHORT).show();
                        } else {
                         *//*   if (count == 1) {
                                Toast.makeText(MainActivity.this, "Please select another Company!", Toast.LENGTH_SHORT).show();
                            } else {*//*
                            if (job_name.getText().toString().equals("--Select--") || job_name.getText().toString().trim().equals("")) {
                                Toast.makeText(MainActivity.this, "Please select job Id!", Toast.LENGTH_SHORT).show();
                            } else {

                                if (aman_geneious == 12) {

                                    //nks
                                    String companyName = company_name.getText().toString().trim();
                                    String jobName = job_name.getText().toString().trim();
                                    if (!company_Name_list.contains(companyName)) {
                                        Toast.makeText(MainActivity.this, "Please enter correct Company Name!", Toast.LENGTH_SHORT).show();
                                    } else if (!job_Name_list.contains(jobName)) {
                                        Toast.makeText(MainActivity.this, "Please enter correct Job Id!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //nks
                                        // choose_for_scan.dismiss();

                                        try {
                                            dialog_companyName.hide();
                                        } catch (Exception e) {
                                            e.getMessage();
                                        }
                                        Dialog_ShowJobInfo_1();


                                    }


                                } else {
                                    if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                                        new get_company_Area().execute(job_name.getText().toString());
                                    } else {
                                        Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }
                            //   }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please select valid Company name!", Toast.LENGTH_SHORT).show();
                    }


                }*/
            }
        });

        try {
            dialog_companyName.show();
        } catch (Exception e) {
            e.getMessage();
        }


        ed.putBoolean("billable", false).commit();
        //dial.dismiss();
    }

    //for comapany name--->


    private void Dialog_ShowJobInfo_from_SWO(JSONObject js_obj) {


        try {
            String job_id = js_obj.getString("job_id");
            final String jobName = js_obj.getString("jobName");
            String JOB_TYPE = js_obj.getString("JOB_TYPE");
            String jobstatus = js_obj.getString("jobstatus");
            String company = js_obj.getString("company");
            String Show_Name = js_obj.getString("Show_Name");
            String desciption = js_obj.getString("desciption");
            String SWO_Status_new = js_obj.getString("SWO_Status_new");
            String dealerID = js_obj.getString("dealerID");

            ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, job_id).apply();
            ed.putString(Utility.JOB_ID_BILLABLE, jobName).apply();
            ed.putString("name", company).apply();
            //  ed.putString(Utility.KEY_SELECTED_SWO_STATUS, SWO_Status_new).apply();


            final Dialog dialog_jobInfo = new Dialog(MainActivity.this);
            dialog_jobInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_jobInfo.setContentView(R.layout.test2_new);
            dialog_jobInfo.setCancelable(false);

            final TextView tv_Company = (TextView) dialog_jobInfo.findViewById(R.id.tv_company);
            final TextView t1 = (TextView) dialog_jobInfo.findViewById(R.id.t1);
            final TextView t2 = (TextView) dialog_jobInfo.findViewById(R.id.t2);
            final TextView t3 = (TextView) dialog_jobInfo.findViewById(R.id.t3);
            final TextView t4 = (TextView) dialog_jobInfo.findViewById(R.id.t4);
            final TextView t5 = (TextView) dialog_jobInfo.findViewById(R.id.t5);
            final Button cancle = (Button) dialog_jobInfo.findViewById(R.id.cancle);
            final Button btn_GO = (Button) dialog_jobInfo.findViewById(R.id.proced);


            tv_Company.setText((company));
            t1.setText(jobName);
            t2.setText(JOB_TYPE);
            t3.setText(jobstatus);
            t4.setText(Show_Name);
            t5.setText(desciption);


            btn_GO.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  dialog_companyName.dismiss();



                    try {
                        dialog_jobInfo.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    if (my_logic_of == 24) {//this for if we had scanned SWO for JOB Files
                        Intent in = new Intent(MainActivity.this, Show_Jobs_Activity_New.class);
                        startActivity(in);
                    } else if (jobName.equalsIgnoreCase(Utility.CLOCK_IN) || jobName.equalsIgnoreCase(Utility.CLOCK_OUT)) {
                        if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                            new Async_Submit_Billable_Timesheet_New().execute();
                        } else {
                            Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        startClockForBillable();//this for if we had scanned SWO for billable job start
                    }


                }
            });

            cancle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        dialog_jobInfo.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
            });

            dialog_jobInfo.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void Getcompany_name() {
        String dealerId = sp.getString(Utility.DEALER_ID, "");

        //  count = 0;
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + "bindClientByDealer";
        final String METHOD_NAME = "bindClientByDealer";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("dealerID", dealerId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();
            if (recved.contains("Job not found!")) {
                //     count = 1;
            } else {

                String[] aa = recved.split("=");
                String a = aa[1];
                String b[] = a.split(";");
                String k = b[0];
                Log.d("BHANU", k);
                Log.d("punnu_chutiya", k);
                JSONObject jsonObject = new JSONObject(k);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                company_id_list.clear();
                company_Name_list.clear();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String comapny_id = jsonObject1.getString("id");
                    String company_name = jsonObject1.getString("Ename");
                    String company_logo = jsonObject1.getString("Imagepath");
                    //     company_id_list.put(company_name,comapny_id);

                    company_id_list.add(comapny_id);
                    company_Name_list.add(company_name);
                    company_Logo_list.add(company_logo);
                }
                //  company_id_list.add(0, "--Select--");
                //  company_Name_list.add(0,"--Select--");

                company_id_list_forIndex = new ArrayList<>(company_id_list);
                company_Name_list_forIndex = new ArrayList<>(company_Name_list);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Getcompany_job_id(String id)   ///by aman kaushik
    {
        //  count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + "BindJob";
        final String METHOD_NAME = "BindJob";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("ClientID", id);
        Log.d("BHANU--ID", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();
            if (recved.contains("No Data Available.")) {
                //      count = 1;
            } else {
/*
                String[] aa = recved.split("=");
                String a = aa[1];
                String b[] = a.split(";");
                String k = b[0];*/
                String json = recved.substring(recved.indexOf("=") + 1, recved.lastIndexOf(";"));

               /* Log.d("BHANU", k);
                Log.d("punnu_chutiya", k);*/

                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                job_id_list.clear();
                job_Name_list.clear();
                job_des_list.clear();
                status_list.clear();
                show_list.clear();
                jobtype_list.clear();
                job_Name_list_Desc.clear();//nks

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String comapny_id = jsonObject1.getString("JOB_ID_PK");
                    String company_name = jsonObject1.getString("JobName");
                    String job_descripition = jsonObject1.getString("txt_Job");


                    String status = jsonObject1.getString("Status");
                    String show = jsonObject1.getString("ShowName");
                    String jobtype = jsonObject1.getString("JOB_TYPE");

////
                    String desc = job_descripition.trim();
                    char space = ' ';
                    int index = 0;
                    if (desc.length() > 30) {
                        for (int j = 30; j < desc.length(); j++) {
                            if (desc.charAt(j) == space) {
                                // String s=desc.substring(j);
                                index = j;
                                break;
                            }
                        }
                    }
                    String total_desc = "";
                    if (index != 0) {
                        desc = desc.substring(0, index) + System.getProperty("line.separator") + (desc.substring(index)).trim();
                    }
                    total_desc = company_name + System.getProperty("line.separator") + desc.trim();
                    /////

                    job_id_list.add(comapny_id);
                    job_Name_list.add(company_name);
                    job_Name_list_Desc.add(total_desc);
                    job_des_list.add(job_descripition);
                    status_list.add(status);
                    show_list.add(show);
                    jobtype_list.add(jobtype);
                }

                job_Name_list_Desc_forIndex = new ArrayList<>(job_Name_list_Desc);
                //   job_id_list.add(0, "--Select--");
                //   job_Name_list.add(0, "--Select--");
                //   job_des_list.add(0,"--Select--");

                //    status_list.add(0,"--Select--");
                //    show_list.add(0,"--Select--");
                //   jobtype_list.add(0,"--Select--");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*  public void Send()   ///by aman kaushik
      {
          try {
              JsonObjectRequest updatess = new JsonObjectRequest(Method.GET, URL_EP1 + "/add_tempary_crate.php",
                      null, new Response.Listener<JSONObject>() {

                  @Override
                  public void onResponse(JSONObject jsono) {
                      try {

                      } catch (Exception e) {
                          e.printStackTrace();
                      }

                  }
              }, new Response.ErrorListener() {

                  @Override
                  public void onErrorResponse(VolleyError arg0) {
                      //
                  }
              });
              AppController.getInstance().addToRequestQueue(updatess);
          } catch (Exception e) {
              e.getMessage();
              Log.d("-nks   :", e + "");
          }
      }
  */
    public void dialog_SearchBy() {
        final Dialog choose_for_scan = new Dialog(MainActivity.this);
        choose_for_scan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        choose_for_scan.setContentView(R.layout.dialog_searchby_new);
        choose_for_scan.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        choose_for_scan.setCancelable(true);
        try {
            if (!choose_for_scan.isShowing()) {
                choose_for_scan.show();
            }
        } catch (Exception e) {
            e.getMessage();
        }

        ImageView closebtn = (ImageView) choose_for_scan.findViewById(R.id.close);
        Button Btn_SearchByJob = (Button) choose_for_scan.findViewById(R.id.Btn_SearchByJob);
        Button Btn_SearchByCompany = (Button) choose_for_scan.findViewById(R.id.Btn_SearchByCompany);
        Button scann_swo = (Button) choose_for_scan.findViewById(R.id.scan);
        Button Btn_MySwo = (Button) choose_for_scan.findViewById(R.id.Btn_MySwo);
        Button Btn_UnassignedSwo = (Button) choose_for_scan.findViewById(R.id.Btn_UnassignedSwo);


        if (userRole.equals(Utility.USER_ROLE_APC) ||
                userRole.equals(Utility.USER_ROLE_ARTIST)) {  // apc,artist
            scann_swo.setVisibility(View.GONE);
            Btn_MySwo.setVisibility(View.GONE);
            Btn_UnassignedSwo.setVisibility(View.GONE);
        } else {  //tech
            scann_swo.setVisibility(View.VISIBLE);
            Btn_MySwo.setVisibility(View.VISIBLE);
            Btn_UnassignedSwo.setVisibility(View.VISIBLE);
        }


        closebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    choose_for_scan.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                try {
                    alertDialog_Paused.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });
        Btn_SearchByJob.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    choose_for_scan.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                new getAllJObByDealer().execute();
                search_byJob = true;

            }
        });

        Btn_SearchByCompany.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    choose_for_scan.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                search_byJob = false;

                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                    new get_company_name().execute();

                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });
        scann_swo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    choose_for_scan.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                scanqr();
            }
        });
        Btn_MySwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IsMySwo = true;
                try {
                    choose_for_scan.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                    new async_getSWOList().execute();
                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });

        Btn_UnassignedSwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                IsMySwo = false;
                try {
                    choose_for_scan.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                try {
                    choose_for_scan.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                    new async_getSWOList().execute();
                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void Dialog_No_JobsAvailable() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);
        final TextView title = dialogView.findViewById(R.id.textView1rr);
        final TextView message = dialogView.findViewById(R.id.texrtdesc);

        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        // dialogBuilder.setTitle("Device Details");
        title.setText("No jobs available!");
        message.setText("Kindly choose another Company!");
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.getCause();
        }







        /*try {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No jobs available!")
                    .setContentText("Kindly choose another Company!")
                    .show();

        } catch (Exception e) {
            e.getMessage();
        }
*/


    }

    //for selct the job id
    public void get_data(String id)   ///by aman kaushik
    {
        comapny_id1 = null;
        company_name1 = null;
        job_descripition1 = null;
        status1 = null;
        show1 = null;
        jobtype1 = null;
        //  count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + "GetAllDetailbyJobtext_New";
        final String METHOD_NAME = "GetAllDetailbyJobtext_New";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = sp.getString(Utility.DEALER_ID, "");
        request.addProperty("job", id);
        request.addProperty("dealerid", dealerId);
        Log.d("BHANU--ID", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();
            if (recved.contains("No Data Available.")) {
                //      count = 1;
            } else {
                String[] aa = recved.split("=");
                String a = aa[1];
                String b[] = a.split(";");
                String k = b[0];
                Log.d("BHANU", k);
                Log.d("punnu_chutiya", k);
                JSONObject jsonObject = new JSONObject(k);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    comapny_id1 = jsonObject1.getString("JOB_ID_PK");
                    company_name1 = jsonObject1.getString("JobName");
                    job_descripition1 = jsonObject1.getString("txt_Job");
                    status1 = jsonObject1.getString("Status");
                    show1 = jsonObject1.getString("ShowName");
                    jobtype1 = jsonObject1.getString("JOB_TYPE");
                    compName = jsonObject1.getString("compname");


                }
                ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, comapny_id1).apply();
                ed.putString("name", compName).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Utility.HideRunningClock(getApplicationContext());
            return;
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit!",
                Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);


    }

    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, Utility.OVERLAY_PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    /*nks*/
    public void INITIALIZE_VIEWS() {

        String nam = sp.getString("tname", "");
        TextView techname = (TextView) findViewById(R.id.textView1rr);
        ImageView merchantname = (ImageView) findViewById(R.id.merchantname);
        ImageView packing_list = (ImageView) findViewById(R.id.show_file6);
        merchantname.setImageResource(R.drawable.exhibitlogoa);

        //nks.....
        LinearLayout ll_billableJob = (LinearLayout) findViewById(R.id.first);
        LinearLayout ll_timeClock = (LinearLayout) findViewById(R.id.first1);
        LinearLayout ll_MaterialMove = (LinearLayout) findViewById(R.id.first2);
        LinearLayout ll_Upload = (LinearLayout) findViewById(R.id.first3);
        LinearLayout ll_jobFiles = (LinearLayout) findViewById(R.id.first5);

        TextView tv_version = (TextView) findViewById(R.id.tv_ver);


        ll_billable_row = (LinearLayout) findViewById(R.id.ll_billable);
        ll_billable_row_new = (LinearLayout) findViewById(R.id.ll_billable_new);
        ll_billable_row.setVisibility(View.VISIBLE);
        ll_billable_row_new.setVisibility(View.GONE);

        LinearLayout ll_LocateCrate = (LinearLayout) findViewById(R.id.ll_LocateCrate);
        LinearLayout ll_DamageReport = (LinearLayout) findViewById(R.id.ll_DamageReport);
        LinearLayout ll_UsageCharges = (LinearLayout) findViewById(R.id.ll_UsageCharges);

        final boolean TIMER_STARTED_FROM_BILLABLE_MODULE = sp.getBoolean(Utility.TIMER_STARTED_FROM_BILLABLE_MODULE, false);
        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            ll_billable_row.setVisibility(View.GONE);
            ll_billable_row_new.setVisibility(View.VISIBLE);
        }
        final String role = sp.getString(Utility.LOGIN_USER_ROLE, "");//Role for tech==9
        final String TIMESHEET_RIGHT = sp.getString(Utility.USER_TIMESHEET_RIGHT, "");//TIMESHEET RIGHT

        ll_billableJob.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //   preventing double, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();


//                if (Utility.isTimeAutomatic(MainActivity.this)) {
                if (TIMESHEET_RIGHT.equals("1")) {
                    //   if (role.equalsIgnoreCase("9")) {
                    boolean isTimerRunningFromAdminClockModule = sp.getBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, false);
                    if (isTimerRunningFromAdminClockModule) {
                        Toast.makeText(MainActivity.this, "Clock already running for admin functions!", Toast.LENGTH_LONG).show();
                    } else {
                        ed.putBoolean(Utility.STARTING_BILLABLE_JOB, true).apply();
                        if (!taskStartedforPauseList) {
                            if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                                new async_getBillableCodes().execute();
                                //  new GetPausedJobs().execute();
                                //  taskStartedforPauseList = true;
                            } else {
                                Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                } else {   // for not auth usr
                    Toast.makeText(MainActivity.this, "You are not authorized to access Billable Job!", Toast.LENGTH_SHORT).show();
                }
//                } else {
//                    dialog_AutoDateTimeSet();
//                }


            }
        });

        /*nks*/
        ll_timeClock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*nks*/
//                if (Utility.isTimeAutomatic(MainActivity.this)) {
                if (TIMESHEET_RIGHT.equals("1")) {
                    //   if (role.equalsIgnoreCase("9")) {//tech login role==9
                    /* because we are using floating view  so we have to check for permission */
                    if (checkDrawOverlayPermission()) {
                        boolean isTimerRunningFromAdminClockModule = sp.getBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, false);
                        if (!isTimerRunningFromAdminClockModule) {
                            //   } else {
                            ed.putBoolean(Utility.STARTING_BILLABLE_JOB, false).commit();
                            //  ed.putBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, true).commit();//nks
                            if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                                new async_getNonBillableCodes().execute();
                                //  startClockForAdmin();
                            } else {
                                Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Clock already running for admin functions!", Toast.LENGTH_LONG).show();

                        }

                    }
                } else// for not auth usr
                {
                    Toast.makeText(MainActivity.this, "You are not authorized to access Time Clock for Admin Function!", Toast.LENGTH_SHORT).show();
                }

//                } else {
//                    dialog_AutoDateTimeSet();
//                }


            }
        });
        ll_MaterialMove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                    startActivity(new Intent(MainActivity.this, SlotMoveactivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });
        ll_Upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {

                    startActivity(new Intent(MainActivity.this, Upload_image_and_cooment.class));
                    ed.putBoolean(Utility.KEY_PHOTO_UPLOAD_FROM_SAME_MODULE, true).apply();
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();

                }

            }
        });
        ll_jobFiles.setVisibility(View.GONE);
        ll_jobFiles.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                my_logic_of = 24;
                Show_Job_File_New = false;

                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {

                    new get_company_name().execute();
                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();

                }


            }
        });


        LinearLayout ll_jobFiles_new = (LinearLayout) findViewById(R.id.first6);
        ll_jobFiles_new.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                my_logic_of = 24;
                Show_Job_File_New = true;

                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {

                    boolean TIMER_STARTED_FROM_BILLABLE_MODULE = sp.getBoolean(Utility.TIMER_STARTED_FROM_BILLABLE_MODULE, false);
                    if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
                        if (search_byJob) {
                            Intent in = new Intent(MainActivity.this, Show_Jobs_Activity_New.class);
                            startActivity(in);
                        } else {
                            Intent in = new Intent(MainActivity.this, Show_Jobs_Activity_New.class);
                            startActivity(in);
                        }
                    } else {
                        new get_company_name().execute();
                    }

                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();

                }

            }
        });


        LinearLayout ll_whatsInside = (LinearLayout) findViewById(R.id.ll_whatsInside);
        ll_whatsInside.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {

                    Intent in = new Intent(MainActivity.this, ShowWhatsInside_MainActivity.class);
                    startActivity(in);

                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();

                }

            }
        });

        //.....nks

        techname.setText("Welcome\n " + nam);
        packing_list.setOnClickListener(new OnClickListener() {                                                    //by aman kaushik (New Development)
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, Packing_Activity.class);
                startActivity(in);

            }
        });


        ImageView ll_logout = findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMyServiceRunning(ChatHeadService.class)) {
                    Toast.makeText(MainActivity.this, "Clock is running, Please submit it first!", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
                        dialogView.setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialogBuilder.setView(dialogView);
                        final TextView title = dialogView.findViewById(R.id.textView1rr);
                        final TextView message = dialogView.findViewById(R.id.texrtdesc);

                        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
                        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
                        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
                        close.setVisibility(View.INVISIBLE);
                        // dialogBuilder.setTitle("Device Details");
                        title.setText("Logout?");
                        message.setText("Are you sure?");
                        positiveBtn.setText("Yes");
                        negativeBtn.setText("No");
                        negativeBtn.setVisibility(View.VISIBLE);
                        positiveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                sp = getApplicationContext().getSharedPreferences("skyline", Context.MODE_PRIVATE);
                                boolean result = sp.edit().clear().commit();
                                if (result) {

                                    Utility.setLoginFalse(MainActivity.this);
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(MainActivity.this, "Unable to clear app data!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        negativeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();

                            }
                        });
                        alertDialog = dialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        try {
                            alertDialog.show();
                        } catch (Exception e) {
                            e.getCause();
                        }


                    } catch (Exception e) {
                        e.getCause();
                    }
                }

            }
        });

        ll_LocateCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                    Intent ii = new Intent(MainActivity.this, LocateCrates.class);
                    startActivity(ii);
                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();

                }
            }
        });


        ll_DamageReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TIMESHEET_RIGHT.equals("1")) {

                    if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                        Intent ii = new Intent(MainActivity.this, DamageReport.class);
                        startActivity(ii);
                    } else {
                        Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    }


                } else {   // for not auth usr
                    Toast.makeText(MainActivity.this, "You are not authorized to access Damage Report!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ll_UsageCharges.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                    new Async_Check_UsageChargeAuth().execute();
                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });
        try {
            PackageManager manager = MainActivity.this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(MainActivity.this.getPackageName(), 0);
            String versionName = info.versionName;
            tv_version.setText("v" + versionName);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void validation_dialog() {
        final Dialog showd = new Dialog(MainActivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.labourcode_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        showd.setCancelable(false);
        TextView textView1rr = (TextView) showd.findViewById(R.id.textView1rr);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView yesforloc = (TextView) showd.findViewById(R.id.Btn_Yes);

        String msg_swo = "You cannot book time to a Job without a Service Work Order.";
        String msg_awo = "You cannot book time to a Job without a Art Work Order.";

        if (userRole.equals(Utility.USER_ROLE_APC) ||
                userRole.equals(Utility.USER_ROLE_ARTIST)) {
            texrtdesc.setText(msg_awo);
        } else {
            texrtdesc.setText(msg_swo);
        }


        textView1rr.setVisibility(View.GONE);


        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!search_byJob) {
                        job_name.setText("");
                    }
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        yesforloc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!search_byJob) {


                        try {
                            dialog_companyName.show();
                        } catch (Exception e) {
                            e.getMessage();
                        }


                        job_name.setText("");
                    } else {


                        try {
                            dialog_ScanSWO.show();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        et_JobName.setText("");
                    }
                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Set_Billable_Row() {

        ed.putBoolean(Utility.TIMER_STARTED_FROM_BILLABLE_MODULE, true).commit();//nks
        ll_billable_row.setVisibility(View.GONE);
        ll_billable_row_new.setVisibility(View.VISIBLE);
    }

    /*nks*/
    public void startClockForBillable() {

        String sDate = Utility.getCurrentTimeString();
        ed.putString(Utility.TIMEGAP_JOB_START_TIME, sDate).commit();//for next job time gap
        ed.putString(Utility.KEY_PAUSED_TIMESHEET_ID, "0").commit();
        if (Utility.IsTimeGapIssue(MainActivity.this)) {
            showDialog_for_TimeGapIssue_1();
        } else {
            ed.putString(Utility.CLOCK_START_TIME, sDate).commit();
            //showChatHead_Billable(MainActivity.this, true);
            showClock();
        }
    }

    public void startClockForPaused() {
        String sDate = Utility.getCurrentTimeString();
        ed.putString(Utility.CLOCK_START_TIME, sDate).commit();
        // showChatHead_Billable(MainActivity.this, true);
        showClock();
    }

    public void startClockForAdmin() {

        String sDate = Utility.getCurrentTimeString();
        ed.putString(Utility.TIMEGAP_JOB_START_TIME, sDate).commit();//for next job time gap

        if (Utility.IsTimeGapIssue(MainActivity.this)) {
            showDialog_for_TimeGapIssue_1();
        } else {

            ed.putString(Utility.CLOCK_START_TIME, sDate).commit();
            // showChatHead_Admin(MainActivity.this, true);
            showClock();
        }


        //String sDate = Utility.getCurrentTimeString();
        //    ed.putString(Utility.CLOCK_START_TIME, sDate).commit();
        //   ed.putBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, true).commit();//nks

        // showChatHead_Admin(MainActivity.this, true);

    }

    public void showDialog_for_TimeGapIssue_1() {

        final Dialog showd = new Dialog(MainActivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.time_gap);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        ImageView close = (ImageView) showd.findViewById(R.id.close);

        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);
        final TextView Btn_Select_Another = (TextView) showd.findViewById(R.id.Btn_Select_Another);
        Btn_Done.setText("OK");
        Btn_Select_Another.setText("Select another option");

        long Total_Elapsed_Seconds = Utility.GetTimeGap(MainActivity.this);
        long totalmin = Total_Elapsed_Seconds / 60;
        long totalSec = Total_Elapsed_Seconds % 60;
        String str_totalmin;
        String str_totalSec;
        if (totalmin < 10) str_totalmin = "0" + String.valueOf(totalmin);
        else str_totalmin = String.valueOf(totalmin);

        if (totalSec < 10) str_totalSec = "0" + String.valueOf(totalSec);
        else str_totalSec = String.valueOf(totalSec);

        String diff = "00:" + str_totalmin + ":" + str_totalSec;

        tv_title.setText("");
        tv_title.setVisibility(View.GONE);


        tv_msg.setText("There is a difference of " + diff + "" +
                " in the time entry between your " +
                "last job and current job. " +
                "ExhibitPower will split the time " +
                "between your previous job and the current job.");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                //split time between prev job and current job
                long Total_Elapsed_Seconds = Utility.GetTimeGap(MainActivity.this);
                String CurrentJobStartTime = sp.getString(Utility.TIMEGAP_JOB_START_TIME, "");//dd-MM-yyyy HH:mm:ss

                int half_Elapsed_Seconds = (int) (Total_Elapsed_Seconds / 2);
                String CurrentJobStartTime_new = Utility.minus_seconds_fromCurrentTime(CurrentJobStartTime, half_Elapsed_Seconds);
                ed.putString(Utility.CLOCK_START_TIME, CurrentJobStartTime_new).commit();

                if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                    new Async_EditTimeSheetSplit().execute();

                    showClock();

                } else {
                    Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });
        Btn_Select_Another.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                showDialog_for_TimeGapIssue_2();
            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void showDialog_for_TimeGapIssue_2() {

        final Dialog showd = new Dialog(MainActivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.time_gap_2);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        ImageView close = (ImageView) showd.findViewById(R.id.close);

        final RadioButton RBtn_AddToPrevJob = (RadioButton) showd.findViewById(R.id.RBtn_AddToPrevJob);
        final RadioButton RBtn_AddToCurrentJob = (RadioButton) showd.findViewById(R.id.RBtn_AddToCurrentJob);
        final RadioButton RBtn_split = (RadioButton) showd.findViewById(R.id.RBtn_split);
        final RadioButton RBtn_Cancel = (RadioButton) showd.findViewById(R.id.RBtn_Cancel);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);
        final TextView Btn_Cancel = (TextView) showd.findViewById(R.id.Btn_Cancel);


        tv_title.setText("");
        tv_title.setVisibility(View.GONE);
        tv_msg.setVisibility(View.GONE);

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if (RBtn_AddToPrevJob.isChecked()) {


                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    // call_api_to_update_timesheet_job1();
                    if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                        // String curent_BILLABLE_START_TIME = Utility.getCurrentTimeString();
                        String curent_BILLABLE_START_TIME = sp.getString(Utility.TIMEGAP_JOB_START_TIME, "");
                        ed.putString(Utility.CLOCK_START_TIME, curent_BILLABLE_START_TIME).commit();

                        new Async_EditTimeSheet().execute();

                        //  showChatHead_Billable(MainActivity.this, true);
                        showClock();
                    } else {
                        Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }

                } else if (RBtn_AddToCurrentJob.isChecked()) {
                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    String Str_PrevJobEndTime = sp.getString(Utility.TIMEGAP_JOB_END_TIME, "");
                    ed.putString(Utility.CLOCK_START_TIME, Str_PrevJobEndTime).commit();


                    // showChatHead_Billable(MainActivity.this, true);
                    showClock();

                } else if (RBtn_split.isChecked()) {

                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    // halftime=timeDifference/2;
                    // call_api_to_update_time_sheet_Prevjob();  //use halftime
                    // add halftime to the start time of current job

                    long Total_Elapsed_Seconds = Utility.GetTimeGap(MainActivity.this);
                    //  String CurrentJobStartTime = Utility.getCurrentTimeString();
                    String CurrentJobStartTime = sp.getString(Utility.TIMEGAP_JOB_START_TIME, "");//dd-MM-yyyy HH:mm:ss
                    int half_Elapsed_Seconds = (int) (Total_Elapsed_Seconds / 2);
                    String CurrentJobStartTime_new = Utility.minus_seconds_fromCurrentTime(CurrentJobStartTime, half_Elapsed_Seconds);
                    ed.putString(Utility.CLOCK_START_TIME, CurrentJobStartTime_new).commit();


                    if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                        new Async_EditTimeSheetSplit().execute();


                        //   showChatHead_Billable(MainActivity.this, true);
                        showClock();
                    } else {
                        Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }

                } else if (RBtn_Cancel.isChecked()) {

                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    //  String CurrentJobStartTime = Utility.getCurrentTimeString();
                    String CurrentJobStartTime = sp.getString(Utility.TIMEGAP_JOB_START_TIME, "");//dd-MM-yyyy HH:mm:ss
                    ed.putString(Utility.CLOCK_START_TIME, CurrentJobStartTime).commit();

                    //  showChatHead_Billable(MainActivity.this, true);
                    showClock();
                } else {
                    Toast.makeText(MainActivity.this, "Please choose an option!", Toast.LENGTH_LONG).show();

                }
            }
        });
        Btn_Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });


        try {
            showd.show();

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String updateTimeSheet() {
        String Id = sp.getString("clientid", "");
        String dt = Utility.parseDateToMMddyyyy(Utility.getCurrentTimeString());

        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx?";
        final String SOAP_ACTION = KEY_NAMESPACE + "EditTimesheet3_Dec";
        final String METHOD_NAME = "EditTimesheet3_Dec";

        String timesheetId = sp.getString(Utility.TIME_SHEET_ID, "");
        String PREV_JOB_START_TIME = sp.getString(Utility.TIMEGAP_PREV_JOB_START_TIME, "");//dd-MM-yyyy HH:mm:ss
        String str_PREV_JOB_START_TIME = PREV_JOB_START_TIME;
        String arr1[] = PREV_JOB_START_TIME.split(" ");
        String str1 = arr1[1];
        PREV_JOB_START_TIME = str1.substring(0, str1.lastIndexOf(":"));

        String CURRENT_JOB_START_TIME = sp.getString(Utility.TIMEGAP_JOB_START_TIME, "");//"dd-MM-yyyy HH:mm:ss"
        String str_CURRENT_JOB_START_TIME = CURRENT_JOB_START_TIME;
        String arr[] = CURRENT_JOB_START_TIME.split(" ");
        String str = arr[1];
        CURRENT_JOB_START_TIME = str.substring(0, str.lastIndexOf(":"));

        boolean bool_IsSameDay = Utility.IsSameDay(str_PREV_JOB_START_TIME, str_CURRENT_JOB_START_TIME);
        String dayInfo = "0";
        if (bool_IsSameDay) dayInfo = "0";
        else dayInfo = "1";


        if (CURRENT_JOB_START_TIME.equals("00:00")) {
            dayInfo = "1";
        }

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("id", timesheetId);
        request.addProperty("start_time", PREV_JOB_START_TIME);
        request.addProperty("end_time", CURRENT_JOB_START_TIME);
        request.addProperty("dayInfo", dayInfo);
        request.addProperty("EMP_ID", Id);
        request.addProperty("DATE_TIME", dt);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONArray jsonArray = new JSONArray(receivedString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return receivedString;
    }

    public String updateTimeSheetSplit() {

        String Id = sp.getString("clientid", "");
        String dt = Utility.parseDateToMMddyyyy(Utility.getCurrentTimeString());
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx?";
        final String SOAP_ACTION = KEY_NAMESPACE + "EditTimesheet3_Dec";
        final String METHOD_NAME = "EditTimesheet3_Dec";


        String timesheetId = sp.getString(Utility.TIME_SHEET_ID, "");
        String PREV_JOB_START_TIME = sp.getString(Utility.TIMEGAP_PREV_JOB_START_TIME, "");//dd-MM-yyyy HH:mm:ss
        String str_PREV_JOB_START_TIME = PREV_JOB_START_TIME;
        String arr1[] = PREV_JOB_START_TIME.split(" ");
        String str1 = arr1[1];
        PREV_JOB_START_TIME = str1.substring(0, str1.lastIndexOf(":"));//HH:mm


        String PREV_JOB_END_TIME = sp.getString(Utility.TIMEGAP_JOB_END_TIME, "");//"dd-MM-yyyy HH:mm:ss"
        String str_PREV_JOB_END_TIME = PREV_JOB_END_TIME;


        long Total_Elapsed_Seconds = Utility.GetTimeGap(MainActivity.this);
        int half_Elapsed_Seconds = (int) (Total_Elapsed_Seconds / 2);
        String PREV_JOB_END_TIME_new = Utility.add_seconds_toCurrentTime(PREV_JOB_END_TIME, half_Elapsed_Seconds);


        String arr[] = PREV_JOB_END_TIME_new.split(" ");
        String str = arr[1];
        PREV_JOB_END_TIME_new = str.substring(0, str.lastIndexOf(":"));//HH:mm


        boolean bool_IsSameDay = Utility.IsSameDay(str_PREV_JOB_START_TIME, str_PREV_JOB_END_TIME);
        String dayInfo = "0";
        if (bool_IsSameDay) dayInfo = "0";
        else dayInfo = "1";

        if (PREV_JOB_END_TIME_new.equals("00:00")) {
            dayInfo = "1";
        }

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("id", timesheetId);
        request.addProperty("start_time", PREV_JOB_START_TIME);
        request.addProperty("end_time", PREV_JOB_END_TIME_new);
        request.addProperty("dayInfo", dayInfo);
        request.addProperty("EMP_ID", Id);
        request.addProperty("DATE_TIME", dt);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONArray jsonArray = new JSONArray(receivedString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedString;
    }

    public void getCompanyInfo(String urlskyline) {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, urlskyline,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub

                try {
                    parseJsonFeed(obj);
                } catch (Exception e) {
                    e.getMessage();
                }

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

    public void check(final String Swo_Id) {


        if (new ConnectionDetector((MainActivity.this)).isConnectingToInternet()) {
            new async_getJobDetailsBySwoId().execute(Swo_Id);
        } else {
            Toast.makeText(MainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }


        //}

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

//
//            JSONArray feedArray = response.getJSONArray("cds");
//            JSONObject jsonObject = feedArray.getJSONObject(0);
//            String companyLogo = jsonObject.getString("logo");
//            String companyName = jsonObject.getString("name");

            if (!companyLogo.equals("")) {
                String imagelogo = URL_EP1 + "/admin/uploads/collateral/"
                        + companyLogo;
                ed.putString("name", companyName).apply();
                ed.putString("imglo", imagelogo).commit();

            } else {

                ed.putString("name", companyName).apply();
                ed.putString("imglo", "").commit();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int GetPausedJob(String ID) {

        int list_size = 0;
        Log.d("BHANU", ID);
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + "GetPauseLIst";
        final String METHOD_NAME = "GetPauseLIst";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("techID", ID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();
            Log.d("BHANU", recved);

            if (recved.contains("No Pause List Available.")) {
                list_size = 0;

            }
            String[] aa = recved.split("=");
            String a = aa[1];
            String b[] = a.split(";");
            String k = b[0];
            Log.d("BHANU", k);
            pause_arrayList = new ArrayList<>();
            jobid_paused = new ArrayList<>();
            jobid_pausedNew = new ArrayList<>();
            cl_id_list_paused = new ArrayList<>();
            jobName_paused = new ArrayList<>();
            jobDesc_paused = new ArrayList<>();
            compName_paused = new ArrayList<>();
            swoStatus_paused = new ArrayList<>();
            PausedTimesheetID = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(k);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String SWO_NAME = jsonObject1.getString("swo_name");
                String swo_id = jsonObject1.getString("swo_id");
                String cl_Id = jsonObject1.getString("Client_ID");
                String jobid = jsonObject1.getString("JobID");
                String jobName = jsonObject1.getString("jobName");
                String compName = jsonObject1.getString("comp");
                String jobDesc = jsonObject1.getString("txt_Jdes");
                String SWO_Status_new = jsonObject1.getString("SWO_Status_new");
                String random_no = jsonObject1.getString("random_no");
                Log.d("BHANU", SWO_NAME + " " + cl_Id);
                pause_arrayList.add(SWO_NAME);
                cl_id_list_paused.add(cl_Id);
                jobid_paused.add(swo_id);
                jobid_pausedNew.add(jobid);
                jobName_paused.add(jobName);
                compName_paused.add(compName);
                jobDesc_paused.add(jobDesc);
                swoStatus_paused.add(SWO_Status_new);
                PausedTimesheetID.add(random_no);
            }
            list_size = pause_arrayList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_size;
    }

    void go(int l) {
        if (l == 0) {
            my_logic_of = 12;
            dialog_SearchBy();

        } else {

            hideprogressdialog();


            dialog_show_Paused_Job_List();


        }
    }


    private void dialog_App_Updated(String text, boolean icon_OK) {
        final Dialog showd = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_app_updated);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(true);
        ImageView close = showd.findViewById(R.id.close);
        TextView tv_ver = showd.findViewById(R.id.tv_ver);
        ImageView imgvw_ok = showd.findViewById(R.id.imgvw_ok);

        // String currentVersion = Utility.getAppVersion(MainActivity.this);
        // String oldVersion = Utility.getOldVersion(MainActivity.this);

        if (icon_OK) {
            imgvw_ok.setImageResource(R.drawable.verified);
        } else {
            imgvw_ok.setImageResource(R.drawable.cancel);
        }

        tv_ver.setText(text);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();
            }
        });
        imgvw_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();
            }
        });

        showd.show();
    }

    private void dialog_show_Paused_Job_List() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pause_job_list_new, null);
        dialogBuilder.setView(dialogView);

        alertDialog_Paused = dialogBuilder.create();
        alertDialog_Paused.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog_Paused.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog_Paused.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        try {
            if (!alertDialog_Paused.isShowing()) {
                alertDialog_Paused.show();
            }
        } catch (Exception e) {
            e.getMessage();
        }

        ListView listView = (ListView) alertDialog_Paused.findViewById(R.id.listView);
        PausedJob_ListAdapter adapter = new PausedJob_ListAdapter(MainActivity.this, pause_arrayList, compName_paused, jobName_paused, jobDesc_paused);
        listView.setAdapter(adapter);

        Button skip = (Button) alertDialog_Paused.findViewById(R.id.btn_skip);
        skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    alertDialog_Paused.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                //to do
                my_logic_of = 12;
                dialog_SearchBy();

            }
        });

    }

    public void getAllJobsByDealerId() {

        list_AllJobs = new ArrayList<>();

        String dealerId = sp.getString(Utility.DEALER_ID, "");
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + "GetallJobByDealerID";
        final String METHOD_NAME = "GetallJobByDealerID";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("DealerId", dealerId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();


            Log.d("BHANU", recved);
            if (recved.contains("No Data Available")) {

            } else {
                recved = recved.substring(recved.indexOf("["), recved.lastIndexOf("]") + 1);
                JSONArray jsonArray = new JSONArray(recved);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String jobName = jsonObject.getString("txt_job");
                    String JobId = jsonObject.getString("job_id_pk");
                    String job_descripition = jsonObject.getString("JOB_DESC");
                    String compID = jsonObject.getString("compID");
                    String desc = job_descripition.trim();
                    char space = ' ';
                    int index = 0;
                    if (desc.length() > 30) {
                        for (int j = 30; j < desc.length(); j++) {
                            if (desc.charAt(j) == space) {
                                // String s=desc.substring(j);
                                index = j;
                                break;
                            }
                        }
                    }
                    String total_desc = "";
                    if (index != 0) {
                        desc = desc.substring(0, index) + System.getProperty("line.separator") + (desc.substring(index)).trim();
                    }
                    total_desc = jobName + System.getProperty("line.separator") + desc.trim();
                    /////

                    HashMap<String, String> hashMap = new HashMap();
                    hashMap.put("jobName", total_desc);
                    hashMap.put("JobId", JobId);
                    hashMap.put("compID", compID);
                    list_AllJobs.add(hashMap);


                }
                list_AllJobs_forIndex = new ArrayList<>(list_AllJobs);
            }
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public int getNonBillableCodes() {
        String receivedString = "";
        int length = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String SOAP_ACTION = KEY_NAMESPACE + "bind_billable_nonBillable_code1";
        final String METHOD_NAME = "bind_billable_nonBillable_code1";
        // Create SOAP request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        // Boolean jobid = sp.getBoolean("billable", false);
        String dealerId = sp.getString(Utility.DEALER_ID, "0");
        request.addProperty("type", "2");
        //nks
        request.addProperty("dealerID", dealerId);
        //nks
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            receivedString = ks.toString();
            String Jsonstring = receivedString;
            String news = Jsonstring.substring(Jsonstring.indexOf("["), Jsonstring.indexOf("]") + 1);
            String n1 = news;
            JSONArray jArray = new JSONArray(n1);
            length = jArray.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    public int getBillableCodes() {

        String receivedString = "";
        int len = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String SOAP_ACTION = KEY_NAMESPACE + "bind_billable_nonBillable_code1";
        final String METHOD_NAME = "bind_billable_nonBillable_code1";
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("type", "1");
        String dealerId = sp.getString(Utility.DEALER_ID, "0");
        request.addProperty("dealerID", dealerId);//nks
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            receivedString = ks.toString();

        } catch (Exception e) {

            e.printStackTrace();
        }

        try {
            String Jsonstring = receivedString;
            String news = Jsonstring.substring(Jsonstring.indexOf("["), Jsonstring.indexOf("]") + 1);
            JSONArray jArray = new JSONArray(news);
            len = jArray.length();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return len;
    }

    public int getBillableCodes_new() {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Utility.METHOD_BILLABLE_NONBILLABLE_CODE;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

        String dealerId = sp.getString(Utility.DEALER_ID, "0");
        String role = sp.getString(Utility.LOGIN_USER_ROLE, "0");
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("dealerID", dealerId);
        request.addProperty("cat", role);
        request.addProperty("type", "1");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        int len = 0;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONArray jArray = new JSONArray(receivedString);
            len = jArray.length();
        } catch (Exception e) {
            e.getMessage();
        }
        return len;
    }

    public int getNonBillableCodes_New() {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Utility.METHOD_BILLABLE_NONBILLABLE_CODE;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;


        // Create SOAP request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = sp.getString(Utility.DEALER_ID, "0");
        String role = sp.getString(Utility.LOGIN_USER_ROLE, "0");
        request.addProperty("dealerID", dealerId);
        request.addProperty("cat", role);
        request.addProperty("type", "2");
        //nks
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        int len = 0;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONArray jArray = new JSONArray(receivedString);
            len = jArray.length();
        } catch (Exception e) {
            e.getMessage();
        }
        return len;

    }

    public int getMySwoList() {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Utility.METHOD_MY_SWO;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;


        // Create SOAP request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = Utility.getDealerID(MainActivity.this);
        String userID = Utility.getUserID(MainActivity.this); //sp.getString(Utility.LOGIN_USER_ROLE, "0");
        request.addProperty("user", userID);
        request.addProperty("DealerID", dealerId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        int len = 0;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONObject jsonObject = new JSONObject(receivedString);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            mySwoArrayList.clear();
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String JOB_ID = jsonObject1.getString("JOB_ID");
                    String JOB_DESC = jsonObject1.getString("JOB_DESC");
                    String CompanyName = jsonObject1.getString("CompanyName");

                    String COMP_ID = jsonObject1.getString("COMP_ID");
                    String txt_job = jsonObject1.getString("txt_job");
                    String SWO_Status_new = jsonObject1.getString("SWO_Status_new");
                    String swo_id = jsonObject1.getString("swo_id");
                    String name = jsonObject1.getString("name");
                    String TECH_ID1 = jsonObject1.getString("TECH_ID1");
                    String TECH_ID = jsonObject1.getString("TECH_ID");

                    mySwoArrayList.add(new MySwo(JOB_ID, JOB_DESC, COMP_ID, txt_job,
                            SWO_Status_new, swo_id, name, TECH_ID1, TECH_ID,CompanyName));

                }


            }

            // sort list alphabetically
           /* Collections.sort(mySwoArrayList, new Comparator<MySwo>() {
                @Override
                public int compare(MySwo u1, MySwo u2) {
                    return u1.getName().compareToIgnoreCase(u2.getName());
                }
            });*/


        } catch (Exception e) {
            e.getMessage();
        }
        return mySwoArrayList.size();

    }

    public int getUnassignedSwo() {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Utility.METHOD_UNASSIGNED_SWO;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;


        // Create SOAP request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = Utility.getDealerID(MainActivity.this);
        String userID = Utility.getUserID(MainActivity.this);
        request.addProperty("DealerID", dealerId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONObject jsonObject = new JSONObject(receivedString);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            mySwoArrayList.clear();
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String JOB_ID = jsonObject1.getString("JOB_ID");
                    String JOB_DESC = jsonObject1.getString("JOB_DESC");
                    String CompanyName = jsonObject1.getString("CompanyName");

                    String COMP_ID = jsonObject1.getString("COMP_ID");
                    String txt_job = jsonObject1.getString("txt_job");
                    String SWO_Status_new = jsonObject1.getString("SWO_Status_new");
                    String swo_id = jsonObject1.getString("swo_id");
                    String name = jsonObject1.getString("name");
                    String TECH_ID1 = jsonObject1.getString("TECH_ID1");
                    String TECH_ID = jsonObject1.getString("TECH_ID");

                    mySwoArrayList.add(new MySwo(JOB_ID, JOB_DESC, COMP_ID, txt_job,
                            SWO_Status_new, swo_id, name, TECH_ID1, TECH_ID,CompanyName));

                }
                // sort list alphabetically
               /* Collections.sort(mySwoArrayList, new Comparator<MySwo>() {
                    @Override
                    public int compare(MySwo u1, MySwo u2) {
                        return u1.getName().compareToIgnoreCase(u2.getName());
                    }
                });*/

            }


        } catch (Exception e) {
            e.getMessage();
        }
        return mySwoArrayList.size();

    }

    public void showErrorDialog_2() {
        final Dialog showd = new Dialog(MainActivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        Button Btn_No = (Button) showd.findViewById(R.id.Btn_No);
        Btn_No.setVisibility(View.GONE);
        Button Btn_Yes = (Button) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView txt_message = (TextView) showd.findViewById(R.id.texrtdesc);
        txt_message.setText("There are NO Job Codes currently in the system, you cannot perform this action!\nPlease ask your admin to add Job Codes.");
        //  txt_message.setText(Utility.NO_INTERNET);
        Btn_Yes.setText("Ok");

        Btn_Yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();

                } catch (Exception e) {

                }

            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void checkWifi() {
        boolean isWifi = Utility.isWiFiConnected(MainActivity.this);
        if (!isWifi) {
            if (!wifiManager.isWifiEnabled()) { // If wifi disabled then enable it

                dialog_TurnOnWifi();

            } else {
                dialog_ConnectWifi();
            }

        } else {

            if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                new checkVersionUpdate().execute();
            }

        }

    }

    private void EnableWifi() {

        wifiManager.setWifiEnabled(true);
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Kindly wait...");
        if (!pDialog.isShowing()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }

        }
        ;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (pDialog.isShowing()) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }

                boolean isWifi = Utility.isWiFiConnected(MainActivity.this);
                if (!isWifi) {
                    dialog_ConnectWifi();
                } else {
                    if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                        new checkVersionUpdate().execute();
                    }
                }

            }
        }, 7000);   //3 seconds


    }

    public void dialog_ConnectWifi() {

        final Dialog showd = new Dialog(MainActivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.wifi_alert);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);

        tv_title.setText("No saved connection!");
        tv_msg.setText("Connect to Wi-Fi");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),
                        WIFI_SETTING_REQUEST_CODE);

            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void dialog_TurnOnWifi() {

        final Dialog showd = new Dialog(MainActivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.wifi_alert);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);

        tv_title.setText("Attention!");
        tv_msg.setText("Turn on Wi-Fi");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                EnableWifi();
            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void dialog_TurnOnMobileData() {

        final Dialog showd = new Dialog(MainActivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.wifi_alert);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);


        tv_title.setText("Wi-Fi connection was unsuccessful!");
        tv_msg.setText("Please turn mobile data ON");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), MOBILE_DATA_SETTING_REQUEST_CODE);

            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void dialog_MobileDataNotTrunedON() {

        final Dialog showd = new Dialog(MainActivity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.wifi_alert);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd.setCancelable(false);
        showd.setCanceledOnTouchOutside(false);

        TextView tv_title = (TextView) showd.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) showd.findViewById(R.id.tv_msg);
        final TextView Btn_Done = (TextView) showd.findViewById(R.id.Btn_Done);


        tv_title.setText("Mobile data not turned ON!");
        tv_msg.setText("No Internet connection");

        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                //  finish();

            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void showClock() {

        boolean isSTARTING_BILLABLE_JOB = sp.getBoolean(Utility.STARTING_BILLABLE_JOB, false);

        if (isSTARTING_BILLABLE_JOB) {
            //  Set_Billable_Row();
            showChatHead_Billable(MainActivity.this, true);
        } else {
            showChatHead_Admin(MainActivity.this, true);
        }


    }

    private void RunIncompleteTask(String api_name, JSONObject api_input_jsonObject) {


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + api_name;
        final String METHOD_NAME = api_name;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        Iterator<?> keys = api_input_jsonObject.keys();
        while (keys.hasNext()) {
            try {
                String key = (String) keys.next();
                String val = api_input_jsonObject.getString(key);
                request.addProperty(key, val);

            } catch (Exception e) {
                e.getCause();
            }
        }
        Log.e("IncompleteAsync--->", "ApiRunning");
        Log.e("Api Data", request.toString());


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String receivedString = SoapPrimitiveresult.toString();

            JSONObject jsonObject = new JSONObject(receivedString);
            String TimeSheetID = jsonObject.getString("cds");
            if (TimeSheetID.contains("=")) {
                TimeSheetID = TimeSheetID.substring(TimeSheetID.indexOf("=") + 1);
            }
            ed.putString(Utility.TIME_SHEET_ID, TimeSheetID).commit();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void SavedTask() {
        JSONArray jsonArray = new JSONArray();
        SharedPreferences sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        Editor ed = sp.edit();

        if (sp.contains(Utility.KEY_INCOMPLETE_ASYNC_ARRAY)) {
            String IncompleteAsyncArray = sp.getString(Utility.KEY_INCOMPLETE_ASYNC_ARRAY, "");
            try {
                jsonArray = new JSONArray(IncompleteAsyncArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String str_apiId = jsonObject1.getString("apiId");
                    String api_name = jsonObject1.getString("api_name");
                    JSONObject jsonObject = jsonObject1.getJSONObject("apiInput");

                    Utility.removeIncompleteAsynctask(MainActivity.this, str_apiId);

                    RunIncompleteTask(api_name, jsonObject);

                }

            } catch (Exception e) {
                e.getCause();
            }

        }


    }

    void SavedTimeSheetDialog() {

        ArrayList<SavedTask> list = Utility.getOfflineTaskList(MainActivity.this);

        if (list.size() > 0) {
            final Dialog dialog1 = new Dialog(MainActivity.this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.offline_timesheet_list);
            dialog1.setCancelable(false);
            ListView listView = (ListView) dialog1.findViewById(R.id.listView);

            SavedTimeSheet_ListAdapter adapter = new SavedTimeSheet_ListAdapter(MainActivity.this, list);
            listView.setAdapter(adapter);

            Button btn_Submit = (Button) dialog1.findViewById(R.id.btn_Submit);
            Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);

            btn_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });

            btn_Submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                        new async_RunIncompleteTask().execute();
                    }
                }
            });

            try {
                dialog1.show();

            } catch (Exception e) {
                e.getMessage();
            }


        } else {
            if (Utility.isOverlapTimesheetExist(MainActivity.this)) {
                dialog_OverlapDataFound();

            }
        }


    }

    private void dialog_AutoDateTimeSet() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);

        final TextView title = dialogView.findViewById(R.id.texrtdesc);
        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        // dialogBuilder.setTitle("Device Details");
        title.setText("Your phone date is inaccurate! \n \n Please set automatic date and time.");
        // message.setText("Please set Automatic date and Time");

        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);

            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void dialog_OverlapDataFound() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);

        final TextView desc = dialogView.findViewById(R.id.texrtdesc);
        final TextView title = dialogView.findViewById(R.id.textView1rr);


        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        // dialogBuilder.setTitle("Device Details");
        title.setText("Overlap time sheet data found!");
        desc.setText("Do you want to edit time sheet now?");
        // message.setText("Please set Automatic date and Time");

        positiveBtn.setText("Yes");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent i = new Intent(getApplicationContext(), TimeSheetList1Activity.class);
                startActivity(i);

            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();


            }
        });
        try {
            alertDialog = dialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void getAwo() {
        list_Awo.clear();

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String SOAP_ACTION = KEY_NAMESPACE + "GetAwoDetailByJob";
        final String METHOD_NAME = "GetAwoDetailByJob";
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        String JobId = sp.getString(Utility.KEY_JOB_ID_FOR_JOBFILES, "0");
        request.addProperty("Job", JobId);//nks

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();
            JSONArray jsonArray = new JSONArray(resultstring);

            for (int k = 0; k < (jsonArray.length()); k++) {
                JSONObject json_obj = jsonArray.getJSONObject(k);
                String ID_PK = json_obj.getString("ID_PK");
                String JOB_ID = json_obj.getString("JOB_ID");
                String swo_name = json_obj.getString("swo_name");
                //  String tech_id = json_obj.getString("tech_id");
                String tech_id = "";
                String SWO_Status_new = json_obj.getString("SWO_Status_new");
                list_Awo.add(new Awo(ID_PK, JOB_ID, swo_name, tech_id, SWO_Status_new));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    private void showSWODialog() {
        swo_dialog = new Dialog(MainActivity.this);
        swo_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        swo_dialog.setContentView(R.layout.swo_list_dialog_new);

        swo_dialog.setCancelable(false);


        final ListView listView = (ListView) swo_dialog.findViewById(R.id.listView);
        final Button backToJobSelect = (Button) swo_dialog.findViewById(R.id.btn_back);
        final TextView _text_noJObs = (TextView) swo_dialog.findViewById(R.id._text_noJObs);
        _text_noJObs.setVisibility(View.GONE);

        if (swo_arrayList.size() > 0) {
            _text_noJObs.setVisibility(View.GONE);
        } else {
            _text_noJObs.setVisibility(View.VISIBLE);
        }


        backToJobSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                if (search_byJob) {

                    try {
                        dialog_ScanSWO.show();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                } else {

                    try {
                        dialog_companyName.show();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        });

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, swo_arrayList);
        listView.setAdapter(stringArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                comp_id_name = comp_id_list.get(i);

             /*   nextact = URL_EP1 + "/crate_web_service.php?id="
                        + comp_id_name;*/
                final String swo_id_IS = jobid_by_aman.get(i);   ///////develop by aman kaushik
                prepareDataForClock(comp_id_name, swo_id_IS, JOB_ID);
//                ed.putString("link", nextact);
//                ed.putString("jobid", swo_id_IS);
//                ed.commit();
//                getCompanyInfo(nextact);
//                startClockForBillable();

                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });
        try {
            swo_dialog.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    private void showSWODialog_1() {
        swo_dialog = new Dialog(MainActivity.this);
        swo_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        swo_dialog.setContentView(R.layout.swo_list_dialog_new);

        swo_dialog.setCancelable(false);


        final ListView listView = (ListView) swo_dialog.findViewById(R.id.listView);
        final Button backToJobSelect = (Button) swo_dialog.findViewById(R.id.btn_back);
        final TextView _text_noJObs = (TextView) swo_dialog.findViewById(R.id._text_noJObs);

        final TextView _txtvw_count = (TextView) swo_dialog.findViewById(R.id.txtvw_count);


        _text_noJObs.setVisibility(View.GONE);

        if (mySwoArrayList.size() > 0) {
            _text_noJObs.setVisibility(View.GONE);
        } else {
            _text_noJObs.setVisibility(View.VISIBLE);
        }
        _txtvw_count.setText("Total: "+String.valueOf(mySwoArrayList.size()));

        backToJobSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                try {
                    dialog_SearchBy();
                } catch (Exception e) {
                    e.getMessage();
                }

              /*
                if (search_byJob) {

                    try {
                        dialog_ScanSWO.show();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                } else {

                    try {
                        dialog_companyName.show();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }*/
            }
        });

        ArrayAdapter<MySwo> stringArrayAdapter = new ArrayAdapter<MySwo>(MainActivity.this, android.R.layout.simple_list_item_1, mySwoArrayList);
        listView.setAdapter(stringArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                comp_id_name = mySwoArrayList.get(i).getCOMP_ID();
                final String swo_id_IS = mySwoArrayList.get(i).getSwo_id();
                final String CompanyName = mySwoArrayList.get(i).getCompanyName();
                final String JobName = mySwoArrayList.get(i).getTxt_job();
                JOB_ID = mySwoArrayList.get(i).getJOB_ID();


                ed.putString("name", CompanyName);
                ed.putString(Utility.JOB_ID_BILLABLE, JobName);
                ed.commit();

                prepareDataForClock(comp_id_name, swo_id_IS, JOB_ID);

                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });
        try {
            swo_dialog.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    private void prepareDataForClock(String CompId, String swoID, String Job_id) {
        nextact = URL_EP1 + "/crate_web_service.php?id="
                + CompId;
        ed.putString("link", nextact);
        ed.putString("jobid", swoID);
        ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, Job_id).apply();
        ed.putString(Utility.COMPANY_ID_BILLABLE, CompId);
        ed.commit();
        getCompanyInfo(nextact);
        startClockForBillable();
    }

    private void Dialog_Choose_Awo_New() {
        swo_dialog = new Dialog(MainActivity.this);
        swo_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        swo_dialog.setContentView(R.layout.swo_list_dialog_new);

        swo_dialog.setCancelable(false);

        final ListView listView = (ListView) swo_dialog.findViewById(R.id.listView);
        final Button backToJobSelect = (Button) swo_dialog.findViewById(R.id.btn_back);
        final TextView _text_noJObs = (TextView) swo_dialog.findViewById(R.id._text_noJObs);
        final TextView title = (TextView) swo_dialog.findViewById(R.id.title);
        title.setText("AWO(s) List");
        _text_noJObs.setVisibility(View.GONE);

        if (list_Awo.size() > 0) {
            _text_noJObs.setVisibility(View.GONE);
        } else {
            _text_noJObs.setVisibility(View.VISIBLE);
        }


        backToJobSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                if (search_byJob) {

                    try {
                        dialog_ScanSWO.show();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                } else {

                    try {
                        dialog_companyName.show();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        });

        ArrayAdapter<Awo> stringArrayAdapter = new ArrayAdapter<Awo>(MainActivity.this, android.R.layout.simple_list_item_1, list_Awo);
        listView.setAdapter(stringArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    swo_dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                SelectedAwo = list_Awo.get(i).getiDPK();
                nextact = URL_EP1 + "/crate_web_service.php?id="
                        + comp_id_name;

                ed.putString("link", nextact);
                ed.putString("jobid", SelectedAwo);
                ed.commit();
                getCompanyInfo(nextact);
                startClockForBillable();


            }
        });
        try {
            swo_dialog.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    private void Dialog_Choose_Swo_Awo() {
    /*    if (count == 1) {
            Toast.makeText(MainActivity.this, "Please enter correct Job Id!", Toast.LENGTH_SHORT).show();


        } else {
            if (count == 2) {

                ed.putString("jobid", new_job_id);
                ed.commit();
                nextact = URL_EP1 + "/crate_web_service.php?id="
                        + comp_id_name;
                ed.putString("link", nextact).commit();

            } else {*/
        if (userRole.equals(Utility.USER_ROLE_APC) ||
                userRole.equals(Utility.USER_ROLE_ARTIST)) {
            //   Dialog_Choose_Awo();
            Dialog_Choose_Awo_New();
        } else {
            showSWODialog();
        }

        //   }
        //}
    }

  /*  private class getSwo extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogressdialog();
        }

        @Override
        protected Void doInBackground(String... param) {
            String ans = param[0];
            if (userRole.equals(Utility.USER_ROLE_APC) ||
                    userRole.equals(Utility.USER_ROLE_ARTIST)) {
                //   getAwo();    //already executed when check awo
            } else {
                //  GetSwoByJob(ans);  //already executed when check swo
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            hideprogressdialog();

            if (count == 1) {
                Toast.makeText(MainActivity.this, "Please enter correct Job Id!", Toast.LENGTH_SHORT).show();


            } else {
                if (count == 2) {

                    ed.putString("jobid", new_job_id);
                    ed.commit();
                    nextact = URL_EP1 + "/crate_web_service.php?id="
                            + comp_id_name;

                    ed.putString("link", nextact).commit();

                } else {
                    if (userRole.equals(Utility.USER_ROLE_APC) ||
                            userRole.equals(Utility.USER_ROLE_ARTIST)) {
                        Dialog_Choose_Awo_New();
                    } else {
                        showSWODialog();
                    }

                }
            }
        }
    }*/

    private void Dialog_Show_JobInfo() {


        ed.putString(Utility.JOB_ID_BILLABLE, company_name1).apply();

        final Dialog choose_for_scan = new Dialog(MainActivity.this);
        choose_for_scan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        choose_for_scan.setContentView(R.layout.test2_new);
        choose_for_scan.setCancelable(false);

        final TextView tv_Company = (TextView) choose_for_scan.findViewById(R.id.tv_company);
        final TextView t1 = (TextView) choose_for_scan.findViewById(R.id.t1);
        final TextView t2 = (TextView) choose_for_scan.findViewById(R.id.t2);
        final TextView t3 = (TextView) choose_for_scan.findViewById(R.id.t3);
        final TextView t4 = (TextView) choose_for_scan.findViewById(R.id.t4);
        final TextView t5 = (TextView) choose_for_scan.findViewById(R.id.t5);
        final Button cancle = (Button) choose_for_scan.findViewById(R.id.cancle);
        final Button btn_GO = (Button) choose_for_scan.findViewById(R.id.proced);


        if (new_jobtype == "null") {
            tv_Company.setText(compName);
            t1.setText(company_name1);
            t2.setText("");
            t3.setText(status1);
            t4.setText(show1);
            t5.setText(job_descripition1);

        } else {
            tv_Company.setText(compName);
            t1.setText(company_name1);
            t2.setText(jobtype1);
            t3.setText(status1);
            t4.setText(show1);
            t5.setText(job_descripition1);

        }


        btn_GO.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    choose_for_scan.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                if (my_logic_of == 24) {


                    Intent in = new Intent(MainActivity.this, Show_Jobs_Activity_New.class);

                    startActivity(in);
                } else {


                    Dialog_Choose_Swo_Awo();


                }

            }
        });

        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    choose_for_scan.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                if (search_byJob) {

                    try {
                        dialog_ScanSWO.show();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }

            }
        });

        try {
            choose_for_scan.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public String Submit_Billable_TimeSheetNew() {  ///use this fo billable

        String JOB_STOP_DateTime = Utility.getCurrentTimeString();//dd-MM-yyyy HH:mm:ss
        String arr[] = JOB_STOP_DateTime.split(" ");
        String str = arr[1];
        String JOB_STOP_HrsMinuts = str.substring(0, str.lastIndexOf(":"));//HH:mm

        String swoId = sp.getString("jobid", "");//ACTUALLY SWO ID
        String JobIdBillable = sp.getString(Utility.KEY_JOB_ID_FOR_JOBFILES, "");//ACTUALLY JOB ID
        String clientid = sp.getString("clientid", "");
        String jobName = sp.getString(Utility.JOB_ID_BILLABLE, "");

        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            e.getCause();
        }

        String Code = "";

        if (jobName.equalsIgnoreCase(Utility.CLOCK_IN)) {
            Code = CLOCK_IN_BILLABLE_CODE;
        } else if (jobName.equalsIgnoreCase(Utility.CLOCK_OUT)) {
            Code = CLOCK_OUT_BILLABLE_CODE;
        }

        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = urlofwebservice11_new;
        final String SOAP_ACTION = KEY_NAMESPACE + Utility.Method_BILLABLE_TIMESHEET;
        final String METHOD_NAME = Utility.Method_BILLABLE_TIMESHEET;

        SoapObject request_new = new SoapObject(NAMESPACE, METHOD_NAME);
        request_new.addProperty("tech_id", clientid);
        request_new.addProperty("swo_id", swoId);
        request_new.addProperty("start_time", JOB_STOP_HrsMinuts);
        request_new.addProperty("end_time", JOB_STOP_HrsMinuts);
        request_new.addProperty("description", jobName);
        request_new.addProperty("code", Code);
        request_new.addProperty("dayInfo", "0");
        request_new.addProperty("status", "2018");
        request_new.addProperty("region", jobName);
        request_new.addProperty("PhoneType", "Android");
        request_new.addProperty("EMI", imei);
        request_new.addProperty("SWOstatus", IN_PROGRESS_SWO_STATUS);
        request_new.addProperty("jobID", JobIdBillable);
        request_new.addProperty("PauseTimeSheetID", "0");


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        envelope.setOutputSoapObject(request_new);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedString;
    }


    public void getCodes() {


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String SOAP_ACTION = KEY_NAMESPACE + "bindclock_swo_tatus";
        final String METHOD_NAME = "bindclock_swo_tatus";
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = sp.getString(Utility.DEALER_ID, "0");
        request.addProperty("dealerID", dealerId);//nks

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();
            //  {"cds":[{"ClockIn":1398,"ClockOut":1399,"SWOStatus":3}]}
            JSONObject jsonObject1 = new JSONObject(resultstring);
            JSONArray jsonArray = jsonObject1.getJSONArray("cds");
            //  JSONArray jsonArray = new JSONArray(resultstring);
            //[{"ClockIn":1398,"ClockOut":1399,"SWOStatus":3}]
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            CLOCK_IN_BILLABLE_CODE = jsonObject.getString("ClockIn");
            CLOCK_OUT_BILLABLE_CODE = jsonObject.getString("ClockOut");
            IN_PROGRESS_SWO_STATUS = jsonObject.getString("SWOStatus");


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public String Auth_UsageCharge() {
        String receivedString = "";
        String status = "";
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Utility.METHOD_AUTH_USAGE_CHARGE;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        // Create SOAP request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = Utility.getDealerID(MainActivity.this);
        String userID = Utility.getUserID(MainActivity.this); //sp.getString(Utility.LOGIN_USER_ROLE, "0");
        request.addProperty("emp", userID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        int len = 0;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONObject jsonObject = new JSONObject(receivedString);
            status = jsonObject.getString("status");
/*{"PermissionID":0,"SubMenuId":40,"MainId":6,"SubMenuName":"Usage Charges",
"SubmenuUrl":"/Register/UsageCharges.aspx","IsSubCheck":null,"IsActive":0,"NGUrl":null,
"IsAdd":1,"IsEdit":1,"IsDelete":1,"IsAddRight":0,"IsEditRight":0,"IsDeleteRight":0,"status":1}*/


        } catch (Exception e) {
            e.getMessage();
        }
        return status;

    }

    private class async_getSWOList extends AsyncTask<Void, Void, Integer> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Kindly wait");
            pDialog.setCancelable(false);
            try {
                pDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
            showSWODialog_1();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //    int size = getNonBillableCodes();
            int size = 0;
            if (IsMySwo) {
                size = getMySwoList();
            } else {
                size = getUnassignedSwo();
            }
            return size;
        }

    }

    private class CheckSwo extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDoalog = new ProgressDialog(MainActivity.this);
            progressDoalog.setMessage(getString(R.string.Loading_text));
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);

            try {
                progressDoalog.show();
            } catch (Exception e) {
                e.getMessage();
            }


        }

        @Override
        protected Void doInBackground(String... param) {

            String ans = param[0];
            // GetSwoByJob(ans);
            if (userRole.equals(Utility.USER_ROLE_APC) ||
                    userRole.equals(Utility.USER_ROLE_ARTIST)) {
                getAwo();
            } else {
                GetSwoByJob(ans);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            try {
                progressDoalog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }

      /*      if (count == 1) {
                Toast.makeText(MainActivity.this, "Please enter correct Job Id!", Toast.LENGTH_SHORT).show();
                dialog_SearchByJob();
            } else {
*/

            if (userRole.equals(Utility.USER_ROLE_APC) ||
                    userRole.equals(Utility.USER_ROLE_ARTIST)) {
                if (list_Awo.size() < 1) {
                    validation_dialog();
                } else if (search_byJob) {
                    try {
                        dialog_ScanSWO.show();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    try {
                        dialog_companyName.show();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            } else if (swo_arrayList.size() < 1) {   //tech
                validation_dialog();
            } else if (search_byJob) {
                try {
                    dialog_ScanSWO.show();
                } catch (Exception e) {
                    e.getMessage();
                }
            } else {
                try {
                    dialog_companyName.show();
                } catch (Exception e) {
                    e.getMessage();
                }
            }


            //  }

        }
    }

    private class get_company_name extends AsyncTask<Void, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                ringProgressDialog.setMessage(getString(R.string.Loading_text));
                ringProgressDialog.setCancelable(false);
                ringProgressDialog.setCanceledOnTouchOutside(false);

                try {
                    ringProgressDialog.show();
                } catch (Exception e) {
                    e.getMessage();
                }


            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //    Send();
            Getcompany_name();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                ringProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }

            if (company_id_list != null && company_id_list.size() > 0) {
                dialog_SearchByCompany();
            } else {
                Toast.makeText(MainActivity.this, "No data found!", Toast.LENGTH_LONG).show();
            }
        }
    }

    //***load the requerment diloge for new diloge for company Name**//
    private class get_company_job_id extends AsyncTask<String, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected Void doInBackground(String... strings) {
            Getcompany_job_id(strings[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  ringProgressDialog.setTitle("Kindly wait ...")
            ringProgressDialog.setMessage(getString(R.string.Loading_text));
            ringProgressDialog.setCancelable(false);

            try {
                ringProgressDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                ringProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }


            if (job_Name_list.size() < 1) {
                //  if (count == 1) {
                company_name.setText("");
                job_name.setText("");
                Dialog_No_JobsAvailable();
            } else {
                job_name.setText("");

                CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(MainActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
                job_name.setAdapter(jobDescAdapter);
                job_name.setDropDownHeight(550);
            }

        }
    }

    //***load the requerment diloge for new diloge for company Name**//
    private class get_company_Area extends AsyncTask<String, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ringProgressDialog.setMessage(getString(R.string.Loading_text));
            ringProgressDialog.setCancelable(false);

            try {
                ringProgressDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }

        }

        @Override
        protected Void doInBackground(String... par) {

            get_data(par[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                ringProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }

            Dialog_Show_JobInfo();

        }
    }

    private class checkVersionUpdate extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.Loading_text));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            try {
                progressDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String val = "0.0";
            final String NAMESPACE = KEY_NAMESPACE + "";
            final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
            final String SOAP_ACTION = KEY_NAMESPACE + "GetVersion";
            final String METHOD_NAME = "GetVersion";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            try {

                final String appPackageName = getPackageName();
                String address = "https://play.google.com/store/apps/details?id=" + appPackageName;
                // String address = "https://itunes.apple.com/in/app/reliance-mutualfund/id691879143?mt=8";

                request.addProperty("address", address);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(URL);
                httpTransport.call(SOAP_ACTION, envelope);
                KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
                for (int j = 0; j < ks.getPropertyCount(); j++) {
                    ks.getProperty(j);
                }
                String recved = ks.toString();

                if (recved.contains("GetVersionResult")) {
                    val = recved.substring(recved.indexOf("=") + 1, recved.indexOf(";"));
                }

                //  val = "1.1";//test
            } catch (Exception e) {
                e.getMessage();
            }
            return val;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            String ver = result;
            Utility.setLatestVersion(MainActivity.this, result);
            Utility.setOldVersion(MainActivity.this);
            super.onPostExecute(result);
            try {
                if (progressDialog != null && progressDialog.isShowing()) {

                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            } catch (Exception e) {
                e.getMessage();
            }

            try {

                PackageManager manager = MainActivity.this.getPackageManager();
                PackageInfo info = manager.getPackageInfo(MainActivity.this.getPackageName(), 0);
                String versionName = info.versionName;
                String nversionName = ver;
                if (nversionName == null) {
                    nversionName = "0";
                }
                Double retuenvl = Double.parseDouble(nversionName);
                if (Double.parseDouble(versionName) < retuenvl) {
                    // if (true) {
                    try {


                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
                        dialogView.setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialogBuilder.setView(dialogView);
                        final TextView title = dialogView.findViewById(R.id.textView1rr);
                        final TextView message = dialogView.findViewById(R.id.texrtdesc);
                        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
                        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
                        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
                        close.setVisibility(View.INVISIBLE);
                        // dialogBuilder.setTitle("Device Details");
                        title.setText("New Update Available");
                        message.setText("Please update to the latest version!");
                        positiveBtn.setText("Update");
                        negativeBtn.setText("No");
                        negativeBtn.setVisibility(View.GONE);
                        positiveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();

                                Utility.setAppUpdateChecked(MainActivity.this);

                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }

                                finish();
                            }
                        });
                        negativeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();

                            }
                        });
                        alertDialog = dialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        try {
                            alertDialog.show();
                        } catch (Exception e) {
                            e.getCause();
                        }






/*                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setTitleText("New Update Available");
                        sweetAlertDialog.setContentText("Please update to the latest version!");
                        sweetAlertDialog.setConfirmText("Update");
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setCanceledOnTouchOutside(false);
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                *//**//*
                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }

                                finish();
                                *//**//*

                            }
                        })
                                .show();*/

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            } catch (Exception err) {
                err.getMessage();
            }


            SavedTimeSheetDialog();


        }


    }

    private class Async_EditTimeSheet extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogressdialog();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return updateTimeSheet();
            //return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideprogressdialog();


            JSONObject jsonObject = null;
            String output = "";
            String msg = "";
            try {
                JSONArray jsonArray = new JSONArray(result);
                jsonObject = jsonArray.getJSONObject(0);
                //  {"ID":"0","Output":"0","Message":"Overlapping Time entries"}
                Log.e("AdminTimeSheetId", result);
                output = jsonObject.getString("overlapp");
                msg = jsonObject.getString("msg");
            } catch (Exception e) {
                e.getCause();
            }


        }
    }

    private class Async_EditTimeSheetSplit extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showprogressdialog();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return updateTimeSheetSplit();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // hideprogressdialog();

            JSONObject jsonObject = null;
            String output = "";
            String msg = "";
            try {
                JSONArray jsonArray = new JSONArray(result);
                jsonObject = jsonArray.getJSONObject(0);
                //  {"ID":"0","Output":"0","Message":"Overlapping Time entries"}
                Log.e("AdminTimeSheetId", result);
                output = jsonObject.getString("overlapp");
                msg = jsonObject.getString("msg");
            } catch (Exception e) {
                e.getCause();
            }

        }
    }

    private class GetPausedJobs extends AsyncTask<Void, Void, Integer> {
        //  ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  try {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage(getString(R.string.Loading_text));
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);


                try {
                    progressDialog.show();
                } catch (Exception e) {
                    e.getMessage();
                }


            } catch (Exception e) {
                e.getMessage();
            }*/
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            String Id = sp.getString("clientid", "");
            int getCheck_length = GetPausedJob(Id);
            //     GetSwoByJob(ans);

            return getCheck_length;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {

            taskStartedforPauseList = false;
            hideprogressdialog();
            try {
                go(aVoid);
            } catch (Exception e) {
                e.getMessage();
            }


        }
    }

    private class getAllJObByDealer extends AsyncTask<Void, Void, Integer> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.Loading_text));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);

            try {
                progressDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }


        }

        @Override
        protected Integer doInBackground(Void... voids) {
            getAllJobsByDealerId();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {

            try {
                progressDialog.dismiss();
                dialog_SearchByJob();
            } catch (Exception e) {
                e.getMessage();
            }


        }
    }

    private class async_getNonBillableCodes extends AsyncTask<Void, Void, Integer> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Kindly wait");
            pDialog.setCancelable(false);

            try {
                pDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }

            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            try {
                pDialog.dismiss();

                if (integer == 0) {
                    showErrorDialog_2();
                } else {
                    startClockForAdmin();
                }

            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //    int size = getNonBillableCodes();
            int size = getNonBillableCodes_New();
            return size;
        }

    }

    private class async_getBillableCodes extends AsyncTask<Void, Void, Integer> {
        //   ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {


            super.onPreExecute();
            showprogressdialog();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //  int size = getBillableCodes();
            int size = getBillableCodes_new();

            return size;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //  super.onPostExecute(integer);

            if (integer == 0) {
                //  Toast.makeText(MainActivity.this, "not billable codes", Toast.LENGTH_SHORT).show();
                //   showErrorDialog();
                showErrorDialog_2();
                hideprogressdialog();
            } else {
                new GetPausedJobs().execute();
                taskStartedforPauseList = true;
            }


        }


    }

    private class async_getJobDetailsBySwoId extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Kindly wait");
            pDialog.setCancelable(false);

            try {
                pDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }

            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(String... param) {
            // TODO Auto-generated method stub

            JSONObject js_obj = new JSONObject();
            String swoid = param[0];
            String receivedString = "";

            final String NAMESPACE = KEY_NAMESPACE + "";
            final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
            final String SOAP_ACTION = KEY_NAMESPACE + "GetJobDetailsBySWO";
            final String METHOD_NAME = "GetJobDetailsBySWO";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("SWO", swoid);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(URL);

            try {
                httpTransport.call(SOAP_ACTION, envelope);

                Object results = (Object) envelope.getResponse();
                String resultstring = results.toString();
                JSONObject jsonObject = new JSONObject(resultstring);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                js_obj = jsonArray.getJSONObject(0);


            } catch (Exception e) {

                e.printStackTrace();
            }


            return js_obj;
        }

        @Override
        protected void onPostExecute(JSONObject js_obj) {
            super.onPostExecute(js_obj);
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }


            try {
                String login_dealerId = sp.getString(Utility.DEALER_ID, "");
                String dealerID = js_obj.getString("dealerID");
                if (!dealerID.equals(login_dealerId)) {
                    Toast.makeText(getApplicationContext(), "Please scan valid QR Code of SWO!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Dialog_ShowJobInfo_from_SWO(js_obj);
                }
            } catch (Exception e) {
                e.getMessage();
            }





               /* String job_id = js_obj.getString("job_id");
                String jobName = js_obj.getString("jobName");
                String JOB_TYPE = js_obj.getString("JOB_TYPE");
                String jobstatus = js_obj.getString("jobstatus");
                String company = js_obj.getString("company");
                String Show_Name = js_obj.getString("Show_Name");
                String desciption = js_obj.getString("desciption");
                String SWO_Status_new = js_obj.getString("SWO_Status_new");
                String dealerID = js_obj.getString("dealerID");

                ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, job_id).apply();
                ed.putString(Utility.JOB_ID_BILLABLE, jobName).apply();
                //  ed.putString(Utility.KEY_SELECTED_SWO_STATUS, SWO_Status_new).apply();
                String login_dealerId = sp.getString(Utility.DEALER_ID, "");

                final Dialog dialog_jobInfo = new Dialog(MainActivity.this);
                dialog_jobInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_jobInfo.setContentView(R.layout.test2_new);
                dialog_jobInfo.setCancelable(false);

                final TextView tv_Company = (TextView) dialog_jobInfo.findViewById(R.id.tv_company);
                final TextView t1 = (TextView) dialog_jobInfo.findViewById(R.id.t1);
                final TextView t2 = (TextView) dialog_jobInfo.findViewById(R.id.t2);
                final TextView t3 = (TextView) dialog_jobInfo.findViewById(R.id.t3);
                final TextView t4 = (TextView) dialog_jobInfo.findViewById(R.id.t4);
                final TextView t5 = (TextView) dialog_jobInfo.findViewById(R.id.t5);
                final Button cancle = (Button) dialog_jobInfo.findViewById(R.id.cancle);
                final Button btn_GO = (Button) dialog_jobInfo.findViewById(R.id.proced);


                tv_Company.setText((company));
                t1.setText(jobName);
                t2.setText(JOB_TYPE);
                t3.setText(jobstatus);
                t4.setText(Show_Name);
                t5.setText(desciption);


                btn_GO.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  dialog_companyName.dismiss();
                        try {
                            dialog_jobInfo.dismiss();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        if (my_logic_of == 24) {//this for if we had scanned SWO for JOB Files
                            Intent in = new Intent(MainActivity.this, Show_Jobs_Activity_New.class);
                            startActivity(in);
                        } else { //this for if we had scanned SWO for billable job start

                            startClockForBillable();
                        }
                    }
                });

                cancle.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog_jobInfo.dismiss();
                        } catch (Exception e) {
                            e.getMessage();
                        }


                    }
                });


                try {
                    dialog_jobInfo.show();
                } catch (Exception e) {
                    e.getMessage();
                }*/

            /*} catch (Exception e) {
                e.getMessage();
            }*/
        }

    }

    private class async_RunIncompleteTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Submitting offline data...");
            pDialog.setCancelable(false);

            try {
                pDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SavedTask();

            return null;
        }

        @Override
        protected void onPostExecute(Void integer) {
            try {
                pDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }
            Toast.makeText(MainActivity.this, "Time sheet(s) submitted successfully!", Toast.LENGTH_SHORT).show();

            if (Utility.isOverlapTimesheetExist(MainActivity.this)) {
                dialog_OverlapDataFound();

            }
        }


    }

    public class PausedJob_ListAdapter extends BaseAdapter {
        Context context;


        //ArrayList<Integer> icon;
        ArrayList<String> list_swo;
        ArrayList<String> list_comp;
        ArrayList<String> list_jobName;
        ArrayList<String> list_jobDesc;

        public PausedJob_ListAdapter(Context activity, ArrayList<String> list_swo, ArrayList<String> list_comp, ArrayList<String> list_jobName, ArrayList<String> list_jobDesc) {
            // TODO Auto-generated constructor stub
            this.context = activity;

            this.list_swo = list_swo;
            this.list_comp = list_comp;
            this.list_jobName = list_jobName;
            this.list_jobDesc = list_jobDesc;
            //      this.icon=icon;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list_comp.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            try {
                final ViewHolder holder;

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    //convertView = inflater.inflate(R.layout.list_row_recent_transac_1, null);
                    convertView = inflater.inflate(R.layout.list_row_recent_transac_2, null);

                    holder = new ViewHolder();
                    holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                    holder.tv_job = (TextView) convertView.findViewById(R.id.tv_job);
                    holder.tv_job_name = (TextView) convertView.findViewById(R.id.tv_job_name);
                    holder.serial_no = (Button) convertView.findViewById(R.id.serial_no);
                    holder.tv_job_desc = (TextView) convertView.findViewById(R.id.tv_job_desc);
                    holder.card_view = convertView.findViewById(R.id.card_view);
                    holder.ll_swo_row = convertView.findViewById(R.id.ll_swo_row);


                    if (userRole.equals(Utility.USER_ROLE_APC) ||
                            userRole.equals(Utility.USER_ROLE_ARTIST)) {
                        holder.ll_swo_row.setVisibility(View.GONE);// Hide SWO ROW
                    } else {
                        holder.ll_swo_row.setVisibility(View.VISIBLE);// SHOW SWO ROW
                    }


                    convertView.setTag(holder);


                } else {
                    holder = (ViewHolder) convertView.getTag();

                }


                holder.serial_no.setText(String.valueOf(position + 1));
                holder.tv_company.setText(list_comp.get(position));
                holder.tv_job.setText(list_swo.get(position));
                holder.tv_job_name.setText(list_jobName.get(position));
                holder.tv_job_desc.setText(list_jobDesc.get(position));
                holder.card_view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            alertDialog_Paused.dismiss();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        ed.putBoolean(Utility.STARTING_BILLABLE_JOB, true).commit();

                        String comp_id_name = cl_id_list_paused.get(position);
                        final String swo_id_IS = jobid_paused.get(position);
                        new_job_id = jobid_pausedNew.get(position);
                        String jobName_forPaused = jobName_paused.get(position);

                        String strPausedTimesheetID = PausedTimesheetID.get(position);
                        ed.putString(Utility.KEY_PAUSED_TIMESHEET_ID, strPausedTimesheetID).apply();

                        ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, new_job_id).apply();
                        ed.putString(Utility.JOB_ID_BILLABLE, jobName_forPaused).apply();
                        ed.putString("name",  compName_paused.get(position)).apply();
                        String nextact = URL_EP1 + "/crate_web_service.php?id=" + comp_id_name;
                        ed.putString("link", nextact);
                        ed.putString("jobid", swo_id_IS);
                        ed.putString(Utility.COMPANY_ID_BILLABLE, cl_id_list_paused.get(position));
                        //   ed.putString(Utility.KEY_SELECTED_SWO_STATUS, swoStatus_paused.get(position));
                        ed.commit();
                        ed.putString("url", nextact).commit();
                        getCompanyInfo(nextact);
                        startClockForPaused();
                    }
                });


            } catch (Exception e) {
                e.getMessage();
            }
            return convertView;
        }

        final public class ViewHolder {

            private TextView tv_company;
            private TextView tv_job;
            private TextView tv_job_name;
            private TextView tv_job_desc;
            private Button serial_no;
            private CardView card_view;
            private LinearLayout ll_swo_row;
        }


    }

    public class Async_Submit_Billable_Timesheet_New extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDoalog = new ProgressDialog(MainActivity.this);
            progressDoalog.setMessage(getString(R.string.Loading_text));
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            try {
                progressDoalog.show();
            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDoalog != null && progressDoalog.isShowing()) {
                try {
                    progressDoalog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            // {"ID":"0","Output":"0","Message":"Failed, Overlapping Time entries found!"}
            JSONObject jsonObject = null;
            String output = "", msg = "";

            try {
                jsonObject = new JSONObject(result);
                output = jsonObject.getString("Output");
                msg = jsonObject.getString("Message");
            } catch (Exception e) {
                e.getCause();
            }
            if (output.equals("1")) {   //means sheet submitted successfully
                String jobName = sp.getString(Utility.JOB_ID_BILLABLE, "");
                if (jobName.equalsIgnoreCase(Utility.CLOCK_IN)) {

                    dialog_App_Updated("You’ve clocked in!", true);
                } else if (jobName.equalsIgnoreCase(Utility.CLOCK_OUT)) {

                    dialog_App_Updated("You’ve clocked out!", true);
                }

            } else {
                dialog_App_Updated(msg, false);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            getCodes();
            return Submit_Billable_TimeSheetNew();

        }

    }

    private class Async_Check_UsageChargeAuth extends AsyncTask<String, Void, String> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected String doInBackground(String... strings) {
            return Auth_UsageCharge();
          //  return "1";

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  ringProgressDialog.setTitle("Kindly wait ...")
            ringProgressDialog.setMessage(getString(R.string.Loading_text));
            ringProgressDialog.setCancelable(false);

            try {
                ringProgressDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }


        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);

            try {
                ringProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }


            if (status.equals("1")) {

                startActivity(new Intent(getApplicationContext(), UsageChargesListActivity.class));

            } else {
                Toast.makeText(MainActivity.this, "You are not authorized to access Usage Charges!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
