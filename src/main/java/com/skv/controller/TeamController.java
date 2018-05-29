package com.skv.controller;

import com.skv.domain.Team;
import com.skv.persistance.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("teams")
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public Team addTeam(@RequestBody Team newTeam) {
        return teamRepository.save(newTeam);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Team getTeam(@PathVariable Long id) {
        return teamRepository.findOne(id);
    }

    @CrossOrigin
    @RequestMapping(value = "/flags", method = RequestMethod.GET)
    public String[] getFlags() {
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:/static/images/*.png");
            return Arrays.stream(resources).map(x -> "/images/" + x.getFilename()).toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
