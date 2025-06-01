package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Vault;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftVault extends CraftBlockData implements Vault {
    private static final EnumProperty<Direction> FACING = VaultBlock.FACING;

    private static final BooleanProperty OMINOUS = VaultBlock.OMINOUS;

    private static final EnumProperty<VaultState> STATE = BlockStateProperties.VAULT_STATE;

    public CraftVault(BlockState state) {
        super(state);
    }

    @Override
    public BlockFace getFacing() {
        return this.get(FACING, BlockFace.class);
    }

    @Override
    public void setFacing(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.isCartesian() && blockFace.getModY() == 0, "Invalid face, only cartesian horizontal face are allowed for this property!");
        this.set(FACING, blockFace);
    }

    @Override
    public Set<BlockFace> getFaces() {
        return this.getValues(FACING, BlockFace.class);
    }

    @Override
    public boolean isOminous() {
        return this.get(OMINOUS);
    }

    @Override
    public void setOminous(final boolean ominous) {
        this.set(OMINOUS, ominous);
    }

    @Override
    public Vault.State getVaultState() {
        return this.get(STATE, Vault.State.class);
    }

    @Override
    public void setVaultState(final Vault.State state) {
        Preconditions.checkArgument(state != null, "state cannot be null!");
        this.set(STATE, state);
    }
}
