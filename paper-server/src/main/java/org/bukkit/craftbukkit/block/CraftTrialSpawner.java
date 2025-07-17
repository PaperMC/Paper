package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.TrialSpawnerBlock;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerStateData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.TrialSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.spawner.TrialSpawnerConfiguration;

public class CraftTrialSpawner extends CraftBlockEntityState<TrialSpawnerBlockEntity> implements TrialSpawner {

    private final CraftTrialSpawnerConfiguration normalConfig;
    private final CraftTrialSpawnerConfiguration ominousConfig;

    public CraftTrialSpawner(World world, TrialSpawnerBlockEntity blockEntity) {
        super(world, blockEntity);
        this.normalConfig = new CraftTrialSpawnerConfiguration(blockEntity.getTrialSpawner().normalConfig(), this.getSnapshot());
        this.ominousConfig = new CraftTrialSpawnerConfiguration(blockEntity.getTrialSpawner().ominousConfig(), this.getSnapshot());
    }

    protected CraftTrialSpawner(CraftTrialSpawner state, Location location) {
        super(state, location);
        this.normalConfig = state.normalConfig;
        this.ominousConfig = state.ominousConfig;
    }

    @Override
    public long getCooldownEnd() {
        return this.getSnapshot().trialSpawner.getStateData().cooldownEndsAt;
    }

    @Override
    public void setCooldownEnd(long ticks) {
        this.getSnapshot().trialSpawner.getStateData().cooldownEndsAt = ticks;
    }

    @Override
    public long getNextSpawnAttempt() {
        return this.getSnapshot().trialSpawner.getStateData().nextMobSpawnsAt;
    }

    @Override
    public void setNextSpawnAttempt(long ticks) {
        this.getSnapshot().trialSpawner.getStateData().nextMobSpawnsAt = ticks;
    }

    @Override
    public int getCooldownLength() {
        return this.getSnapshot().trialSpawner.getTargetCooldownLength();
    }

    @Override
    public void setCooldownLength(int ticks) {
        this.getSnapshot().trialSpawner.config = this.getSnapshot().trialSpawner.config.overrideTargetCooldownLength(ticks);
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.getSnapshot().trialSpawner.getRequiredPlayerRange();
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        this.getSnapshot().trialSpawner.config = this.getSnapshot().trialSpawner.config.overrideRequiredPlayerRange(requiredPlayerRange);
    }

    @Override
    public Collection<Player> getTrackedPlayers() {
        ImmutableSet.Builder<Player> players = ImmutableSet.builder();

        for (UUID uuid : this.getTrialData().detectedPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                players.add(player);
            }
        }
        return players.build();
    }

    @Override
    public boolean isTrackingPlayer(Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null");

        return this.getTrialData().detectedPlayers.contains(player.getUniqueId());
    }

    @Override
    public void startTrackingPlayer(Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null");

        this.getTrialData().detectedPlayers.add(player.getUniqueId());
    }

    @Override
    public void stopTrackingPlayer(Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null");

        this.getTrialData().detectedPlayers.remove(player.getUniqueId());
    }

    @Override
    public Collection<Entity> getTrackedEntities() {
        ImmutableSet.Builder<Entity> entities = ImmutableSet.builder();

        for (UUID uuid : this.getTrialData().currentMobs) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity != null) {
                entities.add(entity);
            }
        }
        return entities.build();
    }

    @Override
    public boolean isTrackingEntity(Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");

        return this.getTrialData().currentMobs.contains(entity.getUniqueId());
    }

    @Override
    public void startTrackingEntity(Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");

        this.getTrialData().currentMobs.add(entity.getUniqueId());
    }

    @Override
    public void stopTrackingEntity(Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");

        this.getTrialData().currentMobs.remove(entity.getUniqueId());
    }

    @Override
    public boolean isOminous() {
        return this.getHandle().getValue(TrialSpawnerBlock.OMINOUS);
    }

    @Override
    public void setOminous(boolean ominous) {
        this.getSnapshot().trialSpawner.isOminous = ominous;
        if (ominous) {
            this.setData(this.getHandle().setValue(TrialSpawnerBlock.OMINOUS, true));
            // TODO: Consider calling TrialSpawnerData#resetAfterBecomingOminous in update(...), but note that method also removes entities
            return;
        }

        this.setData(this.getHandle().setValue(TrialSpawnerBlock.OMINOUS, false));
    }

    @Override
    public TrialSpawnerConfiguration getNormalConfiguration() {
        return this.normalConfig;
    }

    @Override
    public TrialSpawnerConfiguration getOminousConfiguration() {
        return this.ominousConfig;
    }

    @Override
    protected void applyTo(TrialSpawnerBlockEntity blockEntity) {
        super.applyTo(blockEntity);

        blockEntity.trialSpawner.config = blockEntity.trialSpawner.config.overrideConfigs(
            Holder.direct(this.normalConfig.toMinecraft()),
            Holder.direct(this.ominousConfig.toMinecraft())
        );
    }

    private TrialSpawnerStateData getTrialData() {
        return this.getSnapshot().getTrialSpawner().getStateData();
    }

    @Override
    public CraftTrialSpawner copy() {
        return new CraftTrialSpawner(this, null);
    }

    @Override
    public CraftTrialSpawner copy(Location location) {
        return new CraftTrialSpawner(this, location);
    }
}
