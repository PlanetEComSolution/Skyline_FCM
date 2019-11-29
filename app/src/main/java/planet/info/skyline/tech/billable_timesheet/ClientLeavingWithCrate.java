package planet.info.skyline.tech.billable_timesheet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

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
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.REST_API_Client;
import planet.info.skyline.old_activity.AppConstants;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.old_activity.CustomMultiPartEntity;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.ProgressRequestBody;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.CameraUtils;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.util.Utility;
import retrofit2.Call;
import retrofit2.Callback;

import static planet.info.skyline.network.Api.API_UploadPhotoTech;
import static planet.info.skyline.network.Api.API_UploadPhotoTech_new;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class ClientLeavingWithCrate extends BaseActivity implements ProgressRequestBody.UploadCallbacks{
    public ProgressDialog pDialog;
    List<String> id_list = new ArrayList<>();
    String new_location;
    String createid;
    ImageView merchantname, missing, homeacti;
    File file1;
    String path, fname = "";
    int remaini;
    String encode_String = "";
    TextView  timerValue, clientnam;
    Button nofr,yesfor;

    SharedPreferences sp;
    Editor ed;
    DisplayImageOptions options;
    TextView clientid, clientname;
    String webhit = "", webhitforupdate = "", webhitforupdate_photo = "";
    String saveonnewloc = "", scancreateid = "", scancreateloc = "",
            scancreateid_comma = "", scancreateid_comma_copy = "";
    String recentscancrat = "";
    String ids = "";
    RelativeLayout mains;
    String check_crates = "";
    int sellects_opttion = 0;
    int crate_count = 0;
    String imageload = URL_EP2 + "/WebService/techlogin_service.asmx?op=UploadPhotoTechServer";
    String imagename = "";
    String remove_crete = "";
    String URL_Client = "";
    int check_gotocatch;
    String linkforconect = "";
    String urlofwebservice2 = URL_EP2 + "/WebService/techlogin_service.asmx?op=UploadPhotoTech";
    String urlofwebservice = URL_EP2 + "/WebService/techlogin_service.asmx?op=stop_tech_with_resion";
    String reasonsstring = "";
    String location_name = "", locationorbin = "", location = "";
    Uri mImageCaptureUri;
    int serverResponseCode;
    HashMap<String, String> kk;
    List<HashMap<String, String>> noteList = new ArrayList<HashMap<String, String>>();
    Map<String, HashMap<String, String>> noteList2 = new HashMap<String, HashMap<String, String>>();
    List<HashMap<String, String>> SelectedCrateList;

    String crate_name = "";
    long totalSize = 0;
    ArrayList<String> list_path = new ArrayList<>();
    ArrayList<String> list_imageSize = new ArrayList<>();
    ArrayList<String> list_UploadImageName = new ArrayList<>();
    int crateNumber;
    String mainCrate_Name;
    String mainCrate_Id;
    String insideCrate;
    AlertDialog alertDialog;
    ProgressDialog pDailog;
    ArrayList<HashMap<String, String>> list_CratesPackInto;
    String BinId;
    boolean isBin = false;
    boolean isFreight = false;
    boolean isArea = false;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String taskida = intent.getStringExtra("value");
            String type = intent.getStringExtra("type");// /type
            timerValue.setText(taskida);

        }
    };
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static String imageStoragePath;


    ProgressDialog uploadProgressDialog;
    int Count_Image_Uploaded = 0;
 //   API_Interface APIInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alercratebackonrack_new);
        nofr = findViewById(R.id.Btn_No);
        yesfor =  findViewById(R.id.Btn_Yes);
        mains = (RelativeLayout) findViewById(R.id.mains);
        mains.setVisibility(View.INVISIBLE);
        clientid = (TextView) findViewById(R.id.textView1rr);
        clientname = (TextView) findViewById(R.id.textViewclientname);
        merchantname = (ImageView) findViewById(R.id.merchantname);
        merchantname.setVisibility(View.VISIBLE);
        merchantname.setImageResource(R.drawable.exhibitlogoa);
        timerValue = (TextView) findViewById(R.id.timer);

        pDialog = new ProgressDialog(ClientLeavingWithCrate.this);
        pDialog.setMessage(getString(R.string.Loading_text));

        pDialog.setCancelable(false);

        homeacti = (ImageView) findViewById(R.id.homeacti);

        homeacti.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(ClientLeavingWithCrate.this,
                                Clock_Submit_Type_Activity.class);

                        startActivity(ii);
                        // finish();

                    }
                });

        clientnam = (TextView) findViewById(R.id.textView1);

        sp = getApplicationContext().getSharedPreferences("skyline",
                MODE_PRIVATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver, new IntentFilter("subtask"));
        ed = sp.edit();
        missing = (ImageView) findViewById(R.id.missing);
        missing.setVisibility(View.VISIBLE);

     //   String imageloc = sp.getString("imglo", "");
        String imageloc =	Shared_Preference.getCLIENT_IMAGE_LOGO_URL(this);
        clientnam.setVisibility(View.VISIBLE);
        if (imageloc.equals("") || imageloc.equalsIgnoreCase("")) {
            missing.setVisibility(View.GONE);
            clientnam.setVisibility(View.VISIBLE);
        } else {
            imageLoadery.displayImage(imageloc, missing, options);
            clientnam.setVisibility(View.INVISIBLE);
        }
     //   String nam = sp.getString("tname", "");
        String nam =  Shared_Preference.getLOGIN_USERNAME(this);
       // String nams = sp.getString("name", "");
        String nams  = Shared_Preference.getCLIENT_NAME(this);

      //  String names = sp.getString("client_name", "");
        String names =  Shared_Preference.getCLIENT(this);
        if (names != "") {
            clientnam.setText(names);
            clientname.setText(names);
        } else {
            clientnam.setText(nams);
            clientname.setText(nams);
        }
        clientname.setVisibility(View.INVISIBLE);

        //clientnam.setText(nams);
     //   String id = sp.getString("clientid", "");
        String id= Shared_Preference.getLOGIN_USER_ID(this);
        clientid.setText(id);
        clientid.setVisibility(View.INVISIBLE);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.skylinelogopng)

                .showImageForEmptyUri(R.drawable.skylinelogopng)
                .showImageOnFail(R.drawable.skylinelogopng).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage(getApplicationContext().getPackageName());
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 1);
        } catch (Exception e) {

            Uri marketUri = Uri
                    .parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }

        nofr.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(
                            "com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 1);

                } catch (Exception e) {

                    Uri marketUri = Uri
                            .parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                            marketUri);
                    startActivity(marketIntent);

                }

            }
        });
        yesfor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 2);

                } catch (Exception e) {

                    Uri marketUri = Uri
                            .parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                            marketUri);
                    startActivity(marketIntent);

                }

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String result = "";

                String Client_id = "";

                try {
                    String[] gg = null;
                    String contents = data.getStringExtra("SCAN_RESULT");
                    //nks
                    if (contents.contains("^")) {
                        contents = contents.replaceAll(Pattern.quote("^"), "#");//nks
                    }
                    //nks
                    if (contents.contains("_crate_name=")) {
                    } else {
                        Toast.makeText(getApplicationContext(), "Please scan valid QR Code of Crate!", Toast.LENGTH_LONG).show();
                        qcanqrcode(1);
                        return;
                    }

                    if (contents.contains("loc=")) {
                        String hom = contents;// "exhibitpower.com/admin/inventory/qrcrates1.php?act=edit_id=671_client=224_loc=S14-3-0";
                        result = hom.substring(hom.indexOf("_id=") + 4, hom.indexOf("_client"));
                        crate_name = hom.substring(hom.indexOf("_crate_name=") + 12, hom.length());
                        Client_id = hom.substring(hom.indexOf("client="), hom.indexOf("_loc"));
                        Client_id = Client_id.substring(Client_id.indexOf("=") + 1);
                    }

                    createid = result;
                    if (crate_name == null || crate_name.equals("")) {
                        getCrateName(createid,Client_id);
                    } else {
                        addThisCrateToList(Client_id);
                    }

                } catch (Exception e) {
                }


            } else if (requestCode == AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {


                path=imageStoragePath;
                ///change orientation of captured photo
                int orientation = Utility.getExifOrientation(path);
                try {
                    if (orientation == 90) {
                       // list_TempImagePath.add(path);
                        Bitmap bmp = Utility.getRotatedBitmap(path, 90);
                        path = Utility.saveImage(bmp);
                    }
                } catch (Exception e) {
                    e.getCause();
                }
                ///change orientation
                list_path.add(path);
                if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {
                  //  new asyntaskupload1().execute();
                    multipartImageUpload();


                } else {
                    Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ClientLeavingWithCrate.this, Clock_Submit_Type_Activity.class));
                    finish();
                }

            } else if (requestCode == 2) {
                String result = "";
                try {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    //nks
                    if (contents.contains("^")) {
                        contents = contents.replaceAll(Pattern.quote("^"), "#");//nks
                    }
                    //nks
                    Log.d("BHANU_Client_name", contents);
                    String hom = contents;

                    if ((hom.contains(",") || (hom.contains("crate_name")))) {
                        validation_diloge();

                    } else {

/*
 bin
https://beta.ep2.businesstowork.com/ep1//superadmin/inventory/warehouse-qr-code-new.php?type=bin_id=992_aid=260_w=473_wn=TR1221

area
https://beta.ep2.businesstowork.com/ep1//superadmin/inventory/show-qr-warehouse-one.php?type=area_id=39_loc=Client

freight
id :586Vname :Pankaj Saini:Cat :Service Production CoordinatorScanValue : 1

*/

                        if (hom.contains("type=bin_id")) {  //bin

                            isBin = true;
                            isFreight = false;
                            isArea = false;
                            BinId = hom.substring(hom.indexOf("bin_id=") + 7, hom.indexOf("_aid"));
                            GetBinName(BinId);

                        } else if (hom.contains("Vname")) {  //freight vendor
                            isBin = false;
                            isFreight = true;
                            isArea = false;
                            String[] tokens = hom.split(":");
                            scancreateloc = tokens[2].trim();
                            showdialogforupdateconfirm();
                        } else if (hom.contains("type=area_id")) {  //area
                            isBin = false;
                            isFreight = false;
                            isArea = true;
                            BinId = hom.substring(hom.indexOf("area_id=") + 8, hom.indexOf("_loc"));
                            GetAreaName(BinId);

                        } else if (hom.contains("id") && hom.contains("aid")) { //Bin, old qr code
                            isBin = true;
                            isFreight = false;
                            isArea = false;
                            BinId = hom.substring(hom.indexOf("id=") + 3, hom.indexOf("_aid"));
                            GetBinName(BinId);

                        } else {
                            Toast.makeText(ClientLeavingWithCrate.this,
                                    "Please scan valid QR Code of Bin/Freight Vendor/Area!", Toast.LENGTH_LONG)
                                    .show();
                        }


                    }


                } catch (Exception ee) {
                    Toast.makeText(ClientLeavingWithCrate.this,
                            "Please scan valid QR Code of Bin/Freight Vendor/Area!", Toast.LENGTH_LONG)
                            .show();
                }

            } else if (requestCode == 3) {

            } else if (requestCode == 5) {
                Intent i = new Intent(getApplicationContext(),
                        SubmitTimesheet.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            } else if (requestCode == 6) {
                try {
                    String result = "";
                    String[] gg = null;
                    String contents = data.getStringExtra("SCAN_RESULT");
                    String hom = contents;
                    if (contents.contains("_wn"))//bin
                    {
                        location_name = hom.substring(hom.indexOf("_wn=") + 4, hom.length());
                    } else {

                        try {
                            //LocationType="Workstation";
                            gg = contents.split("loc=");
                            String loc = gg[1];
                            location_name = loc;
                        } catch (Exception e) {
                            location_name = "";

                        }


                    }
                } catch (Exception e) {

                }
//				dialog_Image_Description();
                try {
                    String result = "";
                    String[] gg = null;
                    String contents = data.getStringExtra("SCAN_RESULT");
                    String hom = contents;
                    if (contents.contains("type=area"))//bin
                    {
                        locationorbin = "area";
                        location = hom.substring(hom.indexOf("area_id=") + 8, hom.indexOf("_loc"));
                    } else if (contents.contains("type=bin_id")) {

                        try {
                            locationorbin = "bin";
                            location = hom.substring(hom.indexOf("bin_id=") + 7, hom.indexOf("_aid"));
                        } catch (Exception e) {
                            location = "";

                        }
                    }
                } catch (Exception e) {

                }

                //
                String result = "";
                try {

                    String[] gg = null;
                    String contents = data.getStringExtra("SCAN_RESULT");
                    String hom = contents;
                    if (hom.contains("_wn=")) {
                        result = hom.substring(hom.indexOf("_wn=") + 4,
                                hom.length());
                        String ssdcf = result;
                        scancreateloc = result;
                    }

                    if (contents.contains("loc=")) {
                        gg = contents.split("loc=");
                        scancreateloc = gg[1];
                    }
                    confirmcratemove(scancreateloc);

                } catch (Exception e) {

                }
            }

            /**/

            else if (requestCode == 10) {


                String result = "";
                String cratename = "";

                try {

                    String contents = data.getStringExtra("SCAN_RESULT");

                    //nks
                    if (contents.contains("^")) {
                        contents = contents.replaceAll(Pattern.quote("^"), "#");//nks
                    }
                    //nks


                    if (contents.contains("_crate_name=")) {

                    } else {
                        Toast.makeText(getApplicationContext(), "Please scan valid QR Code of Crate!", Toast.LENGTH_LONG).show();
                        qcanqrcode(10);
                        return;
                    }
                    if (contents.contains("loc=")) {

                        // String ll = contents;

                        String hom = contents;// "exhibitpower.com/admin/inventory/qrcrates1.php?act=edit_id=671_client=224_loc=S14-3-0";
                        result = hom.substring(hom.indexOf("_id=") + 4, hom.indexOf("_client"));
                        //  recentscancrat = result;
                        cratename = hom.substring(hom.indexOf("_crate_name=") + 12, hom.length());
                    }

                    createid = result;
                    if (createid.equals(mainCrate_Id)) {
                        //add the main crate to the list
                        HashMap<String, String> hasmap = new HashMap<String, String>();
                        hasmap.put("id", result);
                        hasmap.put("name", cratename);
                        noteList.add(hasmap);

                        //remove the inside crate from the list
                        for (int i = 0; i < noteList.size(); i++) {
                            String crateId = noteList.get(i).get("id");
                            if (crateId.equals(insideCrate)) {
                                noteList.remove(i);
                            }
                        }

                        String msg = "Main crate " + mainCrate_Name + " is added to the list!\nPlease select it to move!";
                        Show_confirmation_Dialog(msg);

                    } else {

                        ShowValidationDialog("Please scan " + mainCrate_Name + " QR code!", 10);
                    }


                } catch (Exception e) {
                    e.getMessage();
                }
            }
        } else {
            finish();
        }
    }


    public void getCrateName(final String Crate_id,final String clientId) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(ClientLeavingWithCrate.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }

        //nks
        String url = SOAP_API_Client.URL_EP1 + Api.API_FETCH_CRATE_NAME + Crate_id;
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                //nks
                if (progressDailog.isShowing()) {
                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                try {

                    JSONArray jsonArray = obj.getJSONArray("cds");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    crate_name = jsonObject.getString("crate_number");
                    addThisCrateToList(clientId);

                } catch (Exception e) {
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {

                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
                Toast.makeText(ClientLeavingWithCrate.this, "Some api error occurred!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ClientLeavingWithCrate.this, MainActivity.class));
                finish();
            }
        });
        AppController.getInstance().addToRequestQueue(bb);

    }


    private void addThisCrateToList(String _clientID){
        HashMap<String, String> hasmap = new HashMap<>();
        if (id_list.size() > 0) {
            if (!(id_list.contains(createid))) {
                id_list.add(createid);
                hasmap.put("id", createid);
                hasmap.put("name", crate_name);
                hasmap.put("Client_id", _clientID);
                noteList.add(hasmap);
            } else {
                Toast.makeText(getApplicationContext(), "This crate is already added!", Toast.LENGTH_SHORT).show();
            }
        } else {
            id_list.add(createid);
            hasmap.put("id", createid);
            hasmap.put("name", crate_name);
            hasmap.put("Client_id", _clientID);
            noteList.add(hasmap);
        }

        workornot();

    }

    public void showdialogforupdateconfirmnew(String loc) {
        scancreateloc = loc;
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.alerdialforupdatelocat_new);
        showd.setCancelable(false);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // showd.setTitle("");
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
//
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
//		texrtdesc.setText("Has crate " + scancreateid + " been put in "
//				+ scancreateloc + " ?");

        crate_name = "";
        scancreateid = "";
        /**/
        for (int i = 0; i < SelectedCrateList.size(); i++) {
            crate_name = crate_name + SelectedCrateList.get(i).get("name") + ",";
            scancreateid = scancreateid + noteList.get(i).get("id") + ",";
        }
        crate_name = crate_name.substring(0, crate_name.lastIndexOf(","));
        scancreateid = scancreateid.substring(0, scancreateid.lastIndexOf(","));
        /**/

       /* texrtdesc.setText("Has Crate(s) " + crate_name + " been put in "
                + scancreateloc + " ?");*/

        String message = "Please confirm that you have moved " + crate_name + " crate(s) to " + scancreateloc;
        texrtdesc.setText(message);

        ImageView close = (ImageView) showd.findViewById(R.id.close);

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

        nofo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(
                            "com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 2);

                } catch (Exception e) {

                    Uri marketUri = Uri
                            .parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                            marketUri);
                    startActivity(marketIntent);

                }


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        });

        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {


                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    showprogressdialog();//
                   // String tnam = sp.getString("tname", "arvind");
                    String tnam =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                    saveonnewloc = URL_EP1 + Api.API_CRATE_LOCATION_UPDATE
                            + scancreateid
                            + "&LocationId="
                            + scancreateloc
                            + "&techname=" + tnam;
                    saveonnewloc = saveonnewloc.replace(" ", "%20");
                    Log.d("move to bin", saveonnewloc);
                    updatelocation(saveonnewloc);
                 //   String jobid = sp.getString("jobid", "");
                    String jobid =   Shared_Preference.getSWO_ID(ClientLeavingWithCrate.this);

                    // webhit="http://exhibitpower2.com/Register/auto_generate_event2.aspx?swo_id="+jobid+"&status=start";//+"&"+jobid;

                    webhitforupdate = URL_EP2 + "/Register/auto_generate_event2.aspx?swo_id="
                            + jobid + "&status=update";// +"&"+jobid;


                } else {
                    Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }


        });

        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void showdialogforupdateconfirm() {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.alerdialforupdatelocat_new);
        showd.setCancelable(false);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);

        crate_name = "";
        scancreateid = "";

        for (int i = 0; i < SelectedCrateList.size(); i++) {
            crate_name = crate_name + SelectedCrateList.get(i).get("name") + ",";
            scancreateid = scancreateid + noteList.get(i).get("id") + ",";
        }
        crate_name = crate_name.substring(0, crate_name.lastIndexOf(","));
        scancreateid = scancreateid.substring(0, scancreateid.lastIndexOf(","));


        //


