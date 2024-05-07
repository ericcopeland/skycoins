package com.copelando.skycoins.services.scheduled;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@ConditionalOnProperty("skycoins.service.scheduled.enabled")
public class ScheduledStatisticService { }
