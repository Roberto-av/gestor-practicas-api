package com.app.services.impl;

import com.app.controllers.dto.GroupDTO;
import com.app.controllers.dto.InstitutionDTO;
import com.app.controllers.dto.StudentInstitutionDTO;
import com.app.controllers.dto.request.StudentRequestDTO;
import com.app.controllers.dto.StudentDTO;
import com.app.exceptions.IdNotFundException;
import com.app.exceptions.UniqueFieldViolationException;
import com.app.exceptions.textNotFundException;
import com.app.persistence.entities.groups.GroupEntity;
import com.app.persistence.entities.institutions.InstitutionEntity;
import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.GroupRepository;
import com.app.persistence.repositories.InstitutionRepository;
import com.app.persistence.repositories.StudentRepository;
import com.app.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl {

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public Optional<StudentDTO> findStudentByControlNumber(int controlNumber) {
        return studentRepository.findStudentByControlNumber(controlNumber)
                .map(this::convertToDTO);
    }

    public Optional<StudentDTO> findStudentByEmail(String email) {
        return studentRepository.findStudentByEmail(email)
                .map(this::convertToDTO);
    }

    public StudentDTO saveStudent(StudentDTO studentDTO) {
        Optional<StudentEntity> existingStudentByEmail = studentRepository.findStudentByEmail(studentDTO.getEmail());
        if (existingStudentByEmail.isPresent()) {
            throw new UniqueFieldViolationException(studentDTO.getEmail());
        }

        Optional<StudentEntity> existingStudentByControlNumber = studentRepository.findStudentByControlNumber(studentDTO.getControlNumber());
        if (existingStudentByControlNumber.isPresent()) {
            throw new UniqueFieldViolationException(String.valueOf(studentDTO.getControlNumber()));
        }

        StudentEntity student = new StudentEntity();
        student.setControlNumber(studentDTO.getControlNumber());
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setProgram(studentDTO.getProgram());
        student.setSemester(studentDTO.getSemester());
        student.setShift(studentDTO.getShift());

        if (studentDTO.getGroup() != null) {
            GroupEntity group = groupRepository.findById(studentDTO.getGroup().getId())
                    .orElseThrow(() -> new IdNotFundException(studentDTO.getGroup().getId()));
            student.setGroup(group);
        }

        return convertToDTO(studentRepository.save(student));
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IdNotFundException(id));
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        StudentEntity existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));

        existingStudent.setControlNumber(studentDTO.getControlNumber());
        existingStudent.setName(studentDTO.getName());
        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setProgram(studentDTO.getProgram());
        existingStudent.setSemester(studentDTO.getSemester());
        existingStudent.setShift(studentDTO.getShift());

        if (studentDTO.getGroup() != null && studentDTO.getGroup().getId() != null) {
            GroupEntity group = groupRepository.findById(studentDTO.getGroup().getId())
                    .orElseThrow(() -> new IdNotFundException(studentDTO.getGroup().getId()));
            existingStudent.setGroup(group);
        } else {
            existingStudent.setGroup(null);
        }

        return convertToDTO(studentRepository.save(existingStudent));
    }


    public void deleteStudent(Long id) {
        StudentEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));

        UserEntity user = student.getUser();
        if (user != null) {
            userRepository.delete(user);
        }

        studentRepository.delete(student);
    }

    public void subscribeStudentToInstitution(StudentInstitutionDTO studentInstitutionDTO) {
        StudentEntity student = studentRepository.findById(studentInstitutionDTO.getStudentId())
                .orElseThrow(() -> new IdNotFundException(studentInstitutionDTO.getStudentId()));

        InstitutionEntity institution = institutionRepository.findById(studentInstitutionDTO.getInstitutionId())
                .orElseThrow(() -> new IdNotFundException(studentInstitutionDTO.getInstitutionId()));

        student.setInstitution(institution);
        studentRepository.save(student);
    }

    public void unsubscribeStudentFromInstitution(StudentInstitutionDTO studentInstitutionDTO) {
        StudentEntity student = studentRepository.findById(studentInstitutionDTO.getStudentId())
                .orElseThrow(() -> new IdNotFundException(studentInstitutionDTO.getStudentId()));

        InstitutionEntity institution = institutionRepository.findById(studentInstitutionDTO.getInstitutionId())
                .orElseThrow(() -> new IdNotFundException(studentInstitutionDTO.getInstitutionId()));

        if (student.getInstitution() != null && student.getInstitution().equals(institution)) {
            student.setInstitution(null);
            studentRepository.save(student);
        } else {
            throw new IllegalArgumentException("El estudiante no está suscrito a esta institución.");
        }
    }


    public StudentDTO convertToDTO(StudentEntity studentEntity) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(studentEntity.getId());
        studentDTO.setControlNumber(studentEntity.getControlNumber());
        studentDTO.setName(studentEntity.getName());
        studentDTO.setEmail(studentEntity.getEmail());
        studentDTO.setProgram(studentEntity.getProgram());
        studentDTO.setSemester(studentEntity.getSemester());
        studentDTO.setShift(studentEntity.getShift());

        if (studentEntity.getGroup() != null) {
            GroupEntity groupEntity = studentEntity.getGroup();
            GroupDTO groupDTO = convertGroupEntityToDTO(groupEntity);
            studentDTO.setGroup(groupDTO);
        }

        studentDTO.setCreatedAt(studentEntity.getCreatedAt());
        studentDTO.setUpdatedAt(studentEntity.getUpdatedAt());

        if (studentEntity.getUser() != null) {
            studentDTO.setUsername(studentEntity.getUser().getUsername());
        }

        if (studentEntity.getInstitution() != null) {
            InstitutionEntity institutionEntity = studentEntity.getInstitution();
            InstitutionDTO institutionDTO = convertInstitutionEntityToDTO(institutionEntity);
            studentDTO.setInstitution(institutionDTO);
        }

        return studentDTO;
    }

    public StudentEntity convertToEntity(StudentDTO studentDTO) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentDTO.getId());
        studentEntity.setControlNumber(studentDTO.getControlNumber());
        studentEntity.setName(studentDTO.getName());
        studentEntity.setEmail(studentDTO.getEmail());
        studentEntity.setProgram(studentDTO.getProgram());
        studentEntity.setSemester(studentDTO.getSemester());
        studentEntity.setShift(studentDTO.getShift());

        GroupEntity group = groupRepository.findById(studentDTO.getGroup().getId())
                .orElseThrow(() -> new IdNotFundException(studentDTO.getGroup().getId()));
        studentEntity.setGroup(group);

        InstitutionEntity institution = institutionRepository.findById(studentDTO.getInstitution().getId())
                .orElseThrow(() -> new IdNotFundException(studentDTO.getInstitution().getId()));
        studentEntity.setInstitution(institution);

        return studentEntity;
    }

    private GroupDTO convertGroupEntityToDTO(GroupEntity groupEntity) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(groupEntity.getId());
        groupDTO.setName(groupEntity.getName());
        return groupDTO;
    }

    private InstitutionDTO convertInstitutionEntityToDTO(InstitutionEntity institutionEntity) {
        InstitutionDTO institutionDTO = new InstitutionDTO();
        institutionDTO.setId(institutionEntity.getId());
        institutionDTO.setName(institutionEntity.getName());
        return institutionDTO;
    }
}

