package org.spigotmc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.painting.Painting;
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

        final io.papermc.paper.configuration.WorldConfiguration config = entity.level().paperConfig();
        if (entity instanceof ServerPlayer) {
            return config.entities.trackingRange.player;
        }

        if (entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon) {
            // Exempt ender dragon
            return ((ServerLevel) entity.level()).getChunkSource().chunkMap.serverViewDistance << 4;
        }

        switch (entity.activationType) {
            case RAIDER:
            case MONSTER:
            case FLYING_MONSTER:
                return config.entities.trackingRange.monster;
            case WATER:
            case VILLAGER:
            case ANIMAL:
                return config.entities.trackingRange.animal;
            case MISC:
        }

        if (entity instanceof ItemFrame || entity instanceof Painting || entity instanceof ItemEntity || entity instanceof ExperienceOrb) {
            return config.entities.trackingRange.misc;
        } else if (entity instanceof Display) {
            return config.entities.trackingRange.display;
        } else {
            return config.entities.trackingRange.other;
        }
    }
}
