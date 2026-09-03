package com.danalistiq.potionlooker;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PotionLookerEffects {

    private static final Map<Integer, Map<Holder<MobEffect>, StoredEffect>> EFFECTS =
            new ConcurrentHashMap<>();

    private PotionLookerEffects() {
    }

    public static void update(ClientboundUpdateMobEffectPacket packet) {
        EFFECTS
                .computeIfAbsent(
                        packet.getEntityId(),
                        id -> new ConcurrentHashMap<>()
                )
                .put(
                        packet.getEffect(),
                        new StoredEffect(
                                packet.getEffect().value().getDisplayName().getString(),
                                packet.getEffectAmplifier(),
                                packet.getEffectDurationTicks(),
                                System.nanoTime()
                        )
                );
    }

    public static List<StoredEffect> get(int entityId) {
        Map<Holder<MobEffect>, StoredEffect> effects =
                EFFECTS.get(entityId);

        if (effects == null) {
            return List.of();
        }

        long now = System.nanoTime();

        effects.entrySet().removeIf(
                entry -> entry.getValue().isExpired(now)
        );

        return new ArrayList<>(effects.values());
    }

    public static final class StoredEffect {

        public final String name;
        public final int amplifier;
        public final int durationTicks;

        private final long receivedAtNanos;

        public StoredEffect(
                String name,
                int amplifier,
                int durationTicks,
                long receivedAtNanos
        ) {
            this.name = name;
            this.amplifier = amplifier;
            this.durationTicks = durationTicks;
            this.receivedAtNanos = receivedAtNanos;
        }

        public int getRemainingTicks() {
            if (durationTicks < 0) {
                return -1;
            }

            long elapsedTicks =
                    (System.nanoTime() - receivedAtNanos) / 50_000_000L;

            return Math.max(
                    0,
                    durationTicks - (int) elapsedTicks
            );
        }

        public boolean isExpired(long now) {
            if (durationTicks < 0) {
                return false;
            }

            long elapsedTicks =
                    (now - receivedAtNanos) / 50_000_000L;

            return elapsedTicks >= durationTicks;
        }
    }
}
