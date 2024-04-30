package com.copelando.skycoins.repositories;

import com.copelando.skycoins.models.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, UUID> {
    List<Statistic> findByType(String type);
}
