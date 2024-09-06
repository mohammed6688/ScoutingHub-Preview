package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.MatchReport;
import com.krnal.products.scoutinghub.model.Team;
import com.krnal.products.scoutinghub.model.VideoReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoReportRepo extends JpaRepository<VideoReport, Integer>, JpaSpecificationExecutor<VideoReport> {
    VideoReport findById(int id);

    Optional<VideoReport> findByTitle(String title);
    List<VideoReport> findAllBy();

    void deleteById(Integer id);

}
