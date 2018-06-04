package com.skv.util;

import com.skv.domain.Bet;
import com.skv.domain.Game;
import com.skv.util.PointsCalculator;
import org.springframework.stereotype.Component;

@Component
public class RegularPointsCalculator implements PointsCalculator {
    @Override
    public Integer calculate(Bet bet) {
        int result = 0;

        if (bet != null && bet.getGame() != null) {
            Game game = bet.getGame();

            if (bet.getScore1() != null && bet.getScore2() != null && game.getScore1() != null && game.getScore2() != null) {
                if (bet.getScore1().equals(game.getScore1()) && bet.getScore2().equals(game.getScore2()))
                    result = 3;
                else if (bet.getScore1() - bet.getScore2() == game.getScore1() - game.getScore2())
                    result = 2;
                else if (bet.getScore1() > bet.getScore2() && game.getScore1() > game.getScore2() ||
                         bet.getScore1() < bet.getScore2() && game.getScore1() < game.getScore2()
                        )
                    result = 1;
            }
        }

        return result;
    }
}
