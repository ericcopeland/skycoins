package com.copelando.skycoins.services;

import com.copelando.skycoins.models.*;
import com.copelando.skycoins.repositories.AuctionRepository;
import com.copelando.skycoins.repositories.ItemRepository;
import com.copelando.skycoins.repositories.ProductEntryRepository;
import com.copelando.skycoins.repositories.ProductRepository;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@EnableScheduling
public class ProductEntryService {
    private final WebClient hypixelApiClient;
    private final ProductRepository productRepository;
    private final ProductEntryRepository productEntryRepository;
    private final AuctionRepository auctionRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ProductEntryService(
            WebClient hypixelApiClient,
            ProductRepository productRepository,
            ProductEntryRepository productEntryRepository,
            AuctionRepository auctionRepository,
            ItemRepository itemRepository
    ) {
        this.hypixelApiClient = hypixelApiClient;
        this.productRepository = productRepository;
        this.productEntryRepository = productEntryRepository;
        this.auctionRepository = auctionRepository;
        this.itemRepository = itemRepository;
    }

    @Scheduled(fixedRate = 1000 * 60 * 1)
    public void retrieveProductEntries() {
        var response = hypixelApiClient.get()
                .uri("/skyblock/bazaar")
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .blockOptional()
                .get();

        for (var productEntryResponse: response.products.values()) {
            var product = productRepository.findById(productEntryResponse.product_id)
                    .orElseGet(() -> {
                        var p = new Product();
                        p.setId(productEntryResponse.product_id);
                        p.setName(productEntryResponse.product_id);
                        return p;
                    });

//            var product = new Product();
//            product.setId(productEntryResponse.product_id);
//            product.setName(productEntryResponse.product_id);

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

            productRepository.save(product);
        }
    }

    private static class ProductResponse {
        public boolean success;
        public long lastUpdated;
        public Map<String, ProductEntryResponse> products;
    }

    private static class ProductEntryResponse {
        public String product_id;
        public QuickStatusResponse quick_status;
    }

    private static class QuickStatusResponse {
        public String productId;
        public double sellPrice;
        public long sellVolume;
        public long sellMovingWeek;
        public long sellOrders;
        public double buyPrice;
        public long buyVolume;
        public long buyMovingWeek;
        public long buyOrders;
    }

//    @Scheduled(fixedRate = 1000 * 10)
    public void retrieveAuctions() {
        var response = hypixelApiClient.get()
                .uri("/skyblock/auctions")
                .retrieve()
                .bodyToMono(AuctionResponseWrapper.class)
                .blockOptional()
                .get();

//        var auction = response.auctions.stream().findFirst().get();

        for (var auction: response.auctions.stream().limit(100).toList()) {
            try {
                Path filePath = Files.createTempFile(null, null);
                Files.write(
                        filePath,
                        Base64.getDecoder().decode(auction.item_bytes.getBytes(StandardCharsets.UTF_8)),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
                var tag = (CompoundTag) NBTUtil.read(filePath.toFile(), true).getTag();

//                System.out.println(tag);

//                System.out.println(tag);
//                System.out.println(tag.getListTag("i"));
//                System.out.println(tag.getListTag("i").asCompoundTagList());
//                System.out.println(tag.getListTag("i").asCompoundTagList().get(0));
                System.out.println(tag.getListTag("i").asCompoundTagList().get(0).getByte("Count"));
                System.out.println(tag.getListTag("i").asCompoundTagList().get(0).getShort("Damage"));
                System.out.println(tag.getListTag("i").asCompoundTagList().get(0).getShort("id"));
//                System.out.println(tag.getListTag("i").asCompoundTagList().get(0).getCompoundTag("tag"));
                System.out.println(tag.getListTag("i").asCompoundTagList().get(0).getCompoundTag("tag").getCompoundTag("display").getString("Name").replaceAll("§[0-9,a-z]", ""));
                System.out.println(tag.getListTag("i").asCompoundTagList().get(0).getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id"));
                System.out.println(tag.getListTag("i").asCompoundTagList().get(0).getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("timestamp"));

                List<String> lore = new ArrayList<>();
                tag.getListTag("i")
                        .asCompoundTagList()
                        .get(0)
                        .getCompoundTag("tag")
                        .getCompoundTag("display")
                        .getListTag("Lore")
                        .asStringTagList()
                        .forEach(stringTag -> {
                            lore.add(stringTag.getValue().replaceAll("§[0-9,a-z]", "") + "\n");
                        });
                System.out.println(String.join("", lore));
            } catch (Exception e) {
                System.out.println("error");
                System.out.println(e.getMessage());
            }
        }
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void retrieveRecentlyEndedAuctions() {
        var response = hypixelApiClient.get()
                .uri("/skyblock/auctions_ended")
                .retrieve()
                .bodyToMono(AuctionEndedResponseWrapper.class)
                .blockOptional()
                .get();

        List<Auction> auctions = new ArrayList<>();

        for (var auctionResponse: response.auctions) {
            Auction auction = new Auction();

            auction.setAuctionId(toUUID(auctionResponse.auction_id));
            auction.setSeller(toUUID(auctionResponse.seller));
            auction.setSellerProfile(toUUID(auctionResponse.seller_profile));
            auction.setBuyer(toUUID(auctionResponse.buyer));
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

//                System.out.println(tag);

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
            } catch (Exception ignored) {
                System.out.println(ignored.getMessage());
            }

            var item = itemRepository.findById(auction.getDecodedItemBytes().getItemId());
            auction.setItem(item.orElse(null));

            auctions.add(auction);
        }

        auctionRepository.saveAll(auctions);
    }

    private UUID toUUID(String uuid) {
        uuid = uuid.replaceAll("(.{8})(.{4})(.{4})(.{4})(.+)", "$1-$2-$3-$4-$5");
        return UUID.fromString(uuid);
    }

    public static class AuctionEndedResponseWrapper {
        public boolean success;
        public long lastUpdated;
        public List<AuctionEndedResponse> auctions;
    }

    public static class AuctionEndedResponse {
        public String auction_id;
        public String seller;
        public String seller_profile;
        public String buyer;
        public long timestamp;
        public long price;
        public boolean bin;
        public String item_bytes;
    }

    public static class AuctionResponseWrapper {
        public boolean success;
        public long lastUpdated;
        public List<AuctionResponse> auctions;
    }

    public static class AuctionResponse {
        public String auctionId;
        public String sellerId;
        public String sellerProfile;
        public String buyer;
        public long timestamp;
        public double price;
        public boolean bin;
        public String item_bytes;
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

    public static class ItemResponseWrapper {
        public boolean success;
        public long lastUpdated;
        public List<ItemResponse> items;
    }

    public static class ItemResponse {
        public String id;
        public String name;
        public String material;
        public String category;
        public String tier;
        public long npc_sell_price;
        public String skin;
    }
}
