package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.configs.Configs;
import com.krnal.products.scoutinghub.dao.FactorRepo;
import com.krnal.products.scoutinghub.dao.MatchReportRepo;
import com.krnal.products.scoutinghub.dto.*;
import com.krnal.products.scoutinghub.mapper.*;
import com.krnal.products.scoutinghub.model.*;
import com.krnal.products.scoutinghub.security.UserSessionHelper;
import com.krnal.products.scoutinghub.specification.MatchReportDecorator;
import com.krnal.products.scoutinghub.specification.SimpleSpecification;
import com.krnal.products.scoutinghub.types.MatchReportResponse;
import com.krnal.products.scoutinghub.types.SearchCriteria;
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
public class MatchReportService {
    private static final Logger logger = LoggerFactory.getLogger(MatchReportService.class);

    private final MatchReportRepo matchReportRepo;
    private final MatchReportMapper matchReportMapper;
    private final TeamMapper teamMapper;
    private final MatchReportUpdateHelper matchReportUpdateHelper;

    public MatchReportService(MatchReportRepo matchReportRepo, MatchReportMapper matchReportMapper, TeamMapper teamMapper, MatchReportUpdateHelper matchReportUpdateHelper) {
        this.matchReportRepo = matchReportRepo;
        this.matchReportMapper = matchReportMapper;
        this.teamMapper = teamMapper;
        this.matchReportUpdateHelper = matchReportUpdateHelper;
    }

