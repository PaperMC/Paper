package io.papermc.paper.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.HolderableBase;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.ItemType;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperCreativeModeTab extends HolderableBase<CreativeModeTab> implements io.papermc.paper.inventory.CreativeModeTab {

    private @MonotonicNonNull Set<ItemType> itemTypes;

    public PaperCreativeModeTab(final Holder<CreativeModeTab> holder) {
        super(holder);
    }

    private void requireContents() {
        CreativeModeTabs.tryRebuildTabContents(CraftRegistry.getEnabledFeatures(), true, CraftRegistry.getMinecraftRegistry());
        if (this.itemTypes == null) {
            this.itemTypes = new HashSet<>();

            for (final ItemStack itemStack : this.getHandle().getDisplayItems()) {
                this.itemTypes.add(CraftItemType.minecraftToBukkitNew(itemStack.getItem()));
            }
        }
    }

    @Override
    public String translationKey() {
        return ((TranslatableContents) this.getHandle().getDisplayName().getContents()).getKey();
    }

    @Override
    public Component displayName() {
        return PaperAdventure.asAdventure(this.getHandle().getDisplayName());
    }

    @Override
    public org.bukkit.inventory.ItemStack iconItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getIconItem());
    }

    @Override
    public boolean scrollbarShown() {
        return this.getHandle().canScroll();
    }

    @Override
    public boolean containsItem(final ItemType type) {
        Preconditions.checkArgument(type != null, "itemType may not be null.");
        this.requireContents();

        return this.itemTypes.contains(type);
    }

    @Override
    public boolean containsItemStack(final org.bukkit.inventory.ItemStack stack) {
        Preconditions.checkArgument(stack != null, "itemStack may not be null.");
        this.requireContents();

        return this.getHandle().getDisplayItems().contains(CraftItemStack.unwrap(stack));
    }

    @Override
    public @Unmodifiable Collection<org.bukkit.inventory.ItemStack> getContents() {
        this.requireContents();

        final ImmutableList.Builder<org.bukkit.inventory.ItemStack> contents = ImmutableList.builder();

        for (final ItemStack itemStack : this.getHandle().getDisplayItems()) {
            contents.add(CraftItemStack.asBukkitCopy(itemStack));
        }

        return contents.build();
    }

    @Override
    public Row getRow() {
        return Row.valueOf(this.getHandle().row().name());
    }

    @Override
    public Type getType() {
        return Type.valueOf(this.getHandle().getType().name());
    }

    @SuppressWarnings("deprecation")
    public CreativeCategory toLegacy() {
        final String translationKey = this.translationKey();

        for (final CreativeCategory category : CreativeCategory.values()) {
            if (category.translationKey().equals(translationKey)) {
                return category;
            }
        }

        return CreativeCategory.NATURAL_BLOCKS;
    }
}
