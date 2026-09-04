package com.danalistiq.potionlooker.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(
            method = "handleUpdateMobEffect",
            at = @At("HEAD")
    )
    private void potionLooker$effectPacket(
            ClientboundUpdateMobEffectPacket packet,
            CallbackInfo ci
    ) {
        System.out.println(
                "POTIONLOOKER EFFECT PACKET: entity="
                        + packet.getEntityId()
                        + " effect="
                        + packet.getEffect()
        );
    }

    @Inject(
            method = "handleCustomPayload",
            at = @At("HEAD")
    )
    private void potionLooker$customPacket(
            ClientboundCustomPayloadPacket packet,
            CallbackInfo ci
    ) {
        try {
            System.out.println(
                    "POTIONLOOKER CUSTOM PACKET: "
                            + packet.payload().type().id()
            );

            System.out.println(
                    "POTIONLOOKER CUSTOM DATA: "
                            + packet.payload()
            );
        } catch (Exception e) {
            System.out.println(
                    "POTIONLOOKER CUSTOM PACKET ERROR: "
                            + e
            );
        }
    }
}
