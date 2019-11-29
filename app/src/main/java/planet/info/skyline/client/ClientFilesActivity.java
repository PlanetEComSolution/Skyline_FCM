package planet.info.skyline.client;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
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
import android.widget.BaseAdapter;
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
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import planet.info.skyline.network.REST_API_Client;
import planet.info.skyline.old_activity.AppConstants;
import planet.info.skyline.old_activity.CustomMultiPartEntity;
import planet.info.skyline.R;
import planet.info.skyline.adapter.CompanyNameAdapter;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.Job;
import planet.info.skyline.model.Myspinner_timezone;
import planet.info.skyline.model.ProjectPhoto;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.ProgressRequestBody;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.CameraUtils;
import planet.info.skyline.util.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static planet.info.skyline.network.Api.API_AllRecentlyUuploadedFiles;
import static planet.info.skyline.network.Api.API_BindJob;
import static planet.info.skyline.network.Api.API_saveclientFileByClient;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

//import com.aditya.filebrowser.Constants;
//import com.aditya.filebrowser.FileChooser;

public class ClientFilesActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static String imageStoragePath;
    Context context;
    TextView tv_msg;
    ArrayList<ProjectPhoto> list_ProjectPhotos = new ArrayList<>();
    SharedPreferences sp;
    String Client_id_Pk, comp_ID, jobID, job_Name, dealerId, Agency, loginUserName;
    List<String> job_Name_list_Desc_forIndex;
    List<String> job_Name_list_Desc = new ArrayList<String>();
    List<String> job_id_list = new ArrayList<String>();
    List<String> job_Name_list = new ArrayList<String>();
    File file1;
    /**/
    AlertDialog alertDialog;
    Uri mImageCaptureUri;
    String path, fname = "";
    ArrayList<String> list_path = new ArrayList<>();
    ArrayList<String> list_TempImagePath = new ArrayList<>();
    ArrayList<HashMap<String, String>> list_ImageDescData = new ArrayList<>();
    ArrayList<String> list_imageSize = new ArrayList<>();
    ArrayList<String> list_UploadImageName = new ArrayList<>();
    //ArrayList<HashMap<String, String>> list_UploadedImageID = new ArrayList<>();
    ExpandableHeightListView listvw_images;
    LvAdapter adpter;
    long totalSize = 0;
    Dialog dialog_image;
    int FILE_REQUEST_CODE = 10;
    int PICK_FILE_REQUEST = 11;
    AlertDialog alertDialog1;
    /**/
    SwipeRefreshLayout pullToRefresh;
    //  SwipeRefreshLayout mSwipeRefreshLayout;
    private Menu menu;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    ProgressDialog uploadProgressDialog;
    int Count_Image_Uploaded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_files);

        // mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        // mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        tv_msg = findViewById(R.id.tv_msg);
        context = ClientFilesActivity.this;


        setTitle(Utility.getTitle("Client File(s)"));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

        /**************/
        Client_id_Pk = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        comp_ID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");
        jobID = "-1"; //by default
        Agency = "0";// by default
        job_Name = getApplicationContext().getResources().getString(R.string.Select_Job);
        dealerId = sp.getString(Utility.CLIENT_LOGIN_DealerID, "");
        loginUserName = sp.getString(Utility.CLIENT_LOGIN_UserName, "");

        /***************/


        /****************/


        /**/
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                callApiProjectPhotos();
//            }
//        });
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
        job_Name_list_Desc.add(getResources().getString(R.string.Select_Job));
        job_id_list.add("-1");
        job_Name_list.add(getResources().getString(R.string.Select_Job));
        /**/


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_BindJob;
        final String METHOD_NAME =API_BindJob;
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



    public void getProjectPhotos2() {
        list_ProjectPhotos.clear();
        ArrayList<ProjectPhoto> yetToBeReviewd = new ArrayList<>();
        ArrayList<ProjectPhoto> snoozed = new ArrayList<>();
        ArrayList<ProjectPhoto> Approved = new ArrayList<>();
        ArrayList<ProjectPhoto> Rejected = new ArrayList<>();


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_AllRecentlyUuploadedFiles;
        final String METHOD_NAME = API_AllRecentlyUuploadedFiles;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("clientUserID", Client_id_Pk);//
        request.addProperty("jobID", jobID);//
        request.addProperty("Agency", Agency);//
        request.addProperty("dealerID", dealerId);//


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

                    int nu_comment_id = jsonObject1.getInt("nu_comment_id_client");
                    int ddl_job_status = 0;//
                    int cl = 0;//

                    String dt = "";//
                    String job = jsonObject1.getString("txt_job");
                    String fsize = jsonObject1.getString("fsize");
                    String latestcom1 = "";//
                    int job_id = jsonObject1.getInt("jobid");
                    String actiondate = jsonObject1.getString("update_comment_date");//

                    String createdate = "";//
                    int nu_approveedit_by_client = 0;//
                    String latestcom = "";
                    int fileid = 0;//
                    int jid = 0;//
                    int View_status_for_client = 0;//
                    int nu_client_id = jsonObject1.getInt("clientId");

                    int nu_reject_by_client = 0;//
                    String snoozDate = "";//

                    int snooz = 0;//
                    int nu_approve_by_client = 0;//

                    int INT_FID = jsonObject1.getInt("INT_FileID_client");
                    int INT_FileID = jsonObject1.getInt("INT_FileID_client");
                    String VCHAR_Heading = "";//
                    String FILE_NAME_question = jsonObject1.getString("orgfilename");
                    String FILE_NAME = jsonObject1.getString("orgfilename");
                    String Action_statusOLD = "";//
                    String Action_status = "";//
                    String Descr = jsonObject1.getString("filename1");
                    String file_heading = "";
                    int id = jsonObject1.getInt("ID");
                    String ImgName = jsonObject1.getString("ImgName");


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


                    list_ProjectPhotos.add(projectPhoto);

                    /*  *//**************************Ordering********************************//*
                    if (Action_status.equalsIgnoreCase("Rejected")) {
                        Rejected.add(projectPhoto);
                    } else if (Action_status.equalsIgnoreCase("Approved")) {
                        Approved.add(projectPhoto);
                    } else if (Action_status.equalsIgnoreCase("Snoozed")) {
                        snoozed.add(projectPhoto);
                    } else {
                        yetToBeReviewd.add(projectPhoto);
                    }*/

                } catch (Exception e) {
                    e.getMessage();
                }

            }

          /*  list_ProjectPhotos.addAll(yetToBeReviewd);
            list_ProjectPhotos.addAll(snoozed);
            list_ProjectPhotos.addAll(Approved);
            list_ProjectPhotos.addAll(Rejected);
*/


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateMenuTitles() {
        try {
            MenuItem bedMenuItem = menu.findItem(R.id.spinner);
            bedMenuItem.setTitle(job_Name);
        } catch (Exception e) {
            e.getCause();
        }

    }

    public void dialog_select_job()
    {

        final Dialog dialog_companyName = new Dialog(ClientFilesActivity.this);
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


        CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(ClientFilesActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
        autocomplete_job_name.setAdapter(jobDescAdapter);
        autocomplete_job_name.setDropDownHeight(550);
        autocomplete_job_name.setThreshold(1);

        //    autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);


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
                        //  autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                        ll_arrow.setVisibility(View.VISIBLE);
                        ll_clear.setVisibility(View.GONE);
                        //refresh adapter
                        job_Name_list_Desc = new ArrayList<>(job_Name_list_Desc_forIndex);
                        CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(ClientFilesActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
                        autocomplete_job_name.setAdapter(jobDescAdapter);
                        autocomplete_job_name.setDropDownHeight(550);

                    } else {
                        ll_arrow.setVisibility(View.GONE);
                        ll_clear.setVisibility(View.VISIBLE);
                        //  autocomplete_job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
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
                    Toast.makeText(ClientFilesActivity.this, "Kindly enter a valid job name!", Toast.LENGTH_SHORT).show();
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

    public void showJobSearchDialogNew(String dialog_title, final Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ClientFilesActivity.this.getLayoutInflater();
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
        if (!alertDialog1.isShowing()) {
            alertDialog1.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.spinner:
                if (new ConnectionDetector(ClientFilesActivity.this).isConnectingToInternet()) {
                    new get_company_job_id().execute();
                } else {
                    Toast.makeText(ClientFilesActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.menu_upload:

                if (jobID.equals("-1")) {
                    Toast.makeText(ClientFilesActivity.this, "Please choose a job first!", Toast.LENGTH_SHORT).show();

                    if (new ConnectionDetector(ClientFilesActivity.this).isConnectingToInternet()) {
                        new get_company_job_id().execute();
                    } else {
                        Toast.makeText(ClientFilesActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }

                } else {
                    opendilogforattachfileandimage_custom();
                }

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_client_files, menu);
        // _menu = menu;
        this.menu = menu;
        return true;
    }

    public void opendilogforattachfileandimage_custom() {
        final Dialog dialog = new Dialog(ClientFilesActivity.this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.openattachmentdilog_new);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        //  dialog.setTitle("Attach");
        LinearLayout cameralayout = (LinearLayout) dialog
                .findViewById(R.id.cameralayout);
        LinearLayout gallarylayout = (LinearLayout) dialog
                .findViewById(R.id.gallarylayout);
        LinearLayout filelayout = (LinearLayout) dialog
                .findViewById(R.id.filelayout);
        filelayout.setVisibility(View.VISIBLE);
        ImageView crosse = (ImageView) dialog
                .findViewById(R.id.close);

        crosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });

        cameralayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

               /*
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "Exhibit Power");

                if (!folder.exists()) {
                    folder.mkdir();
                }
                File folder1 = new File(folder, "Camera");
                if (!folder1.exists()) {
                    folder1.mkdir();
                }

                String unique_id = Utility.getUniqueId();

                file1 = new File(folder1, unique_id + "_FromCamera.jpg");
                Uri mImageCaptureUri = FileProvider.getUriForFile(ClientFilesActivity.this, ClientFilesActivity.this.getApplicationContext().getPackageName() + ".provider", file1);
                try {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent,
                            AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }


            }
        });
        gallarylayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientFilesActivity.this, AlbumSelectActivity.class);
                intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 50);
                startActivityForResult(intent, AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);

                dialog.dismiss();

            }
        });
        filelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, AppConstants.ANY_TYPE_FILE_REQUEST_CODE);

//                try {
//                    Intent intent = new Intent(ClientFilesActivity.this, FilePickerActivity.class);
//                    startActivityForResult(intent, FILE_REQUEST_CODE);
//                } catch (Exception e) {
//                    e.getCause();
//                }
//
//                 ArrayList<MediaFile> mediaFiles = new ArrayList<>();
//                try {
//
//                Intent intent = new Intent(ClientFilesActivity.this, FilePickerActivity.class);
//                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
//                        .setCheckPermission(true)
//                        .setSelectedMediaFiles(mediaFiles)
//                        .setShowFiles(true)
//                        .setShowImages(false)
//                        .setShowVideos(false)
//                        .setMaxSelection(10)
//                        .build());
//                startActivityForResult(intent, FILE_REQUEST_CODE);
//                } catch (Exception e) {
//                    e.getCause();
//                }


//                try {
//                    Intent i2 = new Intent(getApplicationContext(), FileChooser.class);
//                    i2.putExtra(Constants.SELECTION_MODE,Constants.SELECTION_MODES.MULTIPLE_SELECTION.ordinal());
//                    startActivityForResult(i2,PICK_FILE_REQUEST);
//
//                } catch (Exception e) {
//                    e.getCause();
//                }

            }
        });

        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // onResume();
            if (requestCode == AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
                if (data != null) {

                    //The array list has the image paths of the selected images
                    ArrayList<Image> images = data.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                    for (int i = 0; i < images.size(); i++) {
                        list_path.add(images.get(i).path);
                    }
                    dialog_Image_Description();

                } else {

                    //	Uri mImageCaptureUri = Uri.fromFile(file1);
                    Uri mImageCaptureUri = FileProvider.getUriForFile(ClientFilesActivity.this, ClientFilesActivity.this.getApplicationContext().getPackageName() + ".provider", file1);

                    try {
                        path = getPath(mImageCaptureUri,
                                ClientFilesActivity.this); // from Gallery

                        list_path.add(path);
                        dialog_Image_Description();
                        //nks
                    } catch (Exception e) {
                        path = mImageCaptureUri.getPath();
                        list_path.add(path);
                        dialog_Image_Description();
                        //nks
                    }
                    String arr[] = path.split("/");
                    int i;
                }
            } else if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {


                path = imageStoragePath;
                ///change orientation of captured photo
                int orientation = Utility.getExifOrientation(path);
                try {
                    if (orientation == 90) {
                        list_TempImagePath.add(path);
                        Bitmap bmp = Utility.getRotatedBitmap(path, 90);
                        path = Utility.saveImage(bmp);
                    }
                } catch (Exception e) {
                    e.getCause();
                }
                ///change orientation
                list_path.add(path);
                dialog_Image_Description();



               /* if (data != null) {
                    mImageCaptureUri = data.getData();
                    Log.e("Camera URI---", mImageCaptureUri.toString());
                    try {
                        path = getPath(mImageCaptureUri, ClientFilesActivity.this);

                        Log.e("Camera Path---", path);
                        ///change orientation of captured photo
                        int orientation = Utility.getExifOrientation(path);
                        if (orientation == 90) {
                            list_TempImagePath.add(path);
                            Bitmap bmp = Utility.getRotatedBitmap(path, 90);
                            path = Utility.saveImage(bmp);
                        }
                        ///change orientation
                        list_path.add(path);
                        dialog_Image_Description();

                    } catch (Exception e) {
                        try {
                            path = mImageCaptureUri.getPath();
                            ///change orientation of captured photo
                            int orientation = Utility.getExifOrientation(path);
                            if (orientation == 90) {
                                list_TempImagePath.add(path);
                                Bitmap bmp = Utility.getRotatedBitmap(path, 90);
                                path = Utility.saveImage(bmp);
                            }
                            ///change orientation

                            list_path.add(path);
                            dialog_Image_Description();
                            //nks


                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        Log.i("checkot", e.toString());
                    }
                } else {

                    if (file1 == null) {
                        file1 = new File(Environment.getExternalStorageDirectory(), "FromCamera.jpg");
                    }


                    try {
                        //		path = getPath(mImageCaptureUri, Upload_image_and_cooment.this); // from Gallery
                        path = file1 + "";

                        ///change orientation of captured photo
                        int orientation = Utility.getExifOrientation(path);
                        if (orientation == 90) {
                            list_TempImagePath.add(path);
                            Bitmap bmp = Utility.getRotatedBitmap(path, 90);
                            path = Utility.saveImage(bmp);
                        }
                        ///change orientation
                        list_path.add(path);
                        dialog_Image_Description();
                        //nks

                    } catch (Exception e) {
                        //		path = mImageCaptureUri.getPath();
                        path = file1 + "";
                        list_path.add(path);
                        dialog_Image_Description();
                        //nks
                        Log.i("check i", e.toString());
                    }
                    try {
                        // new asyntaskupload().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // String arr[] = path.split("/");
                    // int i;
                }*/


            } else if (requestCode == AppConstants.ANY_TYPE_FILE_REQUEST_CODE) {
                Uri videoUri = data.getData();
                Log.e("File URI---", videoUri.toString());

                try {
                    path = Utility.getPath(this, videoUri);
                    if (path != null) {
                        Log.e("File Path---", path);
                        list_path.add(path);
                    } else {
                        Log.e("File Path---", null);
                        Toast.makeText(getApplicationContext(), "Unable to add this file!", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.getCause();
                }

                dialog_Image_Description();
            }
        } else {

            if (list_ImageDescData.size() > 0) {
                dialog_Image_Description();
            } else {
                opendilogforattachfileandimage_custom();
            }


        }

    }

    public void dialog_Image_Description() {

        dialog_Multiple_image_Desciption();

    }

    private void dialog_Multiple_image_Desciption() {

        dialog_image = new Dialog(ClientFilesActivity.this);
        dialog_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_image.setContentView(R.layout.dialog_photo_comment_list_1);
        try {
            dialog_image.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            e.getMessage();
        }
        dialog_image.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog_image.setCancelable(false);
        dialog_image.setCanceledOnTouchOutside(false);

        listvw_images = (ExpandableHeightListView) dialog_image.findViewById(R.id.list_multiple_photo_Comment);
        listvw_images.setItemsCanFocus(true);

        Button Btn_Submit = (Button) dialog_image.findViewById(R.id.Btn_Submit);
        Button Btn_AddMorePhotos = (Button) dialog_image.findViewById(R.id.Btn_AddMorePhotos);
        Button Btn_Back = (Button) dialog_image.findViewById(R.id.Btn_Back);


       // String clid = sp.getString("clientid", "");
        String clid = Shared_Preference.getLOGIN_USER_ID(this);
       // String name = sp.getString("tname", "");
        String name = Shared_Preference.getLOGIN_USERNAME(ClientFilesActivity.this);
        //nks


        for (int i = 0; i < list_path.size(); i++) {

            String PathImage = list_path.get(i);
            String pathName[] = PathImage.split("/");
            String image_size = "24 kb";
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put(Utility.KEY_Jobid_or_swoid, "");

            hmap.put(Utility.KEY_imagename, pathName[pathName.length - 1]);
            hmap.put(Utility.KEY_image_size, image_size);
            hmap.put(Utility.KEY_name, name);
            hmap.put(Utility.KEY_clid, clid);
            hmap.put(Utility.KEY_job_or_swo, "");

            hmap.put(Utility.KEY_imagePath, PathImage);
            list_ImageDescData.add(hmap);
        }
        list_path.clear();


        ///////////////////////////////////////////////

        adpter = new LvAdapter();
        listvw_images.setAdapter(adpter);
        listvw_images.setExpanded(true);

//To scroll list to 1st item after adding item to list
        //      scroll_list_toTop();
        // listvw_images.smoothScrollToPosition(list_ImageDescData.size()-1);

        Btn_Submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                int valid = validation();
                if (valid == 1) {
                    dialog_image.dismiss();
                    ////////////////////////////////

                    /////////////////////
                    if (new ConnectionDetector(ClientFilesActivity.this).isConnectingToInternet()) {
                      //  new async_UploadFiles().execute();
                        multipartImageUpload();
                    } else {
                        Toast.makeText(ClientFilesActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Btn_AddMorePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_image.dismiss();


                opendilogforattachfileandimage_custom();


            }
        });


        Btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientFilesActivity.this);
                LayoutInflater inflater = LayoutInflater.from(ClientFilesActivity.this);
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
                title.setText("Do you want to exit?");
                message.setText("All photo(s) will be lost!");
                positiveBtn.setText("Exit");
                negativeBtn.setText("No");
                negativeBtn.setVisibility(View.VISIBLE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        dialog_image.dismiss();
                        clearData();

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




             /*   SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ClientFilesActivity.this, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Do you want to exit?");
                sweetAlertDialog.setContentText("All photo(s) will be lost!");
                sweetAlertDialog.setConfirmText("Yes, Exit!");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
//                        Intent in = new Intent(ClientFilesActivity.this, MainActivity.class);
//                        startActivity(in);
                        dialog_image.dismiss();
                        clearData();

                    }
                });
                sweetAlertDialog.setCancelText("No!");
                sweetAlertDialog.showCancelButton(true);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();

                    }
                })
                        .show();*/


            }
        });


        if (list_ImageDescData.size() > 0) {
            dialog_image.show();
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public int validation() {
        //  clearFocusFromEdittext();
        int val = 0;
        int cnt = list_ImageDescData.size();
        for (int i = 0; i < cnt; i++) {


            String textDesc = list_ImageDescData.get(i).get(Utility.KEY_dataa);
            //    int ItemNo = (int) editText.getTag();
            if (textDesc == null) {
                textDesc = "";
            }

            int ItemNo = i + 1;
            if (textDesc.equalsIgnoreCase("")) {
                Toast.makeText(ClientFilesActivity.this,
                        "Please enter description of photo " + ItemNo, Toast.LENGTH_LONG).show();
                val = 0;
                break;
            } else {
                val = 1;

            }

        }
        return val;
    }

    public String enterdata(String CompID, String LoginID, String LoginName,
                            String JobId, String fileName,
                            String ImgName, String Desc, String size, String IsAgency,
                            String count) {

        String UploadedPhotoID = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_saveclientFileByClient;
        final String METHOD_NAME = API_saveclientFileByClient;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("CompID", CompID);
        request.addProperty("LoginID", LoginID);
        request.addProperty("LoginName", LoginName);
        request.addProperty("JobId", JobId);
        request.addProperty("fileName", fileName);
        request.addProperty("ImgName", ImgName);
        request.addProperty("Desc", Desc);
        request.addProperty("size", size);
        request.addProperty("IsAgency", IsAgency);
        request.addProperty("count", count);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {

            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();

            //  JSONObject jsonObject = new JSONObject(result);
            //  UploadedPhotoID = jsonObject.getString("cds");

            // Log.e("UploadedPhotoID--", UploadedPhotoID);

            //  HashMap<String, String> hashMap = new HashMap<>();
            //  hashMap.put("int_job_file_id", UploadedPhotoID);

            // list_UploadedImageID.add(hashMap);
            Log.e("UploadPhotoTech--", "End");
        } catch (Exception ed) {
            ed.printStackTrace();
        }
        return String.valueOf(UploadedPhotoID);
    }

    private void clearData() {
        list_TempImagePath.clear();
        list_path.clear();
        list_ImageDescData.clear();
        list_imageSize.clear();
        list_UploadImageName.clear();
    }



    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void requestCameraPermission(final int type) {
        try {
            Dexter.withActivity(this)
                    .withPermissions(android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    // Manifest.permission.RECORD_AUDIO)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {

                                if (type == MEDIA_TYPE_IMAGE) {
                                    // capture picture
                                    captureImage();
                                } else {
                                    // captureVideo();
                                }

                            } else if (report.isAnyPermissionPermanentlyDenied()) {
                                showPermissionsAlert();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(ClientFilesActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
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

            // getProjectPhotos();
            //   if (jobID.equals("-1")) {   //do not show data when no job is selected,
            getProjectPhotos2();
//            } else {
//                getProjectPhotos1();
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();

            //mSwipeRefreshLayout.setRefreshing(false);


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
            mAdapter = new MoviesAdapter(ClientFilesActivity.this, list_ProjectPhotos);
            recyclerView.setAdapter(mAdapter);


//            adapter = new order_adapter(context, list_ProjectPhotos);
//            listview_Clients.setAdapter(adapter);
//


        }
    }



    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
        private List<ProjectPhoto> moviesList;
        //   private HttpImageManager mHttpImageManager;

        public MoviesAdapter(Activity context, List<ProjectPhoto> moviesList) {
            this.moviesList = moviesList;
            //     mHttpImageManager = ((AppController) context.getApplication()).getHttpImageManager();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_client_clientfiles, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            ProjectPhoto projectPhoto = moviesList.get(position);
            holder.index_no.setText(String.valueOf(position + 1));


            final String FileName = projectPhoto.getFILENAME();

            final String Descr = projectPhoto.getDescr();
            final String job = projectPhoto.getJob();
            String createdate = projectPhoto.getActiondate();

            final String Action_status = projectPhoto.getActionStatus();
            final String ImgName = projectPhoto.getImgName();
            final String FileId = projectPhoto.getId() + "";
            final String IntFileId = projectPhoto.getINTFileID() + "";

            String fileExt = "";
            if (FileName.contains(".")) {
                fileExt = FileName.substring(FileName.lastIndexOf("."));
            }

            boolean isImage = Arrays.asList(Utility.imgExt).contains(fileExt);
            boolean isDoc = Arrays.asList(Utility.docExt).contains(fileExt);
            boolean isMedia = Arrays.asList(Utility.mediaExt).contains(fileExt);
            boolean isWord = Arrays.asList(Utility.wordExt).contains(fileExt);
            boolean isPdf = Arrays.asList(Utility.pdfExt).contains(fileExt);
            boolean isExcel = Arrays.asList(Utility.excelExt).contains(fileExt);
            boolean isText = Arrays.asList(Utility.txtExt).contains(fileExt);
            holder.spinner.setVisibility(View.VISIBLE);
            if (isImage) {
                String url = URL_EP2 + "/upload/" + FileName;

                Glide.with(ClientFilesActivity.this).load(url).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.spinner.setVisibility(View.GONE);

                        holder.thumbnail.setImageResource(R.drawable.no_image);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.spinner.setVisibility(View.GONE);

                        return false;
                    }
                })
                        .into(holder.thumbnail);


            } /*else if (isDoc) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.doc);
            }*/ else if (isWord) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.doc);
            } else if (isPdf) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.pdf);
            } else if (isExcel) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.excel);
            } else if (isText) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.txt_file_icon);
            } else if (isMedia) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.media);
            } else {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.no_image);
            }

            if (ImgName == null || ImgName.trim().equals("")) {
                holder.tv_file_name.setText("Not available");
            } else {
                holder.tv_file_name.setText(ImgName);
            }
            if (job == null || job.trim().equals("")) {
                holder.tv_job_name.setText("Not available");
            } else {
                holder.tv_job_name.setText(job);
            }

            if (createdate == null || createdate.trim().equals("")) {
                holder.tv_dated.setText("Not available");
            } else {

                if (createdate.contains("T")) {
                    createdate = createdate.substring(0, createdate.indexOf("T"));
                }
                String outputDateStr = "";
                try {
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String inputDateStr = createdate;
                    Date date = inputFormat.parse(inputDateStr);
                    outputDateStr = outputFormat.format(date);
                } catch (Exception e) {
                    e.getMessage();
                }
                holder.tv_dated.setText(outputDateStr);
            }


            holder.tv_status_heading.setText("Description");

            if (Descr == null || Descr.trim().equals("")) {
                holder.tv_status.setText("Not available");
            } else {

                holder.tv_status.setText(Descr);

            }

            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ClientFileDetailActivity.class);
                    // intent.putExtra("obj", projectPhoto);
                    intent.putExtra("FileId", FileId);
                    intent.putExtra("FileName", FileName);
                    intent.putExtra("jobID", jobID);
                    intent.putExtra("jobName", job);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_file_name, tv_job_name, tv_dated, tv_status, tv_status_heading;
            ImageView thumbnail;
            Button index_no;
            LinearLayout parentView;

            ProgressBar spinner;

            public MyViewHolder(View view) {
                super(view);

                index_no = (Button) view.findViewById(R.id.serial_no);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                tv_file_name = (TextView) view.findViewById(R.id.tv_file_name);
                tv_job_name = (TextView) view.findViewById(R.id.tv_job_name);
                tv_dated = (TextView) view.findViewById(R.id.tv_dated);
                tv_status = (TextView) view.findViewById(R.id.tv_status);
                parentView = (LinearLayout) view.findViewById(R.id.parentView);
                tv_status_heading = (TextView) view.findViewById(R.id.tv_status_heading);
                spinner = (ProgressBar) view.findViewById(R.id.progressBar1);

            }
        }
    }

    private class get_company_job_id extends AsyncTask<String, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(ClientFilesActivity.this);

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
            //  dialog_select_job();
            showJobSearchDialogNew("Job(s)", ClientFilesActivity.this);
        }
    }

    public class LvAdapter extends BaseAdapter {

        LayoutInflater mInflater;

        public LvAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list_ImageDescData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            final ViewHolder holder;
            convertView = null;
            final String index = String.valueOf(position + 1);
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.dialog_comment_box_new, null); // this is your cell
                holder.et_Desc = (EditText) convertView.findViewById(R.id.et_desc);
                holder.index_no = (Button) convertView.findViewById(R.id.serial_no);
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                holder.img_remove_item = (ImageView) convertView.findViewById(R.id.img_remove_item);
                holder.imageName = (TextView) convertView.findViewById(R.id.imageName);

                holder.et_Desc.setTag(position);
                holder.et_Desc.setText(list_ImageDescData.get(position).get(Utility.KEY_dataa));
                holder.imageName.setText(list_ImageDescData.get(position).get(Utility.KEY_imagename));
                holder.index_no.setText(index);
                holder.img_remove_item.setTag(position);

