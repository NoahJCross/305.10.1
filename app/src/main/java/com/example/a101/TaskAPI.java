package com.example.a101;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskAPI {
    @GET("addTask/{userId}")
    Call<Task> addTask(@Path("userId") String userId, @Query("topic") String topic);

    @GET("getCompletedTasks/{userId}")
    Call<List<Task>> getCompletedTasks(@Path("userId") String userId);

    @GET("getNonCompletedTasks/{userId}")
    Call<List<Task>> getNonCompletedTasks(@Path("userId") String userId);

    @GET("getTaskById/{taskId}")
    Call<Task> getTaskById(@Path("taskId") String taskId);

    @POST("addUser")
    Call<User> addUser(@Body UserRequest userRequest);

    @POST("login")
    Call<User> login(@Body LoginData loginData);

    @POST("getFeedback")
    Call<Task> getFeedback(@Body Task task);
}
