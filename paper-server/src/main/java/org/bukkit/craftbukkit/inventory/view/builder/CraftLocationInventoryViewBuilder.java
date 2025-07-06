package org.bukkit.craftbukkit.inventory.view.builder;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class CraftLocationInventoryViewBuilder<V extends InventoryView> extends CraftInventoryViewBuilder<V> implements LocationInventoryViewBuilder<V> {

    protected final Block block;

    protected @Nullable Location bukkitLocation;
    // world and position are both mutable internal fields, will be inlined in the future if easily possible
    protected @Nullable Level world;
    protected @Nullable BlockPos position;

    public CraftLocationInventoryViewBuilder(final MenuType<?> handle, final Block block) {
        super(handle);
        this.block = block;
    }

    @Override
    public LocationInventoryViewBuilder<V> title(final @Nullable Component title) {
        return (LocationInventoryViewBuilder<V>) super.title(title);
    }

    @Override
    public LocationInventoryViewBuilder<V> location(final Location location) {
        Preconditions.checkArgument(location != null, "The provided location must not be null");
        Preconditions.checkArgument(location.getWorld() != null, "The provided location must be associated with a world");
        this.bukkitLocation = location;
        setupLocation();
        return this;
    }

    @Override
    public LocationInventoryViewBuilder<V> checkReachable(final boolean checkReachable) {
        super.checkReachable = checkReachable;
        return this;
    }

    protected void setupLocation() {
        if (this.bukkitLocation != null) {
            this.world = ((CraftWorld) this.bukkitLocation.getWorld()).getHandle();
            this.position = CraftLocation.toBlockPosition(this.bukkitLocation);
        }
    }
}
