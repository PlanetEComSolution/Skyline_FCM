package planet.info.skyline.tech.job_files_new;

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
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    String[] TAB_TITLES = {"Job File(s)", "Proof & Render(s)","Client File(s)","Project Photo(s)","Project File(s)"};
   String JobId;
   // int tabCount;
  //  ArrayList<HashMap<String, String>> list;




    public SectionsPagerAdapter(Context context, FragmentManager fm, String JobId) {
        super(fm);
        mContext = context;
     //   this.tabCount = tabCount;
       this.JobId = JobId;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;
        Bundle b=new Bundle();
        b.putString("JobId",JobId);

        if (position == 0) {    // job files
            f = new JobFiles_Fragment();

        } else if (position == 1) {  // Proof & Renders
            f = new ProofRenders_Fragment();
        }
        else if (position == 2) {    // Client Files
            f = new ShowClientFiles_Fragment();
        }
        else if (position == 3) {    // Project Photos
            f= new ProjectPhotos_Fragment();
        }
        else if (position == 4) {    //Project Files
            f = new ShowProjectFile_Fragment();
        }
        f.setArguments(b);
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