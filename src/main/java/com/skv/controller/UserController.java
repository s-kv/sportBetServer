package com.skv.controller;

import com.skv.domain.User;
import com.skv.persistance.UserRepository;
import com.skv.util.PointsCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(BetController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointsCalculator pointsCalculator;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public List<User> getBetByUser() {
        return userRepository.findAll().stream()
                .map(x -> {
                    x.setPoints(x.getBets().stream().mapToInt(y -> pointsCalculator.calculate(y)).sum());
                    return x;
                    })
                .sorted(Comparator.comparingInt(User::getPoints).reversed())
                .collect(Collectors.toList());
    }
}
