package com.danalistiq.potionlooker;

import com.danalistiq.potionlooker.config.PotionLookerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
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

        effects.sort(Comparator.comparing(
                effect -> effect.getEffect().value()
                        .getDisplayName()
                        .getString()
        ));

        if (c.maxEffects > 0 && effects.size() > c.maxEffects) {
            effects = effects.subList(0, c.maxEffects);
        }

        int row = 18;
        int pad = 6;
        int width = c.panelWidth;

        int height = pad * 2 + effects.size() * row;

        int x = g.guiWidth() - width - c.rightMargin;
        int y = c.topMargin;

        if (c.background) {
            int alpha = Math.max(
                    0,
                    Math.min(255, c.backgroundAlpha)
            );

            g.fill(
                    x,
                    y,
                    x + width,
                    y + height,
                    (alpha << 24) | 0x101010
            );
        }

        int yy = y + pad;

        for (MobEffectInstance effect : effects) {

            String name = effect.getEffect()
                    .value()
                    .getDisplayName()
                    .getString();

            if (c.showLevel) {
                name += " " + roman(effect.getAmplifier() + 1);
            }

            g.text(
                    mc.font,
                    Component.literal(name),
                    x + pad,
                    yy,
                    0xFFFFFFFF
            );

            if (c.showDuration) {
                String duration = format(effect.getDuration());

                g.text(
                        mc.font,
                        Component.literal(duration),
                        x + width - pad - mc.font.width(duration),
                        yy,
                        0xFFDDDDDD
                );
            }

            yy += row;
        }
    }

    private static String format(int ticks) {
        if (ticks < 0) {
            return "∞";
        }

        int seconds = ticks / 20;

        return (seconds / 60)
                + ":"
                + String.format("%02d", seconds % 60);
    }

    private static String roman(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> Integer.toString(number);
        };
    }
}
