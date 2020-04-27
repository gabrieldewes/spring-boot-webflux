package io.dewes.springbootwebflux.car;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Data
@Builder

@Table(name = "car")
public class Car {

    @Id
    private final UUID id;

    private String model;
    private String plate;
    private int yearModel;
    private int yearManufacture;
    private Manufacturer manufacturer;

}
