package com.example.a101;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    private RecyclerView resultRecyclerView;
    private Button continueButton;
    private List<Result> results = new ArrayList<Result>();

    private TaskAPIService taskAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        taskAPIService = new TaskAPIService();
        // Get task ID from intent
        String taskId = getIntent().getStringExtra("task_id");

        // Initialize continue button and set click listener to navigate to DueTasksActivity
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, DueTasksActivity.class);
                startActivity(intent);
            }
        });


        Task task = Task.getInstance();
        List<Question> questions = task.getQuestions();
        // Populate results list with questions and feedback
        for (Question question : questions) {
            Result result = new Result(question.getQuestion(), question.getFeedback());
            results.add(result);
        }

        // Initialize RecyclerView and set adapter for displaying results
        resultRecyclerView = findViewById(R.id.resultsRecyclerView);
        ResultViewAdapter adapter = new ResultViewAdapter(results, ResultsActivity.this);
        resultRecyclerView.setAdapter(adapter);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(ResultsActivity.this));
    }
}
