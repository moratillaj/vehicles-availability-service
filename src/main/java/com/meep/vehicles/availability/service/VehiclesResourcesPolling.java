package com.meep.vehicles.availability.service;

import com.meep.vehicles.availability.model.PollingInfo;
import com.meep.vehicles.availability.repository.PollingInfoRepository;
import com.meep.vehicles.availability.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@Component
public class VehiclesResourcesPolling {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PollingInfoRepository pollingInfoRepository;

    @Autowired
    private VehiclesResourcesClient vehiclesResourcesClient;

    @Value("${meep.numPollsToRemoveVehicles:2}")
    private int numPollsToRemoveVehicles;


    @Scheduled(fixedRate = 30000)
    @Transactional
    public void pollAvailableVehicles() {
        Long now = Calendar.getInstance().getTimeInMillis();
        pollingInfoRepository.save(new PollingInfo(now));
        vehiclesResourcesClient.getVehiclesResources().stream()
                .map(vehicle -> vehicle.lastTimeAvailable(now))
                .forEach(vehicleRepository::save);
        removeVehiclesOlderThanNumPolls(numPollsToRemoveVehicles);
    }

    private void removeVehiclesOlderThanNumPolls(int numPollsToRemoveVehicles) {
        List<PollingInfo> lastPollingInfos = pollingInfoRepository.findLastPollingInfos();
        if (lastPollingInfos.size() > numPollsToRemoveVehicles) {
            vehicleRepository.deleteByLastTimeAvailableLessThan(lastPollingInfos.get(1).getPollingTimestamp());
        }
    }
}
