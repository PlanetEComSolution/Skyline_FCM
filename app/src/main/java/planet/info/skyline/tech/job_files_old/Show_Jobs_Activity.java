package planet.info.skyline.tech.job_files_old;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import planet.info.skyline.old_activity.BaseActivity;
import planet.info.skyline.R;
import planet.info.skyline.old_activity.Utils1;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.util.FileDownloader;
import planet.info.skyline.util.Utility;

import static planet.info.skyline.network.Api.API_Bind_Job_client;
import static planet.info.skyline.network.Api.API_GetJobFileByTextJob;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class Show_Jobs_Activity extends BaseActivity {

    ListView jobs_list_View, jobs_listby_client;
    String jobtxt_id;
    AlertDialog alertDialog;

    List<HashMap<String, String>> data = new ArrayList<>();
    HashMap<String, String> map;
    RelativeLayout relative;

    List<HashMap<String, String>> data1 = new ArrayList<>();
    HashMap<String, String> map1;
    int count;
    public static final int progress_bar_type = 0;
    ProgressDialog progressDoalog;

    SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show__jobs);
        jobs_list_View = (ListView) findViewById(R.id.cart_listview);
        jobs_listby_client = (ListView) findViewById(R.id.discountlistview);
        relative = (RelativeLayout) findViewById(R.id.newlayout1);
        Intent in = getIntent();
        jobtxt_id = in.getStringExtra("job_idtxt");

        //nks
        sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
        //nks

        if (new ConnectionDetector(Show_Jobs_Activity.this).isConnectingToInternet()) {

            new dealer_loder_Acyntask().execute();
        } else {
            Toast.makeText(Show_Jobs_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
        }




    }


    class dealer_loder_Acyntask extends AsyncTask<Void, Void, Void> {

        ProgressDialog d = new ProgressDialog(Show_Jobs_Activity.this);

        @Override
        protected Void doInBackground(Void... params) {
            fettch_job_list();
            fettch_job_by_Client_list();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            d.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            d.dismiss();
            order_adapter adapter = new order_adapter(Show_Jobs_Activity.this, data);
            jobs_list_View.setAdapter(adapter);
            Utils1.setListViewHeightBasedOnChildren(jobs_list_View);


            Show_client_Adapter adapter1 = new Show_client_Adapter(Show_Jobs_Activity.this, data1);
            jobs_listby_client.setAdapter(adapter1);
            Utils1.setListViewHeightBasedOnChildren(jobs_listby_client);

            if (count == 1) {
                relative.setVisibility(View.INVISIBLE);
            }

        }
    }


    public void fettch_job_list() {

        int index = 1;
        int count = 0;
        final String NAMESPACE = KEY_NAMESPACE+"";
        final String URL = URL_EP2+"/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE+API_GetJobFileByTextJob;
        final String METHOD_NAME = API_GetJobFileByTextJob;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("job", jobtxt_id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();
            if (recved.contains("Job not found")) {
                count = 1;
            } else {
                String[] aa = recved.split("=");
                String a = aa[1];
                String b[] = a.split(";");
                String k = b[0];
                JSONObject jsonObject = new JSONObject(k);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    map = new HashMap<>();
                    map.clear();
                    index = 1 + i;
                    String index1 = Integer.toString(index);
                    String clientnme = jsonObject1.getString("clientnme");
                    String txt_Job = jsonObject1.getString("txt_Job");
                    String int_job_file_id = jsonObject1.getString("int_job_file_id");
                    String int_job_id = jsonObject1.getString("int_job_id");
                    String var_file_name1 = jsonObject1.getString("var_file_name1");
                    String var_file_name = jsonObject1.getString("var_file_name");
                    String var_file_des = jsonObject1.getString("var_file_des");
                    String int_rndm_id_file = jsonObject1.getString("int_rndm_id_file");
                    String dt_file_Udate = jsonObject1.getString("dt_file_Udate");

                    map.put("index1", index1);
                    map.put("clientnme", clientnme);
                    map.put("txt_Job", txt_Job);
                    map.put("int_job_file_id", int_job_file_id);
                    map.put("int_job_id", int_job_id);
                    map.put("var_file_name1", var_file_name1);
                    map.put("var_file_name", var_file_name);
                    map.put("var_file_des", var_file_des);
                    map.put("int_rndm_id_file", int_rndm_id_file);
                    map.put("dt_file_Udate", dt_file_Udate);

                    data.add(map);


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class Download_images extends AsyncTask<String, Void, String> {///this class make in adapter for downloading the images

        //   ProgressDialog progressDoalog;
        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Skyline");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            FileDownloader.download_images(fileUrl, pdfFile);
            return pdfFile.getAbsolutePath();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDoalog = new ProgressDialog(Show_Jobs_Activity.this);


            progressDoalog.setMessage("Downloading please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            progressDoalog.dismiss();
            //  Toast.makeText(Show_Jobs_Activity.this, "File downloaded successfully !", Toast.LENGTH_SHORT).show();

            Toast.makeText(Show_Jobs_Activity.this, "File downloaded successfully !" +
                    "  " + "Location:" + path, Toast.LENGTH_SHORT).show();
        }


    }

    class DownloadFile extends AsyncTask<String, Void, String> {///this class make in adapter for downloading the pdf


        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Skyline");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {

                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return pdfFile.getAbsolutePath();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // showDialog(progress_bar_type);

            progressDoalog = new ProgressDialog(Show_Jobs_Activity.this);
            progressDoalog.setMessage("Downloading please wait...");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            progressDoalog.dismiss();
            Toast.makeText(Show_Jobs_Activity.this, "File downloaded successfully !" +
                    "  " + "Location:" + path, Toast.LENGTH_SHORT).show();
        }


    }

    public class order_adapter extends BaseAdapter {
        List<HashMap<String, String>> beanArrayList;
        Context context;
        int count = 1;

        public order_adapter(Context context, List<HashMap<String, String>> beanArrayList) {
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
            final String index1 = beanArrayList.get(i).get("index1");
            final String clientnme = beanArrayList.get(i).get("clientnme");
            final String txt_Job = beanArrayList.get(i).get("txt_Job");
            final String job_file_id = beanArrayList.get(i).get("int_job_file_id");
            final String job_id = beanArrayList.get(i).get("int_job_id");
            final String file_name1 = beanArrayList.get(i).get("var_file_name1");
            final String file_name = beanArrayList.get(i).get("var_file_name");
            final String file_des = beanArrayList.get(i).get("var_file_des");
            final String rndm_id_file = beanArrayList.get(i).get("int_rndm_id_file");
            final String file_Udate = beanArrayList.get(i).get("dt_file_Udate");

            if (convertview == null) {
                holder = new Holder();

                convertview = LayoutInflater.from(context).inflate(R.layout.jobfile_list_item, null);
              /*  boolean show_job_new = sp.getBoolean(Utility.SHOW_JOB_FILE_NEW, false);

                if (show_job_new) {
                    convertview = LayoutInflater.from(context).inflate(R.layout.row_job_files, null);
                } else {
                    convertview = LayoutInflater.from(context).inflate(R.layout.jobfile_list_item, null);
                }*/

                holder.index_no = (Button) convertview.findViewById(R.id.serial_no);
                holder.Source = (TextView) convertview.findViewById(R.id.source);
                holder.user_name = (TextView) convertview.findViewById(R.id.user);
                holder.file_upload = (TextView) convertview.findViewById(R.id.flie_upload);
                holder.date_upload = (TextView) convertview.findViewById(R.id.date_upload);
                holder.view_file = (Button) convertview.findViewById(R.id.view_file);
                holder.download_file = (Button) convertview.findViewById(R.id.download_file);
                //nks
                holder.row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);
                //nks

                convertview.setTag(holder);
            } else {
                holder = (Holder) convertview.getTag();
            }
            holder.index_no.setText(index1);
            holder.Source.setText(txt_Job);
            holder.user_name.setText(clientnme);
//nks
            //holder.file_upload.setText(file_name);
            holder.file_upload.setText(file_des);
            //nks
            holder.date_upload.setText(file_Udate);
            holder.view_file.setText("View");
            holder.download_file.setText("Download");

            holder.download_file.setOnClickListener(new View.OnClickListener() {    ///for download a pdf
                @Override
                public void onClick(View view) {

                    download_file(file_name1);


                }
            });


            holder.view_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    view_file(file_name1);


                }
            });


            holder.row_jobFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Show_Jobs_Activity.this);
                    LayoutInflater inflater = LayoutInflater.from(Show_Jobs_Activity.this);
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
                    title.setText("Please select the action!");
                    message.setText("Press Back to cancel!");
                    positiveBtn.setText("View");
                    negativeBtn.setText("Download");
                    negativeBtn.setVisibility(View.VISIBLE);
                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();

                            view_file(file_name1);

                        }
                    });
                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            download_file(file_name1);
                        }
                    });
                    alertDialog = dialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.setCancelable(true);
                    alertDialog.show();









                    /*new SweetAlertDialog(Show_Jobs_Activity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Please select the action !")
                            .setContentText("Press Back to cancel !")
                            .setConfirmText("View File")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    view_file(file_name1);
                                }
                            })
                            .setCancelText("Download File")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    download_file(file_name1);
                                }
                            })
                            .show();*/


                }
            });

            return convertview;
        }


        class Holder {
            TextView Source, user_name, file_upload, date_upload;

            //nks
            Button index_no;
            Button view_file,download_file;
            LinearLayout row_jobFile;

           //nks
        }


    }

    public void fettch_job_by_Client_list()//methord for job by client
    {

        int index = 1;
        count = 0;
        final String NAMESPACE = KEY_NAMESPACE+"";
        final String URL = URL_EP2+"/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE+API_Bind_Job_client;
        final String METHOD_NAME = API_Bind_Job_client;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("job", jobtxt_id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();
            if (recved.contains("No file Available.")) {
                count = 1;

            } else {
                String[] aa = recved.split("=");
                String a = aa[1];
                String b[] = a.split(";");
                String k = b[0];
                JSONObject jsonObject = new JSONObject(k);
                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    map1 = new HashMap<>();
                    map1.clear();
                    index = 1 + i;
                    String index1 = Integer.toString(index);
                    String clientnme = jsonObject1.getString("Source");
                    String txt_Job = jsonObject1.getString("txt_C_Name");
                    String int_job_file_id = jsonObject1.getString("INT_FID_client");
                    String int_job_id = jsonObject1.getString("job_id_client");
                    String var_file_name1 = jsonObject1.getString("TXT_Filename_client");
                    String var_file_name = jsonObject1.getString("DTR_SubmitDate_client");
                    String var_file_des = jsonObject1.getString("VCHAR_Heading_client");

                    map1.put("index1", index1);
                    map1.put("clientnme", clientnme);
                    map1.put("txt_Job", txt_Job);
                    map1.put("int_job_file_id", int_job_file_id);
                    map1.put("int_job_id", int_job_id);
                    map1.put("var_file_name1", var_file_name1);
                    map1.put("var_file_name", var_file_name);
                    map1.put("var_file_des", var_file_des);

                    data1.add(map1);


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class Show_client_Adapter extends BaseAdapter {
        List<HashMap<String, String>> beanArrayList;
        Context context;
        int count = 1;

        public Show_client_Adapter(Context context, List<HashMap<String, String>> beanArrayList) {
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
            final String index1 = beanArrayList.get(i).get("index1");
            final String clientnme = beanArrayList.get(i).get("clientnme");
            final String txt_Job = beanArrayList.get(i).get("txt_Job");
            final String job_file_id = beanArrayList.get(i).get("int_job_file_id");
            final String job_id = beanArrayList.get(i).get("int_job_id");
            final String file_name1 = beanArrayList.get(i).get("var_file_name1");
            final String file_name = beanArrayList.get(i).get("var_file_name");
            final String file_des = beanArrayList.get(i).get("var_file_des");


            if (convertview == null) {
                holder = new Holder();
                convertview = LayoutInflater.from(context).inflate(R.layout.jobfile_list_item, null);

                /*boolean show_job_new = sp.getBoolean(Utility.SHOW_JOB_FILE_NEW, false);

                if (show_job_new) {
                    convertview = LayoutInflater.from(context).inflate(R.layout.row_job_files, null);
                } else {
                    convertview = LayoutInflater.from(context).inflate(R.layout.jobfile_list_item, null);
                }*/

                holder.index_no = (Button) convertview.findViewById(R.id.serial_no);
                holder.Source = (TextView) convertview.findViewById(R.id.source);
                holder.user_name = (TextView) convertview.findViewById(R.id.user);
                holder.file_upload = (TextView) convertview.findViewById(R.id.flie_upload);
                holder.date_upload = (TextView) convertview.findViewById(R.id.date_upload);
                holder.view_file = (Button) convertview.findViewById(R.id.view_file);
                holder.download_file = (Button) convertview.findViewById(R.id.download_file);
                //
                //nks
                holder.row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);
                //nks

                //
                convertview.setTag(holder);
            } else {
                holder = (Holder) convertview.getTag();
            }
            holder.index_no.setText(index1);
            holder.Source.setText(clientnme);
            holder.user_name.setText(txt_Job);

            //nks
            // holder.file_upload.setText(file_name1);
            holder.file_upload.setText(file_des);
            //nks
            holder.date_upload.setText(file_name);
            holder.view_file.setText("View");
            holder.download_file.setText("Download");

            holder.download_file.setOnClickListener(new View.OnClickListener() {    ///for download a pdf
                @Override
                public void onClick(View view) {

                    download_file(file_name1);


                }
            });


            holder.view_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    view_file(file_name1);

                }
            });
            holder.row_jobFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Show_Jobs_Activity.this);
                    LayoutInflater inflater = LayoutInflater.from(Show_Jobs_Activity.this);
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
                    title.setText("Please select the action!");
                    message.setText("Press Back to cancel!");
                    positiveBtn.setText("View");
                    negativeBtn.setText("Download");
                    negativeBtn.setVisibility(View.VISIBLE);
                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();

                            view_file(file_name1);

                        }
                    });
                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            download_file(file_name1);
                        }
                    });
                    alertDialog = dialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.setCancelable(true);
                    alertDialog.show();










                    /*new SweetAlertDialog(Show_Jobs_Activity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Please select the action !")
                            .setContentText("Press Back to cancel !")
                            .setConfirmText("View File")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    view_file(file_name1);
                                }
                            })
                            .setCancelText("Download File")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    download_file(file_name1);
                                }
                            })
                            .show();*/


                }
            });


            return convertview;
        }


        class Holder {
            TextView Source, user_name, file_upload, date_upload;
            Button index_no;
            Button view_file,download_file;
            LinearLayout row_jobFile;
        }


    }

    //nks
    public void download_file(String file_name1) {
        /*if (file_name1.contains(".pdf") ||
                file_name1.contains(".doc") ||
                file_name1.contains(".txt")) {
            new DownloadFile().execute("http://exhibitpower2.com/upload/" + file_name1, file_name1);
        }*/
        if ((file_name1.contains(".jpg")) || (file_name1.contains(".JPG"))
                || (file_name1.contains(".jpeg")) || (file_name1.contains(".JPEG"))
                || (file_name1.contains(".png")) || (file_name1.contains(".PNG"))
                || (file_name1.contains(".bmp")) || (file_name1.contains(".BMP"))
                || (file_name1.contains(".gif")) || (file_name1.contains(".GIF"))
                || (file_name1.contains(".webp")) || (file_name1.contains(".WEBP"))
                ) {


            if (new ConnectionDetector(Show_Jobs_Activity.this).isConnectingToInternet()) {
                new Download_images().execute(URL_EP2+"/upload/" + file_name1, file_name1);

            } else {
                Toast.makeText(Show_Jobs_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }
        } else {
            // Toast.makeText(Show_Jobs_Activity.this, "No pdf file Avalable", Toast.LENGTH_SHORT).show();

            if (new ConnectionDetector(Show_Jobs_Activity.this).isConnectingToInternet()) {
                new DownloadFile().execute(URL_EP2+"/upload/" + file_name1, file_name1);

            } else {
                Toast.makeText(Show_Jobs_Activity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }





        }
    }

    public void view_file(String file_name1) {
        if ((file_name1.contains(".jpg")) || (file_name1.contains(".JPG"))
                || (file_name1.contains(".jpeg")) || (file_name1.contains(".JPEG"))
                || (file_name1.contains(".png")) || (file_name1.contains(".PNG"))
                || (file_name1.contains(".bmp")) || (file_name1.contains(".BMP"))
                || (file_name1.contains(".gif")) || (file_name1.contains(".GIF"))
                || (file_name1.contains(".webp")) || (file_name1.contains(".WEBP"))
                ) {

            final Dialog d = new Dialog(Show_Jobs_Activity.this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.popup);
            ImageView image = (ImageView) d.findViewById(R.id.popup_image);///for open a image
            ImageButton close = (ImageButton) d.findViewById(R.id.imageButton3);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            Picasso.with(Show_Jobs_Activity.this).load(URL_EP2+"/upload/" + file_name1).into(image);
            d.show();

        } else if ((file_name1.contains(".doc")) || (file_name1.contains(".DOC"))
                || (file_name1.contains(".psd")) || (file_name1.contains(".PSD"))
                || (file_name1.contains(".docx")) || (file_name1.contains(".DOCX"))
                || (file_name1.contains(".pdf")) || (file_name1.contains(".PDF"))
                ) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://docs.google.com/gview?embedded=true&url=http://www.exhibitpower2.com/upload/" + file_name1));
            startActivity(browserIntent);
        } else if ((file_name1.contains("..aac")) || (file_name1.contains(".DOC"))
                || (file_name1.contains(".m4a")) || (file_name1.contains(".M4A"))
                || (file_name1.contains(".mp4")) || (file_name1.contains(".MP4"))
                || (file_name1.contains(".3gp ")) || (file_name1.contains(".3GP"))
                || (file_name1.contains(".m4b")) || (file_name1.contains(".M4B"))
                || (file_name1.contains(".mp3 ")) || (file_name1.contains(".MP3"))
                || (file_name1.contains(".wave ")) || (file_name1.contains(".WAVE"))
                ) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_EP2+"/upload/" + file_name1));
            startActivity(browserIntent);
        } else {
            Toast.makeText(Show_Jobs_Activity.this, "Unrecognized file format ! Please download to view the file!", Toast.LENGTH_SHORT).show();
        }

    }


}
