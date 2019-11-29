package planet.info.skyline.client;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.tech.fullscreenview.FullscreenWebView;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;

import planet.info.skyline.model.ProjectPhoto;
import planet.info.skyline.util.FileDownloader;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_GetClientFileByID;
import static planet.info.skyline.network.Api.API_ShowClientFileByJobeID;
import static planet.info.skyline.network.Api.API_deleteFile;
import static planet.info.skyline.network.Api.API_updateclientfiles;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class ClientFileDetailActivity extends AppCompatActivity {
    String status = "", Comment = "";
    SharedPreferences sp;
    String Client_id_Pk, comp_ID, jobID, FileId, dealerId, doneBy;
    String commentFileShare = "", MailId = "";
    ImageView img_update, thumbnail, img_download, img_delete;
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
    String str_fileName,jobName;
    String str_desc;
    //private HttpImageManager mHttpImageManager;
    AlertDialog alertDialog;
    private ProgressBar spinner;
    SwipeRefreshLayout pullToRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_file_detail);


        setTitle(Utility.getTitle("Details"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    //    mHttpImageManager = ((AppController) ClientFileDetailActivity.this.getApplication()).getHttpImageManager();

        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

        Client_id_Pk = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        comp_ID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");
        dealerId = sp.getString(Utility.CLIENT_LOGIN_DealerID, "");
        doneBy = sp.getString(Utility.CLIENT_LOGIN_UserName, "");

        //  ProjectPhoto mPhoto = (ProjectPhoto) getIntent().getSerializableExtra("obj");
        FileId = getIntent().getStringExtra("FileId");
        FileName = getIntent().getStringExtra("FileName");
        jobID = getIntent().getStringExtra("jobID");
        jobName = getIntent().getStringExtra("jobName");


        thumbnail = findViewById(R.id.thumbnail);
        img_update = findViewById(R.id.img_share);
        img_delete = findViewById(R.id.img_delete);
        img_download = findViewById(R.id.img_download);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);


        img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_EnterComment();
            }
        });
        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download_file(FileName);
            }
        });
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_file();
            }
        });
        final String url = URL_EP2 + "/upload/" + FileName;

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

