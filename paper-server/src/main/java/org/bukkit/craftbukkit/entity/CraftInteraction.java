package org.bukkit.craftbukkit.entity;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;

public class CraftInteraction extends CraftEntity implements Interaction {

    public CraftInteraction(CraftServer server, net.minecraft.world.entity.Interaction entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Interaction getHandle() {
        return (net.minecraft.world.entity.Interaction) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftInteraction";
    }

    @Override
    public EntityType getType() {
        return EntityType.INTERACTION;
    }

    @Override
    public float getInteractionWidth() {
        return getHandle().getWidth();
    }

    @Override
    public void setInteractionWidth(float width) {
        getHandle().setWidth(width);
    }

    @Override
    public float getInteractionHeight() {
        return getHandle().getHeight();
    }

    @Override
    public void setInteractionHeight(float height) {
        getHandle().setHeight(height);
    }

    @Override
    public boolean isResponsive() {
        return getHandle().getResponse();
    }

    @Override
    public void setResponsive(boolean response) {
        getHandle().setResponse(response);
    }

    @Override
    public PreviousInteraction getLastAttack() {
        net.minecraft.world.entity.Interaction.PlayerAction last = getHandle().attack;

        return (last != null) ? new CraftPreviousInteraction(last.player(), last.timestamp()) : null;
    }

    @Override
    public PreviousInteraction getLastInteraction() {
        net.minecraft.world.entity.Interaction.PlayerAction last = getHandle().interaction;

        return (last != null) ? new CraftPreviousInteraction(last.player(), last.timestamp()) : null;
    }

    private static class CraftPreviousInteraction implements PreviousInteraction {

        private final UUID uuid;
        private final long timestamp;

        public CraftPreviousInteraction(UUID uuid, long timestamp) {
            this.uuid = uuid;
            this.timestamp = timestamp;
        }

        @Override
        public OfflinePlayer getPlayer() {
            return Bukkit.getOfflinePlayer(uuid);
        }

        @Override
        public long getTimestamp() {
            return timestamp;
        }
    }
}
