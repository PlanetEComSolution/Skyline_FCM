package planet.info.skyline.tech.job_files_new;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.tech.fullscreenview.FullscreenWebView;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.ProjectPhoto;
import planet.info.skyline.model.ProjectPhotoComment;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.network.Api;
import planet.info.skyline.util.FileDownloader;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_GetProjectFileComment;
import static planet.info.skyline.network.Api.API_SaveProjectFileComment;
import static planet.info.skyline.network.Api.API_UpdateProjectPhotoStatusByClient;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;
import static planet.info.skyline.util.Utility.isValidEmail;

//import planet.info.skyline.httpimage.HttpImageManager;

public class ProjectFileDetailActivityNonClient extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String status = "", Comment = "", SnoozDate = "";
    SharedPreferences sp;
    String Client_id_Pk, comp_ID, jobID, FileId, dealerId, googleId,masterId;
    // String commentFileShare = "";
    String MailId = "";
    ImageView img_share, thumbnail, img_download;
    String fileExt;
    boolean isImage;
    boolean isDoc;
    boolean isMedia;
    boolean isWord;
    boolean isPdf;
    boolean isExcel;
    boolean isText;

    String FileName;
    String Agency = "0";// by default
    ProjectPhoto mPhoto;
    ArrayList<ProjectPhotoComment> list_ProjectPhotoComment = new ArrayList<>();
    EditText et_textDate, et_comment;
 //   private HttpImageManager mHttpImageManager;
    private ProgressBar spinner;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_file_detail_non_clientl);


        setTitle(Utility.getTitle("Details"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  mHttpImageManager = ((AppController) ProjectFileDetailActivityNonClient.this.getApplication()).getHttpImageManager();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

       Client_id_Pk = "0";


      //  comp_ID = sp.getString(Utility.COMPANY_ID_BILLABLE, "");
        comp_ID =   Shared_Preference.getCOMPANY_ID_BILLABLE(this);

      //  dealerId = sp.getString(Utility.DEALER_ID, "");
        dealerId = Shared_Preference.getDEALER_ID(this);
        //  ProjectPhoto mPhoto = (ProjectPhoto) getIntent().getSerializableExtra("obj");
        FileId = getIntent().getStringExtra("FileId");
        FileName = getIntent().getStringExtra("FileName");
        jobID = getIntent().getStringExtra("jobID");
        googleId = getIntent().getStringExtra("googleId");

        masterId = getIntent().getStringExtra("masterId");


        Button btn_Approve, btn_RevisionNeeded, btn_Snooze,btn_Comment;
        btn_Approve = findViewById(R.id.btn_Approve);
        btn_RevisionNeeded = findViewById(R.id.btn_PevisionNeeded);
        btn_Snooze = findViewById(R.id.btn_Snooze);
        btn_Comment= findViewById(R.id.btn_Comment);

        thumbnail = findViewById(R.id.thumbnail);
        img_share = findViewById(R.id.img_share);
        img_download = findViewById(R.id.img_download);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);


        btn_Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "2";
                Dialog_EnterComment();
            }
        });

        btn_RevisionNeeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "3";
                Dialog_EnterComment();
            }
        });
        btn_Snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "4";
                Dialog_EnterComment_Snoozed();
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "8";
                Intent intent = new Intent(ProjectFileDetailActivityNonClient.this, SharePhotosToClientActivity.class);
                Bundle args = new Bundle();
                intent.putExtra("BUNDLE", args);
                intent.putExtra(Utility.isFrom_PROJECT_FILE, "true");
                startActivityForResult(intent,Utility.PROJECT_FILE_GET_CLIENT_MAILS);




            }
        });
        btn_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "5";
                Dialog_EnterComment();
            }
        });
        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download_file(FileName);
            }
        });

     //   final String url = "https://drive.google.com/open?id=" + googleId;
        final String url = "https://docs.google.com/uc?id=" + googleId;

        if (FileName.contains(".")) {
            fileExt = FileName.substring(FileName.lastIndexOf("."));
        }
        isImage = Arrays.asList(Utility.imgExt).contains(fileExt);
        isDoc = Arrays.asList(Utility.docExt).contains(fileExt);
        isMedia = Arrays.asList(Utility.mediaExt).contains(fileExt);

        isWord = Arrays.asList(Utility.wordExt).contains(fileExt);
        isPdf = Arrays.asList(Utility.pdfExt).contains(fileExt);
        isExcel = Arrays.asList(Utility.excelExt).contains(fileExt);
        isText = Arrays.asList(Utility.txtExt).contains(fileExt);


        if (isImage) {


            final String url_thumbnail = "https://drive.google.com/thumbnail?id=" + googleId;
            Glide.with(ProjectFileDetailActivityNonClient.this).load(url_thumbnail).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    spinner.setVisibility(View.GONE);

                    thumbnail.setImageResource(R.drawable.no_image);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    spinner.setVisibility(View.GONE);

                    return false;
                }
            })
                    .into(thumbnail);


