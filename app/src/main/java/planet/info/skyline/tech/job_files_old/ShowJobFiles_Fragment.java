package planet.info.skyline.tech.job_files_old;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.tech.fullscreenview.FullscreenImageView;
import planet.info.skyline.tech.job_files_new.ProjectFileDetailActivityNonClient;
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.PathProjectFile;
import planet.info.skyline.model.ProjectFileFolder;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.network.Api;
import planet.info.skyline.util.FileDownloader;
import planet.info.skyline.util.Utility;

import static android.content.Context.MODE_PRIVATE;
import static planet.info.skyline.network.Api.API_Bind_Job_client11;

import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class ShowJobFiles_Fragment extends Fragment {


    View rootView;
    String jobtxt_id, tab;
    TextView tv_msg;

    ArrayList<HashMap<String, String>> List_JobFiles = new ArrayList<>();
    HashMap<String, String> map;

    ArrayList<HashMap<String, String>> List_ClientFiles = new ArrayList<>();
    HashMap<String, String> map1;
    AlertDialog alertDialog;
    SwipeRefreshLayout pullToRefresh;

    ArrayList<ProjectFileFolder> List_ProjectFileFolder = new ArrayList<>();

    String masterId = "0";
    // String FileId = "0";
    SharedPreferences sp;

    ArrayList<PathProjectFile> List_Path = new ArrayList<>();
    private RecyclerView path_recyclerView, jobFiles_recyclerView;
    // ListView jobs_list_View;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fragment__show_job_files, container, false);
        //jobs_list_View = (ListView) rootView.findViewById(R.id.cart_listview);

        jobFiles_recyclerView = (RecyclerView) rootView.findViewById(R.id.jobFiles_recyclerView);
        jobFiles_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        jobFiles_recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        sp = getActivity().getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);

        path_recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        path_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        path_recyclerView.setItemAnimator(new DefaultItemAnimator());

        pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                masterId = List_Path.get(List_Path.size() - 1).getMasterId();
                if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                    new get_jobFiles_Acyntask().execute();
                } else {
                    Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
                // your code
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            jobtxt_id = bundle.getString("jobtxt_id", "");
            tab = bundle.getString("tab", "");
        }
        if (tab.equals("4")) {
            path_recyclerView.setVisibility(View.VISIBLE);
        } else {
            path_recyclerView.setVisibility(View.GONE);
        }

        List_Path.add(new PathProjectFile("Root >", "0"));
        refreshAdapter();

        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
            new get_jobFiles_Acyntask().execute();
        } else {
            Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }

    public void fettch_job_list() {
        List_JobFiles = new ArrayList<>();
        int index = 1;
        int count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";

        final String METHOD_NAME =Api. API_GetJobFileByTextJob11;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
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

    public void fettch_job_by_Client_list()//methord for job by client
    {
        List_ClientFiles = new ArrayList<>();

        int index = 1;
        //  count = 0;
        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String SOAP_ACTION = KEY_NAMESPACE + API_Bind_Job_client11;
        final String METHOD_NAME = API_Bind_Job_client11;
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
                //   count = 1;

            } else {
                recved = recved.substring(recved.indexOf("=") + 1, recved.lastIndexOf(";"));
                JSONObject jsonObject = new JSONObject(recved);


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
                    List_ClientFiles.add(map1);

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

    public void fetch_projectfiles_FOLDER() {

        //  int index = 1;


       // String userRole = sp.getString(Utility.LOGIN_USER_ROLE, "");
        final String userRole = Shared_Preference.getUSER_ROLE(getActivity());
       // String dealerId = sp.getString(Utility.DEALER_ID, "");
        String dealerId = Shared_Preference.getDEALER_ID(getActivity());

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String METHOD_NAME = Api.API_FETCH_PROJECTFILE_FOLDER;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Job_Id", jobtxt_id);
        request.addProperty("masterID", masterId);
        request.addProperty("CateID", userRole);
        request.addProperty("Dealer_ID", dealerId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);


            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String receivedString = SoapPrimitiveresult.toString();
            Log.e("Folder", receivedString);
            JSONObject jsonObject = new JSONObject(receivedString);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String dealerID = jsonObject1.getString("dealerID");
                String file_ID_PK = jsonObject1.getString("file_ID_PK");
                String file_cat = jsonObject1.getString("file_cat");
                String file_status = jsonObject1.getString("file_status");
                String file_submit_date = jsonObject1.getString("file_submit_date");
                String masterID = jsonObject1.getString("masterID");
                String parentID = jsonObject1.getString("parentID");
                String path = jsonObject1.getString("path");
                String JobName = jsonObject1.getString("JobName");
                String whoDelete = jsonObject1.getString("whoDelete");
                String job_id = jsonObject1.getString("job_id");
                String GoogleID = jsonObject1.getString("GoogleID");
                String PGoogleID = jsonObject1.getString("PGoogleID");


                ProjectFileFolder projectFileFolder =
                        new ProjectFileFolder("", "", "", "", "", "",
                                "", "", "", "", "", "", "",
                                "", "", "", "", "", "", "",
                                "", "", "", "", "",
                                dealerID, file_ID_PK, file_cat, file_status,
                                file_submit_date, masterID, parentID, path,
                                JobName, whoDelete, job_id, GoogleID,
                                PGoogleID, "1");

                List_ProjectFileFolder.add(projectFileFolder);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void fetch_projectfiles_FILES() {

       // String userRole = sp.getString(Utility.LOGIN_USER_ROLE, "");
       // String dealerId = sp.getString(Utility.DEALER_ID, "");
        String dealerId = Shared_Preference.getDEALER_ID(getActivity());
      //  String compID = sp.getString(Utility.COMPANY_ID_BILLABLE, "");
        String compID =   Shared_Preference.getCOMPANY_ID_BILLABLE(getActivity());

        final String NAMESPACE = KEY_NAMESPACE + "";
        final String URL = URL_EP2 + "/WebService/techlogin_service.asmx";
        final String METHOD_NAME = Api.API_FETCH_PROJECTFILE;
        final String SOAP_ACTION = KEY_NAMESPACE + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("Job_id", jobtxt_id);
        request.addProperty("clientID", compID);
        request.addProperty("id_pk", "0");
        request.addProperty("masterID", masterId);
        request.addProperty("file_status", "");
        request.addProperty("DealerID", dealerId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive SoapPrimitiveresult = (SoapPrimitive) envelope.getResponse();
            String receivedString = SoapPrimitiveresult.toString();
            Log.e("Files", receivedString);
            JSONObject jsonObject = new JSONObject(receivedString);
            JSONArray jsonArray = jsonObject.getJSONArray("cds");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                String id_pk = jsonObject1.getString("id_pk");
                String Job_id = jsonObject1.getString("Job_id");
                String clientID = jsonObject1.getString("clientID");
                String GoogleID = jsonObject1.getString("GoogleID");
                String OriginalFilename = jsonObject1.getString("OriginalFilename");
                String CreatedDate = jsonObject1.getString("CreatedDate");
                String ModifyDate = jsonObject1.getString("ModifyDate");
                String status = jsonObject1.getString("status");
                String Description = jsonObject1.getString("Description");
                String masterfolderID = jsonObject1.getString("masterfolderID");
                String parentfolderID = jsonObject1.getString("parentfolderID");
                String path = jsonObject1.getString("path");
                String uploadedBy = jsonObject1.getString("uploadedBy");
                String txt_job = jsonObject1.getString("txt_job");
                String txt_C_Name = jsonObject1.getString("txt_C_Name");
                String Internal_Status = jsonObject1.getString("Internal_Status");
                String LastActionDate = jsonObject1.getString("LastActionDate");
                String Client_Status = jsonObject1.getString("Client_Status");
                String UploadbyName = jsonObject1.getString("UploadbyName");
                String iconLink = jsonObject1.getString("iconLink");
                String FileType = jsonObject1.getString("FileType");
                String ApprovalRequired = jsonObject1.getString("ApprovalRequired");
                String PGoogleID = jsonObject1.getString("PGoogleID");
                String degree = jsonObject1.getString("degree");
                String d1 = jsonObject1.getString("d1");


                ProjectFileFolder projectFileFolder =
                        new ProjectFileFolder(
                                id_pk,
                                Job_id,
                                clientID,
                                GoogleID,
                                OriginalFilename,
                                CreatedDate,
                                ModifyDate,
                                status,
                                Description,
                                masterfolderID,
                                parentfolderID,
                                path,
                                uploadedBy,
                                txt_job,
                                txt_C_Name,
                                Internal_Status,
                                LastActionDate,
                                Client_Status,
                                UploadbyName,
                                iconLink,
                                FileType,
                                ApprovalRequired,
                                PGoogleID,
                                degree,
                                d1,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "", "0");

                List_ProjectFileFolder.add(projectFileFolder);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void refreshAdapter() {

        PathAdapter mAdapter = new PathAdapter(List_Path);
        path_recyclerView.setAdapter(mAdapter);


    }

    private void refreshPath(int position, List<PathProjectFile> _pathList) {
        if (position < _pathList.size()) {
            List<PathProjectFile> tempPathList = new ArrayList<>();
            for (int i = 0; i <= position; i++) {
                tempPathList.add(_pathList.get(i));
            }
            List_Path.clear();
            List_Path.addAll(tempPathList);
        }

    }

    class get_jobFiles_Acyntask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDoalog;

        @Override
        protected Void doInBackground(Void... params) {
            if (tab.equalsIgnoreCase("0") || tab.equalsIgnoreCase("1") || tab.equalsIgnoreCase("3")) {
                // 0=Job Files  ,1= Client Art or Project Photos,3=Project photos

                fettch_job_list();


            } else if (tab.equalsIgnoreCase("2")) {
                // Client Files
                fettch_job_by_Client_list();
            } else if (tab.equalsIgnoreCase("4")) {
                // Project Files
                List_ProjectFileFolder.clear();
                fetch_projectfiles_FOLDER();
                fetch_projectfiles_FILES();
            }
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


            if (tab.equalsIgnoreCase("0") || tab.equalsIgnoreCase("1") || tab.equalsIgnoreCase("3")) {
                if (List_JobFiles.size() < 1) {
                    tv_msg.setVisibility(View.VISIBLE);
                } else {
                    tv_msg.setVisibility(View.GONE);
                }

                ArrayList<HashMap<String, String>> JobfilesList = new ArrayList<>();
                ArrayList<HashMap<String, String>> ProofRendersList = new ArrayList<>();
                ArrayList<HashMap<String, String>> ProjectPhotosList = new ArrayList<>();

                for (int i = 0; i < List_JobFiles.size(); i++) {
                    HashMap<String, String> hashMap = List_JobFiles.get(i);
                    String fileType = hashMap.get("ftype");
                    if (fileType.equals("Project Photo(s)")) {
                        ProjectPhotosList.add(hashMap);
                    } else if (fileType.equals("Client Art")) {
                        ProofRendersList.add(hashMap);
                    } else {
                        JobfilesList.add(hashMap);
                    }
                }
                if (tab.equalsIgnoreCase("0")) {

                    JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(getActivity(), JobfilesList);
                    jobFiles_recyclerView.setAdapter(jobFilesAdapter);
                } else if (tab.equalsIgnoreCase("1")) {
                    JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(getActivity(), ProofRendersList);
                    jobFiles_recyclerView.setAdapter(jobFilesAdapter);
                } else if (tab.equalsIgnoreCase("3")) {
                    JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(getActivity(), ProjectPhotosList);
                    jobFiles_recyclerView.setAdapter(jobFilesAdapter);
                }


                //      JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(getActivity(), List_JobFiles);
                //     jobFiles_recyclerView.setAdapter(jobFilesAdapter);

            } else if (tab.equalsIgnoreCase("2")) {
                if (List_ClientFiles.size() < 1) {
                    tv_msg.setVisibility(View.VISIBLE);
                } else {
                    tv_msg.setVisibility(View.GONE);
                }

                ClientFilesAdapter clientFilesAdapter = new ClientFilesAdapter(getActivity(), List_ClientFiles);
                jobFiles_recyclerView.setAdapter(clientFilesAdapter);


            } else if (tab.equalsIgnoreCase("4")) {
                if (List_ProjectFileFolder.size() < 1) {
                    tv_msg.setVisibility(View.VISIBLE);
                } else {
                    tv_msg.setVisibility(View.GONE);
                }

                final ProjectFilesAdapter projectFilesAdapter = new ProjectFilesAdapter(getActivity(), List_ProjectFileFolder);
                jobFiles_recyclerView.setAdapter(projectFilesAdapter);


                GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
                mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return projectFilesAdapter.getItemViewType(position);

                    }
                });
                jobFiles_recyclerView.setLayoutManager(mLayoutManager);


            }
            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
            }
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
            final String isSelected = beanArrayList.get(i).get("isSelected");


            if (convertview == null) {
                holder = new Holder();

                convertview = LayoutInflater.from(context).inflate(R.layout.row_job_files_new, null);
                holder.index_no = (Button) convertview.findViewById(R.id.serial_no);
                holder.Source = (TextView) convertview.findViewById(R.id.source);
                holder.user_name = (TextView) convertview.findViewById(R.id.user);
                holder.file_upload = (TextView) convertview.findViewById(R.id.flie_upload);
                holder.date_upload = (TextView) convertview.findViewById(R.id.date_upload);
                holder.view_file = (Button) convertview.findViewById(R.id.view_file);
                holder.download_file = (Button) convertview.findViewById(R.id.download_file);
                holder.row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);
                holder.checkBox = (CheckBox) convertview.findViewById(R.id.checkBox);


                convertview.setTag(holder);
            } else {
                holder = (Holder) convertview.getTag();
            }
            holder.index_no.setText(index1);

            if (txt_Job.equals(null) || txt_Job.trim().equals("")) {
                holder.Source.setText("Not available");
            } else {
                holder.Source.setText(txt_Job);

                if (txt_Job.equalsIgnoreCase("Project Photo(s)")) {
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

                    beanArrayList.set(getPosition, hashMap);
                    ((Show_Jobs_Activity_New) getActivity()).setProjectPhotoList(beanArrayList);


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

                   /* new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
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
            Button view_file, download_file;
            LinearLayout row_jobFile;
            CheckBox checkBox;

            //nks
        }


    }

    public class Show_client_Adapter extends BaseAdapter {
        List<HashMap<String, String>> beanArrayList;
        Context context;


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
                convertview = LayoutInflater.from(context).inflate(R.layout.row_job_files_new, null);


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
                holder.checkBox = (CheckBox) convertview.findViewById(R.id.checkBox);
                holder.checkBox.setVisibility(View.GONE);
                //
                convertview.setTag(holder);
            } else {
                holder = (Holder) convertview.getTag();
            }
            holder.index_no.setText(index1);
            if (clientnme.equals(null) || clientnme.trim().equals("")) {
                holder.Source.setText("Not available");
            } else {
                holder.Source.setText(clientnme);
            }
            if (txt_Job.equals(null) || txt_Job.trim().equals("")) {
                holder.user_name.setText("Not available");
            } else {
                holder.user_name.setText(txt_Job);
            }
            //nks
            // holder.file_upload.setText(file_name1);
            if (file_des.equals(null) || file_des.trim().equals("")) {
                holder.file_upload.setText("Not available");
            } else {
                holder.file_upload.setText(file_des.trim());
            }

            if (file_name.equals(null) || file_name.trim().equals("")) {
                holder.date_upload.setText("Not available");
            } else {
                holder.date_upload.setText(file_name);
            }
            //nks

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

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
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
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();




                    /*new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
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
            Button view_file, download_file;
            LinearLayout row_jobFile;
            CheckBox checkBox;
        }


    }

    public class ProjectFiles_adapter extends BaseAdapter {
        List<ProjectFileFolder> listProjectFiles;
        Context context;


        public ProjectFiles_adapter(Context context, List<ProjectFileFolder> listProjectFiles) {
            this.context = context;
            this.listProjectFiles = listProjectFiles;

        }

        @Override
        public int getCount() {
            return listProjectFiles.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            final Holder holder;


            if (view == null) {
                holder = new Holder();

                view = LayoutInflater.from(context).inflate(R.layout.row_tech_project_file, null);

                holder.index_no = (Button) view.findViewById(R.id.serial_no);
                holder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                holder.tv_file_name = (TextView) view.findViewById(R.id.tv_file_name);
                holder.tv_job_name = (TextView) view.findViewById(R.id.tv_job_name);
                holder.tv_dated = (TextView) view.findViewById(R.id.tv_dated);
                holder.tv_status = (TextView) view.findViewById(R.id.tv_status);
                holder.parentView = (LinearLayout) view.findViewById(R.id.parentView);
                holder.spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
                holder.tv_comp_name = (TextView) view.findViewById(R.id.tv_comp_name);
                holder.tv_internal_status = (TextView) view.findViewById(R.id.tv_internal_status);
                holder.ll_file = (LinearLayout) view.findViewById(R.id.ll_file);
                holder.tv_folder_name = (TextView) view.findViewById(R.id.tv_folder_name);

                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }


            holder.index_no.setText(String.valueOf(i + 1));
            final String isFolder = listProjectFiles.get(i).getIsFOLDER();

            if (isFolder.equals("1")) {   // FOLDER
                holder.thumbnail.setImageResource(R.drawable.folder_1);
                holder.tv_folder_name.setText(listProjectFiles.get(i).getFOLDER_fileCat());
                holder.ll_file.setVisibility(View.GONE);
                holder.spinner.setVisibility(View.GONE);
                holder.tv_folder_name.setVisibility(View.VISIBLE);

            } else {                // FILE
                holder.ll_file.setVisibility(View.VISIBLE);

                holder.spinner.setVisibility(View.VISIBLE);
                holder.tv_folder_name.setVisibility(View.GONE);
                String FileName = listProjectFiles.get(i).getFILE_originalFilename();

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


                if (isImage) {

                    String url = "https://drive.google.com/thumbnail?id=" + listProjectFiles.get(i).getFILE_googleID();

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

            }

            try {
                holder.tv_file_name.setText(listProjectFiles.get(i).getFILE_originalFilename());
                holder.tv_job_name.setText(listProjectFiles.get(i).getFILE_txtJob());
                holder.tv_dated.setText(listProjectFiles.get(i).getFILE_modifyDate());
                holder.tv_status.setText(listProjectFiles.get(i).getFILE_clientStatus());
                holder.tv_internal_status.setText(listProjectFiles.get(i).getFILE_internalStatus());
                holder.tv_comp_name.setText(listProjectFiles.get(i).getFILE_txtCName());


                String Client_Status = listProjectFiles.get(i).getFILE_clientStatus();
                if (Client_Status.equalsIgnoreCase("Rejected")) {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.red));
                } else if (Client_Status.equalsIgnoreCase("Approved")) {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.main_green_color));
                } else if (Client_Status.equalsIgnoreCase("Snoozed")) {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.snoozed));
                } else {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.main_orange_light_color));
                }


                String internalStatus = listProjectFiles.get(i).getFILE_internalStatus();
                if (internalStatus.equalsIgnoreCase("Rejected")) {
                    holder.tv_internal_status.setTextColor(getResources().getColor(R.color.red));
                } else if (internalStatus.equalsIgnoreCase("Approved")) {
                    holder.tv_internal_status.setTextColor(getResources().getColor(R.color.main_green_color));
                } else if (internalStatus.equalsIgnoreCase("Snoozed")) {
                    holder.tv_internal_status.setTextColor(getResources().getColor(R.color.snoozed));
                } else {
                    holder.tv_internal_status.setTextColor(getResources().getColor(R.color.main_orange_light_color));
                }


            } catch (Exception e) {
                e.getMessage();
            }
            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    masterId = listProjectFiles.get(i).getFOLDER_fileIDPK();
                    //FileId = listProjectFiles.get(i).getFOLDER_fileIDPK();
                    String folderName = listProjectFiles.get(i).getFOLDER_fileCat();
                    if (isFolder.equals("1")) {   // FOLDER
                        showFolderData(folderName);

                    } else {
                        /*String url = "https://drive.google.com/thumbnail?id=" + listProjectFiles.get(i).getFILE_googleID();
                        String filename = listProjectFiles.get(i).getFILE_originalFilename();
                        Intent i = new Intent(getActivity(), FullscreenWebViewNew.class);
                        i.putExtra("url", url);
                        i.putExtra("FileName", filename);
                        startActivity(i);*/


                        String FileId = listProjectFiles.get(i).getFILE_idPk();
                        String ImgName = listProjectFiles.get(i).getFILE_originalFilename();
                        String jobID = listProjectFiles.get(i).getFILE_jobId();
                        String googleId = listProjectFiles.get(i).getFILE_googleID();


                        Intent intent = new Intent(context, ProjectFileDetailActivityNonClient.class);
                        intent.putExtra("FileId", FileId);
                        intent.putExtra("FileName", ImgName);
                        intent.putExtra("jobID", jobID);
                        intent.putExtra("googleId", googleId);
                        intent.putExtra("masterId", listProjectFiles.get(i).getFILE_masterfolderID());

                        startActivity(intent);


                    }
                }
            });


            return view;
        }

        private void showFolderData(String folderName) {

            try {
                List_Path.add(new PathProjectFile(folderName + " >", masterId));
                refreshAdapter();
            } catch (Exception e) {
                e.getMessage();
            }

            if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                new get_jobFiles_Acyntask().execute();
            } else {
                Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
            }
        }

        class Holder {
            TextView tv_file_name, tv_job_name, tv_dated, tv_status, tv_folder_name;
            ImageView thumbnail;
            Button index_no;
            LinearLayout parentView, ll_file;
            ProgressBar spinner;
            TextView tv_comp_name, tv_internal_status;
            //nks
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

    public class PathAdapter extends RecyclerView.Adapter<PathAdapter.BookViewHolder> {

        private List<PathProjectFile> pathList;

        public PathAdapter(List<PathProjectFile> pathList) {
            this.pathList = pathList;
        }

        @Override
        public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.project_file_path, parent, false);

            return new BookViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BookViewHolder holder, final int position) {
            holder.path.setText(pathList.get(position).getName());
            holder.path.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    masterId = pathList.get(position).getMasterId();
                    if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                        new get_jobFiles_Acyntask().execute();
                    } else {
                        Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    }

                    refreshPath(position, pathList);
                    refreshAdapter();


                }
            });
        }

        @Override
        public int getItemCount() {
            return pathList.size();
        }

        public class BookViewHolder extends RecyclerView.ViewHolder {

            public TextView path;

            public BookViewHolder(View view) {
                super(view);

                path = (TextView) view.findViewById(R.id.tv_path);
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

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int i) {


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


            holder.index_no.setText(String.valueOf(i+1));

            if (txt_Job.equals(null) || txt_Job.trim().equals("")) {
                holder.Source.setText("Not available");
            } else {
                holder.Source.setText(txt_Job);

                if (txt_Job.equalsIgnoreCase("Project Photo(s)")) {
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

                    beanArrayList.set(getPosition, hashMap);
                    ((Show_Jobs_Activity_New) getActivity()).setProjectPhotoList(beanArrayList);


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

                   /* new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
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

        }

        @Override
        public int getItemCount() {
            return beanArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Source, user_name, file_upload, date_upload;

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

            }
        }
    }

    public class ProjectFilesAdapter extends RecyclerView.Adapter<ProjectFilesAdapter.MyViewHolder> {

        List<ProjectFileFolder> listProjectFiles;
        Context context;


        public ProjectFilesAdapter(Context context, List<ProjectFileFolder> listProjectFiles) {
            this.context = context;
            this.listProjectFiles = listProjectFiles;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_tech_project_file, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int i) {


            holder.index_no.setText(String.valueOf(i + 1));
            final String isFolder = listProjectFiles.get(i).getIsFOLDER();

            if (isFolder.equals("1")) {   // FOLDER
                holder.thumbnail.setImageResource(R.drawable.folder_icon);
                holder.tv_folder_name.setText(listProjectFiles.get(i).getFOLDER_fileCat());
                holder.ll_file.setVisibility(View.GONE);
                holder.spinner.setVisibility(View.GONE);
                holder.tv_folder_name.setVisibility(View.VISIBLE);

            } else {                // FILE
                holder.ll_file.setVisibility(View.VISIBLE);

                holder.spinner.setVisibility(View.VISIBLE);
                holder.tv_folder_name.setVisibility(View.GONE);
                String FileName = listProjectFiles.get(i).getFILE_originalFilename();

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


                if (isImage) {

                    String url = "https://drive.google.com/thumbnail?id=" + listProjectFiles.get(i).getFILE_googleID();

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

            }

            try {
                holder.tv_file_name.setText(listProjectFiles.get(i).getFILE_originalFilename());
                holder.tv_job_name.setText(listProjectFiles.get(i).getFILE_txtJob());
                holder.tv_dated.setText(listProjectFiles.get(i).getFILE_modifyDate());
                holder.tv_status.setText(listProjectFiles.get(i).getFILE_clientStatus());
                holder.tv_internal_status.setText(listProjectFiles.get(i).getFILE_internalStatus());
                holder.tv_comp_name.setText(listProjectFiles.get(i).getFILE_txtCName());


                String Client_Status = listProjectFiles.get(i).getFILE_clientStatus();
                if (Client_Status.equalsIgnoreCase("Rejected")) {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.red));
                } else if (Client_Status.equalsIgnoreCase("Approved")) {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.main_green_color));
                } else if (Client_Status.equalsIgnoreCase("Snoozed")) {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.snoozed));
                } else {
                    holder.tv_status.setTextColor(getResources().getColor(R.color.main_orange_light_color));
                }


                String internalStatus = listProjectFiles.get(i).getFILE_internalStatus();
                if (internalStatus.equalsIgnoreCase("Rejected")) {
                    holder.tv_internal_status.setTextColor(getResources().getColor(R.color.red));
                } else if (internalStatus.equalsIgnoreCase("Approved")) {
                    holder.tv_internal_status.setTextColor(getResources().getColor(R.color.main_green_color));
                } else if (internalStatus.equalsIgnoreCase("Snoozed")) {
                    holder.tv_internal_status.setTextColor(getResources().getColor(R.color.snoozed));
                } else {
                    holder.tv_internal_status.setTextColor(getResources().getColor(R.color.main_orange_light_color));
                }


            } catch (Exception e) {
                e.getMessage();
            }
            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    masterId = listProjectFiles.get(i).getFOLDER_fileIDPK();
                    //FileId = listProjectFiles.get(i).getFOLDER_fileIDPK();
                    String folderName = listProjectFiles.get(i).getFOLDER_fileCat();
                    if (isFolder.equals("1")) {   // FOLDER
                        showFolderData(folderName);

                    } else {
                        /*String url = "https://drive.google.com/thumbnail?id=" + listProjectFiles.get(i).getFILE_googleID();
                        String filename = listProjectFiles.get(i).getFILE_originalFilename();
                        Intent i = new Intent(getActivity(), FullscreenWebViewNew.class);
                        i.putExtra("url", url);
                        i.putExtra("FileName", filename);
                        startActivity(i);*/


                        String FileId = listProjectFiles.get(i).getFILE_idPk();
                        String ImgName = listProjectFiles.get(i).getFILE_originalFilename();
                        String jobID = listProjectFiles.get(i).getFILE_jobId();
                        String googleId = listProjectFiles.get(i).getFILE_googleID();


                        Intent intent = new Intent(context, ProjectFileDetailActivityNonClient.class);
                        intent.putExtra("FileId", FileId);
                        intent.putExtra("FileName", ImgName);
                        intent.putExtra("jobID", jobID);
                        intent.putExtra("googleId", googleId);
                        intent.putExtra("masterId", listProjectFiles.get(i).getFILE_masterfolderID());

                        startActivity(intent);


                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return listProjectFiles.size();
        }

        @Override
        public int getItemViewType(int position) {
            String isFolder = listProjectFiles.get(position).getIsFOLDER();
            if (isFolder.equals("1")) {
                return 1;
            } else
                return 2;
        }

        private void showFolderData(String folderName) {
            try {
                List_Path.add(new PathProjectFile(folderName + " >", masterId));
                refreshAdapter();
            } catch (Exception e) {
                e.getMessage();
            }

            if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                new get_jobFiles_Acyntask().execute();
            } else {
                Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_file_name, tv_job_name, tv_dated, tv_status, tv_folder_name;
            ImageView thumbnail;
            Button index_no;
            LinearLayout parentView, ll_file;
            ProgressBar spinner;
            TextView tv_comp_name, tv_internal_status;

            public MyViewHolder(View view) {
                super(view);

                index_no = (Button) view.findViewById(R.id.serial_no);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                tv_file_name = (TextView) view.findViewById(R.id.tv_file_name);
                tv_job_name = (TextView) view.findViewById(R.id.tv_job_name);
                tv_dated = (TextView) view.findViewById(R.id.tv_dated);
                tv_status = (TextView) view.findViewById(R.id.tv_status);
                parentView = (LinearLayout) view.findViewById(R.id.parentView);
                spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
                tv_comp_name = (TextView) view.findViewById(R.id.tv_comp_name);
                tv_internal_status = (TextView) view.findViewById(R.id.tv_internal_status);
                ll_file = (LinearLayout) view.findViewById(R.id.ll_file);
                tv_folder_name = (TextView) view.findViewById(R.id.tv_folder_name);


            }
        }
    }

    public class ClientFilesAdapter extends RecyclerView.Adapter<ClientFilesAdapter.MyViewHolder> {
        List<HashMap<String, String>> beanArrayList;
        Context context;

        public ClientFilesAdapter(Context context, List<HashMap<String, String>> beanArrayList) {
            this.context = context;
            this.beanArrayList = beanArrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_job_files_new, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int i) {


            final String index1 = beanArrayList.get(i).get("index1");
            final String clientnme = beanArrayList.get(i).get("clientnme");
            final String txt_Job = beanArrayList.get(i).get("txt_Job");
            final String job_file_id = beanArrayList.get(i).get("int_job_file_id");
            final String job_id = beanArrayList.get(i).get("int_job_id");
            final String file_name1 = beanArrayList.get(i).get("var_file_name1");
            final String file_name = beanArrayList.get(i).get("var_file_name");
            final String file_des = beanArrayList.get(i).get("var_file_des");


            holder.index_no.setText(String.valueOf(i+1));
            holder.checkBox.setVisibility(View.GONE);
            if (clientnme.equals(null) || clientnme.trim().equals("")) {
                holder.Source.setText("Not available");
            } else {
                holder.Source.setText(clientnme);
            }
            if (txt_Job.equals(null) || txt_Job.trim().equals("")) {
                holder.user_name.setText("Not available");
            } else {
                holder.user_name.setText(txt_Job);
            }
            //nks
            // holder.file_upload.setText(file_name1);
            if (file_des.equals(null) || file_des.trim().equals("")) {
                holder.file_upload.setText("Not available");
            } else {
                holder.file_upload.setText(file_des.trim());
            }

            if (file_name.equals(null) || file_name.trim().equals("")) {
                holder.date_upload.setText("Not available");
            } else {
                holder.date_upload.setText(file_name);
            }
            //nks

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

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
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
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();




                    /*new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
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

        }

        @Override
        public int getItemCount() {
            return beanArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Source, user_name, file_upload, date_upload;
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


            }
        }
    }

}
