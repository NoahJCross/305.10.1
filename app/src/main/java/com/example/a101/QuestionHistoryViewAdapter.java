package com.example.a101;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionHistoryViewAdapter extends RecyclerView.Adapter<QuestionHistoryViewAdapter.ViewHolder> {

    private List<Question> questions;
    private Context context;

    // Constructor
    public QuestionHistoryViewAdapter(List<Question> questions, Context context){
        this.questions = questions;
        this.context = context;
    }

    // Create new views
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View itemView = LayoutInflater.from(context).inflate(R.layout.question_history_card, parent, false);
        return new ViewHolder(itemView);
    }

    // Replace the contents of a view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from the dataset at this position and replace the contents of the view with that element
        Question question = questions.get(position);
        holder.questionTitleTextView.setText("Question " + (position + 1) + ".");
        holder.questionTextView.setText(question.getQuestion());
        holder.questionFeedbackTextView.setText(question.getFeedback());
        holder.answer1CheckBox.setText(question.getAnswer1());
        holder.answer2CheckBox.setText(question.getAnswer2());
        holder.answer3CheckBox.setText(question.getAnswer3());
        holder.answer1CheckBox.setButtonTintList(ContextCompat.getColorStateList(context, android.R.color.white));
        holder.answer2CheckBox.setButtonTintList(ContextCompat.getColorStateList(context, android.R.color.white));
        holder.answer3CheckBox.setButtonTintList(ContextCompat.getColorStateList(context, android.R.color.white));
        applyCheckBoxColors(holder, question);
    }

    // Return the size of the dataset
    @Override
    public int getItemCount() { return questions.size(); }


    private void applyCheckBoxColors(ViewHolder holder, Question question) {
        String correctAnswer = question.getCorrectAnswer();
        String usersAnswer = question.getUsersAnswer();

        CheckBox correctCheckBox = null;
        CheckBox usersCheckBox = null;

        if (question.getAnswer1().equals(correctAnswer)) {
            correctCheckBox = holder.answer1CheckBox;
        } else if (question.getAnswer2().equals(correctAnswer)) {
            correctCheckBox = holder.answer2CheckBox;
        } else if (question.getAnswer3().equals(correctAnswer)) {
            correctCheckBox = holder.answer3CheckBox;
        }

        if (question.getAnswer1().equals(usersAnswer)) {
            usersCheckBox = holder.answer1CheckBox;
        } else if (question.getAnswer2().equals(usersAnswer)) {
            usersCheckBox = holder.answer2CheckBox;
        } else if (question.getAnswer3().equals(usersAnswer)) {
            usersCheckBox = holder.answer3CheckBox;
        }

        if (correctAnswer.equals(usersAnswer)) {
            // Correct answer case
            if (correctCheckBox != null) {
                correctCheckBox.setButtonTintList(ContextCompat.getColorStateList(context, android.R.color.holo_green_light));
                correctCheckBox.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
                correctCheckBox.setChecked(true);
            }
        } else {
            // Incorrect answer case
            if (usersCheckBox != null) {
                usersCheckBox.setButtonTintList(ContextCompat.getColorStateList(context, android.R.color.holo_red_light));
                usersCheckBox.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
                usersCheckBox.setChecked(true);
            }
            if (correctCheckBox != null) {
                correctCheckBox.setButtonTintList(ContextCompat.getColorStateList(context, android.R.color.holo_green_light));
                correctCheckBox.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
                correctCheckBox.setChecked(true);
            }
        }
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView questionTitleTextView;
        private TextView questionTextView;
        private TextView questionFeedbackTextView;
        private CheckBox answer1CheckBox;
        private CheckBox answer2CheckBox;
        private CheckBox answer3CheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            questionTitleTextView = itemView.findViewById(R.id.questionTitleTextView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            questionFeedbackTextView = itemView.findViewById(R.id.questionFeedbackTextView);
            answer1CheckBox = itemView.findViewById(R.id.answer1CheckBox);
            answer2CheckBox = itemView.findViewById(R.id.answer2CheckBox);
            answer3CheckBox = itemView.findViewById(R.id.answer3CheckBox);
        }
    }
}
