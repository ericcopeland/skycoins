package com.copelando.skycoins.services.responses.product;

import java.util.Map;

public class ProductResponse {
    public boolean success;
    public long lastUpdated;
    public Map<String, ProductEntryResponse> products;
}
