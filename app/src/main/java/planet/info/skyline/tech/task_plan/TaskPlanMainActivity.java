package planet.info.skyline.tech.task_plan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;*/
import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.SWO;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.REST_API_Client_TaskPlan;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;
import retrofit2.Response;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TaskPlanMainActivity extends AppCompatActivity {
    TextView tv_msg,tv_first_msg;
    ArrayList<SWO> list_Swo = new ArrayList<>();

    String compID = "";
    String jobID = "";
    String company_Name = "";
    String job_Name = "";
    private RecyclerView recyclerView;
   // private MoviesAdapter mAdapter;
    private Menu menu;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_plan_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Utility.getTitle("SWO(s) List"));
        setView();
        final boolean TIMER_STARTED_FROM_BILLABLE_MODULE = Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(this);
        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            compID = Shared_Preference.getCOMPANY_ID_BILLABLE(this);
            jobID = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
            company_Name = Shared_Preference.getCLIENT_NAME(this);
            job_Name = Shared_Preference.getJOB_NAME_BILLABLE(this);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();


        if(jobID.equals("")){
            tv_first_msg.setVisibility(View.VISIBLE);
        }else {
            tv_first_msg.setVisibility(View.GONE);
            if (new ConnectionDetector(TaskPlanMainActivity.this).isConnectingToInternet()) {
                GetSwoByJob();
            } else {
                Toast.makeText(TaskPlanMainActivity.this, Utility.NO_INTERNET, Toast.LENGTH_LONG).show();
            }

        }



    }

    private void setView() {

        progressDialog = new ProgressDialog(TaskPlanMainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_msg = findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);

        tv_first_msg = findViewById(R.id.tv_first_msg);
        tv_first_msg.setVisibility(View.GONE);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TaskPlanMainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void GetSwoByJob() {

        showDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("JOB_ID_PK", jobID);

        API_Interface apiService = REST_API_Client_TaskPlan.getClient().create(API_Interface.class);

        apiService.GetSwoByJob(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SWO>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                        String error = e.getMessage();
                        Toast.makeText(TaskPlanMainActivity.this,
                                "Network Error....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<SWO> swoList) {
                        list_Swo.clear();
                        updateMenuTitles();
                        if (swoList != null && !swoList.isEmpty()) {
                            list_Swo.addAll(swoList);
                        }

                        hideDialog();

                        if (list_Swo.isEmpty()) {
                            tv_msg.setVisibility(View.VISIBLE);

                        } else {
                            tv_msg.setVisibility(View.GONE);
                        }

                        MoviesAdapter jobFilesAdapter = new MoviesAdapter(TaskPlanMainActivity.this, list_Swo);
                        recyclerView.setAdapter(jobFilesAdapter);
                        // String status = login_model.getStatus();


                    }
                });


    }

   /* private void GetSwoByJob1() {

      showDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("JOB_ID_PK", jobID);

        API_Interface apiService = REST_API_Client_TaskPlan.getClient().create(API_Interface.class);
 *//*
        apiService.GetSwoByJob(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<SWO>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onSuccess(Response<List<SWO>> listResponse) {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                });

*//*


        Single<Response<List<SWO>>> testObservable= apiService.GetSwoByJob1(hashMap);
        testObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<SWO>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onSuccess(Response<List<SWO>> listResponse) {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                });






    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.choose:
                ///  Check_Clock_Status();

                Intent i = new Intent(TaskPlanMainActivity.this, SelectCompanyActivity.class);
                i.putExtra(Utility.IS_JOB_MANDATORY,"1");
                i.putExtra(Utility.Show_DIALOG_SHOW_INFO,false);
                startActivityForResult(i, Utility.CODE_SELECT_COMPANY);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usage_report, menu);
        // _menu = menu;
        this.menu = menu;
        return true;
    }

    private void updateMenuTitles() {

        try {
            MenuItem bedMenuItem = menu.findItem(R.id.choose);

            if (!company_Name.equals("")) {
                if (!job_Name.equals("")) {
                    bedMenuItem.setTitle(company_Name + "\n" + job_Name);
                } else {
                    bedMenuItem.setTitle(company_Name);
                }
            } else {
                bedMenuItem.setTitle(getApplicationContext().getResources().getString(R.string.Select_Job));
            }

        } catch (Exception e) {
            e.getCause();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utility.CODE_SELECT_COMPANY) {

            if (resultCode == Activity.RESULT_OK) {
                try {
                    compID = data.getStringExtra("CompID");
                    jobID = data.getStringExtra("JobID");
                    company_Name = data.getStringExtra("CompName");
                    job_Name = data.getStringExtra("JobName");


                    onResume();
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "Exception caught!", Toast.LENGTH_SHORT).show();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                finish();
            }
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

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
        private List<SWO> moviesList;
        //   private HttpImageManager mHttpImageManager;

        public MoviesAdapter(Activity context, List<SWO> moviesList) {
            this.moviesList = moviesList;
            //     mHttpImageManager = ((AppController) context.getApplication()).getHttpImageManager();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_swo, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final SWO swo = moviesList.get(position);
            holder.index_no.setText(String.valueOf(position + 1));


            holder.swo.setText(swo.getSwoName());
            holder.Job_Desc.setText(swo.getDescription());
            holder.due_date.setText(swo.getServiceDueDate());


            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TaskPlanMainActivity.this, TaskPlanTabActivity.class);
                    intent.putExtra("job_id",jobID);
                    intent.putExtra("swo_id", swo.getIDPK());
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView swo,
                    Job_Desc,
                    due_date;


            Button index_no;
            LinearLayout parentView;

            public MyViewHolder(View convertview) {
                super(convertview);

                parentView = convertview.findViewById(R.id.row_jobFile);
                index_no = (Button) convertview.findViewById(R.id.serial_no);

                swo = (TextView) convertview.findViewById(R.id.swo);
                Job_Desc = (TextView) convertview.findViewById(R.id.Job_Desc);
                due_date = (TextView) convertview.findViewById(R.id.due_date);
            }
        }
    }

}
