package com.example.a101;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context context;

    // Constructor
    public HistoryViewAdapter(List<Task> tasks, Context context){
        this.tasks = tasks;
        this.context = context;
    }

    // Create new views
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_history_card, parent, false);
        return new ViewHolder(itemView);
    }

    // Replace the contents of a view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the contents of the view with that element
        holder.taskTitle.setText(tasks.get(position).getTitle());
        holder.taskDescription.setText(tasks.get(position).getDescription());
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() { return tasks.size(); }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView taskTitle;
        private TextView taskDescription;
        private ImageButton dropDownButton;
        private RecyclerView questionRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            dropDownButton = itemView.findViewById(R.id.dropDownButton);
            questionRecyclerView = itemView.findViewById(R.id.questionRecyclerView);

            // Set click listener for dropDownButton
            dropDownButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (questionRecyclerView.getVisibility() == View.VISIBLE) {
                        questionRecyclerView.setVisibility(View.GONE);
                    } else {
                        questionRecyclerView.setVisibility(View.VISIBLE);
                        // Get the position of the item clicked
                        int position = getAdapterPosition();
                        // Get the task associated with the clicked item
                        Task clickedTask = tasks.get(position);
                        // Set up the RecyclerView with the adapter and layout manager
                        QuestionHistoryViewAdapter adapter = new QuestionHistoryViewAdapter(clickedTask.getQuestions(), context);
                        questionRecyclerView.setAdapter(adapter);
                        questionRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }
                }
            });
        }
    }
}
