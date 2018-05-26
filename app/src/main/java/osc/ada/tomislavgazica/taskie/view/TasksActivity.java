package osc.ada.tomislavgazica.taskie.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import osc.ada.tomislavgazica.taskie.DatabaseHandler;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.view.fragments.AllTasksFragment;
import osc.ada.tomislavgazica.taskie.view.fragments.FavoriteTasksFragment;
import osc.ada.tomislavgazica.taskie.view.fragments.TasksPageAdapter;


public class TasksActivity extends AppCompatActivity {

    private static final int REQUEST_NEW_TASK = 10;

    @BindView(R.id.fab_tasks_addNew)
    FloatingActionButton addNewTask;

    @BindView(R.id.fragmentContainer)
    ViewPager viewPager;

    private TasksPageAdapter tasksPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskie);
        ButterKnife.bind(this);

        tasksPageAdapter = new TasksPageAdapter(getSupportFragmentManager());

        List<Fragment> pages = new ArrayList<>();
        pages.add(new AllTasksFragment());
        pages.add(new FavoriteTasksFragment());

        tasksPageAdapter.setItems(pages);
        viewPager.setAdapter(tasksPageAdapter);
    }

    @OnClick(R.id.fab_tasks_addNew)
    public void startNewTaskActivity() {
        Intent newTask = new Intent();
        newTask.setClass(this, NewTaskActivity.class);
        startActivityForResult(newTask, REQUEST_NEW_TASK);
    }

}
