package osc.ada.tomislavgazica.taskie.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import osc.ada.tomislavgazica.taskie.DatabaseHandler;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.Task;
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
    Realm realm;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskie);
        ButterKnife.bind(this);

        Realm.init(this);
        realm = Realm.getDefaultInstance();
        databaseHandler = DatabaseHandler.getInstance();
        updateDatabase();

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

    public void updateDatabase() {
        realm.beginTransaction();
        RealmResults<Task> tasks = realm.where(Task.class).equalTo("isUploaded", false).findAll();
        if (tasks != null && !tasks.isEmpty()) {
            boolean areTasksUploaded = databaseHandler.uploadTasks(this, tasks);
            if (areTasksUploaded) {
                tasks.deleteAllFromRealm();
            }
        }

        List<Task> taskList = new ArrayList<>();
        if (databaseHandler.getFavoriteTasks(getApplicationContext()) != null && databaseHandler.getFavoriteTasks(getApplicationContext()) != null) {
            taskList.addAll(databaseHandler.getNotFavoriteTasks(getApplicationContext()));
            taskList.addAll(databaseHandler.getFavoriteTasks(getApplicationContext()));
        }

        for (int i = 0; i < taskList.size(); i++) {
            if (realm.where(Task.class).equalTo("ID", taskList.get(i).getID()).findFirst() == null) {
                realm.createObject(Task.class, taskList.get(i).getID());
                realm.insertOrUpdate(taskList.get(i));
            }
        }
        realm.commitTransaction();
    }

}
