package planet.info.skyline.tech.upload_photo;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import planet.info.skyline.R;
import planet.info.skyline.adapter.CompanyNameAdapter;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.network.REST_API_Client;
import planet.info.skyline.old_activity.AppConstants;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.ProgressRequestBody;
import planet.info.skyline.tech.job_files_new.SharePhotosToClientActivity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.CameraUtils;
import planet.info.skyline.util.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static planet.info.skyline.network.Api.API_BindJob;
import static planet.info.skyline.network.Api.API_GetAllDetailbyJobtext_New;
import static planet.info.skyline.network.Api.API_GetJobBySWO;
import static planet.info.skyline.network.Api.API_GetJobDetailsBySWO;
import static planet.info.skyline.network.Api.API_UploadPhotoTech;
import static planet.info.skyline.network.Api.API_UploadPhotoTech_new;
import static planet.info.skyline.network.Api.API_bindClientByDealer;
import static planet.info.skyline.network.Api.API_check_job_exist;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class Upload_image_and_cooment extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {
  //  public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    // Image and Video file extensions
  //  public static final String IMAGE_EXTENSION = "jpg";
  //  public static final String VIDEO_EXTENSION = "mp4";
    public static final int MEDIA_TYPE_IMAGE = 1;
  //  public static final int MEDIA_TYPE_VIDEO = 2;
    private static String imageStoragePath;
  //  List<HashMap<String, String>> listad = new ArrayList<HashMap<String, String>>();
  //  ListView listdatal;
    // EditText item1,item2,item3,item4;
  //  TextView gobacktosca;
    int pankajtester_chutya;
    String main_jobid = "";
    int aman_geneious = 0;
    String new_job_id, new_des;
    String main_status;////EDIT IN THIS
    List<String> company_id_list = new ArrayList<String>();
    List<String> company_Name_list = new ArrayList<String>();
    String job, comp_id_name;
    List<String> job_id_list = new ArrayList<String>();
    List<String> job_Name_list = new ArrayList<String>();
    List<String> job_des_list = new ArrayList<String>();
    List<String> status_list = new ArrayList<String>();
    List<String> show_list = new ArrayList<String>();
    List<String> jobtype_list = new ArrayList<String>();
    AutoCompleteTextView job_name, company_name;
    DisplayImageOptions options;
   // SharedPreferences sp;
    //Editor ed;
   // Uri mImageCaptureUri;
   // int serverResponseCode;
   // TextView clientname;
  //  ImageView merchantname, missing, homeacti;
    ProgressDialog pDialog;
    String path, fname = "";
    File file1;
  /*  TextView jobid, projmgt, date, salesrap, tech, configsiz, gomissi,
            itencount, uploadpict, uploadpict1;// size
    String item, desc, esimate, labourhou, linkforreport, withimgh, withoutimg;
    EditText item1, item2, item3, item4;
    EditText item5, item6, item7, item8;
    String wjobid, wjobidq, wimg, wuname, royaldesc, royaldescq, royalid;*/
    int resultmy, check_gotocatch;
    String itemtype = "";
//    Boolean second = false;
    String urlofwebservice = URL_EP2 + "/WebService/url=",
            receivedString, pmname = "", jobname = "";
    //  List<HashMap<String, String>> allcommentandimage;
    String urlofwebservice1 = URL_EP2 + "/WebService/techlogin_service.asmx?op=check_job_exist";
    String urlofwebservice2 = URL_EP2 + "/WebService/techlogin_service.asmx?op=UploadPhotoTech";
   // String imageload = URL_EP2 + "/WebService/techlogin_service.asmx?op=UploadPhotoTechServer";
    HashMap<String, String> hascoment;
  //  String webhit;
   // String alluploadimage = "", allcommemnt = "", enterjobid = "";
    String imagename = "", Jobid_or_swoid = "";
    int job_or_swo = 1;
    //String encode_String = "", urlskyline;
    //Intent kk;
    int count;
    String comapny_id1;
    String company_name1;
    String job_descripition1;
    String status1;
    String show1;
    String jobtype1;
    String new_jobtype;
    String new_show;
    int satatus = 0;
    AlertDialog alertDialog;
    String selectedJobId;//nks
    Dialog dialog_Select_company;
    ArrayList<String> list_path = new ArrayList<>();
    ArrayList<String> list_TempImagePath = new ArrayList<>();
    ArrayList<HashMap<String, String>> list_ImageDescData = new ArrayList<>();
    ArrayList<String> list_imageSize = new ArrayList<>();
    ArrayList<String> list_UploadImageName = new ArrayList<>();
    ArrayList<HashMap<String, String>> list_UploadedImageID = new ArrayList<>();
    long totalSize = 0;
    List<String> job_Name_list_Desc = new ArrayList<String>();
    List<String> company_id_list_forIndex = new ArrayList<String>();
    List<String> company_Name_list_forIndex = new ArrayList<String>();
    CompanyNameAdapter companyNameAdapter;
    List<String> job_Name_list_Desc_forIndex = new ArrayList<String>();
    String compName;
    //  ListView listvw_images;
    ExpandableHeightListView listvw_images;
    LvAdapter adpter;
    String userRole = "";
    ProgressDialog uploadProgressDialog;
    int Count_Image_Uploaded = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_image_and_cooment);
        pDialog = new ProgressDialog(Upload_image_and_cooment.this);
        pDialog.setMessage(getString(R.string.Loading_text));
        pDialog.setCancelable(false);
       // sp = getApplicationContext().getSharedPreferences("skyline",
          //      MODE_PRIVATE);
       // ed = sp.edit();
        //     userRole = sp.getString(Utility.LOGIN_USER_ROLE, "");
        userRole = Shared_Preference.getUSER_ROLE(this);

        /*nks*/
        /*boolean TimerFromAdminClockModule = false;
        if (sp.contains(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE)) {
            TimerFromAdminClockModule = sp.getBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, false);

        }*/

        /*nks*/


      //  boolean TIMER_STARTED_FROM_BILLABLE_MODULE = sp.getBoolean(Utility.TIMER_STARTED_FROM_BILLABLE_MODULE, false);
        final boolean TIMER_STARTED_FROM_BILLABLE_MODULE =   Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(this);
        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            Jobid_or_swoid = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
            job_or_swo = 3;
            opendilogforattachfileandimage_custom();

        } else {
            showtochoseswoorjob();
        }


    }

    public void opendilogforattachfileandimage_custom() {
        final Dialog dialog = new Dialog(Upload_image_and_cooment.this);
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

        ImageView crosse = (ImageView) dialog
                .findViewById(R.id.close);


        crosse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


                exitMethod();


            }
        });

        cameralayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


             /*
                boolean success=true;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                   File folder = new File(extStorageDirectory, "Exhibit Power");
                try {
                    if (!folder.exists()) {
                        success = folder.mkdir();
                    }
                    File folder1 = new File(folder, "Camera");
                    if (!folder1.exists()) {
                        success = folder1.mkdir();
                    }

                    String unique_id = Utility.getUniqueId();

                    file1 = new File(folder1, unique_id + "_FromCamera.jpg");
                } catch (Exception e) {
                    e.getCause();
                }
                Uri mImageCaptureUri = FileProvider.getUriForFile(Upload_image_and_cooment.this, Upload_image_and_cooment.this.getApplicationContext().getPackageName() + ".provider", file1);
                try {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent,
                            AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
*/

                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }


            }
        });
        gallarylayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Upload_image_and_cooment.this, AlbumSelectActivity.class);
                intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 50);
                startActivityForResult(intent, AppConstants.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);

                dialog.dismiss();

            }
        });
        filelayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("file/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Complete action using"),
                        4);

            }
        });

        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
            }
        });
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //finish();
        finishs();
    }

    public void showprogressdialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public void hideprogressdialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void dialog_Image_Description() {

        // dialog_Single_image_Desciption();
        dialog_Multiple_image_Desciption();


    }

    public void showtochoseswoorjob() {
        if (new ConnectionDetector(Upload_image_and_cooment.this).isConnectingToInternet()) {
            new get_company_name().execute();
        } else {
            Toast.makeText(Upload_image_and_cooment.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }






    /*	Log.d("punnu_chutya","punnu desi fucker");
        final Dialog showd = new Dialog(Upload_image_and_cooment.this);
		showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		showd.setContentView(R.layout.enterjobidorscanswo);
		showd.setCancelable(false);
		TextView job = (TextView) showd.findViewById(R.id.Ok);
		TextView swo = (TextView) showd.findViewById(R.id.needcrates);
		ImageView close = (ImageView) showd.findViewById(R.id.close);
		final EditText texrtdesc = (EditText) showd
				.findViewById(R.id.texrtdesc);
		// exrtdesc.setText("Confirm you have moved # of crates to bin xyz");
		// yesfo.setText("  OK  ");

		showd.onBackPressed();
		TextView textviewheader = (TextView) showd
				.findViewById(R.id.textView1rr);
		textviewheader.setText("Please enter Job");
		job.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				job_or_swo = 0;
				Jobid_or_swoid = texrtdesc.getText().toString();
				enterjobid = texrtdesc.getText().toString();

				// only for  job descripition

				if(Jobid_or_swoid.length()==0)
				{
					Toast.makeText(Upload_image_and_cooment.this,"Please enter Job Id",Toast.LENGTH_SHORT).show();
				}
				else
				{
					new get_company_Area().execute(Jobid_or_swoid);
					showd.dismiss();
				}
			//	new async_login().execute();

			}
		});
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showd.dismiss();
				//finish();
				finishs();

			}
		});
		swo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					job_or_swo = 1;
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 5);
					showd.dismiss();

				} catch (Exception e) {

					Uri marketUri = Uri
							.parse("market://details?id=com.google.zxing.client.android");
					Intent marketIntent = new Intent(Intent.ACTION_VIEW,
							marketUri);
					startActivity(marketIntent);

				}
			}
		});
		/*
		 * yesfo.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) {
		 *
		 * showd.dismiss(); finish(); } });
		 */

        //showd.show();
    }

    public void dialog_sharePhotos() {
        final Dialog showd = new Dialog(Upload_image_and_cooment.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_yes_no);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        showd.setCancelable(false);
        TextView Btn_No = (TextView) showd.findViewById(R.id.Btn_No);
        TextView Btn_Yes = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        texrtdesc.setText("Do you want to share these photos?");


        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);

        textviewheader.setText("Uploaded Successfully!");

        Btn_No.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showd.dismiss();
                list_UploadedImageID.clear();
                finishs();

            }
        });
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();
                list_UploadedImageID.clear();
                finishs();

            }
        });
        Btn_Yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showd.dismiss();


                Intent intent = new Intent(Upload_image_and_cooment.this, SharePhotosToClientActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) list_UploadedImageID);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
                finish();

            }
        });

        showd.show();


    }

    public String enterdata(String a, String b, String c, String d, String e,
                            String f, String g, String h) {


        String UploadedPhotoID = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice2;
        final String SOAP_ACTION = KEY_NAMESPACE + API_UploadPhotoTech;
        final String METHOD_NAME = API_UploadPhotoTech;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("job_swo_id", a);
        request.addProperty("description", b);
        request.addProperty("filename", c);
        request.addProperty("comment", d);
        request.addProperty("filesize", e);
        request.addProperty("tech_name", f);
        request.addProperty("tech_id", g);
        request.addProperty("type", h);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            Log.e("UploadPhotoTech--", "Called");
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();

            JSONObject jsonObject = new JSONObject(result);
            UploadedPhotoID = jsonObject.getString("cds");

            Log.e("UploadedPhotoID--", UploadedPhotoID);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("int_job_file_id", UploadedPhotoID);

            list_UploadedImageID.add(hashMap);

            Log.e("UploadPhotoTech--", "End");
        } catch (Exception ed) {

            ed.printStackTrace();
        }


        return String.valueOf(UploadedPhotoID);
    }

    public String generate_event(String job_swo_id, String tech_id, String type, String count) {
        String result = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        //  final String URL = urlofwebservice2;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_UploadPhotoTech_new;
        final String METHOD_NAME = API_UploadPhotoTech_new;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("job_swo_id", job_swo_id);
        request.addProperty("tech_id", tech_id);
        request.addProperty("type", type);
        request.addProperty("count", count);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j); // if complex type is present then you can

            }
            String response = ks.toString();
            if (response.contains("st=1")) {
                result = "1";
            } else {
                result = "0";
            }
        } catch (Exception ed) {
            ed.printStackTrace();
        }
        return result;
    }


    public void finishs() {
        Intent iiu = new Intent(Upload_image_and_cooment.this, MainActivity.class);
        iiu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iiu);
        finish();
    }

    public void get_data(String id)   ///by aman kaushik
    {
        comapny_id1 = null;
        company_name1 = null;
        job_descripition1 = null;
        status1 = null;
        show1 = null;
        jobtype1 = null;
        compName = null;
        count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetAllDetailbyJobtext_New;
        final String METHOD_NAME = API_GetAllDetailbyJobtext_New;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //  String dealerId = sp.getString(Utility.DEALER_ID, "");
        String dealerId = Shared_Preference.getDEALER_ID(this);

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

                //   ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, comapny_id1).apply();
                Shared_Preference.setJOB_ID_FOR_JOBFILES(this, comapny_id1);
                selectedJobId = comapny_id1;//save for when jobIdOrSWOID not exists
                Jobid_or_swoid = selectedJobId;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Validation_photo_upload_Diloge() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment.this);
        LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment.this);
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
        title.setText("Photo uploading failed!");
        message.setText("Try again?");
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                opendilogforattachfileandimage_custom();
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in = new Intent(Upload_image_and_cooment.this, MainActivity.class);
                startActivity(in);
                finish();

            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();










       /* new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Photo uploading failed !")
                .setContentText("Try again ?")
                .setConfirmText("Upload again !")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        *//**//*
                        opendilogforattachfileandimage_custom();

                        *//**//*

                    }
                })
                .setCancelText("Go back !")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();

                        Intent in = new Intent(Upload_image_and_cooment.this, Upload_image_and_cooment.class);
                        startActivity(in);
                        finish();

                    }
                })
                .show();
*/

    }

    public void Getcompany_name()   ///by aman kaushik
    {
        //   String dealerId = sp.getString(Utility.DEALER_ID, "");
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
            if (recved.contains("Job not found")) {
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

                    //     company_id_list.put(company_name,comapny_id);

                    company_id_list.add(comapny_id);
                    company_Name_list.add(company_name);
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

    public void diloge_for_company_name()    /////by aman kaushik
    {
        pankajtester_chutya = 0;
        dialog_Select_company = new Dialog(Upload_image_and_cooment.this);
        dialog_Select_company.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_Select_company.setContentView(R.layout.test1_new);
        dialog_Select_company.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_Select_company.setCancelable(true);
        ImageView closebtn = (ImageView) dialog_Select_company.findViewById(R.id.close);
        company_name = (AutoCompleteTextView) dialog_Select_company.findViewById(R.id.company);
        job_name = (AutoCompleteTextView) dialog_Select_company.findViewById(R.id.job);
        Button btn_GO = (Button) dialog_Select_company.findViewById(R.id.go_button);
        Button scann_swo = (Button) dialog_Select_company.findViewById(R.id.Scann);


        TextView or = (TextView) dialog_Select_company.findViewById(R.id.or);


        if (userRole.equals(Utility.USER_ROLE_APC) ||
                userRole.equals(Utility.USER_ROLE_ARTIST)) {
            scann_swo.setVisibility(View.GONE);
            or.setVisibility(View.GONE);

        } else {
            scann_swo.setVisibility(View.VISIBLE);
            or.setVisibility(View.VISIBLE);
        }





       /* ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Upload_image_and_cooment.this, android.R.layout.simple_list_item_1, company_Name_list);
        company_name.setAdapter(dataAdapter);*/

        companyNameAdapter = new CompanyNameAdapter(Upload_image_and_cooment.this, android.R.layout.simple_list_item_1, company_Name_list);
        company_name.setAdapter(companyNameAdapter);

        company_name.setDropDownHeight(550);
        dialog_Select_company.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
///ontouch for company name-->
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
                    companyNameAdapter = new CompanyNameAdapter(Upload_image_and_cooment.this, android.R.layout.simple_list_item_1, company_Name_list);
                    company_name.setAdapter(companyNameAdapter);
                    company_name.setDropDownHeight(550);


                } else {
                    company_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
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
                if (job_name.getText().length() == 0) {
                    job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                    if (company_name.getText().toString().length() == 0) {
                        job_name.setAdapter(null);
                    } else {
                        //refresh adapter
                        job_Name_list_Desc = new ArrayList<>(job_Name_list_Desc_forIndex);
                        CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(Upload_image_and_cooment.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
                        job_name.setAdapter(jobDescAdapter);
                        job_name.setDropDownHeight(550);
                    }
                } else {
                    job_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                }
            }
        });

        scann_swo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //scanqr();
                try {
                    job_or_swo = 1;
                    Intent intent = new Intent(
                            "com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());

                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 5);
                    dialog_Select_company.dismiss();

                } catch (Exception e) {

                    Uri marketUri = Uri
                            .parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                            marketUri);
                    startActivity(marketIntent);

                }


            }


        });


        company_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("punnu_chutiya", company_name.getText().toString());
                if (company_name.getText().equals("--Select--") || (company_name.getText().length() == 0)) {

                } else {
                    pankajtester_chutya = 1;
                    int index = company_Name_list_forIndex.indexOf(company_name.getText().toString());
                    comp_id_name = company_id_list_forIndex.get(index);


                    if (new ConnectionDetector(Upload_image_and_cooment.this).isConnectingToInternet()) {
                        new get_company_job_id().execute(comp_id_name);

                    } else {
                        Toast.makeText(Upload_image_and_cooment.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }


                }
            }
        });


        job_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                aman_geneious = 12;
                // int index = job_Name_list.indexOf(job_name.getText().toString());
                //  int index = i;

                ////

                String job_txt = job_name.getText().toString();
                job_txt = job_txt.substring(0, job_txt.indexOf("\n"));
                int index = job_Name_list.indexOf(job_txt);

                main_jobid = job_Name_list.get(index);////EDIT IN THIS
                new_job_id = job_id_list.get(index);
                Shared_Preference.setJOB_ID_FOR_JOBFILES(Upload_image_and_cooment.this, new_job_id);
                new_des = job_des_list.get(index);
                main_status = status_list.get(index);////EDIT IN THIS
                new_jobtype = jobtype_list.get(index);
                new_show = show_list.get(index);
                job_name.setText("");
                job_name.setText(main_jobid);
            }
        });


        closebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Upload_image_and_cooment.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });
        //nks
        dialog_Select_company.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent in = new Intent(Upload_image_and_cooment.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });
