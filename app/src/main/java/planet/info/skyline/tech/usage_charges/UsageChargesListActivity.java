package planet.info.skyline.tech.usage_charges;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Locale;

import planet.info.skyline.R;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivity;
import planet.info.skyline.adapter.CompanyNameAdapterNew;
import planet.info.skyline.adapter.JobNameAdapterNew;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.Company;
import planet.info.skyline.model.Job_1;
import planet.info.skyline.model.UsageCharge;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.network.Api;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_BindJob;
import static planet.info.skyline.network.Api.API_bindClientByDealer;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class UsageChargesListActivity extends AppCompatActivity {
    TextView tv_msg;
    ArrayList<UsageCharge> usageChargesList = new ArrayList<>();


    String compID = "";
    String jobID = "";
    String company_Name = "";
    String job_Name = "";
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_charges_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(Utility.getTitle("Usage Charges List"));
        setView();
        final boolean TIMER_STARTED_FROM_BILLABLE_MODULE =   Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(this);

        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            compID =   Shared_Preference.getCOMPANY_ID_BILLABLE(this);
             jobID = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
            company_Name  = Shared_Preference.getCLIENT_NAME(this);
            job_Name =   Shared_Preference.getJOB_NAME_BILLABLE(this);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsageChargesListActivity.this, UsageChargesUpdateActivity.class);
                intent.putExtra(Utility.USAGE_CHARGE_ID, "0");
                intent.putExtra(Utility.IS_ADD, "1");
                intent.putExtra("jobID", jobID);
                intent.putExtra("compid", compID);
                intent.putExtra("vendorid", "");
                intent.putExtra("Job", job_Name);
                intent.putExtra("Company", company_Name);
                intent.putExtra("Vendor", "");
                intent.putExtra("desc", "");
                intent.putExtra("cost", "");
                intent.putExtra("quantity", "");
                intent.putExtra("total", "");

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check_Clock_Status();
        if (new ConnectionDetector(UsageChargesListActivity.this).isConnectingToInternet()) {
            new async_list_usage_report().execute();
        } else {
            Toast.makeText(UsageChargesListActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    private void setView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        tv_msg = findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(UsageChargesListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public String ListUsageReport() {
        usageChargesList.clear();
        String dealer_id =  Shared_Preference.getDEALER_ID(this);
        String receivedString = "";

        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String METHOD_NAME = Api.API_USAGE_REPORT_LIST;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("client", compID);
        request.addProperty("job", jobID);
        request.addProperty("dealerID", dealer_id);
        request.addProperty("dealerType", "0");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);

            // {"status":"1","Message":"Inserted Successfully.","ReturnID":"0"}
            //      JSONObject jsonObject = new JSONObject(receivedString);

            JSONArray jsonArray = new JSONArray(receivedString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String Usage_id = jsonObject.getString("Usage_id");
                String Quantity = jsonObject.getString("Quantity");
                String username = jsonObject.getString("username");
                String Username1 = jsonObject.getString("Username1");
               // String cost =String.valueOf( jsonObject.getDouble("cost"));
                double _cost=jsonObject.getDouble("cost");
                String cost=   String.format(Locale.US,"%.2f", _cost);


                String Vendor = jsonObject.getString("Vendor");
                String Vendorid = jsonObject.getString("Vendorid");
                String Descriptions = jsonObject.getString("Descriptions");
              //  String Total = String.valueOf( jsonObject.getString("Total"));

                double _Total=jsonObject.getDouble("Total");
                String Total=   String.format(Locale.US,"%.2f", _Total);


                String job = jsonObject.getString("job");
                String jobid = jsonObject.getString("jobid");
                String Company = jsonObject.getString("Company");
                String Companyid = jsonObject.getString("Companyid");

                usageChargesList.add(new UsageCharge(Usage_id,
                        Quantity,
                        username,
                        Username1,
                        cost,
                        Vendor,
                        Vendorid,
                        Descriptions,
                        Total,
                        job,
                        jobid,
                        Company,
                        Companyid));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedString;
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

                Intent i = new Intent(UsageChargesListActivity.this, SelectCompanyActivity.class);
                i.putExtra(Utility.IS_JOB_MANDATORY,"0");
                i.putExtra(Utility.Show_DIALOG_SHOW_INFO,false);
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


                    onResume();
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

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
        private List<UsageCharge> moviesList;
        //   private HttpImageManager mHttpImageManager;

        public MoviesAdapter(Activity context, List<UsageCharge> moviesList) {
            this.moviesList = moviesList;
            //     mHttpImageManager = ((AppController) context.getApplication()).getHttpImageManager();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_usage_report, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final UsageCharge usageCharge = moviesList.get(position);
            holder.index_no.setText(String.valueOf(position + 1));


            holder.tv_job_number.setText(usageCharge.getJob());
            holder.tv_company.setText(usageCharge.getCompany());
            holder.tv_User.setText(usageCharge.getUsername());
            holder.tv_Vendor.setText(usageCharge.getVendor());
            holder.tv_Desc.setText(usageCharge.getDescriptions());

            holder.tv_quantity.setText(usageCharge.getQuantity());
            holder.tv_cost.setText( usageCharge.getCost());
            holder.tv_total.setText(usageCharge.getTotal());

            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UsageChargesListActivity.this, UsageChargesUpdateActivity.class);
                    intent.putExtra(Utility.USAGE_CHARGE_ID, usageCharge.getUsage_id());
                    intent.putExtra(Utility.IS_ADD, "0");
                    intent.putExtra("jobID", usageCharge.getJobid());
                    intent.putExtra("compid", usageCharge.getCompanyid());
                    intent.putExtra("vendorid", usageCharge.getVendorid());
                    intent.putExtra("Job", usageCharge.getJob());
                    intent.putExtra("Company", usageCharge.getCompany());
                    intent.putExtra("Vendor", usageCharge.getVendor());
                    intent.putExtra("desc", usageCharge.getDescriptions());
                    intent.putExtra("cost", usageCharge.getCost());
                    intent.putExtra("quantity", usageCharge.getQuantity());
                    intent.putExtra("total", usageCharge.getTotal());
                    startActivity(intent);


                }
            });

            holder.imgvw_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UsageChargesListActivity.this, UsageChargesUpdateActivity.class);
                    intent.putExtra(Utility.USAGE_CHARGE_ID, usageCharge.getUsage_id());
                    intent.putExtra(Utility.IS_ADD, "0");

                    intent.putExtra("jobID", usageCharge.getJobid());
                    intent.putExtra("compid", usageCharge.getCompanyid());
                    intent.putExtra("vendorid", usageCharge.getVendorid());
                    intent.putExtra("Job", usageCharge.getJob());
                    intent.putExtra("Company", usageCharge.getCompany());
                    intent.putExtra("Vendor", usageCharge.getVendor());
                    intent.putExtra("desc", usageCharge.getDescriptions());
                    intent.putExtra("cost", usageCharge.getCost());
                    intent.putExtra("quantity", usageCharge.getQuantity());
                    intent.putExtra("total", usageCharge.getTotal());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_job_number,
                    tv_company,
                    tv_User,
                    tv_Vendor,
                    tv_Desc,
                    tv_quantity,
                    tv_cost,
                    tv_total;
            ImageView imgvw_edit;
            Button index_no;
            LinearLayout parentView;

            public MyViewHolder(View convertview) {
                super(convertview);

                parentView = convertview.findViewById(R.id.row_jobFile);
                index_no = (Button) convertview.findViewById(R.id.serial_no);


                tv_job_number = (TextView) convertview.findViewById(R.id.tv_job_number);
                tv_company = (TextView) convertview.findViewById(R.id.tv_company);
                tv_User = (TextView) convertview.findViewById(R.id.tv_User);
                tv_Vendor = (TextView) convertview.findViewById(R.id.tv_Vendor);
                tv_Desc = (TextView) convertview.findViewById(R.id.tv_Desc);
                tv_quantity = (TextView) convertview.findViewById(R.id.tv_quantity);
                tv_cost = (TextView) convertview.findViewById(R.id.tv_cost);
                tv_total = (TextView) convertview.findViewById(R.id.tv_total);
                imgvw_edit = convertview.findViewById(R.id.imgvw_edit);

            }
        }
    }

    private  class async_list_usage_report extends AsyncTask<Void, Void, String> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(UsageChargesListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                ringProgressDialog.setMessage(getString(R.string.Loading_text));
                ringProgressDialog.setCancelable(false);
                ringProgressDialog.setCanceledOnTouchOutside(false);

                try {
                    ringProgressDialog.show();
                } catch (Exception e) {
                    e.getMessage();
                }


            } catch (Exception e) {
                e.getMessage();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            String result = ListUsageReport();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//{"status":"1","Message":"Inserted Successfully.","ReturnID":"0"}
            try {
                ringProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
            updateMenuTitles();
            if (usageChargesList.isEmpty()) {
                // Toast.makeText(getApplicationContext(),"No data found!",Toast.LENGTH_SHORT).show();
                tv_msg.setVisibility(View.VISIBLE);
                /*try {
                    dialog_companyName.show();
                } catch (Exception e) {
                    e.getMessage();
                }*/
            } else {
                tv_msg.setVisibility(View.GONE);
                mAdapter = new MoviesAdapter(UsageChargesListActivity.this, usageChargesList);
                recyclerView.setAdapter(mAdapter);
            }


        }
    }


}
