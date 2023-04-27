package com.accenture.assessment.bni;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    public void saveTest() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("firstname");
        userRequest.setLastName("lastname");
        userRequest.setSsn("12318");
        userRequest.setBirthDate(LocalDate.now());

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_data.ssn", is("0000000000012318")))
                .andExpect(jsonPath("$.user_data.first_name", is("firstname")))
                .andExpect(jsonPath("$.user_data.last_name", is("lastname")))
                .andExpect(jsonPath("$.user_data.birth_date", is(LocalDate.now().toString())));
    }
}