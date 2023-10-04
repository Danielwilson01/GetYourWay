package com.sky.getyourway.rest;

import com.duffel.DuffelApiClient;
import com.duffel.model.*;
import com.duffel.model.request.OfferRequest;
import com.duffel.model.request.OrderCancellationRequest;
import com.duffel.model.request.OrderRequest;
import com.duffel.model.request.Payment;
import com.duffel.model.response.Offer;
import com.duffel.model.response.OfferResponse;
import com.duffel.model.response.Order;
import com.duffel.model.response.OrderCancellation;
import com.duffel.model.response.offer.Segment;
import com.fasterxml.jackson.core.io.BigDecimalParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.domain.Pair;
import com.sky.getyourway.domain.User;
import com.sky.getyourway.dtos.*;
import com.sky.getyourway.services.BookingService;
import com.sky.getyourway.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
Search Controller manages the request received from front end for:
  - get flight offers
     - receives the info inputted by user
     - sends Duffle API the request to search offers for the parameters provided
     - returns a list of offers to front end
   - execute offer orders
     - receives the offer selected from user from front end
     - request from front end will include passengers details
     - executes the order with TEST payment
     - adds the order to MySQL database calling in the BookingService
   - cancels executed orders:
     - receives the order reference to be cancelled from the front end
     - sends the request to Duffle for the cancellation
     - removes the booking from the MySQL database
* */

@RestController
@RequestMapping("/search")
@CrossOrigin()
public class SearchController {

    // Instance of the DuffelApiClient imported class
    // Note that the dependency was added to the pom doc
    // used to interact with the Duffel API.
    private DuffelApiClient client;

    // service for booking table to enable adding/removing bookings to the MySQL table
    private BookingService bookingService;

    private OrderService orderService;

    // *******CONSTRUCTORS*******
    public SearchController(DuffelApiClient client, BookingService bookingService, OrderService orderService) {
        this.client = client;
        this.bookingService = bookingService;
        this.orderService = orderService;
    }


    // *******GETTER*******
    public DuffelApiClient getClient() {
        return client;
    }



    // *******REQUESTS*******

