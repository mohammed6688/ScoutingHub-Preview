package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.Position;
import com.krnal.products.scoutinghub.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepo extends JpaRepository<Position, Integer> {
    Position findById(int id);
    Optional<Position> findByPositionName(String name);
    List<Position> findAllBy();
    void deleteById(Integer id);

}
