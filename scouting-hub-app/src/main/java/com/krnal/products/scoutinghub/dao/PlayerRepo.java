package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.PlayerMatchReport;
import com.krnal.products.scoutinghub.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepo extends JpaRepository<Player, Integer>, JpaSpecificationExecutor<Player> {
    Player findById(int id);
    Optional<Player> findByName(String name);
    @Query("SELECT p FROM PlayerMatchReport p WHERE p.player.id = :playerId")
    Optional<List<PlayerMatchReport>> findPlayerMatchReportByPlayerId(@Param("playerId") Integer playerId);
    List<Player> findAllBy();
    void deleteById(Integer id);

}
