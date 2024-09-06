package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.MatchReport;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchReportRepo extends JpaRepository<MatchReport, Integer>, JpaSpecificationExecutor<MatchReport> {
    MatchReport findById(int id);

    Optional<MatchReport> findByFirstTeamAndSecondTeamAndMatchDate(Team firstTeam, Team secondTeam, Date matchDate);

    List<MatchReport> findAllBy();

    void deleteById(Integer id);

}
