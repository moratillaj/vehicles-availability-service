package com.meep.vehicles.availability.controller;

import com.meep.vehicles.availability.model.Vehicle;
import com.meep.vehicles.availability.service.VehiclesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehiclesController {

    @Autowired
    private VehiclesService vehiclesService;

    @GetMapping
    public List<Vehicle> getVehicles() {
        return vehiclesService.getVehicles();
    }
}
