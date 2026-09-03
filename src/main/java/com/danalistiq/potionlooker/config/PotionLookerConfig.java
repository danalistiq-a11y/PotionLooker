package com.danalistiq.potionlooker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.*;

public class PotionLookerConfig {
    public boolean enabled = true, background = true, showLevel = true, showDuration = true;
    public int rightMargin = 10, topMargin = 10, panelWidth = 260, backgroundAlpha = 160, maxEffects = 0;
    public double maxDistance = 32.0;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("potionlooker.json");

    public static PotionLookerConfig load() {
        try {
            if (Files.exists(FILE)) {
                try (Reader r = Files.newBufferedReader(FILE)) {
                    PotionLookerConfig c = GSON.fromJson(r, PotionLookerConfig.class);
                    if (c != null) return c;
                }
            }
        } catch (Exception ignored) {}
        PotionLookerConfig c = new PotionLookerConfig();
        c.save();
        return c;
    }

    public void save() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer w = Files.newBufferedWriter(FILE)) {
                GSON.toJson(this, w);
            }
        } catch (Exception ignored) {}
    }
}
