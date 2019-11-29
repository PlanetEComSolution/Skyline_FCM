package planet.info.skyline.tech.billable_timesheet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import planet.info.skyline.network.Api;
import planet.info.skyline.old_activity.AppConstants;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.R;
import planet.info.skyline.old_activity.Rubishcratedataupload;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.tech.upload_photo.Upload_image_and_cooment;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;

public class Clock_Submit_Type_Activity extends BaseActivity {

    protected ImageLoader imageLoadery = ImageLoader.getInstance();
    ImageView selectvis, selectvis1, selectvis2, merchantname, missing;
    //TextView timerValue;
    TextView btn_StopWork, btn_FinishWork, clientname, btnChangeTimeCode;
    //TextView   textView1rr;
    DisplayImageOptions options;
    SharedPreferences sp;
    Editor ed;
    File file1;
    ProgressDialog pDialog;
    String path;
    int resultmy;
    int serverResponseCode;
    String fname;
    String urlofwebservice = "http://www.exhibitpower2.com/WebService/techlogin_service.asmx?op=varify_tech";
    int check_gotocatch;
    Uri mImageCaptureUri;
    String wjobid, wimg, wuname;
    ImageView selectvis2d3, homeimg;
    String webhit = "";
    String webhit1 = "";
    TextView btn_PauseWork;
    String receivedString;
    String JOB_ID_BILLABLE_FOR_PAUSED_CLOCK;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_danage_refurbish_new);
        merchantname = (ImageView) findViewById(R.id.merchantname);
        btn_PauseWork = (TextView) findViewById(R.id.pause);
        selectvis = (ImageView) findViewById(R.id.selectvisd);
        selectvis1 = (ImageView) findViewById(R.id.selectvis1d);
        selectvis2 = (ImageView) findViewById(R.id.selectvis2d);
        selectvis2d3 = (ImageView) findViewById(R.id.selectvis2d3);
        //    timerValue = (TextView) findViewById(R.id.timer);
        btn_StopWork = (TextView) findViewById(R.id.gobacktosca);
        btn_FinishWork = (TextView) findViewById(R.id.gomissi);
        btnChangeTimeCode = (TextView) findViewById(R.id.btnChangeTimeCode);


        // textView1rr = (TextView) findViewById(R.id.textView1rr);
        homeimg = (ImageView) findViewById(R.id.homeimg);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*nks*/
    /*Start chat head here again because if running clock will lost anywhere
    then when user will land  on this page by clicking on notification , the clock should appear.*/

        /**/
        /*nks*/


        /*nks*/

        LinearLayout firstjk = (LinearLayout) findViewById(R.id.firstjk);
        LinearLayout first1 = (LinearLayout) findViewById(R.id.first1);
        LinearLayout first2 = (LinearLayout) findViewById(R.id.first2);


        firstjk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //   showdialogfortype();

                Intent ii = new Intent(Clock_Submit_Type_Activity.this, Rubishcratedataupload.class);
                startActivity(ii);


            }
        });
        first1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        first2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            //    startActivity(new Intent(Clock_Submit_Type_Activity.this, Upload_image_and_cooment.class));
            }
        });


        //hide icons
        firstjk.setVisibility(View.GONE);
        first1.setVisibility(View.GONE);
        first2.setVisibility(View.GONE);
        //  textView1rr.setVisibility(View.GONE);
        //hide icons


        homeimg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Clock_Submit_Type_Activity.this, MainActivity.class));
                finish();

            }
        });


        /*nks*/


        selectvis.setImageResource(R.drawable.reportdamage);
        selectvis1.setImageResource(R.drawable.viewpakage);
        selectvis2.setImageResource(R.drawable.reportdamage);
        selectvis2d3.setImageResource(R.drawable.reportdamage);
        selectvis2d3.setVisibility(View.VISIBLE);
        merchantname.setImageResource(R.drawable.exhibitlogoa);
        missing = (ImageView) findViewById(R.id.missing);
        missing.setVisibility(View.VISIBLE);
        clientname = (TextView) findViewById(R.id.textView1);
        clientname.setText("");
        //
        pDialog = new ProgressDialog(Clock_Submit_Type_Activity.this);
        pDialog.setMessage("Kindly wait");
        pDialog.setCancelable(false);


        sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        ed = sp.edit();
      //  String names = sp.getString("client_name", "");
        String names =  Shared_Preference.getCLIENT(this);
     //   String nam = sp.getString("name", "");
        String nam  = Shared_Preference.getCLIENT_NAME(this);
        if (names != "") {
            clientname.setText(names);
        } else {
            clientname.setText(nam);
        }
        merchantname = (ImageView) findViewById(R.id.merchantname);
