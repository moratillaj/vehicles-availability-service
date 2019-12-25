package com.meep.vehicles.availability.service;

import com.meep.vehicles.availability.model.PollingInfo;
import com.meep.vehicles.availability.model.Vehicle;
import com.meep.vehicles.availability.repository.PollingInfoRepository;
import com.meep.vehicles.availability.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

import static com.meep.vehicles.availability.model.AvailabilityEnum.AVAILABLE;
import static com.meep.vehicles.availability.model.AvailabilityEnum.BUSSY;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
public class VehiclesService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PollingInfoRepository pollingInfoRepository;

    @Transactional
    public List<Vehicle> getVehicles() {
        List<Long> lastPollingInfos = pollingInfoRepository.findLastPollingInfos().stream()
                .map(PollingInfo::getPollingTimestamp)
                .collect(toList());
        if (lastPollingInfos.isEmpty()) {
            return emptyList();
        }

        Stream<Vehicle> availableVehicles = vehicleRepository.findByLastTimeAvailable(lastPollingInfos.get(0))
                .stream()
                .map(vehicle -> vehicle.availability(AVAILABLE));

        if (lastPollingInfos.size() == 1) {
            return availableVehicles.collect(toList());
        }

        Stream<Vehicle> bussyVehicles = vehicleRepository.findByLastTimeAvailable(lastPollingInfos.get(1))
                .stream()
                .map(vehicle -> vehicle.availability(BUSSY));

        return Stream.concat(availableVehicles, bussyVehicles).collect(toList());
    }

}
