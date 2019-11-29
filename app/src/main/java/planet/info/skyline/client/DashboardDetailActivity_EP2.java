package planet.info.skyline.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.ProjectPhoto;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_ProofRenderByStatus;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;

public class DashboardDetailActivity_EP2 extends AppCompatActivity {

    Context context;
    TextView tv_msg;
    ArrayList<ProjectPhoto> list_ProjectPhotos = new ArrayList<>();
    SharedPreferences sp;
    String Client_id_Pk, comp_ID, jobID, job_Name, dealerId, Agency;
    String status = "";
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    SwipeRefreshLayout pullToRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_files);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        tv_msg = findViewById(R.id.tv_msg);
        context = DashboardDetailActivity_EP2.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);

        /*******************/
        status = getIntent().getExtras().getString("status");
        setTitle(Utility.getTitle(status + " List"));
        /**************/
        Client_id_Pk = sp.getString(Utility.CLIENT_LOGIN_userID, "");
        comp_ID = sp.getString(Utility.CLIENT_LOGIN_CompID, "");
        jobID = "-1"; //by default
        Agency = "0";// by default
        job_Name = getApplicationContext().getResources().getString(R.string.Select_Job);
        dealerId = sp.getString(Utility.CLIENT_LOGIN_DealerID, "");
        jobID = getIntent().getExtras().getString("job");
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApiDashboardDeatils();
                // your code

            }
        });
        /***************/
        //   callApiDashboardDeatils();
        /****************/




    }

    private void callApiDashboardDeatils() {
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            new Async_DashboardDetails().execute();
        } else {
            Toast.makeText(context, Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callApiDashboardDeatils();
    }

    public void getProjectPhotos() {
        list_ProjectPhotos.clear();


        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_ProofRenderByStatus;
        final String METHOD_NAME =API_ProofRenderByStatus;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("ClientUserID", Client_id_Pk);//
        request.addProperty("jobID", jobID);//
        request.addProperty("CommentID", "0");
        request.addProperty("status", status);
        request.addProperty("compID", comp_ID);//
        request.addProperty("Agency", Agency);//
        request.addProperty("dealerID", dealerId);//

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
                    int nu_comment_id = 0;
                    int ddl_job_status = 0;//
                    int cl = 0;//
                    String dt = "";//
                    String job = jsonObject1.getString("job");
                    String fsize = jsonObject1.getString("pageType");
                    String latestcom1 = "";//
                    int job_id = jsonObject1.getInt("jid");
                    String actiondate = jsonObject1.getString("LastActionDate");//
                    String createdate = jsonObject1.getString("createdate");//
                    int nu_approveedit_by_client = 0;//
                    String latestcom = "";
                    int fileid = 0;//
                    int jid = 0;//
                    int View_status_for_client = 0;//
                    int nu_client_id = jsonObject1.getInt("ddl_Client");

                    int nu_reject_by_client = 0;//
                    String snoozDate = "";//

                    int snooz = 0;//
                    int nu_approve_by_client = 0;//
                    int INT_FID = jsonObject1.getInt("fid");
                    int INT_FileID = jsonObject1.getInt("fid");
                    String VCHAR_Heading = "";//
                    String FILE_NAME_question = jsonObject1.getString("FILE_NAME");
                    String FILE_NAME = jsonObject1.getString("FILE_NAME");
                    String Action_statusOLD = jsonObject1.getString("latest_status");
                    String Action_status = jsonObject1.getString("latest_status");
                    String Descr = jsonObject1.getString("Descr");
                    String file_heading = "";
                    int id = jsonObject1.getInt("fid");
                    String ImgName = jsonObject1.getString("ImgName");

                    list_ProjectPhotos.add(new ProjectPhoto(
                            nu_comment_id, ddl_job_status,
                            cl, dt, job, fsize,
                            latestcom1, job_id, actiondate, createdate,

                            nu_approveedit_by_client, latestcom, fileid, jid,
                            View_status_for_client, nu_client_id, nu_reject_by_client,
                            snoozDate, snooz, nu_approve_by_client,

                            INT_FID, INT_FileID, VCHAR_Heading,

                            FILE_NAME_question, FILE_NAME,

                            Action_statusOLD, Action_status,

                            Descr, file_heading,
                            id, ImgName));


                } catch (Exception e) {
                    e.getMessage();
                }

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

    private class Async_DashboardDetails extends AsyncTask<Void, Void, Void> {

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

            getProjectPhotos();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDoalog.dismiss();
            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
            //mSwipeRefreshLayout.setRefreshing(false);


            if (list_ProjectPhotos.size() < 1) {
                tv_msg.setVisibility(View.VISIBLE);

            } else {
                tv_msg.setVisibility(View.GONE);
            }


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new MoviesAdapter(DashboardDetailActivity_EP2.this, list_ProjectPhotos);
            recyclerView.setAdapter(mAdapter);


        }
    }

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private List<ProjectPhoto> moviesList;


        public MoviesAdapter(Activity context, List<ProjectPhoto> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_dashboard, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            ProjectPhoto projectPhoto = moviesList.get(position);

            holder.index_no.setText(String.valueOf(position + 1));
            final String FileName = projectPhoto.getFILENAME();
            final String Descr = projectPhoto.getDescr();
            final String job = projectPhoto.getJob();
            String createdate = projectPhoto.getCreatedate();

            final String Actiondate = projectPhoto.getActiondate();
            final String ImgName = projectPhoto.getImgName();
            final String FileId = projectPhoto.getId() + "";

            final String PageType = projectPhoto.getFsize();

            String fileExt = "";
            if (FileName.contains(".")) {
                fileExt = FileName.substring(FileName.lastIndexOf("."));
            } else if (ImgName.contains(".")) {
                fileExt = ImgName.substring(ImgName.lastIndexOf("."));
            }
            if (!fileExt.equals("")) {
                boolean isImage = Arrays.asList(Utility.imgExt).contains(fileExt);
                boolean isDoc = Arrays.asList(Utility.docExt).contains(fileExt);
                boolean isMedia = Arrays.asList(Utility.mediaExt).contains(fileExt);
                boolean isWord = Arrays.asList(Utility.wordExt).contains(fileExt);
                boolean isPdf = Arrays.asList(Utility.pdfExt).contains(fileExt);
                boolean isExcel = Arrays.asList(Utility.excelExt).contains(fileExt);
                boolean isText = Arrays.asList(Utility.txtExt).contains(fileExt);

                if (isImage) {
                    String url = "";
                    if (PageType.equals("3")) {
                        url = "https://drive.google.com/thumbnail?id=" + FileName;
                    } else {
                        url = URL_EP2 + "/upload/" + FileName;
                    }

                    Log.e("img url",url);
                    Glide.with(DashboardDetailActivity_EP2.this).load(url).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.spinner.setVisibility(View.GONE);

                            holder.thumbnail.setImageResource(R.drawable.no_image);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.spinner.setVisibility(View.GONE);

                            return false;
                        }
                    })
                            .into(holder.thumbnail);

                }/* else if (isDoc) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.doc);
            } */ else if (isWord) {
                    holder.spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.doc);
                } else if (isPdf) {
                    holder.spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.pdf);
                } else if (isExcel) {
                    holder.spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.excel);
                } else if (isText) {
                    holder.spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.txt_file_icon);
                } else if (isMedia) {
                    holder.spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.media);
                } else {
                    holder.spinner.setVisibility(View.GONE);
                    holder.thumbnail.setImageResource(R.drawable.no_image);
                }
            } else {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.no_image);
            }



            if (PageType.equalsIgnoreCase("1")) {

                holder.pageType.setText("Proof & Renders");
            } else if (PageType.equalsIgnoreCase("2")) {

                holder.pageType.setText("Project Photos");

            } else if (PageType.equalsIgnoreCase("3")) {

                holder.pageType.setText("Project Files");

            }



            if (FileName == null || FileName.trim().equals("")) {
                holder.tv_file_name.setText("Not available");
            } else {
            //   if (PageType.equalsIgnoreCase("3")){
                   holder.tv_file_name.setText(ImgName);
            //   }else {
            //       holder.tv_file_name.setText(FileName);
          //     }


            }
            if (Descr == null || Descr.trim().equals("")) {
                holder.tv_job_name.setText("Not available");
            } else {
                holder.tv_job_name.setText(Descr);
            }

            if (createdate == null || createdate.trim().equals("")) {
                holder.tv_dated.setText("Not available");
            } else {
                String dated = createdate;
                if (dated.contains("-")) {
                    dated = dated.replaceAll("-", "/");
                }
                holder.tv_dated.setText(dated);
            }


            if (Actiondate == null || Actiondate.trim().equals("")) {
                holder.tv_status.setText("Not available");
            } else {
                String lastActionDate=Actiondate;
                if(lastActionDate.contains(" ")){
                    lastActionDate=lastActionDate.substring(0,lastActionDate.indexOf(" "));
                }
                holder.tv_status.setText(lastActionDate);


            }

            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (PageType.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(context, ProofRenders_DetailActivity.class);
                        intent.putExtra("FileId", FileId);
                        intent.putExtra("FileName", FileName);
                        intent.putExtra("jobID", jobID);
                        startActivity(intent);
                    } else if (PageType.equalsIgnoreCase("2")) {
                        Intent intent = new Intent(context, ProjectPhotoDetailActivity.class);
                        intent.putExtra("FileId", FileId);
                        intent.putExtra("FileName", FileName);
                        intent.putExtra("jobID", jobID);
                        startActivity(intent);
                    } else if (PageType.equalsIgnoreCase("3")) {
                        Intent intent = new Intent(context, ProjectFileDetailActivity.class);
                        intent.putExtra("FileId", FileId);
                        intent.putExtra("FileName", ImgName);
                        intent.putExtra("jobID", jobID);
                        intent.putExtra("googleId", FileName);
                        startActivity(intent);
                    }


                }
            });


        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_file_name, tv_job_name, tv_dated, tv_status, tv_status_heading,pageType;
            ImageView thumbnail;
            Button index_no;
            LinearLayout parentView;

            ProgressBar spinner;

            public MyViewHolder(View view) {
                super(view);

                index_no = (Button) view.findViewById(R.id.serial_no);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                tv_file_name = (TextView) view.findViewById(R.id.tv_file_name);
                tv_job_name = (TextView) view.findViewById(R.id.tv_job_name);
                tv_dated = (TextView) view.findViewById(R.id.tv_dated);
                tv_status = (TextView) view.findViewById(R.id.tv_status);
                parentView = (LinearLayout) view.findViewById(R.id.parentView);
                tv_status_heading = (TextView) view.findViewById(R.id.tv_status_heading);
                pageType= (TextView) view.findViewById(R.id.pageType);
                spinner = (ProgressBar) view.findViewById(R.id.progressBar1);

            }
        }
    }


}

