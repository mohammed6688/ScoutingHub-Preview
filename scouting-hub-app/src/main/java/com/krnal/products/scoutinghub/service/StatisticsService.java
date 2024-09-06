package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.PlayerMatchReportRepo;
import com.krnal.products.scoutinghub.dao.PlayerRepo;
import com.krnal.products.scoutinghub.dao.RatingRepo;
import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.enums.FactorRatingEnum;
import com.krnal.products.scoutinghub.enums.PlayerSuitabilityEnum;
import com.krnal.products.scoutinghub.mapper.PlayerMapper;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.PlayerMatchReport;
import com.krnal.products.scoutinghub.model.Rating;
import com.krnal.products.scoutinghub.types.PlayerComparator;
import com.krnal.products.scoutinghub.types.PlayerComparatorResponse;
import com.krnal.products.scoutinghub.types.FactorEfficiency;
import com.krnal.products.scoutinghub.types.PlayerSuitability;
import com.krnal.products.scoutinghub.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

import static com.krnal.products.scoutinghub.constants.Constant.MIN_FACTOR_RATING_REPORTS_COUNT;
import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@Service
public class StatisticsService {
    Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    RatingRepo ratingRepo;
    @Autowired
    PlayerMatchReportRepo playerMatchReportRepo;
    @Autowired
    PlayerRepo playerRepo;
    @Autowired
    PlayerMapper playerMapper;

