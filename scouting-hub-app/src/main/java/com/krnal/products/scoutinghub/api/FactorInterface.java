package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.dto.FactorDTO;
import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.service.FactorService;
import com.krnal.products.scoutinghub.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@RestController
@RequestMapping("/api")
public class FactorInterface {
    Logger logger = LoggerFactory.getLogger(FactorInterface.class);

    @Autowired
    FactorService factorService;

    @GetMapping("factors")
    public ResponseEntity<List<FactorDTO>> getAll() {
        String c = "PositionInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        List<FactorDTO> factorDTOList = factorService.getFactors();
        logger.info(createLogMessage(c, m, "Success", "FactorsSize", String.valueOf(factorDTOList.size())));
        return ResponseEntity.status(HttpStatus.OK).body(factorDTOList);
    }

}
