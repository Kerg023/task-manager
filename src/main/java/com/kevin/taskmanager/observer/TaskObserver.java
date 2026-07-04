package com.kevin.taskmanager.observer;

import com.kevin.taskmanager.model.Task;

public interface TaskObserver {
    void onTaskCreated(Task task);
    void onTaskUpdated(Task task);
    void onTaskDeleted(Long id);
}