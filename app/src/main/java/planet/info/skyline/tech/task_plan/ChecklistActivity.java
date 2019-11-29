package planet.info.skyline.tech.task_plan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.Checklist;
import planet.info.skyline.network.SOAP_API_Client;
import planet.info.skyline.util.Utility;


public class ChecklistActivity extends AppCompatActivity {


    // We don't use namespaces
    //https://developer.android.com/training/basics/network-ops/xml
    private static final String ns = null;
    //    View rootView;
    String jobtxt_id, tab;
    TextView tv_msg;
    List<Checklist> list_Checklists = new ArrayList<>();
    Button btn_Add, btn_Cancel;
    AlertDialog alertDialog;
    //SwipeRefreshLayout pullToRefresh;
    SharedPreferences sp;
    String updatable = "";
    Integer request_code = 0;
    String job_id;
    String task_id;
    String id;
    private RecyclerView Checklist_recyclerView;

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        setTitle(Utility.getTitle("Checklist"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIntentData();
        setView();
        APIGetCheckList();


    }

    private void setView() {

        Checklist_recyclerView = (RecyclerView) findViewById(R.id.Checklist_recyclerView);
        Checklist_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Checklist_recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView) findViewById(R.id.tv_msg);

        btn_Add = findViewById(R.id.btn_Add);
        btn_Cancel = findViewById(R.id.btn_Cancel);

        if (updatable.equals("1")) {
            btn_Add.setVisibility(View.VISIBLE);
        } else {
            btn_Add.setVisibility(View.GONE);
        }

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // jobtxt_id = bundle.getString("jobtxt_id", "");
            // tab = bundle.getString("tab", "");
        }
    }

    private void getIntentData() {

        Bundle bundle = getIntent().getExtras();


        try {
            if (bundle != null) {
                if (bundle.containsKey("Updatable")) {
                    updatable = bundle.getString("Updatable");
                }
                if (bundle.containsKey("RequestCode")) {
                    request_code = bundle.getInt("RequestCode");

                }

                job_id = bundle.getString("job_id");
                task_id = bundle.getString("task_id");
                id = bundle.getString("id");


            }
        } catch (Exception e) {
            e.getMessage();
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

    private void APIGetCheckList() {

        String url = SOAP_API_Client.URL_EP2 + "/ep6/api/TaskPlanning/GetMasterCheckList?tid=" + task_id + "&Complexity=0&jobID=" + job_id + "&ttID=" + id;
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //convertXMLtoJSON(response);
                list_Checklists.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String Col1 = jsonObject.getString("Col1");
                            String Col2 = jsonObject.getString("Col2");
                            String Col3 = jsonObject.getString("Col3");
                            String Col4 = jsonObject.getString("Col4");
                            String Col5 = jsonObject.getString("Col5");

                            list_Checklists.add(new Checklist(Col1, Col2, Col3, Col4, Col5));


                        }

                    }

                } catch (Exception e) {
                    e.getMessage();
                }

                JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(ChecklistActivity.this);
                Checklist_recyclerView.setAdapter(jobFilesAdapter);


                if (list_Checklists.isEmpty()) {
                    tv_msg.setVisibility(View.VISIBLE);
                } else {
                    tv_msg.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("err", "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }


    public class JobFilesAdapter extends RecyclerView.Adapter<JobFilesAdapter.MyViewHolder> {

      //  List<Checklist> list_Checklists;
        Context context;


        public JobFilesAdapter(Context context){//, List<Checklist> Checklists) {
            this.context = context;
          //  this.list_Checklists = Checklists;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_checklist_2, parent, false);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int i) {


            final String Name = list_Checklists.get(i).getName();
            final String _ischecked = list_Checklists.get(i).getIsChecked();

            boolean ischecked = false;
            if (_ischecked.equals("1")) {
                ischecked = true;
            } else {
                ischecked = false;
            }

            holder.index_no.setText(String.valueOf(i + 1));


            if (Name == null || Name.trim().equals("")) {
                holder.Name.setText("Not available");
            } else {
                holder.Name.setText(Name);

            }

            if (!updatable.equals("1")) {
                holder.chkbox.setEnabled(false);
            } else {
                holder.chkbox.setEnabled(true);
            }


            holder.chkbox.setChecked(ischecked);
            if (ischecked) {

                holder.Name.setForeground(context.getResources().getDrawable(R.drawable.strike_through));

                //  holder.Name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                //   holder.Name.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
                holder.Name.setForeground(null);

            }


            holder.chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {
                        list_Checklists.get(i).setIsChecked("1");
                    } else {
                        list_Checklists.get(i).setIsChecked("0");
                    }


                    if (b) {

                        holder.Name.setForeground(context.getResources().getDrawable(R.drawable.strike_through));

                        //  holder.Name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        //   holder.Name.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
                        holder.Name.setForeground(null);

                    }


                    Intent intent = new Intent();
                    if(getCheckedCount()== list_Checklists.size()){
                        intent.putExtra(Utility.IS_CHECKLIST_DONE, true);
                        setResult(request_code, intent);
                        finish();//finishing activity
                    }





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
            return list_Checklists.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Name;
            CheckBox chkbox;
            Button index_no;
            LinearLayout row_jobFile;

            public MyViewHolder(View convertview) {
                super(convertview);

                index_no = (Button) convertview.findViewById(R.id.serial_no);
                Name = (TextView) convertview.findViewById(R.id.Name);
                chkbox = convertview.findViewById(R.id.chkbox);

                row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);

            }
        }
    }


    private int getCheckedCount(){
        int total = 0;
        for (int i = 0; i < list_Checklists.size(); i++) {

            if (list_Checklists.get(i).getIsChecked().equals("1")) {
                total = total + 1;
            }
        }
       /* if (total == list_Checklists.size()) {
            Intent intent = new Intent();
            intent.putExtra(Utility.IS_CHECKLIST_DONE, true);
            setResult(request_code, intent);
            finish();//finishing activity
        }*/
        return total;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if(getCheckedCount()== list_Checklists.size()){
            intent.putExtra(Utility.IS_CHECKLIST_DONE, true);

        }else {
            intent.putExtra(Utility.IS_CHECKLIST_DONE, false);
        }
        setResult(request_code, intent);
        finish();//finishing activity
    }
}
