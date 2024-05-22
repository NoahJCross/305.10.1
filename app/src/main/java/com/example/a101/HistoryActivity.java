package com.example.a101;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private Button backButton;
    private RecyclerView taskHistoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        taskHistoryRecyclerView = findViewById(R.id.taskHistoryRecyclerView);

        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Retrieve tasks from Task class
        List<Task> tasks = Task.getInstances();

        // Set up RecyclerView with task history
        taskHistoryRecyclerView.setVisibility(View.VISIBLE);
        HistoryViewAdapter adapter = new HistoryViewAdapter(tasks, HistoryActivity.this);
        taskHistoryRecyclerView.setAdapter(adapter);
        taskHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
    }
}
