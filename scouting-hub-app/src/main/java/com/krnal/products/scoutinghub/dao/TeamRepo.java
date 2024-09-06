package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.Team;
import com.krnal.products.scoutinghub.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepo extends JpaRepository<Team, Integer>, JpaSpecificationExecutor<Team> {
    Team findById(int id);
    Optional<Team> findByTeamName(String name);
    @Query("SELECT p FROM Player p WHERE p.team.id = :teamId")
    Optional<List<Player>> findPlayersByTeamId(@Param("teamId") Integer teamId);
    List<Team> findAllBy();
    void deleteById(Integer id);

}
