package com.app.controllers;

import com.app.controllers.dto.ResponsibleDTO;
import com.app.services.impl.ResponsibleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responsible")
public class ResponsibleController {

    @Autowired
    private ResponsibleServiceImpl responsibleService;

    @PostMapping("/create")
    public ResponseEntity<ResponsibleDTO> createResponsible(@RequestBody ResponsibleDTO responsibleDTO) {
        return new ResponseEntity<>(responsibleService.createResponsible(responsibleDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsibleDTO> getResponsibleById(@PathVariable Long id) {
        ResponsibleDTO responsibleDTO = responsibleService.getResponsibleById(id);
        return new ResponseEntity<>(responsibleDTO, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<ResponsibleDTO>> getAllResponsible() {
        return new ResponseEntity<>(responsibleService.getResponsibles(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponsibleDTO> updateResponsible(@Valid @RequestBody ResponsibleDTO responsibleDTO) {
        Long id = responsibleDTO.getId();
        ResponsibleDTO updateResponsible = responsibleService.updateResponsible(id, responsibleDTO);
        return new ResponseEntity<>(updateResponsible, HttpStatus.OK);
    }
}
