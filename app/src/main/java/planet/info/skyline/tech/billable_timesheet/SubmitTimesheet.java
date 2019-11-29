package planet.info.skyline.tech.billable_timesheet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.model.SWO_Status;
import planet.info.skyline.network.Api;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.tech.update_timesheet.TimeSheetList1Activity;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class SubmitTimesheet extends BaseActivity {
    String result = "";
    SharedPreferences sp;
    Editor ed;
    String selectcrateforworkstation = "";

    String urlforselect = "";
    String techname = "";

    String urlofwebservice = URL_EP2 + "/WebService/techlogin_service.asmx/bind_code";
    String urlofwebservice11_new = URL_EP2 + "/WebService/techlogin_service.asmx";

    String receivedString;


    int[] arrid;//=new int[4];
    String[] arrname;//=new String[4];
    String descriptionm;
    String idddd;


    String scanedlabourcode = "";
    int aman_status = 11;// develope timesheet4_6 by aman
    Dialog showd;
    SoapObject request1;

    SoapObject request2;
    SoapObject request_new;

    //String MethodName_timeSheet = "timesheet4_6";
    String MethodName_stopWork = "stop_tech_with_resion4_6";
    // String MethodName_timeSheet_new = "timesheet4_6_1June";

    String JOB_START_DateTime;//dd-MM-yyyy HH:mm:ss
    String JOB_STOP_DateTime;//dd-MM-yyyy HH:mm:ss
    AlertDialog alertDialog;
    String jobid = "";
    String dayInfo = "0";
    String reason = "";
    String JOB_START_HrsMinuts = "", JOB_STOP_HrsMinuts = "";
    String status = "";
    String jobType = "";
    ArrayList<SWO_Status> list_Swo_Status = new ArrayList<>();

    String Selected_Swo_Status = "", oldSwoStatus = "", Swo_Status = "";
    boolean resetClock = false;
    String userRole = "";
    String JobIdBillable = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scanforworkstation);
        sp = getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);
        ed = sp.edit();

        //   Selected_Swo_Status = sp.getString(Utility.KEY_SELECTED_SWO_STATUS, "-1");
        //  userRole = sp.getString(Utility.LOGIN_USER_ROLE, "");
        userRole = Shared_Preference.getUSER_ROLE(this);


        Bundle bbb = getIntent().getExtras();

        //start_time = sp.getString("starttimenew", "");
        //  jobid = sp.getString("jobid", "");//ACTUALLY SWO ID
        jobid = Shared_Preference.getSWO_ID(SubmitTimesheet.this);
        //  JobIdBillable = sp.getString(Utility.KEY_JOB_ID_FOR_JOBFILES, "");//ACTUALLY JOB ID

        JobIdBillable = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
        if (!(bbb == null)) {
            aman_status = bbb.getInt("aman_status");/////by amman kaushik
        }


        /*pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);*/

        if (new ConnectionDetector(SubmitTimesheet.this).isConnectingToInternet()) {
            new async_Get_Billable_Code().execute();
        } else {
            Toast.makeText(SubmitTimesheet.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }


    }

    public void showprogressdialog() {
        // pDialog.show();
    }

    public void hideprogressdialog() {
        // pDialog.dismiss();
    }

    public void Alert_Choose_BillableCode() {


        final Dialog showd = new Dialog(SubmitTimesheet.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.labourcode_new);
        showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));


        showd.setCancelable(false);
        // showd.setTitle("");
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

        TextView yesfo = (TextView) showd.findViewById(R.id.Btn_Yes);
        yesfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (new ConnectionDetector(SubmitTimesheet.this).isConnectingToInternet()) {
                    new async_Get_Billable_Code().execute();
                } else {
                    Toast.makeText(SubmitTimesheet.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            }
        });

        ImageView close = (ImageView) showd.findViewById(R.id.close);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Intent in = new Intent(SubmitTimesheet.this, Clock_Submit_Type_Activity.class);
                startActivity(in);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        String locationn, casee;

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                try {

                    String result = "";
                    String[] gg = null;
                    String contents = intent.getStringExtra("SCAN_RESULT");


                    gg = contents.split("loc=");

                    String hom = contents;
                    result = hom.substring(hom.indexOf("_loc=") + 5,
                            hom.length());

                    String ssdcf = result;


                    try {
                        String workloc = result;


                        //   ed.putString("worklocation", workloc).commit();
                        Shared_Preference.setWORK_LOCATION(this, workloc);
                        Toast.makeText(SubmitTimesheet.this, workloc,
                                Toast.LENGTH_LONG).show();
                        Toast.makeText(SubmitTimesheet.this,
                                selectcrateforworkstation, Toast.LENGTH_LONG).show();
                        urlforselect = URL_EP1 + Api.API_UPDATE_CRATE_1
                                + selectcrateforworkstation
                                + "&workloc="
                                + workloc + "&tech=" + techname;
                        //  getjsonobject();
                        Alert_Choose_BillableCode();
                    } catch (Exception ee) {
                        // Toast.makeText(SubmitTimesheet.this,"hello 1  ",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ee) {

                }
                // getoldloc(updateloc);
                // showprogressdialog();

            } else if (requestCode == 2) {
                //finish();

                String contents = intent.getStringExtra("SCAN_RESULT");
                scanedlabourcode = contents;

                if (new ConnectionDetector(SubmitTimesheet.this).isConnectingToInternet()) {
                    new async_Get_Billable_Code().execute();

                } else {
                    Toast.makeText(SubmitTimesheet.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }


            } else if (requestCode == 3) {
                //  String worklo = sp.getString("worklocation", "");
                String worklo = Shared_Preference.getWORK_LOCATION(this);
                urlforselect = URL_EP1 + Api.API_UPDATE_CRATE_1
                        + selectcrateforworkstation
                        + "&workloc="
                        + worklo
                        + "&tech=" + techname;
                // getjsonobject();
                Alert_Choose_BillableCode();
            } else if (requestCode == 4) {
                try {

                    String result = "";
                    String[] gg = null;
                    String contents = intent.getStringExtra("SCAN_RESULT");

                    String hom = contents;
                    result = hom.substring(hom.indexOf("_loc=") + 5,
                            hom.length());

                    String ssdcf = result;

                    String workloc = result;

                    //   ed.putString("worklocation", workloc).commit();
                    Shared_Preference.setWORK_LOCATION(this, workloc);
                } catch (Exception ee) {

                }
                //  String worklo = sp.getString("worklocation", "");
                String worklo = Shared_Preference.getWORK_LOCATION(this);
                urlforselect = URL_EP1 + Api.API_UPDATE_CRATE_1
                        + selectcrateforworkstation
                        + "&workloc="
                        + worklo
                        + "&tech=" + techname;
                //  Call_Api_MoveCrateToBin_Freight_Area();
                Alert_Choose_BillableCode();
            }
            // finish();
        }
        if (resultCode == RESULT_CANCELED) {


            if (new ConnectionDetector(SubmitTimesheet.this).isConnectingToInternet()) {

                new async_Get_Billable_Code().execute();
            } else {
                Toast.makeText(SubmitTimesheet.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void Dialog_EnterDesc() {
        final Dialog showd1 = new Dialog(SubmitTimesheet.this);
        showd1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd1.setContentView(R.layout.enterdescriptiondata_new);
        showd1.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        showd1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        showd1.setCancelable(false);
        try {
            showd1.show();
        } catch (Exception e) {
            e.getMessage();
        }
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ");
        String time1 = sdf.format(dt);
        //   ed.putString("endtime", time1).commit();
        //end_time = sp.getString("endtime", "");

        final EditText ed1 = (EditText) showd1.findViewById(R.id.texrtdesc);
        TextView no = (TextView) showd1.findViewById(R.id.Btn_No);
        ImageView close = (ImageView) showd1.findViewById(R.id.close);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd1.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Intent in = new Intent(SubmitTimesheet.this, MainActivity.class);
                startActivity(in);

                finish();


            }
        });

        no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // if (Utility.isTimeAutomatic(SubmitTimesheet.this)) {

                if (ed1.getText().length() < 1) {
                    Toast.makeText(SubmitTimesheet.this, "Please enter description   ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    descriptionm = ed1.getText().toString().trim();
                    Utility.hideKeyboard(SubmitTimesheet.this);
                    try {
                        showd1.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    String Total_time = Utility.get_TotalClockTime(SubmitTimesheet.this);

                    /**************************************************************/
                    if (aman_status == 1) {
                        status = "1";//stop work

                    } else if (aman_status == 0) {
                        status = "0";//btn_PauseWork work

                    } else {
                        status = "3";//finish work

                    }


                    if (oldSwoStatus.equals(Selected_Swo_Status)) {
                        Swo_Status = "0";
                        reason = descriptionm + "  " + "(" + Total_time + ")";
                    } else {
                        Swo_Status = Selected_Swo_Status;
                        //    status = "10";
                        reason = descriptionm + "  " + "(" + Total_time + ")";
                    }
                    /*****************************************************************/

                    if (aman_status == 100) {      ///Change Time Code
                        jobType = Utility.CHANGE_TIME_CODE;
                        resetClock = true;
                        status = "2018";

                        PrepareApiData();

                    } else {                   // Pause , Stop  , Finish
                        jobType = Utility.BILLABLE;
                        resetClock = false;
                        PrepareApiData();
                    }

                }

                /*} else {
                    showd1.dismiss();
                    dialog_AutoDateTimeSet();
                }*/
            }

        });

    }

    public void Dialog_Choose_Labor_Code() {

        showd = new Dialog(SubmitTimesheet.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.radiogroupfortimesheat);
        showd.setCancelable(false);
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

        RadioGroup rg = (RadioGroup) showd.findViewById(R.id.radioGroup1);
        // int ii = 1000;
        /*for (int i = 0; i < 8; i++) {
            ii = ii--;
            RadioButton button = new RadioButton(this);
            button.setId(ii);
            button.setText("");
            button.setChecked(false); // Only select button with same index as

            rg.addView(button);
            button.setVisibility(View.GONE);// 31/03/2016 invisble >> GONE
        }*/

        if (arrid != null && arrid.length > 0) {
            for (int i = 0; i < arrid.length; i++) {
                RadioButton button = new RadioButton(this);
                button.setId(arrid[i]);
                String names = arrname[i];
                button.setText(names);
                button.setChecked(false); // Only select button with same index as
                // currently selected number of hours
                /*if (scanedlabourcode.equalsIgnoreCase(names)) {
                }*/
                rg.addView(button);
            }
        } else {
            Toast.makeText(SubmitTimesheet.this, "Some api_error occured!", Toast.LENGTH_SHORT).show();
        }

        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                idddd = String.valueOf(checkedId);
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                 if (userRole.equals(Utility.USER_ROLE_APC) ||userRole.equals(Utility.USER_ROLE_ARTIST)) {
             //   if (userRole.equals(Utility.USER_ROLE_APC)) {
                    Dialog_EnterDesc();
                } else {          // for tech,artist
                    Dialog_Choose_Swo_Awo_Status();
                }


                //   Dialog_EnterDesc();

            }
        });
    }

    public void Dialog_Choose_Swo_Awo_Status() {
        showd = new Dialog(SubmitTimesheet.this);
        showd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showd.setContentView(R.layout.dialog_swo_status);


      /*  showd.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));*/
        showd.setCancelable(false);
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
        }

        RadioGroup radioGroup = showd.findViewById(R.id.radioGroup);
        TextView title = showd.findViewById(R.id.textView1rr);

        if (userRole.equals(Utility.USER_ROLE_ARTIST)) {  // artist

            title.setText("Update AWO Status");

        } else if (userRole.equals(Utility.USER_ROLE_TECH)) {  //tech

            title.setText("Update SWO Status");
        }
        for (int i = 0; i < list_Swo_Status.size(); i++) {
            RadioButton radioButton = new RadioButton(SubmitTimesheet.this);
            int id = 0;
            try {
                id = Integer.parseInt(list_Swo_Status.get(i).getIDPK());
            } catch (Exception e) {
                e.getMessage();
            }
            radioButton.setId(id);
            radioButton.setText(list_Swo_Status.get(i).getTxtStatus());
            if (String.valueOf(id).equals(oldSwoStatus)) {
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Selected_Swo_Status = String.valueOf(checkedId);
                //   Toast.makeText(getApplicationContext(),Selected_Swo_Status,Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_Done = showd.findViewById(R.id.btn_Done);
        btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                Dialog_EnterDesc();
            }
        });

    }

    public void getBillableCodes() {
        String receivedString = "";

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Api.API_BILLABLE_NONBILLABLE_CODE;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;


        // Create SOAP request
        //  String dealerId = sp.getString(Utility.DEALER_ID, "0");
        String dealerId = Shared_Preference.getDEALER_ID(this);

        // String role = sp.getString(Utility.LOGIN_USER_ROLE, "0");
        String role = Shared_Preference.getUSER_ROLE(this);


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
       /* request.addProperty("type", "1");
        request.addProperty("dealerID", dealerId);//nks*/
        request.addProperty("dealerID", dealerId);
        request.addProperty("cat", role);
        request.addProperty("type", "1");
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
            String n1 = news;
            JSONArray jArray = new JSONArray(n1);
            int len = jArray.length();
            arrid = new int[len];
            arrname = new String[len];
            for (int k = 0; k < (jArray.length()); k++) {

                JSONObject json_obj = jArray.getJSONObject(k);
                arrid[k] = json_obj.getInt("Lalor_ID_PK");
                arrname[k] = json_obj.getString("Labor_name");

            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void getSwo_Details_BySwoID() {
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
         final String METHOD_NAME =Api. API_GetSwoDetailByID;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        // String swoID = sp.getString("jobid", "");
        String swoID = Shared_Preference.getSWO_ID(SubmitTimesheet.this);
        //  String role = sp.getString(Utility.LOGIN_USER_ROLE, "0");
     //   String role = Shared_Preference.getUSER_ROLE(this);
        request.addProperty("swoID", swoID);

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

            if (jsonArray.length() > 0) {
                JSONObject json_obj = jsonArray.getJSONObject(0);
                oldSwoStatus = json_obj.getString("SWO_Status_new");
                Selected_Swo_Status = oldSwoStatus;

            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void getSwo_AWO_Details_BySwoID() {
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Api.API_GetSwo_AWODetailByID;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        // String swoID = sp.getString("jobid", "");
        String swoID = Shared_Preference.getSWO_ID(SubmitTimesheet.this);
        //  String role = sp.getString(Utility.LOGIN_USER_ROLE, "0");
        String role = Shared_Preference.getUSER_ROLE(this);
        request.addProperty("swoID", swoID);
        request.addProperty("Role", role);
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

            if (jsonArray.length() > 0) {
                JSONObject json_obj = jsonArray.getJSONObject(0);
                oldSwoStatus = json_obj.getString("SWO_Status_new");
                Selected_Swo_Status = oldSwoStatus;

            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void getSwo_Status() {
        list_Swo_Status.clear();
        //  final String role = sp.getString(Utility.LOGIN_USER_ROLE, "");
      //  String role = Shared_Preference.getUSER_ROLE(this);

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Api.API_bindSWOStatus;

        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //  String dealerId = sp.getString(Utility.DEALER_ID, "0");
        String dealerId = Shared_Preference.getDEALER_ID(this);

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
            JSONArray jsonArray = new JSONArray(resultstring);

            for (int k = 0; k < (jsonArray.length()); k++) {
                JSONObject json_obj = jsonArray.getJSONObject(k);
                String ID_PK = json_obj.getString("ID_PK");
                String txt_status = json_obj.getString("txt_status");
                list_Swo_Status.add(new SWO_Status(ID_PK, txt_status));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }
    public void getSwo_AWO_Status() {
        list_Swo_Status.clear();
        //  final String role = sp.getString(Utility.LOGIN_USER_ROLE, "");
        String role = Shared_Preference.getUSER_ROLE(this);


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Api.API_bind_SWO_AWO_Status;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;

        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //  String dealerId = sp.getString(Utility.DEALER_ID, "0");
        String dealerId = Shared_Preference.getDEALER_ID(this);

        request.addProperty("dealerID", dealerId);//nks
        request.addProperty("Role", role);//nks
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
                String txt_status = json_obj.getString("txt_status");
                list_Swo_Status.add(new SWO_Status(ID_PK, txt_status));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public String Submit_Billable_TimeSheetNew() {  ///use this fo billable
        String receivedString = "";
        String timesheetID = "";
        final String URL = urlofwebservice11_new;
        final String SOAP_ACTION = KEY_NAMESPACE + Utility.Method_BillableTimeSheet;

        if (Utility.Method_BillableTimeSheet.equals(Api.API_BILLABLE_TIMESHEET_Tech_Artist)) {
            String role = Shared_Preference.getUSER_ROLE(this);
            request_new.addProperty("Role", role);
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request_new);
        Log.e("billable time url---", request_new.toString());

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONObject jsonObject = new JSONObject(receivedString);
            //  {"ID":"0","Output":"0","Message":"Overlapping Time entries"}
            timesheetID = jsonObject.getString("ID");
            // ed.putString(Utility.TIME_SHEET_ID, timesheetID).commit();
            Shared_Preference.setTIME_SHEET_ID(this, timesheetID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedString;
    }


    private void PrepareApiData() {
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            e.getCause();
        }

        final String NAMESPACE = KEY_NAMESPACE;
        // String clientid = sp.getString("clientid", "");
        String clientid = Shared_Preference.getLOGIN_USER_ID(SubmitTimesheet.this);
        //String PAUSED_TIMESHEET_ID = sp.getString(Utility.KEY_PAUSED_TIMESHEET_ID, "0");
        String PAUSED_TIMESHEET_ID = Shared_Preference.getPAUSED_TIMESHEET_ID(SubmitTimesheet.this);
/************************************************************************************/
        //  final String METHOD_NAME1 = Api.API_CHANGE_TIME_CODE_TIMESHEET;
        // request1 = new SoapObject(NAMESPACE, METHOD_NAME1);

        //   JOB_START_DateTime = sp.getString(Utility.CLOCK_START_TIME, "");//dd-MM-yyyy HH:mm:ss
        JOB_START_DateTime = Shared_Preference.getCLOCK_START_TIME(this);//dd-MM-yyyy HH:mm:ss
        JOB_STOP_DateTime = Utility.getCurrentTimeString();//dd-MM-yyyy HH:mm:ss

        boolean bool_IsSameDay = Utility.IsSameDay(JOB_START_DateTime, JOB_STOP_DateTime);

        if (bool_IsSameDay) dayInfo = "0";
        else dayInfo = "1";

        String arr1[] = JOB_START_DateTime.split(" ");
        String str1 = arr1[1];
        JOB_START_HrsMinuts = str1.substring(0, str1.lastIndexOf(":"));//HH:mm

        String arr[] = JOB_STOP_DateTime.split(" ");
        String str = arr[1];
        JOB_STOP_HrsMinuts = str.substring(0, str.lastIndexOf(":"));//HH:mm

        if (JOB_STOP_HrsMinuts.equals("00:00")) {
            dayInfo = "1";
        }


/************************************************************************************/

        final String METHOD_NAME3 = Utility.Method_BillableTimeSheet;
        request_new = new SoapObject(NAMESPACE, METHOD_NAME3);

        request_new.addProperty("tech_id", clientid);
        request_new.addProperty("swo_id", jobid);
        request_new.addProperty("start_time", JOB_START_HrsMinuts);
        request_new.addProperty("end_time", JOB_STOP_HrsMinuts);
        request_new.addProperty("description", descriptionm);
        request_new.addProperty("code", idddd);
        request_new.addProperty("dayInfo", dayInfo);
        request_new.addProperty("status", status);
        request_new.addProperty("region", reason);
        request_new.addProperty("PhoneType", "Android");
        request_new.addProperty("EMI", imei);
        request_new.addProperty("SWOstatus", Swo_Status);
        request_new.addProperty("jobID", JobIdBillable);
        request_new.addProperty("PauseTimeSheetID", PAUSED_TIMESHEET_ID);

/**************************************************************************************/


        if (new ConnectionDetector(SubmitTimesheet.this).isConnectingToInternet()) {
         /*   ed.putString(Utility.TIMEGAP_JOB_END_TIME, JOB_STOP_DateTime).commit();//for next job time gap
            ed.putString(Utility.TIMEGAP_PREV_JOB_START_TIME, JOB_START_DateTime).commit();//for next job time gap
         */
            if (!resetClock) {
                Utility.StopRunningClock(SubmitTimesheet.this);
            }
            //ed.putString("imglo", "");
            Shared_Preference.setCLIENT_IMAGE_LOGO_URL(this, "");
            //   ed.putString("client_name", "");
            Shared_Preference.setCLIENT(this, "");
            // ed.putString("name", "").apply();
            Shared_Preference.setCLIENT_NAME(this, "");

            new Async_Submit_Billable_Timesheet_New().execute();


        } else {
            Toast.makeText(SubmitTimesheet.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            showDialog_for_OfflineApiDataSave(SubmitTimesheet.this, request_new.toString(), Utility.getUniqueId());
        }
    }

    public void showDialog_for_OfflineApiDataSave(final Context context, final String api_input, final String unique_apiId) {
        //ToDo
        final Dialog showd = new Dialog(context);
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
        final TextView Btn_Cancel = (TextView) showd.findViewById(R.id.Btn_Select_Another);
        Btn_Done.setText("OK");
        Btn_Cancel.setText("Cancel");

        tv_title.setText("Lost Internet!");
        tv_title.setVisibility(View.GONE);


        tv_msg.setText(Utility.MSG_OFFLINE_DATA_SAVE);
        //tv_msg.setGravity(Gravity.CENTER | Gravity.LEFT);
        Btn_Done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showd.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }


                Utility.StopRunningClock(SubmitTimesheet.this);
                // ed.putString("imglo", "");
                Shared_Preference.setCLIENT_IMAGE_LOGO_URL(SubmitTimesheet.this, "");
                //ed.putString("client_name", "");
                Shared_Preference.setCLIENT(SubmitTimesheet.this, "");
                //  ed.putString("name", "").apply();
                Shared_Preference.setCLIENT_NAME(SubmitTimesheet.this, "");
                Utility.saveOfflineIncompleteAsynctask(context, api_input, unique_apiId, "Billable");
                Toast.makeText(SubmitTimesheet.this, Utility.SAVED, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SubmitTimesheet.this, MainActivity.class));
                finish();

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


                onBackPressed();

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


                onBackPressed();
            }
        });
        try {
            showd.show();
        } catch (Exception e) {
            e.getMessage();
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

    private void dialog_OverlappingTimeEntry() {
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
        title.setText("Overlapping Time Entry.");
        desc.setText("Edit the Entry!");
        // message.setText("Please set Automatic date and Time");

        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //  String PAUSED_TIMESHEET_ID = sp.getString(Utility.KEY_PAUSED_TIMESHEET_ID, "0");
                String PAUSED_TIMESHEET_ID = Shared_Preference.getPAUSED_TIMESHEET_ID(SubmitTimesheet.this);
                Utility.saveOverlapTimeSheetData(SubmitTimesheet.this,
                        jobid,
                        JOB_START_HrsMinuts,
                        JOB_STOP_HrsMinuts,
                        descriptionm,
                        idddd,
                        reason,
                        status,
                        dayInfo,
                        jobType,
                        sp.getString("JOB_ID_BILLABLE", "Billable"), Swo_Status, JobIdBillable, PAUSED_TIMESHEET_ID);

                Intent i = new Intent(getApplicationContext(), TimeSheetList1Activity.class);

              /*  i.putExtra("jobId", jobid);
                i.putExtra("start_time", JOB_START_HrsMinuts);
                i.putExtra("end_time", JOB_STOP_HrsMinuts);
                i.putExtra("description", descriptionm);
                i.putExtra("code", idddd);
                i.putExtra("region", reason);
                i.putExtra("dayInfo", dayInfo);
                i.putExtra("status", status);
                i.putExtra("jobDesc", sp.getString("JOB_ID_BILLABLE","Billable"));
                i.putExtra("jobType",jobType);*/

                startActivity(i);
                finish();

            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //   String PAUSED_TIMESHEET_ID = sp.getString(Utility.KEY_PAUSED_TIMESHEET_ID, "0");
                String PAUSED_TIMESHEET_ID = Shared_Preference.getPAUSED_TIMESHEET_ID(SubmitTimesheet.this);
                Utility.saveOverlapTimeSheetData(SubmitTimesheet.this,
                        jobid,
                        JOB_START_HrsMinuts,
                        JOB_STOP_HrsMinuts,
                        descriptionm,
                        idddd,
                        reason,
                        status,
                        dayInfo,
                        jobType,
                        sp.getString("JOB_ID_BILLABLE", "Billable"), Swo_Status, JobIdBillable, PAUSED_TIMESHEET_ID);


                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();


            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    public void AssignTechToUnassignedSwo() {
        //    String clientid = sp.getString("clientid", "");
        String clientid = Shared_Preference.getLOGIN_USER_ID(SubmitTimesheet.this);
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Api.API_SaveTech;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("SWOid", jobid);
        request.addProperty("tech", clientid);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void Assign_Tech_Artist_ToUnassigned_Swo_AWO() {
        //  String clientid = sp.getString("clientid", "");
        String clientid = Shared_Preference.getLOGIN_USER_ID(SubmitTimesheet.this);
        //  final String role = sp.getString(Utility.LOGIN_USER_ROLE, "");
        String role = Shared_Preference.getUSER_ROLE(this);

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = urlofwebservice11_new;
        final String METHOD_NAME = Api.API_SaveUser;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("SWOid", jobid);
        request.addProperty("tech", clientid);
        request.addProperty("Role", role);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public class async_Get_Billable_Code extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDoalog = new ProgressDialog(SubmitTimesheet.this);
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
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            if (progressDoalog != null && progressDoalog.isShowing()) {
                try {
                    progressDoalog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            if (arrid != null && arrid.length > 0) {
                Dialog_Choose_Labor_Code();
            } else {
                Toast.makeText(getApplicationContext(), "Error! There are NO Job Codes currently in the system!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //getSwo_Details_BySwoID();
            getSwo_AWO_Details_BySwoID();
            getBillableCodes();
          //  getSwo_Status();
            getSwo_AWO_Status();
            return null;
        }

    }

    public class Async_Submit_Billable_Timesheet_New extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDoalog = new ProgressDialog(SubmitTimesheet.this);
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

            JSONObject jsonObject = null;
            String output = "";
            String msg = "";
            try {
                jsonObject = new JSONObject(result);
                //  {"ID":"0","Output":"0","Message":"Overlapping Time entries"}
                Log.e("AdminTimeSheetId", result);
                output = jsonObject.getString("Output");
                msg = jsonObject.getString("Message");
            } catch (Exception e) {
                e.getCause();
            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            if (output.equals("1")) {   //means sheet submitted successfully
                //  ed.putString(Utility.TIMEGAP_JOB_END_TIME, JOB_STOP_DateTime).commit();//for next job time gap
                Shared_Preference.setTIMEGAP_JOB_END_TIME(SubmitTimesheet.this, JOB_STOP_DateTime);
                //  ed.putString(Utility.TIMEGAP_PREV_JOB_START_TIME, JOB_START_DateTime).commit();//for next job time gap
                Shared_Preference.setTIMEGAP_PREV_JOB_START_TIME(SubmitTimesheet.this, JOB_START_DateTime);

                if (resetClock) {
                    Utility.ResetClock(SubmitTimesheet.this);
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            } else if (output.equals("0")) { //overlap
                if (resetClock) {
                    Utility.ResetClock(SubmitTimesheet.this);
                }
                dialog_OverlappingTimeEntry();
            }


            //  dialog_OverlappingTimeEntry();//test


        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String timesheet_id = Submit_Billable_TimeSheetNew();

            AssignTechToUnassignedSwo();
            // Assign_Tech_Artist_ToUnassigned_Swo_AWO();
            return timesheet_id;
        }

    }

}