    /* Offers(): post request received from the front end when a user searches for a specific route
    @params sdto is an object of SearchDTO containing the search selection from users
            (airport FROM/TO, date DEPARTURE/RETURN, number of passengers ADULT/CHILD/INFANT)
    @return  List<Pair> which is a list of all the paired offers (inbound and outbound) for the selection made
    */
    @PostMapping("/flights")
    public List<Pair> offers(@RequestBody SearchDTO sdto) {

        // slices represent the slices to be sent to Duffle on the request:
        // outbound and inbound slice - Note that inbound slice is only sent for return flights
        List<OfferRequest.Slice> slices = new ArrayList<>();

        // outbound slice
        OfferRequest.Slice outboundSlice = new OfferRequest.Slice();
        outboundSlice.setDepartureDate(sdto.getDepartureDate());
        outboundSlice.setOrigin(sdto.getAirportFrom());
        outboundSlice.setDestination(sdto.getAirportTo());

        slices.add(outboundSlice);

        // inbound slice:
        // we check if there's a return date, otherwise is a one way request and no inbound needed
        if (sdto.getReturnDate() != null && !sdto.getReturnDate().isEmpty()) {
            OfferRequest.Slice inboundSlice = new OfferRequest.Slice();
            inboundSlice.setDepartureDate(sdto.getReturnDate());
            inboundSlice.setOrigin(sdto.getAirportTo());
            inboundSlice.setDestination(sdto.getAirportFrom());
            slices.add(inboundSlice);
        }

        // passengers
        List<Passenger> passengerList = new ArrayList<>();
        // checks for passenger type
        for (Map.Entry<String,Integer> passengerType : sdto.getPassengers().entrySet()) {
            String getType = passengerType.getKey();
            Integer amount = passengerType.getValue();
            for (int i = 0; i < amount; i++) {
                Passenger passenger = new Passenger();
                switch (getType) {
                    case "adult":
                        passenger.setType(PassengerType.adult);
                        break;
                    case "child":
                        passenger.setType(PassengerType.child);
                        break;
                    case "infant":
                        passenger.setType(PassengerType.infant_without_seat);
                        break;
                    default:
                }
                passengerList.add(passenger);
            }
        }

        // cabin class
        CabinClass cc = null;

        switch (sdto.getCabinClass()) {
            case "economy":
                cc = CabinClass.economy;
                break;
            case "first":
                cc = CabinClass.first;
                break;
            case "buisness":
                cc = CabinClass.business;
                break;
            case "premium_economy":
                cc = CabinClass.premium_economy;
                break;
        }

        // make offer request
        OfferRequest request = new OfferRequest();
        // Set the info for the request:
        request.setMaxConnections(sdto.getConnections());  // max connections
        request.setCabinClass(cc); // cabin class
        request.setSlices(slices);  // slices for outbound/inbound
        request.setPassengers(passengerList); // passenger list containing the passenger type

        // offer response
        OfferResponse offerResponse = client.offerRequestService.post(request);
        List<Offer> offers = offerResponse.getOffers();

        List<Pair> offerDTOPairList = new ArrayList<>();

        // Loop through all offers received to retrieve the information
        for (Offer offer : offers) {

            OfferDTO outboundDTO = new OfferDTO();
            OfferDTO inboundDTO = new OfferDTO();

            // Pair represents an offer pair containing outbound/inbound flights
            Pair journey = new Pair();
            journey.setCurrency(offer.getBaseCurrency());  // gets currency of the offer
            journey.setPrice(offer.getTotalAmount());  // gets total price of the offer
            journey.setOfferId(offer.getId());  // gets ID of the offer

            // Each slice (route) has segments containing the flight details of every flight for that route
            // note that we can have more than one flight is the user search is not constraint to direct flights only
            List<String> airlines = new ArrayList<>();
            List<String> flightNumbers = new ArrayList<>();
            List<String> stopsCodes = new ArrayList<>();
            List<String> airportStopsNames = new ArrayList<>();

            for (Segment segment : offer.getSlices().get(0).getSegments()) {
                airlines.add(segment.getOperatingCarrier().getName());
                if (segment.getOperatingCarrierFlightNumber() != null) {
                    flightNumbers.add(segment.getOperatingCarrierFlightNumber());
                } else {
                    flightNumbers.add("TBC");
                }
                stopsCodes.add(segment.getOrigin().getIataCode());
                airportStopsNames.add(segment.getOrigin().getName());
            }

            // We remove the first stop name and code as this is the origin airport and
            // shouldn't count as a stop
            airportStopsNames.remove(0);
            stopsCodes.remove(0);

            // Setting the values for the outbound results to be returned to front end
            outboundDTO.setAirportStopName(airportStopsNames);
            outboundDTO.setAirportStopCode(stopsCodes);
            outboundDTO.setAirline(airlines);
            outboundDTO.setFlightNumber(flightNumbers);
            outboundDTO.setStops(offer.getSlices().get(0).getSegments().size() - 1);
            outboundDTO.setDepartingAt(String.valueOf(offer.getSlices().get(0).getSegments().get(0).getDepartingAt()));
            outboundDTO.setArrivingAt(String.valueOf(offer.getSlices()
                    .get(0)
                    .getSegments()
                    .get(offer.getSlices().get(0).getSegments().size() - 1).getArrivingAt()));
            outboundDTO.setDestination(offer.getSlices().get(0).getDestination().getCityName());
            outboundDTO.setOrigin(offer.getSlices().get(0).getOrigin().getCityName());
            outboundDTO.setDestLat(offer.getSlices().get(0).getDestination().getLatitude());
            outboundDTO.setDestLong(offer.getSlices().get(0).getDestination().getLongitude());
            outboundDTO.setDuration(offer.getSlices().get(0).getDuration().toString());


            // If the search had a return date, set the values for the inbound result to be returned to front end
            if (sdto.getReturnDate() != null && !sdto.getReturnDate().isEmpty()) {

                airlines = new ArrayList<>();
                flightNumbers = new ArrayList<>();
                stopsCodes = new ArrayList<>();
                airportStopsNames = new ArrayList<>();

                for (Segment segment : offer.getSlices().get(1).getSegments()) {
                    airlines.add(segment.getOperatingCarrier().getName());
                    if (segment.getOperatingCarrierFlightNumber() != null) {
                        flightNumbers.add(segment.getOperatingCarrierFlightNumber());
                    } else {
                        flightNumbers.add("TBC");
                    }
                    stopsCodes.add(segment.getOrigin().getIataCode());
                    airportStopsNames.add(segment.getOrigin().getName());
                }
                airportStopsNames.remove(0);
                stopsCodes.remove(0);

                inboundDTO.setAirportStopName(airportStopsNames);
                inboundDTO.setAirportStopCode(stopsCodes);
                inboundDTO.setFlightNumber(flightNumbers);
                inboundDTO.setStops(offer.getSlices().get(1).getSegments().size() - 1);
                inboundDTO.setDepartingAt(String.valueOf(offer.getSlices().get(1).getSegments().get(0).getDepartingAt()));
                inboundDTO.setArrivingAt(String.valueOf(offer.getSlices()
                        .get(1)
                        .getSegments()
                        .get(offer.getSlices().get(1).getSegments().size() - 1).getArrivingAt()));
                inboundDTO.setDestination(offer.getSlices().get(1).getDestination().getCityName());
                inboundDTO.setOrigin(offer.getSlices().get(1).getOrigin().getCityName());
                inboundDTO.setDestLat(offer.getSlices().get(1).getDestination().getLatitude());
                inboundDTO.setDestLong(offer.getSlices().get(1).getDestination().getLongitude());
                inboundDTO.setDuration(offer.getSlices().get(1).getDuration().toString());
                journey.setInboundFlight(inboundDTO);
            }

            journey.setOutboundFlight(outboundDTO);

            // sets the list of passengers (only containing the types at this point)
            List<Passenger> passengersInOffer = offer.getPassengers();
            List<String> passengerIds = new ArrayList<>();
            for (Passenger p : passengersInOffer) {
                passengerIds.add(p.getId());
            }

            journey.setPassengerIds(passengerIds);
            journey.setPassengersType(passengerList.stream().map(p -> p.getType().toString()).collect(Collectors.toList()));
            offerDTOPairList.add(journey);
        }


        return offerDTOPairList;
    }


