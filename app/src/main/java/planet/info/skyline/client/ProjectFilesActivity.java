package planet.info.skyline.client;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.adapter.CompanyNameAdapter;
import planet.info.skyline.crash_report.ConnectionDetector;
//import planet.info.skyline.httpimage.HttpImageManager;
import planet.info.skyline.model.Job;
import planet.info.skyline.model.Myspinner_timezone;
import planet.info.skyline.model.ProjectPhoto;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_BindJob;
import static planet.info.skyline.network.Api.API_GetProjectFileforClient;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class ProjectFilesActivity extends AppCompatActivity {

    Context context;
    TextView tv_msg;
    ArrayList<ProjectPhoto> list_ProjectPhotos = new ArrayList<>();
    SharedPreferences sp;
    String Client_id_Pk, comp_ID, jobID, job_Name, dealerId, Agency;
    List<String> job_Name_list_Desc_forIndex;
    List<String> job_Name_list_Desc = new ArrayList<String>();
    List<String> job_id_list = new ArrayList<String>();
    List<String> job_Name_list = new ArrayList<String>();
   // SwipeRefreshLayout mSwipeRefreshLayout;
    private Menu menu;

    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    AlertDialog alertDialog1;

    SwipeRefreshLayout pullToRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_files);

      //  mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
     //   mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        tv_msg = findViewById(R.id.tv_msg);
        context = ProjectFilesActivity.this;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setTitle(Utility.getTitle("Project File(s)"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

        /**************/
        Client_id_Pk = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        comp_ID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");
        jobID = "-1"; //by default
        Agency = "0";// by default
        job_Name = getApplicationContext().getResources().getString(R.string.Select_Job);
        dealerId = sp.getString(Utility.CLIENT_LOGIN_DealerID, "");
        /***************/


        /****************/


        /**/



        /**/
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiProjectPhotos();
                // your code

            }
        });
        // mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callApiProjectPhotos();
        // mSwipeRefreshLayout.setRefreshing(true);
    }

    private void callApiProjectPhotos() {
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new Async_ProjectPhotos().execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }

    public void Getcompany_job_id()   ///by aman kaushik
    {
        ArrayList<Job> list_job = new ArrayList<>();
        job_id_list.clear();
        job_Name_list_Desc.clear();//nks
        job_Name_list.clear();

        /**/
        job_Name_list_Desc.add(getResources().getString(R.string.Select_Job) );
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
    public void getProjectPhotos() {
        list_ProjectPhotos.clear();
        ArrayList<ProjectPhoto> yetToBeReviewd = new ArrayList<>();
        ArrayList<ProjectPhoto> snoozed = new ArrayList<>();
        ArrayList<ProjectPhoto> Approved = new ArrayList<>();
        ArrayList<ProjectPhoto> Rejected = new ArrayList<>();

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetProjectFileforClient;
        final String METHOD_NAME = API_GetProjectFileforClient;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Job_id", jobID);
        request.addProperty("id_pk", "");
        request.addProperty("file_status", "");
        request.addProperty("Dealer_ID", dealerId);
        request.addProperty("clientID", comp_ID);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArray = jsonObject.getJSONArray("cds");
            for (int i = 0; i < jsonArray.length(); i++) {
                try {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    int nu_comment_id = jsonObject1.getInt("degree");
                    int ddl_job_status = jsonObject1.getInt("status");
                    int cl = 0;

                    String dt = jsonObject1.getString("FileType");
                    String job = jsonObject1.getString("txt_job");
                    String fsize = "";
                    String latestcom1 = jsonObject1.getString("iconLink");
                    int job_id = jsonObject1.getInt("Job_id");
                    String actiondate = jsonObject1.getString("LastActionDate");

                    String createdate = jsonObject1.getString("CreatedDate");
                    int nu_approveedit_by_client =0;
                    String latestcom = jsonObject1.getString("UploadbyName");
                    int fileid = jsonObject1.getInt("id_pk");

                    int jid = jsonObject1.getInt("Job_id");
                    int View_status_for_client = 0;
                    int nu_client_id = jsonObject1.getInt("clientID");
                    int nu_reject_by_client = jsonObject1.getInt("uploadedBy");
                    String snoozDate = "";

                    int snooz = 0;
                    int nu_approve_by_client = 0;
                    int INT_FID = jsonObject1.getInt("id_pk");

                    int INT_FileID =  jsonObject1.getInt("id_pk");
                    String VCHAR_Heading = jsonObject1.getString("GoogleID");
                    String FILE_NAME_question = jsonObject1.getString("GoogleID");

                    String FILE_NAME = jsonObject1.getString("OriginalFilename");
                    String Action_statusOLD = jsonObject1.getString("txt_C_Name");
                    String Action_status = jsonObject1.getString("Client_Status");
                    String Descr = jsonObject1.getString("Description");
                    String file_heading ="";
                    int id = 0;
                    String ImgName ="";




                    ProjectPhoto projectPhoto = new ProjectPhoto(
                            nu_comment_id, ddl_job_status,
                            cl, dt, job, fsize,
                            latestcom1, job_id, actiondate, createdate,

                            nu_approveedit_by_client, latestcom, fileid, jid,
                            View_status_for_client, nu_client_id, nu_reject_by_client,
                            snoozDate, snooz, nu_approve_by_client,

                            INT_FID, INT_FileID, VCHAR_Heading,

                            FILE_NAME_question, FILE_NAME,

                            Action_statusOLD, Action_status,

                            Descr, file_heading,
                            id, ImgName);


               //     list_ProjectPhotos.add(projectPhoto);
                    /**************************Ordering********************************/
                    if (Action_status.equalsIgnoreCase("Rejected")) {
                        Rejected.add(projectPhoto);
                    } else if (Action_status.equalsIgnoreCase("Approved")) {
                        Approved.add(projectPhoto);
                    } else if (Action_status.equalsIgnoreCase("Snoozed")) {
                        snoozed.add(projectPhoto);
                    } else {
                        yetToBeReviewd.add(projectPhoto);
                    }

                } catch (Exception e) {
                    e.getMessage();
                }

            }
            list_ProjectPhotos.addAll(yetToBeReviewd);
            list_ProjectPhotos.addAll(snoozed);
            list_ProjectPhotos.addAll(Approved);
            list_ProjectPhotos.addAll(Rejected);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateMenuTitles() {
        MenuItem bedMenuItem = menu.findItem(R.id.spinner);

        bedMenuItem.setTitle(job_Name);

    }

    public void dialog_select_job()    /////by aman kaushik
    {

        final Dialog dialog_companyName = new Dialog(ProjectFilesActivity.this);
        dialog_companyName.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_companyName.setContentView(R.layout.select_job);
        dialog_companyName.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_companyName.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog_companyName.setCancelable(true);
        ImageView closebtn = (ImageView) dialog_companyName.findViewById(R.id.close);

        final AutoCompleteTextView autocomplete_job_name = (AutoCompleteTextView) dialog_companyName.findViewById(R.id.job);
        Button btn_GO = (Button) dialog_companyName.findViewById(R.id.go_button);
        dialog_companyName.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btn_GO.setVisibility(View.GONE);

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



        CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(ProjectFilesActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
        autocomplete_job_name.setAdapter(jobDescAdapter);
        autocomplete_job_name.setDropDownHeight(550);
        autocomplete_job_name.setThreshold(1);

      //  autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

        //ontouch for job name---->
        autocomplete_job_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                autocomplete_job_name.showDropDown();

                return false;
            }
        });


        autocomplete_job_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String job_txt = autocomplete_job_name.getText().toString();

                if(! job_txt.equals(getResources().getString(R.string.Select_Job))){
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
                callApiProjectPhotos();

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
                        // autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                       // autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        ll_arrow.setVisibility(View.VISIBLE);
                        ll_clear.setVisibility(View.GONE);

                        //refresh adapter
                        job_Name_list_Desc = new ArrayList<>(job_Name_list_Desc_forIndex);
                        CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(ProjectFilesActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
                        autocomplete_job_name.setAdapter(jobDescAdapter);
                        autocomplete_job_name.setDropDownHeight(550);

                    } else {
                      //  autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);

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
                    Toast.makeText(ProjectFilesActivity.this, "Kindly enter a valid job name!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        dialog_companyName.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    callApiProjectPhotos();
                }

            }
        });


        dialog_companyName.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.spinner:

                if (new ConnectionDetector(ProjectFilesActivity.this).isConnectingToInternet()) {
                    new get_company_job_id().execute();
                } else {
                    Toast.makeText(ProjectFilesActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
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
    private class Async_ProjectPhotos extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDoalog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDoalog = new ProgressDialog(context);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getProjectPhotos();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();

          //  mSwipeRefreshLayout.setRefreshing(false);

           // order_adapter adapter = null;
            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }


            if (list_ProjectPhotos.size() < 1) {
                tv_msg.setVisibility(View.VISIBLE);

            } else {
                tv_msg.setVisibility(View.GONE);
            }

            updateMenuTitles();


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new MoviesAdapter(ProjectFilesActivity.this,list_ProjectPhotos);
            recyclerView.setAdapter(mAdapter);

        }
    }
    private class get_company_job_id extends AsyncTask<String, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(ProjectFilesActivity.this);

        @Override
        protected Void doInBackground(String... strings) {
            Getcompany_job_id();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  ringProgressDialog.setTitle("Kindly wait ...")
            ringProgressDialog.setMessage(getString(R.string.Loading_text));
            ringProgressDialog.setCancelable(false);
            ringProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                ringProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
           // dialog_select_job();
            showJobSearchDialogNew("Job(s)", ProjectFilesActivity.this);
        }
    }
    public void showJobSearchDialogNew(String dialog_title, final Context context) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ProjectFilesActivity.this.getLayoutInflater();
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
                callApiProjectPhotos();


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
        if(!alertDialog1.isShowing()) {
            alertDialog1.show();
        }
    }
    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private List<ProjectPhoto> moviesList;
     //   private HttpImageManager mHttpImageManager;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_file_name, tv_job_name, tv_dated, tv_status;
            ImageView thumbnail;
            Button index_no;
            LinearLayout parentView;
            ProgressBar spinner;

            public MyViewHolder(View view) {
                super(view);

                index_no = (Button) view.findViewById(R.id.serial_no);
               thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
               tv_file_name = (TextView) view.findViewById(R.id.tv_file_name);
               tv_job_name = (TextView)view. findViewById(R.id.tv_job_name);
               tv_dated = (TextView)view. findViewById(R.id.tv_dated);
               tv_status = (TextView) view.findViewById(R.id.tv_status);
                parentView = (LinearLayout) view.findViewById(R.id.parentView);
                spinner= (ProgressBar) view.findViewById(R.id.progressBar1);
            }
        }


        public MoviesAdapter(Activity context,List<ProjectPhoto> moviesList) {
            this.moviesList = moviesList;
        //    mHttpImageManager = ((AppController) context.getApplication()).getHttpImageManager();

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_client_project_photos, parent, false);









            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            ProjectPhoto projectPhoto = moviesList.get(position);

            holder.index_no.setText(String.valueOf(position + 1));



            final String FileName = projectPhoto.getFILENAME();

            final String Descr = projectPhoto.getDescr();
            final String job = projectPhoto.getJob();
             String createdate = projectPhoto.getCreatedate();
            final String Action_status = projectPhoto.getActionStatus();
            final String ImgName = projectPhoto.getImgName();
            final String FileId = projectPhoto.getINTFileID() + "";
            final String googleId = projectPhoto.getVCHARHeading() + "";

            String fileExt="";
            if (FileName.contains(".")) {
                fileExt = FileName.substring(FileName.lastIndexOf("."));
            }



            boolean isImage = Arrays.asList(Utility.imgExt).contains(fileExt);
            boolean isDoc = Arrays.asList(Utility.docExt).contains(fileExt);
            boolean isMedia = Arrays.asList(Utility.mediaExt).contains(fileExt);
            boolean isWord = Arrays.asList(Utility.wordExt).contains(fileExt);
            boolean isPdf = Arrays.asList(Utility.pdfExt).contains(fileExt);
            boolean  isExcel = Arrays.asList(Utility.excelExt).contains(fileExt);
            boolean  isText = Arrays.asList(Utility.txtExt).contains(fileExt);
            holder.spinner.setVisibility(View.VISIBLE);
            if (isImage) {
                String url = "https://drive.google.com/thumbnail?id=" + googleId;

//                ImageView imageView = holder.thumbnail;
//                // imageView.setImageResource(R.drawable.ic_launcher);
//                Uri myUri = Uri.parse(url);
//
//                if (projectPhoto != null) {
//                    Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(myUri, imageView));
//                    if (bitmap != null) {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                }


                Glide.with(ProjectFilesActivity.this).load(url).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.spinner.setVisibility(View.GONE);

                        holder.thumbnail.setImageResource(R.drawable.no_image);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.  spinner.setVisibility(View.GONE);

                        return false;
                    }
                })
                        .into(holder.thumbnail);





            } /*else if (isDoc) {
                holder. spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.doc);





            }*/

            else if (isWord) {
                holder. spinner.setVisibility(View.GONE);
                holder.  thumbnail.setImageResource(R.drawable.doc);
            } else if (isPdf) {
                holder. spinner.setVisibility(View.GONE);
                holder.  thumbnail.setImageResource(R.drawable.pdf);
            } else if (isExcel) {
                holder.  spinner.setVisibility(View.GONE);
                holder. thumbnail.setImageResource(R.drawable.excel);
            } else if (isText) {
                holder.   spinner.setVisibility(View.GONE);
                holder.   thumbnail.setImageResource(R.drawable.txt_file_icon);
            }

            else if (isMedia) {
                holder. spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.media);
            }
            else{
                holder. spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.no_image);
            }







            if (FileName == null || FileName.trim().equals("")) {
                holder.tv_file_name.setText("Not available");
            } else {
                holder.tv_file_name.setText(FileName);
            }
            if (job == null || job.trim().equals("")) {
                holder.tv_job_name.setText("Not available");
            } else {
                holder.tv_job_name.setText(job);
            }

            if (createdate == null || createdate.trim().equals("")) {
                holder.tv_dated.setText("Not available");
            } else {
                if(createdate.contains("-")){
                    createdate=createdate.replaceAll("-","/");
                }
                holder.tv_dated.setText(createdate);
            }
            if (Action_status == null || Action_status.trim().equals("")) {
                holder.tv_status.setText("Not available");
            } else {
                holder.tv_status.setText(Action_status);
                if (Action_status.equalsIgnoreCase("Rejected")) {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.red));
                } else if (Action_status.equalsIgnoreCase("Approved")) {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.main_green_color));
                } else if (Action_status.equalsIgnoreCase("Snoozed")) {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.snoozed));
                } else {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.main_orange_light_color));

                }

            }

            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProjectFileDetailActivity.class);
                    // intent.putExtra("obj", projectPhoto);
                    intent.putExtra("FileId", FileId);
                    intent.putExtra("FileName", FileName);
                    intent.putExtra("googleId", googleId);
                    intent.putExtra("jobID", jobID);
                    startActivity(intent);
                }
            });




        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }

}

