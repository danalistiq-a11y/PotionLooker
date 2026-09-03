package com.danalistiq.potionlooker;

import com.danalistiq.potionlooker.config.PotionLookerConfig;
import com.danalistiq.potionlooker.config.PotionLookerConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public final class PotionLookerClient implements ClientModInitializer {
    public static PotionLookerConfig CONFIG;
    private KeyMapping configKey;

    @Override
    public void onInitializeClient() {
        CONFIG = PotionLookerConfig.load();

        configKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.potionlooker.open_config",
                GLFW.GLFW_KEY_O,
                "category.potionlooker"));

        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CROSSHAIR,
                Identifier.fromNamespaceAndPath("potionlooker", "hud"),
                this::renderHud);
    }

    private void renderHud(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        while (configKey.consumeClick()) {
            mc.gui.setScreen(PotionLookerConfigScreen.create(mc.gui.getScreen()));
        }

        if (!CONFIG.enabled || mc.player == null) return;

        if (mc.hitResult instanceof net.minecraft.world.phys.EntityHitResult hit
                && hit.getEntity() instanceof Player target
                && target != mc.player
                && mc.player.distanceTo(target) <= CONFIG.maxDistance) {
            PotionLookerHud.render(graphics, target, CONFIG);
        }
    }
}
