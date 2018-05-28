package osc.ada.tomislavgazica.taskie.interaction;

import osc.ada.tomislavgazica.taskie.model.LoginResponse;
import osc.ada.tomislavgazica.taskie.model.RegistrationToken;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskList;
import osc.ada.tomislavgazica.taskie.networking.ApiService;
import retrofit2.Callback;

public class ApiInteractorImpl implements ApiInteractor {

    private final ApiService apiService;

    public ApiInteractorImpl(ApiService apiService){
        this.apiService = apiService;
    }

    @Override
    public void registerUser(RegistrationToken registerRequest, Callback<RegistrationToken> callback) {
        apiService.registerUser(registerRequest).enqueue(callback);
    }

    @Override
    public void loginUser(RegistrationToken loginRequest, Callback<LoginResponse> callback) {
        apiService.loginUser(loginRequest).enqueue(callback);
    }

    @Override
    public void getOtherTasks(Callback<TaskList> callback, String token) {
        apiService.getOtherTasks(token).enqueue(callback);
    }

    @Override
    public void getFavoriteTasks(Callback<TaskList> callback, String token) {
        apiService.getFavoriteTasks(token).enqueue(callback);
    }

    @Override
    public void addNewTask(Task task, Callback<Task> callback, String token) {
        apiService.postNewTask(token,task).enqueue(callback);
    }

    @Override
    public void editTask(Task task, Callback<Task> callback, String token) {
        apiService.editTask(token,task).enqueue(callback);
    }

    @Override
    public void changeIsFavorite(Callback<RegistrationToken> callback, String id, String token) {
        apiService.changeIsFavorite(token, id).enqueue(callback);
    }

    @Override
    public void changeIsCompleted(Callback<RegistrationToken> callback, String id, String token) {
        apiService.changeIsCompleted(token, id).enqueue(callback);
    }

    @Override
    public void changePriorityLevel(Callback<Task> callback, String token, String id, int priority) {
        apiService.changePriorityLevel(token, id, priority).enqueue(callback);
    }

    @Override
    public void deleteTask(Callback<Task> callback, String token, String id) {
        apiService.deleteTask(token, id).enqueue(callback);
    }
}
