package planet.info.skyline.tech.usage_charges;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import planet.info.skyline.adapter.CompanyNameAdapterNew;
import planet.info.skyline.adapter.JobNameAdapterNew;
import planet.info.skyline.adapter.VendorAdapter;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.Company;
import planet.info.skyline.model.Job_1;
import planet.info.skyline.model.Vendor;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.network.Api;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_BindJob;
import static planet.info.skyline.network.Api.API_bindClientByDealer;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class UsageChargesUpdateActivity extends AppCompatActivity {

    AutoCompleteTextView et_job, et_company, et_Vendor;
    EditText Description, Cost, Total, Quantity;
    ArrayList<Company> List_Company = new ArrayList<>();
    ArrayList<Company> List_Company_forIndex = new ArrayList<>();
    ArrayList<Job_1> List_Job = new ArrayList<>();
    ArrayList<Job_1> List_Job_forIndex = new ArrayList<>();

    ArrayList<Vendor> List_Vendor = new ArrayList<>();
    ArrayList<Vendor> List_Vendor_forIndex = new ArrayList<>();

    AlertDialog alertDialog;
   // SharedPreferences sp;
    //SharedPreferences.Editor ed;
    String job = "", comp_id_name = "";

    Button Btn_Save, Btn_Cancel;
    String compID = "";
    String jobID = "";
    String vendor_id = "";


    String description = "";
    String quantity = "";
    String cost = "";
    String total = "";

    String IsAdd = "1", UsageChargeID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_charges);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(Utility.getTitle("Add Usage Charges"));
       // sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
        //ed = sp.edit();

        setView();
        getDatafromIntent();

        if (new ConnectionDetector(UsageChargesUpdateActivity.this).isConnectingToInternet()) {
             new get_company_name().execute();
        } else {
            Toast.makeText(UsageChargesUpdateActivity.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }

    private void setView() {
        et_job = findViewById(R.id.job);
        et_company = findViewById(R.id.company);
        et_Vendor = findViewById(R.id.Vendor);
        Description = findViewById(R.id.Description);

        Quantity = findViewById(R.id.Quantity);

        Cost = findViewById(R.id.Cost);
        Total = findViewById(R.id.Total);
        Btn_Save = findViewById(R.id.Btn_Save);
        Btn_Cancel = findViewById(R.id.Btn_Cancel);
        Quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getTotal();
            }
        });

        Cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getTotal();
            }
        });


        Btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comp = et_company.getText().toString().trim();
                String job = et_job.getText().toString().trim();
                String vendor = et_Vendor.getText().toString().trim();
                description = Description.getText().toString().trim();
                quantity = Quantity.getText().toString().trim();
                cost = Cost.getText().toString().trim();
                total = Total.getText().toString().trim();

                compID = "";
                jobID = "";
                vendor_id = "";

                for (int i = 0; i < List_Company_forIndex.size(); i++) {
                    String cmp = List_Company_forIndex.get(i).getEname();
                    if (cmp.equals(comp)) {
                        compID = List_Company_forIndex.get(i).getId();
                        break;
                    }
                }
                for (int i = 0; i < List_Job_forIndex.size(); i++) {
                    String jobname = List_Job_forIndex.get(i).getJobName();
                    if (jobname.equals(job)) {
                        jobID = List_Job_forIndex.get(i).getJobId();
                        break;
                    }
                }
                for (int i = 0; i < List_Vendor_forIndex.size(); i++) {
                    String Vendername = List_Vendor_forIndex.get(i).getVenderName();
                    if (Vendername.equals(vendor)) {
                        vendor_id = List_Vendor_forIndex.get(i).getVenderID();
                        break;
                    }
                }
                if (compID.equals("")) {
                    // et_company.setError("Please enter valid company name!");
                    Toast.makeText(getApplicationContext(), "Please enter valid company name!", Toast.LENGTH_SHORT).show();
                } else if (jobID.equals("")) {
                    //  et_job.setError("Please enter valid job name!");
                    Toast.makeText(getApplicationContext(), "Please enter valid job name!", Toast.LENGTH_SHORT).show();
                } else if (vendor_id.equals("")) {
                    //  et_Vendor.setError("Please enter valid vendor name!");
                    Toast.makeText(getApplicationContext(), "Please enter valid vendor name!", Toast.LENGTH_SHORT).show();
                } else if (description.equals("")) {
                    //  Description.setError("Please enter description!");
                    Toast.makeText(getApplicationContext(), "Please enter description!", Toast.LENGTH_SHORT).show();
                } else if (quantity.equals("")) {
                    // Quantity.setError("Please enter quantity!");
                    Toast.makeText(getApplicationContext(), "Please enter quantity!", Toast.LENGTH_SHORT).show();
                } else if (cost.equals("")) {
                    // Cost.setError("Please enter cost!");
                    Toast.makeText(getApplicationContext(), "Please enter cost!", Toast.LENGTH_SHORT).show();
                } else if (total.equals("")) {
                    // Total.setError("Please enter total cost!");
                    Toast.makeText(getApplicationContext(), "Please enter total cost!", Toast.LENGTH_SHORT).show();
                } else {
                    new async_save_usage_report().execute();
                }

            }
        });
        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getDatafromIntent() {
        try {
            IsAdd = getIntent().getStringExtra(Utility.IS_ADD);
            UsageChargeID = getIntent().getStringExtra(Utility.USAGE_CHARGE_ID);

            if (!IsAdd.equals("1")) {
                setTitle(Utility.getTitle("Update Usage Charges"));
            }
            jobID = getIntent().getStringExtra("jobID");
            compID = getIntent().getStringExtra("compid");

            vendor_id = getIntent().getStringExtra("vendorid");
            String Job = getIntent().getStringExtra("Job");
            String Company = getIntent().getStringExtra("Company");
            String Vendor = getIntent().getStringExtra("Vendor");
            description = getIntent().getStringExtra("desc");
            cost = getIntent().getStringExtra("cost");
            quantity = getIntent().getStringExtra("quantity");
            total = getIntent().getStringExtra("total");

            et_company.setText(Company);
            et_job.setText(Job);
            et_Vendor.setText(Vendor);
            Description.setText(description);
            Quantity.setText(quantity);
            Cost.setText(cost);
            Total.setText(total);
            if (!compID.equals("")) {
                if (new ConnectionDetector(UsageChargesUpdateActivity.this).isConnectingToInternet()) {
                    new get_company_job_id().execute(compID);
                } else {
                    Toast.makeText(UsageChargesUpdateActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void getTotal() {
        try {
            String quantity_str = Quantity.getText().toString().trim();
            double quantity = Double.parseDouble(quantity_str);
            String Cost_str = Cost.getText().toString().trim();
            double cost = Double.parseDouble(Cost_str);
            double total = quantity * cost;
            Total.setText(String.valueOf(total));
        } catch (Exception e) {
            e.getMessage();
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

    private void BindDataCompany() {
        CompanyNameAdapterNew companyNameAdapter = new CompanyNameAdapterNew(UsageChargesUpdateActivity.this, android.R.layout.simple_list_item_1, List_Company);
        et_company.setAdapter(companyNameAdapter);
        et_company.setDropDownHeight(550);

        et_company.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_company.showDropDown();
                /*to clear autocomplete*/
          /*      final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (et_company.getRight() - et_company.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            et_company.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }*/
                /**/

                return false;
            }
        });

        et_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (et_company.getText().equals("--Select--") || (et_company.getText().length() == 0)) {

                } else {
                    //    pankajtester_chutya = 1;
                    //   String checc = company_name.getText().toString();
                    //   int index = company_Name_list_forIndex.indexOf(company_name.getText().toString());
                    //


                    int index = 0;
                    for (int j = 0; j < List_Company_forIndex.size(); j++) {
                        Company company = List_Company_forIndex.get(j);
                        if (company.getEname() != null && company.getEname().equals(et_company.getText().toString().trim())) {
                            index = j;
                            break;
                        }
                    }


                    //comp_id_name = company_id_list_forIndex.get(index);
                    comp_id_name = List_Company_forIndex.get(index).getId();

                    if (new ConnectionDetector(UsageChargesUpdateActivity.this).isConnectingToInternet()) {
                        new get_company_job_id().execute(comp_id_name);
                    } else {
                        Toast.makeText(UsageChargesUpdateActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }


                }
            }
        });

        et_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_company.getText().length() == 0) {
                    et_job.setAdapter(null);
                    et_job.setText("");
                 //   et_company.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                    List_Company = new ArrayList<>(List_Company_forIndex);
                    CompanyNameAdapterNew companyNameAdapter = new CompanyNameAdapterNew(UsageChargesUpdateActivity.this, android.R.layout.simple_list_item_1, List_Company);
                    et_company.setAdapter(companyNameAdapter);
                    et_company.setDropDownHeight(550);

                } else {
                //    et_company.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                    if (List_Company.size() == 0) {
                        et_company.setAdapter(null);
                    }

                }
            }
        });
    }

    private void BindDataVendor() {
        VendorAdapter vendorAdapter = new VendorAdapter(UsageChargesUpdateActivity.this, android.R.layout.simple_list_item_1, List_Vendor);
        et_Vendor.setAdapter(vendorAdapter);
        et_Vendor.setDropDownHeight(550);

        et_Vendor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_Vendor.showDropDown();
                /*to clear autocomplete*/
               /* final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (et_Vendor.getRight() - et_Vendor.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            et_Vendor.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
                *//**/

                return false;
            }
        });




