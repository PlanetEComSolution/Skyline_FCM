package planet.info.skyline.client;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_GetClientUserInfo;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class MyAccountActivity extends AppCompatActivity {
    ProgressDialog progressDoalog;
    SharedPreferences sp;


    String CompName;
    String UserName;
    String txt_Phone;
    String txt_Mobile;
    String txt_Mail;
    String txt_ext;
    String Country;
    String State;
    String City;

    String CAType;
    String Imagepath;
    String Billing_Contact;
    String address;
    String Billing_Zipcode;
    String Billing_phone;
    String Billing_Fax;
    String CompanyCode;

    String DC_name;
    String p_name;
    String A_name;
    String CompanyCode1;
    String MasterUser;
    String CaType1;
    String MasterUserName;

    TextView tv_Client, tv_AMCoordinator, tv_Name, tv_consultant, tv_Phone, tv_email;
    TextView tv_Name1, tv_Addrsss, tv_Phone_1,tv_pm,
            tv_City, tv_state, tv_country, tv_zip, tv_fax, tv_email_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        setTitle(Utility.getTitle("My Account"));



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        callApiMyAccountDetails();
        setIds();

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
    private void callApiMyAccountDetails() {


        if (new ConnectionDetector(MyAccountActivity.this).isConnectingToInternet()) {
            new async_getAccountDetails().execute();

        } else {
            Toast.makeText(MyAccountActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }


    }

    private void setIds() {

        tv_Client = findViewById(R.id.tv_Client);

        tv_pm = findViewById(R.id.tv_pm);

        tv_AMCoordinator = findViewById(R.id.tv_AMCoordinator);
        tv_Name = findViewById(R.id.tv_Name);
        tv_consultant = findViewById(R.id.tv_consultant);
        tv_Phone = findViewById(R.id.tv_Phone);
        tv_email = findViewById(R.id.tv_email);
        tv_Name1 = findViewById(R.id.tv_Name1);
        tv_Addrsss = findViewById(R.id.tv_Addrsss);
        tv_Phone_1 = findViewById(R.id.tv_Phone_1);
        tv_City = findViewById(R.id.tv_City);
        tv_state = findViewById(R.id.tv_state);
        tv_country = findViewById(R.id.tv_country);
        tv_zip = findViewById(R.id.tv_zip);
        tv_fax = findViewById(R.id.tv_fax);
        tv_email_1 = findViewById(R.id.tv_email_1);


    }

    public void fun_client_account_details() {

        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
        String Client_id = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        final String NAMESPACE = SOAP_API_Client. KEY_NAMESPACE;
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetClientUserInfo;
        final String METHOD_NAME = API_GetClientUserInfo;
        // Create SOAP request

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("ID", Client_id);

        //request.addProperty("Connection", "Close");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        httpTransport.debug = true;
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            //nks
            Log.d("HTTP REQUEST ", httpTransport.requestDump);
            Log.d("HTTP RESPONSE", httpTransport.responseDump);
            Object results = (Object) envelope.getResponse();

            String resultstring = results.toString();
            //{"cds":[{"Snoozed":"0","Approved":"0","YTBReviewed":"0","Rejected":"0"}]}
            JSONObject jsonObject = new JSONObject(resultstring);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);


            CompName = jsonObject1.getString("CompName");
            UserName = jsonObject1.getString("UserName");
            txt_Phone = jsonObject1.getString("txt_Phone");
            txt_Mobile = jsonObject1.getString("txt_Mobile");
            txt_Mail = jsonObject1.getString("txt_Mail");
            txt_ext = jsonObject1.getString("txt_ext");
            Country = jsonObject1.getString("Country");
            State = jsonObject1.getString("State");
            City = jsonObject1.getString("City");

            CAType = jsonObject1.getString("CAType");
            Imagepath = jsonObject1.getString("Imagepath");
            Billing_Contact = jsonObject1.getString("Billing_Contact");
            address = jsonObject1.getString("address");
            Billing_Zipcode = jsonObject1.getString("Billing_Zipcode");
            Billing_phone = jsonObject1.getString("Billing_phone");
            Billing_Fax = jsonObject1.getString("Billing_Fax");
            CompanyCode = jsonObject1.getString("CompanyCode");

            DC_name = jsonObject1.getString("DC_name");
            p_name = jsonObject1.getString("p_name");
            A_name = jsonObject1.getString("A_name");
            CompanyCode1 = jsonObject1.getString("CompanyCode1");
            MasterUser = jsonObject1.getString("MasterUser");
            CaType1 = jsonObject1.getString("CaType1");
            MasterUserName = jsonObject1.getString("MasterUserName");


        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(MyAccountActivity.this,"Some error occurred!",Toast.LENGTH_SHORT).show();
        }


    }

    private void setData() {


        if (CompName != null && !CompName.equals("")) {
            tv_Client.setText(CompName);
        } else {
            tv_Client.setText("N/A");
        }
        if (UserName != null && !UserName.equals("")) {
            tv_Name.setText(UserName);

        } else {
            tv_Name.setText("N/A");

        }
        if (txt_Phone != null && !txt_Phone.equals("")) {
            tv_Phone.setText(txt_Phone);
        } else {
            tv_Phone.setText("N/A");
        }
        if (txt_Mobile != null && !txt_Mobile.equals("")) {

        } else {

        }
        if (txt_Mail != null && !txt_Mail.equals("")) {
            tv_email.setText(txt_Mail);
            tv_email_1.setText(txt_Mail);
        } else {
            tv_email.setText("N/A");
            tv_email_1.setText("N/A");
        }
        if (txt_ext != null && !txt_ext.equals("")) {

        } else {

        }
        if (Country != null && !Country.equals("")) {
            tv_country.setText(Country);
        } else {
            tv_country.setText("N/A");
        }
        if (State != null && !State.equals("")) {
            tv_state.setText(State);
        } else {
            tv_state.setText("N/A");
        }
        if (City != null && !City.equals("")) {
            tv_City.setText(City);
        } else {
            tv_City.setText("N/A");
        }
        if (CAType != null && !CAType.equals("")) {

        } else {

        }
        if (Imagepath != null && !Imagepath.equals("")) {

        } else {

        }
        if (address != null && !address.equals("")) {
            tv_Addrsss.setText(address);
        } else {
            tv_Addrsss.setText("N/A");
        }
        if (Billing_Zipcode != null && !Billing_Zipcode.equals("")) {
            tv_zip.setText(Billing_Zipcode);
        } else {
            tv_zip.setText("N/A");
        }
        if (Billing_phone != null && !Billing_phone.equals("")) {
            tv_Phone_1.setText(Billing_phone);
        } else {
            tv_Phone_1.setText("N/A");
        }
        if (Billing_Fax != null && !Billing_Fax.equals("")) {
            tv_fax.setText(Billing_Fax);
        } else {
            tv_fax.setText("N/A");
        }
        if (CompanyCode != null && !CompanyCode.equals("")) {

        } else {

        }

        if (DC_name != null && !DC_name.equals("")) {
            tv_consultant.setText(DC_name);
        } else {
            tv_consultant.setText("N/A");
        }
        if (p_name != null && !p_name.equals("")) {
            tv_pm.setText(p_name);
        } else {
            tv_pm.setText("N/A");
        }
        if (A_name != null && !A_name.equals("")) {
            tv_AMCoordinator.setText(A_name);
        } else {
            tv_AMCoordinator.setText("N/A");
        }
        if (CompanyCode1 != null && !CompanyCode1.equals("")) {

        } else {

        }
        if (MasterUser != null && !MasterUser.equals("")) {

        } else {

        }
        if (CaType1 != null && !CaType1.equals("")) {

        } else {

        }
        if (MasterUserName != null && !MasterUserName.equals("")) {

        } else {

        }

        if (Billing_Contact != null && !Billing_Contact.equals("")) {
            tv_Name1.setText(Billing_Contact);
        } else {
            tv_Name1.setText("");
        }
    }

    private class async_getAccountDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {

            progressDoalog = new ProgressDialog(MyAccountActivity.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                progressDoalog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }
            setData();

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            fun_client_account_details();
            return null;
        }

    }

}