    public PlayerComparatorResponse getAllPlayersFactorsEfficiency() {
        String c = "StatisticsService";
        String m = "getAllPlayersFactorsEfficiency";
        try {
            logger.info(createLogMessage(c, m, "Start"));

            List<PlayerDTO> players = playerMapper.getPlayerDTOs(playerRepo.findAll());
            PlayerComparatorResponse playerComparatorResponse = getPlayersFactorsEfficiency(players);

            logger.info(createLogMessage(c, m, "Success"));
            return playerComparatorResponse;
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public PlayerComparatorResponse getPlayersFactorsEfficiency(List<PlayerDTO> players) {
        String c = "StatisticsService";
        String m = "getPlayersFactorsEfficiency";
        try {
            logger.info(createLogMessage(c, m, "Start"));

            List<PlayerComparator> playerComparators = new ArrayList<>();

            for (PlayerDTO player : players) {
                List<FactorEfficiency> factorEfficiencyList = getFactorsEfficiency(player.getId());
                if (factorEfficiencyList != null) {

                    PlayerComparator playerComparator = new PlayerComparator(player);

                    for (FactorEfficiency factorEfficiency : factorEfficiencyList) {
                        String factorName = Utilities.convertToCamelCase(factorEfficiency.getFactorName());

                        // Use reflection to set the correct field
                        try {
                            Field field = PlayerComparator.class.getDeclaredField(factorName);
                            field.setAccessible(true);
                            field.set(playerComparator, factorEfficiency);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            logger.error(createLogMessage(c, m, "Reflection error", "message", e.getMessage()));
                        }
                    }

                    playerComparators.add(playerComparator);
                }
            }

            logger.info(createLogMessage(c, m, "Success"));
            return new PlayerComparatorResponse(playerComparators, playerComparators.size());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public List<FactorEfficiency> getFactorsEfficiency(Integer playerId) {
        String c = "StatisticsService";
        String m = "getFactorsEfficiency";
        try {
            logger.info(createLogMessage(c, m, "Start"));

            List<Rating> playerFactorsRating = getPlayerFactorsRating(playerId);
            List<FactorEfficiency> factorEfficiencyList = aggregateRatingsByFactor(playerFactorsRating);

            logger.info(createLogMessage(c, m, "Success"));
            return factorEfficiencyList;
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public PlayerSuitability getPlayerSuitability(Integer playerId) {
        String c = "StatisticsService";
        String m = "getPlayerSuitability";
        try {
            logger.info(createLogMessage(c, m, "Start"));

            List<PlayerMatchReport> playerMatchReportList = getPlayerMatchReports(playerId);
            PlayerSuitability playerSuitability = aggregateFinalRating(playerMatchReportList);

            logger.info(createLogMessage(c, m, "Success"));
            return playerSuitability;
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    private List<PlayerMatchReport> getPlayerMatchReports(Integer playerId) {
        return playerMatchReportRepo.findAllByPlayerId(playerId);
    }

    private PlayerSuitability aggregateFinalRating(List<PlayerMatchReport> playerMatchReportList) {
        PlayerSuitability playerSuitability = new PlayerSuitability();
        playerSuitability.setReportsCount(playerMatchReportList.size());

        int suitableCount = 0;
        int nonSuitableCount = 0;

        for (PlayerMatchReport report : playerMatchReportList) {
            if (Objects.equals(report.getFinalRating(), PlayerSuitabilityEnum.Suitable.getValue())) { // suitable
                suitableCount++;
            } else if (Objects.equals(report.getFinalRating(), PlayerSuitabilityEnum.NonSuitable.getValue())) { // non suitable
                nonSuitableCount++;
            }
        }

        playerSuitability.setSuitableCount(suitableCount);
        playerSuitability.setNonSuitableCount(nonSuitableCount);

        int totalReports = playerSuitability.getReportsCount();
        if (totalReports > 0) {
            playerSuitability.setSuitability((suitableCount * 100) / totalReports);
        } else {
            playerSuitability.setSuitability(0);
        }

        return playerSuitability;
    }


    private List<Rating> getPlayerFactorsRating(Integer playerId) {
        return ratingRepo.findRatingsByPlayerId(playerId);
    }

    private List<FactorEfficiency> aggregateRatingsByFactor(List<Rating> playerFactorsRating) {
        Map<String, FactorEfficiency> factorEfficiencyMap = new HashMap<>();

        for (Rating rating : playerFactorsRating) {
            String factorName = rating.getFactor().getName();
            FactorEfficiency factorEfficiency = factorEfficiencyMap.getOrDefault(factorName, new FactorEfficiency());

            if (factorEfficiency.getFactorName() == null) {
                factorEfficiency.setFactorName(factorName);
                factorEfficiency.setReportsCount(0);
                factorEfficiency.setSkilledCount(0);
                factorEfficiency.setAverageCount(0);
                factorEfficiency.setDeficitCount(0);
            }

            factorEfficiency.setReportsCount(factorEfficiency.getReportsCount() + 1);


            Integer ratingValue = rating.getValue();
            if (ratingValue.equals(FactorRatingEnum.Skilled.getRatingValue())) {
                factorEfficiency.setSkilledCount(factorEfficiency.getSkilledCount() + 1);
            } else if (ratingValue.equals(FactorRatingEnum.Average.getRatingValue())) {
                factorEfficiency.setAverageCount(factorEfficiency.getAverageCount() + 1);
            } else if (ratingValue.equals(FactorRatingEnum.Deficit.getRatingValue())) {
                factorEfficiency.setDeficitCount(factorEfficiency.getDeficitCount() + 1);
            }

            factorEfficiencyMap.put(factorName, factorEfficiency);
        }

        List<FactorEfficiency> factorEfficiencies = new ArrayList<>();
        for (FactorEfficiency efficiency : factorEfficiencyMap.values()) {
            if (efficiency.getReportsCount() >= MIN_FACTOR_RATING_REPORTS_COUNT) {
                int totalValue = (efficiency.getSkilledCount() * 100) + (efficiency.getAverageCount() * 40) + (efficiency.getDeficitCount() * 10);
                int maxPossibleValue = efficiency.getReportsCount() * 100;
                efficiency.setFactorRating((totalValue * 100) / maxPossibleValue);
                factorEfficiencies.add(efficiency);
            }
        }

        return factorEfficiencies;
    }

    public PlayerComparatorResponse sortPayload(PlayerComparatorResponse playerComparator, String sortDir, String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return playerComparator;
        }
        Comparator<PlayerComparator> comparator = (p1, p2) -> {
            try {
                // Get the FactorEfficiency object from each PlayerComparator
                String sortField = Utilities.convertToCamelCase(sortBy);
                Field factorField = PlayerComparator.class.getDeclaredField(sortField);
                factorField.setAccessible(true);

                FactorEfficiency factor1 = (FactorEfficiency) factorField.get(p1);
                FactorEfficiency factor2 = (FactorEfficiency) factorField.get(p2);

                // Now compare the factorRating within FactorEfficiency
                Integer rating1 = factor1 != null ? factor1.getFactorRating() : null;
                Integer rating2 = factor2 != null ? factor2.getFactorRating() : null;

                // Handle null values for ratings (optional, depending on your needs)
                if (rating1 == null && rating2 == null) return 0;
                if (rating1 == null) return "asc".equalsIgnoreCase(sortDir) ? -1 : 1;
                if (rating2 == null) return "asc".equalsIgnoreCase(sortDir) ? 1 : -1;

                int comparison = rating1.compareTo(rating2);
                return "desc".equalsIgnoreCase(sortDir) ? -comparison : comparison;

            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Sorting Rating Is Not Valid ", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error during sorting", e);
            }
        };

        playerComparator.playerComparator().sort(comparator);

        return new PlayerComparatorResponse(playerComparator.playerComparator(), playerComparator.totalItems());
    }
}
