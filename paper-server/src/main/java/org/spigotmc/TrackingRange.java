package org.spigotmc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.item.ItemEntity;

public final class TrackingRange {

    private TrackingRange() {
    }

    /**
     * Gets the range an entity should be 'tracked' by players and visible in
     * the client.
     *
     * @param defaultRange Default range defined by Mojang
     */
    public static int getEntityTrackingRange(final Entity entity, final int defaultRange) {
        if (defaultRange == 0) {
            return defaultRange;
        }

        final SpigotWorldConfig config = entity.level().spigotConfig;
        if (entity instanceof ServerPlayer) {
            return config.playerTrackingRange;
        }

        if (entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon) {
            // Exempt ender dragon
            return ((ServerLevel) entity.level()).getChunkSource().chunkMap.serverViewDistance << 4;
        }

        switch (entity.activationType) {
            case RAIDER:
            case MONSTER:
            case FLYING_MONSTER:
                return config.monsterTrackingRange;
            case WATER:
            case VILLAGER:
            case ANIMAL:
                return config.animalTrackingRange;
            case MISC:
        }

        if (entity instanceof ItemFrame || entity instanceof Painting || entity instanceof ItemEntity || entity instanceof ExperienceOrb) {
            return config.miscTrackingRange;
        } else if (entity instanceof Display) {
            return config.displayTrackingRange;
        } else {
            return config.otherTrackingRange;
        }
    }
}
