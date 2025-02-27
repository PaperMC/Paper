package io.papermc.paper.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NullMarked
public class PaperCreativeModeTab implements io.papermc.paper.inventory.CreativeModeTab, Handleable<CreativeModeTab> {
    private final NamespacedKey key;
    private final CreativeModeTab handle;
    private final io.papermc.paper.inventory.CreativeModeTab.Type type;

    private final Set<ItemType> itemTypes = new HashSet<>();
    private final Set<ItemStack> itemStacks = ItemStackLinkedSet.createTypeAndComponentsSet();

    private PaperCreativeModeTab(final NamespacedKey key, final CreativeModeTab handle) {
        this.key = key;
        this.handle = handle;
        this.type = io.papermc.paper.inventory.CreativeModeTab.Type.valueOf(handle.getType().name());
    }

    public static PaperCreativeModeTab create(final NamespacedKey key, final CreativeModeTab tab) {
        final PaperCreativeModeTab paperTab = new PaperCreativeModeTab(key, tab);

        // Special handling for search tabs, they contain the items of all other tabs.
        if (tab.getType() == CreativeModeTab.Type.SEARCH) {
            for (final CreativeModeTab otherTab : BuiltInRegistries.CREATIVE_MODE_TAB) {
                if (otherTab.getType() == CreativeModeTab.Type.SEARCH) {
                    continue;
                }

                final PaperCreativeModeTab created = create(key, otherTab);
                paperTab.itemStacks.addAll(created.itemStacks);
                paperTab.itemTypes.addAll(created.itemTypes);
            }
        }

        final ItemDisplayBuilder builder = buildTab(tab);

        paperTab.itemStacks.addAll(Stream.concat(builder.searchTabContents.stream(), builder.tabContents.stream())
            .collect(Collectors.toCollection(ItemStackLinkedSet::createTypeAndComponentsSet)));

        for (final ItemStack itemStack : paperTab.itemStacks) {
            final ItemType itemType = CraftItemType.minecraftToBukkitNew(itemStack.getItem());
            paperTab.itemTypes.add(itemType);
        }

        return paperTab;
    }

    private static ItemDisplayBuilder buildTab(final CreativeModeTab tab) {
        // Tab contents are not built by the server, so we have to build them ourselves.
        final CreativeModeTab.ItemDisplayParameters parameters = new CreativeModeTab.ItemDisplayParameters(FeatureFlags.REGISTRY.allFlags(), true, CraftRegistry.getMinecraftRegistry()); // TODO: use correct flags
        ItemDisplayBuilder itemDisplayBuilder = new ItemDisplayBuilder(tab, parameters.enabledFeatures());

        tab.displayItemsGenerator.accept(parameters, itemDisplayBuilder);

        return itemDisplayBuilder;
    }

    @Override
    public @NotNull Iterator<org.bukkit.inventory.ItemStack> iterator() {
        return this.getContents().iterator();
    }

    @Override
    public @NotNull String translationKey() {
        return ((TranslatableContents) this.handle.getDisplayName().getContents()).getKey();
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public Component displayName() {
        return PaperAdventure.asAdventure(this.handle.getDisplayName());
    }

    @Override
    public org.bukkit.inventory.ItemStack iconItem() {
        return CraftItemStack.asBukkitCopy(this.handle.getIconItem());
    }

    @Override
    public boolean scrollbarShown() {
        return this.handle.canScroll();
    }

    @Override
    public boolean containsItemType(final ItemType itemType) {
        Preconditions.checkArgument(itemType != null, "itemType may not be null.");
        return this.itemTypes.contains(itemType);
    }

    @Override
    public boolean containsItem(final org.bukkit.inventory.ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "itemStack may not be null.");

        final ItemStack stack = CraftItemStack.unwrap(itemStack);
        return this.itemStacks.contains(stack);
    }

    @Override
    public @Unmodifiable Collection<org.bukkit.inventory.ItemStack> getContents() {
        final ImmutableList.Builder<org.bukkit.inventory.ItemStack> list = ImmutableList.builder();

        for (final ItemStack itemStack : this.itemStacks) {
            list.add(CraftItemStack.asBukkitCopy(itemStack));
        }

        return list.build();
    }

    @Override
    public Row getRow() {
        return this.handle.row() == CreativeModeTab.Row.TOP ? Row.TOP : Row.BOTTOM;
    }

    @Override
    public Type getType() {
        return this.type;
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

    public static io.papermc.paper.inventory.CreativeModeTab minecraftToBukkit(CreativeModeTab minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.CREATIVE_MODE_TAB);
    }

    public static CreativeModeTab bukkitToMinecraft(io.papermc.paper.inventory.CreativeModeTab bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    @Override
    public CreativeModeTab getHandle() {
        return this.handle;
    }
}
