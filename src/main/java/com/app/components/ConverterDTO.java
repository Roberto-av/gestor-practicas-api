package com.app.components;

import com.app.controllers.dto.*;
import com.app.persistence.entities.groups.*;
import com.app.persistence.entities.users.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ConverterDTO {


    public TaskDTO toDTO(TaskEntity taskEntity) {
        return TaskDTO.builder()
                .id(taskEntity.getId())
                .tittle(taskEntity.getTittle())
                .description(taskEntity.getDescription())
                .statusTask(taskEntity.getStatusTask())
                .initialDate(taskEntity.getInitialDate())
                .endDate(taskEntity.getEndDate())
                .user(toUserDTO(taskEntity.getUser()))
                .group(toGroupDTO(taskEntity.getGroup()))
                .files(taskEntity.getFiles().stream().map(this::toFileDTO).toList())
                .build();
    }

    public TaskEntity toEntity(TaskDTO taskDTO, UserEntity user, GroupEntity group) {
        return TaskEntity.builder()
                .id(taskDTO.getId())
                .tittle(taskDTO.getTittle())
                .description(taskDTO.getDescription())
                .statusTask(taskDTO.getStatusTask())
                .initialDate(taskDTO.getInitialDate())
                .endDate(taskDTO.getEndDate())
                .user(user)
                .group(group)
                .build();
    }

    public FileDTO toFileDTO(FileEntity fileEntity) {
        return FileDTO.builder()
                .id(fileEntity.getId())
                .name(fileEntity.getName())
                .filePath(fileEntity.getFile_path())
                .taskId(fileEntity.getTask().getId())
                .user(toUserDTO(fileEntity.getUser()))
                .createdAt(fileEntity.getCreatedAt())
                .updatedAt(fileEntity.getUpdatedAt())
                .build();
    }

    public UserDTO toUserDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .build();
    }

    public GroupDTO toGroupDTO(GroupEntity groupEntity) {
        return GroupDTO.builder()
                .id(groupEntity.getId())
                .name(groupEntity.getName())
                .build();
    }

    public SubmissionDTO toSubmissionDTO(SubmissionEntity submissionEntity) {
        if (submissionEntity.getFiles() == null) {
            return SubmissionDTO.builder()
                    .id(submissionEntity.getId())
                    .taskId(submissionEntity.getTask().getId())
                    .userId(submissionEntity.getUser().getId())
                    .submittedAt(submissionEntity.getSubmittedAt())
                    .update_at(submissionEntity.getUpdatedAt())
                    .build();
        } else {
            return SubmissionDTO.builder()
                    .id(submissionEntity.getId())
                    .taskId(submissionEntity.getTask().getId())
                    .userId(submissionEntity.getUser().getId())
                    .submittedAt(submissionEntity.getSubmittedAt())
                    .update_at(submissionEntity.getUpdatedAt())
                    .files(submissionEntity.getFiles().stream().map(this::toSubmissionFileDTO).toList())
                    .build();
        }
    }

    public SubmissionFileDTO toSubmissionFileDTO(SubmissionFileEntity fileEntity) {
        return SubmissionFileDTO.builder()
                .id(fileEntity.getId())
                .name(fileEntity.getName())
                .filePath(fileEntity.getFilePath())
                .submittedAt(fileEntity.getSubmittedAt())
                .build();
    }
}

