package org.bukkit.craftbukkit.inventory.view.builder;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jspecify.annotations.Nullable;

public abstract class CraftAbstractInventoryViewBuilder<V extends InventoryView> implements InventoryViewBuilder<V> {

    protected final MenuType<?> handle;

    protected boolean checkReachable = false;
    protected @Nullable Component title = null;
    protected net.minecraft.network.chat.Component defaultTitle = null;

    public CraftAbstractInventoryViewBuilder(final MenuType<?> handle) {
        this.handle = handle;
    }

    @Override
    public InventoryViewBuilder<V> title(final @Nullable Component title) {
        this.title = title;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V build(final HumanEntity player) {
        Preconditions.checkArgument(player != null, "The given player must not be null");
        Preconditions.checkArgument(player instanceof CraftHumanEntity, "The given player must be a CraftHumanEntity");
        final CraftHumanEntity craftHuman = (CraftHumanEntity) player;
        Preconditions.checkArgument(craftHuman.getHandle() instanceof ServerPlayer, "The given player must be an ServerPlayer");
        final ServerPlayer serverPlayer = (ServerPlayer) craftHuman.getHandle();

        final AbstractContainerMenu container = buildContainer(serverPlayer);
        container.checkReachable = this.checkReachable;
        container.setTitle(this.title != null ? PaperAdventure.asVanilla(this.title) : this.defaultTitle);
        return (V) container.getBukkitView();
    }

    protected abstract AbstractContainerMenu buildContainer(ServerPlayer player);
}
