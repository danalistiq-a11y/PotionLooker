package com.danalistiq.potionlooker.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class PotionLookerConfigScreen {
    public static Screen create(Screen parent) {
        PotionLookerConfig c = ConfigHolder.get();
        ConfigBuilder b = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Potion Looker"));
        ConfigEntryBuilder e = b.entryBuilder();
        var hud = b.getOrCreateCategory(Component.literal("HUD"));

        hud.addEntry(e.startBooleanToggle(Component.literal("Enable HUD"), c.enabled)
                .setSaveConsumer(v -> c.enabled = v).build());
        hud.addEntry(e.startIntField(Component.literal("Right margin"), c.rightMargin)
                .setMin(0).setMax(2000).setSaveConsumer(v -> c.rightMargin = v).build());
        hud.addEntry(e.startIntField(Component.literal("Top margin"), c.topMargin)
                .setMin(0).setMax(2000).setSaveConsumer(v -> c.topMargin = v).build());
        hud.addEntry(e.startIntField(Component.literal("Panel width"), c.panelWidth)
                .setMin(120).setMax(1000).setSaveConsumer(v -> c.panelWidth = v).build());
        hud.addEntry(e.startIntSlider(Component.literal("Background opacity"), c.backgroundAlpha, 0, 255)
                .setSaveConsumer(v -> c.backgroundAlpha = v).build());
        hud.addEntry(e.startBooleanToggle(Component.literal("Background"), c.background)
                .setSaveConsumer(v -> c.background = v).build());
        hud.addEntry(e.startBooleanToggle(Component.literal("Effect levels"), c.showLevel)
                .setSaveConsumer(v -> c.showLevel = v).build());
        hud.addEntry(e.startBooleanToggle(Component.literal("Duration"), c.showDuration)
                .setSaveConsumer(v -> c.showDuration = v).build());

        b.setSavingRunnable(c::save);
        return b.build();
    }
}
