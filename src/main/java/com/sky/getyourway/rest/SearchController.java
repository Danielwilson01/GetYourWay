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
import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.domain.Pair;
import com.sky.getyourway.domain.User;
import com.sky.getyourway.dtos.OfferDTO;
import com.sky.getyourway.dtos.OrderDTO;
import com.sky.getyourway.dtos.PassengerDTO;
import com.sky.getyourway.dtos.SearchDTO;
import com.sky.getyourway.services.BookingService;
import com.sky.getyourway.services.BookingServiceDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
Search Controller manages the request received from front end for:
  - get flight offers
     - receives the info inputted by user
     - sends Duffle API the request to search offers for the parameters privided
     - returns a list of offers to front end
   - execute offer orders
     - receives the offer selected from user from front end
     - request from front end will include passengers details
     - executes the order with TEST payment
* */

@RestController
@RequestMapping("/search")
@CrossOrigin()
public class SearchController {

    private DuffelApiClient client;
    private BookingService bookingService;
    public SearchController(DuffelApiClient client, BookingService bookingService) {
        this.client = client;
        this.bookingService = bookingService;
    }

    @PostMapping("/flights")
    public List<Pair> offers(@RequestBody SearchDTO sdto) {

        List<OfferRequest.Slice> slices = new ArrayList<>();

        // outbound slice
        OfferRequest.Slice outboundSlice = new OfferRequest.Slice();
        outboundSlice.setDepartureDate(sdto.getDepartureDate());
        outboundSlice.setOrigin(sdto.getAirportFrom());
        outboundSlice.setDestination(sdto.getAirportTo());

        slices.add(outboundSlice);

        // inbound slice
        if (sdto.getReturnDate() != null && !sdto.getReturnDate().isEmpty()) {
            OfferRequest.Slice inboundSlice = new OfferRequest.Slice();
            inboundSlice.setDepartureDate(sdto.getReturnDate());
            inboundSlice.setOrigin(sdto.getAirportTo());
            inboundSlice.setDestination(sdto.getAirportFrom());
            slices.add(inboundSlice);
        }

        List<Passenger> passengerList = new ArrayList<>();

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

        // make offer request
        OfferRequest request = new OfferRequest();
        request.setMaxConnections(sdto.getConnections());

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

        request.setCabinClass(cc);
        request.setSlices(slices);
        request.setPassengers(passengerList);

        // offer response
        OfferResponse offerResponse = client.offerRequestService.post(request);
        List<Offer> offers = offerResponse.getOffers();

        List<Pair> offerDTOPairList = new ArrayList<>();

        for (Offer offer : offers) {

            OfferDTO outboundDTO = new OfferDTO();
            OfferDTO inboundDTO = new OfferDTO();

            Pair journey = new Pair();
            journey.setCurrency(offer.getBaseCurrency());
            journey.setPrice(offer.getTotalAmount());
            journey.setOfferId(offer.getId());

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

            airportStopsNames.remove(0);
            stopsCodes.remove(0);

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

    @PostMapping("/order")
    public String order(@RequestBody OrderDTO odto) {

        List<OrderPassenger> orderPassengers = new ArrayList<>();

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

        User user = new User();
        user.setId(odto.getUserId());

        this.bookingService.addBooking(new Booking(order.getId(), user));

        return order.getId();
    }

    @DeleteMapping("/cancel/{orderId}")
    public String cancelBooking(@PathVariable String orderId) {

        OrderCancellationRequest orderCancel = new OrderCancellationRequest();
        orderCancel.setOrderId(orderId);

        Booking booking = this.bookingService.getBookingByOrderReference(orderId);
        this.bookingService.cancelBooking(booking.getId());

        OrderCancellation cancellation = client.orderCancellationService.post(orderCancel);
        cancellation = client.orderCancellationService.confirm(cancellation.getId());

        return "🙅 Cancelled order: " + cancellation.getOrderId() + "\n" +
                "Cancellation Id: " + cancellation.getId() + "\n" + "At: " + cancellation.getConfirmedAt();
    }

    public DuffelApiClient getClient() {
        return client;
    }
}
