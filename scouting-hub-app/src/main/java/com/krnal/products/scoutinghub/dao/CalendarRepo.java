package com.krnal.products.scoutinghub.dao;

import com.krnal.products.scoutinghub.model.Calendar;
import com.krnal.products.scoutinghub.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepo extends JpaRepository<Calendar, Integer> {
    Calendar findById(int id);
    List<Calendar> findAllBy();
    void deleteById(Integer id);

}
