package com.accenture.assessment.bni;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.accenture.assessment.bni.controller.response.UserResponse;
import com.accenture.assessment.bni.mapper.UserMapper;
import com.accenture.assessment.bni.repository.UserRepository;
import com.accenture.assessment.bni.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @Order(1)
    public void whenSaveUserThenUserSuccessfullySaved() throws Exception {
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

    @Test
    @Order(2)
    public void whenUpdateUserWithValidDataThenUserSuccessfullyUpdated() throws Exception {

        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("firstname");
        userRequest.setLastName("lastname");
        userRequest.setSsn("11111");
        userRequest.setBirthDate(LocalDate.now());

        UserResponse response = userService.save(userRequest);

        userRequest.setFirstName("firstnameUpdated");
        userRequest.setLastName("lastnameUpdated");

        mockMvc.perform(put("/v1/users/" + response.getUserData().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_data.ssn", is("0000000000011111")))
                .andExpect(jsonPath("$.user_data.first_name", is("firstnameUpdated")))
                .andExpect(jsonPath("$.user_data.last_name", is("lastnameUpdated")));
    }

    @Test
    @Order(3)
    public void whenGetUserWithValidIdThenUserReturnedSuccessfully() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("firstname");
        userRequest.setLastName("lastname");
        userRequest.setSsn("22222");
        userRequest.setBirthDate(LocalDate.now());

        UserResponse response = userService.save(userRequest);

        mockMvc.perform(get("/v1/users/" + response.getUserData().getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_data.ssn", is("0000000000022222")))
                .andExpect(jsonPath("$.user_data.first_name", is("firstname")))
                .andExpect(jsonPath("$.user_data.last_name", is("lastname")))
                .andExpect(jsonPath("$.user_data.is_active", is(true)));
    }

    @Test
    @Order(4)
    public void whenGetPagedUserThenUserListReturnedBasedOnMaxRecordsAndOffset() throws Exception {

        mockMvc.perform(get("/v1/users?max_records=10&offset=0"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_data.length()", is(3)));

        mockMvc.perform(get("/v1/users?max_records=10&offset=1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_data.length()", is(2)));

        mockMvc.perform(get("/v1/users?max_records=10&offset=2"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_data.length()", is(1)));

        mockMvc.perform(get("/v1/users?max_records=10&offset=3"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_data.length()", is(0)));

        mockMvc.perform(get("/v1/users?max_records=1&offset=0"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_data.length()", is(1)));
    }

    @Test
    @Order(5)
    public void givenUserIdWhenUserExistThenChangeActiveStatusToFalse() throws Exception {

        mockMvc.perform(delete("/v1/users/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/users?max_records=10&offset=0"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.user_data.length()", is(2)));
    }
}