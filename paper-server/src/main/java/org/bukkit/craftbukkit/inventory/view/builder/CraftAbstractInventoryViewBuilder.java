package org.bukkit.craftbukkit.inventory.view.builder;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jetbrains.annotations.NotNull;

public abstract class CraftAbstractInventoryViewBuilder<V extends InventoryView> implements InventoryViewBuilder<V> {

    protected final MenuType<?> handle;

    protected boolean checkReachable = false;
    protected Component title = null;

    public CraftAbstractInventoryViewBuilder(MenuType<?> handle) {
        this.handle = handle;
    }

    @NotNull
    @Override
    public InventoryViewBuilder<V> title(@NotNull final String title) {
        this.title = LegacyComponentSerializer.legacySection().deserialize(title);
        return this;
    }

    @Override
    public @NotNull InventoryViewBuilder<V> title(final @NotNull Component title) {
        this.title = title;
        return this;
    }

    @Override
    public V build(final HumanEntity player) {
        Preconditions.checkArgument(player != null, "The given player must not be null");
        Preconditions.checkArgument(this.title != null, "The given title must not be null");
        Preconditions.checkArgument(player instanceof CraftHumanEntity, "The given player must be a CraftHumanEntity");
        final CraftHumanEntity craftHuman = (CraftHumanEntity) player;
        Preconditions.checkArgument(craftHuman.getHandle() instanceof ServerPlayer, "The given player must be an EntityPlayer");
        final ServerPlayer serverPlayer = (ServerPlayer) craftHuman.getHandle();
        final AbstractContainerMenu container = buildContainer(serverPlayer);
        container.checkReachable = this.checkReachable;
        container.setTitle(PaperAdventure.asVanilla(this.title));
        return (V) container.getBukkitView();
    }

    protected abstract AbstractContainerMenu buildContainer(ServerPlayer player);
}