/*        et_Vendor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (et_Vendor.getText().equals("--Select--") || (et_Vendor.getText().length() == 0)) {

                } else {
                    //    pankajtester_chutya = 1;
                    //   String checc = company_name.getText().toString();
                    //   int index = company_Name_list_forIndex.indexOf(company_name.getText().toString());
                    //


                    int index = 0;
                    for (int j = 0; j < List_Vendor_forIndex.size(); j++) {
                        Vendor vendor = List_Vendor_forIndex.get(j);
                        if (vendor.getVenderName() != null && vendor.getVenderName().equals(et_Vendor.getText().toString().trim())) {
                            index = j;
                            break;
                        }
                    }


                    //comp_id_name = company_id_list_forIndex.get(index);
                  //  String vendor_id = List_Vendor_forIndex.get(index).getVenderID();


                }
            }
        });*/


    }

    public void Getcompany_name() {
       // String dealerId = sp.getString(Utility.DEALER_ID, "");
        String   dealerId = Shared_Preference.getDEALER_ID(this);

        List_Company.clear();
        //  count = 0;
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE +API_bindClientByDealer;
        final String METHOD_NAME =API_bindClientByDealer;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("dealerID", dealerId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);


            if (receivedString.contains("Job not found!")) {
                //     count = 1;
            } else {


                JSONObject jsonObject = new JSONObject(receivedString);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String comapny_id = jsonObject1.getString("id");
                    String company_name = jsonObject1.getString("Ename");
                    String company_logo = jsonObject1.getString("Imagepath");
                    List_Company.add(new Company(comapny_id, company_name, company_logo));
                }

                List_Company_forIndex = new ArrayList<>(List_Company);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetVendor() {
      //  String dealerId = sp.getString(Utility.DEALER_ID, "");
        String   dealerId = Shared_Preference.getDEALER_ID(this);
       // String role = sp.getString(Utility.LOGIN_USER_ROLE, "");
        //final String role = Shared_Preference.getUSER_ROLE(this);
        List_Vendor.clear();

        //  count = 0;
        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + Api.API_VENDOR;
        final String METHOD_NAME = Api.API_VENDOR;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("DealerID", dealerId);
        request.addProperty("Dealer_Type", "0");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);


            if (receivedString.contains("Job not found!")) {
                //     count = 1;
            } else {


                JSONObject jsonObject = new JSONObject(receivedString);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String VenderID = jsonObject1.getString("VenderID");
                    String VenderName = jsonObject1.getString("VenderName");
                    String IsCertified = jsonObject1.getString("IsCertified");
                    List_Vendor.add(new Vendor(VenderID, VenderName, IsCertified));
                }

                List_Vendor_forIndex = new ArrayList<>(List_Vendor);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BindJobData() {
        // et_job.setText("");

        //CompanyNameAdapter jobDescAdapter = new CompanyNameAdapter(MainActivity.this, android.R.layout.simple_list_item_1, job_Name_list_Desc);
        JobNameAdapterNew jobDescAdapter = new JobNameAdapterNew(UsageChargesUpdateActivity.this, android.R.layout.simple_list_item_1, List_Job);

        et_job.setAdapter(jobDescAdapter);
        et_job.setDropDownHeight(550);
        et_job.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_job.showDropDown();
                /*to clear autocomplete*/
               /* final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                try {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (et_job.getRight() - et_job.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            et_job.setText("");
                            return true;
                        }
                    }
                } catch (Exception e) {
                }*/
                /**/

                return false;
            }
        });
        et_job.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  aman_geneious = 12;
                // int index = job_Name_list.indexOf(job_name.getText().toString());
                // int index = i;
                ////
                String job_txt = et_job.getText().toString();
                job_txt = job_txt.substring(0, job_txt.indexOf("\n"));

                //  int index = job_Name_list.indexOf(job_txt);

                int index = 0;
                for (int j = 0; j < List_Job.size(); j++) {
                    Job_1 job = List_Job.get(j);
                    if (job.getJobName().equals(job_txt)) {
                        index = j;
                        break;
                    }
                }


                ///
                //main_jobid = job_Name_list.get(index);////EDIT IN THIS
                //    new_job_id = job_id_list.get(index);
                String main_jobid = List_Job.get(index).getJobName();
                // new_job_id=   List_Job.get(index).getJobId();

                // ed.putString(Utility.KEY_JOB_ID_FOR_JOBFILES, new_job_id).apply();


