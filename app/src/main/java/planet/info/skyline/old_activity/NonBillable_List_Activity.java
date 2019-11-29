package planet.info.skyline.old_activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.floating_view.ChatHeadService;
import planet.info.skyline.network.Api;
import planet.info.skyline.tech.non_billable_timesheet.NonBillable_jobs;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_GetSkylineJob;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP1;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class NonBillable_List_Activity extends BaseActivity {
    private static final int CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    ProgressDialog pDialog;
    ListView l1;
    String nextact, webhit, clientidme;
    SharedPreferences sp;
    int count;
    SharedPreferences.Editor ed;
    List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_billable__list);

        pDialog = new ProgressDialog(NonBillable_List_Activity.this);

        pDialog.setMessage("Kindly wait");
        pDialog.setCancelable(false);

        sp = getApplicationContext().getSharedPreferences("skyline",
                getApplicationContext().MODE_PRIVATE);
        ed = sp.edit();

        //  clientidme = sp.getString("clientid", "");
        clientidme = Shared_Preference.getLOGIN_USER_ID(this);
        l1 = (ListView) findViewById(R.id.listView);

        if (new ConnectionDetector(NonBillable_List_Activity.this).isConnectingToInternet()) {
            new getSwofor_NonBillable().execute();
        } else {
            Toast.makeText(NonBillable_List_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void GetSwoByJob_nobillable()   ///by aman kaushik
    {
        count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetSkylineJob;
        final String METHOD_NAME = API_GetSkylineJob;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

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
                JSONObject jsonObject = new JSONObject(k);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.clear();
                    String JOB_ID = jsonObject1.getString("JOB_ID");
                    String JOB_DESC = jsonObject1.getString("JOB_DESC");
                    String COMP_ID = jsonObject1.getString("COMP_ID");
                    String txt_job = jsonObject1.getString("txt_job");
                    map.put("JOB_ID", JOB_ID);
                    map.put("JOB_DESC", JOB_DESC);
                    map.put("COMP_ID", COMP_ID);
                    map.put("txt_job", txt_job);
                    data.add(map);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void showChatHead(Context context, boolean isShowOverlayPermission) {
        // API22以下かチェック
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startService(new Intent(context, ChatHeadService.class));
            return;
        }

        // 他のアプリの上に表示できるかチェック
        if (Settings.canDrawOverlays(context)) {
            context.startService(new Intent(context, ChatHeadService.class));
            return;
        }

        // オーバレイパーミッションの表示
        if (isShowOverlayPermission) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }

    class getSwofor_NonBillable extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            GetSwoByJob_nobillable();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } catch (Exception e) {
                e.getMessage();
            }
            SubAdapter adapter = new SubAdapter(NonBillable_List_Activity.this, data);
            l1.setAdapter(adapter);

           /*     String jobid = JOB_ID_forNonBillable;
                String clientid = COMP_ID_forNonBillable;

                nextact = "http://exhibitpower.com/crate_web_service.php?id="
                        + clientid;
                webhit = "http://exhibitpower2.com/Register/auto_generate_event2.aspx?done_by="
                        + clientidme + "&swo_id=" + jobid + "&status=start";
                ed.putString("link", nextact);
                ed.putString("jobid", jobid);
                ed.commit();

                //  eventgenerate();

                System.out.println("---------------------------------------------------- its is non billiable"); //by Aman Kaushik
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ");
                String time1 = sdf.format(dt);
                System.out.println("time"+time1);
                ed.putString("starttimenew",time1).commit();


                Intent ii = new Intent(NonBillable_List_Activity.this, NonBillable_jobs.class);
                Intent uuu = new Intent(NonBillable_List_Activity.this, Timerclass.class);
                startService(uuu);
                ii.putExtra("dataa", nextact);
                ii.putExtra("clientid", clientid);
                startActivity(ii);
                finish();*/

        }
    }

    public class SubAdapter extends BaseAdapter {
        List<HashMap<String, String>> beanArrayList;
        Context context;

        public SubAdapter(Context context, List<HashMap<String, String>> beanArrayList) {
            this.context = context;
            this.beanArrayList = beanArrayList;

        }

        @Override
        public int getCount() {
            return beanArrayList.size();
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


            final String JOB_DESC = beanArrayList.get(i).get("JOB_DESC");
            final String JOB_ID = beanArrayList.get(i).get("JOB_ID");
            final String COMP_ID = beanArrayList.get(i).get("COMP_ID");
            final String txt_job = beanArrayList.get(i).get("txt_job");
            if (convertview == null) {
                holder = new Holder();
                convertview = LayoutInflater.from(context).inflate(R.layout.list_item, null);
                holder.location1 = (TextView) convertview.findViewById(R.id.text);

                convertview.setTag(holder);
            } else {
                holder = (Holder) convertview.getTag();
            }

            holder.location1.setText(JOB_DESC);
            holder.location1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String jobid = JOB_ID;
                    String clientid = COMP_ID;

                    nextact = URL_EP1 + Api.API_CRATES_LIST
                            + clientid;
                    webhit = URL_EP2 + "/Register/auto_generate_event2.aspx?done_by="
                            + clientidme + "&swo_id=" + jobid + "&status=start";
                    //   ed.putString("link", nextact);
                    Shared_Preference.setLINK(NonBillable_List_Activity.this, nextact);

                    //   ed.putString("jobid", jobid);
                    Shared_Preference.setSWO_ID(NonBillable_List_Activity.this, jobid);
                    ed.commit();

                    //  eventgenerate();

                    System.out.println("---------------------------------------------------- its is non billiable"); //by Aman Kaushik
                    Date dt = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ");
                    String time1 = sdf.format(dt);
                    System.out.println("time" + time1);
                    //  ed.putString("starttimenew", time1).commit();
                  //  Shared_Preference.setCLOCK_START_TIME(NonBillable_List_Activity.this, time1);

                    //ed.putBoolean(Utility.TIMER_STARTED_FROM_ADMIN_CLOCK_MODULE, true).commit();//nks
                    Shared_Preference.setTIMER_STARTED_FROM_ADMIN_CLOCK_MODULE(NonBillable_List_Activity.this,true);
                    ed.putString(Utility.CLIENT_ID_FOR_NON_BILLABLE, clientid).commit();//nks

                    Intent ii = new Intent(NonBillable_List_Activity.this, NonBillable_jobs.class);
                   /* Intent uuu = new Intent(NonBillable_List_Activity.this, Timerclass.class);
                    startService(uuu);*/

                    showChatHead(NonBillable_List_Activity.this, true);
                    ii.putExtra("dataa", nextact);
                    ii.putExtra("clientid", clientid);

                    startActivity(ii);
                    finish();


                }
            });
            return convertview;
        }

        class Holder {
            TextView location1;
        }
    }
}
