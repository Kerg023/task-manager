package com.kevin.taskmanager.service;

public class DuplicateTaskTitleException extends RuntimeException {

    public DuplicateTaskTitleException(String title) {
        super("Ya existe una tarea con el título \"" + title + "\"");
    }
}