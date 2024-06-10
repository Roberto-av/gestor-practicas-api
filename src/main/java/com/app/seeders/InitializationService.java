package com.app.seeders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class InitializationService {

    private static final Logger logger = LoggerFactory.getLogger(InitializationService.class);


    private final CreateUsers createUsers;
    private final CreateInstitutions createInstitutions;
    private final CreateGroups createGroups;
    private final CreateTasks createTasks;
    private final CreateStudents createStudents;
    private final CreatePermissions createPermissions;
    private final CreateRoles createRoles;

    public InitializationService(CreateUsers createUsers,
                                 CreateInstitutions createInstitutions,
                                 CreateGroups createGroups,
                                 CreateTasks createTasks,
                                 CreateStudents createStudents,
                                 CreatePermissions createPermissions,
                                 CreateRoles createRoles) {
        this.createUsers = createUsers;
        this.createInstitutions = createInstitutions;
        this.createGroups = createGroups;
        this.createTasks = createTasks;
        this.createStudents = createStudents;
        this.createPermissions = createPermissions;
        this.createRoles = createRoles;
    }

    @PostConstruct
    @Transactional
    public void init() {
        createPermissions.createAllPermissions();
        createRoles.createAllRoles();
        createUsers.createAllUsers();
        createInstitutions.CreateAllInstitutions();
        createGroups.createAllGroups();
        createTasks.createAllTasks();
        createStudents.createAllStudents();
    }
}
