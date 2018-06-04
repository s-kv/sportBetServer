package com.skv.util;

import com.skv.domain.Bet;

public interface PointsCalculator {
    Integer calculate(Bet bet);
}
