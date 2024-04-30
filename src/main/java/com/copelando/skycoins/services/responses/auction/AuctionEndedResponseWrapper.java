package com.copelando.skycoins.services.responses.auction;

import java.util.List;

public class AuctionEndedResponseWrapper {
    public boolean success;
    public long lastUpdated;
    public List<AuctionEndedResponse> auctions;
}