    /* Oder(): post request received from the front end when a user selects an offer to proceed with its booking
       @params odto is an object of ObjectDTO containing the offer details and Passengers list with their duffle id
       @return oder ID as a String
       */
    @PostMapping("/order")
    public String order(@RequestBody OrderDTO odto) {

        // set passengers details
        List<OrderPassenger> orderPassengers = new ArrayList<>();

        // Loop through the passengers list from the order to set their details values to the
        // actual info provided by the user for each of them
        for (PassengerDTO p : odto.getPassengersDetails()) {
            OrderPassenger orderPassenger = new OrderPassenger();
            orderPassenger.setEmail(p.getEmail());
            orderPassenger.setGivenName(p.getGivenName());
            orderPassenger.setFamilyName(p.getFamilyName());
            orderPassenger.setTitle(p.getTitle());
            orderPassenger.setBornOn(p.getDob());
            orderPassenger.setId(p.getPassengerId());
            orderPassenger.setPhoneNumber(p.getPhoneNumber());
            orderPassenger.setGender(p.getGender());
            orderPassengers.add(orderPassenger);
        }

        // Payment: due to API limitations, we cannot set a client_token to take test card details
        // Payment is executed using the below
        Payment payment = new Payment();
        payment.setType(PaymentType.balance);
        payment.setAmount(BigDecimalParser.parse(odto.getPrice()));
        payment.setCurrency(odto.getCurrency());

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setType(OrderType.instant);
        orderRequest.setPayments(List.of(payment));
        orderRequest.setSelectedOffers(List.of(odto.getOfferId()));
        orderRequest.setPassengers(orderPassengers);

        Order order = client.orderService.post(orderRequest);

        // Get the user id that executed this order. This is to enable adding the new booking
        // onto the booking MySQL table using the id to identify the user to be assigned as foreign key
        User user = new User();
        user.setId(odto.getUserId());

        this.bookingService.addBooking(new Booking(order.getId(), user));  // adds the booking to MySQL table

        return order.getId();
    }



    /* Cancel(): delete request received from the front end when a user selects a paid offer to be cancelled
       @params orderId duffle ID identifying a specific order/booking
       @return string confirming the booking
       */
    @DeleteMapping("/cancel/{orderId}")
    public String cancelBooking(@PathVariable String orderId) {

        OrderCancellationRequest orderCancel = new OrderCancellationRequest();
        orderCancel.setOrderId(orderId);

        Booking booking = this.bookingService.findBookingByOrderReference(orderId);
        this.bookingService.cancelBooking(booking.getId());  // gets primary key for the corresponding duffle ref

        OrderCancellation cancellation = client.orderCancellationService.post(orderCancel); // Cancels with Duffel API
        cancellation = client.orderCancellationService.confirm(cancellation.getId());  // Removed from database

        return "🙅 Cancelled order: " + cancellation.getOrderId() + "\n" +
                "Cancellation Id: " + cancellation.getId() + "\n" + "At: " + cancellation.getConfirmedAt();
    }


