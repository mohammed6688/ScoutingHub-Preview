package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.Formation;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.PlayerMatchReport;
import com.krnal.products.scoutinghub.model.ShadowList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShadowListRepo extends JpaRepository<ShadowList, Integer>, JpaSpecificationExecutor<ShadowList> {
    ShadowList findById(int id);

    Optional<ShadowList> findByName(String name);

    Optional<ShadowList> findByFormation(Formation formation);

    List<ShadowList> findAllBy();

    void deleteById(Integer id);

}
