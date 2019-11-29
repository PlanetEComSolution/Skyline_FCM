package planet.info.skyline.client;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.adapter.CompanyNameAdapter;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.Job;
import planet.info.skyline.model.Myspinner_timezone;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_BindJob;
import static planet.info.skyline.network.Api.API_ShowClientDashStatus;
import static planet.info.skyline.network.Api.API_ShowStatus;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class DashboardActivity extends AppCompatActivity {

    TextView tv_Approved, tv_Snoozed, tv_Pending, tv_Rejected,
            tv_Client_Order_Paused, tv_Awaiting_Coordinator_Approval, tv_Approved_Orders;
    LinearLayout ll_Approved, ll_Snoozed, ll_Pending, ll_Rejected,
            ll_Client_Order_Paused, ll_Awaiting_Coordinator_Approval, ll_Approved_Orders;

    String Snoozed = "", Approved = "", YTBReviewed = "", Rejected = "";
    String ClientOrderPaused = "", AwaitingCoordinatorApproval = "", ApprovedOrder = "";

    String Client_id_Pk, comp_ID, jobID, job_Name, DealerId;
    SharedPreferences sp;
    ProgressDialog progressDoalog;

    List<String> job_Name_list_Desc_forIndex;
    List<String> job_Name_list_Desc = new ArrayList<String>();
    List<String> job_id_list = new ArrayList<String>();
    List<String> job_Name_list = new ArrayList<String>();
    AlertDialog alertDialog1;
    private Menu menu;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_dashboard);
        tv_Approved = findViewById(R.id.tv_Approved);
        tv_Snoozed = findViewById(R.id.tv_Snoozed);
        tv_Pending = findViewById(R.id.tv_Pending);
        tv_Rejected = findViewById(R.id.tv_Rejected);
        tv_Client_Order_Paused = findViewById(R.id.tv_Client_Order_Paused);
        tv_Awaiting_Coordinator_Approval = findViewById(R.id.tv_Awaiting_Coordinator_Approval);
        tv_Approved_Orders = findViewById(R.id.tv_Approved_Orders);

        ll_Approved = findViewById(R.id.ll_Approved);
        ll_Snoozed = findViewById(R.id.ll_Snoozed);
        ll_Pending = findViewById(R.id.ll_Pending);
        ll_Rejected = findViewById(R.id.ll_Rejected);
        ll_Client_Order_Paused = findViewById(R.id.ll_Client_Order_Paused);
        ll_Awaiting_Coordinator_Approval = findViewById(R.id.ll_Awaiting_Coordinator_Approval);
        ll_Approved_Orders = findViewById(R.id.ll_Approved_Orders);

        progressDoalog = new ProgressDialog(DashboardActivity.this);
        progressDoalog.setMessage(getResources().getString(R.string.Loading_text));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCancelable(false);

        setTitle(Utility.getTitle("Power Dashboard"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

        Client_id_Pk = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        comp_ID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");
        DealerId = sp.getString(Utility.CLIENT_LOGIN_DealerID, "");
        jobID = "-1"; //by default
        job_Name = getApplicationContext().getResources().getString(R.string.Select_Job);

        Log.e("comp_ID", comp_ID);
        Log.e("jobID", jobID);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiDashboardDataEp2();

            }
        });

        /**/
        callApiDashboardDataEp2();
        /**/
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void callApiDashboardDataEp2() {


        if (new ConnectionDetector(DashboardActivity.this).isConnectingToInternet()) {
            new async_getClientDashboardData().execute();

        } else {
            Toast.makeText(DashboardActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }


    }

    private void setTextData() {

        if (Approved != null && !Approved.equals("")) {
            tv_Approved.setText(Approved);
        } else {
            tv_Approved.setText("0");
        }


        if (Snoozed != null && !Snoozed.equals("")) {
            tv_Snoozed.setText(Snoozed);
        } else {
            tv_Snoozed.setText("0");
        }

        if (YTBReviewed != null && !YTBReviewed.equals("")) {
            tv_Pending.setText(YTBReviewed);
        } else {
            tv_Pending.setText("0");
        }

        if (Rejected != null && !Rejected.equals("")) {
            tv_Rejected.setText(Rejected);
        } else {
            tv_Rejected.setText("0");
        }


        if (ClientOrderPaused != null && !ClientOrderPaused.equals("")) {
            tv_Client_Order_Paused.setText(ClientOrderPaused);
        } else {
            tv_Client_Order_Paused.setText("0");
        }

        if (AwaitingCoordinatorApproval != null && !AwaitingCoordinatorApproval.equals("")) {
            tv_Awaiting_Coordinator_Approval.setText(AwaitingCoordinatorApproval);
        } else {
            tv_Awaiting_Coordinator_Approval.setText("0");
        }

        if (ApprovedOrder != null && !ApprovedOrder.equals("")) {
            tv_Approved_Orders.setText(ApprovedOrder);
        } else {
            tv_Approved_Orders.setText("0");
        }

        updateMenuTitles();

        ll_Approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP2.class);
                intent.putExtra("status", "Approved");
                intent.putExtra("job", jobID);
                startActivity(intent);

            }
        });

        ll_Snoozed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP2.class);
                intent.putExtra("status", "Snoozed");
                intent.putExtra("job", jobID);
                startActivity(intent);
            }
        });
        ll_Pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP2.class);
                intent.putExtra("status", "Yet To Be Reviewed");
                intent.putExtra("job", jobID);
                startActivity(intent);
            }
        });

        ll_Rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP2.class);
                intent.putExtra("status", "Rejected");
                intent.putExtra("job", jobID);
                startActivity(intent);
            }
        });

        ll_Client_Order_Paused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP1.class);
                intent.putExtra("status", "1");
                startActivity(intent);
            }
        });
        ll_Awaiting_Coordinator_Approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP1.class);
                intent.putExtra("status", "2");
                startActivity(intent);
            }
        });
        ll_Approved_Orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DashboardDetailActivity_EP1.class);
                intent.putExtra("status", "3");
                startActivity(intent);
            }
        });


    }


    public void fun_client_dashboard() {


        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        ;
        final String SOAP_ACTION = KEY_NAMESPACE + API_ShowClientDashStatus;
        final String METHOD_NAME = API_ShowClientDashStatus;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Client_id_Pk", Client_id_Pk);
        request.addProperty("comp_ID", comp_ID);
        request.addProperty("jobID", jobID);

        //request.addProperty("Connection", "Close");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        httpTransport.debug = true;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            //nks
            Log.d("HTTP REQUEST ", httpTransport.requestDump);
            Log.d("HTTP RESPONSE", httpTransport.responseDump);
            Object results = (Object) envelope.getResponse();

            String resultstring = results.toString();
            //{"cds":[{"Snoozed":"0","Approved":"0","YTBReviewed":"0","Rejected":"0"}]}
            JSONObject jsonObject = new JSONObject(resultstring);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Snoozed = jsonObject1.getString("Snoozed");
                    Approved = jsonObject1.getString("Approved");
                    YTBReviewed = jsonObject1.getString("YTBReviewed");
                    Rejected = jsonObject1.getString("Rejected");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void fun_client_dashboard_2() {


        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        ;
        final String SOAP_ACTION = KEY_NAMESPACE + API_ShowStatus;
        final String METHOD_NAME = API_ShowStatus;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Str_jobId", jobID);
        request.addProperty("dealerID", DealerId);

        //request.addProperty("Connection", "Close");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        httpTransport.debug = true;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            //nks
            Log.d("HTTP REQUEST ", httpTransport.requestDump);
            Log.d("HTTP RESPONSE", httpTransport.responseDump);
            Object results = (Object) envelope.getResponse();

            String resultstring = results.toString();
            //{"cds":[{"Snoozed":"0","Approved":"0","YTBReviewed":"0","Rejected":"0"}]}
            JSONObject jsonObject = new JSONObject(resultstring);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Snoozed = jsonObject1.getString("Snoozed");
                    Approved = jsonObject1.getString("Approved");
                    YTBReviewed = jsonObject1.getString("YTBReviewed");
                    Rejected = jsonObject1.getString("Rejected");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getDahsboardDataEP1() {
        showProgressDialog();
        String client = comp_ID;
        String user = Client_id_Pk;
        String dealer = sp.getString(Utility.CLIENT_LOGIN_DealerID, "");
        String URL = SOAP_API_Client.URL_EP1 + Api.API_GET_SERVICE_ORDER_COUNT + client + "&user=" + user + "&dealer=" + dealer;

        JsonObjectRequest bb = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                hideProgressDialog();

                try {
                    JSONArray jsonArray = obj.getJSONArray("data");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    try {
                        JSONArray jsonArray11 = jsonObject.getJSONArray("pending");
                        JSONObject jsonObject1 = jsonArray11.getJSONObject(0);
                        ClientOrderPaused = jsonObject1.getString("cpending");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    try {
                        JSONArray jsonArray12 = jsonObject.getJSONArray("waiting");
                        JSONObject jsonObject2 = jsonArray12.getJSONObject(0);
                        AwaitingCoordinatorApproval = jsonObject2.getString("waiting");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    try {
                        JSONArray jsonArray13 = jsonObject.getJSONArray("approved");
                        JSONObject jsonObject3 = jsonArray13.getJSONObject(0);
                        ApprovedOrder = jsonObject3.getString("approved");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                } catch (Exception e) {
                    e.getMessage();
                }
                setTextData();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                try {
                    hideProgressDialog();
                    Toast.makeText(DashboardActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.spinner:

                if (new ConnectionDetector(DashboardActivity.this).isConnectingToInternet()) {
                    new get_company_job_id().execute();
                } else {
                    Toast.makeText(DashboardActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dash_client, menu);
        // _menu = menu;
        this.menu = menu;
        return true;
    }


    public void Getcompany_job_id()   ///by aman kaushik
    {


        ArrayList<Job> list_job = new ArrayList<>();
        job_id_list.clear();
        job_Name_list_Desc.clear();//nks
        job_Name_list.clear();

        /**/
        job_Name_list_Desc.add(getResources().getString(R.string.Select_Job));
        job_id_list.add("-1");
        job_Name_list.add(getResources().getString(R.string.Select_Job));
        /**/


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_BindJob;
        final String METHOD_NAME = API_BindJob;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("ClientID", comp_ID);

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

            } else {

                String json = recved.substring(recved.indexOf("=") + 1, recved.lastIndexOf(";"));

                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String comapny_id = jsonObject1.getString("JOB_ID_PK");
                    String company_name = jsonObject1.getString("JobName");
                    String job_descripition = jsonObject1.getString("txt_Job");


                    String status = jsonObject1.getString("Status");
                    String show = jsonObject1.getString("ShowName");
                    String jobtype = jsonObject1.getString("JOB_TYPE");


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

                    /*******for sorting*/
                    list_job.add(new Job(comapny_id, company_name, total_desc));

                    /*******************/

//                    job_id_list.add(comapny_id);
//                    job_Name_list.add(company_name);
//                    job_Name_list_Desc.add(total_desc);

                }

                Collections.sort(list_job, new Comparator<Job>() {
                    @Override
                    public int compare(Job o1, Job o2) {
                        return o1.getJobName().compareTo(o2.getJobName());
                    }
                });

                for (int i = 0; i < list_job.size(); i++) {
                    Job job = list_job.get(i);
                    job_id_list.add(job.getJobID());
                    job_Name_list.add(job.getJobName());
                    job_Name_list_Desc.add(job.getJobDesc());
                }

                job_Name_list_Desc_forIndex = new ArrayList<>(job_Name_list_Desc);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dialog_select_job()    /////by aman kaushik
    {

        final Dialog dialog_companyName = new Dialog(DashboardActivity.this);
        dialog_companyName.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_companyName.setContentView(R.layout.select_job);
        dialog_companyName.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_companyName.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog_companyName.setCancelable(true);

        ImageView closebtn = (ImageView) dialog_companyName.findViewById(R.id.close);

        final AutoCompleteTextView autocomplete_job_name = (AutoCompleteTextView) dialog_companyName.findViewById(R.id.job);
        Button btn_GO = (Button) dialog_companyName.findViewById(R.id.go_button);
        btn_GO.setVisibility(View.GONE);
        dialog_companyName.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final LinearLayout ll_arrow = (LinearLayout) dialog_companyName.findViewById(R.id.ll_arrow);
        final LinearLayout ll_clear = (LinearLayout) dialog_companyName.findViewById(R.id.ll_clear);
        ll_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocomplete_job_name.showDropDown();
            }
        });
        ll_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocomplete_job_name.setText("");
            }
        });


        CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(DashboardActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
        autocomplete_job_name.setAdapter(jobDescAdapter);
        autocomplete_job_name.setDropDownHeight(550);
        autocomplete_job_name.setThreshold(1);
        //  autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

        //ontouch for job name---->
        autocomplete_job_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                autocomplete_job_name.showDropDown();
                /*to clear autocomplete*/
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;
//                try {
//                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                        if (motionEvent.getRawX() >= (autocomplete_job_name.getRight() - autocomplete_job_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                            // your action here
//                            autocomplete_job_name.setText("");
//                            return true;
//                        }
//                    }
//                } catch (Exception e) {
//                }
                /**/

                return false;
            }
        });


        autocomplete_job_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String job_txt = autocomplete_job_name.getText().toString();

                if (!job_txt.equals(getResources().getString(R.string.Select_Job))) {
                    job_txt = job_txt.substring(0, job_txt.indexOf("\n"));
                }


                int index = job_Name_list.indexOf(job_txt);
                jobID = job_id_list.get(index);
                job_Name = job_Name_list.get(index);
                autocomplete_job_name.setText(job_Name);
                Log.e("jobId", jobID);
                Log.e("job_Name", job_Name);

                try {
                    dialog_companyName.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                callApiDashboardDataEp2();

            }
        });

        autocomplete_job_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (autocomplete_job_name.getText().length() == 0) {
                        // autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        //refresh adapter
                        job_Name_list_Desc = new ArrayList<>(job_Name_list_Desc_forIndex);
                        CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(DashboardActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
                        autocomplete_job_name.setAdapter(jobDescAdapter);
                        autocomplete_job_name.setDropDownHeight(550);

                        ll_arrow.setVisibility(View.VISIBLE);
                        ll_clear.setVisibility(View.GONE);

                    } else {
                        // autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                        ll_arrow.setVisibility(View.GONE);
                        ll_clear.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }


        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_companyName.dismiss();
            }
        });

        btn_GO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!job_Name_list.contains(autocomplete_job_name.getText().toString().trim())) {
                    Toast.makeText(DashboardActivity.this, "Kindly enter a valid job name!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        dialog_companyName.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    callApiDashboardDataEp2();
                }

            }
        });
        dialog_companyName.show();

    }

    public void showJobSearchDialogNew(String dialog_title, final Context context) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText autoText_TimeZone = (EditText) dialogView.findViewById(R.id.autoText_TimeZone);
        final ListView listvw = (ListView) dialogView.findViewById(R.id.listview);

        final ArrayList<Myspinner_timezone> list_temp = new ArrayList<>();
        final ArrayList<Myspinner_timezone> list = new ArrayList<>();

        for (int i = 0; i < job_Name_list_Desc.size(); i++) {
            Myspinner_timezone myspinner_timezone = new Myspinner_timezone(job_Name_list_Desc.get(i), "", "");
            list_temp.add(myspinner_timezone);
        }

        list.addAll(list_temp);

        ArrayAdapter<Myspinner_timezone> adapter = new ArrayAdapter<Myspinner_timezone>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, list_temp);
        listvw.setAdapter(adapter);


        listvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                Myspinner_timezone spinner_ = (Myspinner_timezone) listvw.getItemAtPosition(position);
                String job_txt = spinner_.getSpinnerText();

                if (!job_txt.equals(getResources().getString(R.string.Select_Job))) {
                    job_txt = job_txt.substring(0, job_txt.indexOf("\n"));
                }

                int index = job_Name_list.indexOf(job_txt);
                jobID = job_id_list.get(index);
                job_Name = job_Name_list.get(index);
                autoText_TimeZone.setText(job_Name);
                Log.e("jobId", jobID);
                Log.e("job_Name", job_Name);

                try {
                    alertDialog1.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
                callApiDashboardDataEp2();


            }
        });


        autoText_TimeZone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                /*to clear autocomplete*/
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (autoText_TimeZone.getRight() - autoText_TimeZone.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            autoText_TimeZone.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }


                return false;
            }
        });

        autoText_TimeZone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = autoText_TimeZone.getText().toString().trim().toLowerCase();
                if (autoText_TimeZone.getText().length() == 0) {
                    autoText_TimeZone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                    ArrayAdapter<Myspinner_timezone> adapter = new ArrayAdapter<Myspinner_timezone>(context,
                            android.R.layout.simple_list_item_1, android.R.id.text1, list);
                    listvw.setAdapter(adapter);


                } else {
                    autoText_TimeZone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                    list_temp.clear();

                    for (int i = 0; i < list.size(); i++) {
                        String listText = list.get(i).getSpinnerText().toLowerCase();
                        if (listText.contains(text.toLowerCase())) {
                            list_temp.add(list.get(i));
                        }
                    }

                    ArrayAdapter<Myspinner_timezone> adapter = new ArrayAdapter<Myspinner_timezone>(context,
                            android.R.layout.simple_list_item_1, android.R.id.text1, list_temp);
                    listvw.setAdapter(adapter);

                }
            }
        });


        ///// new work


        dialogBuilder.setTitle(dialog_title);
        alertDialog1 = dialogBuilder.create();
        alertDialog1.show();
    }

    private void updateMenuTitles() {
        MenuItem bedMenuItem = menu.findItem(R.id.spinner);

        bedMenuItem.setTitle(job_Name);

    }

    public void showProgressDialog() {

        if (!progressDoalog.isShowing()) {
            progressDoalog.show();
        }
    }

    public void hideProgressDialog() {
        try {
            if (progressDoalog != null) {
                progressDoalog.cancel();
            }
            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
        } catch (Exception e) {
            e.getCause();
        }
    }

    public class async_getClientDashboardData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            showProgressDialog();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
          //  hideProgressDialog();
            setTextData();
            if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                getDahsboardDataEP1();
            } else {
                Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            if (jobID.equalsIgnoreCase("-1")) {
                fun_client_dashboard();
            } else {
                fun_client_dashboard_2();
            }
            return null;
        }

    }

    private class get_company_job_id extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Getcompany_job_id();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
            // dialog_select_job();

            showJobSearchDialogNew("Job(s)", DashboardActivity.this);

        }
    }

}
