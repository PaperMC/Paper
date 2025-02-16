package org.bukkit.craftbukkit.inventory.view.builder;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.jspecify.annotations.Nullable;

public abstract class CraftAbstractLocationInventoryViewBuilder<V extends InventoryView> extends CraftAbstractInventoryViewBuilder<V> implements LocationInventoryViewBuilder<V> {

    protected @Nullable Level world;
    protected @Nullable BlockPos position;

    public CraftAbstractLocationInventoryViewBuilder(final MenuType<?> handle) {
        super(handle);
    }

    @Override
    public LocationInventoryViewBuilder<V> title(final @Nullable Component title) {
        return (LocationInventoryViewBuilder<V>) super.title(title);
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        throw new UnsupportedOperationException("copy is not implemented on CraftAbstractLocationInventoryViewBuilder");
    }

    @Override
    public LocationInventoryViewBuilder<V> checkReachable(final boolean checkReachable) {
        super.checkReachable = checkReachable;
        return this;
    }

    @Override
    public LocationInventoryViewBuilder<V> location(final Location location) {
        Preconditions.checkArgument(location != null, "The provided location must not be null");
        Preconditions.checkArgument(location.getWorld() != null, "The provided location must be associated with a world");
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.position = CraftLocation.toBlockPosition(location);
        return this;
    }
}
