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
    private final CreateProjects createProjects;
    private final CreateStudents createStudents;

    public InitializationService(CreateUsers createUsers,
                                 CreateInstitutions createInstitutions,
                                 CreateGroups createGroups,
                                 CreateTasks createTasks,
                                 CreateProjects createProjects,
                                 CreateStudents createStudents) {
        this.createUsers = createUsers;
        this.createInstitutions = createInstitutions;
        this.createGroups = createGroups;
        this.createTasks = createTasks;
        this.createProjects = createProjects;
        this.createStudents = createStudents;
    }

    @PostConstruct
    @Transactional
    public void init() {
        createUsers.createUsers();
        createInstitutions.CreateAllInstitutions();
        createGroups.createAllGroups();
        createTasks.createAllTasks();
        createProjects.createAllProjects();
        createStudents.createAllStudents();
    }
}
