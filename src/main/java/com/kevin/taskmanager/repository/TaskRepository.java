package com.kevin.taskmanager.repository;

import com.kevin.taskmanager.model.Task;
import com.kevin.taskmanager.model.TaskPriority;
import com.kevin.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);
    boolean existsByTitleIgnoreCase(String title);
    boolean existsByTitleIgnoreCaseAndTaskIdNot(String title, Long taskId);
}
