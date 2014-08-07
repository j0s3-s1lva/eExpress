package co.droidbrain.eexpress.app;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j0s3 on 7/08/14.
 */
public class CargadorViewPager extends FragmentStatePagerAdapter {
    // Lista de fragments con que se usarán con el ViewPager
    List<Fragment> fragments;

    public CargadorViewPager(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
    }

    /**
     * Añadir un nuevo fragment a la list     *
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        this.fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
