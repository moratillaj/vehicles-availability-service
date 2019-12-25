package com.meep.vehicles.availability.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "polling_info")
@Table(name = "polling_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollingInfo {

    @Id
    private Long pollingTimestamp;

}
