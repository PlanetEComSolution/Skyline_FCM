package planet.info.skyline.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Timer;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.EventModel;
import planet.info.skyline.network.Api;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

/**
 * A simple {@link Fragment} subclass.
 */
public class Client_Events extends AppCompatActivity {

    Context myContext;
    //  UTIL utill;
    Timer timer;
    TextView tv_msg;
    ArrayList<EventModel> list_Event = new ArrayList<>();
    SharedPreferences sp;
    String Client_id_Pk, comp_ID;
    private int currentPage = 0;
    private ViewPager mViewPager;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;

    //  String       , jobID, job_Name, dealerId, Agency, loginUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about_us);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        tv_msg = findViewById(R.id.tv_msg);
        //  setTitle("Events");
        myContext = Client_Events.this;
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
        setTitle(Utility.getTitle("Events"));
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
        }
        //  sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

        /**************/
        Client_id_Pk = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        comp_ID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");
    /*     jobID = "-1"; //by default
        Agency = "0";// by default
        job_Name = getApplicationContext().getResources().getString(R.string.Select_Job);
        dealerId = sp.getString(Utility.CLIENT_LOGIN_DealerID, "");
        loginUserName = sp.getString(Utility.CLIENT_LOGIN_UserName, "");
*/
        /***************/

        setView();
        /****************/


    }

    @Override
    protected void onResume() {
        super.onResume();
        callApiEvents();
    }

    private void setView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_msg = findViewById(R.id.tv_msg);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(myContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void callApiEvents() {
        if (new ConnectionDetector(Client_Events.this).isConnectingToInternet()) {
            new Async_Event().execute();
        } else {
            Toast.makeText(Client_Events.this, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }

    public void getProjectPhotos2() {
        list_Event.clear();



        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + Api.API_GetClientNotification;;
        final String METHOD_NAME = Api.API_GetClientNotification;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("ClientUserId", comp_ID);//
        request.addProperty("Agency", "0");//
        request.addProperty("IsCount", "0");//


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String result = SoapPrimitiveresult.toString();
            //  JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                    String Id = jsonObject1.getString("Id");
                    String FileId = jsonObject1.getString("FileId");
                    String CreationDate = jsonObject1.getString("CreationDate");

                    String ModuleId = jsonObject1.getString("ModuleId");
                    String Status = jsonObject1.getString("Status");
                    String DoneBy = jsonObject1.getString("DoneBy");

                    String UploadedFileCount = jsonObject1.getString("UploadedFileCount");
                    String ClientId = jsonObject1.getString("ClientId");

                    String JobId = jsonObject1.getString("JobId");

                    String IsClient = jsonObject1.getString("IsClient");
                    String ClientUserId = jsonObject1.getString("ClientUserId");
                    String CompName = jsonObject1.getString("CompName");

                    String txt_Job = jsonObject1.getString("txt_Job");
                    String StatusName = jsonObject1.getString("StatusName");
                    String Event = jsonObject1.getString("Event");

                    //     String Event = jsonObject1.getString("Event");

                    EventModel projectPhoto = new EventModel(
                            Id,
                            FileId,
                            CreationDate,
                            ModuleId,
                            Status,
                            DoneBy,
                            UploadedFileCount,
                            ClientId,
                            JobId,
                            IsClient,
                            ClientUserId,
                            CompName,
                            txt_Job,
                            StatusName,
                            Event
                    );


                    list_Event.add(projectPhoto);

                    /*  *//**************************Ordering********************************//*
                    if (Action_status.equalsIgnoreCase("Rejected")) {
                        Rejected.add(projectPhoto);
                    } else if (Action_status.equalsIgnoreCase("Approved")) {
                        Approved.add(projectPhoto);
                    } else if (Action_status.equalsIgnoreCase("Snoozed")) {
                        snoozed.add(projectPhoto);
                    } else {
                        yetToBeReviewd.add(projectPhoto);
                    }*/

                } catch (Exception e) {
                    e.getMessage();
                }

            }

          /*  list_ProjectPhotos.addAll(yetToBeReviewd);
            list_ProjectPhotos.addAll(snoozed);
            list_ProjectPhotos.addAll(Approved);
            list_ProjectPhotos.addAll(Rejected);
*/


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class Async_Event extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDoalog = new ProgressDialog(Client_Events.this);
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getProjectPhotos2();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();

            //mSwipeRefreshLayout.setRefreshing(false);


            if (list_Event.size() < 1) {
                tv_msg.setVisibility(View.VISIBLE);

            } else {
                tv_msg.setVisibility(View.GONE);
            }


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new MoviesAdapter(Client_Events.this, list_Event);
            recyclerView.setAdapter(mAdapter);


        }
    }

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
        private List<EventModel> moviesList;
        //   private HttpImageManager mHttpImageManager;

        public MoviesAdapter(Activity context, List<EventModel> moviesList) {
            this.moviesList = moviesList;
            //     mHttpImageManager = ((AppController) context.getApplication()).getHttpImageManager();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_client_event, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            EventModel projectPhoto = moviesList.get(position);
            holder.index_no.setText(String.valueOf(position + 1));


            holder.event.setText(projectPhoto.getEvent()); //date

        /*    holder.user_type.setText(projectPhoto.getCompID());//time
            holder.master.setText(projectPhoto.getCompName());//day*/


/*
            if (Descr == null || Descr.trim().equals("")) {
                holder.tv_status.setText("Not available");
            } else {
                holder.tv_status.setText(Descr);
            }*/


            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  Intent intent = new Intent(context, ClientFileDetailActivity.class);
                    // intent.putExtra("obj", projectPhoto);
                    intent.putExtra("FileId", FileId);
                    intent.putExtra("FileName", FileName);
                    intent.putExtra("jobID", jobID);
                    intent.putExtra("jobName", job);
                    startActivity(intent);*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView event, user_type, master, phone, email;
            ImageView imgvw_edit;
            Button index_no;
            LinearLayout parentView;

            public MyViewHolder(View convertview) {
                super(convertview);

                parentView = convertview.findViewById(R.id.row_jobFile);
                index_no = (Button) convertview.findViewById(R.id.serial_no);
                event = (TextView) convertview.findViewById(R.id.event);
             /*   user_type = (TextView) convertview.findViewById(R.id.user_type);
                master = (TextView) convertview.findViewById(R.id.master);
            *//*  phone = (TextView) convertview.findViewById(R.id.phone);
               email = (TextView) convertview.findViewById(R.id.email);*//*
                imgvw_edit = (ImageView) convertview.findViewById(R.id.imgvw_edit);*/

            }
        }
    }

}
