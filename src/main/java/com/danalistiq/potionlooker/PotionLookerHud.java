package com.danalistiq.potionlooker;

import com.danalistiq.potionlooker.config.PotionLookerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class PotionLookerHud {

    private PotionLookerHud() {
    }

    public static void render(
            GuiGraphicsExtractor g,
            Player target,
            PotionLookerConfig c
    ) {
        Minecraft mc = Minecraft.getInstance();

        List<MobEffectInstance> effects =
                new ArrayList<>(target.getActiveEffects());

        int x = 20;
        int y = 50;

        // Testinformatie
        g.text(
                mc.font,
                Component.literal("Target: " + target.getName().getString()),
                x,
                y,
                0xFFFFFFFF
        );

        g.text(
                mc.font,
                Component.literal("Effects gevonden: " + effects.size()),
                x,
                y + 18,
                0xFFFFFFFF
        );

        int yy = y + 36;

        for (MobEffectInstance effect : effects) {

            String name = effect.getEffect()
                    .value()
                    .getDisplayName()
                    .getString();

            String text = name
                    + " "
                    + (effect.getAmplifier() + 1)
                    + " | "
                    + (effect.getDuration() / 20)
                    + "s";

            g.text(
                    mc.font,
                    Component.literal(text),
                    x,
                    yy,
                    0xFFFFFFFF
            );

            yy += 18;
        }
    }
}
