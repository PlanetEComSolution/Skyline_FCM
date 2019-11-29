package planet.info.skyline.tech.job_files_new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import planet.info.skyline.model.PathProjectFile;
import planet.info.skyline.model.ProjectFileFolder;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.network.Api;
import planet.info.skyline.util.Utility;

import static android.content.Context.MODE_PRIVATE;
import static planet.info.skyline.network.SOAP_API_Client.KEY_NAMESPACE;
import static planet.info.skyline.network.SOAP_API_Client.URL_EP2;


public class ShowProjectFile_Fragment extends Fragment {
    View rootView;
    String jobtxt_id, tab;
    TextView tv_msg;
    AlertDialog alertDialog;
    SwipeRefreshLayout pullToRefresh;
    ArrayList<ProjectFileFolder> List_ProjectFileFolder = new ArrayList<>();
    String masterId = "0";
    SharedPreferences sp;

    ArrayList<PathProjectFile> List_Path = new ArrayList<>();
    private RecyclerView path_recyclerView, jobFiles_recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_project_files, container, false);

        jobFiles_recyclerView = (RecyclerView) rootView.findViewById(R.id.jobFiles_recyclerView);

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
            jobtxt_id = bundle.getString("JobId", "");
            tab = bundle.getString("tab", "");
        }

        path_recyclerView.setVisibility(View.VISIBLE);

        if (List_Path.isEmpty()) {
            List_Path.add(new PathProjectFile("Root >", "0"));
        }
        refreshAdapter();

        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
            new get_jobFiles_Acyntask().execute();
        } else {
            Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }


    public void fetch_projectfiles_FOLDER() {

        //  int index = 1;


      //  String userRole = sp.getString(Utility.LOGIN_USER_ROLE, "");
        String userRole  = Shared_Preference.getUSER_ROLE(getActivity());
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


      //  String dealerId = sp.getString(Utility.DEALER_ID, "");
        String dealerId = Shared_Preference.getDEALER_ID(getActivity());
     //   String compID = sp.getString(Utility.COMPANY_ID_BILLABLE, "");
        String compID =   Shared_Preference.getCOMPANY_ID_BILLABLE(getActivity());
        final String NAMESPACE = KEY_NAMESPACE ;
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

            // Project Files
            List_ProjectFileFolder.clear();
            fetch_projectfiles_FOLDER();
            fetch_projectfiles_FILES();

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

            if (List_ProjectFileFolder.size() < 1) {
                tv_msg.setVisibility(View.VISIBLE);
            } else {
                tv_msg.setVisibility(View.GONE);
            }

            final ProjectFilesAdapter projectFilesAdapter = new ProjectFilesAdapter(getActivity(), List_ProjectFileFolder);
            jobFiles_recyclerView.setAdapter(projectFilesAdapter);


            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set VERTICAL Orientation
            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return projectFilesAdapter.getItemViewType(position);

                }
            });
            jobFiles_recyclerView.setLayoutManager(mLayoutManager);


            if (pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(false);
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

      /*  @Override
        public int getItemViewType(int position) {


            String isFolder = listProjectFiles.get(position).getIsFOLDER();
            if (isFolder.equals("1")) {
                return 1;

            } else
                return 2;
        }*/

        @Override
        public int getItemViewType(int position) {
            int v =2;
            try {
                String isFolder = listProjectFiles.get(position).getIsFOLDER();
                if (isFolder.equals("1")) {
                    // return 1;
                    v = 1;
                } else
                   // return 2;
                v = 2;
            } catch (Exception e) {
                e.getMessage();
            }
            return v;
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


}
