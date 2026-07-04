package com.kevin.taskmanager.service;

import com.kevin.taskmanager.model.Task;
import com.kevin.taskmanager.model.TaskPriority;
import com.kevin.taskmanager.model.TaskStatus;
import com.kevin.taskmanager.observer.TaskObserver;
import com.kevin.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final List<TaskObserver> observers = new CopyOnWriteArrayList<>();

    public TaskService(TaskRepository taskRepository, TaskObserver sseTaskObserver) {
        this.taskRepository = taskRepository;
        this.observers.add(sseTaskObserver);
    }

    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream()
                .filter(task -> task != null)
                .toList();
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Task saveTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (taskRepository.existsByTitleIgnoreCase(task.getTitle())) {
            throw new DuplicateTaskTitleException(task.getTitle());
        }
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }
        if (task.getPriority() == null) {
            task.setPriority(TaskPriority.MEDIUM);
        }

        Task saved = taskRepository.save(task);
        observers.forEach(o -> o.onTaskCreated(saved));
        return saved;
    }

    public Task createTask(Task task) {
        return saveTask(task);
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Task task = getTaskById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (taskRepository.existsByTitleIgnoreCaseAndTaskIdNot(updatedTask.getTitle(), taskId)) {
            throw new DuplicateTaskTitleException(updatedTask.getTitle());
        }

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        if (updatedTask.getStatus() != null) {
            task.setStatus(updatedTask.getStatus());
        }
        if (updatedTask.getPriority() != null) {
            task.setPriority(updatedTask.getPriority());
        }
        task.setDueDate(updatedTask.getDueDate());

        Task saved = taskRepository.save(task);
        observers.forEach(o -> o.onTaskUpdated(saved));
        return saved;
    }

    public void deleteTask(Long taskId) {
        Task task = getTaskById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
        observers.forEach(o -> o.onTaskDeleted(taskId));
    }

    public Task completeTask(Long taskId) {
        Task task = getTaskById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.COMPLETED);
        Task saved = taskRepository.save(task);
        observers.forEach(o -> o.onTaskUpdated(saved));
        return saved;
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .filter(task -> task != null)
                .toList();
    }

    public List<Task> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority).stream()
                .filter(task -> task != null)
                .toList();
    }
}