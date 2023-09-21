package com.sky.getyourway.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

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

}
