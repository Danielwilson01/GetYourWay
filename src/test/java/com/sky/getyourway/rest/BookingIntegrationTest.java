package com.sky.getyourway.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.getyourway.domain.Booking;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:getyourway-schema.sql", "classpath:getyourway-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class BookingIntegrationTest {

    @Autowired
    // MockMvc used to test Spring
    private MockMvc mvc;

    @Autowired
    // converts JSON to java and java to JSON
    private ObjectMapper mapper;

//    @Test
//    void testAddBooking() throws Exception{
//        User testUser = new User("Mike", "Wilson", "mike.wilson@email.com", "password123");
//        Booking testBooking = new Booking("ord_kdjksfjgl", testUser);
//
//        String reqBody = this.mapper.writeValueAsString((testBooking));
//
//        RequestBuilder req = post("/booking/add").content(reqBody).contentType(MediaType.APPLICATION_JSON);
//        ResultMatcher checkStatus = status().isCreated();
//        testUser.setId(3);
//        String resBody = this.mapper.writeValueAsString(testUser);
//        ResultMatcher checkBody = content().json(resBody);
//        mvc.perform(req).andExpect(checkStatus).andExpect(checkBody);
//
//
//    }

}
