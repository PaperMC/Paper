package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.util.CraftMenus;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;

public class CraftMenuType<V extends InventoryView> implements MenuType.Typed<V>, Handleable<net.minecraft.world.inventory.MenuType<?>> {

    private final NamespacedKey key;
    private final net.minecraft.world.inventory.MenuType<?> handle;
    private final Supplier<CraftMenus.MenuTypeData<V>> typeData;

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
        Preconditions.checkArgument(player != null, "The given player must not be null");
        Preconditions.checkArgument(title != null, "The given title must not be null");
        Preconditions.checkArgument(player instanceof CraftHumanEntity, "The given player must be a CraftHumanEntity");
        final CraftHumanEntity craftHuman = (CraftHumanEntity) player;
        Preconditions.checkArgument(craftHuman.getHandle() instanceof ServerPlayer, "The given player must be an EntityPlayer");
        final ServerPlayer serverPlayer = (ServerPlayer) craftHuman.getHandle();

        final AbstractContainerMenu container = this.typeData.get().menuBuilder().build(serverPlayer, this.handle);
        container.setTitle(CraftChatMessage.fromString(title)[0]);
        container.checkReachable = false;
        return (V) container.getBukkitView();
    }

    @Override
    public Typed<InventoryView> typed() {
        return this.typed(InventoryView.class);
    }

    @Override
    public <V extends InventoryView> Typed<V> typed(Class<V> clazz) {
        if (clazz.isAssignableFrom(this.typeData.get().viewClass())) {
            return (Typed<V>) this;
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
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.MENU, Registry.MENU);
    }

    public static MenuType minecraftHolderToBukkit(Holder<net.minecraft.world.inventory.MenuType<?>> minecraft) {
        return CraftMenuType.minecraftToBukkit(minecraft.value());
    }
}
