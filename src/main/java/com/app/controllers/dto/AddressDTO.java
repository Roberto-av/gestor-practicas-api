package com.app.controllers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDTO {

  @NotNull
  private Long id;
  private String street;
  private String city;
  private String state;
  private String postalCode;
  private String country;
}
