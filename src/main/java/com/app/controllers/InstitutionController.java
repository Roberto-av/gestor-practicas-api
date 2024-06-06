package com.app.controllers;

import com.app.controllers.dto.AddressDTO;
import com.app.controllers.dto.InstitutionDTO;
import com.app.services.impl.InstitutionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @PostMapping("/create")
    public ResponseEntity<InstitutionDTO> createInstitution(@RequestBody InstitutionDTO institutionDTO) {
        InstitutionDTO createdInstitution = institutionService.createInstitution(institutionDTO);
        return new ResponseEntity<>(createdInstitution, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<InstitutionDTO>> getAllInstitutions() {
        return new ResponseEntity<>(institutionService.getAllInstitutions(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> getInstitutionById(@PathVariable Long id) {
        InstitutionDTO institutionDTO = institutionService.getInstitutionById(id);
        return new ResponseEntity<>(institutionDTO, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<InstitutionDTO> updateInstitution(@Valid @RequestBody InstitutionDTO institutionDTO) {
        Long id = institutionDTO.getId();
        InstitutionDTO updatedInstitution = institutionService.updateInstitution(id, institutionDTO);
        return new ResponseEntity<>(updatedInstitution, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInstitution(@PathVariable Long id) {
        institutionService.deleteInstitution(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
