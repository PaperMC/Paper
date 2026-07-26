package org.bukkit.craftbukkit.entity;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Interaction;

public class CraftInteraction extends CraftEntity implements Interaction {

    public CraftInteraction(CraftServer server, net.minecraft.world.entity.Interaction entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Interaction getHandle() {
        return (net.minecraft.world.entity.Interaction) this.entity;
    }

    @Override
    public float getInteractionWidth() {
        return this.getHandle().getWidth();
    }

    @Override
    public void setInteractionWidth(float width) {
        this.getHandle().setWidth(width);
    }

    @Override
    public float getInteractionHeight() {
        return this.getHandle().getHeight();
    }

    @Override
    public void setInteractionHeight(float height) {
        this.getHandle().setHeight(height);
    }

    @Override
    public boolean isResponsive() {
        return this.getHandle().getResponse();
    }

    @Override
    public void setResponsive(boolean response) {
        this.getHandle().setResponse(response);
    }

    @Override
    public PreviousInteraction getLastAttack() {
        net.minecraft.world.entity.Interaction.PlayerAction last = this.getHandle().attack;

        return (last != null) ? new CraftPreviousInteraction(last.player(), last.timestamp()) : null;
    }

    @Override
    public PreviousInteraction getLastInteraction() {
        net.minecraft.world.entity.Interaction.PlayerAction last = this.getHandle().interaction;

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
            return Bukkit.getOfflinePlayer(this.uuid);
        }

        @Override
        public long getTimestamp() {
            return this.timestamp;
        }
    }
}
