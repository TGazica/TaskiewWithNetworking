package osc.ada.tomislavgazica.taskie;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskList;
import osc.ada.tomislavgazica.taskie.networking.ApiService;
import osc.ada.tomislavgazica.taskie.networking.RetrofitUtil;
import osc.ada.tomislavgazica.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DatabaseHandler {

    private static DatabaseHandler repository = null;
    private List<Task> taskListFromRealmNotUploaded;
    //    private List<Task> taskListFromNetFavorite;
//    private List<Task> taskListFromNetNotFavorite;
    private TaskList taskListFav;
    private TaskList taskListNotFav;
    private boolean areTasksUploaded = false;

    private DatabaseHandler() {
//        taskListFromNetNotFavorite = new ArrayList<>();
//        taskListFromNetFavorite = new ArrayList<>();
        taskListFromRealmNotUploaded = new ArrayList<>();
        taskListFav = new TaskList();
        taskListNotFav = new TaskList();
    }

    public static synchronized DatabaseHandler getInstance() {
        if (repository == null) {
            repository = new DatabaseHandler();
        }
        return repository;
    }

    public List<Task> getFavoriteTasks(Context context) {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        if (taskListFav.tasksList != null) {
            taskListFav.tasksList.clear();
        }

        Call<TaskList> taskListCall = apiService
                .getFavoriteTasks(SharedPrefsUtil.getPreferencesField(context
                        , SharedPrefsUtil.TOKEN));

        taskListCall.enqueue(new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful()) {

                    taskListFav.tasksList.addAll(response.body().tasksList);

                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
            }
        });
        return taskListFav.tasksList;
    }

    public List<Task> getNotFavoriteTasks(Context context) {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        if (taskListNotFav.tasksList != null) {
            taskListNotFav.tasksList.clear();
        }

        Call<TaskList> taskListCall = apiService
                .getTasks(SharedPrefsUtil.getPreferencesField(context
                        , SharedPrefsUtil.TOKEN));

        taskListCall.enqueue(new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful()) {
                    taskListNotFav.tasksList.addAll(response.body().tasksList);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
            }
        });

        return taskListNotFav.tasksList;
    }

    public boolean uploadTasks(Context context, List<Task> tasks) {

        taskListFromRealmNotUploaded.addAll(tasks);

        for (int i = 0; i < taskListFromRealmNotUploaded.size(); i++) {
            Retrofit retrofit = RetrofitUtil.createRetrofit();
            ApiService apiService = retrofit.create(ApiService.class);

            Call postNewTaskCall = apiService.
                    postNewTask(SharedPrefsUtil.getPreferencesField(context
                            , SharedPrefsUtil.TOKEN), taskListFromRealmNotUploaded.get(i));

            postNewTaskCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        areTasksUploaded = true;
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                }
            });


        }
        return areTasksUploaded;
    }

//    private void addFavoriteToDatabase(List<Task> tasks) {
//        taskListFromNetNotFavorite.addAll(tasks);
//    }
//
//    private void addNotFavoriteToDatabase(List<Task> tasks) {
//        taskListFromNetNotFavorite.addAll(tasks);
//    }

}
