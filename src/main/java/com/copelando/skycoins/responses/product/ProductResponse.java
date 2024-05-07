package com.copelando.skycoins.responses.product;

import java.util.Map;

public class ProductResponse {
    public boolean success;
    public long lastUpdated;
    public Map<String, ProductEntryResponse> products;
}
