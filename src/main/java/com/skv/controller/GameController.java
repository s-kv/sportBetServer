package com.skv.controller;

import com.skv.domain.Bet;
import com.skv.domain.Game;
import com.skv.domain.User;
import com.skv.persistance.BetRepository;
import com.skv.persistance.GameRepository;
import com.skv.persistance.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("games")
public class GameController {

    public static final Logger logger = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BetRepository betRepository;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public Game addGame(@RequestBody Game newGame) {
        return gameRepository.save(newGame);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Game updateGame(@PathVariable Long id, @RequestBody Game updGame) {
        Game game = gameRepository.getOne(id);
        game.setScore1(updGame.getScore1());
        game.setScore2(updGame.getScore2());
        return gameRepository.save(game);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Game getGame(@PathVariable Long id) {
        return gameRepository.findOne(id);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Transactional
    public void deleteGame(@PathVariable Long id) {
        Game game = gameRepository.getOne(id);

        List<User> users = game.getBets().stream().map(x -> x.getUser()).collect(Collectors.toList());
        users.forEach(x -> x.getBets().removeIf(y -> y.getGame().getId() == id));
        userRepository.save(users);

        game.getBets().clear();
        gameRepository.save(game);

        gameRepository.delete(game);
    }

    @CrossOrigin
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public List<Game> getGamesWithUserBet(@PathVariable Long id) {
        List<Bet> userBets = betRepository.findByUser(userRepository.findOne(id));

        return gameRepository.findAll().stream()
                .map(x -> {
                    x.setBet(userBets.stream()
                            .filter(y -> y.getGame().getId() == x.getId())
                            .mapToLong(y -> y.getId())
                            .findFirst().orElse(0L));
                    return x;
                })
                .sorted((x, y) -> {
                    LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+3"));
                    if (x.getStartDateTime().isBefore(now) && y.getStartDateTime().isAfter(now))
                        return 1;
                    else if (x.getStartDateTime().isAfter(now) && y.getStartDateTime().isBefore(now))
                        return -1;
                    else
                        return x.getStartDateTime().isBefore(y.getStartDateTime()) ? -1 : 1;
                })
                .collect(Collectors.toList());
    }
}
