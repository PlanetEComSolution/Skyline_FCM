package planet.info.skyline.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.ClientUserAll;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_GetClientUserListAll;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class UserListActivity extends AppCompatActivity {
    ListView listview_Clients;
    Context context;
    TextView tv_msg;
    ArrayList<ClientUserAll> list_ClientUser = new ArrayList<>();
    SharedPreferences sp;
    SwipeRefreshLayout pullToRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        listview_Clients = findViewById(R.id.cart_listview);
        tv_msg = findViewById(R.id.tv_msg);
        context = UserListActivity.this;


        setTitle(Utility.getTitle("Manage Users"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
       /* if (new ConnectionDetector(context).isConnectingToInternet()) {
            new Async_ClientUserList().execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }*/

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new ConnectionDetector(context).isConnectingToInternet()) {
                    new Async_ClientUserList().execute();
                } else {
                    Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                // your code

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new Async_ClientUserList().execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
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

    public void getClientUserListAll() {
        list_ClientUser.clear();

        ArrayList<ClientUserAll> Client = new ArrayList<>();
        ArrayList<ClientUserAll> MasterUser = new ArrayList<>();
        ArrayList<ClientUserAll> NormalUser = new ArrayList<>();

        String UserID = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        String _CompID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");
        String MasterStatus = sp.getString(Utility.CLIENT_LOGIN_Masterstatus, "");

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetClientUserListAll;
        final String METHOD_NAME =API_GetClientUserListAll;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("UserID", UserID);
        request.addProperty("CompID", _CompID);
        request.addProperty("MasterStatus", MasterStatus);

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

                    String userID = jsonObject1.getString("userID");
                    String txt_Mail = jsonObject1.getString("txt_Mail");
                    String CompID = jsonObject1.getString("CompID");
                    String CompName = jsonObject1.getString("CompName");
                    String UserName = jsonObject1.getString("UserName");
                    String UserCategory = jsonObject1.getString("UserCategory");
                    String CaType = jsonObject1.getString("CaType");
                    String MasterName = jsonObject1.getString("MasterName");
                    String txt_Mobile = jsonObject1.getString("txt_Mobile");

                    ClientUserAll clientUserAll = new ClientUserAll(
                            userID, txt_Mail, CompID,
                            CompName, UserName, UserCategory, CaType, MasterName, txt_Mobile);

                    //  list_Event.add(clientUserAll);


                    /**************************Ordering********************************/
                    if (UserCategory.equalsIgnoreCase("Client")) {
                        Client.add(clientUserAll);
                    } else if (UserCategory.equalsIgnoreCase("Master User")) {
                        MasterUser.add(clientUserAll);
                    } else if (UserCategory.equalsIgnoreCase("Normal User")) {
                        NormalUser.add(clientUserAll);
                    }


                } catch (Exception e) {
                    e.getMessage();
                }

            }
            list_ClientUser.addAll(Client);
            list_ClientUser.addAll(MasterUser);
            list_ClientUser.addAll(NormalUser);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class Async_ClientUserList extends AsyncTask<Void, Void, Void> {
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

            getClientUserListAll();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();
            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
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
        List<ClientUserAll> beanArrayList;
        Context context;
        int count = 1;

        public order_adapter(Context context, List<ClientUserAll> beanArrayList) {
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
            final String UserID = beanArrayList.get(i).getUserID();
            final String Txt_Mail = beanArrayList.get(i).getTxt_Mail();
            final String CompID = beanArrayList.get(i).getCompID();
            final String CompName = beanArrayList.get(i).getCompName();
            final String UserName = beanArrayList.get(i).getUserName();
            final String UserCategory = beanArrayList.get(i).getUserCategory();
            final String CaType = beanArrayList.get(i).getCaType();
            final String MasterName = beanArrayList.get(i).getMasterName();
            final String txt_Mobile = beanArrayList.get(i).gettxt_Mobile();


            if (convertview == null) {
                holder = new Holder();

                convertview = LayoutInflater.from(context).inflate(R.layout.row_client_all_user, null);
                holder.index_no = (Button) convertview.findViewById(R.id.serial_no);

                holder.name = (TextView) convertview.findViewById(R.id.name);
                holder.user_type = (TextView) convertview.findViewById(R.id.user_type);
                holder.master = (TextView) convertview.findViewById(R.id.master);
                holder.phone = (TextView) convertview.findViewById(R.id.phone);
                holder.email = (TextView) convertview.findViewById(R.id.email);
                holder.imgvw_edit = (ImageView) convertview.findViewById(R.id.imgvw_edit);


                convertview.setTag(holder);
            } else {
                holder = (Holder) convertview.getTag();
            }
            holder.index_no.setText(String.valueOf(i + 1));


            if (UserName == null || UserName.trim().equals("")) {
                holder.name.setText("Not available");
            } else {
                holder.name.setText(UserName);
            }

            if (UserCategory == null || UserCategory.trim().equals("")) {
                holder.user_type.setText("Not available");
            } else {
                holder.user_type.setText(UserCategory);
            }


            if (UserCategory.equalsIgnoreCase("Client")) {
                holder.imgvw_edit.setVisibility(View.GONE);
                holder.user_type.setTextColor(getResources().getColor(R.color.main_green_color));

            }else {
                holder.user_type.setTextColor(getResources().getColor(R.color.primaryTextColor));
            }


            if (MasterName == null || MasterName.trim().equals("") || MasterName.trim().equals("null")) {
                holder.master.setText("Not available");
            } else {
                holder.master.setText(MasterName);
            }
            if (txt_Mobile == null || txt_Mobile.trim().equals("")) {
                holder.phone.setText("Not available");
            } else {
                holder.phone.setText(txt_Mobile);
            }
            if (Txt_Mail == null || Txt_Mail.trim().equals("")) {
                holder.email.setText("Not available");
            } else {
                holder.email.setText(Txt_Mail);
            }

            holder.imgvw_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!UserCategory.equalsIgnoreCase("Client")) {
                        Intent i = new Intent(UserListActivity.this, UpdateUserInfoActivity.class);
                        i.putExtra("UserID", UserID);
                        startActivity(i);
                    }
                }
            });

            convertview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!UserCategory.equalsIgnoreCase("Client")) {
                        Intent i = new Intent(UserListActivity.this, UpdateUserInfoActivity.class);
                        i.putExtra("UserID", UserID);
                        startActivity(i);
                    }
                }
            });
            return convertview;
        }


        class Holder {
            TextView name, user_type, master, phone, email;
            ImageView imgvw_edit;
            Button index_no;


        }


    }
}
