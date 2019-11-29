package planet.info.skyline.tech.job_files_new;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.home.MainActivity;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.ClientUser;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_GetClientUserList;
import static planet.info.skyline.network.Api.API_ShareProjectFile;
import static planet.info.skyline.network.Api.API_mail;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class SharePhotosToClientActivity extends AppCompatActivity {
    ListView listview_Clients;
    TextView tv_msg;
    Context context;
    ArrayList<ClientUser> list_ClientUser;
    String CompanyId = "";
  /*  SharedPreferences sp;
    SharedPreferences.Editor ed;*/
    List<HashMap<String, String>> list_Selected_Project_Photos = new ArrayList<>();
    List<ClientUser> list_all_Client = new ArrayList<>();
    List<ClientUser> list_selected_Client = new ArrayList<>();
    Button Btn_Submit;

    String comment = "";
    boolean api_error = false;
    String isFromProjectFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_photos_to_client);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Utility.getTitle("Select Client(s)"));
        listview_Clients = (ListView) findViewById(R.id.cart_listview);

        tv_msg = (TextView) findViewById(R.id.tv_msg);
        context = SharePhotosToClientActivity.this;
        Btn_Submit = (Button) findViewById(R.id.Btn_Submit);
        Btn_Submit.setVisibility(View.GONE);
        try {
            Intent intent = getIntent();
            Bundle args = intent.getBundleExtra("BUNDLE");
            list_Selected_Project_Photos = (List<HashMap<String, String>>) args.getSerializable("ARRAYLIST");
        } catch (Exception e) {
            e.getCause();
        }
        try {
            Intent intent = getIntent();
            if (intent.hasExtra(Utility.isFrom_PROJECT_FILE)) {
                isFromProjectFile = intent.getExtras().getString(Utility.isFrom_PROJECT_FILE);
            }

        } catch (Exception e) {
            e.getCause();
        }


       /* sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
        ed = sp.edit();*/

        //   Toast.makeText(getApplicationContext(), sp.getString(Utility.KEY_JOB_ID_FOR_JOBFILES,""), Toast.LENGTH_SHORT).show();
        // Toast.makeText(getApplicationContext(), list_Selected_Project_Photos.toString(),Toast.LENGTH_SHORT).show();

      //  CompanyId = sp.getString(Utility.COMPANY_ID_BILLABLE, "");
        CompanyId =   Shared_Preference.getCOMPANY_ID_BILLABLE(this);
        if (CompanyId.equals("")) {
            Toast.makeText(context, "Company id not found!", Toast.LENGTH_SHORT).show();
        } else {

            if (new ConnectionDetector(context).isConnectingToInternet()) {
                new Async_ClientUserList().execute();
            } else {
                Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
            }
        }

        Btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.showChatHead(getApplicationContext());
    }

    protected boolean ValidateEmail() {

        boolean isValid = true;
        for (ClientUser selected_Client : list_selected_Client) {
            String selected_mail = selected_Client.getTxt_Mail();
            String selected_Id_Pk = selected_Client.getId_Pk();

            if (!isValidEmail(selected_mail)) {
                isValid = false;

                for (int i = 0; i < list_all_Client.size(); i++) {
                    ClientUser clientUser = list_all_Client.get(i);

                    String Id_Pk = clientUser.getId_Pk();
                    if (Id_Pk.equals(selected_Id_Pk)) {
                        //  listview_Clients.smoothScrollToPosition(i);
                        Toast.makeText(getApplicationContext(), "Wrong email address!", Toast.LENGTH_SHORT).show();

                        //  View v=   getViewByPosition(i,listview_Clients);
                        //  EditText et = (EditText) v.findViewById(R.id.mail_Id);
                        //  et.setError("not valid");

                        listview_Clients.setSelection(i);
                        listview_Clients.requestFocus();
                        break;
                    }
                }

            }


        }
        return isValid;
    }

    public void getClientUserList() {
        list_ClientUser = new ArrayList<>();
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetClientUserList;
        final String METHOD_NAME = API_GetClientUserList;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("id", CompanyId);
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

                    String ename = jsonObject1.getString("ename");
                    String txt_Mail = jsonObject1.getString("txt_Mail");
                    String Id_Pk = jsonObject1.getString("Id_Pk");
                    String int_client_id = jsonObject1.getString("int_client_id");
                    String MasterStatus = jsonObject1.getString("MasterStatus");




                    list_ClientUser.add(new ClientUser(ename, txt_Mail, Id_Pk, int_client_id, false,MasterStatus));
                } catch (Exception e) {
                    e.getMessage();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void ShareProjectFile(String LID, String UID, String Mail, String CID, String JID, String FID, String comment) {
        list_ClientUser = new ArrayList<>();

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_ShareProjectFile;
        final String METHOD_NAME = API_ShareProjectFile;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("LID", LID);
        request.addProperty("UID", UID);
        request.addProperty("Mail", Mail);
        request.addProperty("CID", CID);
        request.addProperty("JID", JID);
        request.addProperty("FID", FID);
        request.addProperty("comment", comment);

        Log.e("Api called", request.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();

            Log.e("Api result", result);

            if (result.equalsIgnoreCase("fail")) {
                api_error = true;
            } else {
                api_error = false;
            }


        } catch (Exception e) {
            e.printStackTrace();
            api_error = true;
            Log.e("Api result err", String.valueOf(e.getMessage()));
        }

    }

    public void Dialog_EnterComment() {
        final Dialog dialog_comment = new Dialog(SharePhotosToClientActivity.this);
        dialog_comment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_comment.setContentView(R.layout.enter_comment);

        dialog_comment.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_comment.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog_comment.setCancelable(false);
        if (!dialog_comment.isShowing()) {
            dialog_comment.show();
        }

        final EditText et_comment = (EditText) dialog_comment.findViewById(R.id.texrtdesc);
        Button Btn_Done = (Button) dialog_comment.findViewById(R.id.Btn_Done);
        Button Btn_Cancel = (Button) dialog_comment.findViewById(R.id.Btn_Cancel);

        ImageView close = (ImageView) dialog_comment.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_comment.dismiss();

            }
        });


        Btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                comment = et_comment.getText().toString().trim();
                if (comment.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please enter comment!", Toast.LENGTH_SHORT).show();
                } else {
                    //   Utility.hideKeyboard(SharePhotosToClientActivity.this);

                    //   dialog_comment.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    dialog_comment.dismiss();
                    if (new ConnectionDetector(context).isConnectingToInternet()) {
                        new Async_ShareProjectFile().execute();
                    } else {
                        Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog_comment.dismiss();
            }
        });

    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      /*  if (isFromProjectFile.equals("true")) {
            finish();
        }*/

     //   startActivity(new Intent(SharePhotosToClientActivity.this, MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.menu_done:

                sendMail();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sharefiles, menu);
        // _menu = menu;

        return true;
    }

    void sendMail() {
        String selectedMail="";
        Utility.hideKeyboard(SharePhotosToClientActivity.this);
        list_selected_Client.clear();
        api_error = false;
        for (int i = 0; i < list_all_Client.size(); i++) {
            if (list_all_Client.get(i).isChecked()) {
                list_selected_Client.add(list_all_Client.get(i));
               selectedMail= selectedMail+list_all_Client.get(i).getTxt_Mail()+",";
            }
        }

        if (list_selected_Client == null || list_selected_Client.size() < 1) {
            Toast.makeText(getApplicationContext(), "No user selected!",
                    Toast.LENGTH_SHORT).show();
        } else {
            selectedMail = selectedMail.substring(0, selectedMail.length() - 1);
            // Dialog_EnterComment();
            Boolean isValid = ValidateEmail();
            if (isValid) {
                if (isFromProjectFile.equals("true")) {

                    Intent intent=new Intent(SharePhotosToClientActivity.this, ProjectFileDetailActivityNonClient.class);
                    intent.putExtra(Utility.Client_Mail,selectedMail);
                    setResult(Utility.PROJECT_FILE_GET_CLIENT_MAILS,intent);
                    finish();//finishing activity

                } else {
                    Dialog_EnterComment();
                }
            }


        }
    }

    public void Mail(String LID, String UID, String Mail, String CID, String JID, String FID) {
        list_ClientUser = new ArrayList<>();

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_mail;
        final String METHOD_NAME = API_mail;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("LID", LID);
        request.addProperty("UID", UID);
        request.addProperty("Mail", Mail);
        request.addProperty("CID", CID);
        request.addProperty("JID", JID);
        request.addProperty("FID", FID);


        Log.e("Api called", request.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();

            Log.e("Api result", result);

            if (result.equalsIgnoreCase("fail")) {
                api_error = true;
            } else {
                api_error = false;
            }


        } catch (Exception e) {
            e.printStackTrace();
            api_error = true;
            Log.e("Api result err", String.valueOf(e.getMessage()));
        }

    }

    class Async_ClientUserList extends AsyncTask<Void, Void, Void> {

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


            getClientUserList();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();

            order_adapter adapter = null;


            if (list_ClientUser.size() < 1) {
                tv_msg.setVisibility(View.VISIBLE);
            } else {
                tv_msg.setVisibility(View.GONE);
            }
            adapter = new order_adapter(context, list_ClientUser);
            listview_Clients.setAdapter(adapter);


        }
    }

    public class order_adapter extends BaseAdapter {
        List<ClientUser> beanArrayList;
        Context context;
        int count = 1;

        public order_adapter(Context context, List<ClientUser> beanArrayList) {
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
            final String Ename = beanArrayList.get(i).getEname();
            final String Id_Pk = beanArrayList.get(i).getId_Pk();
            final String client_id = beanArrayList.get(i).getInt_client_id();
            final String Txt_Mail = beanArrayList.get(i).getTxt_Mail();
            final String getMasterStatus = beanArrayList.get(i).getMasterStatus();
            final boolean isChecked = beanArrayList.get(i).isChecked();

            if (convertview == null) {
                holder = new Holder();

                convertview = LayoutInflater.from(context).inflate(R.layout.row_clients_new, null);
                holder.index_no = (Button) convertview.findViewById(R.id.serial_no);
                holder.ename = (TextView) convertview.findViewById(R.id.ename);
                holder.mail_Id = (EditText) convertview.findViewById(R.id.mail_Id);
                holder.checkBox = (CheckBox) convertview.findViewById(R.id.checkBox);
                // holder.imgvw_ok = (ImageView) convertview.findViewById(R.id.imgvw_ok);
                holder.userType = (TextView) convertview.findViewById(R.id.userType);

                convertview.setTag(holder);
            } else {
                holder = (Holder) convertview.getTag();
            }
            holder.index_no.setText(String.valueOf(i + 1));
/*
* 1--> Client
2-->Master User
3-->Master User
4-->Normal User

*
* */

            if (getMasterStatus.equalsIgnoreCase("1")) {
                holder.userType.setText("Client");
                holder.userType.setTextColor(getResources().getColor(R.color.main_green_color));

            }else if (getMasterStatus.equalsIgnoreCase("2") || getMasterStatus.equalsIgnoreCase("3")) {
                holder.userType.setText("Master User");
                holder.userType.setTextColor(getResources().getColor(R.color.primaryTextColor));
            }
            else if (getMasterStatus.equalsIgnoreCase("4") ) {
                holder.userType.setText("Normal User");
                holder.userType.setTextColor(getResources().getColor(R.color.primaryTextColor));
            }



            if (Ename.equals(null) || Ename.trim().equals("")) {
                holder.ename.setText("Not available");
            } else {
                holder.ename.setText(Ename);


            }

            holder.mail_Id.setTag(i);
            if (Txt_Mail.equals(null) || Txt_Mail.trim().equals("")) {
                holder.mail_Id.setText("Not available");
            } else {
                holder.mail_Id.setText(Txt_Mail);
            }

            holder.mail_Id.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    //  if(isValidEmail(s.toString().trim())){
                    int position = Integer.valueOf(holder.mail_Id.getTag().toString());
                    ClientUser clientUser = beanArrayList.get(position);
                    clientUser.setTxt_Mail(s.toString().trim());

                    beanArrayList.set(position, clientUser);
                    list_all_Client = beanArrayList;

                }
            });


            holder.checkBox.setTag(i);
            if (isChecked) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag(); // Here

                    ClientUser clientUser = beanArrayList.get(getPosition);
                    if (isChecked) {
                        clientUser.setChecked(true);
                    /*
                      boolean isValidEmail=  isValidEmail(clientUser.getTxt_Mail());
                        if(isValidEmail) {
                            clientUser.setChecked(true);
                            holder.checkBox.setChecked(true);
                        }
                        else{
                            holder.checkBox.setChecked(false);
                            clientUser.setChecked(false);
                            holder.mail_Id.setError("wrong");
                        }*/
                    } else {
                        clientUser.setChecked(false);
                    }

                    beanArrayList.set(getPosition, clientUser);
                    list_all_Client = beanArrayList;

                }
            });


            return convertview;
        }


        class Holder {
            TextView ename,userType;
            EditText mail_Id;
            Button index_no;
            CheckBox checkBox;
            //ImageView imgvw_ok;

        }


    }

    class Async_ShareProjectFile extends AsyncTask<Void, Void, Void> {

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


            //String LID = sp.getString("clientid", "");
            String LID= Shared_Preference.getLOGIN_USER_ID(SharePhotosToClientActivity.this);
            String CID = CompanyId;
            //String JID = sp.getString(Utility.KEY_JOB_ID_FOR_JOBFILES, "");
            String JID = Shared_Preference.getJOB_ID_FOR_JOBFILES(SharePhotosToClientActivity.this);
            String FID = "";

            for (HashMap<String, String> hashMap : list_Selected_Project_Photos) {

                if (hashMap.containsKey("int_job_file_id")) {
                    String int_job_file_id = hashMap.get("int_job_file_id");
                    if (int_job_file_id.contains(".")) {
                        int_job_file_id = int_job_file_id.substring(0, int_job_file_id.indexOf("."));
                    }
                    FID = FID + int_job_file_id + ",";
                }

            }
            FID = FID.substring(0, FID.length() - 1);

            Log.e("list_selected_Client--", list_selected_Client.toString());
            Log.e("list_Selected_Photos--", list_Selected_Project_Photos.toString());
            Log.e("FID--", FID);


            String Mail = list_selected_Client.get(0).getTxt_Mail();
            String UID = list_selected_Client.get(0).getId_Pk();

            ShareProjectFile(LID, UID, Mail, CID, JID, FID, comment);

            for (int i = 0; i < list_selected_Client.size(); i++) {
                String Mail_1 = list_selected_Client.get(i).getTxt_Mail();
                String UID_1 = list_selected_Client.get(i).getId_Pk();
                // if (api_error) break;
                Mail(LID, UID_1, Mail_1, CID, JID, FID);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();


            Toast.makeText(getApplicationContext(), "Photo(s) shared successfully!", Toast.LENGTH_SHORT).show();
            Intent iiu = new Intent(SharePhotosToClientActivity.this, MainActivity.class);
            startActivity(iiu);
            finish();

            /*
            if (api_error) {
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Photo(s) shared successfully!", Toast.LENGTH_SHORT).show();
                Intent iiu = new Intent(SharePhotosToClientActivity.this, MainActivity.class);
                startActivity(iiu);
                finish();
            }*/


        }
    }

}