    public MatchReportResponse getMatchReports() {
        String c = "MatchReportService";
        String m = "getMatchReports";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<MatchReport> matchReports = matchReportRepo.findAll();
            List<MatchReportDTO> matchReportDTOS = matchReports.stream()
                    .map(matchReportMapper::getMatchReportDTO)
                    .filter(matchReportDTO -> !(UserSessionHelper.checkUserAccess(Configs.SCOUTER_ROLE)) || matchReportDTO.getCreatorId().equals(UserSessionHelper.getUserId()))
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return new MatchReportResponse(matchReportDTOS, matchReports.size());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public MatchReportResponse getMatchReports(Pageable pageable) {
        String c = "MatchReportService";
        String m = "getMatchReports";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Page<MatchReport> matchReports = matchReportRepo.findAll(pageable);
            List<MatchReportDTO> matchReportDTOS = matchReports.stream()
                    .map(matchReportMapper::getMatchReportDTO)
                    .filter(matchReportDTO -> !(UserSessionHelper.checkUserAccess(Configs.SCOUTER_ROLE)) || matchReportDTO.getCreatorId().equals(UserSessionHelper.getUserId()))
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return new MatchReportResponse(matchReportDTOS, matchReports.getTotalElements());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public MatchReportDTO getMatchReport(Integer id) {
        String c = "MatchReportService";
        String m = "getMatchReport";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<MatchReport> matchReport = matchReportRepo.findById(id);
            if (matchReport.isPresent()) {
                MatchReportDTO matchReportDTO = matchReportMapper.getMatchReportDTO(matchReport.get());
                matchReportDTO.setFirstTeamPlayersDTOList(matchReportDTO.getPlayerMatchReportDTOList()
                        .stream().filter(playerMatchReportDTO ->
                                playerMatchReportDTO.getTeamDTO().getId().equals(matchReportDTO.getFirstTeam().getId())).toList());
                matchReportDTO.setSecondTeamPlayersDTOList(matchReportDTO.getPlayerMatchReportDTOList()
                        .stream().filter(playerMatchReportDTO ->
                                playerMatchReportDTO.getTeamDTO().getId().equals(matchReportDTO.getSecondTeam().getId())).toList());
                logger.info(createLogMessage(c, m, "Success"));
                return matchReportDTO;
            }
            throw new RuntimeException(MATCH_REPORT_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public MatchReportDTO addMatchReport(MatchReportDTO matchReportDTO) {
        String c = "MatchReportService";
        String m = "addMatchReport";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<MatchReport> matchReportOptional = matchReportRepo.findByFirstTeamAndSecondTeamAndMatchDate(
                    teamMapper.getTeam(matchReportDTO.getFirstTeam()),
                    teamMapper.getTeam(matchReportDTO.getSecondTeam()),
                    matchReportDTO.getMatchDate());
            if (matchReportOptional.isEmpty()) { // if provided match report is not exist

                MatchReport matchReport = matchReportMapper.getMatchReport(matchReportDTO);
                setPlayerMatchReports(matchReport);
                matchReport = matchReportRepo.save(matchReport);

                logger.info(createLogMessage(c, m, "Success"));
                return matchReportMapper.getMatchReportDTO(matchReport);
            }
            throw new RuntimeException(MATCH_REPORT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public MatchReportDTO updateMatchReport(MatchReportDTO matchReportDTO) {
        String c = "MatchReportService";
        String m = "updateMatchReport";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<MatchReport> matchReportOptional = matchReportRepo.findById(matchReportDTO.getId());
            if (matchReportOptional.isPresent()) { // if provided match report id is existed

                MatchReport matchReport = matchReportOptional.get();
                matchReportUpdateHelper.set(matchReport, matchReportDTO);
                matchReport = matchReportRepo.save(matchReport);

                logger.info(createLogMessage(c, m, "Success"));
                return matchReportMapper.getMatchReportDTO(matchReport);
            }
            throw new RuntimeException(MATCH_REPORT_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public void deleteMatchReport(Integer id) {
        String c = "MatchReportService";
        String m = "deleteMatchReport";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<MatchReport> matchReportOptional = matchReportRepo.findById(id);
            if (matchReportOptional.isPresent()) { // if provided player id is existed
                matchReportRepo.deleteById(id);
                logger.info(createLogMessage(c, m, "Success"));
                return;
            }
            throw new RuntimeException(MATCH_REPORT_ID_NOT_EXIST);
        } catch (DataIntegrityViolationException e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(MATCH_REPORT_IS_REFERENCED, e); // Re-throw as unchecked exception
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public MatchReportResponse searchMatchReports(List<SearchCriteria> params, Pageable pageable) {
        String c = "MatchReportService";
        String m = "searchMatchReports";

        try {
            logger.info(createLogMessage(c, m, "Start"));

            if (params == null || params.isEmpty()) {
                return getMatchReports(pageable);
            }
            Specification<MatchReport> spec = Specification.where(createSpecification(params.getFirst()));

            for (int i = 1; i < params.size(); i++) {
                spec = spec.and(createSpecification(params.get(i)));
            }

            Page<MatchReport> matchReports = matchReportRepo.findAll(spec, pageable);
                    List<MatchReportDTO> matchReportDTOList = matchReports.stream()
                    .map(matchReportMapper::getMatchReportDTO)
                    .filter(matchReportDTO -> !(UserSessionHelper.checkUserAccess(Configs.SCOUTER_ROLE)) || matchReportDTO.getCreatorId().equals(UserSessionHelper.getUserId()))
                    .toList();

            return new MatchReportResponse(matchReportDTOList, matchReports.getTotalElements());

        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    private Specification<MatchReport> createSpecification(SearchCriteria criteria) {
        Specification<MatchReport> specification = new SimpleSpecification<>(criteria);
        return new MatchReportDecorator(specification, criteria);
    }

    private void setPlayerMatchReports(MatchReport matchReport) {
        List<PlayerMatchReport> playerMatchReportList = matchReport.getPlayerMatchReportList();
        if (playerMatchReportList != null) {
            for (PlayerMatchReport playerMatchReport : playerMatchReportList) {
                playerMatchReport.setMatchReport(matchReport);
                setRatings(playerMatchReport);
            }
        }
    }

    private void setRatings(PlayerMatchReport playerMatchReport) {
        List<Rating> ratings = playerMatchReport.getRatingsList();
        if (ratings != null) {
            for (Rating rating : ratings) {
                rating.setPlayerMatchReport(playerMatchReport);
            }
        }
    }
}