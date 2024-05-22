package com.example.a101;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    // Declaring variables for UI elements
    private TextView profileUsernameTextView;
    private TextView profileEmailTextView;
    private TextView questionsAnswered;
    private TextView correctlyAnswered;
    private TextView taskCount;
    private Button backButton;
    private Button shareButton;
    private CardView historyCardView;
    private CardView upgradeCardView;
    private TaskAPIService taskAPIService;
    private FrameLayout historyLoaderFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initializing TaskAPIService
        taskAPIService = new TaskAPIService();

        // Getting current user instance and setting profile information
        User user = User.getInstance();
        profileEmailTextView = findViewById(R.id.profileEmailTextView);
        profileEmailTextView.setText(user.getEmail());
        profileUsernameTextView = findViewById(R.id.profileUsernameTextView);
        profileUsernameTextView.setText(user.getUsername());
        questionsAnswered = findViewById(R.id.questionsAnswered);
        correctlyAnswered = findViewById(R.id.correctlyAnswered);
        taskCount = findViewById(R.id.taskCount);
        historyLoaderFrame = findViewById(R.id.historyLoaderFrame);

        // Setting click listeners for history and upgrade card views
        historyCardView = findViewById(R.id.historyCardView);
        historyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to HistoryActivity when history card view is clicked
                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        upgradeCardView = findViewById(R.id.upgradeCardView);
        upgradeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UpgradeActivity when upgrade card view is clicked
                Intent intent = new Intent(ProfileActivity.this, UpgradeActivity.class);
                startActivity(intent);
            }
        });

        // Loading user's completed tasks and updating UI
        taskAPIService.getCompletedTasks(user.getId(), new TaskAPIService.GetTasksResponseListener() {
            @Override
            public void onTasksReceived(List<Task> tasks) {
                int totalQuestionsAnswered = 0;
                int totalCorrectlyAnswered = 0;
                int totalTasks = tasks.size();
                Task.setInstances(tasks);
                for (Task task : tasks) {
                    // Iterate over each question in the task
                    for (Question question : task.getQuestions()) {
                        totalQuestionsAnswered++; // Increment total questions answered
                        // Check if user's answer matches correct answer
                        String correctAnswer = question.getCorrectAnswer();
                        String usersAnswer = question.getUsersAnswer();
                        if (correctAnswer != null && correctAnswer.equals(usersAnswer)) {
                            totalCorrectlyAnswered++; // Increment correctly answered count
                        }
                    }
                }
                // Update UI with task statistics
                questionsAnswered.setText(String.valueOf(totalQuestionsAnswered));
                correctlyAnswered.setText(String.valueOf(totalCorrectlyAnswered));
                taskCount.setText(String.valueOf(totalTasks));
                historyLoaderFrame.setVisibility(View.GONE);
            }

            @Override
            public void onTasksError(Exception e) {
                // Show error message if failed to retrieve history
                Toast.makeText(ProfileActivity.this, "Failed to retrieve history: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Setting click listener for back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Setting click listener for share button
        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Share user's profile when share button is clicked
                shareProfile();
            }
        });
    }

    // Method to share user's profile
    private void shareProfile() {
        String shareText = "Check out my profile on the Personalized Learning Experiences App: "
                + profileUsernameTextView.getText().toString() + "\n"
                + "Total Questions Answered: " + questionsAnswered.getText().toString() + "\n"
                + "Correctly Answered: " + correctlyAnswered.getText().toString() + "\n"
                + "Total Tasks: " + taskCount.getText().toString();

        // Create intent to share user's profile
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Profile");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        // Start activity to choose app to share profile
        startActivity(Intent.createChooser(shareIntent, "Share Profile"));
    }
}
