package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.util.CraftMenus;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jetbrains.annotations.NotNull;

public class CraftMenuType<V extends InventoryView, B extends InventoryViewBuilder<V>> implements MenuType.Typed<V, B>, Handleable<net.minecraft.world.inventory.MenuType<?>>, io.papermc.paper.world.flag.PaperFeatureDependent { // Paper - make FeatureDependant

    private final NamespacedKey key;
    private final net.minecraft.world.inventory.MenuType<?> handle;
    private final Supplier<CraftMenus.MenuTypeData<V, B>> typeData;

    public CraftMenuType(NamespacedKey key, net.minecraft.world.inventory.MenuType<?> handle) {
        this.key = key;
        this.handle = handle;
        this.typeData = Suppliers.memoize(() -> CraftMenus.getMenuTypeData(this));
    }

    @Override
    public net.minecraft.world.inventory.MenuType<?> getHandle() {
        return this.handle;
    }

    @Override
    public V create(final HumanEntity player, final String title) {
    // Paper start - adventure
        return builder().title(title != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(title) : null).build(player);
    }
    @Override
    public V create(final HumanEntity player, final net.kyori.adventure.text.Component title) {
    // Paper end - adventure
        return builder().title(title).build(player);
    }

    @Override
    public B builder() {
        return typeData.get().viewBuilder().get();
    }

    @Override
    public Typed<InventoryView, InventoryViewBuilder<InventoryView>> typed() {
        return this.typed(InventoryView.class);
    }

    @Override
    public <V extends InventoryView, B extends InventoryViewBuilder<V>> Typed<V, B> typed(Class<V> clazz) {
        if (clazz.isAssignableFrom(this.typeData.get().viewClass())) {
            return (Typed<V, B>) this;
        }

        throw new IllegalArgumentException("Cannot type InventoryView " + this.key.toString() + " to InventoryView type " + clazz.getSimpleName());
    }

    @Override
    public Class<? extends InventoryView> getInventoryViewClass() {
        return this.typeData.get().viewClass();
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    public static net.minecraft.world.inventory.MenuType<?> bukkitToMinecraft(MenuType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static MenuType minecraftToBukkit(net.minecraft.world.inventory.MenuType<?> minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.MENU);
    }

    public static MenuType minecraftHolderToBukkit(Holder<net.minecraft.world.inventory.MenuType<?>> minecraft) {
        return CraftMenuType.minecraftToBukkit(minecraft.value());
    }
}
