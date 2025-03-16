package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Suppliers;
import io.papermc.paper.registry.HolderableBase;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.inventory.util.CraftMenus;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftMenuType<V extends @NonNull InventoryView, B extends InventoryViewBuilder<V>> extends HolderableBase<net.minecraft.world.inventory.MenuType<?>> implements MenuType.Typed<V, B>, io.papermc.paper.world.flag.PaperFeatureDependent<net.minecraft.world.inventory.MenuType<?>> { // Paper - make FeatureDependant

    private final Supplier<CraftMenus.MenuTypeData<V, B>> typeData;

    public CraftMenuType(final Holder<net.minecraft.world.inventory.MenuType<?>> holder) {
        super(holder);
        this.typeData = Suppliers.memoize(() -> CraftMenus.getMenuTypeData(this));
    }

    @Override
    public V create(final HumanEntity player, final @Nullable String title) {
        return this.builder().title(title != null ? LegacyComponentSerializer.legacySection().deserialize(title) : null).build(player);
    }
    @Override
    public V create(final HumanEntity player, final @Nullable Component title) {
        return this.builder().title(title).build(player);
    }

    @Override
    public B builder() {
        return this.typeData.get().viewBuilder().get();
    }

    @Override
    public Typed<InventoryView, InventoryViewBuilder<InventoryView>> typed() {
        return this.typed(InventoryView.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <NV extends InventoryView, NB extends InventoryViewBuilder<NV>> Typed<NV, NB> typed(Class<NV> clazz) {
        if (clazz.isAssignableFrom(this.typeData.get().viewClass())) {
            return (Typed<NV, NB>) this;
        }

        throw new IllegalArgumentException("Cannot type InventoryView " + this + " to InventoryView type " + clazz.getSimpleName());
    }

    @Override
    public Class<? extends InventoryView> getInventoryViewClass() {
        return this.typeData.get().viewClass();
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
