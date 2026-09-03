package com.danalistiq.potionlooker;

import com.danalistiq.potionlooker.config.PotionLookerConfig;
import com.danalistiq.potionlooker.config.PotionLookerConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
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

        configKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.potionlooker.open_config",
                GLFW.GLFW_KEY_O,
                new KeyMapping.Category(
                        Identifier.fromNamespaceAndPath("potionlooker", "category"))));

        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CROSSHAIR,
                Identifier.fromNamespaceAndPath("potionlooker", "hud"),
                this::renderHud);
    }

  private void renderHud(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
    Minecraft mc = Minecraft.getInstance();

    while (configKey.consumeClick()) {
        mc.gui.setScreen(PotionLookerConfigScreen.create(null));
    }

    if (!CONFIG.enabled || mc.player == null) return;

    if (mc.hitResult instanceof net.minecraft.world.phys.EntityHitResult hit
            && hit.getEntity() instanceof Player target
            && target != mc.player
            && mc.player.distanceTo(target) <= CONFIG.maxDistance) {

        System.out.println("POTIONLOOKER TARGET: " + target.getName().getString());

        PotionLookerHud.render(graphics, target, CONFIG);
    }
}
