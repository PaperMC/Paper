package org.bukkit.craftbukkit.entity;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityLightning;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {
    public CraftLightningStrike(final CraftServer server, final EntityLightning entity) {
        super(server, entity);
    }

    @Override
    public boolean isEffect() {
        return getHandle().visualOnly;
    }

    public int getFlashes() {
        return getHandle().flashes;
    }

    public void setFlashes(int flashes) {
        getHandle().flashes = flashes;
    }

    public int getLifeTicks() {
        return getHandle().life;
    }

    public void setLifeTicks(int ticks) {
        getHandle().life = ticks;
    }

    public Player getCausingPlayer() {
        EntityPlayer player = getHandle().getCause();
        return (player != null) ? player.getBukkitEntity() : null;
    }

    public void setCausingPlayer(Player player) {
        getHandle().setCause((player != null) ? ((CraftPlayer) player).getHandle() : null);
    }

    @Override
    public EntityLightning getHandle() {
        return (EntityLightning) entity;
    }

    @Override
    public String toString() {
        return "CraftLightningStrike";
    }
}
