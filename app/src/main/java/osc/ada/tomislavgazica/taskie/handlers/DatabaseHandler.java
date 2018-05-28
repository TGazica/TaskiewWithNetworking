package osc.ada.tomislavgazica.taskie.handlers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import osc.ada.tomislavgazica.taskie.App;
import osc.ada.tomislavgazica.taskie.interaction.ApiInteractor;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskList;
import osc.ada.tomislavgazica.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabaseHandler {

    private final SharedPreferences preferences;
    private final ApiInteractor apiInteractor;
    private final Realm realm;
    private final Context context;

    private static DatabaseHandler databaseHandler;

    private DatabaseHandler(Context context) {
        preferences = App.getPreferences();
        apiInteractor = App.getApiInteractor();
        realm = App.getRealm();
        this.context = context;
    }

    public static synchronized DatabaseHandler getInstance(Context context) {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler(context);
        }
        return databaseHandler;
    }

    public void uploadTasksToNet() {
        realm.beginTransaction();
        RealmResults<Task> tasks = realm.where(Task.class).equalTo("isUploaded", false).findAll();
        for (int i = 0; i < tasks.size(); i++) {
            apiInteractor.addNewTask(tasks.get(i), addNewTaskCallback(), SharedPrefsUtil.getPreferencesField(context, SharedPrefsUtil.TOKEN));
        }
        tasks.deleteAllFromRealm();
        realm.commitTransaction();
        getTasksFromNet();
    }

    private Callback<Task> addNewTaskCallback() {
        return new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {

            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {

            }
        };
    }

    public void getTasksFromNet() {
        apiInteractor.getFavoriteTasks(getTasksCallback(), SharedPrefsUtil.getPreferencesField(context, SharedPrefsUtil.TOKEN));
        apiInteractor.getOtherTasks(getTasksCallback(), SharedPrefsUtil.getPreferencesField(context, SharedPrefsUtil.TOKEN));
    }

    private Callback<TaskList> getTasksCallback() {
        return new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful()) {
                    saveTasksToRealm(response.body().tasksList);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {

            }
        };
    }

    private void saveTasksToRealm(List<Task> tasks) {
        realm.beginTransaction();
        for (int i = 0; i < tasks.size(); i++) {
            if (realm.where(Task.class).equalTo("id", tasks.get(i).getId()).findFirst() == null) {
                Task task = realm.createObject(Task.class, tasks.get(i).getId());
                task.setTitle(tasks.get(i).getTitle());
                task.setDescription(tasks.get(i).getDescription());
                task.setDueDate(tasks.get(i).getDueDate());
                task.setPriority(tasks.get(i).getPriority() - 1);
                task.setFavorite(tasks.get(i).isFavorite());
                task.setCompleted(tasks.get(i).isCompleted());
            }
        }
        realm.commitTransaction();
    }

    public List<Task> getAllTasks() {
        return realm.where(Task.class).findAll();
    }

    public List<Task> getFavoriteTasks() {
        return realm.where(Task.class).equalTo("isFavorite", true).findAll();
    }

    public void deleteTask(String id) {
        realm.beginTransaction();
        realm.where(Task.class).equalTo("id", id).findFirst().deleteFromRealm();
        realm.commitTransaction();
    }
}



