package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepo extends JpaRepository<Zone, Integer>, JpaSpecificationExecutor<Zone> {
    Zone findById(int id);
    Optional<Zone> findByZoneName(String name);
    List<Zone> findAllBy();
    void deleteById(Integer id);

}