//nks
        btn_GO.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                job_or_swo = 3;//means when Jobid_or_swoid not exists then type=2
                Shared_Preference.setCOMPANY_ID_BILLABLE(Upload_image_and_cooment.this, comp_id_name);
               Shared_Preference.setJOB_NAME_BILLABLE(Upload_image_and_cooment.this, main_jobid);
                /*nks*/
                if (company_name.getText().length() == 0) {
                    if (job_name.getText().length() == 0) {
                        Toast.makeText(Upload_image_and_cooment.this, "Please enter Job Id", Toast.LENGTH_SHORT).show();
                    } else {
                        if (new ConnectionDetector(Upload_image_and_cooment.this).isConnectingToInternet()) {
                            if (dialog_Select_company != null || dialog_Select_company.isShowing()) {
                                dialog_Select_company.dismiss();
                            }
                            new get_company_Area().execute(job_name.getText().toString());

                        } else {
                            Toast.makeText(Upload_image_and_cooment.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                        }


                    }
                } else {
                    if (pankajtester_chutya == 1) {

                        compName = company_name.getText().toString();
                        if (company_name.getText().toString().equals("--Select--")) {
                            Toast.makeText(Upload_image_and_cooment.this, "Please Select Company", Toast.LENGTH_SHORT).show();
                        } else {
                            if (count == 1) {
                                Toast.makeText(Upload_image_and_cooment.this, "Please Select another Company", Toast.LENGTH_SHORT).show();
                            } else {
                                if (job_name.getText().toString().trim().equals("--Select--") || job_name.getText().toString().trim().equals("")) {
                                    Toast.makeText(Upload_image_and_cooment.this, "Please Select job Id", Toast.LENGTH_SHORT).show();
                                } else {


                                    if (aman_geneious == 12) {
                                        /*nks*/
                                        selectedJobId = new_job_id;//save for when jobIdOrSWOID not exists
                                        Jobid_or_swoid = selectedJobId;
                                        job_or_swo = 3;//means when Jobid_or_swoid not exists then type=2


                                        String companyName = company_name.getText().toString().trim();
                                        String CompanyId = job_name.getText().toString().trim();
                                        if (!company_Name_list.contains(companyName)) {
                                            Toast.makeText(Upload_image_and_cooment.this, "Please enter correct Company Name!", Toast.LENGTH_SHORT).show();
                                        } else if (!job_Name_list.contains(CompanyId)) {
                                            Toast.makeText(Upload_image_and_cooment.this, "Please enter correct Job Id!", Toast.LENGTH_SHORT).show();
                                        } else {


                                            dialog_Select_company.hide();
                                            final Dialog dialog_jobInfo = new Dialog(Upload_image_and_cooment.this);
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

                                            if (new_jobtype == "null") {
                                                tv_Company.setText((compName));
                                                t1.setText(main_jobid);
                                                t2.setText("");
                                                t3.setText(main_status);
                                                t4.setText(new_show);
                                                t5.setText(new_des);

                                            } else {
                                                tv_Company.setText((compName));
                                                t1.setText(main_jobid);
                                                t2.setText(new_jobtype);
                                                t3.setText(main_status);
                                                t4.setText(new_show);
                                                t5.setText(new_des);

                                            }

                                            btn_GO.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog_Select_company.dismiss();
                                                    dialog_jobInfo.dismiss();

                                                    opendilogforattachfileandimage_custom();

                                                }
                                            });

                                            cancle.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog_jobInfo.dismiss();
                                                    dialog_Select_company.show();

                                                }
                                            });


                                            dialog_jobInfo.show();
                                        }
                                    } else {
                                        if (new ConnectionDetector(Upload_image_and_cooment.this).isConnectingToInternet()) {

                                            new get_company_Area().execute(job_name.getText().toString());
                                        } else {
                                            Toast.makeText(Upload_image_and_cooment.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                                        }


                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(Upload_image_and_cooment.this, "Please Select Valid Company name", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
        dialog_Select_company.show();

    }

    public void Getcompany_job_id(String id)   ///by aman kaushik
    {
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
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();
            if (recved.contains("No Data Available.")) {
                count = 1;
            } else {

             /*   String[] aa = recved.split("=");
                String a = aa[1];
                String b[] = a.split(";");
                String k = b[0];
                Log.d("BHANU", k);
                Log.d("punnu_chutiya", k);*/


                String json = recved.substring(recved.indexOf("=") + 1, recved.lastIndexOf(";"));


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


                    /**/

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

                    /**/


                    job_id_list.add(comapny_id);
                    job_Name_list.add(company_name);
                    job_Name_list_Desc.add(total_desc);
                    job_des_list.add(job_descripition);

                    status_list.add(status);
                    show_list.add(show);
                    jobtype_list.add(jobtype);
                }
                //   job_id_list.add(0, "--Select--");
                //   job_Name_list.add(0, "--Select--");
                //   job_des_list.add(0,"--Select--");

                //    status_list.add(0,"--Select--");
                //    show_list.add(0,"--Select--");
                //   jobtype_list.add(0,"--Select--");

                job_Name_list_Desc_forIndex = new ArrayList<>(job_Name_list_Desc);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void f2() {



        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment.this);
        LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment.this);
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
        title.setText("No jobs available !");
        message.setText("Kindly choose another Company !");
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

    public String get_JobName_by_SWOId(String SWO_id) {
        String jobName = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetJobBySWO;
        final String METHOD_NAME = API_GetJobBySWO;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("swo_id", SWO_id);
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
            if (!recved.contains("txt_job")) {

            } else {
                recved = recved.substring(recved.indexOf("=") + 1, recved.indexOf(";"));
                JSONObject jsonObject = new JSONObject(recved);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                jobName = jsonObject1.getString("job_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobName;
    }

    public void exitMethod() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment.this);
        LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment.this);
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
        message.setText("Are you sure?");
        positiveBtn.setText("Yes");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in = new Intent(Upload_image_and_cooment.this, MainActivity.class);
                startActivity(in);
                finishs();
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (list_ImageDescData.size() > 0) {

                    dialog_Multiple_image_Desciption();
                } else {
                    opendilogforattachfileandimage_custom();
                }
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();




    }

    private void dialog_Multiple_image_Desciption() {

        final Dialog dialog_image = new Dialog(Upload_image_and_cooment.this);
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


        ////////////////////////////////////

        // String clid = sp.getString("clientid", "");
        String clid = Shared_Preference.getLOGIN_USER_ID(Upload_image_and_cooment.this);
        //  String name = sp.getString("tname", "");
        String name = Shared_Preference.getLOGIN_USERNAME(this);
        //nks
        boolean TimerFromAdminClockModule = false;

        if (Shared_Preference.containsKey(Upload_image_and_cooment.this,Shared_Preference.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE)){
       // if (sp.contains(Shared_Preference.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE)) {
            //   TimerFromAdminClockModule = sp.getBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, false);
            TimerFromAdminClockModule = Shared_Preference.getTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(Upload_image_and_cooment.this);

        }

        if (Jobid_or_swoid == null || Jobid_or_swoid.trim().equalsIgnoreCase("") || TimerFromAdminClockModule) {
            Jobid_or_swoid = selectedJobId;
            job_or_swo = 3;//means when Jobid_or_swoid not exists then type=2
        }
//nks


        for (int i = 0; i < list_path.size(); i++) {

            String PathImage = list_path.get(i);
            String pathName[] = PathImage.split("/");
            String image_size = "24 kb";
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put(Utility.KEY_Jobid_or_swoid, Jobid_or_swoid);
            // hmap.put(Utility.KEY_dataa, dataa);
            //  hmap.put(Utility.KEY_imagename, imagename);
            hmap.put(Utility.KEY_imagename, pathName[pathName.length - 1]);
            hmap.put(Utility.KEY_image_size, image_size);
            hmap.put(Utility.KEY_name, name);
            hmap.put(Utility.KEY_clid, clid);
            hmap.put(Utility.KEY_job_or_swo, job_or_swo + "");

            hmap.put(Utility.KEY_imagePath, PathImage);
            list_ImageDescData.add(hmap);
        }
        list_path.clear();


        ///////////////////////////////////////////////

        adpter = new LvAdapter();
        listvw_images.setAdapter(adpter);
        listvw_images.setExpanded(true);

//To scroll list to 1st item after adding item to list
        //  scroll_list_toTop();

        Btn_Submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                int valid = validation();
                if (valid == 1) {
                    dialog_image.dismiss();
                    ////////////////////////////////
                    Utility.hideKeyboard(Upload_image_and_cooment.this);
                    /////////////////////
                    if (new ConnectionDetector(Upload_image_and_cooment.this).isConnectingToInternet()) {
                        //    new async_UploadFiles().execute();
                        multipartImageUpload();
                    } else {
                        Toast.makeText(Upload_image_and_cooment.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Btn_AddMorePhotos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_image.dismiss();


                opendilogforattachfileandimage_custom();


            }
        });


        Btn_Back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment.this);
                LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment.this);
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
                positiveBtn.setText("Yes");
                negativeBtn.setText("No");
                negativeBtn.setVisibility(View.VISIBLE);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Intent in = new Intent(Upload_image_and_cooment.this, MainActivity.class);
                        startActivity(in);
                        finishs();
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
        });


        dialog_image.show();
    }

    public int validation() {

        int val = 0;
        int cnt = list_ImageDescData.size();
        for (int i = 0; i < cnt; i++) {

            String textDesc = list_ImageDescData.get(i).get(Utility.KEY_dataa);
            if (textDesc == null) {
                textDesc = "";
            }

            int ItemNo = i + 1;
            if (textDesc.equalsIgnoreCase("")) {
                Toast.makeText(Upload_image_and_cooment.this,
                        "Please enter description of photo " + ItemNo, Toast.LENGTH_LONG).show();
                val = 0;
                break;
            } else {
                val = 1;

            }

        }
        return val;
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
                    Uri mImageCaptureUri = FileProvider.getUriForFile(Upload_image_and_cooment.this, Upload_image_and_cooment.this.getApplicationContext().getPackageName() + ".provider", file1);
                    try {
                        path = getPath(mImageCaptureUri,
                                Upload_image_and_cooment.this); // from Gallery
                        list_path.add(path);
                        dialog_Image_Description();
                        //nks
                    } catch (Exception e) {
                        path = mImageCaptureUri.getPath();
                        list_path.add(path);
                        dialog_Image_Description();
                    }

                }
            }
            //

            if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

                path = imageStoragePath;
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


            }
            if (requestCode == 5) {

                String result = "";
                String contents = data.getStringExtra("SCAN_RESULT");

                result = contents;
                String Swo_Id = "";
                String clientid = "";

                if (result.contains(",")) {
                    String[] jj = result.split(",");// jobid,clientid
                    try {
                        Swo_Id = jj[0];
                        clientid = jj[1];
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    Jobid_or_swoid = Swo_Id;


                    if (Swo_Id.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(),
                                "Please scan valid QR Code of SWO!                        ",
                                Toast.LENGTH_LONG).show();
                        showtochoseswoorjob();
                    } else {
                        Shared_Preference.setCOMPANY_ID_BILLABLE(Upload_image_and_cooment.this, clientid);
                        if (new ConnectionDetector((Upload_image_and_cooment.this)).isConnectingToInternet()) {
                            new async_getJobDetailsBySwoId().execute(Swo_Id);
                        } else {
                            Toast.makeText(Upload_image_and_cooment.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please scan valid QR Code of SWO!", Toast.LENGTH_SHORT).show();
                    showtochoseswoorjob();
                }

            }

        } else {

            if (requestCode == 5) {
                onBackPressed();
            } else {
                if (list_ImageDescData.size() > 0) {
                    dialog_Image_Description();
                } else {
                    opendilogforattachfileandimage_custom();
                }
            }

        }

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
                        CameraUtils.openSettings(Upload_image_and_cooment.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void multipartImageUpload() {
        if (Count_Image_Uploaded < list_ImageDescData.size())
            Count_Image_Uploaded++;
        uploadProgressDialog = new ProgressDialog(Upload_image_and_cooment.this);
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
                public void onResponse( Call<ResponseBody> call, Response<ResponseBody> response) {

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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment.this);
        LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment.this);
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
        message.setText("Image(s) not uploaded!\nPlease try after some time!");
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.GONE);
        positiveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finishs();
            }
        });
        negativeBtn.setOnClickListener(new OnClickListener() {
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
            e.getMessage();
        }
    }

    private void Dialog_ShowJobInfo_from_SWO(JSONObject js_obj) {

        try {
            String job_id = js_obj.getString("job_id");
            String jobName = js_obj.getString("jobName");
            String JOB_TYPE = js_obj.getString("JOB_TYPE");
            String jobstatus = js_obj.getString("jobstatus");
            String company = js_obj.getString("company");
            String Show_Name = js_obj.getString("Show_Name");
            String desciption = js_obj.getString("desciption");

            Shared_Preference.setJOB_ID_FOR_JOBFILES(Upload_image_and_cooment.this, job_id);
            Shared_Preference.setJOB_NAME_BILLABLE(Upload_image_and_cooment.this, jobName);

            Jobid_or_swoid = job_id;
            job_or_swo = 3;

            final Dialog dialog_jobInfo = new Dialog(Upload_image_and_cooment.this);
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

                    dialog_jobInfo.dismiss();

                    opendilogforattachfileandimage_custom();

                }
            });

            cancle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_jobInfo.dismiss();
                    onBackPressed();

                }
            });

            dialog_jobInfo.show();

        } catch (Exception e) {
            e.getMessage();
        }

    }

    class get_company_Area extends AsyncTask<String, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(Upload_image_and_cooment.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ringProgressDialog.setMessage(getString(R.string.Loading_text));
            ringProgressDialog.setCancelable(false);
            ringProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... par) {

            get_data(par[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ringProgressDialog.dismiss();

            if (count == 1) {
                Toast.makeText(Upload_image_and_cooment.this, "Please enter correct Job", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(Upload_image_and_cooment.this, Upload_image_and_cooment.class);
                startActivity(in);
                finish();
            } else {

                //ed.putString(Utility.JOB_ID_BILLABLE, company_name1).apply();
                Shared_Preference.setJOB_NAME_BILLABLE(Upload_image_and_cooment.this, company_name1);
                final Dialog choose_for_scan = new Dialog(Upload_image_and_cooment.this);
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
                        choose_for_scan.dismiss();
                       // enterjobid = company_name1;
                        opendilogforattachfileandimage_custom();


                    }
                });

                cancle.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(Upload_image_and_cooment.this, Upload_image_and_cooment.class);
                        startActivity(in);
                        finish();
                    }
                });
                choose_for_scan.show();
            }
        }
    }

    class get_company_name extends AsyncTask<Void, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(Upload_image_and_cooment.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ringProgressDialog.setMessage(getString(R.string.Loading_text));
            ringProgressDialog.setCancelable(false);
            ringProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Getcompany_name();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (ringProgressDialog.isShowing()) {
                    ringProgressDialog.dismiss();
                }
            } catch (Exception e) {
                e.getMessage();
            }

            if (company_id_list != null && company_id_list.size() > 0) {
                diloge_for_company_name();
            } else {
                Toast.makeText(Upload_image_and_cooment.this, "Some api_error occurred!", Toast.LENGTH_LONG).show();
            }
        }
    }

    class get_company_job_id extends AsyncTask<String, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(Upload_image_and_cooment.this);

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
            ringProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ringProgressDialog.dismiss();
            if (count == 1) {
                company_name.setText("");
                job_name.setText("");
                f2();
            } else {
                job_name.setText("");
                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Upload_image_and_cooment.this, R.layout.row, job_Name_list_Desc);
                job_name.setAdapter(dataAdapter1);
                job_name.setDropDownHeight(550);
            }

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

                final String Image_Link = list_ImageDescData.get(position).get(Utility.KEY_imagePath);
                Glide
                        .with(Upload_image_and_cooment.this)
                        .load(new File(Image_Link))     // Uri of the picture
                        .into(holder.thumbnail);
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

            holder.img_remove_item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_image_and_cooment.this);
                    LayoutInflater inflater = LayoutInflater.from(Upload_image_and_cooment.this);
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
                                Intent in = new Intent(Upload_image_and_cooment.this, MainActivity.class);
                                startActivity(in);
                                finishs();
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







