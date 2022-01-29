package com.units.backend.repository.travels;

import com.units.backend.model.travels.Place;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends MongoRepository<Place, String> {

    List<Place> findPlaceByIdUser(String idUser);

    List<Place> findPlacesByDateAndIdUser(String date, String idUser);

    Optional<Place> findPlaceByIdAndIdUser(String id, String idUser);

    List<Place> findPlaceByIdUserAndIdTravel(String idUser, String idTravel);

}
