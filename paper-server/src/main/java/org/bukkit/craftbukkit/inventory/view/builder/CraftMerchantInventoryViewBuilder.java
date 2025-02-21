package org.bukkit.craftbukkit.inventory.view.builder;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MerchantMenu;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftMerchant;
import org.bukkit.craftbukkit.inventory.CraftMerchantCustom;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.view.builder.MerchantInventoryViewBuilder;
import org.jspecify.annotations.Nullable;

public class CraftMerchantInventoryViewBuilder<V extends InventoryView> extends CraftAbstractInventoryViewBuilder<V> implements MerchantInventoryViewBuilder<V> {

    private net.minecraft.world.item.trading.@Nullable Merchant merchant;

    public CraftMerchantInventoryViewBuilder(final MenuType<?> handle) {
        super(handle);
    }

    @Override
    public MerchantInventoryViewBuilder<V> title(final @Nullable Component title) {
        return (MerchantInventoryViewBuilder<V>) super.title(title);
    }

    @Override
    public MerchantInventoryViewBuilder<V> merchant(final Merchant merchant) {
        this.merchant = ((CraftMerchant) merchant).getMerchant();
        return this;
    }

    @Override
    public MerchantInventoryViewBuilder<V> checkReachable(final boolean checkReachable) {
        super.checkReachable = checkReachable;
        return this;
    }

    @Override
    public V build(final HumanEntity player) {
        Preconditions.checkArgument(player != null, "The given player must not be null");
        Preconditions.checkArgument(player instanceof CraftHumanEntity, "The given player must be a CraftHumanEntity");
        final CraftHumanEntity craftHuman = (CraftHumanEntity) player;
        Preconditions.checkArgument(craftHuman.getHandle() instanceof ServerPlayer, "The given player must be an ServerPlayer");
        final ServerPlayer serverPlayer = (ServerPlayer) craftHuman.getHandle();

        final MerchantMenu container;
        if (this.merchant == null) {
            this.merchant = this.title == null ? new CraftMerchantCustom().getMerchant() : new CraftMerchantCustom(title).getMerchant();
        }

        container = new MerchantMenu(serverPlayer.nextContainerCounter(), serverPlayer.getInventory(), this.merchant);

        container.checkReachable = super.checkReachable;
        setDefaultTitle(this.merchant);
        container.setTitle(super.title != null ? PaperAdventure.asVanilla(this.title) : super.defaultTitle);
        return (V) container.getBukkitView();
    }

    private void setDefaultTitle(final net.minecraft.world.item.trading.Merchant merchant) {
        if (merchant instanceof final AbstractVillager villager) {
            super.defaultTitle = villager.getDisplayName();
        } else if (merchant instanceof final CraftMerchantCustom.MinecraftMerchant custom) {
            super.defaultTitle = custom.getScoreboardDisplayName();
        } else {
            throw new IllegalStateException("Provided merchant during MenuType creation can not find a default title! This is a bug!");
        }
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        throw new UnsupportedOperationException("buildContainer is not supported for CraftMerchantInventoryViewBuilder");
    }

    @Override
    public MerchantInventoryViewBuilder<V> copy() {
        final CraftMerchantInventoryViewBuilder<V> copy = new CraftMerchantInventoryViewBuilder<>(super.handle);
        copy.checkReachable = super.checkReachable;
        copy.merchant = this.merchant;
        copy.title = title;
        return copy;
    }
}
