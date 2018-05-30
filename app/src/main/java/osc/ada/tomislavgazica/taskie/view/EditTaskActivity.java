package osc.ada.tomislavgazica.taskie.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
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

public class EditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.edittext_newtask_title)
    EditText title;

    @BindView(R.id.edittext_newtask_description)
    EditText description;

    @BindView(R.id.spinner_newtask_priority)
    Spinner priority;

    @BindView(R.id.textview_newtask_dueDate)
    TextView date;

    Task task;
    String ID;
    String dueDate;
    Realm realm;

    public static final String EDIT_TASK = "edit_task";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        realm = Realm.getDefaultInstance();
        ButterKnife.bind(this);
        setupSpinnerResource();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EDIT_TASK)) {
            ID = intent.getStringExtra(EDIT_TASK);
        }

        task = realm.where(Task.class).equalTo("id", ID).findFirst();

        if (task != null) {
            title.setText(task.getTitle());

            description.setText(task.getDescription());

            date.setText(task.getDueDate());
        }

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
    public void editTask() {

        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Task editedTask = new Task();
        editedTask.setId(task.getId());
        editedTask.setTitle(title.getText().toString());
        editedTask.setDescription(description.getText().toString());
        editedTask.setDueDate(dueDate);
        editedTask.setPriority(priority.getSelectedItemPosition());

        Call editTask = apiService.editTask(SharedPrefsUtil.getPreferencesField(getApplicationContext(), SharedPrefsUtil.TOKEN), editedTask);

        editTask.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    realm.beginTransaction();
                    task.setTitle(title.getText().toString());
                    task.setDescription(description.getText().toString());
                    task.setDueDate(dueDate);
                    task.setPriority(priority.getSelectedItemPosition());
                    realm.commitTransaction();
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network error, not edited.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }

}
