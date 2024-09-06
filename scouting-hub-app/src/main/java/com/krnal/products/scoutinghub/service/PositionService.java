package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.PositionRepo;
import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.mapper.PositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@Service
public class PositionService {
    Logger logger = LoggerFactory.getLogger(PositionService.class);

    @Autowired
    PositionRepo positionRepo;
    @Autowired
    PositionMapper positionMapper;

    public List<PositionDTO> getPositions() {
        String c = "PositionService";
        String m = "getPositions";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<PositionDTO> positionDTOList = positionRepo.findAllBy().stream()
                    .map(position -> positionMapper.getPositionDTO(position))
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return positionDTOList;
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

}
