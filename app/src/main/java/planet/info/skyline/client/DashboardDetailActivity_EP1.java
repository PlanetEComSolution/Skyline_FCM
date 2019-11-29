package planet.info.skyline.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.controller.AppController;
import planet.info.skyline.model.OrderStatus;
import planet.info.skyline.network.Api;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.util.Utility;

public class DashboardDetailActivity_EP1 extends AppCompatActivity {

    Context context;
    TextView tv_msg;

    SharedPreferences sp;
    String Client_id_Pk, comp_ID, jobID, job_Name, dealerId, Agency;
    ProgressDialog progressDoalog;
    String status = "";
    ArrayList<OrderStatus> list_OrderStatus = new ArrayList<>();

    private RecyclerView recyclerView;
    private MoviesAdapterApproved mAdapterApproved;
    private MoviesAdapterPaused mAdapterPaused;
    private MoviesAdapterWaiting mAdapterWaiting;
    SwipeRefreshLayout pullToRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_files_ep1);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_msg = findViewById(R.id.tv_msg);
        context = DashboardDetailActivity_EP1.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

        /*******************/
        status = getIntent().getExtras().getString("status");
        String Order = "";
        if (status.equalsIgnoreCase("1")) {
            Order = "Pending Order";
        } else if (status.equalsIgnoreCase("2")) {
            Order = "Waiting Order";
        } else if (status.equalsIgnoreCase("3")) {
            Order = "Approved Order";
        }

        setTitle(Utility.getTitle(Order + " List"));
        /**************/
        Client_id_Pk = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        comp_ID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");
        jobID = "-1"; //by default
        Agency = "0";// by default
        job_Name = getApplicationContext().getResources().getString(R.string.Select_Job);
        dealerId = sp.getString(Utility.CLIENT_LOGIN_DealerID, "");
        /***************/
        //   callApiDashboardDeatils();
        /****************/
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDahsboardDataEP1();
                // your code

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getDahsboardDataEP1();
    }

    public void getDahsboardDataEP1() {
        showProgressDialog();
        list_OrderStatus.clear();
        String client = comp_ID;
        String user = Client_id_Pk;
        String dealer = sp.getString(Utility.CLIENT_LOGIN_DealerID, "");
        String URL = SOAP_API_Client.URL_EP1 + Api.API_GET_ORDER_COUNT + client + "&user=" + user + "&dealer=" + dealer;

        JsonObjectRequest bb = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject obj) {
                hideProgressDialog();

                try {
                    JSONArray jsonArray = obj.getJSONArray("data");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    JSONArray jsonArrayPending = new JSONArray();
                    JSONArray jsonArraywaiting = new JSONArray();
                    JSONArray jsonArrayapproved = new JSONArray();

                  try {
                      jsonArrayPending = jsonObject.getJSONArray("pending");
                  }catch (Exception e){e.getCause();}

                    try {
                        jsonArraywaiting = jsonObject.getJSONArray("waiting");
                    }  catch (Exception e){e.getCause();}
                    try {
                        jsonArrayapproved = jsonObject.getJSONArray("approved");
                    }  catch (Exception e){e.getCause();}

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());


                    if (status.equals("1")) {

                        if (jsonArrayPending.length() > 0) {
                            for (int i = 0; i < jsonArrayPending.length(); i++) {
                                JSONObject jsonObject1 = jsonArrayPending.getJSONObject(i);
                                String show_name = jsonObject1.getString("show_name");
                                String last_used = jsonObject1.getString("last_used");
                                list_OrderStatus.add(new OrderStatus(show_name, "", last_used, "", "", ""));
                            }

                            mAdapterPaused = new MoviesAdapterPaused(DashboardDetailActivity_EP1.this, list_OrderStatus);
                            recyclerView.setAdapter(mAdapterPaused);
                        } else {
                            tv_msg.setVisibility(View.VISIBLE);
                        }


                    } else if (status.equals("2")) {
                        if (jsonArraywaiting.length() > 0) {
                            for (int i = 0; i < jsonArraywaiting.length(); i++) {
                                JSONObject jsonObject1 = jsonArraywaiting.getJSONObject(i);
                                String show_name = jsonObject1.getString("show_name");
                                String job_name = jsonObject1.getString("job_name");
                                String venue_name = jsonObject1.getString("venue_name");
                                String created_datetime = jsonObject1.getString("created_datetime");


                                list_OrderStatus.add(new OrderStatus(show_name, venue_name, job_name, created_datetime, "", ""));

                            }
                            mAdapterWaiting = new MoviesAdapterWaiting(DashboardDetailActivity_EP1.this, list_OrderStatus);
                            recyclerView.setAdapter(mAdapterWaiting);
                        } else {
                            tv_msg.setVisibility(View.VISIBLE);
                        }


                    } else if (status.equals("3")) {
                        if (jsonArrayapproved.length() > 0) {
                            for (int i = 0; i < jsonArrayapproved.length(); i++) {
                                JSONObject jsonObject1 = jsonArrayapproved.getJSONObject(i);
                                String show_name = jsonObject1.getString("show_name");
                                String job_name = jsonObject1.getString("job_name");
                                String venue_name = jsonObject1.getString("venue_name");


                                String show_date = jsonObject1.getString("show_date");
                                String exhibit_name = jsonObject1.getString("exhibit_name");
                                String job_no = jsonObject1.getString("job_no");


                                list_OrderStatus.add(new OrderStatus(show_name, venue_name, job_name, show_date, exhibit_name, job_no));

                            }
                            mAdapterApproved = new MoviesAdapterApproved(DashboardDetailActivity_EP1.this, list_OrderStatus);
                            recyclerView.setAdapter(mAdapterApproved);
                        } else {
                            tv_msg.setVisibility(View.VISIBLE);
                        }

                    }


                } catch (Exception e) {
                    e.getMessage();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                try {
                    hideProgressDialog();
                    Toast.makeText(DashboardDetailActivity_EP1.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(bb);
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

    public void showProgressDialog() {
        progressDoalog = new ProgressDialog(DashboardDetailActivity_EP1.this);
        progressDoalog.setMessage(getResources().getString(R.string.Loading_text));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCancelable(false);
        if (!progressDoalog.isShowing()) {
            progressDoalog.show();
        }
    }

    public void hideProgressDialog() {
        try {
            if (progressDoalog != null) {
                progressDoalog.dismiss();
            }
            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
        } catch (Exception e) {
            e.getCause();
        }
    }


    public class MoviesAdapterPaused extends RecyclerView.Adapter<MoviesAdapterPaused.MyViewHolder> {

        private List<OrderStatus> moviesList;


        public MoviesAdapterPaused(Activity context, List<OrderStatus> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_dashboard_paused_ep1, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            OrderStatus orderStatus = moviesList.get(position);
            holder.index_no.setText(String.valueOf(position + 1));

            final String Job = orderStatus.getJobName();
            final String Show = orderStatus.getShowName();
            final String Venue = orderStatus.getVenueName();

            final String Show_date = orderStatus.getShow_date();
            final String Exhibit_name = orderStatus.getExhibit_name();
            final String Job_no = orderStatus.getJob_no();


            if (Show == null || Show.trim().equals("")) {
                holder.tv_show_name.setText("Not available");
            } else {
                holder.tv_show_name.setText(Show);
            }

            if (Job == null || Job.trim().equals("")) {
                holder.tv_last_used.setText("Not available");
            } else {
                String outputDateStr = "";
                try {
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    DateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
                    String inputDateStr = Job;
                    Date date = inputFormat.parse(inputDateStr);
                    outputDateStr = outputFormat.format(date);

                } catch (Exception e) {
                    e.getCause();
                }
                holder.tv_last_used.setText(outputDateStr);

            }

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_show_name, tv_last_used;
            // TextView tv_show_name_title,tv_venue_title,tv_job_name_title;
            Button index_no;
            //  LinearLayout parentView,ll_venue,ll_exhibit_title,ll_date;


            public MyViewHolder(View view) {
                super(view);

                index_no = (Button) view.findViewById(R.id.serial_no);
                tv_show_name = (TextView) view.findViewById(R.id.tv_show_name);
                tv_last_used = (TextView) view.findViewById(R.id.tv_last_used);


//                tv_job_name = (TextView) view.findViewById(R.id.tv_job_name);
//                tv_venue = (TextView) view.findViewById(R.id.tv_venue);
//                tv_show_name_title = (TextView) view.findViewById(R.id.tv_show_name_title);
//                tv_venue_title = (TextView) view.findViewById(R.id.tv_venue_title);
//                tv_job_name_title = (TextView) view.findViewById(R.id.tv_job_name_title);
//                tv_date_title = (TextView) view.findViewById(R.id.tv_date_title);
//                tv_date = (TextView) view.findViewById(R.id.tv_date);
//
//                parentView = (LinearLayout) view.findViewById(R.id.parentView);
//                ll_venue = (LinearLayout) view.findViewById(R.id.ll_venue);
//
//                ll_exhibit_title =  view.findViewById(R.id.ll_exhibit_title);
//                tv_exhibit_title =  view.findViewById(R.id.tv_exhibit_title);
//                tv_exhibit_confg =  view.findViewById(R.id.tv_exhibit_confg);
//
//                ll_date=  view.findViewById(R.id.ll_date);
            }
        }
    }

    public class MoviesAdapterApproved extends RecyclerView.Adapter<MoviesAdapterApproved.MyViewHolder> {

        private List<OrderStatus> moviesList;


        public MoviesAdapterApproved(Activity context, List<OrderStatus> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_dashboard_approved_ep1, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            OrderStatus orderStatus = moviesList.get(position);
            holder.index_no.setText(String.valueOf(position + 1));

            final String Job = orderStatus.getJobName();
            final String Show = orderStatus.getShowName();
            final String Venue = orderStatus.getVenueName();

            final String Show_date = orderStatus.getShow_date();
            final String Exhibit_name = orderStatus.getExhibit_name();
            final String Job_no = orderStatus.getJob_no();


            if (Job_no == null || Job_no.trim().equals("")) {
                holder.tv_job_name.setText("Not available");
            } else {
                holder.tv_job_name.setText(Job_no);
            }
            if (Show == null || Show.trim().equals("")) {
                holder.tv_show_name.setText("Not available");
            } else {
                holder.tv_show_name.setText(Show);
            }

            if (Venue == null || Venue.trim().equals("")) {
                holder.tv_venue.setText("Not available");
            } else {
                holder.tv_venue.setText(Venue);
            }
            if (Show_date == null || Show_date.trim().equals("")) {
                holder.tv_date.setText("Not available");
            } else {


                String outputDateStr = "";
                try {

                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String inputDateStr = Show_date;
                    Date date = inputFormat.parse(inputDateStr);
                    outputDateStr = outputFormat.format(date);

                } catch (Exception e) {
                    e.getCause();
                }

                holder.tv_date.setText(outputDateStr);
            }
            if (Exhibit_name == null || Exhibit_name.trim().equals("") || Exhibit_name.trim().equals("null")) {
                holder.tv_exhibit_confg.setText("Not available");
            } else {
                holder.tv_exhibit_confg.setText(Exhibit_name);
            }
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_show_name, tv_job_name, tv_venue, tv_date, tv_exhibit_confg;

            Button index_no;


            public MyViewHolder(View view) {
                super(view);

                index_no = (Button) view.findViewById(R.id.serial_no);
                tv_show_name = (TextView) view.findViewById(R.id.tv_show_name);
                tv_job_name = (TextView) view.findViewById(R.id.tv_job_name);
                tv_venue = (TextView) view.findViewById(R.id.tv_venue);
                tv_date = (TextView) view.findViewById(R.id.tv_date);
                tv_exhibit_confg = view.findViewById(R.id.tv_exhibit_confg);


            }
        }
    }

    public class MoviesAdapterWaiting extends RecyclerView.Adapter<MoviesAdapterWaiting.MyViewHolder> {

        private List<OrderStatus> moviesList;


        public MoviesAdapterWaiting(Activity context, List<OrderStatus> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_dashboard_waiting_ep1, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            OrderStatus orderStatus = moviesList.get(position);
            holder.index_no.setText(String.valueOf(position + 1));

            final String Job = orderStatus.getJobName();
            final String Show = orderStatus.getShowName();
            final String Venue = orderStatus.getVenueName();

            final String Show_date = orderStatus.getShow_date();
            final String Exhibit_name = orderStatus.getExhibit_name();
            final String Job_no = orderStatus.getJob_no();


            if (Show == null || Show.trim().equals("")) {
                holder.tv_show_name.setText("Not available");
            } else {
                holder.tv_show_name.setText(Show);
            }

            if (Job == null || Job.trim().equals("")) {
                holder.tv_job_name.setText("Not available");
            } else {
                holder.tv_job_name.setText(Job);
            }
            if (Show_date == null || Show_date.trim().equals("")) {
                holder.tv_date.setText("Not available");
            } else {

                String outputDateStr = "";
                try {


                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                    DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String inputDateStr = Show_date;
                    Date date = inputFormat.parse(inputDateStr);
                    outputDateStr = outputFormat.format(date);

                } catch (Exception e) {
                    e.getCause();
                }


                holder.tv_date.setText(outputDateStr);
            }

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_show_name, tv_job_name, tv_date;

            Button index_no;


            public MyViewHolder(View view) {
                super(view);

                index_no = (Button) view.findViewById(R.id.serial_no);
                tv_show_name = (TextView) view.findViewById(R.id.tv_show_name);
                tv_job_name = (TextView) view.findViewById(R.id.tv_job_name);

                tv_date = (TextView) view.findViewById(R.id.tv_date);

            }
        }
    }

}

