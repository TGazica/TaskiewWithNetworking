package osc.ada.tomislavgazica.taskie.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import osc.ada.tomislavgazica.taskie.DatabaseHandler;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskList;
import osc.ada.tomislavgazica.taskie.networking.ApiService;
import osc.ada.tomislavgazica.taskie.networking.RetrofitUtil;
import osc.ada.tomislavgazica.taskie.util.DialogClickListener;
import osc.ada.tomislavgazica.taskie.util.SharedPrefsUtil;
import osc.ada.tomislavgazica.taskie.util.TaskClickListener;
import osc.ada.tomislavgazica.taskie.view.EditTaskActivity;
import osc.ada.tomislavgazica.taskie.view.TaskAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoriteTasksFragment extends Fragment implements TaskClickListener, DialogClickListener {

    @BindView(R.id.tasks)
    RecyclerView tasks;
    private TaskAdapter taskAdapter;
    Realm realm;
    List<Task> favoriteTasks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Realm.init(getContext());
        realm = Realm.getDefaultInstance();
        favoriteTasks = new ArrayList<>();
        tasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        tasks.setItemAnimator(new DefaultItemAnimator());
        taskAdapter = new TaskAdapter(this);
        taskAdapter.setContext(getActivity());
        tasks.setAdapter(taskAdapter);

        updateTasksDisplay();
    }

    private void updateTasksDisplay() {
        taskAdapter.updateTasks(realm.where(Task.class).equalTo("isFavorite", true).findAll());
    }


    @Override
    public void onStatusClick(Task task) {

    }

    @Override
    public void onPriorityClick(Task task) {
    }

    @Override
    public void onFavoriteClick(Task task) {

    }

    @Override
    public void onLongClick(Task task) {
        android.support.v4.app.FragmentManager fm = getFragmentManager();
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.setListener(this);
        dialogFragment.setTask(task);
        dialogFragment.show(fm, "dialog");
    }

    @Override
    public void onEditClick(Task task) {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra(EditTaskActivity.EDIT_TASK, task.getID());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(final Task task) {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call deleteTask = apiService.deleteTask(SharedPrefsUtil.getPreferencesField(getContext(), SharedPrefsUtil.TOKEN), task.getID());

        deleteTask.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    realm.beginTransaction();
                    Task deleteTask = realm.where(Task.class).equalTo("ID", task.getID()).findFirst();
                    deleteTask.deleteFromRealm();
                    realm.commitTransaction();
                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getContext(), "Network error, not deleted.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
