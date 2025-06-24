package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import io.papermc.paper.block.PaperTrialSpawnerConfig;
import io.papermc.paper.block.TrialSpawnerConfig;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.Optionull;
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
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftTrialSpawner extends CraftBlockEntityState<TrialSpawnerBlockEntity> implements TrialSpawner {

    private @MonotonicNonNull CraftTrialSpawnerConfiguration normalConfig;
    private @MonotonicNonNull CraftTrialSpawnerConfiguration ominousConfig;

    public CraftTrialSpawner(World world, TrialSpawnerBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftTrialSpawner(CraftTrialSpawner state, @Nullable Location location) {
        super(state, location);
        // only copy if defined (legacy path)
        if (state.normalConfig != null) {
            this.normalConfig = new CraftTrialSpawnerConfiguration(state.normalConfig, this.getSnapshot());
        }
        if (state.ominousConfig != null) {
            this.ominousConfig = new CraftTrialSpawnerConfiguration(state.ominousConfig, this.getSnapshot());
        }
    }

    @Override
    protected void load(TrialSpawnerBlockEntity blockEntity) {
        super.load(blockEntity);
        blockEntity.getTrialSpawner().isOminous = this.getBlockEntity().getTrialSpawner().isOminous;
    }

    @Override
    protected void applyTo(TrialSpawnerBlockEntity blockEntity) {
        super.applyTo(blockEntity);

        if (this.normalConfig != null || this.ominousConfig != null) {
            // only if used, needs to override the modern config for backward compat, remove later
            blockEntity.trialSpawner.config = blockEntity.trialSpawner.config.withConfigs(
                Optionull.mapOrDefault(this.normalConfig, config -> Holder.direct(this.normalConfig.toMinecraft()), blockEntity.trialSpawner.config.normal()),
                Optionull.mapOrDefault(this.ominousConfig, config -> Holder.direct(this.ominousConfig.toMinecraft()), blockEntity.trialSpawner.config.ominous())
            );
        }
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
        this.getSnapshot().trialSpawner.config = this.getSnapshot().trialSpawner.config.withTargetCooldownLength(ticks);
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.getSnapshot().trialSpawner.getRequiredPlayerRange();
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        this.getSnapshot().trialSpawner.config = this.getSnapshot().trialSpawner.config.withRequiredPlayerRange(requiredPlayerRange);
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
        return this.data.getValueOrElse(TrialSpawnerBlock.OMINOUS, false);
    }

    @Override
    public void setOminous(boolean ominous) {
        if (!this.data.hasProperty(TrialSpawnerBlock.OMINOUS)) {
            return; // block data changed
        }

        this.getSnapshot().trialSpawner.isOminous = ominous;
        if (ominous) {
            this.data = this.data.setValue(TrialSpawnerBlock.OMINOUS, true);
            // TODO: Consider calling TrialSpawnerData#resetAfterBecomingOminous in update(...), but note that method also removes entities
            return;
        }

        this.data = this.data.setValue(TrialSpawnerBlock.OMINOUS, false);
    }

    @Override
    public TrialSpawnerConfiguration getNormalConfiguration() {
        if (this.normalConfig == null) {
            this.normalConfig = new CraftTrialSpawnerConfiguration(this.getSnapshot().getTrialSpawner().normalConfig(), this.getSnapshot());
        }
        return this.normalConfig;
    }

    @Override
    public TrialSpawnerConfiguration getOminousConfiguration() {
        if (this.ominousConfig == null) {
            this.ominousConfig = new CraftTrialSpawnerConfiguration(this.getSnapshot().getTrialSpawner().ominousConfig(), this.getSnapshot());
        }
        return this.ominousConfig;
    }

    @Override
    public TrialSpawnerConfig currentConfig() {
        return this.getSnapshot().getTrialSpawner().isOminous() ? this.ominousConfig() : this.normalConfig();
    }

    @Override
    public TrialSpawnerConfig normalConfig() {
        return PaperTrialSpawnerConfig.minecraftHolderToBukkit(this.getSnapshot().getTrialSpawner().config.normal());
    }

    @Override
    public TrialSpawnerConfig ominousConfig() {
        return PaperTrialSpawnerConfig.minecraftHolderToBukkit(this.getSnapshot().getTrialSpawner().config.ominous());
    }

    @Override
    public void configure(TrialSpawnerConfig normalConfig, TrialSpawnerConfig ominousConfig) {
        this.getTrialData().reset(); // clear to repick from the spawn definition
        this.getSnapshot().getTrialSpawner().config = this.getSnapshot().getTrialSpawner().config.withConfigs(
            PaperTrialSpawnerConfig.bukkitToMinecraftHolder(normalConfig),
            PaperTrialSpawnerConfig.bukkitToMinecraftHolder(ominousConfig)
        );
    }

    @Override
    public void configure(TrialSpawnerConfig currentConfig) {
        this.getTrialData().reset(); // clear to repick from the spawn definition
        net.minecraft.world.level.block.entity.trialspawner.TrialSpawner spawner = this.getSnapshot().getTrialSpawner();
        spawner.config = spawner.config.withConfigs(
            !spawner.isOminous() ? PaperTrialSpawnerConfig.bukkitToMinecraftHolder(currentConfig) : spawner.config.normal(),
            spawner.isOminous() ? PaperTrialSpawnerConfig.bukkitToMinecraftHolder(currentConfig) : spawner.config.ominous()
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
