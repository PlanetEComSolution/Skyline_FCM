package planet.info.skyline.tech.task_plan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.Technician;
import planet.info.skyline.util.Utility;

public class CompletedTaskDetailActivity extends AppCompatActivity {
    TextView Task, Description, Checklist,
            deadline_date, Time_Budget, Start_By, Completed_date, Completed_by;
//CardView cardview_technician;


    ImageView cardview_linked;
    String checklist;
    String completedDate;
    String deadlineDate;
    String description;
    String startBy;
    String task;
    String timeBudget;
    String technician, completedBy;
    String taskID;
    LinearLayout ll_checklist;
    RecyclerView tech_recyclerView;
    List<Technician> list_techs = new ArrayList<>();
    String job_id;
    String id, AssignUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task_detail_2);
        setTitle(Utility.getTitle("Completed Task"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setView();
        getIntentData();
        getTechnicians();
       // setRecyclerTempData();
    }

    private void setView() {

        Task = (TextView) findViewById(R.id.Task);
        Description = (TextView) findViewById(R.id.Description);
        Checklist = (TextView) findViewById(R.id.Checklist);
        deadline_date = (TextView) findViewById(R.id.deadline_date);
        Time_Budget = (TextView) findViewById(R.id.Time_Budget);
        Start_By = (TextView) findViewById(R.id.Start_By);
        ll_checklist = findViewById(R.id.ll_checklist);
        Completed_date = findViewById(R.id.Completed_date);
        Completed_by = findViewById(R.id.Completed_by);

        tech_recyclerView = (RecyclerView) findViewById(R.id.tech_recyclerView);
        tech_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tech_recyclerView.setItemAnimator(new DefaultItemAnimator());

        cardview_linked = findViewById(R.id.cardview_linked);
        cardview_linked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CompletedTaskDetailActivity.this, AttachmentListActivity.class);
                i.putExtra("taskID", taskID);


                startActivity(i);
            }
        });

        ll_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CompletedTaskDetailActivity.this, ChecklistActivity.class);

                i.putExtra("Updatable", "0");
                i.putExtra("job_id", job_id);
                i.putExtra("task_id", taskID);
                i.putExtra("id", id);

                startActivity(i);
            }
        });
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();


        try {

            if (bundle != null) {
                checklist = bundle.getString("checklist", "");
                completedDate = bundle.getString("completedDate", "");
                deadlineDate = bundle.getString("deadlineDate", "");
                description = bundle.getString("description", "");
                startBy = bundle.getString("startBy", "");
                task = bundle.getString("task", "");
                timeBudget = bundle.getString("timeBudget", "");
                technician = bundle.getString("technician", "");
                completedBy = bundle.getString("completedBy", "");
                taskID = bundle.getString("taskID", "");
                job_id = bundle.getString("job_id", "");
                id = bundle.getString("id", "");
                AssignUser = bundle.getString("AssignUser", "");
            }


            if (checklist == null || checklist.trim().equals("")) {
                Checklist.setText("Not available");
            } else {
                Checklist.setText(checklist);

            }
            if (deadlineDate == null || deadlineDate.trim().equals("")) {
                deadline_date.setText("Not available");
            } else {
                deadline_date.setText(Utility.ChangeDateFormat("MM/dd/yyyy", "dd MMM yyyy", deadlineDate.trim()));
            }


            if (description == null || description.trim().equals("")) {
                Description.setText("Not available");
            } else {
                Description.setText(description);
            }

            if (startBy == null || startBy.trim().equals("")) {
                Start_By.setText("Not available");
            } else {
                Start_By.setText(Utility.ChangeDateFormat("MM/dd/yyyy", "dd MMM yyyy", startBy.trim()));
            }
            if (task == null || task.trim().equals("")) {
                Task.setText("Not available");
            } else {
                Task.setText(task);
            }
            if (timeBudget == null || timeBudget.trim().equals("")) {
                Time_Budget.setText("Not available");
            } else {
                Time_Budget.setText(timeBudget);
            }

            if (timeBudget == null || timeBudget.trim().equals("")) {
                Time_Budget.setText("Not available");
            } else {
                Time_Budget.setText(timeBudget);
            }
            if (completedDate == null || completedDate.trim().equals("")) {
                Completed_date.setText("Not available");
            } else {
                Completed_date.setText(completedDate);
            }

            if (completedBy == null || completedBy.trim().equals("")) {
                Completed_by.setText("Not available");
            } else {
                Completed_by.setText(completedBy);
            }


        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void getTechnicians() {
        list_techs.clear();
        if (AssignUser != null && !AssignUser.isEmpty()) {
            if (AssignUser.contains(",")) {
                String tech[] = AssignUser.split(",");
                for (int i = 0; i < tech.length; i++) {
                    if(tech[i].trim().equals("")) continue;
                    String technician=tech[i].substring(0,tech[i].indexOf("("));

                    if(completedBy.trim().equals(technician.trim())){
                        list_techs.add(new Technician(tech[i], "", true));
                    }else {
                        list_techs.add(new Technician(tech[i], "", false));
                    }


                }
            }
        }
        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(this, list_techs);
        tech_recyclerView.setAdapter(jobFilesAdapter);

        ScrollView sv = (ScrollView) findViewById(R.id.scrollview);
        sv.scrollTo(0, sv.getTop());


    }

    private void setRecyclerTempData() {



        list_techs.add(new Technician("tech tech(Service Tech)", "28", true));
        list_techs.add(new Technician("test test(Service Tech)", "23", false));
        list_techs.add(new Technician("test1 test1(Service Tech)", "24", false));
        list_techs.add(new Technician("test2 test2(Service Tech)", "26", false));
        list_techs.add(new Technician("test3 test3(Service Tech)", "27", false));
        list_techs.add(new Technician("test4 test4(Service Tech)", "29", false));
        list_techs.add(new Technician("test5 test5(Service Tech)", "21", false));
        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(this, list_techs);
        tech_recyclerView.setAdapter(jobFilesAdapter);

        ScrollView sv = (ScrollView) findViewById(R.id.scrollview);
        sv.scrollTo(0, sv.getTop());
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

    public class JobFilesAdapter extends RecyclerView.Adapter<JobFilesAdapter.MyViewHolder> {

        List<Technician> list_techss;
        Context context;


        public JobFilesAdapter(Context context, List<Technician> Attachments) {
            this.context = context;
            this.list_techss = Attachments;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_techs, parent, false);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int i) {


            final String Name = list_techss.get(i).getName();
            //  final String fileURL = list_techss.get(i).getFileURL();
            // final String date = list_techss.get(i).getDate();

            final boolean isChecklist_Completed = list_techss.get(i).isChecklist_Completed();

            if (isChecklist_Completed) {
                holder.view1.setBackgroundColor(context.getResources().getColor(R.color.main_green_color));
            }

            holder.index_no.setText(String.valueOf(i + 1));


            if (Name == null || Name.trim().equals("")) {
                holder.Name.setText("Not available");
            } else {
                holder.Name.setText(Name);

            }


            holder.row_jobFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }

        @Override
        public int getItemCount() {
            return list_techss.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Name;//, Date;

            Button index_no;
            LinearLayout row_jobFile;
            View view1;

            public MyViewHolder(View convertview) {
                super(convertview);

                index_no = (Button) convertview.findViewById(R.id.serial_no);
                Name = (TextView) convertview.findViewById(R.id.Name);
                view1 = convertview.findViewById(R.id.view1);


                //   Date = (TextView) convertview.findViewById(R.id.Date);

                row_jobFile = (LinearLayout) convertview.findViewById(R.id.row_jobFile);

            }
        }
    }

}
