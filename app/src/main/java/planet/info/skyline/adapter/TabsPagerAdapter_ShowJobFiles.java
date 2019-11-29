package planet.info.skyline.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import planet.info.skyline.tech.job_files_old.ShowJobFiles_Fragment;


/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class TabsPagerAdapter_ShowJobFiles extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
   // ArrayList<HashMap<String, String>> list;

    String jobtxt_id;

    //Constructor to the class
    public TabsPagerAdapter_ShowJobFiles(FragmentManager fm, int tabCount, String jobtxt_id) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.jobtxt_id = jobtxt_id;

    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs

        Fragment f = new ShowJobFiles_Fragment();
        Bundle b = new Bundle();
        b.putString("tab", position + "");
        b.putString("jobtxt_id", jobtxt_id);
        f.setArguments(b);
        return f;




   }


    @Override
    public int getCount() {
        return tabCount;
    }
}
