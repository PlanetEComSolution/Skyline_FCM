package planet.info.skyline.tech.job_files_new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.network.Api;
import planet.info.skyline.util.FileDownloader;
import planet.info.skyline.util.Utility;

import static android.content.Context.MODE_PRIVATE;
//import static planet.info.skyline.network.Api.API_GetJobFileByTextJob11;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class ProjectPhotos_Fragment extends Fragment {


    View rootView;
    String jobtxt_id, tab;
    TextView tv_msg;
    List<HashMap<String, String>> list_ProjectPhotos = new ArrayList<>();

    ArrayList<HashMap<String, String>> List_JobFiles = new ArrayList<>();
    HashMap<String, String> map;


    AlertDialog alertDialog;
    SwipeRefreshLayout pullToRefresh;


    SharedPreferences sp;


    private RecyclerView jobFiles_recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_job_files, container, false);


        jobFiles_recyclerView = (RecyclerView) rootView.findViewById(R.id.jobFiles_recyclerView);
        jobFiles_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        jobFiles_recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        sp = getActivity().getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);

        pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                    new get_jobFiles_Acyntask().execute();
                } else {
                    Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }

            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            jobtxt_id = bundle.getString("JobId", "");
            tab = bundle.getString("tab", "");
        }


        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
            new get_jobFiles_Acyntask().execute();
        } else {
            Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }
    public void fettch_job_list_New() {
        List_JobFiles.clear();
        list_ProjectPhotos.clear();
        int index = 1;
        int count = 0;
       // String User_Role=sp.getString(Utility.LOGIN_USER_ROLE,"");
        final String User_Role = Shared_Preference.getUSER_ROLE(getActivity());
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";

        final String METHOD_NAME = Api.API_GetJobFileByTextJob11withrole;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("job", jobtxt_id);
        request.addProperty("Role", User_Role);


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
                recved = recved.substring(recved.indexOf("=") + 1, recved.lastIndexOf(";"));
                JSONObject jsonObject = new JSONObject(recved);

                JSONArray jsonArray = jsonObject.getJSONArray("cds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
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
                        String ftype = jsonObject1.getString("ftype");
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
                        map.put("isSelected", "false");
                        map.put("ftype", ftype);
                        List_JobFiles.add(map);
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void download_file(String file_name1) {
        if ((file_name1.contains(".jpg")) || (file_name1.contains(".JPG"))
                || (file_name1.contains(".jpeg")) || (file_name1.contains(".JPEG"))
                || (file_name1.contains(".png")) || (file_name1.contains(".PNG"))
                || (file_name1.contains(".bmp")) || (file_name1.contains(".BMP"))
                || (file_name1.contains(".gif")) || (file_name1.contains(".GIF"))
                || (file_name1.contains(".webp")) || (file_name1.contains(".WEBP"))
        ) {
            if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                new Download_images().execute(URL_EP2 + "/upload/" + file_name1, file_name1);
            } else {
                Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Toast.makeText(Show_Jobs_Activity.this, "No pdf file Avalable", Toast.LENGTH_SHORT).show();
            if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                new DownloadFile().execute(URL_EP2 + "/upload/" + file_name1, file_name1);
            } else {
                Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
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
            Intent i = new Intent(getActivity(), FullscreenImageView.class);
            i.putExtra("url", URL_EP2 + "/upload/" + file_name1);
            startActivity(i);


        } else if ((file_name1.contains(".doc")) || (file_name1.contains(".DOC"))
                || (file_name1.contains(".psd")) || (file_name1.contains(".PSD"))
                || (file_name1.contains(".docx")) || (file_name1.contains(".DOCX"))
                || (file_name1.contains(".pdf")) || (file_name1.contains(".PDF"))
        ) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://docs.google.com/gview?embedded=true&url=" + URL_EP2 + "/upload/" + file_name1));
            startActivity(browserIntent);
        } else if ((file_name1.contains(".aac")) || (file_name1.contains(".AAC"))
                || (file_name1.contains(".m4a")) || (file_name1.contains(".M4A"))
                || (file_name1.contains(".mp4")) || (file_name1.contains(".MP4"))
                || (file_name1.contains(".3gp")) || (file_name1.contains(".3GP"))
                || (file_name1.contains(".m4b")) || (file_name1.contains(".M4B"))
                || (file_name1.contains(".mp3")) || (file_name1.contains(".MP3"))
                || (file_name1.contains(".wave")) || (file_name1.contains(".WAVE"))
        ) {
            /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://exhibitpower2.com/upload/" + file_name1));
            startActivity(browserIntent);*/
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_EP2 + "/upload/" + file_name1));
            intent.setDataAndType(Uri.parse(URL_EP2 + "/upload/" + file_name1), "video/*");
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Unrecognized file format ! Please download to view the file!", Toast.LENGTH_SHORT).show();
        }
    }

    class get_jobFiles_Acyntask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDoalog;

        @Override
        protected Void doInBackground(Void... params) {

           // fettch_job_list();
            fettch_job_list_New();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDoalog = new ProgressDialog(getActivity());
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(false);
            progressDoalog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                progressDoalog.dismiss();
            } catch (Exception e) {
                e.getMessage();
            }



            ArrayList<HashMap<String, String>> ProjectPhotosList = new ArrayList<>();

            for (int i = 0; i < List_JobFiles.size(); i++) {
                HashMap<String, String> hashMap = List_JobFiles.get(i);
                String fileType = hashMap.get("ftype");
                if (fileType.equals("Project Photo(s)")) {
                    ProjectPhotosList.add(hashMap);
                }
            }

            // 0=Job Files  ,
            // 1= Client Art or Project Photos,
            // 3=Project photos


                if (ProjectPhotosList.size() < 1) {
                    tv_msg.setVisibility(View.VISIBLE);
                } else {
                    tv_msg.setVisibility(View.GONE);
                }




                JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(getActivity(), ProjectPhotosList);
                jobFiles_recyclerView.setAdapter(jobFilesAdapter);



            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
        }
    }

    class Download_images extends AsyncTask<String, Void, String> {///this class make in adapter for downloading the images

        ProgressDialog progressDoalog;

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];



            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Exhibit Power");

            if (!folder.exists()) {
                folder.mkdir();
            }

            File folder1 = new File(folder, "Download");
            if (!folder1.exists()) {
                folder1.mkdir();
            }


            File pdfFile = new File(folder1, fileName);
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
            progressDoalog = new ProgressDialog(getActivity());


            progressDoalog.setMessage("Downloading please wait....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            progressDoalog.dismiss();
            //  Toast.makeText(Show_Jobs_Activity.this, "File downloaded successfully !", Toast.LENGTH_SHORT).show();

            try {
                Toast.makeText(getActivity(), "File downloaded successfully !" +

                        "  " + "Location:" + path, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.getMessage();
            }
        }

    }

    class DownloadFile extends AsyncTask<String, Void, String> {///this class make in adapter for downloading the pdf
        ProgressDialog progressDoalog;

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            /**/

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Exhibit Power");

            if (!folder.exists()) {
                folder.mkdir();
            }

            File folder1 = new File(folder, "Download");
            if (!folder1.exists()) {
                folder1.mkdir();
            }


            if (fileName.contains("/")) {
                fileName = fileName.substring(fileName.indexOf("/") + 1);
            }
            File pdfFile = new File(folder1, fileName);
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            FileDownloader.downloadFile(fileUrl, pdfFile);


            /**/
           /* String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Skyline");
            folder.mkdir();
            File pdfFile = new File(folder, fileName);
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);*/
            return folder1.getAbsolutePath();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(progress_bar_type);

            progressDoalog = new ProgressDialog(getActivity());
            progressDoalog.setMessage("Downloading please wait...");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            progressDoalog.dismiss();
            try {
                Toast.makeText(getActivity(), "File downloaded successfully !" +
                        "  " + "Location:" + path, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.getMessage();
            }
        }


    }

    public class JobFilesAdapter extends RecyclerView.Adapter<JobFilesAdapter.MyViewHolder> {

        List<HashMap<String, String>> beanArrayList;
        Context context;


        public JobFilesAdapter(Context context, List<HashMap<String, String>> beanArrayList) {
            this.context = context;
            this.beanArrayList = beanArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_job_files_new, parent, false);
            /*View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_job_files_new_1, parent, false);*/


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int i) {


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
            final String isSelected = beanArrayList.get(i).get("isSelected");
            final String ftype = beanArrayList.get(i).get("ftype");

            holder.index_no.setText(String.valueOf(i + 1));


          /*  String FileName = file_name1;
            String fileExt = "";
            if (FileName.contains(".")) {
                fileExt = FileName.substring(FileName.lastIndexOf("."));
            }

            boolean isImage = Arrays.asList(Utility.imgExt).contains(fileExt);
            boolean isDoc = Arrays.asList(Utility.docExt).contains(fileExt);
            boolean isMedia = Arrays.asList(Utility.mediaExt).contains(fileExt);
            boolean isWord = Arrays.asList(Utility.wordExt).contains(fileExt);
            boolean isPdf = Arrays.asList(Utility.pdfExt).contains(fileExt);
            boolean isExcel = Arrays.asList(Utility.excelExt).contains(fileExt);
            boolean isText = Arrays.asList(Utility.txtExt).contains(fileExt);

            final String url;
            if (isImage) {
                url = URL_EP2 + "/upload/" + file_name1;
            } else {
                url = "http://docs.google.com/gview?embedded=true&url=" + URL_EP2 + "/upload/" + file_name1;
            }
            if (isImage) {
                Glide.with(getActivity()).load(url).listener(new RequestListener<Drawable>() {
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
            } else if (isWord) {
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


            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), FullscreenImageView.class);
                    i.putExtra("url", url);
                    startActivity(i);
                }
            });*/


            if (txt_Job.equals(null) || txt_Job.trim().equals("")) {
                holder.Source.setText("Not available");
            } else {
                holder.Source.setText(txt_Job);

                if (ftype.equalsIgnoreCase("Project Photo(s)")
                        ||
                        ftype.equalsIgnoreCase("Client Art")

                ) {
                    holder.checkBox.setVisibility(View.VISIBLE);
                } else {
                    holder.checkBox.setVisibility(View.INVISIBLE);
                }
            }

            if (clientnme.equals(null) || clientnme.trim().equals("")) {
                holder.user_name.setText("Not available");
            } else {
                holder.user_name.setText(clientnme);
            }


            holder.checkBox.setTag(i);
            if (isSelected.equalsIgnoreCase("true")) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag(); // Here

                    HashMap<String, String> hashMap = beanArrayList.get(getPosition);
                    if (isChecked) {
                        hashMap.put("isSelected", "true");
                    } else {
                        hashMap.put("isSelected", "false");
                    }

                    //   beanArrayList.set(getPosition, hashMap);
                    //    setProjectPhotoList(beanArrayList);

                    ((JobFilesTabActivity)getActivity()).addProjectPhotos(hashMap);
                    list_ProjectPhotos.add(hashMap);

                }
            });

