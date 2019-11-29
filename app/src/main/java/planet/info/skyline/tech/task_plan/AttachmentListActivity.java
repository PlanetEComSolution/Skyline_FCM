package planet.info.skyline.tech.task_plan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.Attachment;
import planet.info.skyline.model.SWO;
import planet.info.skyline.model.TaskFile;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.REST_API_Client_TaskPlan;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.tech.fullscreenview.FullscreenWebView;
import planet.info.skyline.util.Utility;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AttachmentListActivity extends AppCompatActivity {


//    View rootView;
    String jobtxt_id, taskID;
    TextView tv_msg;
    List<TaskFile> list_Attachments = new ArrayList<>();

    private ProgressDialog progressDialog;
    AlertDialog alertDialog;
    //SwipeRefreshLayout pullToRefresh;
    SharedPreferences sp;

    private RecyclerView attachment_recyclerView;
    String fileExt;
    boolean isImage;
    boolean isDoc;
    boolean isMedia;
    boolean isWord;
    boolean isPdf;
    boolean isExcel;
    boolean isText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment_list);
        setTitle(Utility.getTitle("Attachment(s)"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setView();
       // setTempData();

        if (new ConnectionDetector(AttachmentListActivity.this).isConnectingToInternet()) {
            GetTaskFiles();
        } else {
            Toast.makeText(AttachmentListActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    private void setView() {

        progressDialog = new ProgressDialog(AttachmentListActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        attachment_recyclerView = (RecyclerView)findViewById(R.id.attachment_recyclerView);
        attachment_recyclerView.setLayoutManager(new LinearLayoutManager(  this, LinearLayoutManager.VERTICAL, false));
        attachment_recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView)findViewById(R.id.tv_msg);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

           // jobtxt_id = bundle.getString("jobtxt_id", "");
            taskID = bundle.getString("taskID", "");
        }
    }


/*
    private void setTempData() {
        list_Attachments.add(new Attachment("Snip20190806_83.png", "http://staging.ep2.businesstowork.com//Upload/135313796_Snip20190806_83.png",  "01/1/2019"));
        list_Attachments.add(new Attachment("Snip20190806_83.png", "http://staging.ep2.businesstowork.com//Upload/135313796_Snip20190806_83.png",  "01/1/2019"));
        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(  this, list_Attachments);
        attachment_recyclerView.setAdapter(jobFilesAdapter);


    }
*/



    private void GetTaskFiles() {

        showDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ID_PK", taskID);

        API_Interface apiService = REST_API_Client_TaskPlan.getClient().create(API_Interface.class);

        apiService.GetTaskFiles(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TaskFile>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                        String error = e.getMessage();
                        Toast.makeText(AttachmentListActivity.this,
                                "Network Error....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<TaskFile> taskFiles) {
                        list_Attachments.clear();

                        if (taskFiles != null && !taskFiles.isEmpty()) {
                            list_Attachments.addAll(taskFiles);
                        }

                        hideDialog();

                        if (list_Attachments.isEmpty()) {
                            tv_msg.setVisibility(View.VISIBLE);

                        } else {
                            tv_msg.setVisibility(View.GONE);
                        }

                        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(  AttachmentListActivity.this, list_Attachments);
                        attachment_recyclerView.setAdapter(jobFilesAdapter);


                    }
                });


    }



    public class JobFilesAdapter extends RecyclerView.Adapter<JobFilesAdapter.MyViewHolder> {

        List<TaskFile> list_Attachmentss;
        Context context;


        public JobFilesAdapter(Context context, List<TaskFile> Attachments) {
            this.context = context;
            this.list_Attachmentss = Attachments;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_attachment_2, parent, false);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int i) {


            final String fileName =list_Attachmentss.get(i).getFilePath();

            final String fileURL = SOAP_API_Client.URL_EP2+"/Upload/"+list_Attachmentss.get(i).getFilePath();
            final String date = list_Attachmentss.get(i).getUploadedOn();


            holder.index_no.setText(String.valueOf(i + 1));

            if (fileName.contains(".")) {
                fileExt = fileName.substring(fileName.lastIndexOf("."));
            }
            isImage = Arrays.asList(Utility.imgExt).contains(fileExt);
            isDoc = Arrays.asList(Utility.docExt).contains(fileExt);
            isMedia = Arrays.asList(Utility.mediaExt).contains(fileExt);

            isWord = Arrays.asList(Utility.wordExt).contains(fileExt);
            isPdf = Arrays.asList(Utility.pdfExt).contains(fileExt);
            isExcel = Arrays.asList(Utility.excelExt).contains(fileExt);
            isText = Arrays.asList(Utility.txtExt).contains(fileExt);
            holder.spinner.setVisibility(View.VISIBLE);

            if (isImage) {
               // String url = URL_EP2 + "/upload/" + FileName;

                String url =    fileURL;

                Glide.with(AttachmentListActivity.this).load(url).listener(new RequestListener<Drawable>() {
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


            } /*else if (isDoc) {
                holder.spinner.setVisibility(View.GONE);
                holder.thumbnail.setImageResource(R.drawable.doc);
            }*/ else if (isWord) {
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


            if (fileName == null || fileName.trim().equals("")) {
                holder.File.setText("Not available");
            } else {
                holder.File.setText(fileName);

            }







            if (date == null || date.trim().equals("")) {
                holder.Date.setText("Not available");
            } else {
                holder.Date.setText(Utility.ChangeDateFormat("MM/dd/yyyy","dd MMM yyyy",date.trim()));
            }



            holder.row_jobFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isImage) {


                        Intent i = new Intent(AttachmentListActivity.this, FullscreenImageView.class);
                        i.putExtra("url", fileURL);
                        startActivity(i);


                    } else if (isDoc || isWord ||isPdf||isText|| isExcel) {


                        Intent i = new Intent(AttachmentListActivity.this, FullscreenWebView.class);
                        i.putExtra("url", fileURL);
                        startActivity(i);

                    }


                }
            });

        }

        @Override
        public int getItemCount() {
            return list_Attachmentss.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView File, Date;

            Button index_no;
            LinearLayout row_jobFile;
           ImageView  thumbnail;
            ProgressBar spinner;
            public MyViewHolder(View convertview) {
                super(convertview);

                index_no = (Button) convertview.findViewById(R.id.serial_no);
                File = (TextView) convertview.findViewById(R.id.File);
                Date = (TextView) convertview.findViewById(R.id.Date);

                row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);
                spinner = (ProgressBar) convertview.findViewById(R.id.progressBar1);
                thumbnail = (ImageView) convertview.findViewById(R.id.thumbnail);
            }
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
    private void showDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
