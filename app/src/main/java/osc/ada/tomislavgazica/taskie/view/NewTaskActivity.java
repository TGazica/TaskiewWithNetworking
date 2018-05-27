package osc.ada.tomislavgazica.taskie.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import osc.ada.tomislavgazica.taskie.DatabaseHandler;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskPriority;
import osc.ada.tomislavgazica.taskie.networking.ApiService;
import osc.ada.tomislavgazica.taskie.networking.RetrofitUtil;
import osc.ada.tomislavgazica.taskie.util.SharedPrefsUtil;
import osc.ada.tomislavgazica.taskie.view.fragments.DatePickerFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.edittext_newtask_title)
    EditText title;

    @BindView(R.id.edittext_newtask_description)
    EditText description;

    @BindView(R.id.spinner_newtask_priority)
    Spinner priority;

    @BindView(R.id.textview_newtask_dueDate)
    TextView date;

    String dueDate;
    Realm realm;
    boolean isUploadedToNet = true;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        realm = Realm.getDefaultInstance();
        ButterKnife.bind(this);
        databaseHandler = DatabaseHandler.getInstance();
        setupSpinnerResource();
        setCurrentDate();
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(day).append(".").append(month).append(".").append(year);
        date.setText(stringBuilder);
        dueDate = stringBuilder.toString();
    }

    private void setupSpinnerResource() {
        priority.setAdapter(new ArrayAdapter<TaskPriority>(this, android.R.layout.simple_list_item_1, TaskPriority.values()));
        priority.setSelection(0);
    }


    @OnClick(R.id.button_newtask_setDate)
    public void setDate(View view) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnClickListener(this);
        datePickerFragment.show(fragmentManager, "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(day).append(".").append(month).append(".").append(year);
        date.setText(stringBuilder);
        dueDate = stringBuilder.toString();
    }

    @OnClick(R.id.imagebutton_newtask_saveTask)
    public void saveTask() {
        Task task;
        String title = this.title.getText().toString();
        String description = this.description.getText().toString();
        int priority = this.priority.getSelectedItemPosition();

        task = new Task(title, description, priority, dueDate);

        createNewTask(task);
    }

    private void createNewTask(Task task) {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call postNewTaskCall = apiService.
                postNewTask(SharedPrefsUtil.getPreferencesField(getApplicationContext()
                        , SharedPrefsUtil.TOKEN), task);

        postNewTaskCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    updateRealmDatabase();
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                isUploadedToNet = false;
                finish();
            }
        });

        if (!isUploadedToNet){
            addNewTaskToRealm(task);
        }

    }

    private void updateRealmDatabase() {
        realm.beginTransaction();
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(databaseHandler.getFavoriteTasks(this));
        tasks.addAll(databaseHandler.getNotFavoriteTasks(this));
        if (!tasks.isEmpty()){
            for (int i = 0; i<tasks.size(); i++){
                Task task = realm.where(Task.class).equalTo("ID", tasks.get(i).getID()).findFirst();
                if(task == null){
                    realm.insert(tasks.get(i));
                }
            }
        }
        realm.commitTransaction();

    }

    private void addNewTaskToRealm(Task task) {
        realm.beginTransaction();
        realm.insert(task);
        task.setUploaded(false);
        realm.commitTransaction();

    }

}