//nks

            if (file_des.equals(null) || file_des.trim().equals("")) {
                holder.file_upload.setText("Not available");
            } else {
                holder.file_upload.setText(file_des.trim());
            }

            //holder.file_upload.setText(file_name);
            //nks

            if (file_Udate.equals(null) || file_Udate.trim().equals("")) {
                holder.date_upload.setText("Not available");
            } else {
                holder.date_upload.setText(file_Udate);
            }

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


                }
            });

        }

        @Override
        public int getItemCount() {
            return beanArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Source, user_name, file_upload, date_upload;
          //  ProgressBar spinner;
         //   ImageView thumbnail;
            //nks
            Button index_no;
            Button view_file, download_file;
            LinearLayout row_jobFile;
            CheckBox checkBox;

            public MyViewHolder(View convertview) {
                super(convertview);

                index_no = (Button) convertview.findViewById(R.id.serial_no);
                Source = (TextView) convertview.findViewById(R.id.source);
                user_name = (TextView) convertview.findViewById(R.id.user);
                file_upload = (TextView) convertview.findViewById(R.id.flie_upload);
                date_upload = (TextView) convertview.findViewById(R.id.date_upload);
                view_file = (Button) convertview.findViewById(R.id.view_file);
                download_file = (Button) convertview.findViewById(R.id.download_file);
                row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);
                checkBox = (CheckBox) convertview.findViewById(R.id.checkBox);
               // thumbnail = (ImageView) convertview.findViewById(R.id.thumbnail);
              //  spinner = (ProgressBar) convertview.findViewById(R.id.progressBar1);
            }
        }
    }


}
