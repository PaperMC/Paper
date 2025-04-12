package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Vault;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@NullMarked
public class CraftVault extends CraftBlockEntityState<VaultBlockEntity> implements Vault {

    public CraftVault(World world, VaultBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftVault(CraftVault state, @Nullable Location location) {
        super(state, location);
    }

    @Override
    public CraftVault copy() {
        return new CraftVault(this, null);
    }

    @Override
    public CraftVault copy(Location location) {
        return new CraftVault(this, location);
    }

    @Override
    public double getActivationRange() {
        return this.getSnapshot().getConfig().activationRange();
    }

    @Override
    public void setActivationRange(final double activationRange) {
        Preconditions.checkArgument(Double.isFinite(activationRange), "activation range must not be NaN or infinite");
        Preconditions.checkArgument(activationRange <= this.getDeactivationRange(), "New activation range (%s) must be less or equal to deactivation range (%s)", activationRange, this.getDeactivationRange());

        final VaultConfig config = this.getSnapshot().getConfig();
        this.getSnapshot().setConfig(new VaultConfig(config.lootTable(), activationRange, config.deactivationRange(), config.keyItem(), config.overrideLootTableToDisplay()));
    }

    @Override
    public double getDeactivationRange() {
        return this.getSnapshot().getConfig().deactivationRange();
    }

    @Override
    public void setDeactivationRange(final double deactivationRange) {
        Preconditions.checkArgument(Double.isFinite(deactivationRange), "deactivation range must not be NaN or infinite");
        Preconditions.checkArgument(deactivationRange >= this.getActivationRange(), "New deactivation range (%s) must be more or equal to activation range (%s)", deactivationRange, this.getActivationRange());

        final VaultConfig config = this.getSnapshot().getConfig();
        this.getSnapshot().setConfig(new VaultConfig(config.lootTable(), config.activationRange(), deactivationRange, config.keyItem(), config.overrideLootTableToDisplay()));
    }

    @Override
    public ItemStack getKeyItem() {
        return this.getSnapshot().getConfig().keyItem().asBukkitCopy();
    }

    @Override
    public void setKeyItem(final ItemStack key) {
        Preconditions.checkArgument(key != null, "key must not be null");

        final VaultConfig config = this.getSnapshot().getConfig();
        this.getSnapshot().setConfig(new VaultConfig(config.lootTable(), config.activationRange(), config.deactivationRange(), CraftItemStack.asNMSCopy(key), config.overrideLootTableToDisplay()));
    }

    @Override
    public LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(this.getSnapshot().getConfig().lootTable());
    }

    @Override
    public void setLootTable(final LootTable lootTable) {
        final ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootTableKey = CraftLootTable.bukkitToMinecraft(lootTable);
        Preconditions.checkArgument(lootTableKey != null, "lootTable must not be null");

        final VaultConfig config = this.getSnapshot().getConfig();
        this.getSnapshot().setConfig(new VaultConfig(lootTableKey, config.activationRange(), config.deactivationRange(), config.keyItem(), config.overrideLootTableToDisplay()));
    }

    @Override
    public @Nullable LootTable getDisplayedLootTable() {
        return this.getSnapshot().getConfig().overrideLootTableToDisplay().map(CraftLootTable::minecraftToBukkit).orElse(null);
    }

    @Override
    public void setDisplayedLootTable(final @Nullable LootTable lootTable) {
        final VaultConfig config = this.getSnapshot().getConfig();

        this.getSnapshot().setConfig(new VaultConfig(config.lootTable(), config.activationRange(), config.deactivationRange(), config.keyItem(), Optional.ofNullable(CraftLootTable.bukkitToMinecraft(lootTable))));
    }

    @Override
    public long getNextStateUpdateTime() {
        return this.getSnapshot().serverData.stateUpdatingResumesAt();
    }

    @Override
    public void setNextStateUpdateTime(final long nextStateUpdateTime) {
        this.getSnapshot().serverData.pauseStateUpdatingUntil(nextStateUpdateTime);
    }

    @Override
    public @Unmodifiable Collection<UUID> getRewardedPlayers() {
        return ImmutableSet.copyOf(this.getSnapshot().serverData.getRewardedPlayers());
    }

    @Override
    public boolean addRewardedPlayer(final UUID playerUUID) {
        Preconditions.checkArgument(playerUUID != null, "playerUUID must not be null");
        return this.getSnapshot().serverData.addToRewardedPlayers(playerUUID);
    }

    @Override
    public boolean removeRewardedPlayer(final UUID playerUUID) {
        Preconditions.checkArgument(playerUUID != null, "playerUUID must not be null");
        return this.getSnapshot().serverData.removeFromRewardedPlayers(playerUUID);
    }

    @Override
    public boolean hasRewardedPlayer(final UUID playerUUID) {
        return this.getSnapshot().serverData.getRewardedPlayers().contains(playerUUID);
    }

    @Override
    public @Unmodifiable Set<UUID> getConnectedPlayers() {
        return ImmutableSet.copyOf(this.getSnapshot().getSharedData().getConnectedPlayers());
    }

    @Override
    public boolean hasConnectedPlayer(final UUID playerUUID) {
        return this.getSnapshot().getSharedData().getConnectedPlayers().contains(playerUUID);
    }

    @Override
    public ItemStack getDisplayedItem() {
        return CraftItemStack.asBukkitCopy(this.getSnapshot().getSharedData().getDisplayItem());
    }

    @Override
    public void setDisplayedItem(final ItemStack displayedItem) {
        Preconditions.checkArgument(displayedItem != null, "displayedItem must not be null");
        this.getSnapshot().getSharedData().setDisplayItem(CraftItemStack.asNMSCopy(displayedItem));
    }
}
