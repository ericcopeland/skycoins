package com.copelando.skycoins.controllers;

import com.copelando.skycoins.models.Statistic;
import com.copelando.skycoins.repositories.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticController {
    private final StatisticRepository statisticRepository;

    @Autowired
    public StatisticController(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @GetMapping("/{type}")
    public List<Statistic> getStatistic(@PathVariable String type) {
        return statisticRepository.findByType(type);
    }
}
