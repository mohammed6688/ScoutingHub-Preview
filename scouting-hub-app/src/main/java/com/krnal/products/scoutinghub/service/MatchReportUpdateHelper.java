package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dto.MatchReportDTO;
import com.krnal.products.scoutinghub.dto.PlayerMatchReportDTO;
import com.krnal.products.scoutinghub.dto.RatingDTO;
import com.krnal.products.scoutinghub.mapper.PlayerMatchReportMapper;
import com.krnal.products.scoutinghub.mapper.RatingMapper;
import com.krnal.products.scoutinghub.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.krnal.products.scoutinghub.constants.Constant.PLAYER_MATCH_REPORT_ID_NOT_EXIST;
import static com.krnal.products.scoutinghub.constants.Constant.RATING_ID_NOT_EXIST;

@Component
public class MatchReportUpdateHelper implements UpdateHelper<MatchReport, MatchReportDTO> {

    @Autowired
    PlayerMatchReportMapper playerMatchReportMapper;
    @Autowired
    RatingMapper ratingMapper;

    @Override
    public void set(MatchReport matchReport, MatchReportDTO matchReportDTO) {
        if (matchReportDTO.getFirstTeam() != null && matchReportDTO.getFirstTeam().getId() != null) {
            matchReport.setFirstTeam(new Team(matchReportDTO.getFirstTeam().getId()));
        }

        if (matchReportDTO.getSecondTeam() != null && matchReportDTO.getSecondTeam().getId() != null) {
            matchReport.setSecondTeam(new Team(matchReportDTO.getSecondTeam().getId()));
        }

        if (matchReportDTO.getFirstTeamScore() != null) {
            matchReport.setFirstTeamScore(matchReportDTO.getFirstTeamScore());
        }

        if (matchReportDTO.getSecondTeamScore() != null) {
            matchReport.setSecondTeamScore(matchReportDTO.getSecondTeamScore());
        }

        if (matchReportDTO.getMatchDate() != null) {
            matchReport.setMatchDate(matchReportDTO.getMatchDate());
        }

        if (matchReportDTO.getSeason() != null) {
            matchReport.setSeason(matchReportDTO.getSeason());
        }

        if (matchReportDTO.getGeneralNotes() != null) {
            matchReport.setGeneralNotes(matchReportDTO.getGeneralNotes());
        }

        if (matchReportDTO.getUpdaterId() != null) {
            matchReport.setUpdaterId(matchReportDTO.getUpdaterId());
        }

        if (matchReportDTO.getPlayerMatchReportDTOList() != null) {
            deleteMissingEntities(matchReport.getPlayerMatchReportList(), matchReportDTO.getPlayerMatchReportDTOList().stream().map(PlayerMatchReportDTO::getId).filter(Objects::nonNull).collect(Collectors.toSet()));
            updatePlayerMatchReports(matchReportDTO.getPlayerMatchReportDTOList(), matchReport);
        }
    }


    private void updatePlayerMatchReports(List<PlayerMatchReportDTO> playerMatchReportDTOList, MatchReport matchReport) {
        for (PlayerMatchReportDTO playerMatchReportDTO : playerMatchReportDTOList) {
            if (playerMatchReportDTO.getId() != null) { // update operation
                Optional<PlayerMatchReport> playerMatchReportOpt = matchReport.getPlayerMatchReportList().stream()
                        .filter(report -> report.getId().equals(playerMatchReportDTO.getId()))
                        .findFirst();

                if (playerMatchReportOpt.isPresent()) {
                    updatePlayerMatchReport(playerMatchReportOpt.get(), playerMatchReportDTO);
                } else {
                    throw new IllegalArgumentException(PLAYER_MATCH_REPORT_ID_NOT_EXIST);
                }

                if (playerMatchReportDTO.getRatingDTOList() != null) { // update ratings
                    deleteMissingEntities(playerMatchReportOpt.get().getRatingsList(), playerMatchReportDTO.getRatingDTOList().stream().map(RatingDTO::getId).filter(Objects::nonNull).collect(Collectors.toSet()));
                    updateRatings(playerMatchReportOpt.get(), playerMatchReportDTO.getRatingDTOList(), playerMatchReportDTO.getId());
                }
            } else { // add operation
                savePlayerMatchReport(playerMatchReportDTO, matchReport);
            }
        }
    }