//                new_des = job_des_list.get(index);
//                main_status = status_list.get(index);////EDIT I

                //    new_des =  List_Job.get(index).getJobDesc();
                //   main_status = List_Job.get(index).getStatus();////EDIT I


///  THIS
                // new_jobtype = jobtype_list.get(index);
                // new_show = show_list.get(index);

                // new_jobtype = List_Job.get(index).getJobType();
                // new_show = List_Job.get(index).getShowName();


                //  et_job.setText("");
                et_job.setText(main_jobid);

            }
        });


        et_job.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (et_job.getText().length() == 0) {
                       // et_job.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                        if (et_company.getText().toString().length() == 0) {
                            et_job.setAdapter(null);
                        } else {


                            //refresh adapter
                            List_Job = new ArrayList<>(List_Job_forIndex);
                            JobNameAdapterNew jobDescAdapter = new JobNameAdapterNew(UsageChargesUpdateActivity.this, android.R.layout.simple_list_item_1, List_Job);
                            et_job.setAdapter(jobDescAdapter);
                            et_job.setDropDownHeight(550);

                        }


                    } else {
                       // et_job.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear, 0);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }


        });


    }

    public void Getcompany_job_id(String id) {
        List_Job.clear();
        List_Job_forIndex.clear();


        //  count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_BindJob;
        final String METHOD_NAME = API_BindJob;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("ClientID", id);
//        Log.d("BHANU--ID", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String receivedString = SoapPrimitiveresult.toString();
            Log.e("receivedString", receivedString);


            if (receivedString.contains("No Data Available.")) {
                //      count = 1;
            } else {


                JSONObject jsonObject = new JSONObject(receivedString);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String job_id = jsonObject1.getString("JOB_ID_PK");
                    String job_name = jsonObject1.getString("JobName");
                    String job_descripition = jsonObject1.getString("txt_Job");


                    String status = jsonObject1.getString("Status");
                    String showName = jsonObject1.getString("ShowName");
                    String jobtype = jsonObject1.getString("JOB_TYPE");

////
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
                    total_desc = job_name + System.getProperty("line.separator") + desc.trim();
                    /////


                    List_Job.add(new Job_1(job_id, job_descripition, showName, job_name, jobtype, status, total_desc));

                }

                //  job_Name_list_Desc_forIndex = new ArrayList<>(job_Name_list_Desc);
                List_Job_forIndex = new ArrayList<>(List_Job);


                //   job_id_list.add(0, "--Select--");
                //   job_Name_list.add(0, "--Select--");
                //   job_des_list.add(0,"--Select--");

                //    status_list.add(0,"--Select--");
                //    show_list.add(0,"--Select--");
                //   jobtype_list.add(0,"--Select--");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Dialog_No_JobsAvailable() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UsageChargesUpdateActivity.this);
        LayoutInflater inflater = LayoutInflater.from(UsageChargesUpdateActivity.this);
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
        title.setText("No jobs available!");
        message.setText("Kindly choose another Company!");
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
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.getCause();
        }


    }

    public String SaveUsageReport() {


      //  String dealer_id = Utility.getDealerID(UsageChargesUpdateActivity.this);
   //     String user_id = Utility.getUserID(UsageChargesUpdateActivity.this);
        String user_id =  Shared_Preference.getLOGIN_USER_ID(this);
       // String Name = sp.getString("tname", "");
        String Name = Shared_Preference.getLOGIN_USERNAME(UsageChargesUpdateActivity.this);
        //String LoginUsername = sp.getString(Utility.LOGIN_USERNAME, "");
        String LoginUsername =Shared_Preference.getLOGIN_ID(this);

        if (IsAdd == null) IsAdd = "1";
        if (UsageChargeID == null) UsageChargeID = "0";
        String receivedString = "";

        final String NAMESPACE = KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String METHOD_NAME = Api.API_SAVE_USAGE_REPORT;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("jobid", jobID);
        request.addProperty("compid", compID);
        request.addProperty("vendorid", vendor_id);
        request.addProperty("desc", description);
        request.addProperty("cost", cost);
        request.addProperty("quantity", quantity);
        request.addProperty("total", total);
        request.addProperty("LoginUserID", LoginUsername);
        request.addProperty("empID", user_id);
        request.addProperty("UserName", Name);
        request.addProperty("IsAdd", IsAdd);
        request.addProperty("UsageChargeID", UsageChargeID);


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


        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedString;
    }

    private class get_company_name extends AsyncTask<Void, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(UsageChargesUpdateActivity.this);

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
        protected Void doInBackground(Void... voids) {
            //    Send();
            Getcompany_name();
            GetVendor();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                ringProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }

            if (List_Company != null && List_Company.size() > 0) {
                //if (company_id_list != null && company_id_list.size() > 0) {
                BindDataCompany();
                BindDataVendor();
            } else {
                Toast.makeText(UsageChargesUpdateActivity.this, "No data found!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class get_company_job_id extends AsyncTask<String, Void, Void> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(UsageChargesUpdateActivity.this);

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

            try {
                ringProgressDialog.show();
            } catch (NullPointerException e) {
                e.getMessage();
            }


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                ringProgressDialog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }


            if (List_Job.size() < 1) {
                //  if (count == 1) {
                et_company.setText("");
                et_job.setText("");
                Dialog_No_JobsAvailable();
            } else {
                BindJobData();
            }

        }
    }

    private class async_save_usage_report extends AsyncTask<Void, Void, String> {

        final ProgressDialog ringProgressDialog = new ProgressDialog(UsageChargesUpdateActivity.this);

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

            String result = SaveUsageReport();
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
            String status = "0";
            try {
                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status");
                String msg = jsonObject.getString("Message");
            } catch (Exception e) {
                e.getMessage();
            }
            if (status.equals("1")) {
                Toast.makeText(getApplicationContext(), "Saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