//            Uri myUri = Uri.parse(url);
//            Bitmap bitmap = mHttpImageManager.loadImage(new HttpImageManager.LoadRequest(myUri, thumbnail));
//            if (bitmap != null) {
//                thumbnail.setImageBitmap(bitmap);
//            }


            Glide.with(ClientFileDetailActivity.this).load(url).listener(new RequestListener<Drawable>() {
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
            }).into(thumbnail);


       /* } else if (isDoc) {
            spinner.setVisibility(View.GONE);

            thumbnail.setImageResource(R.drawable.doc);
   */

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
                    Intent i = new Intent(ClientFileDetailActivity.this, FullscreenImageView.class);
                    i.putExtra("url", url);
                    startActivity(i);

                } else if (isDoc || isWord ||isPdf||isText|| isExcel) {
                    Intent i = new Intent(ClientFileDetailActivity.this, FullscreenWebView.class);
                    i.putExtra("url", url);
                    startActivity(i);

                } else if (isMedia) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setDataAndType(Uri.parse(url), "video/*");
                    startActivity(intent);

                } else {
                    Toast.makeText(ClientFileDetailActivity.this, "Unrecognized file format ! Please download to view the file!", Toast.LENGTH_SHORT).show();
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
        if (new ConnectionDetector(ClientFileDetailActivity.this).isConnectingToInternet()) {
            new Async_ProjectPhotoDetail().execute();
        } else {
            Toast.makeText(ClientFileDetailActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
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
        final Dialog dialog_comment = new Dialog(ClientFileDetailActivity.this);
        dialog_comment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_comment.setContentView(R.layout.update_name);

        dialog_comment.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_comment.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog_comment.setCancelable(false);
        dialog_comment.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (!dialog_comment.isShowing()) {
            dialog_comment.show();
        }

        final EditText et_filename = (EditText) dialog_comment.findViewById(R.id.et_Mail);
        final EditText et_desc = (EditText) dialog_comment.findViewById(R.id.et_comment);
        Button Btn_Done = (Button) dialog_comment.findViewById(R.id.Btn_Done);
        Button Btn_Cancel = (Button) dialog_comment.findViewById(R.id.Btn_Cancel);

        ImageView close = (ImageView) dialog_comment.findViewById(R.id.close);


        et_filename.setText(mPhoto.getImgName());
        et_desc.setText(mPhoto.getDescr());


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_comment.dismiss();

            }
        });


        Btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                str_fileName = et_filename.getText().toString().trim();
                str_desc = et_desc.getText().toString().trim();

                if (str_fileName.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please enter file name!", Toast.LENGTH_SHORT).show();
                } else if (str_desc.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please enter description!", Toast.LENGTH_SHORT).show();
                } else {

                    dialog_comment.dismiss();
                    if (new ConnectionDetector(ClientFileDetailActivity.this).isConnectingToInternet()) {
                        new Async_updateNameDesc().execute();
                    } else {
                        Toast.makeText(ClientFileDetailActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
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

    public void updateFileDetails() {

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_updateclientfiles;
        final String METHOD_NAME =API_updateclientfiles;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("ImgName", str_fileName);
        request.addProperty("filename", FileName);
        request.addProperty("job_id", jobID);
        request.addProperty("Description", str_desc);
        request.addProperty("INT_FID", FileId);
        request.addProperty("NU_FILEID_client", String.valueOf(mPhoto.getINTFID()));
        request.addProperty("client_id", comp_ID);
        request.addProperty("doneby_name", doneBy);//


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

    public void download_file(String file_name1) {

        if (new ConnectionDetector(ClientFileDetailActivity.this).isConnectingToInternet()) {
            new DownloadFile().execute(URL_EP2 + "/upload/" + file_name1, file_name1);
        } else {
            Toast.makeText(ClientFileDetailActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }


//        if (isImage) {
//
//            if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
//                new ShowJobFiles_Fragment.Download_images().execute(URL_EP2 + "/upload/" + file_name1, file_name1);
//            } else {
//                Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
//            }
//
//        } else  {
//            if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
//                new ShowJobFiles_Fragment.DownloadFile().execute(URL_EP2 + "/upload/" + file_name1, file_name1);
//            } else {
//                Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
//            }
//        }


    }

    public void delete_file() {
        try {


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientFileDetailActivity.this);
            LayoutInflater inflater = LayoutInflater.from(ClientFileDetailActivity.this);
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
            title.setText("Delete?");
            message.setText("Are you sure?");
            positiveBtn.setText("Yes");
            negativeBtn.setText("No");
            negativeBtn.setVisibility(View.VISIBLE);
            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();


                    if (new ConnectionDetector(ClientFileDetailActivity.this).isConnectingToInternet()) {
                        new Async_DeleteFile().execute();
                    } else {
                        Toast.makeText(ClientFileDetailActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
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










        /*    new SweetAlertDialog(ClientFileDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Delete?")
                    .setContentText("Are you sure?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            if (new ConnectionDetector(ClientFileDetailActivity.this).isConnectingToInternet()) {
                                new Async_DeleteFile().execute();
                            } else {
                                Toast.makeText(ClientFileDetailActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setCancelText("No")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();

                        }
                    })
                    .show();*/
        } catch (Exception e) {
            e.getCause();
        }





    }

    public void getProjectPhotoDetail() {

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_ShowClientFileByJobeID;
        final String METHOD_NAME = API_ShowClientFileByJobeID;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("clientUserID", Client_id_Pk);//
        request.addProperty("jobID", jobID);//
        //  request.addProperty("comp_ID", comp_ID);
        request.addProperty("Agency", Agency);//
        request.addProperty("dealerID", dealerId);//
        request.addProperty("FileID", FileId);//
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

                int nu_comment_id = jsonObject1.getInt("nu_comment_id_client");
                int ddl_job_status = 0;//
                int cl = 0;//

                String dt = "";//
                String job = jsonObject1.getString("job");
                String fsize = jsonObject1.getString("fsize");
                String latestcom1 = "";//
                int job_id = jsonObject1.getInt("jobid");
                String actiondate = jsonObject1.getString("update_comment_date");//

                String createdate = jsonObject1.getString("DTR_SubmitDate_client");//
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
                String Uploaded_BY_client = jsonObject1.getString("Uploaded_BY_client");
                int id = jsonObject1.getInt("ID");
                String ImgName = jsonObject1.getString("ImgName");


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

                        Descr, Uploaded_BY_client,
                        id, ImgName);

            } catch (Exception e) {
                e.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getProjectPhotoDetailNew() {

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetClientFileByID;
        final String METHOD_NAME = API_GetClientFileByID;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("id", FileId);//
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


                int nu_comment_id = 0;
                int ddl_job_status = 0;//
                int cl = 0;//

                String dt = "";//
                String job = "";
                String fsize = jsonObject1.getString("Size");
                String latestcom1 = "";//
                int job_id = 0;
                String actiondate = jsonObject1.getString("update_comment_date");//

                String createdate = "";//
                int nu_approveedit_by_client = 0;//
                String latestcom = "";
                int fileid = 0;//
                int jid = 0;//
                int View_status_for_client = 0;//
                int nu_client_id = 0;

                int nu_reject_by_client = 0;//
                String snoozDate = "";//

                int snooz = 0;//
                int nu_approve_by_client = 0;//
                int INT_FID = jsonObject1.getInt("INT_FID_client");
                int INT_FileID = jsonObject1.getInt("INT_FileID_client");
                String VCHAR_Heading = "";//
                String FILE_NAME_question = jsonObject1.getString("orgfilename");
                String FILE_NAME = jsonObject1.getString("orgfilename");
                String Action_statusOLD = "";//
                String Action_status = "";//
                String Descr = jsonObject1.getString("des");
                String Uploaded_BY_client = jsonObject1.getString("Uploaded_BY_client");
                int id = jsonObject1.getInt("ID");
                String ImgName = jsonObject1.getString("ImgName");


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

                        Descr, Uploaded_BY_client,
                        id, ImgName);

            } catch (Exception e) {
                e.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void DeleteFile() {

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_deleteFile;
        final String METHOD_NAME = API_deleteFile;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Fileid", mPhoto.getINTFileID()+"");//
        request.addProperty("doneby", doneBy);//
        request.addProperty("Client_id", Client_id_Pk);//
        request.addProperty("job_id_client", jobID);//

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


                int nu_comment_id = 0;
                int ddl_job_status = 0;//
                int cl = 0;//

                String dt = "";//
                String job = "";
                String fsize = jsonObject1.getString("Size");
                String latestcom1 = "";//
                int job_id = 0;
                String actiondate = jsonObject1.getString("update_comment_date");//

                String createdate = "";//
                int nu_approveedit_by_client = 0;//
                String latestcom = "";
                int fileid = 0;//
                int jid = 0;//
                int View_status_for_client = 0;//
                int nu_client_id = 0;

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
                String Descr = jsonObject1.getString("des");
                String Uploaded_BY_client = jsonObject1.getString("Uploaded_BY_client");
                int id = jsonObject1.getInt("ID");
                String ImgName = jsonObject1.getString("ImgName");


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

                        Descr, Uploaded_BY_client,
                        id, ImgName);

            } catch (Exception e) {
                e.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void setData() {

        TextView tv_file_name, tv_job_name,
                tv_upload_dated, tv_last_action_dated, tv_status, tv_file_desc, tv_upload_by;
        tv_file_name = findViewById(R.id.tv_file_name);
        tv_job_name = findViewById(R.id.tv_job_name);
        tv_upload_dated = findViewById(R.id.tv_upload_dated);
        tv_last_action_dated = findViewById(R.id.tv_last_action_dated);
        tv_status = findViewById(R.id.tv_status);
        tv_file_desc = findViewById(R.id.tv_file_desc);
        tv_upload_by = findViewById(R.id.tv_upload_by);
        try {
            jobID = mPhoto.getJobId() + "";

            final String job = mPhoto.getJob();
            String createdate = mPhoto.getCreatedate();
            String lastActiondate = mPhoto.getActiondate();
            final String Action_status = mPhoto.getActionStatus();
            final String ImgName = mPhoto.getImgName();
            final String Descr = mPhoto.getDescr();
            final String UploadedBy = mPhoto.getFileHeading();
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

            if (jobName == null || jobName.trim().equals("")) {
                tv_job_name.setText("Not available");
            } else {
                tv_job_name.setText(jobName);
            }
            if (UploadedBy == null || UploadedBy.trim().equals("")) {
                tv_upload_by.setText("Not available");
            } else {
                tv_upload_by.setText(UploadedBy);
            }

            if (createdate == null || createdate.trim().equals("")) {
                tv_upload_dated.setText("Not available");
            } else {
                if (createdate.contains("-")) {
                    createdate = createdate.replaceAll("-", "/");
                }
                if (createdate.contains("T")) {
                    createdate = createdate.substring(0, createdate.indexOf("T"));
                }
                String outputDateStr = "";
                try {
                    DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
                    DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String inputDateStr = createdate;
                    Date date = inputFormat.parse(inputDateStr);
                    outputDateStr = outputFormat.format(date);
                } catch (Exception e) {
                    e.getMessage();
                }


                tv_upload_dated.setText(outputDateStr);
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
                String outputDateStr = "";
                ;
                try {
                    DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
                    DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String inputDateStr = lastActiondate;
                    Date date = inputFormat.parse(inputDateStr);
                    outputDateStr = outputFormat.format(date);
                } catch (Exception e) {
                    e.getMessage();
                }

                tv_last_action_dated.setText(outputDateStr);
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

    private class Async_updateNameDesc extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDoalog = new ProgressDialog(ClientFileDetailActivity.this);

            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            updateFileDetails();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();
            Toast.makeText(getApplicationContext(), "Updated successfully!", Toast.LENGTH_SHORT).show();
            CallApiProjectPhotoDetail();

        }
    }

    class DownloadFile extends AsyncTask<String, Void, String> {///this class make in adapter for downloading the pdf
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

            progressDoalog = new ProgressDialog(ClientFileDetailActivity.this);
            progressDoalog.setMessage("Downloading, please wait...");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            progressDoalog.dismiss();
            try {
                Toast.makeText(ClientFileDetailActivity.this, "File downloaded successfully !" +
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

            progressDoalog = new ProgressDialog(ClientFileDetailActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //   getProjectPhotoDetail();
            getProjectPhotoDetailNew();
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

    private class Async_DeleteFile extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDoalog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDoalog = new ProgressDialog(ClientFileDetailActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //   getProjectPhotoDetail();
            DeleteFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();
            Toast.makeText(ClientFileDetailActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT)
                    .show();
            finish();

        }
    }

}
