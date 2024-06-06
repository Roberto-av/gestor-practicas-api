package com.app.seeders;

import com.app.persistence.entities.groups.GroupEntity;
import com.app.persistence.entities.groups.TaskEntity;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.GroupRepository;
import com.app.persistence.repositories.TaskRepository;
import com.app.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CreateTasks {

    private final TaskRepository taskRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public CreateTasks(TaskRepository taskRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public void createAllTasks() {
        List<GroupEntity> groups = groupRepository.findAll();
        Optional<UserEntity> userOptional = userRepository.findById(1L);

        if (groups.isEmpty() || userOptional.isEmpty()) {
            throw new RuntimeException("No hay grupos o susurios disponibles");
        }

        UserEntity user = userOptional.get();

        String title = "Tarea";
        String description = "Subir la tarea";

        List<TaskEntity> tasks = new ArrayList<>();

        for (GroupEntity group : groups) {
            for (int i = 1; i <= 5; i++) {
                TaskEntity task = TaskEntity.builder()
                        .tittle(title + " " + i)
                        .description(description + " " + i)
                        .initialDate(LocalDateTime.of(2024, 8, 5, 8, 0))
                        .endDate(LocalDateTime.of(2024, 8, 15, 23, 59))
                        .group(group)
                        .user(user)
                        .build();
                tasks.add(task);
            }
        }

        taskRepository.saveAll(tasks);
    }
}
