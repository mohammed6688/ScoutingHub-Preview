package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.MatchReport;
import com.krnal.products.scoutinghub.model.Rating;
import com.krnal.products.scoutinghub.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepo extends JpaRepository<Rating, Integer>, JpaSpecificationExecutor<Rating> {
    Rating findById(int id);

    Optional<Rating> findByValue(Integer value);

    @Query("SELECT r FROM Rating r JOIN r.playerMatchReport pmr JOIN pmr.player p WHERE p.id = :playerId")
    List<Rating> findRatingsByPlayerId(@Param("playerId") Integer playerId);

    List<Rating> findAllBy();

    void deleteById(Integer id);

}
