package com.meep.vehicles.availability.service;

import com.meep.vehicles.availability.model.PollingInfo;
import com.meep.vehicles.availability.model.Vehicle;
import com.meep.vehicles.availability.repository.PollingInfoRepository;
import com.meep.vehicles.availability.repository.VehicleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.meep.vehicles.availability.model.AvailabilityEnum.AVAILABLE;
import static com.meep.vehicles.availability.model.AvailabilityEnum.BUSSY;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VehiclesServiceTest {

    @InjectMocks
    private VehiclesService vehiclesService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private PollingInfoRepository pollingInfoRepository;

    @Mock
    private PollingInfo lastPollingInfo;

    @Mock
    private PollingInfo previousPollingInfo;

    @Mock
    private Vehicle vehicle1;

    @Mock
    private Vehicle vehicle2;

    @Mock
    private Vehicle vehicle3;

    @Mock
    private Vehicle vehicle4;


    @Test
    public void shouldGetVehiclesWhenOnePollExist() {
        //Given
        when(pollingInfoRepository.findLastPollingInfos()).thenReturn(singletonList(lastPollingInfo));
        long pollTime = 1L;
        when(lastPollingInfo.getPollingTimestamp()).thenReturn(pollTime);
        when(vehicleRepository.findByLastTimeAvailable(pollTime)).thenReturn(asList(vehicle1, vehicle2));
        when(vehicle1.availability(AVAILABLE)).thenReturn(vehicle1);
        when(vehicle2.availability(AVAILABLE)).thenReturn(vehicle2);

        //When
        List<Vehicle> vehicles = vehiclesService.getVehicles();

        //Then
        InOrder inOrder = inOrder(pollingInfoRepository, lastPollingInfo, vehicleRepository, vehicle1, vehicle2);
        inOrder.verify(pollingInfoRepository).findLastPollingInfos();
        inOrder.verify(lastPollingInfo).getPollingTimestamp();
        inOrder.verify(vehicleRepository).findByLastTimeAvailable(pollTime);
        inOrder.verify(vehicle1).availability(AVAILABLE);
        inOrder.verify(vehicle2).availability(AVAILABLE);

        assertThat(vehicles).contains(vehicle1, vehicle2);
    }

    @Test
    public void shouldGetVehiclesWhenTwoPollExist() {
        //Given
        when(pollingInfoRepository.findLastPollingInfos()).thenReturn(asList(lastPollingInfo, previousPollingInfo));
        long lastPollTime = 2L;
        long previousPollTime = 1L;
        when(lastPollingInfo.getPollingTimestamp()).thenReturn(lastPollTime);
        when(previousPollingInfo.getPollingTimestamp()).thenReturn(previousPollTime);
        when(vehicleRepository.findByLastTimeAvailable(lastPollTime)).thenReturn(asList(vehicle1, vehicle2));
        when(vehicleRepository.findByLastTimeAvailable(previousPollTime)).thenReturn(asList(vehicle3, vehicle4));
        when(vehicle1.availability(AVAILABLE)).thenReturn(vehicle1);
        when(vehicle2.availability(AVAILABLE)).thenReturn(vehicle2);
        when(vehicle3.availability(BUSSY)).thenReturn(vehicle3);
        when(vehicle4.availability(BUSSY)).thenReturn(vehicle4);

        //When
        List<Vehicle> vehicles = vehiclesService.getVehicles();

        //Then
        InOrder inOrder = inOrder(
                pollingInfoRepository,
                lastPollingInfo,
                vehicleRepository,
                vehicle1,
                vehicle2,
                vehicle3,
                vehicle4);
        inOrder.verify(pollingInfoRepository).findLastPollingInfos();
        inOrder.verify(lastPollingInfo).getPollingTimestamp();
        inOrder.verify(vehicleRepository).findByLastTimeAvailable(lastPollTime);
        inOrder.verify(vehicleRepository).findByLastTimeAvailable(previousPollTime);
        inOrder.verify(vehicle1).availability(AVAILABLE);
        inOrder.verify(vehicle2).availability(AVAILABLE);
        inOrder.verify(vehicle3).availability(BUSSY);
        inOrder.verify(vehicle4).availability(BUSSY);

        assertThat(vehicles).contains(vehicle1, vehicle2, vehicle3, vehicle4);
    }

    @Test
    public void shouldGetVehiclesReturnEmptyWhenNoPoll() {
        //Given
        when(pollingInfoRepository.findLastPollingInfos()).thenReturn(emptyList());

        //When
        List<Vehicle> vehicles = vehiclesService.getVehicles();

        //Then
        verifyZeroInteractions(vehicleRepository);
        assertThat(vehicles).isEmpty();
    }

}