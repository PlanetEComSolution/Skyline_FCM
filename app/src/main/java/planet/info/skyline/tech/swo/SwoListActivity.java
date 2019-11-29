package planet.info.skyline.tech.swo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.MySwo;
import planet.info.skyline.network.Api;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class SwoListActivity extends AppCompatActivity {
    TextView tv_msg, txtvw_count;
    String compID = "";
    String jobID = "";
    String company_Name = "";
    String job_Name = "";
    boolean IsMySwo = false;
    ArrayList<MySwo> mySwoArrayList = new ArrayList<>();
    ListView listView;
    private RecyclerView recyclerView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swo_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setView();
        getIntentData();

    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            IsMySwo = bundle.getBoolean("MySwo", false);
            String userRole = Shared_Preference.getUSER_ROLE(SwoListActivity.this);
            if (IsMySwo) {  // My SWO/AWO
                if (userRole.equals(Utility.USER_ROLE_ARTIST)) {  //artist
                    setTitle(Utility.getTitle("My AWO(s)"));
                } else {
                    setTitle(Utility.getTitle("My SWO(s)"));
                }

            } else {  // Unassigned SWO/AWO
                if (userRole.equals(Utility.USER_ROLE_ARTIST)) {  //artist
                    setTitle(Utility.getTitle("Unassigned AWO(s"));
                } else {
                    setTitle(Utility.getTitle("Unassigned SWO(s)"));
                }

            }
            if (new ConnectionDetector(SwoListActivity.this).isConnectingToInternet()) {
                new async_getSWOList().execute();
            } else {
                Toast.makeText(SwoListActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }

        }


    }

    private void setView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txtvw_count = findViewById(R.id.txtvw_count);
        tv_msg = findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SwoListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.choose:
                ///  Check_Clock_Status();

                Intent i = new Intent(SwoListActivity.this, SelectCompanyActivity.class);
                i.putExtra(Utility.IS_JOB_MANDATORY, "0");
                startActivityForResult(i, Utility.CODE_SELECT_COMPANY);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usage_report, menu);
        // _menu = menu;
        this.menu = menu;
        return true;
    }

    private void updateMenuTitles() {

        try {
            MenuItem bedMenuItem = menu.findItem(R.id.choose);

            if (!company_Name.equals("")) {
                if (!job_Name.equals("")) {
                    bedMenuItem.setTitle(company_Name + "\n" + job_Name);
                } else {
                    bedMenuItem.setTitle(company_Name);
                }
            } else {
                bedMenuItem.setTitle(getApplicationContext().getResources().getString(R.string.Select_Job));
            }

        } catch (Exception e) {
            e.getCause();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utility.CODE_SELECT_COMPANY) {

            if (resultCode == Activity.RESULT_OK) {
                try {
                    compID = data.getStringExtra("CompID");
                    jobID = data.getStringExtra("JobID");
                    company_Name = data.getStringExtra("CompName");
                    job_Name = data.getStringExtra("JobName");
                    updateMenuTitles();
                    filterSWOList();

                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "Exception caught!", Toast.LENGTH_SHORT).show();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                finish();
            }
        }
    }

    public int getMySwoAwoList() {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String METHOD_NAME = Api.API_MY_SWO_AWO;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;


        // Create SOAP request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = Shared_Preference.getDEALER_ID(this);
        String userID = Shared_Preference.getLOGIN_USER_ID(this);
        final String Role = Shared_Preference.getUSER_ROLE(this);
        request.addProperty("user", userID);
        request.addProperty("DealerID", dealerId);
        request.addProperty("Role", Role);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(receivedString);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            mySwoArrayList.clear();
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String JOB_ID = jsonObject1.getString("JOB_ID");
                    String JOB_DESC = jsonObject1.getString("JOB_DESC");
                    String CompanyName = jsonObject1.getString("CompanyName");

                    String COMP_ID = jsonObject1.getString("COMP_ID");
                    String txt_job = jsonObject1.getString("txt_job");
                    String SWO_Status_new = jsonObject1.getString("SWO_Status_new");
                    String swo_id = jsonObject1.getString("swo_id");
                    String name = jsonObject1.getString("name");
                    String TECH_ID1 = jsonObject1.getString("TECH_ID1");
                    String TECH_ID = jsonObject1.getString("TECH_ID");

                    mySwoArrayList.add(new MySwo(JOB_ID, JOB_DESC, COMP_ID, txt_job,
                            SWO_Status_new, swo_id, name, TECH_ID1, TECH_ID, CompanyName));

                }


            }

            // sort list alphabetically
           /* Collections.sort(mySwoArrayList, new Comparator<MySwo>() {
                @Override
                public int compare(MySwo u1, MySwo u2) {
                    return u1.getName().compareToIgnoreCase(u2.getName());
                }
            });*/


        } catch (Exception e) {
            e.getMessage();
        }
        return mySwoArrayList.size();

    }

    public int getUnassignedSwoAwo() {
        String receivedString = "";
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String METHOD_NAME = Api.API_UNASSIGNED_SWO_AWO;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        String dealerId = Shared_Preference.getDEALER_ID(this);
        final String Role = Shared_Preference.getUSER_ROLE(this);
        request.addProperty("DealerID", dealerId);
        request.addProperty("Role", Role);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(receivedString);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            mySwoArrayList.clear();
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String JOB_ID = jsonObject1.getString("JOB_ID");
                    String JOB_DESC = jsonObject1.getString("JOB_DESC");
                    String CompanyName = jsonObject1.getString("CompanyName");

                    String COMP_ID = jsonObject1.getString("COMP_ID");
                    String txt_job = jsonObject1.getString("txt_job");
                    String SWO_Status_new = jsonObject1.getString("SWO_Status_new");
                    String swo_id = jsonObject1.getString("swo_id");
                    String name = jsonObject1.getString("name");
                    String TECH_ID1 = jsonObject1.getString("TECH_ID1");
                    String TECH_ID = jsonObject1.getString("TECH_ID");

                    mySwoArrayList.add(new MySwo(JOB_ID, JOB_DESC, COMP_ID, txt_job,
                            SWO_Status_new, swo_id, name, TECH_ID1, TECH_ID, CompanyName));

                }
                // sort list alphabetically
               /* Collections.sort(mySwoArrayList, new Comparator<MySwo>() {
                    @Override
                    public int compare(MySwo u1, MySwo u2) {
                        return u1.getName().compareToIgnoreCase(u2.getName());
                    }
                });*/

            }


        } catch (Exception e) {
            e.getMessage();
        }
        return mySwoArrayList.size();

    }

    private void setListAdapter(final ArrayList<MySwo> arrayList) {

        txtvw_count.setText("Total: " + arrayList.size());

        ArrayAdapter<MySwo> stringArrayAdapter = new ArrayAdapter<MySwo>(SwoListActivity.this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(stringArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String comp_id = arrayList.get(i).getCOMP_ID();
                String swo_id = arrayList.get(i).getSwo_id();
                String CompanyName = arrayList.get(i).getCompanyName();
                String JobName = arrayList.get(i).getTxt_job();
                String JOB_ID = arrayList.get(i).getJOB_ID();


                Intent intent = new Intent();
                intent.putExtra("Comp_id", comp_id);
                intent.putExtra("Swo_id", swo_id);
                intent.putExtra("CompanyName", CompanyName);
                intent.putExtra("JobName", JobName);
                intent.putExtra("JOB_ID", JOB_ID);
                setResult(Utility.SWO_LIST_REQUEST_CODE, intent);
                finish();

            }
        });
    }

    private void filterSWOList() {
        if (mySwoArrayList != null && mySwoArrayList.size() > 0) {
            ArrayList<MySwo> filteredSWOList = new ArrayList<>();

            if (!compID.equals("") && jobID.equals("")) {  // only compId
                for (MySwo mySwo : mySwoArrayList) {
                    if (mySwo.getCOMP_ID().equals(compID)) {
                        filteredSWOList.add(mySwo);
                    }
                }
            } else if (compID.equals("") && !jobID.equals("")) {  // only jobId
                for (MySwo mySwo : mySwoArrayList) {
                    if (mySwo.getJOB_ID().equals(jobID)) {
                        filteredSWOList.add(mySwo);
                    }
                }
            } else if (!compID.equals("") && !jobID.equals("")) {  //  compId && jobID
                for (MySwo mySwo : mySwoArrayList) {
                    if (mySwo.getCOMP_ID().equals(compID) && mySwo.getJOB_ID().equals(jobID)) {
                        filteredSWOList.add(mySwo);
                    }
                }
            }

            if (filteredSWOList.size() ==0) {
                tv_msg.setText("No swo found for this Job/Company!");
                tv_msg.setVisibility(View.VISIBLE);
            }else {
                tv_msg.setVisibility(View.GONE);
            }

            setListAdapter(filteredSWOList);


        }
    }

    private class async_getSWOList extends AsyncTask<Void, Void, Integer> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SwoListActivity.this);
            pDialog.setMessage("Kindly wait");
            pDialog.setCancelable(false);
            try {
                pDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
            String userRole = Shared_Preference.getUSER_ROLE(SwoListActivity.this);
            if (mySwoArrayList.size() > 0) {
                tv_msg.setVisibility(View.GONE);
            } else {
                tv_msg.setVisibility(View.VISIBLE);
            }

            if (IsMySwo) {  // My SWO/AWO
                if (userRole.equals(Utility.USER_ROLE_ARTIST)) {  //artist
                    tv_msg.setText("No AWO Available!");
                } else {
                    tv_msg.setText("No SWO Available!");
                }
            } else {  // Unassigned SWO/AWO
                if (userRole.equals(Utility.USER_ROLE_ARTIST)) {  //artist
                    tv_msg.setText("No AWO Available!");
                } else {
                    tv_msg.setText("No SWO Available!");
                }
            }

            setListAdapter(mySwoArrayList);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            int size = 0;
            if (IsMySwo) {

                size = getMySwoAwoList();
            } else {

                size = getUnassignedSwoAwo();


            }
            return size;
        }

    }

}
