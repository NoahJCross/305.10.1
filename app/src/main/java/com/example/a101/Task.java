package com.example.a101;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private static Task instance;
    private static List<Task> instances;
    private String id;
    private String userId;
    private String title;
    private String description;

    private List<Question> questions;

    // Default constructor
    public Task() {
    }

    public static Task getInstance() {
        if (instance == null) {
            instance = new Task();
        }
        return instance;
    }

    // Singleton pattern: static method to get instance with parameters
    public static Task getInstance(String id, String userId, String title, String description, List<Question> questions) {
        if (instance == null) {
            instance = new Task();
            instance.id = id;
            instance.userId = userId;
            instance.title = title;
            instance.description = description;
            instance.questions = questions;
        }
        return instance;
    }

    public static List<Task> getInstances(){
        if(instances == null){
            instances = new ArrayList<Task>();
        }
        return instances;
    }

    public static Task setInstance(Task task) {
        instance = task;
        return instance;
    }

    public static List<Task> setInstances(List<Task> tasks) {
        instances = tasks;
        return instances;
    }


    // Constructor with parameters to initialize the task
    public Task(String userId, String title, String description) {
        this.userId = userId;
        this.title = title;
        this.description = description;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
