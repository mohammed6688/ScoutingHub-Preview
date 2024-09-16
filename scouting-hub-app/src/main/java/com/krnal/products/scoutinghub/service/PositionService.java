package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.PositionRepo;
import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.mapper.PositionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@Service
public class PositionService {
    private static final Logger logger = LoggerFactory.getLogger(PositionService.class);

    private final PositionRepo positionRepo;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepo positionRepo, PositionMapper positionMapper) {
        this.positionRepo = positionRepo;
        this.positionMapper = positionMapper;
    }

    public List<PositionDTO> getPositions() {
        String c = "PositionService";
        String m = "getPositions";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<PositionDTO> positionDTOList = positionRepo.findAllBy().stream()
                    .map(positionMapper::getPositionDTO)
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return positionDTOList;
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

}
