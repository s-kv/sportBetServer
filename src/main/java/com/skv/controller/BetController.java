package com.skv.controller;

import com.skv.domain.Bet;
import com.skv.persistance.BetRepository;
import com.skv.util.CustomErrorType;
import com.skv.util.PointsCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("bets")
public class BetController {
    public static final Logger logger = LoggerFactory.getLogger(BetController.class);
    @Autowired
    private BetRepository betRepository;
    @Autowired
    private PointsCalculator simplePointsCalculator;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public Bet addBet(@RequestBody Bet newBet) throws CustomErrorType {
        checkBet(newBet);
        return betRepository.save(newBet);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Bet updateBet(@PathVariable Long id, @RequestBody Bet updBet) throws CustomErrorType {
        checkBet(updBet);
        return betRepository.save(updBet);
    }

    private void checkBet(Bet updBet) throws CustomErrorType {
        if (LocalDateTime.now().isAfter(updBet.getGame().getStartDateTime()))
            throw new CustomErrorType("Запрещено редактировать или устанавливать ставку после начала матча!");
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
        return betRepository.findAll().stream()
                .filter(x -> x.getUser().getId() == id)
                .map(x -> { x.setPoints(simplePointsCalculator.calculate(x)); return x; })
                .collect(Collectors.toList());
    }
}
