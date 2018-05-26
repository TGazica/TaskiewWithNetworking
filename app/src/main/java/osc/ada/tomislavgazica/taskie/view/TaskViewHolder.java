package osc.ada.tomislavgazica.taskie.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.realm.Realm;
import osc.ada.tomislavgazica.taskie.R;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskPriority;
import osc.ada.tomislavgazica.taskie.networking.ApiService;
import osc.ada.tomislavgazica.taskie.networking.RetrofitUtil;
import osc.ada.tomislavgazica.taskie.util.SharedPrefsUtil;
import osc.ada.tomislavgazica.taskie.util.TaskClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textview_item_title)
    TextView title;

    @BindView(R.id.textview_item_content)
    TextView description;

    @BindView(R.id.textview_item_dueDate)
    TextView date;

    @BindView(R.id.togglebutton_item_status)
    ToggleButton status;

    @BindView(R.id.imagebutton_item_priority)
    ImageButton priority;

    @BindView(R.id.switch_item_favorite)
    Switch favorite;


    private Task item;
    private TaskClickListener listener;
    private Context context;

    private Realm realm = Realm.getDefaultInstance();

    public TaskViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setListener(TaskClickListener listener) {
        this.listener = listener;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setItem(Task current) {
        this.item = realm.where(Task.class).equalTo("ID", current.getID()).findFirst();

        if (item != null) {
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            date.setText(item.getDueDate());

            int color = R.color.taskPriority_unknown;
            switch (current.getTaskPriorityInt()) {
                case LOW:
                    color = R.color.taskpriority_low;
                    break;
                case MEDIUM:
                    color = R.color.taskpriority_medium;
                    break;
                case HIGH:
                    color = R.color.taskpriority_high;
                    break;
            }
            priority.setImageResource(color);
        }
    }

    @OnClick(R.id.imagebutton_item_priority)
    public void onPriorityClick(){
        realm.beginTransaction();


        final Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call isCompleted = apiService.changePriorityLevel(SharedPrefsUtil.getPreferencesField(context,SharedPrefsUtil.TOKEN), item.getID(), item.getPriority());

        isCompleted.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                changePriority();
                if (listener != null) {
                    listener.onPriorityClick(item);
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(context, "Network error, not edited.", Toast.LENGTH_SHORT).show();
            }
        });

        realm.commitTransaction();

    }

    public void changePriority(){
        realm.beginTransaction();
        TaskPriority priority = item.getTaskPriorityInt();
        if (priority == TaskPriority.LOW) {
            item.setPriority(TaskPriority.MEDIUM.ordinal());
            this.priority.setImageResource(R.color.taskpriority_medium);
        } else if (priority == TaskPriority.MEDIUM) {
            item.setPriority(TaskPriority.HIGH.ordinal());
            this.priority.setImageResource(R.color.taskpriority_high);
        } else if (priority == TaskPriority.HIGH) {
            item.setPriority(TaskPriority.LOW.ordinal());
            this.priority.setImageResource(R.color.taskpriority_low);
        }
        realm.commitTransaction();
    }

    @OnClick(R.id.togglebutton_item_status)
    public void onStatusClick() {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call isCompleted = apiService.changeIsCompleted(SharedPrefsUtil.getPreferencesField(context,SharedPrefsUtil.TOKEN), item.getID());

        isCompleted.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                realm.beginTransaction();
                item.setCompleted(!item.isCompleted());
                realm.commitTransaction();


                if (listener != null) {
                    listener.onStatusClick(item);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(context, "Network error, not edited.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @OnClick(R.id.switch_item_favorite)
    public void onFavoriteClick(){
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call isFavorite = apiService.changeIsFavorite(SharedPrefsUtil.getPreferencesField(context,SharedPrefsUtil.TOKEN), item.getID());

        isFavorite.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                realm.beginTransaction();
                item.setFavorite(!item.isFavorite());
                realm.commitTransaction();

                if(listener != null){
                    listener.onFavoriteClick(item);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(context, "Network error, not edited.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @OnLongClick
    public boolean onTaskLongClick() {
        if (listener != null) {
            listener.onLongClick(item);
        }
        return true;
    }
}
