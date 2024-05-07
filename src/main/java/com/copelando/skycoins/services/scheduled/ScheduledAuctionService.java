package com.copelando.skycoins.services.scheduled;

import com.copelando.skycoins.models.Auction;
import com.copelando.skycoins.models.AuctionItemBytes;
import com.copelando.skycoins.models.Statistic;
import com.copelando.skycoins.repositories.AuctionRepository;
import com.copelando.skycoins.repositories.ItemRepository;
import com.copelando.skycoins.repositories.StatisticRepository;
import com.copelando.skycoins.responses.auction.AuctionEndedResponseWrapper;
import com.copelando.skycoins.utility.Converter;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@EnableScheduling
@ConditionalOnProperty("skycoins.service.scheduled.enabled")
public class ScheduledAuctionService {
    private final WebClient hypixelApiClient;
    private final AuctionRepository auctionRepository;
    private final ItemRepository itemRepository;
    private final StatisticRepository statisticRepository;

    @Autowired
    public ScheduledAuctionService(
        WebClient hypixelApiClient,
        AuctionRepository auctionRepository,
        ItemRepository itemRepository,
        StatisticRepository statisticRepository
    ) {
        this.hypixelApiClient = hypixelApiClient;
        this.auctionRepository = auctionRepository;
        this.itemRepository = itemRepository;
        this.statisticRepository = statisticRepository;
    }

    @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60)
    public void retrieveRecentlyEndedAuctions() {
        var response = hypixelApiClient.get()
            .uri("/skyblock/auctions_ended")
            .retrieve()
            .bodyToMono(AuctionEndedResponseWrapper.class)
            .blockOptional()
            .get();

        List<Auction> auctions = new ArrayList<>();

        Statistic auctionCount = new Statistic();
        auctionCount.setType("auction-count");
        auctionCount.setNumericValue(response.auctions.size());
        statisticRepository.save(auctionCount);

        for (var auctionResponse: response.auctions) {
            Auction auction = new Auction();

            auction.setAuctionId(Converter.uuidFromString(auctionResponse.auction_id));
            auction.setSeller(Converter.uuidFromString(auctionResponse.seller));
            auction.setSellerProfile(Converter.uuidFromString(auctionResponse.seller_profile));
            auction.setBuyer(Converter.uuidFromString(auctionResponse.buyer));
            auction.setTimestamp(Instant.ofEpochMilli(auctionResponse.timestamp));
            auction.setPrice(auctionResponse.price);
            auction.setBin(auctionResponse.bin);

            try {
                Path filePath = Files.createTempFile(null, null);
                Files.write(
                    filePath,
                    Base64.getDecoder().decode(auctionResponse.item_bytes.getBytes(StandardCharsets.UTF_8)),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                );
                var tag = (CompoundTag) NBTUtil.read(filePath.toFile(), true).getTag();

                var itemBytes = new AuctionItemBytes();
                itemBytes.setName(tag.getListTag("i").asCompoundTagList().get(0).getCompoundTag("tag").getCompoundTag("display").getString("Name").replaceAll("§[0-9,a-z]", ""));
                itemBytes.setItemId(tag.getListTag("i").asCompoundTagList().get(0).getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id"));
                itemBytes.setCount(tag.getListTag("i").asCompoundTagList().get(0).getByte("Count"));
                itemBytes.setDamage(tag.getListTag("i").asCompoundTagList().get(0).getShort("Damage"));

                List<String> lore = new ArrayList<>();
                tag.getListTag("i")
                    .asCompoundTagList()
                    .get(0)
                    .getCompoundTag("tag")
                    .getCompoundTag("display")
                    .getListTag("Lore")
                    .asStringTagList()
                    .forEach(stringTag -> {
                        lore.add(stringTag.getValue().replaceAll("(§k[a-z])|(§[0-9,a-z])", "").trim());
                    });

                itemBytes.setLore(String.join("\n", lore));
                itemBytes.setRarity(lore.get(lore.size() - 1).split(" ")[0]);

                Pattern pattern = Pattern.compile("(?<=\\[Lvl\\s)(.*?)(?=\\])");
                Matcher matcher = pattern.matcher(itemBytes.getName());
                if (matcher.find()) {
                    itemBytes.setPetLevel(Integer.parseInt(matcher.group()));
                }

                auction.setDecodedItemBytes(itemBytes);
            } catch (Exception ignored) { }

            var item = itemRepository.findById(auction.getDecodedItemBytes().getItemId());
            auction.setItem(item.orElse(null));

            auctions.add(auction);
        }

        auctionRepository.saveAll(auctions);
    }
}
