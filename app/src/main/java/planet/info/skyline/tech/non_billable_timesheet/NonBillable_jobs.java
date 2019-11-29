package planet.info.skyline.tech.non_billable_timesheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.RequestControler.MyAsyncTask;
import planet.info.skyline.RequestControler.ResponseInterface;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.model.LaborCode;
import planet.info.skyline.network.Api;
import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.tech.update_timesheet.TimeSheetList1Activity;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;


/**
 * Created by planet on 5/5/16.
 */
public class NonBillable_jobs extends BaseActivity implements ResponseInterface {

    ArrayList<LaborCode> ListLaborCodes = new ArrayList<>();

    String Selected_LaborCode_ID;

    //String start_time;
    String descriptionm;

    //nks
    Dialog dialog_desc;
    // String clientidme;
    String jobid = "";
    String clientid = "";

    String nextact = URL_EP1 + Api.API_CRATES_LIST
            + clientid;
    Dialog dialog_NonBillableCodes;
    // String UniqueId = "";
    String imei = "";

    String JOB_STOP_HrsMinuts, JOB_START_HrsMinuts, Total_time, dayInfo;
    SoapObject request;

    String JOB_START_DateTime;//dd-MM-yyyy HH:mm:ss
    String JOB_STOP_DateTime;//dd-MM-yyyy HH:mm:ss
    AlertDialog alertDialog;

    // boolean FromClock=false;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanforworkstation);

        jobid = "605";
        clientid = Shared_Preference.getLOGIN_USER_ID(NonBillable_jobs.this);
        nextact = URL_EP1 + Api.API_CRATES_LIST
                + clientid;

