package com.app.seeders;

import com.app.persistence.entities.groups.GroupEntity;
import com.app.persistence.repositories.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CreateGroups {

    private static final Logger logger = LoggerFactory.getLogger(CreateGroups.class);

    private final GroupRepository groupRepository;

    public CreateGroups(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Transactional
    public void createAllGroups() {
        saveGroupIfNotExists("IDS 9no TV", "Grupo de ids turno vestertino");
        saveGroupIfNotExists("IDS 9no TM", "Grupo de ids turno matutino");
        saveGroupIfNotExists("ITC 9no TV", "Grupo de itc turno vestertino");
        saveGroupIfNotExists("ITC 9no TM", "Grupo de itc turno vestertino");
    }

    private void saveGroupIfNotExists(String name, String description) {
        List<GroupEntity> existingGroups = groupRepository.findByNameAndDescription(name, description);
        if (existingGroups.isEmpty()) {
            GroupEntity group = new GroupEntity().builder()
                    .name(name)
                    .description(description)
                    .build();
            groupRepository.save(group);
            logger.info("Group {} - {} created successfully", name, description);
        } else {
            logger.info("Group {} - {} already exists. Skipping...", name, description);
        }
    }
}
