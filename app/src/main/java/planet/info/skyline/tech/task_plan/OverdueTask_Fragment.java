package planet.info.skyline.tech.task_plan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.crash_report.ConnectionDetector;
import planet.info.skyline.model.TaskPlan;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.REST_API_Client_TaskPlan;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;


public class OverdueTask_Fragment extends Fragment {


    View rootView;
    String tab;
    TextView tv_msg;
    List<TaskPlan> list_TaskPlan = new ArrayList<>();


    AlertDialog alertDialog;
    SwipeRefreshLayout pullToRefresh;
    SharedPreferences sp;

    private RecyclerView jobFiles_recyclerView;

    private ProgressDialog progressDialog;

    String job_id, swo_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_task_plan, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        jobFiles_recyclerView = (RecyclerView) rootView.findViewById(R.id.jobFiles_recyclerView);
        jobFiles_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        jobFiles_recyclerView.setItemAnimator(new DefaultItemAnimator());

        tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        sp = getActivity().getApplicationContext().getSharedPreferences("skyline", MODE_PRIVATE);

        pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setEnabled(false);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                    //      new get_jobFiles_Acyntask().execute();
                } else {
                    Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }

            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            job_id = bundle.getString("job_id", "");
            swo_id = bundle.getString("swo_id", "");

            tab = bundle.getString("tab", "");
        }


        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
            getTaskPlanList();
        } else {
            Toast.makeText(getActivity(), Utility.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }

        //  setTempData();

        return rootView;
    }

