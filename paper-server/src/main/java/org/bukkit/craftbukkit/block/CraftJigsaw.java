package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Jigsaw;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

import static net.minecraft.core.registries.Registries.TEMPLATE_POOL;

public class CraftJigsaw extends CraftBlockEntityState<JigsawBlockEntity> implements Jigsaw {

    public CraftJigsaw(World world, JigsawBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftJigsaw(CraftJigsaw state, Location location) {
        super(state, location);
    }

    @Override
    @NotNull
    public NamespacedKey getTargetPool() {
        final Identifier targetPoolKey = getSnapshot().getPool().identifier();
        return CraftNamespacedKey.fromMinecraft(targetPoolKey);
    }

    @Override
    public void setTargetPool(final @NotNull NamespacedKey targetPool) {
        Preconditions.checkArgument(Objects.nonNull(targetPool), "targetPool cannot be null");
        getSnapshot().setPool(CraftNamespacedKey.toResourceKey(TEMPLATE_POOL, targetPool));
    }

    @Override
    @NotNull
    public NamespacedKey getName() {
        final Identifier targetPoolKey = this.getSnapshot().getName();
        return CraftNamespacedKey.fromMinecraft(targetPoolKey);
    }

    @Override
    public void setName(final @NotNull NamespacedKey name) {
        Preconditions.checkArgument(Objects.nonNull(name), "name cannot be null");
        getSnapshot().setName(CraftNamespacedKey.toMinecraft(name));
    }

    @Override
    @NotNull
    public NamespacedKey getTargetName() {
        final Identifier targetPoolKey = this.getSnapshot().getTarget();
        return CraftNamespacedKey.fromMinecraft(targetPoolKey);
    }

    @Override
    public void setTargetName(final @NotNull NamespacedKey targetName) {
        Preconditions.checkArgument(Objects.nonNull(targetName), "targetName cannot be null");
        getSnapshot().setTarget(CraftNamespacedKey.toMinecraft(targetName));
    }

    @Override
    public CraftJigsaw copy() {
        return new CraftJigsaw(this, null);
    }

    @Override
    public CraftJigsaw copy(Location location) {
        return new CraftJigsaw(this, location);
    }
}