//        } else if (isDoc) {
//            spinner.setVisibility(View.GONE);
//            thumbnail.setImageResource(R.drawable.doc);

        } else if (isWord) {
            spinner.setVisibility(View.GONE);
            thumbnail.setImageResource(R.drawable.doc);
        } else if (isPdf) {
            spinner.setVisibility(View.GONE);
            thumbnail.setImageResource(R.drawable.pdf);
        } else if (isExcel) {
            spinner.setVisibility(View.GONE);
            thumbnail.setImageResource(R.drawable.excel);
        } else if (isText) {
            spinner.setVisibility(View.GONE);
            thumbnail.setImageResource(R.drawable.txt_file_icon);
        } else if (isMedia) {
            spinner.setVisibility(View.GONE);
            thumbnail.setImageResource(R.drawable.media);
        } else {
            spinner.setVisibility(View.GONE);
            thumbnail.setImageResource(R.drawable.no_image);
        }

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImage) {


                    Intent i = new Intent(ProjectFileDetailActivityNonClient.this, FullscreenImageView.class);
                    i.putExtra("url", url);
                    startActivity(i);


                } else if (isDoc || isWord ||isPdf||isText|| isExcel) {


                    Intent i = new Intent(ProjectFileDetailActivityNonClient.this, FullscreenWebView.class);
                    i.putExtra("url", url);
                    startActivity(i);

                } else if (isMedia) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setDataAndType(Uri.parse(url), "video/*");
                    startActivity(intent);

                } else {
                    Toast.makeText(ProjectFileDetailActivityNonClient.this, "Unrecognized file format ! Please download to view the file!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        CallApiProjectPhotoDetail();

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CallApiProjectPhotoDetail();
                // your code

            }
        });
    }

    private void CallApiProjectPhotoDetail() {
        if (new ConnectionDetector(ProjectFileDetailActivityNonClient.this).isConnectingToInternet()) {
            new Async_ProjectPhotoDetail().execute();
        } else {
            Toast.makeText(ProjectFileDetailActivityNonClient.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
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

    public void Dialog_EnterComment() {
        final Dialog dialog_comment = new Dialog(ProjectFileDetailActivityNonClient.this);
        dialog_comment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_comment.setContentView(R.layout.enter_comment);

        dialog_comment.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_comment.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog_comment.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        dialog_comment.setCancelable(false);
        if (!dialog_comment.isShowing()) {
            dialog_comment.show();
        }

        final EditText et_comment = (EditText) dialog_comment.findViewById(R.id.texrtdesc);
        if (status.equals("2")) {
            et_comment.setText("Approved");
        } else if (status.equals("3")) {
            et_comment.setText("Revisions Needed");
        }


        Button Btn_Done = (Button) dialog_comment.findViewById(R.id.Btn_Done);
        Button Btn_Cancel = (Button) dialog_comment.findViewById(R.id.Btn_Cancel);

        ImageView close = (ImageView) dialog_comment.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_comment.dismiss();

            }
        });


        Btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Comment = et_comment.getText().toString().trim();
                MailId = "";
                SnoozDate = "";
                if (Comment.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please enter comment!", Toast.LENGTH_SHORT).show();
                } else {

                    dialog_comment.dismiss();
                    if (new ConnectionDetector(ProjectFileDetailActivityNonClient.this).isConnectingToInternet()) {
                        new Async_ShareFileToGuest().execute();

                       // new   Async_ChangeFileStatus.execute();
                    } else {
                        Toast.makeText(ProjectFileDetailActivityNonClient.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog_comment.dismiss();
            }
        });

    }

    public void Dialog_ShareFile(String mail_id) {
        final Dialog dialog_comment = new Dialog(ProjectFileDetailActivityNonClient.this);
        dialog_comment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_comment.setContentView(R.layout.enter_comment_mail);

        dialog_comment.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_comment.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog_comment.setCancelable(false);
        if (!dialog_comment.isShowing()) {
            dialog_comment.show();
        }

        final EditText et_Mail = (EditText) dialog_comment.findViewById(R.id.et_Mail);
        final EditText et_comment = (EditText) dialog_comment.findViewById(R.id.et_comment);
        et_Mail.setText(mail_id);
       // et_Mail.setVisibility(View.GONE);
        Button Btn_Done = (Button) dialog_comment.findViewById(R.id.Btn_Done);
        Button Btn_Cancel = (Button) dialog_comment.findViewById(R.id.Btn_Cancel);

        ImageView close = (ImageView) dialog_comment.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_comment.dismiss();

            }
        });


        Btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // commentFileShare = et_comment.getText().toString().trim();
                Comment = et_comment.getText().toString().trim();
                MailId = et_Mail.getText().toString().trim();
                SnoozDate = "";

                if (!validateMail(MailId)) {
                    Toast.makeText(getApplicationContext(), "Please enter valid e-mail id!", Toast.LENGTH_SHORT).show();
                } else if (Comment.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please enter comment!", Toast.LENGTH_SHORT).show();
                } else {

                    dialog_comment.dismiss();
                    if (new ConnectionDetector(ProjectFileDetailActivityNonClient.this).isConnectingToInternet()) {
                        new Async_ShareFileToGuest().execute();
                    } else {
                        Toast.makeText(ProjectFileDetailActivityNonClient.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog_comment.dismiss();
            }
        });

    }

    private boolean validateMail(String mails) {
        boolean validation = true;
        if (mails.contains(",")) {
            String ar_mail[] = mails.split(",");
            for (int i = 0; i < ar_mail.length; i++) {
                if (!isValidEmail(ar_mail[i])) {
                    validation = false;
                    break;
                }
            }
        } else {
            if (!isValidEmail(mails)) {
                validation = false;
            }
        }

        return validation;
    }

    public void ChangeFileStatus() {


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_UpdateProjectPhotoStatusByClient;
        final String METHOD_NAME = API_UpdateProjectPhotoStatusByClient;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("nu_client_id", Client_id_Pk);
        request.addProperty("vr_comment", Comment);
        request.addProperty("nu_job_id", jobID);
        request.addProperty("nu_comp_id", comp_ID);
        // request.addProperty("vr_comment_by", Client_id_Pk);
        request.addProperty("vr_comment_by", "0");
        request.addProperty("NU_FILEID", FileId);
        request.addProperty("file", "");
        request.addProperty("ImgName", "");
        request.addProperty("status", status);

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


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ShareFileToGuest(String mail_id) {

      // String userID = sp.getString("clientid", "");
        String userID= Shared_Preference.getLOGIN_USER_ID(ProjectFileDetailActivityNonClient.this);

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_SaveProjectFileComment;
        final String METHOD_NAME = API_SaveProjectFileComment;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("CommentLine", Comment);
        request.addProperty("CommentStatus", status);
        request.addProperty("ProjectFileID", FileId);
        request.addProperty("AdditionalDate", SnoozDate);
        request.addProperty("compID", comp_ID);
        request.addProperty("JobID", jobID);
        request.addProperty("ImgName", "");
        request.addProperty("FileUrl", "");
        request.addProperty("isClient", "0");
        request.addProperty("ClientUserID", Client_id_Pk);
        request.addProperty("MailIDs", mail_id);
        request.addProperty("CommentBy", userID);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();
          //  JSONObject jsonObject = new JSONObject(result);

          //  JSONArray jsonArray = jsonObject.getJSONArray("cds");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void download_file(String file_name1) {

        if (new ConnectionDetector(ProjectFileDetailActivityNonClient.this).isConnectingToInternet()) {
            new DownloadFile().execute("https://drive.google.com/thumbnail?id=" + googleId, file_name1);
        } else {
            Toast.makeText(ProjectFileDetailActivityNonClient.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }


    }

    public void getProjectPhotos() {


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE +Api.API_FETCH_PROJECTFILE;
        final String METHOD_NAME = Api.API_FETCH_PROJECTFILE;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Job_id", jobID);
        request.addProperty("clientID", comp_ID);
        request.addProperty("id_pk", FileId);
        request.addProperty("masterID", masterId);
        request.addProperty("file_status", "");
        request.addProperty("DealerID", dealerId);


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

            try {

                JSONObject jsonObject1 = jsonArray.getJSONObject(0);


                /*************************************/
                int id_pk = jsonObject1.getInt("id_pk");
                int Job_id = jsonObject1.getInt("Job_id");
                int clientID = jsonObject1.getInt("clientID");
                String GoogleID = jsonObject1.getString("GoogleID");
                String OriginalFilename = jsonObject1.getString("OriginalFilename");
                String CreatedDate = jsonObject1.getString("CreatedDate");
                String ModifyDate = jsonObject1.getString("ModifyDate");
                String status = jsonObject1.getString("status");
                String Description = jsonObject1.getString("Description");
                String masterfolderID = jsonObject1.getString("masterfolderID");
                String parentfolderID = jsonObject1.getString("parentfolderID");
                String path = jsonObject1.getString("path");
                String uploadedBy = jsonObject1.getString("uploadedBy");
                String txt_job = jsonObject1.getString("txt_job");
                String txt_C_Name = jsonObject1.getString("txt_C_Name");
                String Internal_Status = jsonObject1.getString("Internal_Status");
                String LastActionDate = jsonObject1.getString("LastActionDate");
                String Client_Status = jsonObject1.getString("Client_Status");
                String UploadbyName = jsonObject1.getString("UploadbyName");
                String iconLink = jsonObject1.getString("iconLink");
                String FileType = jsonObject1.getString("FileType");
                String ApprovalRequired = jsonObject1.getString("ApprovalRequired");
                String PGoogleID = jsonObject1.getString("PGoogleID");
                String degree = jsonObject1.getString("degree");
                String d1 = jsonObject1.getString("d1");

                mPhoto = new ProjectPhoto(
                        0, 0,
                        0, CreatedDate, txt_job, "",
                        "", Job_id, LastActionDate, CreatedDate,

                        0, "", id_pk, Job_id,
                        0, clientID, 0,
                        "", 0, 0,

                        id_pk, id_pk, "",

                        OriginalFilename, OriginalFilename,

                        Client_Status, Internal_Status,

                        Description, "",
                        id_pk, "");

                /**************************************/






          /*      int nu_comment_id = jsonObject1.getInt("degree");
                int ddl_job_status = jsonObject1.getInt("status");
                int cl = 0;
                String dt = jsonObject1.getString("FileType");
                String job = jsonObject1.getString("txt_job");
                String fsize = "";
                String latestcom1 = jsonObject1.getString("iconLink");
                int job_id = jsonObject1.getInt("Job_id");
                String actiondate = jsonObject1.getString("LastActionDate");
                String createdate = jsonObject1.getString("CreatedDate");
                int nu_approveedit_by_client = 0;
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
                int INT_FileID = jsonObject1.getInt("id_pk");
                String VCHAR_Heading = jsonObject1.getString("GoogleID");
                String FILE_NAME_question = jsonObject1.getString("GoogleID");
                String FILE_NAME = jsonObject1.getString("OriginalFilename");
                String Action_statusOLD = jsonObject1.getString("txt_C_Name");
                String Action_status = jsonObject1.getString("Client_Status");
                String Descr = jsonObject1.getString("Description");
                String file_heading = "";
                int id = 0;
                String ImgName = "";




                mPhoto = new ProjectPhoto(
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
*/

            } catch (Exception e) {
                e.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void setData() {

        TextView tv_file_name, tv_job_name,
                tv_upload_dated, tv_last_action_dated, tv_status, tv_file_desc,tv_Client_status;
        tv_file_name = findViewById(R.id.tv_file_name);
        tv_job_name = findViewById(R.id.tv_job_name);
        tv_upload_dated = findViewById(R.id.tv_upload_dated);
        tv_last_action_dated = findViewById(R.id.tv_last_action_dated);
        tv_status = findViewById(R.id.tv_status);
        tv_Client_status = findViewById(R.id.tv_client_status);


        tv_file_desc = findViewById(R.id.tv_file_desc);
        try {
            jobID = mPhoto.getJobId() + "";

            /*  recycler*/
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new MoviesAdapter(ProjectFileDetailActivityNonClient.this, list_ProjectPhotoComment);
            recyclerView.setAdapter(mAdapter);
            /*recycler*/


            final String job = mPhoto.getJob();
            String createdate = mPhoto.getCreatedate();
            String lastActiondate = mPhoto.getActiondate();
            final String Action_status = mPhoto.getActionStatus();
            final String ImgName = mPhoto.getFILENAME();
            final String Descr = mPhoto.getDescr();


            final String Client_Status =         mPhoto.getActionStatusOLD();

            if (ImgName == null || ImgName.trim().equals("")) {
                tv_file_name.setText("Not available");
            } else {
                tv_file_name.setText(ImgName);
            }

            if (Descr == null || Descr.trim().equals("")) {
                tv_file_desc.setText("Not available");
            } else {
                tv_file_desc.setText(Descr);
            }

            if (job == null || job.trim().equals("")) {
                tv_job_name.setText("Not available");
            } else {
                tv_job_name.setText(job);
            }

            if (createdate == null || createdate.trim().equals("")) {
                tv_upload_dated.setText("Not available");
            } else {
                if (createdate.contains("-")) {
                    createdate = createdate.replaceAll("-", "/");
                }
                tv_upload_dated.setText(createdate);
            }

            if (lastActiondate == null || lastActiondate.trim().equals("")) {
                tv_last_action_dated.setText("Not available");
            } else {
                if (lastActiondate.contains("-")) {
                    lastActiondate = lastActiondate.replaceAll("-", "/");
                }
                if (lastActiondate.contains("T")) {
                    lastActiondate = lastActiondate.substring(0, lastActiondate.indexOf("T"));
                }
                tv_last_action_dated.setText(lastActiondate);
            }


            if (Client_Status == null || Client_Status.trim().equals("")) {
                tv_Client_status.setText("Not available");
            }else {
                tv_Client_status.setText(Client_Status);
                if (Client_Status.equalsIgnoreCase("Rejected")) {
                    tv_Client_status.setTextColor(getResources().getColor(R.color.red));
                } else if (Client_Status.equalsIgnoreCase("Approved")) {
                    tv_Client_status.setTextColor(getResources().getColor(R.color.main_green_color));
                } else if (Client_Status.equalsIgnoreCase("Snoozed")) {
                    tv_Client_status.setTextColor(getResources().getColor(R.color.snoozed));
                } else {
                    tv_Client_status.setTextColor(getResources().getColor(R.color.main_orange_light_color));
                }
            }

            if (Action_status == null || Action_status.trim().equals("")) {
                tv_status.setText("Not available");
            } else {
                tv_status.setText(Action_status);
                if (Action_status.equalsIgnoreCase("Rejected")) {
                    tv_status.setTextColor(getResources().getColor(R.color.red));
                } else if (Action_status.equalsIgnoreCase("Approved")) {
                    tv_status.setTextColor(getResources().getColor(R.color.main_green_color));
                } else if (Action_status.equalsIgnoreCase("Snoozed")) {
                    tv_status.setTextColor(getResources().getColor(R.color.snoozed));
                } else {
                    tv_status.setTextColor(getResources().getColor(R.color.main_orange_light_color));
                }

            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void getLatestComments() {
        list_ProjectPhotoComment.clear();
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetProjectFileComment;
        final String METHOD_NAME = API_GetProjectFileComment;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("ProjectFileID", FileId);
        request.addProperty("isClient", "0");

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


                    int jobid = jsonObject1.getInt("JobID");
                    int id = jsonObject1.getInt("CommentID");
                    int fileid = 0;
                    int clientid = 0;
                    int client_v_st = 0;
                    int vr_comment_by = 0;
                    int Id_Pk = 0;


                    String upload_date = jsonObject1.getString("CommentedDate");
                    ;
                    String uploaded_by = jsonObject1.getString("CommentByName");
                    String comment = jsonObject1.getString("CommentLine").replaceAll("\n", "");

                    String status = jsonObject1.getString("StatusName");

                    String Imgname = jsonObject1.getString("ImgName");
                    String fileUrl = "";

                    list_ProjectPhotoComment.add(new ProjectPhotoComment(jobid, id, fileid, clientid,
                            client_v_st, vr_comment_by, Id_Pk, upload_date, uploaded_by, comment, status, Imgname, fileUrl));

                } catch (Exception e) {
                    e.getMessage();
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Dialog_EnterComment_Snoozed() {
        final Dialog dialog_comment = new Dialog(ProjectFileDetailActivityNonClient.this);
        dialog_comment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_comment.setContentView(R.layout.enter_comment_snoozed);
        dialog_comment.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_comment.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog_comment.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dialog_comment.setCancelable(false);
        if (!dialog_comment.isShowing()) {
            dialog_comment.show();
        }

        et_comment = (EditText) dialog_comment.findViewById(R.id.texrtdesc);
        et_textDate = (EditText) dialog_comment.findViewById(R.id.textDate);


        Button Btn_Done = (Button) dialog_comment.findViewById(R.id.Btn_Done);
        Button Btn_Cancel = (Button) dialog_comment.findViewById(R.id.Btn_Cancel);
        et_textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click_getDate();
            }
        });
        ImageView close = (ImageView) dialog_comment.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_comment.dismiss();

            }
        });


        Btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Comment = et_comment.getText().toString().trim();
                MailId = "";
                SnoozDate = et_textDate.getText().toString().trim();
                if (Comment.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please enter comment!", Toast.LENGTH_SHORT).show();
                } else if (et_textDate.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter snooze date!", Toast.LENGTH_SHORT).show();

                } else {

                    dialog_comment.dismiss();
                    if (new ConnectionDetector(ProjectFileDetailActivityNonClient.this).isConnectingToInternet()) {
                        new Async_ShareFileToGuest().execute();
                    } else {
                        Toast.makeText(ProjectFileDetailActivityNonClient.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog_comment.dismiss();
            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = +dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        String dayOfMonthString = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String monthOfYearString = monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear;

        //2018-01-19%20

        // String  date_1 = year + "-" + monthOfYearString + "-" + dayOfMonthString;
        String date_1 = monthOfYearString + "/" + dayOfMonthString + "/" + year;

        // et_picking_up_date.setText(date_1);


        et_textDate.setText(date_1);
        et_comment.setText("Art Snoozed for  " + date_1);


    }

    private void Click_getDate() {

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ProjectFileDetailActivityNonClient.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dpd.setThemeDark(false);
        dpd.vibrate(false);
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);

        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(getResources().getColor(R.color.btn_text_sky_blue));


        dpd.setTitle("Select Date");
        dpd.setYearRange(1985, 2028);
        calendar.add(Calendar.DATE, 1);
        dpd.setMinDate(calendar);
        dpd.show(ProjectFileDetailActivityNonClient.this.getFragmentManager(), "dialog");


    }



    private class Async_ShareFileToGuest extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDoalog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDoalog = new ProgressDialog(ProjectFileDetailActivityNonClient.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (MailId.contains(",")) {
                String ar_mails[] = MailId.split(",");

                for (int i = 0; i < ar_mails.length; i++) {
                    ShareFileToGuest(ar_mails[i]);
                }
            } else {
                ShareFileToGuest(MailId);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                progressDoalog.dismiss();
            }catch (Exception e){e.getCause();}

            if (status.equals("2") || status.equals("3") || status.equals("4") || status.equals("5")) {
                Toast.makeText(getApplicationContext(), "Status changed successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Mail sent successfully!", Toast.LENGTH_SHORT).show();

            }
            Comment = "";
            MailId = "";
            status = "";
            CallApiProjectPhotoDetail();

        }
    }

    private class DownloadFile extends AsyncTask<String, Void, String> {///this class make in adapter for downloading the pdf
        ProgressDialog progressDoalog;

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            /**/

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Exhibit Power");

            if (!folder.exists()) {
                folder.mkdir();
            }

            File folder1 = new File(folder, "Download");
            if (!folder1.exists()) {
                folder1.mkdir();
            }


            if (fileName.contains("/")) {
                fileName = fileName.substring(fileName.indexOf("/") + 1);
            }
            File pdfFile = new File(folder1, fileName);
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileDownloader.downloadFile(fileUrl, pdfFile);
            return folder1.getAbsolutePath();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(progress_bar_type);

            progressDoalog = new ProgressDialog(ProjectFileDetailActivityNonClient.this);
            progressDoalog.setMessage("Downloading, please wait...");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            progressDoalog.dismiss();
            try {
                Toast.makeText(ProjectFileDetailActivityNonClient.this, "File downloaded successfully !" +
                        "  " + "Location:" + path, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.getMessage();
            }
        }


    }

    private class Async_ProjectPhotoDetail extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDoalog = new ProgressDialog(ProjectFileDetailActivityNonClient.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //  getProjectPhotoDetail();
            getProjectPhotos();
            getLatestComments();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();

            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
            setData();


        }
    }

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private List<ProjectPhotoComment> moviesList;


        public MoviesAdapter(Activity context, List<ProjectPhotoComment> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_comment, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            ProjectPhotoComment photoComment = moviesList.get(position);

           // holder.index_no.setText(String.valueOf(position + 1));
            holder.index_no.setText(String.valueOf(moviesList.size()-(position)));

            final String comment = photoComment.getComment();
            final String givenBy = photoComment.getUploadedBy();
            String date = photoComment.getUploadDate();
            final String status = photoComment.getStatus();

            if (comment == null || comment.trim().equals("")) {
                holder.tv_Comment.setText("Not available");
            } else {
                holder.tv_Comment.setText(comment);
            }

            if (givenBy == null || givenBy.trim().equals("")) {
                holder.tv_GivenBy.setText("Not available");
            } else {
                holder.tv_GivenBy.setText(givenBy);
            }

            if (date == null || date.trim().equals("")) {
                holder.tv_DateTime.setText("Not available");
            } else {
                String outputDateStr = "";
                if (date.contains("T")) {
                    date = date.replace("T", " ");
                }
                try {
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                    String inputDateStr = date;
                    Date date1 = inputFormat.parse(inputDateStr);
                    outputDateStr = outputFormat.format(date1);

                    String time = outputDateStr.substring(outputDateStr.indexOf(" ") + 1);
                    DateFormat inputFormat1 = new SimpleDateFormat("HH:mm:ss");
                    DateFormat outputFormat1 = new SimpleDateFormat("hh:mm:ss aa");

                    Date date12 = inputFormat1.parse(time);
                    String newTime = outputFormat1.format(date12);

                    outputDateStr = outputDateStr.substring(0, outputDateStr.indexOf(" ")) + " " + newTime;
                } catch (Exception e) {
                    e.getMessage();
                }

                holder.tv_DateTime.setText(outputDateStr);

            }

            if (status == null || status.trim().equals("")) {
                holder.tv_status.setText("Not available");
            } else {
                holder.tv_status.setText(status);
            }


        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_Comment, tv_GivenBy, tv_DateTime, tv_status, tv_status_heading;
            ImageView thumbnail;
            Button index_no;
            LinearLayout parentView;

            ProgressBar spinner;

            public MyViewHolder(View view) {
                super(view);

                index_no = (Button) view.findViewById(R.id.serial_no);
                tv_Comment = (TextView) view.findViewById(R.id.tv_Comment);
                tv_GivenBy = (TextView) view.findViewById(R.id.tv_GivenBy);
                tv_DateTime = (TextView) view.findViewById(R.id.tv_DateTime);
                tv_status = (TextView) view.findViewById(R.id.tv_status);


            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Utility.PROJECT_FILE_GET_CLIENT_MAILS)
        {

           if (resultCode == Activity.RESULT_CANCELED) {
                return;
            }
            String selected_mails=data.getStringExtra(Utility.Client_Mail);
            Dialog_ShareFile(selected_mails);
        }

    }
}
