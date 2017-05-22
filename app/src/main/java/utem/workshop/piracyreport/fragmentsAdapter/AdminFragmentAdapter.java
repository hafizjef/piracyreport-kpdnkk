package utem.workshop.piracyreport.fragmentsAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import utem.workshop.piracyreport.fragments.ReportListAdminFragment;

/**
 * Created by max on 22/5/17.
 */

public class AdminFragmentAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Reports", "Overview"};
    private Context context;

    public AdminFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 : return ReportListAdminFragment.newInstance(position);
            default: return ReportListAdminFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
