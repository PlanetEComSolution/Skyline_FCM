package planet.info.skyline.tech.task_plan;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.model.SWO;
import planet.info.skyline.model.Technician;
import planet.info.skyline.network.API_Interface;
import planet.info.skyline.network.REST_API_Client_TaskPlan;
import planet.info.skyline.util.Utility;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CompleteTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {
    TextView Task, Description, Checklist,
            deadline_date, Time_Budget, Start_By, Completed_date;
    LinearLayout ll_completedDate,ll_completion_date;//,cardview_technician;
    ImageView cardview_linked, Imgvw_OK;

    String checklist;
    String completedDate;
    String deadlineDate;
    String description;
    String startBy;
    String task;
    String timeBudget;
    String technician;
    String taskID;
    LinearLayout ll_checklist;

    Button btn_Add, btn_Cancel;
    RecyclerView tech_recyclerView;
    List<Technician> list_techs = new ArrayList<>();

    boolean IS_CHECKLIST_DONE = false;
    ScrollView sv ;


    String job_id;
    String id ,AssignUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task_3);
        setTitle(Utility.getTitle("Complete Task"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setView();
        getIntentData();
        getTechnicians();
       // setRecyclerTempData();

     //   focusOnView();
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
        ll_completedDate = findViewById(R.id.ll_completedDate);
        ll_completion_date= findViewById(R.id.ll_completion_date);
        ll_completion_date.setVisibility(View.INVISIBLE);

        tech_recyclerView = (RecyclerView) findViewById(R.id.tech_recyclerView);
        tech_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tech_recyclerView.setItemAnimator(new DefaultItemAnimator());



        // Completed_date.set
        if (!IS_CHECKLIST_DONE) {
            Utility.animateTextview(Completed_date, this);
        }

        sv =  findViewById(R.id.scrollview);
        Imgvw_OK = findViewById(R.id.OK);
        Imgvw_OK.setVisibility(View.INVISIBLE);

        btn_Add = findViewById(R.id.btn_Add);
        btn_Cancel = findViewById(R.id.btn_Cancel);

        //  Completed_date.setText(Utility.getCurrentdate("dd MMM yyyy"));


        cardview_linked = findViewById(R.id.cardview_linked);
        cardview_linked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompleteTaskActivity.this, AttachmentListActivity.class);

                i.putExtra("taskID", taskID);
                startActivity(i);
            }
        });
      /*  cardview_technician =  findViewById(R.id.cardview_technician);
        cardview_technician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(CompleteTaskActivity.this,TechnicianListActivity.class);
                startActivity(i);
            }
        });*/
        ll_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IS_CHECKLIST_DONE) {
                    Intent intent = new Intent(CompleteTaskActivity.this, ChecklistActivity.class);
                    intent.putExtra("Updatable", "1");
                    intent.putExtra("RequestCode", Utility.REQUEST_CODE_CHECKLIST);

                    intent.putExtra("job_id", job_id);
                    intent.putExtra("task_id", taskID);
                    intent.putExtra("id", id);


                    startActivityForResult(intent, Utility.REQUEST_CODE_CHECKLIST);// Activity is started with requestCode 2

                }


            }
        });

        ll_completedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IS_CHECKLIST_DONE) {
                    Click_getDate();
                }

            }
        });
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
    }

    private void setRecyclerTempData() {


        list_techs.add(new Technician("tech tech(Service Tech)", "28", false));
        list_techs.add(new Technician("test test(Service Tech)", "23", false));
        list_techs.add(new Technician("test1 test1(Service Tech)", "24", false));
        list_techs.add(new Technician("test2 test2(Service Tech)", "26", false));
        list_techs.add(new Technician("test3 test3(Service Tech)", "27", false));
        list_techs.add(new Technician("test4 test4(Service Tech)", "29", false));
        list_techs.add(new Technician("test5 test5(Service Tech)", "21", false));
        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(this, list_techs);
        tech_recyclerView.setAdapter(jobFilesAdapter);


    }
    private void getTechnicians() {
        list_techs.clear();
        if (AssignUser != null && !AssignUser.isEmpty()) {
            if (AssignUser.contains(",")) {
                String tech[] = AssignUser.split(",");
                for (int i = 0; i < tech.length; i++) {
                    if(tech[i].trim().equals("")) continue;
                    list_techs.add(new Technician(tech[i], "", false));
                }
            }
        }
        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(this, list_techs);
        tech_recyclerView.setAdapter(jobFilesAdapter);

        ScrollView sv = (ScrollView) findViewById(R.id.scrollview);
        sv.scrollTo(0, sv.getTop());


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            } else {
                if (requestCode == Utility.REQUEST_CODE_CHECKLIST) {
                    IS_CHECKLIST_DONE = data.getBooleanExtra(Utility.IS_CHECKLIST_DONE, false);
                    if (IS_CHECKLIST_DONE) {
                        Checklist.setText("3/3");


                        Utility.StopanimateTextview(Completed_date, this);

                        Click_getDate();
                    }
                }
               else if (requestCode == Utility.REQUEST_CODE_COMPLETED_DATE) {

                    IS_CHECKLIST_DONE = data.getBooleanExtra(Utility.IS_CHECKLIST_DONE, false);
                    if (IS_CHECKLIST_DONE) {
                        Checklist.setText("3/3");


                        Utility.StopanimateTextview(Completed_date, this);


                        Imgvw_OK.setVisibility(View.VISIBLE);
                        list_techs.set(0, new Technician("tech tech(Service Tech)", "28", true));
                        JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(this, list_techs);
                        tech_recyclerView.setAdapter(jobFilesAdapter);


                    }

                }


            }



        if (!IS_CHECKLIST_DONE) {
            ll_completion_date.setVisibility(View.INVISIBLE);
            Completed_date.setText("Click Here to Complete");
            Utility.animateTextview(Completed_date, this);
        }




        }



    private void Click_getDate() {
        Date ndate = null;


        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                CompleteTaskActivity.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


        dpd.setThemeDark(false);
        dpd.vibrate(false);
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(getResources().getColor(R.color.colorAccent));


        dpd.setTitle("Completed Date");
        dpd.setYearRange(1985, 2028);
        dpd.setMaxDate(calendar);
        dpd.show(CompleteTaskActivity.this.getFragmentManager(), "dialog");


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = +dayOfMonth + "/" + (++monthOfYear) + "/" + year;

        String dayOfMonthString = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String monthOfYearString = monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear;

        //2018-01-19%20

        // String  date_1 = year + "-" + monthOfYearString + "-" + dayOfMonthString;
        String date_1 = dayOfMonthString + "-" + monthOfYearString + "-" + year;

        Completed_date.setText(Utility.ChangeDateFormat("dd-MM-yyyy", "dd MMM yyyy", date_1));

        ll_completion_date.setVisibility(View.VISIBLE);

        if (IS_CHECKLIST_DONE) {
            Imgvw_OK.setVisibility(View.VISIBLE);
            list_techs.set(0, new Technician("tech tech(Service Tech)", "28", true));
            JobFilesAdapter jobFilesAdapter = new JobFilesAdapter(this, list_techs);
            tech_recyclerView.setAdapter(jobFilesAdapter);
        }else {
            Intent intent = new Intent(CompleteTaskActivity.this, ChecklistActivity.class);
            intent.putExtra("Updatable", "1");
            intent.putExtra("RequestCode", Utility.REQUEST_CODE_COMPLETED_DATE);
            startActivityForResult(intent, Utility.REQUEST_CODE_COMPLETED_DATE);// Activity is started with requestCode 2

        }




    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (!IS_CHECKLIST_DONE) {
            Utility.animateTextview(Completed_date, this);
        }
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
                taskID = bundle.getString("taskID", "");
                job_id = bundle.getString("job_id", "");
                id = bundle.getString("id", "");
                AssignUser= bundle.getString("AssignUser", "");
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

   /* private final void focusOnView(){
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, Task.getTop());
            }
        });
    }*/
}
