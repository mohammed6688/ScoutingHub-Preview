package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormationRepo extends JpaRepository<Formation, Integer> {

    Formation findById(int id);

    Optional<Formation> findByName(String name);

    List<Formation> findAllBy();

    void deleteById(Integer id);
}