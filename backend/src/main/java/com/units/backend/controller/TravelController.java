package com.units.backend.controller;

import com.units.backend.model.travels.Place;
import com.units.backend.model.travels.Travel;
import com.units.backend.payload.request.travel.PlaceRequest;
import com.units.backend.payload.request.travel.TravelRequest;
import com.units.backend.repository.travels.PlaceRepository;
import com.units.backend.repository.travels.TravelRepository;
import com.units.backend.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/travel")
public class TravelController {

    private final Logger logger = LoggerFactory.getLogger(TravelController.class);
    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TravelRepository travelRepository;

    @PostMapping(value = "add/{idUser}/{idTravel}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addPlaces(@RequestBody List<PlaceRequest> placeRequestList, @PathVariable String idUser, @PathVariable String idTravel) {
        try {
            for (PlaceRequest placeRequest : placeRequestList) {
                Place addPlace = new Place(
                        idUser,
                        idTravel,
                        placeRequest.getLng(),
                        placeRequest.getLat(),
                        Objects.equals(placeRequest.getDescription(), "") ? "Not Available" : placeRequest.getDescription(),
                        placeRequest.getAddress(),
                        placeRequest.getCity(),
                        placeRequest.getCountry(),
                        placeRequest.getDate());
                placeRepository.save(addPlace);
            }
            return new ResponseEntity<>("Travel added", HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "add/{idUser}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addTravel(@RequestBody TravelRequest travelRequest, @PathVariable String idUser) {
        try {
            Travel addTravel = new Travel();
            addTravel.setDate(travelRequest.getDate());
            addTravel.setLatAndLng(travelRequest.getLatAndLng());
            addTravel.setIdUser(idUser);
            addTravel.setRouteInfo(Objects.equals(travelRequest.getRouteInfo(), "") ? "Not available" : travelRequest.getRouteInfo());
            addTravel.setTransport(Objects.equals(travelRequest.getTransport(), "") ? "Not Available" : travelRequest.getTransport());
            travelRepository.save(addTravel);
            return new ResponseEntity<>(addTravel.getId(), HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "{idUser}/places", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllPlaces(@PathVariable String idUser) {
        List<Place> placeList = placeRepository.findPlaceByIdUser(idUser);
        return new ResponseEntity<>(placeList, HttpStatus.OK);
    }

    @GetMapping(value = "{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllTravels(@PathVariable String idUser) {
        List<Travel> travelList = travelRepository.findTravelsByIdUser(idUser);
        return new ResponseEntity<>(travelList, HttpStatus.OK);
    }

    @GetMapping(value = "{idUser}/dates", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getTravelDates(@PathVariable String idUser) {
        List<Travel> travelList = travelRepository.findTravelsByIdUser(idUser);
        List<String> datesList = travelList.stream().map(Travel::getDate).distinct().collect(Collectors.toList());
        return new ResponseEntity<>(datesList, HttpStatus.OK);
    }


    @GetMapping(value = "{idUser}/{date}/places", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getPlaceByDate(@PathVariable String idUser, @PathVariable String date) {
        try {
            List<Place> placeListByDateAndIdUser = placeRepository.findPlacesByDateAndIdUser(date, idUser);
            if (placeListByDateAndIdUser.size() == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(placeListByDateAndIdUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "{idUser}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getTravelByDate(@PathVariable String idUser, @PathVariable String date) {
        try {
            List<Travel> travelListByDateAndIdUser = travelRepository.findTravelsByIdUserAndDate(idUser, date);
            if (travelListByDateAndIdUser.size() == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(travelListByDateAndIdUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "place/{idUser}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePlace(@PathVariable String id, @PathVariable String idUser, @RequestBody Place updatePlace) {
        Optional<Place> optionalLocation = placeRepository.findPlaceByIdAndIdUser(id, idUser);
        if (optionalLocation.isPresent()) {
            Place _place = optionalLocation.get();
            _place.setAddress(updatePlace.getAddress());
            _place.setDate(updatePlace.getDate());
            _place.setDescription(updatePlace.getDescription());
            _place.setLng(updatePlace.getLng());
            _place.setLat(updatePlace.getLat());
            _place.setCity(updatePlace.getCity());
            _place.setCountry(updatePlace.getCountry());
            return new ResponseEntity<>(placeRepository.save(_place), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "{idUser}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTravelRoutes(@PathVariable String id, @PathVariable String idUser, @RequestBody Travel updateTravel) {
        try {
            Travel travel = travelRepository.findTravelByIdAndIdUser(id, idUser);
            List<Place> placeList = placeRepository.findPlaceByIdUserAndIdTravel(idUser, travel.getId());
            if (!travel.getDate().equals(updateTravel.getDate())) {
                for (Place place : placeList) {
                    place.setDate(updateTravel.getDate());
                    placeRepository.save(place);
                }
            }
            //update travel route
            travel.setTransport(updateTravel.getTransport());
            travel.setRouteInfo(updateTravel.getRouteInfo());
            travel.setDate(updateTravel.getDate());
            travel.setLatAndLng(updateTravel.getLatAndLng());
            return new ResponseEntity<>(travelRepository.save(travel), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{idUser}/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTravel(@PathVariable String id, @PathVariable String idUser) {
        try {
            Travel travel = travelRepository.findTravelByIdAndIdUser(id, idUser);
            List<Place> placeList = placeRepository.findPlaceByIdUserAndIdTravel(idUser, travel.getId());
            if (placeList.size() > 0) {
                placeRepository.deleteAll(placeList);
            }
            travelRepository.delete(travel);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "place/{idUser}/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePlace(@PathVariable String id, @PathVariable String idUser) {
        try {
            Optional<Place> optionalLocation = placeRepository.findPlaceByIdAndIdUser(id, idUser);
            optionalLocation.ifPresent(Place -> placeRepository.delete(Place));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
