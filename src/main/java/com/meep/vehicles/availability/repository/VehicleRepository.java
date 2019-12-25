package com.meep.vehicles.availability.repository;


import com.meep.vehicles.availability.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByLastTimeAvailable(Long lastTimeAvailable);

    void deleteByLastTimeAvailableLessThan(Long pollingInfo);
}
