package com.app.services.impl;

import com.app.controllers.dto.AddressDTO;
import com.app.exceptions.IdNotFundException;
import com.app.persistence.entities.institutions.AddressEntity;
import com.app.persistence.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl {

    @Autowired
    private AddressRepository addressRepository;

    public AddressDTO createAddress(AddressDTO addressDTO) {
        AddressEntity addressEntity = convertToEntity(addressDTO);
        addressEntity = addressRepository.save(addressEntity);
        return convertToDTO(addressEntity);
    }

    public AddressDTO getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IdNotFundException(id));
    }

    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        if (addressDTO == null) {
            throw new IllegalArgumentException("No se encontro el id en la solicitud");
        }

        AddressDTO existingAddress = getAddressById(id);

        return addressRepository.findById(id)
                .map(address -> {
                    address.setStreet(addressDTO.getStreet());
                    address.setCity(addressDTO.getCity());
                    address.setState(addressDTO.getState());
                    address.setPostalCode(addressDTO.getPostalCode());
                    address.setCountry(addressDTO.getCountry());
                    return convertToDTO(addressRepository.save(address));
                }).orElseGet(() -> {
                    AddressEntity addressEntity = convertToEntity(addressDTO);
                    addressEntity.setId(id);
                    return convertToDTO(addressRepository.save(addressEntity));
                });
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    public AddressDTO convertToDTO(AddressEntity addressEntity) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(addressEntity.getId());
        addressDTO.setStreet(addressEntity.getStreet());
        addressDTO.setCity(addressEntity.getCity());
        addressDTO.setState(addressEntity.getState());
        addressDTO.setPostalCode(addressEntity.getPostalCode());
        addressDTO.setCountry(addressEntity.getCountry());
        return addressDTO;
    }

    public AddressEntity convertToEntity(AddressDTO addressDTO) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(addressDTO.getId());
        addressEntity.setStreet(addressDTO.getStreet());
        addressEntity.setCity(addressDTO.getCity());
        addressEntity.setState(addressDTO.getState());
        addressEntity.setPostalCode(addressDTO.getPostalCode());
        addressEntity.setCountry(addressDTO.getCountry());
        return addressEntity;
    }
}
