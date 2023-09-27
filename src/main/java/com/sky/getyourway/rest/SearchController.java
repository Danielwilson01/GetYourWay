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
import com.sky.getyourway.domain.Pair;
import com.sky.getyourway.dtos.FlightDTO;
import com.sky.getyourway.dtos.OfferDTO;
import com.sky.getyourway.dtos.SearchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    private DuffelApiClient client;
    public SearchController() {
        client = new DuffelApiClient("duffel_test_0JLxWFOyyC7Va53B0658zEldoVi5Pvp65F-UQYeKvKB");
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
        System.out.println("Offer response id: " + offerResponse.getId());
        List<Offer> offers = offerResponse.getOffers();

        List<Pair> offerDTOPairList = new ArrayList<>();

        for (Offer offer : offers) {

            OfferDTO outboundDTO = new OfferDTO();
            OfferDTO inboundDTO = new OfferDTO();

            Pair journey = new Pair();
            journey.setCurrency(offer.getBaseCurrency());
            journey.setPrice(offer.getTotalAmount().intValue());
            journey.setOfferId(offer.getId());

            List<String> airlines = new ArrayList<>();
            List<String> flightNumbers = new ArrayList<>();
            for (Segment segment : offer.getSlices().get(0).getSegments()) {
                airlines.add(segment.getOperatingCarrier().getName());
                flightNumbers.add(segment.getOperatingCarrierFlightNumber());
            }
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

            System.out.println(offer.getSlices().get(0).getDuration().toString());

            journey.setOutboundFlight(outboundDTO);

            offerDTOPairList.add(journey);
        }


        return offerDTOPairList;
    }

    public DuffelApiClient getClient() {
        return client;
    }
}
