package com.danalistiq.potionlooker.mixin;
import com.danalistiq.potionlooker.PotionLookerEffects;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
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
        PotionLookerEffects.update(packet);
    }

    @Inject(
            method = "handleRemoveMobEffect",
            at = @At("HEAD")
    )
    private void potionLooker$removeEffectPacket(
            ClientboundRemoveMobEffectPacket packet,
            CallbackInfo ci
    ) {
        PotionLookerEffects.remove(packet);
    }
}