/*                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Upload_image_and_cooment.this, SweetAlertDialog.WARNING_TYPE);
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
                            //To scroll list to 1st item after adding item to list
                            //         scroll_list_toTop();

                            if (list_ImageDescData.size() < 1) {
                                Intent in = new Intent(Upload_image_and_cooment.this, MainActivity.class);
                                startActivity(in);
                                finishs();
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

    private class async_getJobDetailsBySwoId extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Upload_image_and_cooment.this);
            pDialog.setMessage("Kindly wait");
            pDialog.setCancelable(false);
            pDialog.show();
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
                KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
                for (int j = 0; j < ks.getPropertyCount(); j++) {
                    ks.getProperty(j);
                }
                receivedString = ks.toString();
                receivedString = receivedString.substring(receivedString.indexOf("=") + 1, receivedString.indexOf(";"));
                Log.e("***detail by SWOID", receivedString);
            } catch (Exception e) {

                e.printStackTrace();
            }
            try {

                JSONObject jsonObject = new JSONObject(receivedString);
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
                String dealerID = js_obj.getString("dealerID");
                //  String login_dealerId = sp.getString(Utility.DEALER_ID, "");
                String login_dealerId = Shared_Preference.getDEALER_ID(Upload_image_and_cooment.this);
                if (!dealerID.equals(login_dealerId)) {
                    Toast.makeText(getApplicationContext(), "Please scan valid QR Code of SWO!", Toast.LENGTH_SHORT).show();
                    showtochoseswoorjob();
                    return;
                } else {
                    Dialog_ShowJobInfo_from_SWO(js_obj);
                }
            } catch (Exception e) {
                e.getMessage();
            }



        }

    }

    public class async_LinkUploadedFiles extends AsyncTask<String, Integer, String> {
        // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   progressDialog = new ProgressDialog(Upload_image_and_cooment.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            for (int i = 0; i < list_ImageDescData.size(); i++) {

                String Jobid_or_swoid = list_ImageDescData.get(i).get(Utility.KEY_Jobid_or_swoid);
                String dataa = list_ImageDescData.get(i).get(Utility.KEY_dataa);
                String imagename = list_UploadImageName.get(i);//list_ImageDescData.get(i).get(Utility.KEY_imagename);
                String image_size = list_imageSize.get(i);
                String name = list_ImageDescData.get(i).get(Utility.KEY_name);
                String clid = list_ImageDescData.get(i).get(Utility.KEY_clid);
                String job_or_swo = list_ImageDescData.get(i).get(Utility.KEY_job_or_swo);

                String UploadedPhotoID = enterdata(Jobid_or_swoid, dataa, imagename, dataa, image_size,
                        name, clid, job_or_swo);
            }
            result = generate_event(list_ImageDescData.get(0).get(Utility.KEY_Jobid_or_swoid), list_ImageDescData.get(0).get(Utility.KEY_clid),
                    list_ImageDescData.get(0).get(Utility.KEY_job_or_swo), String.valueOf(list_ImageDescData.size()));


            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //  progressDialog.dismiss();

            for (int i = 0; i < list_TempImagePath.size(); i++) {
                String TempImagePath = list_TempImagePath.get(i);
                Utility.delete(TempImagePath);
            }

            list_TempImagePath.clear();
            list_path.clear();
            list_ImageDescData.clear();
            list_imageSize.clear();
            list_UploadImageName.clear();
            try {
                if (uploadProgressDialog.isShowing())
                    uploadProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
            try {
                if (result.equalsIgnoreCase("1")) {
                    //  Toast.makeText(Upload_image_and_cooment.this, "Image(s) uploaded successfully!", Toast.LENGTH_SHORT).show();
                    dialog_sharePhotos();
                } else {
                    dialog_photo_upload_failed();
                }
            } catch (Exception e) {
                e.getMessage();
            }

        }

    }

}