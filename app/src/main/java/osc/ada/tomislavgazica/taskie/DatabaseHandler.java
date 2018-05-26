package osc.ada.tomislavgazica.taskie;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskList;
import osc.ada.tomislavgazica.taskie.networking.ApiService;
import osc.ada.tomislavgazica.taskie.networking.RetrofitUtil;
import osc.ada.tomislavgazica.taskie.util.SharedPrefsUtil;
import osc.ada.tomislavgazica.taskie.view.TasksActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DatabaseHandler extends AppCompatActivity{

//    private Realm realm;
//    private RealmResults<Task> taskListFromRealmNotUploaded;
//    private List<Task> taskListFromNet;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Realm.init(this);
//        realm = Realm.getDefaultInstance();
//
//        taskListFromNet = new ArrayList<>();
//        updateRealmDatabase();
//        updateDatabase();
//
//        realm.beginTransaction();
//        taskListFromRealmNotUploaded = realm.where(Task.class).equalTo("isUploaded", false).findAll();
//        realm.commitTransaction();
//    }
//
//    public void updateDatabase(){
//        uploadTasks();
//        getFavoriteTasks();
//        getNotFavoriteTasks();
//    }
//
//    public void updateRealmDatabase(){
//        getFavoriteTasks();
//        getNotFavoriteTasks();
//        realm.beginTransaction();
//        realm.insertOrUpdate(taskListFromNet);
//        realm.commitTransaction();
//    }
//
//    public void getFavoriteTasks(){
//        Retrofit retrofit = RetrofitUtil.createRetrofit();
//        ApiService apiService = retrofit.create(ApiService.class);
//
//        Call<TaskList> taskListCall = apiService
//                .getFavoriteTasks(SharedPrefsUtil.getPreferencesField(this
//                        , SharedPrefsUtil.TOKEN));
//
//        taskListCall.enqueue(new Callback<TaskList>() {
//            @Override
//            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
//                if (response.isSuccessful()) {
//                    taskListFromNet.addAll(response.body().tasksList);
//                }
//            }
//            @Override
//            public void onFailure(Call<TaskList> call, Throwable t) {
//            }
//        });
//    }
//
//    public void getNotFavoriteTasks(){
//        Retrofit retrofit = RetrofitUtil.createRetrofit();
//        ApiService apiService = retrofit.create(ApiService.class);
//
//        Call<TaskList> taskListCall = apiService
//                .getTasks(SharedPrefsUtil.getPreferencesField(this
//                        , SharedPrefsUtil.TOKEN));
//
//        taskListCall.enqueue(new Callback<TaskList>() {
//            @Override
//            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
//                if (response.isSuccessful()) {
//                    taskListFromNet.addAll(response.body().tasksList);
//                }
//            }
//            @Override
//            public void onFailure(Call<TaskList> call, Throwable t) {
//            }
//        });
//    }
//
//    public void uploadTasks(){
//        for (int i = 0; i < taskListFromRealmNotUploaded.size(); i++){
//            Retrofit retrofit = RetrofitUtil.createRetrofit();
//            ApiService apiService = retrofit.create(ApiService.class);
//
//            Call postNewTaskCall = apiService.
//                    postNewTask(SharedPrefsUtil.getPreferencesField(this
//                            , SharedPrefsUtil.TOKEN), taskListFromRealmNotUploaded.get(i));
//
//            final int finalI = i;
//            postNewTaskCall.enqueue(new Callback() {
//                @Override
//                public void onResponse(Call call, Response response) {
//                    if (response.isSuccessful()){
//                        realm.beginTransaction();
//                        taskListFromRealmNotUploaded.get(finalI).deleteFromRealm();
//                        realm.commitTransaction();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call call, Throwable t) {
//                }
//            });
//
//
//        }
//    }
}
