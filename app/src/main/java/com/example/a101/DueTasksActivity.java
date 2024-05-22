package com.example.a101;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DueTasksActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView taskAmountTextView;
    private RecyclerView dueTaskRecyclerView;
    private FrameLayout dueTasksLoaderFrame;
    private TaskAPIService taskAPIService;
    private ImageView profileLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_tasks);
        String userId = User.getInstance().getId();

        taskAPIService = new TaskAPIService();
        // Initialize views
        usernameTextView = findViewById(R.id.usernameTextView);
        taskAmountTextView = findViewById(R.id.taskAmountTextView);
        dueTaskRecyclerView = findViewById(R.id.dueTaskRecyclerView);
        dueTasksLoaderFrame = findViewById(R.id.dueTasksLoaderFrame);

        // Profile Link
        profileLink = findViewById(R.id.profileLink);
        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DueTasksActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Retrieve tasks for the current user
        taskAPIService.getNonCompletedTasks(userId, new TaskAPIService.GetTasksResponseListener() {
            @Override
            public void onTasksReceived(List<Task> tasks) {
                // Set username text
                String username = User.getInstance().getUsername();
                if (username != null && !username.isEmpty()) {
                    username = username.substring(0, 1).toUpperCase() + username.substring(1);
                }
                usernameTextView.setText(username);
                // Set task amount text
                taskAmountTextView.setText("You have " + tasks.size() + (tasks.size() != 1 ? " tasks" : " task") + " due");

                // Set up RecyclerView with DueTaskViewAdapter
                DueTaskViewAdapter adapter = new DueTaskViewAdapter(tasks, DueTasksActivity.this);
                dueTaskRecyclerView.setAdapter(adapter);
                dueTaskRecyclerView.setLayoutManager(new LinearLayoutManager(DueTasksActivity.this));
                dueTasksLoaderFrame.setVisibility(View.GONE);
            }

            @Override
            public void onTasksError(Exception e) {
                // Show error message
                Toast.makeText(DueTasksActivity.this, "Failed to retrieve tasks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
