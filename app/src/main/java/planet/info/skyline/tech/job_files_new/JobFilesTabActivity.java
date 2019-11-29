package planet.info.skyline.tech.job_files_new;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import planet.info.skyline.home.MainActivity;
import planet.info.skyline.tech.choose_job_company.SelectCompanyActivity;
import planet.info.skyline.tech.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;


public class JobFilesTabActivity extends AppCompatActivity {
    int position;
    SharedPreferences sp;
    // String Job_Id;
    List<HashMap<String, String>> list_ProjectPhotos = new ArrayList<>();
    List<HashMap<String, String>> list_ProofRenders = new ArrayList<>();
    ImageView share;
    ViewPager viewPager;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list_tab);
        setView();


        /*final boolean TIMER_STARTED_FROM_BILLABLE_MODULE = Shared_Preference.getTIMER_STARTED_FROM_BILLABLE_MODULE(this);
        if (TIMER_STARTED_FROM_BILLABLE_MODULE) {
            String Job_Id = Shared_Preference.getJOB_ID_FOR_JOBFILES(JobFilesTabActivity.this);
            setAdapter(Job_Id);
        } else {
            Intent i = new Intent(this, SelectCompanyActivity.class);
            i.putExtra(Utility.IS_JOB_MANDATORY, "1");
            i.putExtra(Utility.Show_DIALOG_SHOW_INFO, true);
            startActivityForResult(i, Utility.CODE_SELECT_COMPANY);
        }*/



            String Job_Id = Shared_Preference.getJOB_ID_FOR_JOBFILES(JobFilesTabActivity.this);
            setAdapter(Job_Id);


    }

    private void setView() {
        TextView title = findViewById(R.id.title);
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        title.setText("Job File(s)");
        ImageView back = findViewById(R.id.back);
        share = findViewById(R.id.img_share);
        share.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareToClient();
            }
        });
    }


    private void setAdapter(String JobId) {

        //  setTitle(Utility.getTitle("Job File(s)"))
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), JobId);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //    clear();
                position = tab.getPosition();
                if (position == 3 || position == 1) {
                    share.setVisibility(View.VISIBLE);
                } else {
                    share.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void addProjectPhotos(HashMap<String, String> hashMap) {
        if (list_ProjectPhotos.isEmpty()) {
            list_ProjectPhotos.add(hashMap);
            return;
        }

        for (int i = 0; i < list_ProjectPhotos.size(); i++) {
            String fileId1 = list_ProjectPhotos.get(i).get("int_job_file_id");
            String fileId = hashMap.get("int_job_file_id");
            if (fileId.equalsIgnoreCase(fileId1)) {
                list_ProjectPhotos.set(i, hashMap);
                break;
            } else if (i == list_ProjectPhotos.size() - 1) {
                list_ProjectPhotos.add(hashMap);
            }
        }

    }

    public void addProofRenders(HashMap<String, String> hashMap) {

        if (list_ProofRenders.isEmpty()) {
            list_ProofRenders.add(hashMap);
            return;
        }

        for (int i = 0; i < list_ProofRenders.size(); i++) {
            String fileId1 = list_ProofRenders.get(i).get("int_job_file_id");
            String fileId = hashMap.get("int_job_file_id");
            if (fileId.equalsIgnoreCase(fileId1)) {
                list_ProofRenders.set(i, hashMap);
                break;
            } else if (i == list_ProofRenders.size() - 1) {
                list_ProofRenders.add(hashMap);
            }
        }
    }

    public void clear() {
        list_ProjectPhotos.clear();
    }

    public void ShareToClient() {

        List<HashMap<String, String>> list = getList();
        List<HashMap<String, String>> list_selected = new ArrayList<>();

        int selectedFileLength = 0;

        for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> hashMap = list.get(i);
            if (hashMap.containsKey("isSelected")) {
                String isSelected = hashMap.get("isSelected");
                if (isSelected.equalsIgnoreCase("true")) {
                    selectedFileLength++;
                    list_selected.add(hashMap);
                }
            }
        }

        if (selectedFileLength < 1) {
            Toast.makeText(this, "No file selected!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, selectedFileLength + " file(s) selected!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, SharePhotosToClientActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST", (Serializable) list_selected);
            intent.putExtra("BUNDLE", args);
            startActivity(intent);

            //    finish();

        }


    }

    public List<HashMap<String, String>> getList() {
        List<HashMap<String, String>> list = new ArrayList<>();
        if (position == 1)
            list.addAll(list_ProofRenders);

        else if (position == 3)
            list.addAll(list_ProjectPhotos);

        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == Utility.CODE_SELECT_COMPANY) {
                try {
                    String Job_Id = data.getStringExtra("JobID");
                    setAdapter(Job_Id);
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "Exception caught!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (resultCode == RESULT_CANCELED) {
            Intent intent = new Intent(JobFilesTabActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}