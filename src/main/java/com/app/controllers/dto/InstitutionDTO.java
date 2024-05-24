package com.app.controllers.dto;

import com.app.persistence.entities.institutions.ModalityEnum;
import com.app.persistence.entities.institutions.SectorEnum;
import lombok.Data;

@Data
public class InstitutionDTO {
    private Long id;
    private String name;
    private String rfc;
    private String companyName;
    private String giro;
    private String web;
    private boolean support;
    private SectorEnum sector;
    private ModalityEnum modality;
    private String telephoneNumber;
    private AddressDTO address;
    private ResponsibleDTO responsible;
}

