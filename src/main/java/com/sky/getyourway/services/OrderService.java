package com.sky.getyourway.services;

import com.duffel.DuffelApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class OrderService {

    private DuffelApiClient client;

    // Constructor injecting the Duffle client
    public OrderService(DuffelApiClient client) {
        this.client = client;
    }

    @Value("${api.key}")
    private String apiKey;  // api.key environment variable


    // Service created to make the requests to GET info from a placed/paid booking/order
    // Used within the SearchController viewBooking() method
    public JsonNode getOrderDetails(String orderId) {
        RestTemplate restTemplate = new RestTemplate();

        // Create HttpHeaders and set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey); // Example header, replace with your actual header
        headers.set("Duffel-Version", "v1");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        /* HEADERS:
        Accept-Encoding:gzip
        Accept:application/json
        Content-Type:application/json
        Duffel-Version:v1
        Authorization:Bearer duffel_test_dHzrkP2JCtGGKvH9XxobDCyy5mTv6yjI4Q-nPlR2iQV
         */

        // Create an HttpEntity with the headers
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        String apiUrl = "https://api.duffel.com/air/orders/" + orderId;
        // Make an HTTP request with the specified headers
        ResponseEntity<JsonNode> response1 = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                httpEntity,
                JsonNode.class
        );

        System.out.println(response1.getBody());

        return response1.getBody();
    }
}
