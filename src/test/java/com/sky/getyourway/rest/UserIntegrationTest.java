package com.sky.getyourway.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.getyourway.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:getyourway-schema.sql", "classpath:getyourway-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    // MockMvc used to test Spring
    private MockMvc mvc;

    @Autowired
    // converts JSON to java and java to JSON
    private ObjectMapper mapper;

    @Test
    void testRegisterUser() throws Exception{
        User testUser = new User("Mike", "Wilson", "mike.wilson@email.com", "password123");
        String reqBody = this.mapper.writeValueAsString(testUser);  // converts the Java object in JSON
        System.out.println("CUSTOMER ==>> " + testUser);
        System.out.println("JSON ==>> " + reqBody);

        RequestBuilder req = post("/register").content(reqBody).contentType(MediaType.APPLICATION_JSON);

        ResultMatcher checkStatus = status().isCreated();
        testUser.setId(3);
        String resBody = this.mapper.writeValueAsString(testUser);
        System.out.println("SAVED CUSTOMER ==>> "  + testUser);
        System.out.println("RES JSON: " + resBody);
        ResultMatcher checkBody = content().json(resBody);

        mvc.perform(req).andExpect(checkStatus).andExpect(checkBody);

    }

    @Test
    void testLoginSuccessful() throws Exception {
        // customer with id1 in H2 db:  ('Jane', 'Doe', 'jane.doe@email.com', '123ABC'),
        // Expected response from Jane Doe log in is the User object
        User expectedResponse = new User(1, "Jane", "Doe", "jane.doe@email.com", "123ABC");
        // We "translate" the expected response into JSON format
        String expectedResponseJson = this.mapper.writeValueAsString(expectedResponse);

        // Create the request to be tested and expected Status and Body
        RequestBuilder request = get("/login").param("email", "jane.doe@email.com").param("password", "123ABC");
        ResultMatcher expectedStatus = status().isOk();
        ResultMatcher expectedBody = content().json(expectedResponseJson);

        // Execute the request and set expectations
        this.mvc.perform(request).andExpect(expectedStatus).andExpect(expectedBody);
    }

    @Test
    void testLoginIncorrectEmail() throws Exception {
        // customer with id1 in H2 db:  ('Jane', 'Doe', 'jane.doe@email.com', '123ABC'),
        // Expected response from Jane Doe log in is the User object
        User expectedResponse = new User(1, "Jane", "Doe", "jane.doe@email.com", "123ABC");
        // We "translate" the expected response into JSON format
        String expectedResponseJson = this.mapper.writeValueAsString(expectedResponse);

        // Create the request to be tested and expected Status (no response body as we can't find any customer)
        RequestBuilder request = get("/login").param("email", "wrong.email@email.com").param("password", "123ABC");
        ResultMatcher expectedStatus = status().isNotFound();

        // Execute the request and set expectations
        this.mvc.perform(request).andExpect(expectedStatus);
    }

    @Test
    void testLoginIncorrectPassword() throws Exception {
        // customer with id1 in H2 db:  ('Jane', 'Doe', 'jane.doe@email.com', '123ABC'),
        // Expected response from Jane Doe log in is the User object
        User expectedResponse = new User(1, "Jane", "Doe", "jane.doe@email.com", "123ABC");
        // We "translate" the expected response into JSON format
        String expectedResponseJson = this.mapper.writeValueAsString(expectedResponse);

        // Create the request to be tested and expected Status (no response body as we can't find any customer)
        RequestBuilder request = get("/login").param("email", "jane.doe@email.com").param("password", "wrong_password");
        ResultMatcher expectedStatus = status().isUnauthorized();

        // Execute the request and set expectations
        this.mvc.perform(request).andExpect(expectedStatus);
    }


}
