package com.skv.controller;

import com.skv.domain.Game;
import com.skv.persistance.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("games")
public class GameController {

    public static final Logger logger = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private GameRepository gameRepository;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public Game addGame(@RequestBody Game newGame) {
        return gameRepository.save(newGame);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Game updateGame(@PathVariable Long id, @RequestBody Game updGame) {
        return gameRepository.save(updGame);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Game getGame(@PathVariable Long id) {
        return gameRepository.findOne(id);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteGame(@PathVariable Long id) {
        gameRepository.delete(id);
    }


    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public List<Game> getGames() {
        return gameRepository.findAll();
    }
}
