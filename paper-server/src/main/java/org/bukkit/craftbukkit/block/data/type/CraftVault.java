package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Vault;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftVault extends CraftBlockData implements Vault {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> VAULT_STATE = getEnum("vault_state");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty OMINOUS = getBoolean("ominous");

    @Override
    public org.bukkit.block.data.type.Vault.State getVaultState() {
        return this.get(CraftVault.VAULT_STATE, org.bukkit.block.data.type.Vault.State.class);
    }

    @Override
    public org.bukkit.block.data.type.Vault.State getTrialSpawnerState() {
        return this.getVaultState();
    }

    @Override
    public void setVaultState(org.bukkit.block.data.type.Vault.State state) {
        this.set(CraftVault.VAULT_STATE, state);
    }

    @Override
    public void setTrialSpawnerState(org.bukkit.block.data.type.Vault.State state) {
        this.setVaultState(state);
    }

    @Override
    public boolean isOminous() {
        return this.get(CraftVault.OMINOUS);
    }

    @Override
    public void setOminous(boolean ominous) {
        this.set(CraftVault.OMINOUS, ominous);
    }
}
