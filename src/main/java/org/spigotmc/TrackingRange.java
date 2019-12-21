package org.spigotmc;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityEnderDragon; // Paper
import net.minecraft.server.WorldServer; // Paper
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityGhast;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityItemFrame;
import net.minecraft.server.EntityPainting;
import net.minecraft.server.EntityPlayer;

public class TrackingRange
{

    /**
     * Gets the range an entity should be 'tracked' by players and visible in
     * the client.
     *
     * @param entity
     * @param defaultRange Default range defined by Mojang
     * @return
     */
    public static int getEntityTrackingRange(Entity entity, int defaultRange)
    {
        SpigotWorldConfig config = entity.world.spigotConfig;
        if ( entity instanceof EntityPlayer )
        {
            return config.playerTrackingRange;
        // Paper start - Simplify and set water mobs to animal tracking range
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
        if ( entity instanceof EntityItemFrame || entity instanceof EntityPainting || entity instanceof EntityItem || entity instanceof EntityExperienceOrb )
        // Paper end
        {
            return config.miscTrackingRange;
        } else
        {
            if (entity instanceof EntityEnderDragon) return ((WorldServer)(entity.getWorld())).getChunkProvider().playerChunkMap.getLoadViewDistance(); // Paper - enderdragon is exempt
            return config.otherTrackingRange;
        }
    }
}
