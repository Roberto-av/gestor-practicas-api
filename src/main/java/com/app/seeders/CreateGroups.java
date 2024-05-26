package com.app.seeders;

import com.app.persistence.entities.groups.GroupEntity;
import com.app.persistence.repositories.GroupRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateGroups {

    private final GroupRepository groupRepository;

    public CreateGroups(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void createAllGroups() {
        GroupEntity group1 = new GroupEntity().builder()
                .name("IDS 9no TV")
                .description("Grupo de ids turno vestertino")
                .build();

        GroupEntity group2 = new GroupEntity().builder()
                .name("IDS 9no TM")
                .description("Grupo de ids turno matutino")
                .build();

        GroupEntity group3 = new GroupEntity().builder()
                .name("ITC 9no TV")
                .description("Grupo de itc turno vestertino")
                .build();

        GroupEntity group4 = new GroupEntity().builder()
                .name("ITC 9no TM")
                .description("Grupo de itc turno vestertino")
                .build();

        groupRepository.saveAll(List.of(group1, group2, group3, group4));
    }

}
