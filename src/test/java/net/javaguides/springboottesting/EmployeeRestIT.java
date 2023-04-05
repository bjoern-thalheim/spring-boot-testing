package net.javaguides.springboottesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import net.javaguides.springboottesting.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeRestIT extends AbstractPostgreSQLTestContainers {

    // given
    Employee employee = Employee.builder().firstName("Bjoern").lastName("Thalheim").email("bjoern.thalheim@adesso.de").build();
    @Inject
    private MockMvc mockMvc;
    @Inject
    private ObjectMapper objectMapper;

    @Test
    public void testRestCrudScenario() throws Exception {
        // check whether DB is empty
        {
            ResultActions result = mockMvc.perform(get("/api/employees"));
            result.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("[]"));
        }
        // create entity
        {
            ResultActions result = mockMvc.perform(post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(employee)));
            result.andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                    .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                    .andExpect(jsonPath("$.email", is(employee.getEmail())));
            employee = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), Employee.class);
        }
        // read entity()
        {
            ResultActions result = mockMvc.perform(get("/api/employees/{id}", employee.getId()));
            result.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                    .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                    .andExpect(jsonPath("$.email", is(employee.getEmail())));
        }
        // update
        {
            Employee updated = employee.toBuilder()
                    .firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com")
                    .build();
            ResultActions result = mockMvc.perform(put("/api/employees/{id}", employee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updated)));
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.lastName", is(updated.getLastName())))
                    .andExpect(jsonPath("$.firstName", is(updated.getFirstName())))
                    .andExpect(jsonPath("$.email", is(updated.getEmail())));
        }
        // delete
        {
            ResultActions result = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));
            result.andExpect(status().isOk());
        }
        // check whether DB is empty
        {
            ResultActions result = mockMvc.perform(get("/api/employees"));
            result.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("[]"));
        }
    }
}
