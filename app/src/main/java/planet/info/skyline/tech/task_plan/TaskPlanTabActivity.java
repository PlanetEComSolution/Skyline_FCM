package planet.info.skyline.tech.task_plan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import planet.info.skyline.R;
import planet.info.skyline.tech.job_files_new.SectionsPagerAdapter;
import planet.info.skyline.tech.job_files_new.SharePhotosToClientActivity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;


public class TaskPlanTabActivity extends AppCompatActivity {
  //  int position;
   // SharedPreferences sp;
    String job_id,swo_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_task_plan);
      //  sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);
      //  jobtxt_id = Shared_Preference.getJOB_ID_FOR_JOBFILES(this);
        setTitle(Utility.getTitle("Task Plan"));

        try{

         Bundle bundle=   getIntent().getExtras();
        if(bundle!=null){
            job_id=   bundle.getString("job_id");
            swo_id=   bundle.getString("swo_id");
        }
        }catch (Exception e){
            e.getMessage();
        }


        final TaskPlanPagerAdapter sectionsPagerAdapter = new TaskPlanPagerAdapter(this, getSupportFragmentManager(), job_id,swo_id);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setupWithViewPager(viewPager);

        TextView title = findViewById(R.id.title);
        ImageView back = findViewById(R.id.back);


        title.setText("Task Plan");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



     /*   tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //    clear();
                position = tab.getPosition();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}