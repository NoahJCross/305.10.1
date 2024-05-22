package com.example.a101;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TaskAPIService  {

    private static final String BASE_URL = "http://192.168.0.3:5000/";

    private TaskAPI taskAPI;

    // Define interfaces for response callbacks
    public interface TaskResponseListener {
        void onTaskAdded(Task task);
        void onTaskError(Exception e);
    }

    // Interface for receiving list of tasks
    public interface GetTasksResponseListener {
        void onTasksReceived(List<Task> tasks);
        void onTasksError(Exception e);
    }

    // Interface for user-related responses
    public interface UserResponseListener {
        void onUserAdded(User user);
        void onUserError(Exception e);
    }

    // Interface for login responses
    public interface LoginResponseListener {
        void onLoginSuccess(User user);
        void onLoginFailure(Exception e);
    }

    // Interface for feedback responses
    public interface FeedbackResponseListener {
        void onFeedbackReceived(Task task);
        void onFeedbackError(Exception e);
    }

    // Interface for receiving a single task by its ID
    public interface GetTaskByIdListener {
        void onTaskReceived(Task task);
        void onTaskError(Exception e);
    }

    // Constructor
    public TaskAPIService() {
        // Create OkHttpClient with custom settings
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .addInterceptor(new RetryInterceptor(3)) // Add retry interceptor
                .build();

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient) // Set custom OkHttpClient
                .build();

        // Create TaskAPI instance
        taskAPI = retrofit.create(TaskAPI.class);
    }

    // Method to get feedback for a task
    public void getFeedback(Task task, FeedbackResponseListener listener) {
        Call<Task> call = taskAPI.getFeedback(task);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Task taskResponse = response.body();
                    listener.onFeedbackReceived(taskResponse);
                } else {
                    listener.onFeedbackError(new Exception("Failed to fetch feedback. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {
                listener.onFeedbackError(new Exception("Failed to fetch feedback. Error: " + t.getMessage()));
            }
        });

    }

    // Method to add a task
    public void addTask(String userId, String topic, TaskResponseListener listener) {
        Call<Task> call = taskAPI.addTask(userId, topic);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Task task = response.body();
                    listener.onTaskAdded(task);
                } else {
                    listener.onTaskError(new Exception("Failed to add task. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure( @NonNull Call<Task> call, @NonNull Throwable t) {
                listener.onTaskError(new Exception("Failed to add task. Error: " + t.getMessage()));
            }
        });
    }

    // Method to get a task by its ID
    public void getTaskById(String taskId, GetTaskByIdListener listener) {
        Call<Task> call = taskAPI.getTaskById(taskId);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(@NonNull Call<Task> call, @NonNull Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Task task = response.body();
                    listener.onTaskReceived(task);
                } else {
                    listener.onTaskError(new Exception("Failed to fetch task. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Task> call, @NonNull Throwable t) {
                listener.onTaskError(new Exception("Failed to fetch task. Error: " + t.getMessage()));
            }
        });
    }

    // Method to get completed tasks for a user
    public void getCompletedTasks(String userId, GetTasksResponseListener listener) {
        Call<List<Task>> call = taskAPI.getCompletedTasks(userId);
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body();
                    listener.onTasksReceived(tasks);
                } else {
                    listener.onTasksError(new Exception("Failed to get tasks history. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                listener.onTasksError(new Exception("Failed to get completed tasks history. Error: " + t.getMessage()));
            }
        });
    }

    // Method to get non-completed tasks for a user
    public void getNonCompletedTasks(String userId, GetTasksResponseListener listener) {
        Call<List<Task>> call = taskAPI.getNonCompletedTasks(userId);
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(@NonNull Call<List<Task>> call, @NonNull Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body();
                    listener.onTasksReceived(tasks);
                } else {
                    listener.onTasksError(new Exception("Failed to get tasks. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Task>> call, @NonNull Throwable t) {
                listener.onTasksError(new Exception("Failed to get tasks. Error: " + t.getMessage()));
            }
        });
    }

    // Method to add a user
    public void addUser(UserRequest userRequest, UserResponseListener listener) {
        Call<User> call = taskAPI.addUser(userRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    listener.onUserAdded(user);
                } else {
                    listener.onUserError(new Exception("Failed to add user. Response code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                listener.onUserError(new Exception("Failed to add user. Error: " + t.getMessage()));
            }
        });
    }

    // Method to perform user login
    public void login(LoginData loginData, LoginResponseListener listener) {
        Call<User> call = taskAPI.login(loginData);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    listener.onLoginSuccess(user);
                } else {
                    listener.onLoginFailure(new Exception("Failed to login. Error code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                listener.onLoginFailure(new Exception("Failed to login. Error: " + t.getMessage()));
            }
        });
    }
}