/*    private void setTempData() {

        list_TaskPlan.clear();

        list_TaskPlan.add(new TaskPlan("Dept SWO - Task 1", "Dept SWO - Task 1.Dept SWO - Task 1.Dept SWO - Task 1", "1/3", "01/1/2019", "06/14/2019", "30 min", "06/14/2019", "tech tech","test user"));
        list_TaskPlan.add(new TaskPlan("Dept SWO - Task 2", "Dept SWO - Task 2", "1/3", "05/21/2019", "02/08/2019", "30 min", "12/21/2019", "tech tech","test user"));
        list_TaskPlan.add(new TaskPlan("Dept SWO - Task 3", "Dept SWO - Task 3", "1/3", "07/14/2019", "01/05/2019", "30 min", "11/05/2019", "tech tech","test user"));
        list_TaskPlan.add(new TaskPlan("Dept SWO - Task 4", "Dept SWO - Task 4", "1/3", "08/05/2019", "11/11/2019", "30 min", "06/04/2019", "tech tech","test user"));
        list_TaskPlan.add(new TaskPlan("Dept SWO - Task 5", "Dept SWO - Task 5", "1/3", "08/02/2019", "02/14/2019", "30 min", "08/21/2019", "tech tech","test user"));
        list_TaskPlan.add(new TaskPlan("Dept SWO - Task 5", "Dept SWO - Task 5", "1/3", "05/12/2019", "04/25/2019", "30 min", "08/31/2019", "tech tech","test user"));
        list_TaskPlan.add(new TaskPlan("Dept SWO - Task 6", "Dept SWO - Task 6", "1/3", "08/12/2019", "05/11/2019", "30 min", "04/06/2019", "tech tech","test user"));
        list_TaskPlan.add(new TaskPlan("Dept SWO - Task 7", "Dept SWO - Task 7", "1/3", "05/04/2019", "02/12/2019", "30 min", "07/15/2019", "tech tech","test user"));
        list_TaskPlan.add(new TaskPlan("Dept SWO - Task 8", "Dept SWO - Task 8", "1/3", "01/17/2019", "03/21/2019", "30 min", "06/22/2019", "tech tech","test user"));
        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(getActivity(), list_TaskPlan);
        jobFiles_recyclerView.setAdapter(jobFilesAdapter);


    }*/

    private void getTaskPlanList() {
        list_TaskPlan.clear();
        showDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("JOB_ID_PK", job_id);
        hashMap.put("UserID", Shared_Preference.getLOGIN_USER_ID(getActivity()));
        hashMap.put("DepartmentID", "0");
        hashMap.put("IsSwo", "2");
        hashMap.put("SWOId", swo_id);
        API_Interface apiService = REST_API_Client_TaskPlan.getClient().create(API_Interface.class);

        apiService.TaskPlanList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TaskPlan>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                        String error = e.getMessage();
                        Toast.makeText(getActivity(),
                                "Network Error....", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<TaskPlan> taskPlan) {

                        hideDialog();
                        String currentDate = Utility.getCurrentdate("yyyy-MM-dd");
                        if (taskPlan != null && !taskPlan.isEmpty()) {
                            for (TaskPlan taskPlan1 : taskPlan) {
                                String deadlineDate = taskPlan1.getDeadlineDate();
                                boolean NotExpired = Utility.isToDateafterFromDate(currentDate, deadlineDate, "yyyy-MM-dd");
                                boolean Expired = Utility.isToDatebeforeFromDate(currentDate, deadlineDate, "yyyy-MM-dd");
                                boolean SameDate = false;
                                if (!NotExpired && !Expired) SameDate = true;

                                if (Expired) {
                                    list_TaskPlan.add(taskPlan1);
                                }
                            }

                        }
                        hideDialog();

                        if (list_TaskPlan.isEmpty()) {
                            tv_msg.setVisibility(View.VISIBLE);
                        } else {
                            tv_msg.setVisibility(View.GONE);
                        }


                        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(getActivity(), list_TaskPlan);
                        jobFiles_recyclerView.setAdapter(jobFilesAdapter);
                        // String status = login_model.getStatus();


                    }
                });


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

    public class JobFilesAdapter extends RecyclerView.Adapter<JobFilesAdapter.MyViewHolder> {

        List<TaskPlan> List_taskPlans;
        Context context;


        public JobFilesAdapter(Context context, List<TaskPlan> taskPlans) {
            this.context = context;
            this.List_taskPlans = taskPlans;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_incomplete_task_1, parent, false);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int i) {


            final String checklist = List_taskPlans.get(i).getSelectedchklst() + "/" + List_taskPlans.get(i).getTotalchklst();
            final String completedDate = List_taskPlans.get(i).getComletedDate1();
            final String deadlineDate = List_taskPlans.get(i).getDeadlineDate();
            final String description = String.valueOf(List_taskPlans.get(i).getDescription());
            final String startBy = List_taskPlans.get(i).getStartDate();
            final String task = List_taskPlans.get(i).getTask();
            final String timeBudget = List_taskPlans.get(i).getTimeBudget();
            final String technician = List_taskPlans.get(i).getAssignUser();
            final String completedBy = List_taskPlans.get(i).getCName();
            final String taskID = List_taskPlans.get(i).getDTaskID();
            final String ID = List_taskPlans.get(i).getIDPK();
            final String AssignUser = List_taskPlans.get(i).getAssignUser();
            holder.index_no.setText(String.valueOf(i + 1));


            if (checklist == null || checklist.trim().equals("")) {
                holder.Checklist.setText("Not available");
            } else {
                holder.Checklist.setText(checklist);

            }

          /* if (completedDate.equals(null) || completedDate.trim().equals("")) {
                holder.Co.setText("Not available");
            } else {
                holder.user_name.setText(clientnme);
            }*/

            String day_ = Utility.getDayFromDate(deadlineDate, "yyyy-MM-dd");
            if (day_ != null) {

                holder.day.setText(day_);
            }

            String currentDate = Utility.getCurrentdate("yyyy-MM-dd");


            boolean NotExpired = Utility.isToDateafterFromDate(currentDate, deadlineDate, "yyyy-MM-dd");
            boolean Expired = Utility.isToDatebeforeFromDate(currentDate, deadlineDate, "yyyy-MM-dd");
            boolean SameDate = false;
            if (!NotExpired && !Expired) SameDate = true;

            if (Expired) {
                holder.view1.setBackgroundColor(getResources().getColor(R.color.red_material));
            } else if (NotExpired) {
                holder.view1.setBackgroundColor(getResources().getColor(R.color.yellow_material));
            } else if (SameDate) {
                holder.view1.setBackgroundColor(getResources().getColor(R.color.orange_material));
                holder.day.setText("Today");
            }


            if (deadlineDate == null || deadlineDate.trim().equals("")) {
                holder.deadline_date.setText("Not available");
            } else {
                holder.deadline_date.setText(Utility.ChangeDateFormat("yyyy-MM-dd", "dd MMM yyyy", deadlineDate.trim()));
            }


            if (description == null || description.trim().equals("")) {
                holder.Description.setText("Not available");
            } else {
                holder.Description.setText(Utility.TrimString(description, 25));
            }

            if (startBy == null || startBy.trim().equals("")) {
                holder.Start_By.setText("Not available");
            } else {
                holder.Start_By.setText(Utility.ChangeDateFormat("yyyy-MM-dd", "dd MMM yyyy", startBy.trim()));
            }
            if (task == null || task.trim().equals("")) {
                holder.Task.setText("Not available");
            } else {
                holder.Task.setText(Utility.TrimString(task, 25));
            }
            if (timeBudget == null || timeBudget.trim().equals("")) {
                holder.Time_Budget.setText("Not available");
            } else {
                holder.Time_Budget.setText(timeBudget);
            }


            holder.row_jobFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getActivity(), CompleteTaskActivity.class);
                    i.putExtra("checklist", checklist);
                    i.putExtra("completedDate", completedDate);
                    i.putExtra("deadlineDate", deadlineDate);
                    i.putExtra("description", description);
                    i.putExtra("startBy", startBy);
                    i.putExtra("task", task);
                    i.putExtra("timeBudget", timeBudget);
                    i.putExtra("technician", technician);
                    i.putExtra("taskID", taskID);
                    i.putExtra("job_id", job_id);
                    i.putExtra("id", ID);
                    i.putExtra("AssignUser", AssignUser);
                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return List_taskPlans.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Task, Description, Checklist, deadline_date, Time_Budget, Start_By;

            Button index_no;
            LinearLayout row_jobFile;
            View view1;
            TextView day;

            public MyViewHolder(View convertview) {
                super(convertview);

                index_no = (Button) convertview.findViewById(R.id.serial_no);
                Task = (TextView) convertview.findViewById(R.id.Task);
                Description = (TextView) convertview.findViewById(R.id.Description);
                Checklist = (TextView) convertview.findViewById(R.id.Checklist);
                deadline_date = (TextView) convertview.findViewById(R.id.deadline_date);
                Time_Budget = (TextView) convertview.findViewById(R.id.Time_Budget);
                Start_By = (TextView) convertview.findViewById(R.id.Start_By);
                row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);
                view1 = convertview.findViewById(R.id.view1);
                day = convertview.findViewById(R.id.day);
            }
        }
    }


}
