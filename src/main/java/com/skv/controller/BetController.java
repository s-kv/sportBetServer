package com.skv.controller;

import com.skv.domain.Bet;
import com.skv.persistance.BetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("bets")
public class BetController {
    public static final Logger logger = LoggerFactory.getLogger(BetController.class);
    @Autowired
    private BetRepository betRepository;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public Bet addBet(@RequestBody Bet newBet) {
        return betRepository.save(newBet);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Bet updateBet(@PathVariable Long id, @RequestBody Bet updBet) {
        return betRepository.save(updBet);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Bet getBet(@PathVariable Long id) {
        return betRepository.findOne(id);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBet(@PathVariable Long id) {
        betRepository.delete(id);
    }

    @CrossOrigin
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public List<Bet> getBetByUser(@PathVariable Long id) {
        return betRepository.findAll().stream().filter(x -> x.getUser().getId() == id).collect(Collectors.toList());
    }
}
