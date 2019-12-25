package com.meep.vehicles.availability.service;

import com.meep.vehicles.availability.exception.VehiclesResourcesClientException;
import com.meep.vehicles.availability.model.Vehicle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(MockitoJUnitRunner.class)
public class VehiclesResourcesClientTest {

    @InjectMocks
    private VehiclesResourcesClient vehiclesResourcesClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity<List<Vehicle>> responseEntity;

    @Mock
    private Vehicle vehicle1;

    @Mock
    private Vehicle vehicle2;

    @Before
    public void setUp() {
        setField(vehiclesResourcesClient, "meepVehiclesResourcesUrl", "theUrl");
    }

    @Test
    public void shouldGetVehiclesResources() {
        //Given
        when(restTemplate.exchange(
                "theUrl",
                GET,
                null,
                new ParameterizedTypeReference<List<Vehicle>>() {
                }))
                .thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(OK);
        List<Vehicle> foundVehicles = asList(vehicle1, vehicle2);
        when(responseEntity.getBody()).thenReturn(foundVehicles);

        //When
        List<Vehicle> vehiclesResources = vehiclesResourcesClient.getVehiclesResources();

        //Then
        InOrder inOrder = inOrder(restTemplate, responseEntity);
        inOrder.verify(restTemplate).exchange(
                "theUrl", GET, null, new ParameterizedTypeReference<List<Vehicle>>() {
                });
        inOrder.verify(responseEntity).getStatusCode();
        inOrder.verify(responseEntity).getBody();
        assertThat(vehiclesResources).contains(vehicle1, vehicle2);
    }

    @Test
    public void shouldGetVehiclesResourcesFailWhenNotOkResponse() {
        //Given
        when(restTemplate.exchange(
                "theUrl",
                GET,
                null,
                new ParameterizedTypeReference<List<Vehicle>>() {
                }))
                .thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(INTERNAL_SERVER_ERROR);
        when(responseEntity.getStatusCodeValue()).thenReturn(INTERNAL_SERVER_ERROR.value());

        //When && Then
        assertThatThrownBy(() -> vehiclesResourcesClient.getVehiclesResources())
                .isInstanceOf(VehiclesResourcesClientException.class)
                .hasMessage("Error getting routes resources from theUrl. Response status code 500");

        InOrder inOrder = inOrder(restTemplate, responseEntity);
        inOrder.verify(restTemplate).exchange(
                "theUrl", GET, null, new ParameterizedTypeReference<List<Vehicle>>() {
        });
        inOrder.verify(responseEntity).getStatusCode();
        inOrder.verify(responseEntity).getStatusCodeValue();
        verify(responseEntity, never()).getBody();
    }
}