    private void updatePlayerMatchReport(PlayerMatchReport playerMatchReport, PlayerMatchReportDTO playerMatchReportDTO) {
        if (playerMatchReportDTO.getPlayerDTO() != null) {
            playerMatchReport.setPlayer(new Player(playerMatchReportDTO.getPlayerDTO().getId()));
        }

        if (playerMatchReportDTO.getTeamDTO() != null) {
            playerMatchReport.setTeam(new Team(playerMatchReportDTO.getTeamDTO().getId()));
        }

        if (playerMatchReportDTO.getRatingDTOList() != null) {
            setRatings(playerMatchReport.getRatingsList(), playerMatchReportDTO.getRatingDTOList());
        }

        if (playerMatchReportDTO.getPositionDTO() != null) {
            playerMatchReport.setPosition(new Position(playerMatchReportDTO.getPositionDTO().getId()));
        }

        if (playerMatchReportDTO.getComments() != null) {
            playerMatchReport.setComments(playerMatchReportDTO.getComments());
        }

        if (playerMatchReportDTO.getFinalRating() != null) {
            playerMatchReport.setFinalRating(playerMatchReportDTO.getFinalRating());
        }
    }

    private void setRatings(List<Rating> ratingList, List<RatingDTO> ratingDTOList) {
        for (RatingDTO ratingDTO : ratingDTOList) {
            Optional<Rating> ratingOpt = ratingList.stream()
                    .filter(rate -> rate.getId().equals(ratingDTO.getId()))
                    .findAny();

            if (ratingOpt.isPresent()) {
                Rating rating = ratingOpt.get();
                setRating(rating, ratingDTO);
            }
        }
    }

    private void setRating(Rating rating, RatingDTO ratingDTO) {
        rating.setId(ratingDTO.getId());
        rating.setValue(ratingDTO.getValue());
        rating.setFactor(new Factor(ratingDTO.getFactor().getId()));
    }

    private void updateRatings(PlayerMatchReport playerMatchReport, List<RatingDTO> ratingDTOList, Integer playerMatchReportId) {
        for (RatingDTO ratingDTO : ratingDTOList) {
            if (ratingDTO.getId() != null) { // update operation
                Optional<Rating> ratingOptional = playerMatchReport.getRatingsList().stream()
                        .filter(report -> report.getId().equals(ratingDTO.getId()))
                        .findFirst();

                if (ratingOptional.isPresent()) {
                    setRating(ratingOptional.get(), ratingDTO);
                } else {
                    throw new IllegalArgumentException(RATING_ID_NOT_EXIST);
                }
            } else { // add operation
                ratingDTO.setPlayerMatchReport(new PlayerMatchReportDTO(playerMatchReportId));
                if (playerMatchReport.getRatingsList() != null) {
                    playerMatchReport.getRatingsList().add(ratingMapper.getRating(ratingDTO));
                } else {
                    List<Rating> ratingList = new ArrayList<>();
                    ratingList.add(ratingMapper.getRating(ratingDTO));
                    playerMatchReport.setRatingsList(ratingList);
                }
            }
        }
    }

    private void savePlayerMatchReport(PlayerMatchReportDTO playerMatchReportDTO, MatchReport matchReport) {
        PlayerMatchReport playerMatchReport = playerMatchReportMapper.getPlayerMatchReport(playerMatchReportDTO);
        playerMatchReport.setMatchReport(matchReport);
        List<PlayerMatchReport> playerMatchReportList = matchReport.getPlayerMatchReportList();

        if (playerMatchReportList != null) {
            matchReport.getPlayerMatchReportList().add(playerMatchReport);
        } else {
            List<PlayerMatchReport> playerMatchReports = new ArrayList<>();
            playerMatchReports.add(playerMatchReport);
            matchReport.setPlayerMatchReportList(playerMatchReports);
        }
        saveRatings(playerMatchReport);
    }

    public void saveRatings(PlayerMatchReport playerMatchReport) {
        if (playerMatchReport.getRatingsList() == null) {
            return;
        }
        for (Rating rating : playerMatchReport.getRatingsList()) {
            rating.setPlayerMatchReport(playerMatchReport);
        }
    }

    private <T> void deleteMissingEntities(List<T> entityList, Set<Integer> dtoIds) {
        List<T> entitiesToDelete = entityList.stream()
                .filter(entity -> !dtoIds.contains(getEntityId(entity)))
                .toList();

        entityList.removeAll(entitiesToDelete);
    }


    private Integer getEntityId(Object entity) {
        if (entity instanceof PlayerMatchReport) {
            return ((PlayerMatchReport) entity).getId();
        } else if (entity instanceof Rating) {
            return ((Rating) entity).getId();
        } else if (entity instanceof ShadowListPlayer) {
            return ((ShadowListPlayer) entity).getId();
        } else if (entity instanceof ShadowList) {
            return ((ShadowList) entity).getId();
        }
        return null;
    }
}
