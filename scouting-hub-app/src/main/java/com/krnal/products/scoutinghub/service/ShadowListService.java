package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.ShadowListRepo;
import com.krnal.products.scoutinghub.dto.*;
import com.krnal.products.scoutinghub.mapper.ShadowListMapper;
import com.krnal.products.scoutinghub.model.ShadowList;
import com.krnal.products.scoutinghub.model.ShadowListPlayer;
import com.krnal.products.scoutinghub.specification.ShadowListSpecification;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import com.krnal.products.scoutinghub.types.ShadowListResponse;
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
import java.util.stream.Collectors;

import static com.krnal.products.scoutinghub.constants.Constant.*;
import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@Service
public class ShadowListService {
    Logger logger = LoggerFactory.getLogger(ShadowListService.class);

    @Autowired
    ShadowListRepo shadowListRepo;
    @Autowired
    ShadowListMapper shadowListMapper;
    @Autowired
    ShadowListUpdateHelper shadowListUpdateHelper;

    public ShadowListResponse getShadowLists() {
        String c = "ShadowListService";
        String m = "getShadowLists";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<ShadowList> shadowLists = shadowListRepo.findAll();
            List<ShadowListDTO> shadowListDTOList = shadowLists.stream()
                    .map(shadowList -> shadowListMapper.getShadowListDto(shadowList))
                    .collect(Collectors.toList());
            logger.info(createLogMessage(c, m, "Success"));
            return new ShadowListResponse(shadowListDTOList, shadowLists.size());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public ShadowListResponse getShadowLists(Pageable pageable) {
        String c = "ShadowListService";
        String m = "getShadowLists";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Page<ShadowList> shadowLists = shadowListRepo.findAll(pageable);
            List<ShadowListDTO> shadowListDTOList = shadowLists.stream()
                    .map(shadowList -> shadowListMapper.getShadowListDto(shadowList))
                    .collect(Collectors.toList());
            logger.info(createLogMessage(c, m, "Success"));
            return new ShadowListResponse(shadowListDTOList, shadowLists.getTotalElements());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public ShadowListDTO getShadowList(Integer id) {
        String c = "ShadowListService";
        String m = "getShadowList";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<ShadowList> shadowList = shadowListRepo.findById(id);
            if (shadowList.isPresent()) {
                ShadowListDTO shadowListDTO = shadowListMapper.getShadowListDto(shadowList.get());
                logger.info(createLogMessage(c, m, "Success"));
                return shadowListDTO;
            }
            throw new RuntimeException(SHADOW_LIST_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public ShadowListDTO addShadowList(ShadowListDTO shadowListDTO) {
        String c = "ShadowListService";
        String m = "addShadowList";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<ShadowList> shadowListOptional = shadowListRepo.findByName(shadowListDTO.getName());
            if (shadowListOptional.isEmpty()) {

                ShadowList shadowList = shadowListMapper.getShadowList(shadowListDTO);
                setShadowListPlayers(shadowList);
                shadowList = shadowListRepo.save(shadowList);

                logger.info(createLogMessage(c, m, "Success"));
                return shadowListMapper.getShadowListDto(shadowList);
            }
            throw new RuntimeException(SHADOW_LIST_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public ShadowListDTO updateShadowList(ShadowListDTO shadowListDTO) {
        String c = "ShadowListService";
        String m = "updateShadowList";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<ShadowList> shadowListOptional = shadowListRepo.findById(shadowListDTO.getId());
            if (shadowListOptional.isPresent()) {
                ShadowList shadowList = shadowListOptional.get();
                shadowListUpdateHelper.set(shadowList, shadowListDTO);
                shadowList = shadowListRepo.save(shadowList);
                logger.info(createLogMessage(c, m, "Success"));
                return shadowListMapper.getShadowListDto(shadowList);
            }
            throw new RuntimeException(SHADOW_LIST_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public void deleteShadowList(Integer id) {
        String c = "ShadowListService";
        String m = "deleteShadowList";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<ShadowList> shadowListOptional = shadowListRepo.findById(id);
            if (shadowListOptional.isPresent()) {
                shadowListRepo.deleteById(id);
                logger.info(createLogMessage(c, m, "Success"));
                return;
            }
            throw new RuntimeException(SHADOW_LIST_ID_NOT_EXIST);
        } catch (DataIntegrityViolationException e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(SHADOW_LIST_IS_REFERENCED, e); // Re-throw as unchecked exception
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    private void setShadowListPlayers(ShadowList shadowList) {
        List<ShadowListPlayer> shadowListPlayers = shadowList.getShadowListPlayerList();
        if (shadowListPlayers != null) {
            for (ShadowListPlayer shadowListPlayer : shadowListPlayers) {
                shadowListPlayer.setShadowList(shadowList);
            }
        }
    }


    public ShadowListResponse searchShadowLists(List<SearchCriteria> params, Pageable pageable) {
        String c = "ShadowListService";
        String m = "searchShadowLists";

        try {
            logger.info(createLogMessage(c, m, "Start"));

            if (params == null || params.isEmpty()) {
                return getShadowLists(pageable);
            }
            Specification<ShadowList> spec = Specification.where(createSpecification(params.getFirst()));

            for (int i = 1; i < params.size(); i++) {
                spec = spec.and(createSpecification(params.get(i)));
            }

            Page<ShadowList> shadowLists = shadowListRepo.findAll(spec, pageable);
                    List<ShadowListDTO> shadowListDTOList = shadowLists.stream()
                    .map(shadowList -> shadowListMapper.getShadowListDto(shadowList))
                    .toList();

            return new ShadowListResponse(shadowListDTOList, shadowLists.getTotalElements());

        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    private Specification<ShadowList> createSpecification(SearchCriteria criteria) {
        return new ShadowListSpecification(criteria);
    }
}