package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.ZoneRepo;
import com.krnal.products.scoutinghub.specification.SimpleSpecification;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import com.krnal.products.scoutinghub.dto.ZoneDTO;
import com.krnal.products.scoutinghub.mapper.ZoneMapper;
import com.krnal.products.scoutinghub.model.Zone;
import com.krnal.products.scoutinghub.specification.ZoneDecorator;
import com.krnal.products.scoutinghub.types.ZoneResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.krnal.products.scoutinghub.constants.Constant.*;
import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@Service
public class ZoneService {
    private static final Logger logger = LoggerFactory.getLogger(ZoneService.class);

    private final ZoneRepo zoneRepo;
    private final ZoneMapper zoneMapper;
    private final ZoneUpdateHelper zoneUpdateHelper;

    public ZoneService(ZoneRepo zoneRepo, ZoneMapper zoneMapper, ZoneUpdateHelper zoneUpdateHelper) {
        this.zoneRepo = zoneRepo;
        this.zoneMapper = zoneMapper;
        this.zoneUpdateHelper = zoneUpdateHelper;
    }

    public ZoneResponse getZones() {
        String c = "ZoneService";
        String m = "getZones";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<Zone> zones = zoneRepo.findAll();
            List<ZoneDTO> zoneDTOList = zones.stream()
                    .map(zoneMapper::getZoneDTO)
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return new ZoneResponse(zoneDTOList, zones.size());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public ZoneResponse getZones(Pageable pageable) {
        String c = "ZoneService";
        String m = "getZones";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Page<Zone> zones = zoneRepo.findAll(pageable);
            List<ZoneDTO> zoneDTOList = zones.stream()
                    .map(zoneMapper::getZoneDTO)
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return new ZoneResponse(zoneDTOList, zones.getTotalElements());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public ZoneDTO getZone(Integer id) {
        String c = "ZoneService";
        String m = "getZone";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Zone> zone = zoneRepo.findById(id);
            if (zone.isPresent()) {
                ZoneDTO zoneDTO = zoneMapper.getZoneDTO(zone.get());
                logger.info(createLogMessage(c, m, "Success"));
                return zoneDTO;
            }
            throw new RuntimeException(ZONE_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public ZoneDTO addZone(ZoneDTO zoneDTO) {
        String c = "ZoneService";
        String m = "addZone";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Zone> zoneNameOptional = zoneRepo.findByZoneName(zoneDTO.getZoneName());
            if (zoneNameOptional.isEmpty()) { // if provided zone name is not exist
                Zone zone = zoneRepo.save(zoneMapper.getZone(zoneDTO));
                logger.info(createLogMessage(c, m, "Success"));
                return zoneMapper.getZoneDTO(zone);
            }
            throw new RuntimeException(ZONE_NAME_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public ZoneDTO updateZone(ZoneDTO zoneDTO) {
        String c = "ZoneService";
        String m = "updateZone";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Zone> zoneOptional = zoneRepo.findById(zoneDTO.getId());
            if (zoneOptional.isPresent()) { // if provided zone id is existed
                Zone zone = zoneOptional.get();
                zoneUpdateHelper.set(zone, zoneDTO);
                zone = zoneRepo.save(zone);
                logger.info(createLogMessage(c, m, "Success"));
                return zoneMapper.getZoneDTO(zone);
            }
            throw new RuntimeException(ZONE_ID_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public void deleteZone(Integer id) {
        String c = "ZoneService";
        String m = "updateZone";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Zone> zoneOptional = zoneRepo.findById(id);
            if (zoneOptional.isPresent()) { // if provided zone id is existed
                zoneRepo.deleteById(id);
                logger.info(createLogMessage(c, m, "Success"));
                return;
            }
            throw new RuntimeException(ZONE_ID_NOT_EXIST);
        } catch (DataIntegrityViolationException e){
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(ZONE_IS_REFERENCED, e); // Re-throw as unchecked exception
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }


    public ZoneResponse searchZones(List<SearchCriteria> params, Pageable pageable) {
        String c = "ZoneService";
        String m = "searchZones";

        try {
            logger.info(createLogMessage(c, m, "Start"));

            if (params == null || params.isEmpty()) {
                return getZones(pageable);
            }
            Specification<Zone> spec = Specification.where(createSpecification(params.getFirst()));

            for (int i = 1; i < params.size(); i++) {
                spec = spec.and(createSpecification(params.get(i)));
            }

            Page<Zone> zones = zoneRepo.findAll(spec, pageable);
            List<ZoneDTO> zoneDTOList = zones.stream()
                    .map(zoneMapper::getZoneDTO)
                    .toList();

            return new ZoneResponse(zoneDTOList, zones.getTotalElements());

        } catch (Exception e){
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    private Specification<Zone> createSpecification(SearchCriteria criteria) {
        Specification<Zone> specification = new SimpleSpecification<>(criteria);
        return new ZoneDecorator(specification);
    }
}
