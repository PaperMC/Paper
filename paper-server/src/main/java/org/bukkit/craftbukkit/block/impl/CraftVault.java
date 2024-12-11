/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftVault extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Vault, org.bukkit.block.data.Directional {

    public CraftVault() {
        super();
    }

    public CraftVault(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftVault

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> VAULT_STATE = getEnum(net.minecraft.world.level.block.VaultBlock.class, "vault_state");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty OMINOUS = getBoolean(net.minecraft.world.level.block.VaultBlock.class, "ominous");

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

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.VaultBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftVault.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftVault.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftVault.FACING, org.bukkit.block.BlockFace.class);
    }
}
