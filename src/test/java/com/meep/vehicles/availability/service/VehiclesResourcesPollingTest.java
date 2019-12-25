package com.meep.vehicles.availability.service;

import com.meep.vehicles.availability.model.PollingInfo;
import com.meep.vehicles.availability.model.Vehicle;
import com.meep.vehicles.availability.repository.PollingInfoRepository;
import com.meep.vehicles.availability.repository.VehicleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VehiclesResourcesPollingTest {

    @InjectMocks
    private VehiclesResourcesPolling vehiclesResourcesPolling;

    @Mock
    private PollingInfoRepository pollingInfoRepository;

    @Mock
    private VehiclesResourcesClient vehiclesResourcesClient;

    @Mock
    private VehicleRepository vehiclesRepository;

    @Captor
    private ArgumentCaptor<PollingInfo> pollingInfo;

    @Mock
    private Vehicle vehicle1;

    @Mock
    private Vehicle vehicle2;

    @Captor
    private ArgumentCaptor<Long> nowTimestamp;

    @Mock
    private PollingInfo pollingInfo1;

    @Mock
    private PollingInfo pollingInfo2;

    @Mock
    private PollingInfo pollingInfo3;

    @Test
    public void shouldPollAvailableVehicles() {
        //Given
        when(pollingInfoRepository.save(pollingInfo.capture()))
                .then(invocationOnMock -> invocationOnMock.getArgument(0));
        when(vehiclesResourcesClient.getVehiclesResources()).thenReturn(asList(vehicle1, vehicle2));
        when(vehicle1.lastTimeAvailable(nowTimestamp.capture())).thenReturn(vehicle1);
        when(vehicle2.lastTimeAvailable(nowTimestamp.capture())).thenReturn(vehicle2);
        when(pollingInfoRepository.findLastPollingInfos()).thenReturn(asList(pollingInfo1, pollingInfo2, pollingInfo3));
        Long previousTimestamp = 1L;
        when(pollingInfo2.getPollingTimestamp()).thenReturn(previousTimestamp);

        //When
        vehiclesResourcesPolling.pollAvailableVehicles();

        //Then
        InOrder inOrder = Mockito.inOrder(
                vehiclesResourcesClient,
                pollingInfoRepository,
                vehiclesRepository,
                vehicle1,
                vehicle2);
        inOrder.verify(pollingInfoRepository).save(pollingInfo.capture());
        inOrder.verify(vehiclesResourcesClient).getVehiclesResources();
        inOrder.verify(vehicle1).lastTimeAvailable(nowTimestamp.capture());
        inOrder.verify(vehiclesRepository).save(vehicle1);
        inOrder.verify(vehicle2).lastTimeAvailable(nowTimestamp.capture());
        inOrder.verify(vehiclesRepository).save(vehicle2);
        inOrder.verify(vehiclesRepository).deleteByLastTimeAvailableLessThan(previousTimestamp);

        assertThat(pollingInfo.getValue().getPollingTimestamp())
                .isLessThan(Calendar.getInstance().getTimeInMillis());
        assertThat(nowTimestamp.getValue()).isLessThan(Calendar.getInstance().getTimeInMillis());

    }

}