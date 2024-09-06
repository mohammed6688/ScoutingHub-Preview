package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.ShadowListPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShadowListPlayerRepo extends JpaRepository<ShadowListPlayer, Integer>, JpaSpecificationExecutor<ShadowListPlayer> {
    ShadowListPlayer findById(int id);

    @Query("SELECT slp FROM ShadowListPlayer slp WHERE slp.shadowList.id = :shadowListId")
    Optional<List<ShadowListPlayer>> findByShadowListId(@Param("shadowListId") Integer shadowListId);

    @Query("SELECT slp FROM ShadowListPlayer slp WHERE slp.player.id = :playerId")
    Optional<List<ShadowListPlayer>> findByPlayerId(@Param("playerId") Integer playerId);

    @Query("SELECT slp FROM ShadowListPlayer slp WHERE slp.position.id = :positionId")
    Optional<List<ShadowListPlayer>> findByPositionId(@Param("positionId") Integer positionId);

    List<ShadowListPlayer> findAllBy();

    void deleteById(Integer id);

}
