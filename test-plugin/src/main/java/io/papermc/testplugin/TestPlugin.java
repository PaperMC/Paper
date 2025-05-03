package io.papermc.testplugin;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.HopperMenu;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.builder.InventorySupport;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    private Inventory container;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
        container = new CraftInventory(new SimpleContainer(18));
    }

    @EventHandler
    public void event(PlayerJumpEvent event) {
        final var builder = MenuType.GENERIC_9X2.builder();
        if (builder instanceof InventorySupport<?> cast) {
            cast.inventory(this.container)
                    .build(event.getPlayer())
                    .open();
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        final CraftInventoryView view = (CraftInventoryView) event.getView();
        final var handle = view.getHandle();
        if (handle instanceof HopperMenu menu) {
            System.out.println(menu.hopper == ((CraftInventory) this.container).getInventory());
        }
        System.out.println(event.getView().getItem(0));
    }

    @EventHandler
    public void bdie(BlockDropItemEvent event) {
        final var iter = event.getItems().iterator();
        while (iter.hasNext()) {
            final var next = iter.next();
            container.addItem(next.getItemStack());
            iter.remove();
        }
    }
}
