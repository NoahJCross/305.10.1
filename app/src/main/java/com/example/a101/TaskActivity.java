package com.example.a101;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class TaskActivity extends AppCompatActivity implements TaskAPIService.FeedbackResponseListener {
    private TextView taskTitleTextView;
    private TextView taskDescriptionTextView;
    private Button submitButton;
    private FrameLayout taskLoaderFrame;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private String currentQuestionAnswer = "";
    private Task task;
    private String taskId;

    private TaskAPIService taskAPIService;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        taskAPIService = new TaskAPIService();

        // Retrieve task ID from intent
        Intent intent = getIntent();
        taskId = intent.getStringExtra("task_id");

        taskTitleTextView = findViewById(R.id.taskTitleTextView);
        taskDescriptionTextView = findViewById(R.id.taskDescriptionTextView);
        taskLoaderFrame = findViewById(R.id.taskLoaderFrame);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex == questions.size() - 1) {
                    Question question = questions.get(currentQuestionIndex);
                    question.setUsersAnswer(currentQuestionAnswer);
                    taskLoaderFrame.setVisibility(View.VISIBLE);
                    taskAPIService.getFeedback(task, TaskActivity.this);
                }
            }
        });

        task = Task.getInstance();
        // Set task title and description
        taskTitleTextView.setText(task.getTitle());
        taskDescriptionTextView.setText(task.getDescription());

        questions = task.getQuestions();
        // Display the first question
        displayQuestion(currentQuestionIndex);
    }

    // Move to the next question
    public void NextQuestion() {
        if (!currentQuestionAnswer.isEmpty()) {
            Question question = questions.get(currentQuestionIndex);
            question.setUsersAnswer(currentQuestionAnswer);
            currentQuestionIndex++;
            currentQuestionAnswer = "";
            displayQuestion(currentQuestionIndex);
        } else {
            Toast.makeText(this, "Please answer the current question.", Toast.LENGTH_SHORT).show();
        }
    }

    // Move to the previous question
    public void PrevQuestion() {
        if (currentQuestionIndex == questions.size() - 1) {
            submitButton.setVisibility(View.GONE);
        }
        currentQuestionIndex--;
        currentQuestionAnswer = "";
        displayQuestion(currentQuestionIndex);
    }

    // Display a question fragment
    private void displayQuestion(int index) {
        QuestionFragment fragment = QuestionFragment.newInstance(questions, currentQuestionIndex);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
    }

    // Update the current question's answer
    public void UpdateAnswer(String answer) {
        currentQuestionAnswer = answer;
    }

    @Override
    public void onFeedbackReceived(Task task) {
        // Start results activity
        Task.setInstance(task);
        Intent intent = new Intent(TaskActivity.this, ResultsActivity.class);
        intent.putExtra("task_id", taskId);
        startActivity(intent);
    }
    @Override
    public void onFeedbackError(Exception e) {
        taskAPIService.getFeedback(task, this);
        Log.e("TaskActivity", "Failed to get feedback, trying again." + e.getMessage());
    }
}
