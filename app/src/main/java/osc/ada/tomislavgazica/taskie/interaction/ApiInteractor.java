package osc.ada.tomislavgazica.taskie.interaction;

import osc.ada.tomislavgazica.taskie.model.LoginResponse;
import osc.ada.tomislavgazica.taskie.model.RegistrationToken;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskList;
import retrofit2.Callback;

public interface ApiInteractor {

    void registerUser(RegistrationToken registerRequest, Callback<RegistrationToken> callback);

    void loginUser(RegistrationToken loginRequest, Callback<LoginResponse> callback);

    void getOtherTasks(Callback<TaskList> callback, String token);

    void getFavoriteTasks(Callback<TaskList> callback, String token);

    void addNewTask(Task task, Callback<Task> callback, String token);

    void editTask(Task task, Callback<Task> callback, String token);

    void changeIsFavorite(Callback<RegistrationToken> callback, String id, String token);

    void changeIsCompleted(Callback<RegistrationToken> callback, String id, String token);

    void changePriorityLevel(Callback<Task> callback, String token, String id, int priority);

    void deleteTask(Callback<Task> callback, String token, String id);

}
