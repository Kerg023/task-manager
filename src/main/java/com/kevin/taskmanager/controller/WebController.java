package com.kevin.taskmanager.controller;

import com.kevin.taskmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    private final TaskService taskService;

    public WebController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("tasks", taskService.getAllTasks());

        return "index";
    }
}
