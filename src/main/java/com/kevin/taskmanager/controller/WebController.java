package com.kevin.taskmanager.controller;

import com.kevin.taskmanager.model.Task;
import com.kevin.taskmanager.model.TaskPriority;
import com.kevin.taskmanager.model.TaskStatus;
import com.kevin.taskmanager.observer.SseTaskObserver;
import com.kevin.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Controller
public class WebController {

    private final TaskService taskService;
    private final SseTaskObserver sseTaskObserver;

    public WebController(TaskService taskService, SseTaskObserver sseTaskObserver) {
        this.taskService = taskService;
        this.sseTaskObserver = sseTaskObserver;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/stream")
    public SseEmitter streamTasks() {
        return sseTaskObserver.subscribe();
    }

    @GetMapping("/tasks")
    public String listTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            Model model) {

        List<Task> tasks = status != null
                ? taskService.getTasksByStatus(status)
                : taskService.getAllTasks();

        if (priority != null) {
            tasks = tasks.stream()
                    .filter(task -> priority.equals(task.getPriority()))
                    .toList();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPriority", priority);

        return "tasks";
    }

    @GetMapping("/tasks/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());
        return "task-form";
    }

    @GetMapping("/tasks/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());
        return "task-form";
    }

    @PostMapping("/tasks")
    public String createTask(@Valid @ModelAttribute Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());
            return "task-form";
        }

        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}")
    public String updateTask(@PathVariable Long id, @Valid @ModelAttribute Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());
            return "task-form";
        }

        taskService.updateTask(id, task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}