package com.krnal.products.scoutinghub.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerComparator {
    private PlayerDTO player;

    private FactorEfficiency scoringGoals;
    private FactorEfficiency linkUpPlay;
    private FactorEfficiency holdUpPlay;
    private FactorEfficiency runsInBehind;
    private FactorEfficiency movementInsideTheBox;
    private FactorEfficiency anticipation;
    private FactorEfficiency composureInsideTheBox;
    private FactorEfficiency heading;
    private FactorEfficiency pressing;
    private FactorEfficiency receivingSkills;
    private FactorEfficiency cross;
    private FactorEfficiency oneVOne;
    private FactorEfficiency shooting;
    private FactorEfficiency forwardRuns;
    private FactorEfficiency speed;
    private FactorEfficiency goalScoringCreating;
    private FactorEfficiency gettingInTheBox;
    private FactorEfficiency twistAndTurn;
    private FactorEfficiency passesInBehind;
    private FactorEfficiency dribblingOneVOne;
    private FactorEfficiency passingCombinations;
    private FactorEfficiency shieldingTheBall;
    private FactorEfficiency scanning;
    private FactorEfficiency athleticism;
    private FactorEfficiency willingnessToRun;
    private FactorEfficiency gegenpressing;
    private FactorEfficiency fallingBack;
    private FactorEfficiency ballWinningQualities;
    private FactorEfficiency willingnessToGetOnIt;
    private FactorEfficiency supportVsStayBetweenTheLine;
    private FactorEfficiency firstTouch;
    private FactorEfficiency bodyShape;
    private FactorEfficiency switchOfPlay;
    private FactorEfficiency secondBall;
    private FactorEfficiency composedOnTheBall;
    private FactorEfficiency canSeePasses;
    private FactorEfficiency usingBothFeet;
    private FactorEfficiency ballRetention;
    private FactorEfficiency defendingOnFrontFoot;
    private FactorEfficiency oneVOneDefInterception;
    private FactorEfficiency headingTheBall;
    private FactorEfficiency notAfraidToPushHigh;
    private FactorEfficiency passingRange;
    private FactorEfficiency crossing;
    private FactorEfficiency oneVOneInterception;
    private FactorEfficiency highLineDefending;
    private FactorEfficiency restDefending;
    private FactorEfficiency receivingWithBackToGoal;
    private FactorEfficiency passBetweenTheLine;
    private FactorEfficiency receivingInTightSpaces;
    private FactorEfficiency goalsScoringAbilityGetInBox;


    public PlayerComparator(PlayerDTO player) {
        this.player = player;
    }
}
