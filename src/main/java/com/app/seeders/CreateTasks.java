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
            throw new RuntimeException("No hay grupos o usuarios disponibles");
        }

        UserEntity user = userOptional.get();

        String title = "Tarea";
        String description = "Subir la tarea";

        List<TaskEntity> tasks = new ArrayList<>();

        for (GroupEntity group : groups) {
            for (int i = 1; i <= 5; i++) {
                String taskTitle = title + " " + i;
                String taskDescription = description + " " + i;

                // Check if task already exists for this group and parameters
                TaskEntity existingTask = taskRepository.findFirstByTittleAndDescriptionAndGroup(taskTitle, taskDescription, group);
                if (existingTask != null) {
                    // If task exists, log a message and skip adding it again
                    System.out.println("Task with title '" + taskTitle + "' and description '" + taskDescription + "' already exists for group " + group.getName() + ". Skipping...");
                    continue;
                }

                TaskEntity task = TaskEntity.builder()
                        .tittle(taskTitle)
                        .description(taskDescription)
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
