package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.FactorRepo;
import com.krnal.products.scoutinghub.dao.PositionRepo;
import com.krnal.products.scoutinghub.dto.FactorDTO;
import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.mapper.FactorMapper;
import com.krnal.products.scoutinghub.mapper.PositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@Service
public class FactorService {
    Logger logger = LoggerFactory.getLogger(FactorService.class);

    @Autowired
    FactorRepo factorRepo;
    @Autowired
    FactorMapper factorMapper;

    public List<FactorDTO> getFactors() {
        String c = "FactorService";
        String m = "getFactors";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<FactorDTO> factorDTOList = factorRepo.findAllBy().stream()
                    .map(factor -> factorMapper.getFactorDTO(factor))
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return factorDTOList;
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

}
