package com.meep.vehicles.availability.service;

import com.meep.vehicles.availability.exception.VehiclesResourcesClientException;
import com.meep.vehicles.availability.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@Service
public class VehiclesResourcesClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${meep.routesResourcesUrl}")
    private String meepVehiclesResourcesUrl;

    public List<Vehicle> getVehiclesResources() {

        ResponseEntity<List<Vehicle>> response = restTemplate.exchange(
                meepVehiclesResourcesUrl,
                GET,
                null,
                new ParameterizedTypeReference<List<Vehicle>>() {
                });

        if (response.getStatusCode() == OK) {
            return response.getBody();
        }

        throw new VehiclesResourcesClientException("Error getting routes resources from " + meepVehiclesResourcesUrl
                + ". Response status code " + response.getStatusCodeValue());
    }

}
