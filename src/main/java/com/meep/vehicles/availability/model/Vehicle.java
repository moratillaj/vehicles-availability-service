package com.meep.vehicles.availability.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicles")
@Entity
@JsonInclude(NON_NULL)
public class Vehicle {

    @Id
    private String id;

    private String name;

    private Float x;

    private Float y;

    private String licencePlate;
    @Column(name = "vehicle_range")

    private Integer range;

    private Integer batteryLevel;

    private Integer seats;

    private Integer helmets;

    private String model;

    private String resourceImageId;

    private Integer pricePerMinuteParking;

    private Integer pricePerMinuteDriving;

    private Boolean realTimeData;

    private String resourceType;

    private String engineType;

    private Integer companyZoneId;

    private Long lastTimeAvailable;

    @Transient
    private AvailabilityEnum availability;

    public Vehicle availability(AvailabilityEnum availability) {
        this.setAvailability(availability);
        return this;
    }

    public Vehicle lastTimeAvailable(Long now) {
        this.setLastTimeAvailable(now);
        return this;
    }
}