//        options = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.skylinelogopng)
//
//                .showImageForEmptyUri(R.drawable.skylinelogopng)
//                .showImageOnFail(R.drawable.skylinelogopng)
//                .cacheInMemory(true)
//                .cacheOnDisc(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .build();

      //  String imageloc = sp.getString("imglo", "");
        String imageloc =	Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
//        if (imageloc.equals("") || imageloc.equalsIgnoreCase("")) {
//            ed.putString("imglo", "").commit();
//            missing.setVisibility(View.GONE);
//        } else {
//            clientname.setVisibility(View.GONE);
//            missing.setVisibility(View.VISIBLE);
//            imageLoadery.displayImage(imageloc, missing, options);
//        }



      Glide.with(Clock_Submit_Type_Activity.this).load(imageloc).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

              //  String names = sp.getString("client_name", "");
                String names =  Shared_Preference.getCLIENT(Clock_Submit_Type_Activity.this);
              //  String nam = sp.getString("name", "");
                String nam  = Shared_Preference.getCLIENT_NAME(Clock_Submit_Type_Activity.this);
                if (names != "") {
                    clientname.setText(names);
                } else {
                    clientname.setText(nam);
                }

                missing.setVisibility(View.GONE);
                clientname.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                missing.setVisibility(View.VISIBLE);
                clientname.setVisibility(View.GONE);
                    return false;
            }
        })
                .into(missing);







     //   ed.putString("upload", "").commit();
        Shared_Preference.setUPLOAD(this,"");

       // wjobid = sp.getString("jobid", "s");
        wjobid =   Shared_Preference.getSWO_ID(Clock_Submit_Type_Activity.this);
     //   wuname = sp.getString("tname", "");
         wuname =  Shared_Preference.getLOGIN_USERNAME(Clock_Submit_Type_Activity.this);
        btn_PauseWork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {

                //    String clientid = sp.getString("clientid", "");
                    String clientid= Shared_Preference.getLOGIN_USER_ID(Clock_Submit_Type_Activity.this);
                    String nextact = URL_EP1 + Api.API_CRATES_LIST      ///all is develope by aman kaushik
                            + clientid;

                    Intent ii = new Intent(Clock_Submit_Type_Activity.this, SubmitTimesheet.class);
                    ii.putExtra("dataa", nextact);
                    ii.putExtra("aman_status", 0);
                    startActivity(ii);
                    finish();

                } else {
                    Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });
        btn_StopWork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//stop

                if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                 //   String clientid = sp.getString("clientid", "");
                    String clientid= Shared_Preference.getLOGIN_USER_ID(Clock_Submit_Type_Activity.this);
                    String nextact = URL_EP1 +Api.API_CRATES_LIST       ///all is develope by aman kaushik
                            + clientid;
                    Intent ii = new Intent(Clock_Submit_Type_Activity.this, SubmitTimesheet.class);
                    ii.putExtra("dataa", nextact);
                    ii.putExtra("aman_status", 1);

                    startActivity(ii);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });


        btnChangeTimeCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//

                if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()) {
                   // String clientid = sp.getString("clientid", "");
                    String clientid= Shared_Preference.getLOGIN_USER_ID(Clock_Submit_Type_Activity.this);
                    String nextact = URL_EP1 + Api.API_CRATES_LIST       ///all is develope by aman kaushik
                            + clientid;
                    Intent ii = new Intent(Clock_Submit_Type_Activity.this, SubmitTimesheet.class);
                    ii.putExtra("dataa", nextact);
                    ii.putExtra("aman_status", 100);
                    startActivity(ii);
                    // finish();

                } else {
                    Toast.makeText(getApplicationContext(), Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });


        btn_FinishWork.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                    dialog_FinishWithJob();
                } else {
                    Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }

            }
        });


        selectvis2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file1 = new File(Environment.getExternalStorageDirectory(),
                        String.valueOf(System.currentTimeMillis()) + "_FromCamera.jpg");
                File jd = file1;
                File dm = jd;
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10545424L);
                Uri mImageCaptureUri = Uri.fromFile(file1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                intent.putExtra("return-data", true);
                try {
                    startActivityForResult(intent, AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    //	attachpicandfilesubtask.setImageResource(R.drawable.attachafter);
                } catch (Exception e) {
                    //	hideprogressdialog();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Clock_Submit_Type_Activity.this, MainActivity.class));
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        ed = sp.edit();
        ed.putBoolean(Utility.IS_DANAGE_REFURBISH_IN_FRONT, true).apply();
*/
        Utility.showChatHead(Clock_Submit_Type_Activity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
       /* sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        ed = sp.edit();
        ed.putBoolean(Utility.IS_DANAGE_REFURBISH_IN_FRONT, false).apply();*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.danage_refurbish, menu);
        return true;
    }

    public void dialog_FinishWithJob() {


        final Dialog showd = new Dialog(Clock_Submit_Type_Activity.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.alerfinishwithjob_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(true);
        showd.setCanceledOnTouchOutside(false);
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        TextView NoCrate = (TextView) showd.findViewById(R.id.noCrate);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        nofo.setOnClickListener(new OnClickListener() {
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
        NoCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//to do
                if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                    Intent i = new Intent(getApplicationContext(),
                            SubmitTimesheet.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }

            }
        });
        yesfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Clock_Submit_Type_Activity.this, ClientLeavingWithCrate.class));
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

    public void showprogressdialog() {


        try {
            pDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void hideprogressdialog() {

        pDialog.dismiss();

    }

    public int uploadFile(String sourceFileUri) {
        String fileName = sourceFileUri;
        fname = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :");
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            return 0;
        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://www.exhibitpower.com/UploadToServer.php");

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " F:/wamp/wamp/www/uploads";
                        //    ed.putString("upload", fname).commit();
                            Shared_Preference.setUPLOAD(Clock_Submit_Type_Activity.this,fname);
                            wimg = fname;
                            try {
                                String sfs = wimg;//sp.getString("upload", "");
                                String[] arr = sfs.split("/");
                                int le = arr.length;

                                String dds = "";
                                dds = arr[le - 1];
                                dds = URL_EP1 +Api.API_COLLATERAL_PATH + dds;
                                wimg = dds;
                                if (dds == "" || dds.equals("") || dds.equalsIgnoreCase("")) {

                                } else {

                                }
                            } catch (Exception e) {

                            }
                            //webhit="http://exhibitpower2.com/Register/s_Work_Orderch.aspx?id="+wjobid+"&url=http://exhibitpower.com/admin/uploads/collateral/"+wimg+"&name="+wuname;
                            webhit = "http://www.exhibitpower2.com/Register/s_Work_Orderch.aspx?id=" + wjobid + "&url=" + wimg + "&name=" + wuname;
                            webhit = webhit.replace(" ", "%20");

                            String webhit1 = webhit;
                            String jfj = webhit1;
                            getjsonobject1();//
                            //          new async_login().execute();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                //    dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //     messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(Clock_Submit_Type_Activity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "api_error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                // dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Validation_photo_upload_Diloge();

                        //   Toast.makeText(Clock_Submit_Type_Activity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show()
                    }
                });
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            //    dialog.dismiss();
            return serverResponseCode;

        } // End else block


    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //	onResume();
            if (requestCode == AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    mImageCaptureUri = data.getData();
                    try {
                        path = getPath(mImageCaptureUri, Clock_Submit_Type_Activity.this); //from Gallery
                        //	upload();
                        //uploadFile(path);

                        if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                            new asyntaskupload().execute();
                        } else {
                            Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {
                        try {
                            path = mImageCaptureUri.getPath();

                            //upload();
                            if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                                new asyntaskupload().execute();
                            } else {
                                Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                            }


                            //uploadFile(path);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        //  Log.i("check image attach or not", e.toString());
                    }
                } else {
                    Uri mImageCaptureUri = Uri.fromFile(file1);
                    try {
                        path = getPath(mImageCaptureUri, Clock_Submit_Type_Activity.this); //from Gallery
                    } catch (Exception e) {
                        path = mImageCaptureUri.getPath();
                        //    Log.i("check image attach or not", e.toString());
                    }
                    String arr[] = path.split("/");
                    int i;
                }
            }
            //

            if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    mImageCaptureUri = data.getData();
                    try {
                        path = getPath(mImageCaptureUri, Clock_Submit_Type_Activity.this); //from Gallery
                        //	upload();
                        //uploadFile(path);
                        if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                            new asyntaskupload().execute();
                        } else {
                            Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {
                        try {
                            path = mImageCaptureUri.getPath();

                            //upload();

                            if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                                new asyntaskupload().execute();
                            } else {
                                Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                            }


                            //uploadFile(path);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        //  Log.i("check image attach or not", e.toString());
                    }
                } else {

                    if (file1 == null) {
                        //file1="";
                        file1 = new File(Environment.getExternalStorageDirectory(),
                                String.valueOf(System.currentTimeMillis())
                                        + "_FromCamera.jpg");
                    }


                    Uri mImageCaptureUri = Uri.fromFile(file1);
                    try {
                        path = getPath(mImageCaptureUri, Clock_Submit_Type_Activity.this); //from Gallery
                    } catch (Exception e) {
                        path = mImageCaptureUri.getPath();
                        //  Log.i("check image attach or not", e.toString());
                    }

                    if (new ConnectionDetector(Clock_Submit_Type_Activity.this).isConnectingToInternet()) {
                        new asyntaskupload().execute();
                    } else {
                        Toast.makeText(Clock_Submit_Type_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }


                    String arr[] = path.split("/");
                    int i;
                }


            }
            //
        }

    }

    public void getjsonobject1() {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, webhit, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub


                //hideprogressdialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //	hideprogressdialog();
                Log.e("api_error", arg0.toString());

            }
        });
            /*JsonObjectRequest jsonobj =new JsonObjectRequest(urlskyline, null, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject arg0) {
					// TODO Auto-generated method stub

				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					// TODO Auto-generated method stub

				}
			});	*/
        AppController.getInstance().addToRequestQueue(bb);
    }

    public void showtoast() {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a dummy image
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.helloip);

        // set a message
//					TextView text = (TextView) layout.findViewById(R.id.text);
//					text.setText("Button is clicked!");


        // Toast...
        Toast toast = new Toast(getApplicationContext());
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 120);
        //toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 10, 0);
        //toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 0);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    public void Validation_photo_upload_Diloge() {
        final Dialog choose_for_scan = new Dialog(Clock_Submit_Type_Activity.this);
        choose_for_scan.requestWindowFeature(Window.FEATURE_NO_TITLE);
        choose_for_scan.setContentView(R.layout.photo_upload_validatiom);
        choose_for_scan.setCancelable(false);

        final TextView t1 = (TextView) choose_for_scan.findViewById(R.id.scann_again);
        final TextView t2 = (TextView) choose_for_scan.findViewById(R.id.scann_again1);

        t1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_for_scan.dismiss();

                //	opendilogforattachfileandimage_custom();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file1 = new File(Environment.getExternalStorageDirectory(),
                        String.valueOf(System.currentTimeMillis()) + "_FromCamera.jpg");
                File jd = file1;
                File dm = jd;
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10545424L);
                Uri mImageCaptureUri = Uri.fromFile(file1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                intent.putExtra("return-data", true);
                try {
                    startActivityForResult(intent, AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    //	attachpicandfilesubtask.setImageResource(R.drawable.attachafter);
                } catch (Exception e) {
                    //	hideprogressdialog();
                    e.printStackTrace();
                }
            }
        });

        t2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_for_scan.dismiss();
                Intent in = new Intent(Clock_Submit_Type_Activity.this, Clock_Submit_Type_Activity.class);
                startActivity(in);
                finish();
            }
        });

        try {
            choose_for_scan.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public class asyntaskupload extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            uploadFile(path);
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            showprogressdialog();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            if (serverResponseCode == 200) {

            }

            hideprogressdialog();
            showtoast();
            super.onPostExecute(result);
        }

    }




}
