package org.spigotmc;

import net.minecraft.server.Entity;
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
        }  else if ( entity.activationType == ActivationRange.ActivationType.MONSTER || entity.activationType == ActivationRange.ActivationType.RAIDER )
        {
            return config.monsterTrackingRange;
        } else if ( entity instanceof EntityGhast )
        {
            if ( config.monsterTrackingRange > config.monsterActivationRange )
            {
                return config.monsterTrackingRange;
            } else
            {
                return config.monsterActivationRange;
            }
        } else if ( entity.activationType == ActivationRange.ActivationType.ANIMAL )
        {
            return config.animalTrackingRange;
        } else if ( entity instanceof EntityItemFrame || entity instanceof EntityPainting || entity instanceof EntityItem || entity instanceof EntityExperienceOrb )
        {
            return config.miscTrackingRange;
        } else
        {
            return config.otherTrackingRange;
        }
    }
}
