package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@RestController
@RequestMapping("/api")
public class PositionInterface {
    Logger logger = LoggerFactory.getLogger(PositionInterface.class);

    @Autowired
    PositionService positionService;

    @GetMapping("positions")
    public ResponseEntity<List<PositionDTO>> getAll() {
        String c = "PositionInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        List<PositionDTO> positionDTOList = positionService.getPositions();
        logger.info(createLogMessage(c, m, "Success", "positionsSize", String.valueOf(positionDTOList.size())));
        return ResponseEntity.status(HttpStatus.OK).body(positionDTOList);
    }

}
