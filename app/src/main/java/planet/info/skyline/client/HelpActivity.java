package planet.info.skyline.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
//import planet.info.skyline.httpimage.HttpImageManager;
import planet.info.skyline.model.Help;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_GetHelpDetails;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class HelpActivity extends AppCompatActivity {
    Context context;
    TextView tv_msg;
    SharedPreferences sp;
    String Client_id_Pk, CLIENT_LOGIN_CompID;
    ArrayList<Help> listHelp = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        context = HelpActivity.this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        tv_msg = findViewById(R.id.tv_msg);


        setTitle(Utility.getTitle("Help"));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

        Client_id_Pk = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        CLIENT_LOGIN_CompID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");

        /***************/
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiProjectPhotos();
                // your code

            }
        });
        callApiProjectPhotos();
        /****************/
    }

    private void callApiProjectPhotos() {
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new Async_ProjectPhotos().execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }

    public void getProjectPhotosNew() {
        listHelp.clear();

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_GetHelpDetails;
        final String METHOD_NAME =API_GetHelpDetails;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        request.addProperty("clientid", CLIENT_LOGIN_CompID);//

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
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String name = jsonObject1.getString("name");
                    String mail = jsonObject1.getString("mail");
                    String phone = jsonObject1.getString("phone");
                    String typo = jsonObject1.getString("typo");
                    listHelp.add(new Help(0, name, mail, phone, typo));

                }

            } catch (Exception e) {
                e.getMessage();
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

    private class Async_ProjectPhotos extends AsyncTask<Void, Void, Void> {

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

            // getProjectPhotos();
            getProjectPhotosNew();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();
            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new MoviesAdapter(HelpActivity.this, listHelp);
            recyclerView.setAdapter(mAdapter);

        }
    }

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
        private List<Help> moviesList;
     //   private HttpImageManager mHttpImageManager;

        public MoviesAdapter(Activity context, List<Help> moviesList) {
            this.moviesList = moviesList;
         //   mHttpImageManager = ((AppController) context.getApplication()).getHttpImageManager();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_help, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Help projectPhoto = moviesList.get(position);
            holder.index_no.setText(String.valueOf(position + 1));

            final String Name = projectPhoto.getName();
            final String Mail = projectPhoto.getMail();
            final String Typo = projectPhoto.getTypo();
            final String Phone = projectPhoto.getPhone();


            if (Name == null || Name.trim().equals("")) {
                holder.tv_name.setText("Not available");
            } else {
                holder.tv_name.setText(Name);
            }
            if (Mail == null || Mail.trim().equals("")) {
                holder.tv_mail.setText("Not available");
            } else {
                holder.tv_mail.setText(Mail);
            }
            if (Typo == null || Typo.trim().equals("")) {
                holder.tv_type.setText("Not available");
            } else {
                holder.tv_type.setText(Typo);
            }

            if (Phone == null || Phone.trim().equals("")) {
                holder.tv_phone.setText("Not available");
            } else {
                holder.tv_phone.setText(Phone);
            }

            holder.tv_mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
//                        Intent mailClient = new Intent(Intent.ACTION_VIEW);
//                        mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
//                        startActivity(mailClient);

                        String[] TO = {holder.tv_mail.getText().toString().trim()};
                        shareToGMail(TO, "", "");
                    } catch (Exception e) {
                        e.getCause();
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        startActivity(emailIntent);
                    }
                }
            });
            holder.tv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + holder.tv_phone.getText().toString().trim()));
                    startActivity(intent);
                }
            });

        }

        private void shareToGMail(String[] email, String subject, String content) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                    best = info;
            if (best != null)
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            startActivity(emailIntent);
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_name, tv_mail, tv_phone, tv_type;
            Button index_no;
            LinearLayout parentView;

            public MyViewHolder(View view) {
                super(view);

                index_no = (Button) view.findViewById(R.id.serial_no);

                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_mail = (TextView) view.findViewById(R.id.tv_mail);
                tv_phone = (TextView) view.findViewById(R.id.tv_phone);
                tv_type = (TextView) view.findViewById(R.id.tv_type);
                parentView = (LinearLayout) view.findViewById(R.id.parentView);


            }
        }
    }

}
