package planet.info.skyline.tech.choose_job_company;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.adapter.CompanyNameAdapter;
import planet.info.skyline.adapter.JobNameAdapter;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_BindJob;
import static planet.info.skyline.network.Api.API_GetAllDetailbyJobtext_New;
import static planet.info.skyline.network.Api.API_GetJobDetailsBySWO;
import static planet.info.skyline.network.Api.API_GetallJobByDealerID;
import static planet.info.skyline.network.Api.API_bindClientByDealer;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class SelectCompanyActivity extends AppCompatActivity {
    String userRole = "";
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    int count;
    int pankajtester_chutya;
    String job = "", comp_id_name = "";

    ArrayList<HashMap<String, String>> list_AllJobs = new ArrayList<>(),
            list_AllJobs_forIndex = new ArrayList<HashMap<String, String>>();
    List<String> job_Name_list_Desc = new ArrayList<String>();
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

    Dialog dialog_companyName, dialog_ScanSWO;

    AutoCompleteTextView job_name, company_name;
    AutoCompleteTextView et_JobName;
    AlertDialog alertDialog;

    String Is_Job_Mandatory = "";
    boolean showDialog_SHOW_INFO = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        setTitle(Utility.getTitle("Choose Company/Job"));


        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
        ed = sp.edit();
        userRole = Shared_Preference.getUSER_ROLE(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button Btn_SearchByJob = (Button) findViewById(R.id.Btn_SearchByJob);
        Button Btn_SearchByCompany = (Button) findViewById(R.id.Btn_SearchByCompany);
        Button scann_swo = (Button) findViewById(R.id.scan);

        if (userRole.equals(Utility.USER_ROLE_APC) ||
                userRole.equals(Utility.USER_ROLE_ARTIST)) {
            scann_swo.setVisibility(View.GONE);
        } else {
            scann_swo.setVisibility(View.VISIBLE);
        }


        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Is_Job_Mandatory = bundle.getString(Utility.IS_JOB_MANDATORY, "0");
                if (bundle.containsKey(Utility.Show_DIALOG_SHOW_INFO)) {
                    showDialog_SHOW_INFO = bundle.getBoolean(Utility.Show_DIALOG_SHOW_INFO, false);

                }
            }
        } catch (Exception e) {
            e.getMessage();
        }


        Btn_SearchByCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new ConnectionDetector(SelectCompanyActivity.this).isConnectingToInternet()) {
                    new get_company_name().execute();
                } else {
                    Toast.makeText(SelectCompanyActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }
        });

        Btn_SearchByJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new getAllJObByDealer().execute();
                //    search_byJob = true;

            }
        });

        scann_swo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanqr();
            }
        });


    }

    public void Getcompany_name()   ///by aman kaushik
    {
        // String dealerId = sp.getString(Utility.DEALER_ID, "");
        String dealerId = Shared_Preference.getDEALER_ID(this);

        count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_bindClientByDealer;
        final String METHOD_NAME = API_bindClientByDealer;
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
                count = 1;
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

    public void dialog_SearchByCompany()    /////by aman kaushik
    {
        pankajtester_chutya = 0;
        dialog_companyName = new Dialog(SelectCompanyActivity.this);
        dialog_companyName.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_companyName.setContentView(R.layout.test_new);
        dialog_companyName.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_companyName.setCancelable(true);
        dialog_companyName.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        try {
            dialog_companyName.show();
        } catch (Exception e) {
            e.getMessage();
        }
        ImageView closebtn = (ImageView) dialog_companyName.findViewById(R.id.close);
        company_name = (AutoCompleteTextView) dialog_companyName.findViewById(R.id.company);
        job_name = (AutoCompleteTextView) dialog_companyName.findViewById(R.id.job);
        TextView txt_optional = (TextView) dialog_companyName.findViewById(R.id.txt_optional);

        if (Is_Job_Mandatory.equals("1")) {
            txt_optional.setVisibility(View.INVISIBLE);
        } else {
            txt_optional.setVisibility(View.VISIBLE);
        }

        Button btn_GO = (Button) dialog_companyName.findViewById(R.id.go_button);
        Button scann_swo = (Button) dialog_companyName.findViewById(R.id.Scann);


        TextView or = (TextView) dialog_companyName.findViewById(R.id.or);

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
        companyNameAdapter = new CompanyNameAdapter(SelectCompanyActivity.this,
                android.R.layout.simple_list_item_1, company_Name_list);
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

        scann_swo.setOnClickListener(new View.OnClickListener() {
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


                    if (new ConnectionDetector(SelectCompanyActivity.this).isConnectingToInternet()) {
                        new get_company_job_id().execute(comp_id_name);
                    } else {
                        Toast.makeText(SelectCompanyActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }


                }
            }
        });


        job_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String job_txt = job_name.getText().toString();
                job_txt = job_txt.substring(0, job_txt.indexOf("\n"));

                //    int index = job_Name_list.indexOf(job_txt);
                ///
                //  main_jobid = job_Name_list.get(index);////EDIT IN THIS
                //  new_job_id = job_id_list.get(index);
                //     ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, new_job_id).apply();
                //  new_des = job_des_list.get(index);
                //   main_status = status_list.get(index);////EDIT I

                //   new_jobtype = jobtype_list.get(index);
                //   new_show = show_list.get(index);
                //   job_name.setText("");
                job_name.setText(job_txt);


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
                    companyNameAdapter = new CompanyNameAdapter(SelectCompanyActivity.this, android.R.layout.simple_list_item_1, company_Name_list);
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
                            CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(SelectCompanyActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
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
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog_companyName.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });

        btn_GO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utility.hideKeyboard(SelectCompanyActivity.this);
                String str_CompanyName = company_name.getText().toString().trim();
                String str_JobName = job_name.getText().toString().trim();

                String CompanyId = "";
                String JObId = "";

                if (str_CompanyName.equals("")) {
                    Toast.makeText(SelectCompanyActivity.this, "Please select Company!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!company_Name_list_forIndex.contains(str_CompanyName)) {
                    Toast.makeText(SelectCompanyActivity.this, "Please enter correct Company Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Is_Job_Mandatory.equals("1") && str_JobName.equals("")) {
                    Toast.makeText(SelectCompanyActivity.this, "Please select Job!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!str_JobName.equals("") && !job_Name_list.contains(str_JobName)) {
                    Toast.makeText(SelectCompanyActivity.this, "Please enter correct Job Name!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!str_CompanyName.equals("")) {
                    for (int j = 0; j < company_Name_list_forIndex.size(); j++) {
                        String compName = company_Name_list_forIndex.get(j);
                        if (str_CompanyName.equals(compName)) {
                            CompanyId = company_id_list_forIndex.get(j);
                            Log.e("comp_id--->", CompanyId);
                            //  Toast.makeText(getApplicationContext(), "Comp:" + comp_id_name, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }

                if (!str_JobName.equals("")) {
                    for (int j = 0; j < job_Name_list.size(); j++) {
                        String jobName = job_Name_list.get(j);
                        if (str_JobName.equals(jobName)) {
                            JObId = job_id_list.get(j);
                            Log.e("job_id--->", JObId);
                            break;
                        }
                    }
                }

                dialog_companyName.dismiss();
                if (showDialog_SHOW_INFO) {
                    new get_company_Area(CompanyId).execute(str_JobName);
                } else {
                    SendDataToCallingActivity(CompanyId, JObId, str_CompanyName, str_JobName);
                }


            }
        });




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

    public void getAllJobsByDealerId() {

        list_AllJobs = new ArrayList<>();
        // String dealerId = sp.getString(Utility.DEALER_ID, "");
        String dealerId = Shared_Preference.getDEALER_ID(this);

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetallJobByDealerID;
        final String METHOD_NAME = API_GetallJobByDealerID;
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
                    String companyName = jsonObject.getString("company");
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
                    hashMap.put("companyName", companyName);
                    list_AllJobs.add(hashMap);


                }
                list_AllJobs_forIndex = new ArrayList<>(list_AllJobs);
            }
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void dialog_SearchByJob()    /////by aman kaushik
    {
        dialog_ScanSWO = new Dialog(SelectCompanyActivity.this);
        dialog_ScanSWO.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_ScanSWO.setContentView(R.layout.diloge_for_billable_only_new);
        dialog_ScanSWO.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_ScanSWO.setCancelable(true);
        try {
            dialog_ScanSWO.show();
        } catch (Exception e) {
            e.getMessage();
        }
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
        JobNameAdapter jobDescAdapter = new JobNameAdapter(SelectCompanyActivity.this, android.R.layout.simple_list_item_1, list_AllJobs);
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

             /*   if (userRole.equals(Utility.USER_ROLE_APC) ||
                        userRole.equals(Utility.USER_ROLE_ARTIST)) {
                } else {
                    dialog_ScanSWO.hide();
                }*/
                //  search_byJob = true;
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
                //  new MainActivity.CheckSwo().execute(job_txt);
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
                    JobNameAdapter jobDescAdapter = new JobNameAdapter(SelectCompanyActivity.this, android.R.layout.simple_list_item_1, list_AllJobs);
                    et_JobName.setAdapter(jobDescAdapter);
                    et_JobName.setDropDownHeight(550);

                } else {
                    et_JobName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                }
            }
        });


        ///// new work


        closebtn.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dialog_ScanSWO.dismiss();
                        } catch (Exception e) {
                            e.getMessage();
                        }


                    }

                });
        scanswo.setOnClickListener(new View.OnClickListener() {
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

        btn_GO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(SelectCompanyActivity.this);
                String str_CompanyName = "";
                String str_JobName = et_JobName.getText().toString();
                boolean isCorrectJob = false;
                String CompanyId = "";
                String JObId = "";

                if (str_JobName.length() == 0) {
                    Toast.makeText(SelectCompanyActivity.this, "Please select job!", Toast.LENGTH_SHORT).show();
                    return;
                }


                for (int j = 0; j < list_AllJobs_forIndex.size(); j++) {
                    HashMap<String, String> hashMap = list_AllJobs_forIndex.get(j);
                    String jobName = hashMap.get("jobName");
                    jobName = jobName.substring(0, jobName.indexOf("\n"));
                    if (jobName.equals(str_JobName)) {
                        CompanyId = hashMap.get("compID");
                        str_JobName = jobName;
                        JObId = hashMap.get("JobId");
                        str_CompanyName = hashMap.get("companyName");
                        isCorrectJob = true;
                        break;
                    }
                }
                if (isCorrectJob) {

                    dialog_ScanSWO.dismiss();

                    if (showDialog_SHOW_INFO) {
                        new get_company_Area(CompanyId).execute(str_JobName);
                    } else {
                        SendDataToCallingActivity(CompanyId, JObId, str_CompanyName, str_JobName);
                    }

                } else {
                    Toast.makeText(SelectCompanyActivity.this, "Please enter correct job!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(),
                        "Press a button to start a scan.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String result = contents;
                //  System.out.println("jobid & client id-----------------" + result);
                Log.d("BHANU", "" + contents + " " + result);
                String Swo_Id = "";
                String clientid = "";
                //  String dealerid = "";

                if (result.contains(",")) {
                    try {
                        String[] jj = result.split(",");
                        Swo_Id = jj[0];
                        clientid = jj[1];
                        //  dealerid = jj[2];
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "SWO error!", Toast.LENGTH_SHORT).show();
                    }


                    if (clientid.equalsIgnoreCase("")) {
                        // dialog_SearchByJob();
                        Toast.makeText(getApplicationContext(), "Please scan valid QR Code of SWO!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new async_getJobDetailsBySwoId().execute(Swo_Id);
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


    }

    public void f2() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectCompanyActivity.this);
        LayoutInflater inflater = LayoutInflater.from(SelectCompanyActivity.this);
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.getCause();
        }

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


    }

    public void Getcompany_job_id(String id) {
        count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_BindJob;
        final String METHOD_NAME = API_BindJob;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("ClientID", id);
        Log.d("BHANU--ID", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(result);
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
            //   }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SendDataToCallingActivity(String CompanyId, String JObId, String CompanyName, String JobName) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("CompID", CompanyId);
        returnIntent.putExtra("JobID", JObId);
        returnIntent.putExtra("CompName", CompanyName);
        returnIntent.putExtra("JobName", JobName);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        //   onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public String fetch_JobDetailByJobName(String id) {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetAllDetailbyJobtext_New;
        final String METHOD_NAME = API_GetAllDetailbyJobtext_New;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = Shared_Preference.getDEALER_ID(this);

        request.addProperty("job", id);
        request.addProperty("dealerid", dealerId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedString;
    }

    private void dialog_SHOW_Info(final String CompanyID, final String JobID, final String CompanyName, final String JobName, final String JobType, final String JobDesc, final String Status, final String ShowName) {

        final Dialog dialog_JobShowDetails = new Dialog(SelectCompanyActivity.this);
        dialog_JobShowDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_JobShowDetails.setContentView(R.layout.test2_new);
        dialog_JobShowDetails.setCancelable(false);
        dialog_JobShowDetails.show();
        final TextView tv_Company = (TextView) dialog_JobShowDetails.findViewById(R.id.tv_company);
        final TextView t1 = (TextView) dialog_JobShowDetails.findViewById(R.id.t1);
        final TextView t2 = (TextView) dialog_JobShowDetails.findViewById(R.id.t2);
        final TextView t3 = (TextView) dialog_JobShowDetails.findViewById(R.id.t3);
        final TextView t4 = (TextView) dialog_JobShowDetails.findViewById(R.id.t4);
        final TextView t5 = (TextView) dialog_JobShowDetails.findViewById(R.id.t5);
        final Button cancle = (Button) dialog_JobShowDetails.findViewById(R.id.cancle);
        final Button btn_GO = (Button) dialog_JobShowDetails.findViewById(R.id.proced);

        tv_Company.setText(CompanyName);
        t1.setText(JobName);
        t2.setText(JobType);
        t3.setText(Status);
        t4.setText(ShowName);
        t5.setText(JobDesc);


        btn_GO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_JobShowDetails.dismiss();
                SendDataToCallingActivity(CompanyID, JobID, CompanyName, JobName);

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_JobShowDetails.dismiss();
            }
        });

    }

    private class get_company_name extends AsyncTask<Void, Void, Void> {

        final ProgressDialog ringProgressDialog =
                new ProgressDialog(SelectCompanyActivity.this);

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
                Toast.makeText(SelectCompanyActivity.this, "No data found!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class get_company_job_id extends AsyncTask<String, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(SelectCompanyActivity.this);

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

            if (count == 1) {
                // company_name.setText("");
                job_name.setText("");
                f2();
            } else {
                job_name.setText("");
                CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(SelectCompanyActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
                job_name.setAdapter(jobDescAdapter);
                job_name.setDropDownHeight(550);
            }

        }
    }

    private class getAllJObByDealer extends AsyncTask<Void, Void, Integer> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SelectCompanyActivity.this);
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

    private class async_getJobDetailsBySwoId extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(SelectCompanyActivity.this);
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
            final String SOAP_ACTION = KEY_NAMESPACE + API_GetJobDetailsBySWO;
            final String METHOD_NAME = API_GetJobDetailsBySWO;

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
            Utility.hideKeyboard(SelectCompanyActivity.this);
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
            String comp_id = "";
            String job_id = "";
            String jobName = "";
            String companyName = "";

            String JOB_TYPE = "";
            String jobstatus = "";
            String Show_Name = "";
            String desciption = "";

            try {

                job_id = js_obj.getString("job_id");
                jobName = js_obj.getString("jobName");
                JOB_TYPE = js_obj.getString("JOB_TYPE");
                jobstatus = js_obj.getString("jobstatus");
                companyName = js_obj.getString("company");
                Show_Name = js_obj.getString("Show_Name");
                desciption = js_obj.getString("desciption");
                String SWO_Status_new = js_obj.getString("SWO_Status_new");
                String dealerID = js_obj.getString("dealerID");
                comp_id = js_obj.getString("compID");

            } catch (Exception e) {
                e.getMessage();
                Toast.makeText(getApplicationContext(), "Some error occurred!", Toast.LENGTH_SHORT).show();
            }

            if (showDialog_SHOW_INFO) {
                dialog_SHOW_Info(comp_id, job_id, companyName, jobName, JOB_TYPE, desciption, jobstatus, Show_Name);
            } else {
                SendDataToCallingActivity(comp_id, job_id, companyName, jobName);
            }


        }

    }

    private class get_company_Area extends AsyncTask<String, Void, String> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(SelectCompanyActivity.this);

        String CompanyId = "";

        get_company_Area(String Comp_Id) {
            this.CompanyId = Comp_Id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ringProgressDialog.setMessage(getString(R.string.Loading_text));
            ringProgressDialog.setCancelable(false);
            ringProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... par) {

            return fetch_JobDetailByJobName(par[0]);

        }

        @Override
        protected void onPostExecute(String Result) {
            super.onPostExecute(Result);
            ringProgressDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(Result);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String txt_Job = jsonObject1.getString("txt_Job");
                    String ShowName = jsonObject1.getString("ShowName");
                    String JOB_TYPE = jsonObject1.getString("JOB_TYPE");
                    String JobName = jsonObject1.getString("JobName");
                    String Status = jsonObject1.getString("Status");
                    String compname = jsonObject1.getString("compname");
                    String JOB_ID_PK = jsonObject1.getString("JOB_ID_PK");
                    dialog_SHOW_Info(CompanyId, JOB_ID_PK, compname, JobName, JOB_TYPE, txt_Job, Status, ShowName);
                }
            } catch (Exception e) {
                e.getMessage();
            }


        }
    }

}
