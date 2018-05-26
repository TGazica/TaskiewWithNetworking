package osc.ada.tomislavgazica.taskie.view.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TasksPageAdapter extends FragmentPagerAdapter{

    private final List<Fragment> fragmentList = new ArrayList<>();

    public TasksPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setItems(List<Fragment> fragments){
        fragmentList.clear();
        fragmentList.addAll(fragments);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