        FetchNonBillableCodes();

    }

    /*nks*/
    /*on floating view click this class opens and when this class alreay open and we click
     * on floating view then a new window also opens So we have to store in preference
     * the state of this class*/

    private void FetchNonBillableCodes() {
        JSONObject jsonObject = new JSONObject();
        try {
            String dealerId = Shared_Preference.getDEALER_ID(this);
            final String role = Shared_Preference.getUSER_ROLE(this);
            jsonObject.put("dealerID", dealerId);
            jsonObject.put("cat", role);
            jsonObject.put("type", "2");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (new ConnectionDetector(NonBillable_jobs.this).isConnectingToInternet()) {
            new MyAsyncTask(this, this, Api.API_BILLABLE_NONBILLABLE_CODE, jsonObject).execute();
        } else {
            Toast.makeText(NonBillable_jobs.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private void VerifyLaborCode() {

        JSONObject jsonObject = new JSONObject();
        try {
            String dealerId = Shared_Preference.getDEALER_ID(this);
            final String role = Shared_Preference.getUSER_ROLE(this);
            jsonObject.put("dealerID", dealerId);
            jsonObject.put("cat", role);
            jsonObject.put("type", "2");
            jsonObject.put("Lcode", Selected_LaborCode_ID);
            new MyAsyncTask(this, this, Api.API_VerifyLaborCode, jsonObject).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void SubmitNonBillableTimeSheet() {

        if (new ConnectionDetector(NonBillable_jobs.this).isConnectingToInternet()) {
            new MyAsyncTask(this, this, Api.API_NON_BILLABLE_TIMESHEET, Utility.SoapObject_To_JsonObject(request)).execute();
        } else {
            Toast.makeText(NonBillable_jobs.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // AppController.activityResumed();
        Utility.showChatHead(NonBillable_jobs.this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        //   AppController.activityPaused();
    }

    public void Dialog_Choose_NonBillableCode(ArrayList<LaborCode> codeArrayList) {

        dialog_NonBillableCodes = new Dialog(NonBillable_jobs.this);
        dialog_NonBillableCodes.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_NonBillableCodes.setContentView(R.layout.radiogroupfortimesheat);
        dialog_NonBillableCodes.setCancelable(true);//nks
        dialog_NonBillableCodes.setCanceledOnTouchOutside(false);

        RadioGroup rg = (RadioGroup) dialog_NonBillableCodes.findViewById(R.id.radioGroup1);

        for (int i = 0; i < codeArrayList.size(); i++) {
            try {
                RadioButton button = new RadioButton(this);
                button.setId(Integer.parseInt(codeArrayList.get(i).getLalor_ID_PK()));
                button.setText(codeArrayList.get(i).getLabor_name());
                button.setChecked(false);
                rg.addView(button);
            } catch (Exception e) {
                e.getMessage();
            }
        }


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Selected_LaborCode_ID = String.valueOf(checkedId);
                dialog_NonBillableCodes.dismiss();
                VerifyLaborCode();
                // Dialog_EnterDesc();


            }
        });

        //nks
        dialog_NonBillableCodes.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onBackPressed();
            }
        });


        try {
            dialog_NonBillableCodes.show();
        } catch (Exception e) {
            e.getMessage();
        }


        /**/


    }

    public void Dialog_EnterDesc() {

        //final Dialog showd = new Dialog(NonBillable_jobs.this);
        dialog_desc = new Dialog(NonBillable_jobs.this);
        dialog_desc.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_desc.setContentView(R.layout.enterdescriptiondata_new);

        dialog_desc.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_desc.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog_desc.setCancelable(true);
        dialog_desc.setCanceledOnTouchOutside(false);


        final EditText ed1 = (EditText) dialog_desc.findViewById(R.id.texrtdesc);
        TextView btn_Submit = (TextView) dialog_desc.findViewById(R.id.Btn_No);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (ed1.getText().length() == 0) {
                    Toast.makeText(NonBillable_jobs.this, "Please enter description!", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    dialog_desc.dismiss();

                    Utility.hideKeyboard(NonBillable_jobs.this);
                    PrepareApiData(ed1.getText().toString().trim());
                }
            }
        });
        //nks start

        ImageView close = (ImageView) dialog_desc.findViewById(R.id.close);
        close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                onBackPressed();
                return true;
            }
        });


        dialog_desc.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialog_desc.dismiss();
                onBackPressed();
            }

        });


        try {
            dialog_desc.show();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        //  startActivity(new Intent(NonBillable_jobs.this, MainActivity.class));
        finish();

    }

    private void PrepareApiData(String desc) {

        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            e.getCause();
        }
        descriptionm = desc;
        final String NAMESPACE = KEY_NAMESPACE;
        final String METHOD_NAME = Api.API_NON_BILLABLE_TIMESHEET;

        request = new SoapObject(NAMESPACE, METHOD_NAME);

        Total_time = Utility.get_TotalClockTime(NonBillable_jobs.this);
        JOB_START_DateTime = Shared_Preference.getCLOCK_START_TIME(this);//dd-MM-yyyy HH:mm:ss
        JOB_STOP_DateTime = Utility.getCurrentTimeString();//dd-MM-yyyy HH:mm:ss

       /* ed.putString(Utility.TIMEGAP_JOB_END_TIME, JOB_STOP_DateTime).commit();//for next job time gap
        ed.putString(Utility.TIMEGAP_PREV_JOB_START_TIME, JOB_START_DateTime).commit();//for next job time gap
       */
        boolean bool_IsSameDay = Utility.IsSameDay(JOB_START_DateTime, JOB_STOP_DateTime);
        dayInfo = "0";
        if (bool_IsSameDay) dayInfo = "0";
        else dayInfo = "1";

        String arr1[] = JOB_START_DateTime.split(" ");
        String str1 = arr1[1];
        JOB_START_HrsMinuts = str1.substring(0, str1.lastIndexOf(":"));//HH:mm

        String arr[] = JOB_STOP_DateTime.split(" ");
        String str = arr[1];
        JOB_STOP_HrsMinuts = str.substring(0, str.lastIndexOf(":"));//HH:mm

        /**/
        request.addProperty("tech_id", clientid);
        request.addProperty("jobId", jobid);
        request.addProperty("start_time", JOB_START_HrsMinuts);
        request.addProperty("end_time", JOB_STOP_HrsMinuts);
        request.addProperty("description", descriptionm);
        request.addProperty("code", Selected_LaborCode_ID);
        request.addProperty("region", descriptionm + "  " + "(" + Total_time + ")");
        request.addProperty("status", "3");
        request.addProperty("PhoneType", "Android");
        request.addProperty("EMI", imei);

        //  request.addProperty("dayInfo", dayInfo);

        if (new ConnectionDetector(NonBillable_jobs.this).isConnectingToInternet()) {
            /*
            ed.putString(Utility.TIMEGAP_JOB_END_TIME, JOB_STOP_DateTime).commit();//for next job time gap
            ed.putString(Utility.TIMEGAP_PREV_JOB_START_TIME, JOB_START_DateTime).commit();//for next job time gap
           */

            Utility.StopRunningClock(NonBillable_jobs.this);
            //  new Async_Submit_NonBillable_Timesheet().execute();
            SubmitNonBillableTimeSheet();

        } else {
            Toast.makeText(NonBillable_jobs.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            showDialog_for_OfflineApiDataSave(NonBillable_jobs.this, request.toString(), Utility.getUniqueId());
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
        //tv_msg.setGravity( Gravity.CENTER|Gravity.LEFT );
        Btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showd.dismiss();
                Utility.StopRunningClock(NonBillable_jobs.this);
                Utility.saveOfflineIncompleteAsynctask(context, api_input, unique_apiId, "Non-Billable");
                Toast.makeText(NonBillable_jobs.this, Utility.SAVED, Toast.LENGTH_SHORT).show();
                onBackPressed();

            }
        });
        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showd.dismiss();
                onBackPressed();

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showd.dismiss();
                onBackPressed();
            }
        });

        showd.show();


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
        //
        positiveBtn.setText("Ok");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                String reason = descriptionm + "  " + "(" + Total_time + ")";
                Utility.saveOverlapTimeSheetData(NonBillable_jobs.this,
                        jobid,
                        JOB_START_HrsMinuts,
                        JOB_STOP_HrsMinuts,
                        descriptionm,
                        Selected_LaborCode_ID,
                        reason,
                        "3",
                        "",
                        "Non-Billable",
                        Utility.NON_BILLABLE, "", "", "");

                Intent i = new Intent(getApplicationContext(), TimeSheetList1Activity.class);

              /*  i.putExtra("jobId", jobid);
                i.putExtra("start_time", JOB_START_HrsMinuts);
                i.putExtra("end_time", JOB_STOP_HrsMinuts);
                i.putExtra("description", descriptionm);
                i.putExtra("code", Selected_LaborCode_ID);
                i.putExtra("region", reason);
                i.putExtra("status", "3");
                i.putExtra("jobDesc", "Non-Billable");
                i.putExtra("jobType", Utility.NON_BILLABLE);*/

                startActivity(i);
                finish();


            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                String reason = descriptionm + "  " + "(" + Total_time + ")";
                Utility.saveOverlapTimeSheetData(NonBillable_jobs.this,
                        jobid,
                        JOB_START_HrsMinuts,
                        JOB_STOP_HrsMinuts,
                        descriptionm,
                        Selected_LaborCode_ID,
                        reason,
                        "3",
                        "",
                        "Non-Billable",
                        Utility.NON_BILLABLE, "", "", "");

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


    @Override
    public void handleResponse(String responseString, String api) {

        if (api.equalsIgnoreCase(Api.API_VerifyLaborCode)) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(responseString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonArray != null && jsonArray.length() > 0) { // valid labor code
             //   if (false) {
                Dialog_EnterDesc();
            } else {// not valid labor code
                Toast.makeText(NonBillable_jobs.this, "This labor code is not valid!Try again!", Toast.LENGTH_SHORT).show();
                    FetchNonBillableCodes();
            }


        } else if (api.equalsIgnoreCase(Api.API_BILLABLE_NONBILLABLE_CODE)) {
            ListLaborCodes.clear();
            try {
                JSONArray jsonArray = new JSONArray(responseString);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int k = 0; k < (jsonArray.length()); k++) {
                        JSONObject json_obj = jsonArray.getJSONObject(k);
                        String laborId = json_obj.getString("Lalor_ID_PK");
                        String laborName = json_obj.getString("Labor_name");
                        ListLaborCodes.add(new LaborCode(laborId, laborName));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
           if (ListLaborCodes != null && ListLaborCodes.size() > 0) {
               // if (false) {
                Dialog_Choose_NonBillableCode(ListLaborCodes);
            } else {
                Dialog_Choose_NonBillableCode(Utility.getNonBilableCodes(NonBillable_jobs.this));
            }

        } else if (api.equalsIgnoreCase(Api.API_NON_BILLABLE_TIMESHEET)) {
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                String NonBillableTimeSheetID = jsonObject.getString("ID");
                Shared_Preference.setTIME_SHEET_ID(this, NonBillableTimeSheetID);

                try {
                    String output = jsonObject.getString("Output");
                    String msg = jsonObject.getString("Message");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    if (output.equals("1")) {   //means sheet submitted successfully
                        Shared_Preference.setTIMEGAP_JOB_END_TIME(NonBillable_jobs.this, JOB_STOP_DateTime);
                        Shared_Preference.setTIMEGAP_PREV_JOB_START_TIME(NonBillable_jobs.this, JOB_START_DateTime);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();

                    } else if (output.equals("0")) { //overlap
                        dialog_OverlappingTimeEntry();
                    }
                } catch (Exception e) {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
