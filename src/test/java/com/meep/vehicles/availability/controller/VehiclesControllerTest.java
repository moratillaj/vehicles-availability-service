package com.meep.vehicles.availability.controller;

import com.meep.vehicles.availability.model.Vehicle;
import com.meep.vehicles.availability.service.VehiclesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = VehiclesController.class)
@WebAppConfiguration
public class VehiclesControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private VehiclesService vehiclesService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldGetVehicles() throws Exception {
        //Given
        when(vehiclesService.getVehicles()).thenReturn(
                asList(createVehicle("id1", "name1"), createVehicle("id2", "name2")));

        //When && Then
        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("id1"))
                .andExpect(jsonPath("$.[0].name").value("name1"))
                .andExpect(jsonPath("$.[1].id").value("id2"))
                .andExpect(jsonPath("$.[1].name").value("name2"));
    }

    private Vehicle createVehicle(String id, String name) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setName(name);
        return vehicle;
    }

}