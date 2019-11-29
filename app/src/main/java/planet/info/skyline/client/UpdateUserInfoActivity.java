package planet.info.skyline.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import planet.info.skyline.model.City;
import planet.info.skyline.model.Country;
import planet.info.skyline.model.State;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_BindCityByStateID;
import static planet.info.skyline.network.Api.API_BindCountry;
import static planet.info.skyline.network.Api.API_BindStateByCountryID;
import static planet.info.skyline.network.Api.API_GetClientUserInfo;
import static planet.info.skyline.network.Api.API_UpdateClientUser;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class UpdateUserInfoActivity extends AppCompatActivity {

    Context context;
    TextView tv_msg;

    SharedPreferences sp;
    String UserID = "";

    TextInputEditText et_Fax, et_PinCode;
    Spinner et_city, et_state, et_Country;
    TextInputEditText et_Address_2, et_Address_1, et_Mobile, et_Phone, et_Lname, et_Fname;


    String Fax = "", PinCode = "",
            city = "", state = "", Country = "",
            Address_2 = "", Address_1 = "", Mobile = "", Phone = "", Lname = "", Fname = "";

    ArrayList<Country> CountryList = new ArrayList<>();
    ArrayList<State> StateList = new ArrayList<>();
    ArrayList<City> CityList = new ArrayList<>();
    String CountryId;
    String StateId;
    Button btn_update;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        context = UpdateUserInfoActivity.this;
        setTitle(Utility.getTitle("Update User Info"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

        try {
            UserID = getIntent().getExtras().getString("UserID");
        } catch (Exception e) {
        }

        et_Fax = findViewById(R.id.et_Fax);
        et_PinCode = findViewById(R.id.et_PinCode);
        et_city = findViewById(R.id.spnr_city);
        et_state = findViewById(R.id.spnr_state);
        et_Country = findViewById(R.id.spnr_country);
        et_Address_2 = findViewById(R.id.et_Address_2);
        et_Address_1 = findViewById(R.id.et_Address_1);
        et_Mobile = findViewById(R.id.et_Mobile);
        et_Phone = findViewById(R.id.et_Phone);
        et_Lname = findViewById(R.id.et_Lname);
        et_Fname = findViewById(R.id.et_Fname);
        btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUserDetail();
            }
        });


        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new Async_ClientUserInfo().execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }

    }

    public void getClientUserInfo() {


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetClientUserInfo;
        final String METHOD_NAME = API_GetClientUserInfo;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("ID", UserID);

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
            try {
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String id = jsonObject1.getString("id");
                String compid = jsonObject1.getString("compid");
                String CompName = jsonObject1.getString("CompName");
                Fname = jsonObject1.getString("txt_name");
                Lname = jsonObject1.getString("txt_L_name");
                Phone = jsonObject1.getString("txt_Phone");
                Mobile = jsonObject1.getString("txt_Mobile");
                String txt_Mail = jsonObject1.getString("txt_Mail");
                String txt_ext = jsonObject1.getString("txt_ext");
                Country = jsonObject1.getString("Country");
                state = jsonObject1.getString("State");
                city = jsonObject1.getString("City");
                String CAType = jsonObject1.getString("CAType");
                String Imagepath = jsonObject1.getString("Imagepath");
                String Billing_Contact = jsonObject1.getString("Billing_Contact");
                Address_1 = jsonObject1.getString("address");
                Address_2 = jsonObject1.getString("address2");
                // PinCode = jsonObject1.getString("Billing_Zipcode");
                PinCode = jsonObject1.getString("txt_Postal_Code");

                String Billing_phone = jsonObject1.getString("Billing_phone");
                //Fax = jsonObject1.getString("Billing_Fax");

                Fax = jsonObject1.getString("txt_fax");


                String CompanyCode = jsonObject1.getString("CompanyCode");
                String DC_name = jsonObject1.getString("DC_name");
                String p_name = jsonObject1.getString("p_name");
                String A_name = jsonObject1.getString("A_name");
                String CompanyCode1 = jsonObject1.getString("CompanyCode1");
                String MasterUser = jsonObject1.getString("MasterUser");
                String CaType1 = jsonObject1.getString("CaType1");
                String MasterUserName = jsonObject1.getString("MasterUserName");


            } catch (Exception e) {
                e.getMessage();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getCountryId() {
        CountryList.clear();

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_BindCountry;
        final String METHOD_NAME = API_BindCountry;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


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
                    String Country_Id_Pk = jsonObject1.getString("Country_Id_Pk");
                    String Country_Name = jsonObject1.getString("Country_Name");
                    CountryList.add(new Country(Country_Id_Pk, Country_Name));

                } catch (Exception e) {
                    e.getMessage();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getStateByCountryId() {

        StateList.clear();
        StateList.add(new State("-1", "-Select State-"));

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_BindStateByCountryID;
        final String METHOD_NAME =API_BindStateByCountryID;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Str_country", CountryId);

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

                    String State_Id_Pk = jsonObject1.getString("State_Id_Pk");
                    String State_Name = jsonObject1.getString("State_Name");
                    StateList.add(new State(State_Id_Pk, State_Name));

                } catch (Exception e) {
                    e.getMessage();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getCityByStateId() {
        CityList.clear();
        CityList.add(new City("-1", "", "-Select City-", ""));


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_BindCityByStateID;
        final String METHOD_NAME = API_BindCityByStateID;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Str_State", StateId);

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

                    String City_Id_Pk = jsonObject1.getString("City_Id_Pk");
                    String State_Id = jsonObject1.getString("State_Id");
                    String City_Name = jsonObject1.getString("City_Name");
                    String IsDelete = jsonObject1.getString("IsDelete");
                    // String ExtraID = jsonObject1.getString("ExtraID");

                    CityList.add(new City(City_Id_Pk, State_Id, City_Name, IsDelete));

                } catch (Exception e) {
                    e.getMessage();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void UpdateUserDetail() {
      int position_city_selected=  et_city .getSelectedItemPosition();
        int position_state_selected=    et_state .getSelectedItemPosition();




        if (et_Fname.getText().toString().trim().length() == 0) {
            Toast.makeText(UpdateUserInfoActivity.this, "Please enter first name", Toast.LENGTH_SHORT).show();
        } else if (et_Lname.getText().toString().trim().length() == 0) {
            Toast.makeText(UpdateUserInfoActivity.this, "Please enter last name", Toast.LENGTH_SHORT).show();
        } else if (et_Phone.getText().toString().trim().length() == 0) {
            Toast.makeText(UpdateUserInfoActivity.this, "Please enter phone no.", Toast.LENGTH_SHORT).show();
//        }else if (et_Mobile.getText().toString().trim().length() == 0) {
//            Toast.makeText(UpdateUserInfoActivity.this, "Please enter mobile no.", Toast.LENGTH_SHORT).show();
        } else if (et_Address_1.getText().toString().trim().length() == 0) {
            Toast.makeText(UpdateUserInfoActivity.this, "Please enter address 1", Toast.LENGTH_SHORT).show();
//        }else if (et_Address_2.getText().toString().trim().length() == 0) {
//            Toast.makeText(UpdateUserInfoActivity.this, "Please enter address 2", Toast.LENGTH_SHORT).show();
        } else if (et_PinCode.getText().toString().trim().length() == 0) {
            Toast.makeText(UpdateUserInfoActivity.this, "Please enter pin code", Toast.LENGTH_SHORT).show();
//        }else if (et_Fax.getText().toString().trim().length() == 0) {
//            Toast.makeText(UpdateUserInfoActivity.this, "Please enter fax no.", Toast.LENGTH_SHORT).show();
        }

        else if (position_state_selected == 0) {
            Toast.makeText(UpdateUserInfoActivity.this, "Please select State!", Toast.LENGTH_SHORT).show();
        }
        else if (position_city_selected == 0) {
            Toast.makeText(UpdateUserInfoActivity.this, "Please select City!", Toast.LENGTH_SHORT).show();
        }

        else {
            if (new ConnectionDetector(context).isConnectingToInternet()) {
                new Async_UpdateClientUserInfo().execute();
            } else {
                Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void UpdateUserInfo() {

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_UpdateClientUser;
        final String METHOD_NAME = API_UpdateClientUser;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Fname", et_Fname.getText().toString().trim());
        request.addProperty("Lname", et_Lname.getText().toString().trim());
        request.addProperty("Phone", et_Phone.getText().toString().trim());
        request.addProperty("Mobile", et_Mobile.getText().toString().trim());
        request.addProperty("Address1", et_Address_1.getText().toString().trim());
        request.addProperty("Address2", et_Address_2.getText().toString().trim());
        request.addProperty("Country", ((Country) et_Country.getSelectedItem()).getCountry_Id_Pk());
        request.addProperty("state", ((State) et_state.getSelectedItem()).getState_Id_Pk());
        request.addProperty("city", ((City) et_city.getSelectedItem()).getCity_Id_Pk());
        request.addProperty("pinCode", et_PinCode.getText().toString().trim());
        request.addProperty("ID", UserID);
        request.addProperty("fax", et_Fax.getText().toString().trim());


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();
            JSONObject jsonObject = new JSONObject(result);
            String r = jsonObject.getString("cds");

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

    private void showData() {
        et_Fax.setText(Fax);
        et_PinCode.setText(PinCode);
        // et_city.setText(city);
        // et_state.setText(state);
        // et_Country.setText(Country);
        et_Address_2.setText(Address_2);
        et_Address_1.setText(Address_1);
        et_Mobile.setText(Mobile);
        et_Phone.setText(Phone);
        et_Lname.setText(Lname);
        et_Fname.setText(Fname);


        et_Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) et_Country.getSelectedItem();
                CountryId = country.getCountry_Id_Pk();
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                    new Async_State().execute();
                } else {
                    Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        et_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                State State = (State) et_state.getSelectedItem();
                StateId = State.getState_Id_Pk();
                //  if (!StateId.equals("-1")) {
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                    new Async_City().execute();
                } else {
                    Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                //  }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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

    private class Async_ClientUserInfo extends AsyncTask<Void, Void, Void> {

        //  ProgressDialog progressDoalog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog(UpdateUserInfoActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            getClientUserInfo();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  progressDoalog.dismiss();
            if (new ConnectionDetector(context).isConnectingToInternet()) {
                new Async_Country().execute();
            } else {
                Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
            }

            //  hideProgressDialog();
            showData();


        }
    }

    private class Async_UpdateClientUserInfo extends AsyncTask<Void, Void, Void> {

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

            UpdateUserInfo();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();
            Toast.makeText(UpdateUserInfoActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class Async_Country extends AsyncTask<Void, Void, Void> {

        //  ProgressDialog progressDoalog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

         /*   progressDoalog = new ProgressDialog(context);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();*/
            showProgressDialog(UpdateUserInfoActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            getCountryId();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  progressDoalog.dismiss();

            ArrayAdapter<ArrayList<Country>> aa = new ArrayAdapter(UpdateUserInfoActivity.this, android.R.layout.simple_spinner_item, CountryList);
            aa.setDropDownViewResource(R.layout.spinner_item);
            et_Country.setAdapter(aa);

            for (int i = 0; i < CountryList.size(); i++) {
                String countryname = CountryList.get(i).getCountry_Name();
                if (countryname.equalsIgnoreCase(Country)) {
                    et_Country.setSelection(i);
                    break;
                }
            }

        }
    }

    private class Async_State extends AsyncTask<Void, Void, Void> {

        //  ProgressDialog progressDoalog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*
            progressDoalog = new ProgressDialog(context);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();*/
        }

        @Override
        protected Void doInBackground(Void... params) {

            getStateByCountryId();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  progressDoalog.dismiss();

            ArrayAdapter<ArrayList<State>> aa = new ArrayAdapter(UpdateUserInfoActivity.this, android.R.layout.simple_spinner_item, StateList);
            aa.setDropDownViewResource(R.layout.spinner_item);
            et_state.setAdapter(aa);

            if (StateList.size() > 0) {
                for (int i = 0; i < StateList.size(); i++) {
                    String State_Name = StateList.get(i).getState_Name();
                    if (State_Name.equalsIgnoreCase(state)) {
                        et_state.setSelection(i);
                        break;
                    }
                }
            } else {
//                ArrayAdapter<ArrayList<Country>> aa = new ArrayAdapter(UpdateUserInfoActivity.this, android.R.layout.simple_spinner_item, CityList);
//                aa.setDropDownViewResource(R.layout.spinner_item);
                et_city.setAdapter(null);
            }

        }
    }

    private class Async_City extends AsyncTask<Void, Void, Void> {

        //  ProgressDialog progressDoalog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

         /* progressDoalog = new ProgressDialog(context);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();*/


        }

        @Override
        protected Void doInBackground(Void... params) {

            getCityByStateId();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //      progressDoalog.dismiss();

            hideProgressDialog();
            ArrayAdapter<ArrayList<Country>> aa = new ArrayAdapter(UpdateUserInfoActivity.this, android.R.layout.simple_spinner_item, CityList);
            aa.setDropDownViewResource(R.layout.spinner_item);
            et_city.setAdapter(aa);

            for (int i = 0; i < CityList.size(); i++) {
                String City_Name = CityList.get(i).getCity_Name();
                if (City_Name.equalsIgnoreCase(city)) {
                    et_city.setSelection(i);
                    break;
                }
            }

        }
    }

}