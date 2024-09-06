package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.Factor;
import com.krnal.products.scoutinghub.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FactorRepo extends JpaRepository<Factor, Integer> {
    Factor findById(int id);

    Optional<Factor> findByName(String name);

    List<Factor> findAllBy();

    void deleteById(Integer id);

}
