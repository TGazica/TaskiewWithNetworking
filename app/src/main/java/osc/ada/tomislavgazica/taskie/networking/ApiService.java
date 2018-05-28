package osc.ada.tomislavgazica.taskie.networking;

import osc.ada.tomislavgazica.taskie.model.LoginResponse;
import osc.ada.tomislavgazica.taskie.model.RegistrationToken;
import osc.ada.tomislavgazica.taskie.model.Task;
import osc.ada.tomislavgazica.taskie.model.TaskList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/register/")
    Call<RegistrationToken> registerUser(@Body RegistrationToken registrationToken);

    @POST("api/login/")
    Call<LoginResponse> loginUser(@Body RegistrationToken registrationToken);

    @POST("api/note/")
    Call<Task> postNewTask(@Header("authorization") String header, @Body Task task);

    @GET("api/note/")
    Call<TaskList> getOtherTasks(@Header("authorization") String header);

    @GET("api/note/favorite")
    Call<TaskList> getFavoriteTasks(@Header("authorization") String header);

    @POST("api/note/favorite")
    Call<RegistrationToken> changeIsFavorite(@Header("authorization") String header, @Query("id") String id);

    @POST("api/note/complete")
    Call<RegistrationToken> changeIsCompleted(@Header("authorization") String header, @Query("id") String id);

    @POST("api/note/edit")
    @FormUrlEncoded
    Call<Task> changePriorityLevel(@Header("authorization") String header, @Field("id") String id, @Field("taskPriority") int taskPriority);

    @POST("api/note/delete")
    Call<Task> deleteTask(@Header("authorization") String header, @Query("id") String id);

    @POST("api/note/edit")
    Call<Task> editTask(@Header("authorization") String header, @Body Task task);

}
