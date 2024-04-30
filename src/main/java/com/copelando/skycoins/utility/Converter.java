package com.copelando.skycoins.utility;

import java.util.UUID;

public class Converter {
    public static UUID uuidFromString(String uuid) {
        uuid = uuid.replaceAll("(.{8})(.{4})(.{4})(.{4})(.+)", "$1-$2-$3-$4-$5");
        return UUID.fromString(uuid);
    }
}
