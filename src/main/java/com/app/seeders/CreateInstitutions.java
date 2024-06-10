package com.app.seeders;

import com.app.persistence.entities.institutions.*;
import com.app.persistence.repositories.InstitutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CreateInstitutions {
    private static final Logger logger = LoggerFactory.getLogger(InitializationService.class);


    private final InstitutionRepository institutionRepository;

    @Autowired
    public CreateInstitutions(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public void CreateAllInstitutions() {

        AddressEntity address1 = AddressEntity.builder()
                .street("JALISCO 87 LOCAL 2")
                .city("LA PAZ")
                .state("BAJA CALIFORNIA SUR")
                .postalCode("23097")
                .country("México")
                .build();

        ResponsibleEntity responsible1 = ResponsibleEntity.builder()
                .name("EMMANUEL PATRICIO VAZQUEZ SANCHEZ")
                .position("PROPIETARIO")
                .education("PASANTE INGENIERIA EN DESARROLLO COMPUTACIONAL")
                .email("epvazquez@smcbcs.mx")
                .confirmEmail("epvazquez@smcbcs.mx")
                .phone("6121416759")
                .build();

        InstitutionEntity institution1 = InstitutionEntity.builder()
                .name("SERVICIOS MULTIPLES EN COMPUTO")
                .rfc("VASE7409115I6")
                .companyName("EMMANUEL PATRICIO VAZQUEZ SANCHEZ")
                .sector(SectorEnum.PRIVATE)
                .giro("VENTA AL POR MENOR DE EQUIPO DE COMPUTO, REDES Y CCTV")
                .web("NO")
                .support(false)
                .address(address1)
                .telephoneNumber("6121416759")
                .modality(ModalityEnum.HYBRID)
                .responsible(responsible1)
                .build();

        AddressEntity address2 = AddressEntity.builder()
                .street("Toronja 4490, La Paz, B.C.S., México")
                .city("LA PAZ")
                .state("BAJA CALIFORNIA SUR")
                .postalCode("23078")
                .country("México")
                .build();

        ResponsibleEntity responsible2 = ResponsibleEntity.builder()
                .name("IZKIAN JULIAN CARDENAS SERNA")
                .position("JEFE DE ÁREA")
                .education("INGENIERO EN TECNOLOGÍA COMPUTACIONAL")
                .email("izkian@toronjalab.com")
                .confirmEmail("izkian@toronjalab.com")
                .phone("6121368712")
                .build();

        InstitutionEntity institution2 = InstitutionEntity.builder()
                .name("Toronja Lab")
                .rfc("CASI970711757")
                .companyName("IZKIAN JULIAN CARDENAS SERNA")
                .sector(SectorEnum.PRIVATE)
                .giro("SERVICIOS DE SOFTWARE")
                .web("www.toronjalab.com")
                .support(false)
                .address(address2)
                .telephoneNumber("6121368712")
                .modality(ModalityEnum.PRESENT)
                .responsible(responsible2)
                .build();

        saveIfNotExists(institution1);
        saveIfNotExists(institution2);
    }

    private void saveIfNotExists(InstitutionEntity institution) {
        Optional<InstitutionEntity> existingByRfc = institutionRepository.findInstitutionByRfc(institution.getRfc());
        if (existingByRfc.isPresent()) {
            logger.info("Institution with RFC {} already exists. Skipping...", institution.getRfc());
            return;
        }

        Optional<InstitutionEntity> existingByCompanyName = institutionRepository.findInstitutionByCompanyName(institution.getCompanyName());
        if (existingByCompanyName.isPresent()) {
            logger.info("Institution with company name {} already exists. Skipping...", institution.getCompanyName());
            return;
        }

        institutionRepository.save(institution);
    }
}