/*************************************************************/
                String fileExt = "";
                String FileName = list_ImageDescData.get(position).get(Utility.KEY_imagename);
                if (FileName.contains(".")) {
                    fileExt = FileName.substring(FileName.lastIndexOf("."));
                }

                boolean isImage = Arrays.asList(Utility.imgExt).contains(fileExt);
                boolean isDoc = Arrays.asList(Utility.docExt).contains(fileExt);
                boolean isMedia = Arrays.asList(Utility.mediaExt).contains(fileExt);
                boolean isWord = Arrays.asList(Utility.wordExt).contains(fileExt);
                boolean isPdf = Arrays.asList(Utility.pdfExt).contains(fileExt);
                boolean isExcel = Arrays.asList(Utility.excelExt).contains(fileExt);
                boolean isText = Arrays.asList(Utility.txtExt).contains(fileExt);

                if (isImage) {

                    final String Image_Link = list_ImageDescData.get(position).get(Utility.KEY_imagePath);
                    Glide
                            .with(ClientFilesActivity.this)
                            .load(new File(Image_Link))     // Uri of the picture
                            .into(holder.thumbnail);


                } else if (isWord) {
//                    holder. spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.doc);
                } else if (isPdf) {
//                    holder. spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.pdf);
                } else if (isExcel) {
//                    holder.  spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.excel);
                } else if (isText) {
//                    holder.   spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.txt_file_icon);
                } else if (isMedia) {
//                    holder. spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.media);
                } else {
//                    holder. spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.no_image);
                }

