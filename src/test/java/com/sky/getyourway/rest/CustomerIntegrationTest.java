package com.sky.getyourway.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.getyourway.domain.Customer;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:getyourway-schema.sql", "classpath:getyourway-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class CustomerIntegrationTest {

    @Autowired
    // MockMvc used to test Spring
    private MockMvc mvc;

    @Autowired
    // converts JSON to java and java to JSON
    private ObjectMapper mapper;

    @Test
    void testRegisterUser() throws Exception{
        Customer testUser = new Customer("Mike", "Wilson", "mike.wilson@email.com", "password123");
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


}