//
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);


        String message = "Please confirm that you have moved " + crate_name + " crate(s) to " + scancreateloc;
        texrtdesc.setText(message);

        ImageView close = (ImageView) showd.findViewById(R.id.close);

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

        nofo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(
                            "com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 2);

                } catch (Exception e) {

                    Uri marketUri = Uri
                            .parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                            marketUri);
                    startActivity(marketIntent);

                }


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        });

        yesfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {


                    try {
                        showd.dismiss();

                    } catch (Exception e) {
                        e.getMessage();
                    }


                    showprogressdialog();//
                   // String tnam = sp.getString("tname", "arvind");
                    String tnam =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                    if (isBin) {
                        saveonnewloc = URL_EP1 + Api.API_CRATE_INFO_UPDATE +"cratesid=" + scancreateid + "&type=bin&aid=" + BinId + "&tech_id=" + tnam + "&print=yes";
                    } else if (isArea) {
                        saveonnewloc = URL_EP1 + Api.API_CRATE_INFO_UPDATE +"cratesid=" + scancreateid + "&type=area&aid=" + BinId + "&tech_id=" + tnam + "&print=yes";
                    } else if (isFreight) {
                        saveonnewloc = URL_EP1 + Api.API_CRATE_INFO_UPDATE +"cratesid=" + scancreateid + "&type=freight&fname=" + scancreateloc + "&tech_id=" + tnam;
                    }
                    saveonnewloc = saveonnewloc.replace(" ", "%20");
                    Log.d("CrateMoveApi", saveonnewloc);
                    updatelocation(saveonnewloc);
                } else {
                    Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });

        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }



    public void showdialogforcomplete() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientLeavingWithCrate.this);
        LayoutInflater inflater = LayoutInflater.from(ClientLeavingWithCrate.this);
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);
        final TextView title = dialogView.findViewById(R.id.textView1rr);
        final  TextView message = dialogView.findViewById(R.id.texrtdesc);

        final Button positiveBtn = dialogView.findViewById(R.id.Btn_Yes);
        final Button negativeBtn = dialogView.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setVisibility(View.INVISIBLE);
        // dialogBuilder.setTitle("Device Details");
        title.setText("Confirm job completion");
        message.setText("Is the job completely finished?");
        positiveBtn.setText("Yes,Stop Clock");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {

                    ///////there is some wrong by Aman kaushik
                    //	stopService(new Intent(ClientLeavingWithCrate.this, Timerclass.class));
                   // String dd = sp.getString("jobid", "");
                    String dd =   Shared_Preference.getSWO_ID(ClientLeavingWithCrate.this);
                 //   String clientidme = sp.getString("clientid", "");
                    String clientidme= Shared_Preference.getLOGIN_USER_ID(ClientLeavingWithCrate.this);
                 //   String tech = sp.getString("tname", "empty");
                    String tech =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                    Long jj = System.currentTimeMillis();
                    String sd = jj.toString();
                    Random ss = new Random();
                    int i1 = ss.nextInt(99999) + 2;
                    sd = String.valueOf(i1);
                    webhit = URL_EP2 + "/Register/auto_generate_event2.aspx?done_by="
                            + clientidme
                            + "&swo_id="
                            + dd
                            + "&msg="
                            + tech
                            + " Has completely finished the job "
                            + "&status=3";
                    webhit = webhit.replace(" ", "%20");
                    String dh = webhit;
                    String de = dh;
                    getjsonobject11();
                 //   ed.putLong("deviceofftime", 0).commit();

                 //   Boolean bb = sp.getBoolean("billable", false);

                    Intent i = new Intent(getApplicationContext(),
                            SubmitTimesheet.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();


    }

    public void getjsonobject11() {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, webhit, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject obj) {

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.e("api_error", arg0.toString());

            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }


    public void slotmovcess() {
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, linkforconect, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject obj) {
                        showmain();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                showmain();

            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }

    public void showprogressdialog() {
        if (pDialog.isShowing()) {

        } else {


            try {
                pDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }


        }

    }

    public void hideprogressdialog() {
        if (pDialog.isShowing()) {
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }

        } else {

        }

    }

    public String getlocation(String links) {


        final ProgressDialog ringProgressDialog = new ProgressDialog(ClientLeavingWithCrate.this);
        ringProgressDialog.setMessage("Kindly wait...");
        ringProgressDialog.setCancelable(false);
        try {
            ringProgressDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }

        JsonObjectRequest updatess = new JsonObjectRequest(Method.GET, links,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsono) {
                try {
                    ringProgressDialog.dismiss();
                } catch (Exception e) {
                }

                try {
                    new_location = jsono.getString("cds");
                    Log.d("method_id", new_location);
                    showdialogforupdateconfirmnew(new_location);
                    //new_location=scan
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        });
        AppController.getInstance().addToRequestQueue(updatess);
        return new_location;
    }


    public void updatelocation(String links) {
        JsonObjectRequest updatess = new JsonObjectRequest(Method.GET, links,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsono) {
                hideprogressdialog();
                showtoast();
                showdialogforcomplete();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                //
                hideprogressdialog();
                showdialogforcomplete();

            }
        });
        AppController.getInstance().addToRequestQueue(updatess);
    }



    public void showtoast() {
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));

        // set a dummy image
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.apply);

        // set a message
        // TextView text = (TextView) layout.findViewById(R.id.text);
        // text.setText("Button is clicked!");

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 120);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }



    public void Dialog_Show_All_Crates() {
        if (noteList != null && noteList.size() > 0) {
            final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
            showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            showd.setContentView(R.layout.putmoveclient_new_2);
            showd.setCancelable(false);
            showd.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));

            // crate_count = 0;
            // remove_crete = "";
            SelectedCrateList = new ArrayList<>();

            LinearLayout checkboxlayout = (LinearLayout) showd
                    .findViewById(R.id.first4);
            Button Submit = (Button) showd.findViewById(R.id.submits);
            Submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (sellects_opttion == 0 || SelectedCrateList.size() == 0) {
                        Toast.makeText(getApplicationContext(),
                                "Please select one!", Toast.LENGTH_LONG).show();
                    } else if (sellects_opttion == 1) {
                        showd.dismiss();

                        //   mains.setVisibility(View.VISIBLE);
                        // for crate inside another crate

                        crateNumber = 0;
                        if (SelectedCrateList.size() > crateNumber) {
                            Check_This_Crate_Packed_In_Other_Crate();
                        }


                    } else if (sellects_opttion == 2) {
                        showd.dismiss();
                        takeaphoto();
                    }
                }
            });
            CheckBox checkBox;
            class chBxOnclick implements OnClickListener {
                public void onClick(View v) {
                    CheckBox cc = (CheckBox) v;
                    if (cc.isChecked()) {
                        cc.getTag();
                        //    crate_count = crate_count + 1;
                        // remove_crete = remove_crete + "," + cc.getTag();

                        //
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", String.valueOf(cc.getTag()));
                        hashMap.put("name", String.valueOf(cc.getText()));
                        SelectedCrateList.add(hashMap);
                        Log.d("---Selected Crate--", SelectedCrateList.toString());
                        //

                    } else {
                        //  crate_count = crate_count - 1;
                        //  remove_crete = remove_crete.replace("" + cc.getTag(), "");

                        //
                        for (int i = 0; i < SelectedCrateList.size(); i++) {
                            HashMap<String, String> hashMap = SelectedCrateList.get(i);
                            if (hashMap.get("id").equals(cc.getTag())) {
                                SelectedCrateList.remove(i);
                                break;
                            }
                        }
                        //
                        Log.d("---Selected Crate--", SelectedCrateList.toString());
                        //
                    }


                }
            }

            for (int count = 0; count < noteList.size(); count++) {
                checkBox = new CheckBox(this);
                checkBox.setId(count);
                String name = noteList.get(count).get("name");
                checkBox.setText(name);
                checkBox.setTag(noteList.get(count).get("id"));
                checkBox.setOnClickListener(new chBxOnclick());
                checkboxlayout.addView(checkBox);

            }


            Spinner dropdown = (Spinner) showd.findViewById(R.id.spinner1);  /// here is my breaking point
            String[] items = new String[]{"--Select One--",
                    "Put the Crate(s) back in a\nBin/Freight Vendor",
                    "Client is leaving with the Crate(s)"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                           long arg3) {
                    if (pos == 0) {
                        sellects_opttion = 0;
                    } else
                        sellects_opttion = pos;
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    Toast.makeText(getApplicationContext(), "Please select one!",
                            Toast.LENGTH_LONG).show();
                }
            });


            showd.show();
        } else {

            showdialogforcomplete();
        }


    }


    public void workornot() {
        Log.d("BHANU_Client_name", "yes it is call its");
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        TextView yes_text = (TextView) showd.findViewById(R.id.Btn_No);
        TextView no_text = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        //texrtdesc.setText(cratid + "  Scan another crate?");
        texrtdesc.setText(" Scan another crate?");
        yes_text.setText("  Yes  ");
        no_text.setText("  No  ");
        close.setVisibility(View.GONE);
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Crate No. : " + crate_name);
        yes_text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sacancrtaeandopenanother(1);

                // finish();

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


                finish();

            }
        });
        no_text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("--Scanned crate list--", noteList.toString());

                SelectedCrateList = new ArrayList<>();//make a copy of scanned crates


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();

                // finish();
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void takeaphoto() {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        showd.setCancelable(false);
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        texrtdesc.setText("Take a photo of the Clients with the crates");
        nofo.setText("  Yes  ");
        yesfo.setText("  No  ");
        yesfo.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Please select");
        close.setVisibility(View.INVISIBLE);
        nofo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }



                if (CameraUtils.checkPermissions(getApplicationContext())) {
                    captureImage();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }





            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }



    public void confirmcratemove(String loc) {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        if (remove_crete.startsWith(","))
            remove_crete = remove_crete.substring(1, remove_crete.length());

        String[] vv = remove_crete.split(",");
        int lens = vv.length;
        String name = "";
        for (int j = 0; j < lens; j++) {
            try {
                name = name + noteList2.get(vv[j]).get("name") + ",";
            } catch (Exception e) {

            }
        }
        if (name.endsWith(",")) {
            name = name.substring(0, name.length() - 1);
        }
        texrtdesc.setText("Crate " + name + " has been moved to " + loc);
        //texrtdesc.setText("Crate " + crte + " has been moved to " + loc);
        nofo.setText("  Yes  ");
        // yesfo.setText("  No  ");
        yesfo.setVisibility(View.GONE);
        close.setVisibility(View.INVISIBLE);
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("Please select");
        close.setVisibility(View.INVISIBLE);
        nofo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (remove_crete.startsWith(","))
                    remove_crete = remove_crete.substring(1, remove_crete.length());
            //    String clientida = sp.getString("tname", "");
                String clientida =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                linkforconect = URL_EP1 + Api.API_CRATE_INFO_UPDATE +"cratesid=" + remove_crete + "&type=" + locationorbin + "&aid=" + location + "&tech_id=" + clientida + "&print=yes";
                //linkforconect="http://exhibitpower.com/crate_info_updates.php?cratesid="+idd+"&type="+LocationType+"&aid="+LocationId+"&tech_id="+clientid+"&print=yes";
                /*showd.dismiss();
                finish();*/
                linkforconect = linkforconect.replace(" ", "%20");
                //Toast.makeText(getApplicationContext(), linkforconect, 10000).show();
                slotmovcess();


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                //
                // finish();

            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void sacancrtaeandopenanother(int bits) {
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage(getApplicationContext().getPackageName());
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, bits);

        } catch (Exception e) {

            Uri marketUri = Uri
                    .parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
    }

    public void showmain() {

        Log.d("BHANU_Client_name", "mains");
        Intent i = new Intent(getApplicationContext(),
                SubmitTimesheet.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

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



    public void eventgenerate_for_photo() {

        showprogressdialog();
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET,
                webhitforupdate_photo, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                hideprogressdialog();
                showmain();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

                hideprogressdialog();
                showmain();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);
    }




    public void enterdata(String a, String b, String c, String d, String e,
                          String f, String g, String h) {

        check_gotocatch = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice2;
        final String SOAP_ACTION = KEY_NAMESPACE +API_UploadPhotoTech;
        final String METHOD_NAME = API_UploadPhotoTech;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        // request.addProperty("job_id",enterjobid );
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
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j); // if complex type is present then you can
                // cast this to SoapObject and if primitive
                // type is returned you can use toString()
                // to get actuall value.
            }
        } catch (Exception ed) {
        }
        if (check_gotocatch == 0) {
            try {

            } catch (Exception edd) {
            }

        }
    }



    public String generate_event(String job_swo_id, String tech_id, String type, String count) {
        String result = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice2;
        final String SOAP_ACTION = KEY_NAMESPACE + API_UploadPhotoTech_new;
        final String METHOD_NAME =API_UploadPhotoTech_new;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("job_swo_id", job_swo_id);
        request.addProperty("tech_id", tech_id);
        request.addProperty("type", type);
        request.addProperty("count", count);

        Log.d("billable photo event", request.toString());
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

    public void qcanqrcode(int c) {

        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage(getApplicationContext().getPackageName());
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, c);

        } catch (Exception e) {

            Uri marketUri = Uri
                    .parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
    }

    public void validation_diloge() {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.validation_new);
        showd.setCancelable(false);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView yesfo = (TextView) showd.findViewById(R.id.scann_again);
        ImageView close = (ImageView) showd.findViewById(R.id.close);

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
        yesfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 2);

                } catch (Exception e) {

                    Uri marketUri = Uri
                            .parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                            marketUri);
                    startActivity(marketIntent);

                }
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Check_This_Crate_Packed_In_Other_Crate() {

        if (pDailog == null || (!pDailog.isShowing())) {
            pDailog = new ProgressDialog(ClientLeavingWithCrate.this);
            pDailog.setMessage(getString(R.string.Loading_text));
            pDailog.setCancelable(false);
            pDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            try {
                pDailog.show();
            } catch (Exception e) {
                e.getMessage();
            }


        }
        final String CrateId = SelectedCrateList.get(crateNumber).get("id");
        final String CrateName = SelectedCrateList.get(crateNumber).get("name");

        //nks
        String url = URL_EP1 +Api.API_CHECK_CRATE + CrateId;
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //  pDailog.dismiss();
                //nks
                try {
                    if (pDailog != null && pDailog.isShowing()) {


                        try {
                            pDailog.dismiss();
                        } catch (Exception e) {
                            e.getMessage();
                        }


                    }
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1"))//means crate is packed within another crate
                    {


                        insideCrate = CrateId;
                        mainCrate_Name = jsonObject.getString("main_crate");
                        mainCrate_Id = jsonObject.getString("crt_id");
                        Show_Dialog_for_Main_Crate_Move(CrateName, CrateId, mainCrate_Name, mainCrate_Id);

                    } else {
                        Check_Crates_Packed_in_this_Crate(CrateId, CrateName);
                    }




                } catch (Exception e) {
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (pDailog.isShowing()) {

                    try {
                        pDailog.dismiss();

                    } catch (Exception e) {
                        e.getMessage();
                    }


                }

            }
        });
        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Show_confirmation_Dialog(String message) {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        ed = sp.edit();
        TextView nofo = (TextView) showd.findViewById(R.id.Btn_No);
        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView texrtdesc = (TextView) showd.findViewById(R.id.texrtdesc);
        // texrtdesc.setText("Please scan the crates you want to move");
        texrtdesc.setText(message);
        nofo.setText("  No  ");
        nofo.setVisibility(View.GONE);
        yesfo.setText("Ok");
        TextView textviewheader = (TextView) showd
                .findViewById(R.id.textView1rr);
        textviewheader.setText("");


        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();
            }
        });
        yesfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();

            }
        });


        try {
            showd.show();

        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Show_Dialog_for_Main_Crate_Move(final String Crate_Name, final String Crate_Id,
                                                final String mainCrate_Name, final String mainCrate_Id) {

        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_main_crate_move);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);

        TextView text_Main_crateName = (TextView) showd.findViewById(R.id.text_Main_crateName);
        TextView text_crateMsg = (TextView) showd.findViewById(R.id.textView1rr);


        Button btn_MoveMainCrate = (Button) showd.findViewById(R.id.btn_MoveMainCrate);
        Button btn_RemoveFromMainCrate = (Button) showd.findViewById(R.id.btn_RemoveFromMainCrate);
        Button btn_PackInAnotherCrate = (Button) showd.findViewById(R.id.btn_PackInAnotherCrate);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        text_Main_crateName.setText(mainCrate_Name);

        String sourceString = "The Crate " + Crate_Name + " is packed inside: " + "<b>" + mainCrate_Name + "</b> ";

        text_crateMsg.setText(Html.fromHtml(sourceString));
        btn_MoveMainCrate.setText("Please scan the " + mainCrate_Name + " QR Code to move " + mainCrate_Name + " and its contents.");
        btn_RemoveFromMainCrate.setText("Remove this crate from " + mainCrate_Name + " to Store/Ship alone.");
        btn_PackInAnotherCrate.setText("Pack this crate in a different crate.");

        btn_MoveMainCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                //check mainCrate is already added or not
                boolean isMainCrateAlreadySelected = false;
                for (int i = 0; i < noteList.size(); i++) {
                    String crateId = noteList.get(i).get("id");
                    if (crateId.equals(mainCrate_Id)) {
                        isMainCrateAlreadySelected = true;
                        break;
                    } else {
                        if (i == (noteList.size() - 1)) {
                            isMainCrateAlreadySelected = false;
                            break;
                        }
                    }

                }
                //
                if (!isMainCrateAlreadySelected) {

                    try {
                        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                        intent.setPackage(getApplicationContext().getPackageName());
                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                        startActivityForResult(intent, 10);

                    } catch (Exception e) {
                        e.getMessage();

                    }

                } else {

                    //if MAIN CRATE is already added then remove INSIDE CRATE from list
                    for (int i = 0; i < noteList.size(); i++) {
                        String crateId = noteList.get(i).get("id");
                        if (crateId.equals(Crate_Id)) {
                            noteList.remove(i);
                        }
                    }
                    //
                    Show_confirmation_Dialog("The main crate " + mainCrate_Name + " is already added! Please select it to move!");
                }

            }
        });

        btn_RemoveFromMainCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Remove_Crate_from_Main_Crate_to_Ship_Alone(Crate_Name, Crate_Id);
            }
        });


        btn_PackInAnotherCrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Get_Crates_to_Pack_in(Crate_Name, Crate_Id);
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


                Dialog_Show_All_Crates();
            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void ShowValidationDialog(String message, final int extra) {
        final Dialog diloge = new Dialog(ClientLeavingWithCrate.this); // open a diloge if usre Scann wrong qr code
        diloge.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diloge.setContentView(R.layout.validation_new);
        diloge.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        diloge.setCancelable(false);
        TextView scann = (TextView) diloge.findViewById(R.id.scann_again);
        TextView texrtdesc = (TextView) diloge.findViewById(R.id.texrtdesc);
        ImageView close = (ImageView) diloge.findViewById(R.id.close);

        texrtdesc.setText(message);

        scann.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    diloge.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage(getApplicationContext().getPackageName());
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, extra);

                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    marketIntent.setPackage(getApplicationContext().getPackageName());
                    startActivity(marketIntent);

                }
            }
        });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    diloge.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();
            }
        });


        try {
            diloge.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Remove_Crate_from_Main_Crate_to_Ship_Alone(final String Crate_name, String Crate_id) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(ClientLeavingWithCrate.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }


        sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
     //   String clientid = sp.getString("tname", "");
        String clientid =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
        //nks
        String url = URL_EP1 + Api.API_STORE_ALONE_CRATE + Crate_id + "&tech_id=" + clientid;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success"))//means crate is shipped alone
                    {
                        String Message = " Crate " + Crate_name + " has been shipped alone. \n Please select it to move!";
                        Show_confirmation_Dialog(Message);

                    } else {

                        Toast.makeText(ClientLeavingWithCrate.this, "Failed! Crate " + Crate_name + " has not been shipped alone!", Toast.LENGTH_LONG).show();
                        Dialog_Show_All_Crates();
                    }

                } catch (Exception e) {
                    e.getMessage();
                }

                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
                //   Toast.makeText(SlotMoveactivity.this, "Failed! Crate " + Crate_Name + " has not been shipped alone!", Toast.LENGTH_SHORT).show();
                //   Show_Dialog_for_More_Crate_Scan("Do you want to scan more Crate(s)?");
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Get_Crates_to_Pack_in(final String Crate_Name, final String Crate_Id) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(ClientLeavingWithCrate.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }


        list_CratesPackInto = new ArrayList<>();
        String Client_id = "";
        for (int i = 0; i < noteList.size(); i++) {
            if (Crate_Id.equals(noteList.get(i).get("id"))) {
                Client_id = noteList.get(i).get("Client_id");
                break;
            }
        }

        //nks
        String url = URL_EP1 +  Api.API_CHECK_CRATE_PACKED_IN_OTHER_CRATE  + Client_id + "&crate_id=" + Crate_Id;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONArray jsonArray = obj.getJSONArray("cds");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Crate_Id = jsonObject.getString("id");
                        String Crate_Number = jsonObject.getString("crate_number");
                        String Crate_Desc = jsonObject.getString("description");

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Crate_Id", Crate_Id);
                        hashMap.put("Crate_Number", Crate_Number);
                        hashMap.put("Crate_Desc", Crate_Desc);
                        list_CratesPackInto.add(hashMap);

                    }
                    Show_Dialog_for_Crates(Crate_Name, Crate_Id);

                } catch (Exception e) {
                    e.getMessage();
                }

                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {
                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
                Toast.makeText(ClientLeavingWithCrate.this, "Failed! Error getting crate list!", Toast.LENGTH_SHORT).show();
                Dialog_Show_All_Crates();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Show_Dialog_for_Crates(final String Crate_Name, final String Crate_Id) {

        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.radiogroupfortimesheat);
        showd.setCancelable(true);//nks
        showd.setCanceledOnTouchOutside(false);
        TextView title = (TextView) showd.findViewById(R.id.textView1rr);

        title.setText("Select Crate");
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

        RadioGroup rg = (RadioGroup) showd.findViewById(R.id.radioGroup1);
        try {
            for (int i = 0; i < list_CratesPackInto.size(); i++) {
                RadioButton button = new RadioButton(this);
                button.setId(Integer.parseInt(list_CratesPackInto.get(i).get("Crate_Id")));
                String CrateNumber = list_CratesPackInto.get(i).get("Crate_Number");
                String CrateDesc = list_CratesPackInto.get(i).get("Crate_Desc");
                String text = CrateNumber + "\n" + CrateDesc;
                button.setText(text);
                button.setChecked(false);
                rg.addView(button);
            }
        } catch (Exception e) {
            e.getMessage();
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String checked_CrateNum = (String) radioButton.getText();
                checked_CrateNum = checked_CrateNum.substring(0, checked_CrateNum.indexOf("\n"));


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Confirmation_CrateMoveIntoCrate(checkedId + "", checked_CrateNum, Crate_Name, Crate_Id);


            }
        });

        //nks
        showd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onBackPressed();
            }
        });

    }

    public void Dialog_Confirmation_CrateMoveIntoCrate(final String Checked_CrateId, final String Checked_CrateName, final String Crate_Name, final String Crate_id) {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        Button Btn_No = (Button) showd.findViewById(R.id.Btn_No);
        Button Btn_Yes = (Button) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView txt_message = (TextView) showd.findViewById(R.id.texrtdesc);
        String message = "Please confirm that you have moved " + Crate_Name + " crate to crate " + Checked_CrateName + "?";
        txt_message.setText(message);
        Btn_Yes.setText("Yes");
        Btn_No.setText("No");


        Btn_Yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();
                } catch (Exception e) {

                }

                Move_CrateInto_Crate(Crate_id, Checked_CrateId);


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


                Dialog_Show_All_Crates();

            }
        });

        Btn_No.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Dialog_Show_All_Crates();

            }
        });


        try {
            showd.show();

        } catch (Exception e) {
            e.getMessage();
        }


    }

    public void Move_CrateInto_Crate(final String CrateToMove, String CrateIntoMove) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(ClientLeavingWithCrate.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }


        sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
       // String techName = sp.getString("tname", "");
        String techName =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
        //nks
        String url = URL_EP1 + Api.API_MOVE_CRATE_TO_OTHER_LOCATION + techName + "&crate_id=" + CrateToMove + "&new_crate_id=" + CrateIntoMove;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success"))//means crate is moved successfully
                    {

                        //now remove this crate from list
                        for (int i = 0; i < noteList.size(); i++) {
                            if (noteList.get(i).get("id").equals(CrateToMove)) {
                                noteList.remove(i);
                                break;
                            }
                        }

                        String msg = "Crate moved successfully!";
                        Show_confirmation_Dialog(msg);//

                    } else {
                        Toast.makeText(ClientLeavingWithCrate.this, "Failed! Error moving crate into crate!", Toast.LENGTH_LONG).show();
                        Dialog_Show_All_Crates();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();

                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
                Toast.makeText(ClientLeavingWithCrate.this, "Failed! Error moving crate into crate!", Toast.LENGTH_SHORT).show();
                Dialog_Show_All_Crates();
            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void GetBinName(String BinId) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(ClientLeavingWithCrate.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        try {
            progressDailog.show();

        } catch (Exception e) {
            e.getMessage();
        }


        //nks
        String url = URL_EP1 + Api.API_GET_BIN_NAME + BinId;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks
                try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    scancreateloc = jsonObject.getString("win_loc");
                    if (!scancreateloc.equals("")) {
                        showdialogforupdateconfirm();
                    } else {
                        Toast.makeText(ClientLeavingWithCrate.this, "Error getting Bin Name!", Toast.LENGTH_LONG).show();
                        Dialog_Show_All_Crates();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
                Toast.makeText(ClientLeavingWithCrate.this, "Error getting Bin Name!", Toast.LENGTH_LONG).show();
                Dialog_Show_All_Crates();

            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void GetAreaName(String AreaId) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(ClientLeavingWithCrate.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }


        //nks
        String url = URL_EP1 + Api.API_GET_AREA_LOCATION + AreaId;
        url = url.replace(" ", "%20");
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks

                if (progressDailog.isShowing()) {
                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                try {

                    JSONArray jsonArray = obj.getJSONArray("cds");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    scancreateloc = jsonObject.getString("area_name");
                    String areaCode = jsonObject.getString("area_code");
                    String areaId = jsonObject.getString("a_id");

                    Log.e("Location_name", location_name);
                } catch (Exception e) {
                    e.getMessage();
                }
                showdialogforupdateconfirm();

               /* try {
                    JSONObject jsonObject = obj.getJSONObject("cds");
                    scancreateloc = jsonObject.getString("win_loc");
                    if (!scancreateloc.equals("")) {
                        showdialogforupdateconfirm();
                    } else {
                        Toast.makeText(ClientLeavingWithCrate.this, "Error getting Bin Name!", Toast.LENGTH_LONG).show();
                        Dialog_Show_All_Crates();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
             */


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }
                Toast.makeText(ClientLeavingWithCrate.this, "Error getting Bin Name!", Toast.LENGTH_LONG).show();
                Dialog_Show_All_Crates();

            }
        });

        AppController.getInstance().addToRequestQueue(bb);

    }

    public void Check_Crates_Packed_in_this_Crate(final String CrateId, final String CrateName) {
        final ProgressDialog progressDailog;
        progressDailog = new ProgressDialog(ClientLeavingWithCrate.this);
        progressDailog.setMessage(getString(R.string.Loading_text));
        progressDailog.setCancelable(false);
        progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //nks

        try {
            progressDailog.show();
        } catch (Exception e) {
            e.getMessage();
        }


        /*http://www.exhibitpower2.com/ep1/crateslist_insidecrate_webservice.php?id=3016*/
        String url = URL_EP1 + Api.API_GET_INSIDE_CRATE_LIST + CrateId;
        JsonObjectRequest bb = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                // TODO Auto-generated method stub
                //nks


                try {

                    if (progressDailog.isShowing()) {
                        try {
                            progressDailog.dismiss();
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    }

                    JSONArray jsonArray = obj.getJSONArray("data");

                    JSONArray jsonArray_Element1 = new JSONArray();
                    JSONArray jsonArray_Element2 = new JSONArray();
                    JSONObject jsonObject_main = jsonArray.getJSONObject(0);
                    try {

                        jsonArray_Element1 = jsonObject_main.getJSONArray("element1");
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    try {

                        jsonArray_Element2 = jsonObject_main.getJSONArray("element2");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    int totalCrates = jsonArray_Element1.length() + jsonArray_Element2.length();
                    if (totalCrates > 0) { //means crate contains other crate inside it.
                        ArrayList<HashMap<String, String>> List_InsideCrates = new ArrayList<>();

                        for (int i = 0; i < jsonArray_Element1.length(); i++) {

                            JSONObject jsonObject = jsonArray_Element1.getJSONObject(i);
                            String CrateId = jsonObject.getString("id");
                            String CrateName = jsonObject.getString("crate_number");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("CrateId", CrateId);
                            hashMap.put("CrateName", CrateName);
                            List_InsideCrates.add(hashMap);
                        }

                        for (int i = 0; i < jsonArray_Element2.length(); i++) {
                            JSONObject jsonObject = jsonArray_Element2.getJSONObject(i);
                            String CrateId = jsonObject.getString("id");
                            String CrateName = jsonObject.getString("crate_number");

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("CrateId", CrateId);
                            hashMap.put("CrateName", CrateName);
                            List_InsideCrates.add(hashMap);
                        }

                        Show_Dialog_for_Inside_Crates(List_InsideCrates, CrateName, CrateId);
                    } else {  //means crate not contains other crate inside it.

                        if ((SelectedCrateList.size() - 1) == crateNumber) {
                            if (pDailog != null && pDailog.isShowing()) {


                                try {
                                    pDailog.dismiss();
                                } catch (Exception e) {
                                    e.getMessage();
                                }


                            }
                            mains.setVisibility(View.VISIBLE);
                        } else {
                            crateNumber = crateNumber + 1;
                            if (SelectedCrateList.size() > crateNumber) {
                                Check_This_Crate_Packed_In_Other_Crate();
                            }
                        }

                    }

                } catch (Exception e) {
                    if (progressDailog.isShowing()) {
                        progressDailog.dismiss();

                    }
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                //nks
                if (progressDailog.isShowing()) {


                    try {
                        progressDailog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }

            }
        });
        AppController.getInstance().addToRequestQueue(bb);
    }

    public void Show_Dialog_for_Inside_Crates(ArrayList<HashMap<String, String>> List_InsideCrates, final String MainCrateName, final String MainCrateId) {
        final Dialog showd = new Dialog(ClientLeavingWithCrate.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.slotalert_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd.setCancelable(false);
        Button Btn_No = (Button) showd.findViewById(R.id.Btn_No);
        Button Btn_Yes = (Button) showd.findViewById(R.id.Btn_Yes);
        ImageView close = (ImageView) showd.findViewById(R.id.close);
        TextView txt_message = (TextView) showd.findViewById(R.id.texrtdesc);


        String InsideCrates = "";
        for (int i = 0; i < List_InsideCrates.size(); i++) {
            HashMap<String, String> hashMap = List_InsideCrates.get(i);
            InsideCrates = InsideCrates + hashMap.get("CrateName") + ",";
        }

        InsideCrates = InsideCrates.substring(0, InsideCrates.lastIndexOf(","));


        String message = "The Crate " + MainCrateName + " contains Crate(s):" + InsideCrates + ".\n" + "Do you want to move this Crate?";
        txt_message.setText(message);
        Btn_Yes.setText("Yes");
        Btn_No.setText("No");


        Btn_Yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    try {
                        showd.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    if ((SelectedCrateList.size() - 1) == crateNumber) {
                        if (pDailog != null && pDailog.isShowing()) {


                            try {
                                pDailog.dismiss();
                            } catch (Exception e) {
                                e.getMessage();
                            }


                        }
                        mains.setVisibility(View.VISIBLE);
                    } else {
                        crateNumber = crateNumber + 1;
                        if (SelectedCrateList.size() > crateNumber) {
                            Check_This_Crate_Packed_In_Other_Crate();
                        }
                    }

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


                if ((SelectedCrateList.size() - 1) == crateNumber) {
                    if (pDailog != null && pDailog.isShowing()) {


                        try {
                            pDailog.dismiss();
                        } catch (Exception e) {
                            e.getMessage();
                        }


                    }


                    mains.setVisibility(View.VISIBLE);
                } else {
                    crateNumber = crateNumber + 1;
                    if (SelectedCrateList.size() > crateNumber) {
                        Check_This_Crate_Packed_In_Other_Crate();
                    }
                }
            }
        });

        Btn_No.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                //remove the inside crate from the list
                for (int i = 0; i < noteList.size(); i++) {
                    String crateId = noteList.get(i).get("id");
                    if (crateId.equals(MainCrateId)) {
                        noteList.remove(i);
                    }
                }

                Dialog_Show_All_Crates();


            }
        });


        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }


    }



    private void multipartImageUpload() {
        if (Count_Image_Uploaded < list_path.size())
            Count_Image_Uploaded++;
        uploadProgressDialog = new ProgressDialog(ClientLeavingWithCrate.this);
        // uploadProgressDialog.setMessage("Uploading , Please wait..");
        uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_path.size() + ", Please wait..");
        uploadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        uploadProgressDialog.setIndeterminate(false);
        uploadProgressDialog.setProgress(0);
        uploadProgressDialog.setMax(100);
        uploadProgressDialog.setCancelable(false);
        uploadProgressDialog.show();

       // initRetrofitClient();

        /*damage report photo upload*/
        list_UploadImageName.clear();
        //   int statusCode = 0;
        totalSize = 0;


        try {
            /**/
            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[list_path.size()];
            for (int index = 0; index < list_path.size(); index++) {

                String path = list_path.get(index);
                File file = new File(path);
                ProgressRequestBody surveyBody = new ProgressRequestBody(file, this);
                surveyImagesParts[index] = MultipartBody.Part.createFormData("images[]", file.getName(), surveyBody);
                long Size = file.length();
                totalSize = totalSize + Size;
                list_imageSize.add(String.valueOf(Size / 1000));
            }
          //  String jid = sp.getString(Utility.KEY_JOB_ID_FOR_JOBFILES, "");
            String jid =  Shared_Preference.getJOB_ID_FOR_JOBFILES(this);

            String url = URL_EP2 + "/UploadFileHandler.ashx?jid=" + jid;
            /**/

            API_Interface APIInterface = REST_API_Client.getClient().create(API_Interface.class);

            Call<ResponseBody> req = APIInterface.uploadMedia(surveyImagesParts, url);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

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

                                if (list_UploadImageName != null & list_UploadImageName.size() > 0 & (!list_UploadImageName.get(0).equalsIgnoreCase(""))) {
                                    result = "1";
                                } else {
                                    result = "0";
                                }

                            } else {
                                result = "0";
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            result = "0";
                        }


                    } else {

                        result = "0";
                    }

                    for (int i = 0; i < list_path.size(); i++) {
                        String TempImagePath = list_path.get(i);
                        if (TempImagePath.contains("TempImage-")) {
                            Utility.delete(TempImagePath);
                        }
                    }

                    try {
                        uploadProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    if (result.equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), "Photo uploaded successfully!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), " Upload Failed!", Toast.LENGTH_SHORT).show();
                        dialog_photo_upload_failed();
                    }



                    /**/
                    if (result.equalsIgnoreCase("1")) {
                        //    Toast.makeText(ClientLeavingWithCrate.this, "Image(s) uploaded successfully !", Toast.LENGTH_SHORT).show();
                        //if (remove_crete.startsWith(","))
                        //  Log.d("BHANU_Client_name", "under pic send");
                      //  String techName = sp.getString("tname", "");
                        String techName =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                        String selectedCratesId = "";
                        for (int i = 0; i < SelectedCrateList.size(); i++) {
                            selectedCratesId = selectedCratesId + SelectedCrateList.get(i).get("id") + ",";
                        }
                        selectedCratesId = selectedCratesId.substring(0, selectedCratesId.length() - 1);
                        webhitforupdate_photo = URL_EP1 + "/crate_with_client.php?crate=" + selectedCratesId + "&image=" + URL_EP2 + "/upload/" + list_UploadImageName.get(0) + "&tech_id=" + techName;//google.com";
                        webhitforupdate_photo = webhitforupdate_photo.replaceAll(Pattern.quote(" "), "%20");
                        Log.d("--Client is leaving---", webhitforupdate_photo);
                     //   String name = sp.getString("tname", "");
                        String name =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                     //   String clid = sp.getString("clientid", "");
                        String clid= Shared_Preference.getLOGIN_USER_ID(ClientLeavingWithCrate.this);
                       // String Jobid_or_swoid = sp.getString("jobid", "");
                        String Jobid_or_swoid =   Shared_Preference.getSWO_ID(ClientLeavingWithCrate.this);
                        if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {
                            new updateonep1().execute(Jobid_or_swoid, "Client POD", list_UploadImageName.get(0), "Client POD", String.valueOf(list_imageSize.get(0)), name, clid, "" + "1");
                        } else {
                            Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                        }

                    }


                    list_path.clear();
                    //	list_ImageDescData.clear();
                    list_imageSize.clear();
                    list_UploadImageName.clear();
                    /**/




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
                    list_path.clear();
                    Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    dialog_photo_upload_failed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*private void initRetrofitClient() {
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
    }
