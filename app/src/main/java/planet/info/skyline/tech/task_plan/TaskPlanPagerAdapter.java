package planet.info.skyline.tech.task_plan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TaskPlanPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    String[] TAB_TITLES = {"Today's/Future Task(s)","Overdue Task(s)", "Completed Task(s)"};
    String job_id,swo_id;
    int tabCount;





    public TaskPlanPagerAdapter(Context context, FragmentManager fm, String job_id,String swo_id) {
        super(fm);
        mContext = context;
        this.tabCount = tabCount;
        this.job_id = job_id;
        this.swo_id = swo_id;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle b = new Bundle();
        b.putString("tab", position + "");
        b.putString("job_id", job_id);
        b.putString("swo_id", swo_id);

        Fragment f = null;
        if (position == 0) {

            f = new Todays_Future_Task_Fragment();
            f.setArguments(b);
            return f;
        } else if (position == 1) {

            f = new OverdueTask_Fragment();
            f.setArguments(b);
        }
        else if (position == 2) {

            f = new CompletedTask_Fragment();
            f.setArguments(b);
        }

        return f;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }

}