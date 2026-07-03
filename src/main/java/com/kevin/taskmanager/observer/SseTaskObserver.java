package com.kevin.taskmanager.observer;

import com.kevin.taskmanager.model.Task;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseTaskObserver implements TaskObserver {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
        return emitter;
    }

    @Override
    public void onTaskCreated(Task task) {
        broadcast("task-created", task);
    }

    @Override
    public void onTaskUpdated(Task task) {
        broadcast("task-updated", task);
    }

    @Override
    public void onTaskDeleted(Long id) {
        broadcast("task-deleted", id);
    }

    private void broadcast(String eventName, Object data) {
        List<SseEmitter> muertos = new java.util.ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                muertos.add(emitter);
            }
        }
        emitters.removeAll(muertos);
    }
}