package com.copelando.skycoins.services;

import com.copelando.skycoins.models.*;
import com.copelando.skycoins.repositories.ItemRepository;
import com.copelando.skycoins.repositories.ProductEntryRepository;
import com.copelando.skycoins.repositories.ProductRepository;
import com.copelando.skycoins.repositories.StatisticRepository;
import com.copelando.skycoins.services.responses.product.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@EnableScheduling
public class ScheduledProductService {
    private final WebClient hypixelApiClient;
    private final ProductRepository productRepository;
    private final ProductEntryRepository productEntryRepository;
    private final ItemRepository itemRepository;
    private final StatisticRepository statisticRepository;

    @Autowired
    public ScheduledProductService(
            WebClient hypixelApiClient,
            ProductRepository productRepository,
            ProductEntryRepository productEntryRepository,
            ItemRepository itemRepository,
            StatisticRepository statisticRepository
    ) {
        this.hypixelApiClient = hypixelApiClient;
        this.productRepository = productRepository;
        this.productEntryRepository = productEntryRepository;
        this.itemRepository = itemRepository;
        this.statisticRepository = statisticRepository;
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void retrieveProductEntries() {
        var response = hypixelApiClient.get()
                .uri("/skyblock/bazaar")
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .blockOptional()
                .get();

        Statistic productBuyCap = new Statistic();
        productBuyCap.setType("product-buy-cap");

        Statistic productSellCap = new Statistic();
        productSellCap.setType("product-sell-cap");

        for (var productEntryResponse: response.products.values()) {
            var product = productRepository.findById(productEntryResponse.product_id)
                    .orElseGet(() -> {
                        var p = new Product();
                        p.setId(productEntryResponse.product_id);
                        p.setName(productEntryResponse.product_id);
                        return p;
                    });

            var productEntry = new ProductEntry();
            productEntry.setProduct(product);
            productEntry.setBuyPrice(productEntryResponse.quick_status.buyPrice);
            productEntry.setSellPrice(productEntryResponse.quick_status.sellPrice);
            productEntry.setBuyVolume(productEntryResponse.quick_status.buyVolume);
            productEntry.setSellVolume(productEntryResponse.quick_status.sellVolume);
            productEntry.setBuyOrders(productEntryResponse.quick_status.buyOrders);
            productEntry.setSellOrders(productEntryResponse.quick_status.sellOrders);
            productEntry.setBuyMovingWeek(productEntryResponse.quick_status.buyMovingWeek);
            productEntry.setSellMovingWeek(productEntryResponse.quick_status.sellMovingWeek);

            product = productRepository.save(product);
            productEntry = productEntryRepository.save(productEntry);

            if (product.getReferenceProductEntry() == null) {
                product.setReferenceProductEntry(productEntry);
            } else if (product.getReferenceProductEntry().getCreateDate().isBefore(Instant.now().minus(12, ChronoUnit.HOURS))) {
                product.setReferenceProductEntry(productEntry);
            }
            product.setLatestProductEntry(productEntry);

            var item = itemRepository.findById(product.getName());
            product.setItem(item.orElse(null));

            productBuyCap.setNumericValue(productBuyCap.getNumericValue() + productEntry.getBuyPrice() * productEntry.getBuyVolume());
            productSellCap.setNumericValue(productSellCap.getNumericValue() + productEntry.getSellPrice() * productEntry.getSellVolume());

            productRepository.save(product);
        }

        statisticRepository.save(productBuyCap);
        statisticRepository.save(productSellCap);
    }
}