    /* viewBooking(): method to retrieve the information about a specific booking a user paid
       @params orderId duffle ID identifying a specific order/booking
       @return FlightDTO object containing all the info relating to the flights from that booking

       NOTE: in this case we couldn't use the "client" object of DuffelApiClient and a new Service had to
       be created for it (OrderService)
       */
    @GetMapping("/view/{orderId}")
    public FlightDTO viewBooking(@PathVariable String orderId) {

        // outerNode represents the main tree-structure of the JSON response of obtaining the order info from Duffle
        // NOTE: each level of the JSON file is considered a node which we will use to traverse the tree
        JsonNode outerNode = orderService.getOrderDetails(orderId);

        JsonNode orderNode = outerNode.get("data");  // node containing the info about the order

        String totalAmount = orderNode.get("total_amount").asText();  // .asText() retrieves the data from the node
        String totalCurrency = orderNode.get("total_currency").asText();

        ArrayNode slices = (ArrayNode) orderNode.get("slices");

        // Lists journeyTo & journeyBack will contain the info relating to each way: outbound / inbound
        List<String> journeyTo = new ArrayList<>();
        List<String> journeyBack = new ArrayList<>();

        // Retrieve the outbound data. Note that getJourney() is a private method created to be used, defined below
        getJourney(slices, journeyTo, 0);

        // Retrieves the inbound (return flight) only if there's more than one slice (each slice is a "way" in/out)
        if (slices.size() > 1) {
            getJourney(slices, journeyBack, 1);
        }

        // Passengers booked on that journey to be stored in a List
        List<String> passengers = new ArrayList<>();

        // Using the node navigation made above, retrieve the passenger info
        ArrayNode passengerArray = (ArrayNode) orderNode.get("passengers");

        // Iterates through all the passengers and retrieves the info to show (name/surname)
        for (JsonNode passenger : passengerArray) {

            String firstName = passenger.get("given_name").asText();
            String familyName = passenger.get("family_name").asText();

            passengers.add(firstName + " " + familyName);
        }

        // FlightDTO is the DTO created for the return of info to be sent back to front end
        FlightDTO flightDTO = new FlightDTO();

        flightDTO.setCost(totalAmount + " " + totalCurrency);
        flightDTO.setJourneyBack(journeyBack);
        flightDTO.setJourneyTo(journeyTo);
        flightDTO.setPassengers(passengers);

        return flightDTO;
    }


    /* getJourney() is a method created for its use within the GET request method viewBooking() to help retrieving
    the info of the outbound and inbound flights booked
    @params slices (array containing [outboundData, inboundData]. Index 1 can be null if the flight is one way)
            journey (List of strings to hold the flight details)
            index  (int to specify the index of the slice to get: 0 for outbound, 1 for inbound)
    @return void no return as this method main goal is to add info to the respective journey Lists
    */
    private static void getJourney(ArrayNode slices, List<String> journey, int index) {

        // All info comes from the secgments which is the part to store each way's data
        // Note that segments can contain nested segments if there are connectins between flights
        ArrayNode segmentsTo = (ArrayNode) slices.get(index).get("segments");

        // Loop through all flights relating to that journey, gett and add the info
        for (JsonNode segment : segmentsTo) {

            JsonNode origin = segment.get("origin");
            String airportName = origin.get("name").asText();
            String iataCode = origin.get("iata_code").asText();
            String flightNumber = segment.get("operating_carrier_flight_number").asText();
            String name = segment.get("operating_carrier").get("name").asText();
            String departTime = segment.get("departing_at").asText();

            StringBuilder sb1 = new StringBuilder();
            sb1.append("Origin: ").append(iataCode).append(" ").append(airportName);
            journey.add(sb1.toString());
            journey.add("Departing at: " + departTime);

            JsonNode dest = segment.get("destination");
            airportName = dest.get("name").asText();
            iataCode = dest.get("iata_code").asText();

            String arriveTime = segment.get("arriving_at").asText();

            StringBuilder sb2 = new StringBuilder();
            sb2.append("Destination: ").append(iataCode).append(" ").append(airportName);
            journey.add(sb2.toString());
            journey.add("Arriving at: " + arriveTime);

            journey.add("Flight Number: " + flightNumber);
            journey.add("Airline: " + name);

            // Guidance for the Strings info to be stored:
            // Origin: DEL Indra Gandhi Airport     16:30 2024-01-02
            // Destination: LHR London Heathrow     20:00 2024-01-02
            // Flight Number: 1243
            // Airline: Duffel Airways
        }
    }

    @GetMapping("/allUserBookings/{userId}")
    public List<FlightDTO> allUserBookings(@PathVariable int userId) {
        List<FlightDTO> results = new ArrayList<>();  // list to contain all Flights info for user orders

        // get all orders for the given user
        List<BookingDTO> bookings = this.bookingService.getBookingsByUserID(userId);
        System.out.println("Bookings: " + bookings);


        // Loop through all orders to get the FlightDTOs and add them to results
        for(BookingDTO b : bookings) {
            String orderRef = b.getOrderReference();
            FlightDTO flight = viewBooking(orderRef);
            System.out.println("Flight: " + flight);
            results.add(flight);
            System.out.println("Results: " + results);
        }
        return results;

    }
}
