package planet.info.skyline.tech.update_timesheet;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import planet.info.skyline.home.MainActivity;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.OverlapTimesheet;
import planet.info.skyline.model.Timesheet;
import planet.info.skyline.tech.billable_timesheet.SubmitTimesheet;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.network.Api;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_EditTimesheet3_Dec;
import static planet.info.skyline.network.Api.API_GetFutureTimeEntry;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

//import android.support.v4.app.FragmentManager;


public class TimeSheetList1Activity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    ListView listview_Clients;
    TextView tv_msg;
    Context context;
    ArrayList<Timesheet> list_TimeSheet = new ArrayList<>();

  //  SharedPreferences sp;
   // SharedPreferences.Editor ed;

    Button Btn_Submit;

    String comment = "";
    boolean api_error = false;
    String tech_id = "",
            jobId = "",
            start_time = "",
            end_time = "",
            description = "",
            code = "",
            region = "",
            status = "",
            dayInfo = "",
            jobType = "", jobDesc = "", Swo_Status = "", JobIdBillable = "",pausedTimesheetID="";
    String str_startTime, str_EndTime, timeSheetID;
    String JOB_START_DateTime;//dd-MM-yyyy HH:mm:ss
    String JOB_STOP_DateTime;//dd-MM-yyyy HH:mm:ss
    String imei = "";
    boolean doubleBackToExitPressedOnce = false;
    AlertDialog alertDialog;
    //  order_adapter.Holder holder;
    boolean startTimeClicked = false;
    int position;
    Timesheet clientUser;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet_new);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(Utility.getTitle("Update Time Sheet"));
        listview_Clients = (ListView) findViewById(R.id.cart_listview);

        tv_msg = (TextView) findViewById(R.id.tv_msg);
        context = TimeSheetList1Activity.this;
        Btn_Submit = (Button) findViewById(R.id.Btn_Submit);
        Btn_Submit.setVisibility(View.GONE);

        //sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
       // ed = sp.edit();
       // tech_id = sp.getString("clientid", "");
         tech_id = Shared_Preference.getLOGIN_USER_ID(this);
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            e.getCause();
        }


        //  getOverlapTimesheetData();


        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new Async_TimesheetList().execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }


        Btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   sendMail();

            }
        });

    }


    public void getTimesheetList() {
        list_TimeSheet.clear();
        getOverlapTimesheetData();
        String strDate = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        strDate = mdformat.format(calendar.getTime());

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetFutureTimeEntry;
        final String METHOD_NAME = API_GetFutureTimeEntry;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("emp", tech_id);
        request.addProperty("DATE_TIME", strDate);
        request.addProperty("start_time", start_time);
        //request.addProperty("start_time", "11:00");
        request.addProperty("end_time", end_time);


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

                    String ID_PK = jsonObject1.getString("ID_PK");
                    String EMP_ID = jsonObject1.getString("EMP_ID");
                    String JOB_ID = jsonObject1.getString("JOB_ID");
                    String AWO_ID = jsonObject1.getString("AWO_ID");
                    String Job_desc = jsonObject1.getString("Job_desc");
                    String compid = jsonObject1.getString("compid");
                    String WORK_CODE = jsonObject1.getString("WORK_CODE");
                    String START_TIME = Utility.getCorrectTime(jsonObject1.getString("START_TIME"));
                    String END_TIME = Utility.getCorrectTime(jsonObject1.getString("END_TIME"));
                    String DESC = jsonObject1.getString("DESC");
                    String TOTAL_HOUR = jsonObject1.getString("TOTAL_HOUR");
                    String DATE_TIME = jsonObject1.getString("DATE_TIME");
                    String SUBMIT_DATE = jsonObject1.getString("SUBMIT_DATE");
                    String basedon = jsonObject1.getString("basedon");
                    String workdays = jsonObject1.getString("workdays");
                    String workdays1 = jsonObject1.getString("workdays1");
                    String AccessStatus = jsonObject1.getString("AccessStatus");
                    String SWO_ID = jsonObject1.getString("SWO_ID");
                    String dayInfo = jsonObject1.getString("dayInfo");
                    String JobName = jsonObject1.getString("JobName");


                    list_TimeSheet.add(new Timesheet(
                            ID_PK,
                            EMP_ID,
                            JOB_ID,
                            AWO_ID,
                            JobName,
                            compid,
                            WORK_CODE,
                            START_TIME,
                            END_TIME,
                            DESC,
                            TOTAL_HOUR,
                            DATE_TIME,
                            SUBMIT_DATE,
                            basedon,
                            workdays,
                            workdays1,
                            AccessStatus,
                            SWO_ID,
                            dayInfo,
                            "0", "0", JOB_ID,"0"));

                } catch (Exception e) {
                    e.getMessage();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //    MenuInflater inflater = getMenuInflater();
        //  inflater.inflate(R.menu.menu_sharefiles, menu);
        // _menu = menu;

        return true;
    }


    private void showProgressDialog(Context context) {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        try {
            mProgressDialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void hideProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String Submit_Billable_TimeSheetNew() {  ///use this fo billable
        String receivedString = "";
        String timesheetID = "";

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + Utility.Method_BillableTimeSheet;
        final String METHOD_NAME =Utility.Method_BillableTimeSheet;

        if (str_EndTime.equals("00:00")) {
            dayInfo = "1";
        }


        SoapObject request_new = new SoapObject(NAMESPACE, METHOD_NAME);
        request_new.addProperty("tech_id", tech_id);
        request_new.addProperty("swo_id", jobId);
        request_new.addProperty("start_time", str_startTime);
        request_new.addProperty("end_time", str_EndTime);
        request_new.addProperty("description", description);
        request_new.addProperty("code", code);
        request_new.addProperty("dayInfo", dayInfo);
        request_new.addProperty("status", status);
        request_new.addProperty("region", region);
        request_new.addProperty("PhoneType", "Android");
        request_new.addProperty("EMI", imei);
        request_new.addProperty("SWOstatus", Swo_Status);
        request_new.addProperty("jobID", JobIdBillable);
        request_new.addProperty("PauseTimeSheetID", pausedTimesheetID);
        Log.e("overlap Billable data", request_new.toString());


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request_new);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONObject jsonObject = new JSONObject(receivedString);
            //  {"ID":"0","Output":"0","Message":"Overlapping Time entries"}
            timesheetID = jsonObject.getString("ID");
        //    ed.putString(Utility.TIME_SHEET_ID, timesheetID).commit();
            Shared_Preference.setTIME_SHEET_ID(this,timesheetID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedString;
    }

    public String submit_NonBillable_Timesheet() {
        String receivedString = "";
        String NonBillableTimeSheetID = "";
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + Api.API_NON_BILLABLE_TIMESHEET;
        final String METHOD_NAME = Api.API_NON_BILLABLE_TIMESHEET;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("tech_id", tech_id);
        request.addProperty("jobId", jobId);
        request.addProperty("start_time", str_startTime);
        request.addProperty("end_time", str_EndTime);
        request.addProperty("description", description);
        request.addProperty("code", code);
        request.addProperty("region", region);
        request.addProperty("status", status);
        request.addProperty("PhoneType", "Android");
        request.addProperty("EMI", imei);

        Log.e("overlap non-Bill data", request.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONObject jsonObject = new JSONObject(receivedString);
            //  {"ID":"0","Output":"0","Message":"Overlapping Time entries"}
            NonBillableTimeSheetID = jsonObject.getString("ID");
          //  ed.putString(Utility.TIME_SHEET_ID, NonBillableTimeSheetID).commit();
            Shared_Preference.setTIME_SHEET_ID(this,NonBillableTimeSheetID);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
        }

        return receivedString;
    }

    public String updateTimeSheet() {

        String dt = Utility.parseDateToMMddyyyy(Utility.getCurrentTimeString());
        String receivedString = "";

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx?";
        final String SOAP_ACTION = KEY_NAMESPACE + API_EditTimesheet3_Dec;
        final String METHOD_NAME = API_EditTimesheet3_Dec;


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("id", timeSheetID);
        request.addProperty("start_time", str_startTime);
        request.addProperty("end_time", str_EndTime);
        request.addProperty("dayInfo", "0");
        request.addProperty("EMP_ID", tech_id);
        request.addProperty("DATE_TIME", dt);

        Log.e("overlap editsheet data", request.toString());


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);
            JSONArray jsonArray = new JSONArray(receivedString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            // JSONObject jsonObject = new JSONObject(receivedString);
            // {"ID":"0","Output":"0","Message":"Overlapping Time entries"}
            //  String TimeSheetID = jsonObject.getString("ID");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return receivedString;
    }

    @Override
    public void onBackPressed() {

        dialog_Exit();


    }

    private void dialog_Exit() {

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
        title.setText("Done all the changes?");
        desc.setText("");
        // message.setText("Please set Automatic date and Time");

        positiveBtn.setText("Yes");
        negativeBtn.setText("No");
        negativeBtn.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Utility.removeAllOverlapTimesheetData(TimeSheetList1Activity.this);
                startActivity(new Intent(TimeSheetList1Activity.this, MainActivity.class));
                finish();
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

    private void Click_getTime(int hr, int min) {
        boolean is24HourMode = true;
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                TimeSheetList1Activity.this,
                hr,
                min,
                is24HourMode);


        tpd.setThemeDark(false);
        tpd.vibrate(false);
        tpd.dismissOnPause(false);
        tpd.enableSeconds(false);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        //  tpd.setAccentColor(Color.parseColor("#9C27B0"));
        tpd.setAccentColor(getResources().getColor(R.color.colorAccent));
        tpd.setTitle("Change Time");

        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        FragmentManager f = getFragmentManager();
        tpd.show(f, "Timepickerdialog");


    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String times = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
        String time = hourString + ":" + minuteString;

        if (startTimeClicked) {
            //    holder.startTime.setText(time);


//            int position = Integer.valueOf(holder.startTime.getTag().toString());
//            Timesheet clientUser = list_TimeSheet.get(position);
            clientUser.setsTARTTIME(time);
            list_TimeSheet.set(position, clientUser);


        } else {
            // holder.endTime.setText(t);
//            int position = Integer.valueOf(holder.endTime.getTag().toString());
//            Timesheet clientUser = list_TimeSheet.get(position);
            clientUser.seteNDTIME(time);
            list_TimeSheet.set(position, clientUser);
        }


        order_adapter adapter = null;
        adapter = new order_adapter(context);
        listview_Clients.setAdapter(adapter);


    }



    private void getOverlapTimesheetData() {

        list_TimeSheet.clear();

        try {

            ArrayList<OverlapTimesheet> list_overlapData = Utility.getOverlapTimesheetDataList(TimeSheetList1Activity.this);

            if (list_overlapData.size() > 0) {
                for (int i = 0; i < list_overlapData.size(); i++) {

                    String jobId = list_overlapData.get(i).getJobId();
                    start_time = list_overlapData.get(i).getStart_time();
                    end_time = list_overlapData.get(i).getEnd_time();
                    String description = list_overlapData.get(i).getDescription();
                    String code = list_overlapData.get(i).getCode();
                    String region = list_overlapData.get(i).getRegion();
                    String status = list_overlapData.get(i).getStatus();
                    String dayInfo = list_overlapData.get(i).getDayInfo();
                    String jobType = list_overlapData.get(i).getJobType();
                    String jobDesc = list_overlapData.get(i).getJobDesc();
                    String Swo_Status = list_overlapData.get(i).getSwo_Status();
                    String JobIdBillable = list_overlapData.get(i).getJobIdBillable();
                    String pausedTimesheet = list_overlapData.get(i).getPausedTimesheetId();

                    list_TimeSheet.add(new Timesheet(
                            "",
                            "",
                            jobId,
                            "",
                            jobDesc,
                            "",
                            code,
                            start_time,
                            end_time,
                            description,
                            "",
                            "",
                            "",
                            "",
                            status,
                            region,
                            Swo_Status,
                            "",
                            dayInfo,
                            "1", jobType, JobIdBillable,pausedTimesheet));
                }
            }


        } catch (Exception e) {
            e.getMessage();
        }





    }

    class Async_TimesheetList extends AsyncTask<Void, Void, Void> {

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


            getTimesheetList();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();

            order_adapter adapter = null;


            if (list_TimeSheet.size() < 1) {
                tv_msg.setVisibility(View.VISIBLE);
            } else {
                tv_msg.setVisibility(View.GONE);
            }
            adapter = new order_adapter(context);
            listview_Clients.setAdapter(adapter);


        }
    }

    public class order_adapter extends BaseAdapter {
        // List<Timesheet> beanArrayList;
        Context context;
        int count = 1;

        public order_adapter(Context context) {
            this.context = context;
            //   this.beanArrayList = beanArrayList;

        }

        @Override
        public int getCount() {
            return list_TimeSheet.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertview, ViewGroup viewGroup) {

            final Holder holder;
            final String TimeSheetID = list_TimeSheet.get(i).getiDPK();
            final String DESC = list_TimeSheet.get(i).getdESC();
            final String STARTTIME = list_TimeSheet.get(i).getsTARTTIME();
            final String ENDTIME = list_TimeSheet.get(i).geteNDTIME();
            final String JobDesc = list_TimeSheet.get(i).getJobDesc();
            final String jobType = list_TimeSheet.get(i).getIsBillable();
            final String isCurrentTimeSheet = list_TimeSheet.get(i).getIsCurrentTimeSheet();

            if (convertview == null) {
                holder = new Holder();

                convertview = LayoutInflater.from(context).inflate(R.layout.row_timesheet, null);
                holder.index_no = (Button) convertview.findViewById(R.id.serial_no);
                holder.Desc = (TextView) convertview.findViewById(R.id.Desc);
                holder.jobDesc = (TextView) convertview.findViewById(R.id.jobDesc);
                holder.startTime = (EditText) convertview.findViewById(R.id.startTime);
                holder.endTime = (EditText) convertview.findViewById(R.id.endTime);
                holder.checkBox = (CheckBox) convertview.findViewById(R.id.checkBox);
                holder.update = (Button) convertview.findViewById(R.id.update);
                holder.img_badge = (ImageView) convertview.findViewById(R.id.img_badge);


                convertview.setTag(holder);
            } else {
                holder = (Holder) convertview.getTag();
            }
            holder.index_no.setText(String.valueOf(i + 1));

            if (DESC.equals(null) || DESC.trim().equals("")) {
                holder.Desc.setText("Not available");
            } else {
                holder.Desc.setText(DESC);
            }

            if (JobDesc.equals(null) || JobDesc.trim().equals("")) {
                holder.jobDesc.setText("Not available");
            } else {
                holder.jobDesc.setText(JobDesc);
            }


            holder.startTime.setTag(i);
            if (STARTTIME.equals(null) || STARTTIME.trim().equals("")) {
                holder.startTime.setText("00:00");
            } else {
                holder.startTime.setText(STARTTIME);
            }


            holder.startTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTimeClicked = true;
                    position = Integer.valueOf(holder.startTime.getTag().toString());
                    clientUser = list_TimeSheet.get(position);

                    String time = holder.startTime.getText().toString().trim();
                    String[] tm = time.split(":");
                    String hr = tm[0];
                    String min = tm[1];
                    int hour = 0, minutes = 0;
                    try {
                        hour = Integer.parseInt(hr);
                        minutes = Integer.parseInt(min);
                    } catch (Exception e) {
                        e.getCause();
                    }

                    Click_getTime(hour, minutes);
                }
            });


            holder.endTime.setTag(i);

            if (ENDTIME.equals(null) || ENDTIME.trim().equals("")) {
                holder.endTime.setText("00:00");
            } else {
                holder.endTime.setText(ENDTIME);
            }


            holder.endTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTimeClicked = false;
                    position = Integer.valueOf(holder.startTime.getTag().toString());
                    clientUser = list_TimeSheet.get(position);

                    String time = holder.endTime.getText().toString().trim();
                    String[] tm = time.split(":");
                    String hr = tm[0];
                    String min = tm[1];
                    int hour = 0, minutes = 0;
                    try {
                        hour = Integer.parseInt(hr);
                        minutes = Integer.parseInt(min);
                    } catch (Exception e) {
                        e.getCause();
                    }

                    Click_getTime(hour, minutes);
                }
            });


            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    str_startTime = list_TimeSheet.get(i).getsTARTTIME();
                    str_EndTime = list_TimeSheet.get(i).geteNDTIME();
                    timeSheetID = list_TimeSheet.get(i).getiDPK();
                    JobIdBillable = list_TimeSheet.get(i).getJOB_ID_BILLABLE();
                    code = list_TimeSheet.get(i).getwORKCODE();
                    jobId = list_TimeSheet.get(i).getjOBID();
                    status = list_TimeSheet.get(i).getWorkdays();
                    region = list_TimeSheet.get(i).getWorkdays1();
                    description = list_TimeSheet.get(i).getdESC();
                    dayInfo = list_TimeSheet.get(i).getDayInfo();
                    Swo_Status = list_TimeSheet.get(i).getAccessStatus();
                    pausedTimesheetID = list_TimeSheet.get(i).getPausedTimesheetId();
                    if (new ConnectionDetector(TimeSheetList1Activity.this).isConnectingToInternet()) {
                        if (isCurrentTimeSheet.equals("1")) {
                            if (jobType.equals(Utility.NON_BILLABLE)) {
                                new Async_Submit_NonBillable_Timesheet().execute();
                            } else
                                //   if (jobType.equals(Utility.BILLABLE)) {
                                new Async_Submit_Billable_Timesheet_New().execute();
                            //  } else if (jobType.equals(Utility.CHANGE_TIME_CODE)) {
                            //      new Async_ChangeTimeCode_Submit().execute();
                            //  }
                        } else {
                            new Async_UpdateTimeSheet().execute();
                        }
                    } else {
                        Toast.makeText(TimeSheetList1Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }
                }
            });


            if (list_TimeSheet.get(i).getIsCurrentTimeSheet().equals("1")) {
                holder.img_badge.setImageResource(R.drawable.current);
                holder.img_badge.setVisibility(View.VISIBLE);
            } else {
                //  String old_startTime = list_TimeSheet.get(i).getsTARTTIME();
                //  String old_EndTime = list_TimeSheet.get(i).geteNDTIME();

                //  if (isEntryOverLaps(start_time, end_time, old_startTime, old_EndTime)) {
                holder.img_badge.setImageResource(R.drawable.overlap);
                holder.img_badge.setVisibility(View.VISIBLE);
                //  } else {
                //      holder.img_badge.setVisibility(View.GONE);
                //  }


            }


            return convertview;
        }


        class Holder {
            TextView Desc, jobDesc;
            EditText endTime, startTime;
            Button index_no;
            CheckBox checkBox;
            //ImageView imgvw_ok;
            Button update;
            ImageView img_badge;

        }


    }

    public class Async_Submit_NonBillable_Timesheet extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(TimeSheetList1Activity.this);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();

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
                msg = "Some error occurred!";
            }

          //  JOB_START_DateTime = sp.getString(Utility.CLOCK_START_TIME, "");//dd-MM-yyyy HH:mm:ss
            JOB_START_DateTime =Shared_Preference.getCLOCK_START_TIME(TimeSheetList1Activity.this);//dd-MM-yyyy HH:mm:ss
            JOB_STOP_DateTime = Utility.getCurrentTimeString();//dd-MM-yyyy HH:mm:ss

            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            if (output.equals("1")) {   //means sheet submitted successfully
               // ed.putString(Utility.TIMEGAP_JOB_END_TIME, JOB_STOP_DateTime).commit();//for next job time gap

                Shared_Preference.setTIMEGAP_JOB_END_TIME(TimeSheetList1Activity.this,JOB_STOP_DateTime);
             //   ed.putString(Utility.TIMEGAP_PREV_JOB_START_TIME, JOB_START_DateTime).commit();//for next job time gap
                Shared_Preference.setTIMEGAP_PREV_JOB_START_TIME(TimeSheetList1Activity.this, JOB_START_DateTime);
                Utility.removeAllOverlapTimesheetData(TimeSheetList1Activity.this);
                startActivity(new Intent(TimeSheetList1Activity.this, MainActivity.class));
                finish();
            }


        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String NonBillableTimeSheetID = submit_NonBillable_Timesheet();
            Log.e("NonBillableTimeSheetID", NonBillableTimeSheetID);
            return NonBillableTimeSheetID;
        }

    }

    public class Async_Submit_Billable_Timesheet_New extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(TimeSheetList1Activity.this);
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressDialog();

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
                msg = "Some error occurred!";
            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            if (output.equals("1")) {   //means sheet submitted successfully

                Shared_Preference.setTIMEGAP_JOB_END_TIME(TimeSheetList1Activity.this,JOB_STOP_DateTime);


                Shared_Preference.setTIMEGAP_PREV_JOB_START_TIME(TimeSheetList1Activity.this, JOB_START_DateTime);
                Utility.removeAllOverlapTimesheetData(TimeSheetList1Activity.this);
                startActivity(new Intent(TimeSheetList1Activity.this, MainActivity.class));
                finish();
            }


           /* if (new ConnectionDetector(context).isConnectingToInternet()) {
                new Async_TimesheetList().execute();
            } else {
                Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
            }*/


        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String timesheet_id = Submit_Billable_TimeSheetNew();
            return timesheet_id;
        }

    }

    private class Async_UpdateTimeSheet extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(TimeSheetList1Activity.this);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res = updateTimeSheet();
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            JSONObject jsonObject = null;
            String output = "";
            String msg = "Failed!";
            try {
                JSONArray jsonArray = new JSONArray(result);
                jsonObject = jsonArray.getJSONObject(0);

                Log.e("AdminTimeSheetId", result);

                msg = jsonObject.getString("msg");
            } catch (Exception e) {
                e.getCause();
            }


            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            if (new ConnectionDetector(context).isConnectingToInternet()) {
                new Async_TimesheetList().execute();
            } else {
                Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
            }

        }
    }


}