*/

    public class asyntaskupload1 extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //showprogressdialog();

            progressDialog = new ProgressDialog(ClientLeavingWithCrate.this);
            progressDialog.setMessage("Uploading , please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);

            try {
                progressDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }


            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {

            String result = "0";
            int statusCode = 0;
            totalSize = 0;
            try {
                //String jid = sp.getString(Utility.KEY_JOB_ID_FOR_JOBFILES, "");
                String jid =  Shared_Preference.getJOB_ID_FOR_JOBFILES(ClientLeavingWithCrate.this);
                String FILE_UPLOAD_URL = URL_EP2 + "/UploadFileHandler.ashx?jid=" + jid + "&uid=2";
                //uid=2 is for generating uploaded photo name as Client_Pod_Job_JobNo._imageCount_uniqueNo.
                // if uid is not passes then uploaded photo name will be generated as Job_JobNo._imageCount_uniqueNo.

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

                for (int i = 0; i < list_path.size(); i++) {
                    try {
                        String path = list_path.get(i);
                        File sourceFile_1 = new File(path);
                        entity.addPart("image" + i, new FileBody(sourceFile_1));
                        long Size = sourceFile_1.length();
                        totalSize = totalSize + Size;
                        list_imageSize.add(String.valueOf(Size / 1000));

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
                    String s[] = responseStr.split(",");
                    List<String> stringList = new ArrayList<String>(Arrays.asList(s)); //new Ar
                    list_UploadImageName = new ArrayList<String>(stringList);
                    if (list_UploadImageName != null & list_UploadImageName.size() > 0 & (!list_UploadImageName.get(0).equalsIgnoreCase(""))) {
                        result = "1";
                    } else
                        result = "0";

                } else {
                    String resultdata = "Error occurred! Http Status Code: "
                            + statusCode;
                    result = "0";
                }
            } catch (Exception e) {
                e.getMessage();
                result = "0";
            }

/////////////////////////////////////////////////////////
            if (String.valueOf(statusCode).equalsIgnoreCase("200")) {


            } else {

            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }


            for (int i = 0; i < list_path.size(); i++) {
                String TempImagePath = list_path.get(i);
                if (TempImagePath.contains("TempImage-")) {
                    Utility.delete(TempImagePath);//delete tem photo from storage
                }
            }

            if (result.equalsIgnoreCase("1")) {
                Toast.makeText(ClientLeavingWithCrate.this, "Image(s) uploaded successfully !", Toast.LENGTH_SHORT).show();
                //if (remove_crete.startsWith(","))
                //  Log.d("BHANU_Client_name", "under pic send");
               // String techName = sp.getString("tname", "");
                String techName =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                String selectedCratesId = "";
                for (int i = 0; i < SelectedCrateList.size(); i++) {
                    selectedCratesId = selectedCratesId + SelectedCrateList.get(i).get("id") + ",";
                }
                selectedCratesId = selectedCratesId.substring(0, selectedCratesId.length() - 1);
                webhitforupdate_photo = URL_EP1 + "/crate_with_client.php?crate=" + selectedCratesId + "&image=" + URL_EP2 + "/upload/" + list_UploadImageName.get(0) + "&tech_id=" + techName;//google.com";
                webhitforupdate_photo = webhitforupdate_photo.replaceAll(Pattern.quote(" "), "%20");
                Log.d("--Client is leaving---", webhitforupdate_photo);
              //  String name = sp.getString("tname", "");
                String name =  Shared_Preference.getLOGIN_USERNAME(ClientLeavingWithCrate.this);
                //String clid = sp.getString("clientid", "");
                String clid= Shared_Preference.getLOGIN_USER_ID(ClientLeavingWithCrate.this);
              //  String Jobid_or_swoid = sp.getString("jobid", "");

                String Jobid_or_swoid =   Shared_Preference.getSWO_ID(ClientLeavingWithCrate.this);
                if (new ConnectionDetector(ClientLeavingWithCrate.this).isConnectingToInternet()) {
                    new updateonep1().execute(Jobid_or_swoid, "Client POD", list_UploadImageName.get(0), "Client POD", String.valueOf(list_imageSize.get(0)), name, clid, "" + "1");
                } else {
                    Toast.makeText(ClientLeavingWithCrate.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }

            } else //if (result.equalsIgnoreCase("0"))

            {
                dialog_photo_upload_failed();

            }


            list_path.clear();
            //	list_ImageDescData.clear();
            list_imageSize.clear();
            list_UploadImageName.clear();



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
    }
    private void dialog_photo_upload_failed() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientLeavingWithCrate.this);
        LayoutInflater inflater = LayoutInflater.from(ClientLeavingWithCrate.this);
        final View dialogView = inflater.inflate(R.layout.dialog_yes_no, null);
        dialogView.setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);
        final TextView title = dialogView.findViewById(R.id.textView1rr);
        final  TextView message = dialogView.findViewById(R.id.texrtdesc);

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
                finish();
                startActivity(new Intent(ClientLeavingWithCrate.this, Clock_Submit_Type_Activity.class));

            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }
    public class updateonep1 extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            showprogressdialog();
            Log.d("BHANU_Client_name", "async_UploadFiles");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {

            hideprogressdialog();
            eventgenerate_for_photo();///////
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(String... params) {

            enterdata(params[0], params[1], params[2], params[3], params[4],
                    params[5], params[6], params[7]);


            generate_event(params[0], params[6], params[7], "1");

            return null;
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
        }catch (Exception e){
            e.getMessage();
        }


    }
    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(ClientLeavingWithCrate.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
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
        if (Count_Image_Uploaded < list_path.size())
            Count_Image_Uploaded++;
        uploadProgressDialog.setMessage("Uploading " + Count_Image_Uploaded + "/" + list_path.size() + ", Please wait..");

    }

    @Override
    public void uploadStart() {
        //  textView.setText("0%");
    }

}



