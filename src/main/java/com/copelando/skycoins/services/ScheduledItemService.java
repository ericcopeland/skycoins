package com.copelando.skycoins.services;

import com.copelando.skycoins.models.Item;
import com.copelando.skycoins.repositories.ItemRepository;
import com.copelando.skycoins.services.responses.item.ItemResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@EnableScheduling
public class ScheduledItemService {
    private final WebClient hypixelApiClient;
    private final ItemRepository itemRepository;

    @Autowired
    public ScheduledItemService(
        WebClient hypixelApiClient,
        ItemRepository itemRepository
    ) {
        this.hypixelApiClient = hypixelApiClient;
        this.itemRepository = itemRepository;
    }

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void retrieveItems() {
        var response = hypixelApiClient.get()
            .uri("/resources/skyblock/items")
            .retrieve()
            .bodyToMono(ItemResponseWrapper.class)
            .blockOptional()
            .get();

        for (var itemResponse: response.items) {
            Item item = new Item();

            item.setId(itemResponse.id);
            item.setName(itemResponse.name);
            item.setMaterial(itemResponse.material);
            item.setCategory(itemResponse.category);
            item.setTier(itemResponse.tier);
            item.setNpcSellPrice(itemResponse.npc_sell_price);
            item.setSkin(itemResponse.skin);

            itemRepository.save(item);
        }
    }
}
