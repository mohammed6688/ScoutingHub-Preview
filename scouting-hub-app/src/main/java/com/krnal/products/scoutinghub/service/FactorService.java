package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.FactorRepo;
import com.krnal.products.scoutinghub.dto.FactorDTO;
import com.krnal.products.scoutinghub.mapper.FactorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@Service
public class FactorService {
    private static final Logger logger = LoggerFactory.getLogger(FactorService.class);

    private final FactorRepo factorRepo;
    private final FactorMapper factorMapper;

    public FactorService(FactorRepo factorRepo, FactorMapper factorMapper) {
        this.factorRepo = factorRepo;
        this.factorMapper = factorMapper;
    }

    public List<FactorDTO> getFactors() {
        String c = "FactorService";
        String m = "getFactors";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<FactorDTO> factorDTOList = factorRepo.findAllBy().stream()
                    .map(factorMapper::getFactorDTO)
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return factorDTOList;
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

}
