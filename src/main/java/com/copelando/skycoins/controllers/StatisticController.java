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
    public List<Statistic> getStatistics(@PathVariable String type) {
        return statisticRepository.findByTypeOrderByCreateDateAsc(type);
    }

    @GetMapping("/{type}/newest")
    public Statistic getNewestStatistic(@PathVariable String type) {
        return statisticRepository.findTopByTypeOrderByCreateDateDesc(type);
    }

    @GetMapping("/{type}/oldest")
    public Statistic getOldestStatistic(@PathVariable String type) {
        return statisticRepository.findTopByTypeOrderByCreateDateAsc(type);
    }
}
