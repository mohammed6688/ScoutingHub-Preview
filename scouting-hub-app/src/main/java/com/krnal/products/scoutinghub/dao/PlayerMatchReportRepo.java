package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.MatchReport;
import com.krnal.products.scoutinghub.model.PlayerMatchReport;
import com.krnal.products.scoutinghub.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerMatchReportRepo extends JpaRepository<PlayerMatchReport, Integer>, JpaSpecificationExecutor<PlayerMatchReport> {
    PlayerMatchReport findById(int id);

    List<PlayerMatchReport> findAllByPlayerId(Integer playerId);

    void deleteById(Integer id);

}
