package com.danalistiq.potionlooker.config;
import com.danalistiq.potionlooker.PotionLookerClient;
public final class ConfigHolder {
    private ConfigHolder() {}
    public static PotionLookerConfig get() { return PotionLookerClient.CONFIG; }
}
