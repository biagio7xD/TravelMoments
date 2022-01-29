package com.units.backend.repository.travels;

import com.units.backend.model.travels.Travel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends MongoRepository<Travel, String> {

    List<Travel> findTravelsByIdUser(String idUser);

    List<Travel> findTravelRoutesByDate(String date);

    List<Travel> findTravelsByIdUserAndDate(String idUser, String date);

    Travel findTravelByIdAndIdUser(String id, String idUser);


}