/*************************************************************/


                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            int tag_position = (Integer) holder.et_Desc.getTag();
            holder.et_Desc.setId(tag_position);

            int tag_position1 = (Integer) holder.img_remove_item.getTag();
            holder.img_remove_item.setId(tag_position1);

            holder.et_Desc.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    final int position2 = holder.et_Desc.getId();
                    final EditText Caption = (EditText) holder.et_Desc;

                    HashMap hashMap = (HashMap) list_ImageDescData.get(position2);
                    hashMap.put(Utility.KEY_dataa, Caption.getText().toString().trim());
                    list_ImageDescData.set(position2, hashMap);


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });

            holder.img_remove_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientFilesActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(ClientFilesActivity.this);
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
                    title.setText("Remove this item?");
                    message.setText("Are you sure?");
                    positiveBtn.setText("Yes");
                    negativeBtn.setText("No");
                    negativeBtn.setVisibility(View.VISIBLE);
                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            final int index = holder.img_remove_item.getId();
                            list_ImageDescData.remove(index);
                            notifyDataSetChanged();

                            if (list_ImageDescData.size() < 1) {

                                dialog_image.dismiss();
                                clearData();
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
                    alertDialog.show();














                 /*  SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ClientFilesActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitleText("Remove this item?");
                    sweetAlertDialog.setContentText("Are you sure?");
                    sweetAlertDialog.setConfirmText("Yes");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();


                            final int index = holder.img_remove_item.getId();
                            list_ImageDescData.remove(index);
                            notifyDataSetChanged();

                            if (list_ImageDescData.size() < 1) {
//                                Intent in = new Intent(ClientFilesActivity.this, MainActivity.class);
//                                startActivity(in);
                                //finishs();
                                dialog_image.dismiss();
                                clearData();
                            }

                        }
                    });
                    sweetAlertDialog.setCancelText("No");
                    sweetAlertDialog.showCancelButton(true);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();

                        }
                    })
                            .show();*/

                }
            });


            return convertView;
        }

    }

    public class ViewHolder {
        EditText et_Desc;
        Button index_no;
        ImageView thumbnail, img_remove_item;
        TextView imageName;
    }

    public class async_UploadFiles extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //showprogressdialog();
            super.onPreExecute();
           try {
               progressDialog = new ProgressDialog(getApplicationContext());
               progressDialog.setMessage("Uploading, Please wait..");
               progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
               progressDialog.setIndeterminate(false);
               progressDialog.setProgress(0);
               progressDialog.setMax(100);
               progressDialog.setCancelable(false);
               progressDialog.show();
           }catch (Exception e){
               e.getMessage();
           }

        }

        @Override
        protected String doInBackground(String... params) {

            String result = "0";

            //  String statusCode = test();

////////////////////////////////////////////////////////////

            int statusCode = 0;
            totalSize = 0;
            try {

                //String FILE_UPLOAD_URL = "http://mobileappupload.businesstowork.com/api/fileupload";
                String jid = jobID;
                String FILE_UPLOAD_URL = URL_EP2 + "/UploadFileHandler.ashx?jid=" + jid;
                FILE_UPLOAD_URL = FILE_UPLOAD_URL.replaceAll(Pattern.quote(" "), "%20");//nks
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);

                CustomMultiPartEntity entity = new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        int progress = (int) ((num / (float) totalSize) * 100);
                        publishProgress(progress);

                    }
                });

                //  MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                for (int i = 0; i < list_ImageDescData.size(); i++) {
                    try {
                        String path = list_ImageDescData.get(i).get(Utility.KEY_imagePath);
                        File sourceFile_1 = new File(path);
                        entity.addPart("image" + i, new FileBody(sourceFile_1));
                        // entity.addPart("jid", new StringBody(jid));
                        long Size = sourceFile_1.length();
                        totalSize = totalSize + Size;
                        list_imageSize.add(String.valueOf(Size / 1000));//kb

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }


                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    String resultdata = statusCode + "";
                    String responseStr = EntityUtils.toString(response.getEntity());
                    if (!responseStr.contains("api_error")) {
                        String s[] = responseStr.split(",");
                        List<String> stringList = new ArrayList<String>(Arrays.asList(s)); //new Ar
                        list_UploadImageName = new ArrayList<String>(stringList);
                        result = "1";
                    } else {
                        result = "0";
                    }
                } else {
                    String resultdata = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (Exception e) {
                e.getMessage();
                result = "0";
            }

/////////////////////////////////////////////////////////
            if (!result.equalsIgnoreCase("0")) {
                if (String.valueOf(statusCode).equalsIgnoreCase("200")) {

                    for (int i = 0; i < list_ImageDescData.size(); i++) {
                        String Jobid_or_swoid = list_ImageDescData.get(i).get(Utility.KEY_Jobid_or_swoid);
                        String dataa = list_ImageDescData.get(i).get(Utility.KEY_dataa);
                        String imagename = list_UploadImageName.get(i);//list_ImageDescData.get(i).get(Utility.KEY_imagename);
                        String image_size = list_imageSize.get(i);
                        String name = list_ImageDescData.get(i).get(Utility.KEY_name);
                        String clid = list_ImageDescData.get(i).get(Utility.KEY_clid);
                        String job_or_swo = list_ImageDescData.get(i).get(Utility.KEY_job_or_swo);

                        int count = 0;
                        if (i == list_ImageDescData.size() - 1) {
                            count = list_ImageDescData.size();
                        }

                        enterdata(comp_ID, Client_id_Pk, loginUserName,
                                jobID, imagename, imagename, dataa, image_size,
                                Agency, String.valueOf(count));


                    }
                }
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            //   super.onProgressUpdate(progress[0]);
            try {
                progressDialog.setProgress(progress[0]);
            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            for (int i = 0; i < list_TempImagePath.size(); i++) {
                String TempImagePath = list_TempImagePath.get(i);
                Utility.delete(TempImagePath);
            }
            int Totalfiles = list_ImageDescData.size();

            clearData();


            if (result.equalsIgnoreCase("1")) {
                Toast.makeText(ClientFilesActivity.this, String.valueOf(Totalfiles) + " files(s) uploaded successfully!", Toast.LENGTH_SHORT).show();

            } else {


                dialog_photo_upload_failed();




            }
            callApiProjectPhotos();


        }

    }


    private void multipartImageUpload() {
        if (Count_Image_Uploaded < list_ImageDescData.size())
            Count_Image_Uploaded++;
        uploadProgressDialog = new ProgressDialog(ClientFilesActivity.this);
        // uploadProgressDialog.setMessage("Uploading , Please wait..");
        uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_ImageDescData.size() + ", Please wait..");
        uploadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        uploadProgressDialog.setIndeterminate(false);
        uploadProgressDialog.setProgress(0);
        uploadProgressDialog.setMax(100);
        uploadProgressDialog.setCancelable(false);
        uploadProgressDialog.show();

      //  initRetrofitClient();

        try {
            /**/
            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[list_ImageDescData.size()];
            for (int index = 0; index < list_ImageDescData.size(); index++) {
                File file = new File(list_ImageDescData.get(index).get(Utility.KEY_imagePath));
                ProgressRequestBody surveyBody = new ProgressRequestBody(file, this);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("images[]", file.getName(), surveyBody);

                long Size = file.length();
                totalSize = totalSize + Size;
                list_imageSize.add(String.valueOf(Size / 1000));//kb
            }
            String jid = list_ImageDescData.get(0).get(Utility.KEY_Jobid_or_swoid);
            String url = URL_EP2 + "/UploadFileHandler.ashx?jid=" + jid;
            /**/


            API_Interface APIInterface = REST_API_Client.getClient().create(API_Interface.class);

            Call<ResponseBody> req = APIInterface.uploadMedia(surveyImagesParts, url);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    String result = "";
                    Count_Image_Uploaded = 0;
                    if (String.valueOf(response.code()).equals("200")) {
                        try {
                            uploadProgressDialog.setProgress(100);
                            String responseStr = response.body().string();
                            if (!responseStr.contains("api_error")) {
                                String s[] = responseStr.split(",");
                                List<String> stringList = new ArrayList<String>(Arrays.asList(s)); //new Ar
                                list_UploadImageName = new ArrayList<String>(stringList);

                                result = "1";
                            } else {
                                result = "0";
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            result = "0";
                        }
                        if (result.equalsIgnoreCase("1")) {
                            new async_LinkUploadedFiles().execute();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Upload Failed!", Toast.LENGTH_SHORT).show();
                        dialog_photo_upload_failed();

                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        if (uploadProgressDialog.isShowing())
                            uploadProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Count_Image_Uploaded = 0;
                    Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    dialog_photo_upload_failed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .build();
        try {
            APIInterface = new Retrofit.Builder().baseUrl(URL_EP2).client(client).build().create(API_Interface.class);
        } catch (Exception e) {
            e.getMessage();
        }
    }*/

    @Override
    public void onProgressUpdate(int percentage) {
        // textView.setText(percentage + "%");
        uploadProgressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {
        //  textView.setText("Uploaded Failed!");
    }

    @Override
    public void onFinish() {
        if (Count_Image_Uploaded < list_ImageDescData.size())
            Count_Image_Uploaded++;
        uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_ImageDescData.size() + ", Please wait..");

    }

    @Override
    public void uploadStart() {
        //  textView.setText("0%");
    }

    private void dialog_photo_upload_failed() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientFilesActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ClientFilesActivity.this);
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
        title.setText("Failed!");
        message.setText("Image(s) not uploaded!");
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
        alertDialog.show();
    }

    public class async_LinkUploadedFiles extends AsyncTask<String, Integer, String> {
      //  ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //showprogressdialog();
            super.onPreExecute();
            /*try {
                progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setMessage("Uploading, Please wait..");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setIndeterminate(false);
                progressDialog.setProgress(0);
                progressDialog.setMax(100);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }catch (Exception e){
                e.getMessage();
            }*/

        }

        @Override
        protected String doInBackground(String... params) {


                    for (int i = 0; i < list_ImageDescData.size(); i++) {
                        String Jobid_or_swoid = list_ImageDescData.get(i).get(Utility.KEY_Jobid_or_swoid);
                        String dataa = list_ImageDescData.get(i).get(Utility.KEY_dataa);
                        String imagename = list_UploadImageName.get(i);//list_ImageDescData.get(i).get(Utility.KEY_imagename);
                        String image_size = list_imageSize.get(i);
                        String name = list_ImageDescData.get(i).get(Utility.KEY_name);
                        String clid = list_ImageDescData.get(i).get(Utility.KEY_clid);
                        String job_or_swo = list_ImageDescData.get(i).get(Utility.KEY_job_or_swo);

                        int count = 0;
                        if (i == list_ImageDescData.size() - 1) {
                            count = list_ImageDescData.size();
                        }

                        enterdata(comp_ID, Client_id_Pk, loginUserName,
                                jobID, imagename, imagename, dataa, image_size,
                                Agency, String.valueOf(count));


                    }


            return "";
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
          //  progressDialog.dismiss();
            for (int i = 0; i < list_TempImagePath.size(); i++) {
                String TempImagePath = list_TempImagePath.get(i);
                Utility.delete(TempImagePath);
            }
            clearData();
            try {
                if (uploadProgressDialog.isShowing())
                    uploadProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }

            callApiProjectPhotos();
        }

    }

}

